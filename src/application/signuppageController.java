package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class signuppageController {
	private double x=0;
	private double y=0;

    @FXML
    private RadioButton Admin;

    @FXML
    private CheckBox ChkBoxShowPass;

    @FXML
    private RadioButton Student;

    @FXML
    private Button btnSignUP;

    @FXML
    private Button closeBtn;

    @FXML
    private PasswordField confirmPass;

    @FXML
    private PasswordField enterPass;
    @FXML
    private TextField txtEnterPass;

    @FXML
    private TextField txtconfirmPass;

    @FXML
    private ToggleGroup user;


    @FXML
    private AnchorPane anchorSignUp;
    
    @FXML
    private TextField usrName;

    @FXML
    void closeWindow(MouseEvent event) {
    	Stage stage= (Stage) closeBtn.getScene().getWindow();
    	stage.close();
    }
    

    @FXML
    void changeVisibility(ActionEvent event) {
    	if(ChkBoxShowPass.isSelected()) {
    		txtEnterPass.setText(enterPass.getText());
    		txtEnterPass.setVisible(true);
    		enterPass.setVisible(false);
    		
    		txtconfirmPass.setText(confirmPass.getText());
    		txtconfirmPass.setVisible(true);
    		confirmPass.setVisible(false);
    		return;
    	}
    	
    	enterPass.setText(txtEnterPass.getText());
    	enterPass.setVisible(true);
    	txtEnterPass.setVisible(false);
    	
    	confirmPass.setText(txtconfirmPass.getText());
    	confirmPass.setVisible(true);
    	txtconfirmPass.setVisible(false);
    	
    }

    @FXML
    void signUp(MouseEvent event) throws IOException {
    	
    	String urname=usrName.getText();
    	String pass="";
    	String conPass="";
    	if (ChkBoxShowPass.isSelected()) {
    		pass=txtEnterPass.getText();
    		conPass=txtconfirmPass.getText();
    	}
    	else {
    		pass=enterPass.getText();
    		conPass=confirmPass.getText();
    	}
    	
    	String user="";
    	if (Student.isSelected()) {
    		user="Student";
    	}
    	else if(Admin.isSelected()) {
    		user="Admin";
    	}
    	
    	if(!urname.isEmpty()) {
    		databaseOperations db=new databaseOperations();
        	if (pass.equals(conPass)) {
        		if (user.equals("Admin")) {	
        			popUpAnchor.setVisible(true);
        			anchorSignUp.setVisible(false);
        		}
        		else {
            		db.insertSignUpDetails(urname, pass, user);
            		String msg="Sign Up success!";
            		showMessage(msg);
            		Stage currentStage = (Stage) btnSignUP.getScene().getWindow();
                	currentStage.close();
                	
                	FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("LoginPage.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());
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
        	}
        	else {
        		String msg="Enter correct confirmation password";
        		showError(msg);
        	}
    	}
    	
    	else {
    		String msg="Username cannot be empty";
    		showError(msg);
    	}
    	//System.out.println("Username = "+ urname + " Password = " + pass + " ConfirmPass = " + conPass + " User = " + user);
    	
    	
    	
    }
    public void showError(String str) {
    	Alert alert = new Alert(AlertType.ERROR);
    	alert.setTitle("Error Message");
    	alert.setHeaderText(null);
    	alert.setContentText(str);
    	alert.showAndWait();
    }
    public void showMessage(String str) {

		Alert alert = new Alert(AlertType.INFORMATION);
    	alert.setTitle("Success");
    	alert.setHeaderText(null);
    	alert.setContentText(str);
    	alert.showAndWait(); 
    }
    

    @FXML
    private Button btn_popUp_confirmPass;

    @FXML
    private Button btnclosePopUp;



    @FXML
    private CheckBox popChkShowPass;

    @FXML
    private PasswordField popEnterPass;

    @FXML
    private AnchorPane popUpAnchor;

    @FXML
    private TextField popUptextPass;



    @FXML
    void chkPass(MouseEvent event) throws IOException {
    	String password1="";
    	if(popChkShowPass.isSelected()) {
    		password1=popUptextPass.getText();
    	}
    	else {
    		password1=popUptextPass.getText();
    	}
    	
    	if (password1.equals("SignUp@Admin")) {
    		popUpAnchor.setVisible(false);
    		String urname=usrName.getText();
        	String pass="";
        	String conPass="";
        	if (ChkBoxShowPass.isSelected()) {
        		pass=txtEnterPass.getText();
        		conPass=txtconfirmPass.getText();
        	}
        	else {
        		pass=enterPass.getText();
        		conPass=confirmPass.getText();
        	}
        	
        	String user="";
        	if (Student.isSelected()) {
        		user="Student";
        	}
        	else if(Admin.isSelected()) {
        		user="Admin";
        	}
    		databaseOperations db=new databaseOperations();
    		db.insertSignUpDetails(urname, pass, user);
    		String msg="Sign Up success!";
    		showMessage(msg);
    		Stage currentStage = (Stage) btnSignUP.getScene().getWindow();
        	currentStage.close();
        	
        	FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("LoginPage.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
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
    		//anchorSignUp.setVisible(true);
    		
    	}
    	else {
    		String msg="Enter a Correct Password";
    		showError(msg);
    	}
    }

    @FXML
    void closePopUp(MouseEvent event) {
    	popUpAnchor.setVisible(false);
    	anchorSignUp.setVisible(true);
    }


    @FXML
    void putPassToText(ActionEvent event) {
    	if(popChkShowPass.isSelected()) {
    		popUptextPass.setVisible(true);
    		popUptextPass.setText(popEnterPass.getText());
    		popEnterPass.setVisible(false);
    		return;
    	}
    	popEnterPass.setVisible(true);
    	popEnterPass.setText(popUptextPass.getText());
    	popUptextPass.setVisible(false);

    }


}
