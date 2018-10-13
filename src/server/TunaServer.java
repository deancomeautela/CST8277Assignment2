package server;
/* File: TunaServer.java
 * Author: Dean Comeau, Stanley Pieda, based on course materials by Todd Kelley
 * Modified Date: October 2018
 * Description: Simple echo client.
 */
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import datatransfer.Message;

import dataaccesslayer.TunaDaoImpl;

/**
 * Need programming comments with correct author name throughout this class
 * @author Dean Comeau
 */
public class TunaServer {


	private ServerSocket server;
	private Socket connection;
	private int portNum = 8081;
	private TunaDaoImpl tunaDAO = new TunaDaoImpl();
	public static ExecutorService threadExecutor = Executors.newCachedThreadPool();

	/**
	 * @param args first argument indicates port to be run on.
	 */
	public static void main(String[] args) {
		if(args.length > 0){
			(new TunaServer(Integer.parseInt(args[0]))).runServer();
		}else{
			(new TunaServer(8081)).runServer();
		}
	}
	/**
	 * @param portNum constructor using port number to be used by server.
	 */
	public TunaServer(int portNum){
		this.portNum = portNum;
	}
	/**
	 * @param connection variable used to connect to the client.
	 * code to insert tuna object received from user into database and return to user.
	 */
	public void talkToClient(final Socket connection){
		//begin thread
		threadExecutor.execute( new Runnable () {
			public void run(){	
				ObjectOutputStream output = null;
				ObjectInputStream input = null;
				Message msg = new Message("DefaultMSG");
				System.out.println("Got a connection");
				try {
					SocketAddress remoteAddress = connection.getRemoteSocketAddress();
					String remote = remoteAddress.toString();
					//set up input and output streams with client
					output = new ObjectOutputStream (connection.getOutputStream());
					input = new ObjectInputStream( connection.getInputStream());               
					do {
						msg = (Message) input.readObject();
						if(msg.getTuna()==null)
							System.out.println("From:" + remote + ">"+"Command: " + msg.getCommand() + " Tuna: null");
						else {
							System.out.println("From:" + remote + ">"+"Command: " + msg.getCommand() + " Tuna: "+msg.getTuna().toString());
						}
						//						System.out.println("From:" + remote + ">"+messageString);
						//						indicates the client wants to disconnect.. has to be changed in coordination with client. maybe "exit" string. 
						if(msg.getCommand().equalsIgnoreCase("disconnect")) {
							msg = null;
						}
						else { 
							try {
							tunaDAO.insertTuna(msg.getTuna());
							
							msg = new Message("insert_success" , tunaDAO.getTunaByUUID(msg.getTuna().getUUID()));
							}catch(Exception e) {
								output.writeObject(new Message("disconnect"));
								msg = null;
								
								
							}
						}

						output.writeObject(msg);//This is what gets sent back to client
						output.flush();
					} while (msg != null);
					System.out.println(remote + " disconnected via request");
				} catch (IOException exception) {
					System.err.println(exception.getMessage());
					exception.printStackTrace();
				
				} catch (ClassNotFoundException e) {
					System.out.println("Invalid object returned from client.");


				} 
				finally {
					try{if(input != null){input.close();}}catch(IOException ex){
						System.out.println(ex.getMessage());}
					try{if(output != null){output.flush(); output.close();}}catch(IOException ex){
						System.out.println(ex.getMessage());}
					try{if(connection != null){connection.close();}}catch(IOException ex){
						System.out.println(ex.getMessage());}
				}
			}
		});
	}
	/**
	 * code that launches the server.
	 */
	public void runServer(){
		try {
			server = new ServerSocket(portNum);
		}catch (IOException e){
			e.printStackTrace();
		}
		System.out.println("Listenting for connections...");
		while(true){
			try{
				connection = server.accept();
				talkToClient(connection);
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}
	}	
}
