package com.jojo.ratelimit1.controller;

import com.jojo.ratelimit1.tools.RateLimit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@RequestMapping
@Controller
public class LimiterController {
    @Autowired
    private RedisTemplate redisTemplate;

    // 10 秒中，可以访问10次
    @RateLimit(key = "test", count = 5)
    @GetMapping("/test")
    public @ResponseBody   String luaLimiter() {
        RedisAtomicInteger entityIdCounter = new RedisAtomicInteger("entityIdCounter", redisTemplate.getConnectionFactory());

        DateFormat df = new SimpleDateFormat("\"yyyy-MM-dd HH:mm:ss.SSS\"");
        String date = df.format(new Date());

        return date + " 累计访问次数：" + entityIdCounter.getAndIncrement();
    }
}
