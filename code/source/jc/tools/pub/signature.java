package jc.tools.pub;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.util.Base64URL;
import com.nimbusds.jwt.*;
import net.minidev.json.*;
import java.text.ParseException;
// --- <<IS-END-IMPORTS>> ---

public final class signature

{
	// ---( internal utility methods )---

	final static signature _instance = new signature();

	static signature _newInstance() { return new signature(); }

	static signature _cast(Object o) { return (signature)o; }

	// ---( server methods )---




	public static final void genKeyPair (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(genKeyPair)>> ---
		// @sigtype java 3.5
		IDataCursor c = pipeline.getCursor();
		
		try {
			KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("DSA");
			
			keyGenerator.initialize(2048);
		
		    KeyPair kp = keyGenerator.genKeyPair();
		    DSAPublicKey publicKey = (DSAPublicKey) kp.getPublic();
		    DSAPrivateKey privateKey = (DSAPrivateKey) kp.getPrivate();
		
		    IDataUtil.put(c, "publicKey", new String(Base64.getEncoder().encode(publicKey.getEncoded())));
		    IDataUtil.put(c, "publicKeyFormat", publicKey.getFormat());
		
		    IDataUtil.put(c, "privateKey", new String(Base64.getEncoder().encode(privateKey.getEncoded())));
		    IDataUtil.put(c, "privateKeyFormat",privateKey.getFormat());
		
		} catch (NoSuchAlgorithmException e) {
			throw new ServiceException(e);
		}
		 
		c.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void sign (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(sign)>> ---
		// @sigtype java 3.5
		// [i] field:0:required privateKey
		// [i] field:0:required payload
		// [o] field:0:required stringSigned
		// pipeline in
		
		IDataCursor pipelineCursor = pipeline.getCursor();
		String privateKey = IDataUtil.getString(pipelineCursor, "privateKey");
		String payload = IDataUtil.getString(pipelineCursor, "payload");
		
		// process
		
		String sig;
		
		try {
			KeyFactory kf = KeyFactory.getInstance("DSA");
			PrivateKey privKey = kf.generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey)));
			
			//Creating a Signature object
			
			Signature sign = Signature.getInstance("SHA256WithDSA");
			sign.initSign(privKey);
			
			sign.update(payload.getBytes());
			
			sig = Base64.getEncoder().encodeToString(sign.sign());
			
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			throw new ServiceException("Invalid DSA Key: " + e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			throw new ServiceException("No Such Algorithm: " + e.getMessage());
		} catch (SignatureException e) {
			throw new ServiceException("Error signing text:" + e.getMessage());
		} catch (InvalidKeySpecException e) {
			throw new ServiceException("Error Key Encoding:" + e.getMessage());
		}
		
		// pipeline out
		
		IDataUtil.put(pipelineCursor, "signature", sig);
		pipelineCursor.destroy();
			
		// --- <<IS-END>> ---

                
	}



	public static final void verify (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(verify)>> ---
		// @sigtype java 3.5
		// [i] field:0:required publicKey
		// [i] field:0:required payload
		// [i] field:0:required signature
		// [o] object:0:required Untitled
		// pipeline
		IDataCursor pipelineCursor = pipeline.getCursor();
		String publicKey = IDataUtil.getString(pipelineCursor, "publicKey");
		String payload = IDataUtil.getString(pipelineCursor, "payload");			
		String signature = IDataUtil.getString(pipelineCursor, "signature");
		
		boolean valid = false;
		
		try {
			KeyFactory kf = KeyFactory.getInstance("DSA");
			PublicKey pubKey = kf.generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey)));
						
			//Creating a Signature object
			Signature sign = Signature.getInstance("SHA256WithDSA");
			sign.initVerify(pubKey);
			
			sign.update(payload.getBytes());
			
			valid = sign.verify(Base64.getDecoder().decode(signature));
			
		} catch (InvalidKeyException e) {
			throw new ServiceException("Invalid DSA Key: " + e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			throw new ServiceException("No Such Algorithm: " + e.getMessage());
		} catch (SignatureException e) {
			throw new ServiceException("Error signing text:" + e.getMessage());
		} catch (InvalidKeySpecException e) {
			throw new ServiceException("Error Key Encoding:" + e.getMessage());
		}
		
		// pipeline out
		
		IDataUtil.put(pipelineCursor, "isValid", valid);
		pipelineCursor.destroy();
			
		// --- <<IS-END>> ---

                
	}
}

