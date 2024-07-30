package application;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
//import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class loginPageController implements Initializable {
	private double x=0;
	private double y=0;
    @FXML
    private CheckBox chkBox;

    @FXML
    private Button loginBtn;

    @FXML
    private PasswordField passField;

    @FXML
    private TextField passTextField;

    @FXML
    private TextField userName;
    @FXML
    void changeVisibility(ActionEvent event) {
        if(chkBox.isSelected()){
            passTextField.setText(passField.getText());
            passTextField.setVisible(true);
            passField.setVisible(false);
            return;
        }
        passField.setText(passTextField.getText());
        passField.setVisible(true);
        passTextField.setVisible(false);
    }
    @FXML
    private Button closeBtn;
    @FXML
    void closeWindow(ActionEvent event) {
    	Stage stage= (Stage) closeBtn.getScene().getWindow();
    	stage.close();
    }

    @FXML
    private Label errorMsg;

	private Scene scene;

	private FXMLLoader fxmlLoader;
    @FXML
    void longInUser(MouseEvent event) throws IOException {
        String user=userName.getText();
        String pass;
        if (chkBox.isSelected()) {
        	pass=passTextField.getText();
        }
        else {
        	pass=passField.getText();
        }
        if (user.isEmpty() || pass.isEmpty()) {
        	Alert alert = new Alert(AlertType.ERROR);
        	alert.setTitle("Error Message");
        	alert.setHeaderText(null);
        	alert.setContentText("Please fill all the blank fields");
        	alert.showAndWait();
        }
        else {
        	databaseOperations dbop =new databaseOperations();
            if (dbop.checkCredentials(user, pass)) {
            	//System.out.println("Login Success");
            	
            	//proceed to homepage
            	Alert alert1 = new Alert(AlertType.INFORMATION);
            	alert1.setTitle("Information Message");
            	alert1.setHeaderText(null);
            	alert1.setContentText("Successfully Login");
            	alert1.showAndWait();
            	
            	Stage currentStage = (Stage) loginBtn.getScene().getWindow();
            	currentStage.close();
            	
            	fxmlLoader = new FXMLLoader(Main.class.getResource("homePage.fxml"));
                scene = new Scene(fxmlLoader.load());
                homePageController controller=fxmlLoader.getController();
                controller.displayName(user);
                controller.UserType(user, pass);
                Stage stage=new Stage();
                stage.initStyle(StageStyle.TRANSPARENT);
                scene.setOnMousePressed( (event1)->{
                	x=event1.getSceneX();
                	y=event1.getSceneY();
                });
                scene.setOnMouseDragged( (event1)->{
                    stage.setX(event1.getScreenX() - x);
                    stage.setY(event1.getScreenY() - y);
                });
                stage.setScene(scene);
                stage.show();
                
                
                
            }
            else {
            errorMsg.setText("Invalid Login Credentials!!");
            errorMsg.setVisible(true);
            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), errorMsg);
            fadeTransition.setFromValue(2.0);
            fadeTransition.setToValue(0.0);
            fadeTransition.playFromStart();
            }
        }
    }
    
    @FXML
    private Text txtSignUp;
    

    @FXML
    void signUp(MouseEvent event) throws IOException {
    	Stage currentStage = (Stage) txtSignUp.getScene().getWindow();
    	currentStage.close();
    	
    	fxmlLoader = new FXMLLoader(Main.class.getResource("signuppage.fxml"));
        scene = new Scene(fxmlLoader.load());
        Stage stage=new Stage();
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setOnMousePressed( (event1)->{
        	x=event1.getSceneX();
        	y=event1.getSceneY();
        });
        scene.setOnMouseDragged( (event1)->{
            stage.setX(event1.getScreenX() - x);
            stage.setY(event1.getScreenY() - y);
        });
        stage.setScene(scene);
        stage.show();
        

    }
    
    
    
    
    public void initialize(URL url, ResourceBundle resourceBundle) {
        errorMsg.setVisible(false);
    }

}
