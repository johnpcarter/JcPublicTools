package jc.tools.pub.geolocation;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.ibm.icu.util.StringTokenizer;
// --- <<IS-END-IMPORTS>> ---

public final class elaborateAddress

{
	// ---( internal utility methods )---

	final static elaborateAddress _instance = new elaborateAddress();

	static elaborateAddress _newInstance() { return new elaborateAddress(); }

	static elaborateAddress _cast(Object o) { return (elaborateAddress)o; }

	// ---( server methods )---




	public static final void convertAddressDocToUrlFriendlyString (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(convertAddressDocToUrlFriendlyString)>> ---
		// @sigtype java 3.5
		// [i] recref:0:required addressDoc jc.canonicals.eu.common:Address
		// [o] field:0:required address
		// pipeline in
		
		IDataCursor pipelineCursor = pipeline.getCursor();
		IData addressDoc = IDataUtil.getIData(pipelineCursor, "addressDoc");
			
		// process
		
		String address = "";
		
		if ( addressDoc != null)
		{
			IDataCursor addressDocCursor = addressDoc.getCursor();
			String streetNo = IDataUtil.getString(addressDocCursor, "streetNo");
			String street = IDataUtil.getString(addressDocCursor, "street");
			String line2 = IDataUtil.getString(addressDocCursor, "line2");
			String line3 = IDataUtil.getString(addressDocCursor, "line3");
			String line4 = IDataUtil.getString(addressDocCursor, "line4");
			String city = IDataUtil.getString(addressDocCursor, "city");
			String postCode = IDataUtil.getString(addressDocCursor, "postCode");
			String country = IDataUtil.getString(addressDocCursor, "country");
			addressDocCursor.destroy();
			
			if (streetNo != null)
				address += replaceSpaces(streetNo);
			
			if (street != null)
				address += "+" + replaceSpaces(street);
			
			if (line2 != null)
				address += "+" + replaceSpaces(line2);
			
			if (line3 != null)
				address += "+" + replaceSpaces(line3);
			
			if (line4 != null)
				address += "+" + replaceSpaces(line4);
			
			if (city != null)
				address += "+" + replaceSpaces(city);
			
			if (postCode != null)
				address += "+" + replaceSpaces(postCode);
			
			if (country != null)
				address += "+" + replaceSpaces(country);
			
			if (address.startsWith("+"))
				address = address.substring(1);
		}
		
		// pipeline out
		
		IDataUtil.put(pipelineCursor, "address", address);
		pipelineCursor.destroy();
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	
	private static String replaceSpaces(String str)
	{
		StringTokenizer tk = new StringTokenizer(str, " ");
		String out = "";
		
		while (tk.hasMoreTokens())
		{
			out += tk.nextToken();
			
			if (tk.hasMoreTokens())
				out += "+";
		}
		
		return out;
	}
	// --- <<IS-END-SHARED>> ---
}

