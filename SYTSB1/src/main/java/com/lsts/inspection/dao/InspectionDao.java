package com.lsts.inspection.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.khnt.base.Factory;
import com.khnt.core.crud.dao.impl.EntityDaoImpl;
import com.khnt.utils.StringUtil;
import com.lsts.inspection.bean.FlowInfoDTO;
import com.lsts.inspection.bean.Inspection;



/**
 * 报检大厅管理   dao
 * 
 * @author 肖慈边 2014-2-13
 */

@Repository("inspectionDao")
public class InspectionDao extends EntityDaoImpl<Inspection> {
	private static Connection conn = null;  // 数据库连接
    private static PreparedStatement pstmt = null; // 数据库操作对象
    private static ResultSet rs = null; // 结果集
    
    // 待处理业务
    @SuppressWarnings("unchecked")
    public List<FlowInfoDTO> getFlowInfo(String userId) {
		List<FlowInfoDTO> list = new ArrayList<FlowInfoDTO>();
    	try {
    		if (StringUtil.isNotEmpty(userId)) {
    			conn = getConn();
    			String sql = "SELECT  T.ACTIVITY_NAME,subStr(t.TITLE,0, instr(t.TITLE,'-')-1) as flowName,count(t.id) as num, a.activity_id,a.function, b.fk_flow_index_id"
    				+ "  FROM V_PUB_WORKTASK T, TZSB_INSPECTION_INFO b,TZSB_INSPECTION T1,flow_activity a where "
    				+ " t.SERVICE_ID = b.id and B.FK_INSPECTION_ID=T1.ID   and b.flow_note_id = a.activity_id and a.id = t.ACTIVITY_ID and T.STATUS='0' and b.data_status<>'99' and t.HANDLER_ID='"+userId+"'"
    				+ " group by t.ACTIVITY_NAME,  a.function,a.activity_id, b.fk_flow_index_id,subStr(t.TITLE,0, instr(t.TITLE,'-')-1)";
    			pstmt = conn.prepareStatement(sql);
    			rs = pstmt.executeQuery();
    			while (rs.next()){
    				FlowInfoDTO flowInfoDTO = new FlowInfoDTO();
    				flowInfoDTO.setActivity_name(rs.getString(1));
    				flowInfoDTO.setFlow_name(rs.getString(2));
    				flowInfoDTO.setNum(rs.getString(3));
    				flowInfoDTO.setActivity_id(rs.getString(4));
    				flowInfoDTO.setFunction(rs.getString(5));
    				flowInfoDTO.setFk_flow_index_id(rs.getString(6));
    				list.add(flowInfoDTO);
    			}
    			closeConn();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return list;
    }
    
    // 待处理批量业务
    @SuppressWarnings("unchecked")
    public List<FlowInfoDTO> getBatchFlowInfo(String userId) {
		List<FlowInfoDTO> list = new ArrayList<FlowInfoDTO>();
    	try {
    		if (StringUtil.isNotEmpty(userId)) {
    			conn = getConn();
    			String sql = "SELECT  T.ACTIVITY_NAME,subStr(t.TITLE,0, instr(t.TITLE,'-')-1) as flowName,count(t.id) as num, a.activity_id,a.function, b.fk_flow_index_id"
					+ "  FROM V_PUB_WORKTASK T, TZSB_INSPECTION_INFO b,flow_activity a where "
					+ " t.SERVICE_ID = b.id and b.flow_note_id = a.activity_id and a.id = t.ACTIVITY_ID "
					+ " and b.fk_inspection_id is null"
					+ " and T.STATUS='0' and b.data_status<>'99' and t.HANDLER_ID='"
					+ userId
					+ "'"
					+ " group by t.ACTIVITY_NAME, a.function,a.activity_id, b.fk_flow_index_id,subStr(t.TITLE,0, instr(t.TITLE,'-')-1)";
    			pstmt = conn.prepareStatement(sql);
    			rs = pstmt.executeQuery();
    			while (rs.next()){
    				FlowInfoDTO flowInfoDTO = new FlowInfoDTO();
    				flowInfoDTO.setActivity_name(rs.getString(1));
    				flowInfoDTO.setFlow_name(rs.getString(2));
    				flowInfoDTO.setNum(rs.getString(3));
    				flowInfoDTO.setActivity_id(rs.getString(4));
    				flowInfoDTO.setFunction(rs.getString(5));
    				flowInfoDTO.setFk_flow_index_id(rs.getString(6));
    				list.add(flowInfoDTO);
    			}
    			closeConn();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return list;
    }
    
    @SuppressWarnings("unchecked")
    public List<FlowInfoDTO> getDocInfo(String userId) {
		List<FlowInfoDTO> list = new ArrayList<FlowInfoDTO>();
    	try {
    		if (StringUtil.isNotEmpty(userId)) {
    			conn = getConn();
    			String sql = "SELECT t.ID,t.TITLE,t.CREATER_TIME FROM V_PUB_WORKTASK T"
    				+ " where work_type in ('oa_bumfsend', 'oa_bumfreceive') "
    				+ " and t.STATUS = '0' and t.HANDLER_ID='"+userId+"'"
    				+ " ORDER BY T.CREATER_TIME DESC";
    			pstmt = conn.prepareStatement(sql);
    			rs = pstmt.executeQuery();
    			while (rs.next()){
    				FlowInfoDTO flowInfoDTO = new FlowInfoDTO();
    				flowInfoDTO.setId(rs.getString(1));
    				flowInfoDTO.setTitle(rs.getString(2));
    				flowInfoDTO.setCreater_time(rs.getDate(3));
    				list.add(flowInfoDTO);
    			}
    			closeConn();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return list;
    }
    
    
    /**
	 * 根据业务ID和流程环节id查询信息
	 * @param info_id -- 业务ID
	 * @param flow_note_id -- 流程环节id
	 * @author GaoYa
	 * @date 2015-10-19 上午10:32:00
	 */
	@SuppressWarnings("unchecked")
	public String getActivity_id(String info_id, String flow_note_id){
		String activity_id = "";
		String sql = "select v.ACTIVITY_ID from tzsb_inspection_info t,V_PUB_WORKTASK v ";
		sql += "where t.id=v.SERVICE_ID and v.STATUS='0' and t.flow_note_id=v.flow_num_id and t.data_status<>'99'";
		if (StringUtil.isNotEmpty(info_id)) {
			sql += " and t.id='"+info_id+"'";
		}
		if (StringUtil.isNotEmpty(flow_note_id)) {
			sql += " and t.flow_note_id='"+flow_note_id+"'";
		}
		List list = this.createSQLQuery(sql).list(); 
		if(list.size()>0){
			activity_id = String.valueOf(list.get(0));
		}
		return activity_id;
	}
	
	/**
	 * 根据业务ID查询流程信息
	 * @param info_id -- 业务ID
	 * @author GaoYa
	 * @date 2015-10-27 下午03:32:00
	 */
	@SuppressWarnings("unchecked")
	public String getProcess(String info_id){
		String process_id = "";
		String sql = "select t.ID from FLOW_PROCESS t";
		if (StringUtil.isNotEmpty(info_id)) {
			sql += " where t.business_id='"+info_id+"'";
		}
		List list = this.createSQLQuery(sql).list(); 
		if(list.size()>0){
			process_id = String.valueOf(list.get(0));
		}
		return process_id;
	}
   
	// 获取数据库连接
    public Connection getConn() {
        try {
            conn = Factory.getDB().getConnetion();
        } catch (Exception e) {
        	logger.error("获取数据库连接失败：" + e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }

    // 关闭连接
    public void closeConn() {
        try {
            /*if (null != rs)
                rs.close();
            if (null != pstmt)
                pstmt.close();
            if (null != conn)
                conn.close();*/
        	Factory.getDB().freeConnetion(conn);	// 释放连接
        } catch (Exception e) {
        	logger.error("关闭数据库连接错误：" + e.getMessage());
            e.printStackTrace();
        }
    }

	public Object getActivtyInfo(String infoId) {
		String sql = "select a.name, a.id\n" +
				"  from flow_process p, flow_activity a\n" + 
				" where a.process = p.id\n" + 
				"   and p.business_id = ? " + 
				"   and a.state = '300'";
		List<Object> list = this.createSQLQuery(sql, infoId).list();
		if(list.size()>0) {
			return list.get(0);
		}
		return null;
	}
}
