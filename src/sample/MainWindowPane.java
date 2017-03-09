package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import org.apache.thrift.TException;
import sample.thrift.PatternModel;
import sample.thrift.WebPatternDB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZloiY on 3/9/2017.
 */
public class MainWindowPane {
    private List<PatternModel> patternList;
    private TabPane tabPane;
    private WebPatternDB.Client client;
    public MainWindowPane(BorderPane mainPane, WebPatternDB.Client client){
        tabPane = new TabPane();
        mainPane.setCenter(tabPane);
        this.client = client;
        patternList = searchAllPatterns();
        addAllTabs();
        Button addPatternBtn = new Button("+");
        mainPane.setRight(addPatternBtn);
        addPatternBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addPatternTab();
            }
        });
    }

    private void addPatternTab(){
        Tab tab = new Tab("tab");
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
        Window addWindow = new Window();
        addWindow.editLayout();
        tab.setContent(addWindow.getBorderPane());
        addWindow.getApplyBtn().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                PatternModel newPattern = new PatternModel();
                if (addWindow.getNewPatternName().getText().length() > 10 || addWindow.getNewPatternName().getText().isEmpty()) {
                    AlertBox.makeAlert("Your pattern name more than 10 characters or it's empty");
                    return;
                }else
                    newPattern.setName(addWindow.getNewPatternName().getText());
                if (addWindow.getNewPatternDescription().getText().length() > 500 || addWindow.getNewPatternDescription().getText().isEmpty()){
                    AlertBox.makeAlert("Your pattern description more than 500 characters or it's empty");
                    return;
                }else
                    newPattern.setDescription(addWindow.getNewPatternDescription().getText());
                try {
                    client.addPattern(newPattern);
                    PatternModel pattern = client.getLastPattern();
                    Window showWindow = new Window(pattern);
                    showWindow.showLayout();
                    tab.setText(pattern.getName());
                    tab.setClosable(false);
                    tab.setContent(showWindow.getBorderPane());
                }catch (TException e){
                    e.printStackTrace();
                }
            }
        });
    }

    private ArrayList<PatternModel> searchAllPatterns(){
        PatternModel pattern = new PatternModel();
        try {
            return new ArrayList<>(client.findPattern(pattern));
        }catch (TException e){
            e.printStackTrace();
            return null;
        }
    }

    private void addAllTabs(){
        for (PatternModel pattern : patternList){
            Tab tab = new Tab(pattern.name);
            Window window = new Window(pattern);
            window.showLayout();
            tab.setContent(window.getBorderPane());
            window.getDelBtn().setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    PatternModel deletePattern = new PatternModel();
                    deletePattern.setId(window.getPatternID());
                    try {
                        client.deletePattern(deletePattern);
                    }catch (TException e){
                        e.printStackTrace();
                    }
                    tabPane.getTabs().remove(tab);
                }
            });
            tab.setClosable(false);
            tabPane.getTabs().add(tab);
        }
    }
}
