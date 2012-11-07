package me.fns.gwt.datehandling.client.util;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.TimeZone;
import com.google.gwt.i18n.client.constants.TimeZoneConstants;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.datepicker.client.CalendarUtil;


/**
 * <p>
 * GWT-based client-side time utility.
 * </p>
 * <p>
 * Convenience methods exist for:
 * <ul>
 * <li>Calculating an ISO no-millis hour provided a java.util.Date and a
 * pre-defined hour label</li>
 * <li>Calculating an ISO no-millis interval provided a java.util.Date, an hour,
 * and an int minute interval</li>
 * <li>Converting an ISO no-millis formatted String to a pre-defined hour label</li>
 * <li>Converting an ISO no-millis formatted String to a minute interval label</li>
 * <li>Converting a java.util.Date to a pre-defined hour label</li>
 * <li>Converting a jaa.util.Date to a pre-defined minute interval label</li>
 * <li>Generating a java.util.Date supplying int-based year, month, day and day
 * offset params</li>
 * <li>Generating a java.util.Date supplying int-based year, month, day, hour
 * and hour offset params</li>
 * <li>Determining the number of hours in a day given a java.util.Date</li>
 * <li>Determining if the current hour is an extra hour (for a "transition day"
 * -- going from Daylight Savings to Standard Time).</li>
 * <li>Converting an ISO no-millis formatted String to a java.util.Date</li>
 * <li>Generating pre-defined hour labels for a java.util.Date</li>
 * </ul>
 * .
 * </p>
 * <p>
 * Works with GWT's DateTimeFormat, TimeZone, and TimeZoneConstants
 * infrastructure to format and parse inputs.
 * </p>
 * 
 * @author cphillipson
 * 
 */
public class CSTimeUtil {

	/**
	 * Instantiates an instance of TimeZoneContants via deferred binding
	 * mechanism.
	 */
	private static final TimeZoneConstants TZ_CONSTANTS_INSTANCE = GWT.create(TimeZoneConstants.class);

	/**
	 * TimeZone instance used to coerce browser client time zone to be
	 * "Market time".
	 */
	private static final TimeZone TZ = TimeZone.createTimeZone(TZ_CONSTANTS_INSTANCE.americaChicago());

	/**
	 * ISO8601 formatter yyyy-MM-ddTHH:mm:ss.SZZZZ, where hours are represented
	 * 0-23, where 0 is midnight (12:00AM) and 23 is 11:00PM
	 */
	private static DateTimeFormat isoFormat = DateTimeFormat.getFormat("yyyy-MM-ddTHH:mm:ss.SZZZZ");

	/**
	 * Day formatter (day of month, where first day of month is 1)
	 */
	private static DateTimeFormat dayFormat = DateTimeFormat.getFormat("dd");

	/**
	 * Month formatter (where 1 = January and 12 = December)
	 */
	private static DateTimeFormat monthFormat = DateTimeFormat.getFormat("MM");

	/**
	 * Year format (4-digit representation)
	 */
	private static DateTimeFormat yearFormat = DateTimeFormat.getFormat("yyyy");

	/**
	 * Padded hour format (first hour of day is 0, last hour of day is 23)
	 */
	private static DateTimeFormat paddedHourFormat = DateTimeFormat.getFormat("HH");

	/**
	 * Padded minute format (ranged from 00-59)
	 */
	private static DateTimeFormat minuteFormat = DateTimeFormat.getFormat("mm");

	/**
	 * The number of days in each month of the year.
	 */
	private static int[] daysInMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

	/**
	 * Hour labels for a 24 hour day, regardless of Daylight Savings or Standard
	 * Time time zone offset. Hour 1 is 1:00AM, Hour 24 is 12:00AM of the
	 * following day.
	 */
	private static String[] normalDayLabels = new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09",
		"10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24" };

	/**
	 * Hour labels for 23 hour day, where day is a "transition day" from
	 * Standard Time to Daylight Savings. Hour 1 is 1:00AM, Hour 23 is 12:00AM
	 * of the following day. 2:00AM is skipped.
	 */
	private static String[] shortDayLabels = new String[] { "01", "03", "04", "05", "06", "07", "08", "09", "10", "11",
		"12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24" };

	/**
	 * Hour labels for 25 hour day, where day is a "transition day" from
	 * Daylight Savings to Standard Time. Hour 1 is 1:00AM, Hour 25 is 12:00AM
	 * of the following day. 2:00AM is repeated.
	 */
	private static String[] longDayLabels = new String[] { "01", "02", "02*", "03", "04", "05", "06", "07", "08", "09",
		"10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24" };


	/**
	 * Calculates a java.util.Date from an ISO8601 formatted String (with no
	 * millis) If the String does not contain a "GMT" prefix for the time zone
	 * offset hours it is added to the String before being parsed by
	 * <code>isoFormat</code>.
	 * 
	 * @param iso
	 *            an IS601 formatted String
	 * @return a java.util.Date
	 */
	public static Date isoNoMillisToDate(final String iso) {
		Date result = null;
		if (iso != null) {
			final String part1 = iso.substring(0, 19);
			final String part2 = iso.substring(19);
			final StringBuffer buf = new StringBuffer();
			// yyyy-MM-ddTHH:mm:ss
			buf.append(part1);
			// millis
			buf.append(".000");
			// coerce offset to be GMT +/-
			if (!part2.contains("GMT")) {
				buf.append("GMT");
			}
			buf.append(part2);
			result = isoFormat.parse(buf.toString());
		}
		return result;
	}

	/**
	 * Determines the hour label corresponding to a java.util.Date
	 * 
	 * @param date
	 *            a java.util.Date (typically a whole hour)
	 * @return an hour label where 01 represents 1:00AM and 24 represents
	 *         12:00AM of the following day
	 */
	public static String dateToHourLabel(final Date date) {
		String hour = null;
		if (date != null) {
			hour = paddedHourFormat.format(date, TZ);
			final String tz = determineTimezoneOffset(date, 0);
			if (hour != null) {
				// midnight is the 24th hour!
				if (hour.equals("00")) {
					hour = "24";
				}
				if (hour.equals("01")) {
					final Date piorHourAsDate = generateHour(date, -1);
					final String priorTz = determineTimezoneOffset(piorHourAsDate, 0);
					if (!tz.equals(priorTz)) {
						hour = "02";
					}
				}
				if (isExtraHour(date)) {
					hour = "02*";
				}
			}
		}
		return hour;
	}

	/**
	 * Determines the minute label corresponding to a java.util.Date
	 * 
	 * @param date
	 *            a java.util.Date (with minutes)
	 * @return a minute interval label where 01 represents the 5th minute and 12
	 *         represents the 0th minute of the following day
	 */
	public static String dateToMinuteLabel(final Date date) {
		String minute = null;
		if (date != null) {
			// FIXME not exactly what we need!
			minute = minuteFormat.format(date, TZ);
		}
		return minute;
	}


	/**
	 * Determines whether current hour is an "extra hour" when transitioning
	 * from Daylight Savings to Standard Time
	 * 
	 * @param date
	 *            a java.util.Date
	 * @return true if the date is a "transition day"; false otherwise
	 */
	public static boolean isExtraHour(final Date date) {
		boolean result = false;
		final Date twoHoursBefore = generateHour(date, -2);
		final Date oneHourBefore = generateHour(date, -1);
		final Date currentDate = generateHour(date, 0);

		if (TZ.isDaylightTime(twoHoursBefore) && !TZ.isDaylightTime(oneHourBefore) && !TZ.isDaylightTime(currentDate)) {
			result = true;
		}

		return result;
	}

	/**
	 * Determine an hour for a day/month/year using offset, offset can be a
	 * negative or positive number of hours
	 * 
	 * @param date
	 *            a java.util.Date
	 * @param offset
	 *            a positive of negative number of hours to increment or
	 *            decrement the date by respectively
	 * @return a java.util.Date that is the result of adding or subtracting
	 *         offset from date
	 */
	public static Date generateHour(final Date date, final int offset) {

		final int year = Integer.valueOf(yearFormat.format(date, TZ));
		final int month = Integer.valueOf(monthFormat.format(date, TZ));
		final int day = Integer.valueOf(dayFormat.format(date, TZ));
		final int hour = Integer.valueOf(paddedHourFormat.format(date, TZ));
		String tz = null;

		// This algorithm is limited... offset value must therefore be between
		// -/+ 24 hours
		if (offset > 24 || offset < -24) {
			throw new IllegalArgumentException("Offset value must be between -/+ 24 hours!");
		}

		// Note: an IllegalArgumentException will be thrown when year, month,
		// day and hour combination does not represent a real calendar day/time.

		int newYear = year;
		int newMonth = month;
		int newDay = day;
		int newHour = hour + offset;
		// yesterday
		if (newHour <= 0) {
			tz = determineTimezoneOffset(date, -1);
			newHour = 24 + newHour;
			newDay--;
			if (newDay == 0) {
				newMonth--;
				if (newMonth == 0) {
					newMonth = 12;
					newYear--;
				}
				newDay = daysInMonth[newMonth - 1];
				// Adjust for leap year
				if (newMonth == 2) {
					if (newYear % 400 == 0 || newYear % 4 == 0 && newYear % 100 != 0) {
						newDay++;
					}
				}
			}
		} else
			// same day
			if (newHour >= 0 && newHour <= 23) {
				tz = determineTimezoneOffset(date, 0);
				int numDays = daysInMonth[month - 1];
				// Adjust for leap year
				if (month == 2) {
					if (year % 400 == 0 || year % 4 == 0 && year % 100 != 0) {
						numDays++;
					}
				}
				if (newDay > numDays) {
					newDay = 1;
					newMonth++;
					if (newMonth > 12) {
						newMonth = 1;
						newYear++;
					}
				}
			} else
				// tomorrow
				if (newHour > 23) {
					tz = determineTimezoneOffset(date, 1);
					newHour = 0 + offset - 1;
					newDay++;
					int numDays = daysInMonth[month - 1];
					// Adjust for leap year
					if (month == 2) {
						if (year % 400 == 0 || year % 4 == 0 && year % 100 != 0) {
							numDays++;
						}
					}
					if (newDay > numDays) {
						newDay = 1;
						newMonth++;
						if (newMonth > 12) {
							newMonth = 1;
							newYear++;
						}
					}
				}

		final StringBuffer buf = new StringBuffer();
		buf.append(newYear).append("-").append(newMonth).append("-").append(newDay);
		buf.append("T").append(newHour).append(":00:00.000").append(tz);
		final Date newDate = isoFormat.parse(buf.toString());
		return newDate;
	}

	private static String determineTimezoneOffset(final Date date, final int days) {
		return determineTimezoneOffset(date, days, true);
	}

	@SuppressWarnings("deprecation")
	private static String determineTimezoneOffset(final Date date, final int days, final boolean addOffsetFromGMT) {
		String isoTz = null;
		if (date != null) {
			final Date copy = CalendarUtil.copyDate(date);
			copy.setDate(copy.getDate() + days);
			final StringBuffer buf = new StringBuffer();
			if (addOffsetFromGMT) {
				buf.append("GMT");
			}
			buf.append(TZ.getISOTimeZoneString(copy));
			isoTz = buf.toString();
		}
		return isoTz;
	}

	private static String tomorrowAsYMDString(final Date tomorrow) {
		final StringBuffer result = new StringBuffer();
		if (tomorrow != null) {
			result.append(yearFormat.format(tomorrow, TZ));
			result.append("-");
			result.append(monthFormat.format(tomorrow, TZ));
			result.append("-");
			result.append(dayFormat.format(tomorrow, TZ));
		}
		return result.toString();
	}

	/**
	 * Determine another day for a year, month day combo using an offset, where
	 * offset can be a negative or positive number of days
	 * 
	 * @param date
	 *            a java.util.Date
	 * @param offset
	 *            a positive of negative number of days to increment or
	 *            decrement the date by respectively
	 * @return a java.util.Date that is the result of adding or subtracting
	 *         offset from date
	 */
	public static Date generateDay(final Date date, final int offset) {

		final int year = Integer.valueOf(yearFormat.format(date, TZ));
		final int month = Integer.valueOf(monthFormat.format(date, TZ));
		final int day = Integer.valueOf(dayFormat.format(date, TZ));
		final String tz = determineTimezoneOffset(date, offset);

		// This algorithm is limited... offset value must therefore be between
		// -/+ 20 days
		if (offset > 20 || offset < -20) {
			throw new IllegalArgumentException("Offset value must be between -/+ 20 days!");
		}

		// Note: an IllegalArgumentException will be thrown when year, month,
		// and day combination does not represent a real calendar day.

		int newYear = year;
		int newMonth = month;
		int newDay = day + offset;

		if (newDay < 0) {
			newMonth--;
			if (newMonth == 0) {
				newMonth = 12;
				newYear--;
			}
			newDay = daysInMonth[newMonth - 1] + offset + 1;
			// Adjust for leap year
			if (newMonth == 2) {
				if (newYear % 400 == 0 || newYear % 4 == 0 && newYear % 100 != 0) {
					newDay++;
				}
			}
		} else {

			int numDays = daysInMonth[month - 1];
			// Adjust for leap year
			if (month == 2) {
				if (year % 400 == 0 || year % 4 == 0 && year % 100 != 0) {
					numDays++;
				}
			}
			if (newDay > numDays) {
				newDay = newDay - numDays;
				newMonth++;
				if (newMonth > 12) {
					newMonth = 1;
					newYear++;
				}
			}
		}

		final StringBuffer buf = new StringBuffer();
		buf.append(newYear).append("-").append(newMonth).append("-").append(newDay);
		buf.append("T").append("00:00:00.000").append(tz);
		final Date newDate = isoFormat.parse(buf.toString());
		return newDate;
	}

	/**
	 * Determine whether date is a 23, 24, or 25 hour day.
	 * 
	 * @param date
	 *            a java.util.Date
	 * @return 23 if date is transition from Standard Time to Daylight Savings,
	 *         24 if either a regular Standard Time or Daylight Savings date, or
	 *         25 if date is a transition from Daylight Savings to Standard Time
	 */
	public static int hoursInDay(final Date date) {
		int result = -1;
		if (date != null) {
			final Date currentDate = generateDay(date, 0);
			final Date nextDate = generateDay(date, 1);

			final Long hoursBetween = (nextDate.getTime() - currentDate.getTime()) / (60 * 60 * 1000);

			result = hoursBetween.intValue();
		}
		return result;
	}

	// Return set of pre-defined hour labels for a day
	public static Set<String> labelsForDay(final Date dt) {
		final int hoursInDay = hoursInDay(dt);
		Set<String> result = new HashSet<String>();
		if (hoursInDay == 24) {
			result = new LinkedHashSet<String>(Arrays.asList(normalDayLabels));
		} else if (hoursInDay == 23) {
			result = new LinkedHashSet<String>(Arrays.asList(shortDayLabels));
		} else if (hoursInDay == 25) {
			result = new LinkedHashSet<String>(Arrays.asList(longDayLabels));
		}
		return result;
	}

	/**
	 * Calculates the equivalent ISO8601 formatted String (no millis) for the
	 * day (at midnight) and the hour label
	 * 
	 * @param dayAtMidnight
	 *            an ISO8601 formatted String (no millis) at midnight
	 * @param hourAsString
	 *            an hour label
	 * @return an ISO8601 formatted String (no millis) representing the hour
	 */
	public static String calculateIsoNoMillisHour(final String dayAtMidnight, final String hourAsString) {
		final Date midnight = isoNoMillisToDate(dayAtMidnight);
		final int hoursInDay = hoursInDay(midnight);
		StringBuffer isoHour = new StringBuffer();
		isoHour.append(dayAtMidnight.substring(0, 10));
		isoHour.append("T");
		String hourPart;

		int hour = 1;

		String tz = determineTimezoneOffset(midnight, 0, false);
		boolean fiddleTzOffset = false;
		int offset = 0;
		if (hourAsString.contains("*")) {
			hour = Integer.valueOf(hourAsString.replace("*", ""));
			fiddleTzOffset = true;
			offset = 1;
		} else if (hoursInDay == 25 && hourAsString.equals("02")) {
			hour = 1;
			fiddleTzOffset = true;
			offset = 1;
		} else {
			hour = Integer.valueOf(hourAsString);
			if (hour == 24) {
				hour = 0;
				final Date tomorrow = generateDay(midnight, 1);
				final String tomorrowAsString = tomorrowAsYMDString(tomorrow);
				isoHour = new StringBuffer();
				isoHour.append(tomorrowAsString);
				isoHour.append("T");
				tz = determineTimezoneOffset(tomorrow, 0, false);
			}
			if (hoursInDay == 25 && hour > 2) {
				fiddleTzOffset = true;
				offset = 1;
			}
			if (hoursInDay == 23 && hour > 1) {
				fiddleTzOffset = true;
				offset = -1;
			}
		}

		if (fiddleTzOffset) {
			String tzEnhancedHr;
			final String tzSign = tz.substring(0, 1);
			final String tzMinutes = tz.substring(3);
			final int tzEnhHr = Integer.valueOf(tz.substring(1, 3)) + offset;
			if (tzEnhHr < 10) {
				tzEnhancedHr = "0" + String.valueOf(tzEnhHr);
			} else {
				tzEnhancedHr = String.valueOf(tzEnhHr);
			}
			tz = tzSign + tzEnhancedHr + tzMinutes;
		}
		final String minutesSecondsPart = ":00:00";
		if (hour < 10) {
			hourPart = "0" + String.valueOf(hour);
		} else {
			hourPart = String.valueOf(hour);
		}
		isoHour.append(hourPart);
		isoHour.append(minutesSecondsPart);
		isoHour.append(tz);
		return isoHour.toString();
	}

	/**
	 * Calculates the equivalent ISO8601 formatted String (no millis) for the
	 * day (at midnight), the hour and the minute interval
	 * 
	 * @param dayAtMidnight
	 *            an ISO8601 formatted String (no millis) at midnight
	 * @param hour
	 *            an hour (00-23)
	 * @return an ISO8601 formatted String (no millis) representing the minute
	 */
	public static String calculateIsoNoMillisInterval(final String dayAtMidnight, final String hour, final int minuteInterval) {
		final StringBuffer isoInterval = new StringBuffer();
		isoInterval.append(dayAtMidnight.substring(0, 10));
		isoInterval.append("T");
		isoInterval.append(hour);
		isoInterval.append(":");
		String minutesSecondsPart;
		if (minuteInterval < 10) {
			minutesSecondsPart = "0" + String.valueOf(minuteInterval) + ":00";
		} else {
			minutesSecondsPart = String.valueOf(minuteInterval) + ":00";
		}
		isoInterval.append(minutesSecondsPart);
		final String tz = TZ.getISOTimeZoneString(isoNoMillisToDate(dayAtMidnight));
		isoInterval.append(tz);
		return isoInterval.toString();
	}

	/**
	 * Converts an ISO8601 formatted String (no millis) into an hour label
	 * 
	 * @param isoDateTime
	 *            an ISO8601 String (no millis)
	 * @return an hour label
	 */
	public static String convertIsoNoMillisToHourLabel(final String isoDateTime) {
		String result = "";
		if (isoDateTime != null && !isoDateTime.isEmpty()) {
			final Date dateTime = CSTimeUtil.isoNoMillisToDate(isoDateTime);
			if (dateTime != null) {
				result = CSTimeUtil.dateToHourLabel(dateTime);
			}
		}
		return result;
	}

	/**
	 * Converts an ISO8601 formatted String (no millis) into a minute label
	 * 
	 * @param isoDateTime
	 *            an ISO8601 String (no millis)
	 * @return a minute label
	 */
	public static String convertIsoNoMillisToMinuteLabel(final String isoDateTime) {
		String result = "";
		if (isoDateTime != null && !isoDateTime.isEmpty()) {
			final Date dateTime = CSTimeUtil.isoNoMillisToDate(isoDateTime);
			if (dateTime != null) {
				result = CSTimeUtil.dateToMinuteLabel(dateTime);
			}
		}
		return result;
	}

}
