package webquery.config;

import webquery.exception.EnvironmentException;

/**
 * This class loads and provides environment variables that are used by the application
 * 
 * In some cases, if the environment variable is not found, sets a default value for the 
 * corresponding attribute.
 *  
 * @author Daniel
 */
public class Environment {

	static final String ENV_BASE_URL = "BASE_URL";
	static final String ENV_IGNORE_CASE = "IGNORE_CASE";
	static final String ENV_MAX_RESULT = "MAX_RESULT";
	static final String ENV_SHOW_MESSAGES = "SHOW_MESSAGES";

	static final boolean DEFAULT_IGNORE_CASE = true;
	static final int DEFAULT_MAX_RESULT = Integer.MAX_VALUE;
	static final boolean DEFAULT_SHOW_MESSAGES = false;
	
	private static int parseInt(String str, int defaultValue) {
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}
	
	private static boolean parseBoolean(String str, boolean defaultValue) {
		if (str == null || str.isEmpty()) {
			return defaultValue;
		} else {
			return Boolean.parseBoolean(str);
		}
	}
	
	private boolean loaded = false;
	private String baseUrl;
	private boolean ignoreCase;
	private int maxResult;
	private boolean showMessages;

	private void checkEnv() {
		if (this.baseUrl == null || this.baseUrl.isEmpty()) {
			throw new EnvironmentException("BASE_URL environment variable is empty.");
		}
		if (this.maxResult <= 0) {
			this.maxResult = DEFAULT_MAX_RESULT;
		}
	}
	
	private synchronized void load() {
		this.baseUrl = getenv(ENV_BASE_URL);
		this.ignoreCase = parseBoolean(getenv(ENV_IGNORE_CASE), DEFAULT_IGNORE_CASE);
		this.maxResult = parseInt(getenv(ENV_MAX_RESULT), DEFAULT_MAX_RESULT);
		this.showMessages = parseBoolean(getenv(ENV_SHOW_MESSAGES), DEFAULT_SHOW_MESSAGES);
		this.checkEnv();
	}
	
	private synchronized void checkLoaded() {
		if (!loaded) {
			load();
			loaded = true;
		}
	}
	
	/**
	 * Get the base search URL from the BASE_URL environment variable.
	 * @return Value of the BASE_URL environment variable or null if not found.
	 */
	public String getBaseUrl() {
		checkLoaded();
		return baseUrl;
	}

	/**
	 * Get the max search results from the MAX_RESULT environment variable as int.
	 * @return Value of the BASE_URL environment variable or Integer.MAX_VALUE if not found.
	 */
	public int getMaxResult() {
		checkLoaded();
		return maxResult;
	}

	/**
	 * Get the ignore case flag from the IGNORE_CASE environment variable as boolean.
	 * @return Value of the IGNORE_CASE environment variable or true if not found.
	 */
	public boolean isIgnoreCase() {
		checkLoaded();
		return ignoreCase;
	}

	/**
	 * Get the show messages flag from the SHOW_MESSAGES environment variable as boolean.
	 * @return Value of the SHOW_MESSAGES environment variable or false if not found.
	 */
	public boolean isShowMessages() {
		checkLoaded();
		return showMessages;
	}
	
	/**
	 * Get the value from an environment variable as string. Method implemented as public 
	 * to allow mocking in unit tests.
	 * @return Value of environment variable or null if not found.
	 */
	public String getenv(String name) {
		return System.getenv(name);
	}
	
}
