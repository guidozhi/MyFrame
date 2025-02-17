<%@page import="com.khnt.utils.StringUtil"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@page import="com.khnt.security.util.SecurityUtil"%>
<%@page import="com.khnt.security.CurrentSessionUser"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<base href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/" />
<%@include file="/k/kui-base-form.jsp"%>
<%CurrentSessionUser user = SecurityUtil.getSecurityUser();
String user_name = user.getName(); %>
<style type="text/css">
    .l-icon-exportDoc{background:url('k/kui/skins/icons/word.gif') no-repeat center;}
    .l-icon-printPreview{background:url('k/kui/skins/icons/search2.gif') no-repeat center;}
    .l-icon-fullScreen{background:url('k/kui/skins/icons/div-drag.gif') no-repeat center;}
</style>
<script type="text/javascript" src="app/common/lodop/LodopFuncs.js"></script>
<script type="text/javascript">
	$(function(){
		// 获取运维日志详情
		$.get("maintenance/info/getPrintData.do?info_id=${param.info_id}",function(res){
			if(res.success){
				if(res.task_attach_id != "" && res.task_attach_id != null){
					// 获取功能说明附件ID
					$("#pic1").attr("src",$("base").attr("href")+"maintenance/info/getAttachment.do?id="+res.task_attach_id);
				}
				if(res.update_attach_id != "" && res.update_attach_id != null){
					// 获取更新说明附件ID
					$("#pic2").attr("src",$("base").attr("href")+"maintenance/info/getAttachment.do?id="+res.update_attach_id);
				}
				
				LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM')); 
				LODOP.SET_PRINT_PAGESIZE(0,"20.5cm","20.25cm","");
				LODOP.ADD_PRINT_TEXT("1.89cm","2.76cm",160,23,getDeviceType(res.device_sort_code));
				LODOP.SET_PRINT_STYLEA(0,"ReadOnly",2);
				LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
				LODOP.SET_PRINT_STYLEA(0,"Bold",1);

					LODOP.ADD_PRINT_TEXT("1.89cm","10.26cm",160,23,res.device_sort);
					 LODOP.SET_PRINT_STYLEA(0,"ReadOnly",2);
					 LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
					 LODOP.SET_PRINT_STYLEA(0,"Bold",1);
					 if(res.company_name!=null&&res.company_name.length>28){
							 LODOP.ADD_PRINT_TEXT("2.42cm","2.76cm",450,23,res.company_name);
							 LODOP.SET_PRINT_STYLEA(0,"LineSpacing",-7);
						}else{
							LODOP.ADD_PRINT_TEXT("2.77cm","2.76cm",450,23,res.company_name);
						}
					 LODOP.SET_PRINT_STYLEA(0,"ReadOnly",2);
					 LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
					 LODOP.SET_PRINT_STYLEA(0,"Bold",1);
					
					LODOP.ADD_PRINT_TEXT("3.70cm","2.76cm",160,23,res.internal_num);
					 LODOP.SET_PRINT_STYLEA(0,"ReadOnly",2);
					 LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
					 LODOP.SET_PRINT_STYLEA(0,"Bold",1);
					 
					if(res.device_code!=null&&res.device_code!="undefined"&&res.device_code.length>16){
						 LODOP.ADD_PRINT_TEXT("3.34cm","10.30cm",160,23,res.device_code);
						 LODOP.SET_PRINT_STYLEA(0,"LineSpacing",-7); 
					}else{
						LODOP.ADD_PRINT_TEXT("3.70cm","10.30cm",160,23,res.device_code);
					}
					 LODOP.SET_PRINT_STYLEA(0,"ReadOnly",2);
					 LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
					 LODOP.SET_PRINT_STYLEA(0,"Bold",1);
					
					LODOP.ADD_PRINT_TEXT("4.50cm","2.76cm",450,23,res.registration_agencies);
					 LODOP.SET_PRINT_STYLEA(0,"ReadOnly",2);
					 LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
					 LODOP.SET_PRINT_STYLEA(0,"Bold",1);
					
					LODOP.ADD_PRINT_TEXT("5.34 cm","2.76cm",450,23,"四川省特种设备检验研究院");
					 LODOP.SET_PRINT_STYLEA(0,"ReadOnly",2);
					 LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
					 LODOP.SET_PRINT_STYLEA(0,"Bold",1);

					 if(res.registration_num!=null&&res.registration_num!="undefined"&&res.registration_num.length>16){
						 LODOP.ADD_PRINT_TEXT("5.87cm","2.76cm",160,23,res.registration_num);
						 LODOP.SET_PRINT_STYLEA(0,"LineSpacing",-7); 
					}else{
						LODOP.ADD_PRINT_TEXT("6.23cm","2.76cm",160,23,res.registration_num);
					}
						 LODOP.SET_PRINT_STYLEA(0,"ReadOnly",2);
						 LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
					 	 LODOP.SET_PRINT_STYLEA(0,"Bold",1);
					
					LODOP.ADD_PRINT_TEXT("6.23cm","10.26cm",160,23,res.inspect_next_date);
					 LODOP.SET_PRINT_STYLEA(0,"ReadOnly",2);
					 LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
					 LODOP.SET_PRINT_STYLEA(0,"Bold",1);
					if(res.maintain_unit_name!=null&&res.maintain_unit_name.length>10){
						LODOP.ADD_PRINT_TEXT("6.80cm","2.76cm",160,16,res.maintain_unit_name);
						LODOP.SET_PRINT_STYLEA(0,"LineSpacing",-7);  
					}else{
						LODOP.ADD_PRINT_TEXT("7.09cm","2.76cm",160,23,res.maintain_unit_name);
					}
					 LODOP.SET_PRINT_STYLEA(0,"Bold",1);
					 
					LODOP.ADD_PRINT_TEXT("7.02cm","10.26cm",160,23,res.security_tel)
					 LODOP.SET_PRINT_STYLEA(0,"ReadOnly",2);
					 LODOP.SET_PRINT_STYLEA(0,"FontSize",11);
					 LODOP.SET_PRINT_STYLEA(0,"Bold",1);
					 
					
					 /* LODOP.ADD_PRINT_HTM("7.10cm","12.09cm","3.0cm","3.0cm",
							 document.getElementById("div1").innerHTML);  */
					 LODOP.ADD_PRINT_HTM("7.37cm","12.06cm","2.5cm","BottomMargin:1mm",
							 document.getElementById("div1").innerHTML); 
					LODOP.SET_PRINT_STYLEA(0,"Stretch",1);//(可变形)扩展缩放模式 
					//LODOP.ADD_PRINT_BARCODE("5.0cm","10.5cm","2.6cm","2.6cm","QRCode","http://111.9.177.232//baseDeviceDocumentAction/getMobileInfo.do?id=${param.id}");
				  
				  	//LODOP.PREVIEW();
				  	LODOP.ADD_PRINT_SETUP_BKIMG("<img border='0' src='"+$("base").attr("href")+"/app/device/usesign/img/elevator.jpg'>");
					LODOP.SET_SHOW_MODE("BKIMG_LEFT",1);
					LODOP.SET_SHOW_MODE("BKIMG_TOP",1);
					LODOP.SET_SHOW_MODE("BKIMG_WIDTH","180mm");
					//LODOP.SET_SHOW_MODE("BKIMG_HEIGHT","99mm"); //这句可不加，因宽高比例固定按原图的
					LODOP.SET_SHOW_MODE("BKIMG_IN_PREVIEW",1);	
					LODOP.SET_PREVIEW_WINDOW(0,0,0,1000,800,"");
				 	// LODOP.PRINT_DESIGN();
				 	LODOP.PRINT();
					 //LODOP.PREVIEW();
				 	$.get("report/query/savePrintTagsRecord.do?id=${param.inspection_info_id}&report_id=${param.report_id}&device_id=${param.device_id}",function(res){
				 		})
				 	api.close();
			}
		})
	});
		
	function getDeviceType(device_sort){
		var device_type  = "";
		var d = device_sort.substring(0,1);
		var dd = device_sort.substring(0,2);
		if(d=="1"){
			device_type  = "锅炉";
		}else if(d=="2"){
			device_type  = "压力容器";
		}else if(d=="3"){
			device_type  = "电梯";
		}else if(d=="4"){
			device_type  = "起重机械";
		}else if(d=="5"){
			device_type  = "场（厂）内专用机动车辆";
		}else if(d=="6"){
			device_type  = "大型游乐设施";
		}else if(d=="7"){
			device_type  = "压力管道元件";
		}else if(d=="8"){
			device_type  = "压力管道";
		}else if(d=="9"){
			device_type  = "客运索道";
		}
		return device_type;
	}
	
	function getDeviceName(device_sort){
		var device_name  = "";
		var d = device_sort.substring(0,1);
		var dd = device_sort.substring(0,2);
		if(dd=="11"){
			device_name  = "承压蒸汽锅炉";
		}else if(dd=="12"){
			device_name  = "承压热水锅炉";
		}else if(dd=="13"){
			device_name  = "有机热载体锅炉";
		}else if(dd=="21"){
			device_name  = "固定式压力容器";
		}else if(dd=="22"){
			device_name  = "移动式压力容器";
		}else if(dd=="23"){
			device_name  = "气瓶";
		}else if(dd=="24"){
			device_name  = "氧舱";
		}else if(dd=="25"){
			device_name  = "其它";
		}else if(dd=="31"){
			device_name  = "乘客电梯";
		}else if(dd=="32"){
			device_name  = "载货电梯";
		}else if(dd=="33"){
			device_name  = "液压电梯";
		}else if(dd=="34"){
			device_name  = "杂物电梯";
		}else if(dd=="35"){
			device_name  = "自动扶梯";
		}else if(dd=="36"){
			device_name  = "自动人行道";
		}else if(dd=="41"){
			device_name  = "桥式起重机";
		}else if(dd=="42"){
			device_name  = "门式起重机";
		}else if(dd=="43"){
			device_name  = "塔式起重机";
		}else if(dd=="44"){
			device_name  = "流动式起重机";
		}else if(dd=="47"){
			device_name  = "门座起重机";
		}else if(dd=="48"){
			device_name  = "升降机";
		}else if(dd=="49"){
			device_name  = "缆索起重机";
		}else if(dd=="4A"){
			device_name  = "桅杆起重机";
		}else if(dd=="4D"){
			device_name  = "机械式停车设备";
		}else if(dd=="51"){
			device_name  = "挖掘机";
		}else if(dd=="52"){
			device_name  = "装载机";
		}else if(dd=="53"){
			device_name  = "铲运机";
		}else if(dd=="54"){
			device_name  = "固定平台搬运车";
		}else if(dd=="55"){
			device_name  = "牵引车";
		}else if(dd=="56"){
			device_name  = "推顶车";
		}else if(dd=="57"){
			device_name  = "场内汽车";
		}else if(dd=="58"){
			device_name  = "场内拖拉机";
		}else if(dd=="61"){
			device_name  = "观览车类";
		}else if(dd=="62"){
			device_name  = "滑行车类";
		}else if(dd=="63"){
			device_name  = "架空游览车类";
		}else if(dd=="64"){
			device_name  = "陀螺类";
		}else if(dd=="66"){
			device_name  = "转马类";
		}else if(dd=="67"){
			device_name  = "自控飞机类";
		}else if(dd=="68"){
			device_name  = "赛车类";
		}else if(dd=="69"){
			device_name  = "小火车类";
		}else if(dd=="6A"){
			device_name  = "碰碰车类";
		}else if(dd=="6B"){
			device_name  = "电池车类";
		}else if(dd=="6C"){
			device_name  = "观光车类";
		}else if(dd=="6D"){
			device_name  = "水上游乐设施";
		}else if(dd=="6E"){
			device_name  = "无动力游乐设施";
		}else if(dd=="71"){
			device_name  = "压力管道管子";
		}else if(dd=="72"){
			device_name  = "流压力管道管件";
		}else if(dd=="73"){
			device_name  = "阀门";
		}else if(dd=="74"){
			device_name  = "法兰";
		}else if(dd=="75"){
			device_name  = "补偿器";
		}else if(dd=="76"){
			device_name  = "压力管道支承件";
		}else if(dd=="77"){
			device_name  = "压力管道密封元件";
		}else if(dd=="7T"){
			device_name  = "压力管道特种元件";
		}else if(dd=="81"){
			device_name  = "长输（油气）管道";
		}else if(dd=="82"){
			device_name  = "公用管道";
		}else if(dd=="83"){
			device_name  = "工业管道";
		}else if(dd=="91"){
			device_name  = "客运架空索道";
		}else if(dd=="92"){
			device_name  = "客运缆车";
		}else if(dd=="93"){
			device_name  = "客运拖牵索道";
		}
		return device_name;
	}	
</script>
</head>
<body>
	<div id="div1" style="display: none;">
		<img style="width: 800mm;height: 600mm;" src="" alt="" id="pic1" />
	</div>
	<div id="div2" style="display: none;">
		<img style="width: 800mm;height: 600mm;" src="" alt="" id="pic2" />
	</div>
</body>
</html>