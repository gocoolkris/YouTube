package edu.upenn.cis455.youtube;

import java.net.Socket;

import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.NodeHandle;

/**
 * @author gokul.
 * the PastryMessage template that would be used for
 * sending and receiving replies.
 * A typical PastryMessage consists of
 * sender - the NodeHandler of the sender Node
 * content - the Content(SOAP Message containing keyword, results etc..)
 * wantResponse - the boolean requesting a response to the sender.
 * type - message type of the message.
 * creationTime - the timestamp at which the message was received at the
 * cacheServer.
 */
public class PastryMessage implements Message{
	
	private static final long serialVersionUID = 1L;
	NodeHandle sender;
	String content;
	boolean wantResponse = true;
	MessageType type;
	long timeCreation;
	
	public PastryMessage(NodeHandle sender, MessageType type, String content){
		this.sender = sender;
		this.type = type;
		this.content = content;
	}
	
	public void setCreationTime(long time){
		this.timeCreation = time;
	}
	
	
	@Override
	public int getPriority() {
		return Message.LOW_PRIORITY;
	}

}
