package com.lee.mapper;

import com.lee.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper {
    List<User> getAll();
    User findUserById(@Param("id") int id);
    int addUser(@Param("user") User user);
    int updatePassword(@Param("pwd") String pwd,@Param("id") int id);
    int updatePhoto(@Param("path") String path,@Param("id") int id);
    int updateUser(@Param("user") User user);
    int delUser(@Param("id") int id);
    User findUserByName(@Param("username") String username);
}
