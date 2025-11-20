package com.xiaozhi.tool;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.pinecone.PineconeEmbeddingStore;
import dev.langchain4j.store.embedding.pinecone.PineconeServerlessIndexConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmbeddingStoreConfig {
    @Autowired
    private EmbeddingModel embeddingModel;
    @Bean
    public EmbeddingStore<TextSegment> embeddingStore() {
//创建向量存储
        EmbeddingStore<TextSegment> embeddingStore = PineconeEmbeddingStore.builder()
                .apiKey("pcsk_6ux2ua_Rtz6P3wTiTzhNaSpjz8Qg51oWyXLMp12ubeLz7516jLf9PHNXANNZZij4ztoFWK")
                .index("medicare-index")//如果指定的索引不存在，将创建一个新的索引
                .nameSpace("medicare-namespace") //如果指定的名称空间不存在，将创建一个新的名称 空间

                .createIndex(PineconeServerlessIndexConfig.builder()
                                .cloud("AWS") //指定索引部署在 AWS 云服务上。
                                .region("us-east-1") //指定索引所在的 AWS 区域为 us-east-1。
                                .dimension(embeddingModel.dimension()) //指定索引的向量维度，该维度与 embeddedModel 生成的向量维度相同。 2.5、测试向量存储3、相似度匹配接收请求获取问题，将问题转换为向量，在 Pinecone 向量数据库中进行相似度搜索，找到最相似的文本片段，并将其文本内容返回给客户端。
.build())
.build();
        return embeddingStore;
    }

}
