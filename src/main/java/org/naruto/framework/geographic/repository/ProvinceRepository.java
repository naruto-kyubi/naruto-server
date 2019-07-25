package org.naruto.framework.geographic.repository;

import org.naruto.framework.core.repository.CustomRepository;
import org.naruto.framework.geographic.domain.Province;

import java.util.List;

public interface ProvinceRepository extends CustomRepository<Province,String> {
    List<Province> queryProvincesByOrderBySeqAsc();
}
