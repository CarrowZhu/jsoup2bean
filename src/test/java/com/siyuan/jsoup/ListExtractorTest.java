package com.siyuan.jsoup;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.siyuan.entity.Person;
import com.siyuan.jsoup2bean.ListExtractor;
import com.siyuan.jsoup2bean.PrimitiveExtractor;
import com.siyuan.util.DateUtils;

public class ListExtractorTest {
	
	private Document doc = null;
	
	@Before
	public void setUp() {
		String html = "<table>"
				+ "<tr><td>siyuan</td><td>1987-10-01</td><td>26</td></tr>"
				+ "<tr><td>siyuan1</td><td>1988-10-01</td><td>25</td></tr>"
				+ "</table>";
		doc = Jsoup.parse(html);
	}
	
	@After
	public void tearDown() {
		
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void testExtract() throws ParseException {
		ListExtractor extractor = new ListExtractor();
		extractor.setElementType("com.siyuan.entity.Person");
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
		List<Person> persons = (List<Person>) container.get("linkedList");
		Person person = persons.get(0);
		Assert.assertEquals("siyuan", person.getName());
		Assert.assertEquals(DateUtils.parse("1987-10-01", "yyyy-MM-dd"), person.getBirth());
		Assert.assertEquals(26, person.getAge());
		Person person1 = persons.get(1);
		Assert.assertEquals("siyuan1", person1.getName());
		Assert.assertEquals(DateUtils.parse("1988-10-01", "yyyy-MM-dd"), person1.getBirth());
		Assert.assertEquals(25, person1.getAge());
	}
	
}
