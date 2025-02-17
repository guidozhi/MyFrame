var deviceCopyInfoGrid;
function createDeviceInfoGrid() {
    var columns=[
    	{ display: 'id', name: 'id', hide:true},
        { display: '设备注册代码', width: '50%', name: 'device_registration_code', type: 'text', editor: { type: 'text'},required:true,maxlength:20}       
    ];
	
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

    deviceCopyInfoGrid = $("#infos").ligerGrid({
    	columns: columns,
    	enabledEdit: pageStatus!="detail",
        rownumbers: true,    
        height:"45%",
        width:"99%",
        //是否显示行序号
        frozenRownumbers: false,
        usePager: false,
        //onAfterEdit: f_onAfterEdit,
        data: {Rows: [
        ]}
    });
    gridConfig["infos"].manager=deviceCopyInfoGrid;
    
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
                 //$.getJSON(gridConfig[name]["delUrl"], {ids: data.id}, function (json) {
                     //if (json.success) {
                         grid.deleteSelectedRow();
                     //}
                 //});
             }
         });
    }
}

var gridConfig={	
    infos:{manager:deviceCopyInfoGrid,delUrl:""}
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
