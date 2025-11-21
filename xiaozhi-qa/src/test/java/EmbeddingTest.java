import com.xiaozhi.MedicalAssistant;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.splitter.DocumentBySentenceSplitter;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = MedicalAssistant.class)
public class EmbeddingTest {

    @Autowired
    private EmbeddingModel embeddingModel;

    @Autowired
    private EmbeddingStore embeddingStore;


    @Test
    public void testDimensions() {
        String testText = "测试文本";
        Embedding embedding = embeddingModel.embed(testText).content();
        System.out.println("嵌入模型维度: " + embedding.vector().length);
    }
    @Test
    public void testEmbeddingModel(){
        Response<Embedding> embed = embeddingModel.embed("你好");
        System.out.println("向量维度：" + embed.content().vector().length);
        System.out.println("向量输出：" + embed.toString());
    }

    @Test
    public void testPineconeEmbeded() {
//将文本转换成向量
        TextSegment segment1 = TextSegment.from("我喜欢羽毛球");
        Embedding embedding1 = embeddingModel.embed(segment1).content();
//存入向量数据库
        embeddingStore.add(embedding1, segment1);
        TextSegment segment2 = TextSegment.from("今天天气很好");
        Embedding embedding2 = embeddingModel.embed(segment2).content();
        embeddingStore.add(embedding2, segment2);
    }

    @Test
    public void testUploadKnowledgeLibrary() {
//使用FileSystemDocumentLoader读取指定目录下的知识库文档
//并使用默认的文档解析器对文档进行解析
        Document document1 = FileSystemDocumentLoader.loadDocument("/Users/david/project/agent/xiaozhi-qa/src/main/resources/knowledge/医院信息.md");
        Document document2 = FileSystemDocumentLoader.loadDocument("/Users/david/project/agent/xiaozhi-qa/src/main/resources/knowledge/科室信息.md");
        Document document3 = FileSystemDocumentLoader.loadDocument("/Users/david/project/agent/xiaozhi-qa/src/main/resources/knowledge/神经内科.md");
        List<Document> documents = Arrays.asList(document1, document2, document3);
//文本向量化并存入向量数据库：将每个片段进行向量化，得到一个嵌入向量
        EmbeddingStoreIngestor
                .builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .build()
                .ingest(documents);
    }

    @Test
    public void testUploadKnowledgeLibrar() {
        // 使用简单的文档分割器替代递归分割器
        DocumentSplitter splitter = new DocumentBySentenceSplitter(300, 50);
        // 或者使用按段落分割
        // DocumentSplitter splitter = new DocumentByParagraphSplitter(500, 100);

        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(splitter)  // 使用简单的分割器
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();

        // 加载文档
        List<Document> documents = loadDocuments();
        ingestor.ingest(documents);

        // 验证
        assertThat(embeddingStore).isNotNull();
    }

    private List<Document> loadDocuments() {
        // 你的文档加载逻辑
        return Arrays.asList(
                FileSystemDocumentLoader.loadDocument("/Users/david/project/agent/xiaozhi-qa/src/main/resources/knowledge/医院信息.md"),
                FileSystemDocumentLoader.loadDocument("/Users/david/project/agent/xiaozhi-qa/src/main/resources/knowledge/科室信息.md"),
                FileSystemDocumentLoader.loadDocument("/Users/david/project/agent/xiaozhi-qa/src/main/resources/knowledge/神经内科.md")
        );
    }




}
