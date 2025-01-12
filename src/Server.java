import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.util.Random;

// שרת פשוט עם תמיכה בהזדהות
class Server {

	public static Question[][] chooseFirstGame() {

		// read question from excel
		return ReadDataFromExcel.Read2DQuestionArrayFromExcel("questions data//blockbustersgame.xls", 0);

	}

	public static Question[][] chooseRandomGame() {

		String[] games = null;
		try {
			games = ReadDataFromExcel.getSheetNames("questions data//blockbustersgame.xls");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JComboBox comboBox = new JComboBox(games);
		int selection = JOptionPane.showOptionDialog(null, comboBox, "Choose game:", JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, null, null);

		// read question from excel
		Question questions[][] = ReadDataFromExcel.Read2DQuestionArrayFromExcel("questions data//blockbustersgame.xls",
				selection);

		return questions;
	}

	public static void main(String[] args) throws IOException {

		InitializeGameObject initializeGameObject = null;
		Question questions[][];

		ServerSocket serverSocket = new ServerSocket(5000);
		System.out.println("השרת מאזין");

		// קבלת שני לקוחות
		Socket client1 = serverSocket.accept();
		Socket client2 = serverSocket.accept();

		// העברת נתוני חיבור
		ObjectOutputStream out1 = new ObjectOutputStream(client1.getOutputStream());
		ObjectOutputStream out2 = new ObjectOutputStream(client2.getOutputStream());
		ObjectInputStream in1 = new ObjectInputStream(client1.getInputStream());
		ObjectInputStream in2 = new ObjectInputStream(client2.getInputStream());

		// שליחת מספרי לקוח
		out1.writeInt(1);
		out1.flush();
		out2.writeInt(2);
		out2.flush();

		// קבלת שמות שחקנים
		String player1Name = null;
		String player2Name = null;
		try {
			player1Name = (String) in1.readObject();
			player2Name = (String) in2.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("שחקן 1: " + player1Name);
		System.out.println("שחקן 2: " + player2Name);
		/*
		 * // החלפת שמות בין הלקוחות out1.writeObject(player2Name); out1.flush();
		 * out2.writeObject(player1Name); out2.flush();
		 */
		// object with teams names and game questions/answers
		initializeGameObject = new InitializeGameObject(player1Name, player2Name, chooseFirstGame());

		// החלפת שמות בין הלקוחות
		out1.writeObject(initializeGameObject);
		out1.flush();
		out2.writeObject(initializeGameObject);
		out2.flush();
		
		new Thread(() -> handleClient(client1, in1, out1, out2)).start(); // Pass out2 here
	    new Thread(() -> handleClient(client2, in2, out2, out1)).start();
	}// Pass out1 here
		
		// לולאת העברת אובייקטים
	    private static void handleClient(Socket client, ObjectInputStream in, ObjectOutputStream out, ObjectOutputStream outOtherClient) {
	        String clientName = "Client"; // Default name
	        

	        try {
	            while (true) {
	                try {
	                    Object result = in.readObject();

	                    if (result instanceof Point) {
	                        Point point = (Point) result;
	                        System.out.println(clientName + ": Received Point: " + point);
	                        if (outOtherClient != null) {
	                            outOtherClient.writeObject(point);
	                            outOtherClient.flush();
	                        }
	                    } else if (result instanceof MoveObject) {
	                        MoveObject moveObject = (MoveObject) result;
	                        System.out.println(clientName + ": Received MoveObject: " + moveObject);
	                        if (outOtherClient != null) {
	                            outOtherClient.writeObject(moveObject);
	                            outOtherClient.flush();
	                        }
	                    } else if (result instanceof GameResult) {
	                        GameResult gameResult = (GameResult) result;
	                        System.out.println(clientName + ": Received GameResult: " + gameResult);
	                        SQLQuery.insertGameResult(gameResult);
	                        String win = gameResult.getWinTeamName();
	                        String lose = gameResult.getLoseTeamName();
	                        String[][] results = SQLQuery.readGameResultData2DString(win, lose);
	                        out.writeObject(results); // Send result back to the same client
	                        out.flush();
	                        if (outOtherClient != null) {
	                            outOtherClient.writeObject(results);
	                            outOtherClient.flush();
	                        }
	                    } else if (result == null) {
	                        System.out.println(clientName + " disconnected");
	                        break;
	                    } else {
	                        System.out.println(clientName + ": Received unknown object: " + (result != null ? result.getClass().getName() : "null"));
	                    }
	                } catch (ClassNotFoundException e) {
	                    System.err.println(clientName + ": ClassNotFoundException: " + e.getMessage());
	                } catch (SocketException e) {
	                    System.out.println(clientName + " disconnected");
	                    break;
	                } catch (IOException e) {
	                    System.err.println(clientName + ": IOException: " + e.getMessage());
	                    break;
	                }
	            }
	        } finally {
	            // Close resources
	            try { if (in != null) in.close(); if (out != null) out.close(); if (client != null) client.close(); } catch (IOException e) { System.err.println("Error closing resources: " + e.getMessage()); }
	        }
	        System.out.println(clientName + " handler finished.");
	    }
	}