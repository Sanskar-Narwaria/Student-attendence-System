package application;
import java.awt.Checkbox;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

import javax.xml.transform.Result;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.*;


public class homePageController implements Initializable{
	
	
	//changing pages
    @FXML
    private AnchorPane pageModification;

    @FXML
    private AnchorPane pageTakeAttendence;

    @FXML
    private AnchorPane pageViewAttendence;

    @FXML
    private Button modify_btn;

    @FXML
    private Button takeAtt_btn;

    @FXML
    private Button viewAtt_btn;
    
    public void switchForm(ActionEvent event) {
    	if (event.getSource()==takeAtt_btn) {
    		pageModification.setVisible(false);
    		pageTakeAttendence.setVisible(true);
    		pageViewAttendence.setVisible(false);
    		
    	}
    	else if (event.getSource()==modify_btn) {
    		pageModification.setVisible(true);
    		pageTakeAttendence.setVisible(false);
    		pageViewAttendence.setVisible(false);
    		
    	}
        else if (event.getSource()==viewAtt_btn) {
    		pageModification.setVisible(false);
    		pageTakeAttendence.setVisible(false);
    		pageViewAttendence.setVisible(true);
    	}
    }
	
    @FXML
    private Button closeBtn;
    @FXML
    void closeWindow(ActionEvent event) {
    	Stage sc=(Stage) closeBtn.getScene().getWindow();
    	sc.close();
    }
    @FXML
    private Text username;
    
    void displayName(String str) {
    	username.setText(str);
    }
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/attendence";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "sanskar";
    
    @FXML
    private TableView<Student> tableViewCalAttendence;
    
    @FXML
    private TableColumn<Student, String> name= new TableColumn<>("NAME");
    
    @FXML
    private TableColumn<Student, Integer> present = new TableColumn<>("Present");
    
    @FXML
    private TableColumn<Student, Integer> absent = new TableColumn<>("Absent");
    
    @FXML
    private TableColumn<Student, Integer> total = new TableColumn<>("Total");
    
    @FXML
    private TableColumn<Student, Float> percentage = new TableColumn<>("Percentage");
    
    @FXML
    void calAttendence(MouseEvent event) {
    	tableViewCalAttendence.getItems().clear();
    	
    	String branch=choiceBranch1.getSelectionModel().getSelectedItem();
    	databaseOperations dbop=new databaseOperations();
		List<String> names = new ArrayList<>();
		names=dbop.returnNames(branch);
		
		List<Integer> Presents = new ArrayList<>();
		Presents=dbop.returnPresent(branch, names);
		
		List<Integer> Absents= new ArrayList<>();
		Absents=dbop.returnAbsent(branch, names);
			
		List<Integer> Total = new ArrayList<>();
		Total=dbop.returnTotal(Presents,Absents);
		
		List<Float> Percent = new ArrayList<>();
		Percent=dbop.returnPercentage(Presents, Total);
		int count=names.size();
		//System.out.println("Names = " + names.toString() + " Present = " + Presents.toString() + " Absents = " + Absents.toString() + " Total = " + Total.toString() + " Percentage = " + Percent.toString() + " count = " + count );
		
		ObservableList<Student> list= FXCollections.observableArrayList();	
		for (int i=0;i<count;i++) {
			list.add(new Student(names.get(i),Presents.get(i),Absents.get(i),Total.get(i),Percent.get(i)));
		}
		tableViewCalAttendence.setItems(list);
		tableViewCalAttendence.setStyle("-fx-font-weight:bolder; -fx-alignment: center;-fx-padding: 5px");
    }
    
    @FXML
    void clearTableCalAttendence(MouseEvent event) {
    	tableViewCalAttendence.getItems().clear();
    }
    
	@FXML
    private TableView<ObservableList<String>> tableViewAttendence;
    @FXML
    void clrTable(MouseEvent event) {
    	tableViewAttendence.getItems().clear();
    	tableViewAttendence.getColumns().clear();
    }
    
    @FXML
    private DatePicker AttDate;

    @FXML
    private TextField StuName;
    
    @FXML
    void getBranch(MouseEvent event) {
    	
    	tableViewAttendence.getColumns().clear();
    	tableViewAttendence.getItems().clear();
    	tableViewAttendence.setStyle(" -fx-border-width:2px; -fx-border-color:#a997df;");
    	
    	String date=String.valueOf(AttDate.getValue());
    	//System.out.println(date);
    	String brName=choiceBranch.getSelectionModel().getSelectedItem();
    	//System.out.println(brName);
    	String stuName = displayNames.getSelectionModel().getSelectedItem();
    	//String stuName = StuName.getText().replaceAll("\\s+", "");
    	//System.out.println(stuName + "Student Name");
    	// Connect to the database
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            Statement statement = connection.createStatement()) {
        	ResultSet rset=statement.executeQuery("SELECT COUNT(*) AS count FROM information_schema.tables WHERE table_name = '" + brName + "'");
        	rset.next();
            int count = rset.getInt("count");
            if (count > 0) {
            	if (date!="null" && stuName==null) { 
            		String query="SELECT * FROM " + brName + " where Date = '" + date + "'";
            		ResultSet rset1=statement.executeQuery(query);
            		if (rset1.next()) {
                		// Retrieve column names from the database
                        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + brName + " LIMIT 1");
                        ResultSetMetaData metaData = resultSet.getMetaData();
                        int columnCount = metaData.getColumnCount();
                        // Create TableColumn instances dynamically based on column names
                        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                            final int index = columnIndex; // for lambda expression
                            TableColumn<ObservableList<String>, String> column = new TableColumn<>(metaData.getColumnName(columnIndex));
                            column.setCellValueFactory(cellData -> {
                                ObservableList<String> row = cellData.getValue();
                                if (row != null && row.size() > index - 1) {
                                	return javafx.beans.binding.Bindings.createObjectBinding(() -> row.get(index - 1));
                                } else {
                                	return javafx.beans.binding.Bindings.createObjectBinding(() -> "");
                                }
                            });
                            column.setStyle("-fx-font-weight:bolder; -fx-text-alignment: center; -fx-border-width: 2px;");
                            tableViewAttendence.getColumns().add(column);
                        }
                        // Fetch data from the database and populate the TableView
                        resultSet = statement.executeQuery("SELECT * FROM " + brName + " where Date = '" + date + "'");
                        while (resultSet.next()) {
                            ObservableList<String> row = FXCollections.observableArrayList();
                            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                                row.add(resultSet.getString(columnIndex));
                            }
                            tableViewAttendence.getItems().add(row);
                            tableViewAttendence.setStyle("-fx-background-color:  #F8EDFF;-fx-padding: 5px; -fx-font-weight:bolder; -fx-text-alignment: center;");
                        }
            			//System.out.println("ture");
            		}
            		else {
            			String str="Attendence for " + date + " does not exists";
            			showError(str);
            		}
            	}
            	//----------------------------------------------------------------------------
            	else if(date=="null" && !(stuName==null)) {
            		DatabaseMetaData metaData1 = connection.getMetaData();
            		 try (ResultSet columns = metaData1.getColumns(null, null, brName, null)) {
                         Set<String> columnNames = new HashSet<>();
                         while (columns.next()) {
                             String columnName = columns.getString("COLUMN_NAME");
                             columnNames.add(columnName);
                          }
                         if (columnNames.contains(stuName)) {
                        	// Retrieve column names from the database
                             ResultSet resultSet = statement.executeQuery("SELECT date,"+stuName+" FROM " + brName + " LIMIT 1");
                             ResultSetMetaData metaData = resultSet.getMetaData();
                             int columnCount = metaData.getColumnCount();
                             // Create TableColumn instances dynamically based on column names
                             for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                                 final int index = columnIndex; // for lambda expression
                                 TableColumn<ObservableList<String>, String> column = new TableColumn<>(metaData.getColumnName(columnIndex));
                                 column.setCellValueFactory(cellData -> {
                                     ObservableList<String> row = cellData.getValue();
                                     if (row != null && row.size() > index - 1) {
                                     	return javafx.beans.binding.Bindings.createObjectBinding(() -> row.get(index - 1));
                                     } else {
                                     	return javafx.beans.binding.Bindings.createObjectBinding(() -> "");
                                     }
                                 });
                                 column.setStyle("-fx-font-weight:bolder; -fx-text-alignment: center; -fx-border-width: 2px;");
                                 tableViewAttendence.getColumns().add(column);
                             }
                             // Fetch data from the database and populate the TableView
                             resultSet = statement.executeQuery("SELECT Date,"+stuName+" FROM " + brName );
                             while (resultSet.next()) {
                                 ObservableList<String> row = FXCollections.observableArrayList();
                                 for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                                     row.add(resultSet.getString(columnIndex));
                                 }
                                 tableViewAttendence.getItems().add(row);
                                 tableViewAttendence.setStyle("-fx-background-color:  #F8EDFF;-fx-padding: 5px; -fx-font-weight:bolder; -fx-text-alignment: center;");
                             } 
                         }
                         else {
                        	String str="Attendence for Name" + stuName + " does not exists";
                        	showError(str);
                         }
            		 }	 
            	}
            	else if(date!="null" && !(stuName==null)) {
            		String query="SELECT " + stuName +" FROM " + brName + " where Date = '" + date + "'";
            		ResultSet rset2=statement.executeQuery(query);
            		if (rset2.next()) {
            			// Retrieve column names from the database
                        ResultSet resultSet = statement.executeQuery("SELECT Date,"+ stuName +" FROM " + brName + " LIMIT 1");
                        ResultSetMetaData metaData = resultSet.getMetaData();
                        int columnCount = metaData.getColumnCount();
                        // Create TableColumn instances dynamically based on column names
                        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                            final int index = columnIndex; // for lambda expression
                            TableColumn<ObservableList<String>, String> column = new TableColumn<>(metaData.getColumnName(columnIndex));
                            column.setCellValueFactory(cellData -> {
                                ObservableList<String> row = cellData.getValue();
                                if (row != null && row.size() > index - 1) {
                                	return javafx.beans.binding.Bindings.createObjectBinding(() -> row.get(index - 1));
                                } else {
                                	return javafx.beans.binding.Bindings.createObjectBinding(() -> "");
                                }
                            });
                            column.setStyle("-fx-font-weight:bolder; -fx-text-alignment: center; -fx-border-width: 2px;");
                            tableViewAttendence.getColumns().add(column);
                        }
                        // Fetch data from the database and populate the TableView
                        resultSet = statement.executeQuery("SELECT Date," + stuName +" FROM " + brName + " where Date = '" + date + "'");
                        while (resultSet.next()) {
                            ObservableList<String> row = FXCollections.observableArrayList();
                            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                                row.add(resultSet.getString(columnIndex));
                            }
                            tableViewAttendence.getItems().add(row);
                            tableViewAttendence.setStyle("-fx-background-color:  #F8EDFF;-fx-padding: 5px; -fx-font-weight:bolder; -fx-text-alignment: center;");
                        }
            			//System.out.println("ture");
            		}
            		else {
            			String str="Give correct StudentName and Date";
            			showError(str); 
            		}
            	}
            	
            	else {
            		// Retrieve column names from the database
                    ResultSet resultSet = statement.executeQuery("SELECT * FROM " + brName + " LIMIT 1");
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    // Create TableColumn instances dynamically based on column names
                    for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                        final int index = columnIndex; // for lambda expression
                        TableColumn<ObservableList<String>, String> column = new TableColumn<>(metaData.getColumnName(columnIndex));
                        column.setCellValueFactory(cellData -> {
                            ObservableList<String> row = cellData.getValue();
                            if (row != null && row.size() > index - 1) {
                            	return javafx.beans.binding.Bindings.createObjectBinding(() -> row.get(index - 1));
                            } else {
                            	return javafx.beans.binding.Bindings.createObjectBinding(() -> "");
                            }
                        });
                        column.setStyle("-fx-font-weight:bolder; -fx-text-alignment: center; -fx-border-width: 2px;");
                        tableViewAttendence.getColumns().add(column);
                    }
                    // Fetch data from the database and populate the TableView
                    resultSet = statement.executeQuery("SELECT * FROM " + brName);
                    while (resultSet.next()) {
                        ObservableList<String> row = FXCollections.observableArrayList();
                        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                            row.add(resultSet.getString(columnIndex));
                        }
                        tableViewAttendence.getItems().add(row);
                        tableViewAttendence.setStyle("-fx-background-color:  #F8EDFF;-fx-padding: 5px; -fx-font-weight:bolder; -fx-text-alignment: center;");
                    }
            	} 
            	
                
            } 
            
            else {
            	String str="Attendence for " + brName + " does not exists";
            	showError(str);
            }
            AttDate.setValue(null);
        	displayNames.getSelectionModel().clearSelection();
        } catch (SQLException e) {
            e.printStackTrace();}
    }
    @FXML
    private ChoiceBox<String> choiceBranch1;
    
    @FXML
    private ChoiceBox<String> choiceBranch;
    
    /*@FXML
    private ChoiceBox<String> choiceNames;*/
    

    @FXML
    private ChoiceBox<String> displayNames;
    
    
    public void UserType(String usrname,String pass) {
    	databaseOperations dbop=new databaseOperations();
    	if (!dbop.checkUser(usrname, pass)) {
    		modify_btn.setVisible(false);
    		takeAtt_btn.setVisible(false);
    	}
    }

    
    public void initialize(URL url, ResourceBundle resourceBundle) {
    	
    	
    	
    	
    	
    	databaseOperations dbop=new databaseOperations();
    	List<String> lst=new ArrayList<>();
    	lst=dbop.getTables("attendence");
    	

    	choiceBranch.getItems().addAll(lst);
    	//choiceBranch.getItems().addAll("MCA","MBA","BTech","MTech");
    	choiceBranch.setValue(lst.get(0));
    	choiceBranch1.getItems().addAll(lst);
    	//choiceBranch1.getItems().addAll("MCA","MBA","BTech","MTech");
        choiceBranch1.setValue(lst.get(0));
        /*
        choiceBranch.getSelectionModel().selectedItemProperty().addListener((observable,oldvalue,newvalue)->{
        	databaseOperations dbop=new databaseOperations();
        	String branch=newvalue;
        	List<String> names = dbop.returnNames(branch);
        	displayNames.getItems().addAll(names);
        	
        });*/
        
        choiceBranch.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            	databaseOperations dbop=new databaseOperations();
            	List<String> names=new ArrayList<>();
            	names=dbop.returnNames(newValue);
            	displayNames.getItems().clear();
            	displayNames.getItems().addAll(names);
            	
            }
        });
        
        
        selectBranchInsert.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            	databaseOperations dbop=new databaseOperations();
            	List<String> names=new ArrayList<>();
            	names=dbop.returnNames(newValue);
            	chocieStudName.getItems().clear();
                chocieStudName.getItems().addAll(names);  
                datePickUpdate.setValue(null);
                choiceStatus.getItems().clear();
                choiceStatus.getItems().addAll(Arrays.asList("P","A"));
            }
        });
        
        
        choiceStatus.getItems().addAll(Arrays.asList("P","A"));
        
        selectBranchMain.getItems().addAll(lst);
        selectBranchInsert.getItems().addAll(lst);
        selectBranchInsert.setValue(lst.get(0));
        selectBranchMain.setValue(lst.get(0));
        
        choiceBranchTakeAttendence.getItems().addAll(lst);
        choiceBranchTakeAttendence.setValue(lst.get(0));
        
    	tableViewCalAttendence.getColumns().addAll(name,present,absent,total,percentage);
    	name.setPrefWidth(300);
    	percentage.setPrefWidth(150);
    	
    	
    	enrolledStudents.setStyle(" -fx-border-width:2px; -fx-border-color:#a997df;");
    	tableTakeAttendence.setStyle(" -fx-border-width:2px; -fx-border-color:#a997df;");
    	tableViewAttendence.setStyle(" -fx-border-width:2px; -fx-border-color:#a997df;");
    	tableViewCalAttendence.setStyle(" -fx-border-width:2px; -fx-border-color:#a997df;");
    	//name.setStyle("-fx-background-color:red");
    	
    	name.setCellValueFactory(new PropertyValueFactory<Student,String>("name"));
    	name.setStyle("-fx-font-weight:bolder; -fx-alignment: cneter;");
    	
    	present.setCellValueFactory(new PropertyValueFactory<Student, Integer>("present"));
    	present.setStyle("-fx-font-weight:bolder; -fx-alignment: center;");
    	
    	absent.setCellValueFactory(new PropertyValueFactory<Student, Integer>("absent"));
    	absent.setStyle("-fx-font-weight:bolder; -fx-alignment: center;");
    	
    	total.setCellValueFactory(new PropertyValueFactory<Student, Integer>("total"));
    	total.setStyle("-fx-font-weight:bolder; -fx-alignment: center;");
    	
    	percentage.setCellValueFactory(new PropertyValueFactory<Student, Float>("percentage"));
    	percentage.setStyle("-fx-font-weight:bolder; -fx-alignment: center;");
    	
    	
    	tableTakeAttendence.getColumns().addAll(name1,attendence);
    	name1.setCellValueFactory(new PropertyValueFactory<studentAttendence,String>("name"));
    	attendence.setCellValueFactory(new PropertyValueFactory<studentAttendence,CheckBox>("attendence"));
    	name1.setStyle("-fx-font-weight:bolder; -fx-alignment: center;");
    	name1.setPrefWidth(380);
    	attendence.setStyle("-fx-font-weight:bolder; -fx-alignment: center;");
    	attendence.setPrefWidth(200);
    	
    	enrolledStudents.getColumns().addAll(name3);
    	name3.setCellValueFactory(new PropertyValueFactory<studentNames,String>("name"));
    	name3.setStyle("-fx-font-weight:bolder; -fx-alignment: center;");
    	name3.setPrefWidth(350);
    	
    	Currentbranches.getColumns().add(branch);
    	branch.setCellValueFactory(new PropertyValueFactory<Branches,String>("branch"));
    	branch.setStyle("-fx-font-weight:bolder; -fx-alignment: center;");
    	branch.setPrefWidth(256);
    	
    	
    }
    
    //-------------------------------------------------------------------------------------------------------
    
    @FXML
    private DatePicker datePickerTakeAtt;
    

    @FXML
    private ChoiceBox<String> choiceBranchTakeAttendence;
    
    @FXML
    private TableView<studentAttendence> tableTakeAttendence;
    
    @FXML
    private TableColumn<studentAttendence, String> name1= new TableColumn<>("NAME");
    
    @FXML
    private TableColumn<studentAttendence, CheckBox> attendence = new TableColumn<>("SELECT");
    

    @FXML
    void takeAttendence(MouseEvent event) {
    	tableTakeAttendence.getItems().clear();
    	
    	if(datePickerTakeAtt.getValue()==null) {
    		String str="Give a Date value";
    		showError(str);
    	}
    	else {
    		String date=datePickerTakeAtt.getValue().toString();
    		String branch=choiceBranchTakeAttendence.getSelectionModel().getSelectedItem();
        	databaseOperations dbop=new databaseOperations();
    		List<String> names = new ArrayList<>();
    		names=dbop.returnNames(branch);
         	int count=names.size();
         	if (dbop.checkDuplicateDates(branch, date)) {
        		String str="Error!! Dupicate Insertion of records";
             	showError(str); 	
        	}
         	else {
        		ObservableList<studentAttendence> list= FXCollections.observableArrayList();	
        		for (int i=0;i<count;i++) {
        			CheckBox ch=new CheckBox();
        			list.add(new studentAttendence(names.get(i),ch));
        		}
        		tableTakeAttendence.getItems().addAll(list);
        		attendence.setText(datePickerTakeAtt.getValue().toString());
         		
         	}
    	}

        	
		
    }
    @FXML
    void submitAttendence(MouseEvent event) {
    	databaseOperations dbop=new databaseOperations();
    	String branch=choiceBranchTakeAttendence.getSelectionModel().getSelectedItem();
    	ObservableList<studentAttendence> list=tableTakeAttendence.getItems();
    	List<String> presentees=new ArrayList<>();
    	String date=datePickerTakeAtt.getValue().toString();
    	presentees.add(date);
    	for (studentAttendence stu:list) {
    		if (stu.getAttendence().isSelected()) {
    			presentees.add("P");
    		}
    		else {
    			presentees.add("A");
    		}
    	}
    	
    	//System.out.println(presentees.toString());
    	String value="(";
    	int count=presentees.size();
    	for(int i=0;i<count;i++) {
    		if (i==count-1) {
    			value=value+ "'" +presentees.get(i)+"')";
    		}
    		else {
    			value=value+ "'" +presentees.get(i)+"',";
    		}
    	}
    	if (dbop.checkDuplicateDates(branch, date)) {
    		String str="Error!! Dupicate Insertion of records";
    		showError(str); 
    		
    	}
    	else {
    		if (dbop.submitAttendence(branch, value)) {
    			
    			String str="Attendence Submitted Successfully";
    			showMessage(str);
    			tableTakeAttendence.getItems().clear();
        	}
        	else {
        		String str="Error in Submission of Attendence";
        		showError(str);
        	}
    	}
    	
    	//System.out.println(value);
    }

    @FXML
    void clearTableTakeAttendence(MouseEvent event) {
    	tableTakeAttendence.getItems().clear();
    }
    
    
    //-----------------------------------------------------------------------------------------------

    @FXML
    private TableView<studentNames> enrolledStudents;
    
    @FXML
    private TableColumn<studentNames, String> name3= new TableColumn<>("NAME");
    
    
    @FXML
    private ChoiceBox<String> selectBranchInsert;

    @FXML
    private ChoiceBox<String> selectBranchMain;


    @FXML
    void clearRecord(MouseEvent event) {
    	enrolledStudents.getItems().clear();
    }
    @FXML
    void showRecord(MouseEvent event) {
    	enrolledStudents.getItems().clear();
    	databaseOperations dbop=new databaseOperations();
    	List<String> names=new ArrayList<>();
    	String branch=selectBranchMain.getSelectionModel().getSelectedItem();
    	names=dbop.returnNames(branch);
    	//System.out.println(names.toString());
    	int count=names.size();
    	ObservableList<studentNames> list= FXCollections.observableArrayList();	
		for (int i=0;i<count;i++) {
			list.add(new studentNames(names.get(i)));
		}
		enrolledStudents.setItems(list);	
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
    private AnchorPane AnchoreInsertStudent;

    @FXML
    private Button InsertBranch_btn;

    @FXML
    private Button btn_addStudent;

    @FXML
    private ChoiceBox<String> chocieStudName;

    @FXML
    private ChoiceBox<String> choiceStatus;

    @FXML
    private DatePicker datePickUpdate;

    @FXML
    private TextField textStuName;

    @FXML
    private Button deleteBranch_btn;

    @FXML
    private Button delete_btn;
    
    @FXML
    private TextField getBranchName;

    @FXML
    private Text insertText;

    @FXML
    private Text textBranch;

    @FXML
    private Text textBranchEnter;

    @FXML
    private Text textSelectDate;

    @FXML
    private Text textSelectStudent;
 
    @FXML
    private Text textStatus;
 
    @FXML
    private Button update_btn;
    
    @FXML
    private Button show1;

    @FXML
    private Button show2;

    @FXML
    private Button show3;

    @FXML
    private Button show4;

    @FXML
    private Button show5;

    @FXML
    void showThings(ActionEvent event) {
    	if (event.getSource()==show1) {
    		selectBranchInsert.setVisible(true);
    		textBranch.setVisible(true);
    		insertText.setVisible(true);
    		textStuName.setVisible(true);
    		btn_addStudent.setVisible(true);
    		
    	    InsertBranch_btn.setVisible(false);
    	    update_btn.setVisible(false);
    	    deleteBranch_btn.setVisible(false);
    	    delete_btn.setVisible(false);
    		getBranchName.setVisible(false);
    		textBranchEnter.setVisible(false);
    		textSelectDate.setVisible(false);
    		textSelectStudent.setVisible(false);
    		textStatus.setVisible(false);
    		chocieStudName.setVisible(false);
    		choiceStatus.setVisible(false);
    		datePickUpdate.setVisible(false);
    	}
    	else if (event.getSource()==show2) {
    		selectBranchInsert.setVisible(true);
    		textBranch.setVisible(true);
    		chocieStudName.setVisible(true);
    		textSelectDate.setVisible(true);
    		textSelectStudent.setVisible(true);
    		datePickUpdate.setVisible(true);
            textStatus.setVisible(true);
    		choiceStatus.setVisible(true);
    		update_btn.setVisible(true);
    
    		insertText.setVisible(false);
    		textStuName.setVisible(false);
    		btn_addStudent.setVisible(false);
    	    InsertBranch_btn.setVisible(false);	    
    	    deleteBranch_btn.setVisible(false);
    	    delete_btn.setVisible(false);
    		getBranchName.setVisible(false);
    		textBranchEnter.setVisible(false);
    		
    	}
    	else if(event.getSource()==show3) {
    		selectBranchInsert.setVisible(true);
    		textBranch.setVisible(true);
    		chocieStudName.setVisible(true);
    		delete_btn.setVisible(true);
    		textSelectStudent.setVisible(true);
    		
    		insertText.setVisible(false);
    		textStuName.setVisible(false);
    		btn_addStudent.setVisible(false);
    	    InsertBranch_btn.setVisible(false);
    	    update_btn.setVisible(false);
    	    deleteBranch_btn.setVisible(false);
    		getBranchName.setVisible(false);
    		textBranchEnter.setVisible(false);
    		textSelectDate.setVisible(false);
    		textStatus.setVisible(false);
    		choiceStatus.setVisible(false);
    		datePickUpdate.setVisible(false);
    		
    	}
    	else if (event.getSource()==show5) {
    		
    		selectBranchInsert.setVisible(true);
    		textBranch.setVisible(true);
    		deleteBranch_btn.setVisible(true);
    		
    		chocieStudName.setVisible(false);
    		delete_btn.setVisible(false);
    		textSelectStudent.setVisible(false);
    		insertText.setVisible(false);
    		textStuName.setVisible(false);
    		btn_addStudent.setVisible(false);
    	    InsertBranch_btn.setVisible(false);
    	    update_btn.setVisible(false);
    		getBranchName.setVisible(false);
    		textBranchEnter.setVisible(false);
    		textSelectDate.setVisible(false);
    		textStatus.setVisible(false);
    		choiceStatus.setVisible(false);
    		datePickUpdate.setVisible(false);
    	
    	
    	}
    	else if (event.getSource()==show4) {

    	    InsertBranch_btn.setVisible(true);
    		getBranchName.setVisible(true);
    		textBranchEnter.setVisible(true);
    		selectBranchInsert.setVisible(false);
    		textBranch.setVisible(false);
    		deleteBranch_btn.setVisible(false);
    		
    		chocieStudName.setVisible(false);
    		delete_btn.setVisible(false);
    		textSelectStudent.setVisible(false);
    		insertText.setVisible(false);
    		textStuName.setVisible(false);
    		btn_addStudent.setVisible(false);
    	    update_btn.setVisible(false);    		
    		textSelectDate.setVisible(false);
    		textStatus.setVisible(false);
    		choiceStatus.setVisible(false);
    		datePickUpdate.setVisible(false);
    	}

    }
    
    @FXML
    void updateStudent(MouseEvent event) {
    	String branch=selectBranchInsert.getSelectionModel().getSelectedItem();
    	String name=chocieStudName.getSelectionModel().getSelectedItem();
    	String date=String.valueOf(datePickUpdate.getValue());
    	
    	String status=choiceStatus.getSelectionModel().getSelectedItem();
    	
    	if (date=="null" && name==null && status==null){
    		String str="Enter Date, Name and Status Entries!!";
    		showError(str);
    	}
    	else if (date=="null" && name!=null && status==null) {
    		String str="Enter Date and Status Entries!!";
    		showError(str);
    	}
    	else if (date=="null" && name==null && status!=null) {
    		String str="Enter Date and Name Entries!!";
    		showError(str);
    	}
    	else if (date!="null" && name==null && status==null) {
    		String str="Enter Name and Status Entries!!";
    		showError(str);
    	}
    	else if (date=="null" && name!=null && status!=null) {
    		String str="Enter Date Entry!!";
    		showError(str);
    	}
    	else if (date!="null" && name==null && status!=null) {
    		String str="Enter Name Entry!!";
    		showError(str);
    	}
    	else if (date!="null" && name!=null && status==null) {
    		String str="Enter Status Entry!!";
    		showError(str);
    	}
    	else {
    		databaseOperations dbop=new databaseOperations();
    		if (dbop.checkHasDate(branch, date)) {
    			dbop.updateValues(branch, date, status, name);
    			String str="Record Updated Successfully!!";
				showMessage(str);
    		}
    		else {
    			String str="Record for the date = "+date +" does not exists";
    			showError(str);
    		}
    		
    	}
    	clearALL();
    	initialize();
    	System.out.printf("Branch = %s Name = %s Date =%s Status = %s\n",branch,name,date,status);
    	
    }
    
    @FXML
    void addBranch(MouseEvent event) {
    	String branch=getBranchName.getText().replaceAll("\\s+", "").toUpperCase();
    	databaseOperations dbop=new databaseOperations();
    	List<String> branches=new ArrayList<>();
    	branches =dbop.getTables("attendence");
    	if (branch.isEmpty()) {
    		String str="Insert a Name of the Branch";
    		showError(str);
    	}
    	else {
    		if(branches.contains(branch)) {
    			String str="Branch "+ branch + " already exits !!";
    			showError(str);
    		}
    		else {
    			dbop.addBranch(branch);
    			String str="Branch Added Successfully!!";
				showMessage(str);
    		}
    	}
    	showBranches(event);
    	clearALL();
    	initialize();
    }

 
    @FXML
    void deleteBranch(MouseEvent event) {
    	databaseOperations dbop=new databaseOperations();
    	String branch=selectBranchInsert.getSelectionModel().getSelectedItem();
    	dbop.deleteBranch(branch);
    	String str="Successfully delete branch "+branch;
    	showMessage(str);
    	showBranches(event);
    	clearALL();
    	initialize();
    }
    
    void getVals(String str) {
    	enrolledStudents.getItems().clear();
    	databaseOperations dbop=new databaseOperations();
    	List<String> names=new ArrayList<>();
    	String branch=str;
    	names=dbop.returnNames(branch);
    	//System.out.println(names.toString());
    	int count=names.size();
    	ObservableList<studentNames> list= FXCollections.observableArrayList();	
		for (int i=0;i<count;i++) {
			list.add(new studentNames(names.get(i)));
		}
		enrolledStudents.setItems(list);
    }
    
    @FXML
    void addStudent(MouseEvent event) {
    	String branch=selectBranchInsert.getSelectionModel().getSelectedItem();
    	String name=textStuName.getText();
    	databaseOperations dbop=new databaseOperations();
    	if (dbop.checkDuplicateNames(branch, name)) {
    		String str="Name : "+name+ " already exists in records";
    		showError(str);
    	}
    	else {
    		dbop.addStudent(branch, name);
    		getVals(branch);
    		String str="Student Added Successfully!!";
			showMessage(str); 		
    	}
    	
    	clearALL();
    	initialize();
    	
    }
    @FXML
    void deleteStudent(MouseEvent event) {
    	String branch=selectBranchInsert.getSelectionModel().getSelectedItem();
    	String name=chocieStudName.getSelectionModel().getSelectedItem();
    	databaseOperations dbop=new databaseOperations();
    	if(name==null) {
    		String str="Select Student name from the list";
    		showError(str);
    	}
    	else {
    		dbop.deleteStudent(branch, name);
    		getVals(branch);
    		String str="Record Deleted Successfully!!";
			showMessage(str);
    		
    	}
    	
    	clearALL();
    	initialize();
    }
    
    @FXML
    public void clearALL() {
    	choiceBranch.getItems().clear();
    	choiceBranch1.getItems().clear();
    	displayNames.getItems().clear();
    	chocieStudName.getItems().clear();
    	
    	choiceStatus.getItems().clear();
    	
        selectBranchMain.getItems().clear();
        selectBranchInsert.getItems().clear();     
        choiceBranchTakeAttendence.getItems().clear();
        
    	tableViewCalAttendence.getColumns().clear();
    	tableTakeAttendence.getColumns().clear();
    	enrolledStudents.getColumns().clear();
    	
    }
    @FXML
    void initialize() {
    	databaseOperations dbop=new databaseOperations();
    	List<String> lst=new ArrayList<>();
    	
    	lst=dbop.getTables("attendence");
    	System.out.println("In initialise " +lst.toString());
        selectBranchMain.getItems().addAll(lst);
        
        selectBranchInsert.getItems().addAll(lst);
        
        selectBranchInsert.setValue(lst.get(0));
        
        selectBranchMain.setValue(lst.get(0));
        
        choiceBranchTakeAttendence.getItems().addAll(lst);
        choiceBranchTakeAttendence.setValue(lst.get(0));
        
    	choiceBranch.getItems().addAll(lst);
    	//choiceBranch.getItems().addAll("MCA","MBA","BTech","MTech");
    	choiceBranch.setValue(lst.get(0));
    	choiceBranch1.getItems().addAll(lst);
    	//choiceBranch1.getItems().addAll("MCA","MBA","BTech","MTech");
        choiceBranch1.setValue(lst.get(0));
        /*
        choiceBranch.getSelectionModel().selectedItemProperty().addListener((observable,oldvalue,newvalue)->{
        	databaseOperations dbop=new databaseOperations();
        	String branch=newvalue;
        	List<String> names = dbop.returnNames(branch);
        	displayNames.getItems().addAll(names);
        	
        });*/
        
        choiceBranch.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            	databaseOperations dbop=new databaseOperations();
            	List<String> names=new ArrayList<>();
            	names=dbop.returnNames(newValue);
            	displayNames.getItems().clear();
            	displayNames.getItems().addAll(names);
            	
            }
        });
        
        
        selectBranchInsert.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            	databaseOperations dbop=new databaseOperations();
            	List<String> names=new ArrayList<>();
            	names=dbop.returnNames(newValue);
            	chocieStudName.getItems().clear();
                chocieStudName.getItems().addAll(names);  
                datePickUpdate.setValue(null);
                choiceStatus.getItems().clear();
                choiceStatus.getItems().addAll(Arrays.asList("P","A"));
            }
        });
        
        
        choiceStatus.getItems().addAll(Arrays.asList("P","A"));
        
        
    	tableViewCalAttendence.getColumns().addAll(name,present,absent,total,percentage);
    	name.setPrefWidth(300);
    	percentage.setPrefWidth(150);
    	//name.setStyle("-fx-background-color:red");
    	name.setCellValueFactory(new PropertyValueFactory<Student,String>("name"));
    	name.setStyle("-fx-font-weight:bolder; -fx-alignment: cneter;");
    	present.setCellValueFactory(new PropertyValueFactory<Student, Integer>("present"));
    	present.setStyle("-fx-font-weight:bolder; -fx-alignment: center;");
    	absent.setCellValueFactory(new PropertyValueFactory<Student, Integer>("absent"));
    	absent.setStyle("-fx-font-weight:bolder; -fx-alignment: center;");
    	total.setCellValueFactory(new PropertyValueFactory<Student, Integer>("total"));
    	total.setStyle("-fx-font-weight:bolder; -fx-alignment: center;");
    	percentage.setCellValueFactory(new PropertyValueFactory<Student, Float>("percentage"));
    	percentage.setStyle("-fx-font-weight:bolder; -fx-alignment: center;");
    	
    	
    	tableTakeAttendence.getColumns().addAll(name1,attendence);
    	name1.setCellValueFactory(new PropertyValueFactory<studentAttendence,String>("name"));
    	attendence.setCellValueFactory(new PropertyValueFactory<studentAttendence,CheckBox>("attendence"));
    	name1.setStyle("-fx-font-weight:bolder; -fx-alignment: center;");
    	name1.setPrefWidth(380);
    	attendence.setStyle("-fx-font-weight:bolder; -fx-alignment: center;");
    	attendence.setPrefWidth(200);
    	
    	enrolledStudents.getColumns().addAll(name3);
    	name3.setCellValueFactory(new PropertyValueFactory<studentNames,String>("name"));
    	name3.setStyle("-fx-font-weight:bolder; -fx-alignment: center;");
    	name3.setPrefWidth(350);
    	

    	
    }
    
    @FXML
    private Button signOutBtn;
    
	private double x=0;
	private double y=0;


    @FXML
    void signOut(ActionEvent event) throws IOException {
    	Stage currentStage1 = (Stage) signOutBtn.getScene().getWindow();
    	currentStage1.close();
    	
    	
    	FXMLLoader fxmlLoader1 = new FXMLLoader(Main.class.getResource("LoginPage.fxml"));
        Scene scene1 = new Scene(fxmlLoader1.load());
        loginPageController controller1=fxmlLoader1.getController();
        Stage stage1=new Stage();
        stage1.initStyle(StageStyle.TRANSPARENT);
        scene1.setOnMousePressed( (event1)->{
        	x=event1.getSceneX();
        	y=event1.getSceneY();
        });
        scene1.setOnMouseDragged( (event1)->{
            stage1.setX(event1.getScreenX() - x);
            stage1.setY(event1.getScreenY() - y);
        });
        stage1.setScene(scene1);
        stage1.show();
    }
    
    @FXML
    private TableView<Branches> Currentbranches;
    
    
    @FXML
    private TableColumn<Branches, String> branch = new TableColumn<>("Branches");
    
    
    @FXML
    void showBranches(MouseEvent event) {
    	Currentbranches.getItems().clear();
    	databaseOperations dbop=new databaseOperations();
    	List<String> branches=new ArrayList<>();
    	branches=dbop.getTables("attendence");
    	//System.out.println(branches.toString());
    	
    	ObservableList<Branches> list= FXCollections.observableArrayList();
    	int count=branches.size();
    	
    	for (int i=0;i<count;i++) {
    		list.add(new Branches(branches.get(i)));
    	}
    	//System.out.println(list.toString());
    	Currentbranches.getItems().addAll(list);
    }
    
    @FXML
    void clearBranches(MouseEvent event) {
    	Currentbranches.getItems().clear();
    }

}

