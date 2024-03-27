package io.jacobking.quickticket.core.image;

import io.jacobking.quickticket.gui.alert.Alerts;
import javafx.scene.image.Image;
import org.apache.commons.io.FileUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class ImageBlob {

    private byte[] blob;

    public ImageBlob(final File file) {
        this.blob = convertFileToBlob(file);
    }

    public ImageBlob(final byte[] blob) {
        this.blob = blob;
    }

    public Optional<Image> readBlobAsImage() {
        if (blob.length == 0)
            return Optional.empty();
        return Optional.of(new Image(new ByteArrayInputStream(blob)));
    }

    private byte[] convertFileToBlob(final File file) {
        try {
            return FileUtils.readFileToByteArray(file);
        } catch (IOException e) {
            Alerts.showException("Failed to convert file to blob.", e.fillInStackTrace());
            return new byte[0];
        }
    }

    public byte[] getBlob() {
        return blob;
    }
}
