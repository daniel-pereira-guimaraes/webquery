package webquery.model;

/**
 * Modal class for web page.
 * 
 * @author Daniel
 *
 */
public class WebPage {
	
	private String url;
	private String body;
	
	/**
	 * Create an instance of WebPage with url and body.
	 * @param url Page URL.
	 * @param body Page body (content).
	 */
	public WebPage(String url, String body) {
		this.url = url;
		this.body = body;
	}

	/**
	 * Get the page URL.
	 * @return URL page.
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Get the page body/content.
	 * @return Page body.
	 */
	public String getBody() {
		return body;
	}

}
