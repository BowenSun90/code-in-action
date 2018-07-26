package com.alex.space.springboot.dao;

import com.alex.space.springboot.dto.SysUser;

/**
 * @author Alex
 * Created by Alex on 2017/12/13.
 */
public interface UserMapper {

	SysUser findByUserName(String username);
}
