package com.byborgip.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;

public class PingResult {


    private String result;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime dateTime;

    private boolean status;

    public PingResult() {
    }

    public PingResult(String result, LocalDateTime dateTime, boolean status) {
        this.result = result;
        this.dateTime = dateTime;
        this.status = status;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public boolean getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "PingResult{" +
                "result:'" + result + '\'' +
                ", dateTime:" + dateTime +
                ", status:" + status +
                '}';
    }
}
