package com.hp.ts.dwf.gss;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.apache.commons.codec.binary.Base64;



public abstract class JavaSerializationSupport {

    protected String encode(byte[] b) {
        byte[] apacheBytes = Base64.encodeBase64(b);
        String fromApacheBytes = new String(apacheBytes);
        return fromApacheBytes;
    }

    protected byte[] decode(String s) {
        return Base64.decodeBase64(s);
    }

	protected static byte[] toByteArray(Object o) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(o);
		return baos.toByteArray();
	}

	protected static Object toJava(byte[] b) throws IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(b));
		return ois.readObject();
	}
	
}
