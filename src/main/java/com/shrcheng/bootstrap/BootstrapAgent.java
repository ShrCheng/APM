package com.shrcheng.bootstrap;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.text.MessageFormat;
import java.util.Collection;

public class BootstrapAgent {
    
    private static long startTime = 0; // start time
    public static final ClassLoader AGENT_CLASSLOADER = BootstrapAgent.class.getClassLoader(); // get
                                                                                               // classLoader
    
    public BootstrapAgent() {
    }
    
    public static void main(String[] args) {
//        Collection<URL> url = Boo
        try {
            ClassLoader classLoader = new URLClassLoader(null);
            Class<?> agentClass = classLoader.loadClass("com.shrcheng.agent.Agent");
            Method main = agentClass.getDeclaredMethod("main", new Class[] { String[].class });
            main.invoke(null, new Object[] { args });
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    
    public static void premain(String agentArgs, Instrumentation instrumentation) {
        String javaVersion = System.getProperty("java.version", "");
        if (javaVersion.startsWith("1.5")) {
            String msg = MessageFormat.format(
                    "Java version is: {0}.  This version of the Agent does not support Java 1.5.  Please use a later version of Java.",
                    new Object[] { javaVersion });
            System.err.println(msg);
            return;
        }
    }
    
    /**
     * start agent manager
     */
    static void startAgent(String agentArgs, Instrumentation instrumentation) {
        try {
            startTime = System.currentTimeMillis();
            // load agent class
            Class<?> agentClass = ClassLoader.getSystemClassLoader().loadClass("com.shrcheng.agent.Agent");
            Method premain = agentClass.getDeclaredMethod("premain",
                    new Class[] { String.class, Instrumentation.class });
            premain.invoke(null, new Object [] {agentClass, instrumentation}); // continue invoke
        } catch (Throwable throwable) {
            System.err.println(MessageFormat.format("Error bootstrapping agent: {0}", new Object[] { throwable }));
            throwable.printStackTrace();
        }
    }
    
    public static long getAgentStartTime() {
        return startTime;
    }
    
}
