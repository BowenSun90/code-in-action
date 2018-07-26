package com.alex.space.springboot.service;

import com.alex.space.springboot.dao.PermissionMapper;
import com.alex.space.springboot.dao.UserMapper;
import com.alex.space.springboot.dto.Permission;
import com.alex.space.springboot.dto.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alex
 * Created by Alex on 2017/12/13.
 * <p>
 * 将用户权限交给 springsecurity 进行管控
 */
@Service
public class CustomUserService implements UserDetailsService {
	//自定义UserDetailsService 接口

	@Autowired
	UserMapper userMapper;

	@Autowired
	PermissionMapper permissionMapper;

	@Override
	public UserDetails loadUserByUsername(String username) {
		SysUser user = userMapper.findByUserName(username);
		if (user != null) {
			List<Permission> permissions = permissionMapper.findByAdminUserId(user.getId());
			List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
			for (Permission permission : permissions) {
				if (permission != null && permission.getName() != null) {
					GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(permission.getName());
					//1 此处将权限信息添加到 GrantedAuthority 对象中，在后面进行全权限验证时会使用GrantedAuthority 对象。
					grantedAuthorities.add(grantedAuthority);
				}
			}
			return new User(user.getUsername(), user.getPassword(), grantedAuthorities);
		} else {
			throw new UsernameNotFoundException("admin: " + username + " do not exist!");
		}
	}

}