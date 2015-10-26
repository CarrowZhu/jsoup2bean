package com.siyuan.jsoup2bean;

import org.jsoup.nodes.Element;

/**
 * extract java object from the org.jsoup.nodes.Element 
 */
public interface Extractor {
	
	/**
	 * extract java object from the org.jsoup.nodes.Element,
	 * and store it in the container
	 * @param container 
	 * @param element
	 */
	void extract(Object container, Element element);
	
}
