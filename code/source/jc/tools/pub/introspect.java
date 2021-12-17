package jc.tools.pub;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.jc.wm.introspection.PackageIntrospector;
// --- <<IS-END-IMPORTS>> ---

public final class introspect

{
	// ---( internal utility methods )---

	final static introspect _instance = new introspect();

	static introspect _newInstance() { return new introspect(); }

	static introspect _cast(Object o) { return (introspect)o; }

	// ---( server methods )---




	public static final void apisForPackage (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(apisForPackage)>> ---
		// @sigtype java 3.5
		// [i] field:0:required packageName
		// [i] field:0:optional details {"false","true"}
		// [o] field:1:optional apis
		// [o] record:1:optional apis
		// pipeline in
		
		IDataCursor pipelineCursor = pipeline.getCursor();
		String packageName = IDataUtil.getString(pipelineCursor, "packageName");
		String details = IDataUtil.getString(pipelineCursor, "details");
		pipelineCursor.destroy();
		
		// pipeline out
		
		if (details != null && details.equalsIgnoreCase("true"))
			IDataUtil.put(pipelineCursor, "apis", PackageIntrospector.defaultInstance("default").apiDetailsForPackage(packageName));
		else
			IDataUtil.put(pipelineCursor, "apis", PackageIntrospector.defaultInstance("default").apiReferencesForPackage(packageName));
		pipelineCursor.destroy();
		
			
		// --- <<IS-END>> ---

                
	}



	public static final void packageDetails (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(packageDetails)>> ---
		// @sigtype java 3.5
		// [i] field:0:optional name
		// [o] record:1:required packages
		// [o] - field:0:required name
		// [o] - field:0:required version
		// [o] - field:0:required build
		// [o] - field:0:optional description
		// [o] - field:0:optional startup
		// [o] - field:0:optional shutdown
		// [o] - field:0:required testStatus
		// pipeline in
		
		IDataCursor pipelineCursor = pipeline.getCursor();
		String packageName = IDataUtil.getString(pipelineCursor, "name");
		
		pipelineCursor.destroy();
				
		IData[] packages = null;
		
		if (packageName == null) {
			packages = PackageIntrospector.defaultInstance("default").packageDetails();
		} else {
			packages = new IData[1];
			packages[0] = PackageIntrospector.defaultInstance("default").packageInfo(packageName).toIData(true, true, null);
		}
		
		// pipeline out
				
		IDataUtil.put(pipelineCursor, "packages", packages);
		pipelineCursor.destroy();
			
		// --- <<IS-END>> ---

                
	}



	public static final void packageList (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(packageList)>> ---
		// @sigtype java 3.5
		// [i] field:0:optional rescan {"false","true"}
		// [o] field:1:required packages
		// pipeline in
		
		IDataCursor pipelineCursor = pipeline.getCursor();
		String rescan = IDataUtil.getString(pipelineCursor, "rescan");
				
		String[] packages = PackageIntrospector.defaultInstance("default", "./packages", null, rescan != null && rescan.equalsIgnoreCase("true")).packages();
				
		// pipeline out
				
		IDataUtil.put(pipelineCursor, "packages", packages);
		pipelineCursor.destroy();
			
		// --- <<IS-END>> ---

                
	}



	public static final void rescan (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(rescan)>> ---
		// @sigtype java 3.5
		PackageIntrospector.defaultInstance("default", "/packages", null, true);
			
		// --- <<IS-END>> ---

                
	}



	public static final void servicesForPackage (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(servicesForPackage)>> ---
		// @sigtype java 3.5
		// [i] field:0:required name
		// [o] field:1:required services
		// pipeline in
		
		IDataCursor pipelineCursor = pipeline.getCursor();
		String packageName = IDataUtil.getString(pipelineCursor, "name");
		pipelineCursor.destroy();
		
		String[] services = PackageIntrospector.defaultInstance("default").servicesForPackage(packageName);
		
		// pipeline out
		
		IDataUtil.put(pipelineCursor, "services", services);
		pipelineCursor.destroy();
		
			
		// --- <<IS-END>> ---

                
	}
}

