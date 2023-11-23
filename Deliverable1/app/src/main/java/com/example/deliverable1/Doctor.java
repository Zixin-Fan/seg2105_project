package com.example.deliverable1;
import java.util.ArrayList;
import java.util.Arrays;

/*
 * Doctor class will store the information of doctor including their employeeNumber and specialization
 * Doctor extends the User class which has the variables to store info
 * like doctor name, Contact info(Email , Phone & Address), Account password
 * This class provides method to access, manage or update information related to a doctor 
 * @author Mukhveer Kaur
 * @author's Student ID -300317874
 * 
 */

public class Doctor extends User {
	private String employeeNumber;
	private ArrayList<String> specialization;


	/*
	 * Creates a new doctor with specified parameters 
	 * this constructor will call back the super constructor of User class
	 * @param firstName is firstName of a doctor
	 * @param lastName is LastName of a doctor
	 * @param emailAddress is email of a doctor
	 * @param password is password of a doctor
	 * @param phNumber is PhoneNumber of a doctor
	 * @param Address is address of doctor
	 * @param ID is  employeeNumber of doctor
	 * @param speciality is  specialization(s) of doctor
	 */
	public Doctor(String firstName, String lastName, String emailAddress, String password, String phNumber, String Address, String ID, String speciality) {
		super(firstName,lastName,emailAddress,password,phNumber,Address);
		this.employeeNumber = ID;
		//convert string into array list
		String [] elem=speciality.split(",");// split list of specialty given to constructor into array

		this.specialization = new ArrayList<String>(Arrays.asList(elem));// convert array to ArrayList

	}
		
	
	/*
	 *This method returns a unique employeeNumber of a doctor  
	 * @returns employeeNumber of a doctor  
	 */
	
public String getEmployeeNumber() {
	return this.employeeNumber;	
}


/*
 *This method sets a unique employeeNumber of a doctor  
 * @param ID is employeeNumber of a doctor 
 */

public void setEmployeeNumber(String ID) {
   this.employeeNumber= ID ;	
}

/*
 * This method returns the Doctor specialization in a String
 * @returns the Specialization of a doctor
 */

public ArrayList<String> getSpecialization() {
	return specialization;
}

/* This method sets the specialization of the doctor 
 * @param speciality is the specialization of the doctor
 */

public void setSpecialization(String speciality) {
	specialization.add(speciality);
}

//Doctor class has method to access patient records with patient name or id 



}
