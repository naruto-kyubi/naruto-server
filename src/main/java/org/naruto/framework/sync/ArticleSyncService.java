package org.naruto.framework.sync;

import com.alibaba.otter.canal.protocol.CanalEntry;
import org.naruto.framework.article.domain.Article;
import org.naruto.framework.article.service.ArticleService;
import org.naruto.framework.search.article.domain.EsArticle;
import org.naruto.framework.search.article.service.ArticleEsService;
import org.naruto.framework.utils.ObjUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ArticleSyncService {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleEsService articleEsService;

    public void sync(CanalEntry.RowChange rowChange){
        CanalEntry.EventType eventType = rowChange.getEventType();

        for (CanalEntry.RowData rowData : rowChange.getRowDatasList()) {
            if (eventType == CanalEntry.EventType.DELETE) {

                String value = CanalUtils.getColumnValue(rowData.getBeforeColumnsList(),"id");
                articleEsService.delete(value);
            } else{

                String value = CanalUtils.getColumnValue(rowData.getAfterColumnsList(),"id");
                Article article =articleService.queryArticleById(value);
                EsArticle esArticle = (EsArticle) ObjUtils.convert(article,EsArticle.class);
                esArticle.setUserId(esArticle.getOwner().getId());
                articleEsService.save(esArticle);
            }
        }
    }
}
