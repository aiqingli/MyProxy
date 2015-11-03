package com.hp.ts.dwf.gss;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.commons.codec.binary.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class JsonSerializer implements Serializer {
	
	private static final long serialVersionUID = 1L;
	
	static Log log = LogFactory.getLog(JsonSerializer.class);

	protected String encode(byte[] b) {
        byte[] apacheBytes = Base64.encodeBase64(b);
        String fromApacheBytes = new String(apacheBytes);
        return fromApacheBytes;
    }

    protected byte[] decode(String s) {
        return Base64.decodeBase64(s);
    }

	@Override
	public String serializeUpdates(Map<String, Object> updates) throws IOException {
		Set<String> names = updates.keySet();
		
		Gson gson = new Gson();
		
		List<String> classes = new ArrayList<String>();
		List<Object> values = new ArrayList<Object>();
		for (String name : names) {
			Object o = updates.get(name);
			values.add(o);
			classes.add(o.getClass().getCanonicalName());
		}
		
		String jsonNames = gson.toJson(names);
		String jsonClasses = gson.toJson(classes);
		String jsonValues = gson.toJson(values);
		
		log.debug("GSS attibute names (JSON): " + gson.toJson(names));
		log.debug("GSS attibute classes (JSON): " + gson.toJson(classes));
		log.debug("GSS attibute values (JSON): " + gson.toJson(values));
		
		StringBuilder sb = new StringBuilder(encode(jsonNames.getBytes("UTF-8")));
		sb.append(";");
		sb.append(encode(jsonClasses.getBytes("UTF-8")));
		sb.append(";");
		sb.append(encode(jsonValues.getBytes("UTF-8")));
		
		return sb.toString();
	}

	@Override
	public Map<String, Object> deserializeUpdates(String value) throws IOException, ClassNotFoundException {
		String[] sa = value.split(";");
		String jsonNames = new String(decode(sa[0]), "UTF-8");
		String jsonClasses = new String(decode(sa[1]), "UTF-8");
		String jsonValues = new String(decode(sa[2]), "UTF-8");
		
		log.debug("GSS attibute names (JSON): " + jsonNames);
		log.debug("GSS attibute classes (JSON): " + jsonClasses);
		log.debug("GSS attibute values (JSON): " + jsonValues);
		
		Gson gson = new Gson();
		@SuppressWarnings("unchecked")
		List<String> names = gson.fromJson(jsonNames, List.class);
		@SuppressWarnings("unchecked")
		List<String> clsNames = gson.fromJson(jsonClasses, List.class);
		List<Class<?>> classes = new ArrayList<Class<?>>();
		for (String clsName : clsNames) {
			Class<?> cls = Class.forName(clsName);
			classes.add(cls);
		}
		log.debug("classes: " + classes);
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		JsonParser parser = new JsonParser();
		JsonArray array = parser.parse(jsonValues).getAsJsonArray();
		Iterator<JsonElement> it = array.iterator();
		for (int i = 0; it.hasNext(); i++) {
			Object attrValue = gson.fromJson(it.next(), classes.get(i));
			map.put(names.get(i), attrValue);
		}
		
		log.debug("Map: " + map);
		
		return map;
	}

	@Override
	public String serializeDeletes(List<String> deletes) throws IOException {
		Gson gson = new Gson();
		String jsonNames = gson.toJson(deletes);
		return encode(jsonNames.getBytes("UTF-8"));
	}

	@Override
	public List<String> deserializeDeletes(String value)
			throws ClassNotFoundException, IOException {
		Gson gson = new Gson();
		String[] a = gson.fromJson(new String(decode(value), "UTF-8"), String[].class);
		return Arrays.asList(a);
	}

}
