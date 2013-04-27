package edu.upenn.cis455.youtube;

import java.util.Random;

import rice.p2p.commonapi.Id;
import edu.upenn.cis455.components.GlobalData;

/**
 * The Ping Service that runs as a separate thread which periodically
 * (every 3 seconds) and picks a random node and pings it.
 * @author gokul.
 *
 */
public class PingPong implements Runnable {

	private static Random rand = new Random();
	private long dummyValue = 1L;
	@Override
	public void run() {
		while(true){
			try{
				Thread.sleep(GlobalData.sleepTime);
				String rid = rand.nextLong() + "";
				Id randId = GlobalData.app.nodeFactory.getIdFromString(rid);
				GlobalData.app.sendMessage(randId, "blah blah",MessageType.PING,dummyValue);
			}catch(Exception e){}
		}
	}

}
