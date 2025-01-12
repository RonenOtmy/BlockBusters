import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;

import javax.swing.JOptionPane;

class Client {

	// IO streams
	private static ObjectOutputStream toServer;
	private static ObjectInputStream fromServer;

	private Socket socket;
	
	

	public Client() {

		try {
			// Create a socket to connect to the server
			socket = new Socket("localhost", 8000);

			// Create an output stream to send data
			// to the server
			toServer = new ObjectOutputStream(socket.getOutputStream());

			// Create an input stream to receive data
			// from the server
			fromServer = new ObjectInputStream(socket.getInputStream());

		} catch (ConnectException e) {
			JOptionPane.showMessageDialog(null, "unable to connect to server");
		} catch (IOException e) {
			// e.printStackTrace();
		}

	}

	public void writeMoveObjectToServer(MoveObject mo) {
		try {
			toServer.writeObject(mo);
			// toServer.flush();
			toServer.reset();
		} catch (NullPointerException e) {
			JOptionPane.showMessageDialog(null, "unable to send MoveObject to server");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeInitializeGameObjectToServer(InitializeGameObject mo) {
		try {
			toServer.writeObject(mo);
			// toServer.flush();
			toServer.reset();
		} catch (NullPointerException e) {
			JOptionPane.showMessageDialog(null, "unable to InitializeGameObject data to server");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public MoveObject readMoveObjectFromServer() {
		try {

			return (MoveObject) fromServer.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public InitializeGameObject readInitializeGameObjectFromServer() {
		try {

			return (InitializeGameObject) fromServer.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
