import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class TeamPanel extends JPanel {

	private String name;
	private String color;
	private int clientId;
	private int score;
	private String teamColorTurn;

	private JLabel lblTeamName, lblScore;

	public TeamPanel(String name, String color) {
		this.name = name;
		this.color = color;
		this.score = 0;
		this.teamColorTurn = "white";

		this.setLayout(new BorderLayout());

		Font font = new Font("arial", Font.BOLD, 25);

		this.lblTeamName = new JLabel(name + " - ", JLabel.CENTER);
		this.lblScore = new JLabel("0", JLabel.CENTER);

		this.add(lblTeamName, BorderLayout.NORTH);
		lblTeamName.setFont(font);
		this.add(lblScore, BorderLayout.CENTER);
		lblScore.setFont(font);

	}

	public TeamPanel(String color) {
		this.name = "unknown";
		this.color = color;
		this.score = 0;
		this.teamColorTurn = "white";

		this.setLayout(new BorderLayout());

		Font font = new Font("arial", Font.BOLD, 25);

		this.lblTeamName = new JLabel(name + " - ", JLabel.CENTER);
		this.lblScore = new JLabel("0", JLabel.CENTER);

		this.add(lblTeamName, BorderLayout.NORTH);
		lblTeamName.setFont(font);
		this.add(lblScore, BorderLayout.CENTER);
		lblScore.setFont(font);

	}

	public void setTeamColorByID(int id) {

		if (id == 1) {
			setBackground(Color.white);
			this.add(new JLabel(new ImageIcon("icon data//left-right-arrow.png")), BorderLayout.SOUTH);

		} else {
			setBackground(Color.blue);
			this.add(new JLabel(new ImageIcon("icon data//up-arrow.png")), BorderLayout.SOUTH);

		}

		validate();

	}

	public JLabel getLblTeamName() {
		return lblTeamName;
	}

	public JLabel getLblScore() {
		return lblScore;
	}

	public void setLblTeamName(JLabel lblTeamName) {
		this.lblTeamName = lblTeamName;
	}

	public void addScore() {
		this.score++;
		this.lblScore.setText("" + score);
	}

	public void resetScore() {
		this.lblScore.setText("0");
	}

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

}