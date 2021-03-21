package main;


import java.util.HashMap;
import java.util.List;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.Math;
import lib.*;
import main.Home;


public class DentChooser extends JPanel{
	/* components */
	private String num = ((Integer.parseInt(FileAccess.readLine("./files/serial.txt", 1))) + 1) + "";
	private String zeros = "";
	private String fname, lname, age, amount, date, choosedMutuelle;
	private int account = 0;
	private String phone = "";
	private String email = "";
	private String address = "";
	private String choosedTouth = "Please choose one!";
	private char selectedSex, selectedPos;
	private HashMap<String, Boolean> selection = new HashMap<String, Boolean>();// to check if a field was specified
	private JPanel barre, body_bg, body_fg;
	private HoverJButton back, cancel, nextButton;
	private Color bege, orange, bloody;
	private BufferedImage buff;
	private JTextField ageTf, phoneTf, emailTf, addressTf, amountTf, recievedTf, choosedTouthTf, numTf;
	private JLabel sexLbl, fNameLbl, lNameLbl, ageLbl, phoneLbl, emailLbl, addressLbl, amountLbl, recievedLbl, choosedTouthLbl,
		   	positionLbl, mutuelleLbl, numLbl;
	private JLabel error = new JLabel("");
	private JRadioButton sexM, sexW, up, down;
	private AutocompleteJComboBox fNameTf, lNameTf, mutuelle;

	List<String> operations = Arrays.asList("First",
						   "Second",
						   "Third",
						   "Forth");

	/* constructor */
	public DentChooser(JFrame main, JPanel home, JPanel precedent) throws IOException{
		for (int i = 0; i < Math.max(10 - num.length(), 0); i++){
			zeros += "0";
		}
		num = zeros + num;
		// selection initiation
		selection.put("tooth", false);
		selection.put("fname", false);
		selection.put("lname", false);
		selection.put("age", false);
		selection.put("sex", false);
		selection.put("amount", false);
		selection.put("recieved", false);
		selection.put("phone number", true);
		selection.put("email", true);
		selection.put("address", true);
		selection.put("position", false);
		selection.put("mutuelle", false);
		selection.put("number", false);
		
		// color initiation
		bege = new Color(255, 255, 153);
		orange = new Color(255, 128, 0);
		bloody = new Color(138, 3, 3);

		setSize(500, 300);
		setLayout(new BorderLayout(0, 0));

		barre = new JPanel(new FlowLayout(FlowLayout.LEADING, 5, 5));
		barre.setBackground(Color.white);
		add(barre, BorderLayout.PAGE_START); 
		JPanel body0 = new JPanel(new GridLayout(2, 1));
		add(body0, BorderLayout.CENTER);
		JPanel body = new JPanel(new BorderLayout(0, 0)); 
		body0.add(body);

		body_bg = new ImageJPanel(new BorderLayout(0, 0), "/images/bg_human_teeth.png");
		body.add(body_bg, BorderLayout.CENTER);

		Label instruction = new Label("Please choose the concerned tooth");
		instruction.setBackground(bloody);
		instruction.setForeground(Color.white);
		body.add(instruction, BorderLayout.NORTH);

		back = new HoverJButton("Back", bege);
		back.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				main.remove(DentChooser.this);
				main.add(precedent, BorderLayout.CENTER);
				main.validate();
				main.repaint();
			}
		});
		barre.add(back);

		cancel = new HoverJButton("Cancel", orange);
		cancel.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				main.remove(DentChooser.this);
				main.add(home, BorderLayout.CENTER);
				main.validate();
				main.repaint();
			}
			});
		barre.add(cancel);

		
		buff = new BufferedImage(body_bg.getWidth(), body_bg.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g = buff.createGraphics();
		body_bg.paint(g);
		g.dispose();
		body_bg.addComponentListener(new ComponentAdapter(){
			@Override
			public void componentResized(ComponentEvent e){
				int width = body_bg.getWidth();
				int height = body_bg.getHeight();
				Image tmp = buff.getScaledInstance(width, height, Image.SCALE_SMOOTH);
				BufferedImage new_buff = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				Graphics2D g2 = new_buff.createGraphics();
				g2.drawImage(tmp, 0, 0, null);
				buff = new_buff;
				g2.dispose();
			}
		});

		body_fg = new ImageJPanel(new BorderLayout(0, 0), "/images/human_teeth.png");
		body_fg.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e){
				boolean selected = false;
				switch(Integer.toHexString(buff.getRGB(e.getX(), e.getY()))){
					case "ff72864f":
						choosedTouth = "Central Incisor";
						selected = true;
						break;
					case "ffdaff97":
						choosedTouth = "Lateral Incisor";
						selected = true;
						break;
					case "ffda2297":
						choosedTouth = "Canine";
						selected = true;
						break;
					case "ff1c6b00":
						choosedTouth = "First Premolar";
						selected = true;
						break;
					case "ffdadb97":
						choosedTouth = "Second Premolar";
						selected = true;
						break;
					case "fffafd0b":
						choosedTouth = "First Molar";
						selected = true;
						break;
					case "ff81cb09":
						choosedTouth = "Second Molar";
						selected = true;
						break;
					case "ff000be2":
						choosedTouth = "Third Molar";
						selected = true;
						break;
				}
				if (selected){
					choosedTouthTf.setText(choosedTouth);
					selection.put("tooth", true);
				}
			}
		});
		body_bg.add(body_fg, BorderLayout.CENTER);

		setVisible(true);

		/* *** patient informations *** */
		SpringLayout layout = new SpringLayout();
		JPanel body1 = new JPanel(layout);
		body1.setBackground(bloody);
		body0.add(body1);
		
		// components
		numLbl = new JLabel("Nb: ");
		numTf = new JTextField(10);
		numTf.setText(num);
		numTf.setEditable(false);
		numTf.setBackground(Color.BLACK);
		numTf.setForeground(Color.WHITE);

		fNameLbl = new JLabel("first name");
		lNameLbl = new JLabel("last name");
		lNameTf = new AutocompleteJComboBox(new StringSearchable(new ArrayList(((Home)main).getPatients().keySet())));
		lNameTf.getEditor().getEditorComponent().addFocusListener(new FocusAdapter(){
			@Override
			public void focusLost(FocusEvent e){
				try{
					Patient pat = new Patient(main, (String)fNameTf.getSelectedItem(), (String)lNameTf.getSelectedItem());
					ageTf.setText(pat.getAge());
					if (pat.getSex() == 'M')
						sexM.setSelected(true);
					else
						sexW.setSelected(true);
					selectedSex = pat.getSex();
					selection.put("sex", true);
					phoneTf.setText(pat.getPhone());
					emailTf.setText(pat.getEmail());
					addressTf.setText(pat.getAddress());
					mutuelle.setSelectedItem(pat.getMutuelle());
				}catch(Patient.PatientDoesNotExist ex){
					System.out.println("Patient " + (String)fNameTf.getSelectedItem() + " " +
						   (String)lNameTf.getSelectedItem() + " not found !!");
					ageTf.setText("");
					sexM.setSelected(false);
					sexW.setSelected(false);
					selectedSex = 'N';
					selection.put("sex", false);
					phoneTf.setText("");
					emailTf.setText("");
					addressTf.setText("");
					mutuelle.setSelectedItem("");
				}
			}
		});
		JTextComponent tcl = (JTextComponent)lNameTf.getEditor().getEditorComponent();
		tcl.getDocument().addDocumentListener(new DocumentListener(){
			@Override
			public void changedUpdate(DocumentEvent e){
			}
			@Override
			public void insertUpdate(DocumentEvent e){
				amountTf.setText("");
				recievedTf.setText("0");
			}
			@Override
			public void removeUpdate(DocumentEvent e){
				amountTf.setText("");
				recievedTf.setText("0");
			}
		});
		fNameTf = new AutocompleteJComboBox(new StringSearchable(new ArrayList<String>()));
		fNameTf.getEditor().getEditorComponent().addFocusListener(new FocusAdapter(){
			@Override
			public void focusGained(FocusEvent e){
				try{
					fNameTf.setSearchable(new StringSearchable(new ArrayList(
									((Home)main).getPatients().get(lNameTf.getSelectedItem())
									)));
				}catch(NullPointerException ex){
				}
			}

			@Override
			public void focusLost(FocusEvent e){
				try{
					Patient pat = new Patient(main, (String)fNameTf.getSelectedItem(), (String)lNameTf.getSelectedItem());
					ageTf.setText(pat.getAge());
					if (pat.getSex() == 'M')
						sexM.setSelected(true);
					else
						sexW.setSelected(true);
					selectedSex = pat.getSex();
					selection.put("sex", true);
					phoneTf.setText(pat.getPhone());
					emailTf.setText(pat.getEmail());
					addressTf.setText(pat.getAddress());
					mutuelle.setSelectedItem(pat.getMutuelle());
					account = pat.getAccount();
				}catch(Patient.PatientDoesNotExist ex){
					System.out.println("Patient " + (String)fNameTf.getSelectedItem() + " " +
						   (String)lNameTf.getSelectedItem() + " not found !!");
					ageTf.setText("");
					sexM.setSelected(false);
					sexW.setSelected(false);
					selectedSex = 'N';
					selection.put("sex", false);
					phoneTf.setText("");
					emailTf.setText("");
					addressTf.setText("");
					mutuelle.setSelectedItem("");
					account = 0;
				}
			}
		});
		JTextComponent tcf = (JTextComponent)fNameTf.getEditor().getEditorComponent();
		tcf.getDocument().addDocumentListener(new DocumentListener(){
			@Override
			public void changedUpdate(DocumentEvent e){
			}
			@Override
			public void insertUpdate(DocumentEvent e){
				amountTf.setText("");
				recievedTf.setText("0");
			}
			@Override
			public void removeUpdate(DocumentEvent e){
				amountTf.setText("");
				recievedTf.setText("0");
			}
		});

		ageLbl = new JLabel("Age");
		ageTf = new JTextField(3);

		sexLbl = new JLabel("sex");
		sexM = new JRadioButton("Man");
		sexM.setBackground(orange);
		sexM.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent evt){
				sexW.setSelected(false);
				selectedSex = 'M';
				selection.put("sex", true);
			}
		});
		sexW = new JRadioButton("Woman");
		sexW.setBackground(orange);
		sexW.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent evt){
				sexM.setSelected(false);
				selectedSex = 'W';
				selection.put("sex", true);
			}
		});

		phoneLbl = new JLabel("phone number");
		phoneTf = new JTextField(10);
		emailLbl = new JLabel("email");
		emailTf = new JTextField(20);

		addressLbl = new JLabel("address");
		addressTf = new JTextField();
		addressTf.setPreferredSize(new Dimension(350, 15));

		choosedTouthLbl = new JLabel("choosed tooth");
		choosedTouthTf = new JTextField(15);
		choosedTouthTf.setEditable(false);
		choosedTouthTf.setText(choosedTouth);
		choosedTouthTf.setBackground(Color.BLACK);
		choosedTouthTf.setForeground(Color.WHITE);

		amountLbl = new JLabel("amount to pay");
		amountTf = new JTextField(6);

		nextButton = new HoverJButton("Next", bege);
		nextButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent evt){
				error.setForeground(Color.WHITE);
				error.setText("");
				main.validate();
				main.repaint();

				num = numTf.getText();
				if (num.length() > 0)
					selection.put("number", true);

				fname = (String)fNameTf.getSelectedItem();
				if (fname.length() > 0)
					selection.put("fname", true);

				lname = (String)lNameTf.getSelectedItem();
				if (lname.length() > 0)
					selection.put("lname", true);

				// add patient to patients list
				if (selection.get("lname") && selection.get("fname"))
					((Home)main).addPatient(lname, fname);

				age = ageTf.getText();
				try{
					if (Integer.parseInt(age) > 0)
						selection.put("age", true);
					else
						System.out.println("the field age must be correctly filled");
				}catch(NumberFormatException e){
					System.out.println("exception!! the field age must be correctly filled");
				}

				String phone_var = phoneTf.getText();
				if (phone_var.length() > 0){
					try{
						if (Integer.parseInt(phone_var) > 0 && phone_var.length() == 10 && phone_var.charAt(0) == '0'){
							phone = phone_var;
							selection.put("phone number", true);
						}else{
							selection.put("phone number", false);
							System.out.println("the field phone must be correctly filled");
						}
					}catch(NumberFormatException e){
						selection.put("phone number", false);
						System.out.println("exception!! the field phone must be correctly filled");
					}
				}

				String email_var = emailTf.getText();
				if (email_var.length() > 0){
					if (validEmail(email_var)){
						selection.put("email", true);
						email = email_var;
					}else{
						selection.put("email", false);
						System.out.println("the field email must be correctly filled");
					}
				}

				String address_var = addressTf.getText();
				if (address_var.length() > 0){
					address = address_var;
				}

				amount = amountTf.getText();
				try{
					if (Integer.parseInt(amount) >= 0){
						selection.put("amount", true);
						account -= Integer.parseInt(amount);
					}else
						System.out.println("the field amount must be correctly filled");
				}catch(NumberFormatException e){
					System.out.println("the field amount must be correctly filled");
				}

				try{
					if (Integer.parseInt(recievedTf.getText()) >= 0){
						selection.put("recieved", true);
						account += Integer.parseInt(recievedTf.getText());
					}else
						System.out.println("the field amount must be correctly filled");
				}catch(NumberFormatException e){
					System.out.println("the field amount must be correctly filled");
				}

				choosedMutuelle = (String)mutuelle.getSelectedItem();
				if (operations.contains(choosedMutuelle))
					selection.put("mutuelle", true);

				for (String key : selection.keySet()){
					if (!selection.get(key)){
						error.setText("the field " + key +  " must be correctly filled");
						main.validate();
						main.repaint();
						return;
					}
				}


				String recordInfo = '\n' + capitalize(fname) + ';' + capitalize(lname) + ';' + age + ';';
				recordInfo += "" + selectedSex + ';' + phone + ';' + email + ';' + address + ';' + choosedMutuelle + ';' + account;
				FileAccess.freeFile("./files/info_"+ fname.toLowerCase() + "_" + lname.toLowerCase() + ".csv");
				try{
					FileAccess.writeLine("./files/info_"+ fname.toLowerCase() + "_" + lname.toLowerCase() + ".csv",
										"first name;last name;age;sex;phone number;email;address;mutuelle;account");
					FileAccess.writeLine("./files/info_"+ fname.toLowerCase() + "_" + lname.toLowerCase() + ".csv", recordInfo);
				}catch(IOException e){
					e.printStackTrace();
				}
			   	String recordOperation = '\n' + num + ';' + amount + ';' + recievedTf.getText() + ';';
				recordOperation += choosedTouth + ';' + selectedPos + ';';
				try{
					FileAccess.writeLine("./files/patient_"+ fname.toLowerCase() + "_" + lname.toLowerCase() + ".csv", recordOperation);
				}catch(IOException e){
					if (e.getCause() instanceof FileNotFoundException){
						File newFile = new File("./files/patient_"+ fname.toLowerCase() + "_" + lname.toLowerCase() + ".csv");
						try{
							String firstLine = "number;amount to pay;payed;traited tooth;position;operation;date";
							if (newFile.createNewFile()){
								FileAccess.writeLine("./files/patient_"+ fname.toLowerCase() + "_" + lname.toLowerCase() +
												   	".csv", firstLine);
								FileAccess.writeLine("./files/patient_"+ fname.toLowerCase() + "_" + lname.toLowerCase() +
												   	".csv", recordOperation);
							}
							else{
								e.printStackTrace();
								return;
							}
						}catch(IOException exc){
							exc.printStackTrace();
							return;
						}
					}else{
						e.printStackTrace();
						return;
					}
				}

				JPanel next = new Operation(main, home, DentChooser.this, fname, lname, choosedTouth);
				main.remove(DentChooser.this);
				main.add(next, BorderLayout.CENTER);
				main.validate();
				main.repaint();

				// reset
				Component[] comps = body1.getComponents();
				for (Component comp : comps){
					if (comp instanceof JTextField){
						((JTextField)comp).setText("");
					}
				}
				choosedTouthTf.setText("Please choose one!");
				sexM.setSelected(false);
				sexW.setSelected(false);
				up.setSelected(false);
				down.setSelected(false);
				for (String key : selection.keySet()){
					if (!(key.equals("phone number") || key.equals("email") || key.equals("address")))
						selection.put(key, false);
				}
				num = ((Integer.parseInt(num)) + 1) + "";
				zeros = "";
				for (int i = 0; i < Math.max(10 - num.length(), 0); i++){
					zeros += "0";
				}
				num = zeros + num;
				numTf.setText(num);
				recievedTf.setText("0");
				fNameTf.setSelectedItem("");
				lNameTf.setSelectedItem("");
				lNameTf.setSearchable(new StringSearchable(new ArrayList(((Home)main).getPatients().keySet())));
				mutuelle.setSelectedItem("");
			}
		});

		mutuelleLbl = new JLabel("mutuelle");
		mutuelle = new AutocompleteJComboBox(new StringSearchable(operations));

		positionLbl = new JLabel("position");
		up = new JRadioButton("upper");
		up.setBackground(orange);
		up.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent evt){
				down.setSelected(false);
				selectedPos = 'U';
				selection.put("position", true);
			}
		});
		down = new JRadioButton("lower");
		down.setBackground(orange);
		down.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent evt){
				up.setSelected(false);
				selectedPos = 'L';
				selection.put("position", true);
			}
		});

		recievedLbl = new JLabel("payed amount");// stupid naming but...it's 3:29 AM
		recievedTf = new JTextField(6);
		recievedTf.setText("0");

		// adding components
		body1.add(numLbl);
		body1.add(numTf);
		body1.add(fNameLbl);
		body1.add(lNameLbl);
		body1.add(fNameTf);
		body1.add(lNameTf);
		body1.add(ageTf);
		body1.add(ageLbl);
		body1.add(sexLbl);
		body1.add(sexM);
		body1.add(sexW);
		body1.add(phoneLbl);
		body1.add(phoneTf);
		body1.add(emailLbl);
		body1.add(emailTf);
		body1.add(addressLbl);
		body1.add(addressTf);
		body1.add(choosedTouthLbl);
		body1.add(choosedTouthTf);
		body1.add(amountLbl);
		body1.add(amountTf);
		body1.add(nextButton);
		body1.add(error);
		body1.add(mutuelle);
		body1.add(positionLbl);
		body1.add(up);
		body1.add(down);
		body1.add(mutuelleLbl);
		body1.add(recievedLbl);
		body1.add(recievedTf);

		// set foreground color
		Component[] comps = body1.getComponents();
		for (Component comp : comps){
			if (comp instanceof JLabel){
				((JLabel)comp).setForeground(Color.WHITE);
			}
		}

		// layout constraints
		layout.putConstraint(SpringLayout.NORTH, numLbl, 5, SpringLayout.NORTH, body1);
		layout.putConstraint(SpringLayout.WEST, numLbl, -180, SpringLayout.EAST, body1);
		layout.putConstraint(SpringLayout.NORTH, numTf, 5, SpringLayout.NORTH, body1);
		layout.putConstraint(SpringLayout.WEST, numTf, -150, SpringLayout.EAST, body1);

		layout.putConstraint(SpringLayout.WEST, lNameLbl, 5, SpringLayout.WEST, body1); 
		layout.putConstraint(SpringLayout.NORTH, lNameLbl, 16, SpringLayout.SOUTH, numTf); 
		layout.putConstraint(SpringLayout.WEST, lNameTf, 5, SpringLayout.EAST, lNameLbl); 
		layout.putConstraint(SpringLayout.NORTH, lNameTf, 15, SpringLayout.SOUTH, numTf); 

		layout.putConstraint(SpringLayout.WEST, fNameLbl, 10, SpringLayout.EAST, lNameTf); 
		layout.putConstraint(SpringLayout.NORTH, fNameLbl, 16, SpringLayout.SOUTH, numTf); 
		layout.putConstraint(SpringLayout.WEST, fNameTf, 5, SpringLayout.EAST, fNameLbl); 
		layout.putConstraint(SpringLayout.NORTH, fNameTf, 15, SpringLayout.SOUTH, numTf); 

		layout.putConstraint(SpringLayout.WEST, ageLbl, 5, SpringLayout.WEST, body1); 
		layout.putConstraint(SpringLayout.NORTH, ageLbl, 16, SpringLayout.SOUTH, fNameLbl); 
		layout.putConstraint(SpringLayout.WEST, ageTf, 5, SpringLayout.EAST, ageLbl); 
		layout.putConstraint(SpringLayout.NORTH, ageTf, 16, SpringLayout.SOUTH, fNameLbl); 
		
		layout.putConstraint(SpringLayout.WEST, sexLbl, 40, SpringLayout.EAST, ageTf);
		layout.putConstraint(SpringLayout.NORTH, sexLbl, 17, SpringLayout.SOUTH, fNameLbl); 
		layout.putConstraint(SpringLayout.WEST, sexM, 5, SpringLayout.EAST, sexLbl);
		layout.putConstraint(SpringLayout.NORTH, sexM, 15, SpringLayout.SOUTH, fNameLbl); 
		layout.putConstraint(SpringLayout.WEST, sexW, 3, SpringLayout.EAST, sexM);
		layout.putConstraint(SpringLayout.NORTH, sexW, 15, SpringLayout.SOUTH, fNameLbl); 
		
		layout.putConstraint(SpringLayout.WEST, phoneLbl, 5, SpringLayout.WEST, body1);
	    layout.putConstraint(SpringLayout.NORTH, phoneLbl, 15, SpringLayout.SOUTH, ageLbl);	
		layout.putConstraint(SpringLayout.WEST, phoneTf, 5, SpringLayout.EAST, phoneLbl);
	    layout.putConstraint(SpringLayout.NORTH, phoneTf, 15, SpringLayout.SOUTH, ageLbl);	

		layout.putConstraint(SpringLayout.WEST, emailLbl, 10, SpringLayout.EAST, phoneTf);
	    layout.putConstraint(SpringLayout.NORTH, emailLbl, 15, SpringLayout.SOUTH, ageLbl);	
		layout.putConstraint(SpringLayout.WEST, emailTf, 5, SpringLayout.EAST, emailLbl);
	    layout.putConstraint(SpringLayout.NORTH, emailTf, 15, SpringLayout.SOUTH, ageLbl);	

		layout.putConstraint(SpringLayout.WEST, addressLbl, 5, SpringLayout.WEST, body1);
		layout.putConstraint(SpringLayout.NORTH, addressLbl, 15, SpringLayout.SOUTH, emailTf);
		layout.putConstraint(SpringLayout.WEST, addressTf, 5, SpringLayout.EAST, addressLbl);
		layout.putConstraint(SpringLayout.NORTH, addressTf, 15, SpringLayout.SOUTH, emailTf);

		layout.putConstraint(SpringLayout.WEST, choosedTouthLbl, 5, SpringLayout.WEST, body1);
		layout.putConstraint(SpringLayout.NORTH, choosedTouthLbl, 17, SpringLayout.SOUTH, addressTf);
		layout.putConstraint(SpringLayout.WEST, choosedTouthTf, 5, SpringLayout.EAST, choosedTouthLbl);
		layout.putConstraint(SpringLayout.NORTH, choosedTouthTf, 15, SpringLayout.SOUTH, addressTf);

		layout.putConstraint(SpringLayout.WEST, positionLbl, 10, SpringLayout.EAST, choosedTouthTf);
		layout.putConstraint(SpringLayout.NORTH, positionLbl, 17, SpringLayout.SOUTH, addressTf);
		layout.putConstraint(SpringLayout.WEST, up, 5, SpringLayout.EAST, positionLbl);
		layout.putConstraint(SpringLayout.NORTH, up, 15, SpringLayout.SOUTH, addressTf);
		layout.putConstraint(SpringLayout.WEST, down, 5, SpringLayout.EAST, up);
		layout.putConstraint(SpringLayout.NORTH, down, 15, SpringLayout.SOUTH, addressTf);

		layout.putConstraint(SpringLayout.WEST, mutuelleLbl, 5, SpringLayout.WEST, body1);
		layout.putConstraint(SpringLayout.NORTH, mutuelleLbl, 17, SpringLayout.SOUTH, choosedTouthTf);
		layout.putConstraint(SpringLayout.WEST, mutuelle, 5, SpringLayout.EAST, mutuelleLbl);
		layout.putConstraint(SpringLayout.NORTH, mutuelle, 15, SpringLayout.SOUTH, choosedTouthTf);
		layout.putConstraint(SpringLayout.WEST, amountLbl, 15, SpringLayout.EAST, mutuelle);
		layout.putConstraint(SpringLayout.NORTH, amountLbl, 17, SpringLayout.SOUTH, choosedTouthTf);
		layout.putConstraint(SpringLayout.WEST, amountTf, 0, SpringLayout.WEST, recievedTf);
		layout.putConstraint(SpringLayout.NORTH, amountTf, 15, SpringLayout.SOUTH, choosedTouthTf);
		layout.putConstraint(SpringLayout.WEST, recievedLbl, 15, SpringLayout.EAST, mutuelle);
		layout.putConstraint(SpringLayout.NORTH, recievedLbl, 17, SpringLayout.SOUTH, amountTf);
		layout.putConstraint(SpringLayout.WEST, recievedTf, 5, SpringLayout.EAST, recievedLbl);
		layout.putConstraint(SpringLayout.NORTH, recievedTf, 15, SpringLayout.SOUTH, amountTf);

		layout.putConstraint(SpringLayout.EAST, nextButton, -5, SpringLayout.EAST, body1);
		layout.putConstraint(SpringLayout.SOUTH, nextButton, -5, SpringLayout.SOUTH, body1);

		layout.putConstraint(SpringLayout.WEST, error, 5, SpringLayout.WEST, body1);
		layout.putConstraint(SpringLayout.SOUTH, error, -5, SpringLayout.SOUTH, body1);
	}


	/* helper methods */

	// String modifiers
	public static String capitalize(String word){
		if (word == null)
			return null;
		else
			return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
	}

	// pattern verification
	public static boolean validEmail(String email){
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                            "[a-zA-Z0-9_+&*-]+)*@" +
                            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                            "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    } 
	


	// back ground panel
	private class ImageJPanel extends JPanel{
		Image img;
		public ImageJPanel(LayoutManager layout, String path) throws IOException{
			super(layout);
			img = ImageIO.read(getClass().getResource(path));
			this.setSize(new Dimension(img.getWidth(null), img.getHeight(null)));
		}
		@Override
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
		}
	}
}
