package sample;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import sample.thrift.PatternModel;

import java.io.ByteArrayInputStream;

public class Window {
    private int patternID;
    private Label patternName;
    private Label patternDescription;
    private ImageView patternSchemaImage;
    private BorderPane borderPane;
    private TextField newPatternName;
    private TextField newPatternDescription;
    private ImageView newPatternSchema;
    private Button applyBtn;
    private Button cnclBtn;
    private VBox vBox;
    private Button delBtn;
    private Button editBtn;
    private HBox delEditBox;

    public Window(PatternModel pattern){
        patternID = pattern.id;
        patternName = new Label(pattern.name);
        patternDescription = new Label(pattern.description);
        if (pattern.schema != null)
        patternSchemaImage = new ImageView(new Image(new ByteArrayInputStream(pattern.schema.array())));
        else patternSchemaImage = new ImageView();
        newPatternName = new TextField();
        newPatternDescription = new TextField();
        newPatternSchema = new ImageView();
        applyBtn = new Button("Add");
        cnclBtn = new Button("Cancel");
        editBtn = new Button("Edit");
        delBtn = new Button("Delete");
        borderPane = new BorderPane();
        vBox = new VBox(10);
        delEditBox = new HBox(10);
        delEditBox.getChildren().addAll(editBtn,delBtn);
        borderPane.setCenter(vBox);
        borderPane.setBottom(delEditBox);

    }

    public Window(){
        newPatternName = new TextField();
        newPatternDescription = new TextField();
        newPatternSchema = new ImageView();
        applyBtn = new Button("Add");
        cnclBtn = new Button("Cancel");
        borderPane = new BorderPane();
        vBox = new VBox(10);
        borderPane.setCenter(vBox);
    }

    public TextField getNewPatternName() {
        return newPatternName;
    }

    public TextField getNewPatternDescription() {
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
        addSchema();
    }

    public void editLayout(){
        vBox.getChildren().clear();
        vBox.getChildren().addAll(newPatternName,newPatternDescription,newPatternSchema);
        HBox btnBox = new HBox(10);
        btnBox.getChildren().addAll(applyBtn, cnclBtn);
        vBox.getChildren().add(btnBox);
    }
}


