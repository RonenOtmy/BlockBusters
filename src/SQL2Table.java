import javax.swing.JFrame;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class SQL2Table extends JFrame {

	private String[][] strArr;

	public SQL2Table(String[][] arr) {
		super("Last Games");
		this.strArr = arr;

		// Table
		JTable j;

		// Column Names
		String[] columnNames = { "Win Game", "Lose Game" };

		// Initializing the JTable
		j = new JTable(strArr, columnNames);
		j.setBounds(30, 40, 200, 300);

		// adding it to JScrollPane
		JScrollPane sp = new JScrollPane(j);
		this.add(sp);
		// Frame Size
		this.setSize(500, 200);
		// Frame Visible = true
		this.setVisible(true);

	}
}