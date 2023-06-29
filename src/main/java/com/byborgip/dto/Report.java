package com.byborgip.dto;

public class Report {

    private String host;
    private PingResult icmp_ping;
    private PingResult tcp_ping;
    private PingResult trace;

    public Report(String host, PingResult icmp_ping, PingResult tcp_ping, PingResult trace) {
        this.host = host;
        this.icmp_ping = icmp_ping;
        this.tcp_ping = tcp_ping;
        this.trace = trace;
    }

    public Report() {
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public PingResult getIcmp_ping() {
        return icmp_ping;
    }

    public void setIcmp_ping(PingResult icmp_ping) {
        this.icmp_ping = icmp_ping;
    }

    public PingResult getTcp_ping() {
        return tcp_ping;
    }

    public void setTcp_ping(PingResult tcp_ping) {
        this.tcp_ping = tcp_ping;
    }

    public PingResult getTrace() {
        return trace;
    }

    public void setTrace(PingResult trace) {
        this.trace = trace;
    }
}
