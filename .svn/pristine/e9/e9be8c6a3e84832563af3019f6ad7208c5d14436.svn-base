package edu.upenn.cis455.components;

public class SOAPMessage {

	public SOAPMessage(){}
	
	
	/**
	 * API method that constructs the SOAP message with keyword
	 * in the QUERY element tag.
	 * @param keyword - the search key.
	 * @return the SOAP message.
	 */
	public String constructSOAPMessage(String keyword){
		String doctype = "<?xml version=\"1.0\"?>";
		String soapHeader = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2001/12/soap-envelope\" " + 
"soap:encodingStyle=\"http://www.w3.org/2001/12/soap-encoding\">";
		String soapBody = "<soap:Body>" + "<QUERY>"+ keyword + "</QUERY></soap:Body>";
		String closeTag = "</soap:Envelope>";
		StringBuffer contents = new StringBuffer();
		contents.append(doctype);
		contents.append(soapHeader);
		contents.append(soapBody);
		contents.append(closeTag);
		return contents.toString();
	}
	
	/**
	 * API method that constructs the Result SOAP Message.
	 * @param contents - the contents to be sent to the servlet.
	 * @return the SOAP Message.
	 */
	public String constructSOAPResultMessage(String contents){
		String doctype = "<?xml version=\"1.0\"?>";
		String soapHeader = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2001/12/soap-envelope\" " + 
"soap:encodingStyle=\"http://www.w3.org/2001/12/soap-encoding\">";
		String soapBody = "<soap:Body>" + "<RESULT>"+ contents + "</RESULT></soap:Body>";
		String closeTag = "</soap:Envelope>";
		StringBuffer msg = new StringBuffer();
		msg.append(doctype);
		msg.append(soapHeader);
		msg.append(soapBody);
		msg.append(closeTag);
		return msg.toString();
	}
	
	
	
	/**
	 * API method that is used to extract code from the SOAP Message.
	 * @param content - the SOAP Message.
	 * @return - the keyword sent as part of the request.
	 */
	public String getKeyword(String content){
		String contents = null;
		if(content.contains("<QUERY>")){
			int startIndex = content.indexOf("<QUERY>");
			int endIndex = content.indexOf("</QUERY>");
			contents = content.substring(startIndex, endIndex);
			contents = contents.replace("<QUERY>", "").trim();
		}
		else if(content.contains("<RESULT>")){
			int startIndex = content.indexOf("<RESULT>");
			int endIndex = content.indexOf("</RESULT>");
			contents = content.substring(startIndex,endIndex);
			contents = contents.replace("<RESULT>", "").trim();
		}
		return contents;
	}
	
}
