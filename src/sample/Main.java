package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("SCHEDULER");
        primaryStage.setMinHeight(610);
        primaryStage.setMinWidth(760);
        primaryStage.setScene(new Scene(root, 760, 610));
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}