package com.lee.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private Integer id;
    private String username;
    private String password;
    private Integer role;
    private String photo;
    @JSONField(format = "yyyy-MM-dd")
    private Date createTime;
    @JSONField(format = "yyyy-MM-dd")
    private Date updateTime;
    private Integer deleteTime;
}
