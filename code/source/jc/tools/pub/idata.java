package jc.tools.pub;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.wm.app.b2b.server.ServerAPI;
import com.softwareag.wx.idata2pojo.generation.GenerationException;
import com.softwareag.wx.idata2pojo.generation.IntegrationServerFacade;
import com.softwareag.wx.idata2pojo.generation.JavaCodeGenerationFacade;
import com.softwareag.wx.idata2pojo.svc.GetNodeOutputForService;
import com.softwareag.wx.idata2pojo.svc.NSNode;
import com.softwareag.wx.idata2pojo.svc.RecordField;
import com.softwareag.wx.idata2pojo.svc.ServiceNode;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
// --- <<IS-END-IMPORTS>> ---

public final class idata

{
	// ---( internal utility methods )---

	final static idata _instance = new idata();

	static idata _newInstance() { return new idata(); }

	static idata _cast(Object o) { return (idata)o; }

	// ---( server methods )---




	public static final void addDocsWithPatternToList (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(addDocsWithPatternToList)>> ---
		// @sigtype java 3.5
		// [i] record:1:required inList
		// [i] field:0:required pattern
		// [o] record:1:required outList
		// pipeline in
		IDataCursor pipelineCursor = pipeline.getCursor();
		IData[]	inList = IDataUtil.getIDataArray(pipelineCursor, "inList");
		String	pattern = IDataUtil.getString(pipelineCursor, "pattern");
		
		// process 
		
		List<IData> outList = new ArrayList<IData>();
		
		if (inList != null)
		{
			for (IData d : inList)
				outList.add(d);
		}
		
		while (pipelineCursor.hasMoreData())
		{
			pipelineCursor.next();
			String key = pipelineCursor.getKey();
			Object value = pipelineCursor.getValue();
			
			if (key.contains(pattern) && value instanceof IData)
				outList.add((IData) value);
		}
		
		// pipeline out
		
		IDataUtil.put(pipelineCursor, "outList", outList.toArray(new IData[outList.size()]));
		pipelineCursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void aggregateSameNameObjects (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(aggregateSameNameObjects)>> ---
		// @sigtype java 3.5
		// [i] field:0:required key
		// [i] record:0:optional doc
		// [o] field:1:required list
		// pipeline in
		
		IDataCursor pipelineCursor = pipeline.getCursor();
		String searchKey = IDataUtil.getString(pipelineCursor, "key");
		IData doc = IDataUtil.getIData(pipelineCursor, "doc");
		
		// process
		
		IDataCursor docCursor = pipelineCursor;
		
		if (doc != null)
			docCursor = doc.getCursor();
			
		List<String> out = new ArrayList<String>();
		
		docCursor.home();
		while(docCursor.hasMoreData())
		{
			docCursor.next();
		
			String key = docCursor.getKey();
			Object value = docCursor.getValue();
			
			if (key.equals(searchKey))
				out.add(value.toString());			
		}
		
		if (doc != null)
			docCursor.destroy();
		
		// pipeline out
		
		IDataUtil.put(pipelineCursor, "list", out.toArray(new String[out.size()]));
		
		pipelineCursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void convertDelimitedStringToDocValues (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(convertDelimitedStringToDocValues)>> ---
		// @sigtype java 3.5
		// [i] field:0:required string
		// [i] field:0:required delimiter
		// [i] record:0:optional docToUpdate
		// pipeline in
		
		IDataCursor pipelineCursor = pipeline.getCursor();
		String string = IDataUtil.getString(pipelineCursor, "string");
		String delimiter = IDataUtil.getString(pipelineCursor, "delimiter");
		IData docToUpdate = IDataUtil.getIData(pipelineCursor, "docToUpdate");
			
		IDataCursor docCursor = null;
		
		if (docToUpdate != null)
			docCursor = docToUpdate.getCursor();
		else
			docCursor = pipelineCursor;
		
		// process
		
		StringTokenizer tk = new StringTokenizer(string, delimiter);
		
		while (tk.hasMoreTokens())
		{
			String next = tk.nextToken();
			int index = next.indexOf("=");
			
			if (index != -1)
			{
				String key = next.substring(0, index).trim();
				String value = next.substring(index+1).trim();
				
				if (value.startsWith("'") || value.startsWith("\""))
					value = value.substring(1, value.length()-1);
				
				IDataUtil.put(docCursor, key, value);
			}
		}
		
		// pipeline out
		
		pipelineCursor.destroy();
			
		// --- <<IS-END>> ---

                
	}



	public static final void convertDocToDelimeteredString (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(convertDocToDelimeteredString)>> ---
		// @sigtype java 3.5
		// [i] field:0:required keyForDoc
		// [i] record:0:required docContainer
		// [i] field:0:required separator
		// [i] field:0:required arraySeparator
		// [o] field:0:required outString
		// pipeline in
		
		IDataCursor pipelineCursor = pipeline.getCursor();
		String keyForDoc = IDataUtil.getString(pipelineCursor, "keyForDoc");
		IData docContainer = IDataUtil.getIData(pipelineCursor, "docContainer");
		String separator = IDataUtil.getString(pipelineCursor, "separator");
		String arraySeparator = IDataUtil.getString(pipelineCursor, "arraySeparator");
		
		// process
		
		IData[] docs = new IData[0];
		
		if (keyForDoc != null)
		{
			IDataCursor c = docContainer.getCursor();
			docs = IDataUtil.getIDataArray(c, keyForDoc);
			c.destroy();
		}
		
		String outString = "";
		
		for (IData d : docs)
		{
			IDataCursor c = d.getCursor();
			
			while (c.hasMoreData())
			{
				c.next();
				String key = c.getKey();
				Object value = c.getValue();
				
				outString += key + ":" + value.toString() + separator;
			}
			
			c.destroy();
			
			outString += arraySeparator;
		}
		
		// pipeline out
		
		IDataUtil.put(pipelineCursor, "outString", outString);
		pipelineCursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void convertDocTypeToDocumentInstance (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(convertDocTypeToDocumentInstance)>> ---
		// @sigtype java 3.5
		// [i] field:0:required documentType
		// [i] field:0:required administratorPassword
		// [o] record:0:required doc
		// pipeline in
		
		IDataCursor pipelineCursor = pipeline.getCursor();
		String documentType = IDataUtil.getString(pipelineCursor, "documentType");
		String password = IDataUtil.getString(pipelineCursor, "administratorPassword");
		// process
		
		IData doc = null;
		try {
			doc = new Converter(ServerAPI.getServerName(), ServerAPI.getCurrentPort(), "Administrator", password).convertToDocument(documentType);
		} 
		catch (com.wm.app.b2b.client.ServiceException | GenerationException e) {
			
			throw new ServiceException(e);
		}
		
		// pipeline out
		
		IDataUtil.put( pipelineCursor, "doc", doc);
		pipelineCursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void convertNameValuePairsToDocument (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(convertNameValuePairsToDocument)>> ---
		// @sigtype java 3.5
		// [i] record:1:required attributes
		// [i] - field:0:required name
		// [i] - field:0:required value
		// [o] record:0:required document
		
		// pipeline in
		
		IDataCursor pipelineCursor = pipeline.getCursor();
		IData[]	attributes = IDataUtil.getIDataArray( pipelineCursor, "attributes" );
			
		// process
		
		IData doc = IDataFactory.create();
		IDataCursor dc = doc.getCursor();
		
		if ( attributes != null)
		{
			for ( int i = 0; i < attributes.length; i++ )
			{
				IDataCursor attributesCursor = attributes[i].getCursor();
				String	name = IDataUtil.getString( attributesCursor, "name" );
				String	value = IDataUtil.getString( attributesCursor, "value" );
				attributesCursor.destroy();
				
				IDataUtil.put(dc, name, value);
			}
		}
		
		IDataUtil.put(pipelineCursor, "document", doc);
		pipelineCursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void extractArgsFromPipeline (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(extractArgsFromPipeline)>> ---
		// @sigtype java 3.5
		// [i] field:0:required exclude
		// [o] record:0:required args
		// pipeline in
		
		IDataCursor pipelineCursor = pipeline.getCursor();
		String exclude = IDataUtil.getString( pipelineCursor, "exclude" );
		
		// process
		
		IData args = IDataFactory.create();
		IDataCursor c = args.getCursor();
		
		pipelineCursor.home();
		
		while(pipelineCursor.hasMoreData())
		{
			String key = pipelineCursor.getKey();
			Object value = pipelineCursor.getValue();
			
			if (value instanceof String && !key.equals("exclude") && (exclude == null || !key.startsWith(exclude)))
				IDataUtil.put(c, key, value);
			
			pipelineCursor.next();
		}
		c.destroy();
		
		// pipeline out
		IDataUtil.put(pipelineCursor, "args", args);
		pipelineCursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void extractCNFromResult (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(extractCNFromResult)>> ---
		// @sigtype java 3.5
		// [i] record:0:required results
		// [i] - record:0:required cn=mary
		// [o] field:0:required cn
		// [o] record:0:required cnObject
		// pipeline in
		IDataCursor pipelineCursor = pipeline.getCursor();
		IData results = IDataUtil.getIData(pipelineCursor, "results");
		
		String cnValue = null;
		IData cnObject = null;
		
		if (results != null)
		{
			IDataCursor resultsCursor = results.getCursor();
			resultsCursor.next();
			String key = resultsCursor.getKey();
			
			if (key != null)
			{
				cnObject = (IData) resultsCursor.getValue();
				cnValue = key.substring(key.indexOf("=")+1);
			}
			
			resultsCursor.destroy();
		}
		
		// pipeline out
		IDataUtil.put(pipelineCursor, "cn", cnValue);
		IDataUtil.put(pipelineCursor, "cnObject", cnObject);
		pipelineCursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void getNamedObjectFromDoc (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(getNamedObjectFromDoc)>> ---
		// @sigtype java 3.5
		// [i] field:0:required key
		// [i] record:0:required doc
		// [o] object:0:required object
		// pipeline in
		
		IDataCursor pipelineCursor = pipeline.getCursor();
		String key = IDataUtil.getString(pipelineCursor, "key");
		IData doc = IDataUtil.getIData( pipelineCursor, "doc");
		
		// process
			
		Object out = null;
				
		if ( doc != null)
		{
			IDataCursor c = doc.getCursor();
			out = IDataUtil.get(c, key);
			c.destroy();
		}
		
		// pipeline out
		IDataUtil.put(pipelineCursor, "object", out);
		pipelineCursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void injectKeyValuePairsIntoDoc (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(injectKeyValuePairsIntoDoc)>> ---
		// @sigtype java 3.5
		// [i] field:0:optional prefix
		// [i] field:0:optional valueTokenizer
		// [i] record:0:required doc
		// [i] record:1:required extraInfo
		// [i] - field:0:required key
		// [i] - field:0:required type
		// [i] - object:0:required value
		// [o] record:0:required doc
		// pipeline in
		
		IDataCursor pipelineCursor = pipeline.getCursor();
		IData doc = IDataUtil.getIData(pipelineCursor, "doc");
		IData[]	extraInfo = IDataUtil.getIDataArray(pipelineCursor, "extraInfo");
		String prefix = IDataUtil.getString(pipelineCursor, "prefix");
		String valueTokenizer = IDataUtil.getString(pipelineCursor, "valueTokenizer");
		
		if (doc == null)
			doc = IDataFactory.create();
		
		if ( extraInfo != null && doc != null)
		{
			IDataCursor docCursor = doc.getCursor();
		
			for ( int i = 0; i < extraInfo.length; i++ )
			{
				IDataCursor extraInfoCursor = extraInfo[i].getCursor();
				String key = IDataUtil.getString(extraInfoCursor, "key");
				Object value = IDataUtil.get(extraInfoCursor, "value");
				String type = IDataUtil.getString(extraInfoCursor, "type");
				extraInfoCursor.destroy();
				
				if (key != null && value != null && (prefix == null || key.startsWith(prefix)))
				{
					// remove prefix from key if present
					
					if (prefix != null)
						key = key.substring(key.indexOf(prefix) + prefix.length());
					
					if (valueTokenizer != null)
					{
						StringTokenizer tk = new StringTokenizer((String) value, valueTokenizer);
						List<String> out = new ArrayList<String>();
						
						while (tk.hasMoreTokens())
						{
							out.add(tk.nextToken());
						}
						
						if (out.size() > 1 || key.endsWith("s"))
							IDataUtil.put(docCursor, key, convertDocStringArray(out));
						else
							IDataUtil.put(docCursor, key, convertDocString(out.get(0)));
					}
					else
					{
						IDataUtil.put(docCursor, key, value);
					}
				}
			}
			
			docCursor.destroy();
		}
		
		//  pipeline out
			
		IDataUtil.put(pipelineCursor, "doc", doc);
		pipelineCursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void mergeDocumentLists (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(mergeDocumentLists)>> ---
		// @sigtype java 3.5
		// [i] record:1:required list1
		// [i] record:1:required list2
		// [i] field:0:optional uniquenessKey
		// [o] record:1:required mergedList
		// pipeline in
		
		IDataCursor pipelineCursor = pipeline.getCursor();
		IData[] list1 = IDataUtil.getIDataArray(pipelineCursor, "list1");
		IData[] list2 = IDataUtil.getIDataArray(pipelineCursor, "list2");
		
		String key = IDataUtil.getString(pipelineCursor, "uniquenessKey");
		
		// process
		
		IData[] mergedList = list1;
		
		if (list1 != null) {
			
			if (list2 != null) {
				
				List<IData> out = new ArrayList<IData>();
								
				for (int i = 0; i < list1.length;i++) {
					out.add(list1[i]);
				}
				
				for (int i = 0; i < list2.length;i++) {
					
					if (key == null || noMatch(out, list2[i], key)) {
						out.add(list2[i]);
					} else {
						// merge content
						
						out.set(i, merge(list2[i], list1[i]));
					}
				}
				
				mergedList = out.toArray(new IData[out.size()]);
			}
		} else {
			mergedList = list2;
		}
		// pipeline out
		
		IDataUtil.put(pipelineCursor, "mergedList", mergedList);
		pipelineCursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void mergeDocuments (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(mergeDocuments)>> ---
		// @sigtype java 3.5
		// [i] record:0:required doc1
		// [i] record:0:required doc2
		// [o] record:0:required mergedDocument
		// pipeline in
		
		IDataCursor pipelineCursor = pipeline.getCursor();
		IData doc1 = IDataUtil.getIData(pipelineCursor, "doc1");
		IData doc2 = IDataUtil.getIData(pipelineCursor, "doc2");
		
		// process
		
		IData mergedDocument = merge(doc1, doc2);
		
		// pipeline out
		
		IDataUtil.put(pipelineCursor, "mergedDocument", mergedDocument);
		pipelineCursor.destroy();
		
			
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	
	private static IData merge(IData doc1, IData doc2) {
				
		if (doc2 != null) {
			
			if (doc1 != null) {
				IDataCursor d1 = doc1.getCursor();
				IDataCursor d2 = doc2.getCursor();
				
				boolean hasMore = d1.first();
				while (hasMore) {
					
					String key = d1.getKey();
					Object defaultValue = d1.getValue();
					Object value = IDataUtil.get(d2, key);
										
					IDataUtil.put(d2, key, value == null ? defaultValue : value);
					
					hasMore = d1.next();
					d2.next();
				}
				
				d2.destroy();
				d1.destroy();
			}
		} else {
			return doc1;
		}
		
		return doc2;
	}
	
	private static boolean noMatch(List<IData> out, IData record, String key) {
	
		boolean match = true;
		String recordKey = getKey(record, key);
		
		for (IData r : out) {
			if (getKey(r, key).contentEquals(recordKey)) {
				match = false;
				break;
			}
		}
		
		return match;
	}
	
	private static String getKey(IData record, String key) {
	
		IDataCursor c = record.getCursor();
		String recordKey = IDataUtil.getString(c, key);
		c.destroy();
		
		return recordKey;
	}
	
	private static class Converter {
	
		private IntegrationServerFacade integrationServerFacade;
		 
		public Converter(String host, int port, String user, String pass) throws ServiceException, com.wm.app.b2b.client.ServiceException {
			
			this.integrationServerFacade = new IntegrationServerFacade(host, port, user, pass);
		}
		
		public IData convertServiceSignatureToDocuments(String serviceName) throws GenerationException {
			
			GetNodeOutputForService service = integrationServerFacade.getService(serviceName);
			
			ServiceNode serviceNode = service.getNode();
			IData input = generateDocumentForRecord(serviceNode.getSvc_sig().getSig_in());
			IData output = generateDocumentForRecord(serviceNode.getSvc_sig().getSig_out());
			
			IData out = IDataFactory.create();
			IDataCursor c = out.getCursor();
			IDataUtil.put(c, "in", input);
			IDataUtil.put(c, "out", output);
	
			c.destroy();
			
			return out;
		}
		
		public IData convertToDocument(String documentType) throws GenerationException {
			
			return generateInternal(documentType);
		}
		
		private IData generateInternal(String documentType) throws GenerationException 
		{	
			IData docForDocumentType = generateDocumentForRecord(integrationServerFacade.getDocumentType(documentType).getNode());
					
			return docForDocumentType;
		}
		
		private IData generateDocumentForRecord(NSNode record) throws GenerationException 
		{			
			IData doc = IDataFactory.create();
			IDataCursor c = doc.getCursor();
			
			for (RecordField recordField : record.getRec_fields()) 
			{
				String type = recordField.getField_type();
				String name = recordField.getField_name();
									
				if (!name.startsWith("_"))
				{
					if ("record".equals(type)) 
					{
						IDataUtil.put(c, name, makeObject(generateDocumentForRecord(recordField), type, Integer.parseInt(recordField.getField_dim())));
					} 
					else if ("recref".equals(type)) 
					{						
						IDataUtil.put(c, name, makeObject(generateInternal(recordField.getRec_ref()), type, Integer.parseInt(recordField.getField_dim())));
					} 
					else 
					{
						IDataUtil.put(c, name, makeObject(null, type, Integer.parseInt(recordField.getField_dim())));
					} 
				}
			}
			
			c.destroy();
			return doc;
		}
		
		private Object makeObject(Object obj, String type, int size)
		{
			if (obj == null)
			{
				if (size == 0)
				{
					return type;
				}
				else
				{
					String[] wrapped = new String[1];
					wrapped[0] = type;
					
					return wrapped;
				}
			}
			else // only get IData
			{
				if (size == 0)
				{
					return obj;
				}
				else
				{
					IData[] wrapped = new IData[1];
					wrapped[0] = (IData) obj;
					
					return wrapped;
				}
			}
		}
			
		private String getLocalName( String fullyQualifiedName) {
			
			if (fullyQualifiedName.indexOf(':') > -1)
				fullyQualifiedName = fullyQualifiedName.split(":")[1];
			 
			return fullyQualifiedName;
		}
	}
	
	private static Object convertDocStringArray(List<String> out)
	{
		if (out.get(0).contains("|"))
		{
			// contains doc elements
			List<IData> docs = new ArrayList<IData>();
			
			for (String s : out)
				docs.add((IData) convertDocString(s));
			
			return docs.toArray(new IData[docs.size()]);
		}
		else
		{
			// simple string list
			
			return out.toArray(new String[out.size()]);
		}
	}
	
	private static Object convertDocString(String str)
	{
		if (!str.contains("|"))
		{
			return str;
		}
		else
		{
			// convert to doc
			IData doc = IDataFactory.create();
			IDataCursor c = doc.getCursor();
			
			StringTokenizer tk = new StringTokenizer(str, "|");
			
			while (tk.hasMoreTokens())
			{
				String keyValuePair = tk.nextToken();
				int sep = keyValuePair.indexOf(":");
				
				String key = keyValuePair.substring(0,sep);
				String value = keyValuePair.substring(sep+1);
				
				IDataUtil.put(c, key, value);
			}
			
			c.destroy();
			return doc;
		}
	}
	// --- <<IS-END-SHARED>> ---
}

