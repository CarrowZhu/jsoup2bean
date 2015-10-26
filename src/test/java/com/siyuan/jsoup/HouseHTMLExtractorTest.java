package com.siyuan.jsoup;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

import com.siyuan.entity.House;
import com.siyuan.jsoup2bean.HTMLExtractor;
import com.siyuan.jsoup2bean.spring.HTMLExtractorFactory;

public class HouseHTMLExtractorTest {
	
	@Test
	public void testGetInstance() throws ParseException, IOException {
		InputStream input = null;
		InputStream source = null;
		try {
			input = HTMLExtractorFactoryTest.class.getResourceAsStream("/house.xml");
			HTMLExtractor extractor = HTMLExtractorFactory.getInstance(input);
			Assert.assertNotNull(extractor);
			
			source = HTMLExtractorFactoryTest.class.getResourceAsStream("/house.html");
			Document doc = Jsoup.parse(source, "utf8", "http://shenzhen.qfang.com/sale/7699845");
			
			Map<String, Object> container = new HashMap<String, Object>();
			extractor.extract(container, doc);
			House house = (House) container.get("house");
			System.out.println(house);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
				}
			}
			if (source != null) {
				try {
					source.close();
				} catch (IOException e) {
				}
			}
		}
	}
	
}
