package server;
/* File: TunaServer.java
 * Author: Stanley Pieda, based on course materials by Todd Kelley
 * Modified Date: Aug 2018
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import datatransfer.Tuna;
import datatransfer.Message;

import dataaccesslayer.TunaDao;
import dataaccesslayer.TunaDaoImpl;

/**
 * Need programming comments with correct author name throughout this class
 * @author xyz abc
 */
public class TunaServer {


	private ServerSocket server;
	private Socket connection;
	private int messagenum;
	private int portNum = 8081;
	public static ExecutorService threadExecutor = Executors.newCachedThreadPool();

	public static void main(String[] args) {
		if(args.length > 0){
			(new TunaServer(Integer.parseInt(args[0]))).runServer();
		}else{
			(new TunaServer(8081)).runServer();
		}
	}
	public TunaServer(int portNum){
		this.portNum = portNum;
	}
	public void talkToClient(final Socket connection){
		//begin thread
		threadExecutor.execute( new Runnable () {
			public void run(){	
				ObjectOutputStream output = null;
				ObjectInputStream input = null;
				String message = "";
				System.out.println("Got a connection");
				try {
					SocketAddress remoteAddress = connection.getRemoteSocketAddress();
					String remote = remoteAddress.toString();
					//set up input and output streams with client
					output = new ObjectOutputStream (connection.getOutputStream());
					input = new ObjectInputStream( connection.getInputStream());               
					do {
						message = (String) input.readObject();
						System.out.println("From:" + remote + ">"+message);
						//indicates the client wants to disconnect.. has to be changed in coordination with client. maybe "exit" string. 
						if(message == null || message.isEmpty()) {
							message = null;
						}
						else {
							message = messagenum++ + " Output> " + message;
						}
						output.writeObject(message);
						output.flush();
					} while (message != null);
					System.out.println(remote + " disconnected via request");
				} catch (IOException exception) {
					System.err.println(exception.getMessage());
					exception.printStackTrace();
				}catch (ClassNotFoundException exception) {
					System.out.println(exception.getMessage());
					exception.printStackTrace();
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
