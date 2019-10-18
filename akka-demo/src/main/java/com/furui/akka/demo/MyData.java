/*
 * MyData.java
 * Copyright 2019 Qunhe Tech, all rights reserved.
 * Qunhe PROPRIETARY/CONFIDENTIAL, any form of usage is subject to approval.
 */

package com.furui.akka.demo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author furui
 *
 */
@Data
@AllArgsConstructor
public class MyData {
    private String name;
    private Long age;
}