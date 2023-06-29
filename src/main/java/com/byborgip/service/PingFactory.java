package com.byborgip.service;

import com.byborgip.dto.Report;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.byborgip.service.Ping.PING_METHODS;

public class PingFactory {

    private final PingsConfigurations configurations;
    private final ExecutorService executor;

    public PingFactory(PingsConfigurations configurations) {
        this.configurations = configurations;
        executor = Executors.newFixedThreadPool(PING_METHODS * configurations.HOSTS.length);
        start(executor, configurations);
    }


    public void start(final ExecutorService executor, final PingsConfigurations configurations) {

        // send a new instance of Report
        // in every pingType if an error occurred then call sendReport in Ping interface
        Arrays.stream(configurations.HOSTS).parallel().forEach(host -> {
            ICMP icmp = new ICMP();
            TcpIP tcpIP = new TcpIP();
            TraceRoute traceRoute = new TraceRoute();
            Report report = new Report();
            report.setHost(host);
            executor.submit(() -> icmp.runThreadScheduled(host, configurations, report));
            executor.submit(() -> tcpIP.runThreadScheduled(host, configurations, report));
            executor.submit(() -> traceRoute.runThreadScheduled(host, configurations, report));

        });
    }


}
