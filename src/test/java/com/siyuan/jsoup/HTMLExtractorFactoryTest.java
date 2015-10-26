package com.siyuan.jsoup;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.siyuan.entity.Person;
import com.siyuan.jsoup2bean.HTMLExtractor;
import com.siyuan.jsoup2bean.spring.HTMLExtractorFactory;
import com.siyuan.util.DateUtils;

public class HTMLExtractorFactoryTest {
	
	private String html = null;
	
	@Before
	public void setUp() {
		html = "<div><table>"
				+ "<tr><td><span>siyuan</span></td><td>1987-10-01</td><td>26</td></tr>"
				+ "</table></div>";
	}
	
	@Test
	public void testGetInstance() throws ParseException {
		InputStream input = null;
		try {
			input = HTMLExtractorFactoryTest.class.getResourceAsStream("/personPage.xml");
			HTMLExtractor extractor = HTMLExtractorFactory.getInstance(input);
			Assert.assertNotNull(extractor);
			
			Map<String, Object> result = extractor.parse(html);
			Person person = (Person) result.get("person");
			Assert.assertEquals("<span>siyuan</span>", person.getName());
			Assert.assertEquals(DateUtils.parse("1987-10-01", "yyyy-MM-dd"), person.getBirth());
			Assert.assertEquals(26, person.getAge());
			
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
				}
			}
		}
	}
	
}
