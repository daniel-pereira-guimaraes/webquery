package webquery.repository.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import webquery.model.WebQuery;
import webquery.repository.WebQueryRepository;

/**
 * Implementation of an in-memory query repository.
 * 
 * @author Daniel
 *
 */
public class WebQueryRepositoryImpl implements WebQueryRepository {

	private AtomicLong currentId = new AtomicLong(0);
	private Map<Long, WebQuery> queries = new ConcurrentHashMap<>();
	
	/**
	 * Save a query into repository.
	 */
	@Override
	public WebQuery save(WebQuery query) {
		
		if (query.getId() == null) {
			query.setId(currentId.incrementAndGet());
		}
		
		queries.put(query.getId(), query);
		return query;
	}

	/**
	 * Find query from repository by ID.
	 * 
	 * @return Query object or null if not found.
	 * 
	 */
	@Override
	public WebQuery findById(Long id) {
		return queries.get(id);
	}

}
