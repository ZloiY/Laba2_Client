package sample.window_process;

import com.sun.istack.internal.Nullable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.apache.thrift.TException;
import sample.Adapter;
import sample.thrift.PatternGroup;
import sample.thrift.PatternModel;
import sample.thrift.WebPatternDB;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ZloiY on 3/9/2017.
 */
public class MainWindowPane {
    private ListView<String> allPattern, mvPatterns, createPatterns, structPatterns, behavePatterns;
    private List<PatternModel> allPatternsList, mvPatternsList, createPatternsList, structPatternsList, behavePatternsList;
    private ComboBox<String> patternsGroups;
    private WebPatternDB.Client client;
    private HashMap<String, Integer> patternsMap;
    private BorderPane pane;
    private VBox leftBox;
    private double listViewWidth = 100.0;
    private Button addPatternBtn;
    public MainWindowPane(BorderPane mainPane, WebPatternDB.Client client){
        pane = mainPane;
        this.client = client;
        allPattern = new ListView<>();
        allPattern.setPrefWidth(listViewWidth);
        mvPatterns = new ListView<>();
        mvPatterns.setPrefWidth(listViewWidth);
        createPatterns = new ListView<>();
        createPatterns.setPrefWidth(listViewWidth);
        structPatterns = new ListView<>();
        structPatterns.setPrefWidth(listViewWidth);
        behavePatterns = new ListView<>();
        behavePatterns.setPrefWidth(listViewWidth);
        patternsGroups = new ComboBox<>();
        patternsGroups.setPrefWidth(190.0);
        patternsMap = new HashMap<>();
        for (int i = 0; i <= 4; i++) {
            patternsMap.put(Adapter.fromEnumToStringPatternGroup(PatternGroup.findByValue(i)),i);
            patternsGroups.getItems().add(Adapter.fromEnumToStringPatternGroup(PatternGroup.findByValue(i)));
        }
        patternsGroups.getSelectionModel().select(0);
        getPatterns(null,allPattern, allPatternsList);
        addPatternBtn = new Button("+");
        patternsGroups.setOnAction(event -> {
           refreshPatternLists();
        });
        leftBox = new VBox(5);
        VBox functionBtnBox = new VBox();
        Button searchButton = new Button("Search");
        addPatternBtn.setPrefWidth(190.0);
        pane.setRight(functionBtnBox);
        leftBox.getChildren().addAll(patternsGroups, allPattern,addPatternBtn);
        leftBox.setPadding(new Insets(5,10,5,5));
        pane.setLeft(leftBox);
        addPatternBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addPatternWindow();
            }
        });
    }

    private void refreshPatternLists(){
        if (PatternGroup.findByValue(patternsMap.get(patternsGroups.getValue()))!= null)
            switch (PatternGroup.findByValue(patternsMap.get(patternsGroups.getValue()))) {
                case MV_PATTERNS:
                    getPatterns(PatternGroup.findByValue(patternsMap.get(patternsGroups.getValue())), mvPatterns, mvPatternsList);
                    leftBox.getChildren().clear();
                    leftBox.getChildren().addAll(patternsGroups, mvPatterns, addPatternBtn);
                    break;
                case BEHAVE_PATTERNS:
                    getPatterns(PatternGroup.findByValue(patternsMap.get(patternsGroups.getValue())), behavePatterns, behavePatternsList);
                    leftBox.getChildren().clear();
                    leftBox.getChildren().addAll(patternsGroups, behavePatterns, addPatternBtn);
                    break;
                case CREAT_PATTERNS:
                    getPatterns(PatternGroup.findByValue(patternsMap.get(patternsGroups.getValue())), createPatterns, createPatternsList);
                    leftBox.getChildren().clear();
                    leftBox.getChildren().addAll(patternsGroups, createPatterns, addPatternBtn);
                    break;
                case STRUCT_PATTERNS:
                    getPatterns(PatternGroup.findByValue(patternsMap.get(patternsGroups.getValue())), structPatterns, structPatternsList);
                    leftBox.getChildren().clear();
                    leftBox.getChildren().addAll(patternsGroups, structPatterns, addPatternBtn);
                    break;
            }
        else{
            getPatterns(null, allPattern, allPatternsList);
            leftBox.getChildren().clear();
            leftBox.getChildren().addAll(patternsGroups, allPattern, addPatternBtn);
        }
    }

    private void addPatternWindow(){
        Window addWindow = new Window();
        addWindow.editLayout();
        addWindow.getNewPatternDescription().setMinSize(300, 300);
        addWindow.getNewPatternDescription().setEditable(true);
        pane.setCenter(addWindow.getBorderPane());
        addWindow.getApplyBtn().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                PatternModel newPattern = new PatternModel();
                if (addWindow.getNewPatternName().getText().length() > 10 || addWindow.getNewPatternName().getText().isEmpty()) {
                    new Alert(Alert.AlertType.WARNING,"Your pattern name more than 10 characters or it's empty").show();
                    return;
                }else newPattern.setName(addWindow.getNewPatternName().getText());
                if (addWindow.getNewPatternDescription().getText().length() > 500 || addWindow.getNewPatternDescription().getText().isEmpty()){
                    new Alert(Alert.AlertType.WARNING,"Your pattern description more than 500 characters or it's empty").show();
                    return;
                }else newPattern.setDescription(addWindow.getNewPatternDescription().getText());
                if (addWindow.getNewPatternGroup().getSelectionModel().isEmpty()){
                    new Alert(Alert.AlertType.WARNING, "Your must choose pattern group").show();
                    return;
                }else newPattern.setPatternGroup(addWindow.getPatternGroupId());
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
                    refreshPatternLists();
                }catch (TException e){
                    new Alert(Alert.AlertType.ERROR,"Service is offline try again latter.").show();
                }
            }
        });
    }

    private ArrayList<PatternModel> searchAllPatterns(@Nullable PatternGroup patternGroup){
        PatternModel pattern = new PatternModel();
        if (patternGroup !=  null)
            pattern.setPatternGroup(patternGroup.getValue());
        try {
            return new ArrayList<>(client.findPattern(pattern));
        }catch (TException e){
            new Alert(Alert.AlertType.ERROR,"Service is offline try again later.").show();
            return null;
        }
    }

    private void getPatterns(@Nullable PatternGroup patternGroup, ListView<String> patternModelListView, List<PatternModel> patternsList){
        patternsList = searchAllPatterns(patternGroup);
        patternModelListView.getItems().remove(0, patternModelListView.getItems().size());
        for (PatternModel patternModel : patternsList)
            patternModelListView.getItems().add(patternModel.getName());
        patternModelListView.getSelectionModel().selectedItemProperty().addListener(setListSelectEvent(patternsList, patternModelListView));
        setAllPatternsListAndView();
    }

    private void setAllPatternsListAndView(){
        allPatternsList = searchAllPatterns(null);
        allPattern.getItems().clear();
        for (PatternModel patternModel : allPatternsList)
            allPattern.getItems().add(patternModel.getName());
        allPattern.getSelectionModel().selectedItemProperty().addListener(setListSelectEvent(allPatternsList, allPattern));
    }

    private ChangeListener<String> setListSelectEvent(List<PatternModel> patternsList, ListView<String> patternModelListView){
        ChangeListener<String> changeListener = new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (patternModelListView.getSelectionModel().getSelectedIndex() >= 0 && patternModelListView.getSelectionModel().getSelectedIndex() < patternModelListView.getItems().size()) {
                    Window patternWindow = new Window(patternsList.get(patternModelListView.getSelectionModel().getSelectedIndex()));
                    patternWindow.showLayout();
                    patternWindow.getEditBtn().setOnAction(setEditEvent(patternWindow));
                    patternWindow.getDelBtn().setOnAction(setDelEvent(patternWindow));
                    pane.setCenter(patternWindow.getBorderPane());
                }
            }
        };
        return changeListener;
    }

    private EventHandler<ActionEvent> setEditEvent(Window window){
        EventHandler<ActionEvent> editEvent = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                window.editLayout();
                window.getApplyBtn().setText("Edit");
                window.setPatternID(window.getPatternID());
                window.getNewPatternName().setText(window.getPatternName().getText());
                window.getNewPatternDescription().setText(window.getPatternDescription().getText());
                window.getNewPatternGroup().getSelectionModel().select(window.getPatternGroup());
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
                        Window newWindow = new Window(oldPattern);
                        newWindow.showLayout();
                        newWindow.getDelBtn().setOnAction(setDelEvent(newWindow));
                        newWindow.getEditBtn().setOnAction(setEditEvent(newWindow));
                    } catch (TException exception) {
                        exception.getCause();
                    }
                });
            }
        };
        return editEvent;
    }

    private EventHandler<ActionEvent> setDelEvent(Window window){
        EventHandler<ActionEvent> delEvent = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                PatternModel deletePattern = new PatternModel();
                deletePattern.setId(window.getPatternID());
                try {
                    client.deletePattern(deletePattern);
                }catch (TException e){
                    new Alert(Alert.AlertType.ERROR, "Service is offline try again later.").show();
                }
                if (PatternGroup.findByValue(patternsMap.get(patternsGroups.getValue())) != null)
                switch (PatternGroup.findByValue(patternsMap.get(patternsGroups.getValue()))){
                    case MV_PATTERNS:
                        deleteManipulation(mvPatterns);
                        break;
                    case BEHAVE_PATTERNS:
                        deleteManipulation(behavePatterns);
                        break;
                    case CREAT_PATTERNS:
                        deleteManipulation(createPatterns);
                        break;
                    case STRUCT_PATTERNS:
                        deleteManipulation(structPatterns);
                        break;
                }
                else getPatterns(null, allPattern, allPatternsList);
                window.blankWindow();
            }
        };
        return delEvent;
    }

    private void deleteManipulation(ListView<String> currentListView){
        if (currentListView.getSelectionModel().getSelectedIndex()!=0) {
            int selectedId = currentListView.getSelectionModel().getSelectedIndex();
            currentListView.getSelectionModel().select( selectedId- 1);
            currentListView.getItems().remove(selectedId);
        }else{ currentListView.getSelectionModel().clearSelection();
            currentListView.getItems().remove(0);
        }
        getPatterns(null,allPattern, allPatternsList);
    }
}
