package com.gkttk.monitoring.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.mail")
public record MailProperties(String host, int port, String username, String password, String protocol,
                             boolean auth, boolean starttlsEnabled, boolean debug) {

}
