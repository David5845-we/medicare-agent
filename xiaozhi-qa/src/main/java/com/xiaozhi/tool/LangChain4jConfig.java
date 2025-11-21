package com.xiaozhi.tool;


import org.springframework.context.annotation.Configuration;

@Configuration
public class LangChain4jConfig {

   /* @Bean
    public QwenChatModel qwenChatModel() {
        return QwenChatModel.builder()
                .apiKey("sk-a9e19fe3575645f49af95c298cdb4da5")
                .modelName("qwen-turbo")
                .temperature(0.7f)
                .topP(0.8)
                .maxTokens(2000)
                .build();
    }*/

    /*@Bean
    public QwenEmbeddingModel embeddingModel() {
        return QwenEmbeddingModel.builder()
                .apiKey("sk-a9e19fe3575645f49af95c298cdb4da5")
                .modelName("text-embedding-v2")
                .build();
    }

    @Bean
    public QwenStreamingChatModel qwenStreamingChatModel() {
        return QwenStreamingChatModel.builder()
                .apiKey("sk-a9e19fe3575645f49af95c298cdb4da5")
                .modelName("qwen-turbo")
                .temperature(0.7f)
                .topP(0.8)
                .maxTokens(2000)
                .build();
    }*/
}
