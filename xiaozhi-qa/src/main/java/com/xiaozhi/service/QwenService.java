package com.xiaozhi.service;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class QwenService {

    @Value("${DASHSCOPE_API_KEY:}")
    private String apiKey;

    public String chat(String userMessage) {
        try {
            Generation gen = new Generation();
            GenerationParam param = GenerationParam.builder()
                    .apiKey(apiKey)
                    .model("qwen-turbo")
                    .prompt(userMessage)
                    .build();

            GenerationResult result = gen.call(param);
            return result.getOutput().getChoices().get(0).getMessage().getContent();
        } catch (ApiException | NoApiKeyException | InputRequiredException e) {
            throw new RuntimeException("调用通义千问失败: " + e.getMessage(), e);
        }
    }

    // 流式聊天方法（如果需要）
   /* public void chatStream(String userMessage, StreamResponseHandler handler) {
        try {
            Generation gen = new Generation();
            GenerationParam param = GenerationParam.builder()
                    .apiKey(apiKey)
                    .model("qwen-turbo")
                    .prompt(userMessage)
                    .incrementalOutput(true) // 启用流式输出
                    .build();

            gen.streamCall(param, handler);
        } catch (ApiException | NoApiKeyException e) {
            throw new RuntimeException("调用通义千问流式接口失败: " + e.getMessage(), e);
        }
    }*/

    public interface StreamResponseHandler {
        void onPartialResult(String partialResult);
        void onComplete(String fullResult);
        void onError(Throwable error);
    }
}