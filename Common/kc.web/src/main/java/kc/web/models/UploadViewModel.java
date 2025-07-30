package kc.web.models;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadViewModel implements java.io.Serializable{
    private Boolean success;

    private String message;

    private String state;

    private String url;

    private List<String> list;

    private String uId;

    private String id;

    private List<String> ids;

    private String title;

    private String original;

    private String error;

    private int start;
    private int size;
    private int total;
}
