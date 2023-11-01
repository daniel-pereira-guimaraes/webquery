package webquery.dto;

/**
 * JSON object representation sent as a request response to initiate a 
 * query when executed successfully.
 * 
 * @author Daniel
 */
public class StartQueryResponseDTO implements ResponseDTO {
	
	private Long id;
	
	public StartQueryResponseDTO(Long id) {
		this.id = id;
	}
	
	/**
	 * Get the query ID.
	 * @return Query ID.
	 */
	public Long getId() {
		return this.id;
	}

}
