package jc.tools;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.wm.util.GlobalVariables;
import com.wm.app.b2b.server.globalvariables.GlobalVariablesManager;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import com.jc.wm.util.ServiceUtils;
import com.wm.app.b2b.server.ServerAPI;
import com.wm.app.b2b.server.ServerStartupNotifier;
// --- <<IS-END-IMPORTS>> ---

public final class _priv

{
	// ---( internal utility methods )---

	final static _priv _instance = new _priv();

	static _priv _newInstance() { return new _priv(); }

	static _priv _cast(Object o) { return (_priv)o; }

	// ---( server methods )---




	public static final void getNextUniqueCode (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(getNextUniqueCode)>> ---
		// @sigtype java 3.5
		// [i] field:0:required code
		// [i] field:0:optional prefix
		// [i] field:0:required numDigits
		// [i] field:0:required numeric {"true","false"}
		// [o] field:0:required code
		IDataCursor c = pipeline.getCursor();
		String code = IDataUtil.getString(c, "code");
		String numDigits = IDataUtil.getString(c, "numDigits");
		String prefix = IDataUtil.getString(c, "prefix");
		String numeric = IDataUtil.getString(c, "numeric");
		
		String nextCode;
		
		if (numeric != null && numeric.equalsIgnoreCase("true"))
			nextCode = rotateNums(code, numDigits != null ? Integer.parseInt(numDigits) : 5);
		else
			nextCode = rotateChars(code, numDigits != null ? Integer.parseInt(numDigits) : 5);
		
		if (prefix != null)
			nextCode = prefix + nextCode;
		
		IDataUtil.put(c, "code", nextCode);
		
		c.destroy();
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	
	static String zeros = "000000";
	
	public static synchronized String rotateNums(String code, int numDigits)
	{
		int n = 0;
		
		if (code != null) {
			try { n = Integer.parseInt(code) + 1; } catch(Exception e) {}
		}
						
		String num = "" + n;
					
		if (num.length() > numDigits) {
			num = "0";
		}
		
		if (numDigits - num.length() > 0)
			num = zeros.substring(0, numDigits - num.length()) + num;
			
		return num;
	}
			
	public static synchronized String rotateChars(String code, int numDigits)
	{
		char[] chars = new char[numDigits];
		
		if (code != null) {		
			for (int i = 0; i < numDigits; i++) {
				if (i < code.length())
					chars[i] = code.charAt(i);
				else
					chars[i] = 'A';
			}
		}
		
		if (chars[4]++ == 'Z')
		{
			chars[4] = 'A';
			chars[3] += 1;
		}
		
		if (numDigits >= 1 && chars[3] == 'Z')
		{
			chars[3] = 'A';
			chars[2] += 1;
		}
		
		if (numDigits > 2 && chars[2] == 'Z')
		{
			chars[2] = 'A';
			chars[1] += 1;
		}
		
		if (numDigits > 3 && chars[1] == 'Z')
		{
			chars[1] = 'A';
			chars[0] += 1;
		}
		
		if (numDigits > 4 && chars[0] == 'Z')
		{
			// reset
			
			chars[0] = 'A';
			chars[1] = 'A';
			chars[2] = 'A';
			chars[3] = 'A';
			chars[4] = 'A';
		}
	
		if (numDigits >= 5)
			return "" + chars[0] + chars[1] + chars[2] + chars[3] + chars[4];
		else if (numDigits >= 4)
			return "" + chars[1] + chars[2] + chars[3] + chars[4];
		else if (numDigits >= 3)
			return "" + chars[2] + chars[3] + chars[4];
		else
			return "" + chars[3] + chars[4];
	}
		
	// --- <<IS-END-SHARED>> ---
}

