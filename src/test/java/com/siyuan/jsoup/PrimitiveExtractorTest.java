package com.siyuan.jsoup;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.siyuan.jsoup2bean.PrimitiveExtractor;
import com.siyuan.util.DateUtils;

public class PrimitiveExtractorTest {
	
	private Document doc = null;
	
	@Before
	public void setUp() {
		String html = "<div char='c' boolean='false' date='2014-08-11' pattern='第3层'>123</div>";
		doc = Jsoup.parse(html);
	}
	
	@After
	public void tearDown() {
		
	}
	
	@Test
	public void testExtractByte() {
		PrimitiveExtractor extractor = new PrimitiveExtractor();
		extractor.setType("byte");
		extractor.setSelector("div");
		Map<String, Object> container = new HashMap<String, Object>();
		extractor.extract(container, doc);
		Assert.assertEquals((byte) 123, container.get("byte"));
	}
	
	@Test
	public void testExtractChar() {
		PrimitiveExtractor extractor = new PrimitiveExtractor();
		extractor.setType("char");
		extractor.setSelector("div");
		extractor.setValue("char");
		Map<String, Object> container = new HashMap<String, Object>();
		extractor.extract(container, doc);
		Assert.assertEquals('c', container.get("char"));
	}
	
	@Test
	public void testExtractShort() {
		PrimitiveExtractor extractor = new PrimitiveExtractor();
		extractor.setType("short");
		extractor.setSelector("div");
		Map<String, Object> container = new HashMap<String, Object>();
		extractor.extract(container, doc);
		Assert.assertEquals((short) 123, container.get("short"));
	}
	
	@Test
	public void testExtractInt() {
		PrimitiveExtractor extractor = new PrimitiveExtractor();
		extractor.setType("int");
		extractor.setSelector("div");
		Map<String, Object> container = new HashMap<String, Object>();
		extractor.extract(container, doc);
		Assert.assertEquals(123, container.get("int"));
	}
	
	@Test
	public void testExtractLong() {
		PrimitiveExtractor extractor = new PrimitiveExtractor();
		extractor.setType("long");
		extractor.setSelector("div");
		Map<String, Object> container = new HashMap<String, Object>();
		extractor.extract(container, doc);
		Assert.assertEquals((long) 123, container.get("long"));
	}
	
	@Test
	public void testExtracFloat() {
		PrimitiveExtractor extractor = new PrimitiveExtractor();
		extractor.setType("float");
		extractor.setSelector("div");
		Map<String, Object> container = new HashMap<String, Object>();
		extractor.extract(container, doc);
		Assert.assertEquals((float) 123, container.get("float"));
	}
	
	@Test
	public void testExtractDouble() {
		PrimitiveExtractor extractor = new PrimitiveExtractor();
		extractor.setType("double");
		extractor.setSelector("div");
		Map<String, Object> container = new HashMap<String, Object>();
		extractor.extract(container, doc);
		Assert.assertEquals((double) 123, container.get("double"));
	}
	
	@Test
	public void testExtractBoolean() {
		PrimitiveExtractor extractor = new PrimitiveExtractor();
		extractor.setType("boolean");
		extractor.setSelector("div");
		extractor.setValue("boolean");
		Map<String, Object> container = new HashMap<String, Object>();
		extractor.extract(container, doc);
		Assert.assertEquals(false, container.get("boolean"));
	}
	
	@Test
	public void testExtractString() {
		PrimitiveExtractor extractor = new PrimitiveExtractor();
		extractor.setType("string");
		extractor.setSelector("div");
		Map<String, Object> container = new HashMap<String, Object>();
		extractor.extract(container, doc);
		Assert.assertEquals("123", container.get("string"));
	}
	
	@Test
	public void testExtractDate() throws ParseException {
		PrimitiveExtractor extractor = new PrimitiveExtractor();
		extractor.setType("date");
		extractor.setSelector("div");
		extractor.setValue("date");
		Map<String, Object> container = new HashMap<String, Object>();
		extractor.extract(container, doc);
		Assert.assertEquals(DateUtils.parse("2014-08-11", "yyyy-MM-dd"), container.get("date"));
	}
	
	@Test
	public void testPattern() throws ParseException {
		PrimitiveExtractor extractor = new PrimitiveExtractor();
		extractor.setType("string");
		extractor.setSelector("div");
		extractor.setValue("pattern");
		extractor.setPattern("第(.*)层");
		extractor.setGroup(1);
		Map<String, Object> container = new HashMap<String, Object>();
		extractor.extract(container, doc);
		Assert.assertEquals("3", container.get("string"));
	}
	
}
