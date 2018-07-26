package com.alex.space.springboot.dto;

import lombok.Data;

/**
 * @author Alex
 * Created by Alex on 2017/12/13.
 */
@Data
public class Permission {

	private int id;

	//权限名称
	private String name;

	//权限描述
	private String descritpion;

	//授权链接
	private String url;

	//父节点id
	private int pid;

}
