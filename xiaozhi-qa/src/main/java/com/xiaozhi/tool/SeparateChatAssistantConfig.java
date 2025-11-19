package com.xiaozhi.tool;

import com.xiaozhi.service.MongoChatMemoryStore;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SeparateChatAssistantConfig {

    @Autowired
    private MongoChatMemoryStore mongoChatMemoryStore;

    @Bean
    ChatMemoryProvider chatMemoryProvider() {
        return memoryId -> MessageWindowChatMemory.builder()
                .id(memoryId)
                .maxMessages(10)
                .chatMemoryStore(mongoChatMemoryStore)//配置持久化对象
                .build();
    }
/*
    @Bean
    public EmbeddingModel embeddingModel() {
        return HuggingFaceEmbeddingModel.builder()
                .accessToken("your-hf-token") // 可选
                .modelId("sentence-transformers/all-MiniLM-L6-v2")
                .build();
    }*/
}
