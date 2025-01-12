import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;

public class BlockBusters extends JFrame implements Runnable {


	private int[][] board = new int[Constants.BSIZE][Constants.BSIZE];
	private Question questions[][];

	private JPanel pnlBoardGame;
	private JPanel pnlTitle, pnlTeamsNamesAndScore, pnlMyTeam, pnlOpponentTeam, pnlMiddle;
	private String mode = "";
	
	private Client client;
	

	public BlockBusters(String mode) {
		super(mode);
		this.mode = mode; // client/server/none
	}
	@Override
	public void run() {
		Hexagon.setXYasVertex(false); // RECOMMENDED: leave this as FALSE.

		Hexagon.setHeight(Constants.HEXSIZE); // Either setHeight or setSize must be run to initialize the hex
		Hexagon.setBorders(Constants.BORDERS);


		setDefaultLookAndFeelDecorated(true);
		pnlMiddle = new JPanel(new BorderLayout());
		pnlMyTeam = new TeamPanel("white" );
		pnlOpponentTeam = new TeamPanel("blue");

		pnlMiddle.add(pnlMyTeam, BorderLayout.EAST);
		pnlMiddle.add(pnlOpponentTeam, BorderLayout.WEST);

		//pnlBoardGame = new BoardGamePanel(this, board, questions);
				//pnlMiddle.add(pnlBoardGame, BorderLayout.CENTER);

				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				setContentPane(pnlMiddle);
				// for hexes in the FLAT orientation, the height of a 10x10 grid is 1.1764 * the
				// width. (from h / (s+t))
				setSize((int) (Constants.SCRSIZE / 1.1), Constants.SCRSIZE);
				// setExtendedState(JFrame.MAXIMIZED_BOTH);
				// setResizable(false);
				
				
		
		// read question from excel
		if(mode.equals("Local"))
			newGameSetupLocal();	
		else
			newGameSetupClientServer();
		
		
		setLocationRelativeTo(null);
		setVisible(true);
	
	}

	public void chooseGame() {
		
		String[] games = null;
		try {
			games = ReadDataFromExcel.getSheetNames("questions data//blockbustersgame.xls");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 JComboBox comboBox = new JComboBox(games); 
		 int selection= JOptionPane.showOptionDialog(null, comboBox, "Choose game:",
				 JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, null,null);
		
		// read question from excel
		questions = ReadDataFromExcel.Read2DQuestionArrayFromExcel("questions data//blockbustersgame.xls", selection);
	}
	
	public void newGameSetupLocal() {
		for (int i = 0; i < Constants.BSIZE; i++) {
			for (int j = 0; j < Constants.BSIZE; j++) {
				board[i][j] = Constants.EMPTY;
			}
		}

		chooseGame(); // for questions

		pnlBoardGame = new BoardGamePanel(this, board, questions);
		pnlMiddle.add(pnlBoardGame, BorderLayout.CENTER);
		
		((TeamPanel)pnlMyTeam).resetScore();
		((TeamPanel)pnlOpponentTeam).resetScore();

	}
	
	public void newGameSetupClientServer() {
		for (int i = 0; i < Constants.BSIZE; i++) {
			for (int j = 0; j < Constants.BSIZE; j++) {
				board[i][j] = Constants.EMPTY;
			}
		}

		//chooseGame(); // for questions
		
		// choose team name
		//((TeamPanel)pnlWhiteTeam).getLblTeamName().setText(JOptionPane.showInputDialog("Enter team name: "));
	
		pnlBoardGame = new BoardGameClientPanel(this, board, questions);
		pnlMiddle.add(pnlBoardGame, BorderLayout.CENTER);
		
		((TeamPanel)pnlMyTeam).resetScore();
		((TeamPanel)pnlOpponentTeam).resetScore();
		
		

	}
	
	public TeamPanel getMyTeamPanel() {
		return (TeamPanel)pnlMyTeam;
	}
	
	public TeamPanel getOpponentTeamPanel() {
		return (TeamPanel)pnlOpponentTeam;
	}
	
	public String getMyTeamName() {
		return ((TeamPanel)pnlMyTeam).getLblTeamName().getText();
	}
	
	public String getOpponentTeamName() {
		return ((TeamPanel)pnlOpponentTeam).getLblTeamName().getText();
	}
	
	public void setMyTeamName(String name) {
		 ((TeamPanel)pnlMyTeam).getLblTeamName().setText(name);
	}
	public void setOpponentTeamName(String name) {
		 ((TeamPanel)pnlOpponentTeam).getLblTeamName().setText(name);
	}
	
	public String getMyTeamScore() {
		return ((TeamPanel)pnlMyTeam).getLblScore().getText();
	}
	
	public String getOpponentTeamScore() {
		return ((TeamPanel)pnlOpponentTeam).getLblScore().getText();
	}
	public void addMyTeamScore() {
		String scoretxt=((TeamPanel)pnlMyTeam).getLblScore().getText();
		int scoreValue=Integer.parseInt(scoretxt)+1;
		 ((TeamPanel)pnlMyTeam).getLblScore().setText(scoreValue+"");
	}
	
	public void addOpponentTeamScore() {
		String scoretxt=((TeamPanel)pnlOpponentTeam).getLblScore().getText();
		int scoreValue=Integer.parseInt(scoretxt)+1;
		 ((TeamPanel)pnlOpponentTeam).getLblScore().setText(scoreValue+"");
	}
	
		
	
	

	

	public static void main(String[] args) {
/*
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new BlockBusters("Local");
			}
		});
		*/
		new Thread(new BlockBusters("ClientServer")).start();
	}

}