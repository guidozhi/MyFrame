var deviceGrid;

var g = null;
var fj = [ {  id:'0' ,text:'否'}, { id:'1', text:'是' } ];

var reprot_type=null;
function createdeviceGrid() {

	var columns = [
			{
				display : 'id',
				name : 'id',
				hide : true
			},
			{
				display : 'sn',
				name : 'sn',
				hide : true
			},
			{
				display : 'is_back',
				name : 'is_back',
				hide : true
			},
			{
				display : 'fk_tsjc_device_document_id',
				name : 'fk_tsjc_device_document_id',
				hide : true
			},{
				display:'设备类别id',
				name:'device_code_t',
				hide : true
			},
			{
				display : '复检',
				width : '4%',
				name : 'is_recheck',
				editor : {
					type : 'text'
				},
				required : false,
				editor : {
					type : 'select',
					data : fj,
					ext : {
						emptyOption : false
					}
				},
				render : function(item) {
					if (item.fj == '1')
						return '是';
					return '否';
				}
			},
			{
				display : '设备类别',
				width : '14%',
				name : 'device_sort',
				type : 'text',
				required : true,

				render : function(item) {

					return render(item["device_sort"], deviceType);
				}
			},{
				display : '设备名称',
				width : '14%',
				name : 'device_name',
				type : 'text',
				required : true,
				maxlength : 100
			},

			{
				display : '注册代码',
				width : '14%',
				name : 'device_registration_code',
				type : 'text',
				required : true,
				maxlength : 32
			},

			{
				display : '报告类型',
				width : '17%',
				name : 'report_type',
				type : 'text',
				required : true,
				editor : {
					type : 'select',
					data : reportType,
					ext : {
						emptyOption : false,
						selectBoxHeight : 240
					}
				},
				render : function(item) {

					return render(item["report_type"], reportType);
				}
			},

			{
				display : '应收金额',
				width : '6%',
				name : 'advance_fees',
				type : 'float',
				editor : {
					type : 'spinner'
				},
				required : false,
				maxlength : 32,
				hide : true
			},
			{
				display : '检验日期',
				width : '10%',
				name : 'advance_time',
				editor : {
					type : 'date'
				},
				format : 'yyyy-MM-dd',
				type : 'date',
				required : true
			},
			{
				display : '参检人员ID',
				width : '17%',
				name : 'check_op_id',
				type : 'text',
				required : true,
				hide : true,

				render : function(item) {
					var arr1 = [];
					for ( var i in fzr) {
						var bds = ("," + item["check_op_id"] + ",").indexOf(","
								+ fzr[i]["id"] + ",") >= 0;
						if (bds) {
							arr1.push(fzr[i]['text']);
						}
					}
					if (arr1.toString() == '' || arr1.toString() == null) {
						return render(item["check_op_id"], fzr);
					} else {
						return arr1.toString();
					}

				}

			},
			{
				display : '参检人员',
				width : '14%',
				name : 'check_op_name',
				type : 'text',
				required : true,

				render : function(item) {
					var arr1 = [];
					for ( var i in fzr) {
						var bds = ("," + item["check_op_name"] + ",")
								.indexOf("," + fzr[i]["id"] + ",") >= 0;
						if (bds) {
							arr1.push(fzr[i]['text']);
						}
					}
					if (arr1.toString() == '' || arr1.toString() == null) {
						return render(item["check_op_name"], fzr);
					} else {
						return arr1.toString();
					}

				}

			}, {

				display : '检验任务单',
				width : '14%',
				name : 'contract_task_id',
				type : 'text',
				required : true
			}, {
				display : '检验联系人',
				width : '8%',
				name : 'check_op',
				type : 'text',
				editor : {
					type : 'text'
				},
				required : false
			}, {
				display : '联系人电话',
				width : '10%',
				name : 'check_tel',
				type : 'text',
				editor : {
					type : 'text'
				},
				required : false
			},

			{
				display : 'is_back',
				name : 'is_back',
				hide : true
			}, {
				display : 'is_report_input',
				name : 'is_report_input',
				hide : true
			}, {
				display : 'fk_flow_index_id',
				name : 'fk_flow_index_id',
				hide : true
			}, {
				display : 'is_flow',
				name : 'is_flow',
				hide : true
			}, {
				display : 'flow_note_id',
				name : 'flow_note_id',
				hide : true
			}, {
				display : 'flow_note_name',
				name : 'flow_note_name',
				hide : true
			}, {
				display : 'report_sn',
				name : 'report_sn',
				hide : true
			}, {
				display : 'report_item',
				name : 'report_item',
				hide : true
			}
	];

	if (pageStatus != "detail") {
		columns
				.unshift({
					display : "<a class='l-a iconfont l-icon-add' href='javascript:void(0);' onclick='addNewRow(\"device\")' title='增加'><span>增加</span></a>",
					isSort : false,
					width : '4%',
					height : '5%',
					render : function(rowdata, rowindex, value) {
						var h = "";
						if (!rowdata._editing) {
							h += "<a class='l-a l-icon-del' href='javascript:deleteRow(\"device\")' title='删除'><span>删除</span></a> ";
						}
						return h;
					}
				});
	}

	deviceGrid = $("#device").ligerGrid({
		columns : columns,
		enabledEdit : pageStatus != "detail",
		onBeforeEdit : f_onBeforeEdit,
		rownumbers : true,
		height : "260",
		width : "100%",
		//是否显示行序号
		frozenRownumbers : false,
		usePager : false,
		data : {
			Rows : []
		}
	});
	gridConfig["device"].manager = deviceGrid;

}

function addNewRow(name) {
	var fk_unit_id = document.getElementById("fk_unit_id").value;
	
	if(fk_unit_id==null||fk_unit_id==undefined || fk_unit_id ==""){
		top.$.notice("请先选择受检单位！");
	}else{
		
		//var type = device_type.toString().substring(0, 1);
		


		 dia1 = $.ligerDialog.open({ title: '选择', width: 700, height: 600,parent:api, url: 'app/inspection/dev_list.jsp?org_id='+fk_unit_id,data: {"pwindow" : window},buttons: [

                                                                                         //   { text: '新增', onclick: f_addDevice },
		                                                                                                     { text: '确定', onclick: f_importOK },
		                                                                                                     { text: '取消', onclick: f_importCancel }
		                                                                                                 ]
		                                                                                                 }); 

		
		}
	
	//var grid=gridConfig[name].manager; 	//grid.addEditRow({}); //添加一行
}

function f_addDevice(item, dialog)
{

	var fk_unit_id = document.getElementById("fk_unit_id").value;
	top.$.dialog({
		width : 700, 
		height : 260, 
		lock : true, 
		title:"新增",
		parent:api,

		//content: 'url:app/device/add_device_detail.jsp?org_id='+fk_unit_id+'&device_type='+device_type+'&type='+type,
		content: 'url:app/device/add_device_detail.jsp?org_id='+fk_unit_id,

		data : {"window" : window}
	});
	
}


function f_importOK(item, dialog)
{ 
    var rows = dialog.frame.f_select();
    if (!rows)
    {
    	top.$.notice("请选择行！");
        return;
    } 
    var devRow = deviceGrid.rows;
    
    var isexist=false;
    for(var i in rows){
    	for(var j in devRow){
    		if(rows[i].id == devRow[j].fk_tsjc_device_document_id){
    			isexist = true;
    			break;
    		}else{
    			isexist = false;
    		}
    	}
    	if(!isexist){
    		$.post("department/basic/getPage.do?checkType="+checkType+"&device_code="+rows[i].device_code_t, function(resp) {
				if (resp.data) {
					$("#report_types").ligerGetComboBoxManager().setData(eval(resp.data));
					reportType = eval(resp.data);
					var obj = deviceGrid.getColumn(10);
					obj.editor.data = reportType;
					
					
				}
				
			});
    		var cur_date = currentTime();
    		var tt = {is_back:rows[i].is_back,device_sort:rows[i].device_sort,device_name:rows[i].device_name,device_registration_code:rows[i].device_registration_code,advance_time:cur_date,check_op:rows[i].security_op,check_tel:rows[i].security_tel,fk_tsjc_device_document_id:rows[i].id,advance_fees:rows[i].advance_fees,device_sort:rows[i].device_sort,device_code_t:rows[i].device_code_t};
			deviceGrid.addRow(tt);
    	
    	}
    }
    dialog.close();
}
function f_importCancel(item, dialog)
{ 
    dialog.close();
}

function currentTime(){
	
	var d = new Date(),
	
	 str = '';
	
	 str += d.getFullYear()+'-';
	 if(d.getMonth()<10){
	 	str  += "0";
	 }
	
	 str  += d.getMonth() + 1+'-';
	 if(d.getDate()<10){
	 	str  += "0";
	 }
	 str  += d.getDate()+' ';
	
	 //str += d.getHours()+':';
	
	 //str  += d.getMinutes()+':';
	
	//str+= d.getSeconds();
	
	
	
	return str;
	
	}


function deleteRow(name) {
	
    var grid=gridConfig[name].manager;   //删除一行
    
    var data = grid.getSelectedRow();
    
    if(data.id==null||data.id==undefined||data.id==""){
    	grid.deleteSelectedRow();
    }else{
    	 $.ligerDialog.confirm(kui.DEL_MSG, function (yes) {
             if (yes) {
                 $.getJSON(gridConfig[name]["delUrl"], {ids: data.id}, function (json) {
                     if (json.success) {
                         grid.deleteSelectedRow();
                     }
                 });
             }
         });
    }
    
}

var gridConfig={
		
		
    device:{manager:deviceGrid,delUrl:"inspectionInfo/basic/del.do"}
 
};

function render(value,data){
    for (var i in data) {
    	if (data[i]["id"] == value)
        {
    		
        	return data[i]['text'];
        }
		if(data[i].children)
		{
			for(var j in data[i].children)
			{
				if(data[i].children[j]["id"] ==value)
					return data[i].children[j]['text'];
				if(data[i].children[j].children)
				{
					for(var k in data[i].children[j].children)
						if(data[i].children[j].children[k]["id"]==value)
						{
							return data[i].children[j].children[k]["text"];
						}
				}
			}
		}
    }
    return value;
}


function f_onBeforeEdit(e)
{ 
	//for(var i in e){alert(i)}
	if(e.record.is_report_input == "2" ){
			return false;
			}else{
				return true;
			}
		}

