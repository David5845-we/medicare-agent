package com.xiaozhi.tool;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;

import static dev.langchain4j.service.spring.AiServiceWiringMode.EXPLICIT;

@AiService(
        wiringMode = EXPLICIT,
        chatModel = "qwenChatModel",
        chatMemoryProvider = "chatMemoryProviderMedicalCare",
        tools = "appointmentTools",
        contentRetriever = "contentRetrieverMedicalCare")
public interface MedicalAssistantAgent {
    @SystemMessage(fromResource = "medical-prompt-template.txt")
    String chat(@MemoryId Long memoryId, @UserMessage String userMessage);
}
