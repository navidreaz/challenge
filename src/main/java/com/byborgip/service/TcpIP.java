package com.byborgip.service;

import com.byborgip.dto.PingResult;
import com.byborgip.dto.Report;
import com.byborgip.util.HttpUtil;

import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.concurrent.*;

public class TcpIP implements Ping {

    private PingResult result;

    @Override
    public void apply(final String host, final PingsConfigurations config, final Report report) {
        final String uri = "http://"+host;
        this.result = new PingResult();
        try {
            final long start = System.nanoTime();
            final HttpUtil httpUtil = new HttpUtil();
            final CompletableFuture<HttpResponse<String>> response = httpUtil.get(uri, config.TCP_TIMEOUT);
            final HttpResponse<String> httpResponse = response.get(config.TCP_MAX_RESPONSE_TIME, TimeUnit.SECONDS);
            final long responseTime = System.nanoTime() - start;
            if (httpResponse.body().isEmpty() || httpResponse.statusCode()!= 200) {
                this.result.setDateTime(LocalDateTime.now());
            }else {
                this.result.setDateTime(LocalDateTime.now());
                this.result.setStatus(true);
            }
            this.result.setResult("URL:" + httpResponse.uri() + ", response time: " + responseTime + ", httpStatus: " + httpResponse.statusCode());
        } catch (URISyntaxException e) {
            this.result.setDateTime(LocalDateTime.now());
            this.result.setResult("Your uri is not correct" + uri);
            e.printStackTrace();
        } catch (InterruptedException e) {
            this.result.setDateTime(LocalDateTime.now());
            this.result.setResult("Something goes wrong");
            e.printStackTrace();
        } catch (ExecutionException | TimeoutException e) {
            this.result.setDateTime(LocalDateTime.now());
            this.result.setResult("The response time exceeded");
            e.printStackTrace();
        }
        this.checkForReport(config, report);
    }

    @Override
    public void runThreadScheduled(final String host, final PingsConfigurations config, Report report) {
        final ScheduledExecutorService exe = Executors.newSingleThreadScheduledExecutor();
        exe.scheduleWithFixedDelay(() ->
                        this.apply(host, config, report),
                config.TCP_SCHEDULED_DELAY,
                config.TCP_SCHEDULED_DELAY,
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
        report.setTcp_ping(this.getPingResult());
        if (!this.getPingResult().getStatus() ) {
            Ping.sendReport(configurations, report);
            Ping.insertLog(configurations, report);
        }
    }

}
