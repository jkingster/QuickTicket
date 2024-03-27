package io.jacobking.quickticket.gui.model.impl;

import io.jacobking.quickticket.core.image.ImageBlob;
import io.jacobking.quickticket.gui.model.ViewModel;
import io.jacobking.quickticket.tables.pojos.ProfilePicture;
import javafx.beans.property.*;


public class ProfilePictureModel extends ViewModel<ProfilePicture> {

    private final StringProperty            imageName          = new SimpleStringProperty();
    private final ObjectProperty<ImageBlob> imageProperty      = new SimpleObjectProperty<>();
    private final IntegerProperty           employeeIdProperty = new SimpleIntegerProperty();


    public ProfilePictureModel(int id, String imageName, ImageBlob imageBlob, int employeeId) {
        super(id);
        this.imageName.setValue(imageName);
        this.imageProperty.setValue(imageBlob);
        this.employeeIdProperty.setValue(employeeId);
    }

    public ProfilePictureModel(final ProfilePicture profilePicture) {
        this(
                profilePicture.getId(),
                profilePicture.getImageName(),
                new ImageBlob(profilePicture.getImage()),
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

    public ImageBlob getImageProperty() {
        return imageProperty.get();
    }

    public ObjectProperty<ImageBlob> imageProperty() {
        return imageProperty;
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
                getImageProperty().getBlob(),
                getEmployeeIdProperty()
        );
    }


}
