package webquery.service;

import webquery.model.WebQuery;

/**
 * Abstract web query service.
 * 
 * @author Daniel
 *
 */
public interface WebQueryService {
	
	/**
	 * Find query by ID.
	 * @param id Query ID for find.
	 * @return Query found or null if not found.
	 */
	WebQuery findById(Long id);
	
	/**
	 * Start a query (search) by keyword.
	 * @param keyword
	 * @return Query object of the initiated query.
	 */
	WebQuery start(String keyword);
}