package com.ssongman.consul.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;


@Component
@RefreshScope
public class MyConfig {
	
	@Value("${dataMessage}")
	private String dataMessage;

	public String getDataMessage() {
		return dataMessage;
	}

	public void setDataMessage(String dataMessage) {
		this.dataMessage = dataMessage;
	}

}
