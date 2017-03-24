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

/**
 * Класс служащий для запуска клиента.
 */
public class Main extends Application {
    /**
     * Клиентский сокет.
     */
    private TSocket socket;

    /**
     * Точка входа в программу.
     * @param primaryStage главное окно клиента
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        BorderPane mainPane = new BorderPane();
        try{
            socket = new TSocket("localhost",1488);
            socket.open();
            TProtocol protocol = new TBinaryProtocol(socket);
            WebPatternDB.Client client = new WebPatternDB.Client(protocol);
            MainWindowPane mainWindowPane = new MainWindowPane(mainPane, client);
        }catch (Exception e){
            e.printStackTrace();
        }
        Scene scene = new Scene(mainPane, 610, 400);
        primaryStage.setTitle("Web Application Patterns");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Закрывает клиентский сокет.
     * @throws Exception
     */
    @Override
    public void stop() throws Exception {
        super.stop();
        socket.close();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
