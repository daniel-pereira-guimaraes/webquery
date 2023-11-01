package webquery.dto;

import java.util.Collection;

import webquery.model.WebQuery;

/**
 * Representation of a JSON object sent as a data request response to an already initiated query.
 * 
 * @author Daniel
 */
public class WebQueryResponseDTO implements ResponseDTO {
	
	private Long id;
	private String status;
	private Collection<String> urls;
	
	public WebQueryResponseDTO(WebQuery query) {
		this.id = query.getId();
		this.status = query.getStatus().getId();
		this.urls = query.getResult();
	}

	/**
	 * Get the query ID.
	 * @return Query ID.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Get Query status (active or done).
	 * @return Query status as string.
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Gets the collection of URLs of pages that contain the searched keyword.
	 * @return Collection of URLs of pages with the keyword.
	 */
	public Collection<String> getUrls() {
		return urls;
	}

}
