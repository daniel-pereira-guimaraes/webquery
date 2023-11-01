package webquery.common;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class UtilsTest {
	
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	@ParameterizedTest
	@CsvSource({
			"2023-10-01 06:00:00, 2023-10-01 07:02:03, 01:02:03",
			"2023-10-01 06:00:00, 2023-10-03 07:02:03, 49:02:03",
			"2023-10-01 06:00:00, 2023-10-06 07:02:03, 121:02:03"
		})
	void testFormatDuration(String start, String end, String expected) {

		final Instant startInstant = LocalDateTime.parse(start, DATE_TIME_FORMATTER)
				.atZone(ZoneId.systemDefault()).toInstant();
		final Instant endInstant = LocalDateTime.parse(end, DATE_TIME_FORMATTER)
				.atZone(ZoneId.systemDefault()).toInstant();
		final Duration duration = Duration.between(startInstant, endInstant);
		
		final String formatted = Utils.formatDuration(duration);
		
		assertEquals(expected, formatted);
	}

}
