package com.lee.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lee.entity.User;
import com.lee.mapper.UserMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;
    public PageInfo<User> getAll(int pagesize, int pagenum) {
        PageHelper.startPage(pagenum,pagesize);
        List<User> list = userMapper.getAll();
        PageInfo<User> pageInfo = new PageInfo<User>(list);
        return pageInfo;
    }
    public User findUserById(@Param("id") int id){
        return userMapper.findUserById(id);
    }
    public int addUser(@Param("user") User user){
        String password = user.getPassword();
        String pwd = DigestUtils.md5DigestAsHex(password.getBytes());
        user.setPassword(pwd);
        return userMapper.addUser(user);
    }
    public int updatePassword(String pwd,int id){
        pwd = DigestUtils.md5DigestAsHex(pwd.getBytes());
        return userMapper.updatePassword(pwd,id);
    }
    public int updatePhoto(String path,int id){
        return userMapper.updatePhoto(path,id);
    }
    public int updateUser(@Param("user") User user){
        return userMapper.updateUser(user);
    }
    public int deleteUser(@Param("id") int id){
        return userMapper.delUser(id);
    }
    //根据name查找用户or登录判断
    public User findUserByName(User user){
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        User newuser = userMapper.findUserByName(user.getUsername());
        if(user.getPassword().equals(newuser.getPassword())){
            newuser.setPassword("");
            return newuser;
        }else{
            return null;
        }
    }
}
