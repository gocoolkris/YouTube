package edu.upenn.cis455.youtube;

import java.net.URL;
import java.util.List;

import com.google.gdata.client.Query.CategoryFilter;
import com.google.gdata.client.youtube.YouTubeQuery;
import com.google.gdata.client.youtube.YouTubeService;
import com.google.gdata.data.Category;
import com.google.gdata.data.Person;
import com.google.gdata.data.media.mediarss.MediaContent;
import com.google.gdata.data.media.mediarss.MediaThumbnail;
import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.data.youtube.VideoFeed;
import com.google.gdata.data.youtube.YouTubeMediaGroup;
import com.google.gdata.data.youtube.YouTubeNamespace;

import edu.upenn.cis455.components.CacheService;
/**
 * The class fetches the search results from the cache. If the results are not
 * present, it then uses the API to fetch the results, cache them and then
 * return the results.
 * @author gokul
 *
 */
public class YouTubeClient {

	private CacheService cacheService;

	public YouTubeClient(String dbLocation){
		cacheService = new CacheService(dbLocation);
	}

	/**
	 * the API method used to fetch the search results.
	 * it searches the cache for the keyword and if not
	 * present, it then uses the API to get the results.
	 * @param keyword - the keyword to be searched for.
	 * @return results.
	 */
	public String searchVideos(String keyword){
		try{
			if(cacheService.containsEntry(keyword)){
				String contents = cacheService.getCacheContent(keyword);
				System.err.println("Query for " + keyword + " resulted in a cache <hit>");
				return contents;
			}
			else{
				System.err.println("Query for " + keyword + " resulted in a cache <miss>");
				YouTubeService service = new YouTubeService("cis455YouTubeSearch");
				YouTubeQuery query = new YouTubeQuery(new URL("http://gdata.youtube.com/feeds/api/videos"));
				query.setOrderBy(YouTubeQuery.OrderBy.RELEVANCE);
				query.setFullTextQuery(keyword);
				query.setSafeSearch(YouTubeQuery.SafeSearch.NONE);
				CategoryFilter categoryFilter = new CategoryFilter();
				Category category = new Category(YouTubeNamespace.KEYWORD_SCHEME,keyword);
				categoryFilter.addCategory(category);
				query.addCategoryFilter(categoryFilter);
				VideoFeed videoFeed = service.query(query, VideoFeed.class);
				StringBuffer contents = new StringBuffer();
				contents.append("<html><head><title>YouTube Search</title>");
				contents.append("<link rel=\"shortcut icon\" href=\"http://s.ytimg.com/yts/img/favicon-vfldLzJxy.ico\" type=\"image/x-icon\">");
				contents.append("</head><body>");
				
				for(VideoEntry ve : videoFeed.getEntries()){
					contents.append("<div>");

					YouTubeMediaGroup mediagroup = ve.getMediaGroup();
					if(mediagroup != null){
						List<MediaThumbnail> tnails = mediagroup.getThumbnails();
						if(tnails != null){
							MediaThumbnail nail = tnails.get(0);

							contents.append("<img src=\""+nail.getUrl() + "\"");
							contents.append(" height=\""+nail.getHeight() + "\"");
							contents.append(" width=\""+nail.getWidth() + "\"><br>");
						}
						List<MediaContent> med_con= mediagroup.getContents();
						String contenturl = "";
						if(med_con != null){
							for(MediaContent c : med_con){
								if(c.getType().contains("application")){
									contenturl = c.getUrl();
									break;
								}
							}
						}
						if(ve.getTitle() != null){
							String link = "<a href=\""+contenturl+"\">" + ve.getTitle().getPlainText() + "</a><br>";
							contents.append(link);
						}
						List<Person> authors = ve.getAuthors();
						if(authors != null){
							String  authorlist = "";
							for(Person p : authors) {
								if(p.getUri() != null){
									authorlist += "<a href=\"" + p.getUri() +"\">" + p.getName() + "</a>,";
								} else{
									authorlist += p.getName() + ",";
								}
							}
							authorlist = authorlist.substring(0, authorlist.lastIndexOf(','));
							contents.append(authorlist+"<br>");
						}
						if(mediagroup.getDescription() != null){
							contents.append(mediagroup.getDescription().getPlainTextContent());
						}
					}
					contents.append("</div>");
				}
				contents.append("</body></html>");
				cacheService.addToCache(keyword, contents.toString());
				return contents.toString();
			}
		}catch(Exception e){
			e.printStackTrace();	
		}
		return null;
	}
}
