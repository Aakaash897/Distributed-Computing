package Records;

public class TeacherRecord extends Record{

	private String address;
	private String phone;
	private String specialization;  //use enum
	private String location;  //use enum
	
	
	public TeacherRecord(String firstName, String lastName) {
		super(firstName, lastName);
		this.recordID = "TR"+Integer.toString(Record.baseID++);
	}
	
	public TeacherRecord() {
		this("N/A", "N/A");
	}

	public TeacherRecord(String firstName, String lastName, String address, String phone,String special, String loc) {
		super(firstName, lastName);
		this.recordID = "TR"+Integer.toString(Record.baseID++);
        this.address = address;
        this.phone = phone;
        this.specialization = special;
        this.location = loc;
	}
	
	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getRecordID(){
		return recordID;
	}

	public void setRecordID(String recordID) {
		this.recordID = recordID;
	}

	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	public String getSpecialization() {
		return specialization;
	}


	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}


	public String getLocation() {
		return location;
	}


	public void setLocation(String location) {
		this.location = location;
	}
}
