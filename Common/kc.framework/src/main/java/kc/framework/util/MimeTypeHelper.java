package kc.framework.util;

import java.util.Arrays;
import java.util.List;

import kc.framework.enums.*;

public class MimeTypeHelper {

    /**
     * 根据DocFormat类型，获取Http能解析的MineType
     *
     * @param format DocFormat类型
     * @return String
     */
    public static String GetMineType(DocFormat format) {
        String result = "";
        switch (format) {
            case Doc:
                result = "application/msword";
                break;
            case Docx:
                result = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
                break;
            case Xls:
                result = "application/vnd.ms-excel";
                break;
            case Xlsx:
                result = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                break;
            case Ppt:
                result = "application/vnd.ms-powerpoint";
                break;
            case Pptx:
                result = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
                break;
            case Pdf:
                result = "application/pdf";
                break;
            case Text:
                result = "text/plain";
                break;
            case Xml:
                result = "text/xml";
                break;
            //图片
            case Bmp:
                result = "image/bmp";
                break;
            case Gif:
                result = "image/gif";
                break;
            case Icon:
                result = "image/vnd.microsoft.icon";
                break;
            case Jpeg:
                result = "image/jpeg";
                break;
            case Png:
                result = "image/png";
                break;
            case Tiff:
                result = "image/tiff";
                break;
            case Wmf:
                result = "image/wmf";
                break;
            case Dwg:
                result = "image/vnd.dwg";
                break;
            //压缩
            case Rar:
                result = "application/x-rar-compressed";
                break;
            case Zip:
                result = "application/zip";
                break;
            case Gzip:
                result = "application/x-gzip";
                break;
            // 语音
            case Basic:
                result = "audio/basic";
                break;
            case Wav:
                result = "audio/x-wav";
                break;
            case Mpeg:
                result = "audio/mpeg";
                break;
            case Ram:
                result = "audio/x-pn-realaudio";
                break;
            case Rmi:
                result = "audio/mid";
                break;
            case Aif:
                result = "audio/x-aiff";
                break;

            // 视频
            case Wmv:
                result = "video/x-ms-wmv";
                break;
            case Mp4:
                result = "video/mp4";
                break;
            case Flv:
                result = "video/x-flv";
                break;
            case Avi:
                result = "video/x-msvideo";
                break;
            case Mov:
                result = "video/quicktime";
                break;

            default:
                result = "application/unknown";
                break;
        }

        return result;
    }

    /**
     * 根据ImageFormat类型，获取Http能解析的MineType
     *
     * @param format ImageFormat类型
     * @return String
     */
    public static String GetImageMineType(ImageFormat format) {
        String result = "";
        switch (format) {
            case Bmp:
                result = "image/bmp";
                break;
            case Gif:
                result = "image/gif";
                break;
            case Icon:
                result = "image/vnd.microsoft.icon";
                break;
            case Jpeg:
                result = "image/jpeg";
                break;
            case Png:
                result = "image/png";
                break;
            case Tiff:
                result = "image/tiff";
                break;
            case Wmf:
                result = "image/wmf";
                break;
            case Dwg:
                result = "image/vnd.dwg";
                break;
            default:
                result = "application/unknown";
                break;
        }

        return result;
    }

    /**
     * 根据AudioFormat类型，获取Http能解析的MineType
     *
     * @param format AudioFormat类型
     * @return String
     */
    public static String GetAudioMineType(AudioFormat format) {
        String result = "";
        switch (format) {
            // 语音
            case Basic:
                result = "audio/basic";
                break;
            case Wav:
                result = "audio/x-wav";
                break;
            case Mpeg:
                result = "audio/mpeg";
                break;
            case Ram:
                result = "audio/x-pn-realaudio";
                break;
            case Rmi:
                result = "audio/mid";
                break;
            case Aif:
                result = "audio/x-aiff";
                break;
            default:
                result = "application/unknown";
                break;
        }

        return result;
    }

    /**
     * 根据VideoFormat类型，获取Http能解析的MineType
     *
     * @param format VideoFormat类型
     * @return String
     */
    public static String GetVideoMineType(VideoFormat format) {
        String result = "";
        switch (format) {
            // 视频
            case Wmv:
                result = "video/x-ms-wmv";
                break;
            case Mp4:
                result = "video/mp4";
                break;
            case Flv:
                result = "video/x-flv";
                break;
            case Avi:
                result = "video/x-msvideo";
                break;
            case Mov:
                result = "video/quicktime";
                break;
            default:
                result = "application/unknown";
                break;
        }

        return result;
    }


    /**
     * 根据ContentType类型，获取ImageFormat
     *
     * @param contentType ContentType类型
     * @return ImageFormat
     */
    public static ImageFormat GetImageFormatByContentType(String contentType) {
        ImageFormat result = ImageFormat.Unknown;
        switch (contentType.toLowerCase()) {
            case "image/bmp":
                result = ImageFormat.Bmp;
                break;
            case "image/gif":
                result = ImageFormat.Gif;
                break;
            case "image/vnd.microsoft.icon":
                result = ImageFormat.Icon;
                break;
            case "image/jpeg":
                result = ImageFormat.Jpeg;
                break;
            case "image/png":
                result = ImageFormat.Png;
                break;
            case "image/tiff":
                result = ImageFormat.Tiff;
                break;
            case "image/wmf":
                result = ImageFormat.Wmf;
                break;
            case "image/vnd.dwg":
                result = ImageFormat.Dwg;
                break;
            default:
                result = ImageFormat.Unknown;
                break;
        }

        return result;
    }

    /**
     * 根据文件扩展名，获取ImageFormat
     *
     * @param extensionStr 文件扩展名
     * @return ImageFormat
     */
    public static ImageFormat GetImageFormatByExt(String extensionStr) {
        // String[] Image = new String[] { "jpg", "jpeg", "png", "gif", "bmp",
        // "JPEG2000", "TIFF", "PSD", "PNG", "SVG", "PCX", "DXF", "WMF" };
        ImageFormat result = ImageFormat.Unknown;
        switch (extensionStr.toLowerCase()) {
            case "bmp":
                result = ImageFormat.Bmp;
                break;
            case "gif":
                result = ImageFormat.Gif;
                break;
            case "jpg":
            case "jpeg":
            case "JPEG2000":
                result = ImageFormat.Jpeg;
                break;
            case "png":
                result = ImageFormat.Png;
                break;
            case "TIFF":
                result = ImageFormat.Tiff;
                break;
            case "wmf":
                result = ImageFormat.Wmf;
                break;
            case "dwg":
                result = ImageFormat.Dwg;
                break;
            default:
                result = ImageFormat.Unknown;
                break;
        }

        return result;
    }

    /**
     * 根据ContentType类型，获取DocFormat
     *
     * @param contentType ContentType类型
     * @return DocFormat
     */
    public static DocFormat GetDocFormatByContentType(String contentType) {
        DocFormat result = DocFormat.Unknown;
        switch (contentType.toLowerCase()) {
            //文档格式
            case "application/msword":
                result = DocFormat.Doc;
                break;
            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document":
                result = DocFormat.Docx;
                break;
            case "application/vnd.ms-excel":
                result = DocFormat.Xls;
                break;
            case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet":
                result = DocFormat.Xlsx;
                break;
            case "application/vnd.ms-powerpoint":
                result = DocFormat.Ppt;
                break;
            case "application/vnd.openxmlformats-officedocument.presentationml.presentation":
                result = DocFormat.Pptx;
                break;
            case "application/pdf":
                result = DocFormat.Pdf;
                break;
            case "text/plain":
                result = DocFormat.Text;
                break;
            case "text/xml":
                result = DocFormat.Xml;
                break;

            //图片格式
            case "image/bmp":
                result = DocFormat.Bmp;
                break;
            case "image/gif":
                result = DocFormat.Gif;
                break;
            case "image/vnd.microsoft.icon":
                result = DocFormat.Icon;
                break;
            case "image/jpeg":
                result = DocFormat.Jpeg;
                break;
            case "image/png":
                result = DocFormat.Png;
                break;
            case "image/tiff":
                result = DocFormat.Tiff;
                break;
            case "image/wmf":
                result = DocFormat.Wmf;
                break;
            case "image/vnd.dwg":
                result = DocFormat.Dwg;
                break;
            //压缩格式
            case "application/x-rar-compressed":
                result = DocFormat.Rar;
                break;
            case "application/zip":
                result = DocFormat.Zip;
                break;
            case "application/x-gzip":
                result = DocFormat.Gzip;
                break;
            //音频格式
            case "audio/basic":
                result = DocFormat.Basic;
                break;
            case "audio/x-wav":
                result = DocFormat.Wav;
                break;
            case "audio/mpeg":
                result = DocFormat.Mpeg;
                break;
            case "audio/x-pn-realaudio":
                result = DocFormat.Ram;
                break;
            case "audio/mid":
                result = DocFormat.Rmi;
                break;
            case "audio/x-aiff":
                result = DocFormat.Aif;
                break;

            //视频格式
            case "video/x-ms-wmv":
                result = DocFormat.Wmv;
                break;
            case "video/quicktime":
                result = DocFormat.Mov;
                break;
            case "video/mp4":
                result = DocFormat.Mp4;
                break;
            case "video/x-msvideo":
                result = DocFormat.Avi;
                break;
            case "video/x-flv":
                result = DocFormat.Flv;
                break;
            default:
                result = DocFormat.Unknown;
                break;
        }
        return result;
    }

    /**
     * 根据文件扩展名，获取DocFormat
     *
     * @param extensionStr 文件扩展名
     * @return DocFormat
     */
    public static DocFormat GetDocFormatByExt(String extensionStr) {
        DocFormat result = DocFormat.Unknown;
        switch (extensionStr.toLowerCase()) {
            //文件格式
            case "doc":
                result = DocFormat.Doc;
                break;
            case "docx":
                result = DocFormat.Docx;
                break;
            case "xls":
                result = DocFormat.Xls;
                break;
            case "xlsx":
                result = DocFormat.Xlsx;
                break;
            case "ppt":
                result = DocFormat.Ppt;
                break;
            case "pptx":
                result = DocFormat.Pptx;
                break;
            case "pdf":
                result = DocFormat.Pdf;
                break;
            case "txt":
                result = DocFormat.Text;
                break;
            case "xml":
                result = DocFormat.Xml;
                break;

            //图片格式
            case "bmp":
                result = DocFormat.Bmp;
                break;
            case "gif":
                result = DocFormat.Gif;
                break;
            case "icon":
                result = DocFormat.Icon;
                break;
            case "jpg":
            case "jpeg":
            case "jpeg2000":
                result = DocFormat.Jpeg;
                break;
            case "png":
                result = DocFormat.Png;
                break;
            case "tiff":
                result = DocFormat.Tiff;
                break;
            case "wmf":
                result = DocFormat.Wmf;
                break;
            case "dwg":
                result = DocFormat.Dwg;
                break;
            //压缩格式
            case "rar":
                result = DocFormat.Rar;
                break;
            case "zip":
                result = DocFormat.Zip;
                break;
            case "gzip":
                result = DocFormat.Gzip;
                break;
            //音频格式
            case "au":
            case "snd":
                result = DocFormat.Basic;
                break;
            case "wav":
                result = DocFormat.Wav;
                break;
            case "ram":
                result = DocFormat.Ram;
                break;
            case "rmi":
                result = DocFormat.Rmi;
                break;
            case "mp3":
                result = DocFormat.Mpeg;
                break;
            case "aif":
            case "aifc":
            case "aiff":
                result = DocFormat.Aif;
                break;

            //视频格式
            case "flv":
                result = DocFormat.Flv;
                break;
            case "mp4":
                result = DocFormat.Mp4;
                break;
            case "mov":
            case "ra":
                result = DocFormat.Mov;
                break;
            case "avi":
            case "mid":
                result = DocFormat.Avi;
                break;
            case "wmv":
                result = DocFormat.Wmv;
                break;

            default:
                result = DocFormat.Unknown;
                break;
        }
        return result;
    }

    /**
     * 根据ContentType，获取AudioFormat
     *
     * @param contentType ContentType类型
     * @return AudioFormat
     */
    public static AudioFormat GetAudioFormatByContentType(String contentType) {
        AudioFormat result = AudioFormat.Unknown;
        switch (contentType.toLowerCase()) {
            case "audio/basic":
                result = AudioFormat.Basic;
                break;
            case "audio/x-wav":
                result = AudioFormat.Wav;
                break;
            case "audio/mpeg":
                result = AudioFormat.Mpeg;
                break;
            case "audio/x-pn-realaudio":
                result = AudioFormat.Ram;
                break;
            case "audio/mid":
                result = AudioFormat.Rmi;
                break;
            case "audio/x-aiff":
                result = AudioFormat.Aif;
                break;
            default:
                result = AudioFormat.Unknown;
                break;
        }

        return result;
    }

    /**
     * 根据ContentType，获取VideoFormat
     *
     * @param contentType ContentType类型
     * @return VideoFormat
     */
    public static VideoFormat GetVideoFormatByContentType(String contentType) {
        VideoFormat result = VideoFormat.Unknown;
        switch (contentType.toLowerCase()) {
            case "video/x-ms-wmv":
                result = VideoFormat.Wmv;
                break;
            case "video/quicktime":
                result = VideoFormat.Mov;
                break;
            case "video/mp4":
                result = VideoFormat.Mp4;
                break;
            case "video/x-msvideo":
                result = VideoFormat.Avi;
                break;
            case "video/x-flv":
                result = VideoFormat.Flv;
                break;
            default:
                result = VideoFormat.Unknown;
                break;
        }

        return result;
    }
    /**
     * 根据文件扩展名，获取AudioFormat
     *
     * @param extensionStr 文件扩展名
     * @return AudioFormat
     */
    public static AudioFormat GetAudioFormatByExt(String extensionStr) {
        // String[] Audio = new String[] { "au", "wav", "ram", "rmi", "mp3", "aif" };
        AudioFormat result = AudioFormat.Unknown;
        switch (extensionStr.toLowerCase()) {
            case "au":
            case "snd":
                result = AudioFormat.Basic;
                break;
            case "wav":
                result = AudioFormat.Wav;
                break;
            case "ram":
            case "ra":
                result = AudioFormat.Ram;
                break;
            case "rmi":
            case "mid":
                result = AudioFormat.Rmi;
                break;
            case "mp3":
                result = AudioFormat.Mpeg;
                break;
            case "aif":
            case "aifc":
            case "aiff":
                result = AudioFormat.Aif;
                break;
            default:
                result = AudioFormat.Unknown;
                break;
        }

        return result;
    }
    /**
     * 根据文件扩展名，获取VideoFormat
     *
     * @param extensionStr 文件扩展名
     * @return VideoFormat
     */
    public static VideoFormat GetVideoFormatByExt(String extensionStr) {
        // String[] Audio = new String[] { "au", "wav", "ram", "rmi", "mp3", "aif" };
        VideoFormat result = VideoFormat.Unknown;
        switch (extensionStr.toLowerCase()) {
            case "flv":
                result = VideoFormat.Flv;
                break;
            case "mp4":
                result = VideoFormat.Mp4;
                break;
            case "mov":
            case "ra":
                result = VideoFormat.Mov;
                break;
            case "avi":
            case "mid":
                result = VideoFormat.Avi;
                break;
            case "wmv":
                result = VideoFormat.Wmv;
                break;
            default:
                result = VideoFormat.Unknown;
                break;
        }

        return result;
    }
    /**
     * 根据contentType，获取文档类型
     *
     * @param contentType 文件扩展名
     * @return VideoFormat
     */
    public static FileType GetFileTypeByContentType(String contentType) {
        FileType result = FileType.Unknown;
        switch (contentType.toLowerCase()) {
            case "application/msword":
            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document":
                result = FileType.Word;
                break;
            case "application/vnd.ms-excel":
                result = FileType.Excel;
                break;
            case "application/vnd.ms-powerpoint":
            case "application/vnd.openxmlformats-officedocument.presentationml.presentation":
                result = FileType.PPT;
                break;
            case "application/pdf":
                result = FileType.PDF;
                break;
            case "image/bmp":
            case "image/gif":
            case "image/vnd.microsoft.icon":
            case "image/jpeg":
            case "image/png":
            case "image/tiff":
            case "image/wmf":
            case "image/vnd.dwg":
                result = FileType.Image;
                break;
            case "text/plain":
                result = FileType.Text;
                break;
            case "text/xml":
                result = FileType.Xml;
                break;
            case "application/zip":
            case "application/x-gzip":
            case "application/x-rar-compressed":
                result = FileType.Zip;
            case "audio/basic":
            case "audio/x-wav":
            case "audio/mpeg":
            case "audio/x-pn-realaudio":
            case "audio/mid":
            case "audio/x-aiff":
                result = FileType.Audio;
                break;
            case "video/x-ms-wmv":
            case "video/mp4":
            case "video/x-msvideo":
            case "video/x-flv":
            case "video/quicktime":
                result = FileType.Video;
                break;
            default:
                result = FileType.Unknown;
                break;
        }

        return result;
    }
    /**
     * 根据文件扩展名，获取文档类型
     *
     * @param extensionStr 文件扩展名
     * @return VideoFormat
     */
    public static FileType GetFileType(String extensionStr) {
        List<String> Image = Arrays.asList(new String[]{"jpg", "jpeg", "png", "gif", "bmp", "JPEG2000", "TIFF", "PSD",
                "PNG", "SVG", "PCX", "DXF", "WMF","DWG"});
        List<String> Word = Arrays.asList(new String[]{"doc", "docx", "rtf"});
        List<String> Excel = Arrays.asList(new String[]{"xls", "xlsx", "xlsm", "xltm"});
        List<String> PDF = Arrays.asList(new String[]{"pdf"});
        List<String> PPT = Arrays.asList(new String[]{"ppt", "pptx", "ppsx", "ppsm"});
        List<String> Text = Arrays.asList(new String[]{"txt"});
        List<String> Xml = Arrays.asList(new String[]{"xml"});
        List<String> Zip = Arrays.asList (new String[]{"zip", "rar", "gzip" });
        List<String> Audio = Arrays.asList(new String[]{"au", "wav", "ram", "rmi", "mp3", "aif"});
        List<String> Video = Arrays.asList(new String[]{"wmv", "mp4", "avi", "flv", "mov"});
        if (Image.contains(extensionStr)) {
            return FileType.Image;
        }
        if (Word.contains(extensionStr)) {
            return FileType.Word;
        }
        if (Excel.contains(extensionStr)) {
            return FileType.Excel;
        }
        if (PDF.contains(extensionStr)) {
            return FileType.PDF;
        }
        if (PPT.contains(extensionStr)) {
            return FileType.PPT;
        }
        if (Text.contains(extensionStr)) {
            return FileType.Text;
        }
        if (Xml.contains(extensionStr)) {
            return FileType.Xml;
        }
        if (Audio.contains(extensionStr)) {
            return FileType.Audio;
        }
        if (Video.contains(extensionStr)) {
            return FileType.Video;
        }
        if (Zip.contains(extensionStr))
        {
            return FileType.Zip;
        }
        return FileType.Unknown;
    }
    /**
     * 根据文件扩展名，获取文档格式
     *
     * @param extensionStr 文件扩展名
     * @return VideoFormat
     */
    public static String GetFileFormatByExt(String extensionStr)
    {
        FileType type = GetFileType(extensionStr);
        return getFormatString(type, GetImageFormatByExt(extensionStr), GetAudioFormatByExt(extensionStr), GetVideoFormatByExt(extensionStr), GetDocFormatByExt(extensionStr));
    }
    /**
     * 根据contentType，获取文档格式
     *
     * @param contentType 文件扩展名
     * @return VideoFormat
     */
    public static String GetFileFormatByContentType(String contentType)
    {
        FileType type = GetFileTypeByContentType(contentType);
        return getFormatString(type, GetImageFormatByContentType(contentType), GetAudioFormatByContentType(contentType), GetVideoFormatByContentType(contentType), GetDocFormatByContentType(contentType));
    }

    private static String getFormatString(FileType type, ImageFormat imageFormat, AudioFormat audioFormat, VideoFormat videoFormat, DocFormat docFormat) {
        if (type == FileType.Image) {
            return imageFormat.toString();
        } else if(type == FileType.Audio) {
            return audioFormat.toString();
        } else if(type == FileType.Video) {
            return videoFormat.toString();
        } else if(type == FileType.Word
                || type == FileType.Excel
                || type == FileType.PDF
                || type == FileType.PPT
                || type == FileType.Text
                || type == FileType.Xml
                || type == FileType.Zip ) {
            return docFormat.toString();
        } else {
            return "Unknown";
        }
    }
}
