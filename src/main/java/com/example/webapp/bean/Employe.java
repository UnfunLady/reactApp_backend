package com.example.webapp.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("employee")
public class Employe {
    private Integer deptno;
    private Integer employno;
    private String employname;
    private String employage;
    private String employsex;
    private String employidcard;
    private String employphone;
    private String entryDate;
    private String employemail;
    private String employaddress;
    private String employsalary;
    private String isuse;
}
