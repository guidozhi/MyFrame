package com.khnt.rtbox.config.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.khnt.core.crud.dao.impl.EntityDaoImpl;
import com.khnt.rtbox.config.bean.RtDir;

/**
 * <p>
 * 
 * </p>
 * 
 * @ClassName RtDirDao
 * @JDK 1.5
 * @author CODER_V3.0
 * @date 2016-03-22 09:51:04
 */
@Repository("rtDirDao")
public class RtDirDao extends EntityDaoImpl<RtDir> {

	public RtDir getByCode(String rtCode) {
		@SuppressWarnings("unchecked")
		List<RtDir> list = this.createQuery("from RtDir where rtCode=?", rtCode).list();
		if (list != null && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}
	
	
	
}
