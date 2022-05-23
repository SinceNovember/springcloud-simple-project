package com.simple.admin.account.api;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.service.IService;
import com.simple.admin.account.api.entity.User;

import java.util.List;

public interface IUserService extends IService<User> {

    /**
     * �����û��б�
     * @return
     */
    List<User> listUser();

    User getUserById(Integer id);


}
