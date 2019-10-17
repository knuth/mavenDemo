/*
 * MyRandomDataGenerator.java
 * Copyright 2019 Qunhe Tech, all rights reserved.
 * Qunhe PROPRIETARY/CONFIDENTIAL, any form of usage is subject to approval.
 */

package com.furui.akka.demo;

import java.util.concurrent.Callable;

/**
 * @author furui
 *
 */
public class MyRandomDataGenerator implements Callable<MyData> {

    private final Long millisPause;
    private final String name;
    private final Long age;

    public MyRandomDataGenerator(final String name, final long age) {
        millisPause = Math.round(Math.random() * 3000) + 1000; // 1,000 to 4,000
        //System.out.println(name + " will pause for " + millisPause
        //        + " milliseconds");
        this.name = name;
        this.age = age;
    }

    public MyData call() throws Exception {
        Thread.sleep(millisPause);
        System.out.println(this.name + " was paused for " + millisPause
                + " milliseconds");
        return new MyData(name, age);
    }
}