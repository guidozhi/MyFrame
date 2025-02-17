function selectOrgUser(options) {
	var cfg = {
		type : options.type || 1,
		checkbox : options.checkbox || 0,
		code :options.code || "",
		name :options.name || "",
		callback: options.callback || function(){}
	};
	selectUnitOrUser(cfg.type, cfg.checkbox, cfg.code, cfg.name, cfg.callback);
}
/**
 * 参数说明：
 * 参数3、4、5都是可选，但是【3、4】和【5】必须存在一个，否则页面获取不到选择结果。
 * 
 * 参数1：[0]选择本单位及以下机构]，[00]选择全部范围内机构，[000]选择本部门及以下；
         [1]选系统全范围人员、[10]选本单位内人员、[11]选择本部门内人员
         [2]选择角色；
         [3]选择本单位及以下机构和人，[33]选择全部范围内的机构和人，[30]只选择本部门及以下机构r；
         [4]岗位选择本单位， [44]选择本部门岗位
 * 参数2：0.单选；1.多选;
 * 参数3：要填充的code字段控件id，可为空;
 * 参数4：要填充的name字段控件id，可为空；
 * 参数5：选择完成回调方法，可为空，回调参数为选择的结果：callback(selectResult)
 * isAsyn:单位部门选择是否采用异步方式
 */
function selectUnitOrUser(type, isCheckBox, code, name,ids, callback,isAsyn) {
	var title = "人员选择";
	var url = "url:pub/selectUser/org_user_select.jsp?type=" + type
	var width = 1000;
	if (type == "0" || type == "00" || type=="000") {
		title = "单位/部门选择";
		url = "url:pub/selectUser/org_select.jsp?type=" + type + "&_r="+Math.random();
		try {
			if(isAsyn=="1"){
				url = "url:pub/selectUser/org_select_asyn.jsp?type=" + type + "&_r="+Math.random();
			}
		} catch (e) {
		}
		width = 350;
	}
	else if (type == "2") {
		title = "系统角色选择";
		url = "url:pub/selectUser/role_select.jsp?_r="+Math.random();
		width = 600;
	}
	else if (type == "3" || type == "33" || type == "30") {
		title = "单位/部门和人员选择";
		url = "url:pub/selectUser/org_user_select.jsp?chooseOrg=1&type=" + type + "&_r="+Math.random();
	}else if(type=="4" || type == "44"){
		title = "岗位选择";
		url = "url:pub/selectUser/org_position_select.jsp?type="+type+"&_r="+Math.random();
	}else if(type=="444"){
		title = "岗位选择";
		url = "url:pub/selectUser/org_position_select.jsp?position=1&type="+type+"&_r="+Math.random();
	}
	url += "&checkbox=" + isCheckBox + "&fieldName=" + name + "&fieldId=" + code;	
	top.$.dialog({
		width : width,
		height : 500,
		lock : true,
		parent : api,
		id : "selectUnitOrUser",
		title : title,
		content : url,
		cancel: true,
		ok : function() {
			var datas = this.iframe.contentWindow.getSelectResult();
			if(datas){
				if(code)
					//$("#" + code).val(datas.code);
				//alert(datas.code+"---"+ids)
				//资源分享
				$.post("resourceInfo/resourceShare.do",{id:ids,userId:datas.code},function(res){
							if(res.success){
								top.$.notice("分享成功！");
								api.close();
							}else{
								$.ligerDialog.error(res.msg);
							}
							
						})
				
				return true;
				//
				if(name)
					$("#" + name).val(datas.name)
				if(callback) 
					callback(datas);
				
			}
			else return false;
		}
	});
}