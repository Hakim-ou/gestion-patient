package main;


import javax.swing.*;
import javax.swing.filechooser.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Properties;
import lib.*;


public class Folder extends JPanel {
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
				   imageLbl = new JLabel(), account = new JLabel("redevance: "),
				   lastOp = new JLabel("dernière opération: "),
				   dateOp = new JLabel("le: ");

	private JLabel nomT = new JLabel(""), prenomT = new JLabel(""),
		   	pckDate = new JLabel(""), telPortableT = new JLabel(""), telFixeT = new JLabel(""),
		   	emailT = new JLabel(""), mutuType = new JLabel(""), mutuNum = new JLabel(""),
			accountT = new JLabel(""), lastOpT = new JLabel(""), dateOpT = new JLabel("");

	private JLabel adresseT = new JLabel();

	private JCheckBox mr = new JCheckBox("Mr"), mme = new JCheckBox("Mme");

	private JButton modify, transactions, operations;

	public Folder(JFrame main, JPanel prev, Patient patient) {
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

		loadInfo(patient);

		modify = new HoverJButton("modifier", orange);
		modify.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				main.remove(Folder.this);
				main.add(new AddPatient(main, Folder.this, patient), BorderLayout.CENTER);
				main.validate();
				main.repaint();
			}
		});

		transactions = new HoverJButton("réglements", orange);
		transactions.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				//TODO
			}
		});

		operations = new HoverJButton("opérations", orange);
		operations.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				//TODO
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
		// account
		bodyLyt.putConstraint(N, account, 10, N, body); bodyLyt.putConstraint(W, account, 0, W, lastOp);
		body.add(account);
		bodyLyt.putConstraint(N, accountT, 0, N, account); bodyLyt.putConstraint(W, accountT, 5, E, account);
		body.add(accountT);
		// lastOp
		bodyLyt.putConstraint(N, lastOpT, 10, S, account); bodyLyt.putConstraint(E, lastOpT, -5, E, body);
		body.add(lastOpT);
		bodyLyt.putConstraint(N, lastOp, 0, N, lastOpT); bodyLyt.putConstraint(E, lastOp, -5, W, lastOpT);
		body.add(lastOp);
		bodyLyt.putConstraint(N, dateOp, 5, S, lastOp); bodyLyt.putConstraint(W, dateOp, 0, W, lastOp);
		body.add(dateOp);
		bodyLyt.putConstraint(N, dateOpT, 0, N, dateOp); bodyLyt.putConstraint(W, dateOpT, 5, E, dateOp);
		body.add(dateOpT);
		// gender
		bodyLyt.putConstraint(N, mr, 25, S, imageLbl); bodyLyt.putConstraint(W, mr, 5, W, body);
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
		// buttons
		bodyLyt.putConstraint(S, modify, -10, S, body); bodyLyt.putConstraint(E, modify, -5, E, body);
		body.add(modify);
		bodyLyt.putConstraint(S, transactions, -10, S, body); bodyLyt.putConstraint(E, transactions, -5, W, modify);
		body.add(transactions);
		bodyLyt.putConstraint(S, operations, -10, S, body); bodyLyt.putConstraint(E, operations, -5, W, transactions);
		body.add(operations);
	}

	/**
	 * Don't change image if file not found
	 */
	private void loadInfo(Patient patient) {
		nomT.setText(patient.getlName());
		prenomT.setText(patient.getfName());
		mr.setEnabled(false);
		mme.setEnabled(false);
		if (patient.getSex() == 'W') {mr.setSelected(false); mme.setSelected(true);}
		else {mr.setSelected(true); mme.setSelected(false);}
		if (patient.getBirthdate() != null) pckDate.setText(patient.getBirthdate().toString());
		telPortableT.setText(patient.getPhone());
		telFixeT.setText(patient.getFixe());
		emailT.setText(patient.getEmail());
		adresseT.setText(patient.getAddress());
		mutuType.setText(patient.getMutuelle());
		mutuNum.setText("N°: " + patient.getMutuNum());
		accountT.setText(Integer.toString(patient.getAccount()));
		lastOpT.setText(patient.getLastOp());
		if (patient.getDateLastOp() != null) dateOpT.setText(patient.getDateLastOp().toString());
	}
}
