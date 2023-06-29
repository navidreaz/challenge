package com.byborgip.service;

import com.byborgip.dto.PingResult;
import com.byborgip.dto.Report;
import com.byborgip.util.HttpUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public interface Ping {

    int PING_METHODS = 3;
    String HOST_IN_COMMAND = "HOST";

    Logger logger = Logger.getLogger(Ping.class.getName());

    void apply(String host, PingsConfigurations config, final Report report);

    void runThreadScheduled(String host, PingsConfigurations config, Report report);

    void storePingResult(final PingResult result);

    PingResult getPingResult();

    void checkForReport(final PingsConfigurations configurations, final Report report);

    static void sendReport(PingsConfigurations config, Report report) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            synchronized (Ping.class) {
                String body = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(report);
                new HttpUtil().post(config.REPORT_URL, body);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        catch (URISyntaxException e) {
            e.printStackTrace();

        }
    }

    static void insertLog(PingsConfigurations config, Report report) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String data = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(report);
            synchronized (Ping.class) {
                FileHandler fh = new FileHandler(config.LOG_LOCATION_FILE, true);
                logger.addHandler(fh);
                fh.setLevel(Level.WARNING);
                SimpleFormatter simpleFormatter = new SimpleFormatter();
                fh.setFormatter(simpleFormatter);
                logger.log(Level.WARNING, data);
                fh.close();
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
