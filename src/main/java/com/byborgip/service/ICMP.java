package com.byborgip.service;

import com.byborgip.dto.PingResult;
import com.byborgip.dto.Report;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ICMP implements Ping {

    private PingResult result;

    @Override
    public void apply(final String host, final PingsConfigurations config, final Report report) {
        try {
            this.result = new PingResult();
            final String command = config.ICMP_PING_COMMAND.replace(HOST_IN_COMMAND, host);
            final Process p = Runtime.getRuntime().exec(command);
            if (!p.waitFor(config.ICMP_TIMEOUT, TimeUnit.SECONDS)) {
                // set result with error description
                this.result = new PingResult("Time out ...", LocalDateTime.now(), false);
            } else {
                final StringBuilder stringBuilder = this.getProcessResponse(p);
                this.result = new PingResult(stringBuilder.toString(), LocalDateTime.now(), true);
                // check packed infos
                // send this.result to the method
                this.checkPacketInfo(stringBuilder.toString(), this.result);
            }
            p.destroy();
            this.storePingResult(this.result);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        this.checkForReport(config, report);
    }

    public StringBuilder getProcessResponse(Process p) throws IOException {
        final BufferedReader inputStream = new BufferedReader(new InputStreamReader(p.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();
        String inputLine;
        while ((inputLine = inputStream.readLine()) != null) {
            stringBuilder.append(inputLine + "\n");
        }
        inputStream.close();
        return stringBuilder;
    }

    @Override
    public void runThreadScheduled(final String host, final PingsConfigurations config, final Report report) {
        final ScheduledExecutorService exe = Executors.newSingleThreadScheduledExecutor();
        final ScheduledFuture<?> r = exe.scheduleAtFixedRate(
                () -> this.apply(host, config, report),
                config.ICMP_SCHEDULED_DELAY,
                config.ICMP_SCHEDULED_DELAY,
                TimeUnit.SECONDS);
    }

    @Override
    public void storePingResult(final PingResult result) {
        this.result = result;
    }

    @Override
    public PingResult getPingResult() {
        return this.result;
    }


    private void checkPacketInfo(final String response, final PingResult result) {
        final String[] info = response.split("---\n")[1].split(", ");
        final String numberTransmitted = info[0].split(" ")[0];
        final String numberReceived = info[1].split(" ")[0];
        final String packetLoss = info[2].split(" ")[0];
        if (!numberTransmitted.equals(numberReceived)) {
            // error
            result.setResult("The number of packet isn't equal. " + numberTransmitted + " packets transmitted, " + numberReceived + " received.");
            result.setStatus(false);
        } else if (!packetLoss.equals("0%")) {
            // error
            result.setResult("Packet loss occurred, " + packetLoss + " .");
            result.setStatus(false);
        } else {
            // OK
            result.setStatus(true);
            result.setResult(response);
        }
    }


    public void checkForReport(final PingsConfigurations configurations, final Report report) {
        report.setIcmp_ping(this.getPingResult());
        if (!this.getPingResult().getStatus()) {
            Ping.sendReport(configurations, report);
            Ping.insertLog(configurations, report);
        }
    }


}
