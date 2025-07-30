package kc.service.util;

import kc.dto.codegenerate.GitFileTreeDTO;
import lombok.*;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.TreeItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Gitlab4jUtil {
    private static final Logger logger = LoggerFactory.getLogger(Gitlab4jUtil.class);

    private static final String default_Branch = "main";

    /**
     * 根据Gitlab设置的token值，获取gitlab API对象
     *
     * @param gitHostUrl gitlab的host地址
     * @param token      gitlab设置的token值
     * @return gitlab API对象
     */
    public static GitLabApi getGitlabApi(String gitHostUrl, String token) {
        //System.out.println(String.format("==============set the git api client with gitHost url: %s  token: %s", gitHostUrl, token));
        // 传入gitlab服务地址和token，初始化服务
        GitLabApi result = new GitLabApi(gitHostUrl, token);
        result.setIgnoreCertificateErrors(true);
        return result;
    }

    /**
     * 根据Gitlab设置的token值，获取gitlab API对象
     *
     * @param gitHostUrl   gitlab的host地址
     * @param userName     gitlab用户账号
     * @param userPassword gitlab用户密码
     * @return gitlab API对象
     */
    public static GitLabApi getGitlabApi(String gitHostUrl, String userName, String userPassword) throws GitLabApiException {
        //System.out.println(String.format("==============set the git api client with gitHost url: %s  user: %s password: %s", gitHostUrl, userName, userPassword));
        // 传入gitlab服务地址和token，初始化服务
        return GitLabApi.oauth2Login(gitHostUrl, userName, userPassword, true);
    }

    /**
     * 读取gitlab项目的所有文件
     *
     * @param projectId Gitlab项目Id
     * @param directory Gitlab项目目录
     * @param branch    Gitlab项目分支名称
     * @throws GitLabApiException Gitlab api异常
     */
    public static void readAllFile(GitLabApi gitLabApi, Integer projectId, String directory, String branch) throws GitLabApiException {
        // 获取工程的所有文件
        final List<GitFileTreeDTO> allFile = getAllFiles(gitLabApi, projectId, null, 1, directory, branch);
        // 遍历获取所有文件，进行相关的处理
        for (GitFileTreeDTO file : allFile) {
            final InputStream inputStream;
            String filePath = file.getPath();
            inputStream = gitLabApi.getRepositoryFileApi().getRawFile(projectId, branch, filePath);
            final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            final StringBuilder stringBuilder = new StringBuilder();
            while (true) {
                try {
                    if (!reader.ready()) break;

                    final String lineContent = reader.readLine();
                    stringBuilder.append(lineContent);
                    // todo 进行相关的处理
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 读取工程仓库的所有文件列表
     *
     * @param projectId Gitlab项目Id
     * @param parentId 父节点Id，默认设置为的根Id：null
     * @param parentLevel  父节点的层次，默认设置为根层次：1
     * @param directory Gitlab项目目录，默认设置为根目录：/
     * @param branch    Gitlab项目分支名称
     * @return 文件列表
     * @throws GitLabApiException Gitlab api异常
     */
    public static List<GitFileTreeDTO> getAllFiles(GitLabApi gitLabApi, Integer projectId, String parentId, int parentLevel, String directory, String branch) throws GitLabApiException {
        List<GitFileTreeDTO> result = new ArrayList<>();
        // 如果当前是目录，则继续获取其下的文件列表
        int i = 1;
        for (TreeItem file : gitLabApi.getRepositoryApi().getTree(projectId, directory, branch)) {
            if (file.getType().equals(TreeItem.Type.TREE)) {
                parentLevel++;
                List<GitFileTreeDTO> children = getAllFiles(gitLabApi, projectId, file.getId(), parentLevel, file.getPath(), branch);
                parentLevel--;
                // 如果是文件类型，直接添加
                GitFileTreeDTO node = new GitFileTreeDTO();
                node.setId(file.getId());
                node.setIndex(i);
                node.setLeaf(false);
                node.setDirectory(true);
                node.setLevel(parentLevel);
                node.setParentId(parentId);
                node.setName(file.getName());
                node.setPath(directory.equalsIgnoreCase("/")
                        ? String.join("/", file.getName())
                        : String.join("/", directory, file.getName()));
                node.setChildren(children);

                result.add(node);
                i++;
                continue;
            }

            // 如果是文件类型，直接添加
            GitFileTreeDTO node = new GitFileTreeDTO();
            node.setId(file.getId());
            node.setIndex(i);
            node.setLeaf(true);
            node.setDirectory(false);
            node.setLevel(parentLevel );
            node.setParentId(parentId);
            node.setName(file.getName());
            node.setPath(directory.equalsIgnoreCase("/")
                    ? String.join("/", file.getName())
                    : String.join("/", directory, file.getName()));
            result.add(node);

            i++;
        }
        return result;
    }

    /**
     * Webhook 回调函数处理，根据自己的需求进行处理
     *
     * @param gitlabPushEvent
     */
    private static void pushEvent(GitlabPushEvent gitlabPushEvent) {
        logger.info("收到Gitlab Webhook请求");
        if (!gitlabPushEvent.getRef().equals(String.format("refs/heads/%s", default_Branch))) {
            logger.info("非设定分支的push事件，不进行更新");
            return;
        }

        for (GitlabPushEvent.Commit commit : gitlabPushEvent.getCommits()) {
            for (String filePath : commit.getAdded()) {
                // todo 处理新增的文件
            }
            for (String filePath : commit.getModified()) {
                // todo 处理修改的文件
            }
            for (String filePath : commit.getRemoved()) {
                // todo 处理删除的文件
            }
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class GitlabPushEvent implements java.io.Serializable {
        // 这个提交的分支信息
        private String ref;
        // 提交的详细信息
        private List<Commit> commits;

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public class Commit {
            // 新增的文件列表
            private List<String> added;
            // 修改的文件列表
            private List<String> modified;
            // 删除的文件列表
            private List<String> removed;
        }
    }

}
