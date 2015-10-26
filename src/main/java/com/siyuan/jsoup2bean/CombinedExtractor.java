package com.siyuan.jsoup2bean;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * extract java objects from a Element multiple times
 */
public class CombinedExtractor implements Extractor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CombinedExtractor.class);
	
	/**
	 * jsoup selector expression
	 * Used to get the org.jsoup.nodes.Element, which contains 
	 * the content of the java object to be extracted, from the 
	 * org.jsoup.nodes.Element passed in the extract function.
	 * Stands for the org.jsoup.nodes.Element passed in if blank
	 */
	private String selector;
	
	/**
	 * used to extract the java objects
	 */
	private List<Extractor> extractors = new LinkedList<Extractor>();
	
	@Override
	public void extract(Object container, Element element) {
		
		if (container == null 
				|| element == null) {
			throw new IllegalArgumentException(
					"The argument container and element must not be null");
		}
		
		Element target = element;
		if (StringUtils.isNotBlank(selector)) {
			target = element.select(this.selector).first();
		}
		
		for (Extractor extractor : extractors) {
			LOGGER.debug("Extract the property with extractor [" + extractor + "]");
			extractor.extract(container, target);
		}
		
	}

	public String getSelector() {
		return selector;
	}

	public void setSelector(String selector) {
		this.selector = selector;
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
	
}
