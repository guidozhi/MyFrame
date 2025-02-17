﻿<%@page
	import="org.apache.xmlbeans.impl.xb.xsdschema.IncludeDocument.Include"%>
	<%@ page language="java" import="cn.test.*" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.khnt.security.CurrentSessionUser"%>
<%@page import="com.khnt.security.util.SecurityUtil"%>
<%@ taglib uri="/WEB-INF/Ming.tld" prefix="ming"%>
<%@ page import="com.ming.webreport.*"%>
<%@ page import="com.lsts.inspection.bean.ReportItemValue"%>
<%@ page import="java.util.*"%>
<%@ page import="com.khnt.utils.StringUtil"%>
<%@ page import="com.khnt.utils.DateUtil"%>
<%@ page import="util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head pageStatus="add">
		<title>报告书录入</title>
		<%
			MRDataSet mrds = new MRDataSet();
			DataRecord rec = new DataRecord();
			//Map<String,Object> 111Map 
			////= (Map<String,Object>)request.getAttribute("supvision");
			//System.out.print(111Map);
			//处理检验业务表基本信息
			String inspection_date = ""; // 检验日期
			String inspection_date_end = ""; // 检验结束日期
			String enter_date = ""; // 编制日期
			String last_check_date = ""; // 下次检验日期（根据检验日期翻1年）
			String xsq_last_check_date = ""; // 限速器下次检验日期（根据检验日期翻2年）
			String iscopy = TS_Util
					.nullToString(request.getAttribute("ISCOPY"));
			String com_name = "";
			String com_code = "";
			String com_addr = "";
			String device_use_place = "";
			String security_op = "";
			String security_tel = "";
			String device_model = "";
			String make_date = "";
			String make_units_name = "";
			String maintain_unit_name = "";
			String device_type = "";	// 设备类别代码（代码首位，用来判断是否默认编制日期为检验日期加1天）
			String device_sort = "";	// 设备类别代码（二类，用来判断下次检验日期根据检验日期翻1年还是2年）
			String device_sort_code = TS_Util
					.nullToString(request.getAttribute("DEVICE_SORT_CODE"));	// 设备类别代码（小类，用来判断下次检验日期根据检验日期翻1年还是2年）
			String device_area_name = "";
			String device_street_name = "";	// 设备街道名称
			String kd = "";
			String cws = "";
			String p40002002 = "";

			if (iscopy.equals("1")) { //是复制报告的就先加载value表 
	
				List<ReportItemValue> reportItemValueList = (List) request
						.getAttribute("REPORTITEMVALUE");
				for (ReportItemValue riv : reportItemValueList) {
					//System.out.println("getItem_name:"+riv.getItem_name()
					//		+"----getItem_value:"+item_value);
					String item_value = "";
			    	if("ZLSCQK".equals(riv.getItem_name()) || "LAST_CHECK_PROBLEMS".equals(riv.getItem_name())
			    			|| riv.getItem_name().contains("REMARKS") || riv.getItem_name().contains("INSPECTION_CONCLUSION")
			    			 || riv.getItem_name().contains("JLBG_WTJCLYJ") || riv.getItem_name().contains("JLBG_FXWT") || riv.getItem_name().contains("DTQDP30012")){
			    		item_value = TS_Util.nullToString(riv.getItem_value());
			    	}else{
			    		item_value = TS_Util.nullToString(riv.getItem_value()).replaceAll("\r|\n","");
			    	}
					if ("COM_NAME".equals(riv.getItem_name())) {
						com_name = TS_Util.nullToString(item_value);
					} else if ("COM_ADDRESS".equals(riv.getItem_name())) {
						if(StringUtil.isEmpty(com_addr)){
							com_addr = TS_Util.nullToString(item_value);
						}
					}  else if ("DEVICE_USE_PLACE".equals(riv.getItem_name())) {
						device_use_place = TS_Util.nullToString(item_value);
					} else if ("COM_CODE".equals(riv.getItem_name())) {
						com_code = TS_Util.nullToString(item_value);
					} else if ("SECURITY_OP".equals(riv.getItem_name())) {
						security_op = TS_Util.nullToString(item_value);
						rec.setValue(riv.getItem_name(), security_op);
					} else if ("MAKE_UNITS_NAME".equals(riv.getItem_name())) {
						make_units_name = TS_Util.nullToString(item_value);
					} else if ("MAINTAIN_UNIT_NAME".equals(riv.getItem_name())) {
						maintain_unit_name = TS_Util.nullToString(item_value);
					} else if ("SECURITY_TEL".equals(riv.getItem_name())) {
						security_tel = TS_Util
								.nullToString(item_value);
					} else if ("DEVICE_MODEL".equals(riv.getItem_name())) {
						device_model = TS_Util
								.nullToString(item_value);
					} else if ("MAKE_DATE".equals(riv.getItem_name())) {
						make_date = TS_Util
								.nullToString(item_value);
					} else if ("P_KD".equals(riv.getItem_name())) {
						kd = TS_Util
						.nullToString(item_value);
					} else if("DEVICE_AREA_NAME".equals(riv.getItem_name())){
						if(StringUtil.isNotEmpty(item_value)){
							device_area_name = TS_Util.nullToString(item_value);
						}
					} else if ("DEVICE_STREET_NAME".equals(riv.getItem_name())) {
						if(StringUtil.isEmpty(device_street_name)){
							device_street_name = TS_Util
							.nullToString(item_value);
						}
					} else if ("INSPECTION_DATE_END".equals(riv.getItem_name())) {
						if(StringUtil.isEmpty(item_value)){
							//inspection_date_end = DateUtil.getDateTime("yyyy-MM-dd", new Date());
							//rec.setValue(riv.getItem_name(), inspection_date_end);
						}else{
							inspection_date_end = TS_Util
							.nullToString(item_value);
							rec.setValue(riv.getItem_name(), inspection_date_end);
						}
					} else if ("INSPECT_DATE".equals(riv.getItem_name())) {
						if(StringUtil.isNotEmpty(item_value)){
							
							rec.setValue(riv.getItem_name(), TS_Util
									.nullToString(
											item_value.toString()
													.split(" ")[0])
									.getBytes("GB2312"));
						}
					} else if ("INSPECTION_DATE".equals(riv.getItem_name())) {
						if(StringUtil.isNotEmpty(item_value)){
							inspection_date = TS_Util
									.nullToString(item_value);
							rec.setValue(riv.getItem_name(), TS_Util
									.nullToString(
											item_value.toString()
													.split(" ")[0])
									.getBytes("GB2312"));
						}
					} else if ("DRAFT_DATE".equals(riv.getItem_name())) {
						if(StringUtil.isNotEmpty(item_value)){
							enter_date = TS_Util.nullToString(item_value);
						}
					} else {
						if("4D00".equals(device_sort)){
							if ("REMARKS".equals(riv.getItem_name())) {
								cws = TS_Util.nullToString(item_value);
							}else{
								rec.setValue(TS_Util.nullToString(riv.getItem_name()),
										TS_Util.nullToString(item_value));
							}
						}else{
							rec.setValue(TS_Util.nullToString(riv.getItem_name()),
									TS_Util.nullToString(item_value));
						}
					}
				}

				Map<String, Object> infoMap = (Map<String, Object>) request
						.getAttribute("INSPECTIONINFO");
				for (String key : infoMap.keySet()) {
					String info_value = TS_Util.nullToString(infoMap.get(key)).replaceAll("\r|\n","");
					if (("REPORT_COM_NAME").equals(key)) {//报告书上使用单位名称

						rec.setValue("COM_NAME", info_value.getBytes("GB2312"));
						com_name = info_value;
					}

					else if (("REPORT_COM_ADDRESS").equals(key)) {//报告书上使用单位名称
						if(StringUtil.isEmpty(com_addr)){
							com_addr = TS_Util.nullToString(info_value);
						}
						if(StringUtil.isEmpty(device_use_place)){
							//device_use_place = info_value;
						}
					}

					else if (("CHECK_OP_NAME").equals(key)) {///检验员

						rec.setValue("INSPECTION_OP_STR", TS_Util
								.nullToString(info_value));
					}

					else if (("ENTER_OP_NAME").equals(key)) {//检验员

						rec.setValue("INSPECTION_ENTER_STR", TS_Util
								.nullToString(info_value).getBytes(
										"GB2312"));
					} else if (("ENTER_TIME").equals(key)) {//编制日期
						enter_date = info_value;
						if (info_value != null) {
							rec
									.setValue("DRAFT_DATE", TS_Util
											.nullToString(
													info_value.toString()
															.split(" ")[0])
											.getBytes("GB2312"));
						}

					} else if (("ADVANCE_TIME").equals(key)) {//检验时间
						if (StringUtil.isEmpty(inspection_date) && StringUtil.isNotEmpty(info_value)) {
							inspection_date = TS_Util.nullToString(info_value.toString()
									.split(" ")[0]);							
						}	
						if(StringUtil.isNotEmpty(inspection_date)){
							rec
							.setValue("INSPECTION_DATE", TS_Util
									.nullToString(
											inspection_date.toString()
													.split(" ")[0])
									.getBytes("GB2312"));
					rec
							.setValue("INSPECTION_DATE＿1", TS_Util
									.nullToString(
											inspection_date.toString()
													.split(" ")[0])
									.getBytes("GB2312"));
					rec
							.setValue("INSPECTION_DATE＿2", TS_Util
									.nullToString(
											inspection_date.toString()
													.split(" ")[0])
									.getBytes("GB2312"));
					rec
					.setValue("INSPECTION_DATE_1", TS_Util
							.nullToString(
									inspection_date.toString()
											.split(" ")[0])
							.getBytes("GB2312"));
			rec
					.setValue("INSPECTION_DATE_2", TS_Util
							.nullToString(
									inspection_date.toString()
											.split(" ")[0])
							.getBytes("GB2312"));
					rec
							.setValue("INSPECTION_DATE_TOP", TS_Util
									.nullToString(
											inspection_date.toString()
													.split(" ")[0])
									.getBytes("GB2312"));
						}
					} else if (("LAST_CHECK_TIME").equals(key)) {//下一次检验时间
						if (!info_value.equals("")
								&& info_value != null) {
							last_check_date = info_value.toString();
							Calendar calendar = Calendar.getInstance();
							calendar.setTime(DateUtil.convertStringToDate(
									"yyyy-MM-dd", last_check_date));
							calendar.add(Calendar.YEAR, 1);
							//calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
							xsq_last_check_date = DateUtil.getDate(calendar
									.getTime());
							rec.setValue("LAST_INSPECTION_DATE",
									last_check_date);
							//客运索道下次检验日期（报告只显示年月，不需要精确到日，但返写设备基础信息时，需要精确到年月日）
							String ky_last_check_date = TS_Util.nullToString(last_check_date).substring(0,TS_Util.nullToString(last_check_date).lastIndexOf("-"));
							rec.setValue("KY_LAST_INSPECTION_DATE",
											TS_Util.nullToString(ky_last_check_date));
						}
					} else if (("CONFIRM_DATE").equals(key)) {
						// 校核日期不做任何处理
					} else {

						rec.setValue(key, TS_Util
								.nullToString(info_value).getBytes(
										"GB2312"));
					}
				}
				
				if(!"2610".equals(device_sort_code) && !"2210".equals(device_sort_code) && !"2220".equals(device_sort_code)){
					String  inpect_date="";
					//处理设备主表信息
					Map<String, Object> docMap = (Map<String, Object>) request
							.getAttribute("DEVICEDOCUMENT");
					for (String key : docMap.keySet()) {
						String device_value = TS_Util.nullToString(docMap.get(key)).replaceAll("\r|\n","");
						if (("DEVICE_SORT_CODE").equals(key)) {
							device_type = device_value
									.substring(0, 1);
							/*
							device_sort_code = device_value;
							
							String temp = device_value
									.substring(0, 2);*/
							//if("2".equals(device_type)){
								rec.setValue(key, device_value);
							/*}else{
								rec.setValue(key, TS_Util.nullToString(temp).getBytes(
										"GB2312"));

								rec.setValue("DEVICE_DETAIL_CODE", TS_Util
										.nullToString(device_value).getBytes(
												"GB2312"));
							}*/
						} else if (("DEVICE_SORT").equals(key)) {
							device_sort = device_value;
							rec.setValue(key, TS_Util.nullToString(device_value));
						}else if (("INSPECT_DATE").equals(key)) {
							
							if (docMap.get(key)!=null) {
								
								String inspect_date = TS_Util.nullToString(docMap.get(
										key).toString().split(" ")[0]);
								rec
								.setValue("INSPECT_DATE", TS_Util
										.nullToString(
												inspect_date)
										.getBytes("GB2312"));
							}
						}
						else if (("LAST_CHECK_TIME").equals(key)) {//下一次检验时间（根据检验日期翻1年）
							if (!infoMap.get(key).equals("")
									&& infoMap.get(key) != null) {
								last_check_date = TS_Util.nullToString(infoMap
										.get(key));
								Calendar calendar = Calendar.getInstance();
								calendar.setTime(DateUtil.convertStringToDate(
										"yyyy-MM-dd", last_check_date));
								calendar.add(Calendar.YEAR, 1);
								//calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
								xsq_last_check_date = DateUtil.getDate(calendar
										.getTime());
								rec.setValue("LAST_INSPECTION_DATE",
										last_check_date);
								//客运索道下次检验日期（报告只显示年月，不需要精确到日，但返写设备基础信息时，需要精确到年月日）
								String ky_last_check_date = TS_Util.nullToString(last_check_date).substring(0,TS_Util.nullToString(last_check_date).lastIndexOf("-"));
								rec.setValue("KY_LAST_INSPECTION_DATE",
												TS_Util.nullToString(ky_last_check_date));
							}
						} else if (("SECURITY_TEL").equals(key)) {
							if(StringUtil.isEmpty(security_tel)){
								security_tel = TS_Util.nullToString(device_value);
							}
						}else if(key.equals("SECURITY_OP")){
							if(StringUtil.isEmpty(security_op)){
								rec.setValue(key
										,TS_Util.nullToString(device_value));
							}
						} else if (("DEVICE_MODEL").equals(key)) {
							if(StringUtil.isEmpty(device_model)){
								device_model = TS_Util.nullToString(device_value);
							}
						}  else if (("MAKE_DATE").equals(key)) {
							if(StringUtil.isEmpty(make_date)){
								make_date = TS_Util.nullToString(device_value);
							}
						}  else if (("DEVICE_USE_PLACE").equals(key)) {
							if(StringUtil.isEmpty(device_use_place)){
								device_use_place = TS_Util.nullToString(device_value);
							}
						} else if (("DEVICE_AREA_NAME").equals(key)) {
							if(StringUtil.isEmpty(device_area_name)){
								device_area_name = TS_Util.nullToString(device_value);
							}
						}  else if (("DEVICE_STREET_NAME").equals(key)) {
							if(StringUtil.isEmpty(device_street_name)){
								device_street_name = TS_Util.nullToString(device_value);
							}
						} else if (("USE_DATE").equals(key)) {// 投入使用日期
							if("2610".equals(device_sort_code)){
								if (StringUtil.isNotEmpty(device_value)) {
									device_value = device_value.replaceAll("－", "-");
									device_value = device_value.replaceAll("/", "-");
									device_value = device_value.replaceAll(".", "-");
									Calendar calendar = Calendar.getInstance();
									calendar.setTime(DateUtil.convertStringToDate(
											"yyyy-MM-dd", device_value));
									String use_date_str = DateUtil.getDate(calendar
											.getTime());
									//常压罐车投入使用日期（报告只显示年月，不需要精确到日）
									String use_date = TS_Util.nullToString(use_date_str).substring(0,4)+"年"+TS_Util.nullToString(use_date_str).substring(5,7)+"月";
									rec.setValue("USE_DATE",
													TS_Util.nullToString(use_date));
								}
							}else{
								rec.setValue(key, TS_Util.nullToString(device_value)
										.getBytes("GB2312"));
							}
						} else if (("INSPECT_DATE").equals(key)) {// 上次检验日期
							inpect_date=device_value;
							
						}/* else if (("MAKE_DATE").equals(key)) {// 上次检验日期
							if("2610".equals(device_sort_code)){
								if (StringUtil.isNotEmpty(device_value)) {
									device_value = device_value.replaceAll("－", "-");
									device_value = device_value.replaceAll("/", "-");
									device_value = device_value.replaceAll(".", "-");
									if(device_value.indexOf("年")==-1){
										Calendar calendar = Calendar.getInstance();
										calendar.setTime(DateUtil.convertStringToDate(
												"yyyy-MM-dd", device_value));
										String make_date_str = DateUtil.getDate(calendar
												.getTime());
										//常压罐车投入使用日期（报告只显示年月，不需要精确到日）
										String make_date = TS_Util.nullToString(make_date_str).substring(0,4)+"年"+TS_Util.nullToString(make_date_str).substring(5,7)+"月";
										rec.setValue("MAKE_DATE",
														TS_Util.nullToString(make_date));
									}
								}
							}else{
								rec.setValue(key, TS_Util.nullToString(device_value)
										.getBytes("GB2312"));
							}
							
						}*/  else {

							rec.setValue(key, TS_Util.nullToString(device_value)
									.getBytes("GB2312"));
						}
					}
				}
			
				
				//处理企业信息
				//	Map<String,Object> comMap 
				//		= (Map<String,Object>)request.getAttribute("COMPANY");
				//	for (String key : comMap.keySet()) {
				//System.out.println("================="+docMap.get(key));
				//		rec.setValue(key, 
				//				TS_Util.nullToString(comMap.get(key)).getBytes("GB2312") );
				//	}
				//处理压力容器参数表信息
				//	Map<String,Object> accMap 
				//		= (Map<String,Object>)request.getAttribute("ACCESSORY");
				//	for (String key : accMap.keySet()) {
				//System.out.println("================="+docMap.get(key));
				//		rec.setValue(key, 
				//				TS_Util.nullToString(accMap.get(key)).getBytes("GB2312") );
				//	}
				//处理锅炉参数表信息
				Map<String, Object> boilerMap = (Map<String, Object>) request
						.getAttribute("BOILERPARA");
				for (String key : boilerMap.keySet()) {
					rec.setValue(key, TS_Util.nullToString(boilerMap.get(key))
							.getBytes("GB2312"));
				}

				//处理客运索道表信息
				Map<String, Object> calMap = (Map<String, Object>) request
						.getAttribute("CALPARA");
				for (String key : calMap.keySet()) {

					if (("FIRST_INSTALL_DATE").equals(key)) {

						String made_date = "";

						if (calMap.get(key) != null
								&& !calMap.get(key).equals("")) {

							if (calMap.get(key).toString().indexOf(" ") != -1) {
								made_date = calMap.get(key).toString().split(
										" ")[0];

							} else {
								made_date = calMap.get(key).toString();
							}

							rec.setValue(key, TS_Util.nullToString(made_date)
									.getBytes("GB2312"));
						}
					} else if (("REMAKE_DATE").equals(key)) {

						String made_date = "";

						if (calMap.get(key) != null
								&& !calMap.get(key).equals("")) {

							if (calMap.get(key).toString().indexOf(" ") != -1) {
								made_date = calMap.get(key).toString().split(
										" ")[0];

							} else {
								made_date = calMap.get(key).toString();
							}

							rec.setValue(key, TS_Util.nullToString(made_date)
									.getBytes("GB2312"));
						}
					} else if (("REPAIR_DATE").equals(key)) {

						String made_date = "";

						if (calMap.get(key) != null
								&& !calMap.get(key).equals("")) {

							if (calMap.get(key).toString().indexOf(" ") != -1) {
								made_date = calMap.get(key).toString().split(
										" ")[0];

							} else {
								made_date = calMap.get(key).toString();
							}
							rec.setValue(key, TS_Util.nullToString(made_date)
									.getBytes("GB2312"));
						}
					} else {

						rec.setValue(key, TS_Util.nullToString(calMap.get(key))
								.getBytes("GB2312"));
					}
				}

				if(!"2610".equals(device_sort_code) && !"2210".equals(device_sort_code) && !"2220".equals(device_sort_code)){
					//处理压力容器参数表信息
					Map<String, Object> vesselMap = (Map<String, Object>) request
							.getAttribute("VESSELPARA");
					for (String key : vesselMap.keySet()) {
						String device_value = TS_Util.nullToString(vesselMap.get(key)).replaceAll("\r|\n","");
						if (("DESIGN_DATE").equals(key)) {
							if("2610".equals(device_sort_code)){
								if (StringUtil.isNotEmpty(device_value)&&device_value.indexOf("年")==-1) {
									device_value = device_value.replaceAll("－", "-");
									device_value = device_value.replaceAll("/", "-");
									device_value = device_value.replaceAll(".", "-");
									Calendar calendar = Calendar.getInstance();
									calendar.setTime(DateUtil.convertStringToDate(
											"yyyy-MM-dd", device_value));
									String design_date_str = DateUtil.getDate(calendar
											.getTime());
									//常压罐车投入使用日期（报告只显示年月，不需要精确到日）
									String design_date = TS_Util.nullToString(design_date_str).substring(0,4)+"年"+TS_Util.nullToString(design_date_str).substring(5,7)+"月";
									System.out.println("啊啊啊啊啊啊啊啊啊啊啊啊啊=======12======常压罐车投入使用日期："+design_date);
									rec.setValue("DESIGN_DATE",
													TS_Util.nullToString(design_date));
								}
							}else{
								rec.setValue(key, TS_Util.nullToString(device_value)
										.getBytes("GB2312"));
							}
						}else{
						
							rec.setValue(key, TS_Util.nullToString(vesselMap.get(key))
								.getBytes("GB2312"));
						}
						
						
					}
				}

				//处理电梯参数表信息
				Map<String, Object> elevatorMap = (Map<String, Object>) request
						.getAttribute("ELEVATORPARA");
				for (String key : elevatorMap.keySet()) {

					//System.out.println("================="+docMap.get(key));
					rec.setValue(key, TS_Util
							.nullToString(elevatorMap.get(key)).getBytes(
									"GB2312"));
				}

				//处理起重机参数表信息
				Map<String, Object> craneMap = (Map<String, Object>) request
						.getAttribute("CRANEPARA");
				for (String key : craneMap.keySet()) {
					//System.out.println("================="+docMap.get(key));
					if("P_KD".equals(key)){
						if(StringUtil.isEmpty(kd)){
							kd = TS_Util.nullToString(craneMap.get(key));
						}
					}else if("P40002002".equals(key)){
						p40002002 = TS_Util.nullToString(craneMap.get(key));
					}else if("P_CWS".equals(key)){
						if("4D00".equals(device_sort)){
							if(StringUtil.isEmpty(cws)){
								String cws1 = TS_Util.nullToString(craneMap.get(key));
								if(StringUtil.isNotEmpty(cws1)){
									rec.setValue("REMARKS", "车位数："+cws1+"个");
								}
							}else{
								rec.setValue("REMARKS", cws);
							}
						}
						rec.setValue(key, TS_Util.nullToString(craneMap.get(key))
								.getBytes("GB2312"));
					}else{
						rec.setValue(key, TS_Util.nullToString(craneMap.get(key))
								.getBytes("GB2312"));
					}
				}

				//处理场内机动车参数表信息
				Map<String, Object> engineMap = (Map<String, Object>) request
						.getAttribute("ENGINEPARA");
				for (String key : engineMap.keySet()) {
					//System.out.println("================="+docMap.get(key));
					rec.setValue(key, TS_Util.nullToString(engineMap.get(key))
							.getBytes("GB2312"));
				}

				//处理游乐设施参数表信息
				Map<String, Object> rideMap = (Map<String, Object>) request
						.getAttribute("RIDEPARA");
				for (String key : rideMap.keySet()) {
					//System.out.println("================="+docMap.get(key));
					rec.setValue(key, TS_Util.nullToString(rideMap.get(key))
							.getBytes("GB2312"));
				}

				//处理产品监检表
				Map<String, Object> supMap = (Map<String, Object>) request
						.getAttribute("supvision");
				for (String key : supMap.keySet()) {

					if (("PRODUCT_MADE_DATE").equals(key)) {//检验时间

						String made_date = "";

						if (supMap.get(key) != null
								&& !supMap.get(key).equals("")) {

							if (supMap.get(key).toString().indexOf(" ") != -1) {
								made_date = supMap.get(key).toString().split(
										" ")[0].split("-")[0]
										+ "-"
										+ supMap.get(key).toString().split(" ")[0]
												.split("-")[1];

							} else {
								made_date = supMap.get(key).toString().split(
										"-")[0]
										+ "-"
										+ supMap.get(key).toString().split("-")[1];
							}

							rec.setValue(key, TS_Util.nullToString(made_date)
									.getBytes("GB2312"));
						}
					} else {
						rec.setValue(key, TS_Util.nullToString(supMap.get(key))
								.getBytes("GB2312"));
					}
				}
				//rec.setValue("LAST_INSPECTION_DATE",request.getAttribute("LASTDATE"));
				//rec.setValue("NEXT_INSPECTION_DATE",request.getAttribute("LASTDATE"));//下次检验日期(为了某些报告不能将下次检验日期回写到设备信息上)
				if (StringUtil.isNotEmpty(com_name)) {
					rec.setValue("COM_NAME", com_name);
				}
				if (StringUtil.isNotEmpty(com_code)) {
					rec.setValue("COM_CODE", com_code);
				}
				
				if(StringUtil.isNotEmpty(com_addr)){
					rec.setValue("COM_ADDRESS", com_addr);
				}
				if (StringUtil.isNotEmpty(device_use_place)) {
					rec.setValue("DEVICE_USE_PLACE", device_use_place);
				}
				if (StringUtil.isNotEmpty(security_op)) {
					rec.setValue("SECURITY_OP", security_op);
				}
				if (StringUtil.isNotEmpty(make_units_name)) {
					rec.setValue("MAKE_UNITS_NAME", make_units_name);
				}
				if (StringUtil.isNotEmpty(maintain_unit_name)) {
					rec.setValue("MAINTAIN_UNIT_NAME", maintain_unit_name);
				}
				
				if(StringUtil.isNotEmpty(security_tel)){
					rec.setValue("SECURITY_TEL", security_tel);
				}
				
				if(StringUtil.isNotEmpty(device_model)){
					rec.setValue("DEVICE_MODEL", device_model);
				}
				if(StringUtil.isNotEmpty(make_date)){
					rec.setValue("MAKE_DATE", make_date.getBytes("GB2312"));
				}
				if(StringUtil.isEmpty(kd)){
					rec.setValue("P_KD", p40002002);
				}else{
					rec.setValue("P_KD", kd);
				}
				if(StringUtil.isEmpty(p40002002)){
					rec.setValue("P_KD", kd);
				}
				if(StringUtil.isNotEmpty(device_area_name)){
					rec.setValue("DEVICE_AREA_NAME", device_area_name);
				}
				if(StringUtil.isNotEmpty(device_street_name)){
					rec.setValue("DEVICE_STREET_NAME", device_street_name);
				}
				if(StringUtil.isEmpty(inspection_date_end)){
					//inspection_date_end = DateUtil.getDateTime("yyyy-MM-dd", new Date());
					// 默认锅炉检验结束日期为系统当前日期
					//rec.setValue("INSPECTION_DATE_END", inspection_date_end);	
				}
			} else {

				String check_op_name = "";

				Map<String, Object> infoMap = (Map<String, Object>) request.getAttribute("INSPECTIONINFO");
				String ky_last_check_date = "";
				System.out.println("=================" + infoMap.size());

				for (String key : infoMap.keySet()) {
					System.out.println("=================" + key + "==" + infoMap.get(key));
					String info_value = TS_Util.nullToString(infoMap.get(key)).replaceAll("\r|\n", "");

					if (("REPORT_COM_NAME").equals(key)) {//报告书上使用单位名称

						rec.setValue("COM_NAME", info_value.getBytes("GB2312"));
						com_name = info_value;
					}

					else if (("REPORT_COM_ADDRESS").equals(key)) {//报告书上使用单位名称
						if (StringUtil.isEmpty(com_addr)) {
							com_addr = info_value;
						}
						if (StringUtil.isEmpty(device_use_place)) {
							//device_use_place = info_value;
						}
					}

					else if (("CHECK_OP_NAME").equals(key)) {///检验员

						check_op_name = info_value;

						rec.setValue("INSPECTION_OP_STR", info_value);
					}

					else if (("ENTER_OP_NAME").equals(key)) {//检验员

						rec.setValue("INSPECTION_ENTER_STR", info_value.getBytes("GB2312"));
					} else if (("ENTER_TIME").equals(key)) {//编制日期
						enter_date = info_value;
						if (infoMap.get(key) != null) {
							rec.setValue("DRAFT_DATE", info_value.split(" ")[0].getBytes("GB2312"));
						}

					} else if (("ADVANCE_TIME").equals(key)) {//检验时间
						if (StringUtil.isEmpty(inspection_date) && StringUtil.isNotEmpty(info_value)) {
							inspection_date = TS_Util.nullToString(info_value.split(" ")[0]);
							System.out.println("%%%%%%%%%%%%%%%%%%%" + inspection_date);
						}
						if (StringUtil.isNotEmpty(inspection_date)) {
							rec.setValue("INSPECTION_DATE",
									TS_Util.nullToString(inspection_date.toString().split(" ")[0]).getBytes("GB2312"));
							rec.setValue("INSPECTION_DATE＿1",
									TS_Util.nullToString(inspection_date.toString().split(" ")[0]).getBytes("GB2312"));
							rec.setValue("INSPECTION_DATE＿2",
									TS_Util.nullToString(inspection_date.toString().split(" ")[0]).getBytes("GB2312"));
							rec.setValue("INSPECTION_DATE_1",
									TS_Util.nullToString(inspection_date.toString().split(" ")[0]).getBytes("GB2312"));
							rec.setValue("INSPECTION_DATE_2",
									TS_Util.nullToString(inspection_date.toString().split(" ")[0]).getBytes("GB2312"));
							rec.setValue("INSPECTION_DATE_TOP",
									TS_Util.nullToString(inspection_date.toString().split(" ")[0]).getBytes("GB2312"));
						}

					} else if (("LAST_CHECK_TIME").equals(key)) {//下一次检验时间
						if (!infoMap.get(key).equals("") && infoMap.get(key) != null) {
							last_check_date = info_value;
							Calendar calendar = Calendar.getInstance();
							calendar.setTime(DateUtil.convertStringToDate("yyyy-MM-dd", last_check_date));
							calendar.add(Calendar.YEAR, 1);
							//calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
							xsq_last_check_date = DateUtil.getDate(calendar.getTime());
							rec.setValue("LAST_INSPECTION_DATE", last_check_date);
							//客运索道下次检验日期（报告只显示年月，不需要精确到日，但返写设备基础信息时，需要精确到年月日）
							ky_last_check_date = TS_Util.nullToString(last_check_date).substring(0,
									TS_Util.nullToString(last_check_date).lastIndexOf("-"));
							System.out.println("啊啊啊啊啊啊啊啊啊啊啊啊啊=======21======客运索道下次检验日期：" + ky_last_check_date);
							rec.setValue("KY_LAST_INSPECTION_DATE", TS_Util.nullToString(ky_last_check_date));
						}
					} else {

						rec.setValue(key, info_value.getBytes("GB2312"));
					}
				}

				String inpect_date = "";

				//处理设备主表信息
				Map<String, Object> docMap = (Map<String, Object>) request.getAttribute("DEVICEDOCUMENT");
				for (String key : docMap.keySet()) {
					String device_value = TS_Util.nullToString(docMap.get(key)).replaceAll("\r|\n", "");

					System.out.println("啊啊啊啊啊啊啊啊啊啊啊啊啊=======" + key + "===____________________：" + device_value);
					if (("DEVICE_SORT_CODE").equals(key)) {
						device_type = device_value.substring(0, 1);
						/*
						device_sort_code = device_value;
						String temp = device_value
								.substring(0, 2);
						if("2".equals(device_type)){*/
						rec.setValue(key, device_value);
						/*
						}else{
							rec.setValue(key, TS_Util.nullToString(temp).getBytes(
									"GB2312"));
						
							rec.setValue("DEVICE_DETAIL_CODE", TS_Util
									.nullToString(device_value).getBytes(
											"GB2312"));
						}*/
					} else if (("DEVICE_SORT").equals(key)) {
						device_sort = device_value;
						rec.setValue(key, TS_Util.nullToString(device_value));
					} else if (key.equals("SECURITY_OP")) {
						security_op = TS_Util.nullToString(device_value);
						rec.setValue(key, TS_Util.nullToString(device_value));
					} else if ("DEVICE_USE_PLACE".equals(key)) {
						if (StringUtil.isEmpty(device_use_place)) {
							device_use_place = TS_Util.nullToString(device_value);
						}
					} else if (("DEVICE_AREA_NAME").equals(key)) {
						if (StringUtil.isEmpty(device_area_name)) {
							device_area_name = TS_Util.nullToString(device_value);
						}
					} else if (("DEVICE_STREET_NAME").equals(key)) {
						if (StringUtil.isEmpty(device_street_name)) {
							device_street_name = TS_Util.nullToString(device_value);
						}
					} else if (("USE_DATE").equals(key)) {// 投入使用日期
						if ("2610".equals(device_sort_code)) {
							if (StringUtil.isNotEmpty(device_value)) {
								if (!"/".equals(device_value.trim()) && !"－".equals(device_value.trim())
										&& !"-".equals(device_value.trim())) {
									device_value = device_value.trim();
									device_value = device_value.replaceAll("－", "-");
									device_value = device_value.replaceAll("/", "-");
									device_value = device_value.replaceAll(".", "-");
									Calendar calendar = Calendar.getInstance();
									calendar.setTime(DateUtil.convertStringToDate("yyyy-MM-dd", device_value));
									String use_date_str = DateUtil.getDate(calendar.getTime());
									//常压罐车投入使用日期（报告只显示年月，不需要精确到日）
									String use_date = TS_Util.nullToString(use_date_str).substring(0, 4) + "年"
											+ TS_Util.nullToString(use_date_str).substring(5, 7) + "月";
									rec.setValue("USE_DATE", TS_Util.nullToString(use_date));
								}else{
									rec.setValue(key, TS_Util.nullToString(device_value).getBytes("GB2312"));
								}
							}
						} else {
							rec.setValue(key, TS_Util.nullToString(device_value).getBytes("GB2312"));
						}
					} else if (("INSPECT_DATE").equals(key)) {// 上次检验日期
						inpect_date = device_value;

					}/* else if (("MAKE_DATE").equals(key)) {// 上次检验日期
						if ("2610".equals(device_sort_code)) {
							if (StringUtil.isNotEmpty(device_value) && device_value.indexOf("年") == -1) {
								if (!"/".equals(device_value.trim()) && !"－".equals(device_value.trim())
										&& !"-".equals(device_value.trim())) {
									device_value = device_value.trim();
									device_value = device_value.replaceAll("－", "-");
									device_value = device_value.replaceAll("/", "-");
									device_value = device_value.replaceAll(".", "-");
									Calendar calendar = Calendar.getInstance();
									calendar.setTime(DateUtil.convertStringToDate("yyyy-MM-dd", device_value));
									String make_date_str = DateUtil.getDate(calendar.getTime());
									//常压罐车投入使用日期（报告只显示年月，不需要精确到日）
									String make_date = TS_Util.nullToString(make_date_str).substring(0, 4) + "年"
											+ TS_Util.nullToString(make_date_str).substring(5, 7) + "月";
									rec.setValue("MAKE_DATE", TS_Util.nullToString(make_date));
								}else{
									rec.setValue(key, TS_Util.nullToString(device_value).getBytes("GB2312"));
								}
							}
						} else {
							rec.setValue(key, TS_Util.nullToString(device_value).getBytes("GB2312"));
						}

					} */else {

						rec.setValue(key, TS_Util.nullToString(device_value).getBytes("GB2312"));
					}
				}
				//判断如果是常压罐车 日期改成年月
				if ("2610".equals(device_sort_code)) {
					if (StringUtil.isNotEmpty(inpect_date)) {
						if (StringUtil.isNotEmpty(inpect_date) && inpect_date.indexOf("年") == -1) {
							if (!"/".equals(inpect_date.trim()) && !"－".equals(inpect_date.trim())
									&& !"-".equals(inpect_date.trim())) {
								inpect_date = inpect_date.trim();
								inpect_date = inpect_date.replaceAll("－", "-");
								inpect_date = inpect_date.replaceAll("/", "-");
								inpect_date = inpect_date.replaceAll(".", "-");
								Calendar calendar = Calendar.getInstance();
								calendar.setTime(DateUtil.convertStringToDate("yyyy-MM-dd", inpect_date.split(" ")[0]));
								String inspect_date_str = DateUtil.getDate(calendar.getTime());
								String inspectDate = TS_Util.nullToString(inspect_date_str).substring(0, 4) + "年"
										+ TS_Util.nullToString(inspect_date_str).substring(5, 7) + "月";
								rec.setValue("INSPECT_DATE", TS_Util.nullToString(inspectDate));
							}
						}else{
							rec.setValue("INSPECT_DATE", TS_Util.nullToString(inpect_date).getBytes("GB2312"));
						}
					}
				} else {
					rec.setValue("INSPECT_DATE", TS_Util.nullToString(inpect_date).getBytes("GB2312"));

				}
				//处理企业信息
				Map<String, Object> comMap = (Map<String, Object>) request.getAttribute("COMPANY");

				for (String key : comMap.keySet()) {
					System.out.println("_______________+++++++++++++++++++++++++" + key);
					if (("COM_NAME").equals(key)) {

						if (com_name.equals("")) {
							com_name = TS_Util.nullToString(comMap.get(key));
						}
					} else if (("COM_ADDRESS").equals(key)) {
						if (com_addr.equals("")) {
							com_addr = TS_Util.nullToString(comMap.get(key));
						}

					}
				}
				//处理压力容器参数表信息
				//	Map<String,Object> accMap 
				//		= (Map<String,Object>)request.getAttribute("ACCESSORY");
				//	for (String key : accMap.keySet()) {
				//System.out.println("================="+device_value);
				//		rec.setValue(key, 
				//				TS_Util.nullToString(accMap.get(key)).getBytes("GB2312") );
				//	}
				//处理锅炉参数表信息
				Map<String, Object> boilerMap = (Map<String, Object>) request.getAttribute("BOILERPARA");
				for (String key : boilerMap.keySet()) {
					//System.out.println("================="+device_value);
					rec.setValue(key, TS_Util.nullToString(boilerMap.get(key)).getBytes("GB2312"));
				}

				//处理客运索道表信息
				Map<String, Object> calMap = (Map<String, Object>) request.getAttribute("CALPARA");
				for (String key : calMap.keySet()) {
					//System.out.println("================="+device_value);

					if (("FIRST_INSTALL_DATE").equals(key)) {

						String made_date = "";

						if (calMap.get(key) != null && !calMap.get(key).equals("")) {

							if (calMap.get(key).toString().indexOf(" ") != -1) {
								made_date = calMap.get(key).toString().split(" ")[0];

							} else {
								made_date = calMap.get(key).toString();
							}

							rec.setValue(key, TS_Util.nullToString(made_date).getBytes("GB2312"));
						}
					} else if (("REMAKE_DATE").equals(key)) {

						String made_date = "";

						if (calMap.get(key) != null && !calMap.get(key).equals("")) {

							if (calMap.get(key).toString().indexOf(" ") != -1) {
								made_date = calMap.get(key).toString().split(" ")[0];

							} else {
								made_date = calMap.get(key).toString();
							}

							rec.setValue(key, TS_Util.nullToString(made_date).getBytes("GB2312"));
						}
					} else if (("REPAIR_DATE").equals(key)) {

						String made_date = "";

						if (calMap.get(key) != null && !calMap.get(key).equals("")) {

							if (calMap.get(key).toString().indexOf(" ") != -1) {
								made_date = calMap.get(key).toString().split(" ")[0];

							} else {
								made_date = calMap.get(key).toString();
							}

							rec.setValue(key, TS_Util.nullToString(made_date).getBytes("GB2312"));
						}
					} else {

						rec.setValue(key, TS_Util.nullToString(calMap.get(key)).getBytes("GB2312"));
					}
				}

				//处理压力容器参数表信息
				Map<String, Object> vesselMap = (Map<String, Object>) request.getAttribute("VESSELPARA");
				for (String key : vesselMap.keySet()) {
					//System.out.println("================="+device_value);
					String device_value = TS_Util.nullToString(vesselMap.get(key)).replaceAll("\r|\n", "");
					if (("DESIGN_DATE").equals(key)) {
						if ("2610".equals(device_sort_code)) {
							if (StringUtil.isNotEmpty(device_value) && device_value.indexOf("年") == -1) {
								if (!"/".equals(device_value.trim()) && !"－".equals(device_value.trim())
										&& !"-".equals(device_value.trim())) {
									device_value = device_value.trim();
									device_value = device_value.replaceAll("－", "-");
									device_value = device_value.replaceAll("/", "-");
									device_value = device_value.replaceAll(".", "-");
									Calendar calendar = Calendar.getInstance();
									calendar.setTime(DateUtil.convertStringToDate("yyyy-MM-dd", device_value));
									String design_date_str = DateUtil.getDate(calendar.getTime());
									//常压罐车投入使用日期（报告只显示年月，不需要精确到日）
									String design_date = TS_Util.nullToString(design_date_str).substring(0, 4) + "年"
											+ TS_Util.nullToString(design_date_str).substring(5, 7) + "月";
									rec.setValue("DESIGN_DATE", TS_Util.nullToString(design_date));
								}else{
									rec.setValue(key, TS_Util.nullToString(device_value).getBytes("GB2312"));
								}
							}
						} else {
							rec.setValue(key, TS_Util.nullToString(device_value).getBytes("GB2312"));
						}
					} else {

						rec.setValue(key, TS_Util.nullToString(vesselMap.get(key)).getBytes("GB2312"));
					}

				}

				//处理电梯参数表信息
				Map<String, Object> elevatorMap = (Map<String, Object>) request.getAttribute("ELEVATORPARA");
				for (String key : elevatorMap.keySet()) {

					//System.out.println("================="+device_value);
					rec.setValue(key, TS_Util.nullToString(elevatorMap.get(key)).getBytes("GB2312"));
				}

				//处理起重机参数表信息
				Map<String, Object> craneMap = (Map<String, Object>) request.getAttribute("CRANEPARA");
				for (String key : craneMap.keySet()) {
					//System.out.println("================="+device_value);
					if ("P_KD".equals(key)) {
						if (StringUtil.isEmpty(kd)) {
							kd = TS_Util.nullToString(craneMap.get(key));
						}
					} else if ("P40002002".equals(key)) {
						p40002002 = TS_Util.nullToString(craneMap.get(key));
					} else if ("P_CWS".equals(key)) {
						if ("4D00".equals(device_sort)) {
							if (StringUtil.isEmpty(cws)) {
								String cws1 = TS_Util.nullToString(craneMap.get(key));
								if (StringUtil.isNotEmpty(cws1)) {
									cws = "车位数：" + cws1 + "个";
									//rec.setValue("REMARKS", "车位数："+cws+"个");
								}
							}
						}
						rec.setValue(key, TS_Util.nullToString(craneMap.get(key)).getBytes("GB2312"));
					} else {
						rec.setValue(key, TS_Util.nullToString(craneMap.get(key)).getBytes("GB2312"));
					}
				}

				//处理场内机动车参数表信息
				Map<String, Object> engineMap = (Map<String, Object>) request.getAttribute("ENGINEPARA");
				for (String key : engineMap.keySet()) {
					//System.out.println("================="+device_value);
					rec.setValue(key, TS_Util.nullToString(engineMap.get(key)).getBytes("GB2312"));
				}

				//处理游乐设施参数表信息
				Map<String, Object> rideMap = (Map<String, Object>) request.getAttribute("RIDEPARA");
				for (String key : rideMap.keySet()) {
					//System.out.println("================="+device_value);
					rec.setValue(key, TS_Util.nullToString(rideMap.get(key)).getBytes("GB2312"));
				}

				//处理产品监检表
				Map<String, Object> supMap = (Map<String, Object>) request.getAttribute("supvision");
				for (String key : supMap.keySet()) {

					if (("PRODUCT_MADE_DATE").equals(key)) {//检验时间

						System.out.println("=================" + key + "=" + supMap.get(key));

						String made_date = "";

						if (supMap.get(key) != null && !supMap.get(key).equals("")) {

							if (supMap.get(key).toString().indexOf(" ") != -1) {
								made_date = supMap.get(key).toString().split(" ")[0].split("-")[0] + "-"
										+ supMap.get(key).toString().split(" ")[0].split("-")[1];

							} else {
								made_date = supMap.get(key).toString().split("-")[0] + "-"
										+ supMap.get(key).toString().split("-")[1];
							}

							rec.setValue(key, TS_Util.nullToString(made_date).getBytes("GB2312"));
						}
					} else {
						rec.setValue(key, TS_Util.nullToString(supMap.get(key)).getBytes("GB2312"));
					}
				}
				//rec.setValue("LAST_INSPECTION_DATE",request.getAttribute("LASTDATE"));
				//rec.setValue("NEXT_INSPECTION_DATE",request.getAttribute("LASTDATE"));//下次检验日期(为了某些报告不能将下次检验日期回写到设备信息上)

				List<ReportItemValue> reportItemValueList = (List) request.getAttribute("REPORTITEMVALUE");
				for (ReportItemValue riv : reportItemValueList) {
					//System.out.println("getItem_name:"+riv.getItem_name()
					//		+"----getItem_value:"+item_value);
					String item_value = "";
					if ("ZLSCQK".equals(riv.getItem_name()) || "LAST_CHECK_PROBLEMS".equals(riv.getItem_name())
							|| riv.getItem_name().contains("REMARKS")
							|| riv.getItem_name().contains("INSPECTION_CONCLUSION")
							|| riv.getItem_name().contains("JLBG_WTJCLYJ")
							|| riv.getItem_name().contains("JLBG_FXWT")
							 || riv.getItem_name().contains("DTQDP30012")) {
						item_value = TS_Util.nullToString(riv.getItem_value());
					} else {
						item_value = TS_Util.nullToString(riv.getItem_value()).replaceAll("\r|\n", "");
					}
					if ("LAST_INSPECTION_DATE".equals(riv.getItem_name())) {
						if (StringUtil.isEmpty(item_value)) {
							rec.setValue("LAST_INSPECTION_DATE", last_check_date);
						} else {
							rec.setValue("LAST_INSPECTION_DATE", TS_Util.nullToString(item_value));
						}
					} else if ("KY_LAST_INSPECTION_DATE".equals(riv.getItem_name())) {
						if (StringUtil.isEmpty(item_value)) {
							rec.setValue("KY_LAST_INSPECTION_DATE", ky_last_check_date);
						} else {
							rec.setValue("KY_LAST_INSPECTION_DATE", TS_Util.nullToString(item_value));
						}
					} else if (("CHECK_OP_NAME").equals(riv.getItem_name())) {///检验员

						rec.setValue("INSPECTION_OP_STR", TS_Util.nullToString(item_value));
					} else if ("DEVICE_USE_PLACE".equals(riv.getItem_name())) {
						if (StringUtil.isNotEmpty(item_value)) {
							device_use_place = TS_Util.nullToString(item_value);
						}
					} else if ("COM_ADDRESS".equals(riv.getItem_name())) {
						if (StringUtil.isNotEmpty(riv.getItem_value())) {
							com_addr = TS_Util.nullToString(riv.getItem_value());
						}
					} else if ("P_KD".equals(riv.getItem_name())) {
						if (StringUtil.isNotEmpty(item_value)) {
							kd = TS_Util.nullToString(item_value);
						}
					} else if (("DEVICE_AREA_NAME").equals(riv.getItem_name())) {
						if (StringUtil.isEmpty(device_area_name)) {
							device_area_name = TS_Util.nullToString(item_value);
						}
					} else if ("DEVICE_STREET_NAME".equals(riv.getItem_name())) {
						if (StringUtil.isEmpty(device_street_name)) {
							device_street_name = TS_Util.nullToString(item_value);
						}
					} else if ("SECURITY_OP".equals(riv.getItem_name())) {
						if (StringUtil.isNotEmpty(item_value)) {
							security_op = TS_Util.nullToString(item_value);
						}
						rec.setValue(riv.getItem_name(), security_op);
					} else if ("INSPECTION_DATE_END".equals(riv.getItem_name())) {
						if (StringUtil.isEmpty(item_value)) {
							//inspection_date_end = DateUtil.getDateTime("yyyy-MM-dd", new Date());
							//rec.setValue(riv.getItem_name(), inspection_date_end);
						} else {
							rec.setValue(TS_Util.nullToString(riv.getItem_name()), TS_Util.nullToString(item_value));
						}
					} else if ("INSPECT_DATE".equals(riv.getItem_name())) {
						if (StringUtil.isNotEmpty(item_value)) {
							rec.setValue(riv.getItem_name(),
									TS_Util.nullToString(item_value.toString().split(" ")[0]).getBytes("GB2312"));
						}
					} else if ("INSPECTION_DATE".equals(riv.getItem_name())) {
						if (StringUtil.isNotEmpty(item_value)) {
							inspection_date = item_value;
							rec.setValue(riv.getItem_name(),
									TS_Util.nullToString(item_value.toString().split(" ")[0]).getBytes("GB2312"));
						}
					} else {
						if ("4D00".equals(device_sort)) {
							if ("REMARKS".equals(riv.getItem_name())) {
								if (StringUtil.isNotEmpty(item_value)) {
									cws = TS_Util.nullToString(item_value);
									rec.setValue(TS_Util.nullToString(riv.getItem_name()),
											TS_Util.nullToString(item_value));
								} else {
									rec.setValue(TS_Util.nullToString(riv.getItem_name()), cws);
								}
							} else {
								rec.setValue(TS_Util.nullToString(riv.getItem_name()),
										TS_Util.nullToString(item_value));
							}
						} else {
							rec.setValue(TS_Util.nullToString(riv.getItem_name()), TS_Util.nullToString(item_value));
						}
					}
				}
				if (StringUtil.isNotEmpty(device_use_place)) {
					rec.setValue("DEVICE_USE_PLACE", device_use_place);
				}

				System.out.println("%%%%%%%%%%%%%%%%%%%@@@@@@@@@@@@@@@@@@@@@@@@@" + inspection_date);
				rec.setValue("INSPECTION_OP_STR", check_op_name);
				rec.setValue("INSPECTION_DATE", inspection_date);
				rec.setValue("INSPECTION_DATE＿1", inspection_date);
				rec.setValue("INSPECTION_DATE＿2", inspection_date);
				rec.setValue("INSPECTION_DATE_TOP", inspection_date);
				if (StringUtil.isEmpty(kd)) {
					rec.setValue("P_KD", p40002002);
				} else {
					rec.setValue("P_KD", kd);
				}
				if (StringUtil.isEmpty(p40002002)) {
					rec.setValue("P_KD", kd);
				}
				if (StringUtil.isNotEmpty(com_addr)) {
					rec.setValue("COM_ADDRESS", com_addr);
				}
				if (StringUtil.isNotEmpty(device_area_name)) {
					rec.setValue("DEVICE_AREA_NAME", device_area_name);
				}
				if (StringUtil.isNotEmpty(device_street_name)) {
					rec.setValue("DEVICE_STREET_NAME", device_street_name);
				}
				if (StringUtil.isEmpty(inspection_date_end)) {
					//inspection_date_end = DateUtil.getDateTime("yyyy-MM-dd", new Date());
					// 默认锅炉检验结束日期为系统当前日期
					//rec.setValue("INSPECTION_DATE_END", inspection_date_end);	
				}
			}

			Map<String, Object> paraMap = (Map<String, Object>) request.getAttribute("REPORTPARA");

			// 编制人
			CurrentSessionUser user = SecurityUtil.getSecurityUser();
			rec.setValue("INSPECTION_ENTER_STR", user.getName());

			Date cur_date = DateUtil.convertStringToDate("yyyy-MM-dd", DateUtil.getCurrentDateTime());
			if (StringUtil.isEmpty(enter_date)) {
				String draft_date = "";
				Calendar calendar = Calendar.getInstance();
				// 1：锅炉 2：压力容器 3：电梯 5：厂车 6：游乐设施 7：压力管道元件 8：压力管道 9：客运索道 B：部件 F：安全附件 0：其他
				if ("3".equals(device_type) || "6".equals(device_type) || "9".equals(device_type)
						|| "1".equals(device_type) || "2".equals(device_type) || "7".equals(device_type)
						|| "8".equals(device_type) || "B".equals(device_type) || "F".equals(device_type)
						|| "0".equals(device_type)) {
					calendar.setTime(DateUtil.convertStringToDate("yyyy-MM-dd", inspection_date));
					calendar.add(Calendar.DATE, 1); // 电梯类、游乐、索道类机电设备编制日期（2014-10-08要求，编制日期=检验日期+1天）
					// 厂车编制日期（机电五部2015-11-24龚高科要求，编制日期=检验日期+1天）
					//calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);

					if (calendar.getTime().after(cur_date)) {
						draft_date = DateUtil.getDate(cur_date);
					} else {
						draft_date = DateUtil.getDate(calendar.getTime());
					}
				} else {
					// 4：起重机
					if ("4".equals(device_type)) {
						calendar.setTime(DateUtil.convertStringToDate("yyyy-MM-dd", inspection_date));
						//calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);

						// 塔式起重机、升降机、流动式起重机每年检验一次，其中轮胎式集装箱门式起重机每2年检验1次
						if ("4300".equals(device_sort) || "4400".equals(device_sort) || "4800".equals(device_sort)) {
							if ("4240".equals(device_sort_code)) { // 4240：轮胎式集装箱门式起重机
								calendar.add(Calendar.YEAR, 2);
							} else {
								calendar.add(Calendar.YEAR, 1);
							}
						} else {
							// 微小型起重设备、桥式起重机、门式起重机、门座起重机、缆索起重机、桅杆起重机、铁路起重机、旋臂起重机、机械停车设备每2年检验1次，
							// 其中吊运熔融金属和炽热金属的起重机每年检验1次
							// 4150：冶金桥式起重机每年检验1次
							if ("4150".equals(device_sort_code)) {
								// 应机电五部的要求，此处暂时忽略吊运熔融金属和炽热金属的起重机的情况，后续增加
								// 4150：冶金桥式起重机每年检验1次（2017-11-07修改）
								calendar.add(Calendar.YEAR, 1);
							}else{
								calendar.add(Calendar.YEAR, 2);
							}
						}
						String next_inspection_date = DateUtil.getDateTime("yyyy-MM-dd", calendar.getTime());
						rec.setValue("LAST_INSPECTION_DATE_Y", next_inspection_date.substring(0, 4)); // 起重机下次检验日期之年份
						rec.setValue("LAST_INSPECTION_DATE_M", next_inspection_date.substring(5, 7)); // 起重机下次检验日期之月份

						// 起重机类设备编制日期
						calendar.setTime(DateUtil.convertStringToDate("yyyy-MM-dd", inspection_date));
						calendar.add(Calendar.DATE, 1); // 起重机编制日期（机电五部2015-08-24要求，编制日期=检验日期+1天）
						if (calendar.getTime().after(cur_date)) {
							draft_date = DateUtil.getDate(cur_date);
						} else {
							draft_date = DateUtil.getDate(calendar.getTime());
						}
					} else if ("5".equals(device_type)) { // 5：场（厂）内专用机动车辆
						calendar.setTime(DateUtil.convertStringToDate("yyyy-MM-dd", inspection_date));
						calendar.add(Calendar.YEAR, 1);

						String next_inspection_date = DateUtil.getDateTime("yyyy-MM-dd", calendar.getTime());
						rec.setValue("LAST_INSPECTION_DATE_Y", next_inspection_date.substring(0, 4)); // 场（厂）内专用机动车辆下次检验日期之年份
						rec.setValue("LAST_INSPECTION_DATE_M", next_inspection_date.substring(5, 7)); // 场（厂）内专用机动车辆下次检验日期之月份

						// 场（厂）内专用机动车辆类设备编制日期
						calendar.setTime(DateUtil.convertStringToDate("yyyy-MM-dd", inspection_date));
						calendar.add(Calendar.DATE, 1); // 场（厂）内专用机动车辆编制日期（机电五部2015-10-21要求，编制日期=检验日期+1天）
						if (calendar.getTime().after(cur_date)) {
							draft_date = DateUtil.getDate(cur_date);
						} else {
							draft_date = DateUtil.getDate(calendar.getTime());
						}
					} else {
						// 其他类设备编制日期默认检验日期
						draft_date = DateUtil.getDateTime("yyyy-MM-dd",
								DateUtil.convertStringToDate("yyyy-MM-dd", inspection_date));
					}

				}
				if (StringUtil.isNotEmpty(draft_date)) {
					draft_date = DateUtil.getDateTime("yyyy-MM-dd",
							DateUtil.convertStringToDate("yyyy-MM-dd", draft_date));
				}
				rec.setValue("DRAFT_DATE", draft_date); // 编制日期（2014-10-08要求，编制日期=检验日期+1天）
			} else {
				String draft_date = "";
				Calendar cal = Calendar.getInstance();
				// 1：锅炉 2：压力容器 3：电梯 5：厂车 6：游乐设施 7：压力管道元件 8：压力管道 9：客运索道 B：部件 F：安全附件 0：其他
				if ("3".equals(device_type) || "6".equals(device_type) || "9".equals(device_type)
						|| "1".equals(device_type) || "2".equals(device_type) || "7".equals(device_type)
						|| "B".equals(device_type) || "F".equals(device_type) || "0".equals(device_type)) {
					cal.setTime(DateUtil.convertStringToDate("yyyy-MM-dd", enter_date));
					//cal.add(Calendar.DATE, 1); // 电梯类、游乐、索道类机电设备编制日期（2014-10-08要求，编制日期=检验日期+1天）
					// 厂车编制日期（机电五部2015-11-24龚高科要求，编制日期=检验日期+1天）
					//calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
					if (cal.getTime().after(cur_date)) {
						draft_date = DateUtil.getDate(cur_date);
					} else {
						draft_date = DateUtil.getDate(cal.getTime());
					}
					if (StringUtil.isNotEmpty(draft_date)) {
						draft_date = DateUtil.getDateTime("yyyy-MM-dd",
								DateUtil.convertStringToDate("yyyy-MM-dd", draft_date));
					}
					rec.setValue("DRAFT_DATE", draft_date); // 编制日期（2014-10-08要求，编制日期=检验日期+1天）
				} else if ("8".equals(device_type)) {
					cal.setTime(DateUtil.convertStringToDate("yyyy-MM-dd", enter_date));
					if (cal.getTime().after(cur_date)) {
						enter_date = DateUtil.getDate(cur_date);
					} else {
						enter_date = DateUtil.getDate(cal.getTime());
					}
					if (StringUtil.isNotEmpty(enter_date)) {
						enter_date = DateUtil.getDateTime("yyyy-MM-dd",
								DateUtil.convertStringToDate("yyyy-MM-dd", enter_date));
					}
					rec.setValue("DRAFT_DATE", enter_date);
				} else {
					cal.setTime(DateUtil.convertStringToDate("yyyy-MM-dd", enter_date));
					draft_date = DateUtil.getDateTime("yyyy-MM-dd", cal.getTime());
					if (cal.getTime().after(cur_date)) {
						draft_date = DateUtil.getDate(cur_date);
					} else {
						draft_date = DateUtil.getDateTime("yyyy-MM-dd", cal.getTime());
					}
					if (StringUtil.isNotEmpty(draft_date)) {
						draft_date = DateUtil.getDateTime("yyyy-MM-dd",
								DateUtil.convertStringToDate("yyyy-MM-dd", draft_date));
					}
					rec.setValue("DRAFT_DATE", draft_date); // 编制日期（2014-10-08要求，编制日期=检验日期+1天）
				}
			}

			if (StringUtil.isNotEmpty(xsq_last_check_date)) {
				rec.setValue("XSQ_LAST_INSPECTION_DATE", xsq_last_check_date); // 限速器下次检验日期（根据检验日期翻2年）
			}

			//处理用户上传的图片信息
			Map<String, Object> picMap = (Map<String, Object>) request.getAttribute("PICTURE");
			for (String key : picMap.keySet()) {
				String keyid = key.substring(0, key.length() - 1);
				//rec.setValue(keyid, "");
				if (key.endsWith("P")) {
					rec.setValue(key, (byte[]) picMap.get(key));
				} else {
					rec.setValue(key, TS_Util.nullToString(picMap.get(key)).getBytes("GB2312"));
				}
			}

			// 显示电子签名
			Map<String, Object> imgMap = (Map<String, Object>) request.getAttribute("IMAGES");

			byte[] check_op_img = (byte[]) imgMap.get("check_op_img");
			byte[] examine_op_img = (byte[]) imgMap.get("examine_op_img");
			byte[] issue_op_img = (byte[]) imgMap.get("issue_op_img");
			byte[] enter_op_img = (byte[]) imgMap.get("enter_op_img");

			//设置打印签名不可见，电子签名可见
			rec.setValue("INSPECTION_OP_STR", "");
			rec.setValue("INSPECTION_AUDIT_STR", "");
			rec.setValue("INSPECTION_CONFIRM_STR", "");
			rec.setValue("INSPECTION_ENTER_STR", "");

			//检验员电子签名
			rec.setValue("INSPECTION_OP_PICTURE", check_op_img != null ? check_op_img : "");
			//审核电子签名
			rec.setValue("INSPECTION_AUDIT_PICTURE", examine_op_img != null ? examine_op_img : "");
			//签发(批准)电子签名
			rec.setValue("INSPECTION_CONFIRM_PICTURE", issue_op_img != null ? issue_op_img : "");
			//编制电子签名
			rec.setValue("INSPECTION_ENTER_PICTURE", enter_op_img != null ? enter_op_img : "");

			// 处理单独检验、审核人员电子签名
			Map<String, Object> page_check_imgMap = (Map<String, Object>) request.getAttribute("PAGE_CHECK_IMAGES");
			for (String key : page_check_imgMap.keySet()) {
				if (key.startsWith("INSPECT_MAN_PICTURE")) {
					String page_index = key.substring("INSPECT_MAN_PICTURE".length());
					String item_name = "INSPECT_MAN_PTR" + page_index;
					rec.setValue(item_name, ""); // 设置检验人员姓名不显示

					//rec.setValue("INSPECT_DATE"+page_index, DateUtil.getDateTime("yyyy-MM-dd", new Date()));	// 默认检验日期为系统当前日期
				}
				if (key.startsWith("AUDIT_MAN_PICTURE")) {
					String item_name = "AUDIT_MAN_PTR" + key.substring("AUDIT_MAN_PICTURE".length());
					rec.setValue(item_name, ""); // 设置审核人员姓名不显示
				}
				if (key.startsWith("EVAL_PIC_MAN_PICTURE")) {
					String page_index = key.substring("EVAL_PIC_MAN_PICTURE".length());
					String item_name = "EVAL_PIC_MAN_PTR" + page_index;
					rec.setValue(item_name, ""); // 设置评片人员姓名不显示
					//rec.setValue("EVAL_PIC_DATE"+page_index, DateUtil.getDateTime("yyyy-MM-dd", new Date()));	// 默认评片日期为系统当前日期
				}
				rec.setValue(key, page_check_imgMap.get(key)); // 设置显示人员电子签名
			}

			// 检验依据
			//String jyyjStr = TS_Util.nullToString(request.getAttribute("JYYJFILES"));
			//if(StringUtil.isNotEmpty(jyyjStr)){
			//	rec.setValue("DTQDP30009", jyyjStr);
			//}

			// 固化箱
			//List<EquipmentBox> boxList = (List) request
			//			.getAttribute("boxList");
			//String boxNum = "";
			//for(EquipmentBox equipmentBox : boxList){
			///	if(boxNum.length()>0){
			///		boxNum += "、";
			//	}
			//	boxNum += equipmentBox.getBoxNumber();
			// 主要检验仪器设备
			//	List<BaseEquipment2> equipmentList = equipmentBox.getBaseEquipment2s();
			//	for(int i=0;i<equipmentList.size();i++){
			//		BaseEquipment2 equipment = equipmentList.get(i);
			//		if(StringUtil.isNotEmpty(equipment.getEq_name())){
			//			rec.setValue("ZJSB_EQUIPMENT_NAME"+(i+1), equipment.getEq_name());	
			//		}
			//		if(StringUtil.isNotEmpty(equipment.getEq_no())){
			//			rec.setValue("ZJSB_EQUIPMENT_NO"+(i+1), equipment.getEq_no());	
			//		}
			//		if(i==5){
			//			break;
			//		}
			//	}
			//	break;
			//}
			//if(StringUtil.isNotEmpty(boxNum)){
			//	// 检验仪器设备箱号
			//	rec.setValue("JYYQSBXH", boxNum);	
			//}

			// 其他检验仪器设备
			//List<BaseEquipment2> qtEquipmentList = (List) request
			//			.getAttribute("qtEquipmentList");
			//String qtEquipmentStr = "";
			//int i=1;
			//for(BaseEquipment2 equipment2 : qtEquipmentList){
			//	if(StringUtil.isNotEmpty(equipment2.getEq_name())){
			///		if(qtEquipmentStr.length()>0){
			//			qtEquipmentStr += "、";
			//		}
			//		qtEquipmentStr += equipment2.getEq_name();
			//	}
			//	if(i==3){
			//		break;
			//	}
			//	i++;
			//}
			//if(StringUtil.isNotEmpty(qtEquipmentStr)){
			//	rec.setValue("QTJYYQ", qtEquipmentStr+"等");	
			//}

			//额外的一些报表设置参数
			rec.setValue("TotalP", paraMap.get("TotalP"));
			//rec.setValue("JGHZH", "TS7110306-2019");
			mrds.addRow(rec);
			MREngine engine = new MREngine(pageContext);

			engine.setRootPath((String) paraMap.get("report_root_path"));
			engine.addReport((String) paraMap.get("report_file"));
			engine.addMRDataSet((String) paraMap.get("MRDataSet"), mrds);
			engine.bind();
		%>
		<%@ include file="/k/kui-base-form.jsp"%>
		<%@ include file="report.js.jsp"%>

		<script language="javascript">
	var com_name = "<%=com_name%>";
	var device_use_place = "<%=device_use_place%>";
	var security_op = "<%=security_op%>";
	var device_type = "<%=device_type%>";

	$(function() {
		 manager = $("#btn").ligerButton(
		            {
		                click: function ()
		                {
		                    
		                }
		            }
		            );
		
		var height = $(window).height()-$('.toolbar-tm').height();
		$("#scroll-tm").css({height:height});
		setReports();
		
		$('#save').click(function(){
			
			$("#save").attr("disabled","disabled");
			$("#sub").attr("disabled","disabled");

			
			doSave();
				
			var infoId = "${infoId}";
			
			var reportId ="${report_type}";
			
			var deviceId ="${device_id}";
			
			var flag = "${param.isSub}";
			
			
			
			if(flag=="no"){
				
				
				api.data.window.api.data.window.saveReport(infoId,reportId,deviceId);
				//api.data.window.api.data.window.submitAction();
				api.data.window.api.close();
				api.close();
			}else{
				api.data.window.saveReport(infoId,reportId,deviceId);
				api.close();
			}
		});
		
		$('#sub').click(function(){
			$("#sub").attr("disabled","disabled");
			$("#save").attr("disabled","disabled");
			doSave();
			
			var flag = "${param.isSub}";
			
			
			if(flag=="no"){
				
				api.data.window.api.data.window.submitAction();
				api.data.window.api.close();
				api.close();
			}else{
				api.data.window.submitAction();
				
				api.close();
			}
		});
		
		$('#cancel').click(function(){
			
			var flag = "${param.isSub}";
			if(flag=="no"){
				api.data.window.api.close();
				api.close();
			}else{
				api.close();
				
			}
		});
	})
	
	//保存开始禁用按钮
	function disableButton(){
		//$.ligerDialog.alert(111);
	}
	//保存结束按钮可用
	function enableButton(){
		//$.ligerDialog.alert(222);
	}
</script>
	</head>
	<body>
		<div id="scroll-tm" style="overflow: hidden">
			<ming:MRViewer id="MRViewer" shownow="true" width="100%"
				height="100%" simple="false" canedit="true"
				invisiblebuttons="Close,PrintPopup,ExportPopup,Find,FindNext"
				postbackurl="" wrapparams="true" />
		</div>
		<div class="toolbar-tm">
			<div class="toolbar-tm-bottom" style="text-align: center;">
				<a id="sub" class="l-button-warp l-button" ligeruiid="Button1012">
					<span class="l-button-main l-button-hasicon"> <span
						class="l-button-text">保存并关闭</span> <span
						class="l-button-icon l-icon-search"></span> </span> </a>
				<a id="save" class="l-button-warp l-button" ligeruiid="Button1012">
					<span class="l-button-main l-button-hasicon"> <span
						class="l-button-text">保存</span> <span
						class="l-button-icon l-icon-save"></span> </span> </a>

				<a id="cancel" class="l-button-warp l-button" ligeruiid="Button1012">
					<span class="l-button-main l-button-hasicon"> <span
						class="l-button-text">关闭</span> <span
						class="l-button-icon l-icon-cancel"></span> </span> </a>
			</div>
		</div>
	</body>
</html>