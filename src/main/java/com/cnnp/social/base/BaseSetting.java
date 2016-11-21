package com.cnnp.social.base;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "base")
public class BaseSetting {
	private String splitchar;
	private String dateFormatter;

	public String getSplitchar() {
		return splitchar;
	}

	public void setSplitchar(String splitchar) {
		this.splitchar = splitchar;
	}

	public String getDateFormatter() {
		return dateFormatter;
	}

	public void setDateFormatter(String dateFormatter) {
		this.dateFormatter = dateFormatter;
	}
	
	
}
