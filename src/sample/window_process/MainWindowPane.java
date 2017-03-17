package sample.window_process;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.apache.thrift.TException;
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
public class MainWindowPane {
    private List<PatternModel> patternList;
    private TabPane tabPane;
    private WebPatternDB.Client client;
    public MainWindowPane(BorderPane mainPane, WebPatternDB.Client client){
        tabPane = new TabPane();
        patternList = new ArrayList<>();
        mainPane.setCenter(tabPane);
        this.client = client;
        addAllTabs();
        VBox functionBtnBox = new VBox();
        Button addPatternBtn = new Button("+");
        Button searchButton = new Button("Search");
        addPatternBtn.setMinWidth(52);
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
        addWindow.getNewPatternDescription().setMinSize(200, 300);
        addWindow.getNewPatternDescription().setEditable(true);
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
                if (addWindow.getNewPatternSchema().getImage() != null){
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
                    client.addPattern(newPattern);
                    tabPane.getTabs().remove(tab);
                    addAllTabs();
                }catch (TException e){
                    new Alert(Alert.AlertType.ERROR,"Service is offline try again latter.").show();
                }
            }
        });
    }

    private ArrayList<PatternModel> searchAllPatterns(){
        PatternModel pattern = new PatternModel();
        try {
            return new ArrayList<>(client.findPattern(pattern));
        }catch (TException e){
            new Alert(Alert.AlertType.ERROR,"Service is offline try again later.").show();
            return null;
        }
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
            window.getDelBtn().setOnAction(setDelEvent(window, tab));
            window.getEditBtn().setOnAction(setEditEvent(window,tab));
            tabPane.getTabs().add(tab);
            if (setSelected) {
                tabPane.getSelectionModel().select(tab);
            }
        }
    }

    private EventHandler<ActionEvent> setEditEvent(Window window, MyTab tab){
        EventHandler<ActionEvent> editEvent = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                window.editLayout();
                window.getApplyBtn().setText("Edit");
                window.setPatternID(window.getPatternID());
                window.getNewPatternName().setText(window.getPatternName().getText());
                window.getNewPatternDescription().setText(window.getPatternDescription().getText());
                window.setNewPatternSchema(window.getPatternSchemaImage());
                window.getApplyBtn().setOnAction(e -> {
                    PatternModel newPattern = new PatternModel();
                    PatternModel oldPattern = new PatternModel();
                    oldPattern.setId(window.getPatternID());
                    oldPattern.setName(window.getPatternName().getText());
                    newPattern.setId(window.getPatternID());
                    newPattern.setName(window.getNewPatternName().getText());
                    if(newPattern.getName().length() > 10 || newPattern.getName().isEmpty()) {
                        new Alert(Alert.AlertType.ERROR, "Your pattern name more than 10 characters or it's empty").show();
                        return;
                    }
                    newPattern.setDescription(window.getNewPatternDescription().getText());
                    if (newPattern.getDescription().length() > 500 || newPattern.getDescription().isEmpty()) {
                        new Alert(Alert.AlertType.ERROR, "Your pattern description more than 500 characters or it's empty").show();
                        return;
                    }
                    if (window.getNewPatternSchema().getImage() != null) {
                        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(window.getNewPatternSchema().getImage(), null);
                        ByteArrayOutputStream output = new ByteArrayOutputStream();
                        try {
                            ImageIO.write(bufferedImage, "png", output);
                            newPattern.setSchema(output.toByteArray());
                            output.close();
                        }catch (IOException ioException){
                            ioException.printStackTrace();
                        }
                    }
                    try {
                        client.replacePattern(oldPattern, newPattern);
                        oldPattern = client.findPatternById(newPattern.getId());
                        Window newWindow = new Window(oldPattern);
                        newWindow.showLayout();
                        newWindow.getDelBtn().setOnAction(setDelEvent(newWindow,tab));
                        newWindow.getEditBtn().setOnAction(setEditEvent(newWindow,tab));
                        tab.setContent(newWindow.getBorderPane());
                        tab.setText(newWindow.getPatternName().getText());
                    } catch (TException exception) {
                        exception.getCause();
                    }
                });
                tab.setContent(window.getBorderPane());
            }
        };
        return editEvent;
    }

    private EventHandler<ActionEvent> setDelEvent(Window window, MyTab tab){
        EventHandler<ActionEvent> delEvent = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                PatternModel deletePattern = new PatternModel();
                deletePattern.setId(window.getPatternID());
                try {
                    client.deletePattern(deletePattern);
                    tabPane.getTabs().remove(tab);
                }catch (TException e){
                    new Alert(Alert.AlertType.ERROR, "Service is offline try again later.").show();
                }
            }
        };
        return delEvent;
    }
}
