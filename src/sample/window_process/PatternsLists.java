package sample.window_process;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.ListView;
import sample.thrift.PatternModel;

import java.util.List;

/**
 * Created by ZloiY on 3/21/2017.
 */
public class PatternsLists {
    private List<PatternModel> patternsLists;
    private ListView<String> patternsView;
    private double listsWidth = 100.0;

    public PatternsLists(){
        patternsView = new ListView<>();
        patternsView.setPrefWidth(listsWidth);
    }

    private void setPatternsLists(List<PatternModel> patterns){
        patternsLists = patterns;
        patternsView.getItems().clear();
        for (PatternModel patternModel : patternsLists)
            patternsView.getItems().add(patternModel.getName());
    }

    public ListView<String> getPatternsView(){ return patternsView; }

    public void compareLists(List<PatternModel> newList){
        setPatternsLists(newList);
    }

    public PatternModel getSelectedPattern(){
        return patternsLists.get(patternsView.getSelectionModel().getSelectedIndex());
    }

    public void setListViewListner(ChangeListener<String> listner){
        patternsView.getSelectionModel().selectedItemProperty().addListener(listner);
    }

    public void deleteFromPatternsView() {
        if (patternsView.getSelectionModel().getSelectedIndex()!=0) {
            int selectedId = patternsView.getSelectionModel().getSelectedIndex();
            patternsView.getSelectionModel().select( selectedId- 1);
            patternsView.getItems().remove(selectedId);
        }else{ patternsView.getSelectionModel().clearSelection();
            patternsView.getItems().remove(0);
        }
    }
}
