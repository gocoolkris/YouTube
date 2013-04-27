package edu.upenn.cis455.youtube;

import java.net.ServerSocket;
import java.net.Socket;

import edu.upenn.cis455.components.GlobalData;

/**
 * Daemon class that runs as the server application listening to
 * the servlet request.
 * @author gokul.
 *
 */
public class Daemon implements Runnable{

	int portnumber;
	ServerSocket serverSocket;
	public Daemon(int portnumber){
		try{
			this.portnumber = portnumber;
			serverSocket = new ServerSocket(portnumber);
			System.out.println("Initializing a new ServerSocket");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	/**
	 * the Daemon thread runs in an infinite loop to 
	 * accept the socket connections and enqueues it.
	 */
	@Override
	public void run() {
		while(true){
			try{
				Socket s = serverSocket.accept();
				GlobalData.userRequests.enqueue(s);
				
			}catch(Exception e){}
		}
	}




}
