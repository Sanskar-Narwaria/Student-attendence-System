package application;

import java.util.ArrayList;
import java.util.List;

public class controller2 {
	public static void main(String[] args) {
		databaseOperations dbop=new databaseOperations();
		List<String> names = new ArrayList<>();
		names=dbop.returnNames("mca");
		System.out.println("Names = " +names.toString());
		
		List<Integer> Presents = new ArrayList<>();
		Presents=dbop.returnPresent("mca", names);
		System.out.println("Total Presents = " + Presents.toString());
		
		List<Integer> Absents= new ArrayList<>();
		Absents=dbop.returnAbsent("mca", names);
		System.out.println("Total Absents = " + Absents.toString());
		
		List<Integer> Total = new ArrayList<>();
		Total=dbop.returnTotal(Presents,Absents);
		System.out.println("Total Attendence = " + Total.toString());
		
		List<Float> Percentage = new ArrayList<>();
		Percentage=dbop.returnPercentage(Presents, Total);
		System.out.println("Percentage = " + Percentage.toString());
		
		List<String> tables = new ArrayList<>();
		tables=dbop.getTables("attendence");
		System.out.println("Names = " +tables.toString());
		
		dbop.addStudent("MCA", "Prashant Pasi");
		
		dbop.updateValues("MCA", "2024-03-05","P", "Sanskar");
	}
}
