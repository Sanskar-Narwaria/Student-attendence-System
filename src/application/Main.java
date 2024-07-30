package application;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;    

public class Main extends Application {
	private double x=0;
	private double y=0;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("LoginPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        //scene.setFill(Color.TRANSPARENT);
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setOnMousePressed( (event)->{
        	x=event.getSceneX();
        	y=event.getSceneY();
        });
        scene.setOnMouseDragged( (event)->{
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);
        });
        stage.setTitle("LogIn");
        stage.setScene(scene);
        stage.show();
        
    }
    public static void main(String[] args) {
        launch();
    }
}