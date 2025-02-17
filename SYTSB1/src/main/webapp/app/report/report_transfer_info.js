var reportTransferGrid;

function createReportTransferRecordGrid() {
    var columns=[
        { display: 'id', name: 'id', hide:true},
        { display: 'sn', name: 'sn', hide:true},
        { display: 'org_id', name: 'org_id', hide:true},
        { display: 'info_id', name: 'info_id', hide:true},
        { display: 'device_id', name: 'device_id', hide:true},
        { display: 'report_id', name: 'report_id', hide:true},
        { display: '部门名称', width: '20%', name: 'org_name', type: 'text', editor: { type: 'text'},required:false,maxlength:200},    
        { display: '单位名称', width: '30%', name: 'com_name', type: 'text', editor: { type: 'text'},required:false,maxlength:200},     
        { display: '报告编号', width: '15%', name: 'report_sn', type: 'text', editor: { type: 'text'},required:true,maxlength:200},
        { display: '金额', width: '10%', name: 'advance_fees',type: 'float',  editor: { type: 'float'}, required:false,maxlength:32},
        { display: '份数', width: '8%', name: 'report_count', type: 'int', editor: { type: 'spinner'},required:false,maxlength:32},       
        //{ display: '制造单位', width: '15%', name: 'made_unit_name', type: 'text', editor: { type: 'text'},required:false,maxlength:200},    
        //{ display: '安装单位', width: '15%', name: 'construction_unit_name', type: 'text', editor: { type: 'text'},required:false,maxlength:200}, 
        { display: '报送人ID', name: 'commit_user_id', hide:true},
        { display: '报送人', width: '8%', name: 'commit_user_name', type: 'text', editor: { type: 'text'},required:false,maxlength:20},
        { display: '报送时间', width: '10%', name: 'commit_time', editor: { type: 'date'},format:'yyyy-MM-dd',type:'date',required:false},
        { display: '接收人', width: '8%', name: 'receive_user_name', type: 'text', editor: { type: 'text'},required:false,maxlength:20},
        { display: '接收时间', width: '10%', name: 'receive_time', editor: { type: 'date'},format:'yyyy-MM-dd',type:'date',required:false},
        { display: '备注', width: '20%', name: 'remark', type: 'text', editor: { type: 'text'},required:false,maxlength:1000}
    ];
	
    if(pageStatus!="detail"){
        columns.unshift({ display: "<a class='l-a iconfont l-icon-add' href='javascript:void(0);' onclick='addNewRow(\"reportTransferRecords\")' title='增加'><span>增加</span></a>", isSort: false, width: '4%',height:'5%', render: function (rowdata, rowindex, value) {
            var h = "";
            if (!rowdata._editing) {
                h += "<a class='l-a l-icon-del' href='javascript:deleteRow(\"reportTransferRecords\")' title='删除'><span>删除</span></a> ";
            }
            return h;
        }
        });
    }

    reportTransferGrid = $("#reportTransferRecords").ligerGrid({
    	columns: columns,
    	enabledEdit: pageStatus!="detail",
        rownumbers: true,    
        height:"85%",
        width:"100%",
        //是否显示行序号
        frozenRownumbers: false,
        usePager: false,
        //onAfterEdit: f_onAfterEdit,
        data: {Rows: [
        ]}
    });
    gridConfig["reportTransferRecords"].manager=reportTransferGrid;
    
}

//编辑后事件 
function f_onAfterEdit(e){
	var grid=gridConfig["reportTransferRecords"].manager; 
	var data = grid.getSelectedRow();   
	alert(data.device_type_code);
	alert(e.column.name);
	grid.updateCell('device_name', data.device_type_code, e.record);
}

function addNewRow(name) {
	var grid=gridConfig[name].manager; 
	grid.addEditRow({}); //添加一行
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
    reportTransferRecords:{manager:reportTransferGrid,delUrl:"report/transfer/record/del.do"}
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
