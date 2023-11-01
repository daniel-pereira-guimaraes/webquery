package webquery.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class StartQueryRequestDTOTest {
	
	private static final String KEYWORD = "keyword";
	
	@Test
	void testConstructorAndGet() {
		
		final StartQueryRequestDTO dto = new StartQueryRequestDTO(KEYWORD);
		
		assertEquals(KEYWORD, dto.getKeyword());
	}

}
