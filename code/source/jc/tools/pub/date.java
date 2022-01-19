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
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
// --- <<IS-END-IMPORTS>> ---

public final class date

{
	// ---( internal utility methods )---

	final static date _instance = new date();

	static date _newInstance() { return new date(); }

	static date _cast(Object o) { return (date)o; }

	// ---( server methods )---




	public static final void dateTimeRangeForDayDate (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(dateTimeRangeForDayDate)>> ---
		// @sigtype java 3.5
		// [i] object:0:required date
		// [o] object:0:required dateTimeAtStartOfDate
		// [o] object:0:required dateTimeAtEndOfDate
		// pipeline in
		
		IDataCursor pipelineCursor = pipeline.getCursor();
		Date date = (Date) IDataUtil.get(pipelineCursor, "date");
		pipelineCursor.destroy();
		
		Date start = null;
		Date end = null;
		
		if (date == null)
			date = new Date();
		
		start = getStartOfDay(date);
		end = getEndOfDay(date);
		
		// pipeline out
		
		IDataUtil.put(pipelineCursor, "dateTimeAtStartOfDate", start);
		IDataUtil.put(pipelineCursor, "dateTimeAtEndOfDate", end);
		pipelineCursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void dateToOptimizeDateTimeString (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(dateToOptimizeDateTimeString)>> ---
		// @sigtype java 3.5
		// [i] object:0:required date
		// [o] field:0:required dateString
		// pipeline in
		
		IDataCursor pipelineCursor = pipeline.getCursor();
		Date date = (Date) IDataUtil.get(pipelineCursor, "date");
		pipelineCursor.destroy();
		
		// process
		
		if (date == null)
			date = new Date();
		
		String dateString = "";
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		dateString = formatter.format(date) + "T";
		
		formatter = new SimpleDateFormat("HH:mm:ss");
		dateString += formatter.format(date) + "%2B0000";
		
		dateString = dateString.replaceAll(":", "%3A");
		// pipeline out
		
		IDataUtil.put(pipelineCursor, "dateString", dateString);
		pipelineCursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void dateToString (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(dateToString)>> ---
		// @sigtype java 3.5
		// [i] object:0:required date
		// [i] field:0:required pattern
		// [o] field:0:required stringDate
		// pipeline in
		
		IDataCursor pipelineCursor = pipeline.getCursor();
		Object date = IDataUtil.get(pipelineCursor, "date");
		String pattern = IDataUtil.getString(pipelineCursor, "pattern");
		
		// process
		
		if (date != null) {
					
			DateFormat fmt = new SimpleDateFormat(pattern);
			String stringDate = fmt.format(date);
		
		// pipeline out
		
			IDataUtil.put(pipelineCursor, "stringDate", stringDate);
		}
		
		pipelineCursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void diff (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(diff)>> ---
		// @sigtype java 3.5
		// [i] object:0:required date1
		// [i] object:0:required date2
		// [o] field:0:required diff
		// pipeline in
		
		IDataCursor pipelineCursor = pipeline.getCursor();
		Date date1 = (Date) IDataUtil.get(pipelineCursor, "date1");
		Date date2 = (Date) IDataUtil.get(pipelineCursor, "date2");
		
		// process
		
		long diff = 0;
		
		if (date1 != null && date2 != null)
		{
			diff = date2.getTime() - date1.getTime();
		}
		
		// pipeline out
		
		IDataUtil.put(pipelineCursor, "diff", "" + diff);
		pipelineCursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void incrementDate (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(incrementDate)>> ---
		// @sigtype java 3.5
		// [i] object:0:required date
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

