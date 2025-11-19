package com.xiaozhi.tool;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;

import static dev.langchain4j.service.spring.AiServiceWiringMode.EXPLICIT;

@AiService(
        wiringMode = EXPLICIT,
        chatMemory = "chatMemory",
        chatMemoryProvider = "chatMemoryProvider",
        chatModel = "qwenChatModel",
        tools = "appointmentTools" //配置tools
)
public interface SeparateChatAssistant {
    /**
     * 分离聊天记录
     * @param memoryId 聊天id
     * @param userMessage 用户消息
     * @return
     */
    @SystemMessage("你是公司助手，今天是{{current_date}}")//系统消息提示词
    String chat(@MemoryId int memoryId, @UserMessage String userMessage);
}
