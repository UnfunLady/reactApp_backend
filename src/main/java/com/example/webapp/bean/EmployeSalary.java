package com.example.webapp.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("employesalary")
public class EmployeSalary {
    public Integer deptno;
    public Integer deptid;
    public Integer socialSub;
    public Integer houseSub;
    public Integer eatSub;
    public Integer transSub;
    public Integer hotSub;
    public Integer performance;
    public String isuse;
}
