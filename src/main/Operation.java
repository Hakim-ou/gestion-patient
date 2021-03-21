package main;


import java.util.*;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.text.*;
import javax.swing.text.Highlighter.*;
import java.time.*;
import java.time.format.*;
import lib.*;


public class Operation extends JPanel{
	/* components */
	Color bege, orange, bloody;
	List<String> operations = Arrays.asList("First",
						   "Second",
						   "Third",
						   "Forth");
	JComboBox choice;
	JPanel barre, body;
	HoverJButton back, cancel, setTime, start, finish;
	Instant startTime;
	long duration;
	StringBuilder record = new StringBuilder();
	String mTooth, operation;
	Image tooth;

	/* constructor */
	public Operation(JFrame main, JPanel home, JPanel precedent, String fname, String lname, String choosedTooth){
		bege = new Color(255, 255, 153);
		orange = new Color(255, 128, 0);
		bloody = new Color(138, 3, 3);

		mTooth = choosedTooth;

		setSize(500, 300);
		setLayout(new BorderLayout(0, 0));

		barre = new JPanel(new FlowLayout(FlowLayout.LEADING, 5, 5));
		barre.setBackground(Color.white);
		add(barre, BorderLayout.PAGE_START); 
		JPanel body = new JPanel(new GridBagLayout()); 
		body.setBackground(bloody);
		add(body, BorderLayout.CENTER); 

		back = new HoverJButton("Back", bege);
		back.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				FileAccess.eraseLast("./files/patients_" + fname.toLowerCase() + "_" + lname.toLowerCase() + ".csv");
				main.remove(Operation.this);
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
				FileAccess.eraseLast("./files/patients_" + fname.toLowerCase() + "_" + lname.toLowerCase() + ".csv");
				main.remove(Operation.this);
				main.add(home, BorderLayout.CENTER);
				main.validate();
				main.repaint();
			}
		});
		barre.add(cancel);
		
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridx = 10;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(0, 0, 8, 0);
		try{
			InputStream imageStream = getClass().getResourceAsStream("/images/lateral_incisor.png");
			BufferedImage image = ImageIO.read(imageStream);
			int newW = image.getWidth(null) / 2;
		   	int newH = image.getHeight(null) / 2;
			// resizing the image
			Image tmp = image.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
			BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = dimg.createGraphics();
			g2d.drawImage(tmp, 0, 0, null);
			g2d.dispose();
			// inserting it
			JLabel piclabel = new JLabel(new ImageIcon(dimg));
			body.add(piclabel, gbc);
		}catch(IOException e){
			e.printStackTrace();
		}

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 6;
		gbc.gridheight = 3;
		gbc.fill = GridBagConstraints.BOTH;
		JPanel pane1 = new JPanel(new GridBagLayout());
		pane1.setBackground(Color.WHITE);
		JTextArea msg = new JTextArea("You choosed: " + choosedTooth + "\nPlease choose the corresponding Operation " + 
				"\nor get back to change tooth");
		Highlighter highlighter = msg.getHighlighter();
		HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);
		int p0 = msg.getText().indexOf(choosedTooth);
		int p1 = p0 + choosedTooth.length();
		try{
			highlighter.addHighlight(p0, p1, painter);
		}catch (BadLocationException e){
			e.printStackTrace();
		}
		msg.setForeground(bloody);
		pane1.add(msg);
		body.add(pane1, gbc);

		gbc.gridx = 0;
		gbc.gridy = 6;
		gbc.gridwidth = 6;
		gbc.gridheight = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(8, 0, 4, 0);
		choice = new AutocompleteJComboBox(new StringSearchable(operations));
		choice.setEditable(true);
		choice.addActionListener(new ActionListener(){//TODO autocompletion
			@Override
			public void actionPerformed(ActionEvent e){
				String choosedOperation = (String)choice.getSelectedItem();
				System.out.println(choosedOperation);
			}
		});
		body.add(choice, gbc);

		JPanel pane2 = new JPanel(new FlowLayout());
		pane2.setBackground(bloody);

		setTime = new HoverJButton("set Time", new Color(234, 122, 215));
		setTime.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				if (!operations.contains(choice.getSelectedItem())){
					return;
				}
				JDialog jd = new JDialog(main, "Time Setter", true);
				jd.setLayout(new GridBagLayout());
				jd.setSize(500, 150);

				gbc.gridx = 2;
				gbc.gridy = 0;
				gbc.gridwidth = 10;
				gbc.gridheight = 3;
				gbc.fill = GridBagConstraints.BOTH;
				gbc.insets = new Insets(4, 0, 4, 0);
				JTextArea msg1 = new JTextArea("\tPlease set the duration of the operation\n" +
						   	"Note: using the programm's timer is always better, please use it next time !");
				msg1.setForeground(bloody);
				msg1.setBackground(Color.WHITE);
				msg1.setEditable(false);
				jd.add(msg1, gbc);

				gbc.gridx = 4;
				gbc.gridy = 8;
				gbc.gridwidth = 6;
				gbc.gridheight = 1;
				gbc.fill = GridBagConstraints.HORIZONTAL;
				gbc.insets = new Insets(8, 0, 4, 0);
				JPanel pane3 = new JPanel(new FlowLayout());
				pane3.add(new JLabel("hours: "));
				JTextField hh = new JTextField("00");
				pane3.add(hh);
				pane3.add(new JLabel("minutes: "));
				JTextField mm = new JTextField("00");
				pane3.add(mm);
				pane3.add(new JLabel("seconds: "));
				JTextField ss = new JTextField("00");
				pane3.add(ss);
				jd.add(pane3, gbc);

				gbc.gridx = 4;
				gbc.gridy = 12;
				gbc.gridwidth = 6;
				gbc.gridheight = 1;
				gbc.fill = GridBagConstraints.HORIZONTAL;
				gbc.insets = new Insets(2, 0, 4, 0);
				HoverJButton submit = new HoverJButton("submit", bloody);
				submit.setForeground(Color.WHITE);
				submit.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent evt){
						duration = Integer.parseInt(hh.getText()) * 3600 +
								   Integer.parseInt(mm.getText()) * 60 +
								   Integer.parseInt(ss.getText());
						Operation.this.setRecord(false);
						try{
							// write statistics
							FileAccess.writeLine("./files/records.csv", record.toString());
							String helperRecord = (String)choice.getSelectedItem() + ';' +
								   	LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()).toString();
							// complete patient informations
							FileAccess.writeLine("./files/patient_"+ fname.toLowerCase() + "_" + lname.toLowerCase() +
												".csv", helperRecord);
							String num = ((Integer.parseInt(FileAccess.readLine("./files/serial.txt", 1))) + 1) + "";
							FileAccess.eraseLast("./files/serial.txt");
							FileAccess.writeLine("./files/serial.txt", num);
						}catch(IOException exc){
							exc.printStackTrace();
						}
						start.setEnabled(false);
						finish.setEnabled(false);
						jd.setVisible(false);
						main.remove(Operation.this);
						main.add(home, BorderLayout.CENTER);
						main.validate();
						main.repaint();
					}
				});
				jd.add(submit, gbc);

				jd.setVisible(true);
			}
		});
		pane2.add(setTime);

		start = new HoverJButton("start Timer", new Color(218, 184, 243));
		start.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				if (!operations.contains(choice.getSelectedItem())){
					return;
				}
				start.setEnabled(false);
				finish.setEnabled(true);
				startTime = Instant.now();
			}
		});
		pane2.add(start);

		finish = new HoverJButton("stop Timer", new Color(222, 221, 250));
		finish.setEnabled(false);
		finish.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				duration = Duration.between(startTime, Instant.now()).getSeconds();
				Operation.this.setRecord(true);
				try{
					// write statistics
					FileAccess.writeLine("./files/records.csv", record.toString());
					String helperRecord = (String)choice.getSelectedItem() + ';' +
						   	LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()).toString();
					// complete patient informations
					FileAccess.writeLine("./files/patient_"+ fname.toLowerCase() + "_" + lname.toLowerCase() +
										".csv", helperRecord);
					String num = ((Integer.parseInt(FileAccess.readLine("./files/serial.txt", 1))) + 1) + "";
					FileAccess.eraseLast("./files/serial.txt");
					FileAccess.writeLine("./files/serial.txt", num);
				}catch(IOException exc){
					exc.printStackTrace();
				}
				finish.setEnabled(false);
				main.remove(Operation.this);
				main.add(home, BorderLayout.CENTER);
				main.validate();
				main.repaint();
			}
		});
		pane2.add(finish);

		gbc.gridx = 0;
		gbc.gridy = 10;
		gbc.gridwidth = 6;
		gbc.gridheight = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.LAST_LINE_END;
		gbc.insets = new Insets(8, 0, 4, 0);
		body.add(pane2, gbc);
	}

	private void setRecord(boolean fromTimer){// fromTimer is true if the method is called from the programm's timer
		if (!fromTimer){
			startTime = Instant.now();
		}
		LocalDateTime dateStartTime = LocalDateTime.ofInstant(startTime, ZoneId.systemDefault());
		record.append('\n');
		record.append(mTooth);
		record.append(';');
		record.append((String)choice.getSelectedItem());
		record.append(';');
		record.append(dateStartTime.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault()));
		record.append(';');
		record.append(Integer.toString(dateStartTime.getDayOfMonth()));
		record.append(';');
		record.append(dateStartTime.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()));
		record.append(';');
		record.append(Integer.toString(dateStartTime.getHour()) + "h" + Integer.toString(dateStartTime.getMinute()));
		record.append(';');
		record.append(Long.toString(duration) + "sec");
		record.append(';');
		record.append(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()) + "");
		System.out.println(record);
	}
}
