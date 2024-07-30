package application;

import javafx.scene.control.CheckBox;

public class studentAttendence {
	private String name;
	private CheckBox attendence;
	public studentAttendence(String name) {
		this.name=name;
	}
	public studentAttendence(String name,CheckBox attendence) {
		this.name=name;
		this.attendence=attendence;
		// TODO Auto-generated constructor stub
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public CheckBox getAttendence() {
		return attendence;
	}
	public void setAttendence(CheckBox attendence) {
		this.attendence = attendence;
	}
}
