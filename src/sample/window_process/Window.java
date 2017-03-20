package sample.window_process;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.Adapter;
import sample.SchemaViewer;
import sample.thrift.PatternGroup;
import sample.thrift.PatternModel;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

public class Window {
    private int patternID;
    private Label patternName;
    private Label patternGroup;
    private ComboBox<String> newPatternGroup;
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
    private VBox descripitionBox;
    private Button delBtn;
    private Button editBtn;
    private HBox delEditBox;
    private HashMap<String , Integer> patternsMap;

    public Window(PatternModel pattern){
        patternModel = pattern;
        patternID = pattern.id;
        patternGroup = new Label(Adapter.fromEnumToStringPatternGroup(pattern.getPatternGroup()));
        patternName = new Label(pattern.name);
        patternsMap = new HashMap<>();
        patternDescription = new Text(pattern.description);
        patternDescription.setWrappingWidth(380);
        patternSchemaImage = new ImageView();
        if (pattern.schema != null) {
            Image image = new Image(new ByteArrayInputStream(pattern.schema.array()));
            patternSchemaImage.setImage(image);
        }
        newPatternName = new TextField();
        newPatternDescription = new TextArea();
        newPatternDescription.setWrapText(true);
        newPatternGroup = new ComboBox<>();
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
        descripitionBox = new VBox(5);
        delEditBox = new HBox(5);
        delEditBox.getChildren().addAll(editBtn, delBtn, schemaViewer);
        borderPane.setCenter(descripitionBox);
        borderPane.setBottom(delEditBox);
        borderPane.setPadding(new Insets(5,15,5,5));

    }

    public Window(){
        newPatternName = new TextField();
        newPatternDescription = new TextArea();
        newPatternSchema = new ImageView();
        newPatternGroup = new ComboBox<>();
        patternGroup = new Label();
        patternsMap = new HashMap<>();
        for (int i = 1; i <= 4; i++) {
            patternsMap.put(Adapter.fromEnumToStringPatternGroup(PatternGroup.findByValue(i)),i);
            newPatternGroup.getItems().add(Adapter.fromEnumToStringPatternGroup(PatternGroup.findByValue(i)));
        }
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
        borderPane.setPadding(new Insets(5,15,5,5));
        descripitionBox = new VBox(5);
        borderPane.setCenter(descripitionBox);
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

    public void showLayout(){
        descripitionBox.getChildren().clear();
        descripitionBox.getChildren().addAll(patternName,patternGroup,patternDescription);
    }

    public PatternModel getPatternModel(){
        return patternModel;
    }

    public ComboBox<String> getNewPatternGroup() {
        return newPatternGroup;
    }

    public int getPatternGroup(){
        return patternsMap.get(newPatternGroup.getValue());
    }

    public void editLayout(){
        descripitionBox.getChildren().clear();
        descripitionBox.getChildren().addAll(newPatternName,newPatternGroup,newPatternDescription);
        HBox btnBox = new HBox(10);
        btnBox.getChildren().addAll(applyBtn, cnclBtn, schemaChooseBtn, schemaViewer);
        btnBox.setPadding(new Insets(0,5,0,0));
        descripitionBox.getChildren().add(btnBox);
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


