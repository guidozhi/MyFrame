var paramss = GetRequest();
var saveResult = true;
var codeTable = {};
var focus_element;
$(document).ready(  
        function() {  
        	if(input=="1"){
//自定义校验规则
	        	$('input').bind('keydown',function(event){
	        	    if(event.keyCode == "13") {
	        	    	var id = $("#inputFocus").val();
	        	    	
	        	    		change($("#"+id)[0]);
	        	    	}
	        	   
	        	}); 
	        	addValidateMethod();
	        	
	        	$("#formObj").validate();
	        	$.metadata.setType("attr", "validate");
        	}
        	//alert(input)
        	if(input==""){
        		//$("form").css("height","900px");
        		//$("form").css("overflow","auto");
         		//非录入状态
         		$("body").keydown(function(e){
         			var ev = window.event || e;
         			var code = ev.keyCode || ev.which;		
         			try{
             			// 捕获ctrl事件
             			if (code==17) {
             				parent.window.f_batch_s();
             			}
             			// 捕获shift事件
             			if (code==16) {
             				parent.window.f_batch_e();
             			}
         			} catch (e) {
         				// TODO: handle exception
         			}
         		});
         	}
        }
       
    );

//自定义校验规则
function addValidateMethod(){
	
	if ($.validator) {
		$.validator.addMethod("reportDate", function(value, element, param) {
			if(value==""||value==null){
				return true;
			}
			 var date = value;
			    var result = date.match(/^(\d{4})(-|\/)(\d{1,2})\2(\d{1,2})$/);//yyyy-MM-dd yyyy/MM/dd
			    var result1 = date.match(/^(\d{4})(-|\/)(\d{1,2})$/);//yyyy-MM  yyyy/MM 
			    var result2 = date.match(/^(\d{4})年(\d{1,2})月(\d{1,2})日$/);
			    var result3 = date.match(/^(\d{4})年(\d{1,2})月$/);
			    if (result == null&&result1==null&&result2==null&&result3==null&&value!="/"&&value!="—"){
			    	return false;	
			    }else if (result != null){
			    	var d = new Date(result[1], result[3] - 1, result[4]);
				    return (d.getFullYear() == result[1] && (d.getMonth() + 1) == result[3] && d.getDate() == result[4]);
			    }else if (result1 != null){
			    	var d = new Date(result1[1], result1[3] - 1);
				    return (d.getFullYear() == result1[1] && (d.getMonth() + 1) == result1[3] );
			    }else{
			    	return true;
			    }
			   
		}, "日期格式不对");
		$.validator.addMethod("reqiuired", function(value, element, param) {
			
			if(value==""||value==null){
				return false;
			}else{
				return true;
			}
			
			   
		}, "*");
	}
	
}


/**
 * *******************分页报表详情页面相关JS*************************************
 */
var color = "";

$(function() {
	
});
function textareafocus(obj){
	//alert($(obj).html())
	//alert($(obj).attr("id"))
	
	$("#inputFocus").val($(obj).attr("id"));
	bindInputEl = obj;
	focus_element = obj;
}


/**
 * *******************分页报表详情页面相关JS*************************************
 */



function submitForm() {

	if (!$("#formObj").validate().form()) {
		saveResult = false;
		return false;
	}
	//预览时不用保存
	comm.mask("正在保存，请稍后...");
	var array = new Array();
	var code_ext = $("#code_ext").val();
	// 通过配置参数解决fk_report_id和fk_inspection_info_id

	$("input").each(function() {
		var type = $(this).attr("type");
		var classs = $(this).attr("class");
		var name = $(this).attr("name");
		//var mark  = getMark(this);//$("#"+$(this).attr("id")+"_mark").html();//标注
		//alert(mark)
		
		// checkbox,radio类型的使用隐藏的_val获取值 ZQ EDIT 20170827
		if (type != 'button' && type != "checkbox" && type != "radio"&&name!=undefined ) {


			//var value = getInputVal(name);
			//var value=$(this).val();
			
			//有问题，后修改
			//if(type == "hidden"&&classs!="checkbox"){
			//	value = $(this).next().html();
			

			var color = $(this).css("color");// 颜色
			//color = rgb2hex(color);
			var italic = $(this).css("font-style");// 字体风格
			var bold = $(this).css("font-weight");// 字体粗细
			var family = $(this).css("font-family");// 字体
			var size = $(this).css("font-size");// 字号
			var classs = $(this).attr("class");
			var mark = null;
			if($(this).parents("p.DocDefaults").find("div.bzhu_box").get(0)){
				mark  = getMark(this,input);//$("#"+$(this).attr("id")+"_mark").html();//标注
			}
			var image = null;

			if (classs != undefined && classs.substring(0, classs.length - 1) == "receiptfiles") {
				// 图片特殊处理
				var id = $(this).attr("id");

				type = "image";
				var n = classs.substring(classs.length - 1, classs.length);
				// v2.0 修改

				value = pictures[n - 1]["fileid"];
				var width = pictures[n - 1]["width"];
				var height = pictures[n - 1]["height"];
				var scale = pictures[n - 1]["scale"];
				image = width + "pt," + height + "pt," + scale;
			}else{
				
				try {
					var value = getInputVal(name);
				} catch (e) {
					// TODO: handle exception
				}
				try {
					if(ltype=="select"){
						value = $(this).ligerGetComboBoxManager().getValue();
					}
				} catch (e) {
					// TODO: handle exception
				}
				// 有问题 后修改 _ ZQ EDIT 2017 0827=======================要改10.16
				if (type == "hidden" && $(this).attr('cg')== '1') {
						value = $(this).parent().find('div .input11').html();
				}
				if (type == "hidden" && $(this).attr('class')== 'editDivInput') {
					value = $(this).parent("p").find('div.editDiv').html();
				}
				// 获取checkbox,radio类型的值 ZQ EDIT 20170827
				if (name.indexOf("_val")==name.length-4) {
					name = name.substring(0,name.length-4);
					try {
						value = $("#"+name).ligerGetComboBoxManager().getValue();
					} catch (e) {
						// TODO: handle exception
					}
				}
				
			}
			if(name!=undefined&&value!=undefined&&name!=""){
				if(value.indexOf("&nbsp;")>-1){
					value = value.replace(/&nbsp;/g," ");
				}
				if(value.indexOf("<br>")>-1){
					value = value.replace(/<br>/g,"");
				}
				
				
				var json = {
						name : name,
						value : value,
						type : type,
						color : color,
						bold : bold,
						italic : italic,
						family : family,
						size : size,
						image : image,
						markContent : mark == null? null : JSON.stringify(mark)
					};
				
				array.push(json);
			}
			
		}
		

	});
	
	
	$("textarea").each(function() {
		var name = $(this).attr("name");
		if(name!=undefined){
			var value = $(this).val();
			var type = $(this).attr("type");
			var color = $(this).css("color");
			//color = rgb2hex(color);
			var italic = $(this).css("font-style");// 字体风格
			var bold = $(this).css("font-weight");// 字体粗细
			var family = $(this).css("font-family");// 字体
			var size = $(this).css("font-size");// 字号
			var classs = $(this).attr("class");
			var mark = null;
			if($(this).parents("p.DocDefaults").find("div.bzhu_box").get(0)){
				mark  = getMark(this,input);//$("#"+$(this).attr("id")+"_mark").html();//标注
			}
			var json = {
				name : name,
				value : value,
				type : type,
				color : color,
				bold : bold,
				italic : italic,
				family : family,
				size : size,
				markContent : mark == null? null : JSON.stringify(mark)
			}
			array.push(json);
		}
		
	});


	//分页存值、重要
	var json = {
		name : "fkCodeExt",
		value : code_ext
	}
	array.push(json);
	//业务id保证业务id存在
	var json1 = {
			name : "fk_inspection_info_id",
			value : info_id
		}
	array.push(json1);
	
	//处理复选框
	//getCheckBoxValue(array);
	
	
	//当前页名字
	var pageName="";
	
	try {
		
		for (var i = 0; i < $("a").length; i++) {
			if(pageName!=""){
				break;
			}
			var item = $("a")[i];
			var name = $(item).attr("name");

			if(name!=undefined&&name.indexOf("RTPAGE_")!=-1){
				pageName = name.substring(10,name.length);

			}
		}
		
	} catch (e) {
		// TODO: handle exception
	}
	
	pageName = encodeURI(pageName);
	saveResult = false;
	var indexN = paramss.pageName;
	var nowPage = indexN.substring(indexN.indexOf("index")+5,indexN.length-5);
	$.ajax({
		url : $("#formObj").attr("action")+"?pageName="+pageName+"&page="+nowPage,// 'reportItemValueAction/saveMap.do'
		type : 'post',
		async:false,
		dataType : "json",
		contentType : 'application/json;charset=utf-8', // 设置请求头信息
		data : JSON.stringify(array),
		success : function(response) {
			if (response.success) {
				comm.Content("保存成功！");
				comm.LoadEnd(1);
				saveResult = true;
			} else {
				comm.Content("保存失败！" + response.msg);
				comm.LoadEnd(2);
				saveResult = false;
				
			}

			
		}
	});
	return saveResult;
	//return true;
}

/**
 * 标记颜色
 */
function markColor() {
	var id = $("#inputFocus").val()
	var color = $("#" + id).css("color");
	color = rgb2hex(color);
	if (color == "#000000") {
		var reg = /[\s\S]*_div/;
		if (reg.test(id)) {
			$("#" + id).css("color", "red");
			$("#" + id.substr(0, id.length - 4)).css("color", "red");
		} else {
			$("#" + id).css("color", "red");
		}
	} else {

		$("#" + id).css("color", "black");

	}
	$("#" + id).focus();

}
function tsColor(value) {
	var id = $("#inputFocus").val();
	$("#" + id).css("color", "" + value);
}
function rgb2hex(rgb) {
	 if (!!window.ActiveXObject || "ActiveXObject" in window){
		 return rgb;
	 }else{
		 rgb = rgb.match(/^rgb\((\d+),\s*(\d+),\s*(\d+)\)$/);
			function hex(x) {
			return ("0" + parseInt(x).toString(16)).slice(-2);
			}
			return "#" + hex(rgb[1]) + hex(rgb[2]) + hex(rgb[3]);
			}
	 }
/**
 * 去掉颜色
 */
function unmarkColor() {
	var id = $("#inputFocus").val();
	/*
	 * if (id) { $("#" + id).css("color", "black"); }
	 */
	if (id) {
		var reg = /[\s\S]*_div/;
		if (reg.test(id)) {
			$("#" + id).css("color", "black");
			$("#" + id.substr(0, id.length - 4)).css("color", "black");
		} else {
			$("#" + id).css("color", "black");
		}
	}
}
/**
 * 改变字体大小
 */
function fontSizes(grade) {
	var id = $("#inputFocus").val()
	if (id) {
		$("#" + id).css("font-size", grade + "px");
	}
	$("#" + id).focus();
}
/**
 * 改变字体样式
 */
function fontFamilys(grade) {

	var id = $("#inputFocus").val()
	if (id) {
		$("#" + id).css("font-family", grade);
	}
	$("#" + id).focus();
}
/**
 * 改变字体粗细
 */
function fontBold() {

	var id = $("#inputFocus").val()
	var bold = $("#" + id).css("font-weight");
	if (bold == "400") {

		if (id) {
			$("#" + id).css("font-weight", "bold");
		}
	} else {
		if (id) {
			$("#" + id).css("font-weight", "normal");
		}
	}
	$("#" + id).focus();

}
/**
 * 改变字体倾斜
 */
function fontItalic() {
	var id = $("#inputFocus").val();
	var bold = $("#" + id).css("font-style");
	if (bold == "normal") {
		if (id) {
			$("#" + id).css("font-style", "italic");
		}
	} else {
		if (id) {
			$("#" + id).css("font-style", "normal");
		}
	}
	$("#" + id).focus();
}



function initInput(){
	try {
	
		$("input[type='text']").focus(function() {
			$("#inputFocus").val($(this).attr("id"));
			bindInputEl = this;
			focus_element = this;
		});
		if (document.getElementById('tsColor') != undefined) {
			document.getElementById('tsColor').onchange = function() {
				var id = $("#inputFocus").val();
				$("#" + id).css("color", "" + this.value);
			};
		}
		
		
	$(".l-text").css("display",'inline-block');
	$(".l-text").css("width",'96%');
	$("input").css("width",'99%');
	$("textarea").css("width",'99%');
	$(".l-text").css("border-color",'#FFFFFF');
	$(".l-text").css("border-color-bottom",'black');
	//$(".l-text").css("background-color",'#F0F8FF');
	$(".l-textarea").css("border-color",'#FFFFFF');
	$(".l-textarea").css("width",'99%');
	
	$(".l-text").css('min-height','5mm');
	$("input").css('min-height','5mm');
	$(".l-textarea").css('min-height','5mm');
	//日期框
	$(".l-text-date").css("width",'99%');
	$(".l-text-wrapper").css("width",'99%');
	//$(".l-textarea").css("background-color",'#F0F8FF');
	//$("textarea").css("background-color",'#F0F8FF');
	//#F8F8FF     CAE1FF
	//添加换行事件，禁止手动换行,现在需要换行，后台导出时处理过换行，所以就没有限制了pingZhou
	//$("textarea").attr("onkeydown",'checkEnter(event)');
	//$("input").css("background-color",'#F0F8FF');
	/*var ll = $("form").find("table").length;
	$($("#layout2").find("table")[ll-1]).attr("class","two");*/
	
	$(".l-layout-left, .l-layout-right, .l-layout-center, .l-layout-top, .l-layout-bottom, .l-layout-centerbottom").css("border-color",'#FFFFFF');

	if (pageStatus != undefined &&pageStatus != ""&& pageStatus == "detail") {
		$(".l-trigger").remove();
		$(".l-trigger-icon").remove();
		$("input").parents("p").css("padding","1px");
		$("textarea").parents("p").css("padding","1px");
	}else{
		var inp = "<style>#layout2 table input:focus { outline: 0 none; background: rgba(0,0,0,.02); border: 1px solid #ccc; }"
			+"#layout2 table textarea:focus { outline: 0 none; background: rgba(0,0,0,.02); border: 1px solid #ccc; }</style>";
		$("head").append(inp);
		$("input").css("height","6mm");
		$("input").parents("p").css("padding","1px");
		$("textarea").parents("p").css("padding","1px");
	}
	
	//#F8F8FF     CAE1FF
	//添加换行事件，禁止手动换行,现在需要换行，后台导出时处理过换行，所以就没有限制了pingZhou
	//$("textarea").attr("onkeydown",'checkEnter(event)');
	//$("input").css("background-color",'#F0F8FF');
/*	var styleafter = "<style>"
		+"body, table, tr, th, td, input { margin: 0; padding: 0; }"
		+".dtitle { text-align: center; font-weight: bold; font-size:24px; font-family: 'simsun'; }"
		+"#layout2 { margin: 0 auto; width: 90%; font-family: 'simsun'; }"
		+"#layout2 table { position: relative; font-size: 14px; table-layout: fixed; vertical-align: top; border-collapse: collapse!important; width: 90%; }"
		+"#layout2 table tr{min-height:7mm;height:auto; }"
		+"#layout2 table.two { border: 1px solid #000; cellpadding:1; cellspacing:1; }"
		+"#layout2 table.two td { border: 1px solid #000; }"
		+"#layout2 table.l-checkboxlist-table { border: 0px solid #fff; cellpadding:1; cellspacing:1; }"
		+"#layout2 table.l-checkboxlist-table td { border: 0px solid #fff; }"
		
		+"#layout2 table.two td.noborder1 {border-right:0px solid #fff!important;}"
		+"#layout2 table.two td.noborder2 {border-left:0px solid #fff!important;}"
		+"#layout2 table p { margin: 0; padding: 1px; width: 90%; text-align: center; }"
		+"#layout2 table input { width: 50%; height: 1.4em; border: 1px solid #fff;padding: 10px; }"
		+"#layout2 table input.iptw2 {width:72%;}"
		+"#layout2 table input:focus { outline: 0 none; background: rgba(0,0,0,.02); border: 1px solid #ccc; }"
		+"#layout2 table textarea { width: 90%;border: 1px solid #fff;}"
		+"#layout2 table textarea:focus { outline: 0 none; background: rgba(0,0,0,.02); border: 1px solid #ccc; }"
		+"#layout2 table.l-checkboxlist-table input { width:20px}"
		+"#layout2 table p.bianma {text-align:right;}"
		+".l-text{height:100%;}"
		+'.l-text-field{position:static!important;width:100%}'
		+'.l-text-wrapper{width:95%}'
		+'#docx4j_tbl_4 tr td:nth-child(1){  text-align: left;}'
		+'#docx4j_tbl_4 tr td:nth-child(1) p{  text-align: left;}'
		+'#docx4j_tbl_4 tr td:nth-child(1) p span{  text-align: left;}'
		+'#docx4j_tbl_4 tr td:nth-child(1) span{  text-align: left;}'
		+"</style>"
	$("head").append(styleafter)*/
	
	var width = $("#layout2").width();
	var maxheight = 900;
	
	//内容窗口自适应
	for (var i = 0; i < $("form").find("table").length; i++) {
		var tb = $("form").find("table")[i];
		var tw = $(tb).width();
		var th = $(tb).height();
		if((th+100)>maxheight){
			maxheight = (th+100);
		}
		//alert(width*0.8>tw)
		if(width*0.8>tw){
			$(".l-layout-center").css("width","98%")
			$(tb).css("width","94%")
		}
	}
	$(".SY").css("width","80%");
	} catch (e) {
		// TODO: handle exception
	}
	
	if(paramss.mobile!=undefined&&paramss.mobile=="1"){
		//移动端查看，不能删
		return;
	}
	
	setTimeout(function(){
		try {
			var formObjh = $("#formObj").outerHeight(true)+50
			var landFlag = $("#landFlag").val();
			
			if(formObjh<950&&landFlag==undefined){
				formObjh = 950;
			}else if(landFlag!=undefined){
				formObjh = 800;
			}
			$("#formObj").parent("div").css("height",formObjh);
			$(".layout2").css("height",formObjh+50);
			
			var maxheight=$("#formObj").parent("div").outerHeight(true)+50;
			if(maxheight<900){
				maxheight=900;
			}
			$("#formObj").css("height",'98%');
		//$("body").outerHeight(true)+50;alert(maxheight)
		if(parent.setH){
			parent.setH(maxheight);
		}
			
		
		
		} catch (e) {
			// TODO: handle exception
		}
	},100);
	
}

/**
 * 获取url参数
 * @returns
 */
function GetRequest() {  
	   var url = location.search; //获取url中"?"符后的字串  
	   var theRequest = new Object();  
	   if (url.indexOf("?") != -1) {  
	      var str = url.substr(1);  
	      strs = str.split("&");  
	      for(var i = 0; i < strs.length; i ++) {  
	         theRequest[strs[i].split("=")[0]]=unescape(strs[i].split("=")[1]);  
	      }  
	   }  
	   return theRequest;  
	} 


//textarea禁止手动换行
function checkEnter(e)
{
	var et=e||window.event;
	var keycode=et.charCode||et.keyCode;
	if(keycode==13)
	{
		//alert(keycode+"--"+window.event)
	if(window.event!=undefined){
		//alert(window.event+"---"+JSON.stringify(window.event))
		window.event.returnValue = false;
	
		
	}
	/*else*/
	e.preventDefault();//for firefox
	}
}

//复制内容到同行

function getItemByLtype(ltype){
	var items=[];
	for ( var item in $("input")) {
		if($(item).attr("ltype")==ltype){
			
			items[items.length]=item;
			
		}
	}
	return items;
}

function initItemByLtype(ltype){
	
	for (var i = 0; i < $("input").length; i++) { 
		var item = $("input")[i];
		var id = $(item).attr("id");
				var ligerui1 = $(item).attr("ligerui");
				
				if(ligerui1!=undefined&&ligerui1!=""){
					var ligerui = [];
					try {
						ligerui = eval('(' + ligerui1 + ')');
					} catch (e) {
						//alert(ligerui1)
					}
					if(ligerui.data==undefined||ligerui.data==null||ligerui.data==""){
						if(ligerui.code!=undefined&&ligerui.code!=""){
							var code = ligerui.code;
							
							//动态取码表
							if(codeTable[code]!=undefined){
								ligerui["data"] = codeTable[code];
								$("#"+id).ligerGetComboBoxManager().setData(codeTable[code]);
								var initValue = ligerui.initValue;
								var isTextBoxMode = ligerui.isTextBoxMode;
								if(initValue!=undefined&&initValue!=null&&initValue!=""){
									
									$("#"+id).ligerGetComboBoxManager().setValue(initValue);
								}
								//$("#"+id).ligerGetComboBoxManager().setDatas(codeTable[code]);
							}else{
								$.ajax({url:"report/item/record/queryCodeTable.do?code="+code,
									async:false,
									success:function(res){
										if(res.success){
											var codetable = res.codeTable;
											if(codetable!=undefined){
												var datacode = [];
												for (var j = 0; j < codetable.length;j++) {
													
													var datan = {};
													datan["id"] = codetable[j].value;
													datan["text"] = codetable[j].name;
													datacode[datacode.length] = datan;  
												}
												//放入码表
												codeTable[code] = datacode;
												$("#"+id).ligerGetComboBoxManager().setData(codeTable[code]);
												//alert(id)
												//$("#"+id).attr("ligerui",JSON.stringify(ligerui));
												//alert($("#"+id).attr("ligerui"))
												//$("#"+id).ligerGetComboBoxManager().setData(codeTable[code]);
											}
											if(pageStatus==null||pageStatus!="detail"){
												var initValue = ligerui.initValue;
												var isTextBoxMode = ligerui.isTextBoxMode;
												
												if(initValue!=undefined&&initValue!=null&&initValue!=""){
													$("#"+id).ligerGetComboBoxManager().setValue(initValue);
												}
											}
											
										}
									},
									error: function (data,stats) {
										isContinue=false;
						               // $.ligerDialog.error('提示：' + data.msg);
						            }
							});
							
							}
						}
					}else{
						var initValue = ligerui.initValue;
						
						var isTextBoxMode = ligerui.isTextBoxMode;
						
						if(initValue!=undefined&&initValue!=null&&initValue!=""){
						//	alert(id+"---"+initValue)
							$("#"+id).ligerGetComboBoxManager().setValue(initValue);
						}
					}
					/*var ltype = $(item).attr("ltype");
					if(ligerui.data!=undefined){
						
						ligerui["disabled"]=true;
						
					}*/
					
					
				}
				
		
		
	}
	
}


function copyToRow(obj){
	var value = $(obj).val();
	var tr = $(obj).parents("tr").get(0);
	try {
		
		$(tr).find('input').val(value);
	} catch (e) {
		
	}
	try {
		$(tr).find('input').ligerGetComboBoxManager.setValue(value);
	} catch (e) {
		
	}
	
}

//自动生成序号
function sequence(obj,initValue,prefix,suffix){
	//var tr = $(obj).parent().parent().parent().parent().get(0);
	var tr = $(obj).parents("tr");
	var otds = $(tr).find("td");
	obj.index="";
	for(var i=0;i<otds.length;i++){
		if($(otds[i]).find("input")[0]==obj){
			obj.index = i;
			break;
		}
	}
	var nextTrs = $(tr).nextAll();   
	var inputs = [];
	inputs.push(obj);
	for(var j=0;j<nextTrs.length;j++){
		if(nextTrs[j].tagName=="TR"){
			var tds = $(nextTrs[j]).find("td");
			if(tds.length==otds.length){
				if($(tds[obj.index]).find("input")[0]!=null){
					inputs.push($(tds[obj.index]).find("input")[0]);
				}
			}
		}
		
	}
	var prefix = (prefix==undefined ? '' : prefix);
	var suffix = (suffix==undefined ? '' : suffix);
	if( initValue != undefined){
		$.each(inputs,function(id,item){
			$(item).val(prefix+initValue+suffix);
			initValue ++;
		});
	}else{
		initValue = 1;
		$.each(inputs,function(id,item){
			$(item).val(prefix+initValue+suffix);
			initValue ++;
		});
	}
	/*var value = $(obj).val();
	if(value != null && value != '' && !isNaN(parseInt(value))){
		var sn = parseInt(value);
		$(obj).val(sn);
		$.each(inputs,function(id,item){
			$(item).val(++sn);
		});
	}else{
		//
		var sn = 1;
		$(obj).val(sn);
		$.each(inputs,function(id,item){
			$(item).val(++sn);
		});
	}*/
}

function initForm() {
	try{
		//加载完成后才允许翻页保存
		parent.saveFlag = false;
		
	} catch (e) {
		// TODO: handle exception
	}
	
	try {
		initInput();
	} catch (e) {
		// TODO: handle exception
	}
	try {
		initItemByLtype("select")
	} catch (e) {
		// TODO: handle exception
	}
	//预览时没有参数
	if (pageStatus == "") {
		
		return;
	}
	
	if (input == "1") {
	//录入时才需要加载默认值和默认的参检人员信息
		
			
			//alert(check_op)
		if(check_op!=''){
			setSignData("base__insepct_op",check_op)
		
		}
	}	
	
	
	comm.mask("正在获取数据，请稍后...");
	
	var formParam = {
		fk_report_id : fk_report_id
	};
	if (input != "") {
		formParam = {
			fk_report_id : fk_report_id,
			input : input
		};
	}
	//$("#formObj").attr("getAction") + '&input=' + input + '&' + relColumn+'&id='+info_id
	// relColumn为rtpage中配置的关联字段
	
	var indexN = paramss.pageName;
	$.ajax({
		url : $("#formObj").attr("getAction") + '?input=' + input + '&' + relColumn+'&id='+info_id+"&code_ext="+code_ext+"&now="+indexN,// reportItemValueAction/detailMap.do
		type : 'post',
		dataType : "json",
		// data : formParam,
		success : function(response) {
			if (response.success) {
				var data = response.data;
				//添加页脚
				var pageMap = response.pageMap;
				//setFootNum(pageMap.nowPage,pageMap.sumPage);
				//处理人员签名
				if (pageStatus != undefined &&pageStatus != ""&& pageStatus == "detail") {
					//处理人员签名
					setSginPicture(pageMap.signPicture);
				}
				comm.LoadEnd();
				if (data != null) {
					for ( var row in data) {
						var name = data[row]["name"];
						var value = data[row]["value"];
						var color = data[row]["color"];
						var bold = data[row]["bold"];
						var italic = data[row]["italic"];
						var size = data[row]["size"];
						var family = data[row]["family"];
						var image = data[row]["image"];
						var markContent = data[row]["markContent"];
						if (code_ext) {
							if (name.indexOf(code_ext) > 0) {
								name = name.replace("__"+code_ext, "");
								//name = name.replace("__", "");
								
								
							} 
							
							if((name.indexOf('inspect_op')>0||name.indexOf('enter_op')>0||name.indexOf('audit_op')>0||name.indexOf('sign_op')>0
									||name.indexOf('confirm_op')>0)&&value!=null&&value!=""){
								//if (pageStatus != undefined &&pageStatus != ""&& pageStatus != "detail") {
									//setSignData(name,value);
								//}
							}else if((name.indexOf('op')>0||name.indexOf('_time')>0||name.indexOf('_date')>0)&&value!=null&&value!=""){
								setSignData(name,value);
								
							}else if(value!=null&&value!=""){
								setInputVal(name, value);
								try {
									setCheckBoxValue(name, value);
								} catch (e) {
									// TODO: handle exception
								}
								try {
									$("#"+name).ligerGetComboBoxManager().setValue(value);
								} catch (e) {
									// TODO: handle exception
								}
							}
							
							
							if (color) {
								$("#" + name).css("color", "" + color);
							}
							if (bold) {
								$("#" + name).css("font-weight", "bold");
							}
							if (italic) {
								$("#" + name).css("font-style", "italic");
							}
							if (size) {
								$("#" + name).css("font-size", size);
							}
							if (family) {
								$("#" + name).css("font-family", family);
							}
							if (markContent&&(mobile!=2)) {
								//必须判断，有可能当页没有该name的input
								//alert(markContent)
								if($("#" + name)[0]){
									var mark = JSON.parse(markContent);
									if(mark.status == '0' || mark.status == '' || input ==''){
										//change($("#" + name)[0],'1',markContent,input);
										label($("#" + name)[0],input,markContent)
									}
								}
							}
							if (image != null) {
								// 图片处理
								var classs = $("input[name='" + name + "']:eq(0)").attr("class");
								// 没有找到图片控件则不显示
								if (classs != undefined) {

									// 图片的高宽样式没有设置，因为目前初始化的时候设置了
									var n = classs.substring(classs.length - 1, classs.length);

									var wh = image.split("||")[0];
									var w = wh.split(";")[0];
									var h = wh.split(";")[1];
									var w1 = w.substring(0, w.length - 2).split(":")[1];
									var h1 = h.substring(0, h.length - 2).split(":")[1];

									pictures[n - 1]["width"] = w1;
									pictures[n - 1]["height"] = h1;
									pictures[n - 1]["fileid"] = value;
									// alert("w1---"+w1+"---h1--"+h1)
									if (image.split("||").length > 1) {
										var scale = image.split("||")[1];
										$("#canvas" + n + "-range").val(scale);
										pictures[n - 1]["scale"] = scale;
									}
									$("#receiptfiles" + n + 'AttchNames').val(value);

									// v2.0 修改了图片控件
									// changeCanvasInit(n,value,0,0);
									// $("#"+classs+"P").attr("src","fileupload/downloadByObjId.do?id="+value);

									// alert(classs)

								}
							}
						} else {
							if((name.indexOf('inspect_op')>0||name.indexOf('enter_op')>0||name.indexOf('audit_op')>0||name.indexOf('sign_op')>0
									||name.indexOf('confirm_op')>0)&&value!=null&&value!=""){
								//if (pageStatus != undefined &&pageStatus != ""&& pageStatus != "detail") {
									setSignData(name,value);
								//}
							}else if((name.indexOf('op')>0||name.indexOf('_time')>0||name.indexOf('_date')>0)&&value!=null&&value!=""){
								setSignData(name,value);
								
							}else if(value!=null&&value!=""){
								setInputVal(name, value);
								try {
									setCheckBoxValue(name, value);
								} catch (e) {
									// TODO: handle exception
								}
								try {
									$("#"+name).ligerGetComboBoxManager().setValue(value);
								} catch (e) {
									// TODO: handle exception
								}
							}
							
							
							//setInputVal(name, value);
							if (color) {
								$("#" + name).css("color", "" + color);
							}
							if (bold) {
								$("#" + name).css("font-weight", "bold");
							}
							if (italic) {
								$("#" + name).css("font-style", "italic");
							}
							if (size) {
								$("#" + name).css("font-size", size);
							}
							if (family) {
								$("#" + name).css("font-family", family);
							}
							if (markContent&&(mobile!=2)) {
								//必须判断，有可能当页没有该name的input
								//alert(markContent)
								if($("#" + name)[0]){
									var mark = JSON.parse(markContent);
									if(mark.status == '0' || mark.status == '' || input ==''){
										//change($("#" + name)[0],'1',markContent,input);
										label($("#" + name)[0],input,markContent)
									}
								}
							}
							if (image != null) {
								// 图片处理
								var classs = $("input[name='" + name + "']:eq(0)").attr("class");
								// 没有找到图片控件则不显示
								if (classs != undefined) {

									// 图片的高宽样式没有设置，因为目前初始化的时候设置了
									var n = classs.substring(classs.length - 1, classs.length);

									var wh = image.split("||")[0];
									var w = wh.split(";")[0];
									var h = wh.split(";")[1];
									var w1 = w.substring(0, w.length - 2).split(":")[1];
									var h1 = h.substring(0, h.length - 2).split(":")[1];

									pictures[n - 1]["width"] = w1;
									pictures[n - 1]["height"] = h1;
									pictures[n - 1]["fileid"] = value;
									// alert("w1---"+w1+"---h1--"+h1)
									if (image.split("||").length > 1) {
										var scale = image.split("||")[1];
										$("#canvas" + n + "-range").val(scale);
										pictures[n - 1]["scale"] = scale;
									}
									$("#receiptfiles" + n + 'AttchNames').val(value);

									// v2.0 修改了图片控件
									// changeCanvasInit(n,value,0,0);
									// $("#"+classs+"P").attr("src","fileupload/downloadByObjId.do?id="+value);

									// alert(classs)

								}
							}
						}

					}
					$("body").focus();
					try{
						//加载完成后才允许翻页保存
						parent.saveFlag = true;
						
					} catch (e) {
						// TODO: handle exception
					}
					
				}

				
				var objst = $("textarea").get();
				for (var i = 0, l = objst.length; i < l; i++) {
					if (pageStatus != undefined &&pageStatus != ""&& pageStatus == "detail") {
						$(objst[i]).attr("readonly", "readonly");
						//$(objst[i]).attr("disabled","disabled");
						//$(".l-trigger-hover").hide();
					}
					autoTextarea(objst[i]);
				}
				
				// 处理上下标
				var objs = $("input").get();
				for (var i = 0, l = objs.length; i < l; i++) {
					if (pageStatus != undefined &&pageStatus != ""&& pageStatus == "detail") {
						$(objs[i]).attr("readonly", "readonly");
						
						//处理下日期格式
						if($(objs[i]).attr("ltype")=="date"){
							formatDateIuput(objs[i]);
						}
						
						//$(objs[i]).attr("disabled","disabled")
						//$(".l-trigger-hover").hide();
					}
					var val = $(objs[i]).val();
					var reg = /<sub>[\s\S]*<\/sub>|<sup>[\s\S]*<\/sup>|<SUB>[\s\S]*<\/SUB>|<SUP>[\s\S]*<\/SUP>/;
					var result = reg.test(val);
					if (result) {
						change(objs[i]);
					}
				}
				var textareas = $("textarea").get();
				for (var j = 0, m = textareas.length; j < m; j++) {
					if (pageStatus != undefined &&pageStatus != ""&& pageStatus == "detail") {
						$(textareas[j]).attr("readonly", "readonly");
					}
					var val = $(textareas[j]).val();
					var reg = /<sub>[\s\S]*<\/sub>|<sup>[\s\S]*<\/sup>|<SUB>[\s\S]*<\/SUB>|<SUP>[\s\S]*<\/SUP>/;
					var result = reg.test(val);
					if (result) {
						change(textareas[j]);
					}
				}
				
				
				if (pictures != undefined) {
					if (pictures.length > 0) {
						loadImage(0);
					}
				}

				if(paramss.mobile==2){
					var $signPic = $(".signPic");
					
					for (var i = 0; i < $signPic.length; i++) {
						var item = $signPic[i];
						var value = $(item).val();
						var values = value.split(",");
						if(values.length==1){
							values = value.split(";");
						}
						if(values.length==1){
							values = value.split("；");
						}
						var html = "";
						for (var j = 0; j < values.length; j++) {
							html = html +"["+values[j]+"]";
							if(values.length>1){
								html = html +'<span style="margin-left:50px;"></span>';
							}
							
						}
						//var itemN = $(item).val().replace(/,/g,'][').replace(/;/g,'][');
						$(item).parents("td").css("font-size","1px");
						$(item).parents("td").attr("align","center");
						$(item).parents("td").css("color","white");
						$(item).parents("td").html(html);
					}
				}

				//ZQ ADD 非IE浏览器判断图片加载完成
				var isIE = (navigator.userAgent.indexOf('MSIE')>=0) || (navigator.userAgent.indexOf('Trident')>=0);
				if(!isIE){
					var imgs=document.getElementsByTagName("img");
					//debugger;
					var imgCount=imgs.length;
					if(imgCount>0){
						 
						var waitFor = setInterval(function(){
							var flag=0;
							for(var idx=0;idx<imgs.length;idx++){
								var img=imgs[idx];
								if(!img.complete){
									console.log("img no complete:"+img.src);
									break;
								}
								flag++;
							}
							
							if(flag==imgCount){
								$("form").append("<input type='hidden' name='initReadyFlag' id='initReadyFlag' value='0'/>")
								clearInterval(waitFor);
							}
							
						}, 250);
					}else{
						$("form").append("<input type='hidden' name='initReadyFlag' id='initReadyFlag' value='0'/>")
					}
				}else{
					$("form").append("<input type='hidden' name='initReadyFlag' id='initReadyFlag' value='0'/>")
				}
				
			} else {
				comm.Content("获取失败！" + response.msg);
				comm.LoadEnd(2);
			}

		}
	});
	return false;
}

//检验项目的检验结果和检验结论联动事件
function changeRecord(input,val){
	
	var id = $(input).attr("id");
	if(val==null||val==""){
		return;
	}
	/*var ss = false;
	if("S"==id.substring(id.length-1, id.length)){
		ss = true;
	}*/
	
	var concl_num = "";
	
	//var num = id.substring(8, 11);
	var num = $(input).attr("class").split(" ")[0];
	var flag = true;
	var wcx = 0;
	for (var i = 0; i < $("."+num).length; i++) {
		var obj = $("."+num)[i];
		if(i==0){
			concl_num = $($("."+num)[0]).attr("id").replace("record__","");
			//alert(concl_num)
		}
		//var record = $(obj).ligerGetComboBoxManager().getValue();
		var nid = $(obj).attr("id");
		var record = $("#"+nid+"_val").val();
		//alert(record)
		if(record=="无此项"){
			wcx = wcx +1;
		}
		if(record!="符合"&&record!="资料确认符合"&&record!="无此项"){
			flag = false;
				try {
					$("#conclusion__"+concl_num).ligerGetComboBoxManager().setValue("不合格");
				} catch (e) {
					// TODO: handle exception
				}
			
			
			return;
		}
	}
	if($("."+num).length>0&&flag){
		var jl = "合格";
		if(wcx==$("."+num).length){
			jl = "无此项";
		}
		
		
			try {
				$("#conclusion__"+concl_num).ligerGetComboBoxManager().setValue(jl);
			} catch (e) {
				// TODO: handle exception
			}
		
	}
	
}
var i = 0;
//c重写标签赋值
function setInputVal(name, val,type) {
	try {


		if(name.indexOf("_t")>0){
			names=name.substring(0,name.indexOf("_t"));
			$("#"+names).parent().css("height",'40px');
	    	$("#"+names).hide();
	    	$("#"+names).after("<div class='sup_sub_div' onfocus='textareafocus(this)' id='"+names+"_t' contenteditable='true' name='"+names+"_t' rows='2' cols='43' style='background-color:#CAE1FF'  >s<sup>ss</sup>s</div>");
		
	    	try {
	    		
	    		$("#"+names).parents("tr").show();
			} catch (e) {
				console.log(e);
				// TODO: handle exception
			}
		}else{
			$('#' + name).val(val);
			try {
				/*if(i<1&&$("#"+name).parents("tr").html()!=undefined){
					alert($("#"+name).parents("tr").html())
					i++;
				}*/
				
				$("#"+name).parents("tr").show();
			} catch (e) {
				console.log(e);
				// TODO: handle exception
			}
		}
		
	} catch (e) {
		try {
			$('#' + name).ligerGetComboBoxManager.setValue(val);
		} catch (e) {
			try {
				$('#' + name).ligerGetDateEditorManager.setValue(val);
			} catch (e) {
				try {
					$('#' + name).ligerGetRadioGroupManager().setValue(val);
				} catch (e) {
					//document.getElementById(name).value = val;
				}
			}
		}
	}
}

function setSignData(name,val){
	
	if(name.indexOf('_time')>0||name.indexOf('_date')>0){
		if(val.length>10){
			val = val.substring(0,10);
		}
	}
	
	var page=0;
	var indexN = paramss.pageName;
	//alert(indexN)
	page = indexN.substring(5,indexN.length-5);
	var check_ops = val.split(",");
	var value = "";
	for (var i = 0; i < check_ops.length; i++) {
		if(value == ""){
			value = check_ops[i];	
		}else{
			value = value+";"+check_ops[i];	
		}
	}

	$("#"+name).val(value)
	
		try {
			$("#"+name).ligerGetComboBoxManager().setValue(value);

		} catch (e) {
			//console.error(name+"----"+e.message)
			// TODO: handle exception

		}
		try {
			var prev = $("#"+name+page).ligerGetComboBoxManager().getValue();
			if(prev==""||prev==null){
				$("#"+name+page).ligerGetComboBoxManager().setValue(value);
			}

		} catch (e) {
			//console.error(e)
			// TODO: handle exception

		}
		
}


/**
 * 单元格画斜线
 * @param header 需要画斜线的td的id
 * @param line_width 线宽度 一般 值1
 * @param line_color 线颜色
 * @param line_number 线数量
 * @returns {Number}
 */
function line(header,line_width,line_color,line_number){  
	$("body").append('<canvas id="line_'+header+'" style="display:none;"></canvas>');
	    var table = document.getElementById(header);   
	    var xpos = table.clientWidth;  
	    var ypos = table.clientHeight;  
	    var canvas = document.getElementById("line_"+header);  
	    if(canvas.getContext){  
	        var ctx = canvas.getContext('2d');  
	        ctx.clearRect(0,0,xpos,ypos); //清空画布，多个表格时使用  
	        ctx.fill();  
	        ctx.lineWidth = line_width;  
	        ctx.strokeStyle = line_color;  
	        ctx.beginPath();  
	        switch(line_number){  
	            case 1:  
	                ctx.moveTo(0,0);  
	                ctx.lineTo(xpos,ypos);  
	                break;  
	            case 2:  
	                ctx.moveTo(0,0);  
	                ctx.lineTo(xpos/2,ypos);  
	                ctx.moveTo(0,0);  
	                ctx.lineTo(xpos,ypos/2);  
	                break;  
	            case 3:  
	                ctx.moveTo(0,0);  
	                ctx.lineTo(xpos,ypos);  
	                ctx.moveTo(0,0);  
	                ctx.lineTo(xpos/2,ypos);  
	                ctx.moveTo(0,0);  
	                ctx.lineTo(xpos,ypos/2);  
	                break;  
	            default:  
	            return 0;     
	        }  
	                  
	        ctx.stroke();  
	        ctx.closePath();  
	        document.getElementById(header).style.backgroundImage = 'url("' + ctx.canvas.toDataURL() + '")';  
	        //document.getElementById(header).style.background-attachment >= 'fixed';  
	    }  
	} 


function checkSameName(){
	//alert(1)
	var hasNamed = {};
	var sameNamed = {};
	var inputs = $("input");
	for(var i=0;i<inputs.length;i++){
		var item = inputs[i];
		var name = $(item).attr("name");
		if(name!=undefined&&name!=""){
			var pretd = $(item).parents("td").prev("td");
			var text = pretd.text().replace(/\t/g,"").replace(/\n/g,"");
			//alert(text)
			if(hasNamed[name]!=null){
				sameNamed[name+"___"+i] = hasNamed[name];	
				sameNamed[name] = text;	
			}else{
				hasNamed[name] = text;	
			}
			
		}
	}
	console.log(JSON.stringify(hasNamed))
	console.log(JSON.stringify(sameNamed))
	//alert(JSON.stringify(hasNamed))
	var show = "";
	for(var key in sameNamed ){
		var keyn = key;
		if(keyn.indexOf("___")>0){
			keyn = key.split("___")[0];
		}
		if(show==""){
			show = sameNamed[key]+":"+keyn
		}else{
			show = show + "\n"+sameNamed[key]+":"+keyn
		}
	}
	//alert(show)
}

function checkAllBaseName(){
	var hasNamed = {};
	var baseNamed = {};
	var inputs = $("input");
	for(var i=0;i<inputs.length;i++){
		var item = inputs[i];
		var name = $(item).attr("name");
		if(name!=undefined&&name!=""){
			var pretd = $(item).parents("td").prev("td");
			var text = pretd.text().replace(/\t/g,"").replace(/\n/g,"");
			if(name.indexOf("base__")==0){
				baseNamed[name] = text;	
			}
			if(name.indexOf("base__")==-1&&name.indexOf("TBL")==-1&&name.indexOf("FK")==-1&&name.indexOf("fk")==-1){
				baseNamed[name] = text;	
			}
			//alert(text)
			if(hasNamed[name]!=null){
				baseNamed[name+"___"+i] = hasNamed[name];	
				baseNamed[name] = text;	
			}else{
				hasNamed[name] = text;	
			}
			
		}
	}
	console.log(JSON.stringify(hasNamed))
	console.log(JSON.stringify(baseNamed))
	var show = "";
	for(var key in baseNamed ){
		var keyn = key;
		if(keyn.indexOf("___")>0){
			keyn = key.split("___")[0];
		}
		if(show==""){
			show = baseNamed[key]+":"+keyn
		}else{
			show = show + "\n"+baseNamed[key]+":"+keyn
		}
	}
	//alert(JSON.stringify(hasNamed))
	//alert(JSON.stringify(baseNamed))
	//alert(show)
	
}

function setSginPicture(signMap){
	
	var signInput = $(".signPic");
	for (var i = 0; i < signInput.length; i++) {
		var item = signInput[i];
		var id = $(item).attr("id");
		var name = $(item).attr("name");
		if(signMap[name+"_signp"]!=null&&signMap[name+"_signp"]!=""
			&&name!="base__inspect_op"&&name!="base__enter_op"
				&&name!="base__confirm_op"&&name!="base__audit_op"
					&&name!="base__sign_op"){
			//取录入时选择的人员的签名
			var signId = signMap[name+"_signp"];
			var signIds = signId.split(",");
			var parent = $(item).parents("td");
			$(item).parents("td").html("");
			//alert(name+"---"+signId)
			for (var j = 0; j < signIds.length; j++) {
				
				
				$(parent).append('<img style="padding:1px;height:40px;" src="fileupload/downloadByObjId.do?id='+signIds[j]+'"/>');
			}
		}else{
			//取流程中人员的签名
			var flowOp = null;
			if(name.indexOf("base__inspect_op")!=-1){
				flowOp = "base__inspect_op";
			}else if(name.indexOf("base__enter_op")!=-1){
				flowOp = "base__enter_op";
			}else if(name.indexOf("base__confirm_op")!=-1){
				flowOp = "base__confirm_op";
			}else if(name.indexOf("base__audit_op")!=-1){
				flowOp = "base__audit_op";
			}else if(name.indexOf("base__sign_op")!=-1){
				flowOp = "base__sign_op";
			}
			if(flowOp != null){
				if(signMap[flowOp+"_signp"]!=null&&signMap[flowOp+"_signp"]!=""){
					var signId = signMap[flowOp+"_signp"];
					//alert(name+"---"+signId)
					var signIds = signId.split(",");
					var parent = $(item).parents("td");
					$(item).parents("td").html("");
					for (var j = 0; j < signIds.length; j++) {
						$(parent).append('<img style="padding:1px;height:40px;" src="fileupload/downloadByObjId.do?id='+signIds[j]+'"/>');
					}
				}
			}
			 
		}
	}
	
}

function setSignPictrue(name,val){
	name = name.substring(0,name.length-5);
	var page=0;
	var indexN = paramss.pageName;
	//alert(indexN)
	page = indexN.substring(5,indexN.length-5);
	var check_ops = val.split(",");
	var value = "";
	for (var i = 0; i < check_ops.length; i++) {
		if(value == ""){
			value = check_ops[i];	
		}else{
			value = value+";"+check_ops[i];	
		}
	}

	$("#"+name).hide();
	
	$("#"+name).parents("td").append('<img style="padding: 1px;height:40px;" src="fileupload/downloadByObjId.do?id='+val+'"/>');
	
		try {
			
			$("#"+name).ligerGetComboBoxManager().setValue(value);

		} catch (e) {
			try {
				var prev = $("#"+name+page).ligerGetComboBoxManager().getValue();
				if(prev==""||prev==null){
					$("#"+name+page).parents("div").hide();
					$("#"+name+page).parents("td").append('<img  style="padding: 1px;height:40px;" src="fileupload/downloadByObjId.do?id='+val+'"/>');
				}

			} catch (e) {
				//console.error(e)
				// TODO: handle exception

			}// TODO: handle exception

		}
		
		
}


/**

 * 文本框根据输入内容自适应高度

* @param                {HTMLElement}        输入框元素

* @param                {Number}                设置光标与输入框保持的距离(默认0)

 * @param                {Number}                设置最大高度(可选)

 */

var autoTextarea = function (elem, extra, maxHeight) {
		
        extra = extra || 0;

        var isFirefox = !!document.getBoxObjectFor || 'mozInnerScreenX' in window,

        isOpera = !!window.opera && !!window.opera.toString().indexOf('Opera'),

                addEvent = function (type, callback) {

                        elem.addEventListener ?

                                elem.addEventListener(type, callback, false) :

                                elem.attachEvent('on' + type, callback);

                },

                getStyle = elem.currentStyle ? function (name) {

                        var val = elem.currentStyle[name];

 

                        if (name === 'height' && val.search(/px/i) !== 1) {

                                var rect = elem.getBoundingClientRect();

                                return rect.bottom - rect.top -

                                        parseFloat(getStyle('paddingTop')) -

                                        parseFloat(getStyle('paddingBottom')) + 'px';        

                        };

 

                        return val;

                } : function (name) {

                                return getComputedStyle(elem, null)[name];

                },

                minHeight = parseFloat(getStyle('height'));

 

        elem.style.resize = 'none';

 

        var change = function () {

                var scrollTop, height,

                        padding = 0,

                        style = elem.style;

 

                if (elem._length === elem.value.length) return;

                elem._length = elem.value.length;

 

                if (!isFirefox && !isOpera) {

                        padding = parseInt(getStyle('paddingTop')) + parseInt(getStyle('paddingBottom'));

                };

                scrollTop = document.body.scrollTop || document.documentElement.scrollTop;

 

                elem.style.height = minHeight + 'px';

                if (elem.scrollHeight > minHeight) {

                        if (maxHeight && elem.scrollHeight > maxHeight) {

                                height = maxHeight - padding;

                                style.overflowY = 'auto';

                        } else {

                                height = elem.scrollHeight - padding;

                                style.overflowY = 'hidden';

                        };

                        style.height = height + extra + 'px';

                        scrollTop += parseInt(style.height) - elem.currHeight;

                        document.body.scrollTop = scrollTop;

                        document.documentElement.scrollTop = scrollTop;

                        elem.currHeight = parseInt(style.height);

                };

        };

 

        addEvent('propertychange', change);

        addEvent('input', change);

        addEvent('focus', change);

        change();

};

//自定义checkbox渲染方式
function initCheckBoxDIV(dataD){
	var dataa = dataD.data;
	var name = dataD.name;
	var ligerui = "{}";
    if($("#"+name).attr("ligerui")){
    	ligerui=$("#"+name).attr("ligerui");
	}
    
	var itemj = eval("(" + ligerui.toString() + ")");
    var initValue = itemj.initValue==undefined?"":itemj.initValue;
    var initValues = initValue.replace(",");
    var xm = "";
	for (var i = 0; i < dataa.length; i++) {
		 var initv = "";
	        if(initValues.indexOf(dataa[i].id)>-1){
	            initv = 'checked="checked"';
	        }
	        xm = xm+'<div style="display:inline;width:100%;"><input type="checkbox" style="width:20px;" name="'+name
			+'" value="'+dataa[i].id+'" id="'+name+'-'+i+'" class="checkBox-'+name+'" onchange="changeCheck(this)"'+initv+'><label for="'+name+'-'+i+'">'+dataa[i].text+'</label></div>'
		}
		var html  = '<div class="l-checkboxlist-inner" style="width:100%;">'
			+'<table cellpadding="0" cellspacing="0" border="0" class="l-checkboxlist-table" style="width:100%;"><tbody><tr><td>'
			+xm
			+'</td></tr></tbody></table>'
			+' </div><input type="hidden" name="'+name+'_val" id="'+name+'_val" data-ligerid="'+name+'" value="">';
		
		//$("#"+name).attr("class","l-checkboxlist");
		$("#"+name).css("width","100%");
		$("#"+name).attr("ligeruiid",name);
		$("#"+name).html(html);
	
	
	/*<div class="l-checkbox-wrapper l-over"><a class="l-checkbox"></a><input type="checkbox" style="width: 99%; min-height: 5mm; height: 6mm;" name="TBL00008" value="桁架" id="TBL00008-2" class="checkBox-TBL00008 l-hidden" onchange="changeCheck(this)" ligeruiid="TBL00008-2"></div>
		*/
		
	/*	<div class="l-checkboxlist-inner"><table cellpadding="0" cellspacing="0" border="0" class="l-checkboxlist-table"><tbody><tr><td><input type="checkbox" name="TBL00008" value="π形箱梁" id="TBL00008-0"><label for="TBL00008-0">π形箱梁</label></td><td><input type="checkbox" name="TBL00008" value="U形箱梁" id="TBL00008-1"><label for="TBL00008-1">U形箱梁</label></td><td><input type="checkbox" name="TBL00008" value="桁架" id="TBL00008-2"><label for="TBL00008-2">桁架</label></td></tr><tr><td><input type="checkbox" name="TBL00008" value="H型梁" id="TBL00008-3"><label for="TBL00008-3">H型梁</label></td><td><input type="checkbox" name="TBL00008" value="其他：" id="TBL00008-4"><label for="TBL00008-4">其他：</label></td></tr></tbody></table></div>
		*/
}

//自定义checkbox渲染方式
function changeCheck(inp){
	var name = inp.name;
	var checkedi = $(inp).attr("checked");
	if(checkedi==undefined){
		 $(inp).attr("checked","checked");
	}else{
		 $(inp).removeAttr("checked");
	}
	var classs = $(inp).attr("class");
	var inpClass = $("."+classs);
	var val = "";
	for (var i = 0; i < inpClass.length; i++) {
		var checked = $(inpClass[i]).attr("checked");
		if(checked!=undefined){
			var value =  $(inpClass[i]).val();
			if(val==""){
				val = value;
			}else{
				val = val+";"+value;
			}
		}
		
	}
	alert(val)
	$("#"+name+"_val").val(val);
}
//自定义checkbox赋值方式
function setCheckBoxValue(name,value){
	$("#"+name+"_val").val(value);
	var val =  ";"+value+";";
	var checkName = $(".checkBox-"+name);
	for (var i = 0; i < checkName.length; i++) {
		var item = checkName[i];
		var iv = $(item).val()
		if(val.indexOf(";"+iv+";")!=-1){
			$(item).attr("checked","checked");
		}
	}
}

//下拉框改变事件
function onSelectValueChange(inp,inpN){
	var text = $("#"+inp.id).val();
	
	$("#"+inpN).val(text);
}


//加载人员下拉框
function initOpSelectInput(){
	var indexN = paramss.pageName;
	var nowPage = indexN.substring(indexN.indexOf("index")+5,indexN.length-5);
	//检验员
	try {
		
		$("#base__inspect_op").ligerGetComboBoxManager().setData(inspectOpData);

	} catch (e) {
		try {
			$("#base__inspect_op"+nowPage).ligerGetComboBoxManager().setData(inspectOpData);

		} catch (e) {
			//console.error(e)
			// TODO: handle exception

		}// TODO: handle exception

	}
	//审核人
	try {
		
		$("#base__audit_op").ligerGetComboBoxManager().setData(auditOpData);

	} catch (e) {
		try {
			$("#base__audit_op"+nowPage).ligerGetComboBoxManager().setData(auditOpData);

		} catch (e) {
			//console.error(e)
			// TODO: handle exception

		}// TODO: handle exception

	}
	
}


/**
 * 日期格式化
 * @param value
 * @param format
 * @returns
 */
function formatDate(value,format){
	//alert(value)
	if(value==null||value==""){
		//alert(1)
		return "";
	}else{
		//alert(2+"---"+format)
		//alert(new Date("2018-12-19").format(format))
		return new Date("2018-12-19").format(format);
	}
	
}

/**
 * 格式化日期控件
 * @param item
 * @returns
 */
function formatDateIuput(item){
	try {
		var $item = $(item);
		if($item.val()==""){
			return;
		}
		var ligerui1 = $item.attr("ligerui");
		if(ligerui1!=undefined&&ligerui1!=""){
			var ligerui = {};
			try {
				ligerui =JSON.parse(ligerui1);
			} catch (e) {
			}
			if(ligerui.format!=undefined){
				var valuen = getFormatDate(new Date($item.val()),ligerui.format);//formatDate($item.val(),ligerui.format);
				$item.val(valuen);
			}else{
				var valuen = getFormatDate(new Date($item.val()),"yyyy年MM月dd日");//formatDate($item.val(),"yyyy年MM月dd日");
				$item.val(valuen);
			}
		}
	} catch (e) {
		
		// TODO: handle exception
	}
	
}

function getFormatDate(date, pattern) {
	 if (date == undefined) {
	 date = new Date(); 
	 }
	 var Year= date.getFullYear();
	 var Month= date.getMonth()+1; 
	 var Day = date.getDate();
	 var Hour=date.getHours();
	 var Minute=date.getMinutes();
	 var Second=date.getSeconds();
	 if (pattern == undefined) {
	 pattern = "yyyy-MM-dd hh:mm:ss";
	 }
	 return pattern.replace("yyyy",Year).replace("MM",Month).replace("DD", Day).replace("dd", Day).replace("HH",Hour).replace("mm",Minute).replace("ss", Second);
	}


/**
 * 设置复选框默认值
 * @returns
 */
function setligerCheckBoxListInitValue(){
	//处理复选框默认值
	var items = $(".checkboxDiv");
	for (var i = 0; i < items.length; i++) { 
		var item = items[i];
		var id = $(item).attr("id");
		var ligerui1 = $(item).attr("ligerui");
				
		if(ligerui1!=undefined&&ligerui1!=""){
			var ligerui = {};
			try {
			ligerui = eval('(' + ligerui1 + ')');
			
			} catch (e) {
				//alert(ligerui1)
			}
			if(ligerui.initValue!=undefined&&ligerui.initValue!=""){
				$(item).ligerGetCheckBoxManager().setValue(ligerui.initValue);
				$("#"+id+"_val").val(ligerui.initValue);
			}
		}
	}
}