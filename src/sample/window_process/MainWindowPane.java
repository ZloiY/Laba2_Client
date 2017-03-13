package sample.window_process;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.apache.thrift.TException;
import sample.Errors;
import sample.MyTab;
import sample.thrift.PatternModel;
import sample.thrift.WebPatternDB;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZloiY on 3/9/2017.
 */
public class MainWindowPane implements Errors {
    private List<PatternModel> patternList;
    private TabPane tabPane;
    private WebPatternDB.Client client;
    public MainWindowPane(BorderPane mainPane, WebPatternDB.Client client){
        tabPane = new TabPane();
        patternList = new ArrayList<>();
        mainPane.setCenter(tabPane);
        this.client = client;
        addAllTabs();
        HBox functionBtnBox = new HBox();
        Button addPatternBtn = new Button("+");
        Button searchButton = new Button("Search");
        functionBtnBox.getChildren().addAll(addPatternBtn,searchButton);
        mainPane.setRight(functionBtnBox);
        addPatternBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addPatternTab();
            }
        });
        searchButton.setOnAction(e->{
            SearchWindow searchTab = new SearchWindow(this.client);
            Tab search = new Tab("Search");
            search.setContent(searchTab.getBorderPane());
            tabPane.getTabs().add(search);
            tabPane.getSelectionModel().select(search);
        });
    }

    private void addPatternTab(){
        MyTab tab = new MyTab("tab");
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
                    new Alert(Alert.AlertType.ERROR,"Your pattern name more than 10 characters or it's empty").show();
                    return;
                }else
                    newPattern.setName(addWindow.getNewPatternName().getText());
                if (addWindow.getNewPatternDescription().getText().length() > 500 || addWindow.getNewPatternDescription().getText().isEmpty()){
                    new Alert(Alert.AlertType.ERROR,"Your pattern description more than 500 characters or it's empty").show();
                    return;
                }else
                    newPattern.setDescription(addWindow.getNewPatternDescription().getText());
                if (addWindow.getNewPatternSchema() != null){
                    BufferedImage bufferedImage = SwingFXUtils.fromFXImage(addWindow.getNewPatternSchema().getImage(), null);
                    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                    try {
                        ImageIO.write(bufferedImage,"png",outStream);
                        newPattern.setSchema(outStream.toByteArray());
                        outStream.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
                try {
                    if (client.isConnected())
                    client.addPattern(newPattern);
                    tabPane.getTabs().remove(tab);
                    addAllTabs();
                }catch (TException e){
                    new Alert(Alert.AlertType.ERROR,"Service is offline try again latter.("+addErr+")").show();
                }
            }
        });
    }

    private ArrayList<PatternModel> searchAllPatterns(){
        PatternModel pattern = new PatternModel();
        try {
            if (client.isConnected())
            return new ArrayList<>(client.findPattern(pattern));
        }catch (TException e){
            new Alert(Alert.AlertType.ERROR,"Service is offline try again later.("+searchAllErr+")").show();
            return null;
        }
        return null;
    }

    private void addAllTabs(){
        boolean setSelected = true;
        patternList = searchAllPatterns();
        for (int i =0; i < patternList.size(); i++){
            setSelected = true;
            if (!tabPane.getTabs().isEmpty() && tabPane.getTabs().size() > i && ((MyTab)tabPane.getTabs().get(i)).getWindow().getPatternID() == patternList.get(i).getId()){
                setSelected = false;
                continue;
            }
            Window window = new Window(patternList.get(i));
            MyTab tab = new MyTab(patternList.get(i).name);
            window.showLayout();
            tab.setWindow(window);
            tab.setContent(window.getBorderPane());
            tab.setClosable(false);
            window.getDelBtn().setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    PatternModel deletePattern = new PatternModel();
                    deletePattern.setId(window.getPatternID());
                    try {
                        if (client.isConnected())
                        client.deletePattern(deletePattern);
                        tabPane.getTabs().remove(tab);
                    }catch (TException e){
                        new Alert(Alert.AlertType.ERROR, "Service is offline try again later.("+deleteErr+")").show();
                    }
                }
            });
            window.getEditBtn().setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Window editWindow = new Window(window.getPatternModel());
                    editWindow.editLayout();
                    editWindow.getApplyBtn().setText("Edit");
                    editWindow.setPatternID(window.getPatternID());
                    editWindow.getNewPatternName().setText(window.getPatternName().getText());
                    editWindow.getNewPatternDescription().setText(window.getPatternDescription().getText());
                    editWindow.setNewPatternSchema(editWindow.getPatternSchemaImage());
                    editWindow.getApplyBtn().setOnAction(e -> {
                        PatternModel newPattern = new PatternModel();
                        PatternModel oldPattern = new PatternModel();
                        oldPattern.setId(window.getPatternID());
                        newPattern.setId(editWindow.getPatternID());
                        newPattern.setName(editWindow.getNewPatternName().getText());
                        newPattern.setDescription(editWindow.getNewPatternDescription().getText());
                        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(editWindow.getNewPatternSchema().getImage(), null);
                        ByteArrayOutputStream output = new ByteArrayOutputStream();
                        try {
                            ImageIO.write(bufferedImage, "png", output);
                            newPattern.setSchema(output.toByteArray());
                            output.close();
                            if (client.isConnected())
                            client.replacePattern(oldPattern,newPattern);
                            if (client.isConnected())
                            oldPattern = client.getLastPattern();
                            Window newWindow = new Window(oldPattern);
                            newWindow.showLayout();
                            tab.setContent(newWindow.getBorderPane());
                            tab.setText(newWindow.getPatternName().getText());
                        }catch (IOException|TException exception){
                            exception.getCause();
                        }
                    });
                    tab.setContent(editWindow.getBorderPane());
                }
            });
            tabPane.getTabs().add(tab);
            if (setSelected) {
                tabPane.getSelectionModel().select(tab);
            }
        }
    }
}
