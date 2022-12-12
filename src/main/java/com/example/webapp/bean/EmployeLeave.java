package com.example.webapp.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("leaveRequest")
public class EmployeLeave {
    private Integer leaveNumber;
    private String whyLeave;
    private String leaveLong;
    private Integer employeno;
    private String leaveWhen;
    private String reply;
    private String verfiyState;
    private Integer deptid;
    private Integer dno;
    private String whichVerfiy;
    private String employename;
}
