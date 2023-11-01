package webquery.dto;

/**
 * Representation of the JSON object received in the request to initiate a query.
 * @author Daniel
 * */
public class StartQueryRequestDTO {
	
	private String keyword;
	
	public StartQueryRequestDTO(String keyword) {
		this.keyword = keyword;
	}

	/**
	 * Get the keyword for query.
	 * @return Query keyword.
	 */
	public String getKeyword() {
		return keyword;
	}

}
