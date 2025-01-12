import java.io.Serializable;
import java.util.Arrays;

public class Question implements Serializable {

	private String question;
	private String[] answers;
	private int correctAnswerIndex;

	public Question(String question) {
		super();
		this.correctAnswerIndex = 0;
		this.question = question;
		this.answers = new String[4];
	}

	public Question(String question, String[] answers) {
		super();
		this.correctAnswerIndex = (int) (Math.random() * 4);
		this.question = question;
		this.answers = changeAnswers(answers, correctAnswerIndex);

	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String[] getAnswers() {
		return answers;
	}

	public void setAnswerByIndex(String answer, int index) {
		this.answers[index] = answer;
	}

	public void setAnswers(String[] answers) {
		this.answers = answers;
	}

	public int getCorrectAnswerIndex() {
		return correctAnswerIndex;
	}

	public void setCorreectAnswerIndex(int correectAnswerIndex) {
		this.correctAnswerIndex = correectAnswerIndex;
	}

	@Override
	public String toString() {
		return "Question [question=" + question + ", answers=" + Arrays.toString(answers) + ", correectAnswerIndex="
				+ correctAnswerIndex + "]\n";
	}

	public boolean isCorrect(int userAnswer) {
		return userAnswer == correctAnswerIndex;
	}

	private String[] changeAnswers(String[] answers, int correctAnswerIndex) {
		String temp = null;
		temp = answers[0];
		answers[0] = answers[correctAnswerIndex];
		answers[correctAnswerIndex] = temp;
		return answers;

	}

	public void shuffleAnswers() {
		this.correctAnswerIndex = (int) (Math.random() * 4);
		this.answers = changeAnswers(answers, correctAnswerIndex);

	}

	public static void main(String[] args) {
		Question q1 = new Question("hello", new String[] { "a", "b", "c", "d" });
		System.out.println(q1);
	}
}
