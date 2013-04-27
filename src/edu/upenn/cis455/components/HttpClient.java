package edu.upenn.cis455.components;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;

public class HttpClient {

	private String url, hostname, filename;
	private Socket socket;
	private StringBuffer header, body;
	private boolean isBody;
	private BufferedReader reader;
	private PrintWriter writer;
	private InetAddress ip;
	public HttpClient(String url){
		try{
			this.url = url;
			header = new StringBuffer();
			body = new StringBuffer();
			initialize();
		}catch(Exception e){}
	}

	private void initialize() {
		try{
			URL Url = new URL(url);
			hostname = Url.getHost();
			filename = Url.getFile();
			ip = InetAddress.getByName(hostname);
			socket = new Socket(ip, 80);
			writer = new PrintWriter(socket.getOutputStream(), true);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}catch(Exception e){}
	}

	public String fetchContents(){
		try{
			this.sendRequest();
			this.readResponse();
			return body.toString();
		}catch(Exception e){}
		finally{
			try{
				writer.close();
				reader.close();
				socket.close();
			}catch(Exception e){}
		}
		return null;
	}	
	
	private void sendRequest(){
		try{
			writer.print("GET " + filename + " HTTP/1.0\r\n");
			writer.print("Host: " + hostname + "\r\n");
			writer.print("User-Agent: cis455crawler\r\n");
			writer.print("Accept: text/html,text/xml,application/xml\r\n");
			writer.print("Accept-Charset: utf-8\r\n");
			writer.print("Accept-Encoding:gzip, deflate\r\n");
			writer.print("Accept-Language:en-US\r\n");
			writer.print("Connection:close\r\n\r\n");
			writer.flush();
		}catch(Exception e){}
	}

	private void readResponse(){
		try{
			String header = reader.readLine();
			if(header.toUpperCase().contains("200 OK")){
				String line;
				while((line = reader.readLine())!= null){
					if(!isBody){
						if(isBlankLine(line)){
							isBody = true;
						}
					}
					else{
						body.append(line + "\n");
					}
				}
			}
			else return;
		}catch(Exception e){}
	}

	private boolean isBlankLine(String line) {
		if(line.trim().equals("")) return true;
		return false;
	}

}
