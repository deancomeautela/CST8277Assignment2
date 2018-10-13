package client;
/* File: TunaClient.java
 * Author: Dean Comeau, Stanley Pieda, based on course example by Todd Kelley
 * Modified Date: October 2018
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
 * 
 * @author Dean Comeau
 */
public class TunaClient {
	LocalDateTime dateTime = LocalDateTime.now();
	DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM d yyyy hh:mm a");
	private Socket connection;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String serverName = "localhost";
	private int portNum = 8081;
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	/**
	 * @param args - Command line arguments. Allow for running the client on different ports or servernames.
	 */
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
	/**
	 * @param serverName server name to be used
	 * @param portNum port number to be used
	 */
	public TunaClient(String serverName, int portNum){
		this.serverName = serverName;
		this.portNum = portNum;
	}
	/**
	 * Method where client connects to the server. Prompts client to enter the 
	 * fields of a tuna object, and sends the object to the server to be added to the database.
	 * The client can also disconnect from the server when indicating that they do not want to enter
	 * another tuna object.
	 */
	public void runClient(){

		try {
			connection = new Socket(InetAddress.getByName(serverName), portNum);
			output = new ObjectOutputStream (connection.getOutputStream());
			input = new ObjectInputStream( connection.getInputStream());  
			System.out.printf("TunaClient by:  Dean Comeau run on %s%n",dateTime.format(format));
			do { 
				UUID uuid = UUID.randomUUID();
				Tuna tuna = new Tuna();
				System.out.println("Enter Data For new Tuna:");
				String prompt = "Please enter record number: ";
				while(true) {
					System.out.print(prompt);

					try {
						tuna.setRecordNumber(Integer.parseInt(br.readLine()));
						break;
					}

					catch(NumberFormatException e){
						prompt = "Error, not an integer, please enter a record number: ";
					}
				}

				System.out.print("Please enter omega: ");
				tuna.setOmega(br.readLine());

				System.out.print("Please enter delta: ");
				tuna.setDelta(br.readLine());

				System.out.print("Please enter theta: ");
				tuna.setTheta(br.readLine());

				tuna.setUUID(uuid.toString());

				Message msg = new Message("add", tuna);



				//write to the server, then flush
				output.writeObject(msg);
				output.flush();
				//receive input from server
				msg = (Message) input.readObject();
				//print input from server
				if (msg.getTuna()==null) {
					System.out.println("Server failed to perform requested operation");
					break;
				}
				else {
					System.out.println("Command: " + msg.getCommand() + " Returned Tuna: "+msg.getTuna());
				}

				System.out.println("Do you want to insert another tuna? (y/n)");

			} while (!br.readLine().equalsIgnoreCase("n"));
			output.writeObject(new Message("disconnect"));
			System.out.println("Shutting down connection to server");
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
