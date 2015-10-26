package com.siyuan.jsoup2bean;

import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * implements Extractor to provide a processing template of function extract,
 * which includes 3 steps:
 * 1)getObjectClass
 * 2)getObject
 * 3)storeObject
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractExtractor implements Extractor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractExtractor.class);
	
	/**
	 * The name of container property, where the java object extracted 
	 * will be stored in.
	 * Will use the uncapitalized simple class name of the java object 
	 * extracted if blank
	 */
	private String name;
	
	/**
	 * jsoup selector expression
	 * Used to get the org.jsoup.nodes.Element, which contains 
	 * the content of the java object to be extracted, from the 
	 * org.jsoup.nodes.Element passed in the extract function.
	 * Stands for the org.jsoup.nodes.Element passed in if blank
	 */
	private String selector = "";
	
	/**
	 * the type of the java object to be extracted
	 */
	private String type;
	
	@Override
	public void extract(Object container, Element element) {
		
		if (container == null) {
			LOGGER.debug("return directly as the container is null");
			return;
		}
		if (element == null) {
			LOGGER.debug("return directly as the element is null");
			return;
		}
		
		Class objClass = getObjectClass(container);
		
		Object obj = getObject(objClass, element);
		
		storeObject(container, obj);
		
	}
	
	/**
	 * get the Class of the java object to be extracted
	 * @param container
	 * @return
	 */
	public abstract Class getObjectClass(Object container);
	
	/**
	 * get the extracted java object
	 * @param objClass
	 * @param element
	 * @return
	 */
	public abstract Object getObject(Class objClass, Element element);
	
	/**
	 * store the java object extracted into the container
	 * @param container
	 * @param obj
	 */
	@SuppressWarnings("unchecked")
	public void storeObject(Object container, Object obj) {
		if (container == null || obj == null) {
			return;
		}
		
		String name = this.name;
		if (StringUtils.isBlank(name)) {
			name = getDefaultName(obj);
			LOGGER.debug("The property [name] is null, try to use the uncapitalized simple "
					+ "class name of the extracted java object [{}] instead.", name);
		}
		
		if (container instanceof Map) {
			LOGGER.debug("The container is an instance of Map, the extracted java object "
					+ "will be as a key [{}]", name);
			((Map) container).put(name, obj);
		} else {
			try {
				BeanUtils.setProperty(container, name, obj);
			} catch (Exception e) {
				throw new ExtractException("Fail to store the extracted java object as the "
						+ "property [" + name + "] of the container [" + container + "]", e);
			}
		}
		
	}
	
	/**
	 * get the uncapitalized simple class name of the java object 
	 * to be extracted, invoked when the property name is blank
	 * @param obj
	 * @return
	 */
	protected String getDefaultName(Object obj) {
		return StringUtils.uncapitalize(obj.getClass().getSimpleName());
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSelector() {
		return selector;
	}

	public void setSelector(String selector) {
		this.selector = selector;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
