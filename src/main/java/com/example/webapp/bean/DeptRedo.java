package com.example.webapp.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("deptredo")
public class DeptRedo {
    private Integer id;
    private Integer deptno;
    private String deptname;
    private String location;
    private Integer count;
    private Integer countCovid;
    private String confirmTime;
    private String whichDone;
}
