import java.io.Serializable;
import java.util.Vector;

public class MoveObject implements Serializable {

	private int i, j; // cell location on board
	private int cellValueForUpdate;
	
	private int clientId;
    private String playerName;

	public MoveObject(int i, int j, int clientId, String playerName ) {

		this.i = i;
		this.j = j;
		this.clientId=clientId;
		this.playerName=playerName;
		this.cellValueForUpdate = -1;
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public int getJ() {
		return j;
	}

	public void setJ(int j) {
		this.j = j;
	}

	

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public int getCellValue() {
		return cellValueForUpdate;
	}

	public void setCellValue(int cellValue) {
		this.cellValueForUpdate = cellValue;
	}

	@Override
	public String toString() {
		return "MoveObject [i=" + i + ", j=" + j + ", resultQuestion=" + cellValueForUpdate + ", clientId=" + clientId
				+ ", playerName=" + playerName + "]";
	}

	

}
