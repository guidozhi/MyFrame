<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
<%@include file="/k/kui-base-list.jsp"%>
<%@page import="com.khnt.security.util.SecurityUtil"%>
<%@page import="com.khnt.security.CurrentSessionUser"%>
<%@page import="com.khnt.rbac.impl.bean.User"%>
<script type="text/javascript" src="pub/bpm/js/util.js"></script>
<script type="text/javascript" src="pub/fileupload/js/fileupload.js"></script>
<%
CurrentSessionUser curUser = SecurityUtil.getSecurityUser();
User uu = (User)curUser.getSysUser();
com.khnt.rbac.impl.bean.Employee e = (com.khnt.rbac.impl.bean.Employee)uu.getEmployee();
e.getId();
String userId=e.getId();
	String uId = SecurityUtil.getSecurityUser().getId();
	%>
<script type="text/javascript">
	var qmUserConfig={
			sp_defaults:{columnWidth:0.3,labelAlign:'right',labelSeparator:'',labelWidth:120},	// 默认值，可自定义
			sp_fields:[
			           {name:"status",compare:"like"},
			           {name:"main_dep",compare: "like"},
			           {name:"main_lead_name",compare: "like"},
						{name:"main_duty_name",compare: "like"},
			            {group:[
			                    
			            	{name:"start_time",compare:">=",width:"100"},
			            	{label:"到",name:"start_time",compare:"<=",labelAlign: "center", labelWidth:20,width:100}
			            ]},
			            {group:[
			                    
				            	{name:"finish_time",compare:">="},
				            	{label:"到",name:"finish_time",compare:"<=",labelAlign: "center", labelWidth:20}
				            ]}
					   
					   
			],
			tbar:[
			      {text: '详情', id: 'detail', icon: 'detail', click: detail}
			      <sec:authorize access="hasRole('TJY2_OFFICE_IMP')">
			      ,"-",{text: '新增', id: 'add', icon: 'add', click: add }, 
			       "-", {text: '修改', id: 'edit', icon: 'modify', click:edit}, 
			      "-",{text: '删除', id: 'del', icon: 'delete', click:del}, 
			      "-",{text: '下发', id: 'send', icon: 'issued', click:send}
			      </sec:authorize>  
			],
			listeners:{
				rowClick:function(rowData,rowIndex){
				},
				rowDblClick:function(rowDate,rowIndex){
					detail();
				},
				//下发按钮判断
				selectionChange: function(rowData, rowIndex) {
					var count = Qm.getSelectedCount();
					Qm.setTbar({
    					detail: count==1,
    					add: count==0,
    					edit: count>0,
    					del: count==1,
    					send:count==1
    				});
					if(count > 0){
						var up_status = Qm.getValuesByName("status").toString();
						var strs = new Array(); //定义一数组
		 					strs = up_status.split(","); //字符分割\
		 				var isUsd=false;
		 				var isStart=false;
		 				var isConduct=false; 
						var Unfinished=false;
						var Complete=false;
						for(var i=0;i<count;i++){
							if(strs[i]=="未下发"){
								isUsd = true;
							}if(strs[i]=="未开始"){
								isStart = true;
							}if(strs[i]=="进行中"){
								isConduct = true;
							}if(strs[i]=="未完成"){
								Unfinished = true;
							}if(strs[i]=="已完成"){
								Complete=true;
							}
						}
						if(isUsd)
							Qm.setTbar({detail: true,add : true,edit : true,del:true,send:true});
						if(isStart)
							Qm.setTbar({detail: true,add : true,edit : true,del:true,send:false});
						if(Unfinished||Complete||isConduct)
							Qm.setTbar({detail: true,add : true,edit : false,del:false,send:false});
					}
				}
		},
		rowAttrRender : function(rowData, rowid) {
            var fontColor="black";
            if (rowData.status=='已完成'){
                fontColor="green";
            }else if(rowData.status=='未完成') {
                fontColor="red";
            }else if(rowData.status=='未开始') {
                fontColor="orange";
            }else if(rowData.status=='进行中'){
            	fontColor="blue";
            }
            return "style='color:"+fontColor+"'";
        }
		
	};

	function detail() {
		var id = Qm.getValueByName("id");
	
			top.$.dialog({
				width: 900,
				height: 400,
				lock: true,
				data: {
					window: window
				},
				title: "详情",
				content: 'url:app/office/office_fb_datali1.jsp?pageStatus=detail&id='+id
			});
		}
		
	
	
	
	function add(){
		top.$.dialog({
			width:900,
			height:500,
			lock:true,
			data: {window: window},
			title:"新增",
			content:'url:app/office/office_weightyTask_datail.jsp?pageStatus=add'
		});
	}

	function edit(){
		var id = Qm.getValueByName("id");
		top.$.dialog({
			width:900,
			height:500,
			lock:true,
			data: {window: window},
			title:"修改",
			content:'url:app/office/office_weightyTask_datail.jsp?id=' + id + '&pageStatus=modify'
		});
	}

	function del() {
		$.del(kFrameConfig.DEL_MSG, "weighty/Task/delete.do", {
			"ids": Qm.getValuesByName("id").toString()
		});
	}
	
	function send(){
	    var id = Qm.getValueByName("id");
	    $.ligerDialog.confirm('是否提交下发？', function (yes){
	        if(!yes){return false;}
	        top.$.ajax({
	                     url: "weighty/Task/taskSend.do?id="+id,
	                     type: "GET",
	                     dataType:'json',
	                     async: false,
	                     success:function (data) {
	                        if(data.success){
	                            $.ligerDialog.success("下发成功！");
	                            Qm.refreshGrid();//刷新
	                        }else{
	                            $.ligerDialog.warn(data.msg);
	                        }
	                     },
	                     error:function () {
	                         $.ligerDialog.warn("下发失败！");
	                     }
	                 });
	    });
	}		
	
    $(function(){
        importData();
    });
function importData() {
//创建上传实例
khFileUploader = new KHFileUploader({
    fileSize: "10mb",//文件大小限制
    buttonId: "importData",//上传按钮ID
    container: "filecontainer3",//上传控件容器ID
    title: "银行转账数据",//文件选择框提示
    saveDB : false,
    extName: "xls,xlsx",//文件扩展名限制
    fileNum: 1,//限制上传文件数目
    callback : function(files){
                //文件无误，执行保存
                saveData(files);
    }
});
}

//上传完成，开始保存汇编数据
function saveData(files){
$("body").mask("正在保存...");
$.post("weighty/Task/saveTask.do",{files:$.ligerui.toJSON(files)},function (data) {
        $("body").unmask();
        if (data.success) {
        	 $.ligerDialog.success("成功导入");
            Qm.refreshGrid();
        } else {
            $.ligerDialog.error("保存失败！<br/>");
        }
   },"json");
}
	
</script>


</head>
<body>
	<div class="item-tm" id="titleElement">
        <div class="l-page-title">
            <div class="l-page-title-note">提示：列表数据项
                <font color="black">“黑色”</font>代表未下发，
                <font color="green">“绿色”</font>代表已完成，
                <font color="orange">“橙色”</font>代表未开始，
                <font color="red">“红色”</font>代表未完成，
                <font color="blue">“蓝色”</font>代表进行中。
            </div>
        </div>
    </div>

<p id="filecontainer3" style="margin:5px"><a class="l-button-warp l-button" id="importData"><span class="l-button-main l-button-hasicon"><span class="l-button-text">导入重大任务数据</span><span class="l-button-icon iconfont l-icon-excel-import"></span></span></a></p>
	<qm:qm pageid="TJY2_BG_ZDRW" script="false" singleSelect="true">
	</qm:qm>
</body>
</html>