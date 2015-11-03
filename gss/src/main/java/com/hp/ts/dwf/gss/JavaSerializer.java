package com.hp.ts.dwf.gss;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JavaSerializer extends JavaSerializationSupport implements Serializer {

	private static final long serialVersionUID = 1L;

	public String serialize(Object o) throws IOException {
		return encode(toByteArray(o));
	}

	public Object deserialize(String value) throws ClassNotFoundException, IOException {
		return toJava(decode(value));
	}
	@Override
	public String serializeUpdates(Map<String, Object> updates) throws IOException {
		return serialize(updates);
	}

	@Override
	public String serializeDeletes(List<String> deletes) throws IOException {
		return serialize(deletes);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> deserializeUpdates(String value) throws ClassNotFoundException, IOException {
		return (Map<String, Object>) deserialize(value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> deserializeDeletes(String value)
			throws ClassNotFoundException, IOException {
		return (List<String>) deserialize(value);
	}
	
}
