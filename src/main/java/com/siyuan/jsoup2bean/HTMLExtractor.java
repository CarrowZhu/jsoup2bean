package com.siyuan.jsoup2bean;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * extract the java objects from the HTML
 */
public class HTMLExtractor implements Extractor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HTMLExtractor.class);
	
	/**
	 * used to extract the java objects from the HTML
	 */
	private List<Extractor> extractors = new LinkedList<Extractor>();
	
	public HTMLExtractor() {
	}
	
	/**
	 * extracts java objects from the HTML,
	 * and stores them in a Map
	 * @param html 
	 * @return
	 */
	public Map<String, Object> parse(String html) {
		if (StringUtils.isBlank(html)) {
			throw new IllegalArgumentException("The argument html must not be blank");
		}
		LOGGER.debug("Begin to extract data from the html");
		Map<String, Object> result = new HashMap<String, Object>();
		extract(result, Jsoup.parse(html));
		LOGGER.debug("Finish extracting data from the html");
		return result;
	}
	
	/**
	 * extracts java objects from the HTML,
	 * and stores them in a Map
	 * @param url
	 * @param timeoutMilis
	 * @return
	 */
	public Map<String, Object> parse(URL url, int timeoutMilis) {
		Map<String, Object> result = new HashMap<String, Object>();
		LOGGER.debug("Begin to extract data from the url [{}]", url);
		try {
			extract(result, Jsoup.parse(url, timeoutMilis));
		} catch (IOException e) {
			throw new ExtractException("Fail to extract data from the url [" + url + "]", e);
		}
		LOGGER.debug("Finish extracting data from the url [{}]", url);
		return result;
	}
	
	@Override
	public void extract(Object container, Element element) {
		for (Extractor extractor : extractors) {
			extractor.extract(container, element);
		}
	}
	
	public List<Extractor> getExtractors() {
		return extractors;
	}

	public void setExtractors(List<Extractor> extractors) {
		this.extractors = extractors;
	}

	public void addExtractor(Extractor extractor) {
		this.extractors.add(extractor);
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
