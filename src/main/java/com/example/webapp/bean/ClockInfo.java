package com.example.webapp.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("clockemploye")
public class ClockInfo {
    private Integer dno;
    private Integer deptid;
    private Integer employeno;
    private String employename;
    private String type;
    private String clockTime;
    private String location;
}
