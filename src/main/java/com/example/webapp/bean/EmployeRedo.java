package com.example.webapp.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("employeredo")
public class EmployeRedo {
    public Integer dno;
    public Integer deptno;
    public Integer employno;
    public String employname;
    public String employage;
    public String employsex;
    public String employidcard;
    public String employphone;
    public String entryDate;
    public String employemail;
    public String employaddress;
    public String employsalary;
    public String isuse;
    public String confirmTime;
    public String whichDone;
}
