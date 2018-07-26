package com.alex.space.springboot.dao;

import com.alex.space.springboot.dto.Permission;

import java.util.List;

/**
 * @author Alex
 * Created by Alex on 2017/12/13.
 */
public interface PermissionMapper {

	List<Permission> findAll();

	List<Permission> findByAdminUserId(int userId);
}
