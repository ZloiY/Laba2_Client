package sample.window_process;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.SchemaViewer;
import sample.thrift.PatternModel;

import java.io.ByteArrayInputStream;
import java.io.File;

public class Window {
    private int patternID;
    private Label patternName;
    private Text patternDescription;
    private ImageView patternSchemaImage;
    private BorderPane borderPane;
    private TextField newPatternName;
    private TextArea newPatternDescription;
    private ImageView newPatternSchema;
    private PatternModel patternModel;
    private Button schemaViewer;
    private Button schemaChooseBtn;
    private Button applyBtn;
    private Button cnclBtn;
    private VBox vBox;
    private Button delBtn;
    private Button editBtn;
    private HBox delEditBox;

    public Window(PatternModel pattern){
        patternModel = pattern;
        patternID = pattern.id;
        patternName = new Label(pattern.name);
        patternDescription = new Text(pattern.description);
        patternDescription.setWrappingWidth(500);
        patternSchemaImage = new ImageView();
        if (pattern.schema != null) {
            Image image = new Image(new ByteArrayInputStream(pattern.schema.array()));
            patternSchemaImage.setImage(image);
        }
        newPatternName = new TextField();
        newPatternDescription = new TextArea();
        newPatternDescription.setWrapText(true);
        newPatternSchema = new ImageView();
        applyBtn = new Button("Add");
        cnclBtn = new Button("Cancel");
        editBtn = new Button("Edit");
        delBtn = new Button("Delete");
        schemaViewer = new Button("View schema");
        schemaViewer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                SchemaViewer schemaViewer = new SchemaViewer(patternSchemaImage);
                schemaViewer.showViewer();
            }
        });
        schemaChooseBtn = new Button("Choose schema");
        borderPane = new BorderPane();
        vBox = new VBox(10);
        delEditBox = new HBox(10);
        delEditBox.getChildren().addAll(editBtn, delBtn, schemaViewer);
        borderPane.setCenter(vBox);
        borderPane.setBottom(delEditBox);

    }

    public Window(){
        newPatternName = new TextField();
        newPatternDescription = new TextArea();
        newPatternSchema = new ImageView();
        schemaViewer = new Button("Schema viewer");
        schemaViewer.setOnAction(e->{
            if (newPatternSchema!=null) {
                SchemaViewer schemaViewer = new SchemaViewer(newPatternSchema);
                schemaViewer.showViewer();
            }
        });
        applyBtn = new Button("Add");
        cnclBtn = new Button("Cancel");
        schemaChooseBtn = new Button("Choose schema");
        borderPane = new BorderPane();
        vBox = new VBox(10);
        borderPane.setCenter(vBox);
    }

    public Label getPatternName() {
        return patternName;
    }

    public Text getPatternDescription() {
        return patternDescription;
    }

    public ImageView getPatternSchemaImage() {
        return patternSchemaImage;
    }

    public void setPatternID(int patternID) {
        this.patternID = patternID;
    }

    public void setNewPatternSchema(ImageView newPatternSchema) {
        this.newPatternSchema = newPatternSchema;
    }

    public TextField getNewPatternName() {
        return newPatternName;
    }

    public TextArea getNewPatternDescription() {
        return newPatternDescription;
    }

    public ImageView getNewPatternSchema() {
        return newPatternSchema;
    }

    public Button getApplyBtn() {
        return applyBtn;
    }

    public Button getCnclBtn() {
        return cnclBtn;
    }

    public Button getDelBtn() {
        return delBtn;
    }

    public Button getEditBtn() {
        return editBtn;
    }

    public int getPatternID(){return patternID;}

    public BorderPane getBorderPane() {
        return borderPane;
    }

    public void addName(){
        vBox.getChildren().add(patternName);
    }

    public void addDescription(){
        vBox.getChildren().add(patternDescription);
    }

    public void addSchema(){ vBox.getChildren().add(patternSchemaImage); }

    public void showLayout(){
        vBox.getChildren().clear();
        addName();
        addDescription();
    }

    public PatternModel getPatternModel(){
        return patternModel;
    }

    public void editLayout(){
        vBox.getChildren().clear();
        vBox.getChildren().addAll(newPatternName,newPatternDescription);
        HBox btnBox = new HBox(10);
        btnBox.getChildren().addAll(applyBtn, cnclBtn, schemaChooseBtn, schemaViewer);
        vBox.getChildren().add(btnBox);
        if (delEditBox!=null)
            delEditBox.visibleProperty().set(false);
        cnclBtn.setOnAction(e -> {
            showLayout();
            delEditBox.visibleProperty().set(true);
            delEditBox.getChildren().add(schemaViewer);
        });
        schemaChooseBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage schemaChooseWindow = new Stage();
                FileChooser fileChooser = new FileChooser();
                fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
                fileChooser.setTitle("Pattern schema chooser");
                fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("png","*.png"),
                        new FileChooser.ExtensionFilter("bmp","*.bmp"),
                        new FileChooser.ExtensionFilter("jpg", "*.jpg"));
                File file = fileChooser.showOpenDialog(schemaChooseWindow);
                if(file != null)
                    newPatternSchema.setImage(new Image(file.toURI().toString()));
            }
        });
    }
}


