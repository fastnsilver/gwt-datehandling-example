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

	private static final TimeZoneConstants TZ_CONSTANTS_INSTANCE = GWT.create(TimeZoneConstants.class);

	// Caution: client's time zone is coerced to be whatever is specified here
	private static final TimeZone TZ = TimeZone.createTimeZone(TZ_CONSTANTS_INSTANCE.americaChicago());

	// yyyy-MM-ddTHH:mm:ss.SZZZZ, where hours are represented 0-23, where 0 is
	// midnight (12:00AM) and 23 is 11:00PM
	private static DateTimeFormat isoFormat = DateTimeFormat.getFormat("yyyy-MM-ddTHH:mm:ss.SZZZZ");

	private static DateTimeFormat isoNoTzFormat = DateTimeFormat.getFormat("yyyy-MM-ddTHH:mm:ss.S");

	private static DateTimeFormat dayFormat = DateTimeFormat.getFormat("dd");

	private static DateTimeFormat monthFormat = DateTimeFormat.getFormat("MM");

	private static DateTimeFormat yearFormat = DateTimeFormat.getFormat("yyyy");

	private static DateTimeFormat paddedHourFormat = DateTimeFormat.getFormat("HH");

	private static DateTimeFormat minuteFormat = DateTimeFormat.getFormat("mm");

	private static int[] daysInMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

	private static String[] normalDayLabels = new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09",
		"10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24" };

	private static String[] shortDayLabels = new String[] { "01", "03", "04", "05", "06", "07", "08", "09", "10", "11",
		"12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24" };

	private static String[] longDayLabels = new String[] { "01", "02", "02*", "03", "04", "05", "06", "07", "08", "09",
		"10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24" };


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

	public static String dateToHour(final Date date) {
		String hour = null;
		if (date != null) {
			hour = paddedHourFormat.format(date, TZ);
			if (hour != null) {
				// midnight is the 24th hour!
				if (hour.equals("00")) {
					hour = "24";
				}
				if (hoursInDay(date) == 25 && hour.equals("01") && !TZ.isDaylightTime(date)) {
					hour = "02";
				}
				if (isExtraHour(date)) {
					hour = "02*";
				}
			}
		}
		return hour;
	}

	// FIXME not exactly what we need!
	public static String dateToMinute(final Date date) {
		String minute = null;
		if (date != null) {
			minute = minuteFormat.format(date, TZ);
		}
		return minute;
	}


	// determine whether current hour is an extra hour when transitioning from
	// Daylight Savings to Standard Time
	public static boolean isExtraHour(final Date date) {
		boolean result = false;

		final int year = Integer.valueOf(yearFormat.format(date, TZ));
		final int month = Integer.valueOf(monthFormat.format(date, TZ));
		final int day = Integer.valueOf(dayFormat.format(date, TZ));
		final int hour = Integer.valueOf(paddedHourFormat.format(date, TZ));
		final String tz = "GMT" + TZ.getISOTimeZoneString(date);

		final Date twoHoursBefore = generateHour(year, month, day, hour, tz, -2);
		final Date oneHourBefore = generateHour(year, month, day, hour, tz, -1);
		final Date currentDate = generateHour(year, month, day, hour, tz, 0);

		if (TZ.isDaylightTime(twoHoursBefore) && !TZ.isDaylightTime(oneHourBefore) && !TZ.isDaylightTime(currentDate)) {
			result = true;
		}

		return result;
	}

	// Determine an hour for a day/month/year using offset, offset can be a
	// negative or positive number of hours
	public static Date generateHour(final int year, final int month, final int day, final int hour, final String tz,
			final int offset) {

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

	private static Date calculateTomorrow(final Date currentDay) {
		Date tomorrow = null;
		if (currentDay != null) {
			final int year = Integer.valueOf(yearFormat.format(currentDay, TZ));
			final int month = Integer.valueOf(monthFormat.format(currentDay, TZ));
			final int day = Integer.valueOf(dayFormat.format(currentDay, TZ));
			tomorrow = generateDay(year, month, day, 1);
		}
		return tomorrow;
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

	// Determine another day for a year, month day combo using an offset, where
	// offset can be a negative or positive number of days
	public static Date generateDay(final int year, final int month, final int day, final int offset) {

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
		buf.append("T").append("00:00:00.000");
		final Date newDate = isoNoTzFormat.parse(buf.toString());
		return newDate;
	}

	// Determine whether 23,24, or 25 hour day.
	public static int hoursInDay(final Date date) {
		int result = -1;
		if (date != null) {
			final int year = Integer.valueOf(yearFormat.format(date, TZ));
			final int month = Integer.valueOf(monthFormat.format(date, TZ));
			final int day = Integer.valueOf(dayFormat.format(date, TZ));

			// make sure that current day starts at midnight for whatever hour
			// was passed in
			final Date currentDate = generateDay(year, month, day, 0);
			final Date nextDate = generateDay(year, month, day, 1);

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

	public static String calculateIsoNoMillisHour(final String dayAtMidnight, final String hourAsString) {
		final Date midnight = isoNoMillisToDate(dayAtMidnight);
		final int hoursInDay = hoursInDay(midnight);
		StringBuffer isoHour = new StringBuffer();
		isoHour.append(dayAtMidnight.substring(0, 10));
		isoHour.append("T");
		String hourPart;

		int hour = 1;

		String tz = TZ.getISOTimeZoneString(midnight);
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
				final Date tomorrow = calculateTomorrow(midnight);
				final String tomorrowAsString = tomorrowAsYMDString(tomorrow);
				isoHour = new StringBuffer();
				isoHour.append(tomorrowAsString);
				isoHour.append("T");
				tz = TZ.getISOTimeZoneString(tomorrow);
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

	public static String convertIsoNoMillisToHour(final String isoDateTime) {
		String result = "";
		if (isoDateTime != null && !isoDateTime.isEmpty()) {
			final Date dateTime = CSTimeUtil.isoNoMillisToDate(isoDateTime);
			if (dateTime != null) {
				result = CSTimeUtil.dateToHour(dateTime);
			}
		}
		return result;
	}

	public static String convertIsoNoMillisToMinute(final String isoDateTime) {
		String result = "";
		if (isoDateTime != null && !isoDateTime.isEmpty()) {
			final Date dateTime = CSTimeUtil.isoNoMillisToDate(isoDateTime);
			if (dateTime != null) {
				result = CSTimeUtil.dateToMinute(dateTime);
			}
		}
		return result;
	}

}