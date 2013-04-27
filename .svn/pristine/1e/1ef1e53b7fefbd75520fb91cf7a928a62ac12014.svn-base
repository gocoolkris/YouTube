package edu.upenn.cis455.youtube;

import java.io.OutputStream;
import java.io.PrintWriter;

import rice.p2p.commonapi.Application;
import edu.upenn.cis455.components.GlobalData;
import edu.upenn.cis455.components.SOAPMessage;
import edu.upenn.cis455.youtube.MessageType;
import rice.p2p.commonapi.Endpoint;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.Node;
import rice.p2p.commonapi.NodeHandle;
import rice.p2p.commonapi.RouteMessage;

public class PastryApplication implements Application{

	NodeFactory nodeFactory;
	Node node;
	Endpoint endpoint;


	public PastryApplication(NodeFactory nfactory){
		this.nodeFactory = nfactory;
		this.node = nodeFactory.getNode();
		this.endpoint = node.buildEndpoint(this, "PastryApplication");
		endpoint.register();
	}

	/**
	 * API method that sends the message to a particular node in the ring.
	 * Based on the message type special actions are taken.
	 * @param receiverId - the Id of the intended receiver node.
	 * @param msgToSend - the PastryMessage to be sent to.
	 * @param type - the message type that specifies the action to be taken on.
	 * @param time - the time stamp at which the incoming requests was accessed.
	 */
	public void sendMessage(Id receiverId, String msgToSend, MessageType type, long time){
		PastryMessage msg = null;
		if(type == MessageType.PING){
			System.out.println("Sending PING to " + receiverId);
			msg = new PastryMessage(node.getLocalNodeHandle(),type,msgToSend);
		} else if(type == MessageType.PONG){

		} else if(type == MessageType.QUERY){
			msg = new PastryMessage(node.getLocalNodeHandle(),type,msgToSend);
			msg.setCreationTime(time);

		} else if(type == MessageType.RESULT){

		}
		endpoint.route(receiverId, msg, null);
	}

	/**
	 * the asynchronous method that is called whenever a node in the ring receives
	 * a message.
	 */
	@Override
	public void deliver(Id id, Message message) {
		PastryMessage msg = (PastryMessage) message;

		switch(msg.type){
		case PING:
			System.out.println("Received PING to ID " + id + "from node " + msg.sender + "; returning PONG");
			if(msg.wantResponse){
				PastryMessage reply = new PastryMessage(node.getLocalNodeHandle(),MessageType.PONG,"Sending Pong Back");
				reply.wantResponse = false;
				GlobalData.app.endpoint.route(null, reply, msg.sender);
			}
			break;
		case PONG:
			System.out.println("Received PONG from node " + msg.sender);
			break;
		case QUERY:
			System.out.println("Received query" + msg.content);
			String searchResults = getSearchResults(msg);
			String soapmsg = getSOAPMessage(searchResults);
			PastryMessage results = new PastryMessage(node.getLocalNodeHandle(),MessageType.RESULT,soapmsg);
			results.setCreationTime(msg.timeCreation);
			GlobalData.app.endpoint.route(null, results, msg.sender);
			break;
		case RESULT:
			GlobalData.resultsqueue.enqueue(msg);
			break;
		default:
			break;
		}
		//System.out.println("Received message " + msg.content + "from " + msg.sender);
	}

	/**
	 * helper method that is used to construct a SOAP message.
	 * @param contents - the contents that is to be sent to the
	 * cacheServer node.
	 * @return - the SOAP message to be sent to cacheServer node.
	 */
	private String getSOAPMessage(String contents){
		try{
			SOAPMessage message = new SOAPMessage();
			String msg = message.constructSOAPResultMessage(contents);
			return msg;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * helper method that gets the search results from the 
	 * Berkeley Database.
	 * @param msg - the request message.
	 * @return - the search results as found/constructed by the client 
	 * instance.
	 */
	private String getSearchResults(PastryMessage msg){
		try{
			String contents = null;
			SOAPMessage soap = new SOAPMessage();
			String message = msg.content;
			String keyword = soap.getKeyword(message);
			System.out.print("A request received at node :" + node.getId());
			System.out.println(" for keyword - " + keyword);
			contents = GlobalData.client.searchVideos(keyword);
			return contents;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Not required to implement.
	 */
	@Override
	public boolean forward(RouteMessage arg0) {
		return true;
	}

	/**
	 * Not required to implement.
	 */
	@Override
	public void update(NodeHandle arg0, boolean arg1) {
	}



}
