package io.jacobking.quickticket.core.image;

import io.jacobking.quickticket.gui.alert.Alerts;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;

public class ImageBlob {

    private BufferedImage image;

    private ImageBlob() {

    }

    public static Optional<BufferedImage> readImage(final byte[] blob) {
        final ImageBlob imageBlob = new ImageBlob();
        imageBlob.readBlob(blob);
        return imageBlob.getImage();
    }

    public static byte[] bufferedImageToBlob(final BufferedImage bufferedImage) {
        try {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            Alerts.showException("Failed to convert image back to blob.", e.fillInStackTrace());
            return new byte[0];
        }
    }

    public void readBlob(final byte[] byteBlob) {
        if (byteBlob.length == 0)
            return;

        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteBlob);
        try {
            this.image = ImageIO.read(byteArrayInputStream);
        } catch (IOException e) {
            Alerts.showException("Failed to read image blob.", e.fillInStackTrace());
        }
    }

    public Optional<BufferedImage> getImage() {
        return (image == null) ? Optional.empty() : Optional.of(image);
    }


}
