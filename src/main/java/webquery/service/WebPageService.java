package webquery.service;

import java.util.Set;

import webquery.model.WebPage;

/**
 * Abstrate web page service.
 * 
 * @author Daniel
 *
 */
public interface WebPageService {

	/**
	 * Extracts from the body of the page represented by the webPage object the 
	 * relative and absolute ULRs that have the same base URL configured in the 
	 * BASE_URL environment variable.
	 * 
	 * @param Object that represents the web page.
	 * @return Set of URLs found.
	 */
	Set<String> extractUrls(WebPage webPage);

	/**
	 * Downloads the content of the page from the given URL. 
	 * @param url Web page URL.
	 * @return Page content (usually HTML).
	 */
	WebPage download(String url);

}