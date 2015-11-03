//package com.hp.ts.dwf.gss;
//
//import static org.junit.Assert.*;
//
//import java.io.IOException;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.junit.Test;
//
//public class JavaSerializerTest {
//	
//	private static final String HEADER = "rO0ABXNyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRocmVzaG9sZHhwP0AAAAAAAAx3CAAAABAAAAABdAAGZ3NzLnRzc3IADmphdmEudXRpbC5EYXRlaGqBAUtZdBkDAAB4cHcIAAABULL2DKh4eA==";
//	private JavaSerializer x = new JavaSerializer();
//
//	@Test
//	public void testSerialize() throws IOException {
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("gss.ts", new Date(1446111481000L));
//		String s = x.serializeUpdates(map);
//		assertEquals(HEADER, s);
//	}
//
//	@Test
//	public void testDeserialize() throws ClassNotFoundException, IOException {
//		Map<String, Object> map = x.deserializeUpdates(HEADER);
//		assertEquals(1, map.size());
//		assertTrue(map.get("gss.ts") != null);
//		Date date = (Date) map.get("gss.ts");
//		assertEquals(1446111481000L, date.getTime());
//	}
//
//}
