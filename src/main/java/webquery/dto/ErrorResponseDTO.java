package webquery.dto;

/**
 * Class to generate HTTP response with JSON containing error message.
 * @author Daniel
 *
 */
public class ErrorResponseDTO implements ResponseDTO {
	
	private String message;
	
	public ErrorResponseDTO(String message) {
		this.message = message;
	}

	/**
	 * Get the error message.
	 * @return Error message.
	 */
	public String getMessage() {
		return message;
	}

}
