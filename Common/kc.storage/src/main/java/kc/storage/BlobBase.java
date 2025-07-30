package kc.storage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import kc.framework.TimeSpan;
import kc.framework.base.BlobInfo;
import kc.framework.extension.DateExtensions;
import kc.framework.extension.StringExtensions;
import kc.storage.util.Encryption;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.common.primitives.Ints;
import org.xml.sax.SAXException;


public abstract class BlobBase {
	protected static String GetActualBlobId(String blobId)
    {
        int offset = -1;
        int length = -1;
        return GetActualBlobIdAndOffset(blobId, offset, length).getBlobId();
    }

    protected static BlobResult GetActualBlobIdAndOffset(String blobId, int offset, int length)
    {
        if (!blobId.contains("_"))
        {
            return new BlobResult(blobId, offset, length);
        }

        try
        {
        	Integer oset = offset;
        	Integer len = length;
            String[] splitStr = blobId.split("_");
            if (blobId.startsWith("t") && splitStr.length == 5)
            {
                blobId = splitStr[0];
                oset = Ints.tryParse(splitStr[3]);
                len = Ints.tryParse(splitStr[4]);
            }
            else if (splitStr.length == 3 || splitStr.length == 5)
            {
                blobId = splitStr[0];
                oset = Ints.tryParse(splitStr[1]);
                len = Ints.tryParse(splitStr[2]);
            }
            
            return new BlobResult(blobId, oset, len);
        }
        catch (Exception ex)
        {
        	return new BlobResult(blobId, offset, length);
        }
    }

    protected static BlobInfo GetBlobSegment(BlobInfo bi, int offset, int length, String segBlobId)
    {
        if (bi == null) return null;

        bi.setId(segBlobId);
        bi.setSize(length);
        byte[] chunkData = new byte[length];
        System.arraycopy(bi.getData(), offset, chunkData, 0, length);
        bi.setData(chunkData);
        return bi;
    }

    protected static String GetModifiedTime()
    {
        return DateExtensions.getUTCNowDateString(DateExtensions.FMT_yMdHms1);
    }


    protected static void CheckBlobAccess(String containerName, boolean isInternal, String userId, String blobId, Map<String, String> blobMetadata) throws Exception
    {
        if (isInternal) return;

        // Tenant check
        if (blobMetadata.containsKey("Tenant"))
        {
            if (!containerName.equals(blobMetadata.get("Tenant")))
            {
                throw new Exception("Blob Access Violation: " + blobId + " belongs to another tenant.");
            }
        }

        boolean isUserLevel = blobMetadata.containsKey("IsUserLevelBlob") && Boolean.parseBoolean(blobMetadata.get("IsUserLevelBlob"));
        if (isUserLevel && blobMetadata.containsKey("UserId"))
        {
            String checkId = isInternal ? "Internal" : userId;
            if (!checkId.equals(blobMetadata.get("UserId")))
            {
                throw new Exception("Blob Access Violation: " + blobId + " belongs to another user.");
            }
        }

        // Expiration Check
        if (blobMetadata.containsKey("Expiration"))
        {
            long expiration = Long.parseLong(blobMetadata.get("Expiration"));
            if (expiration > 0)
            {
                long createTicks = blobMetadata.containsKey("CreateTime") ? Long.parseLong(blobMetadata.get("CreateTime")) : 0;
                long elapsedTicks = DateExtensions.getUtcNowTicks() - createTicks;
                TimeSpan elapsedSpan = new TimeSpan(elapsedTicks);
                if (elapsedSpan.TotalSeconds() > expiration)
                {
                    throw new Exception("Blob Access Violation: " + blobId + " has already expired.");
                }
            }
        }
    }

    protected static byte[] DecryptIfNeeded(String encryptionKey, boolean isInternal, String userId, byte[] data, Map<String, String> metadata)
    {
        boolean isEncrypted = metadata.containsKey("IsEncrypted") && Boolean.parseBoolean(metadata.get("IsEncrypted"));
        if (isEncrypted)
        {
            boolean isUserLevelBlob = metadata.containsKey("IsUserLevelBlob") && Boolean.parseBoolean(metadata.get("IsUserLevelBlob"));
            String realEncryptionKey = Encryption.GetEncryptionKey(encryptionKey, isUserLevelBlob, isInternal, userId);
            return Encryption.Decrypt(data, realEncryptionKey);
        }
        else
        {
            return data;
        }
    }

    public static Map<String, String> BuildMetadata(String containerName, boolean isInternal, String userId, String blobId, String type, String format, String fileName, long size, long expireAfterSecond, boolean isEncypted, boolean isUserLevelBlob, Map<String, String> extraMetadata)
    {
        Map<String, String> metadata = new HashMap<String, String>();
        metadata.put("Tenant", containerName);
        metadata.put("UserId", isInternal ? "Internal" : userId);
        metadata.put("Id", blobId);
        metadata.put("Type", StringExtensions.isNullOrEmpty(type) ? "Unknown" : type);
        metadata.put("Format", StringExtensions.isNullOrEmpty(format) ? "Unknown" : format);
        metadata.put("FileName", fileName);
        metadata.put("Size", size + "");
        metadata.put("CreateTime", Long.toString(DateExtensions.getUtcNowTicks()));
        metadata.put("LastModifiedTime", GetModifiedTime());
        metadata.put("Expiration", Long.toString(expireAfterSecond));
        metadata.put("IsEncrypted", isEncypted ? "True" : "False");
        metadata.put("IsUserLevelBlob", isUserLevelBlob ? "True" : "False");
        if (extraMetadata != null)
        {
            for (Map.Entry<String,String> metadataItem : extraMetadata.entrySet())
            {
                metadata.put(metadataItem.getKey(), metadataItem.getValue());
            }
        }
        return metadata;
    }

    protected static String MetadataToXml(Map<String, String> metadata)
    {
        StringBuilder metadataXmlSb = new StringBuilder();
        metadataXmlSb.append("<Metadata>");
        for (Map.Entry<String,String> metadataEntry : metadata.entrySet())
        {
            metadataXmlSb.append(String.format("<%s>%s</%s>", metadataEntry.getKey(), metadataEntry.getValue(), metadataEntry.getKey()));
        }
        metadataXmlSb.append("</Metadata>");
        return metadataXmlSb.toString();
    }

    protected static Map<String, String> ParseMetadataXml(String metadataXmlString)
    {
        Map<String, String> metadata = new HashMap<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();//得到创建 DOM 解析器的工厂。
            DocumentBuilder builder = factory.newDocumentBuilder();//得到 DOM 解析器对象。
            Document metadataXDoc = builder.parse(new ByteArrayInputStream(metadataXmlString.getBytes("utf-8"))); //得到代表整个文档的 Document 对象
            Element root = metadataXDoc.getDocumentElement();
            if (root != null)
            {
                NodeList nodes = root.getChildNodes();
                for (int i = 0; i < nodes.getLength(); i++) {
                    Node node = nodes.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        //Element child = (Element) node;
                        metadata.put(node.getNodeName(), node.getTextContent());
                    }
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return metadata;
    }
}
