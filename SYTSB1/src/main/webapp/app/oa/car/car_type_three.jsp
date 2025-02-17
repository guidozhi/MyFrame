<%@page import="com.khnt.security.util.SecurityUtil"%>
<%@page import="com.khnt.security.CurrentSessionUser"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%
	CurrentSessionUser user = SecurityUtil.getSecurityUser();
	String orgId = user.getUnit().getId();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<%@ include file="/k/kui-base-list.jsp"%>
<script type="text/javascript">
	var qmUserConfig = {
		sp_defaults:{columnWidth:0.33,labelAlign:'right',labelSeparator:'',labelWidth:100},//可以自己定义 layout:column,float,
		sp_fields : [
 			{name : "car_brand", compare : "like"},
 			{name : "state", compare : "="},
 			{name : "repairType", compare : "="},
 			{name : "load_number", compare : ">="},
 			{name : "car_num", compare : "like"},
 			{group:[
 			           {name:"buy_date",compare:">=",value:""},
 			           {label:"到",name:"buy_date",compare:"<=",value:"",labelAlign:"center",labelWidth:20}
 			           ]
 			}
 		],
 		tbar : [
			{text : '维修', id : 'repairCar', icon : 'setting', click : repairCar}
		],
		listeners: {
			rowAttrRender: function (rowData, rowid) {
                if(rowData.CAR_STATE=='用车中' || isLimit(rowData.car_num)==true) // 记录为红色
                {
                    return "style='color:red'";
                }
                if(rowData.CAR_STATE=='派车中') // 记录为红色
                {
                    return "style='color:orange'";
                }
            },
			selectionChange: function(rowData, rowIndex) {
				var count = Qm.getSelectedCount();
				Qm.setTbar({
					repairCar: count==1&&(Qm.getValuesByName("repairType")!="维保中"&&Qm.getValuesByName("repairType")!="维保申请中")
				});
			},
            onAfterShowData : function() {
				//initGridSelectRange();
			}
			<c:if test="${param.checkbox=='1'}">
				,
				onSelectRow: function(rowdata, rowindex, rowDomElement){
					var type = Qm.getValuesByName("CAR_STATE").toString();
					var car_num = Qm.getValuesByName("car_num").toString();
					if("${param.op_type}"=="00"){
						parent.parent.addOrRemoveUser(true,rowdata);
						var count = Qm.getSelectedCount();
						Qm.setTbar({
							repairCar: count==1&&(Qm.getValuesByName("repairType")!="维保中"&&Qm.getValuesByName("repairType")!="维保申请中")
						});
					}else{
						if(type.indexOf("派车中") >= 0 || type.indexOf("用车中") >= 0){
							$.ligerDialog.error("车辆"+type+"，请重新选择！");
						}else{
							var isLimit0 = false;
							//若是公务用车则判断是否限号，否则不判断是否限号
							if("${param.op_type}"=="0"){
								isLimit0 = isLimit(car_num);
							}else if("${param.op_type}"=="1"){
								if(rowdata.repair_type == "维保中" || rowdata.repair_type == "维保申请中" ){
									$.ligerDialog.error("车辆已在"+rowdata.repair_type+"，请重新选择！");
									return
								}else{
									isLimit0 = false;
								}
							}
							if(!isLimit0){
								parent.parent.addOrRemoveUser(true,rowdata);
								var count = Qm.getSelectedCount();
								Qm.setTbar({
									repairCar: count==1&&(Qm.getValuesByName("repairType")!="维保中"&&Qm.getValuesByName("repairType")!="维保申请中")
								});
							}else{
								$.ligerDialog.error("车辆"+car_num+"今日限号，请重新选择！");
							}
						}
					}
				},
	 			onUnSelectRow: function(rowdata, rowindex, rowDomElement){
	 				var type = Qm.getValuesByName("CAR_STATE").toString();
	 				var car_num = Qm.getValuesByName("car_num").toString();
					if("${param.op_type}"=="00"){
						parent.parent.addOrRemoveUser(false,rowdata);
		 				var count = Qm.getSelectedCount();
						Qm.setTbar({
							repairCar: count==1&&(Qm.getValuesByName("repairType")!="维保中"&&Qm.getValuesByName("repairType")!="维保申请中")
						});
					}else{
						if(type.indexOf("派车中") >= 0 || type.indexOf("用车中") >= 0){
							$.ligerDialog.error("车辆"+type+"，请重新选择！");
						}else{
							var isLimit0 = false;
							//若是公务用车则判断是否限号，否则不判断是否限号
							if("${param.op_type}"=="0"){
								isLimit0 = isLimit(car_num);
							}else if("${param.op_type}"=="1"){
								if(rowdata.repair_type == "维保中" || rowdata.repair_type == "维保申请中" ){
									$.ligerDialog.error("车辆已在"+rowdata.repair_type+"，请重新选择！");
									return
								}else{
									isLimit0 = false;
								}
							}
							if(!isLimit0){
								parent.parent.addOrRemoveUser(false,rowdata);
				 				var count = Qm.getSelectedCount();
								Qm.setTbar({
									repairCar: count==1&&(Qm.getValuesByName("repairType")!="维保中"&&Qm.getValuesByName("repairType")!="维保申请中")
								});
							}else{
								$.ligerDialog.error("车辆"+car_num+"今日限号，请重新选择！");
							}
						}
					}
	 			}
			</c:if>
			/* <c:if test="${param.checkbox=='1'}">
			onCheckRow : function(checked, rowdata, rowindex, rowDomElement){
				window.parent.addOrRemoveUser(checked,rowdata);
			},
			rowAttrRender:function(rowData,rowIndex){
				if(rowData.Status=="否"){
					  return 'style="color:#e6640d;"';
				}
			},
			onCheckAllRow:function(checked,row){
				var data = Qm.getQmgrid().getData();
				for(var i in data){
					window.parent.addOrRemoveUser(checked, data[i]);
				}
			}
			
			</c:if>
			<c:if test="${param.checkbox=='0'}">
			onSelectRow: function(rowdata, rowindex, rowDomElement){
				window.parent.addOrRemoveUser(true,rowdata);
			},
 			onUnSelectRow: function(rowdata, rowindex, rowDomElement){
 				window.parent.addOrRemoveUser(false,rowdata);
 			}
			</c:if> */
		}
	};
	
	function repairCar(){
		$.ligerDialog.confirm('是否维修！', function (yes) {
  			if(yes){
 				$.post('oa/car/info/repairCar.do?ids='+Qm.getValuesByName("id").toString(),function(res){
 					if(res){
 						top.$.notice("操作成功！",3);
 						Qm.refreshGrid();
 					}else{
 						$.ligerDialog.error("操作失败");
 					}
 				})
  			}
  		})
	}
	//判断车辆是否限号
	function isLimit(car_num){
		var reg = /^[0-9]+.?[0-9]*$/;
		var isLimit_temp = false;
		var week = new Date().getDay(); 
		for(var i=car_num.length-1;i>=0;i--){
			var num_temp = car_num.substr(i,1);
			if(reg.test(num_temp)){
				if(week == 0 || week == 6){
					isLimit_temp = false;
				} else if (week == 1) {  
			        if(num_temp==1 || num_temp==6 ){
			        	isLimit_temp = true;
			        }  
				} else if (week == 2) {  
				    if(num_temp==2 || num_temp==7 ){
				    	isLimit_temp = true;
				    }
				} else if (week == 3) {  
				    if(num_temp==3 || num_temp==8 ){
				    	isLimit_temp = true;
				    }
				} else if (week == 4) {  
				    if(num_temp==4 || num_temp==9 ){
				    	isLimit_temp = true;
				    }
				} else if (week == 5) {  
				    if(num_temp==5 || num_temp==0 ){
				    	isLimit_temp = true;
				    }
				}
				break;
			}
		}
		return isLimit_temp;
	}
</script>
</head>
<body>
	<div class="item-tm" id="titleElement">
	    <div class="l-page-title">
			<div class="l-page-title-note">提示：列表数据项
				<font color="red">“红色”</font>代表用车中或今日限号
				<font color="orange">“橙色”</font>代表派车中
			</div>
		</div>
	</div>
	<qm:qm pageid="oa_carinfo" script="false" singleSelect="true">
    	<qm:param name="type" value="3" compare="=" />
    	<c:if test="${param.op_type=='0'}">
    	<qm:param name="repairType" value="0" compare="=" />
    	</c:if>
    	<%-- <qm:param name="CAR_STATE" value="${param.status}" compare="like"/> --%>
	</qm:qm>		
</body>
</html>