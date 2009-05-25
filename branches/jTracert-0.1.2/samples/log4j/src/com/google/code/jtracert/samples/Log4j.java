package com.google.code.jtracert.samples;

import org.apache.log4j.Logger;

public class Log4j {

    private final static Logger log = Logger.getLogger(Log4j.class);

    public static void main(String[] arguments) throws Exception {
        log.debug("main <<<");
        log.warn("main <<<");
        log.info("main <<<");
        log.error("main <<<");
        log.fatal("main <<<");
        method1();
        method2();
        log.fatal("main >>>");
        log.error("main >>>");
        log.info("main >>>");
        log.warn("main >>>");
        log.debug("main >>>");
    }

    private static void method1() {
        method2();
        method2();
    }

    private static void method2() {
    }

}
