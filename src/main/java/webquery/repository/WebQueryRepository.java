package webquery.repository;

import webquery.model.WebQuery;

/**
 * Abstract query repository.
 * 
 * @author Daniel
 *
 */
public interface WebQueryRepository {

	WebQuery save(WebQuery query);
	WebQuery findById(Long id);

}