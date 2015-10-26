package com.siyuan.jsoup2bean;

/**
 * super class of the Exception in extracting
 */
public class ExtractException extends RuntimeException {

	private static final long serialVersionUID = 1592805614610042248L;

	public ExtractException() {
	}

	public ExtractException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExtractException(String message) {
		super(message);
	}

	public ExtractException(Throwable cause) {
		super(cause);
	}
	
}
