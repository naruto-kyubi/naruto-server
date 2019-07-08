package org.naruto.framework.elasticsearch.user.service;

import org.naruto.framework.user.vo.UserVo;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface UserEsService {
    public Page<UserVo> search(Map map);
}
