/*
 * App.java
 * Copyright 2019 Qunhe Tech, all rights reserved.
 * Qunhe PROPRIETARY/CONFIDENTIAL, any form of usage is subject to approval.
 */

package com.furui.akka.demo;

import akka.dispatch.ExecutionContexts;
import akka.dispatch.Mapper;
import akka.dispatch.OnSuccess;
import scala.Tuple2;
import scala.concurrent.Await;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.util.Try;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static akka.dispatch.Futures.future;
import static akka.dispatch.Futures.sequence;

/**
 * @author furui
 *
 */
public class App {
    /**
     * entry point
     * @param args
     */
    public static void main(final String[] args) {
        System.out.println("start tests");

        final ExecutorService executor = Executors.newFixedThreadPool(4);

        final App app = new App(executor);
        app.test1();
        app.test2();

        executor.shutdown();

        System.out.println("finished tests");
    }

    ///
    final ExecutionContext ec;

    public App(final ExecutorService executor) {
        ec = ExecutionContexts.fromExecutorService(executor);
    }

    public Future<MyData> createDataAsync(final String name, final Long age) {
        return future(new MyRandomDataGenerator(name, age), ec);
    }

    private Future<MyData> booleanCutAsync(
            final Future<MyData> s,
            final Future<MyData> v,
            final String newName) {
        final Future<MyData> result = s.zip(v).map(
                new Mapper<Tuple2<MyData, MyData>, MyData>() {

                    public MyData apply(final scala.Tuple2<MyData, MyData> zipped) {
                        System.out.println(zipped._1.name + " age: " + zipped._1.age);
                        System.out.println(zipped._2.name + " age: " + zipped._2.age);

                        zipped._2.age = 0L;
                        return new MyData(newName, -1L);
                    }
                }, ec);
        return result;
    }

    public void test1() {
        final Future<MyData> s1 = createDataAsync("s1", 100L);
        final Future<MyData> s2 = createDataAsync("s2", 200L);
        final Future<MyData> v1 = createDataAsync("v1", 300L);

        final Future<MyData> c1 = booleanCutAsync(s1, v1, "s1-v1");
        final Future<MyData> c2 = booleanCutAsync(s2, v1, "s2-v1");

        c1.onComplete(new PrintResult<Try<MyData>>(), ec);
        c2.onComplete(new PrintResult<Try<MyData>>(), ec);
    }

    private void test2() {

        final Future<MyData> s1 = createDataAsync("t2-s1", 100L);
        final Future<MyData> s2 = createDataAsync("t2-s2", 200L);
        final Future<MyData> v1 = createDataAsync("t2-v1", 300L);

        final List<Future<MyData>> futures = new ArrayList<Future<MyData>>();
        futures.add(s1);
        futures.add(s2);
        futures.add(v1);

        System.out.println("Akka Futures says: There are " + futures.size()
                + " RandomPause's currently running");

        // compose a sequence of the futures
        final Future<Iterable<MyData>> futuresSequence = sequence(futures, ec);

        // Find the sum of the odd numbers
        final Future<MyData> futureSum = futuresSequence.map(
                new Mapper<Iterable<MyData>, MyData>() {
                    public MyData apply(final Iterable<MyData> ints) {
                        final long sumAges = 0;
                        final MyData sumData = new MyData("sum", 1000L);
                        for (final MyData i : ints) {
                        }
                        return sumData;
                    }
                }, ec);

        try {
            final MyData result = Await.result(futureSum, Duration.apply(10, TimeUnit.SECONDS));
            System.out.println("Result :" + result.name + " " + result.age);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static final class PrintResult<T> extends OnSuccess<T> {
        @Override
        public final void onSuccess(final T t) {
            final Try<MyData> d = (Try<MyData>) (t);
            final MyData data = d.get();
            System.out.println(data.name + "  " + data.age);
        }
    }
}
