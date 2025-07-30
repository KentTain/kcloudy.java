package kc.dto;

import kc.framework.GlobalConfig;
import kc.framework.util.MimeTypeHelper;
import kc.framework.extension.StringExtensions;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.*;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
@MappedSuperclass
public class BlobInfoDTO implements java.io.Serializable, Cloneable {
    /**
     * 对应的PropertyId
     */
    private Integer propertyId;

    /**
     * 文件Id
     */
    @Length(min = 0, max = 128, message = "文件Id不能超过128个字符")
    @NotBlank(message = "文件Id不能为空")
    private String blobId;

    /**
     * 文件名称
     */
    @Length(min = 0, max = 256, message = "文件Id不能超过256个字符")
    @NotBlank(message = "文件名称不能为空")
    private String blobName;

    /**
     * 文件扩展名
     */
    @Length(min = 0, max = 50, message = "文件Id不能超过50个字符")
    private String ext;

    /**
     * 文件类型：Com.Common.FileHelper.FileType
     * Image=1、Word=2、Excel=3、PDF=4、PPT=5、Text=6、Xml=7、Audio=8、Unknown
     */
    private String fileType;
    public String getFileType() {
        if (StringExtensions.isNullOrEmpty(ext))
            return null;

        return MimeTypeHelper.GetFileType(ext).toString();
    }

    /**
     * 文件格式：
     * Com.Common.FileHelper.ImageFormat <br/>
     * ( Bmp = 1, Gif = 2, Icon = 3, Jpeg = 4, Png = 5, Tiff = 6, Wmf = 7, Unknown = 99) <br/>
     * <p>
     * Com.Common.FileHelper.DocFormat <br/>
     * (Doc = 1, Docx = 2, Xls = 3, Xlsx = 4, Ppt = 5, Pptx = 6, Pdf = 7, Unknown = 99) <br/>
     * Com.Common.FileHelper.AudioFormat <br/>
     * (Basic = 1, Wav = 2, Mpeg = 3, Ram = 4, Rmi = 5, Aif = 6, Unknown = 99) <br/>
     */
    @Length(min = 0, max = 50, message = "文件Id不能超过50个字符")
    private String fileFormat;
    public String getFileFormat() {
        if (StringExtensions.isNullOrEmpty(ext))
            return null;

        return MimeTypeHelper.GetDocFormatByExt(ext).toString();
    }

    /**
     * 文件大小
     */
    private long size;

    @com.fasterxml.jackson.annotation.JsonProperty("isSelect")
    private Boolean isSelect;

    /**
     * 文件大小
     */
    private String tenantName;

    public String showImageUrl;
    public String getShowImageUrl() {
        if (StringExtensions.isNullOrEmpty(tenantName))
            return "";

        var docApiDomain = GlobalConfig.GetTenantWebApiDomain(GlobalConfig.DocWebDomain, tenantName);
        return docApiDomain + "Resources/ShowImage?id=" + blobId;
    }

    public String downloadFileUrl;
    public String getDownloadFileUrl() {
        if (StringExtensions.isNullOrEmpty(tenantName))
            return "";

        var docApiDomain = GlobalConfig.GetTenantWebApiDomain(GlobalConfig.DocWebDomain, tenantName);
        return docApiDomain + "Resources/DownloadFile?id=" + blobId;
    }

    public BlobInfoDTO Clone() throws CloneNotSupportedException {
        return (BlobInfoDTO) super.clone();
    }
}
