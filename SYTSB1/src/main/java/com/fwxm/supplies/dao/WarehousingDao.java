package com.fwxm.supplies.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.fwxm.material.bean.Goods;
import com.fwxm.supplies.bean.Warehousing;
import com.khnt.core.crud.dao.impl.EntityDaoImpl;
import com.khnt.utils.StringUtil;

@Repository("warehousingDao")
public class WarehousingDao  extends EntityDaoImpl<Warehousing>{

	
	public List<Warehousing> getWarehousingByFybxId(String fybxId){
		String hql="from Warehousing where fybxd_id=?";
		Query query=this.createQuery(hql, fybxId);
		return query.list();
	}
	public Map<String, Object>  getBeanByYear( String year){
		String sql="SELECT * FROM ( SELECT to_number(substr(WAREHOUSING_NUM,11)) as WAREHOUSING_NUM FROM TJY2_CH_WAREHOUSING WHERE WAREHOUSING_NUM LIKE  '"+year+"%'   ) order by WAREHOUSING_NUM desc";
		List<Map<String, Object>> list=this.createSQLQuery(sql).setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
		if(list!=null && list.size()>0){
			return list.get(0);
		}else {
			return null;
		}
	}
	
	//根据入库单Id查询物品信息
	public List<Goods> getGoodsByRkId(String id){
		String sql="select goods.* FROM TJY2_CH_WAREHOUSING w "
				+ "INNER JOIN  TJY2_CH_GOODSANDORDER g  ON g.FK_ORDER_ID=w.'ID' "
				+ " INNER JOIN  TJY2_CH_GOODS goods ON goods.id=g.FK_GOODS_ID"
				+ " WHERE w.id=?";
		SQLQuery query=((SQLQuery)this.createSQLQuery(sql, id)).addEntity(Goods.class);
		return query.list();
		
	}
	
	public List<Map<String, Object>> getGoods (String id,String type){
		String hql="SELECT g.* ,o.bz gbz,o.fph FROM  TJY2_CH_GOODS g "
				+ " INNER JOIN TJY2_CH_GOODSANDORDER o ON g.ID=o.FK_GOODS_ID AND g.FK_WAREHOUSING_ID=o.FK_ORDER_ID"
				+ " WHERE g.STATE='2' AND g.FK_WAREHOUSING_ID in ('"+id.trim().replaceAll(",", "','")+"') AND g.FK_GOODSTYPE_ID=?";
		List<Map<String, Object>> list=this.createSQLQuery(hql,type).setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}
	
	public List<Map<String, Object>> getYsGoods(String rkId,String typeId){
		String sql="SELECT * FROM ("
				+ " SELECT g.WPMC,g.GYSMC,g.GGJXH,g.DW,o.SJLQSL SL,g.JE,g.SE,out.lqbm FROM TJY2_CH_GOODS g"
				+ " INNER JOIN TJY2_CH_GOODSANDORDER o ON g.id=o.FK_GOODS_ID"
				+ " INNER JOIN TJY2_CH_OUT out ON out.id=o.FK_ORDER_ID"
				+ " WHERE g.STATE='2' AND o.type='CK' AND o.CRK_TYPE='LQ' AND o.SJLQSL <> 0 AND o.SJLQSL IS NOT NULL AND g.FK_WAREHOUSING_ID in ('"+rkId.trim().replaceAll(",", "','")+"')"
				+ " AND g.FK_GOODSTYPE_ID=?"
				+ " union all  "
				+ " SELECT g.WPMC,g.GYSMC,g.GGJXH,g.DW,g.SL,g.JE,g.SE,g.CREATE_ORG_NAME lqbm FROM TJY2_CH_GOODS g"
				+ " WHERE g.STATE='2'  AND g.SL <> 0"
				+ " AND g.FK_WAREHOUSING_ID in ('"+rkId.trim().replaceAll(",", "','")+"')"
						+ " AND g.FK_GOODSTYPE_ID=?"
				+ " ) a ORDER BY a.lqbm ,a.WPMC";
		List<Map<String, Object>> list=this.createSQLQuery(sql,typeId,typeId).setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}
	/**
	 * 根据入库编号查询入库单
	 * @param rkbh
	 * @return
	 */
	public List<Warehousing> getWarehousingByRkBh(String[] rkbh){
		String hql="from Warehousing where warehousing_num in (:rkbh)";
		Query query=this.createQuery(hql);
		query.setParameterList("rkbh", rkbh);
		return query.list();
	}
	
	public List<Map<String, Object>> getTzData(String rkId,String startTime,String endTime,String orgId){
		String sql="SELECT g.wpmc,g.GGJXH,g.DW,to_char(g.RK_TIME,'yyyy-mm-dd') RK_TIME,g.CSSL,g.JE,to_char(o.lqsj,'yyyy-mm-dd') lqsj,go.sjlqsl,o.lqr,go.sl from TJY2_CH_GOODS g"
				+ " INNER JOIN TJY2_CH_GOODSANDORDER go ON go.fk_goods_id=g.id"
				+ " INNER JOIN TJY2_CH_OUT o ON o.id=go.FK_ORDER_ID"
				+ " where state='2' and g.create_org_id="+orgId ;
				if(StringUtil.isNotEmpty(rkId)){
					sql += " and g.fk_warehousing_id in ('"+rkId.trim().replaceAll(",", "','")+"') ";
				}
				if(StringUtil.isNotEmpty(startTime)){
					sql += " and g.rk_time >=to_date('"+startTime+"','yyyy-mm-dd')";
				}
				if(StringUtil.isNotEmpty(endTime)){
					sql+=" and g.rk_time <=to_date('"+endTime+"','yyyy-mm-dd')";
				}
				sql+=" ORDER BY g.RK_TIME DESC,o.lqsj DESC";
		List<Map<String, Object>> list=this.createSQLQuery(sql).setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}

}
