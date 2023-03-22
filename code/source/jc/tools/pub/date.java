package jc.tools.pub;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
// --- <<IS-END-IMPORTS>> ---

public final class date

{
	// ---( internal utility methods )---

	final static date _instance = new date();

	static date _newInstance() { return new date(); }

	static date _cast(Object o) { return (date)o; }

	// ---( server methods )---




	public static final void currentYear (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(currentYear)>> ---
		// @sigtype java 3.5
		// [o] field:0:required currentYear
		IDataCursor cursor = pipeline.getCursor();
		IDataUtil.put(cursor, "currentYear", "" + java.time.Year.now().getValue());
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void dateTimeRangeForDayDate (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(dateTimeRangeForDayDate)>> ---
		// @sigtype java 3.5
		// [i] object:0:optional date
		// [i] field:0:optional dateRange {"DAY","WEEK","MONTH","YEAR"}
		// [o] object:0:required start
		// [o] object:0:required end
		// [o] field:0:optional label
		// pipeline in
		
		IDataCursor pipelineCursor = pipeline.getCursor();
		Date date = (Date) IDataUtil.get(pipelineCursor, "date");
		String dateRange = IDataUtil.getString(pipelineCursor, "dateRange");
		
		pipelineCursor.destroy();
		
		Date start = null;
		Date end = null;
		String label = null;
		
		if (date == null)
			date = new Date();
		
		if (dateRange == null || dateRange.equalsIgnoreCase("DAY")) {
		
			start = getStartOfDay(date);
			end = getEndOfDay(date);
			
			DayOfWeek day = (dateToLocalDateTime(date)).getDayOfWeek();
		    label = day.getDisplayName(TextStyle.FULL, Locale.getDefault());
			
		} else if (dateRange.equalsIgnoreCase("WEEK")) {
			
			Date[] dates = getWeekInterval(date);
			start = dates[0];
			end = dates[1];
			
		    label = "Weekly";
			
		} else if (dateRange.equalsIgnoreCase("MONTH")) {
			
			Date[] dates = getMonthInterval(date);
			start = dates[0];
			end = dates[1];
			
			Month month = (dateToLocalDateTime(date)).getMonth();
		    label = month.getDisplayName(TextStyle.FULL, Locale.getDefault());
		    
		} else if (dateRange.equalsIgnoreCase("YEAR")) {
			
			Date[] dates = getYearInterval(date);
			start = dates[0];
			end = dates[1];
			
			label = "" + (dateToLocalDateTime(date)).getYear();
		}
		
		// pipeline out
		
		IDataUtil.put(pipelineCursor, "start", start);
		IDataUtil.put(pipelineCursor, "end", end);
		IDataUtil.put(pipelineCursor, "label", label);
		
		pipelineCursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void dateToString (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(dateToString)>> ---
		// @sigtype java 3.5
		// [i] object:0:optional date
		// [i] field:0:required pattern
		// [o] field:0:required stringDate
		// pipeline in
		
		IDataCursor pipelineCursor = pipeline.getCursor();
		Object date = IDataUtil.get(pipelineCursor, "date");
		String pattern = IDataUtil.getString(pipelineCursor, "pattern");
		
		// process
		
		if (date == null) {
			date = new Date();
		}
					
		DateFormat fmt = new SimpleDateFormat(pattern);
		String stringDate = fmt.format(date);
		
		// pipeline out
		
		IDataUtil.put(pipelineCursor, "stringDate", stringDate);
		
		
		pipelineCursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void diff (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(diff)>> ---
		// @sigtype java 3.5
		// [i] object:0:required date1
		// [i] object:0:optional date2
		// [o] field:0:required diff
		// [o] field:0:required diffInSeconds
		// pipeline in
		
		IDataCursor pipelineCursor = pipeline.getCursor();
		Date date1 = (Date) IDataUtil.get(pipelineCursor, "date1");
		Date date2 = (Date) IDataUtil.get(pipelineCursor, "date2");
		
		// process
		
		long diff = 0;
		
		if (date1 != null)
		{
			if (date2 == null)
				date2 = new Date();
			
			diff = date2.getTime() - date1.getTime();
		}
		
		// pipeline out
		
		IDataUtil.put(pipelineCursor, "diff", "" + diff);
		IDataUtil.put(pipelineCursor, "diffInSeconds", "" + diff / 1000);
		
		pipelineCursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void incrementDate (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(incrementDate)>> ---
		// @sigtype java 3.5
		// [i] object:0:optional date
		// [i] field:0:optional days
		// [i] field:0:optional hours
		// [i] field:0:optional minutes
		// [i] field:0:optional seconds
		// [o] object:0:required date
		// pipeline in
		
		IDataCursor pipelineCursor = pipeline.getCursor();
		Date date = (Date) IDataUtil.get(pipelineCursor, "date");
		String days = IDataUtil.getString(pipelineCursor, "days");
		String hours = IDataUtil.getString(pipelineCursor, "hours");
		String minutes = IDataUtil.getString(pipelineCursor, "minutes");
		String seconds = IDataUtil.getString(pipelineCursor, "seconds");
		pipelineCursor.destroy();
		
		// process
		
		int hoursInt = 0;
		int minsInt = 0;
		int secsInt = 0;
		
		try { hoursInt = Integer.parseInt(days) * 24;} catch(Exception e){}
		try { hoursInt += Integer.parseInt(hours);} catch(Exception e){}
		try { minsInt = Integer.parseInt(minutes);} catch(Exception e){}
		try { secsInt = Integer.parseInt(seconds);} catch(Exception e){}
		
		if (date == null)
			date = new Date();
		
		date = DateTimeUtils.sumTimeToDate(date, hoursInt, minsInt, secsInt);
		
		// pipeline out
		
		IDataUtil.put(pipelineCursor, "date", date);
		pipelineCursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void stringToDate (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(stringToDate)>> ---
		// @sigtype java 3.5
		// [i] field:0:required string
		// [i] field:0:required pattern
		// [o] object:0:required date
		// pipeline in
		
		IDataCursor pipelineCursor = pipeline.getCursor();
		String string = IDataUtil.getString(pipelineCursor, "string");
		String pattern = IDataUtil.getString(pipelineCursor, "pattern");
		
		// process
		
		if (pattern == null)
			throw new ServiceException("Please provide a valid date pattern");
		
		Date date = null;
		
		if (string == null)
		{
			date = new Date();
		}
		else
		{
			try {
				date = new SimpleDateFormat(pattern).parse(string);
			} catch (ParseException e) {
				throw new ServiceException(e);
			}
		}
		
		// pipeline out
		
		IDataUtil.put(pipelineCursor, "date", date);
		pipelineCursor.destroy();
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	
	public static Date[] getWeekInterval(Date date) {
		
		ZoneId defaultZoneId = ZoneId.systemDefault();
	
		final DayOfWeek firstDayOfWeek = WeekFields.of(Locale.US).getFirstDayOfWeek();
		final DayOfWeek lastDayOfWeek = DayOfWeek.of(((firstDayOfWeek.getValue() + 5) % DayOfWeek.values().length) + 1);
		
		LocalDate localDate = Instant.ofEpochMilli(date.getTime())
			      .atZone(ZoneId.systemDefault())
			      .toLocalDate();
		
		Date[] dates = new Date[2];
	
		dates[0] = Date.from(localDate.with(TemporalAdjusters.previousOrSame(firstDayOfWeek)).atStartOfDay(defaultZoneId).toInstant());
		dates[1] = Date.from(localDate.with(TemporalAdjusters.nextOrSame(lastDayOfWeek)).atStartOfDay(defaultZoneId).toInstant());
		
		return dates;
	}
	
	public static Date[] getMonthInterval(Date data) {
	
		Date[] dates = new Date[2];
	
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
	
		start.setTime(data);
		start.set(Calendar.DAY_OF_MONTH, start.getActualMinimum(Calendar.DAY_OF_MONTH));
		start.set(Calendar.HOUR_OF_DAY, 0);
		start.set(Calendar.MINUTE, 0);
		start.set(Calendar.SECOND, 0);
	
		end.setTime(data);
		end.set(Calendar.DAY_OF_MONTH, end.getActualMaximum(Calendar.DAY_OF_MONTH));
		end.set(Calendar.HOUR_OF_DAY, 23);
		end.set(Calendar.MINUTE, 59);
		end.set(Calendar.SECOND, 59);
	
	//System.out.println("start "+ start.getTime());
	//System.out.println("end   "+ end.getTime());
	
		dates[0] = start.getTime();
		dates[1] = end.getTime();
	
		return dates;
	}
	
	public static Date[] getYearInterval(Date date) {
	
		ZoneId defaultZoneId = ZoneId.systemDefault();
		
		LocalDate localDate = Instant.ofEpochMilli(date.getTime())
			      .atZone(ZoneId.systemDefault())
			      .toLocalDate();
		
		Date[] dates = new Date[2];
		
		dates[0] = Date.from(localDate.with(localDate.with(java.time.temporal.TemporalAdjusters.firstDayOfYear())).atStartOfDay(defaultZoneId).toInstant());
		dates[1] = Date.from(localDate.with(localDate.with(java.time.temporal.TemporalAdjusters.lastDayOfYear())).atTime(23, 59).toInstant(ZoneOffset.MAX));
		
		return dates;
	}
	
	public static Date getEndOfDay(Date date) 
	{
		  LocalDateTime localDateTime = dateToLocalDateTime(date);
		  LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
		  return localDateTimeToDate(endOfDay);
		}
	
		public static Date getStartOfDay(Date date) 
		{
		  LocalDateTime localDateTime = dateToLocalDateTime(date);
		  LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
		  //LocalDateTime startOfDay = localDateTime.atStartOfDay();
		  return localDateTimeToDate(startOfDay);
		}
		
		private static Date localDateTimeToDate(LocalDateTime startOfDay) 
		{
			return Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
		}
	
		private static LocalDateTime dateToLocalDateTime(Date date) 
		{
			return LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
		}
	
		public static class DateTimeUtils {
		    private static final long ONE_HOUR_IN_MS = 3600000;
		    private static final long ONE_MIN_IN_MS = 60000;
		    private static final long ONE_SEC_IN_MS = 1000;
	
		    public static Date sumTimeToDate(Date date, int hours, int mins, int secs) {
		        long hoursToAddInMs = hours * ONE_HOUR_IN_MS;
		        long minsToAddInMs = mins * ONE_MIN_IN_MS;
		        long secsToAddInMs = secs * ONE_SEC_IN_MS;
		        return new Date(date.getTime() + hoursToAddInMs + minsToAddInMs + secsToAddInMs);
		    }
		}
	// --- <<IS-END-SHARED>> ---
}

