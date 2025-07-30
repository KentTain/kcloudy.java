package kc.framework.enums;

import java.util.List;

public enum DocFormat implements EnumBase {
	// 文档格式
	Doc("Doc", 1), Docx("Docx", 2),

	Xls("Xls", 3), Xlsx("Xlsx", 4),

	Ppt("Ppt", 5), Pptx("Pptx", 6),

	Pdf("Pdf", 7),
	// 图片格式
	Dwg("Dwg", 9),Bmp("Bmp", 10), Gif("Gif", 11), Icon("Icon", 12), Jpeg("Jpeg", 13), Png("Png", 14), Tiff("Tiff", 15),
	Wmf("Wmf", 16),
	// 图片格式
	Text("Text", 17), Xml("Xml", 18),
	// 音频格式
	Basic("Basic", 19), Wav("Wav", 20), Mpeg("Mpeg", 21), // mp3
	Ram("Ram", 22), Rmi("Rmi", 23), Aif("Aif", 24),
	// 视频格式30
	Wmv("Wmv", 30), Mp4("Mp4", 31),Flv("flv", 32),Avi("avi", 33),Mov("mov", 34),
	// 压缩格式
	Rar("Rar", 40), Zip("Xml", 41),Gzip("Gzip", 42),


	Unknown("未知", 99);
	// 定义一个 private 修饰的实例变量
	private String desc;
	private Integer index;

	// 定义一个带参数的构造器，枚举类的构造器只能使用 private 修饰
	private DocFormat(String desc, Integer index) {
		this.desc = desc;
		this.index = index;
	}

	/**
	 * 根据索引值：int的值获取Enum对象
	 * 
	 * @param index 索引值
	 * @return ConfigType Enum对象
	 */
	@com.fasterxml.jackson.annotation.JsonCreator
	public static DocFormat valueOf(Integer index) {
		for (DocFormat type : values()) {
			if (type.getIndex() == index) {
				return type;
			}
		}
		return null;
	}

	
	/**
	 * 获取Enum所有类型的Map<索引值, Enum类型的描述>
	 * 
	 * @param index 需要排除的Enum类型列表
	 * @return Map<Integer, String>
	 */
	public static String getDesc(Integer index) {
		for (DocFormat c : values()) {
			if (c.getIndex() == index) {
				return c.getDesc();
			}
		}
		return null;
	}
	
	/**
	 * 获取Enum所有类型的Map<索引值, Enum类型的描述>
	 * 
	 * @param exceptEnums 需要排除的Enum类型列表
	 * @return Map<Integer, String>
	 */
	public static java.util.Map<Integer, String> getList(List<DocFormat> exceptEnums) {
		java.util.Map<Integer, String> result = new java.util.HashMap<Integer, String>();
		for (DocFormat c : values()) {
			if (exceptEnums != null && exceptEnums.contains(c))
				continue;
			result.put(c.getIndex(), c.getDesc());
		}

		return result;
	}

	// 定义 get set 方法
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	@com.fasterxml.jackson.annotation.JsonValue
	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}
}
