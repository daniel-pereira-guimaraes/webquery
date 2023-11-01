package webquery.controller;

import org.eclipse.jetty.http.HttpStatus;

import com.google.gson.Gson;

import spark.Request;
import spark.Response;
import webquery.common.Message;
import webquery.dto.ErrorResponseDTO;
import webquery.dto.ResponseDTO;
import webquery.dto.StartQueryRequestDTO;
import webquery.dto.StartQueryResponseDTO;
import webquery.dto.WebQueryResponseDTO;
import webquery.model.WebQuery;
import webquery.service.WebQueryService;

/**
 * This class controls the inputs and outputs of the query API, receiving GET and POST 
 * requests and forwarding them to the corresponding service.
 * 
 * @author Daniel
 */
public class WebQueryController {
	
	private static final String APPLICATION_JSON = "application/json";
	private static final int MIN_KEYWORD_LENGTH = 4;
	private static final int MAX_KEYWORD_LENGTH = 32;
	
	private WebQueryService webQueryService;
	
	/**
	 * Creates a controller instance with an injected service instance.
	 * 
	 * @param webQueryService Service dependency injection.
	 */
	public WebQueryController(WebQueryService webQueryService) {
		this.webQueryService = webQueryService;
	}
	
	/**
	 * Processes POST request to start a query. If the query started successfully, 
	 * it returns status 200 and a JSON object with query ID. In case of failure, 
	 * it returns status 400 and an error message.
	 * 
	 * @param req HTTP request data with keyword in the body.
	 * @param res HTTP response data.
	 * @return JSON object with ID of the started query or error message in case of failure.
	 */
	public String startQuery(Request req, Response res) {

		ResponseDTO responseDTO;
		final Gson gson = new Gson();
		final StartQueryRequestDTO requestDTO = gson.fromJson(req.body(), StartQueryRequestDTO.class);
		final String keyword = requestDTO.getKeyword();

		if (keyword == null 
				|| keyword.length() < MIN_KEYWORD_LENGTH 
				|| keyword.length() > MAX_KEYWORD_LENGTH) {
			responseDTO = new ErrorResponseDTO(Message.get("invalid.keyword"));
			res.status(HttpStatus.BAD_REQUEST_400);
		} else {
			final WebQuery webQuery = webQueryService.start(keyword);
			responseDTO = new StartQueryResponseDTO(webQuery.getId());
		}
		
		res.type(APPLICATION_JSON);
		return gson.toJson(responseDTO);
	}
	
	/**
	 * Processes GET request for started query. If the query found, it returns status 200 
	 * and a JSON object with query data (id, status, urls). In case of failure, it returns 
	 * status 400 and an error message.
	 * 
	 * @param req HTTP request data.
	 * @param res HTTP response data.
	 * @return JSON object with query data (id, status, urls) or error messsage.
	 */
	public String getQuery(Request req, Response res) {
		ResponseDTO responseDTO;
		try {
			final WebQuery webQuery = webQueryService.findById(Long.valueOf(req.params("id")));
			if (webQuery == null) {
				responseDTO = new ErrorResponseDTO(Message.get("query.not.found"));
				res.status(HttpStatus.BAD_REQUEST_400);
			} else {
				responseDTO = new WebQueryResponseDTO(webQuery);
			}
		} catch (NumberFormatException e) {
			responseDTO = new ErrorResponseDTO(Message.get("invalid.query.id"));
			res.status(HttpStatus.BAD_REQUEST_400);
		}
		res.type(APPLICATION_JSON);
		return new Gson().toJson(responseDTO);
	}

}
