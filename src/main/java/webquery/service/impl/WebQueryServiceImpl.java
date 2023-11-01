package webquery.service.impl;

import java.time.Duration;

import webquery.common.Utils;
import webquery.config.Environment;
import webquery.model.WebPage;
import webquery.model.WebQuery;
import webquery.repository.WebQueryRepository;
import webquery.service.WebPageService;
import webquery.service.WebQueryService;

/**
 * Implementation of web query service.
 * 
 * @author Daniel
 *
 */
public class WebQueryServiceImpl implements WebQueryService {

	private Environment environment;
	private WebQueryRepository webQueryRepository;
	private WebPageService webPageService;
	
	/**
	 * Create a service instance, with injected dependencies.
	 * 
	 * @param environment Injected environment object.
	 * @param webQueryRepository Injected web query repository object.
	 * @param webPageService Injected web page service object.
	 */
	public WebQueryServiceImpl(Environment environment, WebQueryRepository webQueryRepository, 
			WebPageService webPageService) {
		this.webQueryRepository = webQueryRepository;
		this.webPageService = webPageService;
		this.environment = environment;
	}

	/**
	 * Find query by ID.
	 * @param id Query ID for find.
	 * @return Query found or null if not found.
	 */
	@Override
	public WebQuery findById(Long id) {
		return webQueryRepository.findById(id);
	}
	
	/**
	 * Start a query (search) by keyword.
	 * @param keyword
	 * @return Query object of the initiated query.
	 */
	@Override
	public WebQuery start(String keyword) {

		if (environment.isShowMessages()) {
			Utils.show("Starting search for [" + keyword + "]");
		}

		keyword = environment.isIgnoreCase() ? keyword.toUpperCase() : keyword;
		final WebQuery webQuery = webQueryRepository.save(new WebQuery(keyword));

		new Thread(() -> search(webQuery)).start();
		
		return webQuery;
	}

	private void search(final WebQuery webQuery) {
		recursiveSearch(webQuery, environment.getBaseUrl());
		webQuery.markAsDone();
		if (environment.isShowMessages()) {
			final Duration duration = Duration.between(webQuery.getStartInstant(), webQuery.getDoneInstant());
			final String time = Utils.formatDuration(duration);
			Utils.show("Search for [%s] is done! Pages: %d, Results: %d, Time: %s", 
					webQuery.getKeyword(),	webQuery.getUrls().size(), webQuery.getResultCount(), time);
			if (webQuery.getResultCount() > 0) {
				Utils.show("\t[" + webQuery.getKeyword() + "] found at:");
				webQuery.getResult().forEach(url -> Utils.show("\t\t" + url));
			}
		}
	}

	private boolean canContinue(WebQuery webQuery) {
		return webQuery.getStatus() == WebQuery.Status.ACTIVE
				&& webQuery.getResultCount() < environment.getMaxResult();
	}
	
	private void recursiveSearch(WebQuery webQuery, String url) {
		
		if (environment.isShowMessages()) {
			Utils.show("Searching for [%s] at %s", webQuery.getKeyword(), url);
		}
		
		final WebPage webPage = this.webPageService.download(url);

		if (webPage == null) {
			if (environment.isShowMessages()) {
				Utils.show("Page not found: " + url);
			}
			webQuery.getUrls().put(url, false);
		} else {
			
			final boolean keywordFound = isKeywordFound(webQuery, webPage);
			
			webQuery.getUrls().put(url, keywordFound);
			if (keywordFound) {
				webQuery.incResultCount();
			}
			
			if (canContinue(webQuery)) {
				searchNextPages(webQuery, webPage);
			}
			
		}
	}

	private void searchNextPages(WebQuery webQuery, final WebPage webPage) {
		for (String extractedUrl : webPageService.extractUrls(webPage)) {
			if (canContinue(webQuery) && !webQuery.getUrls().containsKey(extractedUrl)) {
				recursiveSearch(webQuery, extractedUrl);
			}
		}
	}

	private boolean isKeywordFound(WebQuery webQuery, final WebPage webPage) {
		if (environment.isIgnoreCase()) {
			return webPage.getBody().toUpperCase().contains(webQuery.getKeyword());
		} else {
			return webPage.getBody().contains(webQuery.getKeyword());
		}
	}
}
