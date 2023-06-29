package com.byborgip.service;

import com.byborgip.PingApplication;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PingsConfigurations {

    private static final String config = "config.properties";
    private static final String HOSTS_ = "HOSTS";
    private static final String COMMA_REGEX = ",";

    public final String[] HOSTS;
    public final int ICMP_SCHEDULED_DELAY ;
    public final String ICMP_PING_COMMAND;
    public final int ICMP_TIMEOUT;

    public final int TCP_SCHEDULED_DELAY;
    public final int TCP_TIMEOUT;
    public final int TCP_MAX_RESPONSE_TIME;

    public final int TRACE_SCHEDULED_DELAY;
    public final String TRACE_COMMAND;
    public final int TRACE_TIMEOUT;

    public final String REPORT_URL;

    public final String LOG_LEVEL;
    public final String LOG_LOCATION_FILE;

    private static PingsConfigurations instance;

    private PingsConfigurations() {
        try (InputStream inputStream = PingApplication.class.getClassLoader().getResourceAsStream(config)) {
            Properties properties = new Properties();

            properties.load(inputStream);
            HOSTS = properties.getProperty(HOSTS_).split(COMMA_REGEX);
            ICMP_SCHEDULED_DELAY = Integer.parseInt(properties.getProperty("ICMP_SCHEDULED_DELAY"));
            ICMP_PING_COMMAND = properties.getProperty("ICMP_PING_COMMAND");
            ICMP_TIMEOUT = Integer.parseInt(properties.getProperty("ICMP_TIMEOUT"));
            TCP_SCHEDULED_DELAY = Integer.parseInt(properties.getProperty("TCP_SCHEDULED_DELAY"));
            TCP_TIMEOUT = Integer.parseInt(properties.getProperty("TCP_TIMEOUT"));
            TCP_MAX_RESPONSE_TIME = Integer.parseInt(properties.getProperty("TCP_MAX_RESPONSE_TIME"));
            TRACE_SCHEDULED_DELAY = Integer.parseInt(properties.getProperty("TRACE_SCHEDULED_DELAY"));
            TRACE_COMMAND = properties.getProperty("TRACE_COMMAND");
            TRACE_TIMEOUT = Integer.parseInt(properties.getProperty("TRACE_TIMEOUT"));
            REPORT_URL = properties.getProperty("REPORT_URL");
            LOG_LEVEL = properties.getProperty("LOG_LEVEL");
            LOG_LOCATION_FILE = properties.getProperty("LOG_LOCATION_FILE");

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("There is not config");
        }
    }


    public static PingsConfigurations getInstance() {
        if (instance == null) {
            instance = new PingsConfigurations();
        }
        return instance;
    }


}
