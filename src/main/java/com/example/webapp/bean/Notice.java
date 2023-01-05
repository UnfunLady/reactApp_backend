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
@TableName("notice")
public class Notice {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String thyme;
    private Integer specialDno;
    private Integer specialDeptId;
    private String content;
    private String isAll;
    private String startTime;
    private String endTime;
    private String postMan;
    private String postTime;
}
