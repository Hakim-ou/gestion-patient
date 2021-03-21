package main;


import java.util.Map;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.table.DefaultTableModel;
import lib.*;
import main.Home;


public class Details extends JFrame {
	/* components */
	Color bege, orange, bloody;
	JLabel title, nameLbl, sexAgeLbl, phoneLbl, emailLbl, addressLbl, mutuelleLbl, accountLbl, imageLbl;
	JTextArea addressTa;
    HoverJButton back, modify;
	JScrollPane sp;
	JTable table;
	JPanel barre, body;

	/* constructor */
	public Details(JFrame main, Patient patient){
		bege = new Color(255, 255, 153);
		orange = new Color(255, 128, 0);
		bloody = new Color(138, 3, 3);

		// page configuration
		setSize(650, 700);
		setLayout(new BorderLayout(0, 0));
		setVisible(true);
		// closing
		addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){
				Details.this.dispose();
			}
		});

		SpringLayout bodyLayout = new SpringLayout();
		JPanel body = new JPanel(bodyLayout); 
		body.setBackground(bloody);
		body.setPreferredSize(new Dimension(650, 700));

		// body components
		title = new JLabel("Fiche du Malade");
		Font font = new Font(Font.MONOSPACED, Font.ITALIC, 32);
		Map attributes = font.getAttributes();
		attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		title.setFont(font.deriveFont(attributes));

		nameLbl = new JLabel("Full Name: " + patient.getfName() + " " + patient.getlName());
		sexAgeLbl = new JLabel("Sex & Age: " + ((patient.getSex() == 'M')?"Man":"Woman") + ",  " + patient.getAge() + "years old");
		phoneLbl = new JLabel("Phone number: " + patient.getPhone());
		emailLbl = new JLabel("Email: " + patient.getEmail());
		addressLbl = new JLabel("Address: ");
		addressTa = new JTextArea();
		addressTa.setEditable(false);
		addressTa.setText(patient.getAddress());
		mutuelleLbl = new JLabel("Mutuelle: " + patient.getMutuelle());
		imageLbl = new JLabel();

		accountLbl = new JLabel("Account: " + (patient.getAccount()));
		if (patient.getAccount() >= 0)
			accountLbl.setForeground(Color.GREEN);
		else
			accountLbl.setForeground(Color.RED);

		if (patient.getImage().equals("None")){
			ImageIcon imageicon = new ImageIcon("./images/unknown.png");
			Image img = imageicon.getImage();
			img = img.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
			imageicon = new ImageIcon(img);
			imageLbl.setIcon(imageicon);
		}else{
			ImageIcon imageicon = new ImageIcon(patient.getImage());
			Image img = imageicon.getImage();
			img = img.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
			imageicon = new ImageIcon(img);
			imageLbl.setIcon(imageicon);
		}

		modify = new HoverJButton("modify", orange);
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
					System.out.println(selectedFile.getAbsolutePath());
					String[] sHelper = selectedFile.getAbsolutePath().split("\\.");
					System.out.println(sHelper.length);
					String extension = sHelper[sHelper.length - 1];
					System.out.println(extension);
					assert (extension.equals("jpeg") || extension.equals("png") || extension.equals("jpg"));
					String path = "./images/profile_" + patient.getfName().toLowerCase() + "_"
													  + patient.getlName().toLowerCase()
													  + "." + extension;
					File imageFile = FileAccess.freeFile(path);
					InputStream is = null;
					OutputStream os = null;
					try{
						is = new FileInputStream(jfc.getSelectedFile());
						os = new FileOutputStream(imageFile);
						byte[] buffer = new byte[1024];
						int length;
						while ((length = is.read(buffer)) > 0)
							os.write(buffer, 0, length);
						patient.setImage(path);
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
		
					if (patient.getImage().equals("None")){
						ImageIcon imageicon = new ImageIcon(getClass().getResource(".././images/unknown.png"));
						Image img = imageicon.getImage();
						img = img.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
						imageicon = new ImageIcon(img);
						imageLbl.setIcon(imageicon);
					}else{
						ImageIcon imageicon = new ImageIcon(getClass().getResource("../" + patient.getImage()));
						Image img = imageicon.getImage();
						img = img.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
						imageicon = new ImageIcon(img);
						imageLbl.setIcon(imageicon);
					}
				}
			}
		});

		// history
		patient.loadHistory();
		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(patient.getHistoryColumnNames());
		for (String[] operation : patient.getHistory())
			model.addRow(operation);
		table = new JTable();
		table.setModel(model);
		sp = new JScrollPane(table);

		// constraints
		bodyLayout.putConstraint(SpringLayout.NORTH, title, 10, SpringLayout.NORTH, body);
		bodyLayout.putConstraint(SpringLayout.WEST, title, 0, SpringLayout.WEST, body);

		bodyLayout.putConstraint(SpringLayout.NORTH, nameLbl, 25, SpringLayout.SOUTH, title);
		bodyLayout.putConstraint(SpringLayout.WEST, nameLbl, 5, SpringLayout.WEST, body);

		bodyLayout.putConstraint(SpringLayout.NORTH, sexAgeLbl, 5, SpringLayout.SOUTH, nameLbl);
		bodyLayout.putConstraint(SpringLayout.WEST, sexAgeLbl, 5, SpringLayout.WEST, body);

		bodyLayout.putConstraint(SpringLayout.NORTH, phoneLbl, 5, SpringLayout.SOUTH, sexAgeLbl);
		bodyLayout.putConstraint(SpringLayout.WEST, phoneLbl, 5, SpringLayout.WEST, body);

		bodyLayout.putConstraint(SpringLayout.NORTH, emailLbl, 5, SpringLayout.SOUTH, phoneLbl);
		bodyLayout.putConstraint(SpringLayout.WEST, emailLbl, 5, SpringLayout.WEST, body);

		bodyLayout.putConstraint(SpringLayout.NORTH, addressLbl, 5, SpringLayout.SOUTH, emailLbl);
		bodyLayout.putConstraint(SpringLayout.WEST, addressLbl, 5, SpringLayout.WEST, body);
		bodyLayout.putConstraint(SpringLayout.NORTH, addressTa, 5, SpringLayout.SOUTH, emailLbl);
		bodyLayout.putConstraint(SpringLayout.WEST, addressTa, 5, SpringLayout.EAST, addressLbl);

		bodyLayout.putConstraint(SpringLayout.NORTH, mutuelleLbl, 5, SpringLayout.SOUTH, addressTa);
		bodyLayout.putConstraint(SpringLayout.WEST, mutuelleLbl, 5, SpringLayout.WEST, body);

		bodyLayout.putConstraint(SpringLayout.NORTH, sp, 25, SpringLayout.SOUTH, mutuelleLbl);
		bodyLayout.putConstraint(SpringLayout.WEST, sp, 5, SpringLayout.WEST, body);
		bodyLayout.putConstraint(SpringLayout.EAST, sp, -5, SpringLayout.EAST, body);

		bodyLayout.putConstraint(SpringLayout.NORTH, imageLbl, 15, SpringLayout.SOUTH, title);
		bodyLayout.putConstraint(SpringLayout.EAST, imageLbl, -15, SpringLayout.EAST, body);

		bodyLayout.putConstraint(SpringLayout.NORTH, modify, 5, SpringLayout.SOUTH, imageLbl);
		bodyLayout.putConstraint(SpringLayout.EAST, modify, -15, SpringLayout.EAST, body);
		//bodyLayout.putConstraint(SpringLayout.NORTH, sp, 5, SpringLayout.SOUTH, modify);

		bodyLayout.putConstraint(SpringLayout.NORTH, accountLbl, 5, SpringLayout.SOUTH, sp);
		bodyLayout.putConstraint(SpringLayout.SOUTH, accountLbl, -5, SpringLayout.SOUTH, body);
		bodyLayout.putConstraint(SpringLayout.EAST, accountLbl, -35, SpringLayout.EAST, sp);

		// adding components to body
		body.add(title);
		body.add(nameLbl);
		body.add(sexAgeLbl);
		body.add(phoneLbl);
		body.add(emailLbl);
		body.add(addressLbl);
		body.add(addressTa);
		body.add(mutuelleLbl);
		body.add(sp);
		body.add(accountLbl);
		body.add(imageLbl);
		body.add(modify);
		for (Component c : body.getComponents()){
			if (c instanceof JLabel && !c.equals(imageLbl) && !c.equals(accountLbl))
				((JLabel)c).setForeground(bege);
		}

		// adding body
		//JScrollPane mainSp = new JScrollPane(body, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		//mainSp.setPreferredSize(new Dimension(600, 400));
		JScrollPane mainSp = new JScrollPane();
		mainSp.setViewportView(body);
		add(mainSp, BorderLayout.CENTER); 
	}
}
