import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

class BoardGameClientPanel extends JPanel {

	private int[][] board;
	private Question[][] questions;
	private String turnColor;
	private BlockBusters blockb; // for Question JDialog

	private ObjectOutputStream out;
	private ObjectInputStream in;

	private InitializeGameObject initializeGameObject;
	private MyMouseListener clickListener;

	private int clientId;
	private boolean isMyTurn = true;
	private String myName;
	private String opponentName;

	private MoveObject moveObject;

	ArrayList<Point> opponentPoints = new ArrayList<>();
	ArrayList<Point> myPoints = new ArrayList<>();

	public static boolean containsNoZero(int[][] array) {
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				if (array[i][j] == 0) {
					return false; // מצאנו אפס, נחזיר שקר
				}
			}
		}
		return true; // לא מצאנו אפס, נחזיר אמת
	}

	private Point p;
	int questionResult;

	public BoardGameClientPanel(BlockBusters blockb, int[][] board, Question[][] questions) {

		myName = JOptionPane.showInputDialog(null, "הכנס את שמך:", "הזדהות", JOptionPane.QUESTION_MESSAGE);
		blockb.setTitle(myName);

		setBackground(Constants.COLOURTWO);
		this.blockb = blockb;
		this.board = board;
		this.questions = questions;
		// turnColor = "white";

		setLayout(new BorderLayout());

		clickListener = new MyMouseListener();
		addMouseListener(clickListener);

		// connecting to server: include send team name and getting InitializeGameObject
		connectToServer();

		// sendButton.addActionListener(e -> sendCircle());

	}

	public boolean isPointExist(ArrayList<Point> arr, Point p) {
		for (int i = 0; i < arr.size(); i++) {
			if (arr.get(i).getX() == p.getX() && arr.get(i).getY() == p.getY())
				return true;
		}
		return false;
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

	private void connectToServer() {
		try {
			Socket socket = new Socket("localhost", 5000);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());

			// קבלת מספר לקוח
			clientId = in.readInt();

			// שליחת השם לשרת
			out.writeObject(myName);
			out.flush();

			initializeGameObject = (InitializeGameObject) in.readObject();
			questions = initializeGameObject.getQuestions();

			if (clientId == 1) {
				myName = initializeGameObject.getMyTeamName();
				opponentName = initializeGameObject.getOpponentTeamName();
				blockb.getMyTeamPanel().setTeamColorByID(1);
				blockb.getOpponentTeamPanel().setTeamColorByID(2);

			} else {
				myName = initializeGameObject.getOpponentTeamName();
				opponentName = initializeGameObject.getMyTeamName();
				blockb.getMyTeamPanel().setTeamColorByID(2);
				blockb.getOpponentTeamPanel().setTeamColorByID(1);

			}

			blockb.setMyTeamName(myName);
			blockb.setOpponentTeamName(opponentName);

			// ���� �� �����

			// ����� �����
			System.out.println("לקוח " + clientId + ": " + myName);
			System.out.println("שחקן יריב: " + opponentName + "\n");

			// ��� ����� ������
			new Thread(() -> {
				try {
					while (true) {
						Object obj = in.readObject();
						if (obj instanceof Point) {
							Point prePoint = (Point) obj;
							System.out.println("Pre Point is: " + prePoint);
							continue;
						} else if (obj instanceof MoveObject) {
							System.out.println("enter if -->>");
							MoveObject mo = (MoveObject) obj;

							Point p1 = new Point(mo.getI(), mo.getJ());
							opponentPoints.add(p1);

							SwingUtilities.invokeLater(() -> {
								// messageArea.append(circle.toString() + "\n");
								System.out.println("got MoveObject: " + mo);

								// update result on board
								int result = mo.getCellValue();

								if (result == 0 && isPointExist(myPoints, p1))
									board[mo.getJ()][mo.getI()] = 3; // 2 pass answers
								else if (result == clientId) { // correct answer for id 1 or wrong answer for id 2
									board[mo.getJ()][mo.getI()] = 2;
									if (result == 1)
										blockb.addOpponentTeamScore();
								} // correct answer of opponent
								else if (result != clientId && result != 0) { // wrong answer for id 1 or correct answer
																				// for id 2
									board[mo.getJ()][mo.getI()] = 1;
									if (result == 1)
										blockb.addOpponentTeamScore();
								} // correct answer of opponent

								repaint();

								// display massage of winner
								checkWinner();
								updateColor();
								isMyTurn = true;
								updateTurnState();
							});

						} // and of if object
						else if (obj instanceof String[][]) {

							String[][] arr = (String[][]) obj;
							new SQL2Table(arr);

						}
					}
					// end of while
				} catch (SocketException e) {
					System.out.println("client diconnected.");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}).start();

			// ������ ����� ������ ����
			isMyTurn = (clientId == 2);
			updateTurnState();
		} catch (ConnectException e) {
			System.out.println("server not connected");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateTurnState() {
		SwingUtilities.invokeLater(() -> {
			if (isMyTurn)
				addMouseListener(clickListener);
			else
				removeMouseListener(clickListener);

		});
	}

	private void updateColor() {
		if (getBackground().equals(Constants.COLOURONE))
			setBackground(Constants.COLOURTWO);
		else
			setBackground(Constants.COLOURONE);
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

	// display massage of winner
	public void checkWinner() {
		// check if we have winner:
		// 1 white team (left - right)
		// 2 blue team (buttom - up)
		int algorithmResult = Algorithm.hasSequence(board);
		/*
		 * if (algorithmResult == 0) System.out.println("no winner till now"); if
		 * (algorithmResult == 1 && clientId == 1) {
		 * System.out.println(blockb.getMyTeamName() + " is the winner");
		 * JOptionPane.showMessageDialog(blockb, blockb.getMyTeamName() + " win"); } if
		 * (algorithmResult == 2 && clientId == 1) {
		 * System.out.println(blockb.getOpponentTeamName() +" is the winner");
		 * JOptionPane.showMessageDialog(blockb, blockb.getOpponentTeamName() + " win");
		 * } if (algorithmResult == 1 && clientId == 2) {
		 * System.out.println(blockb.getOpponentTeamName() +" is the winner");
		 * JOptionPane.showMessageDialog(blockb, blockb.getOpponentTeamName() + " win");
		 * } if (algorithmResult == 2 && clientId == 2) {
		 * System.out.println(blockb.getMyTeamName() +" is the winner");
		 * JOptionPane.showMessageDialog(blockb, blockb.getMyTeamName() + " win"); }
		 */
		if (algorithmResult == 0)
			System.out.println("no winner till now");
		else if (algorithmResult == clientId) {
			System.out.println(blockb.getMyTeamName() + " is the winner");
			JOptionPane.showMessageDialog(blockb, blockb.getMyTeamName() + " win");
			sendGameResult();
		}

		else if (algorithmResult != clientId && algorithmResult != 0) {
			System.out.println(blockb.getOpponentTeamName() + " is the winner");
			JOptionPane.showMessageDialog(blockb, blockb.getOpponentTeamName() + " win");

		}

		if (containsNoZero(board)) {
			int opponentScore = Integer.parseInt(blockb.getOpponentTeamScore());
			int myScore = Integer.parseInt(blockb.getMyTeamScore());

			if (opponentScore > myScore) {
				System.out.println(blockb.getOpponentTeamName() + " win");
				JOptionPane.showMessageDialog(blockb, blockb.getOpponentTeamName() + " win");
			} else if (opponentScore < myScore) {
				System.out.println(blockb.getMyTeamName() + " win");
				JOptionPane.showMessageDialog(blockb, blockb.getMyTeamName() + " win");
				sendGameResult();
			} else {
				System.out.println("No winner Today!");
				JOptionPane.showMessageDialog(blockb, "No winner Today!");
			}
		} // end if no zero
	}

	public void sendGameResult() {
		String win = blockb.getMyTeamName();
		String lose = blockb.getOpponentTeamName();

		GameResult gr = new GameResult(win, lose);

		try {
			out.writeObject(gr);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void sendMoveObject() {
		if (!isMyTurn)
			return;

		try {

			//
			moveObject = new MoveObject(p.x, p.y, clientId, myName);

			moveObject.setCellValue(questionResult); // update cell result (0-pass, 1-correct, 2- wrong)

			// שליחה לשרת
			out.writeObject(moveObject);
			out.flush();

			// עדכון ממשק
			System.out.println("send" + moveObject + "\n");

			// עדכון תור
			isMyTurn = false;
			updateTurnState();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class MyMouseListener extends MouseAdapter {// inner class

		public void mouseClicked(MouseEvent e) {

			if (!isMyTurn)
				return;

			int x = e.getX();
			int y = e.getY();

			p = new Point(Hexagon.pxtoHex(x, y));

			if (board[p.y][p.x] != 0 || isPointExist(myPoints, p)) // use by myself or opponent
				return;

			if (p.x < 0 || p.y < 0 || p.x >= Constants.BSIZE || p.y >= Constants.BSIZE)
				return;

//			if (isPointExist(myPoints, p))// || isPointExist(opponentPoints, p))
//				return;

			myPoints.add(p);

			try {
				out.writeObject(p);
				System.out.println("Sent Point");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			questionResult = new BoardQuestionDialog(blockb, questions[p.x][p.y]).getResult();
			/*
			 * if (clientId == 1) { System.out.println("clientId 1 is playing." + myName);
			 * if (questionResult == 0) { // pressed pass if (isPointExist(opponentPoints,
			 * p)) { board[p.y][p.x] = 3; // both tried to answer
			 * 
			 * } } else if (questionResult == 1) { board[p.y][p.x] = 1;
			 * blockb.addMyTeamScore(); System.out.println("correct answer of white.");
			 * 
			 * } else { board[p.y][p.x] = 2; System.out.println("wrong answer of white.");
			 * 
			 * } } if (clientId == 2) { System.out.println("clientId 2 is playing." +
			 * opponentName); if (questionResult == 0) { // pressed pass if
			 * (isPointExist(opponentPoints, p)) { board[p.y][p.x] = 3;
			 * 
			 * } } else if (questionResult == 1) { board[p.y][p.x] = 2;
			 * blockb.addMyTeamScore(); System.out.println("correct answer of white.");
			 * 
			 * } else { board[p.y][p.x] = 1; System.out.println("wrong answer of white.");
			 * 
			 * } }
			 */
			if (clientId == 1)
				System.out.println("clientId 1 is playing." + myName);
			else
				System.out.println("clientId 2 is playing." + opponentName);

			if (questionResult == 0) { // pressed pass
				if (isPointExist(opponentPoints, p))
					board[p.y][p.x] = 3; // both tried to answer
			}

			if (clientId == questionResult) {
				board[p.y][p.x] = 1;
				if (questionResult == 1) {
					blockb.addMyTeamScore();
					System.out.println("correct answer of white.");
				} else
					System.out.println("wrong answer of Blue.");
			} else if (clientId != questionResult && questionResult != 0) {
				board[p.y][p.x] = 2;
				if (questionResult == 1) {
					blockb.addMyTeamScore();
					System.out.println("correct answer of blue.");
				} else
					System.out.println("wrong answer of white.");
			}

			blockb.validate();

			sendMoveObject();
			
			
			displayLogicBoard2Console();
			repaint();

			checkWinner();
			updateColor();
			isMyTurn = false;
			updateTurnState();

			// display board to console for debug

		} // end of mouse clicked function
	} // end of myMouseListener inner class
}
