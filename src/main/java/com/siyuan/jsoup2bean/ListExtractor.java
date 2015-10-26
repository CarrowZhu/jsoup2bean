package com.siyuan.jsoup2bean;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * extract a List from org.jsoup.nodes.Element
 */
@SuppressWarnings("rawtypes")
public class ListExtractor extends AbstractExtractor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ListExtractor.class);
	
	/**
	 * default type of the List
	 */
	public static final String DEFAULT_TYPE = "java.util.LinkedList";
	
	/**
	 * type of the List element
	 */
	private String elementType;
	
	/**
	 * used to extract the List element
	 */
	private List<Extractor> extractors = new LinkedList<Extractor>();
	
	public ListExtractor() {
	}
	
	@Override
	public Class getObjectClass(Object container) {
		String type = getType();
		if (StringUtils.isBlank(type)) {
			type = DEFAULT_TYPE;
			LOGGER.debug("The property [type] is blank, will use the [{}] instead.", DEFAULT_TYPE);
		}
		
		try {
			return Class.forName(type);
		} catch (ClassNotFoundException e) {
			throw new ExtractException("Fail to get the List Class by the name [" + type + "].", e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object getObject(Class objClass, Element element) {
		List<Object> list = null;
		try {
			list = (List<Object>) objClass.newInstance();
		} catch (Exception e) {
			throw new ExtractException("Could not new the List from the default "
					+ "constructor of [" +  objClass + "]", e);
		}
		
		Elements elements = null;
		if (StringUtils.isBlank(getSelector())) {
			LOGGER.debug("The property [selector] is blank, only one List element will "
					+ "be extracted with the element passed in.");
			elements = new Elements();
			elements.add(element);
		} else {
			LOGGER.debug("Try to get the Elements with the selector "
					+ "[{}] from the element passed in.", getSelector());
			elements = element.select(getSelector());
		}
		for (Element objElement : elements) {
			Object object;
			try {
				LOGGER.debug("Extract the List element with jsoup element [{}]", objElement);
				object = Class.forName(this.elementType).newInstance();
				for (Extractor extractor : extractors) {
					LOGGER.debug("Extract the property of List element with extractor [{}]", extractor);
					extractor.extract(object, objElement);
				}
				list.add(object);
			} catch (Exception e) {
				throw new ExtractException("Fail to extract the List element "
						+ "from the jsoup element [" + objElement+ "]", e);
			}
		}
		
		return list;
	}
	
	public String getElementType() {
		return elementType;
	}

	public void setElementType(String elementType) {
		this.elementType = elementType;
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
