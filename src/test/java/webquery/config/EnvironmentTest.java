package webquery.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import webquery.exception.EnvironmentException;

class EnvironmentTest {
	
	private static final String BASE_URL = "http://www.company.com.br/";

	@BeforeEach
	void beforeEach() {
	}

	@Test
	void testBaseUrlNull() {
		final Environment environment = Mockito.spy(Environment.class);
		
		Mockito.doReturn(null).when(environment).getenv(Mockito.anyString());
		
		assertThrows(EnvironmentException.class, () -> environment.getBaseUrl());
	}

	@Test
	void testDefaultValues() {
		final Environment environment = Mockito.spy(Environment.class);
		
		Mockito.doReturn(null).when(environment).getenv(Mockito.any(String.class));
		Mockito.doReturn(BASE_URL).when(environment).getenv(Environment.ENV_BASE_URL); // No default value!
		
		assertEquals(Environment.DEFAULT_IGNORE_CASE, environment.isIgnoreCase());
		assertEquals(Environment.DEFAULT_MAX_RESULT, environment.getMaxResult());
		assertEquals(Environment.DEFAULT_SHOW_MESSAGES, environment.isShowMessages());
	}
	
}
