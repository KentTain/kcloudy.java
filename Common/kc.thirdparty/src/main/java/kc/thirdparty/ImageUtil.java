package kc.thirdparty;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import kc.framework.extension.StringExtensions;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

@lombok.extern.slf4j.Slf4j
public class ImageUtil {

	/**
	 * 默认图片大小
	 */
	private static final int DEFAULT_IMAGE_SIZE = 300;
	private static final double DEFAULT_SCALE = 0.25;
	private static final String DEFAULT_WATERMARK = "http://www.kcloudy.com";
	private static final String DEFAULT_FORMAT = "jpg";

	/**
	 * 将字节数组转换为BufferedImage对象
	 * 
	 * @param bytes 图片的字节数组
	 * @return BufferedImage
	 */
	public static BufferedImage fromBytes(byte[] bytes) {
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(bytes); // 将b作为输入流；
			return (BufferedImage) ImageIO.read(in); // 将in作为输入流，读取图片存入image中，而这里in可以为ByteArrayInputStream();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * 将BufferedImage转换为byte[]
	 * 
	 * @param BufferedImage 图片
	 * @return bytes[]
	 */
	public static byte[] toBytes(BufferedImage image) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ImageIO.write(image, "png", bos);
			return bos.toByteArray();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * 获取图片的BufferedImage
	 * 
	 * @param image 图片
	 * @return BufferedImage
	 * @throws IOException
	 */
	public static BufferedImage getBufferedImage(Image image) {
		// 获取 Image 对象的高度和宽度
		int width = image.getWidth(null);
		int height = image.getHeight(null);
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = bi.getGraphics();
		// 通过 BufferedImage 绘制图像并保存在其对象中
		g.drawImage(image, 0, 0, width, height, null);
		return bi;
	}

	/**
	 * 获取图片的BufferedImage
	 * 
	 * @param imagePath 图片地址
	 * @return BufferedImage
	 * @throws IOException
	 */
	public static BufferedImage getBufferedImage(String imagePath) {

		try {
			Image image = ImageIO.read(new File(imagePath));
			return (BufferedImage) image;
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * 按宽高，获取图片的缩略图
	 * 
	 * @param originalImage 源图片
	 * @param width         缩略图宽：为0时，默认为300
	 * @param height        缩略图高：为0时，默认为300
	 * @return BufferedImage
	 */
	public static BufferedImage getThumbnail(BufferedImage originalImage, int width, int height) {
		if (originalImage == null)
			return null;
		if (width <= 0) {
			width = DEFAULT_IMAGE_SIZE;
		}
		if (height <= 0) {
			height = DEFAULT_IMAGE_SIZE;
		}

		try {
			return Thumbnails.of(originalImage).size(width, height).asBufferedImage();
		} catch (IOException e) {
			log.error(e.getMessage(), e);

			return null;
		}
	}

	/**
	 * 按比例，获取图片的缩略图
	 * 
	 * @param originalImage 源图片
	 * @param scale         缩略比例：小于0时，默认为0.25
	 * @return BufferedImage
	 */
	public static BufferedImage getThumbnail(BufferedImage originalImage, double scale) {
		if (originalImage == null)
			return null;
		if (scale <= 0) {
			scale = DEFAULT_SCALE;
		}

		try {
			return Thumbnails.of(originalImage).scale(scale).asBufferedImage();
		} catch (IOException e) {
			log.error(e.getMessage(), e);

			return null;
		}
	}

	/**
	 * 按宽高，获取图片的缩略图
	 * 
	 * @param originalImagePath 源图片地址
	 * @param thumbnailPath     缩略图地址
	 * @param width             缩略图宽：为0时，默认为300
	 * @param height            缩略图高：为0时，默认为300
	 * @return boolean 是否成功
	 */
	public static boolean getThumbnail(String originalImagePath, String thumbnailPath, int width, int height) {
		if (StringExtensions.isNullOrEmpty(originalImagePath) || StringExtensions.isNullOrEmpty(thumbnailPath))
			return false;

		if (width <= 0) {
			width = DEFAULT_IMAGE_SIZE;
		}
		if (height <= 0) {
			height = DEFAULT_IMAGE_SIZE;
		}

		try {
			Thumbnails.of(originalImagePath).size(width, height).toFile(thumbnailPath);
			return true;
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			return false;
		}
	}

	/**
	 * 按比例，获取图片的缩略图
	 * 
	 * @param originalImagePath 源图片地址
	 * @param thumbnailPath     缩略图地址
	 * @param scale             缩略比例：小于0时，默认为0.25
	 * @return boolean 是否成功
	 */
	public static boolean getThumbnail(String originalImagePath, String thumbnailPath, double scale) {
		if (StringExtensions.isNullOrEmpty(originalImagePath) || StringExtensions.isNullOrEmpty(thumbnailPath))
			return false;

		if (scale <= 0) {
			scale = DEFAULT_SCALE;
		}

		try {
			Thumbnails.of(originalImagePath).scale(scale).toFile(thumbnailPath);
			return true;
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			return false;
		}
	}

	/**
	 * 获取代水印的图片（右下角，50%透明度）
	 * 
	 * @param originalImage   源图片地址
	 * @param targetImagePath 生成的目标图片地址
	 * @param watermarkImage  水印图片地址
	 * @return boolean 是否成功
	 */
	public static boolean getWatermark(String originalImagePath, String targetImagePath, String watermarkPath) {
		if (StringExtensions.isNullOrEmpty(originalImagePath) || StringExtensions.isNullOrEmpty(targetImagePath)
				|| StringExtensions.isNullOrEmpty(watermarkPath))
			return false;

		try {
			BufferedImage watermarkImage = ImageIO.read(new File(watermarkPath));
			if (watermarkImage == null)
				return false;

			BufferedImage resultImage = Thumbnails.of(originalImagePath).scale(1)
					.watermark(Positions.BOTTOM_RIGHT, watermarkImage, 0.5f).asBufferedImage();
			// 输出图片
			FileOutputStream outImgStream = new FileOutputStream(targetImagePath);
			ImageIO.write(resultImage, DEFAULT_FORMAT, outImgStream);
			System.out.println("添加水印完成");
			outImgStream.flush();
			outImgStream.close();

			return true;
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			return false;
		}
	}

	/**
	 * 获取代水印的图片（右下角，50%透明度）
	 * 
	 * @param originalImage  源图片
	 * @param watermarkImage 水印图片
	 * @return BufferedImage
	 */
	public static BufferedImage getWatermark(BufferedImage originalImage, BufferedImage watermarkImage) {

		if (originalImage == null || watermarkImage == null)
			return null;

		try {
			return Thumbnails.of(originalImage).watermark(Positions.BOTTOM_RIGHT, watermarkImage, 0.5f)
					.asBufferedImage();
		} catch (IOException e) {
			log.error(e.getMessage(), e);

			return null;
		}
	}

	/**
	 * 获取代水印的图片（右下角，50%透明度）
	 * 
	 * @param originalImagePath 源图片地址
	 * @param watermarkPath     水印图片地址
	 * @return BufferedImage
	 */
	public static BufferedImage getWatermark(String originalImagePath, String watermarkPath) {
		if (StringExtensions.isNullOrEmpty(originalImagePath) || StringExtensions.isNullOrEmpty(watermarkPath))
			return null;

		try {
			BufferedImage watermarkImage = ImageIO.read(new File(watermarkPath));
			if (watermarkImage == null)
				return null;

			return Thumbnails.of(originalImagePath).watermark(Positions.BOTTOM_RIGHT, watermarkImage, 0.5f)
					.asBufferedImage();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * 给图片加载文字水印（文字水印默认样式：Color.lightGray，new Font("微软雅黑", Font.PLAIN, 34) ）
	 * 
	 * @param originalImagePath 源图片路径
	 * @param targetImagePath   保存的图片路径
	 * @param waterMarkContent  水印内容（如为空，默认为：http://www.kcloudy.com）
	 * @return boolean 是否成功
	 */
	public static boolean addWatermark(String originalImagePath, String targetImagePath, String waterMarkContent) {

		try {
			BufferedImage resultImage = addWatermark(originalImagePath, waterMarkContent);

			// 输出图片
			FileOutputStream outImgStream = new FileOutputStream(targetImagePath);
			ImageIO.write(resultImage, DEFAULT_FORMAT, outImgStream);
			// System.out.println("添加水印完成");
			outImgStream.flush();
			outImgStream.close();

			return true;

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return false;
		}
	}

	/**
	 * 给图片加载文字水印（文字水印默认样式：Color.lightGray，new Font("微软雅黑", Font.PLAIN, 34) ）
	 * 
	 * @param originalImagePath 源图片路径
	 * @param waterMarkContent  水印内容（如为空，默认为：http://www.kcloudy.com）
	 * @return BufferedImage
	 */
	public static BufferedImage addWatermark(String originalImagePath, String waterMarkContent) {
		try {
			// 读取原图片信息
			File srcImgFile = new File(originalImagePath);// 得到文件
			Image srcImg = ImageIO.read(srcImgFile);// 文件转化为图片

			return addWatermark((BufferedImage) srcImg, waterMarkContent);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * 给图片加载文字水印（文字水印默认样式：Color.lightGray，new Font("微软雅黑", Font.PLAIN, 34) ）
	 * 
	 * @param originalImage    源图片
	 * @param waterMarkContent 水印内容（如为空，默认为：http://www.kcloudy.com）
	 * @return BufferedImage
	 */
	public static BufferedImage addWatermark(BufferedImage originalImage, String waterMarkContent) {
		if (originalImage == null)
			return null;

		if (StringExtensions.isNullOrEmpty(waterMarkContent))
			waterMarkContent = DEFAULT_WATERMARK;

		try {
			int srcImgWidth = originalImage.getWidth(null);// 获取图片的宽
			int srcImgHeight = originalImage.getHeight(null);// 获取图片的高
			// 加水印
			BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = bufImg.createGraphics();
			g.drawImage(originalImage, 0, 0, srcImgWidth, srcImgHeight, null);
			g.setColor(Color.lightGray); // 根据图片的背景设置水印颜色
			g.setFont(new Font("微软雅黑", Font.PLAIN, 34)); // 设置字体

			// 设置水印的坐标
			int x = srcImgWidth - 2 * getWatermarkLength(waterMarkContent, g);
			int y = srcImgHeight - 2 * getWatermarkLength(waterMarkContent, g);
			g.drawString(waterMarkContent, x, y); // 画出水印
			g.dispose();

			return bufImg;

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}

	private static int getWatermarkLength(String waterMarkContent, Graphics2D g) {
		return g.getFontMetrics(g.getFont()).charsWidth(waterMarkContent.toCharArray(), 0, waterMarkContent.length());
	}

	/**
	 * 插入LOGO
	 * 
	 * @param sourceImage     源图片
	 * @param targetImagePath 目标图片地址
	 * @param logoImage       LOGO图片
	 * @return BufferedImage
	 * @throws Exception
	 */
	public static BufferedImage insertImage(BufferedImage sourceImage, BufferedImage logoImage) {
		if (sourceImage == null || logoImage == null)
			return null;
		try {
			int logoWidth = logoImage.getWidth(null);
			int logoHeight = logoImage.getHeight(null);
			if (logoWidth > 60) {
				logoWidth = 60;
			}
			if (logoHeight > 60) {
				logoHeight = 60;
			}
			Image image = logoImage.getScaledInstance(logoWidth, logoHeight, Image.SCALE_SMOOTH);
			BufferedImage tag = new BufferedImage(logoWidth, logoHeight, BufferedImage.TYPE_INT_RGB);
			Graphics g = tag.getGraphics();
			g.drawImage(image, 0, 0, null); // 绘制缩小后的图
			g.dispose();

			// 插入LOGO
			Graphics2D graph = sourceImage.createGraphics();
			int sourceWidth = sourceImage.getWidth(null);
			int sourceHeight = sourceImage.getHeight(null);
			int x = (sourceWidth - logoWidth) / 2;
			int y = (sourceHeight - logoHeight) / 2;
			graph.drawImage(image, x, y, logoWidth, logoHeight, null);
			Shape shape = new RoundRectangle2D.Float(x, y, logoWidth, logoHeight, 6, 6);
			graph.setStroke(new BasicStroke(3f));
			graph.draw(shape);
			graph.dispose();

			return getBufferedImage(sourceImage);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			return null;
		}
	}

	/**
	 * 插入LOGO
	 * 
	 * @param sourceImagePath 源图片地址
	 * @param targetImagePath 目标图片地址
	 * @param logoImagePath   LOGO图片地址
	 * @return boolean 是否成功
	 * @throws Exception
	 */
	public static boolean insertImage(String sourceImagePath, String targetImagePath, String logoImagePath) {
		if (StringExtensions.isNullOrEmpty(targetImagePath) || StringExtensions.isNullOrEmpty(targetImagePath)
				|| StringExtensions.isNullOrEmpty(logoImagePath))
			return false;

		try {
			BufferedImage sourceImage = ImageIO.read(new File(sourceImagePath));
			BufferedImage logoImage = ImageIO.read(new File(logoImagePath));

			// 输出图片
			BufferedImage bufimg = insertImage(sourceImage, logoImage);
			FileOutputStream outImgStream = new FileOutputStream(targetImagePath);
			ImageIO.write(bufimg, DEFAULT_FORMAT, outImgStream);
			// System.out.println("添加水印完成");
			outImgStream.flush();
			outImgStream.close();

			return true;
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			return false;
		}
	}
}
