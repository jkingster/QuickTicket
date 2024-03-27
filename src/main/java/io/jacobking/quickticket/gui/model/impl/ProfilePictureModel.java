package io.jacobking.quickticket.gui.model.impl;

import io.jacobking.quickticket.core.image.ImageBlob;
import io.jacobking.quickticket.gui.model.ViewModel;
import io.jacobking.quickticket.tables.pojos.ProfilePicture;
import javafx.beans.property.*;

import java.awt.image.BufferedImage;


public class ProfilePictureModel extends ViewModel<ProfilePicture> {

    private final StringProperty                imageName             = new SimpleStringProperty();
    private final ObjectProperty<BufferedImage> bufferedImageProperty = new SimpleObjectProperty<>();
    private final IntegerProperty               employeeIdProperty    = new SimpleIntegerProperty();


    public ProfilePictureModel(int id, String imageName, byte[] blob, int employeeId) {
        super(id);
        this.imageName.setValue(imageName);
        this.employeeIdProperty.setValue(employeeId);
        ImageBlob.readImage(blob).ifPresent(bufferedImageProperty::setValue);
    }

    public ProfilePictureModel(final ProfilePicture profilePicture) {
        this(
                profilePicture.getId(),
                profilePicture.getImageName(),
                profilePicture.getImage(),
                profilePicture.getEmployeeId()
        );
    }

    public String getImageName() {
        return imageName.get();
    }

    public StringProperty imageNameProperty() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName.set(imageName);
    }

    public BufferedImage getBufferedImageProperty() {
        return bufferedImageProperty.get();
    }

    public ObjectProperty<BufferedImage> bufferedImageProperty() {
        return bufferedImageProperty;
    }

    public void setBufferedImageProperty(BufferedImage bufferedImageProperty) {
        this.bufferedImageProperty.set(bufferedImageProperty);
    }

    public int getEmployeeIdProperty() {
        return employeeIdProperty.get();
    }

    public IntegerProperty employeeIdProperty() {
        return employeeIdProperty;
    }

    public void setEmployeeIdProperty(int employeeIdProperty) {
        this.employeeIdProperty.set(employeeIdProperty);
    }

    @Override public ProfilePicture toEntity() {
        return new ProfilePicture(
                getId(),
                getImageName(),
                ImageBlob.bufferedImageToBlob(getBufferedImageProperty()),
                getEmployeeIdProperty()
        );
    }


}
