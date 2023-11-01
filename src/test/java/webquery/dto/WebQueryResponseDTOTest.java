package webquery.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import webquery.model.WebQuery;

class WebQueryResponseDTOTest {
	
	private static final Long ID = 1L;
	private static final String KEYWORD = "keyword";
	private static final String URL1 = "http://www.company.com/z";
	private static final String URL2 = "http://www.company.com/b";
	private static final String URL3 = "http://www.company.com/a";
	
	@Test
	
	void testConstructorAndGets() {
		
		final WebQuery webQuery = new WebQuery(ID, KEYWORD);
		webQuery.getUrls().put(URL1, false);
		webQuery.getUrls().put(URL2, true);
		webQuery.getUrls().put(URL3, true);
		
		final List<String> urls = Arrays.asList(URL2, URL3).stream()
				.sorted().collect(Collectors.toList());

		final WebQueryResponseDTO dto = new WebQueryResponseDTO(webQuery);
		
		assertEquals(webQuery.getId(), dto.getId());
		assertEquals(webQuery.getStatus().getId(), dto.getStatus());
		assertEquals(urls.toString(), dto.getUrls().toString());		
	}

}
