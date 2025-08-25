package com.example.msdesafiotecnico.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@SpringBootTest
@ActiveProfiles("test")
class CacheConfigTest {

    @Test
    void testCacheManagerCreation() {
        // Given
        CacheConfig cacheConfig = new CacheConfig();
        RedisConnectionFactory mockConnectionFactory = mock(RedisConnectionFactory.class);

        // When
        CacheManager cacheManager = cacheConfig.cacheManager(mockConnectionFactory);

        // Then
        assertThat(cacheManager).isNotNull();
        assertThat(cacheManager).isInstanceOf(RedisCacheManager.class);
    }
}

class AsyncConfigTest {

    @Test
    void testTaskExecutorCreation() {
        // Given
        AsyncConfig asyncConfig = new AsyncConfig();

        // When
        var executor = asyncConfig.taskExecutor();

        // Then
        assertThat(executor).isNotNull();
        assertThat(executor.getClass().getSimpleName()).contains("ThreadPoolTaskExecutor");
    }
}
