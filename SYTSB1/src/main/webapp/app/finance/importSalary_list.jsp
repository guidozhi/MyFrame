<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
<%@include file="/k/kui-base-list.jsp"%>
<script type="text/javascript" src="pub/bpm/js/util.js"></script>
<script type="text/javascript" src="pub/fileupload/js/fileupload.js"></script>
<script type="">
$(document).ready(function() { 
var yzm=<%=request.getParameter("yzm")%> 

<%
String ss="";

if(request.getSession().getAttribute("edcrfv")!=null){
	ss=request.getSession().getAttribute("edcrfv").toString();}%>

var ss="";
<sec:authorize access="hasRole('TJY2_CW_GZ')">
		var ss=<%=ss%>+"";
    	</sec:authorize>

if(ss!="9999"){
	var pathName=window.document.location.pathname;  
    			var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1); 
 return window.location.href=projectName+"/finance/messageCheck_detail.jsp"; 
}

}); 
</script>
<script type="text/javascript">
    var khFileUploader;
	var qmUserConfig = {
		sp_fields : [
		             
		   
		],
		tbar:[{ text: 'EXCEL 数据导入', id: 'import1', icon: 'excel-import', click: impdb }
		      ,"-",
              {text: '详情', id: 'detail', icon: 'detail', click: detail}
		     //  ,"-",
		    //  {text: '新增', id: 'add', icon: 'add', click: add}
		 //      ,"-",
         //     {text: '修改', id: 'edit', icon: 'edit', click: edit}
               ,"-",
              {text: '删除', id: 'del', icon: 'del', click: del}
            ],
		listeners: {
			rowClick: function(rowData, rowIndex) {
			},
			rowDblClick: function(rowData, rowIndex) {
				detail();
			},	
			selectionChange: function(rowData, rowIndex) {
				var count = Qm.getSelectedCount();
				Qm.setTbar({
					detail: count==1,
					edit: count==1,
					del: count>0
				});
			}
		}
	};
	

// 上传完成，开始保存汇编数据

function impdb() {
			top.$.dialog({
				width: 450,
				height:250,
				lock: true,
				parent: api,
				data: {
					window: window
				},
				title: "EXCEL数据导入",
				content: "url:app/finance/import.jsp?cfg=test_excel_db_import&pageStatus=add"
			});
		}
function impcallback(){
	Qm.refreshGrid();
}

function expdb(){
	location.href=$("base").attr("href") + "pub/expimp/export.do?config=test_excel_db_import";
}
function expbean(){
	location.href=$("base").attr("href") + "pub/expimp/export.do?config=exp_imp_demo";
}
function expbus(){
	location.href=$("base").attr("href") + "pub/expimp/export.do?config=exp_imp_demo";
}
function expMap(){
	location.href=$("base").attr("href") + "pub/expimp/export.do?config=exp_imp_demo";
}
		
		
		
function add() {
	top.$.dialog({
		width: 900,
		height: 600,
		lock: true,
		parent: api,
		data: {
			window: window
		},
		title: "新增",
		content: 'url:app/finance/importSalary_detail.jsp?pageStatus=add'
	});
}
	function edit() {
		var id = Qm.getValueByName("id");
		top.$.dialog({
			width: 900,
			height: 600,
			lock: true,
			parent: api,
			data: {
				window: window
			},
			title: "修改",
			content: 'url:app/finance/salary_detail.jsp?id=' + id + '&pageStatus=modify'
		});
	}
	function detail() {
		var id = Qm.getValueByName("id");
		top.$.dialog({
			width: 1000,
			height: 600,
			lock: true,
			parent: api,
			data: {
				window: window
			},
			title: "工资查询",
			content: 'url:app/finance/salary_list.jsp?id=' + id + '&pageStatus=detail'
		});
	}
	function del() {
		$.del(kFrameConfig.DEL_MSG, "finance/importSalaryAction/delete.do", {
			"ids": Qm.getValuesByName("id").toString()
		});
	}
	
</script>
</head>
<body>
	

	<qm:qm pageid="TJY2_IMPORT_SALARY" script="false" singleSelect="false">
	
	</qm:qm>
</body>
</html>