<%@ page contentType="text/html;charset=UTF-8"%>
<%@page import="com.khnt.utils.StringUtil"%>
<%@ taglib uri="http://khnt.com/tags/ui" prefix="u" %>
<%@page import="com.khnt.security.util.SecurityUtil"%>
<%@page import="com.khnt.security.CurrentSessionUser"%>
<%@page import="com.khnt.rbac.impl.bean.User"%>
<%

CurrentSessionUser usee = SecurityUtil.getSecurityUser();
User uu = (User)usee.getSysUser();
com.khnt.rbac.impl.bean.Employee e = (com.khnt.rbac.impl.bean.Employee)uu.getEmployee();
e.getId();
%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>业务信息查询</title>
		<%@include file="/k/kui-base-list.jsp"%>
		<script type="text/javascript" src="pub/bpm/js/util.js"></script>
		<script type="text/javascript" src="pub/worktask/js/worktask.js"></script>
		<script language="javascript" src="app/flow/report/report.js"></script>
<%
	CurrentSessionUser user = SecurityUtil.getSecurityUser();
	String userId = user.getId();
	String user_account = user.getSysUser().getAccount();
	String userType = ((com.khnt.rbac.impl.bean.Org)(user.getDepartment())).getProperty();

	String org_code = user.getDepartment().getOrgCode();
	String isDt = "";
%>
		<script type="text/javascript">
		var w = window.screen.availWidth;
		var h = window.screen.availHeight;
		var deviceType=<u:dict sql="select t.value id,t.name text from PUB_CODE_TABLE_VALUES t,pub_code_table t1 where t.code_table_id=t1.id and   t1.code = 'device_classify' "/>
		var areas = <u:dict sql="select id,regional_name text from V_AREA_CODE where ID in ('510104','510106','510109','510122','510189')"></u:dict>;
		var check_types = <u:dict code="BASE_CHECK_TYPE"></u:dict>;
		var check_deps = <u:dict sql="select id code, ORG_NAME text from SYS_ORG where ORG_CODE like 'jd%' or ORG_CODE like 'cy%' order by orders "></u:dict>;
		var reports = <u:dict sql='select id,report_name from base_reports'></u:dict>;
		var payments = <u:dict code="PAYMENT_STATUS"></u:dict>;
		
        var qmUserConfig = {
        	sp_defaults:{columnWidth:0.3,labelAlign:'right',labelSeparator:'',labelWidth:120},	// 默认值，可自定义
            sp_fields:[
                {name:"report_sn",compare:"like",value:""},
                {name:"report_com_name",compare:"like",value:""},
            	{name:"check_unit_id", id:"check_unit_id", compare:"like"},
            	{name:"enter_op_name", id:"enter_op_name", compare:"like"},
            	{name:"flow_note_name", id:"flow_note_name", compare:"="},
                {name:"isExpired", id:"isExpired", compare:"="}
            ],
          tbar: [{ text:'详情', id:'detail',icon:'detail', click:detail },
                 '-', 
                 { text:'查看报告', id:'showReport',icon:'detail', click: showReport},
                 "-",
                 { text:'流转过程', id:'turnHistory',icon:'follow', click: turnHistory},
                 "-",
                 { text:'签发过期', id:'signExpired',icon:'info-del', click: signExpired},
                 '-', 
  				 {text : '清空', id : 'empty', icon : 'modify', click : empty}    
                 ],
          listeners: {
                rowDblClick :function(rowData,rowIndex){
                    detail();
                    },
                selectionChange :function(rowData,rowIndex){
                    var count=Qm.getSelectedCount();
                    if(count==1){
                            Qm.setTbar({detail:true});
                    }else{
                        Qm.setTbar({detail:false});
                    }
                },
                rowAttrRender: function(rowData, rowid) {
                	$("#day").text(rowData.nk_days);
                	if(rowData.flow_note_name=="报告录入" || 
                			rowData.flow_note_name=="报告送审" ||
                			rowData.flow_note_name=="报告审核" || 
                			rowData.flow_note_name=="报告审批" ||
                			rowData.flow_note_name=="报告签发"){
	                    		if(rowData.nk_days==""){
		                   			
	                 			}else if(rowData.isExpired=="已超期"){
	                         		return "style='color: red'";
		                     }
                     }
                }
            }
        };
        
        function cs(){
			var id = Qm.getValueByName("inspection_info_id").toString();//报告的id
			var day =$("#day").html();//翻红时限
			var lrsj = "QCDQF";//录入时间
			$.ajax({
                url: "report/yjsz/reportYj.do?id="+id,
				type : "post",
				async : false,
				success : function(data) {
					if (data.success) {
						top.$.notice("成功！");
						Qm.refreshGrid();
					} else {
						$.ligerDialog.alert("失败！" + data.msg);
					}
				}
			});
        	
			
        	
        }
	
		// 详情
		function detail(id){
			if($.type(id)!="string"){
				id = Qm.getValueByName("inspection_info_id").toString();
			}		
			top.$.dialog({
				width : 800,
				height : 500,
				lock : true,
				title : "业务详情",
				content : 'url:app/flow/info_detail.jsp?status=detail&id='+ id,
				data : {
					"window" : window
				}
			});
		}
		
		// 流转过程
		function turnHistory(){	
			top.$.dialog({
	   			width : 400, 
	   			height : 700, 
	   			lock : true, 
	   			title: "流程卡",
	   			content: 'url:department/basic/getFlowStep.do?ins_info_id='+Qm.getValueByName("inspection_info_id"),
	   			data : {"window" : window}
	   		});
		}
		
		// 签发过期
		function signExpired(){	
			$("#qm-search-p input").each(function(){
				$(this).val("");
			})
			$("input[name='check_unit_id-txt']").ligerComboBox().selectValue('');	// 检验部门
			$("input[name='flow_note_name-txt']").ligerComboBox().selectValue('报告签发');	// 流程步骤
			$("input[name='isExpired-txt']").ligerComboBox().selectValue('1');
			Qm.searchData();
    		Qm.setCondition([]);
		}
		function empty(){
			$("#qm-search-p input").each(function(){
				$(this).val("");
			})
			$("input[name='check_unit_id-txt']").ligerComboBox().selectValue('');	// 检验部门
			$("input[name='flow_note_name-txt']").ligerComboBox().selectValue('');	// 流程步骤
			$("input[name='isExpired-txt']").ligerComboBox().selectValue('');
		}
		// 查看报告
		function showReport(){	
			var id = Qm.getValueByName("inspection_info_id").toString();
			var report_id = Qm.getValueByName("report_id").toString();	// 报告类型
			var is_user_defined = Qm.getValueByName("is_user_defined").toString();	// 自定义报告
			if(is_user_defined==""){
				var url = "report/query/showReport.do?id="+id+"&report_id="+report_id;
				top.$.dialog({
					width : w, 
					height : h, 
					lock : true, 
					title:"报告信息",
					content: 'url:'+url,
					data : {"window" : window}
				}).max();
				//var fileValue = window.showModalDialog(url,[],"dialogwidth:"+w+";dialogheight:"+h+";help=no;status=no;center=yes;edge=sunken;resizable=yes");
			} else {
				var file_name = Qm.getValueByName("file_name").toString();	// 自定义报告文件名
				//获取自定义报告的文件名
				var urls = "app/query/getInfoFileName.jsp?id="+id+"&file_name="+file_name;
				temp = SendXML(urls);
				if(temp!="false"){
					var url = "/pub/fileupload/down_file.jsp?fileName=" + temp;
					WinOpen(1,url,"",340,600);
				} else {
					$.ligerDialog.alert("操作异常，请重新操作。");
				}
			}
		}
    </script>
	</head>
	<body>
	<div class="item-tm" id="titleElement">
    <div class="l-page-title">
        <div class="l-page-title-note">提示：列表数据项
            <font color="red">“红色”</font>代表——起草到签发超期，翻红时限为：<span id="day"></span>天
        </div>
    </div>
    </div>
		<qm:qm pageid="TJY2_REPORT_NK" singleSelect="true">
			<%
				if("shuangliu".equals(user_account)){
				%>
				<qm:param name="device_area_code" value="510122" compare="=" />
				<%	
			}else if("jinjiang".equals(user_account)){
				%>
				<qm:param name="device_area_code" value="510104" compare="=" />
				<%	
			}else if("gaoxin".equals(user_account)){
				%>
				<qm:param name="device_area_code" value="510109" compare="=" />
				<%	
			}else if("jinniu".equals(user_account)){
				%>
				<qm:param name="device_area_code" value="510106" compare="=" />
				<%	
			}
			if("AREA_TFXQ".equals(org_code)){
				%>
				<qm:param name="device_area_code" value="510189" compare="=" />
				<%	
			}
				if(org_code.contains("jd")){
					if("jd5".equals(org_code)){
						isDt = "";
						%>
						<qm:param name="check_unit_id" value="100024" compare="=" />
						<%
					}else{
						isDt = "dt";
						%>
						<qm:param name="check_unit_id" value="100020" compare="=" logic="or"/>
						<qm:param name="check_unit_id" value="100021" compare="=" logic="or"/>
						<qm:param name="check_unit_id" value="100022" compare="=" logic="or"/>
						<qm:param name="check_unit_id" value="100023" compare="=" logic="or"/>
						<%
					}
				}else if(org_code.contains("cy")){
					isDt = "";
					%>
					<qm:param name="check_unit_id" value="100034" compare="=" logic="or"/>
					<qm:param name="check_unit_id" value="100035" compare="=" logic="or"/>
					<qm:param name="check_unit_id" value="100036" compare="=" logic="or"/>
					<qm:param name="check_unit_id" value="100037" compare="=" logic="or"/>
					<qm:param name="check_unit_id" value="100033" compare="=" logic="or"/>
					<%
				}
			%>
		</qm:qm>
		<script type="text/javascript">
//    根据 sql或码表名称获得Json格式数据
<%--var aa=<u:dict code="community_type"></u:dict>;--%>
Qm.config.columnsInfo.flow_note_name.binddata = [
	{id: '报告录入', text: '报告录入'},
	{id: '报告送审', text: '报告送审'},
	{id: '报告审核', text: '报告审核'},
	{id: '报告签发', text: '报告签发'},
	{id: '打印报告', text: '打印报告'},
	{id: '报告领取', text: '报告领取'},
	{id: '报告归档', text: '报告归档'}

]
</script>
	</body>
</html>
