package com.shrcheng.service.environment;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * environment manager
 * 
 * @author shicheng
 *         
 */
public class Environment {
    
    private final List<List<?>> environmentMap = new ArrayList();
    private static final Pattern JSON_WORKAROUND = Pattern.compile("\\\\+$");
    
    public Environment() {
        OperatingSystemMXBean systemMXBean = ManagementFactory.getOperatingSystemMXBean();
        addVariable("Logical Processors", Integer.valueOf(systemMXBean.getAvailableProcessors()));
        addVariable("Arch", systemMXBean.getArch());
        addVariable("OS version", systemMXBean.getVersion());
        addVariable("OS", systemMXBean.getName());
        addVariable("Java vendor", System.getProperty("java.vendor"));
        addVariable("Java VM", System.getProperty("java.vm.name"));
        addVariable("Java VM version", System.getProperty("java.vm.version"));
        addVariable("Java version", System.getProperty("java.version"));
        addVariable("Log path", null);
        MemoryUsage heapMemoryUsage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
        addVariable("Heap initial (MB)", Float.valueOf((float) heapMemoryUsage.getInit() / 1048576.0F));
        addVariable("Heap max (MB)", Float.valueOf((float) heapMemoryUsage.getMax() / 1048576.0F));
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        List<String> inputArguments = fixInputArguments(runtimeMXBean.getInputArguments());
        String dispatcher = null;
        if (System.getProperty("com.sun.aas.installRoot") != null) {
            dispatcher = "Glassfish";
        } else if (System.getProperty("resin.home") != null) {
            dispatcher = "Resin";
        } else if (System.getProperty("org.apache.geronimo.base.dir") != null) {
            dispatcher = "Apache Geronimo";
        } else if (System.getProperty("weblogic.home") != null) {
            dispatcher = "WebLogic";
        } else if (System.getProperty("wlp.install.dir") != null) {
            dispatcher = "WebSphere Application Server";
        } else if (System.getProperty("was.install.root") != null) {
            dispatcher = "IBM WebSphere Application Server";
        } else if (System.getProperty("jboss.home") != null) {
            dispatcher = "JBoss";
        } else if ((System.getProperty("jboss.home.dir") != null)
                || (System.getProperty("org.jboss.resolver.warning") != null)
                || (System.getProperty("jboss.partition.name") != null)) {
            dispatcher = "JBoss Web";
        } else if (System.getProperty("catalina.home") != null) {
            dispatcher = "Apache Tomcat";
        } else if (System.getProperty("jetty.home") != null) {
            dispatcher = "Jetty";
        }
        addVariable("dispatcher", dispatcher);
        addVariable("Framework", "java");
        this.environmentMap.add(Arrays.asList(new Object[] { "JVM arguments", inputArguments }));
        System.out.println(this.environmentMap);
    }
    
    private void addVariable(String name, Object value) {
        this.environmentMap.add(Arrays.asList(new Object[] { name, value }));
    }
    
    private static List<String> fixInputArguments(List<String> args) {
        List<String> fixed = new ArrayList(args.size());
        for (String arg : args) {
            fixed.add(fixString(arg));
        }
        return fixed;
    }
    
    static String fixString(String arg) {
        Matcher matcher = JSON_WORKAROUND.matcher(arg);
        return matcher.replaceAll("");
    }
    
    public static void main(String[] args) {
        new Environment();
    }
    
}
