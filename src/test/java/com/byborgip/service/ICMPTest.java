package com.byborgip.service;

import com.byborgip.dto.PingResult;
import com.byborgip.dto.Report;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ICMPTest {

    private static PingsConfigurations configurations;

    @BeforeAll
    static void init() {
        configurations = PingsConfigurations.getInstance();
    }

    private static Report report;
    private static PingResult result;
    private static ICMP icmp;

    @BeforeEach
    void setUp() {
        result = new PingResult();

        icmp = new ICMP();
        report = new Report();
        String host = configurations.HOSTS[0];
        report.setHost(host);

    }

    @Test
    void apply_check_status_when_status_true() {

        result.setStatus(true);
        icmp.apply(report.getHost(), configurations, report);

        assertTrue(icmp.getPingResult().getStatus());
    }

    @Test
    void apply_check_status_when_status_false() {

        result.setStatus(false);
        report.setHost(configurations.HOSTS[1]);
        icmp.apply(report.getHost(), configurations, report);

        assertFalse(icmp.getPingResult().getStatus());
    }

    @Test
    void apply_check_result_when_status_true() {

        result.setStatus(true);
        icmp.apply(report.getHost(), configurations, report);

        assertNotNull(icmp.getPingResult().getResult());
        assertNotEquals("", icmp.getPingResult().getResult());
    }

    @Test
    void apply_check_result_when_status_false() {

        result.setStatus(false);
        report.setHost(configurations.HOSTS[1]);
        icmp.apply(report.getHost(), configurations, report);

        assertNotNull(icmp.getPingResult().getResult());
        assertNotEquals("", icmp.getPingResult().getResult());
    }

    @Test
    void apply_check_time_when_status_true() {

        result.setStatus(true);
        icmp.apply(report.getHost(), configurations, report);

        assertNotNull(icmp.getPingResult().getDateTime());
    }

    @Test
    void apply_check_time_when_status_false() {

        result.setStatus(false);
        report.setHost(configurations.HOSTS[1]);
        icmp.apply(report.getHost(), configurations, report);

        assertNotNull(icmp.getPingResult().getDateTime());
    }


    @Test
    void checkForReport_check_report_filled_when_status_true() {

        result.setStatus(true);
        icmp.apply(configurations.HOSTS[0], configurations, report);

        assertThat(report.getIcmp_ping()).isEqualTo(icmp.getPingResult());
    }


    @Test
    void getPingResult_check_filled_when_status_true() {

        result.setStatus(true);
        icmp.apply(configurations.HOSTS[0], configurations, report);

        assertNotNull(icmp.getPingResult());
        assertTrue(icmp.getPingResult().getStatus());
    }


    @Test
    void getProcessResponse_check_filled() {
        String command = "ping -c 3 127.0.0.1";
        final Process p;
        StringBuilder response;
        try {
            p = Runtime.getRuntime().exec(command);
            response = icmp.getProcessResponse(p);
            assertNotEquals("", response.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}