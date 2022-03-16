package jc.tools.pub;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;
// --- <<IS-END-IMPORTS>> ---

public final class security

{
	// ---( internal utility methods )---

	final static security _instance = new security();

	static security _newInstance() { return new security(); }

	static security _cast(Object o) { return (security)o; }

	// ---( server methods )---




	public static final void generateRSAKeyPair (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(generateRSAKeyPair)>> ---
		// @sigtype java 3.5
		// [i] field:0:optional keySize
		// [i] field:0:optional formatSSHPubKey {"false","true"}
		// [o] field:0:required publicKey
		// [o] field:0:required privateKey
		IDataCursor c = pipeline.getCursor();
		String keySizeStr = IDataUtil.getString(c, "keySize");
		String useSSH = IDataUtil.getString(c, "formatSSHPubKey");
		
		// process
		
		int keySize = 2048;
		
		try { keySize = Integer.parseInt(keySizeStr); } catch (Exception e) {}
		
		String pubStr = null;
		String pvtStr = null;
		
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			
			kpg.initialize(keySize);
			KeyPair kp = kpg.generateKeyPair();
					
			if (useSSH != null && useSSH.equalsIgnoreCase("true")) {
				pubStr = convertToOpenSSHPubkey((RSAPublicKey) kp.getPublic(), "jcart@mac.com");
			} else {
				pubStr = convertToRSAKey(kp.getPublic(), PUB_KEY);
			}
			
			pvtStr = convertToPem(convertToPKS1(kp.getPrivate()));
		
			
		} catch (NoSuchAlgorithmException e) {
			throw new ServiceException("RSA algorithm not supported!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// pipeline out
		
		IDataUtil.put(c, "publicKey", pubStr);
		IDataUtil.put(c, "privateKey", pvtStr);
		c.destroy();
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	
	private static final String PUB_KEY = "PUBLIC";
	
	private static String convertToRSAKey(Key key, String type) throws IOException {
	
		Base64.Encoder encoder = Base64.getEncoder();
		
		StringWriter writer = new StringWriter();
		writer.write("-----BEGIN RSA " + type +" KEY-----\n");
		writer.write(encoder.encodeToString(key.getEncoded()));
		writer.write("\n-----END RSA " + type + " KEY-----\n");
		writer.close();
		
		return writer.toString();
	}
	
	private static String convertToOpenSSHPubkey(RSAPublicKey key, String id) throws IOException {
	
		Base64.Encoder encoder = Base64.getEncoder();
		
		StringWriter writer = new StringWriter();
		
		//String base64PubKey = encoder.encodeToString(key.getEncoded());
	    ByteArrayOutputStream byteOs = new ByteArrayOutputStream();
	    DataOutputStream dos = new DataOutputStream(byteOs);
	    dos.writeInt("ssh-rsa".getBytes().length);
	    dos.write("ssh-rsa".getBytes());
	    dos.writeInt(key.getPublicExponent().toByteArray().length);
	    dos.write(key.getPublicExponent().toByteArray());
	    dos.writeInt(key.getModulus().toByteArray().length);
	    dos.write(key.getModulus().toByteArray());
	    String publicKeyEncoded = new String(encoder.encodeToString(byteOs.toByteArray()));
	    
	    String keyStr =  "ssh-rsa " + publicKeyEncoded + " ";
	    writer.write(keyStr);		
		writer.close();
		
		return writer.toString();
	}
	
	private static byte[] convertToPKS1(PrivateKey priv) throws IOException {
		
		byte[] privBytes = priv.getEncoded();
	
		PrivateKeyInfo pkInfo = PrivateKeyInfo.getInstance(privBytes);
		ASN1Encodable encodable = pkInfo.parsePrivateKey();
		ASN1Primitive primitive = encodable.toASN1Primitive();
		byte[] privateKeyPKCS1 = primitive.getEncoded();
		
		return privateKeyPKCS1;
		//return Base64.getEncoder().encodeToString(privateKeyPKCS1);		
	}
	
	private static String convertToPem(byte[] pkcs1Key) throws IOException {
	
		PemObject pemObject = new PemObject("RSA PRIVATE KEY", pkcs1Key);
		StringWriter stringWriter = new StringWriter();
		PemWriter pemWriter = new PemWriter(stringWriter);
		pemWriter.writeObject(pemObject);
		pemWriter.close();
		
		return stringWriter.toString();
	}
	// --- <<IS-END-SHARED>> ---
}

