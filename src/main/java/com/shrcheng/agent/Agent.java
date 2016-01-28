package com.shrcheng.agent;

import java.lang.instrument.Instrumentation;

public class Agent {
    
    private final Instrumentation instrumentation;
    
    public Agent(Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
    }
    
}
