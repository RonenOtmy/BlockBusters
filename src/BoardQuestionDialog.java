import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class BoardQuestionDialog extends JDialog implements ActionListener {

	private JPanel pnlMain, pnlQuest, pnlAns, pnlPass;
	private JButton btnAnswers[], pass;
	private JLabel lblQuestTitle;
	private Question question;
	private int result;

	public BoardQuestionDialog(JFrame parent,  Question q) {
		 super(parent, "?", true);
		this.question = q;

		pnlMain = new JPanel();

		pnlQuest = new JPanel();
		pnlAns = new JPanel();
		pnlPass = new JPanel(new BorderLayout());

		btnAnswers = new JButton[4];

		for (int i = 0; i < btnAnswers.length; i++) {
			btnAnswers[i] = new JButton(q.getAnswers()[i]);
			btnAnswers[i].addActionListener(this);
			
		}

		pass = new JButton("Pass");
		pass.addActionListener(this);

		pnlPass.add(pass, BorderLayout.WEST);

		lblQuestTitle = new JLabel(q.getQuestion());
		pnlQuest.add(lblQuestTitle);
		setContentPane(pnlMain);

		pnlMain.setLayout(new BorderLayout());

		pnlMain.add(pnlQuest, BorderLayout.NORTH);
		pnlMain.add(pnlAns, BorderLayout.CENTER);
		pnlMain.add(pnlPass, BorderLayout.SOUTH);

		pnlAns.setLayout(new GridLayout(4, 1));

		for (int i = 0; i < btnAnswers.length; i++) 
			pnlAns.add(btnAnswers[i]);
			
		

		setLocation(400, 200);
		setSize(800, 400);

		setVisible(true);
	}

	
	
	public int getResult() {
		System.out.println("get result active");
		return result;
	}



	public void setResult(int result) {
		this.result = result;
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == pass) {
			System.out.println("pass question");
			result=0; // pass

		}

		else {
			for (int i = 0; i < btnAnswers.length; i++)
				if (e.getSource() == btnAnswers[i])
					if (question.getCorrectAnswerIndex() == i) {
						btnAnswers[i].setBackground(Color.green);
						result=1; // correct
						System.out.println("correct answer");
					}
					else {
						btnAnswers[i].setBackground(Color.red);
						result=2; // wrong
						System.out.println("wrong answer");
						
					}
		}
		
		Timer timer = new Timer(700, ev -> {
		    dispose();
		});
		timer.setRepeats(false);
		timer.start();
	}

	public static void main(String[] args) {
		String answers[] = { "good", "very good", "poor", "ok" };
		Question q = new Question("How are you?", answers);

		BoardQuestionDialog win = new BoardQuestionDialog(null, q);
	}

}
