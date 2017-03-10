package sample;

import javafx.scene.control.Tab;
import sample.window_process.Window;

/**
 * Created by ZloiY on 3/10/2017.
 */
public class MyTab extends Tab {
    private Window window;

    public MyTab(String name){
        super(name);
    }

    public void setWindow(Window window){
        this.window = window;
    }

    public Window getWindow(){
        return window;
    }
}
