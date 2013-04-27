package test.edu.upenn.cis455;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class RunAllTests extends TestCase 
{
  public static Test suite() 
  {
    try {
      Class[]  testClasses = {
        /* Add the names of your unit test classes here */
        // Class.forName("your.class.name.here") 
    		  Class.forName("test.edu.upenn.cis455.CacheServiceTest"),
    		  Class.forName("test.edu.upenn.cis455.HttpClientTest"),
    		  Class.forName("test.edu.upenn.cis455.YouTubeClientTest")
    		  
      };   
      
      return new TestSuite(testClasses);
    } catch(Exception e){
      e.printStackTrace();
    } 
    
    return null;
  }
}
