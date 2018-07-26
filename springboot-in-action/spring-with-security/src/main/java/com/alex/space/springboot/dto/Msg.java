package com.alex.space.springboot.dto;

import lombok.Data;

/**
 * @author Alex
 * Created by Alex on 2017/12/13.
 */
@Data
public class Msg {
	private String title;
	private String content;
	private String extraInfo;

	public Msg(String title, String content, String extraInfo) {
		super();
		this.title = title;
		this.content = content;
		this.extraInfo = extraInfo;
	}

}
