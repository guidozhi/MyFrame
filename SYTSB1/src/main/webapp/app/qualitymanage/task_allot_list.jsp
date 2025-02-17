<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
<%@include file="/k/kui-base-list.jsp"%>
<script type="text/javascript">
var iscw="0";
<sec:authorize access="hasRole('TJY2_ZLGL_CHECK')">
	iscw = "1";
</sec:authorize>

var qmUserConfig={
		sp_defaults:{columnWidth:0.3,labelAlign:'right',labelSeparator:'',labelWidth:120},	
		sp_fields:[
			         {name:"task_sn",compare: "like"},
			         {name:"item_name",compare: "like"},
			         {name:"status",compare: "="},
			         {name:"duty_dep",compare: "like"},
			         {name:"duty_name",compare: "like"},
			         {group: [
			  				{name: "register_date", compare: ">="},
			  				{label: "到", name: "register_date", compare: "<=", labelAlign: "center", labelWidth:20}
			  		 ]}
			  		 
			        ],
			tbar:[{
				text: '详情', id: 'detail', icon: 'detail', click: detail
			}, "-", {
				text: '新增', id: 'add', icon: 'add', click: add 
			}, "-", {
				text: '修改', id: 'edit', icon: 'modify', click:edit
			}, "-", {
				text: '删除', id: 'del', icon: 'delete', click:del
			}, "-", {
				text: '提交', id: 'submit', icon: 'submit', click:submit
			/*}, "-" ,{
				text: '审核', id: 'check',icon:'check', click: check*/
			}, "-" ,{
				text: '处理', id: 'dispose',icon:'dispose', click: dispose
			}
			],
			 listeners:{
					rowClick:function(rowData,rowIndex){
					},
					rowDblClick:function(rowDate,rowIndex){
						detail();
					},
					selectionChange:function(rowDate,rowIndex){
						var count = Qm.getSelectedCount();
						Qm.setTbar({
							detail:count==1,
							edit:count==1,
							del:count>0,
							submit:count>0,
							check:count==1,
							dispose:count==1
						});
						var up_status = Qm.getValuesByName("status");
						/* var strs = new Array(); //定义一数组
		 					strs = up_status.split(","); //字符分割\ */
		 				var isUsd=true; //未提交
		 				var isStart=true; //未开始
		 				var isConduct=true; //进行中
						var Unfinished=true; //未完成
						var Complete=true; //已完成
						var Check = true; //待审核
						var NoCheck = true; //审核不通过
					
						if(count > 0){
							for(var i=0;i<up_status.length;i++){
									/* alert(up_status[i]); */
								if(up_status[i]!="未提交" && up_status[i]!="审核不通过"){
									isUsd = false;
								}if(up_status[i]!="未开始"){
									isStart = false;
								}if(up_status[i]!="进行中"){
									isConduct = false;
								}if(up_status[i]!="未完成"){
									Unfinished = false;
								}if(up_status[i]!="已完成"){
									Complete=false;
								}if(up_status[i]!="待审核"){
								    Check = false;
								}
									
							}
						}
						if(iscw !='1'){
							if (count == 0) {
								Qm.setTbar({
									detail: false,
									add : true,
									edit : false,
									del:false,
									submit:false,
									check:false,
									dispose:false
								});
							} else if (count == 1) {
								Qm.setTbar({
									detail: true,
									add : true,
									edit : true&&(isUsd || Check),
									del:true&&isUsd,
									submit:true&&isUsd,
									check:true&&Check,
									dispose:true&&isConduct
								});
							} else {
								Qm.setTbar({
									detail: false,
									add : true,
									edit : false,
									del:false,
									submit:false,
									check:false,
									dispose:false
								});
							}
						}else {
							if (count == 0) {
								Qm.setTbar({
									detail: false,
									add : true,
									edit : false,
									del:false,
									submit:false,
									check:false,
									dispose:false
								});
							}else if (count == 1) {
								Qm.setTbar({
									detail: true,
									add : true,
									edit : true&&isUsd,
									del:true,
									submit:true&&isUsd,
									check:true&&Check,
									dispose:true&&isConduct
								});
							} else {
								Qm.setTbar({
									detail: false,
									add : true,
									edit : false,
									del:true,
									submit:false,
									check:false,
									dispose:false
								});
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
			            }else if(rowData.status=='待审核'){
			            	fontColor="#6F00D2";
			            }else if(rowData.status=='审核不通过'){
                       	 	fontColor="#8E8E8E";
                        }
			            return "style='color:"+fontColor+"'";
			        }
         						
				
				 }
			
	};


function detail() {
	var id = Qm.getValueByName("id");
	var status = Qm.getValueByName("status1");
	top.$.dialog({
		width: 900,
		height: 350,
		lock: true,
		parent: api,
		title: "任务详情",
		content: 'url:app/qualitymanage/task_allot_fb_datail.jsp?pageStatus=detail&id='+ id+"&status="+status,
		data: {
			window: window
		}
	});
}

function add(){
	top.$.dialog({
		width:900,
		height:500,
		lock:true,
		parent:api,
		data:{
			window:window
		},
		title:"新增",
		content:'url:app/qualitymanage/task_allot_datail.jsp?pageStatus=add'
	});
}

function edit(){
	var id = Qm.getValueByName("id");
	var up_status = Qm.getValueByName("status");
	top.$.dialog({
		width:900,
		height:500,
		lock:true,
		parent:api,
		data:{
		    window:window
		},
		title:"修改",
		content:'url:app/qualitymanage/task_allot_datail.jsp?id=' + id + '&pageStatus=modify&up_status'+up_status
	});
}

function del() {
	var id = Qm.getValueByName("id");
	$.ligerDialog.confirm('您确定要删除所选数据吗？', function (yes){
    	if(!yes){return false;}
		top.$.ajax({
            url: "taskAllot/allot/delete.do?ids="+id,
            type: "POST",
            dataType:'json',
            async: false,
            success:function (data) {
               if(data.success){
                  top.$.notice(data.msg,3);
                   Qm.refreshGrid();//刷新
               }else{
                   $.ligerDialog.warn(data.msg);
                   Qm.refreshGrid();//刷新
               }
            },
            error:function () {
                //$.ligerDialog.warn("提交失败！");
           	 $.ligerDialog.error("出错了！请重试！!");
            }
        });
	});
	
	/* $.del(kFrameConfig.DEL_MSG, "taskAllot/allot/delete.do", {
		"ids": Qm.getValuesByName("id").toString()
	}); */
}

function check(){
	var id = Qm.getValueByName("id");
	var up_status = Qm.getValueByName("status");
	top.$.dialog({
		width: 900,
		height: 350,
		lock: true,
		parent: api,
		data: {
			window: window
		},
		title: "审核",
		content: 'url:app/qualitymanage/task_allot_datail.jsp?pageStatus=detail&id='+ id+"&up_status="+up_status+"&isCheck=1"
	});
}

function dispose(){
	var id = Qm.getValueByName("id");
		top.$.dialog({
			width: 900,
			height: 500,
			lock: true,
			parent: api,
			title: "处理任务",
			content: 'url:app/qualitymanage/task_allot_fb.jsp?pageStatus=modify&id='+ id,
			data: {
				window: window
			}
	});
}

function submit(){
    var id = Qm.getValueByName("id");
    $.ligerDialog.confirm('是否提交任务书？', function (yes){
        if(!yes){return false;}
        top.$.ajax({
                 url: "taskAllot/allot/submit.do?id="+id,
                 type: "GET",
                 dataType:'json',
                 async: false,
                 success:function (data) {
                    if(data.success){
                        $.ligerDialog.success("提交成功！");
                        Qm.refreshGrid();//刷新
                    }else{
                        $.ligerDialog.warn(data.msg);
                    }
                 },
                 error:function () {
                     $.ligerDialog.warn("提交失败！");
                 }
             });
    });
} 
</script>
	<div class="item-tm" id="titleElement">
        <div class="l-page-title">
            <div class="l-page-title-note">提示：列表数据项
                <font color="black">“黑色”</font>代表未提交，
                <font color="#6F00D2">“紫色”</font>代表待审核,
                <font color="orange">“橙色”</font>代表未开始，
                <font color="blue">“橙色”</font>代表进行中，
                <font color="green">“绿色”</font>代表已完成，
                <font color="red">“红色”</font>代表未完成，
                <font color="8E8E8E">"灰色"</font>代表审核不通过。
            </div>
        </div>
    </div>
</head>
<body>
	<qm:qm pageid="TJY2_ZLGL_RWGL" script="false" singleSelect="false"></qm:qm>
</body>
</html>