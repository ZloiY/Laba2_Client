package sample;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import org.apache.thrift.TException;
import sample.thrift.WebPatternDB;
import sample.thrift.WorkWithClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZloiY on 3/9/2017.
 */
public class MainWindowPane {
    private List<WorkWithClient> withClients;
    private TabPane tabPane;
    private WebPatternDB.Client client;
    public MainWindowPane(BorderPane mainPane, WebPatternDB.Client client){
        tabPane = new TabPane();
        mainPane.setCenter(tabPane);
        this.client = client;
        withClients = searchAllPatterns();
        addAllTabs();
    }

    private ArrayList<WorkWithClient> searchAllPatterns(){
        WorkWithClient workWithClient = new WorkWithClient();
        try {
            return new ArrayList<>(client.workWithSearchRequest(1, workWithClient));
        }catch (TException e){
            e.printStackTrace();
            return null;
        }
    }

    private void addAllTabs(){
        for (WorkWithClient withClient : withClients){
            Tab tab = new Tab(withClient.name);
            Window window = new Window(withClient);
            window.addAll();
            tab.setContent(window.getBorderPane());
            tab.setClosable(false);
            tabPane.getTabs().add(tab);
        }
        Tab plusTab = new Tab("+");
        plusTab.setClosable(false);
        tabPane.getTabs().add(plusTab);
    }
}
