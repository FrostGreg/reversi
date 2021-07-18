/* Sqaure Class / Author: @Greg.Frost
 * 
 * - extends JButton, implements MouseListener interface
 * - handles button graphics
 * */

package reversi;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class Square extends JButton implements MouseListener, ActionListener{
	Color drawColor, borderColor, originalColor, backColor = Color.green;
	int row, col;
	Frame frame;
	
	public Square(int size, int R, int C, Frame F)
	{
		row = R;
		col = C;
		frame = F;
		
		// Call inherited methods from JButton
		setPreferredSize(new Dimension(size, size));
		
		//creates and sets the border
		setBorder(BorderFactory.createLineBorder(Color.black));
		
		//gives button a mouse listener
		addMouseListener(this);
		addActionListener(this);
	}

	public void paintComponent(Graphics g) {
		//creates background the same size as the button
		g.setColor(backColor);
		g.fillRect(0,0,50,50);
		
		//creates a larger circle as an outline
		g.setColor(borderColor);
		g.fillOval(3, 3, 44, 44);
		
		//creates a smaller circle for the main disk
		g.setColor(drawColor);
		g.fillOval(5, 5, 40, 40);
	}
	
	//changes the disk colour
	public void setDiskColour(Color clr) {
		drawColor = clr;
		
		//sets the outline to be the opposite of the disk colour
		if (drawColor == Color.WHITE)
			borderColor = Color.BLACK;
		else if (drawColor == Color.BLACK)
			borderColor = Color.WHITE;
		else
			borderColor = backColor; //if it's not a piece keep the color the same as the background
	}
	
	//returns true of that square contains a piece
	public boolean isPiece() {
		if (drawColor == Color.WHITE || drawColor == Color.BLACK)
			return true;
		return false;
	}
	
	//The MouseListener Methods
	@Override
	public void mouseClicked(MouseEvent e) {
	}

	//When the mouse is pressed, makes it a darker colour than the hover
	@Override
	public void mousePressed(MouseEvent e) {
		Color highlight = new Color(0, 150, 0);
		backColor = highlight;
		
		//if there is a piece there only change the background
		if (isPiece())
			return;
		setDiskColour(highlight);
	}
	
	//revert the piece back to it's original was the mouse is released
	@Override
	public void mouseReleased(MouseEvent e) {
		backColor = Color.GREEN;
		
		//if there is a piece there don't change the disk
		if (isPiece())
			return;
		setDiskColour(Color.GREEN);
	}
	
	//highlight the square when the user hover's over it
	@Override
	public void mouseEntered(MouseEvent e) {
		//set background to a darker colour
		originalColor = drawColor;
		Color selected = new Color(0, 200, 0);
		backColor = selected;
		
		//only change the center circle if there is not a piece there
		if (isPiece())
			return;
		setDiskColour(selected);
	}
	
	//reverts the square back to it's original when the mouse is no longer hovered over it
	@Override
	public void mouseExited(MouseEvent e) {
		//sets the background back to default green
		backColor = Color.GREEN;
		
		//if there is a piece there don't change it's look
		if (isPiece())
			return;
		setDiskColour(originalColor);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		frame.btnClick(row, col);
	}
}