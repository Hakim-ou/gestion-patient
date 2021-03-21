package main;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import lib.*;
import main.Home;


public class Checking extends JPanel{
	/* components */
	Color bege, orange, bloody;
	JPanel barre, body;
	HoverJButton back, searchBtn;
	JTextField searchTxt;
	JList patientsList;
	JScrollPane sp;
	DefaultListModel<Patient> model = new DefaultListModel<>();

	/* constructor */
	public Checking(JFrame main, JPanel home, JPanel precedent){
		bege = new Color(255, 255, 153);
		orange = new Color(255, 128, 0);
		bloody = new Color(138, 3, 3);

		// page configuration
		setSize(500, 300);
		setLayout(new BorderLayout(0, 0));

		// main panels
		barre = new JPanel(new FlowLayout(FlowLayout.LEADING, 5, 5));
		barre.setBackground(Color.white);
		add(barre, BorderLayout.PAGE_START); 
		SpringLayout bodyLayout = new SpringLayout();
		JPanel body = new JPanel(bodyLayout); 
		body.setBackground(bloody);
		add(body, BorderLayout.CENTER); 

		// barre components
		back = new HoverJButton("Back", bege);
		back.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				main.remove(Checking.this);
				main.add(precedent, BorderLayout.CENTER);
				main.validate();
				main.repaint();
			}
		});
		barre.add(back);

		// body components
		searchTxt = new JTextField(30);
		final JTextComponent tc = (JTextComponent)searchTxt;

		tc.getDocument().addDocumentListener(new DocumentListener(){
			@Override
			public void changedUpdate(DocumentEvent arg0) {}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				setPatientList(main, searchTxt.getText(), false);
				body.validate();
				body.repaint();
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				setPatientList(main, searchTxt.getText(), false);
				body.validate();
				body.repaint();
			}
		});
		searchBtn = new HoverJButton("search", orange);

		setPatientList(main, "", true);
		MouseAdapter adapter = new MouseAdapter(){
			@Override
			public void mouseMoved(MouseEvent e){
				int index = patientsList.locationToIndex(e.getPoint());
				if (index > -1 && patientsList.getCellBounds(index, index).contains(e.getPoint())){
					Object value = model.getElementAt(index);
					Component cell = patientsList.getCellRenderer().getListCellRendererComponent(patientsList, (Patient)value,
																								 index, true, true);
					Rectangle rectCell = patientsList.getCellBounds(index, index);
					cell.setBounds(rectCell);
					if (cell instanceof JPanel){
						Point pt = new Point(e.getPoint());
						pt.translate(-cell.getX(), -cell.getY());
						Component panel = cell.getComponentAt(pt);
						if (panel instanceof JPanel){
							pt.translate(-panel.getX(), -panel.getY());
							Component label = panel.getComponentAt(pt);
							Patient patient = (Patient)value;
							if (label instanceof JLabel && ((JLabel)label).getText().equals("details")){
								patientsList.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
							}else{
								patientsList.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
							}
						}
					}
				}
			}

			@Override
			public void mouseClicked(MouseEvent e){
				int index = patientsList.locationToIndex(e.getPoint());
				if (index > -1 && patientsList.getCellBounds(index, index).contains(e.getPoint())){
					Object value = patientsList.getModel().getElementAt(index);
					Component cell = patientsList.getCellRenderer().getListCellRendererComponent(patientsList, (Patient)value,
																								 index, true, true);
					Rectangle rectCell = patientsList.getCellBounds(index, index);
					cell.setBounds(rectCell);
					if (cell instanceof JPanel){
						Point pt = new Point(e.getPoint());
						pt.translate(-cell.getX(), -cell.getY());
						Component panel = cell.getComponentAt(pt);
						if (panel instanceof JPanel){
							pt.translate(-panel.getX(), -panel.getY());
							Component label = panel.getComponentAt(pt);
							if (label instanceof JLabel && ((JLabel)label).getText().equals("details")){
								Patient patient = (Patient)value;
								new Details(patient.main, patient);
							}
						}
					}
				}
			}
		};
		patientsList.addMouseMotionListener(adapter);
		patientsList.addMouseListener(adapter);
		sp = new JScrollPane(patientsList);

		bodyLayout.putConstraint(SpringLayout.NORTH, searchTxt, 20, SpringLayout.NORTH, body);
		bodyLayout.putConstraint(SpringLayout.NORTH, searchBtn, 20, SpringLayout.NORTH, body);
		bodyLayout.putConstraint(SpringLayout.WEST, searchTxt, 45, SpringLayout.WEST, body);
		bodyLayout.putConstraint(SpringLayout.WEST, searchBtn, 5, SpringLayout.EAST, searchTxt);
		bodyLayout.putConstraint(SpringLayout.SOUTH, searchBtn, 0, SpringLayout.SOUTH, searchTxt);
		bodyLayout.putConstraint(SpringLayout.NORTH, sp, 20, SpringLayout.SOUTH, searchTxt);
		bodyLayout.putConstraint(SpringLayout.WEST, sp, 5, SpringLayout.WEST, body);
		bodyLayout.putConstraint(SpringLayout.EAST, sp, -5, SpringLayout.EAST, body);
		bodyLayout.putConstraint(SpringLayout.SOUTH, sp, -5, SpringLayout.SOUTH, body);

		body.add(searchBtn);
		body.add(searchTxt);
		body.add(sp);
	}

	/* methods */
	public void setPatientList(JFrame main, String initial, boolean first){
		model.clear();
		for (String lname : ((Home)main).patients.keySet()){
			for (String fname : ((Home)main).patients.get(lname)){
				if ((fname + " " + lname).substring(0, initial.length()).toLowerCase().equals(initial.toLowerCase()) ||
					   	initial.equals("")){
					try{
						model.addElement(new Patient(main, fname, lname));
					}catch(Patient.PatientDoesNotExist exc){
					}
				}
			}
		}
		if (first)
			patientsList = new JList<Patient>(model);
		else
			patientsList.setModel(model);
		patientsList.setCellRenderer(new Patient.PatientListRenderer());
	}
}
