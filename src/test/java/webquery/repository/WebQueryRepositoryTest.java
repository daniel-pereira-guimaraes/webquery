package webquery.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import webquery.model.WebQuery;
import webquery.repository.impl.WebQueryRepositoryImpl;

class WebQueryRepositoryTest {
	
	private static final int QUERY_COUNT = 5;
	
	private WebQueryRepository queryRepository;
	
	@BeforeEach
	void beforeEach() {
		this.queryRepository = new WebQueryRepositoryImpl();
	}
	
	@Test
	void testFindByIdFound() {
		final WebQuery[] queries = new WebQuery[QUERY_COUNT];
		
		for (int i = 0; i < queries.length; i++) {
			queries[i] = this.queryRepository.save(new WebQuery(null));
		}

		for (int i = 0; i < queries.length; i++) {
			assertEquals(queries[i], this.queryRepository.findById(queries[i].getId()));
		}
	}

	@Test
	void testFindByIdNotFound() {

		final WebQuery query = this.queryRepository.save(new WebQuery(null));
		
		assertNull(this.queryRepository.findById(query.getId() + 1));
	}

}
