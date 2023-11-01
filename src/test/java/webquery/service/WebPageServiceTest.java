package webquery.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

import webquery.config.Environment;
import webquery.model.WebPage;
import webquery.service.impl.WebPageServiceImpl;

class WebPageServiceTest {
	
	private static final char SINGLE_QUOTE = '\'';
	private static final char DOUBLE_QUOTE = '"';
	private static final String HTTP = "http://";
	private static final String HTTPS = "https://";
	private static final String BASE_URL = HTTP + "www.company.com/site/";
	private static final String PAGE_URL = HTTP + "www.company.com/site/page.html";
	private static final String DOWNLOAD_URL = "https://www.google.com.br/";
	private static final List<String> DOWNLOAD_CONTENTS = Arrays.asList("<html", "</html>"); 
	
	private static final String PAGE_HTML = ""
			+ "<!DOCTYPE html> "
			+ "<html>"
			+ "  <head>" 
			+ "    <title>Tests</title>"
			+ "  </head>"
			+ "  <body>"
			+ "    <h1>Links examples</h1>"
			+ "    <p>Here are some examples of links:</p>"
			+ "    <a href='/'>Relative1</a><br>"
			+ "    <a href='/page1.html'>Relative2</a><br>"
			+ "    <a href='page2.html'>Relative3</a><br>"
			+ "    <a href='page3.html?id=123&name=Jo%C3%A3o'>With parameters</a><br>"
			+ "    <a href='http://www.company.com/site/page4'>Absolute1</a><br>"
			+ "    <a href='http://www.company.com/site/page5/'>Absolute2</a><br>"
			+ "    <a href='http://www.google.com'>External</a><br>"
			+ "  </body>"
			+ "</html>";

	private static final List<String> INTERNAL_URLS = Arrays.asList(
			"http://www.company.com/site/page2.html",
			"http://www.company.com/site/page3.html?id=123&name=Jo%C3%A3o",
			"http://www.company.com/site/page4",
			"http://www.company.com/site/page5/");
	
	private String fixQuoted(String url, boolean doubleQuote) {
		return doubleQuote ? url.replace(SINGLE_QUOTE, DOUBLE_QUOTE) 
				: url.replace(DOUBLE_QUOTE, SINGLE_QUOTE);
	}
	
	private String fixProtocol(String url, boolean https) {
		return https ? url.replace(HTTP, HTTPS) : url.replace(HTTPS, HTTP);
	}
	
	@ParameterizedTest
	@CsvSource({"false,false", "false,true", "true,false", "true,true"})
	void testExtractUrls(boolean doubleQuote, boolean https) {

		final Environment environment = Mockito.mock(Environment.class);
		Mockito.when(environment.getBaseUrl()).thenReturn(fixProtocol(BASE_URL, https));

		final WebPageService webPageService = new WebPageServiceImpl(environment);
		final String html = fixProtocol(fixQuoted(PAGE_HTML, doubleQuote), https);
		final String pageURL = fixProtocol(PAGE_URL, https);
		final WebPage webPage = new WebPage(pageURL, html);
		final List<String> expectedUrls = INTERNAL_URLS.stream().sorted()
				.map(s -> fixProtocol(s, https)).collect(Collectors.toList());
		
		final List<String> extractedUrls = webPageService.extractUrls(webPage).stream()
				.sorted().collect(Collectors.toList());

		for (int i = 0; i < expectedUrls.size(); i++) {
			assertEquals(expectedUrls.get(i), extractedUrls.get(i));
		}
	}
	
	@Test
	@Disabled("Don't run integration tests")
	void testDownloadOk() throws MalformedURLException, IOException {
		
		final Environment environment = Mockito.mock(Environment.class);
		Mockito.when(environment.getBaseUrl()).thenReturn(BASE_URL);
		
		final WebPageService webPageService = new WebPageServiceImpl(environment);

		final WebPage webPage = webPageService.download(DOWNLOAD_URL);

		assertNotNull(webPage);
		DOWNLOAD_CONTENTS.forEach(str -> {
			assertTrue(webPage.getBody().contains(str));
		});
	}
	
	@Test
	void testDownloadWithException() throws MalformedURLException, IOException {
		final Environment environment = Mockito.mock(Environment.class);
		Mockito.when(environment.getBaseUrl()).thenReturn(BASE_URL);
		
		final WebPageService webPageService = new WebPageServiceImpl(environment);
		
		assertNull(webPageService.download("wrong url"));
	}
	
}
