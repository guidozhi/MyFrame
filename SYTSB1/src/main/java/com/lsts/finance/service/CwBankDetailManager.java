package com.lsts.finance.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.khnt.base.Factory;
import com.khnt.base.SysParaInf;
import com.khnt.core.crud.manager.impl.EntityManageImpl;
import com.khnt.pub.fileupload.service.AttachmentManager;
import com.khnt.security.CurrentSessionUser;
import com.khnt.security.util.SecurityUtil;
import com.khnt.utils.StringUtil;
import com.lsts.device.bean.DeviceDTO;
import com.lsts.finance.bean.CwBankDetail;
import com.lsts.finance.dao.CwBankDetailDao;
import com.lsts.log.bean.SysDataMdyLog;
import com.lsts.log.bean.SysLog;
import com.lsts.log.dao.SysLogDao;
import com.sun.org.apache.xpath.internal.operations.And;


/**
 *银行转账明细manager
 *
 * @author dxf
 *
 * @date 2015年10月16日
 */
@Service("cwBankDetailManager")
public class CwBankDetailManager extends EntityManageImpl<CwBankDetail,CwBankDetailDao>{
    @Autowired
    CwBankDetailDao cwBankDetailDao;
    @Autowired
    AttachmentManager attachmentManager;
    @Autowired
	private SysLogDao sysLogDao;
    
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
	 * 保存导入的财务数据
	 * 
	 * @param file
	 * @return
     * @throws ParseException 
	 */
	public String[] saveBankData(String files, CurrentSessionUser user) throws ParseException {
		JSONArray array = JSONArray.fromObject(files);
		String[] contents = new String[2];
		contents[0]="0";
		contents[1]="";
		String[] fileName = new String[array.length()];// 文件名
		String[] filePath = new String[array.length()];// 文件路径
//		String[] result= new String[array.length()];
		for (int i = 0; i < array.length(); i++) {
			String[] content = new String[2];
			fileName[i] = array.getJSONObject(i).getString("name");
			filePath[i] = attachmentManager.download(array.getJSONObject(i).getString("id")).getFilePath();
			content = saveDate(fileName[i], filePath[i], user);
			contents[0] = Integer.toString(Integer.parseInt(contents[0])+Integer.parseInt(content[0]));
			contents[1] = contents[1]+content[1];
		}
		return contents;
	}

	/**
	 * 根据导入的文件名获取并解析财务数据
	 * 
	 * @param file
	 * @throws ParseException 
	 * @throws IOException
	 */
	public String[] saveDate(String fileName, String filePath, CurrentSessionUser user) throws ParseException {
		String repData="";//重复数据
		int total=0;//导入成功的数量
		String[] content = new String[2];
		File bankfile = new File(filePath); // 创建文件对象  
		Workbook bankWb=null;
		try {
			bankWb = WorkbookFactory.create(bankfile);
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    Sheet bankSheet = bankWb.getSheetAt(0);//获得sheet
	    //取出前一个月的银行转账信息
	    List<CwBankDetail> cwBankDetailM = cwBankDetailDao.getCwBankDetailM();
	    for (int i=1;i<=bankSheet.getLastRowNum();i++) {
	    	Row row = bankSheet.getRow(i);
	    	if(row!=null && !"null".equals(row)) {
	    		if(row.getCell(0)!=null && !"null".equals(row.getCell(0))) {
		    		CwBankDetail cwBankDetail = new CwBankDetail();
			    	cwBankDetail.setJyTime(row.getCell(0).getStringCellValue().trim()!=""?parseDate(row.getCell(0).getStringCellValue().trim()):null);
			    	cwBankDetail.setMoney(new BigDecimal(row.getCell(1).getNumericCellValue()));
			    	cwBankDetail.setRestMoney(new BigDecimal(row.getCell(1).getNumericCellValue()));
			    	cwBankDetail.setAccountName(row.getCell(2).getStringCellValue().trim()!=""?row.getCell(2).getStringCellValue().trim():null);
			    	if(cwBankDetailM!=null && cwBankDetailM.size()>0) {
			    		for(int j=0;j<cwBankDetailM.size();j++){
			    			//判断交易时间重复的转账数据
			    			if(sdf.format(cwBankDetail.getJyTime()).equals(sdf.format(cwBankDetailM.get(j).getJyTime()))){
			    				repData = repData+sdf.format(cwBankDetail.getJyTime())+"</br>";
			    			}
			    		}
			    	}
			    	total = total + 1;
			    	cwBankDetailDao.save(cwBankDetail);
	    		}
	    	}
	    }
//		} catch (Exception e) {
//			 //导入失败
//			 e.printStackTrace();
//		}
	    content[0]=Integer.toString(total);
	    content[1]=repData;
		return content;
	}
	
	/**
	 * 日期转换
	 * @param st
	 * @return
	 * @throws ParseException
	 */
	@SuppressWarnings("finally")
	public Date parseDate(String st) throws ParseException {
		Date dt=null;
		String ymd="";
		String hms="";
		try {
//			if (st.indexOf("至")>-1){
//				return null;
//			}
//			if (st.indexOf("今")>-1){
//				return null;
//			}
			ymd = st.substring(0,4)+"/"+st.substring(4,6)+"/"+st.substring(6,8);
			hms = st.substring(8, st.length());
			st = ymd+hms;
			dt = sdf.parse(sdf.format(sdf.parse(st.trim().replaceAll("[./—]", "-"))));
		} finally {
			return dt;
		}
	}
	
	/**
	 * 数值转换
	 * @param st
	 * @return
	 */
	@SuppressWarnings("finally")
	public BigDecimal parseBigDecimal(String st) {
		BigDecimal bg=null;
		try {
			bg = new BigDecimal(st);
		} finally {
			return bg;
		}
	}
	
	public String getSystemFilePath() {
		
		SysParaInf sp = Factory.getSysPara();
		String attachmentPath = sp.getProperty("attachmentPath");
		String attachmentPathType = sp.getProperty("attachmentPathType", "relative");
		
		if ("relative".equals(attachmentPathType)) {
			return Factory.getWebRoot() + File.separator + attachmentPath;
		}
		return attachmentPath;
	}
	
	/**
	 * 根据银行转账ids获取银行转账信息
	 * @param ids -- 银行转账id字符串
	 * 
	 * @return 
	 * @author GaoYa
	 * @date 2015-11-18 上午10:56:00
	 */
	public List<CwBankDetail> queryBankInfos(String ids) throws Exception{
		return cwBankDetailDao.queryBankInfos(ids);
	}
	
	/**
	 * 根据银行转账ids获取转账剩余金额
	 * @param ids -- 银行转账id字符串
	 * 
	 * @return 
	 * @author GaoYa
	 * @date 2015-12-03 上午08:56:00
	 */
	public double queryBankMoney(String ids) throws Exception{
		return cwBankDetailDao.queryBankMoney(ids);
	}
	
	/**删除
	 * @param ids
	 */
	public void delete(String ids) {
		cwBankDetailDao.delete(ids);
    }
	/**
	 * 获取旧数据并执行保存操作
	 * @param request
	 * @param cwBankDetail
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> changeCom(HttpServletRequest request, CwBankDetail cwBankDetail)
			throws Exception {
		Map<String, String> oldMap = cwBankDetailDao.queryBankDetailById(cwBankDetail.getId());
		cwBankDetailDao.save(cwBankDetail);
		return oldMap;
	}
	/**
	 * 
	 * @param infoMap
	 * @param oldMap
	 * @param newMap
	 * @param request
	 */
	public void compareMapOfBean(CwBankDetail cwBankDetail,Map<String, String> oldMap, Map<String, String> newMap, HttpServletRequest request){
		CurrentSessionUser user = SecurityUtil.getSecurityUser();
		SysLog sysLog = new SysLog();
		try {
			Iterator it = newMap.keySet().iterator(); 
			String op_remark="";
			while(it.hasNext()){ 
			    String key; 
			    String new_value; 
			    String old_value;
			    String keyTemp="";
			    key = it.next().toString(); 
			    new_value = newMap.get(key); 
			    old_value = oldMap.get(key);
			    if(key.equals("jy_time")){
			    	keyTemp="交易时间";
			    }else if(key.equals("money")){
			    	keyTemp="转入金额";
			    }else if(key.equals("account_name")){
			    	keyTemp="对方户名";
			    }else if(key.equals("rest_money")){
			    	keyTemp="剩余金额";
			    }else if(key.equals("used_money")){
			    	keyTemp="已收金额";
			    }else if(key.equals("remrk")){
			    	keyTemp="备注";
			    }
			    if(new_value == null && old_value == null){
			    	continue;
			    }
			    if(new_value == null && old_value == ""){
			    	continue;
			    }
			    if(new_value == "" && old_value == null){
			    	continue;
			    }
			    if (!new_value.equals(old_value)) {
			    	if(StringUtil.isNotEmpty(op_remark)){
			    		op_remark+=","+keyTemp+":"+old_value+"变更为"+new_value;
			    	}else{
			    		op_remark+=keyTemp+":"+old_value+"变更为"+new_value;
			    	}
				}
			} 
			if(StringUtil.isNotEmpty(op_remark)){
				sysLog.setBusiness_id(cwBankDetail.getId());
				sysLog.setOp_user_id(user.getId());
				sysLog.setOp_user_name(user.getName());
				sysLog.setOp_time(new Date());
				sysLog.setOp_action("修改往来账转账信息");
				sysLog.setOp_remark(op_remark);
				sysLog.setOp_ip(request != null ? request.getRemoteAddr() : "");
				sysLogDao.save(sysLog);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveCusAccount(CwBankDetail cwBankDetail) throws Exception{
		String sql = "select count(*) from TJY2_CW_CUS_ACCOUNT where ACCOUNT_NAME =? And TRANSFER_PERSON=?";
		List<Object> list = cwBankDetailDao.createSQLQuery(sql, cwBankDetail.getAccountName(),cwBankDetail.getTransferPerson()).list();
		if("0".equals(list.get(0).toString())){
			String sql1 = "insert into TJY2_CW_CUS_ACCOUNT (id,ACCOUNT_NAME,TRANSFER_PERSON,TRANSFER_PERSON_TEL,TRANSFER_ADDRESS) values (?,?,?,?,?)";
			cwBankDetailDao.createSQLQuery(sql1, UUID.randomUUID().toString().replaceAll("-", ""),cwBankDetail.getAccountName(),cwBankDetail.getTransferPerson(),
					cwBankDetail.getTransferPersonTel(),cwBankDetail.getTransferAddress()).executeUpdate();
		}else{
			String sql2 = "update TJY2_CW_CUS_ACCOUNT set TRANSFER_PERSON_TEL=?,TRANSFER_ADDRESS=? where ACCOUNT_NAME=? AND TRANSFER_PERSON=?";
			cwBankDetailDao.createSQLQuery(sql2, cwBankDetail.getTransferPersonTel(),cwBankDetail.getTransferAddress(),
					cwBankDetail.getAccountName(),cwBankDetail.getTransferPerson()).executeUpdate();
		}
	}
}
