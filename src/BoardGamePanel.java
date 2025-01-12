import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

class BoardGamePanel extends JPanel {

	private int[][] board;
	private Question[][] questions;
	private String turnColor;
	private BlockBusters blockb; // for Question JDialog

	public BoardGamePanel(BlockBusters blockb, int[][] board, Question[][] questions) {
		setBackground(Constants.COLOURONE);
		this.blockb = blockb;
		this.board = board;
		this.questions = questions;
		turnColor = "white";

		
		// set teams names
		
		blockb.setMyTeamName(JOptionPane.showInputDialog(this, "Enter name:"));
		blockb.setOpponentTeamName(JOptionPane.showInputDialog(this, "Enter name:"));
		
		// set teams background color
		blockb.getMyTeamPanel().setTeamColorByID(1);
		blockb.getOpponentTeamPanel().setTeamColorByID(2);
		
		
		setLayout(new BorderLayout());

		MyMouseListener ml = new MyMouseListener();
		addMouseListener(ml);
	}

	void displayLogicBoard2Console() {

		for (int i = 0; i < Constants.BSIZE; i++) {
			for (int j = 0; j < Constants.BSIZE; j++) {
				if (board[i][j] == Constants.EMPTY)
					System.out.print("0 ");
				else
					System.out.print(board[i][j] + " ");
			}

			System.out.println();

		}

		System.out.println();
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		super.paintComponent(g2);
		// draw grid
		for (int i = 0; i < Constants.BSIZE; i++) {
			for (int j = 0; j < Constants.BSIZE; j++) {
				Hexagon.drawHex(i, j, g2);
			}
		}
		// fill in hexes
		for (int i = 0; i < Constants.BSIZE; i++) {
			for (int j = 0; j < Constants.BSIZE; j++)
				Hexagon.fillHex(i, j, board[j][i], g2); // here
		}

	}
	ArrayList<Point> bluePoints = new ArrayList<>();
	ArrayList<Point> whitePoints = new ArrayList<>();
	
	int counterTurns = 0;
		
	public void checkWinner() {
		// check if we have winner:
		// 1 white team (left - right)
		// 2 blue team (buttom - up)
		int algorithmResult = Algorithm.hasSequence(board);

		if (algorithmResult == 0)
			System.out.println("no winner till now");
		else if (algorithmResult == 1) {
			System.out.println("1 is the winner");
			JOptionPane.showMessageDialog(blockb, blockb.getMyTeamName() + "win");
			// System.exit(0);
		} else {
			System.out.println("2 is the winner");
			JOptionPane.showMessageDialog(blockb, blockb.getOpponentTeamName() + "win");
			// System.exit(0);
		}
		System.out.println("now the conter is: " + counterTurns);

		if (counterTurns == 25) {
			if (Integer.parseInt(blockb.getOpponentTeamScore()) > Integer.parseInt(blockb.getMyTeamScore())) {
				System.out.println(blockb.getOpponentTeamName() + "win");
				JOptionPane.showMessageDialog(blockb, blockb.getOpponentTeamName() + "win");
			} else if (Integer.parseInt(blockb.getOpponentTeamScore()) < Integer.parseInt(blockb.getMyTeamScore())) {
				System.out.println(blockb.getMyTeamName() + "win");
				JOptionPane.showMessageDialog(blockb, blockb.getMyTeamName() + "win");
			} else {
				System.out.println("No winner Today!");
				JOptionPane.showMessageDialog(blockb, "No winner Today!");
			}
		} // end if counter turns
	}

	class MyMouseListener extends MouseAdapter {// inner class
		
		public boolean isPointExist(ArrayList<Point> arr, Point p) {
			for(int i= 0; i< arr.size(); i++) {
				if (arr.get(i).getX() == p.getX() &&arr.get(i).getY() == p.getY())
					return true;
			}
			return false;
		}
		public void mouseClicked(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();
			
			
			
			Point p = new Point(Hexagon.pxtoHex(e.getX(), e.getY()));
			
			if (board[p.y][p.x] == 0) {
				
			if (p.x < 0 || p.y < 0 || p.x >= Constants.BSIZE || p.y >= Constants.BSIZE)
				return;
			if(board[p.y][p.x] == 0) {
				if (turnColor.equals("white")) {
					if (isPointExist(whitePoints, p))
						return;
			
				}
				else {
					if (isPointExist(bluePoints, p))
						return;
				}
			
			}
			int questionResult = 0;
			questionResult = new BoardQuestionDialog(blockb, questions[p.x][p.y]).getResult();
			
			
				
			
			if (turnColor.equals("white")) {
				
				whitePoints.add(p);
				for(int i= 0; i< whitePoints.size(); i++) {
					
					System.out.println("white Point "+whitePoints.get(i));
				}
				
				
				// questionResult = new BoardQuestionFrame(questions[p.x][p.y]).getResult();
				System.out.print("white turn: ");

				if (questionResult == 0) { // pressed pass
					if (isPointExist(bluePoints, p)) {
						board[p.y][p.x] = 3;
						counterTurns ++;
					}
					turnColor = "blue"; // no result 
					setBackground(Constants.COLOURTWO);
					return;}

				if (questionResult == 1) {
					board[p.y][p.x] = 1; // 1
					((TeamPanel) blockb.getMyTeamPanel()).addScore();
					System.out.println("correct answer of white.");
					counterTurns ++;
				} else {
					board[p.y][p.x] = 2;
					System.out.println("wrong answer of white.");
					counterTurns ++;
				}

				turnColor = "blue";
				setBackground(Constants.COLOURTWO);

			} else { // blue turn
				System.out.print("blue turn: ");
				
				bluePoints.add(p);
				
				for(int i= 0; i< bluePoints.size(); i++) {
					System.out.println("blue Point "+bluePoints.get(i));
				}
				
				if (questionResult == 0) { // pressed pass
					if (isPointExist(whitePoints, p)) {
						board[p.y][p.x] = 3;
						counterTurns ++;
					}
					turnColor = "white"; // no result
					setBackground(Constants.COLOURONE);

					return;}
				
				if (questionResult == 1) {
					board[p.y][p.x] = 2;
					((TeamPanel) blockb.getOpponentTeamPanel()).addScore();
					System.out.println("correct answer of blue.");
					counterTurns ++;
				} else {
					board[p.y][p.x] = 1;
					System.out.println("wrong answer of blue.");
					counterTurns ++;
				}

				turnColor = "white";
				setBackground(Constants.COLOURONE);

			}
			
			// display board to console for debug
			displayLogicBoard2Console();
			repaint();
			
			checkWinner();

			
		}
	}
	}
}