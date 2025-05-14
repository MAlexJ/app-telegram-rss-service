package com.malex.configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/*
 * Explanation:
 *
 * Executors.newVirtualThreadPerTaskExecutor() returns an ExecutorService that:
 * - Creates a new virtual thread for every task.
 * - Is ideal for concurrent I/O-bound tasks (e.g., HTTP calls, database queries).
 *
 * Virtual threads are lightweight and scale well compared to platform (OS) threads.
 *
 * shutdown() is still valid — this executor also needs to be shut down to clean up thread factories.
 */
@EnableAsync
@Configuration
public class AsyncEventConfiguration {

    @Bean(name = "virtualThreadExecutor", destroyMethod = "shutdown")
    public ExecutorService virtualThreadExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }
}

