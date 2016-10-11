package com.diilo.streetartphone.server.core.utils.implementations;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.diilo.streetartphone.server.core.utils.ImageUtilsService;
import com.jhlabs.image.GaussianFilter;

@Service
public class ImageUtilsImplService implements ImageUtilsService {

	private static final Logger		LOG							= LoggerFactory.getLogger(ImageUtilsImplService.class);

	private static final float		LOW_JPEGCOMPRESSIONQUALITY	= 0.0577f;
	private static final int		GAUSSIAN_RADIUS				= 15;

	private static final int		IPHONE3GS_WIDTH				= 320;
	private static final int		IPHONE3GS_HEIGHT			= 480;

	private static BufferedImage	iconImg						= null;

	static {
		try {
			iconImg = ImageIO.read(ImageUtilsImplService.class.getResource("/img/play.jpg"));
		} catch (IOException e) {
			LOG.error("The play image can't be opened");
		}
	}

	/**
	 * Thumb height on the iPhone 3G/3GS screen in pixels. For a given thumb height of 80px, we put 6 pictures
	 * thumb per screen.
	 */
	private static final int		THUMB_SQUARE				= 80;

	private static final int		PLAY_ICON_X					= 55;
	private static final int		PLAY_ICON_Y					= 55;

	private final static int		BLUR_WIDTH					= IPHONE3GS_WIDTH - THUMB_SQUARE;
	private final static int		BLUR_HEIGHT					= (IPHONE3GS_HEIGHT / IPHONE3GS_WIDTH) * BLUR_WIDTH;
	private final static int		BLUR_Y						= (BLUR_HEIGHT / 3) - (BLUR_HEIGHT / THUMB_SQUARE / 2);

	public void scale(final File pictureFile, final int destWidth, final int destHeight, final File destinationFile)
			throws IOException {
		BufferedImage src = ImageIO.read(pictureFile);
		BufferedImage dest = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = dest.createGraphics();
		AffineTransform at = AffineTransform.getScaleInstance((double) destWidth / src.getWidth(), (double) destHeight
				/ src.getHeight());
		g.drawRenderedImage(src, at);
		ImageIO.write(dest, "JPG", destinationFile);
	}

	/**
	 * @param image
	 *            the image to analyse
	 * @return true, if the height of the given image is superior to its width
	 */
	public boolean isVertical(final BufferedImage image) {
		return (image.getHeight() > image.getWidth());
	}

	/**
	 * @param image
	 * @return a buffered image representing a small square of the give image
	 */
	private BufferedImage squareThumb(final BufferedImage image) {
		// Resize
		BufferedImage destRatio;
		boolean isVerticalImage = isVertical(image);
		AffineTransform at;
		double scaleRatio;

		int width, height;
		if (isVerticalImage) {
			scaleRatio = (double) THUMB_SQUARE / (double) image.getWidth();
			width = THUMB_SQUARE;
			height = (int) (((double) image.getHeight() / (double) image.getWidth()) * THUMB_SQUARE);
		} else {
			// TODO
			scaleRatio = (double) THUMB_SQUARE / (double) image.getHeight();
			height = THUMB_SQUARE;
			width = (int) (((double) image.getWidth() / (double) image.getHeight()) * THUMB_SQUARE);
		}
		// LOG.debug("scaleRatio: " + scaleRatio + " - destRatio: " + width + " x " + height);

		at = AffineTransform.getScaleInstance(scaleRatio, scaleRatio);
		destRatio = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = destRatio.createGraphics();
		g.drawRenderedImage(image, at);

		// Crop as a square at the middle
		BufferedImage cropImage;
		int x, y;
		if (isVerticalImage) {
			x = 0;
			y = (height - THUMB_SQUARE) / 2;
		} else {
			// TODO right location
			x = (width - THUMB_SQUARE) / 2;
			y = 0;
		}
		cropImage = destRatio.getSubimage(x, y, THUMB_SQUARE, THUMB_SQUARE);

		return cropImage;
	}

	/**
	 * @param image
	 * @return
	 */
	private BufferedImage blurThumb(final BufferedImage image) {
		BufferedImage dest = new BufferedImage(BLUR_WIDTH, BLUR_HEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = dest.createGraphics();
		AffineTransform at = AffineTransform.getScaleInstance((double) BLUR_WIDTH / (double) image.getWidth(),
				(double) BLUR_HEIGHT / (double) image.getHeight());
		g.drawRenderedImage(image, at);

		// Crop, Blur, and Alpha
		BufferedImage cropImage = dest.getSubimage(0, BLUR_Y, BLUR_WIDTH, THUMB_SQUARE);

		GaussianFilter gaussianFilter = new GaussianFilter(GAUSSIAN_RADIUS);
		gaussianFilter.filter(cropImage, cropImage);

		return cropImage;
	}

	/**
	 * The thumb is a smaller picture
	 */
	@Override
	public void produceThumb(final BufferedImage bufferedImage, final File destinationFile) throws IOException {
		produceThumb(bufferedImage, destinationFile, false);
	}

	@Override
	public BufferedImage rotate(BufferedImage img, int angle) {
		int w = img.getWidth();
		int h = img.getHeight();
		BufferedImage dimg = dimg = new BufferedImage(w, h, img.getType());
		Graphics2D g = dimg.createGraphics();
		g.rotate(Math.toRadians(angle), w / 2, h / 2);
		g.drawImage(img, null, 0, 0);
		return dimg;
	}

	@Override
	public void produceThumb(BufferedImage bufferedImage, File destinationFile, boolean videoIcon) throws IOException {
		// 1- Create the components of the image
		BufferedImage squareImage = squareThumb(bufferedImage);
		BufferedImage blurImage = blurThumb(bufferedImage);

		// 2- Create the final image
		BufferedImage dest = new BufferedImage(IPHONE3GS_WIDTH, THUMB_SQUARE, BufferedImage.TYPE_INT_RGB);
		dest.createGraphics().drawImage(squareImage, 0, 0, null);
		dest.createGraphics().drawImage(blurImage, THUMB_SQUARE, 0, null);

		// 3- Create the play icon image
		if (videoIcon) {
			dest.createGraphics().drawImage(iconImg, PLAY_ICON_X, PLAY_ICON_Y, null);
		}
		// 4- write it to disk
		ImageIO.write(dest, "JPG", destinationFile);
	}

	@Override
	public void produceLowQuality(final BufferedImage bufferedImage, final File destinationFile) throws IOException {

		Iterator<?> iter = ImageIO.getImageWritersByFormatName("jpeg");

		ImageWriter writer = (ImageWriter) iter.next();
		// instantiate an ImageWriteParam object with default compression options
		ImageWriteParam iwp = writer.getDefaultWriteParam();

		iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		iwp.setCompressionQuality(LOW_JPEGCOMPRESSIONQUALITY); // an integer between 0 and 1
		// 1 specifies minimum compression and maximum quality

		FileImageOutputStream output = new FileImageOutputStream(destinationFile);
		writer.setOutput(output);
		IIOImage image = new IIOImage(bufferedImage, null, null);
		writer.write(null, image, iwp);
		writer.dispose();
	}

}
