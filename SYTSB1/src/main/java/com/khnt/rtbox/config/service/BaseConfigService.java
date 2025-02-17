package com.khnt.rtbox.config.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.khnt.core.crud.manager.impl.EntityManageImpl;
import com.khnt.rtbox.config.bean.BaseConfig;
import com.khnt.rtbox.config.dao.BaseConfigDao;

@Service("baseConfigService")
public class BaseConfigService extends EntityManageImpl<BaseConfig, BaseConfigDao> {

	@Autowired
	private BaseConfigDao baseConfigDao;


	public List<HashMap<String, String>> getCodeTable(String name) {
		
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		
		List<Object> rlist = baseConfigDao.getCodeTable(name);
		for (int i = 0; i < rlist.size(); i++) {
			
			Object [] obj = (Object[]) rlist.get(i);
			HashMap<String, String> map= new HashMap<String, String>();
			map.put("id", obj[0].toString());
			map.put("text", obj[1].toString());
			list.add(map);
		}
		return list;
	}


	public List<HashMap<String, String>> getCodeTableValues(String code) {

		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		
		List<Object> rlist = baseConfigDao.getCodeTableValues(code);
		for (int i = 0; i < rlist.size(); i++) {
			
			Object [] obj = (Object[]) rlist.get(i);
			HashMap<String, String> map= new HashMap<String, String>();
			map.put("id", obj[0].toString());
			map.put("text", obj[1].toString());
			list.add(map);
		}
		return list;
	}


	public List<HashMap<String, String>> getCodeBySql(String sql) {
		
		return baseConfigDao.createSQLQuery(sql).setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
	}
	
}
