<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head pageStatus="${param.pageStatus}">
    <title></title>
    <!-- 每个页面引入，页面编码、引入js，页面标题 -->
    <%@include file="/k/kui-base-form.jsp" %>

    <script type="text/javascript">
    var pageStatus="${param.pageStatus}";
    $(function () {
    	if("add"==pageStatus || "modify"==pageStatus){
			tbar=[
				{text: "保存", id: 'save', icon: "save", click: saveInfo},
				{text: "关闭", id: 'close', icon: "cancel", click: function(){api.close();}}
			];
		}else{
			tbar=[
				{text: "关闭", id: 'close', icon: "cancel", click: function(){api.close();}}
			];
		}
    	$("#form1").initForm({    //参数设置示例
    		success: function (res) {
    			if(res.success){
    				top.$.dialog.notice({
                 		content: "保存成功！"
                	});		
    				api.data.window.Qm.refreshGrid();
    				api.close();
    			}else{
    				$.ligerDialog.error('保存失败！<br/>' + res.msg);
    			}
    		},
    		afterParse:function(formObj){//form表单完成渲染后，回调函数
           	 	if(api.data.fk_car_id){
           	 		$("#fkCarId").val(api.data.fk_car_id);
           	 		$("#carNum").val(api.data.car_num);
           	 	}
    		},
			showToolbar: true,
            toolbarPosition: "bottom",
            toolbar: tbar
    	});
    });
  	//保存
	function saveInfo(){
  		var url = "carInsDetailAction/saveInfo.do?pageStatus=${param.pageStatus}";;
  		if("${param.opType}" == "history"){
  			url = "carInsDetailAction/saveHistoryInfo.do?pageStatus=${param.pageStatus}";
  		}
    	if ($("#form1").validate().form()) {
    		$("#save").attr("disabled","disabled");
    		var formData = $("#form1").getValues();
    		var data = {};
			data = formData;
			$("body").mask("正在保存数据，请稍后！");
			$.ajax({
				url: url,
				type: "POST",
			 	datatype: "json",
			 	contentType: "application/json; charset=utf-8",
			 	data: $.ligerui.toJSON(formData),
			  	success: function (resp) {
			   		$("body").unmask();
			      	if (resp["success"]) {
			       		if(api.data.window.Qm){
			                api.data.window.Qm.refreshGrid();
			   			}
			         	top.$.dialog.notice({content:'保存成功！'});
			     		api.close();
			     	}else{
			      		$.ligerDialog.error(resp.msg);
			      		$("#save").removeAttr("disabled");
			    	}
			  	},
				error: function (resp) {
			   		$("body").unmask();
					$.ligerDialog.error(resp.msg);
					$("#save").removeAttr("disabled");
				}
			});
    	}else{
    		$.ligerDialog.error('提示：' + '请将信息填写完整后保存！');
    	}
	}
  	
	function SumAmount() {//求和商业险总计+交强险+车船税=单车保费总计
	    var money1 = document.getElementById("insSyxzj").value;
	    var money2 = document.getElementById("insJqx").value;
	    var money3 = document.getElementById("insCcx").value;
	    var total = document.getElementById("insDcbxzj");
	    var sumAmount = parseFloat(money1 == "" ? 0 : money1) + parseFloat(money2 == "" ? 0 : money2) + parseFloat
	(money3 == "" ? 0 : money3) ;
	    total.value = sumAmount == 0 ? "" : sumAmount.toFixed(2); 
	}
    </script>
</head>
<body>
    <form id="form1" action="carInsDetailAction/saveInfo.do?pageStatus=${param.pageStatus}" getAction="carInsDetailAction/detail.do?id=${param.id}">
    	<input type="hidden" id="id" name="id"/>
    	<input type="hidden" id="fkCarId" name="fkCarId"/>
    	<input type="hidden" id="insEndDate" name="insEndDate"/>
    	<input type="hidden" id="createUserId" name="createUserId"/>
        <input type="hidden" id="createUserName" name="createUserName"/>
        <input type="hidden" id="createDate" name="createDate"/>
        <input type="hidden" id="lastModifyUserId" name="lastModifyUserId"/>
        <input type="hidden" id="lastModifyUserName" name="lastModifyUserName"/>
        <input type="hidden" id="lastModifyDate" name="lastModifyDate"/>
    	<input type="hidden" id="dataStatus" name="dataStatus" value="0"/>
       <table border="1" cellpadding="3" cellspacing="0" width="" class="l-detail-table">
       <tr> 
        <td class="l-t-td-left"> 车牌号</td>
        <td class="l-t-td-right"> 
        <input id="carNum" name="carNum" type="text" ltype='text' validate="{required:true,maxlength:32}" readonly="readonly" unselectable='on'/>
        </td>
         <td class="l-t-td-left"> 保险起期</td>
        <td class="l-t-td-right"> 
        <input id="insStartDate" name="insStartDate" type="text" ltype='date' ligerui="{format:'yyyy-MM-dd'}"  validate="{required:true}}"/>
        </td>
       </tr>
       <tr>
        <td class="l-t-td-left"> 承保单位</td>
        <td class="l-t-td-right"> 
        <input id="insComName" name="insComName" type="text" ltype='text' validate="{required:true,maxlength:100}"/>
        </td> 
        <td class="l-t-td-left"> 投保单位</td>
        <td class="l-t-td-right"> 
        <input id="coverInsComName" name="coverInsComName" type="text" ltype='text' validate="{required:true,maxlength:100}" value="四川省特种设备检验研究院"/>
        </td>
       </tr>
       <tr> 
        <td class="l-t-td-left"> 车辆损失</td>
        <td class="l-t-td-right"> 
        <input id="insClss" name="insClss" type="text" ltype='text' validate="{required:false,maxlength:22}"/>
        </td>
        <td class="l-t-td-left"> 第三者责任保额</td>
        <td class="l-t-td-right"> 
        <input id="insDszrBe" name="insDszrBe" type="text" ltype='text' validate="{required:false,maxlength:22}"/>
        </td>
         </tr>
       <tr> 
        <td class="l-t-td-left"> 第三者责任金额</td>
        <td class="l-t-td-right"> 
        <input id="insDszrJe" name="insDszrJe" type="text" ltype='text' validate="{required:false,maxlength:22}"/>
        </td>
        <td class="l-t-td-left"> 全车盗抢险</td>
        <td class="l-t-td-right"> 
        <input id="insQcdqx" name="insQcdqx" type="text" ltype='text' validate="{required:false,maxlength:22}"/>
        </td>
         </tr>
       <tr> 
        <td class="l-t-td-left"> 司机座位责任保额</td>
        <td class="l-t-td-right"> 
        <input id="insSjzwzrBe" name="insSjzwzrBe" type="text" ltype='text' validate="{required:false,maxlength:22}"/>
        </td>
        <td class="l-t-td-left"> 司机座位责任金额</td>
        <td class="l-t-td-right"> 
        <input id="insSjzwzrJe" name="insSjzwzrJe" type="text" ltype='text' validate="{required:false,maxlength:22}"/>
        </td>
        </tr>
       <tr> 
        <td class="l-t-td-left"> 乘客座位责任保额</td>
        <td class="l-t-td-right"> 
        <input id="insCkzwzrBe" name="insCkzwzrBe" type="text" ltype='text' validate="{required:false,maxlength:22}"/>
        </td>
        <td class="l-t-td-left"> 乘客座位责任金额</td>
        <td class="l-t-td-right"> 
        <input id="insCkzwzrJe" name="insCkzwzrJe" type="text" ltype='text' validate="{required:false,maxlength:22}"/>
        </td>
        </tr>
       <tr> 
        <td class="l-t-td-left"> 玻璃险</td>
        <td class="l-t-td-right"> 
        <input id="insBlx" name="insBlx" type="text" ltype='text' validate="{required:false,maxlength:22}"/>
        </td>
        <td class="l-t-td-left"> 自然险</td>
        <td class="l-t-td-right"> 
        <input id="insZrx" name="insZrx" type="text" ltype='text' validate="{required:false,maxlength:22}"/>
        </td>
        </tr>
       <tr> 
        <td class="l-t-td-left"> 涉水险</td>
        <td class="l-t-td-right"> 
        <input id="insSsx" name="insSsx" type="text" ltype='text' validate="{required:false,maxlength:22}"/>
        </td>
        <td class="l-t-td-left"> 无法找到第三方特<br />	约险</td>
        <td class="l-t-td-right"> 
        <input id="insWfzddsftyx" name="insWfzddsftyx" type="text" ltype='text' validate="{required:false,maxlength:22}"/>
        </td>
        </tr>
		  <tr> 
        <td class="l-t-td-left"> 划痕险</td>
        <td class="l-t-td-right"> 
        <input id="insHhx" name="insHhx" type="text" ltype='text' validate="{required:false,maxlength:22}"/>
        </td>
        </tr>
       <tr> 
        <td class="l-t-td-left"> 专修厂特约险</td>
        <td class="l-t-td-right"> 
        <input id="insZxctyx" name="insZxctyx" type="text" ltype='text' validate="{required:false,maxlength:22}"/>
        </td>
        <td class="l-t-td-left"> 车损险不计免赔</td>
        <td class="l-t-td-right"> 
        <input id="insCsxbjmp" name="insCsxbjmp" type="text" ltype='text' validate="{required:false,maxlength:22}"/>
        </td>
        </tr>
       <tr> 
        <td class="l-t-td-left"> 三者险不计免赔</td>
        <td class="l-t-td-right"> 
        <input id="insSzxbjmp" name="insSzxbjmp" type="text" ltype='text' validate="{required:false,maxlength:22}"/>
        </td>
        <td class="l-t-td-left"> 盗抢险不计免赔</td>
        <td class="l-t-td-right"> 
        <input id="insDqxbjmp" name="insDqxbjmp" type="text" ltype='text' validate="{required:false,maxlength:22}"/>
        </td>
        </tr>
       <tr> 
        <td class="l-t-td-left"> 座位险不计免赔</td>
        <td class="l-t-td-right"> 
        <input id="insZwxnjmp" name="insZwxnjmp" type="text" ltype='text' validate="{required:false,maxlength:22}"/>
        </td>
        <td class="l-t-td-left"> 自然险不计免赔</td>
        <td class="l-t-td-right"> 
        <input id="insZrxbjmp" name="insZrxbjmp" type="text" ltype='text' validate="{required:false,maxlength:22}"/>
        </td>
        </tr>
       <tr> 
        <td class="l-t-td-left"> 涉水险不计免赔</td>
        <td class="l-t-td-right"> 
        <input id="insSsxbjmp" name="insSsxbjmp" type="text" ltype='text' validate="{required:false,maxlength:22}"/>
        </td>
        <td class="l-t-td-left"> 商业险总计</td>
        <td class="l-t-td-right"> 
        <input id="insSyxzj" name="insSyxzj" type="text" ltype='text' validate="{required:true,maxlength:22}" onkeyup="SumAmount();"/>
        </td>
        </tr>
       <tr> 
        <td class="l-t-td-left"> 交强险</td>
        <td class="l-t-td-right"> 
        <input id="insJqx" name="insJqx" type="text" ltype='text' validate="{required:true,maxlength:22}" onkeyup="SumAmount();"/>
        </td>
        <td class="l-t-td-left"> 车船税</td>
        <td class="l-t-td-right"> 
        <input id="insCcx" name="insCcx" type="text" ltype='text' validate="{required:true,maxlength:22}" onkeyup="SumAmount();"/>
        </td>
        </tr>
       <tr> 
        <td class="l-t-td-left"> 单车保费总计</td>
        <td class="l-t-td-right"> 
        <input id="insDcbxzj" name="insDcbxzj" type="text" ltype='text' validate="{required:true,maxlength:22}" />
        </td>
       	<td class="l-t-td-left"> 收款单位户名</td>
        <td class="l-t-td-right"> 
        <input id="collectComeName" name="collectComeName" type="text" ltype='text' validate="{required:false,maxlength:100}"/>
        </td>
       </tr>
       <tr> 
        <td class="l-t-td-left"> 开户行</td>
        <td class="l-t-td-right"> 
        <input id="bankOfDiposit" name="bankOfDiposit" type="text" ltype='text' validate="{required:false,maxlength:100}"/>
        </td>
        <td class="l-t-td-left"> 账号</td>
        <td class="l-t-td-right"> 
        <input id="account" name="account" type="text" ltype='text' validate="{required:false,maxlength:32}"/>
        </td>
       </tr>
       <tr> 
       <td class="l-t-td-left"> 经纪联系人</td>
        <td class="l-t-td-right"> 
        <input id="insContacts" name="insContacts" type="text" ltype='text' validate="{required:false,maxlength:32}"/>
        </td>
        <td class="l-t-td-left"> 经纪联系人电话</td>
        <td class="l-t-td-right"> 
        <input id="insContactsTel" name="insContactsTel" type="text" ltype='text' validate="{required:false,maxlength:32}"/>
        </td>
       </tr>
      </table>
    </form> 
</body>
</html>
