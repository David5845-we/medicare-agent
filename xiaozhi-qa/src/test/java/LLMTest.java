

import com.xiaozhi.MedicalAssistant;
import com.xiaozhi.dto.ChatMessages;
import com.xiaozhi.tool.Assistant;
import com.xiaozhi.tool.MemoryChatAssistant;
import com.xiaozhi.tool.SeparateChatAssistant;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

@SpringBootTest(classes = MedicalAssistant.class)

public class LLMTest {
    //@Test
    public void testGPTDemo() {
//初始化模型
        OpenAiChatModel model = OpenAiChatModel.builder()
                .baseUrl("http://langchain4j.dev/demo/openai/v1")
                .apiKey("demo")
                .modelName("gpt-4o-mini") //设置模型名称
                .build();
        //向模型提问
        String answer = model.chat("你好");
//输出结果
        System.out.println(answer);

//LangChain4j提供的代理服务器，该代理服务器会将演示密钥替换成真实密钥， 再将请求转
       // 发给OpenAI API
//.baseUrl("http://langchain4j.dev/demo/openai/v1") //设置模型api地址（如
        //果apiKey="demo"，则可省略baseUrl的配置）
//.apiKey("demo") //设置模型apiKey
                //.modelName("gpt-4o-mini") //设置模型名称
                //.build();

    }

    //@Autowired
    //private OpenAiChatModel openAiChatModel;
    //@Test
   /* public void testSpringBoot() {
//向模型提问
        String answer = openAiChatModel.chat("你好");
//输出结果
        System.out.println(answer);
    }*/

    @Autowired
    private QwenChatModel qwenChatModel;
    @Test
    public void testChat() {
//创建AIService
        Assistant assistant = AiServices.create(Assistant.class, qwenChatModel);
//调用service的接口
        String answer = assistant.chat("Hello");
       System.out.println(answer);
    }


    @Test
    public void testDashScopeQwen() {
//向模型提问
        String answer = qwenChatModel.chat("你好");
//输出结果
        System.out.println(answer);
    }

    @Test
    public void testChatMemory3() {
//创建chatMemory
        MessageWindowChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);
//创建AIService
        Assistant assistant = AiServices
                .builder(Assistant.class)
                .chatLanguageModel(qwenChatModel)
                .chatMemory(chatMemory)
                .build();
//调用service的接口
        String answer1 = assistant.chat("我是David");
        System.out.println(answer1);
        String answer2 = assistant.chat("我是谁");
        System.out.println(answer2);
    }

    @Autowired
    private SeparateChatAssistant separateChatAssistant;
    @Test
    public void testChatMemory5() {
        String answer1 = separateChatAssistant.chat(1,"我是David");
        System.out.println(answer1);
        String answer2 = separateChatAssistant.chat(1,"我是谁");
        System.out.println(answer2);
        String answer3 = separateChatAssistant.chat(2,"我是谁");
        System.out.println(answer3);
    }
    @Autowired
    private MemoryChatAssistant memoryChatAssistant;
    @Test
    public void testUserMessage() {
        String answer = memoryChatAssistant.chat("我是David");
        System.out.println(answer);
    }

    @Test
    public void testSystemMessage() {
        String answer = separateChatAssistant.chat(3,"今天几号");
        System.out.println(answer);
    }

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void testInsert2() {
        ChatMessages chatMessages = new ChatMessages();
        chatMessages.setContent("聊天记录列表");
        mongoTemplate.insert(chatMessages);
    }

    @Test
    public void testFindById() {
        ChatMessages chatMessages = mongoTemplate.findById("691c664d5489a40ea4a57ccb",
                ChatMessages.class);
        System.out.println(chatMessages);
    }

    @Test
    public void testUpdate() {
        Criteria criteria = Criteria.where("_id").is("691c664d5489a40ea4a57ccb");
        Query query = new Query(criteria);

        Update update = new Update();
        update.set("content", "新的聊天记录列表");
//修改或新增
        mongoTemplate.upsert(query, update, ChatMessages.class);
    }

    @Test
    public void testUpdate2() {
        Criteria criteria = Criteria.where("_id").is("100");
        Query query = new Query(criteria);
        Update update = new Update();
        update.set("content", "新的聊天记录列表");
//修改或新增
        mongoTemplate.upsert(query, update, ChatMessages.class);
    }

    /**
     * 删除文档
     */
    @Test
    public void testDelete() {
        Criteria criteria = Criteria.where("_id").is("100");
        Query query = new Query(criteria);
        mongoTemplate.remove(query, ChatMessages.class);
    }

    @Test
    public void testCalculatorTools() {
        String answer = separateChatAssistant.chat(1, "1+2等于几，475695037565的平方根是多\n" +
                "少");
//答案：3，689706.4865
                System.out.println(answer);
    }
}
