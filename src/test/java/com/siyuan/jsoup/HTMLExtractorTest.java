package com.siyuan.jsoup;

import java.text.ParseException;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.siyuan.entity.Person;
import com.siyuan.jsoup2bean.BeanExtractor;
import com.siyuan.jsoup2bean.HTMLExtractor;
import com.siyuan.jsoup2bean.PrimitiveExtractor;
import com.siyuan.util.DateUtils;

public class HTMLExtractorTest {
	
	private String html = null;
	
	@Before
	public void setUp() {
		html = "<table>"
				+ "<tr><td>siyuan</td><td>1987-10-01</td><td>26</td></tr>"
				+ "</table>";
	}
	
	@After
	public void tearDown() {
		
	}
	
	@Test
	public void testParse() throws ParseException {
		HTMLExtractor extractor = new HTMLExtractor();
		
		BeanExtractor personExtractor = new BeanExtractor();
		personExtractor.setType("com.siyuan.entity.Person");
		personExtractor.setSelector("table tr");
		
		PrimitiveExtractor nameExtractor = new PrimitiveExtractor();
		nameExtractor.setName("name");
		nameExtractor.setSelector("td:eq(0)");
		personExtractor.addExtractor(nameExtractor);
		
		PrimitiveExtractor birthExtractor = new PrimitiveExtractor();
		birthExtractor.setName("birth");
		birthExtractor.setSelector("td:eq(1)");
		personExtractor.addExtractor(birthExtractor);
		
		PrimitiveExtractor ageExtractor = new PrimitiveExtractor();
		ageExtractor.setName("age");
		ageExtractor.setSelector("td:eq(2)");
		personExtractor.addExtractor(ageExtractor);
		
		extractor.addExtractor(personExtractor);
		
		Map<String, Object> result = extractor.parse(html);
		Person person = (Person) result.get("person");
		Assert.assertEquals("siyuan", person.getName());
		Assert.assertEquals(DateUtils.parse("1987-10-01", "yyyy-MM-dd"), person.getBirth());
		Assert.assertEquals(26, person.getAge());
	}
	
}
