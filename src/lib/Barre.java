package lib;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class Barre extends JPanel {
	public Barre(JFrame main, JPanel prev, JPanel curr) {
		setLayout(new FlowLayout(FlowLayout.LEADING, 5, 5));
		this.setBackground(Color.white);

		JButton back = new HoverJButton("Back", new Color(255, 255, 153));
		back.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				main.remove(curr);
				main.add(prev, BorderLayout.CENTER);
				main.validate();
				main.repaint();
			}
		});
		this.add(back);
	}
}
