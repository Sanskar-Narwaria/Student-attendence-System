package application;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mysql.cj.x.protobuf.MysqlxCrud.Collection;
public class databaseOperations {
	public boolean checkUser(String usrname,String passwd) {
		try{
            String url = "jdbc:mysql://localhost:3306/";
            String db="javaprojectattendence";
            String username = "root";
            String password = "sanskar";
            Connection conn = DriverManager.getConnection(url+db, username, password);
            String query="select user from logincredentials where username = '" + usrname + "' and password = '" + passwd + "'"; 
            Statement stm=conn.createStatement();
            ResultSet rs=stm.executeQuery(query);
            String userType="";
            if (rs.next()) {
            	userType=rs.getString(1);
            }
            System.out.println("user type = "+ userType);
            if(userType.equals("Admin")) {
            	return true;
            }
            conn.close();
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
		return false;
	}
	public boolean checkCredentials(String usrname,String passwd) {
		try{
            String url = "jdbc:mysql://localhost:3306/";
            String db="javaprojectattendence";
            String username = "root";
            String password = "sanskar";
            Connection conn = DriverManager.getConnection(url+db, username, password);
            String query="select * from logincredentials where username = '" + usrname + "' and password = '" + passwd + "'";
            
            Statement stm=conn.createStatement();
            ResultSet rs=stm.executeQuery(query);
            if(rs.next()) {
            	conn.close();
            	return true;
        }
        }
        catch(Exception e){
            e.printStackTrace();
            //return false;
        }
		return false;
	}

	public void insertSignUpDetails(String urname,String pass,String user) {
		try{
            String url = "jdbc:mysql://localhost:3306/";
            String db="javaprojectattendence";
            String username = "root";
            String password = "sanskar";
            Connection conn = DriverManager.getConnection(url+db, username, password);
            String query="INSERT INTO logincredentials values ('" + urname +"',"+"'"+pass+"',"+"'"+user+"')";
            
            Statement stm=conn.createStatement();
            stm.executeUpdate(query);
            conn.close();
        }
        catch(Exception e){
            e.printStackTrace();
            //return false;
        }
	}
	
	
	
	public List<String> returnNames(String branch){
		try{
            String url = "jdbc:mysql://localhost:3306/";
            String db="attendence";
            String username = "root";
            String password = "sanskar";
            Connection conn = DriverManager.getConnection(url+db, username, password);
            String query="SELECT * from " + branch;
            Statement stm=conn.createStatement();
            ResultSet rs=stm.executeQuery(query);
            ResultSetMetaData metadata=rs.getMetaData();
            int columnCount=metadata.getColumnCount();
            List<String> lst=new ArrayList<>();
            
            for (int i=2;i<=columnCount;i++) {	
            	lst.add(metadata.getColumnName(i));
            }
            conn.close();
            return lst;
        }
        catch(Exception e){
            e.printStackTrace();
        }
		return new ArrayList<>();
	}
	
	public List<Integer> returnPresent(String branch, List<String> names){
		try{
			List <Integer> lst=new ArrayList<>();
            String url = "jdbc:mysql://localhost:3306/";
            String db="attendence";
            String username = "root";
            String password = "sanskar";
            Connection conn = DriverManager.getConnection(url+db, username, password);
            for(String str:names) {
            	Statement stm=conn.createStatement();
            	String query="SELECT count(" +str+ ") as Count from  "+ branch +" where " + str + " = 'P' "  ;
            	
            	ResultSet resultSet=stm.executeQuery(query);
            	if (resultSet.next()) {
            		lst.add(resultSet.getInt(1));
            	}
            }
            conn.close();
            return lst;
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
		return new ArrayList<>();
	}
	public List<Integer> returnAbsent(String branch, List<String> names){
		try{
			List <Integer> lst=new ArrayList<>();
            String url = "jdbc:mysql://localhost:3306/";
            String db="attendence";
            String username = "root";
            String password = "sanskar";
            Connection conn = DriverManager.getConnection(url+db, username, password);
            for(String str:names) {
            	Statement stm=conn.createStatement();
            	String query="SELECT count(" +str+ ") as Count from  "+ branch +" where " + str + " = 'A' "  ;
            	
            	ResultSet resultSet=stm.executeQuery(query);
            	if (resultSet.next()) {
            		lst.add(resultSet.getInt(1));
            	}
            }
            conn.close();
            return lst;
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
		return new ArrayList<>();
	}
	public List<Integer> returnTotal(List<Integer> Present ,List<Integer> Absent){
		List<Integer> lst=new ArrayList<>();
		int count= Present.size();
		for (int i=0;i<count;i++) {
			lst.add(Present.get(i) + Absent.get(i));
		}
		return lst;
	}
	
	public List<Float> returnPercentage(List<Integer> Present ,List<Integer> Total){
		List<Float> lst=new ArrayList<>();
		int count= Present.size();
		for (int i=0;i<count;i++) {
			float per= ((float)Present.get(i)) / Total.get(i);
			String formattedNum = String.format("%.2f", per*100);
			float num=Float.parseFloat(formattedNum);
			lst.add(num); 
		}
		return lst;		
	}
	public List<String> getTables(String dbname){
		try{
            String url = "jdbc:mysql://localhost:3306/";
            String db=dbname;
            String username = "root";
            String password = "sanskar";
            Connection conn = DriverManager.getConnection(url+db, username, password);
            String query="SHOW TABLES";
            Statement stm=conn.createStatement();
            ResultSet rs=stm.executeQuery(query);
            List<String> lst=new ArrayList<>();
            while(rs.next()) {
            	lst.add(rs.getString(1).toUpperCase());
            }
            conn.close();
            return lst;
            
        }
        catch(Exception e){
            e.printStackTrace();
            //return false;
        }
		return new ArrayList<>();
	}
	
	public boolean submitAttendence(String branch,String vals) {
		try{
            String url = "jdbc:mysql://localhost:3306/";
            String db="attendence";
            String username = "root";
            String password = "sanskar";
            Connection conn = DriverManager.getConnection(url+db, username, password);
            String query="INSERT INTO "+ branch + " values "+vals; 
            Statement stm=conn.createStatement();
            int rowAffected=stm.executeUpdate(query);
            if(rowAffected>0) {
            	conn.close();
            	return true;
            }
            else {
            	conn.close();
            	return false;
            	
             }
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
	  }
	public boolean checkDuplicateDates(String branch,String date){
		try{
            String url = "jdbc:mysql://localhost:3306/";
            String db="attendence";
            String username = "root";
            String password = "sanskar";
            Connection conn = DriverManager.getConnection(url+db, username, password);
            String query="SELECT * FROM "+ branch + " where date = '"+ date +"'";
            Statement stm=conn.createStatement();
            ResultSet rset=stm.executeQuery(query);
            if(rset.next()) {
            	conn.close();
            	return true;
            }
            else {
            	conn.close();
            	return false;
            	
             }
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
	}
	

	public boolean checkHasDate(String branch,String date) {
		try{
            String url = "jdbc:mysql://localhost:3306/";
            String db="attendence";
            String username = "root";
            String password = "sanskar";
            Connection conn = DriverManager.getConnection(url+db, username, password);
            String query="SELECT * FROM "+ branch + " where date = '"+ date +"'";
            Statement stm=conn.createStatement();
            ResultSet rset=stm.executeQuery(query);
            if(rset.next()) {
            	conn.close();
            	return true;
            }
            else {
            	conn.close();
            	return false;
            	
             }
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
		
	}
	
	public void updateValues(String branch,String date,String status,String name) {
		try{
			
            String url = "jdbc:mysql://localhost:3306/";
            String db="attendence";
            String username = "root";
            String password = "sanskar";
            Connection conn = DriverManager.getConnection(url+db, username, password);
            String query= "UPDATE " + branch + " SET "+ name + "='" + status + "' where date='"+ date +"'";
            System.out.println(query);
            
            Statement stm=conn.createStatement();
            int rowAffected=stm.executeUpdate(query);
            
             
        }
        catch(Exception e){
            e.printStackTrace();
            
        }
		
	}
	public boolean checkDuplicateNames(String branch,String name) {
            String stuName = name.replaceAll("\\s+", "");
            List<String> names=new ArrayList<>();
            names=returnNames(branch);
            if (names.contains(stuName)) {
            	return true;
            }
            else {
            	return false;
            }
       }
	
	
	public void deleteStudent(String branch,String name) {
		try{
            String url = "jdbc:mysql://localhost:3306/";
            String db="attendence";
            String username = "root";
            String password = "sanskar";
            Connection conn = DriverManager.getConnection(url+db, username, password);

            String query="Alter table "+branch + " drop column "+ name;
            
            Statement stm=conn.createStatement();
            int rowAffected=stm.executeUpdate(query);
            conn.close();
        }
        catch(Exception e){
            e.printStackTrace();
            
        }
		
	}
		
	public void addBranch(String branch) {
		try{
            String url = "jdbc:mysql://localhost:3306/";
            String db="attendence";
            String username = "root";
            String password = "sanskar";
            Connection conn = DriverManager.getConnection(url+db, username, password);
            
            String query="Create table "+ branch + "(Date date not null unique)";
            
            Statement stm=conn.createStatement();
            int rowAffected=stm.executeUpdate(query);
            conn.close();
        }
        catch(Exception e){
            e.printStackTrace(); 
        }
	}
	
	
	public void deleteBranch(String branch) {
		try{
            String url = "jdbc:mysql://localhost:3306/";
            String db="attendence";
            String username = "root";
            String password = "sanskar";
            Connection conn = DriverManager.getConnection(url+db, username, password);
            
            String query="drop table "+ branch;
            
            Statement stm=conn.createStatement();
            int rowAffected=stm.executeUpdate(query);
            conn.close();
        }
        catch(Exception e){
            e.printStackTrace(); 
        }
		
	}
	public void addStudent(String branch,String name) {
		try{
            String url = "jdbc:mysql://localhost:3306/";
            String db="attendence";
            String username = "root";
            String password = "sanskar";
            Connection conn = DriverManager.getConnection(url+db, username, password);
            String stuName = name.replaceAll("\\s+", "");
            List<String> names=new ArrayList<>();
            names=returnNames(branch);
            
            names.add(stuName);
            Collections.sort(names);
            System.out.println(names.toString());
            int loc=names.indexOf(stuName);
            System.out.println("Location = "+loc);
            String query;
            if (loc>0) {
            	String previousName=names.get(loc -1);
                query="Alter table "+ branch + " Add Column "+ stuName + " char(1) default 'A' check( "+ stuName + " in ('P','A')) after " + previousName;
            }
            else {
            	
                query="Alter table "+ branch + " Add Column "+ stuName + " char(1) default 'A' check( "+ stuName + " in ('P','A')) after date";
            }
            
            Statement stm=conn.createStatement();
            int rowAffected=stm.executeUpdate(query);
            conn.close();
        }
        catch(Exception e){
            e.printStackTrace();
            
        }
	}
	
}
