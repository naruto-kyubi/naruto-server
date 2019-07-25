package org.naruto.framework.geographic.repository;

import org.naruto.framework.core.repository.CustomRepository;
import org.naruto.framework.geographic.domain.City;

import java.util.List;

public interface CityRepository extends CustomRepository<City,String> {

    List<City> queryCitiesByProvinceIdOrderBySeqAsc(String provinceId);
}
