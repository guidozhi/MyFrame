package com.lsts.flow.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.khnt.base.Factory;
import com.khnt.bpm.core.bean.Activity;
import com.khnt.bpm.core.engine.FlowEngine;
import com.khnt.bpm.ext.service.FlowDefinitionManager;
import com.khnt.bpm.ext.service.FlowWorktaskManager;
import com.khnt.bpm.ext.support.FlowExtWorktaskParam;
import com.khnt.core.crud.manager.impl.EntityManageImpl;
import com.khnt.core.exception.KhntException;
import com.khnt.security.CurrentSessionUser;
import com.khnt.security.util.SecurityUtil;
import com.khnt.utils.StringUtil;
import com.lsts.flow.bean.BaseFlowConfig;
import com.lsts.flow.dao.BaseFlowConfigDao;
import com.lsts.inspection.bean.InspectionInfo;
import com.lsts.inspection.dao.InspectionInfoDao;
import com.lsts.log.service.SysLogService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 流程配置业务逻辑对象
 * @ClassName BaseFlowConfigService
 * @JDK 1.7
 * @author GaoYa
 * @date 2014-03-11 下午02:34:00
 */
@Service("baseFlowConfigService")
public class BaseFlowConfigService extends EntityManageImpl<BaseFlowConfig, BaseFlowConfigDao>{

	@Autowired
	private BaseFlowConfigDao baseFlowConfigDao;
	
	@Autowired
	private FlowDefinitionManager flowDefManager;

	@Autowired
	private FlowWorktaskManager flowExtManager;

	@Autowired
	private InspectionInfoDao infoDao ;

	@Autowired
	private SysLogService sysLogService ;
	
	// 获取流程配置信息
	public String getFlowConfig(String device_type, String check_type){
		return baseFlowConfigDao.getFlowConfig(device_type, check_type);
	}
	
	// 获取流程编号
	public String getFlowCode(String device_type, String check_type){
		return baseFlowConfigDao.getFlowCode(device_type, check_type);
	}
	// 获取流程ID
	public String getFlowCY(){
		return baseFlowConfigDao.getFlowCY();
	}
	
	// 删除流程配置信息
    public void delete(String ids) {
    	baseFlowConfigDao.delete(ids);
    }
    /**
   	 * 根据业务ID查询流程信息
   	 * @param info_id -- 业务ID
   	 *
   	 * @author GaoYa
   	 * @date 2018-08-07 上午11:37:00
   	 */
   	public String getProcess(String info_id){
   		String process_id = "";
   		String sql = "select t.ID from FLOW_PROCESS t";
   		if (StringUtil.isNotEmpty(info_id)) {
   			sql += " where t.business_id='"+info_id+"'";
   		}
   		List list = baseFlowConfigDao.createSQLQuery(sql).list();
   		if(list.size()>0){
   			process_id = String.valueOf(list.get(0));
   		}
   		return process_id;
   	}
   	/**
   	 * 启动流程
   	 * @param HashMap<String,String> map
   	 * 			key: 
   	 * 				String infoId - 业务ID
   	 * 				String flowId - 流程ID
   	 * 				JSONArray personArray  - 下一步环节处理人
   	 * @return void
   	 */
   	public void StartFlowProcess(Map<String,Object> map,
   			HttpServletRequest request) throws KhntException{
   		
   		if(Factory.getSysPara().getProperty("DEVELOP").equals("true"))
   			log.debug("-----------------------流程启动开始");
   		//声明流程参数对象
   		Map<String, Object> Param = null;
   		//业务ID
   		String infoId = (String)map.get("infoId");
   		//流程ID
   		String flowId= (String)map.get("flowId");
   		
   		//取得检验业务Bean
   		InspectionInfo  info = infoDao.get(infoId);
   		//判断是否流程已经启动,未启动才执行。
   		if("0".equals(info.getIs_flow())){
   			JSONObject databus = new JSONObject();
   			//获取流程名称
   			String flow_name = flowDefManager.get(flowId).getFlowname();
   			//获取流程编码
   			String flow_type =  flowDefManager.get(flowId).getFlowtype();
   			//组装流程参数
   			Param = setFlowParaMap(infoId,flow_type,flow_name,flowId,true);
   			
   			//将下一步操作人放入数据总线
   			if(map.get("personArray")!=null){
   				//获取启动后下一步环节处理人
   				JSONArray personArray = (JSONArray)map.get("personArray");
   	            databus.put(FlowEngine.DATA_BUS_PARTICIPANT_KEY_DEFAULT, personArray);
   	            Param.put(FlowExtWorktaskParam.DATA_BUS, databus);
   			}
            //启动流程
            Map<String, Object> flowMap = null ;
           
            try{
           		flowMap = flowExtManager.startFlowProcess(Param);
            } catch(Exception e){
        	   	e.printStackTrace();
				log.error("启动流程失败：业务ID："+infoId);
				throw new KhntException("启动流程失败：业务ID："+infoId);
            }
            //获取下一步流程步骤
   			List<Activity> list = (List<Activity>) flowMap.get(FlowExtWorktaskParam.RESULT_ACTIVITY_LIST);
   			//流程ID插入业务表
   			//info.setFkFlowIndexId(flowId);
   			//改变业务表状态 1 已启动流程
   			info.setIs_flow("1");
   			//改变业务表当前环节的ActivityId
   			info.setFlow_note_id(list.get(0).getActivityId());
   			//改变业务表当前环节的名称
   			info.setFlow_note_name(list.get(0).getName());
   			//设置业务表流程是否结束 0 - 未结束  1 - 已结束
   			info.setFlow_note_end("0");
   			
   			infoDao.save(info);
   			
   			CurrentSessionUser user = SecurityUtil.getSecurityUser();
   			
   			sysLogService.setLogs(infoId, "检验任务生成",
   					"从检验任务生成环节进入报告录入环节。",
   					user.getSysUser().getId(), user.getSysUser().getName(),
   					new Date(), request.getRemoteAddr(), "1");
   			
   			if(Factory.getSysPara().getProperty("DEVELOP").equals("true"))
   				log.debug("-----------------------流程启动结束");
   		}
   	}
   	
   	/**
   	 * 提交流程到下一环节
   	 * @param HashMap<String,String> map
   	 * 			key: 
   	 * 				String infoId - 业务ID
   	 * 				String activityId - FLOW_ACTIVITY表ID
   	 * 				String flowNodeId  - 业务流程环节ID
   	 * 				JSONArray personArray  - 下一步环节处理人（可以为null）
   	 * 				String sub_remark  - 备注（可选）
   	 * @return void
   	 */
   	public synchronized void subFlowProcess(Map<String, Object> map
   			,HttpServletRequest request) throws KhntException{
   		if(Factory.getSysPara().getProperty("DEVELOP").equals("true"))
   			log.debug("-----------------------流程处理提交开始");
   		//定义流程参数对象
   		Map<String,Object> paramMap = new HashMap<String,Object>();
   		//定义数据总线对象
   		JSONObject dataBus = new JSONObject();
   		//获取业务ID
   		String infoId=  map.get("infoId").toString();
   		//获取ActivityId
   		String activityId = map.get("activityId").toString();
   		//获取业务流程环节ID
   		String flowNodeId = map.get("flowNodeId").toString();
   		//如果传入了下一环节处理人，则把处理人放入数据总线
   		if( map.get("personArray" ) != null ){
   			JSONArray personArray = (JSONArray)map.get("personArray");
   			dataBus.put(FlowEngine.DATA_BUS_PARTICIPANT_KEY_DEFAULT,personArray );
   			paramMap.put(FlowExtWorktaskParam.DATA_BUS, dataBus);
   		}
   		
   		paramMap.put(FlowExtWorktaskParam.ACTIVITY_ID, activityId);
   		//获取业务对象
   		InspectionInfo  info  = infoDao.get(infoId);
   		//获取的业务流程环节ID与页面传来业务流程环节ID比较
   		//避免在方法执行过程中其他地方已经提交。
   		if(info.getFlow_note_id().equals(flowNodeId)){
   			Map<String, Object> flowMap = null ;
   			try {
   				flowMap = flowExtManager.submitActivity(paramMap);
   			} catch (Exception e) {
   				e.printStackTrace();
   				log.error("提交流程失败：业务ID："+infoId);
   				throw new KhntException("提交流程失败：业务ID："+infoId);
   			}
   			
   			//获取下一步流程步骤
   			List<Activity> list = (List) flowMap
   					.get(FlowExtWorktaskParam.RESULT_ACTIVITY_LIST);
   			
   			//为记录日志准备
   			//取得环节名称
   			String nodeName = info.getFlow_note_name();
   			//准备操作说明
   			String opRemark = "从【"+nodeName+"】环节进入【"
   					+list.get(0).getName()+"】环节。";
   		 	//页面提交的操作说明
   			String sub_remark = map.get("sub_remark")==null?""
   					:map.get("sub_remark").toString();
   			if(!sub_remark.equals("")) {
   				opRemark += "\n说明："+sub_remark ;
   			}
   			
   			//修改业务表业务流程环节ID
   			info.setFlow_note_id(list.get(0).getActivityId());
   			//修改业务表业务流程环节名称
   			info.setFlow_note_name(list.get(0).getName());
   			//保存业务信息
   			infoDao.save(info);
   			
   			CurrentSessionUser user = SecurityUtil.getSecurityUser();
   			
   			sysLogService.setLogs(infoId, nodeName,
   					opRemark,
   					user.getSysUser().getId(), user.getSysUser().getName(),
   					new Date(), request.getRemoteAddr(), "1");
   		}
   		if(Factory.getSysPara().getProperty("DEVELOP").equals("true"))
   			log.debug("-----------------------流程处理提交结束");
   	}
   	

   	//流程启动组装流程参数
   	public Map<String, Object> setFlowParaMap(String service_id 
   			, String service_type, String service_title, String flow_id
   			, boolean current_user_task ){
   		Map<String, Object> Param= new HashMap<String, Object>();
   		//流程业务ID
   		Param.put(FlowExtWorktaskParam.SERVICE_ID, service_id);
   		//流程编码
   		Param.put(FlowExtWorktaskParam.SERVICE_TYPE, service_type);
   		//流程标题
   		Param.put(FlowExtWorktaskParam.SERVICE_TITLE,service_title);
   		//流程ID
   		Param.put(FlowExtWorktaskParam.FLOW_DEFINITION_ID, flow_id);
   		
   		Param.put(FlowExtWorktaskParam.IS_CURRENT_USER_TASK, current_user_task);
   		return Param ;
   	}
   	//流程流转组装流程参数
   	public Map<String, Object> setFlowParaMap(String service_id 
   			, String service_type, String service_title, String flow_id ){
   		Map<String, Object> Param= new HashMap<String, Object>();
   		//流程业务ID
   		Param.put(FlowExtWorktaskParam.SERVICE_ID, service_id);
   		//流程编码
   		Param.put(FlowExtWorktaskParam.SERVICE_TYPE, service_type);
   		//流程标题
   		Param.put(FlowExtWorktaskParam.SERVICE_TITLE,service_title);
   		//流程ID
   		Param.put(FlowExtWorktaskParam.FLOW_DEFINITION_ID, flow_id);
   		
   		return Param ;
   	}

   	/**
   	 * 获取业务ID和流程环节名称获取业务流程参数(用于原始记录校核后自动提交流程使用）
   	 * 此方法中的HANDLER_ID均为报告录入人员ID
   	 *
   	 * @return
   	 * @author GaoYa
   	 * @date 2018-08-07 上午10:19:00
   	 */
   	public Map<String, Object> getInfoFlowParams(String info_id, String flow_name, String handler_id) {
   		HashMap<String, Object> map = new HashMap<String, Object>();
   		String sql = "select t.id,v.ACTIVITY_ID,t.flow_note_id"
   				+ " from tzsb_inspection_info t, V_PUB_WORKTASK v "
   				+ " where t.id = v.SERVICE_ID and v.STATUS = '0' and t.data_status <> '99'"
   				/*+ " and v.HANDLER_ID = '"+handler_id*/
   				+" and t.id= '"+info_id+"' and v.ACTIVITY_NAME = '"+flow_name+"' ";
   		List list = baseFlowConfigDao.createSQLQuery(sql).list();
   		if (!list.isEmpty()) {
   			for (int i = 0; i < list.size(); i++) {
   				Object[] objArr = list.toArray();
   				Object[] obj = (Object[]) objArr[i];
   				map.put("infoId", obj[0]);
   				map.put("activityId", obj[1]);
   				map.put("flowNodeId", obj[2]);
   				break;
   			}
   		}
   		return map;
   	}
}
