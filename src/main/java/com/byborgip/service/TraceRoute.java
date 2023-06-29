package com.byborgip.service;

import com.byborgip.dto.PingResult;
import com.byborgip.dto.Report;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TraceRoute implements Ping {

    private PingResult result;

    @Override
    public void apply(final String host, final PingsConfigurations config, final Report report) {
        this.result = new PingResult();
        final String command = config.TRACE_COMMAND.replace(HOST_IN_COMMAND, host);
        try {
            final ICMP icmp = new ICMP();
            final Process p = Runtime.getRuntime().exec(command);
            final StringBuilder stringBuilder = icmp.getProcessResponse(p);
            if (stringBuilder.toString().isEmpty()) {
                this.result = new PingResult(stringBuilder.toString(), LocalDateTime.now(), false);
            }else {
                this.result = new PingResult(stringBuilder.toString(), LocalDateTime.now(), true);
            }
            p.destroy();
            this.storePingResult(this.result);
        } catch (IOException e) {
            this.result = new PingResult("Something goes wrong", LocalDateTime.now(), false);
            e.printStackTrace();
        }
        this.checkForReport(config, report);

    }

    @Override
    public void runThreadScheduled(final String host, final PingsConfigurations config, final Report report) {
        final ScheduledExecutorService exe = Executors.newSingleThreadScheduledExecutor();
        exe.scheduleWithFixedDelay(() -> this.apply(host, config, report),
                config.TRACE_SCHEDULED_DELAY,
                config.TRACE_SCHEDULED_DELAY,
                TimeUnit.SECONDS
        );
    }

    @Override
    public void storePingResult(final PingResult result) {
        this.result = result;
    }

    @Override
    public PingResult getPingResult() {
        return this.result;
    }


    @Override
    public void checkForReport(final PingsConfigurations configurations, final Report report) {
        report.setTrace(this.getPingResult());
        if (!this.getPingResult().getStatus() ) {
            Ping.sendReport(configurations, report);
            Ping.insertLog(configurations, report);
        }
    }

}
