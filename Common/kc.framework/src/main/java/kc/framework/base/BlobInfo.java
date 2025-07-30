package kc.framework.base;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BlobInfo implements java.io.Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5354944860328669348L;

	/**
	 * 文件Id
	 */
	private String id;

	/**
	 * 文件类型：Com.Common.FileHelper.FileType
	 * Image=1、Word=2、Excel=3、PDF=4、PPT=5、Text=6、Xml=7、Audio=8、Unknown
	 */
	private String fileType;

	/**
	 * 文件格式： 
	 * Com.Common.FileHelper.ImageFormat <br/>
	 * ( Bmp = 1, Gif = 2, Icon = 3, Jpeg = 4, Png = 5, Tiff = 6, Wmf = 7, Unknown = 99) <br/>
	 * 
	 * Com.Common.FileHelper.DocFormat <br/>
	 * (Doc = 1, Docx = 2, Xls = 3, Xlsx = 4, Ppt = 5, Pptx = 6, Pdf = 7, Unknown = 99) <br/>
	 * Com.Common.FileHelper.AudioFormat <br/>
	 * (Basic = 1, Wav = 2, Mpeg = 3, Ram = 4, Rmi = 5, Aif = 6, Unknown = 99) <br/>
	 */
	private String fileFormat;
	/**
	 * 文件名称
	 */
	private String fileName;
	/**
	 * 文件流
	 */
	private byte[] data;
	/**
	 * 文件大小
	 */
	private long size;

	/**
	 * 水印
	 */
	private Boolean placeholder;

	/**
	 * 本地Id
	 */
	private String localId;

	public BlobInfo(String id, String type, String format, String fileName) {
		this.id = id;
		this.fileType = type;
		this.fileFormat = format;
		this.fileName = fileName;
	}

	public BlobInfo(String id, String type, String format, String fileName, byte[] data) {
		this.id = id;
		this.fileType = type;
		this.fileFormat = format;
		this.data = data;
		this.size = data == null ? 0 : data.length;
		this.fileName = fileName;
	}

	public BlobInfo Clone() throws CloneNotSupportedException {
		return (BlobInfo) super.clone();
	}

}
