package lib;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class HoverJButton extends JButton implements MouseListener{
	private Color OriginalColor;
	/* constructor */
	public HoverJButton(String title, Color givenColor){
		super(title);
		this.OriginalColor = givenColor;
		this.setBackground(OriginalColor);
		this.setBorderPainted(false);
		addMouseListener(this);
	}

	/* methods */
	@Override
	public void setBackground(Color c){
		super.setBackground(c);
		this.OriginalColor = c;
	}

	public void mouseEntered(MouseEvent evt){
		if (this.isEnabled()){
			super.setBackground(OriginalColor.brighter());
		}
	}

	public void mouseExited(MouseEvent evt){
		if (this.isEnabled()){
			super.setBackground(OriginalColor);
		}
	}

	public void mouseClicked(MouseEvent evt){}
	public void mousePressed(MouseEvent evt){}
	public void mouseReleased(MouseEvent evt){}


}


