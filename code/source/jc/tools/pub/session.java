package jc.tools.pub;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.wm.app.b2b.server.InvokeState;
import com.wm.app.b2b.server.User;
// --- <<IS-END-IMPORTS>> ---

public final class session

{
	// ---( internal utility methods )---

	final static session _instance = new session();

	static session _newInstance() { return new session(); }

	static session _cast(Object o) { return (session)o; }

	// ---( server methods )---




	public static final void assignUserToSession (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(assignUserToSession)>> ---
		// @sigtype java 3.5
		// [i] field:0:required user
		IDataCursor c = pipeline.getCursor();
		String id = IDataUtil.getString(c, "user");
		c.destroy();
		
		if (id == null)
			return;
		//	id = "Administrator";
		
		InvokeState is = InvokeState.getCurrentState();
		 
		if (is != null)
		{
			String[] args = null;
		
			if (is.getAuditRuntime() != null)
			{
				User usr = new User(id);
				is.setUser(usr);
			}
		}
		// --- <<IS-END>> ---

                
	}



	public static final void get (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(get)>> ---
		// @sigtype java 3.5
		// [i] field:0:required key
		// [i] field:0:required static {"false","true"}
		// [o] object:0:required value
		// pipeline in
		
		IDataCursor pipelineCursor = pipeline.getCursor();
		String key = IDataUtil.getString(pipelineCursor, "key");
		String statc = IDataUtil.getString(pipelineCursor, "static");
		
		// process
		
		boolean isStatic = false;
		try {isStatic = Boolean.parseBoolean(statc); } catch(Exception e) {}
		
		Object value = null;
		
		if (isStatic)
			value = _staticMap.get(key);
		else
			value = Service.getSession().get(key);
		
		// pipeline out
		
		IDataUtil.put(pipelineCursor, "value", value);
		pipelineCursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void getSessionID (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(getSessionID)>> ---
		// @sigtype java 3.5
		// [o] field:0:required sessionID
		// [o] field:0:required userID
		// [o] field:0:required uniqueID
 String sessionId = null;
 String name = null;
 
    if (Service.getSession() != null) 
    {
        try {
            sessionId = Service.getSession().getSessionID();
            name = Service.getSession().getUser().getName();
        } catch (Exception e) {
            throw new RuntimeException("Unable to retrieve the session ID : " + e);
        }
    }

    IDataCursor c = pipeline.getCursor();
    IDataUtil.put(c, "sessionID", sessionId);
    
    if (name != null && !name.equals("Default") && !name.equals("webTaskUser"))
    		IDataUtil.put(c, "userID", name);
    
    	IDataUtil.put(c, "uniqueID", sessionId + getNextCount());
    c.destroy();
   
		// --- <<IS-END>> ---

                
	}



	public static final void put (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(put)>> ---
		// @sigtype java 3.5
		// [i] field:0:required key
		// [i] object:0:required value
		// [i] field:0:required static {"false","true"}
		IDataCursor pipelineCursor = pipeline.getCursor();
		String key = IDataUtil.getString(pipelineCursor, "key");
		String statc = IDataUtil.getString(pipelineCursor, "static");
		Object value = IDataUtil.getString(pipelineCursor, "value");
		
		// process
		
		boolean isStatic = false;
		try {isStatic = Boolean.parseBoolean(statc); } catch(Exception e) {}
		
		if (isStatic)
			_staticMap.put(key, value);
		else
			Service.getSession().put(key, value);
		
		// pipeline out
		
		pipelineCursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void remove (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(remove)>> ---
		// @sigtype java 3.5
		// [i] field:0:required key
		// [i] field:0:required static {"false","true"}
		// pipeline in
		
		IDataCursor pipelineCursor = pipeline.getCursor();
		String key = IDataUtil.getString(pipelineCursor, "key");
		String statc = IDataUtil.getString(pipelineCursor, "static");
		
		// process
		
		boolean isStatic = false;
		try {isStatic = Boolean.parseBoolean(statc); } catch(Exception e) {}
		
		Object value = null;
		
		if (isStatic) {
		
			if (key.equals("**ALL**"))
				_staticMap.clear();
			else
				value = _staticMap.remove(key);
		} else {
			value = Service.getSession().remove(key);
		}
		
		// pipeline out
		
		pipelineCursor.destroy();
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	
	private static Map<String, Object> _staticMap =  new HashMap<String, Object>();
	
	private static List<IData> filterOnProcessId(IData[] tasks, String processInstanceID)
	{
		List<IData> out = new ArrayList<IData>();
	
		for (int i = tasks.length-1; i >= 0; i-- )
		{
			String eProcessInstanceID = getProcessIdForTask(tasks[i]);
			
			if (eProcessInstanceID.equals(processInstanceID))
				out.add(tasks[i]);
		}	
		
		return out;
	}
	
	private static List<IData> filterOnCustomId(IData[] tasks, String customTaskID)
	{
		List<IData> out = new ArrayList<IData>();
	
		for (int i = tasks.length-1; i >= 0; i-- )
		{
			String eCustomID = getCustomIdForTask(tasks[i]);
			
			if (eCustomID.equals(customTaskID))
				out.add(tasks[i]);
		}	
		
		return out;
	}
	
	private static String getCustomIdForTask(IData task)
	{
		IDataCursor TasksCursor = task.getCursor();
		IData TaskInfo = IDataUtil.getIData(TasksCursor, "TaskInfo");
		IDataCursor TaskInfoCursor = TaskInfo.getCursor();
		String eProcessInstanceID = IDataUtil.getString(TaskInfoCursor, "customTaskID");
		TaskInfoCursor.destroy();
		TasksCursor.destroy();
		
		return eProcessInstanceID;
	}
	
	private static String getProcessIdForTask(IData task)
	{
		IDataCursor TasksCursor = task.getCursor();
		IData TaskInfo = IDataUtil.getIData(TasksCursor, "TaskInfo");
		IDataCursor TaskInfoCursor = TaskInfo.getCursor();
		String eProcessInstanceID = IDataUtil.getString(TaskInfoCursor, "processInstanceID");
		TaskInfoCursor.destroy();
		TasksCursor.destroy();
		
		return eProcessInstanceID;
	}
	
	/** 
	 * Used to identify the webMethods root context id based in runtime-attribute array
	 * returned by InvokeState. Attention this will have to be tested for each webMethods
	 * version as this is not official.
	 */
	public static final int			WM_ROOT_CONTEXT_ID_INDEX = 0;
	
	
	public static final String 		SERVER_ID_PROPERTY = "server.id";
	
	public static String WM_JDBC_ALIAS = "TaskConsole";
	public static int count = 0;
	
	private static int getNextCount()
	{
		return count++;
	}
	// --- <<IS-END-SHARED>> ---
}

