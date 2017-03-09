package sample;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import sample.thrift.WorkWithClient;

import java.io.ByteArrayInputStream;

public class Window {
    private int patternID;
    private Label patternName;
    private Label patternDescription;
    private Image patternSchemaImage;
    private BorderPane borderPane;
    private VBox vBox;

    public Window(WorkWithClient client){
        patternName = new Label(client.name);
        patternDescription = new Label(client.description);
        patternSchemaImage = new Image(new ByteArrayInputStream(client.schema.array()));
        borderPane = new BorderPane();
        vBox = new VBox(10);
        borderPane.setCenter(vBox);
    }

    public Window(){}

    public BorderPane getBorderPane() {
        return borderPane;
    }

    public int getPatternID() {

        return patternID;
    }

    public void setPatternID(int patternID) {
        this.patternID = patternID;
    }

    public Label getPatternName() {
        return patternName;
    }

    public void setPatternName(Label patternName) {
        this.patternName = patternName;
    }

    public Label getPatternDescription() {
        return patternDescription;
    }

    public void setPatternDescription(Label patternDescription) {
        this.patternDescription = patternDescription;
    }

    public Image getPatternSchemaImage() {
        return patternSchemaImage;
    }

    public void setPatternSchemaImage(Image patternSchemaImage) {
        this.patternSchemaImage = patternSchemaImage;
    }

    public void addName(){
        vBox.getChildren().add(patternName);
    }

    public void addDescription(){
        vBox.getChildren().add(patternDescription);
    }

    public void addSchema(){
        vBox.getChildren().add(new ImageView(patternSchemaImage));
    }

    public void addAll(){
        addName();
        addDescription();
        addSchema();
    }
}


