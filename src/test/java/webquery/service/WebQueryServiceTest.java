package webquery.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import webquery.config.Environment;
import webquery.model.WebPage;
import webquery.model.WebQuery;
import webquery.repository.WebQueryRepository;
import webquery.service.impl.WebQueryServiceImpl;

class WebQueryServiceTest {

	private static final Long QUERY_ID = 1L;
	private static final String BASE_URL = "http://www.company.com/site/";
	private static final String PAGE1_URL = "http://www.company.com/site/page1";
	private static final String PAGE2_URL = "http://www.company.com/site/page2";
	private static final String BASE_BODY = "<html>Keyword here!</html>";
	private static final String PAGE1_BODY = "<html>keyword1</html>";
	private static final String PAGE2_BODY = "<html>others words</html>";
	private static final String KEYWORD = "keyword";
	private static final List<String> RESULT_URLS = Arrays.asList(BASE_URL, PAGE1_URL);
	private static final int QUERY_COUNT = 0;
	private static final long WAIT_FOR_SEARCH = 100;
	private static long currentId = 0;
	
	private Environment environment;
	private Map<Long, WebQuery> webQueryMap;
	private WebQueryRepository webQueryRepository;
	private WebPageService webPageService;

	private WebQueryService webQueryService;
	
	@BeforeEach
	void beforeEach() {
		
		mockEnvironment();
		mockWebQueryRepository();
		mockWebPageService();
		
		this.webQueryService = new WebQueryServiceImpl(environment, webQueryRepository, webPageService);
	}

	private void mockEnvironment() {
		this.environment = Mockito.mock(Environment.class);
		Mockito.when(this.environment.getBaseUrl()).thenReturn(BASE_URL);
		Mockito.when(this.environment.isIgnoreCase()).thenReturn(true);
		Mockito.when(this.environment.getMaxResult()).thenReturn(Integer.MAX_VALUE);
		Mockito.when(this.environment.isShowMessages()).thenReturn(false);
	}
	
	private void mockWebQueryRepository() {
		this.webQueryMap = new ConcurrentHashMap<>();
		this.webQueryRepository = Mockito.mock(WebQueryRepository.class);

		Mockito.doAnswer(invocation -> {
			final WebQuery webQuery = invocation.getArgument(0);
			if (webQuery.getId() == null) {
				webQuery.setId(currentId++);
			}
			this.webQueryMap.put(webQuery.getId(), webQuery);
			return webQuery;
		}).when(this.webQueryRepository).save(Mockito.any(WebQuery.class));
		
		doAnswer(invocation -> {
			return this.webQueryMap.get(invocation.getArgument(0));
		}).when(this.webQueryRepository).findById(Mockito.any(Long.class));
	}

	private void mockWebPageService() {
		
		this.webPageService = Mockito.mock(WebPageService.class);
		
		final WebPage baseWebPage = new WebPage(BASE_URL, BASE_BODY);
		final WebPage webPage1 = new WebPage(PAGE1_URL, PAGE1_BODY);
		final WebPage webPage2 = new WebPage(PAGE2_URL, PAGE2_BODY);
		final Set<String> urls = new HashSet<>(Arrays.asList(PAGE1_URL, PAGE2_URL));

		Mockito.when(this.webPageService.extractUrls(baseWebPage)).thenReturn(urls);
		
		Mockito.when(this.webPageService.download(BASE_URL)).thenReturn(baseWebPage);
		Mockito.when(this.webPageService.download(PAGE1_URL)).thenReturn(webPage1);
		Mockito.when(this.webPageService.download(PAGE2_URL)).thenReturn(webPage2);
	}
	
	@Test
	void testFindByIdFound() {
		
		final WebQuery webQuery = this.webQueryService.start(KEYWORD);
		final WebQuery expected = this.webQueryService.findById(webQuery.getId());
		
		assertEquals(expected, webQuery);
	}
	
	@Test
	void testFindByIdNotFound() {
		assertNull(this.webQueryService.findById(QUERY_ID));
	}
	
	@Test
	void testStartOneSearch() throws InterruptedException {
		
		final WebQuery webQuery = this.webQueryService.start(KEYWORD);
		
		Thread.sleep(WAIT_FOR_SEARCH); //NOSONAR
		
		assertNotNull(webQuery);
		assertNotNull(webQuery.getId());
		assertEquals(RESULT_URLS.toString(), webQuery.getResult().toString());
	}
	
	@Test
	void testStartManySearch() {

		final Set<WebQuery> webQueries = new HashSet<>();

		for (int i = 0; i < QUERY_COUNT; i++) {
			webQueries.add(this.webQueryService.start(KEYWORD + i));
		}
		
		assertEquals(QUERY_COUNT, webQueries.size()); // Unique IDs
		webQueries.forEach(webQuery -> {
			assertNotNull(webQuery);
			assertNotNull(webQuery.getId());
		});
		
		verify(this.webQueryRepository, times(QUERY_COUNT)).save(Mockito.any(WebQuery.class));
	}
	
}
