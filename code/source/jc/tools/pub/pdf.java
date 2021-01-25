package jc.tools.pub;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import org.w3c.tidy.Tidy;
import org.apache.avalon.framework.logger.ConsoleLogger;
import org.apache.fop.apps.Driver;
import org.apache.fop.tools.DocumentInputSource;
import org.w3c.dom.Document;
import org.apache.avalon.framework.logger.Logger;
// --- <<IS-END-IMPORTS>> ---

public final class pdf

{
	// ---( internal utility methods )---

	final static pdf _instance = new pdf();

	static pdf _newInstance() { return new pdf(); }

	static pdf _cast(Object o) { return (pdf)o; }

	// ---( server methods )---




	public static final void convertHtmlToPdf (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(convertHtmlToPdf)>> ---
		// @sigtype java 3.5
		// [i] field:0:required html
		// [o] object:0:required pdf
		IDataCursor c = pipeline.getCursor();
		String htmlString = IDataUtil.getString(c, "html");
				
		ByteArrayOutputStream pdf = new ByteArrayOutputStream();
		
		try {
			
			StringReader input = new StringReader(htmlString);
			Tidy tidy = new Tidy();
			Document xmlDoc = tidy.parseDOM(input, null);
		
			Document foDoc = xml2FO(xmlDoc, "./packages/JcPublicTools/config/xhtml2fo.xsl");
		
		    pdf.write(fo2PDF(foDoc));
		}
		catch (java.io.IOException e) {
		    System.out.println("Error writing PDF: " + e);
		}
		
		IDataUtil.put(c, "pdf", pdf.toByteArray());
		c.destroy();
			
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	
	 private static Document xml2FO(Document xml, String styleSheet) throws ServiceException 
	 {
		DOMSource xmlDomSource = new DOMSource(xml);
		DOMResult domResult = new DOMResult();
	
		Transformer transformer = getTransformer(styleSheet);
			
		if (transformer == null) 
			throw new ServiceException("Error creating transformer for " + styleSheet);
		
		try 
		{
			transformer.transform(xmlDomSource, domResult);
		}
		catch (javax.xml.transform.TransformerException e) 
		{
			throw new ServiceException("pdf transform failed: " + styleSheet);
		}
		
		return (Document) domResult.getNode();
	 }
	
	 private static byte[] fo2PDF(Document foDocument) throws ServiceException 
	 {
		 DocumentInputSource fopInputSource = new DocumentInputSource(foDocument);
	
		 try 
		 {
			 ByteArrayOutputStream out = new ByteArrayOutputStream();
			 Logger log = new ConsoleLogger(ConsoleLogger.LEVEL_WARN);
	
		     Driver driver = new Driver(fopInputSource, out);
		     driver.setLogger(log);
		     driver.setRenderer(Driver.RENDER_PDF);
		     driver.run();
	
		     return out.toByteArray();
		 } 
		 catch (Exception e) 
		 {
			 throw new ServiceException("failed to transform fo to pdf:" + e);
		 }
	 }
	
	 private static Transformer getTransformer(String styleSheet) throws ServiceException 
	 {	
		 try 
		 {
			TransformerFactory tFactory = TransformerFactory.newInstance();
			DocumentBuilderFactory dFactory = DocumentBuilderFactory.newInstance();
	
			dFactory.setNamespaceAware(true);
		      
			DocumentBuilder dBuilder = dFactory.newDocumentBuilder();
			Document xslDoc = dBuilder.parse(styleSheet);
			DOMSource xslDomSource = new DOMSource(xslDoc);
	
			return tFactory.newTransformer(xslDomSource);
		}
		catch (javax.xml.transform.TransformerException e) 
		{
			 throw new ServiceException("failed to generate stylesheet transformer for pdf:" + e);
		}
		catch (java.io.IOException e) 
		{
			 throw new ServiceException("failed to generate stylesheet transformer for pdf:" + e);
		}
		catch (javax.xml.parsers.ParserConfigurationException e) 
		{
			 throw new ServiceException("failed to generate stylesheet transformer for pdf:" + e);
		}
		catch (org.xml.sax.SAXException e) 
		{	
			 throw new ServiceException("failed to generate stylesheet transformer for pdf:" + e);
		}
	}
	// --- <<IS-END-SHARED>> ---
}

