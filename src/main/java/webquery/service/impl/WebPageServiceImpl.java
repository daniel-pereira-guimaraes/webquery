package webquery.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import webquery.config.Environment;
import webquery.model.WebPage;
import webquery.service.WebPageService;

/**
 * Web page service implementation.
 * 
 * @author Daniel
 *
 */
public class WebPageServiceImpl implements WebPageService {
	
	private static final int BUFFER_SIZE = 8192;
	private static final String HTTPS = "https";
	private static final String HTTP = "http";
	private static final String URL_REGEX = "<a\\s+[^>]*href=[\"']([^\"']+)[\"'][^>]*>";
	private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX);
	
	private Environment environment;
	private String baseUrl;
	
	/**
	 * Create a instance of WebPageServiceImpl.
	 * @param environment Injection dependency of Environment object.
	 */
	public WebPageServiceImpl(Environment environment) {
		this.environment = environment;
		this.prepareBaseUrl();
	}
	
	private void prepareBaseUrl() {
		final String url = environment.getBaseUrl();
		final int lastSlashIndex = url.lastIndexOf('/');
		this.baseUrl = lastSlashIndex < 0 ? url : url.substring(0, lastSlashIndex + 1);
	}
	
	private boolean isAbsoluteURL(String url) {
		return url != null && (url.startsWith(HTTP) || url.startsWith(HTTPS));
	}
	
	private String getBeforeHash(String str) {
		int hashIndex = str.lastIndexOf('#');
		if (hashIndex >= 0) {
			return str.substring(0, hashIndex);
		} else {
			return str;
		}
	}
	
	/**
	 * Extracts from the body of the page represented by the webPage object the 
	 * relative and absolute ULRs that have the same base URL configured in the 
	 * BASE_URL environment variable.
	 * 
	 * @param Object that represents the web page.
	 * @return Set of URLs found.
	 */
	@Override
	public Set<String> extractUrls(WebPage webPage) {
		final Set<String> urls = new HashSet<>();
		final String html = webPage.getBody();
		final Matcher matcher = URL_PATTERN.matcher(html);
		try {
	        final URL pageUrls = new URL(webPage.getUrl());
	        while (matcher.find()) {
	            final String href = getBeforeHash(matcher.group(1));
	            processExtractedUrl(urls, pageUrls, href);
	        }
        } catch (MalformedURLException e) {
        	// Ignore!
        }
		return urls;
	}

	private void processExtractedUrl(Set<String> urls, URL pageUrl, String href) {
		try {
			final URL url = isAbsoluteURL(href) ? new URL(href) : new URL(pageUrl, href);
			if (url.toString().startsWith(this.baseUrl)) {
		    	urls.add(url.toString());
		    }
		} catch (MalformedURLException e) {
			//Ignore!
		}
	}
	
	private String readAllContent(BufferedReader reader) throws IOException {

		final StringBuilder sb = new StringBuilder(BUFFER_SIZE);
	    final char[] buffer = new char[BUFFER_SIZE];
	    int bytesRead;
	    
	    while ((bytesRead = reader.read(buffer)) > 0) {
	        sb.append(buffer, 0, bytesRead);
	    }
	    
	    return sb.toString();
	}
	
	/**
	 * Downloads the content of the page from the given URL. 
	 * @param url Web page URL.
	 * @return Page content (usually HTML). In case of failure, returns null.
	 */
	@Override
	public WebPage download(String url) {
		try {
            final URLConnection connection = new URL(url).openConnection();
            try {
	            try (InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
	            	try (BufferedReader bufferedReader = new BufferedReader(reader)) {
	            		return new WebPage(url, readAllContent(bufferedReader));
	            	}
	            }
            } finally {
            	((HttpURLConnection) connection).disconnect();
            }
        } catch (IOException e) {
        	return null;
        }
	}

}
