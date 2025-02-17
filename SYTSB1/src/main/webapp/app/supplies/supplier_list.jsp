<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
<%@include file="/k/kui-base-list.jsp"%>
<script type="text/javascript">
	var qmUserConfig = {
		sp_defaults : {
			columnWidth : 0.3,
			labelAlign : 'right',
			labelSeparator : '',
			labelWidth : 120
		},
		sp_fields : [ 
		              {name : "gysmc",compare : "like"}, 
		              {name : "gysdz",compare : "like"}, 
		              {name : "frdb",compare : "like"}, 
		              {name : "lxr",compare : "like"}, 
		              {name : "lxrdh",compare : "like"}, 
		              {name : "yx",compare : "like"}],
		tbar : [ 
		         {text : '详情',id : 'detail',icon : 'detail',click : detail},"-",
		         {text : '新增',id : 'add',icon : 'add',click : add},
		         {text : '修改',id : 'modify',icon : 'modify',click : modify},
				 { text:'删除', id:'del',icon:'delete', click:del},"-",
		         {text : '导出办公耗材验收表',id : 'outYs',icon : 'export',click : outBgYs},
		         {text : '验收表',id : 'outYs',icon : 'export',click : outYs},
		         {text : '导出入库单',id : 'outRk',icon : 'export',click : outRk}
		],
		             


		listeners : {
			rowClick : function(rowData, rowIndex) {

			},
			rowDblClick : function(rowData, rowIndex) {
				detail();
			},
			selectionChange : function(rowData, rowIndex) {
				var count = Qm.getSelectedCount();
				Qm.setTbar({
					detail : count == 1,
					modify : count == 1,
					del : count > 0,
					outYs:count==1,
					outRk:count==1
				});
			}
		}
	}
	function outRk(){
		 top.$.dialog({
				width : 600,
				height : 400,
				lock: true,
				parent: null,
				data: {
					window: window
				},
				title: "选择时间段",
				content: 'url:app/supplies/supplier_time.jsp',
				cancel: true,
				ok : function() {
					var data = this.iframe.contentWindow.getSelectResult();
					var id = Qm.getValueByName("id");
					console.log(id);
					var url="com/tjy2/supplier/outRk.do?gysId="+id;
			       download(url,"post",id,data.startTime,data.endTime);
				}
		 });
	}
	//其它耗材验收表
	function outYs(){

		 top.$.dialog({
				width : 600,
				height : 400,
				lock: true,
				parent: null,
				data: {
					window: window
				},
				title: "选择时间段",
				content: 'url:app/supplies/supplier_time.jsp',
				cancel: true,
				ok : function() {
					var data = this.iframe.contentWindow.getSelectResult();
					var id = Qm.getValueByName("id");
					var url="com/tjy2/supplier/outYs.do?gysId="+id;
			        download(url,"post",id,data.startTime,data.endTime);
				}
		 });
	}
	//办公耗材验收表
	function outBgYs(){
		 top.$.dialog({
				width : 600,
				height : 400,
				lock: true,
				parent: null,
				data: {
					window: window
				},
				title: "选择时间段",
				content: 'url:app/supplies/supplier_time.jsp',
				cancel: true,
				ok : function() {
					var data = this.iframe.contentWindow.getSelectResult();
					var id = Qm.getValueByName("id");
					var url="com/tjy2/supplier/outBghcYs.do?gysId="+id;
			       download(url,"post",id,data.startTime,data.endTime);
				}
		 });
		
//             top.$.dialog({
//                 width: 480,
//                 height: 300,
//                 lock: true,
//                 title: "导出办公耗材验收表",
//                 content: "url:app/supplies/supplier_time.jsp"
//             });
//         var id = Qm.getValueByName("id");
//         var url="com/tjy2/supplier/outYs.do?id="+id;
//         console.log(url);
//         download(url,"post",id);
	}
	 function download(url, method, id,startTime,endTime){
         jQuery('<form action="'+url+'" method="'+(method||'post')+'">' +  // action请求路径及推送方法
             '<input type="text" name="gysId" value="'+id+'"/>' + // id
             '<input type="text" name="startTime" value="'+startTime+'"/>' + // id
             '<input type="text" name="endTime" value="'+endTime+'"/>' + // id
             '</form>')
             .appendTo('body').submit().remove();
     }
	function del() {
		$.del("确定删除?", "com/tjy2/supplier/deleteByIds.do", {
			"ids" : Qm.getValuesByName("id").toString()
		})
	}

	function detail() {
		top.$.dialog({
					width : 900,
					height : 500,
					lock : true,
					parent : api,
					data : {
						window : window
					},
					title : "详情",
					content : 'url:app/supplies/supplier_detail.jsp?pageStatus=detail&id='
							+ Qm.getValueByName("id")
				});

	}
	function add() {
		top.$.dialog({
					width : 1100,
					height : 500,
					lock : true,
					parent : api,
					data : {
						window : window
					},
					title : "新增供应商",
					content : 'url:app/supplies/supplier_detail.jsp?pageStatus=add'
							+ Qm.getValueByName("id")
				});

	}
	function modify() {
		top.$.dialog({
					width : 900,
					height : 500,
					lock : true,
					parent : api,
					data : {
						window : window
					},
					title : "修改供应商",
					content : 'url:app/supplies/supplier_detail.jsp?pageStatus=modify&id='
							+ Qm.getValueByName("id") + "&status=2"
				});

	}
</script>

</head>
<body>
	<div class="lb_gys_list" id="titleElement">
		<qm:qm pageid="ch_gys_list">
		</qm:qm>
	</div>
</body>
</html>