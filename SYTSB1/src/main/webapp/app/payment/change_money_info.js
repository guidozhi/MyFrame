var inspectionChangeGrid;
function createInfoGrid() {
    var columns=[
        { display: 'id', name: 'id', hide:true},
        { display: 'fk_inspection_info_id', name: 'fk_inspection_info_id', hide:true},
        { display: '报告编号', width: '12%', name: 'report_sn', type: 'text'},
        { display: '使用单位', width: '33%', name: 'report_com_name', type: 'text'},
        { display: '应收金额', width: '10%', name: 'advance_fees', type: 'float'},
        { display: '修改金额', width: '10%', name: 'change_money', type: 'text', editor: { type: 'float'},required:true,maxlength:20}, 
        { display: '备注', width: '35%', name: 'remarks', editor: { type: 'text'}, type:'text', required:false, maxlength:1000}
    ];
	/*
    if(pageStatus!="detail"){
        columns.unshift({ display: "<a class='l-a iconfont l-icon-add' href='javascript:void(0);' onclick='addNewRow(\"infos\")' title='增加'><span>增加</span></a>", isSort: false, width: '4%',height:'5%', render: function (rowdata, rowindex, value) {
            var h = "";
            if (!rowdata._editing) {
                h += "<a class='l-a l-icon-del' href='javascript:deleteRow(\"infos\")' title='删除'><span>删除</span></a> ";
            }
            return h;
        }
        });
    }
	*/
    
    inspectionChangeGrid = $("#infos").ligerGrid({
    	columns: columns,
    	enabledEdit: pageStatus!="detail",
        rownumbers: true,    
        height:"65%",
        width:"100%",
        //是否显示行序号
        frozenRownumbers: false,
        usePager: false,
        //onAfterEdit: f_onAfterEdit,
        data: {Rows: [
        ]}
    });
    gridConfig["infos"].manager=inspectionChangeGrid;
    
}

//编辑后事件 
function f_onAfterEdit(e){
	var grid=gridConfig["infos"].manager; 
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
    infos:{manager:inspectionChangeGrid,delUrl:"inspectionChangeMoney/del.do"}
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
