package jc.tools.pub;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.wm.app.b2b.server.ServerAPI;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import javax.naming.Context;
import javax.naming.NamingException;
import com.jc.wm.ldap.activedirectory.listener.DirSyncEventListener;
// --- <<IS-END-IMPORTS>> ---

public final class ldap

{
	// ---( internal utility methods )---

	final static ldap _instance = new ldap();

	static ldap _newInstance() { return new ldap(); }

	static ldap _cast(Object o) { return (ldap)o; }

	// ---( server methods )---




	public static final void deleteListenerCookie (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(deleteListenerCookie)>> ---
		// @sigtype java 3.5
		// [i] field:0:required service
		IDataCursor pipelineCursor = pipeline.getCursor();
		String service = IDataUtil.getString(pipelineCursor, "service");
				
		boolean found = false;
		
		try {
			File file = new File(ServerAPI.getPackageConfigDir("OcpContactMgmtServices").getAbsolutePath(), "ad-dirsync-cookie_" + service);
			
			if (file.exists())
			{
				Files.deleteIfExists(Paths.get(file.getAbsolutePath()));
				
				found = true; 
			}
		} catch (IOException e) {
			throw new ServiceException(e);
		}
		
		IDataCursor c = pipeline.getCursor();
		IDataUtil.put(c, "found",  found);
		c.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void registerActiveDirectoryListener (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(registerActiveDirectoryListener)>> ---
		// @sigtype java 3.5
		// [i] field:0:optional url
		// [i] field:0:optional principal
		// [i] field:0:optional credentials
		// [i] field:0:required searchBase_dn
		// [i] field:0:optional searchFilter
		// [i] field:1:required returnAttributes
		// [i] field:0:optional wmuser
		// [i] field:0:required service
		// [i] field:0:optional userIdAttributeName
		// [i] field:0:optional timeout
		// [i] field:0:required restartInterval
		// [i] field:0:required debugLevel {"Error","Warn","Info","Debug","Trace"}
		IDataCursor pipelineCursor = pipeline.getCursor();
		String url = IDataUtil.getString(pipelineCursor, "url");
		String principal = IDataUtil.getString(pipelineCursor, "principal");
		String credentials = IDataUtil.getString(pipelineCursor, "credentials");
		String searchBase = IDataUtil.getString(pipelineCursor, "searchBase_dn");
		String searchFilter = IDataUtil.getString(pipelineCursor, "searchFilter");
		String[] userReturnAttributes = IDataUtil.getStringArray(pipelineCursor, "returnAttributes");
		String service = IDataUtil.getString(pipelineCursor, "service");
		String userIdName = IDataUtil.getString(pipelineCursor, "userIdAttributeName");
		String userId = IDataUtil.getString(pipelineCursor, "wmuser");
		String timeout = IDataUtil.getString(pipelineCursor, "timeout");
		String restartIntervalStr = IDataUtil.getString(pipelineCursor, "restartInterval");
		String debugLevel = IDataUtil.getString(pipelineCursor, "debugLevel");
		String altLog = IDataUtil.getString(pipelineCursor, "altLog");
		
		pipelineCursor.destroy();
				
		// process
		
		long restartInterval = 0;
		
		try {
			restartInterval = Long.parseLong(restartIntervalStr);
		} catch(Exception e) {}; // ignore
		
		 Hashtable<String, String> env = new Hashtable<String, String>();	      
		 env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
		 env.put(Context.PROVIDER_URL, url);
		 env.put(Context.SECURITY_AUTHENTICATION,"simple");
		 env.put(Context.SECURITY_PRINCIPAL, principal);
		 env.put(Context.SECURITY_CREDENTIALS,credentials);
		 
		 if (timeout != null && Integer.parseInt(timeout) > 0)
			 env.put("com.sun.jndi.ldap.connect.timeout", timeout);
		 
		 try 
		 {	
			 DirSyncEventListener listener = _listeners.get(service);
					 
			 if (listener != null)
			 {
				 try {
					 listener.pleaseStop();
				 } catch (NamingException e) { }
			 }		
				
			 if (searchFilter == null)
				 searchFilter = SEARCH_FILTER;
			 
			 listener =  new DirSyncEventListener(env, searchBase, searchFilter, userReturnAttributes, service, userId, debugLevel);
		      
			 if (userIdName != null)
				 listener.setUserIdAttributeName(userIdName);
			 
			 if (altLog != null)
				 listener.setAltLogger(altLog);
		      
			 listener.startup(restartInterval);
		      
			 _listeners.put(service, listener);
		 }
		 catch (NamingException e) 
		 {
			 throw new ServiceException("LDAP Notification registration failure. " + e);
		 } 
		 
		// --- <<IS-END>> ---

                
	}



	public static final void stopListener (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(stopListener)>> ---
		// @sigtype java 3.5
		// [i] field:0:required service
		// [o] field:0:required found
		IDataCursor pipelineCursor = pipeline.getCursor();
		String service = IDataUtil.getString(pipelineCursor, "service");
		
		DirSyncEventListener listener = null;
		
		try 
		{	
			listener = _listeners.get(service);
					 
			if (listener != null)
				listener.pleaseStop();
				  
			_listeners.remove(service);
		}
		catch (NamingException e) 
		{
			throw new ServiceException("LDAP stop failure. " + e);
		} 
		 
		IDataUtil.put(pipelineCursor, "found", listener != null);
		pipelineCursor.destroy();
		 
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	
	private static final String	SEARCH_FILTER = "(&(objectClass=person)(objectClass=user)(cn=*))";
	
	private static Map<String, DirSyncEventListener> _listeners;
	
	static {
		_listeners = new HashMap<String, DirSyncEventListener>();
	}
	// --- <<IS-END-SHARED>> ---
}

