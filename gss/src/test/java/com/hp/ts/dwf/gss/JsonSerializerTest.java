//package com.hp.ts.dwf.gss;
//
//import static org.junit.Assert.*;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.junit.Before;
//import org.junit.Test;
//
//public class JsonSerializerTest {
//	
//	private static final String HDR_UPDATE = "WyJnc3MudHMiXQ==;WyJqYXZhLnV0aWwuRGF0ZSJd;WyJPY3QgMjksIDIwMTUgMTA6Mzg6MDEgQU0iXQ==";
//	private static final String HDR_DELTE = "WyJnc3MudGVzdCIsImdzcy5oZWxsbyJd";
//	private JsonSerializer x = new JsonSerializer();
//
//	@Test
//	public void testDeserializeUpdates() throws ClassNotFoundException, IOException {
//		Map<String, Object> map = x.deserializeUpdates(HDR_UPDATE);
//		assertEquals(1, map.size());
//		assertTrue(map.get("gss.ts") != null);
//		Date date = (Date) map.get("gss.ts");
//		assertEquals(1446111481000L, date.getTime());
//	}
//
//	@Test
//	public void testSerializeUpdates() throws IOException {
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("gss.ts", new Date(1446111481000L));
//		String s = x.serializeUpdates(map);
//		assertEquals(HDR_UPDATE, s);
//	}
//	
//	@Test
//	public void testDeserializeDeletes() throws ClassNotFoundException, IOException {
//		List<String> l = x.deserializeDeletes(HDR_DELTE);
//		assertEquals(2, l.size());
//		assertEquals("gss.test", l.get(0));
//		assertEquals("gss.hello", l.get(1));
//	}
//
//	@Test
//	public void testSerializeDeletes() throws IOException {
//		List<String> l = new ArrayList<String>();
//		l.add("gss.test");
//		l.add("gss.hello");
//		String s = x.serializeDeletes(l);
//		assertEquals(HDR_DELTE, s);
//	}
//	
//	
//
//}
