package jc.tools.pub;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.wm.app.b2b.server.ServerAPI;
import com.sun.mail.iap.Response;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import jcifs.ntlmssp.NtlmFlags;
import jcifs.ntlmssp.Type1Message;
import jcifs.ntlmssp.Type2Message;
import jcifs.ntlmssp.Type3Message;
import jcifs.util.Base64;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.impl.auth.BasicSchemeFactory;
import org.apache.http.impl.auth.DigestSchemeFactory;
import org.apache.http.impl.auth.KerberosSchemeFactory;
import org.apache.http.impl.auth.NTLMEngine;
import org.apache.http.impl.auth.NTLMEngineException;
import org.apache.http.impl.auth.NTLMScheme;
import org.apache.http.impl.auth.SPNegoSchemeFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.InvalidCredentialsException;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
// --- <<IS-END-IMPORTS>> ---

public final class ntlm

{
	// ---( internal utility methods )---

	final static ntlm _instance = new ntlm();

	static ntlm _newInstance() { return new ntlm(); }

	static ntlm _cast(Object o) { return (ntlm)o; }

	// ---( server methods )---




	public static final void http (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(http)>> ---
		// @sigtype java 3.5
		// [i] field:0:required url
		// [i] field:0:required method {"get","post","head","put","delete","options","trace"}
		// [i] field:0:optional loadAs {"bytes","stream"}
		// [i] record:0:required data
		// [i] - record:0:optional args
		// [i] - field:0:optional string
		// [i] - object:0:optional bytes
		// [i] - field:0:optional encoding
		// [i] record:0:optional auth
		// [i] - field:0:optional type {"NTLM"}
		// [i] - field:0:required domain
		// [i] - field:0:optional user
		// [i] - field:0:optional pass
		// [i] - field:0:required host
		// [i] record:0:optional headers
		// [i] field:0:optional timeout
		// [o] field:0:required encodedURL
		// [o] record:0:optional header
		// [o] - record:0:required lines
		// [o] - field:0:required status
		// [o] - field:0:required statusMessage
		// [o] record:0:required body
		// [o] - object:0:optional bytes
		// [o] - object:0:optional stream
		// pipeline in
		
		IDataCursor pipelineCursor = pipeline.getCursor();
		String url = IDataUtil.getString(pipelineCursor, "url");
		String method = IDataUtil.getString(pipelineCursor, "method");
		String loadAs = IDataUtil.getString(pipelineCursor, "loadAs");
		IData data = IDataUtil.getIData(pipelineCursor, "data");
		IData auth = IDataUtil.getIData(pipelineCursor, "auth");
		IData headers = IDataUtil.getIData(pipelineCursor, "headers");
		
		IDataCursor dataCursor = data.getCursor();
		IData dataArgs = IDataUtil.getIData(dataCursor, "args");
		String dataString = IDataUtil.getString(dataCursor, "string");
		String encoding = IDataUtil.getString(dataCursor, "encoding");
		dataCursor.destroy();
		
		IDataCursor authCursor = auth.getCursor();
		String type = IDataUtil.getString(authCursor, "type");
		String domain = IDataUtil.getString(authCursor, "domain");
		String host = IDataUtil.getString(authCursor, "host");
		String user = IDataUtil.getString(authCursor, "user");
		String pass = IDataUtil.getString(authCursor, "pass");
		authCursor.destroy();
		
		// process
		
		if (!type.equals("NTLM"))
			throw new ServiceException("User pub.client.http for normal http client requests");
		
		if (encoding == null)
		{	
			encoding = Charset.defaultCharset().displayName();
			IDataUtil.put(pipelineCursor, "encoding", encoding);
		}
		
		//	Register NTLMSchemeFactory with the HttpClient instance you want to NTLM enable.
		
		HttpClientBuilder httpClientBuild = HttpClients.custom();
		httpClientBuild.setDefaultAuthSchemeRegistry(getAuthRegistry());
		httpClientBuild.setDefaultCredentialsProvider(getCredentialsProvider(user, pass, domain, host));
		
		CloseableHttpClient httpClient = httpClientBuild.build();
		
		HttpClientContext context = HttpClientContext.create();
		context.setCredentialsProvider(getCredentialsProvider(user, pass, domain, host));
		
		HttpUriRequest request = null;
		String status = "";
		String contentType = "text/plain";
		String statusMessage = "";
		List<IData> headerLines = new ArrayList<IData>();
		byte[] bodyBytes = null;
		InputStream bodyStream = null;
		
		if (method.equals("get"))
		{
			HttpGet get = new HttpGet(url);
			
			request = get;
		}
		else if (method.equals("post"))
		{
			HttpPost post = new HttpPost(url);
			
			if (headers != null)
				contentType = setHeaders(headers, post);
			
			if (dataString != null)
				post.setEntity(createStringContent(contentType, encoding, dataString));
		
			request = post;
		}
		else if (method.equals("put"))
		{
			HttpPut put = new HttpPut(url);
			
			if (headers != null)
				contentType = setHeaders(headers, put);
			
			if (dataString != null)
				put.setEntity(createStringContent(contentType, encoding, dataString));
		
			request = put;
		}
		else if (method.equals("delete"))
		{
			HttpDelete delete = new HttpDelete(url);
		
			request = delete;
		}
		else
		{
			throw new ServiceException("Unrecognised method, use get, post, put or delete: " + method);
		}
		
		CloseableHttpResponse r = null;
		
		try {
			
			r = httpClient.execute(request, context);
			
			status = "" + r.getStatusLine().getStatusCode();
			statusMessage = r.getStatusLine().getReasonPhrase();
			
			HeaderIterator it = r.headerIterator();
			
			while (it.hasNext())
			{
				Header header = it.nextHeader();
				
				IData hdrDoc = IDataFactory.create();
				IDataCursor hdrDocCursor = hdrDoc.getCursor();
				IDataUtil.put(hdrDocCursor, header.getName(), header.getValue());
				hdrDocCursor.destroy();
				
				headerLines.add(hdrDoc);
			}
			
			// get content
			
			HttpEntity e = r.getEntity();
			
			bodyStream = new BufferedInputStream(e.getContent());
			
			if (loadAs.equals("bytes"))
			{
				ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();				
				byte[] buf = new byte[1024];
				
				while(true)
				{
					int read = bodyStream.read(buf);
					
					if (read == -1)
						break;
					
					byteBuffer.write(buf, 0, read);
				}
		
				bodyStream.close();
				bodyStream = null;
		
				bodyBytes = byteBuffer.toByteArray();
			}
		} 
		catch (ClientProtocolException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		finally
		{
			if (r != null)
			{
				try {
					r.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		// header
		
		IData header = IDataFactory.create();
		IDataCursor headerCursor = header.getCursor();
		IDataUtil.put(headerCursor, "lines", headerLines.toArray(new IData[headerLines.size()]));
		IDataUtil.put(headerCursor, "status", status);
		IDataUtil.put(headerCursor, "statusMessage", statusMessage);
		headerCursor.destroy();
		
		// body
		
		IData bodyDoc = IDataFactory.create();
		IDataCursor bodyCursor = bodyDoc.getCursor();
		
		if (bodyBytes != null)
			IDataUtil.put(bodyCursor, "bytes", bodyBytes);
		else
			IDataUtil.put(bodyCursor, "stream", bodyStream);
		
		bodyCursor.destroy();
		
		IDataUtil.put(pipelineCursor, "encodedURL", "encodedURL");
		IDataUtil.put(pipelineCursor, "header", header);
		IDataUtil.put(pipelineCursor, "body", bodyDoc);
		pipelineCursor.destroy();
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	private static String setHeaders(IData headers, HttpUriRequest request)
	{
		String contentType = null;
		IDataCursor c = headers.getCursor();
		
		while (c.hasMoreData())
		{
			c.next();
			String key = c.getKey();
			String value = c.getValue().toString();
			
			request.addHeader(key, value);
			
			if (key.equals("Content-Type"))
				contentType = value;
			
		}
		
		c.destroy();
		
		return contentType;
	}
	
	private static HttpEntity createStringContent(String content)
	{
		return createStringContent("text/plain", Charset.defaultCharset().name(), content);
	}
	
	private static HttpEntity createStringContent(String mimeType, String content)
	{
		return createStringContent(mimeType, Charset.defaultCharset().name(), content);
	}
	
	private static HttpEntity createStringContent(String mimeType, String charset, String content)
	{
		if (mimeType == null)
			mimeType = "text/plain";
		
		if (charset == null)
			charset = Charset.defaultCharset().name();
			
		return new StringEntity(content, ContentType.create(mimeType, charset));
	}
	
	private static Registry<AuthSchemeProvider> getAuthRegistry()
	{
		Registry<AuthSchemeProvider> authSchemeRegistry = RegistryBuilder.<AuthSchemeProvider>create().register(AuthSchemes.NTLM, new JCIFSNTLMSchemeFactory()).build();
		
		return authSchemeRegistry;
	}
	
	private static CredentialsProvider getCredentialsProvider(String user, String password, String domain, String host)
	{
		NTCredentials credentials = new NTCredentials(user, password, host, domain);
		CredentialsProvider crProvider = new BasicCredentialsProvider();
		crProvider.setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM, AuthSchemes.NTLM), credentials);
		
		return crProvider;
	}
	
	// Implement AuthSchemeProvider interface
	
	private static class JCIFSNTLMSchemeFactory implements AuthSchemeProvider 
	{
	    public AuthScheme create(final HttpContext context) 
	    {
	        debugLog("Info", "Using NTLM engine");
	
	        return new NTLMScheme(new NtlmEngineImpl());
	    }
	}
	
	private static class NtlmEngineImpl implements NTLMEngine
	{
		private static final int TYPE_1_FLAGS = 
	            NtlmFlags.NTLMSSP_NEGOTIATE_56 | 
	            NtlmFlags.NTLMSSP_NEGOTIATE_128 | 
	            NtlmFlags.NTLMSSP_NEGOTIATE_NTLM2 | 
	            NtlmFlags.NTLMSSP_NEGOTIATE_ALWAYS_SIGN | 
	            NtlmFlags.NTLMSSP_REQUEST_TARGET;
	
	    public String generateType1Msg(final String domain, final String workstation)
	            throws NTLMEngineException 
	    {
	        //final Type1Message type1Message = new Type1Message(TYPE_1_FLAGS, domain, workstation);
	        final Type1Message type1Message = new Type1Message(Type1Message.getDefaultFlags(), domain, workstation);
	
	        debugLog("Info", "Got type 1 message");
	
	        return Base64.encode(type1Message.toByteArray());
	    }
	
	    public String generateType3Msg(final String username, final String password,
	            final String domain, final String workstation, final String challenge)
	            throws NTLMEngineException 
	   {
	        Type2Message type2Message;
	   
	        debugLog("Info", "Got type 2 message");
	        
	        try 
	        {
	            type2Message = new Type2Message(Base64.decode(challenge));
	        } 
	        catch (final IOException exception) 
	        {
	            throw new NTLMEngineException("Invalid NTLM type 2 message", exception);
	        }
	        
	        debugLog("Info", "Generating type 3 message:" + domain + ":" + username + "=" + password);
	
	        final int type2Flags = type2Message.getFlags();
	        final int type3Flags = type2Flags & (0xffffffff ^ (NtlmFlags.NTLMSSP_TARGET_TYPE_DOMAIN | NtlmFlags.NTLMSSP_TARGET_TYPE_SERVER));
	        
	        final Type3Message type3Message = new Type3Message(type2Message, password, domain, username, workstation);
	      
	        type3Message.setFlags(type3Flags);
	        
	        return Base64.encode(type3Message.toByteArray());
	    }
	}
		
	public static void debugLog(String debugLevel, String message)
	{
		IData input = IDataFactory.create();
		IDataCursor inputCursor = input.getCursor();
		IDataUtil.put(inputCursor, "message", message);
		IDataUtil.put(inputCursor, "function", "com.ocp.contactmgmt");
		IDataUtil.put(inputCursor, "level", debugLevel);
	
		inputCursor.destroy();
	
		try
		{
			Service.doInvoke("pub.flow", "debugLog", input);
		}
		catch( Exception e)
		{
			ServerAPI.logError(e);
			ServerAPI.logError(new ServiceException("Cannot log message: " + message));
		}   
	}
	// --- <<IS-END-SHARED>> ---
}

