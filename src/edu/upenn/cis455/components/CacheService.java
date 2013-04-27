package edu.upenn.cis455.components;

import java.io.File;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.StoreConfig;

/**
 * @author gokul
 * This class  is used to access the Berkeley Database
 * for storing and retrieving cached results.
 *
 */
public class CacheService {

	private static Environment env;
	private static EntityStore store;
	private String storeName = "P2PCache";
	private String envLocation;
	private PrimaryIndex<String,CacheEntry> queryString;
	public CacheService(String dbLocation){
		this.envLocation = dbLocation;
		setUp();
		queryString = store.getPrimaryIndex(String.class, CacheEntry.class);
	}


	/**
	 * initializes the database if not already setup. if the database
	 * is already present, it then retrieves the cached results.
	 */
	private void setUp(){
		try{
			EnvironmentConfig envConfig = new EnvironmentConfig();
			envConfig.setLockTimeout(1000, TimeUnit.MILLISECONDS);
			StoreConfig strConfig = new StoreConfig();
			envConfig.setReadOnly(false);
			strConfig.setReadOnly(false);
			//if needed to write
			envConfig.setAllowCreate(true);
			strConfig.setAllowCreate(true);
			File path = new File(envLocation);
			if(!path.exists()) path.mkdirs();	        
			env = new Environment(path,envConfig);
			store = new EntityStore(env,storeName,strConfig);
		}catch(Exception e){}
	}

    /**
     * API method that is used to add cache entries.
     * @param querystring - the queryString that was searched for.
     * @param content - the content as returned by the gdata API
     * @return - true, if the cache entry has been successfully added
     * to the database, false otherwise.
     */
	public boolean addToCache(String querystring, String content){
		try{
			evictExpiredEntries();
			CacheEntry c = new CacheEntry(querystring,content);
			c.setNowAsLastAccessTime();
			queryString.put(c);
			while(doesExceedStorageLimit() && queryString.count() > 1)
				evictLeastRecentlyUsedEntry();
			env.sync();
			return true;
		}catch(Exception e){}

		return false;
	}

	/**
	 * helper method that evicts the least recently used cache entry,
	 * from the list of all the cache entries in the database.
	 */
	private void evictLeastRecentlyUsedEntry() {
		EntityCursor<CacheEntry> cacheobjects = queryString.entities();
		try{
			if(cacheobjects.count() == 1) return;
			CacheEntry leastRecentlyUsed = cacheobjects.first();
			for(CacheEntry c : cacheobjects){
				if(leastRecentlyUsed.lastAccessTime.getTime() < c.lastAccessTime.getTime()){
					leastRecentlyUsed = c;
				}
			}
			queryString.delete(leastRecentlyUsed.queryString);
			env.sync();
		}catch(Exception e){}
		finally{
			cacheobjects.close();
		}
	}

	/**
	 * helper method that initially purges the expired entries. 
	 * It then checks whether the database contains the requested
	 * entry.
	 * @param qString - the query String.
	 * @return true if the cache database has the entry, false otherwise.
	 */
	public boolean containsEntry(String qString){
		//evictExpiredEntries();
		while(doesExceedStorageLimit() && queryString.count() > 1)
			evictLeastRecentlyUsedEntry();
		env.sync();
		EntityCursor<CacheEntry> cacheobjects = null;
		try{
			cacheobjects = queryString.entities();
			for(CacheEntry c : cacheobjects){
				if(c.getQueryString().equals(qString))
					return true;
			}
			return false;
		}catch(Exception e){
			return false;
		}finally{
			cacheobjects.close();
		}
	}



	/**
	 * API method that is used to fetch the contents of the entry
	 * if the entry is present in the database. It also updates the 
	 * last access time.
	 * @param querystring - the query string for which the results has
	 * to be fetched.
	 * @return the contents of the queryString.
	 */
	public String getCacheContent(String querystring){
		try{
			CacheEntry c = queryString.get(querystring);
			if(c != null){
				c.setNowAsLastAccessTime();
				return c.getSearchResults();
			}
		}catch(Exception e){}
		finally{
			env.sync();
		}
		return null;
	}


	/**
	 * helper method that checks whether the database size exceeds the
	 * specified storage limit.
	 * @return true if the storage exceeds the specified limit, 
	 * false otherwise.
	 */
	private boolean doesExceedStorageLimit(){
		try{
			File db = new File(envLocation);
			double length = getFolderSize(db);
			double limit = GlobalData.STORAGE_LIMIT_IN_MB * 1024 * 1024;
			if(length > limit) return true;
		}catch(Exception e){}
		return false;
	}

	/**
	 * helper method that calculates the database size.
	 * @param db - the database file of the storage.
	 * @return size of the database.
	 */
	private double getFolderSize(File db){
		try{
			double length = 0;
			for(File f : db.listFiles()){
				if(f.isDirectory()){
					length += getFolderSize(f);
				}
				else length += f.length();
			}
			return length;
		}catch(Exception e){
			return 0;
		}
	}


	/**
	 * helper method that checks whether the cache entry is
	 * expired.
	 * @param c - the cache entry.
	 * @return true if it is expired, false otherwise.
	 */
	private boolean isExpired(CacheEntry c){
		try{
			double lastAccess = c.getLastAccessTime().getTime();
			double now = new Date().getTime(); 
			double ttl_in_ms = GlobalData.TTL_IN_HOURS * 60 * 60 * 1000;
			if(now - lastAccess > ttl_in_ms){
				return true;
			}
		}catch(Exception e){}
		return false;
	}

	/**
	 * helper method that removes the expired 
	 * cache entries.
	 */
	private void evictExpiredEntries(){
		EntityCursor<CacheEntry> cacheobjects = null;
		try{
			cacheobjects = queryString.entities();
			for(CacheEntry c : cacheobjects){
				if(isExpired(c))
					queryString.delete(c.getQueryString());
			}
		}catch(Exception e){}
		finally{
			cacheobjects.close();
			env.sync();
		}

	}



}
