package edu.upenn.cis455.components;

import java.util.Date;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

/**
 * The entity class that stores the CacheEntry results.
 * @author gokul.
 *
 */
@Entity
public class CacheEntry {

	public CacheEntry(){}
	public CacheEntry(String q, String content){
		this.queryString = q;
		this.searchResults = content;
	}
	
	public Date getLastAccessTime() {
		return lastAccessTime;
	}

	public void setNowAsLastAccessTime() {
		Date now = new Date();
		this.lastAccessTime = now;
	}

	public String getQueryString() {
		return queryString;
	}

	public String getSearchResults() {
		return searchResults;
	}
	//query string.
	@PrimaryKey
	String queryString;
	//the search results.
	String searchResults;
	//the last access time the entry was accessed.
	Date lastAccessTime;
	
}
