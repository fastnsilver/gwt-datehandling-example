package me.fns.gwt.datehandling.client.util;

import java.util.Date;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.i18n.shared.DateTimeFormat.PredefinedFormat;
import com.google.gwt.junit.client.GWTTestCase;

/**
 * <p>
 * Client-side tests for date manipulation.
 * </p>
 * <p>
 * See <a
 * href="http://mojo.codehaus.org/gwt-maven-plugin/user-guide/testing.html"
 * >Testing GWT code with Maven</a>.
 * </p>
 * 
 * @author cphillipson
 * 
 */
public class CSTimeUtilTestGwt extends GWTTestCase {

	private static final String MODULE_NAME = "me.fns.gwt.datehandling.Example";

	/** Standard Time, in Central Time time zone **/
	private static final String[] STD_TIMES = new String[] { "2012-12-25T01:00:00.000-06:00",
		"2012-12-25T02:00:00.000-06:00", "2012-12-25T03:00:00.000-06:00", "2012-12-25T04:00:00.000-06:00",
		"2012-12-25T05:00:00.000-06:00", "2012-12-25T06:00:00.000-06:00", "2012-12-25T07:00:00.000-06:00",
		"2012-12-25T08:00:00.000-06:00", "2012-12-25T09:00:00.000-06:00", "2012-12-25T10:00:00.000-06:00",
		"2012-12-25T11:00:00.000-06:00", "2012-12-25T12:00:00.000-06:00", "2012-12-25T13:00:00.000-06:00",
		"2012-12-25T14:00:00.000-06:00", "2012-12-25T15:00:00.000-06:00", "2012-12-25T16:00:00.000-06:00",
		"2012-12-25T17:00:00.000-06:00", "2012-12-25T18:00:00.000-06:00", "2012-12-25T19:00:00.000-06:00",
		"2012-12-25T20:00:00.000-06:00", "2012-12-25T21:00:00.000-06:00", "2012-12-25T22:00:00.000-06:00",
		"2012-12-25T23:00:00.000-06:00", "2012-12-26T00:00:00.000-06:00" };

	/**
	 * Standard Time, in Central Time time zone, no milliseconds
	 **/
	private static final String[] STD_TIMES_NO_MILLIS = new String[] { "2012-12-25T01:00:00-06:00",
		"2012-12-25T02:00:00-06:00", "2012-12-25T03:00:00-06:00", "2012-12-25T04:00:00-06:00",
		"2012-12-25T05:00:00-06:00", "2012-12-25T06:00:00-06:00", "2012-12-25T07:00:00-06:00",
		"2012-12-25T08:00:00-06:00", "2012-12-25T09:00:00-06:00", "2012-12-25T10:00:00-06:00",
		"2012-12-25T11:00:00-06:00", "2012-12-25T12:00:00-06:00", "2012-12-25T13:00:00-06:00",
		"2012-12-25T14:00:00-06:00", "2012-12-25T15:00:00-06:00", "2012-12-25T16:00:00-06:00",
		"2012-12-25T17:00:00-06:00", "2012-12-25T18:00:00-06:00", "2012-12-25T19:00:00-06:00",
		"2012-12-25T20:00:00-06:00", "2012-12-25T21:00:00-06:00", "2012-12-25T22:00:00-06:00",
		"2012-12-25T23:00:00-06:00", "2012-12-26T00:00:00-06:00" };

	/** Daylight Savings -- Fall back dates, in Central Time time zone **/
	private static final String[] DST_TO_STD_TIMES = new String[] { "2012-11-04T01:00:00.000-05:00",
		"2012-11-04T01:00:00.000-06:00", "2012-11-04T02:00:00.000-06:00", "2012-11-04T03:00:00.000-06:00",
		"2012-11-04T04:00:00.000-06:00", "2012-11-04T05:00:00.000-06:00", "2012-11-04T06:00:00.000-06:00",
		"2012-11-04T07:00:00.000-06:00", "2012-11-04T08:00:00.000-06:00", "2012-11-04T09:00:00.000-06:00",
		"2012-11-04T10:00:00.000-06:00", "2012-11-04T11:00:00.000-06:00", "2012-11-04T12:00:00.000-06:00",
		"2012-11-04T13:00:00.000-06:00", "2012-11-04T14:00:00.000-06:00", "2012-11-04T15:00:00.000-06:00",
		"2012-11-04T16:00:00.000-06:00", "2012-11-04T17:00:00.000-06:00", "2012-11-04T18:00:00.000-06:00",
		"2012-11-04T19:00:00.000-06:00", "2012-11-04T20:00:00.000-06:00", "2012-11-04T21:00:00.000-06:00",
		"2012-11-04T22:00:00.000-06:00", "2012-11-04T23:00:00.000-06:00", "2012-11-05T00:00:00.000-06:00" };

	/**
	 * Daylight Savings -- Fall back dates, in Central Time time zone, no
	 * milliseconds
	 **/
	private static final String[] DST_TO_STD_TIMES_NO_MILLIS = new String[] { "2012-11-04T01:00:00-05:00",
		"2012-11-04T01:00:00-06:00", "2012-11-04T02:00:00-06:00", "2012-11-04T03:00:00-06:00",
		"2012-11-04T04:00:00-06:00", "2012-11-04T05:00:00-06:00", "2012-11-04T06:00:00-06:00",
		"2012-11-04T07:00:00-06:00", "2012-11-04T08:00:00-06:00", "2012-11-04T09:00:00-06:00",
		"2012-11-04T10:00:00-06:00", "2012-11-04T11:00:00-06:00", "2012-11-04T12:00:00-06:00",
		"2012-11-04T13:00:00-06:00", "2012-11-04T14:00:00-06:00", "2012-11-04T15:00:00-06:00",
		"2012-11-04T16:00:00-06:00", "2012-11-04T17:00:00-06:00", "2012-11-04T18:00:00-06:00",
		"2012-11-04T19:00:00-06:00", "2012-11-04T20:00:00-06:00", "2012-11-04T21:00:00-06:00",
		"2012-11-04T22:00:00-06:00", "2012-11-04T23:00:00-06:00", "2012-11-05T00:00:00-06:00" };

	/** Daylight Savings -- Spring forward dates, in Central Time time zone **/
	private static final String[] STD_TO_DST_TIMES = new String[] { "2013-03-10T01:00:00.000-06:00",
		"2013-03-10T03:00:00.000-05:00", "2013-03-10T04:00:00.000-05:00", "2013-03-10T05:00:00.000-05:00",
		"2013-03-10T06:00:00.000-05:00", "2013-03-10T07:00:00.000-05:00", "2013-03-10T08:00:00.000-05:00",
		"2013-03-10T09:00:00.000-05:00", "2013-03-10T10:00:00.000-05:00", "2013-03-10T11:00:00.000-05:00",
		"2013-03-10T12:00:00.000-05:00", "2013-03-10T13:00:00.000-05:00", "2013-03-10T14:00:00.000-05:00",
		"2013-03-10T15:00:00.000-05:00", "2013-03-10T16:00:00.000-05:00", "2013-03-10T17:00:00.000-05:00",
		"2013-03-10T18:00:00.000-05:00", "2013-03-10T19:00:00.000-05:00", "2013-03-10T20:00:00.000-05:00",
		"2013-03-10T21:00:00.000-05:00", "2013-03-10T22:00:00.000-05:00", "2013-03-10T23:00:00.000-05:00",
	"2013-03-11T00:00:00.000-05:00" };

	/**
	 * Daylight Savings -- Spring forward dates, in Central Time time zone, no
	 * milliseconds
	 **/
	private static final String[] STD_TO_DST_TIMES_NO_MILLIS = new String[] { "2013-03-10T01:00:00-06:00",
		"2013-03-10T03:00:00-05:00", "2013-03-10T04:00:00-05:00", "2013-03-10T05:00:00-05:00",
		"2013-03-10T06:00:00-05:00", "2013-03-10T07:00:00-05:00", "2013-03-10T08:00:00-05:00",
		"2013-03-10T09:00:00-05:00", "2013-03-10T10:00:00-05:00", "2013-03-10T11:00:00-05:00",
		"2013-03-10T12:00:00-05:00", "2013-03-10T13:00:00-05:00", "2013-03-10T14:00:00-05:00",
		"2013-03-10T15:00:00-05:00", "2013-03-10T16:00:00-05:00", "2013-03-10T17:00:00-05:00",
		"2013-03-10T18:00:00-05:00", "2013-03-10T19:00:00-05:00", "2013-03-10T20:00:00-05:00",
		"2013-03-10T21:00:00-05:00", "2013-03-10T22:00:00-05:00", "2013-03-10T23:00:00-05:00",
	"2013-03-11T00:00:00-05:00" };

	private static String[] normalDayLabels = new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09",
		"10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24" };

	private static String[] shortDayLabels = new String[] { "01", "03", "04", "05", "06", "07", "08", "09", "10", "11",
		"12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24" };

	private static String[] longDayLabels = new String[] { "01", "02", "02*", "03", "04", "05", "06", "07", "08", "09",
		"10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24" };

	/**
	 * Must refer to a valid module that sources this class.
	 */
	@Override
	public String getModuleName() {
		return MODULE_NAME;
	}

	// See
	// http://codewhenbored.blogspot.com/2011/07/no-source-code-is-available-for-type.html
	// assertions restricted to JUnit 3 API

	@Test
	public void testIsoNoMillisToDate() {
		Date actual = CSTimeUtil.isoNoMillisToDate(null);
		Assert.assertNull(actual);

		final DateTimeFormat isoFmt = DateTimeFormat.getFormat(PredefinedFormat.ISO_8601);

		String expectedAsString;
		String actualAsString;
		Date expected;

		// standard time
		for (int i = 0; i < 24; i++) {
			expectedAsString = STD_TIMES[i];
			actualAsString = STD_TIMES_NO_MILLIS[i];
			expected = isoFmt.parse(expectedAsString);
			actual = CSTimeUtil.isoNoMillisToDate(actualAsString);
			Assert.assertEquals(expected, actual);
		}

		// daylight savings to standard
		for (int i = 0; i < 25; i++) {
			expectedAsString = DST_TO_STD_TIMES[i];
			actualAsString = DST_TO_STD_TIMES_NO_MILLIS[i];
			expected = isoFmt.parse(expectedAsString);
			actual = CSTimeUtil.isoNoMillisToDate(actualAsString);
			Assert.assertEquals(expected, actual);
		}

		// standard to daylight savings
		for (int i = 0; i < 23; i++) {
			expectedAsString = STD_TO_DST_TIMES[i];
			actualAsString = STD_TO_DST_TIMES_NO_MILLIS[i];
			expected = isoFmt.parse(expectedAsString);
			actual = CSTimeUtil.isoNoMillisToDate(actualAsString);
			Assert.assertEquals(expected, actual);
		}
	}

	@Test
	public void testDateToMinute() {
		final DateTimeFormat isoFmt = DateTimeFormat.getFormat(PredefinedFormat.ISO_8601);

		String actual = CSTimeUtil.dateToMinute(null);
		Assert.assertNull(actual);

		final String dateAsString1 = "2012-04-11T01:15:00.000-05:00";
		final String expected1 = "15";
		actual = CSTimeUtil.dateToMinute(isoFmt.parse(dateAsString1));
		Assert.assertEquals(expected1, actual);

		final String dateAsString2 = "2013-02-15T16:30:00.000-06:00";
		final String expected2 = "30";
		actual = CSTimeUtil.dateToMinute(isoFmt.parse(dateAsString2));
		Assert.assertEquals(expected2, actual);
	}

	@Test
	public void testDateToHour() {
		// null case
		Date date = null;
		String actual = CSTimeUtil.dateToHour(date);
		Assert.assertNull(actual);

		final DateTimeFormat isoFmt = DateTimeFormat.getFormat(PredefinedFormat.ISO_8601);

		String dateAsString;
		String expected;

		// standard time
		for (int i = 0; i < 24; i++) {
			dateAsString = STD_TIMES[i];
			date = isoFmt.parse(dateAsString);
			actual = CSTimeUtil.dateToHour(date);
			expected = normalDayLabels[i];
			Assert.assertEquals(expected, actual);
		}

		// daylight savings to standard
		for (int i = 0; i < 25; i++) {
			dateAsString = DST_TO_STD_TIMES[i];
			date = isoFmt.parse(dateAsString);
			actual = CSTimeUtil.dateToHour(date);
			expected = longDayLabels[i];
			Assert.assertEquals(expected, actual);
		}

		// standard to daylight savings
		for (int i = 0; i < 23; i++) {
			dateAsString = STD_TO_DST_TIMES[i];
			date = isoFmt.parse(dateAsString);
			actual = CSTimeUtil.dateToHour(date);
			expected = shortDayLabels[i];
			Assert.assertEquals(expected, actual);
		}

	}

	@Test
	public void testIsExtraHour() {
		final DateTimeFormat isoFmt = DateTimeFormat.getFormat(PredefinedFormat.ISO_8601);

		// 1 am (no)
		String dateAsString = "2012-11-04T01:00:00.000-05:00";
		Date date = isoFmt.parse(dateAsString);
		boolean actual = CSTimeUtil.isExtraHour(date);
		boolean expected = false;
		Assert.assertEquals(expected, actual);

		// 2 am (no)
		dateAsString = "2012-11-04T01:00:00.000-06:00";
		date = isoFmt.parse(dateAsString);
		actual = CSTimeUtil.isExtraHour(date);
		expected = false;
		Assert.assertEquals(expected, actual);

		// 2 am (again... yes!)
		dateAsString = "2012-11-04T02:00:00.000-06:00";
		date = isoFmt.parse(dateAsString);
		actual = CSTimeUtil.isExtraHour(date);
		expected = true;
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testGenerateHour() {
		final DateTimeFormat isoFmt = DateTimeFormat.getFormat(PredefinedFormat.ISO_8601);

		// January 31, 2012 11:00PM, offset +2 hours
		Date actual = CSTimeUtil.generateHour(2012, 01, 31, 23, 2);
		Date expected = isoFmt.parse("2012-02-01T01:00:00.000-06:00");
		Assert.assertEquals(expected, actual);

		// January 31, 2012 midnight, offset -2 hours
		actual = CSTimeUtil.generateHour(2012, 01, 31, 0, -2);
		expected = isoFmt.parse("2012-01-30T22:00:00.000-06:00");
		Assert.assertEquals(expected, actual);

		// December 31, 2011 midnight, offset +3 hours
		actual = CSTimeUtil.generateHour(2011, 12, 31, 23, 3);
		expected = isoFmt.parse("2012-01-01T02:00:00.000-06:00");
		Assert.assertEquals(expected, actual);

		// January 1, 2012 3:00am, offset -5 hours
		actual = CSTimeUtil.generateHour(2012, 1, 1, 3, -5);
		expected = isoFmt.parse("2011-12-31T22:00:00.000-06:00");
		Assert.assertEquals(expected, actual);

		// November 3, 2011 2:00am, offset + 6 hours
		actual = CSTimeUtil.generateHour(2011, 11, 3, 2, 6);
		expected = isoFmt.parse("2011-11-03T08:00:00.000-05:00");
		Assert.assertEquals(expected, actual);

		// November 4, 2012 1:00am, offset + 1 hour
		actual = CSTimeUtil.generateHour(2012, 11, 4, 1, 1);
		expected = isoFmt.parse("2012-11-04T01:00:00.000-06:00");
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testGenerateDay() {
		final DateTimeFormat isoFmt = DateTimeFormat.getFormat(PredefinedFormat.ISO_8601);

		// January 31, 2012 midnight, offset +3 days
		Date actual = CSTimeUtil.generateDay(2012, 01, 31, 3);
		Date expected = isoFmt.parse("2012-02-03T00:00:00.000-06:00");
		Assert.assertEquals(expected, actual);

		// January 31, 2012 midnight, offset -2 days
		actual = CSTimeUtil.generateDay(2012, 01, 31, -2);
		expected = isoFmt.parse("2012-01-29T00:00:00.000-06:00");
		Assert.assertEquals(expected, actual);

		// February 28, 2012 midnight, offset +1 day (leap year)
		actual = CSTimeUtil.generateDay(2012, 02, 28, 1);
		expected = isoFmt.parse("2012-02-29T00:00:00.000-06:00");
		Assert.assertEquals(expected, actual);

		// February 28, 2011 midnight, offset +1 day (non-leap year)
		actual = CSTimeUtil.generateDay(2011, 02, 28, 1);
		expected = isoFmt.parse("2011-03-01T00:00:00.000-06:00");
		Assert.assertEquals(expected, actual);

		// December 31, 2011 midnight, offset +3 days
		actual = CSTimeUtil.generateDay(2011, 12, 31, 3);
		expected = isoFmt.parse("2012-01-03T00:00:00.000-06:00");
		Assert.assertEquals(expected, actual);

		// January 1, 2012 midnight, offset -5 days
		actual = CSTimeUtil.generateDay(2012, 1, 1, -5);
		expected = isoFmt.parse("2011-12-27T00:00:00.000-06:00");
		Assert.assertEquals(expected, actual);

		// November 3, 2011 midnight, offset + 6 days
		actual = CSTimeUtil.generateDay(2011, 11, 3, 6);
		expected = isoFmt.parse("2011-11-09T00:00:00.000-06:00");
		Assert.assertEquals(expected, actual);

		// November 4, 2012 midnight, offset + 1 day
		actual = CSTimeUtil.generateDay(2012, 11, 4, 1);
		expected = isoFmt.parse("2012-11-05T00:00:00.000-06:00");
		Assert.assertEquals(expected, actual);

		// June 3, 2011 midnight, offset + 1 day
		actual = CSTimeUtil.generateDay(2012, 6, 3, 1);
		expected = isoFmt.parse("2012-06-04T00:00:00.000-05:00");
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testHoursInDay() {
		final DateTimeFormat isoFmt = DateTimeFormat.getFormat(PredefinedFormat.ISO_8601);

		// null
		int actual = CSTimeUtil.hoursInDay(null);
		int expected = -1;
		Assert.assertEquals(expected, actual);

		// daylight savings (regular)
		String dateAsString = "2012-11-03T00:00:00.000-05:00";
		Date date = isoFmt.parse(dateAsString);
		actual = CSTimeUtil.hoursInDay(date);
		expected = 24;
		Assert.assertEquals(expected, actual);

		// daylight savings to standard switch
		dateAsString = "2012-11-04T00:00:00.000-05:00";
		date = isoFmt.parse(dateAsString);
		actual = CSTimeUtil.hoursInDay(date);
		expected = 25;
		Assert.assertEquals(expected, actual);

		// standard to daylight savings switch
		dateAsString = "2013-03-10T00:00:00.000-06:00";
		date = isoFmt.parse(dateAsString);
		actual = CSTimeUtil.hoursInDay(date);
		expected = 23;
		Assert.assertEquals(expected, actual);

		// standard time (regular)
		dateAsString = "2013-02-15T00:00:00.000-06:00";
		date = isoFmt.parse(dateAsString);
		actual = CSTimeUtil.hoursInDay(date);
		expected = 24;
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testLabelsForDay() {
		final DateTimeFormat isoFmt = DateTimeFormat.getFormat(PredefinedFormat.ISO_8601);

		// daylight savings (regular)
		String dateAsString = "2012-11-03T00:00:00.000-05:00";
		Date date = isoFmt.parse(dateAsString);
		Set<String> actual = CSTimeUtil.labelsForDay(date);
		int expected = 24;
		Assert.assertEquals(expected, actual.size());

		// daylight savings to standard switch
		dateAsString = "2012-11-04T00:00:00.000-05:00";
		date = isoFmt.parse(dateAsString);
		actual = CSTimeUtil.labelsForDay(date);
		expected = 25;
		Assert.assertEquals(expected, actual.size());
		Assert.assertTrue(actual.contains("02*"));

		// standard to daylight savings switch
		dateAsString = "2013-03-10T00:00:00.000-06:00";
		date = isoFmt.parse(dateAsString);
		actual = CSTimeUtil.labelsForDay(date);
		expected = 23;
		Assert.assertEquals(expected, actual.size());
		Assert.assertFalse(actual.contains("02"));

		// standard time (regular)
		dateAsString = "2013-02-15T00:00:00.000-06:00";
		date = isoFmt.parse(dateAsString);
		actual = CSTimeUtil.labelsForDay(date);
		expected = 24;
		Assert.assertEquals(expected, actual.size());

		// bad data
		actual = CSTimeUtil.labelsForDay(null);
		Assert.assertNotNull(actual);
		Assert.assertTrue(actual.isEmpty());
	}

	@Test
	public void testCalculateIsoNoMillisHour() {

		String actual;
		String expected;

		// standard time
		for (int i = 0; i < 24; i++) {
			actual = CSTimeUtil.calculateIsoNoMillisHour(STD_TIMES_NO_MILLIS[i].substring(0, 10), normalDayLabels[i]);
			expected = STD_TIMES_NO_MILLIS[i];
			Assert.assertEquals(expected, actual);
		}

		// daylight savings to standard
		for (int i = 0; i < 25; i++) {
			actual = CSTimeUtil.calculateIsoNoMillisHour(DST_TO_STD_TIMES_NO_MILLIS[i].substring(0, 10),
					longDayLabels[i]);
			expected = DST_TO_STD_TIMES_NO_MILLIS[i];
			Assert.assertEquals(expected, actual);
		}

		// standard to daylight savings
		for (int i = 0; i < 23; i++) {
			actual = CSTimeUtil.calculateIsoNoMillisHour(STD_TO_DST_TIMES_NO_MILLIS[i].substring(0, 10),
					shortDayLabels[i]);
			expected = STD_TO_DST_TIMES_NO_MILLIS[i];
			Assert.assertEquals(expected, actual);
		}
	}


	@Test
	public void testCalculateIsoNoMillisInterval() {
		final String expected = "2012-11-03T20:05:00-05:00";
		final String actual = CSTimeUtil.calculateIsoNoMillisInterval("2012-11-03", "20", 5);
		Assert.assertEquals(expected, actual);
	}


	@Test
	public void testConvertIsoNoMillisToHour() {
		String actual;
		String expected;

		// standard time
		for (int i = 0; i < 24; i++) {
			actual = CSTimeUtil.convertIsoNoMillisToHour(STD_TIMES_NO_MILLIS[i]);
			expected = normalDayLabels[i];
			Assert.assertEquals(expected, actual);
		}

		// daylight savings to standard
		for (int i = 0; i < 25; i++) {
			actual = CSTimeUtil.convertIsoNoMillisToHour(DST_TO_STD_TIMES_NO_MILLIS[i]);
			expected = longDayLabels[i];
			Assert.assertEquals(expected, actual);
		}

		// standard to daylight savings
		for (int i = 0; i < 23; i++) {
			actual = CSTimeUtil.convertIsoNoMillisToHour(STD_TO_DST_TIMES_NO_MILLIS[i]);
			expected = shortDayLabels[i];
			Assert.assertEquals(expected, actual);
		}
	}

	@Test
	public void testConvertIsoNoMillisToMinute() {
		final String actual = CSTimeUtil.convertIsoNoMillisToMinute("2012-11-03T20:05:00-05:00");
		final String expected = "05";
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testYMDToDate() {
		final DateTimeFormat isoFmt = DateTimeFormat.getFormat(PredefinedFormat.ISO_8601);

		// test transition day -- daylight savings day at midnight
		String ymdString = "2012-11-04";
		Date actual = CSTimeUtil.YMDtoDate(ymdString);
		Date expected = isoFmt.parse("2012-11-04T00:00:00.000-05:00");
		Assert.assertEquals(expected, actual);

		// test another transition day -- standard time at midnight
		ymdString = "2013-03-10";
		actual = CSTimeUtil.YMDtoDate(ymdString);
		expected = isoFmt.parse("2013-03-10T00:00:00.000-05:00");
		Assert.assertEquals(expected, actual);
	}


}
