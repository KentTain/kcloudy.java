package kc.thirdparty;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.parallel.Execution;
import org.springframework.test.context.junit.jupiter.*;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipOutputStream;

@Disabled
@DisplayName("Zip压缩测试")
@lombok.extern.slf4j.Slf4j
class ZipUtilTest {
    private final String imagePath = "src/test/resources/Image/";

    @Test
    void testToZip() throws FileNotFoundException {
        /** 测试压缩方法1 */
        FileOutputStream fos1 = new FileOutputStream(new File(imagePath + "myZip-1.zip"));
        ZipUtil.toZip(new File(imagePath + "1.jpg"), fos1, true);
        /** 测试压缩方法2 */
        List<File> fileList = new ArrayList<>();
        fileList.add(new File(imagePath + "1.jpg"));
        fileList.add(new File(imagePath + "2.png"));
        FileOutputStream fos2 = new FileOutputStream(new File(imagePath + "myZip-2.zip"));
        ZipUtil.toZip(fileList, fos2);
    }


    @Test
    void testDownloadToZip() throws FileNotFoundException {
        try {
            Hashtable<String, byte[]> files = new Hashtable<String, byte[]>();

            Hashtable<String, String> fileUrls = new Hashtable<String, String>();
            fileUrls.put("logo.png", "http://sso.kcloudy.com/images/logo.png");
            fileUrls.put("jquery.js", "http://resource.kcloudy.com/lib/jquery/dist/jquery.js");

            Iterator iter = fileUrls.keySet().iterator();
            while (iter.hasNext()) {
                // 获取key
                String fileName = (String) iter.next();
                // 根据key，获取value
                String downloadUrl = fileUrls.get(fileName);
                URL url = new URL(downloadUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                // 设置超时间为3秒
                conn.setConnectTimeout(3 * 1000);
                // 防止屏蔽程序抓取而返回403错误
                conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                // 得到输入流
                InputStream inputStream = conn.getInputStream();
                // 获取自己数组
                byte[] bs = readInputStream(inputStream);
                files.put(fileName, bs);
            }

            FileOutputStream out = new FileOutputStream(new File(imagePath + "myZip-3.zip"));

            // 对文件夹进行压缩
            long zipStart = System.currentTimeMillis();
            ZipUtil.toZip(files, out);
            long zipEnd = System.currentTimeMillis();
            System.out.println("压缩完成，耗时：" + (zipEnd - zipStart) + " ms");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     *  从输入流中获取字节数组   
     *
     * @param inputStream   
     * @return  
     * @throws IOException   
     */
    private static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }
}
