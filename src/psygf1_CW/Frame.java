/* Frame Class / Author: @psygf1
 * 
 * - extends JFrame, implements ActionListener interface
 * - Handles button clicks for grid
 * - creates both frames, the board state, and an AI for each frame.
 * */

package psygf1_CW;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class Frame extends JFrame {
	Square[][] grid = new Square[8][8];
	JLabel playerTurn = new JLabel();
	public Ai btnAI;
	public String framePlayer;
	public ReversiMain gameMain;
	public BoardState boardGlobal;
	
	public Frame(String player, BoardState board, ReversiMain game) {
		//sets board and player to global variables
		boardGlobal = board;
		framePlayer = player;
		gameMain = game;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //application stops running when closed
		
		//declares the 3 sections of the frame as per the specification
		JPanel panelLabel = new JPanel();
		JPanel panelGrid = new JPanel();
		JPanel panelButton = new JPanel();
		
		//sets the frame to a borderLayout for all panels
		getContentPane().setLayout(new BorderLayout());
		panelLabel.setLayout(new BorderLayout());
		panelLabel.add(panelGrid, BorderLayout.CENTER);
		//the Grid panel is unique, this is an 8x8 grid layout to represent the 8x8 board
		panelGrid.setLayout(new GridLayout(8,8));
		panelButton.setLayout(new BorderLayout());
		panelLabel.add(panelButton, BorderLayout.SOUTH);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(panelLabel);
		//defaults the frame to the white frame favicon
		URL iconURL = getClass().getResource("/faviconWF.png");
		//displays the white player frame with a standard view
		if (framePlayer == "White") {
			for (int row = 0; row < 8; row++) {
				for (int column = 0; column < 8; column++) {
					addSquareGrid(row, column, panelGrid);
				}
			}
			
			//displays the board with 180 degree rotation for black player frame
		} else if (framePlayer == "Black") {
			for (int row = 7; row >= 0; row--) {
				for (int column = 7; column >= 0; column--) {
					addSquareGrid(row, column, panelGrid);
				}
			}
			//sets the Icon to the black frame version
			iconURL = getClass().getResource("/faviconBF.png");
		}
		// Ensures the title of the frame says which player it belongs to
		setTitle("Reversi - " + player + " Player");
		playerTurn.setHorizontalAlignment(SwingConstants.CENTER);
			
		//creates an icon from a given file and sets the frames favicon
		ImageIcon icon = new ImageIcon(iconURL);
		setIconImage(icon.getImage());
		
		//adds the label telling who's turn it is to the top of the frame
		panelLabel.add(playerTurn, BorderLayout.NORTH);
		
		//creates new button linked to the greedy AI, this is positioned at the bottom of the frame
		JButton btn = new JButton("Greedy AI (Play " + player + ")");
		btn.addActionListener(new Ai(boardGlobal, player, gameMain)); 
		panelButton.add(btn, BorderLayout.SOUTH);
		
		//sizes the frame so each component is at its preferred size
		pack();
		setLocationRelativeTo(null); // puts frame in the middle of the screen
		setVisible(true);
		updateFrame(); //re-uses the updateFrame method as it draws the grid squares
	}
	
	//adds a button square to the frame grid based on inputed row and column values
	public void addSquareGrid(int row, int column, JPanel panelGrid) {
		Square sq = new Square(50, row, column, this);
		grid[row][column] = sq;
		panelGrid.add(grid[row][column]);
	}
	
	//returns true if it's that frames turn to play
	public boolean isPlayerTurn() {
		if (boardGlobal.isWhiteTurn && framePlayer == "White")
			return true;
		if (!boardGlobal.isWhiteTurn && framePlayer == "Black")
			return true;
		return false;
	}
	
	//redraws the grid within the frame
	public void updateFrame() {
		for (int i=0; i < 8; i++) {
			for (int j=0; j < 8;j++) {
				String piece = boardGlobal.boardPos[i][j];
				if (piece == "W")
					grid[i][j].setDiskColour(Color.WHITE);
				else if (piece == "B")
					grid[i][j].setDiskColour(Color.BLACK);
				else
					grid[i][j].setDiskColour(Color.GREEN);
				grid[i][j].repaint();
				
			}	
		}
		//Updates label to say if its the frame player's turn or not
		if (framePlayer == "White") {
			if (isPlayerTurn())
				playerTurn.setText("White Player Turn - Click Place to Put Piece");
			else
				playerTurn.setText("White Player - Not Your Turn");
		}else if (framePlayer == "Black"){
			if (isPlayerTurn())
				playerTurn.setText("Black Player Turn - Click Place to Put Piece");
			else
				playerTurn.setText("Black Player - Not Your Turn");
		}else {
			System.out.println("Illegal");
		}
		repaint(); //forces the frame to re-draw itself after changes have been made
	}
	
	public void btnClick(int row, int col) {
		if (!isPlayerTurn()) {
			return;
		}
		//if valid then the piece will be captured if not user can try again on another piece
		if (!boardGlobal.cap(col, row))
			return;
		
		//swap the player as the move was valid
		boardGlobal.swapActivePlayer();
		//check if there are any possible moves
		if (!boardGlobal.anyPossibleMoves()) {
			boardGlobal.swapActivePlayer();	//if no possible moves exist return play back to the original player
			if (!boardGlobal.anyPossibleMoves()) {
				gameMain.endGame();			//if there are no plays for either player end the game
			}
		}
		//update both frames with the changes
		gameMain.WF.updateFrame();
		gameMain.BF.updateFrame();
	}
}