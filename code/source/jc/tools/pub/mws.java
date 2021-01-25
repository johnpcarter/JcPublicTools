package jc.tools.pub;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.webmethods.sc.calendar.CalendarException;
import com.webmethods.sc.calendar.CalendarSystemFactory;
import com.webmethods.sc.calendar.ICalendarManager;
import com.webmethods.sc.directory.DirectorySearchQuery;
// --- <<IS-END-IMPORTS>> ---

public final class mws

{
	// ---( internal utility methods )---

	final static mws _instance = new mws();

	static mws _newInstance() { return new mws(); }

	static mws _cast(Object o) { return (mws)o; }

	// ---( server methods )---




	public static final void queryCalendar (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(queryCalendar)>> ---
		// @sigtype java 3.5
		try {
			ICalendarManager cm = CalendarSystemFactory.getCalendarSystem().getCalendarManager();
		} catch (CalendarException e) {
			throw new ServiceException(e);
		}
		// --- <<IS-END>> ---

                
	}
}

