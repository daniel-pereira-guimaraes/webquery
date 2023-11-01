package webquery.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class WebQueryTest {
	
	private static final Long ID = 1L;
	private static final String KEYWORD = "keyword";
	
	@Test
	void testInitialState() {
		
		final WebQuery webQuery = new WebQuery(ID, KEYWORD);
		
		assertEquals(ID, webQuery.getId());
		assertEquals(KEYWORD, webQuery.getKeyword());
		assertEquals(WebQuery.Status.ACTIVE, webQuery.getStatus());
		assertEquals(0, webQuery.getResultCount());
		assertTrue(webQuery.getUrls().isEmpty());
		assertTrue(webQuery.getResult().isEmpty());
	}
	
	@Test
	void testIncResultCount() {

		final WebQuery webQuery = new WebQuery(ID, KEYWORD);
		final long resultCount = webQuery.getResultCount();
		
		webQuery.incResultCount();
		assertEquals(resultCount + 1, webQuery.getResultCount()); 
	}
	
	@Test
	void testResult() {

		final WebQuery webQuery = new WebQuery(ID, KEYWORD);
		
		webQuery.getUrls().put("a", false);
		webQuery.getUrls().put("b", true);
		webQuery.getUrls().put("c", false);
		webQuery.getUrls().put("d", true);
		
		assertEquals("[b, d]", webQuery.getResult().toString());
	}
	
	@Test
	@SuppressWarnings("unlikely-arg-type")
	void testEquals() {

		final WebQuery reference = new WebQuery(ID, KEYWORD);
		final WebQuery equalId = new WebQuery(ID, KEYWORD + "x");
		final WebQuery differentId = new WebQuery(ID + 1, KEYWORD);
		
		assertEquals(reference, equalId);
		assertNotEquals(reference, differentId);
		assertFalse(reference.equals(null)); // NOSONAR
		assertFalse(reference.equals("")); // NOSONAR - Different class!
	}

}
