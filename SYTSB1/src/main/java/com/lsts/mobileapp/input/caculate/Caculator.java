package com.lsts.mobileapp.input.caculate;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.khnt.utils.DateUtil;
import com.khnt.utils.StringUtil;
import com.lsts.constant.Constant;
import com.lsts.device.bean.CablewayPara;
import com.lsts.device.bean.CranePara;
import com.lsts.device.bean.DeviceDocument;
import com.lsts.device.bean.ElevatorPara;
import com.lsts.device.bean.RidesPara;
import com.lsts.device.dao.DeviceDao;
import com.lsts.inspection.bean.InspectionInfo;
import com.lsts.inspection.bean.InspectionInfoPay;
import com.lsts.inspection.dao.InspectionInfoPayDao;
import com.lsts.inspection.dao.ReportItemRecordDao;
import com.lsts.report.dao.ReportDao;
@Component
public class Caculator {
	
	@Autowired
	InspectionInfoPayDao payDao;
	@Autowired
	ReportDao reportDao;
	@Autowired
	ReportItemRecordDao reportItemRecordDao;
	@Autowired
	DeviceDao deviceDao;
	
	public void caculate(InspectionInfo info) throws Exception {
		//设备id
		String ddocId = info.getFk_tsjc_device_document_id();
		//设备基础信息
		DeviceDocument ddoc = deviceDao.get(ddocId);
		
		if(ddoc.getDevice_sort().startsWith("3")) 
		{//电梯
			if(StringUtil.isEmpty(info.getXsqts()) || "null".equals(info.getXsqts())){
				info.setXsqts("0");	
			}
			cElevator(info,ddoc);
		}
		else if(ddoc.getDevice_sort().startsWith("4"))//起重
		{
			cCrane(info, ddoc);
		}
		else if(ddoc.getDevice_sort().startsWith("5"))//厂车
		{
			cLocomotive(info, ddoc);
		}
		else if(ddoc.getDevice_sort().startsWith("6"))//游乐
		{
			cAmusement(info, ddoc);
		}
		else if(ddoc.getDevice_sort().startsWith("9"))//索道
		{
			cRopeway(info, ddoc);
		}
	}
	
	/**
	  *  根据参数名称获取参数值，先从itemvalue 中去取最新值，没有取设备参数信息表中的值
	 * @param info
	 * @param itemName
	 * @param ddoc
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Object getItemValue(InspectionInfo info,DeviceDocument ddoc,String itemName) throws Exception{
		String hql = "select r.item_value from ReportItemRecord r,InspectionInfo i where r.fk_inspection_info_id = i.id and i.id = ? and r.item_name = ?";
		List<Object> list= reportItemRecordDao.createQuery(hql, info.getId(),itemName).list();
		if(null!=list&&list.size()>0) {
			return list.get(0);
		}if(null==list || list.size()==0) {
			//将要返回参数值
			Object obj = null;
			//要调用的get方法
			Method gm = null;
			//参数名
			String field = itemName.substring(0,1).toUpperCase() + itemName.substring(1); 
			if(ddoc.getDevice_sort().startsWith("3")) {//电梯
				Collection<ElevatorPara> c = ddoc.getElevatorParas();
				ElevatorPara p = c.iterator().next();
				gm = p.getClass().getMethod("get" + field);
				obj = gm.invoke(p);
			}
			else if(ddoc.getDevice_sort().startsWith("4"))//起重
			{
				Collection<CranePara> c = ddoc.getCrane();
				CranePara p = c.iterator().next();
				gm = p.getClass().getMethod("get" + field);
				obj = gm.invoke(p);
			}
			else if(ddoc.getDevice_sort().startsWith("6"))//游乐
			{
				Collection<RidesPara> c = ddoc.getRidesPara();
				RidesPara p = c.iterator().next();
				gm = p.getClass().getMethod("get" + field);
				obj = gm.invoke(p);
			}
			else if(ddoc.getDevice_sort().startsWith("9"))//索道
			{
				Collection<CablewayPara> c = ddoc.getCableway();
				CablewayPara p = c.iterator().next();
				gm = p.getClass().getMethod("get" + field);
				obj = gm.invoke(p);
			}
			return obj;
		}
		return null;
	}
	/**
	  *  电梯费用
	 * @param info
	 * @param report
	 * @param ddoc
	 * @param elevatorParams
	 * @throws Exception
	 */
	public void cElevator(InspectionInfo info,DeviceDocument ddoc) throws Exception{
		
		String checkUnitId = info.getCheck_unit_id(); // 检验部门
		if(StringUtil.isEmpty(checkUnitId)) {
			throw new Exception("费用计算：检验部门参数不确定");
		}
		// 西藏报告收费标准，直梯（有机房、无机房）定检1000元整，扶梯定检800元整，杂物梯定检400元整
		// 新疆报告收费标准，直梯（有机房、无机房）定检1000元整，扶梯定检800元整，杂物梯定检400元整
		if (Constant.CHECK_UNIT_100069.equals(checkUnitId)) {// 100069：赴藏特检突击队
			double xz_money = 1000.00;
			if (ddoc.getDevice_sort().equals("3100") || ddoc.getDevice_sort().equals("3200")) {
				xz_money = 1000.00;
			} else if (ddoc.getDevice_sort().equals("3400")) {
				if (ddoc.getDevice_sort_code().equals("3430")) {
					xz_money = 400.00;
				}
			} else if (ddoc.getDevice_sort().equals("3300")) {
				xz_money = 800.00;
			}
			info.setAdvance_fees(xz_money);
			return;
		}
		String checkType = info.getCheck_category_code(); // 获取检验类型
		if(StringUtil.isEmpty(checkType)) {
			throw new Exception("费用计算：检验类型参数不确定");
		}
		//分病床、非病床
		String reportName = reportDao.get(info.getReport_type()).getReport_name();
		// 从2015-08-01开始，检验的电梯，不再打折；2015-08-01以前检验的电梯，均打9折
		Date checkDate = info.getAdvance_time();
		Date noCheapDate = DateUtil.convertStringToDate("yyyy-MM-dd", "2015-08-01");
		// 打折后，四舍五入
		int impreciseFee = 0; 
		// 不打折，也不四舍五入
		double preciseFee = 0;
		//电梯层数
		int elevatorFloor = 0;
		try {
			elevatorFloor = Integer.valueOf(getItemValue(info,ddoc,"p30002005").toString());
		}catch(Exception e) {
			elevatorFloor = 1;
		}
		
		// 九寨沟计算公式无1.3和1.5的系数
		if(reportName.contains("病床") && Constant.CHECK_UNIT_100091.equals(checkUnitId) && "3".equals(checkType) && elevatorFloor <= 5) 
		{//病床、九寨、定期、floor <= 5
				if (info.getXsqts().equals("0")) 
				{// 没有勾选限速器
					impreciseFee = (int) Math.round((500));
				} 
				else if (info.getXsqts().equals("1")) 
				{// 勾选一个限速器
					impreciseFee = (int) Math.round(500 + 200);
				} 
				else if (info.getXsqts().equals("2")) 
				{// 勾选两个限速器
					impreciseFee = (int) Math.round(500 + 400);
				}
		}
		else if(reportName.contains("病床") && Constant.CHECK_UNIT_100091.equals(checkUnitId) && "3".equals(checkType) && elevatorFloor > 5)
		{//病床、九寨、定期、floor > 5
			if (info.getXsqts().equals("0")) 
			{// 没有勾选限速器
				impreciseFee = (int) Math.round(500 + (elevatorFloor - 5) * 50);
			} else if (info.getXsqts().equals("1")) 
			{// 勾选一个限速器
				impreciseFee = (int) Math.round((500 + (elevatorFloor - 5) * 50) + 200);
			} else if (info.getXsqts().equals("2")) 
			{// 勾选两个限速器
				impreciseFee = (int) Math.round((500 + (elevatorFloor - 5) * 50) + 400);
			}
		}
		else if(reportName.contains("病床") && Constant.CHECK_UNIT_100091.equals(checkUnitId) && "2".equals(checkType) && elevatorFloor <= 5)
		{//病床、九寨、监督、floor <= 5
			impreciseFee = (int) Math.round(500);
		}
		else if(reportName.contains("病床") && Constant.CHECK_UNIT_100091.equals(checkUnitId) && "2".equals(checkType) && elevatorFloor > 5)
		{//病床、九寨、监督、floor > 5
			impreciseFee = (int) Math.round((500 + (elevatorFloor - 5) * 50));
		}
		else if(reportName.contains("病床") && !Constant.CHECK_UNIT_100090.equals(checkUnitId) && "3".equals(checkType) && elevatorFloor <= 5) 
		{//病床、非新疆、定期、floor <= 5
				if (info.getXsqts().equals("0")) 
				{// 没有勾选限速器
					impreciseFee = (int) Math.round((500));
				} 
				else if (info.getXsqts().equals("1")) 
				{// 勾选一个限速器
					impreciseFee = (int) Math.round(500 * 1.3 + 200);
					
				} 
				else if (info.getXsqts().equals("2")) 
				{// 勾选两个限速器
					impreciseFee = (int) Math.round(500 * 1.3 + 400);
				}
		}
		else if(reportName.contains("病床") && !Constant.CHECK_UNIT_100090.equals(checkUnitId) && "3".equals(checkType) && elevatorFloor > 5) 
		{//病床、非新疆、定期、floor > 5
				if (info.getXsqts().equals("0")) 
				{// 没有勾选限速器
					impreciseFee = (int) Math.round(500 + (elevatorFloor - 5) * 50);
				} 
				else if (info.getXsqts().equals("1")) 
				{// 勾选一个限速器
					impreciseFee = (int) Math.round((500 + (elevatorFloor - 5) * 50) * 1.3 + 200);
					
				} 
				else if (info.getXsqts().equals("2")) 
				{// 勾选两个限速器
					impreciseFee = (int) Math.round((500 + (elevatorFloor - 5) * 50) * 1.3 + 400);
				}
		}
		else if(reportName.contains("病床") && !Constant.CHECK_UNIT_100090.equals(checkUnitId) && "2".equals(checkType) && elevatorFloor <= 5) 
		{//病床、非新疆、监督、floor <= 5
			impreciseFee = (int) Math.round(500 * 1.5);
		}
		else if(reportName.contains("病床") && !Constant.CHECK_UNIT_100090.equals(checkUnitId) && "2".equals(checkType) && elevatorFloor > 5) 
		{//病床、非新疆、监督、floor > 5
			impreciseFee = (int) Math.round((500 + (elevatorFloor - 5) * 50) * 1.5);
		}
		
		//非病床
		else {
			if(Constant.CHECK_UNIT_100091.equals(checkUnitId)&& "3".equals(checkType) ) 
			{//九寨、定期
				// 区分设备类型
				if (ddoc.getDevice_sort().equals("3100") 
						|| ddoc.getDevice_sort().equals("3200")
						|| ddoc.getDevice_sort_code().equals("3410")
						|| ddoc.getDevice_sort_code().equals("3420")) 
				{
					// 判断是否有限速器
					if (info.getXsqts().equals("0")) {// 没有勾选限速器
						// 判断电梯层数
						if (elevatorFloor <= 5) {
							preciseFee = 550;
						} else {
							preciseFee = (550 + (elevatorFloor - 5) * 55);
						}
					} else if (info.getXsqts().equals("1")) {// 勾选一个限速器
						if (elevatorFloor <= 5) {
							preciseFee = 550 + 200;
						} else {
							preciseFee = (550 + (elevatorFloor - 5) * 55) + 200;
						}

					} else if (info.getXsqts().equals("2")) {// 勾选两个限速器
						if (elevatorFloor <= 5) {
							preciseFee = 550 + 400;
						} else {
							preciseFee = (550 + (elevatorFloor - 5) * 55) + 400;
						}
					}
				} else if (ddoc.getDevice_sort().equals("3300")) {
					preciseFee = 400;
				} else if (ddoc.getDevice_sort_code().equals("3430")) {
					if (info.getXsqts().equals("0")) {// 没有勾选限速器
						preciseFee = 400;
					} else if (info.getXsqts().equals("1")) {// 勾选一个限速器
						preciseFee = 400 + 200;
					} else if (info.getXsqts().equals("2")) {// 勾选两个限速器
						preciseFee = 400 + 200 + 200;
					}
				}
			}
			else if(Constant.CHECK_UNIT_100091.equals(checkUnitId) && "2".equals(checkType) ) 
			{//九寨、定期
				if (ddoc.getDevice_sort_code().equals("3410")
						|| ddoc.getDevice_sort_code().equals("3420")
						|| ddoc.getDevice_sort().equals("3200")
						|| ddoc.getDevice_sort().equals("3100")) 
				{
					if (info.getXsqts().equals("0")) {// 没有勾选限速器
						// 判断电梯层数
						if (elevatorFloor <= 5) {
							preciseFee = 550;
						} else {
							preciseFee = (550 + (elevatorFloor - 5) * 55);
						}

					} else if (info.getXsqts().equals("1")) {// 勾选一个限速器
						if (elevatorFloor <= 5) {
							preciseFee = 550 + 200;
						} else {
							preciseFee = (550 + (elevatorFloor - 5) * 55) + 200;
						}
					} else if (info.getXsqts().equals("2")) {// 勾选一个限速器
						if (elevatorFloor <= 5) {
							preciseFee = 550 + 400;
						} else {
							preciseFee = (550 + (elevatorFloor - 5) * 55) + 400;
						}
					}
				} else if (ddoc.getDevice_sort().equals("3300")) {
					preciseFee = 600;
				} else if (ddoc.getDevice_sort_code().equals("3430")) {
					if (info.getXsqts().equals("0")) {// 没有勾选限速器
						preciseFee = 600;
					} else if (info.getXsqts().equals("1")) {// 勾选一个限速器
						preciseFee = 600 + 200;
					} else if (info.getXsqts().equals("2")) {// 勾选两个限速器
						preciseFee = 600 + 200 + 200;
					}
				}
			}
			else if(!Constant.CHECK_UNIT_100090.equals(checkUnitId) && checkDate.before(noCheapDate) && "3".equals(checkType) ) 
			{//非新疆、打折、定期
				if (ddoc.getDevice_sort().equals("3100") || ddoc.getDevice_sort().equals("3200")
						|| ddoc.getDevice_sort_code().equals("3410")
						|| ddoc.getDevice_sort_code().equals("3420")) {
					// 判断是否有限速器
					if (info.getXsqts().equals("0")) {// 没有勾选限速器
						// 判断电梯层数
						if (elevatorFloor <= 5) {
							impreciseFee = (int) Math.round((550 * 0.9));
						} else {
							impreciseFee = (int) Math.round(((550 + (elevatorFloor - 5) * 55) * 0.9));
						}
					} else if (info.getXsqts().equals("1")) {// 勾选一个限速器
						if (elevatorFloor <= 5) {
							impreciseFee = (int) Math.round(((550 * 1.3 + 200) * 0.9));
						} else {
							impreciseFee = (int) Math.round((((550 + (elevatorFloor - 5) * 55) * 1.3 + 200) * 0.9));
						}

					} else if (info.getXsqts().equals("2")) {// 勾选两个限速器
						if (elevatorFloor <= 5) {
							impreciseFee = (int) Math.round(((550 * 1.3 + 400) * 0.9));
						} else {
							impreciseFee = (int) Math.round((((550 + (elevatorFloor - 5) * 55) * 1.3 + 400) * 0.9));
						}
					}
				}
				else if (ddoc.getDevice_sort().equals("3300")) {
					impreciseFee = (int) (400 * 0.9);
				} else if (ddoc.getDevice_sort_code().equals("3430")) {
					if (info.getXsqts().equals("0")) {// 没有勾选限速器
						impreciseFee = (int) (400 * 0.9);
					} else if (info.getXsqts().equals("1")) {// 勾选一个限速器
						impreciseFee = (int) ((400 + 200) * 0.9);
					} else if (info.getXsqts().equals("2")) {// 勾选两个限速器
						impreciseFee = (int) ((400 + 200 + 200) * 0.9);
					}
				}
			}
			else if(!Constant.CHECK_UNIT_100090.equals(checkUnitId) && checkDate.before(noCheapDate) && "2".equals(checkType) ) 
			{//非新疆、打折、监督
				// 区分设备类型
				if (ddoc.getDevice_sort_code().equals("3410")
						|| ddoc.getDevice_sort_code().equals("3420")
						|| ddoc.getDevice_sort().equals("3200")
						|| ddoc.getDevice_sort().equals("3100")) 
				{
					// 判断电梯层数
					if (elevatorFloor <= 5) {
						impreciseFee = (int) Math.round((550 * 1.5 * 0.9));
					} else {
						impreciseFee = (int) Math.round(((550 + (elevatorFloor - 5) * 55) * 1.5 * 0.9));
					}

				}
				else if (ddoc.getDevice_sort().equals("3300")) {
					impreciseFee = (int) (600 * 0.9);
				} else if (ddoc.getDevice_sort_code().equals("3430")) {
					if (info.getXsqts().equals("0")) {// 没有勾选限速器
						impreciseFee = (int) (600 * 0.9);
					} else if (info.getXsqts().equals("1")) {// 勾选一个限速器
						impreciseFee = (int) ((600 + 200) * 0.9);
					} else if (info.getXsqts().equals("2")) {// 勾选两个限速器
						impreciseFee = (int) ((600 + 200 + 200) * 0.9);
					}
				}
			}
			else if(!Constant.CHECK_UNIT_100090.equals(checkUnitId) && !checkDate.before(noCheapDate) && "3".equals(checkType) ) 
			{//非新疆、不打折、定期
				// 区分设备类型
				if (ddoc.getDevice_sort().equals("3100") || ddoc.getDevice_sort().equals("3200")
						|| ddoc.getDevice_sort_code().equals("3410")
						|| ddoc.getDevice_sort_code().equals("3420")) {
					// 判断是否有限速器
					if (info.getXsqts().equals("0")) {// 没有勾选限速器
						// 判断电梯层数
						if (elevatorFloor <= 5) {
							preciseFee = 550;
						} else {
							preciseFee = (550 + (elevatorFloor - 5) * 55);
						}
					} else if (info.getXsqts().equals("1")) {// 勾选一个限速器
						if (elevatorFloor <= 5) {
							preciseFee = 550 * 1.3 + 200;
						} else {
							preciseFee = (550 + (elevatorFloor - 5) * 55) * 1.3 + 200;
						}

					} else if (info.getXsqts().equals("2")) {// 勾选两个限速器
						if (elevatorFloor <= 5) {
							preciseFee = 550 * 1.3 + 400;
						} else {
							preciseFee = (550 + (elevatorFloor - 5) * 55) * 1.3 + 400;
						}
					}
				}
				else if (ddoc.getDevice_sort().equals("3300")) {
					preciseFee = 400;
				} else if (ddoc.getDevice_sort_code().equals("3430")) {
					if (info.getXsqts().equals("0")) {// 没有勾选限速器
						preciseFee = 400;
					} else if (info.getXsqts().equals("1")) {// 勾选一个限速器
						preciseFee = 400 + 200;
					} else if (info.getXsqts().equals("2")) {// 勾选两个限速器
						preciseFee = 400 + 200 + 200;
					}
				}
			}
			else if(!Constant.CHECK_UNIT_100090.equals(checkUnitId) && !checkDate.before(noCheapDate) && "2".equals(checkType) ) 
			{//非新疆、不打折、监督
				if (ddoc.getDevice_sort_code().equals("3410")
						|| ddoc.getDevice_sort_code().equals("3420")
						|| ddoc.getDevice_sort().equals("3200")
						|| ddoc.getDevice_sort().equals("3100")) {

					if (info.getXsqts().equals("0")) {// 没有勾选限速器
						// 判断电梯层数
						if (elevatorFloor <= 5) {
							preciseFee = 550 * 1.5;
						} else {
							preciseFee = (550 + (elevatorFloor - 5) * 55) * 1.5;
						}

					} else if (info.getXsqts().equals("1")) {// 勾选一个限速器
						if (elevatorFloor <= 5) {
							preciseFee = 550 * 1.5 + 200;
						} else {
							preciseFee = (550 + (elevatorFloor - 5) * 55) * 1.5 + 200;
						}
					} else if (info.getXsqts().equals("2")) {// 勾选一个限速器
						if (elevatorFloor <= 5) {
							preciseFee = 550 * 1.5 + 400;
						} else {
							preciseFee = (550 + (elevatorFloor - 5) * 55) * 1.5 + 400;
						}
					}
				}
				else if (ddoc.getDevice_sort().equals("3300")) {
					preciseFee = 600;
				} else if (ddoc.getDevice_sort_code().equals("3430")) {
					if (info.getXsqts().equals("0")) {// 没有勾选限速器
						preciseFee = 600;
					} else if (info.getXsqts().equals("1")) {// 勾选一个限速器
						preciseFee = 600 + 200;
					} else if (info.getXsqts().equals("2")) {// 勾选两个限速器
						preciseFee = 600 + 200 + 200;
					}
				}
			}
		}
		if (Constant.CHECK_UNIT_100091.equals(checkUnitId)) 
		{
			info.setAdvance_fees(preciseFee);
		}
		else if (!Constant.CHECK_UNIT_100090.equals(checkUnitId)) 
		{
			if (impreciseFee == 0) {
				if ("复检合格".equals(info.getInspection_conclusion())) {
					info.setAdvance_fees((double) Math.round(preciseFee * 0.3));
				} else {
					info.setAdvance_fees(preciseFee);
				}

			} else {
				if ("复检合格".equals(info.getInspection_conclusion())) {
					info.setAdvance_fees((double) Math.round(impreciseFee * 0.3));
				} else {
					info.setAdvance_fees((double) impreciseFee);
				}
			}
		}
		
	}
	/**
	 * 起重设备
	 * @param info
	 * @param ddoc
	 * @param params
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void cCrane(InspectionInfo info, DeviceDocument ddoc) throws Exception{
		String checkUnitId = info.getCheck_unit_id();
		if(StringUtil.isEmpty(checkUnitId)) {
			throw new Exception("检验部门确定，不能计算！");
		}
		if (Constant.CHECK_UNIT_100090.equals(checkUnitId)) 
		{
			throw new Exception("新疆特检突击队起重机报告费用由检验员手动输入，不再自动计算价格！");
		}
		String checkType = info.getCheck_category_code(); // 获取检验类型
		String deviceSortCode = ddoc.getDevice_sort_code();//
		// 起重机费用计算
		double preciseFee = 0; 
		
		int seats = Integer.parseInt(getItemValue(info,ddoc,"p_cws").toString()); //车位数
		double moment = Integer.parseInt(getItemValue(info,ddoc,"p40002005").toString());//起重力矩
		double load = Integer.parseInt(getItemValue(info,ddoc,"p40002004").toString());//起重量【载荷】
		
		// 2016-01-27修改：桥门式起重机暂时取消自动计算金额功能，以用户手动输入为准（因起重量多数值（例如：30/15）的特殊性）
		if (ddoc.getDevice_sort().startsWith("43")) 
		{// 塔式起重机
			// 检验类别（checkType，2：监检 3：定检）
			if ("2".equals(checkType)) {
				// moment = moment / 10000;
				// 监检的起重力矩单位是t(t.m)，所以这里不需要除以10000
			} else if ("3".equals(checkType)) {
				// 定检的起重力矩单位是N.m，所以这里需要除以10000
				moment = moment / 10000;
			}
	
			if (moment <= 25) {// 起重力矩<=25
				preciseFee = 500;
			} else if (moment > 25 && moment <= 40) {// 25<起重力矩<=40
				preciseFee = 550;
			} else if (moment > 40 && moment <= 60) {// 40<起重力矩<=60
				preciseFee = 600;
			} else if (moment > 60 && moment <= 80) {// 60<起重力矩<=80
				preciseFee = 650;
			} else {// 起重力矩>80
				preciseFee = 700;
			}
	
		} 
		else if (ddoc.getDevice_sort().startsWith("44") && !"4450".equals(ddoc.getDevice_sort_code())) 
		{// 流动式起重机（排除铁路起重机）
			if (load <= 16) {// 起重量Q<=16
				preciseFee = 400;
			} else {// 起重量Q<=16
				double jsxc = load - 16;
				preciseFee = 500;
				// 每增加10t加收50元
				if (jsxc >= 10) {
					int t = (int) (jsxc / 10);
					preciseFee = 500 + t * 50;
				}
			}
		} 
		else if (ddoc.getDevice_sort().startsWith("47")) 
		{// 门坐式起重机
			if (load <= 10) {// 起重量Q<=10
				preciseFee = 500;
			} else {// 起重量Q<=16
				double jsxc = load - 10;
				preciseFee = 600;
				// 每增加10t加收50元
				if (jsxc >= 10) {
					int t = (int) (jsxc / 10);
					preciseFee = 600 + t * 50;
				}
			}
	
		}
		else if ("4D00".equals(ddoc.getDevice_sort())) 
		{// 机械式停车设备
			preciseFee = 60 * seats;
		} 
		else 
		{//others
			String hql = "";
			List<InspectionInfoPay> list = null;
			if (StringUtil.isNotEmpty(deviceSortCode)) {
				hql = "from InspectionInfoPay where device_sort_code =? and check_type=? and data_status = '0'";
				list = payDao.createQuery(hql,deviceSortCode,checkType).list();
			}else {
				hql = "from InspectionInfoPay where device_category =? and check_type=? and data_status = '0'";
				list = payDao.createQuery(hql,ddoc.getDevice_sort(),checkType).list();
			}
			if (list != null && list.size() == 1) {
				preciseFee = list.get(0).getPay_value();
			}
			else if(list != null && list.size() != 1)
			{
				throw new Exception("规则不唯一！");
			}
		}
		if ("2".equals(checkType)&& !ddoc.getDevice_sort().startsWith("48")) //4800开头的已经区分了定检 、监检
		{
			preciseFee *= 2;
		}
		// 2016-01-27修改：桥门式起重机暂时取消自动计算金额功能，以用户手动输入为准（因起重量多数值（例如：30/15）的特殊性）
		if (preciseFee != 0 && (info.getAdvance_fees() == null || info.getAdvance_fees() == 0)) 
		{
			info.setAdvance_fees((double) preciseFee);
		}
	}
	/**
	 * 厂内机动车费用计算
	 * @param info
	 * @param ddoc
	 * @throws Exception
	 */
	
	@SuppressWarnings("unchecked")
	public void cLocomotive(InspectionInfo info, DeviceDocument ddoc) throws Exception {
		String checkType = info.getInspection().getCheck_type(); // 获取检验类型
		String deviceSortCode = ddoc.getDevice_sort_code();//
		double preciseFee = 0; 
		String hql = null;
		List<InspectionInfoPay> list = null;
		if (StringUtil.isNotEmpty(deviceSortCode)) {
			hql = "from InspectionInfoPay where device_sort_code =? and data_status = '0'";
			list = payDao.createQuery(hql,deviceSortCode).list();
		}else {
			hql = "from InspectionInfoPay where device_category =? and data_status = '0'";
			list = payDao.createQuery(hql,ddoc.getDevice_sort()).list();
		}
		if (list != null && list.size() == 1) {
			preciseFee = list.get(0).getPay_value();
		}
		else if(list != null && list.size() != 1)
		{
			throw new Exception("规则不唯一！");
		}
		if ("2".equals(checkType)) 
		{
			preciseFee *= 2;
		}
		if (preciseFee != 0 && (info.getAdvance_fees() == null || info.getAdvance_fees() == 0)) {
			info.setAdvance_fees(preciseFee);
		}
	}
	/**
	  *  游乐设施费用
	 * @param info
	 * @param ddoc
	 * @param report
	 * @param params
	 */
	public void cAmusement(InspectionInfo info, DeviceDocument ddoc) throws Exception{
		double preciseFee = 0; 
		
		String checkType = info.getCheck_category_code(); // 获取检验类型
		
		int passenger = Integer.valueOf(getItemValue(info,ddoc,"p60002016").toString());//额定乘客人数、碰碰车座位数
		
		String deviceSort = ddoc.getDevice_sort();
		
		String sql = null;
		String key = null;
		if ("6900".equals(deviceSort)) //小火车类
		{
			if (passenger <= 10) {
				key = "10";
			} else if (passenger >= 11 && passenger <= 30) {
				key = "30";
			} else {
				key = "99";
			}
			sql = "select pay_value from tzsb_inspection_pay where device_sort_code = ? and check_type = ? and pay_key = '"+key+"' and data_status='0' ";
		}
		else //其他类
		{ 
			sql = "select pay_value from tzsb_inspection_pay where device_sort_code = ? and check_type = ?  and data_status='0' ";		
			
		}
		@SuppressWarnings("unchecked")
		List<Object> list = payDao.createSQLQuery(sql,deviceSort,checkType).list();
		if(null!=list && list.size() == 1) {
			preciseFee = Double.valueOf(list.get(0).toString());
		}
		else if(null!=list && list.size()!=1) 
		{
			throw new Exception("费用规则条数不唯一！");
		}
		if (preciseFee != 0 && (info.getAdvance_fees() == null || info.getAdvance_fees() == 0)) {
			info.setAdvance_fees(preciseFee);
		}
	}
	/**
	 * 索道
	 * @param info
	 * @param ddoc
	 * @param params
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void cRopeway(InspectionInfo info, DeviceDocument ddoc) throws Exception{
		double preciseFee = 0; 
		
		String checkType = info.getCheck_category_code(); // 获取检验类型
		String deviceSortCode = ddoc.getDevice_sort_code();//
		double length = Double.valueOf(getItemValue(info,ddoc,"p90002002").toString());
		// 9100：客运架空索道 9300：客运拖牵索道
		if ("9100".equals(ddoc.getDevice_sort())) {
			if (StringUtil.isEmpty(deviceSortCode)) {
				throw new Exception("设备信息不全（device_sort_code），请补全！");
			}
			List<InspectionInfoPay> list = payDao.createQuery("from InspectionInfoPay where device_sort_code =? and check_type=? and data_status = '0'",deviceSortCode,checkType).list();
			for (InspectionInfoPay inspectionInfoPay : list) {
				int pay_key = StringUtil.isNotEmpty(inspectionInfoPay.getPay_key()) ? Integer.parseInt(inspectionInfoPay.getPay_key()) : 0;
				double pay_value = inspectionInfoPay.getPay_value();
				// 在基数的基础上，斜长每增加100米，费用增加费用基数的3%
				double jsxc = length - pay_key;
				if (jsxc < 100) {
					preciseFee = pay_value;
				} else {
					int t = (int) (jsxc / 100);
					preciseFee = pay_value + (t * pay_value * 0.03);
				}
			}
		} else if ("9300".equals(ddoc.getDevice_sort())) {
			String hql = "";
			List<InspectionInfoPay> list = null;
			if (StringUtil.isNotEmpty(deviceSortCode)) {
				hql = "from InspectionInfoPay where device_sort_code =? and check_type=? and data_status = '0'";
				list = payDao.createQuery(hql,deviceSortCode,checkType).list();
			}else {
				hql = "from InspectionInfoPay where device_category =? and check_type=? and data_status = '0'";
				list = payDao.createQuery(hql,ddoc.getDevice_sort(),checkType).list();
			}
			if (list!=null && list.size() == 1) {
				preciseFee = list.get(0).getPay_value();
			}
			else if(list!=null && list.size() != 1) 
			{
				throw new Exception("费用规则条数不唯一！");
			}
		}
		if (preciseFee != 0 && (info.getAdvance_fees() == null || info.getAdvance_fees() == 0)) {
			info.setAdvance_fees(preciseFee);
		}
	}

}
