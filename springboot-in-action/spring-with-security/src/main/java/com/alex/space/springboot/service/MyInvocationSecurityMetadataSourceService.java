package com.alex.space.springboot.service;

import com.alex.space.springboot.dao.PermissionMapper;
import com.alex.space.springboot.dto.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author Alex
 * Created by Alex on 2017/12/13.
 */
@Service
public class MyInvocationSecurityMetadataSourceService implements
		FilterInvocationSecurityMetadataSource {

	@Autowired
	private PermissionMapper permissionMapper;

	private HashMap<String, Collection<ConfigAttribute>> map = null;

	/**
	 * 加载资源，初始化资源变量
	 */
	public void loadResourceDefine() {
		map = new HashMap<>();
		Collection<ConfigAttribute> array;
		ConfigAttribute cfg;
		List<Permission> permissions = permissionMapper.findAll();
		for (Permission permission : permissions) {
			array = new ArrayList<>();
			cfg = new SecurityConfig(permission.getName());
			// 此处只添加了用户的名字，其实还可以添加更多权限的信息，
			// 例如请求方法到ConfigAttribute的集合中去。
			// 此处添加的信息将会作为MyAccessDecisionManager类的decide的第三个参数。
			array.add(cfg);
			//用权限的getUrl() 作为map的key，用ConfigAttribute的集合作为 value，
			map.put(permission.getUrl(), array);
		}

	}

	/**
	 * 此方法是为了判定用户请求的url 是否在权限表中，
	 * 如果在权限表中，则返回给 decide 方法，用来判定用户是否有此权限。如果不在权限表中则放行。
	 */
	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		if (map == null) {
			loadResourceDefine();
		}
		//object 中包含用户请求的request 信息
		HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
		AntPathRequestMatcher matcher;
		String resUrl;
		for (Iterator<String> iter = map.keySet().iterator(); iter.hasNext(); ) {
			resUrl = iter.next();
			matcher = new AntPathRequestMatcher(resUrl);
			if (matcher.matches(request)) {
				return map.get(resUrl);
			}
		}
		return null;
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}
}
