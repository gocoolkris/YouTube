package edu.upenn.cis455.components;

import java.net.Socket;
import java.util.Vector;

import edu.upenn.cis455.youtube.PastryMessage;

public class ResultsQueue {
private Vector<PastryMessage> results;
	
	public ResultsQueue(){
		results = new Vector<PastryMessage>();
		System.out.println("ResultsQueue initialized");
	}
	
	
	/**
	 * synchronized method that puts the result 
	 * from a node to the result queue.
	 * @param s
	 */
	public void enqueue(PastryMessage s){
		synchronized(this){
			results.add(s);
		}
	}
	
	/**
	 * the API method that dequeues the result message
	 * to be sent to the Servlet.
	 * @return the  Pastrymessage containing the results
	 * of the search queue.
	 */
	public PastryMessage dequeue(){
		synchronized(this){
			if(results.isEmpty())
				return null;
			else return results.remove(0);
		}
	}
	
	
	/**
	 * helper method that checks whether result
	 * queue is empty 
	 * @return true if the queue is empty, false otherwise.
	 */
	public boolean isEmpty(){
		synchronized(this){
			return results.isEmpty();
		}
	}
}
