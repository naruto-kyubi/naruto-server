package org.naruto.framework.core.repository;

import org.naruto.framework.utils.PageUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

//自定义数据库接口实现
public class CustomRepositoryImpl <T,ID extends Serializable>
        extends SimpleJpaRepository<T,ID>
        implements CustomRepository<T,ID>{

    public CustomRepositoryImpl(Class<T> domainClass,EntityManager entityManager){
        super(domainClass,entityManager);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Page<T> queryPageByCondition(Map map) {

        Map _map = PageUtils.prepareQueryPageMap(map);

        Pageable pageable = PageUtils.createPageable(_map);
        _map = PageUtils.clearPaginationArgs(_map);
        List<SearchItem> searchItems = PageUtils.getSearchItems(_map);
        return (Page<T>) findAll(CustomerSpecs.byAuto(searchItems),pageable);
    }
}
