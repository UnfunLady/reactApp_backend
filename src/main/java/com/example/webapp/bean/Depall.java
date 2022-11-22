package com.example.webapp.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("depall")
public class Depall {
    private Integer dno;
    private String dname;
    private String explain;
    private String avatar;
    private Integer count;
    private Integer groupCount;
    private String isAllCovid;
    private Integer noCovid;
}
