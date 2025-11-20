package com.xiaozhi.controller;

import com.xiaozhi.dto.ChatForm;
import com.xiaozhi.tool.MedicalAssistantAgent;
import com.xiaozhi.tool.MedicalAssistantStreamAgent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Tag(name = "Medical Assistant")
@RestController
@RequestMapping("/medical")
public class MedicalAssistantController {
    @Autowired
    private MedicalAssistantAgent medicalAssistantAgent;

    @Autowired
    private MedicalAssistantStreamAgent medicalAssistantStreamAgent;


    @Operation(summary = "对话")
    @PostMapping("/chat")
    public String chat(@RequestBody ChatForm chatForm) {
        return medicalAssistantAgent.chat(chatForm.getMemoryId(), chatForm.getMessage());
    }


    @Operation(summary = "对话")
    @PostMapping("/stream/chat")
    public Flux<String> chatStream(@RequestBody ChatForm chatForm) {
        return medicalAssistantStreamAgent.chat(chatForm.getMemoryId(), chatForm.getMessage());
    }
}
