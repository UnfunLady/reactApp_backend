package com.example.webapp.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("covidinfo")
public class CovidInfo {
    private Integer depallid;
    private Integer deptid;
    private Integer employid;
    private String firstInoculation;
    private String secondInoculation;
    private String threeInoculation;
}
