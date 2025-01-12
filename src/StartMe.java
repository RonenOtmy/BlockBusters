import java.awt.Font;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class StartMe extends JFrame implements ActionListener {

	private JButton btnJustFun, btnCreateCode, btnInsertCode;
	private JPanel pnlMain;
	private Font font;

	public StartMe(String s) {
		super(s);

		font = new Font("Arial", Font.PLAIN, 60);

		pnlMain = new JPanel(new GridLayout(3, 1));
		btnJustFun = new JButton("Play local");
		btnCreateCode = new JButton("Generate code to invite friends");
		btnInsertCode = new JButton("Insert code from my friend");

		btnJustFun.setFont(font);
		btnCreateCode.setFont(font);
		btnInsertCode.setFont(font);

		btnJustFun.addActionListener(this);
		btnCreateCode.addActionListener(this);
		btnInsertCode.addActionListener(this);

		pnlMain.add(btnCreateCode);
		pnlMain.add(btnJustFun);
		pnlMain.add(btnInsertCode);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(pnlMain);

		setResizable(false);
		pack();
		setLocationRelativeTo(null);

		setVisible(true);

	}

	public static void main(String args[]) {
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new BlockBusters("server");
			}
		});
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new BlockBusters("client");
			}
		});
/*
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new StartMe("BlockBusters!");
			}
		});
		*/
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnJustFun) {

			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					new BlockBusters("local");
				}
			});
			

		}

		if (e.getSource() == btnCreateCode) {
/*
			// create a code and run game as server
			String code =IP2Letters2IP.ipToLetters(IP2Letters2IP.getMyIP());
			System.out.println("code: " + code);
			JOptionPane.showMessageDialog(this, "send this code: "+code);
			*/
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					new BlockBusters("server");
				}
			});
			
			

		}

		if (e.getSource() == btnInsertCode) {
			
			// user input the code
			//String code=JOptionPane.showInputDialog(this, "Enter the code: ");
			
			// turn code into IP
			//String ip =IP2Letters2IP.lettersToIP(code);
			
			// run BlockBusters with IP (client mode)
			//new BlockBusters(ip);
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					new BlockBusters("client");
				}
			});
			
			

		}

	}

}
