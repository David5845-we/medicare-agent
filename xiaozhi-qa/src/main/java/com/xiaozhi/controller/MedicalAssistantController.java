package com.xiaozhi.controller;

import com.xiaozhi.dto.ChatForm;
import com.xiaozhi.tool.MedicalAssistantAgent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Medical Assistant")
@RestController
@RequestMapping("/medical")
public class MedicalAssistantController {
    @Autowired
    private MedicalAssistantAgent medicalAssistantAgent;
    @Operation(summary = "对话")
    @PostMapping("/chat")
    public String chat(@RequestBody ChatForm chatForm) {
        return medicalAssistantAgent.chat(chatForm.getMemoryId(), chatForm.getMessage());
    }
}
