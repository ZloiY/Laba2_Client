package sample;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Created by ZloiY on 3/9/2017.
 */
public class AlertBox {
    public static void makeAlert(String message){
        VBox mainPane = new VBox(10);
        Stage alertWindow = new Stage();
        Button okBtn = new Button("Ok");
        Label errorMsg = new Label(message);
        alertWindow.setTitle("Error");
        alertWindow.setAlwaysOnTop(true);
        mainPane.getChildren().addAll(errorMsg, okBtn);
        okBtn.setOnAction(e->{
            alertWindow.close();
        });
        alertWindow.setScene(new Scene(mainPane));
        alertWindow.show();
    }
}
