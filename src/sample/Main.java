package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import sample.thrift.WebPatternDB;
import sample.window_process.MainWindowPane;

public class Main extends Application {
    private TSocket socket;
    @Override
    public void start(Stage primaryStage) throws Exception{
        BorderPane mainPane = new BorderPane();
        try{
            socket = new TSocket("localhost",1488);
            socket.open();
            TProtocol protocol = new TBinaryProtocol(socket);
            WebPatternDB.Client client = new WebPatternDB.Client(protocol);
            client.clientConnect(true);
            MainWindowPane mainWindowPane = new MainWindowPane(mainPane, client);
        }catch (Exception e){
            e.printStackTrace();
        }
        primaryStage.setTitle("Web Application Patterns");
        primaryStage.setScene(new Scene(mainPane, 600, 400));
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        socket.close();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
