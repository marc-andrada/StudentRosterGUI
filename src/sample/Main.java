package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.initialize(primaryStage);
        primaryStage.setTitle("Exercise 2 FXML");
        Scene scene = new Scene(root, 900, 905);
        scene.getStylesheets().add("mystyle.css");
        primaryStage.setScene(scene);
        primaryStage.show();
        /*primaryStage.setMaxWidth(900);
        primaryStage.setMaxHeight(900);*/
    }



    public static void main(String[] args) {
        launch(args);
    }
}
