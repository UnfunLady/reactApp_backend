package com.example.webapp.bean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("employesalarydetail")
public class EmployeSalaryDetail {
    private Integer deptno;
    private Integer employno;
    private String employname;
    private String usesocialSub;
    private String usehouseSub;
    private String useeatSub;
    private String usetransSub;
    private String usehotSub;
    private Integer usePerformance;
    private Integer salary;
    private String isuse;
    @TableField(exist = false)
    private String deptname;
}
