package main;


import javax.swing.*;
import javax.swing.filechooser.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import org.jdatepicker.impl.*;
import org.jdatepicker.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Properties;
import java.util.Date;
import java.util.Calendar;
import lib.*;


public class AddPatient extends JPanel {
	// color initiation
	private Color bege = new Color(255, 255, 153);
	private Color orange = new Color(255, 128, 0);
	private Color bloody = new Color(138, 3, 3);

	private JLabel nom = new JLabel("Nom: "), prenom = new JLabel("Prénom: "),
		   		   date = new JLabel("date de Naissance: "),
		   		   telPortable = new JLabel("téléphone portable: "),
		   		   telFixe = new JLabel("téléphone fixe: "),
		   		   email = new JLabel("Email: "),
		   		   adresse = new JLabel("Adresse postale: "),
		   		   mutu = new JLabel("mutuelle (type, N°): "),
				   imageLbl = new JLabel();

	private JTextField nomT = new JTextField(15), prenomT = new JTextField(15),
		   	telPortableT = new JTextField(20), telFixeT = new JTextField(20),
		   	emailT = new JTextField(60), mutuNum = new JTextField(17);

	private JTextArea adresseT = new JTextArea(3, 100);

	private JCheckBox mr = new JCheckBox("Mr"), mme = new JCheckBox("Mme");

	private AutocompleteJComboBox mutuType;

	private JDatePickerImpl pckDate;

	private JButton modify, create, createAndAdd;

	private String imgPath = "/images/unknown.png", sourcePath, lastOp = "";

	private Date dateOp;

	private int id = Patient.nextId(), account = 0;

	/**
	 * constructor - from existing patient (modify)
	 */
	public AddPatient(JFrame main, JPanel prev, Patient patient) {
		this(main, prev);
		fillFromPatient(patient);
	}

	/**
	 * constructor - new patient
	 */
	public AddPatient(JFrame main, JPanel prev) {
		setSize(Home.getSizeH(), Home.getSizeV());
		setLayout(new BorderLayout());
		setVisible(true);
		add(new Barre(main, prev, this), BorderLayout.NORTH);
		SpringLayout bodyLyt = new SpringLayout();
		JPanel body = new JPanel(bodyLyt);
		add(body);
		
		ImageIcon imageicon = new ImageIcon(getClass().getResource("/images/unknown.png"));
		Image img = imageicon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
		imageicon = new ImageIcon(img);
		imageLbl.setIcon(imageicon);

		modify = new HoverJButton("modifier", orange);
		modify.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				FileNameExtensionFilter filter = new FileNameExtensionFilter("png", "jpeg", "jpg");
				JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				jfc.setAcceptAllFileFilterUsed(false);
				jfc.setFileFilter(filter);
				int returnValue = jfc.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION){
					File selectedFile = jfc.getSelectedFile();
					sourcePath = selectedFile.getAbsolutePath();
					ImageIcon imageicon = new ImageIcon(selectedFile.getAbsolutePath());
					Image img = imageicon.getImage();
					img = img.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
					imageicon = new ImageIcon(img);
					imageLbl.setIcon(imageicon);
				}
			}
		});

		setDatePicker();

		mutuType = new AutocompleteJComboBox(new StringSearchable(loadMutuelles()));

		create = new HoverJButton("sauvgarder & sortir", orange);
		create.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				Patient patient = constructPatient();
				Patient.addPatient(patient);
				JPanel patientList = new Checking(main, ((Home)main).home, ((Home)main).home);//TODO [depricated]
				main.remove(AddPatient.this);
				main.add(patientList, BorderLayout.CENTER);
				main.validate();
				main.repaint();
			}
		});

		createAndAdd = new HoverJButton("vers dossier", orange);
		createAndAdd.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				Patient patient = constructPatient();
				Patient.addPatient(patient);
				JPanel folder = new Folder(main, AddPatient.this, patient);
				main.remove(AddPatient.this);
				main.add(folder, BorderLayout.CENTER);
				main.validate();
				main.repaint();
			}
		});

		/* positionning */
		String N = SpringLayout.NORTH;
		String E = SpringLayout.EAST;
		String W = SpringLayout.WEST;
		String S = SpringLayout.SOUTH;
		// image
		bodyLyt.putConstraint(N, imageLbl, 10, N, body); bodyLyt.putConstraint(W, imageLbl, 15, W, body);
		body.add(imageLbl);
		bodyLyt.putConstraint(N, modify, 15, S, imageLbl); bodyLyt.putConstraint(E, modify, 0, E, imageLbl);
		body.add(modify);
		// gender
		bodyLyt.putConstraint(N, mr, 20, S, modify); bodyLyt.putConstraint(W, mr, 5, W, body);
		body.add(mr);
		bodyLyt.putConstraint(N, mme, 0, N, mr); bodyLyt.putConstraint(W, mme, 10, E, mr);
		body.add(mme);
		// nom prenom
		bodyLyt.putConstraint(N, nom, 15, S, mr); bodyLyt.putConstraint(W, nom, 5, W, body);
		body.add(nom);
		bodyLyt.putConstraint(N, nomT, 15, S, mr); bodyLyt.putConstraint(W, nomT, 5, E, nom);
		body.add(nomT);
		bodyLyt.putConstraint(N, prenom, 15, S, mr); bodyLyt.putConstraint(W, prenom, 10, E, nomT);
		body.add(prenom);
		bodyLyt.putConstraint(N, prenomT, 15, S, mr); bodyLyt.putConstraint(W, prenomT, 5, E, prenom);
		body.add(prenomT);
		// birth date
		bodyLyt.putConstraint(N, date, 15, S, prenomT); bodyLyt.putConstraint(W, date, 5, W, body);
		body.add(date);
		bodyLyt.putConstraint(N, pckDate, 15, S, prenomT); bodyLyt.putConstraint(W, pckDate, 5, E, date);
		body.add(pckDate);
		// phone
		bodyLyt.putConstraint(N, telPortable, 15, S, pckDate); bodyLyt.putConstraint(W, telPortable, 5, W, body);
		body.add(telPortable);
		bodyLyt.putConstraint(N, telPortableT, 15, S, pckDate); bodyLyt.putConstraint(W, telPortableT, 5, E, telPortable);
		body.add(telPortableT);
		bodyLyt.putConstraint(N, telFixe, 15, S, telPortableT); bodyLyt.putConstraint(W, telFixe, 5, W, body);
		body.add(telFixe);
		bodyLyt.putConstraint(N, telFixeT, 15, S, telPortableT); bodyLyt.putConstraint(W, telFixeT, 5, E, telFixe);
		body.add(telFixeT);
		// email
		bodyLyt.putConstraint(N, email, 15, S, telFixeT); bodyLyt.putConstraint(W, email, 5, W, body);
		body.add(email);
		bodyLyt.putConstraint(N, emailT, 15, S, telFixeT); bodyLyt.putConstraint(W, emailT, 5, E, email);
		bodyLyt.putConstraint(E, emailT, -5, E, body);
		body.add(emailT);
		// address
		bodyLyt.putConstraint(N, adresse, 15, S, emailT); bodyLyt.putConstraint(W, adresse, 5, W, body);
		body.add(adresse);
		bodyLyt.putConstraint(N, adresseT, 15, S, emailT); bodyLyt.putConstraint(W, adresseT, 5, E, adresse);
		bodyLyt.putConstraint(E, adresseT, -5, E, body);
		body.add(adresseT);
		// mutuelle
		bodyLyt.putConstraint(N, mutu, 15, S, adresseT); bodyLyt.putConstraint(W, mutu, 5, W, body);
		body.add(mutu);
		bodyLyt.putConstraint(N, mutuType, 15, S, adresseT); bodyLyt.putConstraint(W, mutuType, 5, E, mutu);
		body.add(mutuType);
		bodyLyt.putConstraint(N, mutuNum, 15, S, adresseT); bodyLyt.putConstraint(W, mutuNum, 5, E, mutuType);
		bodyLyt.putConstraint(E, mutuNum, -5, E, body);
		body.add(mutuNum);
		// create button
		bodyLyt.putConstraint(S, create, -10, S, body); bodyLyt.putConstraint(E, create, -5, E, body);
		body.add(create);
		bodyLyt.putConstraint(S, createAndAdd, -10, S, body); bodyLyt.putConstraint(E, createAndAdd, -5, W, create);
		body.add(createAndAdd);
	}

	/**
	 * get list of mutuelles
	 */
	private ArrayList<String> loadMutuelles() {
		String mutuName;
		ArrayList<String> mutuelles = new ArrayList<>();
		try {
			Scanner mutuFile = new Scanner(new File("bin/patients/mutuelle.csv"));
			while (mutuFile.hasNextLine()) {
				mutuName = mutuFile.nextLine();
				mutuelles.add(mutuName);
			}
		} catch(FileNotFoundException exc) {
			exc.printStackTrace();
		}
		return mutuelles;
	}

	/**
	 * format date picker
	 */
	private void setDatePicker() {
		UtilDateModel mdlDate = new UtilDateModel();
		Properties prpDate = new Properties();
		prpDate.put("text.today", "Aujourd'hui");
		prpDate.put("text.month", "Mois");
		prpDate.put("text.year", "Année");
		JDatePanelImpl pnlDate = new JDatePanelImpl(mdlDate, prpDate);
		pckDate = new JDatePickerImpl(pnlDate, new DateComponentFormatter());
	}

	/**
	 * format date picker with default date
	 */
	private void setDatePicker(Date date) {
		UtilDateModel mdlDate = new UtilDateModel(date);
		Properties prpDate = new Properties();
		prpDate.put("text.today", "Aujourd'hui");
		prpDate.put("text.month", "Mois");
		prpDate.put("text.year", "Année");
		//mdlDate.setDate(date.getYear(), date.getMonth(), date.getDay());
		mdlDate.setValue(date);
		mdlDate.setSelected(true);
		JDatePanelImpl pnlDate = new JDatePanelImpl(mdlDate, prpDate);
		pckDate = new JDatePickerImpl(pnlDate, new DateComponentFormatter());
	}

	/**
	 * copies image to the patient's folder
	 */
	private void copyImage(int id, String lname, String fname) {
		if (sourcePath == null) return;
		System.out.println(sourcePath);
		String[] sHelper = sourcePath.split("\\.");
		System.out.println(sHelper.length);
		String extension = sHelper[sHelper.length - 1];
		System.out.println(extension);
		assert (extension.equals("jpeg") || extension.equals("png") || extension.equals("jpg"));
		imgPath = "/patients/profile_" + lname.toLowerCase() + "_"
										+ fname.toLowerCase() + "_" + id
										+ "/image." + extension;
		File imageFile = FileAccess.freeFile(imgPath);
		InputStream is = null;
		OutputStream os = null;
		try{
			is = new FileInputStream(new File(sourcePath));
			os = new FileOutputStream(imageFile);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) > 0)
				os.write(buffer, 0, length);
		}catch(IOException exc){
			exc.printStackTrace();
		}finally{
			try{
				is.close();
				os.close();
			}catch(IOException exc){
				exc.printStackTrace();
			}
		}

	}


	/**
	 * construct patient
	 */
	private Patient constructPatient() {
		String lname = nomT.getText(), fname = prenomT.getText();
		new File("./patients/" + lname + "_" + fname + "_" + id).mkdir();
		char sex = (mr.isSelected()) ? 'M' : 'W';
		Date birthdate = (Date)pckDate.getModel().getValue();
		String phone = telPortableT.getText(), fixe = telFixeT.getText();
		String mail = emailT.getText();
		String adr = adresseT.getText();
		copyImage(id, lname, fname);
		String mutuelle = (String)mutuType.getSelectedItem(), num = mutuNum.getText();
		int account = 0;
		String lastOper = "";
		return new Patient(id, lname, fname, sex, birthdate, phone, fixe, mail, adr, imgPath,
						   mutuelle, num, account, lastOper, null);
	}

	/**
	 * fill the fields from patient info
	 */
	private void fillFromPatient(Patient patient) {
		id = patient.getId();
		nomT.setText(patient.getlName()); prenomT.setText(patient.getfName());
		if (patient.getSex() == 'M') {mr.setSelected(true); mme.setSelected(false);}
		else {mr.setSelected(false); mme.setSelected(true);}
		//Calendar calendar = Calendar.getInstance();
		//calendar.setTime(patient.getBirthdate());
		//DateModel<Calendar> dateModel = (DateModel<Calendar>) pckDate.getModel();
		//dateModel.setValue(calendar);
		setDatePicker(patient.getBirthdate());
		telPortableT.setText(patient.getPhone()); telFixeT.setText(patient.getFixe());
		emailT.setText(patient.getEmail());
		adresseT.setText(patient.getAddress());
		imgPath = patient.getImage();
		mutuType.setSelectedItem(patient.getMutuelle()); mutuNum.setText(patient.getMutuNum());
		account = patient.getAccount();
		lastOp = patient.getLastOp();
		dateOp = patient.getDateLastOp();
	}

	public static void main(String[] args) {
		JFrame window = new JFrame();
		window.setSize(Home.getSizeH(), Home.getSizeV());
		window.setLayout(new BorderLayout());
		window.add(new AddPatient(window, new JPanel()), BorderLayout.CENTER);
		window.setVisible(true);
	}
}
