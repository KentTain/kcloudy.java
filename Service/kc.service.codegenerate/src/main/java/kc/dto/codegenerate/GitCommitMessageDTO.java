package kc.dto.codegenerate;

import lombok.Data;

import java.util.Date;

@Data
public class GitCommitMessageDTO {
    private String commitId;

    private String commitIdent;

    private String commitMessage;

    private String commitDate;

    private String lastCommitId;

    private String mergeBranchCommitId;
}
