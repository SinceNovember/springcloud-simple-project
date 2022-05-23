package com.simple.admin.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.simple.admin.auth.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from user where username =#{username}")
    User selectByUserName(String username);
}
