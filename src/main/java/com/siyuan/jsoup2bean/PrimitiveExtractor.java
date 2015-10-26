package com.siyuan.jsoup2bean;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siyuan.util.DateUtils;

/**
 * extract a primitive value from org.jsoup.nodes.Element 
 * primitive types are included in {@link #PRIMITIVE_WRAPPER_TYPES},
 * whose aliases is listed in {@link #PRIMITIVE_TYPES}
 */
@SuppressWarnings("rawtypes")
public class PrimitiveExtractor extends AbstractExtractor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PrimitiveExtractor.class);
	
	/**
	 * aliases of primitive types
	 */
	public static String[] PRIMITIVE_TYPES = {
		"byte",
		"char",
		"short",
		"int",
		"long",
		"float",
		"double",
		"boolean",
		"string",
		"date"
	};
	
	/**
	 * primitive types
	 */
	public static String[] PRIMITIVE_WRAPPER_TYPES = {
		"java.lang.Byte",
		"java.lang.Character",
		"java.lang.Short",
		"java.lang.Integer",
		"java.lang.Long",
		"java.lang.Float",
		"java.lang.Double",
		"java.lang.Boolean",
		"java.lang.String",
		"java.util.Date"
	};
	
	public static Map<String, Object> DEFAULT_PRIMITIVE_VALUES 
		= new HashMap<String, Object>();
	
	/**
	 * default data format
	 */
	public static final String DEFAULT_DATAFORMAT = "yyyy-MM-dd";
	
	static {
		DEFAULT_PRIMITIVE_VALUES.put(PRIMITIVE_WRAPPER_TYPES[0], (byte) 0);
		DEFAULT_PRIMITIVE_VALUES.put(PRIMITIVE_WRAPPER_TYPES[1], new Character('\u0000'));
		DEFAULT_PRIMITIVE_VALUES.put(PRIMITIVE_WRAPPER_TYPES[2], (short) 0);
		DEFAULT_PRIMITIVE_VALUES.put(PRIMITIVE_WRAPPER_TYPES[3], 0);
		DEFAULT_PRIMITIVE_VALUES.put(PRIMITIVE_WRAPPER_TYPES[4], (long) 0);
		DEFAULT_PRIMITIVE_VALUES.put(PRIMITIVE_WRAPPER_TYPES[5], 0.0f);
		DEFAULT_PRIMITIVE_VALUES.put(PRIMITIVE_WRAPPER_TYPES[6], 0.0);
		DEFAULT_PRIMITIVE_VALUES.put(PRIMITIVE_WRAPPER_TYPES[7], false);
	}
	
	/**
	 * the expression of :eq(index) is different between Jsoup and jQuery,
	 * to get the function of jQuery, use index instead of :eq(index)
	 */
	private int index = 0;
	
	/**
	 * the position of the primitive string value in the Element
	 * it is the text node if null,
	 * otherwise it is the attribute whose name is value
	 */
	private String value;
	
	/**
	 * the primitive string value is text or html
	 */
	private boolean text = true;
	
	/**
	 * format used to parse string into primitive value
	 * only active when the primitive type is java.util.Date
	 */
	private String format = DEFAULT_DATAFORMAT;
	
	/**
	 * the pattern used to extract the real primitive string value
	 */
	private String pattern;
	
	/**
	 * the group index of the real primitive string value
	 * in the Matcher
	 */
	private int group;
	
	private Pattern patternObj;
	
	public PrimitiveExtractor() {
	}
	
	@Override
	public Class getObjectClass(Object container) {
		String className = "";
		if (StringUtils.isBlank(getType())) {
			try {
				className = formatType(PropertyUtils.getPropertyType(container, getName()).getName());
				LOGGER.debug("The property [type] is blank, try to get the primitive Class from "
						+ "the property [{}] of container [{}]", getName(), container);
			} catch (Exception e) {
				throw new ExtractException("Fail to get the primitive Class from "
						+ "the property [" + getName() + "] of container [" + container + "]", e);
			}
		} else {
			className = formatType(getType());
		}
		
		boolean support = false;
		for (int i = 0; i < PRIMITIVE_WRAPPER_TYPES.length; i++) {
			if (PRIMITIVE_WRAPPER_TYPES[i].equals(className)) {
				support = true;
			}
		}
		if (!support) {
			throw new ExtractException("The type [" + className + "] is not supported, "
					+ "only the following value is valid:\n"
					+ Arrays.toString(PRIMITIVE_TYPES) + "\n"
					+ Arrays.toString(PRIMITIVE_WRAPPER_TYPES));
		}
		
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new ExtractException("Fail to get the Class by the name [" + getType() + "]");
		}
	}
	
	private String formatType(String type) {
		for (int i = 0; i < PRIMITIVE_TYPES.length; i++) {
			if (PRIMITIVE_TYPES[i].equals(type)) {
				LOGGER.debug("Transfer the primitive name [{}] into [{}]", type, PRIMITIVE_WRAPPER_TYPES[i]);
				return PRIMITIVE_WRAPPER_TYPES[i];
			}
		}
		return type;
	}
	
	@Override
	public Object getObject(Class objClass, Element element) {
		Element objElement = null;
		if (StringUtils.isBlank(getSelector())) {
			LOGGER.debug("The property [selector] is blank, will use the element passed in directly.");
			objElement = element;
		} else {
			LOGGER.debug("The element used to extract the primitive value is selected with "
					+ "the selector [{}] from the element passed in.", getSelector());
			Elements selected = element.select(getSelector());
			if (selected.size() >= index + 1) {
				objElement = selected.get(index);
			}
			if (objElement == null) {
				LOGGER.debug("No element is found with the selector [{}] and the index [{}] "
						+ "from the element passed in. Null is returned as the primitive value", 
						getSelector(), index);
				return DEFAULT_PRIMITIVE_VALUES.get(objClass.getName());
			}
		}
		
		String objStr = null;
		if (StringUtils.isBlank(value)) {
			LOGGER.debug("The property [value] is blank, the primitive string value is "
					+ "extracted from the text node of the element.");
			if (text) {
				objStr = objElement.text();
			} else {
				objStr = objElement.html();
			}
		} else {
			LOGGER.debug("The primitive string value is extracted from "
					+ "the attribute [{}] of the element.", value);
			objStr = objElement.attr(value);
		}
		
		if (StringUtils.isNotBlank(objStr) && this.patternObj != null) {
			Matcher matcher = patternObj.matcher(objStr);
			if (matcher.matches()) {
				objStr = matcher.group(group);
			} else {
				objStr = null;
			}
		}
		
		if (StringUtils.isBlank(objStr)) {
			return DEFAULT_PRIMITIVE_VALUES.get(objClass.getName());
		}
		
		Object obj = null;
		String objClassName = objClass.getName();
		try {
			LOGGER.debug("Try to parse the primitive string value [{}] into [{}].", objStr, objClassName);
			if (PRIMITIVE_WRAPPER_TYPES[0].equals(objClassName)) {
				obj = new Byte(objStr);
			} else if (PRIMITIVE_WRAPPER_TYPES[1].equals(objClassName)) {
				if (objStr.length() == 0) {
					LOGGER.debug("The Character is set to \u0000 as the length of the string is 0.");
					obj = new Character('\u0000');
				} else if (objStr.length() == 1) {
					obj = objStr.charAt(0);
				} else {
					throw new ExtractException("The length of the string [" + objStr 
							+ "] is bigger than 1, can not be parsed into Character.");
				}
			} else if (PRIMITIVE_WRAPPER_TYPES[2].equals(objClassName)) {
				obj = Short.parseShort(objStr);
			} else if (PRIMITIVE_WRAPPER_TYPES[3].equals(objClassName)) {
				obj = Integer.parseInt(objStr);
			} else if (PRIMITIVE_WRAPPER_TYPES[4].equals(objClassName)) {
				obj = Long.parseLong(objStr);
			} else if (PRIMITIVE_WRAPPER_TYPES[5].equals(objClassName)) {
				obj = Float.parseFloat(objStr);
			} else if (PRIMITIVE_WRAPPER_TYPES[6].equals(objClassName)) {
				obj = Double.parseDouble(objStr);
			} else if (PRIMITIVE_WRAPPER_TYPES[7].equals(objClassName)) {
				if ("false".equals(objStr) || "0".equals(objStr)) {
					LOGGER.debug("The string [{}] is parsed into false.", objStr);
					obj = false;
				} else {
					LOGGER.debug("The string [{}] is parsed into true.", objStr);
					obj = true;
				}
			} else if (PRIMITIVE_WRAPPER_TYPES[8].equals(objClassName)) {
				obj = objStr;
			} else if (PRIMITIVE_WRAPPER_TYPES[9].equals(objClassName)) {
				LOGGER.debug("Try to parse the string [{}] into Date with the format [{}].", objStr, format);
				obj = DateUtils.parse(objStr, format);
			}
		} catch (Exception e) {
			throw new ExtractException("Fail to parse the string [" + objStr + "] into " + objClassName, e);
		}
		
		return obj;
	}
	
	@Override
	public String getDefaultName(Object obj) {
		String objClassName = obj.getClass().getName();
		for (int i = 0; i < PRIMITIVE_WRAPPER_TYPES.length; i++) {
			if (PRIMITIVE_WRAPPER_TYPES[i].equals(objClassName))  {
				return PRIMITIVE_TYPES[i];
			}
		}
		throw new ExtractException("Fail to get the default name for class[" + objClassName + "]");
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		if (index < 0) {
			throw new IllegalArgumentException(
					"The index is used to get data from a List, must not be smaller than 0.");
		}
		this.index = index;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public boolean isText() {
		return text;
	}

	public void setText(boolean text) {
		this.text = text;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
		this.patternObj = Pattern.compile(this.pattern);
	}

	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
	}

}
