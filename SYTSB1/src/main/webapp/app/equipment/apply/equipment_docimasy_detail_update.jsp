
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
    	if( "${param.pageStatus}"=="modify"){
    		
 		   document.getElementById('jdjg').style.display = "none";
 	}
//      如果不设置额外参数，不用调用此方法，初始化时会自动调用
        $("#form").initForm({
            success : function(responseText) {//处理成功
                if (responseText.success) {
                    top.$.dialog.notice({content:'保存成功'});
                    api.data.window.Qm.refreshGrid();
                    
                    $("#id").val(responseText.data.id);
                    api.close();
                } else {
                    $.ligerDialog.error('保存失败' + responseText);
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

    });
    
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
    <form id="form" action="eq/docimasyFkAction/save.do" method="get" getAction="eq/docimasyFkAction/detail.do?id=${param.id}" >
        <input type="hidden" id="id" name="id"/>
        <input type="hidden" id="equipmentTime" name="equipmentTime" />
        <input type="hidden" id="practicalTime" name="practicalTime" />
        <input type="hidden" id="result" name="result" />
        <input type="hidden" id="checkOpId" name="checkOpId"/>
        <input type="hidden" id="status" name="status" />
        <input type="hidden" id="createTime" name="createTime" />
        <input type="hidden" id="createName" name="createName" />
        <input type="hidden" id="createId" name="createId" />
        <table border="1" cellpadding="3" cellspacing="0" width="" class="l-detail-table">
      <tr> 
        <td class="l-t-td-left"> 检定设备名称</td>
        <td class="l-t-td-right"> 
        <input name="equipmentId" id="equipmentId" type="hidden" />
        <input name="equipmentName"   id="equipmentName" type="text" ltype='text' validate="{required:true,maxlength:100}" validate="{required:true,maxlength:32}" onclick="selEquipment()" ligerui="{value:'',iconItems:[{icon:'add',click:function(){selEquipment()}}]}"/>
        </td>
         <td class="l-t-td-left"> 内部编号</td>
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
	        <input ligerUi="{disabled:true}"  name="period" type="text" ltype='text' />
        </td>
           <td class="l-t-td-left"> 计划日期</td>
	        <td class="l-t-td-right"> 
	        <input ligerUi="{disabled:true}"  id="nextTime" name="nextTime" type="text" ltype="date" ligerui="{initValue:'',format:'yyyy-MM-dd'}"  onchange="setValues(this.value,'advance_time')" />
	        </td>
       </tr>
       <tr> 
	        <td class="l-t-td-left"> 送检时间</td>
	        <td class="l-t-td-right"> 
	        <input validate="{required:true,maxlength:32}" name="equipmentTime"type="text" ltype="date" validate="{required:false}"ligerui="{initValue:'',format:'yyyy-MM-dd'}"  onchange="setValues(this.value,'advance_time')"/>
	        </td>
	        <td class="l-t-td-left"> 检定单位</td>
        <td class="l-t-td-right"> 
        <input id="unit"  name="unit" validate="required:true" type="text" ltype='text' ligerui="{value:'',iconItems:[{icon:'org',click:function(){selectUnit()}}]}"/>
        </td>
       </tr>
       <tr> 
	        <td class="l-t-td-left">送检责任人</td>
			<td class="l-t-td-right" id="reader_td" colspan="3" >
			<input type="hidden" name="checkOp" id="checkOp"  validate="{required:true}"/>
				 <c:if test="${param.pageStatus=='modify' or param.pageStatus=='add'}"><span class="l-button label" title="送检责任人">
	                          <span  class="l-a l-icon-add"  onclick="selectReaders();">&nbsp;</span>
	                         </c:if>
			
			</td>
       </tr>
        <%--  <tr> 
            <td class="l-t-td-left"> 实际检定日期</td>
	        <td class="l-t-td-right"> 
	          <input validate="{required:true,maxlength:32}" name="practicalTime"type="text" ltype="date" validate="{required:false}"ligerui="{initValue:'',format:'yyyy-MM-dd'}"  onchange="setValues(this.value,'advance_time')"/>
	        </td>
	        <td class="l-t-td-left"> 检定状态 </td>
	        <td class="l-t-td-right"> 
	         <u:combo  validate="{required:true,maxlength:32}"  name="status" code="TJY2_DOCIMASY_STATUS" />
	        </td>
       </tr> --%>
       <tr id="jdjg"> 
       
        <td class="l-t-td-left"> 检定结果</td>
        <td colspan="3" class="l-t-td-right"> 
        <textarea name="remark" rows="4" type="text" ltype='text' ></textarea>
        </td>
       </tr>
      </table>
    </form>
</body>
</html>


