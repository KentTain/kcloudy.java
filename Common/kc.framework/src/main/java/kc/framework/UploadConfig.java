package kc.framework;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadConfig implements java.io.Serializable{

	private static final long serialVersionUID = -6083395393791826550L;

    private int imageMaxSize;

    private int fileMaxSize;

    private String imageExt;

    private String fileExt;
}
