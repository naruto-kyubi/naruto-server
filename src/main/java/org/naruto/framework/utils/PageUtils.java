package org.naruto.framework.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.naruto.framework.core.repository.SearchItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageUtils {
    public static Map prepareQueryPageMap(Map map) {
        if (null == map) map = new HashMap();
        Integer currentPage = null == map.get("currentPage") ? 1 : Integer.valueOf((String) map.get("currentPage"));
        Integer pageSize = null == map.get("pageSize") ? 10 : Integer.valueOf((String) map.get("pageSize"));
        currentPage = currentPage - 1;
        map.put("currentPage", currentPage);
        map.put("pageSize", pageSize);
        return map;
    }

    public static Pageable createPageable(Map map) {
        Integer currentPage  = (Integer) map.get("currentPage");
        Integer pageSize  = (Integer) map.get("pageSize");
        String sorter = (String) map.get("sorter");
        //分页信息
        Pageable pageable = new PageRequest(currentPage, pageSize);

        if (StringUtils.isNotEmpty(sorter)) {
            String[] strs = sorter.split("_");
            if ("ascend".equalsIgnoreCase(strs[1])) {
                pageable = new PageRequest(currentPage, pageSize, new Sort(new Sort.Order(Sort.Direction.ASC, strs[0])));
            } else {
                pageable = new PageRequest(currentPage, pageSize, new Sort(new Sort.Order(Sort.Direction.DESC, strs[0])));
            }
        }
        return pageable;
    }

    public static Map wrapperPagination(Page page){
        Pagination pagination = new Pagination(page.getSize(),page.getNumber()+1,page.getTotalElements());
        Map pageMap  = new HashMap();
        pageMap.put("pagination",pagination);
        return pageMap;
    }

    public static Map clearPaginationArgs(Map map){
        if(map.containsKey("currentPage")) map.remove("currentPage");
        if(map.containsKey("pageSize")) map.remove("pageSize");
        if(map.containsKey("sorter")) map.remove("sorter");
        return map;
    }

    public static List<SearchItem> getSearchItems(Map map){
        List list = new ArrayList();
        for (Object key : map.keySet()) {
            String param = (String)key;
            String[] params =  param.split("\\_");
            if(params.length>1){
                list.add(new SearchItem(params[0], (String) map.get(key),params[1]));
            }else{
                list.add(new SearchItem(params[0], (String) map.get(key),"like"));
            }
        }
        return list;
    }

    @Data
    @AllArgsConstructor
    static class Pagination{
        private int pageSize;
        private int current;
        private long total;
    }

}