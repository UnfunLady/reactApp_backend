package com.example.webapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.webapp.bean.EmployeRedo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface employeReDoMapper extends BaseMapper<EmployeRedo> {
}
