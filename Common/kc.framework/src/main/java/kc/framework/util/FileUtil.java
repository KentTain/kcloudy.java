package kc.framework.util;

import kc.framework.extension.StringExtensions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class FileUtil {
    private static final String[] EMPTY_STRING_ARRAY = {};

    private static final String EMPTY_STRING = "";

    private static final int NOT_FOUND = -1;

    /**
     * The extension separator character.
     *
     * @since 1.4
     */
    public static final char EXTENSION_SEPARATOR = '.';

    /**
     * The extension separator String.
     *
     * @since 1.4
     */
    public static final String EXTENSION_SEPARATOR_STR = Character.toString(EXTENSION_SEPARATOR);

    /**
     * The Unix separator character.
     */
    private static final char UNIX_SEPARATOR = '/';

    /**
     * The Windows separator character.
     */
    private static final char WINDOWS_SEPARATOR = '\\';

    /**
     * The system separator character.
     */
    private static final char SYSTEM_SEPARATOR = File.separatorChar;

    /**
     * The separator character that is the opposite of the system separator.
     */
    private static final char OTHER_SEPARATOR;

    static {
        if (isSystemWindows()) {
            OTHER_SEPARATOR = UNIX_SEPARATOR;
        } else {
            OTHER_SEPARATOR = WINDOWS_SEPARATOR;
        }
    }

    /**
     * Determines if Windows file system is in use.
     *
     * @return true if the system is Windows
     */
    static boolean isSystemWindows() {
        return SYSTEM_SEPARATOR == WINDOWS_SEPARATOR;
    }

    /**
     * Checks if the character is a separator.
     *
     * @param ch the character to check
     * @return true if it is a separator character
     */
    private static boolean isSeparator(final char ch) {
        return ch == UNIX_SEPARATOR || ch == WINDOWS_SEPARATOR;
    }

    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 获取指定文件夹的所有分片
     *
     * @param filePath 文件夹路径
     * @return
     */
    public static List<File> getAllFiles(Path filePath) {
        List<File> result = new ArrayList<>();
        if (Files.exists(filePath)) {
            try (DirectoryStream<Path> entries = Files.newDirectoryStream(filePath, path -> path.toFile().isFile())) {
                for (Path entry : entries) {
                    File file = entry.toFile();
                    if (file.isFile())
                        result.add(file);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * 获取Jar包内的指定文件夹的所有文件路径
     *
     * @param filePath 文件夹路径
     * @return
     */
    public static List<Path> getAllFilePaths(Path filePath) {
        List<Path> result = new ArrayList<>();
        if (Files.exists(filePath)) {
            try {
                return Files.list(filePath).filter(path -> path.toFile().isFile()).collect(Collectors.toList());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * 从jar包中获取文件资源列表
     * @param isClassPath 是否指定：classpath，true：一般为SpringBoot启动后Controller用，false：单元测试使用
     * @param filePath    文件路径，可以为文件的筛选方式，如：/filepath/*.json
     * @return 文件资源列表
     */
    public static List<Resource> getAllResourcesFromJar(boolean isClassPath, String filePath) {
        List<Resource> result = new ArrayList<>();
        try {
            Resource[] resources = ResourcePatternUtils.getResourcePatternResolver(null)
                    .getResources(isClassPath ? "classpath:" + filePath : filePath);
            Collections.addAll(result, resources);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 获取指定文件夹的所有文件路径
     *
     * @param filePath 文件夹路径
     * @return
     */
    public static void cleanSpace(String filePath) {
        try {
            //删除分片文件夹
            Files.deleteIfExists(Paths.get(filePath));
            //删除tmp文件
            Files.deleteIfExists(Paths.get(filePath + ".tmp"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取windows/linux的项目根目录
     *
     * @return
     */
    public static String getConTextPath() {
        String fileUrl = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        if ("usr".equals(fileUrl.substring(1, 4))) {
            fileUrl = (fileUrl.substring(0, fileUrl.length() - 16));//linux
        } else {
            fileUrl = (fileUrl.substring(1, fileUrl.length() - 10));//windows
        }
        return fileUrl;
    }

    public static String getExtension(final String filename) {
        if (filename == null) {
            return null;
        }
        final int index = indexOfExtension(filename);
        if (index == NOT_FOUND) {
            return "";
        } else {
            return filename.substring(index + 1);
        }
    }

    private static int indexOfExtension(final String filename) {
        if (filename == null) {
            return NOT_FOUND;
        }
        final int extensionPos = filename.lastIndexOf(EXTENSION_SEPARATOR);//点的位置
        final int lastSeparator = indexOfLastSeparator(filename);//最后一个斜杠的位置
        return lastSeparator > extensionPos ? NOT_FOUND : extensionPos;
    }

    private static int indexOfLastSeparator(final String filename) {
        if (filename == null) {
            return NOT_FOUND;
        }
        final int lastUnixPos = filename.lastIndexOf(UNIX_SEPARATOR);//unix的/
        final int lastWindowsPos = filename.lastIndexOf(WINDOWS_SEPARATOR);// windows的\
        return Math.max(lastUnixPos, lastWindowsPos);
    }

    /**
     * 获取文件目录
     *
     * @param filePath
     * @return 目录路径，不以'/'或操作系统的文件分隔符结尾
     */
    public static String extractDirPath(String filePath) {
        int separatePos = Math.max(filePath.lastIndexOf('/'), filePath.lastIndexOf('\\')); // 分隔目录和文件名的位置
        return separatePos == -1 ? null : filePath.substring(0, separatePos);
    }

    /**
     * 解析文件路径，获取文件名
     *
     * @param filePath C:/taoxw/20190103.txt
     * @return
     */
    public static String extractFileName(String filePath) {
        if (filePath != null) {
            if (filePath.lastIndexOf("/") >= 0) {
                return filePath.substring(filePath.lastIndexOf("/") + 1);
            } else if (filePath.lastIndexOf("\\") >= 0) {
                return filePath.substring(filePath.lastIndexOf("\\") + 1);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 解析文件，获取文件名
     *
     * @param file
     * @return
     */
    public static String getFileName(File file) {
        return file.getName();
    }

    /**
     * 追加文件名前缀
     *
     * @param filePath 例如 xxx.png
     * @param prefix   例如temp_xxx.png
     * @return
     */
    public static String prefixFilePath(String filePath, String prefix) {
        String sourceName = extractFileName(filePath);
        return filePath.replace(sourceName, prefix + sourceName);
    }

    /**
     * 追加文件名后缀
     *
     * @param filePath 例如 xxx.png
     * @param suffix   例如xxx_yyyyMMdd.png
     * @return
     */
    public static String suffixFilePath(String filePath, String suffix) {
        String extension = getExtension(filePath);
        return filePath.replace("." + extension, suffix + "." + extension);
    }

    /**
     * 随机唯一文件名字
     *
     * @param suffixName .txt .jpg .JPG .png .PNG
     * @return
     */
    public static String createFileName(String suffixName) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        StringBuilder fileNameBuilder = new StringBuilder(df.format(new Date()));
        fileNameBuilder.append("_");
        fileNameBuilder.append(StringExtensions.leftPadForZero(Integer.toString(new Random().nextInt(1000)), 3));
        fileNameBuilder.append(".");
        fileNameBuilder.append(suffixName);
        return fileNameBuilder.toString();
    }

    public static String createFileName(String preffixName, String suffixName) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        StringBuilder fileNameBuilder = new StringBuilder(preffixName);
        fileNameBuilder.append("_");
        fileNameBuilder.append(df.format(new Date()));
        fileNameBuilder.append("_");
        fileNameBuilder.append(StringExtensions.leftPadForZero(Integer.toString(new Random().nextInt(1000)), 3));
        fileNameBuilder.append(".");
        fileNameBuilder.append(suffixName);
        return fileNameBuilder.toString();
    }

    /**
     * 文件或者目录重命名
     *
     * @param oldFilePath 旧文件路径
     * @param newName     新的文件名,可以是单个文件名和绝对路径
     * @return
     */
    public static boolean renameTo(String oldFilePath, String newName) {
        try {
            File oldFile = new File(oldFilePath);
            //若文件存在
            if (oldFile.exists()) {
                //判断是全路径还是文件名
                if (newName.indexOf("/") < 0 && newName.indexOf("\\") < 0) {
                    //单文件名，判断是windows还是Linux系统
                    String absolutePath = oldFile.getAbsolutePath();
                    if (newName.indexOf("/") > 0) {
                        //Linux系统
                        newName = absolutePath.substring(0, absolutePath.lastIndexOf("/") + 1) + newName;
                    } else {
                        newName = absolutePath.substring(0, absolutePath.lastIndexOf("\\") + 1) + newName;
                    }
                }
                File file = new File(newName);
                //判断重命名后的文件是否存在
                if (file.exists()) {
                    logger.info("该文件已存在,不能重命名 newFilePath={}", file.getAbsolutePath());
                } else {
                    //不存在，重命名
                    return oldFile.renameTo(file);
                }
            } else {
                logger.info("原该文件不存在,不能重命名 oldFilePath={}", oldFilePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 文件拷贝操作
     *
     * @param sourceFile
     * @param targetFile 若目标文件目录不存在，也会自动创建
     */
    public static void copy(String sourceFile, String targetFile) {
        File source = new File(sourceFile);
        File target = new File(targetFile);
        target.getParentFile().mkdirs();
        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel in = null;
        FileChannel out = null;
        try {
            fis = new FileInputStream(source);
            fos = new FileOutputStream(target);
            in = fis.getChannel();//得到对应的文件通道
            out = fos.getChannel();//得到对应的文件通道
            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
                if (fos != null) {
                    fos.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建文件
     *
     * @param filePath
     * @return
     */
    public static File mkFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            String dirPath = extractDirPath(filePath);
            if (mkdirs(dirPath)) {
                try {
                    file.createNewFile();
                    logger.info("文件创建成功. filePath={}", filePath);
                    return file;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                logger.info("文件目录创建失败. dirPath={}", dirPath);
            }
        } else {
            logger.info("文件已存在，无需创建. filePath={}", filePath);
        }
        return null;
    }

    /**
     * 创建文件目录
     *
     * @param dirPath
     * @return
     */
    public static boolean mkdirs(String dirPath) {
        try {
            File fileDir = new File(dirPath);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
                logger.info("文件目录创建成功. dirPath={}", dirPath);
            } else {
                logger.info("文件目录已存在，无需创建. dirPath={}", dirPath);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 将文件名称排序
     *
     * @param filePath
     * @return
     */
    public static List<File> sortByName(String filePath) {
        File[] files = new File(filePath).listFiles();
        List<File> fileList = Arrays.asList(files);
        Collections.sort(fileList, new Comparator<File>() {
            public int compare(File o1, File o2) {
                if (o1.isDirectory() && o2.isFile())
                    return -1;
                if (o1.isFile() && o2.isDirectory())
                    return 1;
                return o1.getName().compareTo(o2.getName());
            }
        });
        return fileList;
    }

    /**
     * 将文件修改日期排序
     *
     * @param filePath
     * @return
     */
    public static List<File> sortByDate(String filePath) {
        File[] files = new File(filePath).listFiles();
        List<File> fileList = Arrays.asList(files);
        Collections.sort(fileList, new Comparator<File>() {
            public int compare(File f1, File f2) {
                long diff = f1.lastModified() - f2.lastModified();
                if (diff > 0)
                    return 1;
                else if (diff == 0)
                    return 0;
                else
                    return -1;//如果 if 中修改为 返回-1 同时此处修改为返回 1  排序就会是递减
            }
        });
        return fileList;
    }

    /**
     * 将文件大小排序
     *
     * @param filePath
     * @return
     */
    public static List<File> sortByLength(String filePath) {
        File[] files = new File(filePath).listFiles();
        List<File> fileList = Arrays.asList(files);
        Collections.sort(fileList, new Comparator<File>() {
            public int compare(File f1, File f2) {
                long diff = f1.length() - f2.length();
                if (diff > 0)
                    return 1;
                else if (diff == 0)
                    return 0;
                else
                    return -1;//如果 if 中修改为 返回-1 同时此处修改为返回 1  排序就会是递减
            }
        });
        return fileList;
    }

    /**
     * 删除文件或者目录下文件（包括目录）
     *
     * @param filePath
     * @return
     */
    public static boolean delete(String filePath) {
        try {
            File sourceFile = new File(filePath);
            if (sourceFile.isDirectory()) {
                for (File listFile : sourceFile.listFiles()) {
                    delete(listFile.getAbsolutePath());
                }
            }
            return sourceFile.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
