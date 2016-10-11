/**
 * 
 */
package com.diilo.streetartphone.server.core.utils.implementations;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.diilo.streetartphone.server.core.exceptions.TechnicalException;
import com.diilo.streetartphone.server.core.utils.VideoUtilsService;

/** @author Krishna Vangapandu **/
@Service
public class VideoUtilsImplService implements VideoUtilsService {

	private static final Logger	LOG		= LoggerFactory.getLogger(VideoUtilsImplService.class);

	/**
	 * Command like to create a thumbnail 1- ffmpeg path 2- source file path 3- target file path
	 */
	private final static String	COMMAND	= "%s -i %s -vframes 1 -f image2 %s";

	@Value("#{configCoreProperties['service.picture.ffmpeg.path']}")
	private String				FFMPEG;

	@Override
	public void createThumbnail(final File sourceFile, final File destinationFile) throws TechnicalException {

		String commandToExec = String.format(COMMAND, FFMPEG, sourceFile.getAbsolutePath(),
				destinationFile.getAbsolutePath());
		LOG.debug("Execution: " + commandToExec);
		executionCommandLine(commandToExec);

		if (destinationFile.exists()) {
			correctRotation(destinationFile);
		} else {
			throw new TechnicalException("The thumbnail of video file: " + sourceFile.getAbsolutePath()
					+ " can't be created");
		}
	}

	private void correctRotation(File fileToCorrect) {
		try {
			BufferedImage img = ImageIO.read(fileToCorrect);
			rotate(img, 270);
			ImageIO.write(img, "JPG", fileToCorrect);
		} catch (IOException e) {
			LOG.error("The file " + fileToCorrect.getAbsolutePath() + " can't be rotated");
		}
	}

	private static BufferedImage rotate(BufferedImage img, int angle) {
		int w = img.getWidth();
		int h = img.getHeight();
		BufferedImage dimg = new BufferedImage(w, h, img.getType());
		Graphics2D g = dimg.createGraphics();
		g.rotate(Math.toRadians(angle), w / 2., h / 2.);
		g.drawImage(img, null, 0, 0);
		return dimg;
	}

	private String executionCommandLine(final String cmd) {

		StringBuilder returnContent = new StringBuilder();
		Process pr;
		try {
			Runtime rt = Runtime.getRuntime();
			pr = rt.exec(cmd);

			BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));

			String line = null;

			while ((line = input.readLine()) != null) {
				returnContent.append(line);
			}
			input.close();
			LOG.debug(returnContent.toString());

			// return the exit code
			pr.waitFor();
		} catch (IOException e) {
			LOG.error(e.getMessage());
			returnContent = new StringBuilder();
		} catch (InterruptedException e) {
			LOG.error(e.getMessage());
			returnContent = new StringBuilder();
		}

		return returnContent.toString();

	}

}