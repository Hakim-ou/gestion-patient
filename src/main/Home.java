package main;


import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Scanner;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.*;
import java.io.*;
import main.*;
import lib.*;


public class Home extends JFrame{
	/* list of patients */
	public HashMap<String, Set<String>> patients = new HashMap<String, Set<String>>();

	/* components */
	public JPanel home, dentchooser, operation, checkingPage;
	HoverJButton adding, checking, more;
	private static int sizeH = 515, sizeV = 650;

	/* constructor */
	public Home(){
		// getting list of patients
		setPatients();

		setSize(515, 650);
		setResizable(false);
		setTitle("Study");
		setLayout(new BorderLayout(0, 0));
		// closing
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){
				FileAccess.freeFile("./files/patients.csv");
				String backLine = "";
				for (String lname : patients.keySet()){
					for (String fname : patients.get(lname)){
						try{
							FileAccess.writeLine("./files/patients.csv", backLine + fname + " " + lname);
							backLine = "\n";
						}catch(IOException excep){
							excep.printStackTrace();
						}
					}
				}
				int input = JOptionPane.showConfirmDialog(Home.this, "Are you sure you want to quit ?", "Close window ?",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE);
				if (input == JOptionPane.YES_OPTION){
					System.exit(0);
				}
			}
		});

		home = new JPanel(new GridBagLayout());
		home.setBackground(new Color(138, 3, 3));
		add(home, BorderLayout.CENTER);

		try{
			dentchooser = new DentChooser(this, home, home);
		}catch(IOException e){//TODO
			e.printStackTrace();
		}

		JTextArea msg = new JTextArea("\tWelcome to our study !\nTo add a new record please press 'add operation'" + 
				"\nor press 'check previous' to verify/modify old records" + 
				"\nPress 'more information' to know more about this study");
		msg.setEditable(false);
		msg.setBackground(new Color(138, 3, 3));
		msg.setForeground(Color.WHITE);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 6;
		gbc.gridy = 0;
		gbc.gridwidth = 6;
		gbc.gridheight = 4;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(4, 0, 10, 0);
		home.add(msg, gbc);

		adding = new HoverJButton("add operation", new Color(222, 221, 250));
		adding.setForeground(Color.BLACK);
		adding.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent evt){
				remove(home);
				add(dentchooser, BorderLayout.CENTER);
				validate();
				repaint();
			}
		});
		gbc.gridx = 6;
		gbc.gridy = 8;
		gbc.gridwidth = 6;
		gbc.gridheight = 4;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(10, 0, 4, 0);
		home.add(adding, gbc);

		checking = new HoverJButton("check previous", new Color(218, 184, 243));
		checking.setForeground(Color.BLACK);
		checking.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent evt){
				checkingPage = new Checking(Home.this, home, home);
				remove(home);
				add(checkingPage, BorderLayout.CENTER);
				validate();
				repaint();
			}
		});
		gbc.gridx = 6;
		gbc.gridy = 12;
		gbc.gridwidth = 6;
		gbc.gridheight = 4;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(4, 0, 4, 0);
		home.add(checking, gbc);

		more = new HoverJButton("more information", new Color(234, 122, 215));
		more.setForeground(Color.BLACK);
		gbc.gridx = 6;
		gbc.gridy = 16;
		gbc.gridwidth = 6;
		gbc.gridheight = 4;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(4, 0, 4, 0);
		home.add(more, gbc);

		setVisible(true);
	}

	public static int getSizeH() {
		return sizeH;
	}

	public static int getSizeV() {
		return sizeV;
	}


	/* getting list of patients */
	private void setPatients(){
		String line, lname, fname;
		boolean finish = false;
		int firstSpace;
		try{
			Scanner reader = new Scanner(new File("./files/patients.csv"));
			while (!finish){
    			if (reader.hasNextLine()){ 
					line = reader.nextLine();
					firstSpace = line.indexOf(" ");
					fname = line.substring(0, firstSpace);
					lname = line.substring(firstSpace + 1, line.length());
					addPatient(lname, fname);
				}else
					finish = true;
			}
			reader.close();
		}catch(FileNotFoundException exc){
			exc.printStackTrace();
		}
	}


	public HashMap<String, Set<String>> getPatients(){
		return patients;
	}

	public void addPatient(String lname, String fname){
		try{
			patients.get(DentChooser.capitalize(lname)).add(DentChooser.capitalize(fname));
		}catch(NullPointerException e){
			patients.put(DentChooser.capitalize(lname), new HashSet<String>());
			patients.get(DentChooser.capitalize(lname)).add(DentChooser.capitalize(fname));
		}
	}



	/* main */
	public static void main(String[] args){
		new Home();
	}
}

