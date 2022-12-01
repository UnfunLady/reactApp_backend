package com.example.webapp.mapper;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Mapper
public interface echartsMapper {
    @Select("select de.dname as name,de.count as value from depall de")
    List<Map<String, String>> getOneCharts();
    @MapKey("name")
    List<Map<String, String>> getTwoCharts();
}
