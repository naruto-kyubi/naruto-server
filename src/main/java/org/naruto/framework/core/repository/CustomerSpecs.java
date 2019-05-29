package org.naruto.framework.core.repository;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import java.util.ArrayList;
import java.util.List;

public class CustomerSpecs {

    public static <T> Specification<T> byAuto(List<SearchItem> searchItems){

        return new Specification<T>(){
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();

                EntityType<T> type = root.getModel();

                for (SearchItem item : searchItems) {
//                    Attribute attr =  type.getAttribute(item.getKey());
                    if("equal".equalsIgnoreCase(item.getRule())){
                        predicates.add(criteriaBuilder.equal(root.get(item.getKey()),item.getValue()));

                    }else if("like".equalsIgnoreCase(item.getRule())){
                        predicates.add(criteriaBuilder.like(root.get(item.getKey()),item.getValue() + "%"));
                    }
                    else if("between".equalsIgnoreCase(item.getRule())){
                        //解析value值，数组；
                        String value = item.getValue();
                        String[] values = value.split(",");

                        if(values.length>1){
                            if(StringUtils.isNotEmpty(values[0]) && StringUtils.isNotEmpty(values[1])){
                                predicates.add(criteriaBuilder.between(root.get(item.getKey()),values[0],values[1]));
                            }else if(StringUtils.isNotEmpty(values[0])){
                                predicates.add(criteriaBuilder.greaterThan(root.get(item.getKey()),values[0]));
                            }else if(StringUtils.isNotEmpty(values[1])){
                                predicates.add(criteriaBuilder.lessThan(root.get(item.getKey()),values[1]));
                            }
                        }else{
                            predicates.add(criteriaBuilder.greaterThan(root.get(item.getKey()),values[0]));
                        }
                    }
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
