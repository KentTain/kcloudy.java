package kc.service.util;

import kc.dto.codegenerate.GitCommitMessageDTO;
import kc.framework.exceptions.ComponentException;
import kc.framework.extension.StringExtensions;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.RenameDetector;
import org.eclipse.jgit.errors.NoRemoteRepositoryException;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.storage.file.WindowCacheConfig;
import org.eclipse.jgit.transport.*;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilterGroup;
import org.eclipse.jgit.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class JGitUtil {
    private static final Logger logger = LoggerFactory.getLogger(JGitUtil.class);

    private final static String GIT = ".git";
    private final static String DEFAULT_BRANCH = "main";
    private final static String LOCAL_BRANCH_REF_PREFIX = "refs/remotes/origin/";

    /**
     * 获取gitlab身份令牌
     *
     * @param token gitlab设置的个人token
     * @return gitlab身份令牌
     */
    public static CredentialsProvider getGitCredentials(String token) {
        return new UsernamePasswordCredentialsProvider("PRIVATE-TOKEN", token);
    }
    /**
     * 获取gitlab身份令牌
     *
     * @param username git用户账号
     * @param password git用户密码
     * @return gitlab身份令牌
     */
    public static CredentialsProvider getGitCredentials(String username, String password) {
        return new UsernamePasswordCredentialsProvider(username, password);
    }

    /**
     * 根据本地地址，获取git的客户端对象
     *
     * @param localPath 本地仓库目录
     * @return Git client
     */
    public static Git getGitClientByLocal(String localPath) {
        if (!StringUtils.hasLength(localPath))
            throw new IllegalArgumentException("localPath is null.");

        try {
            //初始化git仓库
            File rootDir = new File(localPath);
            if (!new File(localPath + File.separator + GIT).exists()) {
                Git.init()
                        .setDirectory(rootDir)
                        .setInitialBranch(DEFAULT_BRANCH)
                        .call();
            }

            //打开git仓库
            return Git.open(rootDir);
        } catch (IOException | GitAPIException e) {
            throw new ComponentException(e);
        }
    }
    /**
     * 打开本地仓库/初始化本地仓库
     *
     * @param credentialsProvider gitlab身份令牌
     * @param localPath           本地仓库目录
     * @param remoteRepoURI       远程git地址
     * @return  Git client
     */
    public static Git getGitClientByCredentials(CredentialsProvider credentialsProvider, String remoteRepoURI, String localPath) {
        if (!StringUtils.hasLength(localPath))
            throw new IllegalArgumentException("localPath is null.");

        //建立与远程仓库的联系，仅需要执行一次
        Git git;
        try {
            git = Git.open(new File(localPath));
        } catch (Exception e) {
            File localRepoFile = new File(localPath);
            //如果本地仓库文件目录不存在，就需要先在本地创建文件夹
            if (!localRepoFile.exists()) {
                logger.info("创建本地仓库文件目录开始：" + localPath);
                forceMkdir(localRepoFile);
                logger.info("创建本地仓库文件目录完成：" + localPath);
            }
            try {
//                git = Git.init()
//                        .setDirectory(new File(localPath))
//                        .call();
                git = Git.cloneRepository()
                        .setCredentialsProvider(credentialsProvider)
                        .setURI(remoteRepoURI)
                        .setDirectory(new File(localPath))
                        .call();
            } catch (GitAPIException ex) {
                throw new ComponentException("初始化远程仓库[" + remoteRepoURI + "]失败！\n" + ex.getMessage());
            }
        }

        return git;
    }

    /**
     * 打开本地Git仓资源库
     *
     * @param localPath 本地仓库目录
     * @return  Git Repository
     */
    public static Repository openJGitRepository(String localPath) {
        Git git = getGitClientByLocal(localPath);
        return git.getRepository();
    }
    private static Repository createNewRepository(String localPath) {
        // prepare a new folder
        try {
            File rootDir = File.createTempFile(localPath, "");
            if (!rootDir.delete()) {
                throw new ComponentException("Could not delete temporary file " + rootDir);
            }

            // create the directory
            Repository repository = FileRepositoryBuilder.create(new File(rootDir, ".git"));
            repository.create();
            return repository;
        } catch (IOException e) {
            throw new ComponentException(e);
        }
    }

    private static void configureCommand(TransportCommand<?, ?> command, CredentialsProvider credentialsProvider) {
        command.setTimeout(GitSettingDTO.timeout);
        if (credentialsProvider != null) {
            command.setCredentialsProvider(credentialsProvider);
        }
    }

    /**
     * 克隆分支仓库
     *
     * @param credentialsProvider gitlab身份令牌
     * @param remoteUrl           远程url
     * @param localPath           本地主仓路径
     * @param branchName          分支名称
     */
    public static void cloneRepository(CredentialsProvider credentialsProvider, String remoteUrl, String localPath, String branchName) {
        if (!StringUtils.hasText(branchName))
            branchName = DEFAULT_BRANCH;
        logger.info("cloneRepository from remoteUrl = [" + remoteUrl + "], localPath = [" + localPath + "], branchName = [" + branchName + "]");

        Git git = null;
        File basedir = new File(localPath);
        try {
            CloneCommand cloneCommand = Git.cloneRepository()
                    .setURI(remoteUrl)
                    .setBranch(branchName)
                    .setDirectory(basedir);
            configureCommand(cloneCommand, credentialsProvider);
            git = cloneCommand.call();
        } catch (Exception e) {
            deleteDirIfExists(basedir);
            throw new ComponentException(e);
        } finally {
            try {
                if (git != null) {
                    git.close();
                }
            } catch (Exception e) {
                logger.warn("Could not close git repository", e);
            }
        }
    }

    /**
     * 下载子仓
     *
     * @param localPath 主仓
     * @param sub       子仓
     * @return 子仓内容
     */
    private static String downloadSubRepository(String localPath, String sub) {
        logger.info("localPath = [" + localPath + "], sub = [" + sub + "]");
        try {
            File subGit = new File(localPath + "\\.git\\modules\\" + sub);
            if (subGit.exists()) {
                deleteDirIfExists(subGit);
            }
            Git git = getGitClientByLocal(localPath);
            logger.info("开始下载分支仓。。。");
            SubmoduleInitCommand submoduleInit = git.submoduleInit();
            SubmoduleUpdateCommand submoduleUpdate = git.submoduleUpdate();
            String[] submoduleArr = sub.split(",");
            for (String s : submoduleArr) {
                logger.info("准备下载分支仓：" + s);
                submoduleInit.addPath(s);
                submoduleUpdate.addPath(s);
            }
            submoduleInit.call();
            submoduleUpdate.call();
            logger.info("分支仓下载完成。。。");
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }

    }

    /**
     * 拉取主仓项目
     *
     * @param credentialsProvider gitlab身份令牌
     * @param localPath           本地仓地址
     * @param branchName          分支名称
     */
    public static void pullRepository(CredentialsProvider credentialsProvider, String localPath, String branchName) {
        if (!StringUtils.hasText(branchName))
            branchName = DEFAULT_BRANCH;

        logger.info("pullRepository from localPath = [" + localPath + "], gitCredentials = [" + credentialsProvider + "], branchName = [" + branchName + "]");
        Git git = getGitClientByLocal(localPath);
        try {
            PullCommand pullCommand = git.pull()
                    .setRemoteBranchName(branchName);
            configureCommand(pullCommand, credentialsProvider);
            PullResult result = pullCommand.call();
            if (Optional.ofNullable(result)
                    .map(PullResult::getMergeResult)
                    .map(MergeResult::getConflicts)
                    .map(r -> !r.isEmpty())
                    .orElse(false)) {
                throw new ComponentException("合并发生冲突,请通知管理员");
            }

        } catch (Exception e) {
            throw new ComponentException(e);
        }
    }
    /**
     * 更新分支仓
     *
     * @param localPath 主仓
     * @param sub       分支仓名，多个时用，隔开
     */
    public static void pullSubRepository(String localPath, String sub) {
        logger.info("localPath = [" + localPath + "], sub = [" + sub + "]");
        try {
            String newPath = localPath + "\\.git\\modules";
            String[] subArr = sub.split(",");
            for (String path : subArr) {
                Git git = getGitClientByLocal(newPath + path);
                PullCommand pullCommand = git.pull();
                configureCommand(pullCommand, null);
                pullCommand.call();
            }
        } catch (Exception e) {
            throw new ComponentException(e);
        }
    }

    /**
     * 提交本地所有代码
     *
     * @param localPath     本地目录
     * @param message       备注
     * @param committerName 提交人
     * @return RevCommit
     */
    public static RevCommit commitAllRepository(String localPath, String message, String committerName) {
        return commitRepository(localPath, message, committerName, null);
    }
    /**
     * 提交本地代码
     *
     * @param localPath 本地目录
     * @param files     文件路径，值为空时，默认为：根目录
     * @param message   备注
     * @return RevCommit
     */
    public static RevCommit commitRepository(String localPath, String message, String committerName, List<String> files) {
        boolean hasUncommittedChanges = hasUncommittedChanges(localPath);
        if (!hasUncommittedChanges)
            throw new ComponentException("提交的文件内容都没有被修改，不能提交");

        Git git = getGitClientByLocal(localPath);
        try {
            WindowCacheConfig cfg = new WindowCacheConfig();
            cfg.setPackedGitMMAP(false);
            cfg.install();
//            WindowCache.reconfigure(cfg);

            if (null == files || files.size() <= 0) {
                // Stage all files in the repo including new files, excluding deleted files
                git.add().addFilepattern(".").call();

                // Stage all changed files, including deleted files, excluding new files
                git.add().addFilepattern(".").setUpdate(true).call();
            } else {
                AddCommand addWithTraceCommand = git.add();
                for (String file : files) {
                    addWithTraceCommand.addFilepattern(file);
                }
                addWithTraceCommand.setUpdate(true);
                addWithTraceCommand.call();

                // 新的文件进行提交
                AddCommand addNewCommand = git.add();
                for (String file : files) {
                    addNewCommand.addFilepattern(file);
                }
                addNewCommand.call();
            }

            CommitCommand commitCommand = git.commit();
            commitCommand.setMessage(message);
            commitCommand.setAllowEmpty(false);
            commitCommand.setAuthor(committerName, "");
            RevCommit commitLog = commitCommand.call();
            logger.info(commitLog.toString());
            return commitLog;
        } catch (Exception e) {
            throw new ComponentException(e);
        }
    }
    /**
     * 将文件列表提交到git仓库中
     *
     * @param localPath git仓库目录
     * @param files     需要提交的文件列表
     * @return 返回本次提交的版本号
     */
    public static RevCommit commitRepositoryWithDiff(String localPath, String message, String committerName, List<String> files) {
        if (StringExtensions.isNullOrEmpty(localPath))
            return null;

        boolean hasUncommittedChanges = hasUncommittedChanges(localPath);
        if (!hasUncommittedChanges) {
            throw new ComponentException("提交的文件内容都没有被修改，不能提交");
        }

        List<DiffEntry> diffEntries = getDiffChanges(localPath, files);
        if (null == diffEntries || 0 == diffEntries.size()) {
            throw new ComponentException("提交的文件内容都没有被修改，不能提交");
        }

        Git git = getGitClientByLocal(localPath);
        try {
            //被修改过的文件
            List<String> updateFiles = new ArrayList<>();
            DiffEntry.ChangeType changeType;
            for (DiffEntry entry : diffEntries) {
                changeType = entry.getChangeType();
                switch (changeType) {
                    case ADD:
                        updateFiles.add(entry.getNewPath());
                        break;
                    case COPY:
                        updateFiles.add(entry.getNewPath());
                        break;
                    case DELETE:
                        updateFiles.add(entry.getOldPath());
                        break;
                    case MODIFY:
                        updateFiles.add(entry.getOldPath());
                        break;
                    case RENAME:
                        updateFiles.add(entry.getNewPath());
                        break;
                }
            }
            //将文件提交到git仓库中，并返回本次提交的版本号
            AddCommand addCmd = git.add();
            for (String file : updateFiles) {
                addCmd.addFilepattern(file);
            }
            addCmd.call();

            CommitCommand commitCmd = git.commit();
            for (String file : updateFiles) {
                commitCmd.setOnly(file);
            }
            return commitCmd
                    .setCommitter(committerName, "")
                    .setMessage(message)
                    .call();
        } catch (Exception ex) {
            throw new ComponentException(ex);
        }
    }

    private static boolean hasUncommittedChanges(String localPath) {
        Git git = getGitClientByLocal(localPath);
        try {
            Status status = git.status().call();
            return status.hasUncommittedChanges();
        } catch (GitAPIException e) {
            throw new ComponentException(e);
        }
    }
    private static List<DiffEntry> getDiffChanges(String localPath, List<String> files) {
        //打开git仓库
        Git git = getGitClientByLocal(localPath);
        try {
            //判断是否有被修改过的文件
            return git.diff()
                    .setPathFilter(PathFilterGroup.createFromStrings(files))
                    .setShowNameAndStatusOnly(true)
                    .call();
        } catch (GitAPIException e) {
            throw new ComponentException(e);
        }
    }
    private static void showStatus(String gitRoot) throws GitAPIException {
        System.out.println("==============Show current git status===========");
        Git git = getGitClientByLocal(gitRoot);
        Status status = git.status().call();
        status.getAdded().forEach(m -> System.out.println("Add file: " + m));
        status.getRemoved().forEach(m -> System.out.println("Add file: " + m));
        status.getModified().forEach(m -> System.out.println("Add file: " + m));
        status.getUntracked().forEach(m -> System.out.println("Add file: " + m));
        status.getConflicting().forEach(m -> System.out.println("Add file: " + m));
        status.getMissing().forEach(m -> System.out.println("Add file: " + m));
        System.out.println("=========================================");
    }


    /**
     * push到远程仓库<br>
     *
     * @param credentialsProvider git身份令牌
     * @param localPath           本地目录
     */
    public static void pushRepository(CredentialsProvider credentialsProvider, String localPath) {
        logger.info("localPath = [" + localPath + "]");
        Git git = getGitClientByLocal(localPath);
        try {
            WindowCacheConfig cfg = new WindowCacheConfig();
            cfg.setPackedGitMMAP(false);
            cfg.install();
//            WindowCache.reconfigure(cfg);

            PushCommand pushCommand = git.push();
            configureCommand(pushCommand, credentialsProvider);
//            pushCommand.add(fileNames);
            pushCommand.setPushAll();
            Iterable<PushResult> iterable = pushCommand.call();
            for (PushResult pushResult : iterable) {
                for (RemoteRefUpdate remoteUpdate : pushResult.getRemoteUpdates()) {
                    logger.info("push git result:{}", remoteUpdate.toString());
                    switch (remoteUpdate.getStatus()) {
                        case REJECTED_NODELETE:
                        case REJECTED_OTHER_REASON:
                        case REJECTED_NONFASTFORWARD:
                        case REJECTED_REMOTE_CHANGED:
                            throw new ComponentException("推送到远程仓库被拒绝");
                    }

                }
            }
        } catch (Exception e) {
            throw new ComponentException(e);
        }
    }
    /**
     * 远程提交分支仓
     *
     * @param credentialsProvider git身份令牌
     * @param localPath           本地目录
     * @param sub                 分支仓
     * @return 执行消息
     */
    public static String pushSubRepository(CredentialsProvider credentialsProvider, String localPath, String sub) {
        logger.info("pushSubRepository localPath = [" + localPath + "], sub = [" + sub + "], credentialsProvider = [" + credentialsProvider + "]");
        String newPath = localPath + "\\.git\\modules";
        Git git = getGitClientByLocal(newPath + sub);
        try {
            PushCommand pushCommand = git.push()
                    .setForce(true)
                    .setPushAll();
            configureCommand(pushCommand, credentialsProvider);
            Iterable<PushResult> iterable = pushCommand.call();
            for (PushResult pushResult : iterable) {
                logger.info(pushResult.toString());
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    /**
     * 给提交点打标签，并同步到远程github
     *
     * @param localPath 本地目录
     * @param tagName tag名称
     * @param message 消息
     * @param commitIdStr 提交Id字符串
     * @return 执行消息
     */
    public static String tagAndPush(CredentialsProvider credentialsProvider, String localPath, String tagName, String message, String commitIdStr) {
        String errorMsg = "";
        logger.info("git打标签--localUrl = [{}], commitId = [{}],tagName = [{}], message = [{}]", localPath, commitIdStr, tagName, message);
        Git git = getGitClientByLocal(localPath);
        try {
            //1 >> git tag -a tagName commit-SHA-1 -m 'tag annotation'
            TagCommand tagCommand = git.tag()
                    .setName(tagName)
                    .setAnnotated(true)//带注释的tag
                    .setMessage(message)
                    .setForceUpdate(true);//强制更新替换
            //根据短id获取全id
            String SHA1_ID = "";
            //2 >> git log
            Iterable<RevCommit> commits = git.log().call();
            for (RevCommit commit : commits) {
                if (commit.getName().startsWith(commitIdStr)) {
                    SHA1_ID = commit.getName();
                    break;
                }
            }
            if (StringExtensions.isNullOrEmpty(SHA1_ID)) {
                logger.error("git打标签--失败，根据短提交点id未能获取全的");
                throw new ComponentException("找不到全的提交点id，commitId=" + commitIdStr);
            }
            //不支持短id，需要完整的id
            ObjectId commitId = ObjectId.fromString(SHA1_ID);
            RevWalk revWalk = new RevWalk(git.getRepository());
            RevCommit commit = revWalk.parseCommit(commitId);
            revWalk.close();
            //设置提交点对象
            tagCommand.setObjectId(commit);
            Ref call = tagCommand.call();
            logger.info("git打标签--完成localUrl = [{}]，标记结果Ref=[{}]", localPath, call.toString());
            //3 >> git push origin --tags
            PushCommand pushCommand = git.push()
                    .add("refs/tags/" + tagName);
            configureCommand(pushCommand, credentialsProvider);
            //将所有标签同步到github
//            pushCommand.setPushTags();
            pushCommand.call();

            logger.info("git打标签--推送localUrl = [{}]，tag推送成功", localPath);
        } catch (Exception e) {
            logger.error("git打标签--失败localUrl = [{}],error=[{}]", localPath, e.getMessage());
            errorMsg = e.getMessage();
            e.printStackTrace();
        }
        return errorMsg;
    }


    /**
     * 清理分支
     */
    public static void cleanRepository(String localPath) {
        logger.info("cleanRepository localPath = [" + localPath + "]");
        Git git = getGitClientByLocal(localPath);
        try {
            git.reset().setMode(ResetCommand.ResetType.HARD).call();
            Set<String> call = git.clean().setCleanDirectories(true).setForce(true).call();
            logger.info(call.toString());
        } catch (Exception e) {
            throw new ComponentException(e);
        }
    }

    /**
     * 切换本地分支
     *
     * @param localPath 本地目录
     * @param branchName 分支名称
     */
    private static void switchLocalBranch(String localPath, String branchName) {
        if (!StringUtils.hasText(branchName))
            branchName = DEFAULT_BRANCH;
        logger.info("localPath = [" + localPath + "], branchName = [" + branchName + "]");
        Git git = getGitClientByLocal(localPath);
        try {
            Repository repository = git.getRepository();
            ListBranchCommand listBranchCommand = git.branchList();
            List<Ref> ll = listBranchCommand.setListMode(ListBranchCommand.ListMode.ALL).call();
            for (Ref ref : ll) {
                logger.info(ref.getObjectId().toString());
            }

            logger.info(">>>>>>>>>>>>>>>>>>>>");
            RemoteListCommand remoteListCommand = git.remoteList();
            List<RemoteConfig> list = remoteListCommand.call();
            for (RemoteConfig remoteConfig : list) {
                logger.info(remoteConfig.getName());
            }

            logger.info(">>>>>>>>>>>>>>>>>>>");
            //得到仓库本地分支
            String currentBranchName = repository.getBranch();

            LsRemoteCommand lsRemoteCommand = Git.lsRemoteRepository();
            Collection<Ref> collection = lsRemoteCommand.call();
            for (Ref ref : collection) {
                logger.info(ref.getName());
            }

            logger.info(">>>>>>>>>>>>>>>>>>>>");
            Map<String, Ref> map = repository.getAllRefs();
            Set<String> keys = map.keySet();
            for (String string : keys) {
                logger.info(string);
            }
            logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            Collection<Ref> values = map.values();
            for (Ref ref : values) {
                Ref.Storage storage = ref.getStorage();
                logger.info(ref.getName());
            }
            logger.info(">>>>>>>>>>>>>>>>>>>");
        } catch (Exception e) {
            throw new ComponentException(e);
        }
    }

    /**
     * 切换分支
     * 首先判断本地是否已有此分支
     *
     * @param localPath  本地目录
     * @param branchName 需要切换的分支名称
     */
    public static void checkoutBranch(String localPath, String branchName) {
        if (!StringUtils.hasText(branchName))
            branchName = DEFAULT_BRANCH;
        logger.info("localPath = [" + localPath + "], branchName = [" + branchName + "]");
        Git git = getGitClientByLocal(localPath);

        try {
            String newBranch = branchName.substring(branchName.lastIndexOf("/") + 1);
            CheckoutCommand checkoutCommand = git.checkout();
            List<String> list = findLocalBranchNames(localPath);
            if (!list.contains(newBranch)) {//如果本地分支
                checkoutCommand.setStartPoint(branchName);
                checkoutCommand.setCreateBranch(true);
            }
            checkoutCommand.setName(newBranch);
            checkoutCommand.call();
        } catch (Exception e) {
            throw new ComponentException(e);
        }
    }
    /**
     * 新建一个分支并同步到远程仓库
     *
     * @param credentialsProvider git用户
     * @param remoteRepoURI       远程git地址
     * @param localPath           本地目录
     * @param branchName          分支名称
     */
    public static String checkoutAndPushBranch(CredentialsProvider credentialsProvider, String remoteRepoURI, String localPath, String branchName) {
        if (!StringUtils.hasText(branchName))
            branchName = DEFAULT_BRANCH;
        String newBranchIndex = "refs/heads/" + branchName;
        Git git = getGitClientByCredentials(credentialsProvider, remoteRepoURI, localPath);
        try {
            //检查新建的分支是否已经存在，如果存在则将已存在的分支强制删除并新建一个分支
            List<Ref> refs = git.branchList().call();
            for (Ref ref : refs) {
                if (ref.getName().equals(newBranchIndex)) {
                    System.out.println("Removing branch before");
                    checkoutBranch(localPath, DEFAULT_BRANCH);//要删除分支之前先切换分支到主仓
                    git.branchDelete()
                            .setBranchNames(branchName)
                            .setForce(true)
                            .call();
                    break;
                }
            }
            //新建分支
            Ref ref = git.branchCreate().setName(branchName).call();
            //推送到远程
            git.push()
                    .add(ref)
                    .setCredentialsProvider(credentialsProvider)
                    .call();
            return remoteRepoURI + " " + "feature/" + branchName;
        } catch (Exception e) {
            throw new ComponentException("创建分支失败！[" + branchName + "], \\\n" + e);
        }
    }

    /**
     * fetch
     *
     * @param localPath 本地目录
     * @return FetchResult
     */
    public static FetchResult fetchBranch(String localPath) {
        logger.info("fetchBranch from localPath = [" + localPath + "]");
        Git git = getGitClientByLocal(localPath);
        FetchCommand fetch = git.fetch();
        fetch.setRemote("origin");
        fetch.setTagOpt(TagOpt.FETCH_TAGS);
        fetch.setRemoveDeletedRefs(GitSettingDTO.deleteUnTrackedBranches);
        configureCommand(fetch, null);
        try {
            FetchResult result = git.fetch().call();
            if (result.getTrackingRefUpdates() != null && result.getTrackingRefUpdates().size() > 0) {
                logger.info("Fetched for remote " + DEFAULT_BRANCH + " and found " + result.getTrackingRefUpdates().size() + " updates");
            }
            return result;
        } catch (GitAPIException e) {
            String message = "Could not fetch remote for " + DEFAULT_BRANCH + " remote: "
                    + git.getRepository().getConfig().getString("remote", "origin", "url");
            logger.error(message, e);
            return null;
        }
    }
    /**
     * Get the working directory ready.
     *
     * @param branchName branchName to refresh
     * @return head id
     */
    public String refresh(String localPath, String branchName) {
        Git git = null;
        try {
            git = JGitUtil.getGitClientByLocal(localPath);
            if (shouldPull(git, localPath)) {
                FetchResult fetchStatus = fetchBranch(localPath);
                if (GitSettingDTO.deleteUnTrackedBranches && fetchStatus != null) {
                    deleteUnTrackedLocalBranches(fetchStatus.getTrackingRefUpdates(), git);
                }
            }

            // checkout after fetch, we can get any new branches, tags, ect.
            // if nothing to update so just checkout and merge.
            // Merge because remote branch could have been updated before
            checkout(git, branchName);
            tryMerge(git, branchName);

            // always return what is currently HEAD as the version
            return git.getRepository().findRef("HEAD").getObjectId().getName();
        } catch (RefNotFoundException e) {
            throw new ComponentException("No such branchName: " + branchName, e);
        } catch (NoRemoteRepositoryException e) {
            throw new ComponentException("No such repository:  RemoteGitUrl", e);
        } catch (GitAPIException e) {
            throw new ComponentException("Cannot clone or checkout repository: RemoteGitUrl", e);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot load environment", e);
        } finally {
            try {
                if (git != null) {
                    git.close();
                }
            } catch (Exception e) {
                logger.warn("Could not close git repository", e);
            }
        }
    }

    private Ref checkout(Git git, String branchName) {
        CheckoutCommand checkout = git.checkout().setName(branchName);
        if (shouldTrack(git, branchName)) {
            checkout
                    .setCreateBranch(true)
                    .setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK)
                    .setStartPoint("origin/" + branchName);
        }
        try {
            return checkout.call();
        } catch (GitAPIException e) {
            throw new ComponentException("Failed to clone the remote git url", e);
        }
    }
    private void tryMerge(Git git, String branchName) {
        if (isBranch(git, branchName)) {
            // merge results from fetch
            merge(git, branchName);
            if (!isClean(git, branchName)) {
                logger.warn("The local repository is dirty or ahead of origin. Resetting" + " it to origin/"
                        + branchName + ".");
                //                    resetHard(git, branchName, LOCAL_BRANCH_REF_PREFIX + branchName);
            }
        }
    }
    private MergeResult merge(Git git, String branchName) {
        try {
            MergeCommand merge = git.merge();
            merge.include(git.getRepository().findRef("origin/" + branchName));
            MergeResult result = merge.call();
            if (!result.getMergeStatus().isSuccessful()) {
                logger.warn("Merged from remote " + branchName + " with result " + result.getMergeStatus());
            }
            return result;
        } catch (Exception ex) {
            String message = "Could not merge remote for " + branchName + " remote: "
                    + git.getRepository().getConfig().getString("remote", "origin", "url");
            logger.error(message, ex);
            return null;
        }
    }
    /**
     * Deletes local branches if corresponding remote branch was removed.
     *
     * @param trackingRefUpdates list of tracking ref updates
     * @param git                git instance
     * @return list of deleted branches
     */
    private Collection<String> deleteUnTrackedLocalBranches(Collection<TrackingRefUpdate> trackingRefUpdates, Git git) {
        if (CollectionUtils.isEmpty(trackingRefUpdates)) {
            return Collections.emptyList();
        }

        Collection<String> branchesToDelete = new ArrayList<>();
        for (TrackingRefUpdate trackingRefUpdate : trackingRefUpdates) {
            ReceiveCommand receiveCommand = trackingRefUpdate.asReceiveCommand();
            if (receiveCommand.getType() == ReceiveCommand.Type.DELETE) {
                String localRefName = trackingRefUpdate.getLocalName();
                if (StringUtils.startsWithIgnoreCase(localRefName, LOCAL_BRANCH_REF_PREFIX)) {
                    String localBranchName = localRefName.substring(
                            LOCAL_BRANCH_REF_PREFIX.length(),
                            localRefName.length()
                    );
                    branchesToDelete.add(localBranchName);
                }
            }
        }

        if (CollectionUtils.isEmpty(branchesToDelete)) {
            return Collections.emptyList();
        }

        try {
            // make sure that deleted branch not a current one
            checkout(git, DEFAULT_BRANCH);
            return deleteBranches(git, branchesToDelete);
        } catch (Exception ex) {
            String message = String.format("Failed to delete %s branches.", branchesToDelete);
            logger.error(message, ex);
            return Collections.emptyList();
        }
    }
    private List<String> deleteBranches(Git git, Collection<String> branchesToDelete) throws GitAPIException {
        DeleteBranchCommand deleteBranchCommand = git.branchDelete()
                .setBranchNames(branchesToDelete.toArray(new String[0]))
                // local branch can contain data which is not merged to HEAD - force
                // delete it anyway, since local copy should be R/O
                .setForce(true);
        List<String> resultList = deleteBranchCommand.call();
        logger.info(
                String.format("Deleted %s branches from %s branches to delete.", resultList, branchesToDelete));
        return resultList;
    }

    private boolean shouldPull(Git git, String localPath) throws GitAPIException {
        boolean shouldPull;

        if (GitSettingDTO.refreshRate > 0 && System.currentTimeMillis() - GitSettingDTO.refreshRate < GitSettingDTO.refreshRate * 1000L) {
            return false;
        }

        Status gitStatus;
        try {
            gitStatus = git.status().call();
        } catch (JGitInternalException e) {
            onPullInvalidIndex(git, localPath, e);
            gitStatus = git.status().call();
        }

        boolean isWorkingTreeClean = gitStatus.isClean();
        String originUrl = git.getRepository().getConfig().getString("remote", "origin", "url");

        if (GitSettingDTO.forcePull && !isWorkingTreeClean) {
            shouldPull = true;
            logDirty(gitStatus);
        } else {
            shouldPull = isWorkingTreeClean && originUrl != null;
        }
        if (!isWorkingTreeClean && !GitSettingDTO.forcePull) {
            logger.info("Cannot pull from remote " + originUrl + ", the working tree is not clean.");
        }
        return shouldPull;
    }
    private boolean shouldTrack(Git git, String branchName) {
        return isBranch(git, branchName) && !isLocalBranch(git, branchName);
    }
    private boolean isClean(Git git, String branchName) {
        StatusCommand status = git.status();
        try {
            BranchTrackingStatus trackingStatus = BranchTrackingStatus.of(git.getRepository(), branchName);
            boolean isBranchAhead = trackingStatus != null && trackingStatus.getAheadCount() > 0;
            return status.call().isClean() && !isBranchAhead;
        } catch (Exception e) {
            String message = "Could not execute status command on local repository. Cause: ("
                    + e.getClass().getSimpleName() + ") " + e.getMessage();
            logger.error(message, e);
            return false;
        }
    }
    private boolean isBranch(Git git, String branchName) {
        return containsBranch(git, branchName, ListBranchCommand.ListMode.ALL);
    }
    private boolean isLocalBranch(Git git, String branchName) {
        return containsBranch(git, branchName, null);
    }
    private boolean containsBranch(Git git, String branchName, ListBranchCommand.ListMode listMode) {
        ListBranchCommand command = git.branchList();
        if (listMode != null) {
            command.setListMode(listMode);
        }
        try {
            List<Ref> branches = command.call();
            for (Ref ref : branches) {
                if (ref.getName().endsWith("/" + branchName)) {
                    return true;
                }
            }
            return false;
        } catch (GitAPIException e) {
            throw new ComponentException("Failed to list the branches", e);
        }
    }
    private void onPullInvalidIndex(Git git, String localPath, JGitInternalException e) {
        if (!e.getMessage().contains("Short read of block.")) {
            throw e;
        }
        if (!GitSettingDTO.forcePull) {
            throw e;
        }
        try {
            new File(localPath, ".git/index").delete();
            git.reset().setMode(ResetCommand.ResetType.HARD).setRef("HEAD").call();
        } catch (GitAPIException ex) {
            e.addSuppressed(ex);
            throw e;
        }
    }
    private void logDirty(Status status) {
        Set<String> dirties = new HashSet<>();
        dirties.addAll(status.getAdded());
        dirties.addAll(status.getChanged());
        dirties.addAll(status.getRemoved());
        dirties.addAll(status.getMissing());
        dirties.addAll(status.getModified());
        dirties.addAll(status.getConflicting());
        dirties.addAll(status.getUntracked());
        logger.warn(String.format("Dirty files found: %s", dirties));
    }
    
    /**
     * 将git仓库内容回滚到指定版本的上一个版本
     *
     * @param localPath 仓库目录
     * @param revision  指定的版本号
     * @return true, 回滚成功, 否则，false
     */
    public static boolean rollBackPreRevision(String localPath, String revision) {

        Git git = getGitClientByLocal(localPath);
        try {
            Repository repository = git.getRepository();

            RevWalk walk = new RevWalk(repository);
            ObjectId objId = repository.resolve(revision);
            RevCommit revCommit = walk.parseCommit(objId);
            String preVision = revCommit.getParent(0).getName();
            git.reset().setMode(ResetCommand.ResetType.HARD).setRef(preVision).call();
            repository.close();
            return true;
        } catch (Exception e) {
            throw new ComponentException(e);
        }
    }

    /**
     * 获取本地项目的git的新增、更新、删除的文件列表
     *
     * @param localPath 本地git项目路径
     * @return 返回新增、更新、删除的文件列表
     */
    public static Map<String, Set<String>> getChangedFiles(String localPath) {
        logger.info("localPath = [" + localPath + "]");
        Map<String, Set<String>> result = new HashMap<>();
        Git git = getGitClientByLocal(localPath);
        try {
            StatusCommand statusCommand = git.status();
            Status status = statusCommand.call();
            result.put("added", status.getAdded());
            result.put("changed", status.getChanged());
            result.put("missed", status.getMissing());
        } catch (GitAPIException e) {
            throw new ComponentException(e);
        }

        return result;
    }
    /**
     * 获取指定分支的指定文件内容
     *
     * @param branchName 分支名称
     * @param javaPath   文件路径
     * @return java类
     * @throws IOException 文件流异常
     */
    public static String getBranchSpecificFileContent(String localPath, String branchName, String javaPath) throws IOException {
        Repository repository = openJGitRepository(localPath);
        Ref branch = repository.exactRef("refs/heads/" + branchName);
        ObjectId objId = branch.getObjectId();
        RevWalk walk = new RevWalk(repository);
        RevTree tree = walk.parseTree(objId);
        return getFileContent(repository, javaPath, tree, walk);
    }
    /**
     * 获取指定分支指定Tag版本的指定文件内容
     *
     * @param tagRevision Tag版本
     * @param javaPath    文件路径
     * @return java类
     * @throws IOException  文件流异常
     */
    public static String getTagRevisionSpecificFileContent(String localPath, String tagRevision, String javaPath) throws IOException {
        Repository repository = openJGitRepository(localPath);
        ObjectId objId = repository.resolve(tagRevision);
        RevWalk walk = new RevWalk(repository);
        RevCommit revCommit = walk.parseCommit(objId);
        RevTree tree = revCommit.getTree();
        return getFileContent(repository, javaPath, tree, walk);
    }

    /**
     * 获取指定分支指定的指定文件内容
     *
     * @param javaPath 文件路径
     * @param tree     git RevTree
     * @param walk     git RevWalk
     * @return java类
     * @throws IOException 文件流异常
     */
    private static String getFileContent(Repository repository, String javaPath, RevTree tree, RevWalk walk) throws IOException {
        ObjectId blobId;
        try (TreeWalk treeWalk = TreeWalk.forPath(repository, javaPath, tree)) {
            blobId = treeWalk.getObjectId(0);
        }
        ObjectLoader loader = repository.open(blobId);
        byte[] bytes = loader.getBytes();
        walk.dispose();
        return new String(bytes);
    }
    /**
     * 拿到当前本地分支名
     *
     * @param localPath 主仓
     * @return 当前本地分支名
     */
    public static String getCurrentBranch(String localPath) {
        logger.info("localPath = [" + localPath + "]");
        Git git = getGitClientByLocal(localPath);
        try {
            return git.getRepository().getBranch();
        } catch (IOException e) {
            throw new ComponentException(e);
        }
    }

    /**
     * 拿到当前远程分支名
     *
     * @param localPath 主仓
     * @return 当前远程分支名
     */
    public static String getCurrentRemoteBranch(String localPath) {
        logger.info("localPath = [" + localPath + "]");
        Git git = getGitClientByLocal(localPath);
        StoredConfig storedConfig = git.getRepository().getConfig();
        return storedConfig.getString("branch", getCurrentBranch(localPath), "remote");
    }
    /**
     * 获取所有远程
     *
     * @param localPath 主仓
     * @return 远程Git路径
     */
    public static List<String> findRemotes(String localPath) {
        logger.info("localPath = [" + localPath + "]");
        List<String> result = new LinkedList<>();
        Git git = getGitClientByLocal(localPath);
        try {
            RemoteListCommand remoteListCommand = git.remoteList();
            List<RemoteConfig> list = remoteListCommand.call();
            for (RemoteConfig remoteConfig : list) {
                result.add(remoteConfig.getName());
            }
        } catch (GitAPIException e) {
            throw new ComponentException(e);
        }

        return result;
    }
    /**
     * 获取本地所有分支名
     *
     * @param localPath 主仓
     * @return  远程Git路径
     */
    public static List<String> findLocalBranchNames(String localPath) {
        logger.info("localPath = [" + localPath + "]");
        List<String> result = new LinkedList<String>();
        Git git = getGitClientByLocal(localPath);
        Map<String, Ref> map = git.getRepository().getAllRefs();
        Set<String> keys = map.keySet();
        for (String str : keys) {
            if (str.contains("refs/heads")) {
                String el = str.substring(str.lastIndexOf("/") + 1);
                result.add(el);
            }
        }
        return result;
    }
    /**
     * 根据名称获取所有远程分支
     *
     * @param localPath 主仓
     * @return  远程Git路径
     */
    public static List<String> findRemoteBranchNames(String localPath, String remote) {
        logger.info("localPath = [" + localPath + "], remote = [" + remote + "]");
        List<String> result = new LinkedList<String>();
        Git git = getGitClientByLocal(localPath);
        Map<String, Ref> map = git.getRepository().getAllRefs();
        Set<String> keys = map.keySet();
        String index = "refs/remotes/" + remote;
        for (String str : keys) {
            if (str.contains(index)) {
                //String el=str.substring(str.lastIndexOf("/")+1, str.length());
                result.add(str);
                logger.info(str);
            }
        }
        return result;
    }


    /**
     * 获取项目分支的提交日志
     *
     * @param localPath 主仓
     * @return 项目分支的提交日志
     */
    public static List<GitCommitMessageDTO> getCommitLogs(String localPath, String branchName) {
        if (!StringUtils.hasText(branchName))
            branchName = DEFAULT_BRANCH;
        logger.info("localPath = [" + localPath + "], branchName = [" + branchName + "]");
        List<GitCommitMessageDTO> result = new ArrayList<>();
        Git git = getGitClientByLocal(localPath);
        Repository repository = git.getRepository();
        try {
            int flag = 0;
            RevWalk walk = new RevWalk(repository);
            LogCommand logCommand = git.log();
            Iterable<RevCommit> list = logCommand.call();
            for (RevCommit commit : list) {
                GitCommitMessageDTO commitMessage = new GitCommitMessageDTO();
                boolean foundInThisBranch = false;
                RevCommit targetCommit = walk.parseCommit(commit.getId());
                for (Map.Entry<String, Ref> entry : repository.getAllRefs().entrySet()) {
                    if (entry.getKey().startsWith("refs/remotes/origin")) {
                        if (walk.isMergedInto(targetCommit, walk.parseCommit(entry.getValue().getObjectId()))) {
                            String foundInBranch = entry.getValue().getTarget().getName();
                            if (foundInBranch.contains(branchName)) {
                                // 等于2为：提交记录来自两个合并分支，算是merge的记录
                                if (targetCommit.getParents().length == 2) {
                                    flag++;
                                    foundInThisBranch = true;
                                }
                            }
                        }
                    }
                }
                if (foundInThisBranch) {
                    commitMessage.setCommitId(commit.getName());
                    commitMessage.setCommitIdent(commit.getAuthorIdent().getName());
                    commitMessage.setCommitMessage(commit.getFullMessage());
                    commitMessage.setCommitDate(new Date(commit.getCommitTime() * 1000L).toString());
                    commitMessage.setLastCommitId(commit.getParent(0).getName());
                    commitMessage.setMergeBranchCommitId(commit.getParent(1).getName());
                    result.add(commitMessage);
                }
                //只取merge合并记录的前5条记录
                if (flag == 5) {
                    break;
                }
            }
            return result;
        } catch (Exception e) {
            throw new ComponentException(e);
        }
    }
    /**
     * 查询本次提交的日志
     *
     * @param gitRoot  git仓库
     * @param revision 版本号
     * @return dif列表
     */
    public static List<DiffEntry> getDiffLogs(String gitRoot, String revision) {
        Git git = getGitClientByLocal(gitRoot);
        try {
            Repository repository = git.getRepository();

            ObjectId objId = repository.resolve(revision);
            Iterable<RevCommit> allCommitsLater = git.log().add(objId).call();
            Iterator<RevCommit> iter = allCommitsLater.iterator();
            RevCommit commit = iter.next();
            TreeWalk tw = new TreeWalk(repository);
            tw.addTree(commit.getTree());

            commit = iter.next();
            if (commit != null)
                tw.addTree(commit.getTree());
            else
                return null;

            tw.setRecursive(true);
            RenameDetector rd = new RenameDetector(repository);
            rd.addAll(DiffEntry.scan(tw));

            return rd.compute();
        } catch (Exception e) {
            throw new ComponentException(e);
        }
    }


    /**
     * 文件操作：删除目录
     *
     * @param localPath 本地目录
     * @return 是否成功
     */
    public static boolean deleteDirIfExists(File localPath) {
        logger.info("delete directory = [" + localPath + "]");

        if (!localPath.exists())
            return true;

        if (localPath.isDirectory()) {
            File[] files = localPath.listFiles();
            assert files != null;
            for (File file : files) {
                try {
                    FileUtils.delete(file, FileUtils.RECURSIVE);
                } catch (IOException e) {
                    throw new ComponentException("Failed to initialize base directory", e);
                }
            }
        }
        // 目录此时为空，可以删除
        return localPath.delete();
    }
    /**
     * 文件操作：强制创建目录
     * Makes a directory, including any necessary but nonexistent parent
     * directories. If a file already exists with specified name, it is  not a directory then an IOException is thrown.
     * If the directory cannot be created (or does not already exist) then an IOException is thrown.
     *
     * @param directory directory to create, must not be {@code null}
     * @throws NullPointerException if the directory is {@code null}
     */
    public static void forceMkdir(final File directory) {
        if (directory.exists()) {
            if (!directory.isDirectory()) {
                final String message =
                        "File "
                                + directory
                                + " exists and is "
                                + "not a directory. Unable to create directory.";
                throw new ComponentException(message);
            }
        } else {
            if (!directory.mkdirs()) {
                // Double-check that some other thread or process hasn't made
                // the directory in the background
                if (!directory.isDirectory()) {
                    final String message =
                            "Unable to create directory " + directory;
                    throw new ComponentException(message);
                }
            }
        }
    }
    public static boolean copyFromJar(boolean isClassPath, String sourceDir, String targetDir) {
        try {
            Resource[] resources = ResourcePatternUtils.getResourcePatternResolver(null)
                    .getResources(isClassPath ? "classpath:" + sourceDir : sourceDir);
            for (Resource resource : resources) {
                File file = resource.getFile();
                copyTo(file, "", targetDir);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    private static void copyTo(File file, String sourceDir, String targetDir) {
        if (file.isDirectory()) {
            String dirName = file.getName();
            logger.info("创建目录：" + targetDir + File.separator + dirName);
            File dir = new File(targetDir, dirName);
            if (!dir.exists())
                dir.mkdir();

            File[] files = file.listFiles();
            if (null != files) {
                for (File listFile : files) {
                    copyTo(listFile, sourceDir + File.separator + dirName, targetDir + File.separator + dirName);
                }
            }
        } else {
            logger.info(sourceDir + File.separator + file.getName());
            copyFileStream(sourceDir + File.separator + file.getName(), targetDir, file.getName());
        }
    }
    private static void copyFileStream(String sourceFilePath, String targetDir, String fileName) {
        try (InputStream stream = JGitUtil.class.getClassLoader().getResourceAsStream(sourceFilePath)) {
            Files.copy(stream, Paths.get(targetDir, fileName), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Data
    @Builder
    public static class GitSettingDTO{
//
//    /**
//     * Username for authentication with remote repository.
//     */
//    private String username;
//    /**
//     * Password for authentication with remote repository.
//     */
//    private String password;

        /**
         * Timeout (in seconds) for obtaining HTTP or SSH connection (if applicable). Default 5 seconds.
         */
        protected static int timeout = 2000;
        /**
         * Time (in seconds) between refresh of the git repository.
         */
        protected static int refreshRate = 0;
        /**
         * Time of the last refresh of the git repository.
         */
        protected static long lastRefresh = 5; // 5s刷新一次
        /**
         * Flag to indicate that the repository should be cloned on startup (not on demand). Generally leads to slower
         * startup but faster first query.
         */
        protected static boolean cloneOnStart = true;

        /**
         * Transport configuration callback for JGit commands.
         */
        protected static TransportConfigCallback transportConfigCallback;

        /**
         * Flag to indicate that the repository should force pull. If true discard any local changes and take from remote
         * repository.
         */
        protected static boolean forcePull = true;
        /**
         * Flag to indicate that the branch should be deleted locally if it's origin tracked branch was removed.
         */
        protected static boolean deleteUnTrackedBranches = true;
        /**
         * Flag to indicate that SSL certificate validation should be bypassed when communicating with a repository served
         * over an HTTPS connection.
         */
        protected static boolean skipSslValidation = true;
    }
}
