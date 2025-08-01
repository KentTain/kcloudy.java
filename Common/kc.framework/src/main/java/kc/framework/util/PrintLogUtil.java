package kc.framework.util;

public class PrintLogUtil {
    // 重置所有样式
    public static final String NC = "\033[0m";
    public static final String BOLD = "\033[1m";

    // 颜色
    public static final String RED = "\033[0;31m"; // 红色
    public static final String GREEN = "\033[0;32m"; // 绿色
    public static final String YELLOW = "\033[0;33m"; // 黄色
    public static final String BLUE = "\033[0;34m"; // 蓝色
    public static final String CYAN = "\033[0;36m"; // 青色
    public static final String WHITE = "\033[0;37m"; // 白色

    /**
     * 打印信息（蓝色）
     * 
     * @param message 需要打印的消息
     */
    public static void printInfo(String message) {
        System.out.println(BLUE + "💡[Info] " + message + NC);
    }

    /**
     * 打印成功（绿色）
     * 
     * @param message 需要打印的消息
     */
    public static void printSuccess(String message) {
        System.out.println(GREEN + "✅[Success] " + message + NC);
    }

    /**
     * 打印警告（黄色）
     * 
     * @param message 需要打印的消息
     */
    public static void printWarning(String message) {
        System.out.println(YELLOW + "⚠️[Warning] " + message + NC);
    }

    /**
     * 打印错误（红色）
     * 
     * @param message 需要打印的消息
     */
    public static void printError(String message) {
        System.out.println(RED + "❌[error] " + message + NC);
    }
}
