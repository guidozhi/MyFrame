var billGrid;
var g = null;
var reprot_type=null;
function createbillGrid() {
	 billGrid = $("#degree").ligerGrid({
	    	columns: defineColumns(),
	        enabledEdit: pageStatus!="detail",
	        clickToEdit: true,
	        rownumbers: true,    
	        height:"280",
	        width:"99%",
	        //是否显示行序号
	        frozenRownumbers: false,
	        usePager: false,
	        data: {Rows: [
	        ]}
	    });
}
function defineColumns(){
    var columns=[];
    if(pageStatus!="detail"){
        columns.push({ display: "<a class='l-a iconfont l-icon-add' href='javascript:void(0);' onclick='addNewRow(\"device\")' title='增加'><span>增加</span></a>", isSort: false, width: '4%',height:'5%', render: function (rowdata, rowindex, value) {
            var h = "";
            if (!rowdata._editing) {
                h += "<a class='l-a l-icon-del' href='javascript:deleteRow(\"device\")' title='删除'><span>删除</span></a> ";
            }
            return h;
        }
        });
    };
    columns.push(
    	{ display: 'id', name: 'id', hide:true},
        { display: 'invoice_id', name: 'invoice_id', hide:true},
        { display: 'cw_invoice_lead_id', name: 'cw_invoice_lead_id', hide:true},
        { display: 'data_type', name: 'data_type', hide:true},
        { display: '登记人', name: 'registrant_name',width:'10%', hide:false},
        { display: '登记时间', name: 'registrant_date',width:'10%',format:'yyyy-MM-dd',type:'date', hide:false},
        { display: '领用人', name: 'lead_name',width:'10%', hide:false},
        { display: '领用时间', name: 'lead_date',width:'10%',format:'yyyy-MM-dd',type:'date', hide:false},
        { display: '领用发票编号', name: 'lead_code',width:'10%', hide:false},
        { display: '领用部门', name: 'lead_dep',width:'15%', hide:false},
        { display: '领用数量', name: 'lead_num',width:'10%', hide:false},
        { display: 'lead_id', name: 'lead_id', hide:true},
        { display: 'registrant_id', name: 'registrant_id', hide:true},
        { display: 'lead_dep_id', name: 'lead_dep_id', hide:true},
        { display: 'invoice_type_code', name: 'invoice_type_code', hide:true},
        { display: '发票类型', name: 'invoice_type',width:'15%', hide:false}
        
       
    );
    return columns;
   }	

	//重新渲染列
	function reRenderColumns(){
	billGrid.setOptions( 
				{ columns: defineColumns() } 
				);
				
	billGrid.loadData(true);  
	}


function addNewRow(name) {
	var apply_op_id=$("#apply_op_id").val();
	var apply_op=$("#apply_op").val();
	var audit_op=$("#audit_op").val();
	var audit_op_id=$("#audit_op_id").val();
	var audit_time=$("#audit_time").val();
	var apply_org=$("#apply_org").val();
	var apply_org_id=$("#apply_org_id").val();
	top.$.dialog({
			parent: api,
			width : 1024, 
			height : 550, 
			lock : true, 
			title:"选择未领用发票号",
			content: 'url:app/finance/cw_bill_choose_invoice_list.jsp',
			data : {"parentWindow" : window,apply_op_id:apply_op_id,apply_op:apply_op,
				audit_op:audit_op,audit_op_id:audit_op_id,audit_time:audit_time,apply_org:apply_org,
				apply_org_id:apply_org_id}
		});	
}

function deleteRow(name) {
	
    var data = billGrid.getSelectedRow();
    
    if(data.id==null||data.id==undefined||data.id==""){
    	billGrid.deleteSelectedRow();
    }else{
    	 $.ligerDialog.confirm(kui.DEL_MSG, function (yes) {
             if (yes) {
            	 $.getJSON("cwBillParaAction/del.do", {id: data.id}, function (json) {
					 if (json.success) {
						 billGrid.deleteSelectedRow();
					 }
				 });
             }
         });
    }
    
}

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