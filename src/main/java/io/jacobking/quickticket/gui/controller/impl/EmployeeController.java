package io.jacobking.quickticket.gui.controller.impl;

import io.jacobking.quickticket.core.image.ImageBlob;
import io.jacobking.quickticket.gui.controller.Controller;
import io.jacobking.quickticket.gui.model.impl.EmployeeModel;
import io.jacobking.quickticket.tables.pojos.ProfilePicture;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import org.controlsfx.control.SearchableComboBox;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeController extends Controller {

    @FXML private SearchableComboBox<EmployeeModel> employeeComboBox;


    @FXML private ImageView profilePictureImage;
    @FXML private Button    changePhotoButton;

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        configureEmployeeComboBox();
        bindButtons();
    }

    private void configureEmployeeComboBox() {
        employeeComboBox.setItems(employee.getObservableList());
        employeeComboBox.setConverter(new StringConverter<>() {
            @Override public String toString(EmployeeModel employeeModel) {
                if (employeeModel == null)
                    return "";
                return String.format("%s", employeeModel.getFullName());
            }

            @Override public EmployeeModel fromString(String s) {
                return null;
            }
        });

        employeeComboBox.setCellFactory(data -> new ListCell<>() {
            @Override protected void updateItem(EmployeeModel employeeModel, boolean b) {
                super.updateItem(employeeModel, b);
                if (b || employeeModel == null) {
                    setText(null);
                    return;
                }

                setText(employeeModel.getFullName());
            }
        });
    }

    private void bindButtons() {
        changePhotoButton.disableProperty().bind(employeeComboBox.getSelectionModel()
                .selectedItemProperty().isNull());
    }

    @FXML private void onChangeProfilePicture() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose an image.");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG Files", "*.png"),
                new FileChooser.ExtensionFilter("JPG/JPEG Files", "*.jpg", "*.jpeg"),
                new FileChooser.ExtensionFilter("All formats", "*.png", "*.jpg", "*.jpeg")
        );

        final File file = fileChooser.showOpenDialog(employeeComboBox.getScene().getWindow());
        if (file != null) {
            final ImageBlob imageBlob = new ImageBlob(file);
            imageBlob.readBlobAsImage().ifPresent(image -> profilePictureImage.setImage(image));

            final EmployeeModel employeeModel = employeeComboBox.getSelectionModel().getSelectedItem();
            profilePicture.createModel(new ProfilePicture()
                    .setImageName(file.getName())
                    .setImage(imageBlob.getBlob())
                    .setEmployeeId(employeeModel.getId())
            );
        }
    }
}
