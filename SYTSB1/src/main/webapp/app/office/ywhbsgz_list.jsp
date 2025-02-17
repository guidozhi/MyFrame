<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>银行转账数据列表</title>
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
    var khFileUploader;
	var qmUserConfig = {
			 sp_defaults:{columnWidth:0.33,labelWidth:60},
		sp_fields : [
{name:"status",compare:"like",value:""},
{name:"responsible_person",compare:"like",value:""},
{name:"department",compare:"like",value:""}
		],
		tbar:[
              {text: '详情', id: 'detail', icon: 'detail', click: detail}
              <sec:authorize access="hasRole('TJY2_OFFICE_WORK')">
			      ,"-",{text: '新增', id: 'add', icon: 'add', click: add}
			       ,"-",{text: '修改', id: 'modify', icon: 'modify', click: modify}
	               ,"-",{text: '删除', id: 'del', icon: 'del', click: del}
	               ,"-",{text: '下发', id: 'submit', icon: 'issued', click: addxf}
               </sec:authorize> 
            ],
		listeners: {
			rowClick: function(rowData, rowIndex) {
			},
			rowDblClick: function(rowData, rowIndex) {
				detail();
			},	
			//下发按钮判断
			selectionChange: function(rowData, rowIndex) {
				var count = Qm.getSelectedCount();
				var up_status = Qm.getValueByName("status");
				if("未下发"==up_status){

					Qm.setTbar({
						detail: count==1,
						modify: count==1,
						del: count==1,
						submit:count==1
					});}else if("进行中"==up_status){
						Qm.setTbar({
							detail: count==1,
							modify: count<0,
							del: count<0,
							submit:count<0
						});
						
					} else if("未开始"==up_status){
						Qm.setTbar({
							detail: count==1,
							modify: count==1,
							del: count==1,
							submit:count<0
						});
						
					}else if("未完成"==up_status){
						Qm.setTbar({
							detail: count==1,
							modify: count==1,
							del: count==1,
							submit:count==1
						});
						
					}else if("已完成"==up_status){
						Qm.setTbar({
							detail: count==1,
							modify: count<0,
							del: count<0,
							submit:count<0
						});
						
					}else {
					
						
						Qm.setTbar({
							detail: count==1,
							modify: count<0,
							del: count>0,
							submit:count<0
						});
					}

			},
			rowAttrRender : function(rowData, rowid) {
                var fontColor="black";
                if (rowData.status=='已完成'){
                    fontColor="green";
                }else if(rowData.status=='进行中'){
                	fontColor="blue";
                }else if(rowData.status=='未完成') {
                    fontColor="red";
                }else if(rowData.status=='未开始') {
                    fontColor="orange";
                }
                return "style='color:"+fontColor+"'";
            }
		}
	};
	
	$(function(){
            importData();
        });
function importData() {
    //创建上传实例
    khFileUploader = new KHFileUploader({
        fileSize: "10mb",//文件大小限制
        buttonId: "importData",//上传按钮ID
        container: "filecontainer3",//上传控件容器ID
        title: "工作任务导入",//文件选择框提示
        saveDB : false,
        extName: "xls,xlsx",//文件扩展名限制
        fileNum: 1,//限制上传文件数目
        callback : function(files){
                    //文件无误，执行保存
                    saveData(files);
        }
    });
}


// 上传完成，开始保存汇编数据
function saveData(files){
    $("body").mask("正在保存...");
    $.post("office/ywhbsgzAction/savebsgz.do",{files:$.ligerui.toJSON(files)},function (data) {
            $("body").unmask();
            if (data.success) {
                if(data.repData!=""&&data.repData!=undefined){
                    $.ligerDialog.alert("保存成功！");
                } else {
                    top.$.notice("保存成功！");
                }Qm.refreshGrid();
            } else {
                $.ligerDialog.error("保存失败！<br/>");
            }
       },"json");
}

function add() {
	top.$.dialog({
		width: 900,
		height: 400,
		lock: true,
		data: {
			window: window
		},
		title: "新增",
		content: 'url:app/office/ywhbsgz_detail.jsp?pageStatus=add'
	});
}
function addxf(){
	
	  var id = Qm.getValueByName("id");
	
	    $.ligerDialog.confirm('是否提交下发？', function (yes){
	        if(!yes){return false;}
	        top.$.ajax({
	                     url: "office/ywhbsgzAction/taskSend.do?id="+id,
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
	                    	 $.ligerDialog.success("下发失败！");
	                     }
	                 });
	    });
	
	
}
	function modify() {
		var id = Qm.getValueByName("id");
		top.$.dialog({
			width: 900,
			height: 400,
			lock: true,
			data: {
				window: window
			},
			title: "修改",
			content: 'url:app/office/ywhbsgz_detail.jsp?id=' + id + '&pageStatus=modify'
		});
	}
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
			content: 'url:app/office/Ywhbsgz_yx.jsp?id=' + id + '&pageStatus=detail'
		});
	}
	function del() {
		$.del(kFrameConfig.DEL_MSG, "office/ywhbsgzAction/delete.do", {
			"ids": Qm.getValuesByName("id").toString()
		});
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

<p id="filecontainer3" style="margin:5px"><a class="l-button-warp l-button" id="importData"><span class="l-button-main l-button-hasicon"><span class="l-button-text">导入工作任务<span class="l-button-icon iconfont l-icon-excel-import"></span></span></a></p>
	<qm:qm pageid="TJY2_YWHBSGZ" script="false" singleSelect="true">
	</qm:qm>
</body>
</html>