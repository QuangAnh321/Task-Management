package com.example.task_management.services;

import java.time.LocalTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class SSEService {
    
    private final SseEmitter sseEmitter;

    public SSEService() {
        this.sseEmitter = new SseEmitter(Long.MAX_VALUE);
    }

    public SseEmitter streamSseEvent(Object data) {
        var sseExecutor = Executors.newSingleThreadExecutor();
        sseExecutor.execute(() -> {
            try {
                while (true) {
                    var event = SseEmitter.event().data(data);
                    sseEmitter.send(event);
                    Thread.sleep(5000);
                }
            } catch (Exception e) {
                sseEmitter.completeWithError(e);
            }
        });
        return sseEmitter;
    }

}
