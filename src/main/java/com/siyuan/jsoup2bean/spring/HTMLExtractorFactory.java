package com.siyuan.jsoup2bean.spring;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.digester3.Digester;
import org.apache.commons.digester3.binder.DigesterLoader;
import org.apache.commons.digester3.xmlrules.FromXmlRulesModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import com.siyuan.jsoup2bean.ExtractException;
import com.siyuan.jsoup2bean.HTMLExtractor;

/**
 * parser for HTMLExtractor configuration by Digester
 * 1)xsd file of HTMLExtractor configuration 
 * locates in classpath://htmlExtractor.xsd
 * 2)Digester configuration of HTMLExtractor configuration 
 * locates in classpath://htmlExtractor-Digester.xml
 */
public class HTMLExtractorFactory {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HTMLExtractorFactory.class);
	
	/**
	 * location of Digester configuration
	 */
	public static final String HTMLEXTRACTOR_DIGESTER_CLASSPATH = "/htmlExtractor-Digester.xml";
	
	/**
	 * DigesterLoader for parsing HTMLExtractor configuration
	 */
	private static DigesterLoader loader = null;
	
	static {
		loader = DigesterLoader.newLoader(new FromXmlRulesModule(){

			@Override
			protected void loadRules() {
				LOGGER.debug("Load Digester configuration for HTMLExtractor "
						+ "from the classpath file [/htmlExtractor-Digester.xml].");
				loadXMLRules(HTMLExtractorFactory.class.getResource(HTMLEXTRACTOR_DIGESTER_CLASSPATH));
			}
			
		});
	}
	
	/**
	 * @param digesterCfg
	 * @return
	 */
	public static HTMLExtractor getInstance(InputStream digesterCfg) {
		try {
			Digester digester = loader.newDigester();
			return digester.parse(digesterCfg);
		} catch (Exception e) {
			throw new ExtractException("Fail to parse the digesterCfg[" 
					+ digesterCfg + "] into HTMLExtractor.", e);
		}
	}
	
	/**
	 * @param resource
	 * @return
	 */
	public static HTMLExtractor getInstance(Resource resource) {
		InputStream input = null;
		try {
			Digester digester = loader.newDigester();
			input = resource.getInputStream();
			return digester.parse(input);
		} catch (Exception e) {
			throw new ExtractException("Fail to parse the digesterCfg[" 
					+ resource + "] into HTMLExtractor.", e);
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
