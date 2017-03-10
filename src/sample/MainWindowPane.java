package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import org.apache.thrift.TException;
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
                    AlertBox.makeAlert("Your pattern name more than 10 characters or it's empty");
                    return;
                }else
                    newPattern.setName(addWindow.getNewPatternName().getText());
                if (addWindow.getNewPatternDescription().getText().length() > 500 || addWindow.getNewPatternDescription().getText().isEmpty()){
                    AlertBox.makeAlert("Your pattern description more than 500 characters or it's empty");
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
                    client.addPattern(newPattern);
                    tabPane.getTabs().remove(tab);
                    addAllTabs();
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
                        client.deletePattern(deletePattern);
                        tabPane.getTabs().remove(tab);
                    }catch (TException e){
                        e.printStackTrace();
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
                    tab.setContent(editWindow.getBorderPane());
                }
            });
            tabPane.getTabs().add(tab);
            if (setSelected)
            tabPane.getSelectionModel().select(tab);
        }
    }
}
