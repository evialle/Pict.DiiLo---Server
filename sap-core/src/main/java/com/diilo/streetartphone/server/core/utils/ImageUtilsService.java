/**
 * 
 */
package com.diilo.streetartphone.server.core.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author Eric
 */
public interface ImageUtilsService {
	public void scale(final File pictureFile, final int destWidth, final int destHeight, final File destinationFile)
			throws IOException;

	boolean isVertical(final BufferedImage image);

	void produceThumb(final BufferedImage pictureBuffImg, final File destinationFile) throws IOException;

	void produceThumb(final BufferedImage pictureBuffImg, final File destinationFile, boolean videoIcon)
			throws IOException;

	void produceLowQuality(final BufferedImage pictureBuffImg, final File destinationFile) throws IOException;

	BufferedImage rotate(BufferedImage img, int angle);
}
