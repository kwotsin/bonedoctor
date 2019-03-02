package uk.ac.cam.cl.bravo.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import uk.ac.cam.cl.bravo.pipeline.MainPipeline;

import java.io.File;
import java.io.IOException;

public class DatasetUploaderController {
    private File chosenImage;
    protected Stage stage;
    protected MainPipeline pipeline;

    public DatasetUploaderController(Stage stage, MainPipeline pipeline){
        this.stage = stage;
        this.pipeline = pipeline;
    }

    @FXML
    Button uploadImageButton;

    @FXML
    ComboBox<String> bodyPartListDropdown;

    @FXML
    ImageView uploadedImageView;

    @FXML
    private void handleUploadButton(ActionEvent event){
        // Build dialog for choosing file, filtering only png and jpeg files
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter[]{new FileChooser.ExtensionFilter("Image Files", new String[]{"*.png", "*.jpg"})});
        fileChooser.setTitle("Open Image File");

        // Set the image chosen
        chosenImage = fileChooser.showOpenDialog(stage);

        // Display the image chosen
        uploadedImageView.setImage(new Image(chosenImage.toURI().toString()));

    }
}
