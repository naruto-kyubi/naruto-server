package org.naruto.framework.elasticsearch.user.repository;

import org.naruto.framework.elasticsearch.user.domain.EsUser;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserEsRepository extends ElasticsearchRepository<EsUser,String> {

}
