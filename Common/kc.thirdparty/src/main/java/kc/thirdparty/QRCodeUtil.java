package kc.thirdparty;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import kc.framework.extension.StringExtensions;

/**
 * 二维码操作类
 * 
 * @author tianc
 *
 */
public class QRCodeUtil {
	/**
	 * 指定编码
	 */
	private static final String CHARSET = "UTF-8";

	/**
	 * 指定二维码类型
	 */
	private static final String FORMAT = "PNG";

	/**
	 * 指定二维码尺寸
	 */
	private static final int QRCODE_SIZE = 300;

	/**
	 * 指定LOGO宽度
	 */
	private static final int LOGO_WIDTH = 60;

	/**
	 * 指定LOGO高度
	 */
	private static final int LOGO_HEIGHT = 60;

	/**
	 * 生成二维码
	 * 
	 * @param content 二维码内容
	 * @param size    图片大小
	 * @return BufferedImage 图片
	 * @throws Exception
	 */
	public static BufferedImage encode(String content, int size) throws Exception {
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
		hints.put(EncodeHintType.MARGIN, 1);

		int width = size <= 0 ? QRCODE_SIZE : size;
		int height =  size <= 0 ? QRCODE_SIZE : size;
		BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height,
				hints);
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
			}
		}

		return image;
	}

	/**
	 * 生成二维码
	 * 
	 * @param content      二维码内容
	 * @param logoPath     LOGO地址，为空则不指定logo
	 * @param needCompress 是否需要压缩
	 * @return BufferedImage 图片
	 * @throws Exception
	 */
	public static BufferedImage encode(String content, String logoPath, boolean needCompress) throws Exception {
		BufferedImage image = encode(content, 0);
		if (StringExtensions.isNullOrEmpty(logoPath)) {
			return image;
		}
		// 插入图片
		insertImage(image, logoPath, needCompress);
		return image;
	}

	/**
	 * 生成二维码(可指定内嵌LOGO)可指定二维码文件名
	 * 
	 * @param content      内容
	 * @param logoPath     LOGO地址，为空则不指定logo
	 * @param qrcodePath   存放目录
	 * @param needCompress 是否压缩LOGO
	 * @return
	 * @throws Exception
	 */
	public static boolean encode(String content, String logoPath, String qrcodePath, boolean needCompress) throws Exception{
		if (StringExtensions.isNullOrEmpty(qrcodePath)) {
			throw new Exception("Missing qrcodePath " + qrcodePath);
		}
		
		BufferedImage image = encode(content, logoPath, needCompress);
		mkdirs(qrcodePath);

		ImageIO.write(image, FORMAT, new File(qrcodePath));
		return true;
	}

	/**
	 * 解析二维码
	 * 
	 * @param image 二维码图片
	 * @return String 二维码内容
	 * @throws Exception
	 */
	public static String decode(BufferedImage image) throws Exception {
		if (image == null) {
			return null;
		}
		BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
		Result result;
		Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();
		hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
		result = new MultiFormatReader().decode(bitmap, hints);
		String resultStr = result.getText();
		return resultStr;
	}

	/**
	 * 解析二维码
	 * 
	 * @param file 二维码图片
	 * @return String 二维码内容
	 * @throws Exception
	 */
	public static String decode(File file) throws Exception {
		BufferedImage image;
		image = ImageIO.read(file);
		if (image == null) {
			return null;
		}
		return decode(image);
	}

	/**
	 * 解析二维码
	 * 
	 * @param path 二维码路径
	 * @return 二维码内容
	 * @throws Exception
	 */
	public static String decode(String path) throws Exception {
		return decode(new File(path));
	}

	/**
	 * 插入LOGO
	 * 
	 * @param source       二维码图片
	 * @param logoPath     LOGO图片地址
	 * @param needCompress 是否压缩
	 * @throws Exception
	 */
	private static void insertImage(BufferedImage source, String logoPath, boolean needCompress) throws Exception {
		File file = new File(logoPath);
		if (!file.exists()) {
			throw new Exception("logo file not found.");
		}
		Image logo = ImageIO.read(new File(logoPath));
		int logoWidth = logo.getWidth(null);
		int logoHeight = logo.getHeight(null);
		if (needCompress) { // 压缩LOGO
			if (logoWidth > LOGO_WIDTH) {
				logoWidth = LOGO_WIDTH;
			}
			if (logoHeight > LOGO_HEIGHT) {
				logoHeight = LOGO_HEIGHT;
			}
			Image image = logo.getScaledInstance(logoWidth, logoHeight, Image.SCALE_SMOOTH);
			BufferedImage tag = new BufferedImage(logoWidth, logoHeight, BufferedImage.TYPE_INT_RGB);
			Graphics g = tag.getGraphics();
			g.drawImage(image, 0, 0, null); // 绘制缩小后的图
			g.dispose();
			logo = image;
		}
		// 插入LOGO
		Graphics2D graph = source.createGraphics();
		int x = (QRCODE_SIZE - logoWidth) / 2;
		int y = (QRCODE_SIZE - logoHeight) / 2;
		graph.drawImage(logo, x, y, logoWidth, logoHeight, null);
		Shape shape = new RoundRectangle2D.Float(x, y, logoWidth, logoWidth, 6, 6);
		graph.setStroke(new BasicStroke(3f));
		graph.draw(shape);
		graph.dispose();
	}

	/**
	 * 当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir． (mkdir如果父目录不存在则会抛出异常)
	 * 
	 * @param destPath 存放目录
	 */
	private static void mkdirs(String destPath) {
		File file = new File(destPath);
		if (!file.exists() && !file.isDirectory()) {
			file.mkdirs();
		}
	}

}