package kc.service.util;

import org.eclipse.jgit.transport.CredentialsProvider;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Disabled
@DisplayName("Gitlab测试")
class JGitUtilTest {
    private String local_test_git_path = Gitlab4jUtilTest.local_test_git_path;

    /**
     * http://gitlab.kcloudy.com/tenant/tenant-group/tenant-project.git
     */
    private String project_test_git_url = Gitlab4jUtilTest.gitLabUrl + "/" +
            Gitlab4jUtilTest.tenant_group_name + "/" +
            Gitlab4jUtilTest.test_group_name + "/" +
            Gitlab4jUtilTest.test_project_name + ".git";

    @Test
    @DisplayName("JGit-Jar内文件拷贝")
    void copyFromJar() {
        JGitUtil.copyFromJar(false, "/gitlab/", local_test_git_path);
    }

    @Test
    @DisplayName("JGit-clone、pull、commit")
    void cloneRepository() {
        File gitFile = new File(local_test_git_path + File.separator);
        if (gitFile.exists()) {
            boolean success = JGitUtil.deleteDirIfExists(gitFile);
            System.out.println("----delete the git folder is success? " + success);
        } else {
            boolean success = gitFile.mkdir();
            System.out.println("----create the git folder is success? " + success);
        }

        CredentialsProvider credentialsProvider = JGitUtil.getGitCredentials(Gitlab4jUtilTest.admin_token);
        // 1、下载主仓
        JGitUtil.cloneRepository(credentialsProvider, project_test_git_url, local_test_git_path, Gitlab4jUtilTest.branch_main);

        // 2、pull（主仓+子仓（部分））
        JGitUtil.pullRepository(credentialsProvider, local_test_git_path, Gitlab4jUtilTest.branch_main);

        // 3、代码提交（主仓+子仓（部分））
        JGitUtil.copyFromJar(false, "gitlab", local_test_git_path);
        appendFile(local_test_git_path + File.separator + "README.md", "\r\ntest-" + UUID.randomUUID().toString());
        JGitUtil.commitAllRepository(local_test_git_path, "测试文件夹提交", Gitlab4jUtilTest.test_user_name);
//        JGitUtil.log(local_test_git_path);
//        JGitUtil.commitSubRepository(local_test_git_path, "\\zicang/新建文件夹", "123/123.txt","测试文件夹提交");

        // 4、push（主仓+子仓（部分））
        JGitUtil.pushRepository(credentialsProvider, local_test_git_path);
        //pushSubRepository(local_test_git_path,"\\zicang/新建文件夹");

        // 5、切换分支（子仓切）
        String branch_test = "test";
        String newBranchUrl = JGitUtil.checkoutAndPushBranch(credentialsProvider, project_test_git_url, local_test_git_path, branch_test);
        assertNotNull(newBranchUrl);
        System.out.println(newBranchUrl);

        // 单步骤测试2
        /*checkoutAndPushBranch(local_test_git_path, credentialsProvider, "branchfortest02", project_test_git_url);
        checkoutBranch(local_test_git_path, "branchfortest02");

        cn.flydiy.common.util.FileUtil.forceDeleteQuietly(local_test_git_path + "/default-server-gbqcloundA-client");
        cn.flydiy.common.util.FileUtil.forceDeleteQuietly(local_test_git_path + "/111.txt");

        commitAndPush(local_test_git_path, credentialsProvider);*/

        // 模拟页面操作
        /*String local_branch = "F:\\git\\flydiysz\\idea\\flycloud-generate\\src\\main\\webapp\\default-server-gbqcloundtesta_branchForTest\\gbq";
        GitCommon.newBranchAndSwitchBranch(local_branch, "default-server-gbqcloundtesta", "branchForTest");

        cn.flydiy.common.util.FileUtil.forceDeleteQuietly(local_branch + "/default-server-gbqcloundtesta-client");
        cn.flydiy.common.util.FileUtil.forceDeleteQuietly(local_branch + "/README.md");

        GitCommon.commitAndPushProject(local_branch, "123456789");*/
    }


    private static void appendFile(String path, String content) {
        try {
            File f = new File(path);
            if (!f.exists()) {
                f.createNewFile();
            }
            PrintWriter pw = new PrintWriter(new FileOutputStream(f, true));
            pw.print(content);
            pw.flush();
            pw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
