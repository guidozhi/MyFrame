var inputShow = false;
var seledType = "input";
var managerShowName = "1";

$(function(){
	$('#pro_code_name').bind('input propertychange', function(e) {  
		var val = $('#pro_code_name').val();
		linkSearch(val);
	});
	$('#pro_code').bind('input propertychange', function(e) {  
		var val = $('#pro_code').val();
		linkSearch(val);
	});
	//竖标尺线
	//showBC();
	
	  document.onmousedown = function(e) { 
		 var item = e.target;
		 if($(item).parents(".linkUl").length==0){
			 $(".linkUl").hide();
		 }
	  }
})

function saveFormContent() {
	try {
		rightFrame.saveFormContent();
	} catch (e) {
		// TODO: handle exception
	}
}
function markColor() {
	rightFrame.markColor();
}
function spenFontFamilys() {
	var objS = document.getElementById("fontFamilys");
	var index = objS.selectedIndex
	var grade = objS.options[index].value;
	rightFrame.spenFontFamilys(grade);
}
function sup() {
	rightFrame.sup();
}
function sub() {
	rightFrame.sub();
}
function fontBold() {
	rightFrame.fontBold();
}
function fontItalic() {
	rightFrame.fontItalic();
}
function copyToRow() {
	var obj = rightFrame.bindInputEl;
	rightFrame.copyToRow(obj);
}
function sequence() {
	var obj = rightFrame.bindInputEl;
	var data = {};
	$.ligerDialog.open({
		title : '填写序号规则',
		width : 350,
		height : 200,
		url : "rtbox/app/templates/default/fill_sequence.jsp",
		buttons : [
				{
					text : '确定',
					onclick : function(item, dialog) {
						var data = dialog.frame.getData();
						var reg = /^[0-9]*$/;

						if (reg.test(data.initValue)) {
							rightFrame.sequence(obj, data.initValue,
									data.prefix, data.suffix);
							dialog.hide();
						} else {
							top.$.notice("初始序号必须为正整数！");
							return;
						}

					}
				}, {
					text : '取消',
					onclick : function(item, dialog) {
						dialog.hide();
					}
				} ]
	});

}

function drawMarker() {
	var obj = rightFrame.focus_element;
	rightFrame.label(obj);
}

function setLeft() {
	rightFrame.setLeft();
}
function setCenter() {
	rightFrame.setCenter();
}
function setRight() {
	rightFrame.setRight();
}
function removepclick() {
	rightFrame.removepclick();
}
function spenfontSizes(size) {
	rightFrame.spenfontSizes(size);
}
function addtr() {
	rightFrame.addtr();
}
function justified() {
	rightFrame.justified();
}

function sf() {
	try {
		var fsEl = window.rightFrame.focus_element;
		share.data("focus_element", fsEl);
		top.$.dialog({
			width : 500,
			height : 420,
			lock : false,
			title : "特殊字符",
			content : "url:rtbox/app/templates/default/font/fonts.jsp",
			data : {
				"window" : window
			}
		});
	} catch (err) {
		console.log(err.description);
	}

}

function setPropertiesShow() {
	$(".rightPrepoties").show();
}

function setPropertiesHide() {
	$(".rightPrepoties").hide();
}

/**
 * 实时显示输入框属性
 * @param e
 * @returns
 */
function showProperties(e) {
	$("#pro_name").val("");
	$("#pro_code").val("");
	$("#pro_data_div").val("");
	$("#pro_onclick").val("");
	$("#pro_onchange").val("");
	$(".pro-code").hide();
	
	if(e==null){
		return;
	}
	var $this = $(e);
	
	
	//id
	$("#pro_id").val(e.id);
	//name
	$("#pro_name").val($this.attr("name"));
	if($this.attr("values")!=undefined){
		//默认值
		$("#pro_value").val($this.attr("values"));
	}
	

	var onchange = $this.attr("onchange");
	var onclick = $this.attr("onclick");
	
	$("#pro_onclick").val(onclick);
	$("#pro_onchange").val(onchange);
	
	
	var tagName = e.tagName;
	var tagType = tagName;
	var ltype = $this.attr("ltype");
	var type = $this.attr("type");
	
	var ligerUI = $this.attr("ligerui");
	var classs = $this.attr("class");

	var ligerui = {};
	try {
		ligerui = JSON.parse(ligerUI);
		//ligerui = eval('(' + ligerUI + ')');
	} catch (e) {
		//alert(ligerui1)
	}
	if (tagName == "INPUT") {
		if (ltype != undefined) {
			if (ltype == "select") {
				var initValue = ligerui.initValue;
				if(initValue!=undefined){
					//默认值
					$("#pro_value").val(initValue);
				}
				if (ligerui.isMultiSelect == true
						|| ligerui.isMultiSelect == "true") {
					tagType = "SELECT2";
				} else {
					tagType = "SELECT";
				}
			} else if (ltype == "date") {
				tagType = "DATE";

			}else if (type == "radio") {
				tagType = "RADIO";
			} else if (type == "checkbox") {
				tagType = "CHECKBOX";
			}
		} else {
			if (type == "radio") {
				tagType = "RADIO";
			} else if (type == "checkbox") {
				tagType = "CHECKBOX";
			}
		}
	} else if (tagName == "TEXTATEA") {
		tagType = "TEXTATEA";
	} else if (tagName == "DIV") {
		
		if(classs.indexOf("uploadPhoto")!=-1){
			//图片
			tagType = "PICTRUE";
		}else{
			if(classs.indexOf("checkboxDiv")!=-1){
				//复选框
				tagType = "CHECKBOXDIV";
			}
		}
	}

	//输入框标签
	$("#pro_tag").val(tagType);
	
	if(ltype=="select"||ltype=="checkBox"){
		$(".select input").removeAttr("disabled");
		$(".select textarea").removeAttr("disabled");
		$("#pro_data_div").show();
		$("#pro_code").show();
	}else{
	
		$(".select input").attr("disabled","disabled");
		$(".select textarea").attr("disabled","disabled");
	}
	
	if (ligerui.code != undefined) {
		
		$("#pro_code").val(ligerui.code);
		$(".pro-code").show();
		$("#pro_data_div").hide();
		selectCodeTable(ligerui.code,"");
	}else if (ligerui.data != undefined) {
		
		$("#pro_data_div").val(JSON.stringify(ligerui.data));
		$(".pro-code").hide();
	}
	
	//输入框类型
	//$("#pro_type").val($this.attr("ltype")); 

}

/**
 * 处理属性是否能够修改
 * @returns
 */
function propertySignleConfig(flag){
	if(flag){
		//显示单选能够处理的属性
		$(".rightPrepoties .single input").removeAttr("disabled");
		$(".rightPrepoties .single textarea").removeAttr("disabled");
		$(".rightPrepoties .single select").removeAttr("disabled");

		$(".select input").attr("disabled","disabled");
		$(".select textarea").attr("disabled","disabled");
		
	}else{
		//影藏只有单选能够处理的属性
		$(".rightPrepoties .single input").attr("disabled","disabled");
		$(".rightPrepoties .single textarea").attr("disabled","disabled");
		$(".rightPrepoties .single select").attr("disabled","disabled");
		$(".select input").removeAttr("disabled");
		$(".select textarea").removeAttr("disabled");
	}
}


/**
 * 修改数据库关联基础信息类型
 * @param val
 * @returns
 */
function changeDbLink(val) {
	if ("inspect" == val) {
		//检验业务
		var html = createSelectByJson(inspectNamed, "pro_db_link");
		$("#pro_db_link").remove();
		$("#pro_db_lable").after(html);
	} else if ("device" == val) {
		//设备基础信息
		var html = createSelectByJson(deviceNamed, "pro_db_link");
		$("#pro_db_link").remove();
		$("#pro_db_lable").after(html);
	} else if ("boiler" == val) {
		//锅炉信息
		var html = createSelectByJson(boilerNamed, "pro_db_link");
		$("#pro_db_link").remove();
		$("#pro_db_lable").after(html);
	} else if ("elevator" == val) {
		//电梯信息
		var html = createSelectByJson(elevatorNamed, "pro_db_link");
		$("#pro_db_link").remove();
		$("#pro_db_lable").after(html);
	} else if ("crane" == val) {
		//起重机信息
		var html = createSelectByJson(craneNamed, "pro_db_link");
		$("#pro_db_link").remove();
		$("#pro_db_lable").after(html);
	} else if ("accessory" == val) {
		//安全阀信息
		var html = createSelectByJson(accessoryNamed, "pro_db_link");
		$("#pro_db_link").remove();
		$("#pro_db_lable").after(html);
	} else if ("pressurevessels" == val) {
		//压力容器信息
		var html = createSelectByJson(pressurevesselsNamed, "pro_db_link");
		$("#pro_db_link").remove();
		$("#pro_db_lable").after(html);
	} else if ("engine" == val) {
		//厂车信息
		var html = createSelectByJson(engineNamed, "pro_db_link");
		$("#pro_db_link").remove();
		$("#pro_db_lable").after(html);
	} else if ("rides" == val) {
		//游乐设施信息
		var html = createSelectByJson(ridesNamed, "pro_db_link");
		$("#pro_db_link").remove();
		$("#pro_db_lable").after(html);
	} else if ("pipeline" == val) {
		//压力管道信息
		var html = createSelectByJson(pipelineNamed, "pro_db_link");
		$("#pro_db_link").remove();
		$("#pro_db_lable").after(html);
	} else if ("kysd" == val) {
		//客运索道信息
		var html = createSelectByJson(kysdNamed, "pro_db_link");
		$("#pro_db_link").remove();
		$("#pro_db_lable").after(html);
	}

}

/**
 * 动态改变关联命名下拉
 * @param namedJson
 * @param id
 * @returns
 */
function createSelectByJson(namedJson, id) {
	var html = '<select style="width:70%;" id="' + id
			+ '" onchange="changeSelectNamed(this.value)">';

	html = html + '<option value=""></option>';
	for ( var key in namedJson) {
		html = html + '<option value="' + key + '">' + namedJson[key]
				+ '</option>';
	}

	html = html + '</select>';

	return html;
}

/**
 * 修改选择配置命名
 * @param val
 * @returns
 */
function changeSelectNamed(val) {
	if (val == "") {
		return;
	}
	$("#pro_name").val("base__" + val);
	$("#pro_id").val("base__" + val);
	rightFrame.setinputFocusName(val)

}

/**
 * 获取控件位置
 * @param e
 * @returns
 */
function getIE(e) {
	var t = e.offsetTop;
	var l = e.offsetLeft;
	var width = e.offsetWidth;
	while (e = e.offsetParent) {
		t += e.offsetTop;
		l += e.offsetLeft;
	}
	
	var data = {};
	data["top"] = t;
	data["left"] = l;
	data["width"] = width;
	//alert("top=" + t + "/nleft=" + l);
	return data;
	
}

/**
 * 修改文本框类型
 * @param val
 * @returns
 */
function changeTag(val){
	if(val=="SELECT"||val=="SELECT2"||val=="CHECKBOX"){
		$(".select").show();
	}else{
		$(".select").hide();
	}
	
	rightFrame.changeTag(val);
	
}

/**
 * 显示码表下拉列表
 * @param e
 * @returns
 */
function linkSearchShow(e){
	var p = getIE(e);
	$("#linkSearch").css("top",p.top+20);
	$("#linkSearch").css("left",p.left);
	$("#linkSearch").css("width",p.width);
	$("#linkSearch").show();
}

function showDictData(e){
	 var evt = window.event || e; 
	 var _this = evt.srcElement || evt.target; 
	 if (evt.keyCode == 13){
	    var sql =$(_this).val();
	    if("" == sql)
	    {
	    	return;
	    }
	    $.post(
	    		"baseConfigAction/getCodeBySql.do",
	    		{"sql":sql},
	    		function(res){
					if(res.success){
						var data = res.data;
						var dict = [];
						$.each(data,function(i){
							dict.push({"id":data[i].ID,"text":data[i].TEXT});
						});
						
						$("#pro_data_div").val(JSON.stringify(dict));
						//changeBtnSelectBoxCss();
					}
	    		}
		);
	 }
}

/**
 * 动态根据输入查询码表选择
 * @param val
 * @returns
 */
function linkSearch(val){
	if(val==""){
		$(".pro-code").hide();
		$(".pro-data").show();
		$("#pro_code_name").val("");
		$("#pro_code").val("");
		rightFrame.changeInputCodeConfig(val);
	}
	$.get("baseConfigAction/getCodeTable.do?name="+val,function(res){
		if(res.success){
			var data = res.data;
			var html = "";
			var l = data.length;
			if(data.length>5){
				l = 5;
			}
			for (var i = 0; i < l; i++) {
				var nn = data[i];
				html = html + '<li> <a id="itemCode'+i+' value="'+nn.id+
				'" onclick="selectCodeTable(\''+nn.id+'\',\''+nn.text+'\')">'
				+nn.text+'</a><li>';
			}
			$("#linkSearch").html(html);
			changeBtnSelectBoxCss();
		}
	})
}


/**
 * 显示选择的码表的具体内容
 * @param code
 * @param name
 * @returns
 */
function selectCodeTable(code,name){
	$("#pro_code_name").val(name);
	$("#pro_code").val(code);
	$("#linkSearch").hide();
	rightFrame.changeInputCodeConfig(code);
	$.get("baseConfigAction/getCodeTableValues.do?code="+code,function(res){
		if(res.success){
			var data = res.data;
			$(".pro-code").show();
			$(".pro-data").hide();
			$("#pro_code_div").val(JSON.stringify(data));
		}
	})
}

/**
 * 预览录入效果
 * @returns
 */
function showInput(flag){
	var pageCode = '1';
	if(actionNode!=undefined){
		pageCode = actionNode.data.code;
	}
	if(!inputShow&&flag==undefined){
		inputShow = true;
		var src = basePathUrl+"/rtbox/app/templates/show_right.jsp?code="+rt_code+"&pageCode="+pageCode;
		if (src.indexOf("?") > 0) {
			if(!relColumn||relColumn.length<=0){
				src = src + '&fk_report_id=' + fk_report_id;
			}else{
				src = src + '&' + relColumn;
			}
			
		} else {
			if(!relColumn||relColumn.length<=0){
				src = src + '?fk_report_id=' + fk_report_id;
			}else{
				src = src + '?' + relColumn;
			}
		}
		
		if(input!=""){
			src = src + '&input=' + input;
		}
		src = src +'&' + relColumn;
		
		if(pageStatus!=""){
			src = src + '&pageStatus=' + pageStatus;
		}
		if(rtPageId!=""){
			src = src + '&rtPageId='+rtPageId;
		}
		$("#rightFrame").attr("src", src);
		
	}else{
		if(flag==undefined){
			inputShow = false;
		}
		
		var src = basePath+"show_right.jsp?code="+rt_code+"&pageCode="+pageCode;
		if (src.indexOf("?") > 0) {
			if(!relColumn||relColumn.length<=0){
				src = src + '&fk_report_id=' + fk_report_id;
			}else{
				src = src + '&' + relColumn;
			}
			
		} else {
			if(!relColumn||relColumn.length<=0){
				src = src + '?fk_report_id=' + fk_report_id;
			}else{
				src = src + '?' + relColumn;
			}
		}
		
		if(input!=""){
			src = src + '&input=' + input;
		}
		src = src +'&' + relColumn;
		
		if(pageStatus!=""){
			src = src + '&pageStatus=' + pageStatus;
		}
		if(rtPageId!=""){
			src = src + '&rtPageId='+rtPageId;
		}
		$("#rightFrame").attr("src", src);
		
	}
	
}

/**
 * 改变选中筛选条件
 * @param type
 * @returns
 */
function changeSelectSeledType(type){
	seledType = type;
	if(type=="input"){
		$("#selectTypeBtn").find("img").attr("title","框选输入框");
	}else if(type=="span"){
		$("#selectTypeBtn").find("img").attr("title","框选文本");
	}else if(type=="td"){
		$("#selectTypeBtn").find("img").attr("title","框选单元格");
	}
	hidelinkBtnSelect();
	rightFrame.changeSelectSeledType(type);
}


/**
 * 改变页面方向
 * @returns
 */
function changeScreen(){
	var div = $(".cont_center");
	if(div.length>0){
		$(div).attr("class","cont_center_horizontal");
		$(div).find("iframe").css("height","210mm");
		rightFrame.setHorizontal(true);
	}else{
		$(".cont_center_horizontal").attr("class","cont_center");
		$(".cont_center_horizontal").find("iframe").css("height","297mm");
		rightFrame.setHorizontal(false);
	}
}

//关闭属性窗口
function propertiesWindow(){
	var value = $("#propertiesWindow").attr("value");
	if(value=="0"){
		$(".rightPrepoties").show();
		$("#content_wrapper").css("margin-right","280px");
		$("#propertiesWindow").attr("value","1")
	}else{
		$(".rightPrepoties").hide();
		$("#content_wrapper").css("margin-right","0px");
		$("#propertiesWindow").attr("value","0")
	}
	
}

function leftTreeWindow(){
	var value = $("#leftTreeWindow").attr("value");
	if(value=="0"){
		$("#sidebar_left").show();
		$("#content_wrapper").css("margin-left","250px");
		$("#leftTreeWindow").attr("value","1")
	}else{
		$("#sidebar_left").hide();
		$("#content_wrapper").css("margin-left","0px");
		$("#leftTreeWindow").attr("value","0");
	}
}

/**
 * 竖标尺线，做得太奇葩了
 * @returns
 */
function showBC(){
	//var height = $("#content_wrapper").css("height");
	var height = 1200;
	$("#content_wrapper").append('<div id="bc" style="position: absolute;top:0px;background-color: white;"></div>')
	var bcline = '<div id="bcline" style="position: absolute;top:-1px;height:0px;width: 0px;border: dashed 1px black;"></div>';
	$("#content_wrapper").append(bcline);
	
	var i = 50;
	var num = 0;
	height = height-50;
	while(height>0){
		num++;
		if(i==50){
			$("#bc").append('<div style="margin-top:50px;height:20px;">--'+num+'</div>');
		}else if(i>50){
			$("#bc").append('<div style="height:20px;">--'+num+'</div>');
		}
		i = i+20;
		height = height-20;
	}
	
	var selDat = getIE(document.getElementById("content_wrapper"));
    var startX = selDat.left;
    var startY = selDat.top;
    var selDiv = document.getElementById("bcline");
document.onmousemove = function() { 

    evt = window.event || arguments[0]; 
 
   
   
      if (selDiv.style.display == "none") { 

        selDiv.style.display = ""; 

      } 

      _x = (evt.x || evt.clientX); 

      _y = (evt.y || evt.clientY); 

      /*selDiv.style.left = Math.min(_x, startX) + "px"; 

      selDiv.style.top = Math.min(_y, startY) + "px"; */

      selDiv.style.width = Math.abs(_x - startX) + "px"; 

      selDiv.style.height = Math.abs(_y - startY) + "px"; 

}

}



/**
 * 显示按钮下拉框
 * @param btnId
 * @param btnType
 * @returns
 */
function showBtnSelectBox(btnId,btnType){
	
	var btn = document.getElementById(btnId);
	var p = getIE(btn);
	if(btnType=="select"){
		//修改框选类型
		$("#linkHeadBtn").html("");
		var html = '<li><a href="javascript:changeSelectSeledType(\'input\')">输入框</a><li>'
			+'<li><a href="javascript:changeSelectSeledType(\'span\')">文本</a><li>'
			+'<li><a href="javascript:changeSelectSeledType(\'td\')">单元格</a><li>';
		$("#linkHeadBtn").html(html);
		$("#linkHeadBtn").css("top",p.top+20);
		$("#linkHeadBtn").css("left",p.left);
		$("#linkHeadBtn").css("width",'50px');
		changeBtnSelectBoxCss();
		$("#linkHeadBtn").show();
	}
	
}


/**
 * 关闭按钮下拉框
 * @returns
 */
function hidelinkBtnSelect(){
	$("#linkHeadBtn").hide();
}

/**
 * 选项鼠标悬浮事件
 * @returns
 */
function changeBtnSelectBoxCss(){
	$(".linkUl li").hover(function(){
		//mouseover就写这里面
		$(this).css({"background-color":"#F5F5DC"});
		},function(){
		//mouseout就写这里面
		$(this).css({"background-color":"#FCFCFC"});
	})
	
}

//设置行高（主要用于批量方便点）
function setTrHeight() {  
    $.ligerDialog.prompt("行高：？mm", function (yes, value) { 
    	if (yes){
    		//top.$.dialog.notice({content:'value'});
    		rightFrame.setTrHeight(value+"mm");
    	}
    		 
    })
} 

/**
 * 输入框命名显示控制
 * @returns
 */
function managerShowName(){
	var managerShowName = $("#managerShowName").attr("value");
	if(value=="0"){
		$("#leftTreeWindow").attr("value","1")
	}else{
		$("#leftTreeWindow").attr("value","0");
	}
	rightFrame.managerShowName();
}

/**
 * 切换页需要的操作
 * @returns
 */
function changePage(){
	managerShowName = "1";
	inputShow = false;
}

function setSeledIds(ids){
	$("#select_data_div").val(ids)
}

//源代码编辑
function sourceEdit(){
	if(actionNode){
		pageCode = actionNode.data.code;
	}
	var url = "rtbox/app/sourceEdit/tpl_index.jsp?rtPageId=" + rtPageId + "&pageCode=" + pageCode;
	var isEdit = $("#sourceEdit").attr("value");

	if(isEdit == "0"){
		// 参考propertiesWindow()
		$("#propertiesWindow").attr("value","1");
		propertiesWindow();
		$(".cont_center").css("width","95%");
		$("#sourceEdit").attr("value","1");
		$("#rightFrame").attr("src", url);
	}else{
		$(".cont_center").css("width","");
		$("#propertiesWindow").attr("value","0");
		propertiesWindow();
		showInput(true);
		$("#sourceEdit").attr("value","0");
		openRt(pageCode);
	}
	
}
