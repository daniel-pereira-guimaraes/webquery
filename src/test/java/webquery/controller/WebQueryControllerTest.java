package webquery.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.google.gson.Gson;

import spark.Request;
import spark.Response;
import webquery.common.Message;
import webquery.dto.ErrorResponseDTO;
import webquery.dto.WebQueryResponseDTO;
import webquery.model.WebQuery;
import webquery.service.WebQueryService;

class WebQueryControllerTest {
	
	private static final String getKeyword(int length) {
		final StringBuilder sb = new StringBuilder(length);
		for (int i = 1; i <= length; i++) {
			sb.append('x');
		}
		return sb.toString();
	}
	
	private static final String jsonStartReq(String keyword) {
		return new StringBuilder("{")
				.append('"').append("keyword").append('"').append(':')
				.append('"').append(keyword).append('"')
				.append('}').toString();
	}
	
	private static final String jsonStartOk(Long id) {
		return new StringBuilder("{")
				.append('"').append("id").append('"').append(':').append(id)
				.append('}').toString();
	}
	
	private static final Long ID = 1L;
	
	private static final String KEYWORD_MIN = getKeyword(4);
	private static final String KEYWORD_MAX = getKeyword(32);
	private static final String KEYWORD_SHORT = getKeyword(3);
	private static final String KEYWORD_LONG = getKeyword(33);

	private static final String JSON_KEYWORD_MIN = jsonStartReq(KEYWORD_MIN);
	private static final String JSON_KEYWORD_MAX = jsonStartReq(KEYWORD_MAX);
	private static final String JSON_KEYWORD_SHORT = jsonStartReq(KEYWORD_SHORT);
	private static final String JSON_KEYWORD_LONG = jsonStartReq(KEYWORD_LONG);
	private static final String JSON_START_OK = jsonStartOk(ID);
	
	private WebQueryService webQueryService;
	private WebQueryController webQueryController;
	
	@BeforeEach
	void beforeEach() {
		this.webQueryService = Mockito.mock(WebQueryService.class);
		this.webQueryController = new WebQueryController(this.webQueryService);
	}
	
	@Test
	void testStartQueryKeywordMin() {
		
		final WebQuery webQuery = new WebQuery(ID, KEYWORD_MIN);
		final Request req = Mockito.mock(Request.class);
		final Response res = Mockito.mock(Response.class);
		
		Mockito.when(this.webQueryService.start(KEYWORD_MIN)).thenReturn(webQuery);
		Mockito.when(req.body()).thenReturn(JSON_KEYWORD_MIN);
		
		assertEquals(JSON_START_OK, this.webQueryController.startQuery(req, res));
	}
	
	@Test
	void testStartQueryKeywordMax() {
		
		final WebQuery webQuery = new WebQuery(ID, KEYWORD_MAX);
		final Request req = Mockito.mock(Request.class);
		final Response res = Mockito.mock(Response.class);
		
		Mockito.when(this.webQueryService.start(KEYWORD_MAX)).thenReturn(webQuery);
		Mockito.when(req.body()).thenReturn(JSON_KEYWORD_MAX);
		
		assertEquals(JSON_START_OK, this.webQueryController.startQuery(req, res));
	}
	
	
	@Test
	void testStartQueryKeywordShort() {
		
		final WebQuery webQuery = new WebQuery(KEYWORD_SHORT);
		final Request req = Mockito.mock(Request.class);
		final Response res = Mockito.mock(Response.class);
		
		Mockito.when(this.webQueryService.start(KEYWORD_SHORT)).thenReturn(webQuery);
		Mockito.when(req.body()).thenReturn(JSON_KEYWORD_SHORT);
		
		final String json = this.webQueryController.startQuery(req, res);
		final ErrorResponseDTO error = new Gson().fromJson(json, ErrorResponseDTO.class);
		
		verify(res).status(HttpStatus.BAD_REQUEST_400);
		assertNotNull(error.getMessage());
	}
	
	@Test
	void testStartQueryKeywordLong() {
		
		final WebQuery webQuery = new WebQuery(KEYWORD_LONG);
		final Request req = Mockito.mock(Request.class);
		final Response res = Mockito.mock(Response.class);
		
		Mockito.when(this.webQueryService.start(KEYWORD_LONG)).thenReturn(webQuery);
		Mockito.when(req.body()).thenReturn(JSON_KEYWORD_LONG);
		
		final String json = this.webQueryController.startQuery(req, res);
		final ErrorResponseDTO error = new Gson().fromJson(json, ErrorResponseDTO.class);

		verify(res).status(HttpStatus.BAD_REQUEST_400);
		assertNotNull(error.getMessage());
	}
	
	@Test 
	void testGetQueryOk() {
		
		final WebQuery webQuery = new WebQuery(ID, KEYWORD_MIN);
		final Request req = Mockito.mock(Request.class);
		final Response res = Mockito.mock(Response.class);
		final WebQueryResponseDTO dto = new WebQueryResponseDTO(webQuery);
		final String json = new Gson().toJson(dto);
		
		Mockito.when(req.params("id")).thenReturn(ID.toString());
		Mockito.when(this.webQueryService.findById(ID)).thenReturn(webQuery);
		
		assertEquals(json, this.webQueryController.getQuery(req, res));
	}
	
	@Test 
	void testGetQueryWithInvalidId() {
		
		final Request req = Mockito.mock(Request.class);
		final Response res = Mockito.mock(Response.class);

		Mockito.when(req.params("id")).thenReturn("x");
		
		final String json = this.webQueryController.getQuery(req, res);

		final ErrorResponseDTO error = new Gson().fromJson(json, ErrorResponseDTO.class);
		
		verify(res).status(HttpStatus.BAD_REQUEST_400);
		assertEquals(Message.get("invalid.query.id"), error.getMessage());
	}
	
	@Test 
	void testGetQueryWithNotFound() {
		
		final Request req = Mockito.mock(Request.class);
		final Response res = Mockito.mock(Response.class);

		Mockito.when(req.params("id")).thenReturn(ID.toString());
		Mockito.when(this.webQueryService.findById(ID)).thenReturn(null);
		
		final String json = this.webQueryController.getQuery(req, res);

		final ErrorResponseDTO error = new Gson().fromJson(json, ErrorResponseDTO.class);
		
		verify(res).status(HttpStatus.BAD_REQUEST_400);
		verify(this.webQueryService).findById(ID);
		assertEquals(Message.get("query.not.found"), error.getMessage());
	}
	
}
