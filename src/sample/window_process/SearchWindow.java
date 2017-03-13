package sample.window_process;

import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.apache.thrift.TException;
import sample.thrift.PatternModel;
import sample.thrift.WebPatternDB;

import java.util.List;

/**
 * Created by ZloiY on 3/13/2017.
 */
public class SearchWindow {
    private BorderPane borderPane;
    private TextField patternName;
    private TextField patternDescription;
    private TableView<PatternModel> table;
    private WebPatternDB.Client clientSearch;
    private PatternModel patternModel;

    public SearchWindow(WebPatternDB.Client client){
        clientSearch = client;
        borderPane = new BorderPane();
        patternName = new TextField();
        patternDescription = new TextField();
        patternModel = new PatternModel();
        Label patternNameLabel = new Label("Enter pattern name:");
        Label patternDescriptionLabel = new Label("Enter pattern description:");
        VBox searchPatternBox = new VBox(10);
        searchPatternBox.getChildren().addAll(patternNameLabel,patternName,patternDescriptionLabel, patternDescription);
        patternName.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (!patternName.getText().isEmpty())
                patternModel.setName(patternName.getText());
                try {
                    List<PatternModel> patternModels = clientSearch.findPattern(patternModel);
                    table.setItems(FXCollections.observableArrayList(patternModels));
                }catch (TException e){
                    e.printStackTrace();
                }
            }
        });
        patternDescription.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (!patternDescription.getText().isEmpty())
                patternModel.setDescription(patternDescription.getText());
                try{
                    table.setItems(FXCollections.observableArrayList(clientSearch.findPattern(patternModel)));
                }catch (TException e){
                    e.printStackTrace();
                }
            }
        });
        borderPane.setCenter(searchPatternBox);
        table = new TableView<PatternModel>();
        TableColumn tableColumn = new TableColumn("Pattern");
        tableColumn.setCellValueFactory(new PropertyValueFactory<PatternModel,String>("name"));
        table.getColumns().add(tableColumn);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        borderPane.setRight(table);
    }

    public BorderPane getBorderPane() {
        return borderPane;
    }

}
