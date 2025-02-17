<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.khnt.security.CurrentSessionUser"%>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
    CurrentSessionUser user=(CurrentSessionUser)request.getSession().getAttribute("currentSessionUser");

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head pageStatus="${param.pageStatus}">
    <title></title>
    <!-- 每个页面引入，页面编码、引入js，页面标题 -->
    <%@include file="/k/kui-base-form.jsp" %>
    <script type="text/javascript" src="pub/selectUser/selectUnitOrUser.js"></script>
    <script type="text/javascript">
        $(function () {
        	var nowDate = new Date().format("yyyy-MM-dd");
        	$("#equipmentTime").val(nowDate);
        	if("${param.pageStatus}"=="add" || "${param.pageStatus}"=="modify"  ){
        		   document.getElementById('jdjg').style.display = "none";
        	}
        	
			//如果不设置额外参数，不用调用此方法，初始化时会自动调用
            $("#form").initForm({
                success : function(responseText) {//处理成功
                    if (responseText.success) {
                        top.$.dialog.notice({content:'操作成功'});
                        api.data.window.Qm.refreshGrid();
                        $("#id").val(responseText.data.id);
                    } else {
                        $.ligerDialog.error('操作失败' + responseText);
                    }
                },
                getSuccess:function(res){
                	var readers = [];
                    var names = [];
                    var ids = [];//alert(res.data.dutyName);
                    if(res.data.checkOp){
                         names = res.data.checkOp.split(",");
                         ids = res.data.checkOpId.split(",");
                         for(var i=0;i<names.length;i++){
                            readers.push({
                                types : "reader",
                                name: names[i],
                                id: ids[i]
                            });
                        } 
                        addReader(readers,false);
                    }
                }
            });
            $("#barcode").bind("input propertychange", function () { 
            	var bar_code = $("#barcode").val();
            	if(bar_code.length == 13){
            		loadBybarcode(bar_code);
            	}
            });
            /* $('input[name="unit"]').autocomplete("eq/docimasyFkAction/getBaseunit.do", {
        		max: 20,    //列表里的条目数
                minChars: 1,    //自动完成激活之前填入的最小字符
                width: 300,     //提示的宽度，溢出隐藏
                scrollHeight: 300,   //提示的高度，溢出显示滚动条
                scroll: false,   //当结果集大于默认高度时是否使用卷轴显示
                matchContains: true,    //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
                autoFill: false,    //自动填充
                formatItem: function(row, i, max) {
                    return row.text;
                },
                formatMatch: function(row, i, max) {
                    return row.text;
                },
                formatResult: function(row) {
                    return row.text;
                }
            }).result(function(event, row, formatted) {
            	$("#form1").setValues({"fkTemplateId":row.id});
            }); */
        });
        //扫码后加载数据
        function loadBybarcode(barcode){
        	if(barcode.length==13){
        		$.ajax({
        	    	url: "eq/docimasyFkAction/loadBybarcode.do?barcode="+barcode,
        	        type: "POST",
        	        success: function (resp) {
        	        	if(resp.success){
        	        		var equipment=resp.equipment;
        	        		$('#equipmentId').val(equipment.id);	// 设备ID
        	        		$('#equipmentName').val(equipment.eq_name);	//设备名称
        	        		$('#equipmentNumbe').val(equipment.eq_no);	//内部编号
        	        		$('#eqType').val(equipment.eq_model);	//设备型号
        	        		$('#eqNumbe').val(equipment.eq_sn);	//出厂编号
        	        		$('#period').val(equipment.eq_check_cycle);	//周期
        	        		$('#nextTime').val(equipment.eq_next_check_date);	//下次检定日期
        	        		$('#unit').val(equipment.check_unit);	//检定单位
        	        		if("${param.type}"=="addRecord"){
        	        			$("#nextTime").ligerComboBox({disabled: false });
        	        			$('#nextTime').val("");	//下次检定日期
        	        		}
        	        	}else{
        	        		$.ligerDialog.alert('提示：'+resp.message);
        	        	}
        	        },
        	        error: function (data0,stats) {
        	            $.ligerDialog.warn('提示：扫码查询失败！');
        	        }
        	    }); 
        	}else{
        		$.ligerDialog.error('提示：此二维码不正确！');
        	}
        	$("#barcode").val("").focus(); 
        }
     // 选择设备
    	function selEquipment(){	
    		top.$.dialog({
    			parent: api,
    			width : 800, 
    			height : 550, 
    			lock : true, 
    			title:"选择设备信息",
    			content: 'url:app/equipment/base/equipment_docimasy_choose_list.jsp',
    			data : {"parentWindow" : window}
    		});
    	}

     
    	function callBack(data){
    		$('#equipmentId').val(data.id);	// 设备ID
    		$('#equipmentName').val(data.eq_name);	//设备名称
    		$('#equipmentNumbe').val(data.eq_no);	//内部编号
    		$('#eqType').val(data.eq_model);	//设备型号
    		$('#eqNumbe').val(data.eq_sn);	//出厂编号
    		$('#period').val(data.eq_check_cycle);	//周期
    		$('#nextTime').val(data.eq_next_check_date);	//下次检定日期
    		$('#unit').val(data.check_unit);	//检定单位
    		if("${param.type}"=="addRecord"){
    			$("#nextTime").ligerComboBox({disabled: false });
    			$('#nextTime').val("");	//下次检定日期
    		}
    	}
    	function selectReaders(){
            
            var readers = $("#form").data("readers"); 
            selectUnitOrUser("4",1,"","",function(datas){
                if(!datas.code)return;
                var codeArr = datas.code.split(",");
                var nameArr = datas.name.split(",");
                var readers = [];
                var existReaders = $("#form").data("readers")||[];
                for(var i in codeArr){
                    var exist = false;
                    for(var j in existReaders){
                        if(existReaders[j].stakeholderId == codeArr[i] && existReaders[j].valid == true)
                            exist=true;
                    }
                    if(exist) continue;
                    readers.push({
                        types : "reader",
                        name: nameArr[i],
                        id: codeArr[i]
                    });
                }
                addReader(readers,true);
            });
        }
    	var CCnames='';//送检负责人
        var CCids='';//送检负责人id
         function addReader(newReaders,isNew){
         var names = '';
         var ids = '';
         var repids='';
         var repNames='';
         //alert(newReaders);
          for(var i in newReaders){
                 if(CCids.indexOf(newReaders[i].id)==-1){
                     names = names+newReaders[i].name+","; 
                     ids = ids+newReaders[i].id+",";
                     var mtext='<span class="label label-read" id="' + (isNew?newReaders[i].id:newReaders[i].id) + '"><span class="text">' + newReaders[i].name;
                     if("${param.pageStatus}"!="detail"){
                        mtext = mtext+'</span><span class="del btn btn-lm" onclick="deleteReader(\'' + (isNew?newReaders[i].id:newReaders[i].id) + '\',' + isNew + ')"><img src="k/kui/images/icons/16/delete1.png">&nbsp;</span></span>';
                     }
                     if("${param.pageStatus}"=="detail"){
                        mtext = mtext+',';
                      }
                     $("#reader_td").prepend(mtext);
                 } else if (newReaders[i].name){
                     repNames = repNames+newReaders[i].name+",";
                 }
            }
            if(repNames != ""){
                     $.ligerDialog.warn("人员："+"<span style='color:red;'>"+repNames.substring(0,repNames.length-1)+"</span>"+" 已存在！");
            }
            CCnames = CCnames+names;
            CCids = CCids+ids;
            $("#checkOp").val(CCnames.substring(0, CCnames.length-1));
            $("#checkOpId").val(CCids);
         }
         function deleteReader(id,isNew){
             $("#"+id).remove();
             var ids = CCids.split(",");
             var names = CCnames.split(",");
             for(var i in ids){
                if(id==ids[i]){
                    ids[i]="";
                    names[i]="";
                }
             }
             var cid="";
             var cname="";
             for(var i in ids){
                if(ids[i]!=""){
                    cid = cid+ids[i]+',';
                    cname = cname+names[i]+',';
                }
             }
             CCnames = cname;
             CCids = cid;
             $("#checkOp").val(CCnames);
             $("#checkOpId").val(CCids);
         }
         //单位选择
         function selectUnit(){
	            top.$.dialog({
	                width: 640,
	                height: 300,
	                lock: true,
	                parent: api,
	                title: "送检单位",
	                content: 'url:app/equipment/unit_choose.jsp',
	                cancel: true,
	                ok: function(){
	                    var p = this.iframe.contentWindow.getSelectedUnit();
	                    if(!p){
	                        top.$.notice("请选择送检单位！", 3, "k/kui/images/icons/dialog/32X32/hits.png");
	                        return false;
	                    }
	                    $("#unit").val(p.unit); 
	                }
	            });
	        }
    </script>
</head>
<body>
    <form id="form1" action="eq/docimasyFkAction/save.do?type=${param.type}"  getAction="eq/docimasyFkAction/detail.do?id=${param.id}" >
        <input type="hidden" name="id"/>
         <input type="hidden" value="9dman" name="ids" id="ids"/>
         <input type="hidden" id="checkOpId" name="checkOpId"/>
        <table border="1" cellpadding="3" cellspacing="0" width="" class="l-detail-table">
      <tr> 
        <td class="l-t-td-left"> 条码框</td>
        <td class="l-t-td-right" colspan="3"> 
        <input name="barcode" id="barcode" type="text" ltype='text' validate="{maxlength:13}"/>
        </td>
       </tr>
      <tr> 
        <td class="l-t-td-left"> 设备名称</td>
        <td class="l-t-td-right"> 
        <input name="equipmentId" id="equipmentId" type="hidden" />
        <input name="equipmentName"   id="equipmentName" type="text" ltype='text' validate="{required:true,maxlength:100}" validate="{required:true,maxlength:32}" onclick="selEquipment()" ligerui="{value:'',iconItems:[{icon:'add',click:function(){selEquipment()}}]}"/>
        </td>
         <td class="l-t-td-left"> 设备编号</td>
        <td class="l-t-td-right"> 
        <input id="equipmentNumbe"  ligerUi="{disabled:true}" name="equipmentNumbe" type="text" ltype='text' />
        </td>
       </tr>
         <tr> 
        <td class="l-t-td-left"> 设备型号</td>
        <td class="l-t-td-right">  
        <input name="eqType"  ligerUi="{disabled:true}" id="eqType" type="text" ltype='text'   />
        </td>
         <td class="l-t-td-left"> 出厂编号</td>
        <td class="l-t-td-right"> 
        <input id="eqNumbe"  ligerUi="{disabled:true}" name="eqNumbe" type="text" ltype='text' />
        </td>
       </tr>
       <tr> 
       <td class="l-t-td-left"> 检定周期（月）</td>
	        <td class="l-t-td-right"> 
	        <input ligerUi="{disabled:true}" id="period" name="period" type="text" ltype='text' />
        </td>
           <td class="l-t-td-left"> 计划日期</td>
	        <td class="l-t-td-right"> 
	        <input ligerUi="{disabled:true}"  id="nextTime" name="nextTime" type="text" ltype="date" />
	        </td>
       </tr>
       <tr> 
	         <td class="l-t-td-left"> 送检时间</td>
	        <td class="l-t-td-right"> 
	        <!-- <input validate="{required:true,maxlength:32}" name="equipmentTime"type="text" ltype="date" validate="{required:false}"ligerui="{initValue:'',format:'yyyy-MM-dd'}"  onchange="setValues(this.value,'advance_time')"/> -->
	        <input validate="required:true" id="equipmentTime" name="equipmentTime" type="text" ltype="date" ligerui="{format:'yyyy-MM-dd'}"/>
	        </td>
	         <td class="l-t-td-left"> 检定单位</td>
        <td class="l-t-td-right"> 
        <input id="unit"  name="unit" validate="required:true" type="text" ltype='text' ligerui="{value:'',iconItems:[{icon:'org',click:function(){selectUnit()}}]}"/>
        </td>
       </tr>
       <tr> 
	        <td class="l-t-td-left">送检责任人</td>
			<td class="l-t-td-right" id="reader_td" colspan="3" >
			<input type="hidden" name="checkOp" id="checkOp"/>
				 <c:if test="${param.pageStatus=='modify' or param.pageStatus=='add'}"><span class="l-button label" title="送检责任人">
	                          <span  class="l-a l-icon-add"  onclick="selectReaders();">&nbsp;</span>
	                         </c:if>
			
			</td>
       </tr>
       <tr id="jdjg"> 
        <td class="l-t-td-left"> 检定结果</td>
        <td colspan="3" class="l-t-td-right"> 
        <textarea name="remark" rows="4" type="text" ltype='text' ></textarea>
        </td>
       </tr>
      </table>
    </form>
    <script type="text/javascript">
	        $("#form1").initFormList({
	        /* var docimasy_status=<u:dict code="TJY2_DOCIMASY_STATUS" /> */
	       	actionParam:{"docimasy.id":$("#form1>#ids")}, //保存时会在当前表单上附加此数据，如：{fkId:$("#form1>#id")}会把from1下的name为id的值带上去
	//            delAction:"eq/docimasyFkAction/delete.do",//删除数据的action
	            toolbar:[
	            		  { text:'保存并下发', click:function(){
	            				//更改form1的action
	            			    $("#form1").attr("action", "eq/docimasyFkAction/submit.do");  
	            			    //alert(document.getElementById("form1").action);
	            				//触发submit事件，提交表单 
	            			    $("#form1").submit(); 
							}, icon:'submit'},
	     				  { text:'保存', click:function(){
	     						//更改form1的action
	            			    $("#form1").attr("action", "eq/docimasyFkAction/save.do");  
	            			    //alert(document.getElementById("form1").action);
	            				//触发submit事件，提交表单 
	     					  $("#form1").submit();
	     					  }, icon:'save'},
	     				  { text:'关闭', click:function(){api.data.window.Qm.refreshGrid();api.close();}, icon:'cancel'}
	     				 ],
	     				
	            columns:[
	                //此部分配置同grid
	                { display:'主键', name:'id', width:50, hide:true},
	                { display:'检定设备名称', name:'equipmentName', width:150},
	                { display:'检定设备编号', name:'equipmentNumbe', width:150},
	                { display:'实际检定日期', name:'practicalTime', width:150},
	                { display:'检定单位', name:'unit', width:150},
	                { display:'检定结果', name:'result', width:150},
	                { display:'检定状态', name:'status', width:150,
	                	render:function(rowData){
	                    	if(rowData.status=="jxz"){
	                        	return "进行中";
	                    	}else if(rowData.status=="ywc"){
	                        	return "已完成";
	                    	}else if(rowData.status=="wxf"){
	                        	return "未下发";
	                    	}else if(rowData.status=="wwc"){
	                        	return "未完成";
	                    	}else if(rowData.status=="wks"){
	                        	return "未开始";
	                    	}
	                	}}
	            ]
	      
	        });
    	</script>
</body>
</html>
