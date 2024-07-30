package application;

public class Student {
	private String name;
	private int present;
	private int absent;
	private int total;
	private float percentage;
	public Student(String name,int present,int absent,int total,float percentage) {
		this.name=name;
		this.present=present;
		this.absent=absent;
		this.total=total;
		this.percentage=percentage;
		// TODO Auto-generated constructor stub
	}
	public int getAbsent() {
		return absent;
	}
	public String getName() {
		return name;
	}
	public float getPercentage() {
		return percentage;
	}
	public int getPresent() {
		return present;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public void setPresent(int present) {
		this.present = present;
	}
	public void setPercentage(float percentage) {
		this.percentage = percentage;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setAbsent(int absent) {
		this.absent = absent;
	}
	
	
}
