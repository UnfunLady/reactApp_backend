package com.example.webapp.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("dept")
public class Dept {
    @TableId(value="id",type= IdType.AUTO)
    private Integer id;
    private Integer deptno;
    private String deptname;
    private String location;
    private Integer count;
    private Integer countCovid;
}
