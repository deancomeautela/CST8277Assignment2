package client;
/* File: TunaClient.java
 * Author: Stanley Pieda, based on course example by Todd Kelley
 * Modified Date: August 2018
 * Description: Networking client that uses simple protocol to send and receive transfer objects.
 */
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.UUID;

import datatransfer.Tuna;
import datatransfer.Message;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Need programming comments with correct author name throughout this class
 * @author xyz abc
 */
public class TunaClient {

	private Socket connection;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String message = "";
	private String serverName = "localhost";
	private int portNum = 8081;
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	public static void main(String[] args) {
		switch (args.length){
		case 2:
			(new TunaClient(args[1],Integer.parseInt(args[2]))).runClient();
			break;
		case 1:
			(new TunaClient("localhost",Integer.parseInt(args[1]))).runClient();
			break;
		default:
			(new TunaClient("localhost",8081)).runClient();
		}

	}
	public TunaClient(String serverName, int portNum){
		this.serverName = serverName;
		this.portNum = portNum;
	}
	public void runClient(){
		String myHostName = null;
		try {
			InetAddress myHost = Inet4Address.getLocalHost();
			myHostName = myHost.getHostName();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		try {
			connection = new Socket(InetAddress.getByName(serverName), portNum);
			output = new ObjectOutputStream (connection.getOutputStream());
			input = new ObjectInputStream( connection.getInputStream());               
			do { //Modify loop to prompt user for command, optional disconnect command... while(message != "exit")
				System.out.print("Input> ");
				message = br.readLine();
				if (message == null || message.isEmpty()) {
					message = null; // do not append host name, send null to server to start disconnect.
				}
				else {
					message = myHostName + ": " + message;
				}
				//write to the server, then flush
				output.writeObject(message);
				output.flush();
				//receive input from server
				message = (String) input.readObject();
				//print input from server
				System.out.println(message);
			} while (message != null);
			//catch exceptions
		} catch (IOException exception) {
			System.out.println(exception.getMessage());
			exception.printStackTrace();
		}catch (ClassNotFoundException exception) {
			System.out.println(exception.getMessage());
			exception.printStackTrace();
		} 
		finally{//close client side connection
			try{if(input != null){input.close();}}catch(IOException ex){
				System.out.println(ex.getMessage());}
			try{if(output != null){output.flush(); output.close();}}catch(IOException ex){
				System.out.println(ex.getMessage());}
			try{if(connection != null){connection.close();}}catch(IOException ex){
				System.out.println(ex.getMessage());}
		}
	}
}
