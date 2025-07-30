package kc.framework.security;

import java.nio.charset.StandardCharsets;

import kc.framework.extension.StringExtensions;

@lombok.extern.slf4j.Slf4j
public final class Base64Provider {
    private Base64Provider() {
    }

    /**
     * 将其它编码的字符串转换成Base64编码的字符串
     *
     * @param source 要转换的字符串
     * @return String
     */
    public static String EncodeString(String source) {
        // 如果字符串为空或者长度为0则抛出异常
        if (StringExtensions.isNullOrEmpty(source))
            return null;

        // 将字符串转换成UTF-8编码的字节数组
        byte[] buffer = source.getBytes(StandardCharsets.UTF_8);
        return Encode(buffer);
    }

    /**
     * 将Base64编码的字符串转换成其它编码的字符串
     *
     * @param source 要转换的Base64编码的字符串
     * @return String
     */
    public static String DecodeString(String source) {

        // 如果字符串为空或者长度为0则抛出异常
        if (StringExtensions.isNullOrEmpty(source))
            return null;

        // 将字符串转换成Base64编码的字节数组
        byte[] buffer = Decode(source);
        if (buffer == null)
            return null;
        // 将字节数组转换成UTF-8编码的字符串
        return new String(buffer, StandardCharsets.UTF_8);
    }

    /**
     * 将其它编码的Byte[]转换成Base64编码的字符串
     *
     * @param buffer 字节数组
     * @return String
     */
    public static String Encode(byte[] buffer) {
        //org.apache.commons.codec.binary.Base64 base64 = new org.apache.commons.codec.binary.Base64(true);//url安全的Base64编码
        //byte[] result = base64.encode(buffer);
        //return new String(result, "UTF-8");

        java.util.Base64.Encoder encoder = java.util.Base64.getEncoder();
        // 将UTF-8编码的字节数组转换成Base64编码的字符串
        return encoder.encodeToString(buffer);
    }

    /**
     * 将Base64编码的字符串转换成其它编码的字节数组
     *
     * @param source 要转换的Base64编码的字符串
     * @return byte[]
     */
    public static byte[] Decode(String source) {
        // 如果字符串为空或者长度为0则抛出异常
        if (StringExtensions.isNullOrEmpty(source))
            return null;

        //org.apache.commons.codec.binary.Base64 base64 = org.apache.commons.codec.binary.new Base64(true);//url安全的Base64编码
        // 将字符串转换成Base64编码的字节数组
        //return base64.decode(source);

        java.util.Base64.Decoder decoder = java.util.Base64.getDecoder();
        // 将字符串转换成Base64编码的字节数组
        return decoder.decode(source);
    }

}
