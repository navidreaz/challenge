package com.byborgip;

import com.byborgip.service.PingFactory;
import com.byborgip.service.PingsConfigurations;

public class PingApplication {

    public static void main(String[] args) {

        // load properties
        PingsConfigurations instance = PingsConfigurations.getInstance();

        // call PingFactory
        new PingFactory(instance);
    }
}
