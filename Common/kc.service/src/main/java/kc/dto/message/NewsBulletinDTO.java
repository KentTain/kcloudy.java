package kc.dto.message;

import kc.dto.BlobInfoDTO;
import kc.dto.EntityDTO;
import kc.dto.workflow.WorkflowStartExecuteData;
import kc.enums.MessageStatus;
import kc.enums.NewsBulletinType;
import kc.framework.enums.WorkflowBusStatus;
import kc.framework.extension.StringExtensions;
import kc.framework.util.SerializeHelper;
import lombok.*;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class NewsBulletinDTO extends EntityDTO  implements Serializable {


	@com.fasterxml.jackson.annotation.JsonProperty("isEditMode")
	private boolean isEditMode;

	private int id;

	/**
	 * 文章类型
	 */
	private NewsBulletinType type;
	private String typeString;
	public String getTypeString() { return type.getDesc(); }

	/**
	 * 文章标题
	 */
	private String title;

	/**
	 * 作者
	 */
	private String author;

	/**
	 * 作者邮箱
	 */
	private String authorEmail;

	/**
	 * 关键字
	 */
	private String keywords;


	/**
	 * 图片
	 */
	private String imageBlob;

	/**
	 * 图片对象
	 */
	private BlobInfoDTO image;
	public BlobInfoDTO getImage()
	{
		if (StringExtensions.isNullOrEmpty(imageBlob))
			return null;

		return SerializeHelper.FromJson(imageBlob, BlobInfoDTO.class);
	}

	/**
	 * 附件
	 */
	private String fileBlob;

	/**
	 * 附件对象
	 */
	private BlobInfoDTO file;
	public BlobInfoDTO getFile()
	{
		if (StringExtensions.isNullOrEmpty(fileBlob))
			return null;

		return SerializeHelper.FromJson(fileBlob, BlobInfoDTO.class);
	}

	/**
	 * 描述
	 */
	private String description;

	/**
	 * 外链
	 */
	private String link;

	/**
	 * 文章内容(显示时，填充html description)
	 */
	private String content;

	/**
	 * 是否显示
	 */
	@com.fasterxml.jackson.annotation.JsonProperty("isShow")
	private boolean isShow;

	/**
	 * 发布状态
	 */
	private WorkflowBusStatus status;
	private String statusString;
	public String getStatusString() { return status.getDesc(); }

	private Integer categoryId;

	private String categoryName;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof NewsBulletinDTO))
			return false;
		if (!super.equals(o))
			return false;

		NewsBulletinDTO entity = (NewsBulletinDTO) o;

		if (!Objects.equals(id, entity.id))
			return false;
		if (!Objects.equals(type, entity.type))
			return false;
		if (!Objects.equals(title, entity.title))
			return false;
		if (!Objects.equals(author, entity.author))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + id;
		result = 31 * result + (type != null ? type.hashCode() : 0 );
		result = 31 * result + (title != null ? title.hashCode() : 0 );
		result = 31 * result + (author != null ? author.hashCode() : 0 );
		return result;
	}
}
