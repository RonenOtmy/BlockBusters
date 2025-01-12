import java.io.Serializable;
import java.util.Arrays;

public class InitializeGameObject implements Serializable {

	private String myTeamName;
	private String opponentTeamName;

	private Question questions[][];

	public InitializeGameObject() {
		super();
	}

	public InitializeGameObject(String myTeamName, Question questions[][]) {

		this.myTeamName = myTeamName;
		this.questions = questions;
		this.opponentTeamName = "unknown";

	}

	public InitializeGameObject(String myTeamName, String opponentTeamName, Question[][] questions) {
		super();
		this.myTeamName = myTeamName;
		this.opponentTeamName = opponentTeamName;
		this.questions = questions;
	}

	public String getMyTeamName() {
		return myTeamName;
	}

	public void setMyTeamName(String myTeamName) {
		this.myTeamName = myTeamName;
	}

	public String getOpponentTeamName() {
		return opponentTeamName;
	}

	public void setOpponentTeamName(String opponentTeamName) {
		this.opponentTeamName = opponentTeamName;
	}

	public Question[][] getQuestions() {
		return questions;
	}

	public void setQuestions(Question[][] questions) {
		this.questions = questions;
	}

	@Override
	public String toString() {
		return "InitializeGameObject [whiteTeamName=" + myTeamName + ", BlueTeamName=" + opponentTeamName
				+ ", questions=" + Arrays.toString(questions) + "]";
	}

}
