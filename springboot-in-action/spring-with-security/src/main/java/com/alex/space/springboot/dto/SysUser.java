package com.alex.space.springboot.dto;

import lombok.Data;

import java.util.List;

/**
 * @author Alex
 * Created by Alex on 2017/12/13.
 */
@Data
public class SysUser {

	private Integer id;

	private String username;

	private String password;

	private List<SysRole> roles;

}
