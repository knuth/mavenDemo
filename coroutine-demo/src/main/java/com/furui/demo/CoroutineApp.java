package com.furui.demo;

import com.offbynull.coroutines.user.Continuation;
import com.offbynull.coroutines.user.Coroutine;
import com.offbynull.coroutines.user.CoroutineRunner;

/**
 * Hello world!
 *https://github.com/yanxinyuan/coroutines-example
 * https://github.com/offbynull/coroutines
 */
public class CoroutineApp {
    /**
     *
     */
    public static final class MyCoroutine implements Coroutine {
        @Override
        public void run(final Continuation c) {
            System.out.println("started");
            for (int i = 0; i < 10; i++) {
                echo(c, i);
            }
        }

        private void echo(final Continuation c, final int x) {
            System.out.println(x);
            c.suspend();
        }
    }

    public static void main(final String[] args) {
        final CoroutineRunner r = new CoroutineRunner(new MyCoroutine());
        r.execute();
        r.execute();
        r.execute();
        r.execute();
    }
}
