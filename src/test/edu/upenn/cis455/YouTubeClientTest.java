package test.edu.upenn.cis455;

import edu.upenn.cis455.youtube.YouTubeClient;
import junit.framework.TestCase;

public class YouTubeClientTest extends TestCase {

	YouTubeClient client;
	
	public void testValidKeyword(){
		client = new YouTubeClient("/home/cis455/Documents/YouTubeTest");
		String contents = client.searchVideos("Krsna");
		assertNotNull(contents);
		System.out.println(contents);
	}
	
	public void testValidKeyword2(){
		client = new YouTubeClient("/home/cis455/Documents/YouTubeTest");
		String contents = client.searchVideos("Soccer");
		assertNotNull(contents);
		System.out.println(contents);
	}
	
	
}
