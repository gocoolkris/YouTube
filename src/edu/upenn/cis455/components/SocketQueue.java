package edu.upenn.cis455.components;

import java.net.Socket;
import java.util.Vector;

/**
 * the Socket Queue used to enqueue the incoming socket requests.s
 * @author gokul
 *
 */
public class SocketQueue {

	private Vector<Socket> servletRequests;
	
	public SocketQueue(){
		servletRequests = new Vector<Socket>();
		System.out.println("Queue initialized");
	}
	
	
	/**
	 * API method that enqueues the incoming
	 * socket requests.
	 * @param s - socket request.
	 */
	public void enqueue(Socket s){
		synchronized(this){
			servletRequests.add(s);
		}
	}
	
	/**
	 * dequeues the socket by P2PCache
	 * @return the socket request at the head of the queue.
	 */
	public Socket dequeue(){
		synchronized(this){
			if(servletRequests.isEmpty())
				return null;
			else return servletRequests.remove(0);
		}
	}
	
	
	/**
	 * checks whether the queue is empty or not.
	 * @return true if the queue is empty false otherwise.
	 */
	public boolean isEmpty(){
		synchronized(this){
			return servletRequests.isEmpty();
		}
	}
	
}
