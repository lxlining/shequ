package com.lee.controller;

import com.alibaba.fastjson.JSON;
import com.lee.entity.User;
import com.lee.service.UserService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpRequest;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/admin")
public class UserController {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserService userService;
    @Autowired
    private HttpSession session;
    @Value("${web.upload-path}")
    private String uploadPath;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

    @GetMapping("/user/list/{pagesize}/{pagenum}")
    public String getAll(@PathVariable("pagesize") int pagesize,@PathVariable("pagenum") int pagenum){
        return JSON.toJSONString(userService.getAll(pagesize,pagenum));
    }
    @GetMapping("/user/{id}")
    public String getOne(@PathVariable("id") int id){
        return JSON.toJSONString(userService.findUserById(id));
    }
    @PostMapping("/user/add")
    public String addUser(@RequestBody User user){
        return JSON.toJSONString(userService.addUser(user));
    }
    @PutMapping("/user/update")
    public String updateUser(@RequestBody User user){
        return JSON.toJSONString(userService.updateUser(user));
    }
    @PutMapping("/user/updatepwd")
    public String updatePassword(@RequestBody User user){
        return JSON.toJSONString(userService.updatePassword(user.getPassword(),user.getId()));
    }
    @PutMapping("/user/updatephoto")
    public String updatePhoto(HttpServletRequest request,@RequestParam("file")MultipartFile file, @RequestParam("id") int id) throws Exception{
        String format = sdf.format(new Date());
        File folder = new File(uploadPath + format);
        if (!folder.isDirectory()) {
            folder.mkdirs();
        }
        // 对上传的文件重命名，避免文件重名
        String oldName = file.getOriginalFilename();
        String newName = UUID.randomUUID().toString().replace("-","") + oldName.substring(oldName.lastIndexOf("."), oldName.length());
        // 文件保存
        file.transferTo(new File(folder, newName));
        // 返回上传文件的访问路径
        String filePath = request.getScheme() + "://" + request.getServerName()+ ":" + request.getServerPort()  +"/"+ format + "/"+newName;
        //String filePath = "/"+ format + "/"+newName;
        System.out.println(filePath);
        int res=userService.updatePhoto(filePath,id);
        if(res==1) return JSON.toJSONString(filePath);
        return JSON.toJSONString("0");
    }
    @DeleteMapping("/user/{id}")
    public String delUser(@PathVariable("id") int id){
        return JSON.toJSONString(userService.deleteUser(id));
    }
    @PostMapping("/login")
    public String login(@RequestBody User user){
        User user1 = userService.findUserByName(user);
        System.out.println(user1);
        if(user1!=null){
            redisTemplate.opsForValue().set("user", user1);
            session.setAttribute("user",user1);
            User user2 = (User) redisTemplate.opsForValue().get(user);
            return JSON.toJSONString(user2);
        }
        return JSON.toJSONString("用户名或密码错误");

    }
    @GetMapping("/logout")
    public String logout(){
        System.out.println(session.getAttribute("user"));
        session.removeAttribute("user");
        System.out.println(session.getAttribute("user"));
        return JSON.toJSONString("success");
    }
}
