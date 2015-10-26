package com.siyuan.jsoup2bean;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * extract a bean from org.jsoup.nodes.Element 
 */
@SuppressWarnings("rawtypes")
public class BeanExtractor extends AbstractExtractor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BeanExtractor.class);
	
	/**
	 * used to extract the java bean properties
	 */
	private List<Extractor> extractors = new LinkedList<Extractor>();
	
	public BeanExtractor() {
	}
	
	@Override
	public Class getObjectClass(Object container) {
		Class objClass = null;
		
		if (StringUtils.isBlank(getType())) {
			try {
				objClass = PropertyUtils.getPropertyType(container, getName());
				LOGGER.debug("The property [type] is blank, try to get the bean Class from the "
						+ "property [{}] of container [{}]", getName(), container);
			} catch (Exception e) {
				throw new ExtractException("Fail to get the bean Class from "
						+ "the property [" + getName() + "] of container [" + container + "]", e);
			}
		} else {
			try {
				objClass = Class.forName(getType());
			} catch (ClassNotFoundException e) {
				throw new ExtractException("Fail to get the bean Class by the name [" 
						+ getType() + "]", e);
			}
		}
		
		return objClass;
	}

	@Override
	public Object getObject(Class objClass, Element element) {
		Object obj;
		try {
			obj = objClass.newInstance();
		} catch (Exception e) {
			throw new ExtractException("Could not new the bean "
					+ "by the default constructor of [" +  objClass + "]", e);
		}
		
		Element objElement = null;
		if (StringUtils.isBlank(getSelector())) {
			LOGGER.debug("The property [selector] is blank, will use the element passed in directly.");
			objElement = element;
		} else {
			LOGGER.debug("The element used to extract the bean is selected with the selector "
					+ "[{}] from the element passed in.", getSelector());
			objElement = element.select(getSelector()).first();
			if (objElement == null) {
				LOGGER.debug("No element is found with the selector [{}] from the element passed in"
						+ ", will return null", getSelector());
				return null;
			}
		}
		
		for (Extractor extractor : extractors) {
			LOGGER.debug("Extract the property of bean with extractor [{}]", extractor);
			extractor.extract(obj, objElement);
		}
		
		return obj;
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
