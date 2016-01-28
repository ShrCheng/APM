package com.shrcheng.service.threads;

import java.util.regex.Pattern;

public class ThreadName {
    
//    private static final String DEFAULT_PATTERN = "((?<=[\\W_]|^)([0-9a-fA-F]){4,}(?=[\\W_]|$))|\\d+";
    private final Pattern replacementPattern;
    
    ThreadName() {
        this("((?<=[\\W_]|^)([0-9a-fA-F]){4,}(?=[\\W_]|$))|\\d+");
    }
    
    private ThreadName(String pattern) {
        this.replacementPattern = Pattern.compile(pattern);
    }
    
    public String getThreadName(String name) {
        String renamed = this.replacementPattern.matcher(name).replaceAll("#");
        return renamed.replace('/', '-');
    }
    
}
