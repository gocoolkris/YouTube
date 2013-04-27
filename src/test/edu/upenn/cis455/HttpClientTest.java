package test.edu.upenn.cis455;

import edu.upenn.cis455.components.HttpClient;
import junit.framework.TestCase;

public class HttpClientTest extends TestCase {

	public void testFetchContents() {
		String url = "https://gdata.youtube.com/feeds/api/videos?q=football";
		HttpClient client = new HttpClient(url);
		System.out.println(client.fetchContents());
	}
}
