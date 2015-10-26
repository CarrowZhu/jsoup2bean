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

import com.siyuan.entity.Person;
import com.siyuan.jsoup2bean.BeanExtractor;
import com.siyuan.jsoup2bean.PrimitiveExtractor;
import com.siyuan.util.DateUtils;

public class BeanExtractorTest {
	
	private Document doc = null;
	
	@Before
	public void setUp() {
		String html = "<table>"
				+ "<tr><td>siyuan</td><td>1987-10-01</td><td>26</td></tr>"
				+ "</table>";
		doc = Jsoup.parse(html);
	}
	
	@After
	public void tearDown() {
		
	}
	
	@Test
	public void testExtract() throws ParseException {
		BeanExtractor extractor = new BeanExtractor();
		extractor.setType("com.siyuan.entity.Person");
		extractor.setSelector("table tr");
		
		PrimitiveExtractor nameExtractor = new PrimitiveExtractor();
		nameExtractor.setName("name");
		nameExtractor.setSelector("td:eq(0)");
		extractor.addExtractor(nameExtractor);
		
		PrimitiveExtractor birthExtractor = new PrimitiveExtractor();
		birthExtractor.setName("birth");
		birthExtractor.setSelector("td:eq(1)");
		extractor.addExtractor(birthExtractor);
		
		PrimitiveExtractor ageExtractor = new PrimitiveExtractor();
		ageExtractor.setName("age");
		ageExtractor.setSelector("td:eq(2)");
		extractor.addExtractor(ageExtractor);
		
		Map<String, Object> container = new HashMap<String, Object>();
		extractor.extract(container, doc);
		Person person = (Person) container.get("person");
		Assert.assertEquals("siyuan", person.getName());
		Assert.assertEquals(DateUtils.parse("1987-10-01", "yyyy-MM-dd"), person.getBirth());
		Assert.assertEquals(26, person.getAge());
	}
	
}
