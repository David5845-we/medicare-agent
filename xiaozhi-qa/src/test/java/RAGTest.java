import com.xiaozhi.MedicalAssistant;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.splitter.DocumentByParagraphSplitter;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.segment.TextSegment;

//import dev.langchain4j.model.embedding.onnx.HuggingFaceTokenizer;
import dev.langchain4j.model.embedding.HuggingFaceTokenizer;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = MedicalAssistant.class)
public class RAGTest {
    @Test
    public void testReadDocument() {
//使用FileSystemDocumentLoader读取指定目录下的知识库文档
//并使用默认的文档解析器TextDocumentParser对文档进行解析
        Document document = FileSystemDocumentLoader.loadDocument("/Users/david/project/xiaozhi-qa/src/main/resources/medical-prompt-template.txt");
                System.out.println(document.text());
    }

    @Test
    public void testReadDocumentAndStore() {
//使用FileSystemDocumentLoader读取指定目录下的知识库文档
//并使用默认的文档解析器对文档进行解析(TextDocumentParser)
        Document document = FileSystemDocumentLoader.loadDocument("/Users/david/project/xiaozhi-qa/src/main/resources/knowledge/人工智能.md");
//为了简单起见，我们暂时使用基于内存的向量存储
                InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>
                        ();
//ingest
//1、分割文档：默认使用递归分割器，将文档分割为多个文本片段，每个片段包含不超过 300个token，并且
        //有 30个token的重叠部分保证连贯性
//DocumentByParagraphSplitter(DocumentByLineSplitter(DocumentBySentenceSplitter(Docume
       // ntByWordSplitter)))
//2、文本向量化：使用一个LangChain4j内置的轻量化向量模型对每个文本片段进行向量化
//3、将原始文本和向量存储到向量数据库中(InMemoryEmbeddingStore)
        EmbeddingStoreIngestor.ingest(document, embeddingStore);
//查看向量数据库内容
        System.out.println(embeddingStore);
    }

    @Test
    public void testDocumentSplitter() {
//使用FileSystemDocumentLoader读取指定目录下的知识库文档
//并使用默认的文档解析器对文档进行解析(TextDocumentParser)
        Document document = FileSystemDocumentLoader.loadDocument("/Users/david/project/xiaozhi-qa/src/main/resources/knowledge/人工智能.md");
//为了简单起见，我们暂时使用基于内存的向量存储
                InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>
                        ();
//自定义文档分割器
//按段落分割文档：每个片段包含不超过 300个token，并且有 30个token的重叠部分保证连贯性
//注意：当段落长度总和小于设定的最大长度时，就不会有重叠的必要。
        DocumentByParagraphSplitter documentSplitter = new DocumentByParagraphSplitter(
                300,
                30,
//token分词器：按token计算
                new HuggingFaceTokenizer());
//按字符计算
//DocumentByParagraphSplitter documentSplitter = new
       // DocumentByParagraphSplitter(300, 30);

        /*LangChain4j：
        6.5、工作方式
        1. 实例化一个 “文档分割器”（DocumentSplitter），指定所需的 “文本片段”（TextSegment）大小，并
        且可以选择指定characters 或token的重叠部分。
        2. “文档分割器”（DocumentSplitter）将给定的文档（Document）分割成更小的单元，这些单元的性
        质因分割器而异。例如，“按段落分割文档器”（DocumentByParagraphSplitter）将文档分割成段落
（由两个或更多连续的换行符定义），而 “按句子分割文档器”（DocumentBySentenceSplitter）使
        用 OpenNLP 库的句子检测器将文档分割成句子，依此类推。
        3. 然后，“文档分割器”（DocumentSplitter）将这些较小的单元（段落、句子、单词等）组合成 “文本
        片段”（TextSegment），尝试在单个 “文本片段”（TextSegment）中包含尽可能多的单元，同时不
        超过第一步中设置的限制。如果某些单元仍然太大，无法放入一个 “文本片段”（TextSegment）
        中，它会调用一个子分割器。这是另一个 “文档分割器”（DocumentSplitter），能够将不适合的单
        元分割成更细粒度的单元。会向每个文本片段添加一个唯一的元数据条目 “index”。第一个 “文本片
        段”（TextSegment）将包含 index=0 ，第二个是 index=1 ，依此类推
        模型上下文窗口 可以通过模型参数列表查看：阿里云百炼
                期望的文本片段最大大小
        1. 模型上下文窗口：如果你使用的大语言模型（LLM）有特定的上下文窗口限制，这个值不能超过模
        型能够处理的最大 token 数。例如，某些模型可能最大只能处理 2048 个 token，那么设置的文本片
        段大小就需要远小于这个值，为后续的处理（如添加指令、其他输入等）留出空间。通常，在这种
        情况下，你可以设置为 1000 - 1500 左右，具体根据实际情况调整。*/
        EmbeddingStoreIngestor
                .builder()
                .embeddingStore(embeddingStore)
                .documentSplitter(documentSplitter)
                .build()
                .ingest(document);
    }

   /* @Test
    public void testTokenCount() {
        String text = "这是一个示例文本，用于测试 token 长度的计算。";
        UserMessage userMessage = UserMessage.userMessage(text);
//计算 token 长度
//QwenTokenizer tokenizer = new QwenTokenizer(System.getenv("DASH_SCOPE_API_KEY"),
        //"qwen-max");
        HuggingFaceTokenizer tokenizer = new HuggingFaceTokenizer();
        int count = tokenizer.estimateTokenCountInMessage(userMessage);
        System.out.println("token长度：" + count);
    }*/
}
