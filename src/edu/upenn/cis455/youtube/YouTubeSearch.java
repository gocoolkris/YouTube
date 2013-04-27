package edu.upenn.cis455.youtube;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.upenn.cis455.components.SOAPMessage;

/**
 * The servlet class that displays and POSTs the data to the Pastry
 * Application.
 * @author gokul
 *
 */
public class YouTubeSearch extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response){
		try{

			PrintWriter writer = response.getWriter();
			writer.print("<html><head><title>YouTube Search</title>");
			writer.print("<link rel=\"shortcut icon\" href=\"http://s.ytimg.com/yts/img/favicon-vfldLzJxy.ico\" type=\"image/x-icon\">");
			writer.print("</head><body>");
			writer.print("<div align=\"center\">");
			writer.print("<h3>Name: Gokul Ragunathan</h3>");
			writer.print("<h3>SEAS Login : grag");
			writer.print("</div><div align=\"center\">");
			writer.print("<form method=\"POST\" action=\"youtube\">");
			writer.print("<input type=\"text\" name=\"search\" />");
			writer.print("<input type=\"submit\" value=\"Search\" />");
			writer.print("</form></div></body></html>");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response){
		try{
			String keyword = request.getParameter("search").trim();
			if(keyword.isEmpty() || keyword.equals("null")){
				response.sendRedirect("youtube");
			}
			else{
				String server = this.getServletContext().getInitParameter("cacheServer");
				String prt = this.getServletContext().getInitParameter("cacheServerPort");
				SOAPMessage message = new SOAPMessage();
				String contents = message.constructSOAPMessage(keyword);
				System.out.println("Servlet received the browser request");
				System.out.println(contents);
				int port = Integer.parseInt(prt);
				InetAddress ip = InetAddress.getByName(server);
				Socket socket = new Socket(ip, port);
				OutputStream out = socket.getOutputStream();
				PrintWriter writer = new PrintWriter(out);
				
				writer.println(contents);
				writer.flush();
				//writer.close();
				
				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				StringBuffer results = new StringBuffer();
				String line = null;
				while((line = reader.readLine())!= null){
					results.append(line + "\n");
				}
				System.out.println(results);
				String browserResponse = message.getKeyword(results.toString());
				writer = response.getWriter();
				writer.print(browserResponse);
				writer.flush();
				socket.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}



	}

}
