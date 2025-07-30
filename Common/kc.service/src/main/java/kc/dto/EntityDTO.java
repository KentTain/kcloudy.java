package kc.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.MappedSuperclass;

//import org.springframework.format.annotation.DateTimeFormat;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@MappedSuperclass
public abstract class EntityDTO extends EntityBaseDTO implements Serializable {

	@com.fasterxml.jackson.annotation.JsonProperty("isDeleted")
	private boolean isDeleted = false;

	private String createdBy;

	private String createdName;

	@DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS")
    //@com.fasterxml.jackson.annotation.JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@com.fasterxml.jackson.annotation.JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS")
	private Date createdDate = new Date();

	private String modifiedBy;

	private String modifiedName;

	@DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS")
    //@com.fasterxml.jackson.annotation.JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@com.fasterxml.jackson.annotation.JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS")
	private Date modifiedDate = new Date();
}
