package lib;
/* A container for patient informations that we will read from the patient file */


import java.awt.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.event.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Date;
import java.util.HashMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import lib.*;
import main.Home;
import main.Details;


public class Patient {
	/* attributes */
	private String fName, lName, age, phoneNumber, fixe, email, address, image, mutuelle, mutuNum, lastOp;
	private Date birthdate, dateOp;
	private char sex;
	private int id, account;
	private String[] historyColumnNames = {"number", "amount to pay", "payed", "traited tooth", "position", "operation", "date"}; 
	private List<String[]> history;
	public JFrame main;

	private static HashMap<Integer, Patient> patientsList = new HashMap<>();

	/* constructor */
	public Patient(int id, String lname, String fname, char sex, Date birhtdate, String phone, String fixe,
					String email, String address, String imagePath, String mutuelle, String mutuNum, int account,
					String lastOp, Date dateOp) {
		this.id = id;
		this.lName = lname;
		this.fName = fname;
		this.sex = sex;
		this.birthdate = birhtdate;
		this.phoneNumber = phone;
		this.fixe = fixe;
		this.email = email;
		this.address = address;
		this.image = imagePath;
		this.mutuelle = mutuelle;
		this.mutuNum = mutuNum;
		this.account = account;
		this.lastOp = lastOp;
		this.dateOp = dateOp;
	}

	/* constructor - file loader */
	public Patient(JFrame varMain, String fname, String lname) throws PatientDoesNotExist{
		if (fname == null || lname == null)
			throw new PatientDoesNotExist("The patient: " + fName + " " + lName + " has no file!\n");
		String patientFile = "./files/info_" + fname.toLowerCase() + "_" + lname.toLowerCase() + ".csv";
		File file = new File(patientFile);
		if (!file.exists()){
			System.out.println("File: " + file.getAbsolutePath() + " Not found");
			throw new PatientDoesNotExist("The patient: " + fName + " " + lName + " has no file!\n");
		}else{
			main = varMain;
			fName = capitalize(fname);
			lName = capitalize(lname);
			String sHelper = FileAccess.readLast(patientFile);
			try{
			age = sHelper.split(";")[2];
			sex = sHelper.split(";")[3].charAt(0);
			phoneNumber = sHelper.split(";")[4];
			email = sHelper.split(";")[5];
			address = sHelper.split(";")[6];
			mutuelle = sHelper.split(";")[7];
			account = Integer.parseInt(sHelper.split(";")[8]);
			history = new ArrayList<String[]>();
			}catch(Exception e){
				System.out.println(sHelper);
				System.out.println(patientFile);
				e.printStackTrace();
			}
		}
		image = "./images/profile_" + fname.toLowerCase() + "_" + lname.toLowerCase() + ".png";
		file = new File(image);
		if (!file.exists()){
			image = "./images/profile_" + fname.toLowerCase() + "_" + lname.toLowerCase() + ".jpeg";
			file = new File(image);
			if (!file.exists()){
				image = "./images/profile_" + fname.toLowerCase() + "_" + lname.toLowerCase() + ".jpg";
				file = new File(image);
				if (!file.exists()){
					image = "None";
					System.out.println("No image found for: " + fName + " " + lName);
				}
			}
		}
	}

	/* getters */
	public String getfName(){
		return fName;
	}

	public String getlName(){
		return lName;
	}
	
	public String getAge(){
		return age;
	}

	public Date getBirthdate(){
		return birthdate;
	}

	public char getSex(){
		return sex;
	}

	public String getPhone(){
		return phoneNumber;
	}

	public String getFixe(){
		return fixe;
	}

	public String getEmail(){
		return email;
	}

	public String getAddress(){
		return address;
	}

	public String getImage(){
		return image;
	}

	public String getMutuelle(){
		return mutuelle;
	}

	public String getMutuNum(){
		return mutuNum;
	}

	public String getLastOp(){
		return lastOp;
	}

	public Date getDateLastOp(){
		return dateOp;
	}

	public int getAccount(){
		return account;
	}

	public int getId() {
		return id;
	}


	public List<String[]> getHistory(){
		return history;
	}
	public String[] getHistoryColumnNames(){
		return historyColumnNames;
	}


	/* setters */
	public void loadHistory(){//TODO
		history.clear();
		File file = new File("./files/patient_" + fName.toLowerCase() + "_" + lName.toLowerCase() + ".csv");
		try{
			Scanner reader = new Scanner(file);
			String line = reader.nextLine();
			while (reader.hasNextLine()){
				line = reader.nextLine();
				history.add(line.split(";"));
			}
		}catch(FileNotFoundException exc){
			String[] clearLine = historyColumnNames.clone();
			Arrays.fill(clearLine, "");
			history.add(clearLine);
		}
	}

	public void setImage(String path){
		image = path;
	}

	/* methods */
	public static String capitalize(String word){
		if (word == null)
			return null;
		else if (word.equals(""))
			return word;
		else
			return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
	}

	public static HashMap<Integer, Patient> getPatientsList(){
		return patientsList;
	}

	public static void addPatient(Patient patient){
		patientsList.put(patient.id, patient);
	}

	public static int nextId() {
		int max = 0;
		for (int i : patientsList.keySet()) {
			if (i > max) max = i;
		}
		return max + 1;
	}



	/* graphic representation */
	public static class PatientListRenderer extends JPanel implements ListCellRenderer<Patient>{
		// each cell is a Jpanel, and following are the components of these JPanels
		/* components */
		private JLabel nameLbl, sexAgeLbl, phoneLbl, imageLbl, accountLbl, detailsLbl;
		private JPanel infoPnl, detailsPnl;

		/* constructor */
		public PatientListRenderer(){
			setLayout(new BorderLayout(5, 5));

			nameLbl = new JLabel();
			sexAgeLbl = new JLabel();
			phoneLbl = new JLabel();
			imageLbl = new JLabel();
			accountLbl = new JLabel();
			detailsLbl = new JLabel("details");
			detailsLbl.setForeground(Color.blue);
			infoPnl = new JPanel(new GridLayout(3,1));
			detailsPnl = new JPanel(new GridLayout(2,1));

			add(imageLbl, BorderLayout.WEST);
			add(infoPnl, BorderLayout.CENTER);
			add(detailsPnl, BorderLayout.EAST);

			infoPnl.add(nameLbl);
			infoPnl.add(sexAgeLbl);
			infoPnl.add(phoneLbl);

			detailsPnl.add(accountLbl);
			detailsPnl.add(detailsLbl);
		}

		@Override
		public Component getListCellRendererComponent(JList<? extends Patient> list,
				Patient patient, int index, boolean isSelected, boolean cellHasFocus){
			// content
			nameLbl.setText(patient.getfName() + " " + patient.getlName());
			sexAgeLbl.setText(((patient.getSex() == 'M')?"Man":"Women") + ",  " + patient.getAge() + " years old");
			phoneLbl.setText(patient.getPhone());
			accountLbl.setText("" + patient.getAccount());

			if (patient.getImage().equals("None")){
				ImageIcon imageicon = new ImageIcon("./images/unknown.png");
				Image img = imageicon.getImage();
				img = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
				imageicon = new ImageIcon(img);
				imageLbl.setIcon(imageicon);
			}else{
				ImageIcon imageicon = new ImageIcon(patient.getImage());
				Image img = imageicon.getImage();
				img = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
				imageicon = new ImageIcon(img);
				imageLbl.setIcon(imageicon);
			}

			return this;
		}
	}
	
	/* exceptions */
	public class PatientDoesNotExist extends Exception{
		public PatientDoesNotExist(String errMessage){
			super(errMessage);
		}
	}
}


