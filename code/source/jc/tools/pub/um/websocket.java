package jc.tools.pub.um;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.wm.app.b2b.server.Session;
import com.wm.app.b2b.server.User;
import com.wm.app.b2b.server.UserManager;
import com.wm.app.b2b.server.InvokeState;
import com.wm.app.b2b.server.ServerAPI;
import com.wm.app.b2b.server.dispatcher.DispatchFacade;
import com.wm.app.b2b.server.dispatcher.exceptions.CommException;
import com.wm.app.b2b.server.dispatcher.exceptions.MessagingSubsystemException;
import com.wm.app.b2b.server.dispatcher.wmmessaging.ConnectionAlias;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.pcbsys.nirvana.base.events.nEvent;
import com.pcbsys.nirvana.client.nChannel;
import com.pcbsys.nirvana.client.nChannelAlreadyExistsException;
import com.pcbsys.nirvana.client.nChannelAlreadySubscribedException;
import com.pcbsys.nirvana.client.nChannelAttributes;
import com.pcbsys.nirvana.client.nChannelNotFoundException;
import com.pcbsys.nirvana.client.nConsumeEvent;
import com.pcbsys.nirvana.client.nEventAttributes;
import com.pcbsys.nirvana.client.nEventListener;
import com.pcbsys.nirvana.client.nEventProperties;
import com.pcbsys.nirvana.client.nIllegalArgumentException;
import com.pcbsys.nirvana.client.nIllegalChannelMode;
import com.pcbsys.nirvana.client.nIllegalStateException;
import com.pcbsys.nirvana.client.nMaxBufferSizeExceededException;
import com.pcbsys.nirvana.client.nQueue;
import com.pcbsys.nirvana.client.nQueuePeekContext;
import com.pcbsys.nirvana.client.nQueueReader;
import com.pcbsys.nirvana.client.nQueueReaderContext;
import com.pcbsys.nirvana.client.nRealmUnreachableException;
import com.pcbsys.nirvana.client.nRequestTimedOutException;
import com.pcbsys.nirvana.client.nSecurityException;
import com.pcbsys.nirvana.client.nSelectorParserException;
import com.pcbsys.nirvana.client.nSession;
import com.pcbsys.nirvana.client.nSessionAlreadyInitialisedException;
import com.pcbsys.nirvana.client.nSessionAttributes;
import com.pcbsys.nirvana.client.nSessionFactory;
import com.pcbsys.nirvana.client.nSessionNotConnectedException;
import com.pcbsys.nirvana.client.nSessionPausedException;
import com.pcbsys.nirvana.client.nUnexpectedResponseException;
import com.pcbsys.nirvana.client.nUnknownRemoteRealmException;
// --- <<IS-END-IMPORTS>> ---

public final class websocket

{
	// ---( internal utility methods )---

	final static websocket _instance = new websocket();

	static websocket _newInstance() { return new websocket(); }

	static websocket _cast(Object o) { return (websocket)o; }

	// ---( server methods )---




	public static final void createQueue (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(createQueue)>> ---
		// @sigtype java 3.5
		// [i] field:0:required connectionAlias
		// [i] field:0:required queueName
		IDataCursor p = pipeline.getCursor();
		String umAlias = IDataUtil.getString(p, "connectionAlias");
		String queueName = IDataUtil.getString(p, "queueName");
		
		// process
		
		try 
		{
			UMConnectionInfo info = getConnection(umAlias);
		
			String[] RNAME={info.url}; 
			nSessionAttributes nsa=new nSessionAttributes(RNAME); 
		
			nSession mySession = null;
		
			if (info.id != null)
			{
			// connection parameters
			
				mySession = nSessionFactory.create(nsa, info.id, info.password); 
			}
			else
			{
				mySession = nSessionFactory.create(nsa); 
			}
				
			mySession.init();
		
			createQueue(mySession, queueName);
			
			mySession.close();
		}
		catch (nRealmUnreachableException | nSecurityException | nSessionNotConnectedException
				| nSessionAlreadyInitialisedException e) 
		{
			throw new ServiceException(e);
			
		} catch (MessagingSubsystemException e) {
			
			throw new ServiceException(e);
		} catch (CommException e) {
			
			throw new ServiceException(e);
		} catch (nIllegalArgumentException e) {
			
			throw new ServiceException(e);
		}
			
		// --- <<IS-END>> ---

                
	}



	public static final void getQueueSummary (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(getQueueSummary)>> ---
		// @sigtype java 3.5
		// [i] field:0:required connectionAlias
		// [i] field:0:required baseName
		// [o] field:1:required queueNames
		IDataCursor p = pipeline.getCursor();
		String umAlias = IDataUtil.getString(p, "connectionAlias");
		String baseName = IDataUtil.getString(p, "baseName");
		
		List<String> queueNames = new ArrayList<String>();
		try 
		{
			nSession session = UMConnectionMgr.getSharedConnection(umAlias).getSessionConnection();
			nChannelAttributes[] all = session.getChannels(baseName);
						
			for (int i = 0; i < all.length; i++)
			{
				queueNames.add(all[i].getName());
			}
			
		} catch (nSecurityException e) {
			
			e.printStackTrace();
		} catch (nSessionNotConnectedException e) {
			
			e.printStackTrace();
		} catch (nSessionPausedException e) {
			
			e.printStackTrace();
		} catch (nUnexpectedResponseException e) {
			
			e.printStackTrace();
		} catch (nRequestTimedOutException e) {
			
			e.printStackTrace();
		}
		finally {}
		
		// process out
		
		IDataUtil.put(p, "queueNames", queueNames.toArray(new String[queueNames.size()]));
		p.destroy();
			
		// --- <<IS-END>> ---

                
	}



	public static final void peek (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(peek)>> ---
		// @sigtype java 3.5
		// [i] field:0:required connectionAlias
		// [i] field:0:required baseName
		// [i] field:0:required queueName
		// [o] record:1:required events
		// pipeine in
		
		IDataCursor p = pipeline.getCursor();
		String umAlias = IDataUtil.getString(p, "connectionAlias");
		String baseName = IDataUtil.getString(p, "baseName");
		String queueName = IDataUtil.getString(p, "queueName");
		
		// process
		
		List<IData> events = new ArrayList<IData>();
		
		try 
		{
			nQueue queue = UMConnectionMgr.getSharedConnection(umAlias).connectToQueue(baseName, queueName);
		
			nQueueReader reader = queue.createReader(new nQueueReaderContext());
			nQueuePeekContext ctx = nQueueReader.createContext(10);
				
			while (ctx.hasMore()) 
			{
		    // browse (peek) the queue
				nConsumeEvent[] evts = reader.peek(ctx);
		    
				if (evts != null)
				{
					for (int x=0; x < evts.length; x++) 
					{
		    			IData out = IDataFactory.create();
		    			IDataCursor c = out.getCursor();
		    			nEventProperties props = evts[x].getProperties();
		    		
		    			props.getKeyIterator().forEachRemaining(key -> 
		    			{
		    				Object value = props.get(key);
		    			
		    				System.out.println("processing " + key + " = " + value);
		    				IDataUtil.put(c, key, value);
		    			});
		    		
		    			c.destroy();
		    		
		    			events.add(out);
					}
				}
			}
		} catch (nIllegalArgumentException e) {
			throw new ServiceException(e);
		} catch (nSessionPausedException e) {
			 
			throw new ServiceException(e);
		} catch (nUnknownRemoteRealmException e) {
			 
			throw new ServiceException(e);
		} catch (nSecurityException e) {
			 
			throw new ServiceException(e);
		} catch (nSessionNotConnectedException e) {
			 
			throw new ServiceException(e);
		} catch (nUnexpectedResponseException e) {
			 
			throw new ServiceException(e);
		} catch (nRequestTimedOutException e) {
			 
			throw new ServiceException(e);
		} catch (nIllegalChannelMode e) {
			 
			throw new ServiceException(e);
		} catch (nChannelNotFoundException e) {
			 
			throw new ServiceException(e);
		} catch (nIllegalStateException e) {
			 
			throw new ServiceException(e);
		}
		
		// pipeline out
		
		IDataUtil.put(p, "events", events.toArray(new IData[events.size()]));
		p.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void publish (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(publish)>> ---
		// @sigtype java 3.5
		// [i] field:0:required connectionAlias
		// [i] field:0:required baseName
		// [i] field:0:required channelName
		// [i] field:0:required tag
		// [i] record:0:required properties
		// [i] - field:0:required data
		// [i] field:0:required _data
		// [i] field:0:optional timeToLiveMilliseconds
		// [i] field:0:optional isPersistent {"true","false"}
		// pipeline 
		
		IDataCursor p = pipeline.getCursor();
		String umAlias = IDataUtil.getString(p, "connectionAlias");
		String baseName = IDataUtil.getString(p, "baseName");
		String queueName = IDataUtil.getString(p, "channelName");
		String tag = IDataUtil.getString(p, "tag");
		IData props = IDataUtil.getIData(p, "properties");
		String data = IDataUtil.getString(p, "_data");
		String ttls = IDataUtil.getString(p, "timeToLiveMilliseconds");
		String isPersistents = IDataUtil.getString(p, "isPersistent");
		
		// process
			
		Integer ttl = 0;
		boolean isPersistent = true;
		
		try { ttl = Integer.parseInt(ttls); } catch(Exception e) {};
		try { isPersistent = Boolean.parseBoolean(isPersistents); } catch(Exception e) {};
		
		try {
			nChannel channel = UMConnectionMgr.getSharedConnection(umAlias).connectToChannel(baseName, queueName);
			channel.publish(convertIDataDictToEvent(tag, props, data, ttl, isPersistent));
		
		} catch (nIllegalArgumentException e) {
			
			throw new ServiceException(e);
		} catch (nSessionPausedException e) {
			  
			throw new ServiceException(e);
		} catch (nUnknownRemoteRealmException e) {
			  
			throw new ServiceException(e);
		} catch (nSecurityException e) {
			  
			throw new ServiceException(e);
		} catch (nSessionNotConnectedException e) {
			  
			throw new ServiceException(e);
		} catch (nUnexpectedResponseException e) {
			  
			throw new ServiceException(e);
		} catch (nRequestTimedOutException e) {
			  
			throw new ServiceException(e);
		} catch (nIllegalChannelMode e) {
			  
			throw new ServiceException(e);
		} catch (nMaxBufferSizeExceededException e) {
			  
			throw new ServiceException(e);
		} catch (nChannelNotFoundException e) {
			  
			throw new ServiceException(e);
		}
		
		p.destroy();
			
		// --- <<IS-END>> ---

                
	}



	public static final void queue (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(queue)>> ---
		// @sigtype java 3.5
		// [i] field:0:required connectionAlias
		// [i] field:0:required baseName
		// [i] field:0:required queueName
		// [i] field:0:required tag
		// [i] record:0:required properties
		// [i] field:0:required _data
		// [i] field:0:optional timeToLiveMilliseconds
		// [i] field:0:optional isPersistent {"true","false"}
		// pipeline in
		
		IDataCursor p = pipeline.getCursor();
		String umAlias = IDataUtil.getString(p, "connectionAlias");
		String baseName = IDataUtil.getString(p, "baseName");
		String queueName = IDataUtil.getString(p, "queueName");
		String tag = IDataUtil.getString(p, "tag");
		IData props = IDataUtil.getIData(p, "properties");
		String data = IDataUtil.getString(p, "_data");
		String ttls = IDataUtil.getString(p, "timeToLiveMilliseconds");
		String isPersistents = IDataUtil.getString(p, "isPersistent");
		 
		// process
		
		Integer ttl = 0;
		boolean isPersistent = true;
		
		try { ttl = Integer.parseInt(ttls); } catch(Exception e) {};
		try { isPersistent = Boolean.parseBoolean(isPersistents); } catch(Exception e) {};
		
		try {
			nQueue queue = UMConnectionMgr.getSharedConnection(umAlias).connectToQueue(baseName, queueName);
			queue.push(convertIDataDictToEvent(tag, props, data, ttl, isPersistent));
		
		} catch (nIllegalArgumentException e) {
			
			throw new ServiceException(e);
		} catch (nSessionPausedException e) {
			  
			throw new ServiceException(e);
		} catch (nUnknownRemoteRealmException e) {
			  
			throw new ServiceException(e);
		} catch (nSecurityException e) {
			  
			throw new ServiceException(e);
		} catch (nSessionNotConnectedException e) {
			  
			throw new ServiceException(e);
		} catch (nUnexpectedResponseException e) {
			  
			throw new ServiceException(e);
		} catch (nRequestTimedOutException e) {
			  
			throw new ServiceException(e);
		} catch (nIllegalChannelMode e) {
			  
			throw new ServiceException(e);
		} catch (nMaxBufferSizeExceededException e) {
			  
			throw new ServiceException(e);
		}
		
		p.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void subscribe (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(subscribe)>> ---
		// @sigtype java 3.5
		// [i] field:0:required listenerID
		// [i] field:0:required connectionAlias
		// [i] field:0:required baseName
		// [i] field:0:required channelName
		// [i] field:0:required callbackService
		// [i] field:0:required selector
		// [o] field:0:required listenerID
		// pipeline in
		
		IDataCursor p = pipeline.getCursor();
		String listenerId = IDataUtil.getString(p, "listenerID");
		String umAlias = IDataUtil.getString(p, "connectionAlias");
		String baseName = IDataUtil.getString(p, "baseName");
		String queueName = IDataUtil.getString(p, "channelName");
		String calBackService = IDataUtil.getString(p, "callbackService");
		String selector = IDataUtil.getString(p, "selector");
		
		// process
		
		int index = calBackService.indexOf(":");
		String ifc = calBackService.substring(0, index);
		String svc = calBackService.substring(index+1);
		
		try {
			nChannel channel = UMConnectionMgr.getSharedConnection(umAlias).connectToChannel(baseName, queueName);
					
			nEventListener listener = new nEventListener() {
				
				@Override
				public void go(nConsumeEvent evt) {
										
					try {
						IData eventDoc = convertConsumerEventToIData(evt);
												
						 if (InvokeState.getCurrentState() == null || InvokeState.getCurrentState().getSession() == null)
			    		 { 
			    			 setInvokeStateFor(UserManager.getUser("Administrator"));
			    		 }
						
						Service.doInvoke(ifc,  svc, eventDoc);
					} catch (Exception e) {
						e.printStackTrace();
						ServerAPI.logError(e);
					}
					
				}
			};
			
			if (selector != null)
				channel.addSubscriber(listener, selector);
			else
				channel.addSubscriber(listener);
			
			if (listenerId == null)
				listenerId = "" + listener.hashCode();
			
			_listeners.put(listenerId, listener);
			
		} catch (nIllegalArgumentException e) {
			
			throw new ServiceException(e);
		} catch (nSessionPausedException e) {
			  
			throw new ServiceException(e);
		} catch (nUnknownRemoteRealmException e) {
			  
			throw new ServiceException(e);
		} catch (nSecurityException e) {
			  
			throw new ServiceException(e);
		} catch (nSessionNotConnectedException e) {
			  
			throw new ServiceException(e);
		} catch (nUnexpectedResponseException e) {
			  
			throw new ServiceException(e);
		} catch (nRequestTimedOutException e) {
			  
			throw new ServiceException(e);
		} catch (nIllegalChannelMode e) {
			  
			throw new ServiceException(e);
		} catch (nChannelNotFoundException e) {
			
			throw new ServiceException(e);
		} catch (nChannelAlreadySubscribedException e) {
			
			throw new ServiceException(e);
		} catch (nSelectorParserException e) {
			
			throw new ServiceException(e);
		}
		
		// pipeline out
		
		IDataUtil.put(p, "listenerID", listenerId);
		p.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void unsubscribe (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(unsubscribe)>> ---
		// @sigtype java 3.5
		// [i] field:0:required listenerID
		// [i] field:0:required connectionAlias
		// [i] field:0:required baseName
		// [i] field:0:required channelName
		// [o] field:0:required found {"true","false"}
		// pipeline in
		
		IDataCursor p = pipeline.getCursor();
		String listenerId = IDataUtil.getString(p, "listenerID");
		String umAlias = IDataUtil.getString(p, "connectionAlias");
		String baseName = IDataUtil.getString(p, "baseName");
		String queueName = IDataUtil.getString(p, "queueName");
		
		// process
		
		boolean found = false;
		nEventListener l = _listeners.get(listenerId);
		
		if (l != null)
		{
			found = true;
			_listeners.remove(listenerId);
			
			try {
				nChannel channel = UMConnectionMgr.getSharedConnection(umAlias).connectToChannel(baseName, queueName);
			
				channel.removeSubscriber(l);
		
			} catch (nIllegalArgumentException | nChannelNotFoundException | nSessionPausedException
				| nUnknownRemoteRealmException | nSecurityException | nSessionNotConnectedException
				| nUnexpectedResponseException | nRequestTimedOutException | nIllegalChannelMode e) {
		
				throw new ServiceException(e);
			}
		}
		
		// pipeline out
		
		IDataUtil.put(p, "found", "" + found);
		p.destroy();
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	
	public static Map<String, nEventListener> _listeners = new HashMap<String, nEventListener>();
	
	public final static String QUEUE_STATUS = "queueStatus";
	
	public static IData convertConsumerEventToIData(nConsumeEvent evt)
	{
		IData doc = IDataFactory.create();
		IDataCursor c = doc.getCursor();
				
		IDataUtil.put(c, "_timeStamp", new Date(evt.getTimestamp()));
		IDataUtil.put(c, "_data", evt.getEventData());
		IDataUtil.put(c, "channel", evt.getChannelName());
		IDataUtil.put(c, "tag", evt.getEventTag());
		
		nEventProperties props = evt.getProperties();
		
		IData attribs = IDataFactory.create();
		IDataCursor ac = attribs.getCursor();
		
		props.getKeyIterator().forEachRemaining(name -> {
			
			IDataUtil.put(ac, name, props.get(name));
		});
		
		ac.destroy();
		
		IDataUtil.put(c, "properties", attribs);
	
		c.destroy();
		
		IData wrapper = IDataFactory.create();
		IDataCursor w = wrapper.getCursor();
		IDataUtil.put(w, "event", doc);
		w.destroy();
		
		return wrapper;
	}
	
	public static nConsumeEvent convertIDataDictToEvent(String tag, IData properties, String data, Integer ttl, boolean isPersistent)
	{
		if (data == null)
			data = "";
		
		nConsumeEvent event = new nConsumeEvent(tag, data.getBytes());
		nEventProperties props = new nEventProperties();
		
		IDataCursor dc = properties.getCursor();
		while (dc.next())
		{
			props.put(dc.getKey(), (String) dc.getValue());
		}
		
		dc.destroy();
		
		event.setProperties(props);
		
		if (ttl > 0)
			event.setTTL(ttl);
		
		if (isPersistent)
			event.setPersistant(true);
		else
			event.setPersistant(false);
				
		return event;
	}
	
	public static class UMConnectionMgr
	{
		private static Map<String, UMConnectionMgr> _conn;
		
		private UMConnectionInfo _info;
		private nSession _session;
		
		static 
		{
			_conn = new HashMap<String, UMConnectionMgr>();
		}
		
		public static UMConnectionMgr getSharedConnection(String aliasName) throws ServiceException
		{
			UMConnectionMgr mgr = _conn.get(aliasName);
			
			if (mgr == null)
			{
				mgr = new UMConnectionMgr(aliasName);
				_conn.put(aliasName, mgr);
				
				mgr.connect();
			}
			
			return mgr;
		}
	
		@Override
		protected void finalize() throws Throwable {
			
			if (_session != null && _session.isConnected())
				_session.close();
		}
		
		public UMConnectionMgr(String aliasName) throws ServiceException
		{
			try {
				_info = getConnection(aliasName);
			} catch (MessagingSubsystemException e) {
				throw new ServiceException(e);
			} catch (CommException e) {
				throw new ServiceException(e);
			}
		}
		 
		public nChannel connectToChannel(String baseName, String queueName) throws ServiceException, nIllegalArgumentException, nChannelNotFoundException, nSessionPausedException, nUnknownRemoteRealmException, nSecurityException, nSessionNotConnectedException, nUnexpectedResponseException, nRequestTimedOutException, nIllegalChannelMode
		{
			String fullName = queueName;
			
			if (baseName != null)
				fullName = baseName + "/" + fullName;
			
			nSession s = getSessionConnection();
	
			nChannelAttributes cattrib = new nChannelAttributes(); 
			cattrib.setName(fullName); 
			
			nChannel c = null;
			
			try {
				c = s.findChannel(cattrib);
			} catch (nChannelNotFoundException e) {
				
				c = createChannel(getSessionConnection(), fullName);
				
		// now publish an event to flag new queue
		
				flagNewQueueOrChannel(s, baseName, queueName, fullName, true);
			}	
			
			return c;
		}
		
		public nQueue connectToQueue(String baseName, String queueName) throws ServiceException, nIllegalArgumentException, nSessionPausedException, nUnknownRemoteRealmException, nSecurityException, nSessionNotConnectedException, nUnexpectedResponseException, nRequestTimedOutException, nIllegalChannelMode
		{
			String fullName = queueName;
			
			if (baseName != null)
				fullName = baseName + "/queues/" + fullName;
			
			nSession s = getSessionConnection();
			
			nChannelAttributes cattrib = new nChannelAttributes(); 
			cattrib.setName(fullName); 
			
			nQueue q = null;
			
			try {
				q = s.findQueue(cattrib);
			} catch (nChannelNotFoundException e) {
				
				q = createQueue(getSessionConnection(), fullName);
				
		// now publish an event to flag new queue
		
				flagNewQueueOrChannel(s, baseName, queueName, fullName, false);
			}	
			
			return q;
		}
		
		public nSession getSessionConnection() throws ServiceException
		{
			if (_session == null || !_session.isConnected())
				_session = connect();
			
			return _session;
		}
		
		private void flagNewQueueOrChannel(nSession session, String baseName, String queueName, String fullName, boolean isChannel) throws ServiceException, nIllegalArgumentException
		{
			String newClientQueueTrigger = QUEUE_STATUS;
			
			if (baseName != null)
				newClientQueueTrigger = baseName + "/" + newClientQueueTrigger;
			
			nChannelAttributes ctattrib = new nChannelAttributes(); 
			ctattrib.setName(newClientQueueTrigger); 
			
			nChannel c;
			try {
				c = session.findChannel(ctattrib);
			} catch (nChannelNotFoundException e2) {
				
				c = createChannel(getSessionConnection(), newClientQueueTrigger);
			} catch (Exception e) {
				throw new ServiceException(e);
			}
					
			nConsumeEvent event = new nConsumeEvent("NEW", "".getBytes());
			nEventProperties props = new nEventProperties();
			
			if (isChannel)
				props.put("type", "CHANNEL");
			else
				props.put("type", "QUEUE");
			
			System.out.println("========> fullName is " + fullName);
			
			props.put("name", queueName);
			props.put("fullName", fullName);
			event.setProperties(props);
			
			try {
				c.publish(event);
			} catch (nMaxBufferSizeExceededException e) {
				throw new ServiceException(e);
			} catch (nSessionNotConnectedException e) {
				
				throw new ServiceException(e);
			} catch (nSessionPausedException e) {
	
				throw new ServiceException(e);
			} catch (nSecurityException e) {
	
				throw new ServiceException(e);
			}
		}
		
		private nSession connect() throws ServiceException
		{
			try 
			{
				String[] RNAME={_info.url}; 
				nSessionAttributes nsa=new nSessionAttributes(RNAME); 
		
				nSession session = null;
		
				if (_info.id != null)
				{
					// connection parameters
			
					session = nSessionFactory.create(nsa, _info.id, _info.password); 
				}
				else
				{
					session = nSessionFactory.create(nsa); 
				}
				
				session.init();
				
				return session;
			}
			catch (nIllegalArgumentException e) {
				throw new ServiceException(e);
	
			} catch (nRealmUnreachableException e) {
				throw new ServiceException(e);
	
			} catch (nSecurityException e) {
				throw new ServiceException(e);
	
			} catch (nSessionNotConnectedException e) {
				throw new ServiceException(e);
	
			} catch (nSessionAlreadyInitialisedException e) {
				throw new ServiceException(e);
	
			}
		}
	}
	
	public static class UMConnectionInfo
	{
		public String url;
		public String authType = "none";
		public String id;
		public String password;
	}
	
	public static UMConnectionInfo getConnection(String aliasName) throws MessagingSubsystemException, CommException, ServiceException 
	{
		ConnectionAlias alias = DispatchFacade.getRuntimeConfigurationIfNotNull().getConnectionAlias(aliasName);
		UMConnectionInfo um = new UMConnectionInfo();
	
		if (alias != null)
		{
			IData props = alias.getAsData(true);
		
			IDataCursor c = props.getCursor();
			um.url = IDataUtil.getString(c, "um_rname");
		
			String authType = IDataUtil.getString(c, "clientAuthType");
		
			if (authType != null && authType.equals("basic"))
			{
				um.authType = authType;
				um.id = IDataUtil.getString(c, "umUser");
				um.password = IDataUtil.getString(c, "umPassword");
			}
		
			c.destroy();
		}
		else
		{
			throw new ServiceException("No connection alias for " + aliasName);
		}
		return um;
	}
	
	public static nQueue createQueue(nSession mySession, String queueName) throws ServiceException
	{
		try 
		{
			nChannelAttributes cattrib = new nChannelAttributes(); 
			cattrib.setChannelMode(nChannelAttributes.QUEUE_MODE); 
			cattrib.setMaxEvents(0); 
			cattrib.setTTL(0); 
			cattrib.setType(nChannelAttributes.PERSISTENT_TYPE); 
			cattrib.setName(queueName); 
			nQueue q = mySession.createQueue(cattrib);
	
			return q;
		} 
		catch (nIllegalArgumentException e) {
			
			throw new ServiceException(e);
		
		} catch (nUnknownRemoteRealmException e) {
			
			throw new ServiceException(e);
		
		} catch (nSessionPausedException e) {
			
			throw new ServiceException(e);
		
		} catch (nChannelAlreadyExistsException e) {
			
			throw new ServiceException(e);
		
		} catch (nUnexpectedResponseException e) {
			
			throw new ServiceException(e);
		
		} catch (nRequestTimedOutException e) {
			
			throw new ServiceException(e);
		} catch (nSecurityException e) {
			
			throw new ServiceException(e);
		} catch (nSessionNotConnectedException e) {
			
			throw new ServiceException(e);
		} 
	}
	
	public static nChannel createChannel(nSession mySession, String channelName) throws ServiceException
	{
		try 
		{
			nChannelAttributes cattrib = new nChannelAttributes(); 
			cattrib.setChannelMode(nChannelAttributes.CHANNEL_MODE); 
			cattrib.setMaxEvents(0); 
			cattrib.setTTL(0); 
			cattrib.setType(nChannelAttributes.TRANSIENT_TYPE); 
			cattrib.setName(channelName); 
			nChannel c = mySession.createChannel(cattrib);
	
			return c;
		} 
		catch (nIllegalArgumentException e) {
			
			throw new ServiceException(e);
		
		} catch (nUnknownRemoteRealmException e) {
			
			throw new ServiceException(e);
		
		} catch (nSessionPausedException e) {
			
			throw new ServiceException(e);
		
		} catch (nChannelAlreadyExistsException e) {
			
			throw new ServiceException(e);
		
		} catch (nUnexpectedResponseException e) {
			
			throw new ServiceException(e);
		
		} catch (nRequestTimedOutException e) {
			
			throw new ServiceException(e);
		} catch (nSecurityException e) {
			
			throw new ServiceException(e);
		} catch (nSessionNotConnectedException e) {
			
			throw new ServiceException(e);
		} 
	}
	
	public static void setInvokeStateFor(User user)
	{
		Session session = Service.getSession();
	    	
		if (session == null)
			session = new Session("esb");
	    	
	    InvokeState state = InvokeState.getCurrentState();
	    if (state == null)
	    {
	    	state = new InvokeState();
	    	InvokeState.setCurrentState(state);
	    }
	    	
		state.setSession(session);
		InvokeState.setCurrentUser(user);
	}
	// --- <<IS-END-SHARED>> ---
}

