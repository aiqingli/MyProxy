package com.hp.ts.dwf.gss;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * GSS Serializer.
 * 
 * @author kelly
 */
public interface Serializer extends Serializable {

	/**
	 * Serialize the map to a HTTP header value.
	 * @param updates
	 * @return
	 * @throws IOException 
	 */
	public String serializeUpdates(Map<String, Object> updates) throws IOException;
	
	/**
	 * Serialize the list of deletes.
	 * @param deletes
	 * @return
	 * @throws IOException
	 */
	public String serializeDeletes(List<String> deletes) throws IOException;
	
	/**
	 * Deserialize the header value to a map.
	 * @param value
	 * @return
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public Map<String, Object> deserializeUpdates(String value) throws ClassNotFoundException, IOException;
	
	/**
	 * Deserialize the deletes head to list.
	 * @param value
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public List<String> deserializeDeletes(String value) throws ClassNotFoundException, IOException;
	
}
