package test.edu.upenn.cis455;

import edu.upenn.cis455.components.CacheService;
import junit.framework.TestCase;

public class CacheServiceTest extends TestCase {

	CacheService cacheService;
	
	public void testAddToCache() {
		cacheService = new CacheService("/home/cis455/Documents/YouTubeTest");
		cacheService.addToCache("soccer", "blah blah");
		assertTrue(cacheService.containsEntry("soccer"));
		assertEquals(cacheService.getCacheContent("soccer"),"blah blah");
	}

	public void testContainsEntry() {
		cacheService = new CacheService("/home/cis455/Documents/YouTubeTest");
		assertTrue(cacheService.containsEntry("soccer"));
		assertFalse(cacheService.containsEntry("football"));
	}

	public void testGetCacheContent() {
		cacheService = new CacheService("/home/cis455/Documents/YouTubeTest");
		cacheService.addToCache("soccer", "blah blah");
		assertEquals(cacheService.getCacheContent("soccer"),"blah blah");
		assertNull(cacheService.getCacheContent("football"));
	}

}
