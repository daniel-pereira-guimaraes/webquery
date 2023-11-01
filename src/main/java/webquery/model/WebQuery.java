package webquery.model;

import java.time.Instant;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Representation of a web query.
 * @author Daniel
 */
public class WebQuery {
	
	/**
	 * Query status (active or done)
	 * 
	 * @author Daniel
	 */
	public enum Status {
		
		ACTIVE("active"),
		DONE("done");
		
		private String id;

		Status(String id) {
			this.id = id;
		}

		/**
		 * Get status as string.
		 * @return Status ID ("active" for ACTIVE e "done" for DONE status).
		 */
		public synchronized String getId() {
			return this.id;
		}
		
	}
	
	private Long id;
	private String keyword;
	private Status status = Status.ACTIVE;
	private Map<String, Boolean> urls = new ConcurrentHashMap<>();
	private long resultCount = 0;
	private Instant startInstant = Instant.now();
	private Instant doneInstant;

	/**
	 * Create an WebQuery instance with ID and keyword.
	 * @param id Query ID.
	 * @param keyword Keyword for search.
	 */
	public WebQuery(Long id, String keyword) {
		this.id = id;
		this.keyword = keyword;
	}

	/**
	 * Create an WebQuery instance with keyword.
	 * @param keyword Keyword for search.
	 */
	public WebQuery(String keyword) {
		this.keyword = keyword;
	}
	
	/**
	 * Set the query ID.
	 * @param id Query ID.
	 */
	public synchronized void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * Get the query ID.
	 * @return Query ID.
	 */
	public synchronized Long getId() {
		return id;
	}
	
	/**
	 * Get the query keyword.
	 * @return Query keyword.
	 */
	public synchronized String getKeyword() {
		return keyword;
	}

	/**
	 * Get the query status.
	 * @return Query status (ACTIVE or DONE).
	 */
	public synchronized Status getStatus() {
		return status;
	}
	
	@Override
	public synchronized int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public synchronized boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final WebQuery other = (WebQuery) obj;
		return Objects.equals(id, other.id);
	}

	/**
	 * Gets a map that contains the searched URLs and their respective flag 
	 * that indicates whether the keyword was found on the page.
	 * @return Map with URLs and keyword flag found.
	 */
	public synchronized Map<String, Boolean> getUrls() {
		return urls;
	}

	/**
	 * Increments the page counter with the searched keyword.
	 */
	public synchronized void incResultCount() {
		this.resultCount++;
	}
	
	/**
	 * Gets the number of searched pages that contain the searched keyword.
	 * @return Number of pages with the keyword
	 */
	public synchronized long getResultCount() {
		return resultCount;
	}

	/**
	 * Gets only the URLs where the keyword was found.
	 * @return Collection of URLs of pages containing the keyword, in alphabetical order.
	 */
	public synchronized Collection<String> getResult() {
		return this.getUrls().entrySet().stream()
				.filter(Entry::getValue)
				.map(Entry::getKey)
				.sorted()
				.collect(Collectors.toList());
	}

	/**
	 * Gets the start time of the query.
	 * @return Query start time.
	 */
	public synchronized Instant getStartInstant() {
		return startInstant;
	}

	/**
	 * Gets the done time of the query.
	 * @return Query done time.
	 */
	public synchronized Instant getDoneInstant() {
		return doneInstant;
	}
	
	/**
	 * Change query status to DONE and set the done time.
	 */
	public synchronized void markAsDone() {
		this.status = Status.DONE;
		this.doneInstant = Instant.now();
	}

}
