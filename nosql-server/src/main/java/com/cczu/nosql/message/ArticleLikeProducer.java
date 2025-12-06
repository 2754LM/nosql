package com.cczu.nosql.message;

import com.cczu.nosql.constant.RedisKeyConstant;
import com.cczu.nosql.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ArticleLikeProducer {

  @Autowired private RedisService redisService;

  public boolean publish(ArticleLikeMessage event) {
    log.info("发布消息到文章点赞队列: {}", event);
    return redisService.bQueuePush(RedisKeyConstant.getArticleLikeQueueKey(), event);
  }
}
