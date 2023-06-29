package com.byborgip.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PingFactoryTest {


    private static PingsConfigurations configurations;
    @BeforeAll
    static void init(){
        configurations = PingsConfigurations.getInstance();
    }

    @Test
    void start_parallel_ping_hosts_given_ping_type_then_run_parallel_threads() {

        int pingType = 3;
        PingFactory obj = new PingFactory(configurations);
        ExecutorService executor = mock(ExecutorService.class);
        obj.start(executor,configurations);
        verify(executor,times(pingType*configurations.HOSTS.length)).submit(any(Runnable.class));
    }


}