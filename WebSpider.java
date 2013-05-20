package util;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

public class WebSpider {
	
	private static final String PROXY_HOST = "70.1.172.11";
	private static final String PROXY_PORT = "9090";
	
	/*µÁÄ¹±Ê¼Ç
	private static final String BOOK_NAME = "µÁÄ¹±Ê¼Ç";
	private static final String CONTENT_TEXT_START_STRING = "<div style=\"clear:both\"></div>";
	private static final String CONTENT_TEXT_END_STRING = "<script type=\"text/javascript\"><!--";
	*/
	
	/*´óÄ®²ÔÀÇ*/
	//private static final String BOOK_NAME = "´óÄ®²ÔÀÇ-¾øµØ¿±Ì½";
	private static final String BOOK_NAME = "´óÄ®²ÔÀÇ-¾øÃÜ·ÉÐÐ";
	private static final String CONTENT_TEXT_START_STRING = "<div style=\"clear:both; padding-bottom:8px\"></div>";
	private static final String CONTENT_TEXT_END_STRING = "<p><a href=\"http://www.damocanglang.com/";

	
	
	public static void main(String[] args) throws IOException {
		
		WebSpider webSpider = new WebSpider();
		//webSpider.setProxy(PROXY_HOST, PROXY_PORT);
		
		PrintWriter writer = new PrintWriter(new FileWriter(BOOK_NAME + ".txt")); 
		writer.println("¡¶" + BOOK_NAME + "¡·\n\n");
		for(String key : Const.map.keySet()){
			writer.println(key + "\n");
			String page = webSpider.loadPage(Const.map.get(key));
			writer.println(webSpider.getText(page));
			writer.flush();
		}
		writer.close();
		
	}
	
	public String getText(String html){
		html = html.substring(html.indexOf(CONTENT_TEXT_START_STRING) + CONTENT_TEXT_START_STRING.length());
		html = html.substring(0, html.indexOf(CONTENT_TEXT_END_STRING) );
		html = html.replaceAll("<p>", "").replaceAll("</p>", "\n");
		return html;
	}
	
	public String loadPage(String url){
		
		HttpURLConnection conn = null;
		BufferedReader reader = null;
		try {
			conn = (HttpURLConnection)new URL(url).openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("content-type", "text/html; charset=UTF-8");
			conn.setDoInput(true);
			conn.connect();
			reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			StringBuffer pageStr = new StringBuffer();
			String line = null;
			while ((line = reader.readLine()) != null) {
				pageStr.append(line);
			}
			return pageStr.toString();
		}  catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if(reader != null){
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(conn != null){
				conn.disconnect();
			}
		}
	}
	
	public void setProxy(String proxyHost, String proxyPort){
		Properties prop = System.getProperties();
		prop.setProperty("http.proxyHost", proxyHost);
		prop.setProperty("http.proxyPort", proxyPort);
	}

}
