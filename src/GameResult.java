import java.io.Serializable;

public class GameResult implements Serializable{
	
	private String winTeamName,loseTeamName;

	public GameResult(String winTeamName, String loseTeamName) {
		this.winTeamName = winTeamName;
		this.loseTeamName = loseTeamName;
	}

	public String getWinTeamName() {
		return winTeamName;
	}

	public String getLoseTeamName() {
		return loseTeamName;
	}

	public void setWinTeamName(String winTeamName) {
		this.winTeamName = winTeamName;
	}

	public void setLoseTeamName(String loseTeamName) {
		this.loseTeamName = loseTeamName;
	}

	@Override
	public String toString() {
		return "GameResult [winTeamName=" + winTeamName + ", loseTeamName=" + loseTeamName + "]";
	}
	
	

}