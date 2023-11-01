package webquery.common;

import java.time.Duration;

public class Utils {
	
	private Utils() {
		
	}
	
	/**
	 * Show message on the console.
	 * @param message Message to be displayed.
	 */
	public static void show(String message) {
		System.out.println(message); // NOSONAR
	}
	
	/**
	 * Formats the message with the passed arguments and displays it in the console.
	 * @param message Message to be formatted and displayed.
	 * @param args Arguments that will be passed to String.format(message, args).
	 */
	public static void show(String message, Object... args) {
		show(String.format(message, args));
	}
	
	/**
	 * Formats a time duration as hh:MM:ss (hh can have more than 2 digits).
	 * @param duration Duration object to format.
	 * @return Duration formatted as hh:MM:ss
	 */
	public static String formatDuration(Duration duration) {
		final long totalSeconds = duration.getSeconds();
		final long hours = totalSeconds / 3600;
		final long minutes = (totalSeconds % 3600) / 60;
		final long seconds = totalSeconds % 60;
		final String format = new StringBuilder()
				.append("%0").append(Math.max(2, String.valueOf(hours).length())).append('d')
				.append(':').append("%02d").append(':').append("%02d").toString();
		return String.format(format, hours, minutes, seconds);
	}

}
