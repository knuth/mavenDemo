package com.furui.demo;

import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(final String[] args) {
        System.out.println("Redis demo>>>");

        final Jedis jedis = new Jedis();
        jedis.set("events/city", "32,15,223,828");
        final String cachedValue = jedis.get("events/city");

        System.out.println(cachedValue);

        jedis.lpush("queue", "first");
        jedis.lpush("queue", "second");

        final String task = jedis.rpop("queue");
        System.out.println(task);

        //Sets
        jedis.sadd("nicknames", "jerry");
        jedis.sadd("nicknames", "tom");
        final Set<String> names = jedis.smembers("nicknames");
        final boolean exists = jedis.sismember("nicknames", "jerry");

        //hashes
        jedis.hset("user#1", "name", "peter");
        jedis.hset("user#1", "job", "politician");

        final String name = jedis.hget("user#1", "name");
        System.out.println(name);

        //transaction
    }
}
