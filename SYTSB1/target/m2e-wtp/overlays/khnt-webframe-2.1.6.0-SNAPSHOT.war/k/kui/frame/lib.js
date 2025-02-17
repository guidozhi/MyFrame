/* 
框架基本类库 只有 list form 页面使用此类库。
2014年06月17日 10:49:21 星期二 lybide 与main.js分离。
*/
//判断浏览器
var browser={
    versions:function(){
        var u = navigator.userAgent, app = navigator.appVersion;
        return {         //移动终端浏览器版本信息
             trident: u.indexOf('Trident') > -1, //IE内核
            presto: u.indexOf('Presto') > -1, //opera内核
            webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核
            gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核
            mobile: !!u.match(/AppleWebKit.*Mobile.*/), //是否为移动终端
            ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
            android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或uc浏览器
            iPhone: u.indexOf('iPhone') > -1 , //是否为iPhone或者QQHD浏览器
            iPad: u.indexOf('iPad') > -1, //是否iPad
            webApp: u.indexOf('Safari') == -1 //是否web应该程序，没有头部与底部
        };
     }(),
     language:(navigator.browserLanguage || navigator.language).toLowerCase()
} 
/**
* 页面载入事件
* */
$(function() {
	if (typeof api=="undefined") {
		$("body").addClass("p-body-trans");
		if (PAGE_WORK_BE_CSSNAME) {
			$("body").addClass(PAGE_WORK_BE_CSSNAME);
		}
		if (PAGE_MAIN_BE_CSSNAME) {
			$("body").addClass(PAGE_MAIN_BE_CSSNAME);
		}
	}
	createPageTitle();
});

$.ajaxSetup({
	cache: false,
	statusCode: {
		404:function(){
			top.$.notice("请求的目标地址不存在！",3,"k/kui/images/icons/dialog/32X32/hits.png");
		},
		403:function(){
			top.$.notice("您没有权限执行此操作！",3,"k/kui/images/icons/dialog/32X32/hits.png");
		}
	}
});

$(document).ajaxSuccess(function(evt, request, settings){
	//检查session过期，以及其他
	var restxt = request.responseText;
	var data;
	try{
		data = $.parseJSON(restxt);
	}catch(err){
		return;
	}
	if(data!=null&&data.sessionTimeout){
		//判断是否外壳
		if(browser.versions.android){
			window.KHANJS.backLogin();
		}else{
			doUserLogin();
		}
		return false;
	}
});

function doUserLogin(){
	top.winOpen({
		content: "url:k/login_dialog.jsp",
		width: 300,
		height: 180
	});
}

//表单取值、赋值
(function($) {
	$.fn.setValues = function(obj, onSuccess, bxpepei) {
		if (!obj) {
			$("body").unmask();
			return;
		};
		bxpepei = bxpepei || false;
		return this.each(function() {
			var type = typeof obj;
			var $this = $(this);
			var status = $this.attr("pageStatus") || $("head").attr("pageStatus");
			var suc = function(d) {};
			var url = $this.attr("getAction");
			if (type == "string") {
				url = obj;
			} else if (type == "object") {
				if (!obj) return;
				if (status == "detail") $.fn.setDivValues(obj, $this);
				else $.fn.setValues.setWithData(obj, $this, bxpepei);
				return;
			} else if (type == "function") {
				suc = obj;
			}
			if (typeof onSuccess == "function") {
				suc = onSuccess;
			}
			if (url) {
				$.ajax({
					url: url,
					$this: $this,
					dataType: 'json',
					success: function(data) {
						if (data.success) {
							if (status == "detail") $.fn.setDivValues(data.data, this.$this);
							else $.fn.setValues.setWithData(data.data, this.$this);
						} else {
							var msg = data.msg || data.data;
							$.ligerDialog.warn(msg);
						}
						$("body").unmask();
						suc(data);
					},
					error: function(XMLHttpRequest, textStatus, errorThrown) {
						alert([XMLHttpRequest, textStatus, errorThrown]);
						$("body").unmask();
						$.ligerDialog.error('加载数据失败！' + textStatus, "error");
					}
				});
			} else { //13-5-2 下午9:18 lybide
				$("body").unmask();
			}

		});
	};
	$.fn.getValues = function() {
		var data = {};
		$(":input", this).not(":submit, :reset, :image,:button, [disabled],[groupChild=true]").each(function() {
			var ele = $(this);
			var manager;
			var ltype = ele.attr("ltype");
			var name = ele.attr("name");
			if (!name) return;
			if ((ele.is(":hidden") || ele.is("textarea")) && !ele.attr("ligeruiid")) {
				if (data[name] === undefined) {
					data[name] = ele.val();
				} else {
					if (!$.isArray(data[name])) {
						data[name] = [data[name]];
					}
					data[name].push(ele.val());
				}
				return;
			} else if (ele.is("textarea")) {
				data[name] = ele.val();
			} else if (ele.is(":text")) {
				switch (ltype) {
				case "select":
					manager = ele.ligerComboBox();
					break;
				case "spinner":
					manager = ele.ligerSpinner();
					break;
				case "date":
					//manager = ele.ligerDateEditor();
					data[name] = ele.val();
					break;
				default:
					manager = ele.ligerTextBox();
					break;
				}
			} else if (ele.is(":password")) {
				data[name] = ele.val();
			} else if (ele.is(":radio")) {
				if (ltype == "radioGroup") manager = ele.ligerRadioGroup();
				else manager = ele.ligerRadio();
			} else if (ele.is(":checkbox")) {
				if (ltype == "checkboxGroup") {
					manager = ele.ligerCheckBoxGroup();
				} else manager = ele.ligerCheckBox();
			}
			if (manager) {
				if (data[name] === undefined) {
					data[name] = manager.getValue();
				} else {
					if (!$.isArray(data[name])) {
						data[name] = [data[name]];
					}
					data[name].push(manager.getValue());
				}
			}
		});
		return data;
	};
	function parseName(name, data) {
		if (!data) {
			return undefined;
		}
		if (data[name] !== undefined) return data[name];
		var names = name.split(".");
		var value = data;
		for (var i = 0; i < names.length; i++) {
			if (value[names[i]] != undefined) value = value[names[i]];
			else return undefined;
		}
		return value;
	}

	function parseValues(value, ligerData) {
		if (value == undefined) return "&nbsp;";
		var str = [];
		var values = value.toString().split(typeof KH__valueSplit == "undefined" ? "," : KH__valueSplit); //alert(JSON.stringify(ligerData));
		var data = ligerData.data;
		for (var i = 0; i < values.length; i++) {
			if (ligerData.tree) {
				var valueField = "id";
				if (ligerData.valueField) valueField = ligerData.valueField;
				str.push(getTextById(valueField, values[i], data));
			} else {
				$.each(data, function(index, item) {
					if (values[i] == item["id"]) {
						if (ligerData && ligerData.lineWrap === false) str += ("<li class='l-lineWrap1-li'><div class='l-lineWrap1-li-div'>" + item["text"] + "</div></li>");
						else str.push(item["text"]);
					}
				});
			}
		}
		if (ligerData && ligerData.lineWrap === false) str = "<ul class='l-lineWrap1'>" + str + "</ul>";
		else str = str.join(";");
		return str == "" ? "&nbsp;": str;
	}

	function getTextById(valueField, id, data) {
		for (var i in data) {
			var temp = null;
			if (id == data[i][valueField]) {
				return data[i]["text"];
			}
			if (data[i]["children"]) {
				temp = getTextById(valueField, id, data[i]["children"]);
			}
			if (temp != null) return temp;
		}
		return null;
	}

	$.fn.setValues.setWithData = function(data, t, bxpepei) {
		if (data === undefined) return;
		function parseName(name, data) {
			if (!data) {
				return undefined;
			}
			if (data[name] !== undefined) return data[name];
			var names = name.split(".");
			var value = data;
			for (var i = 0; i < names.length; i++) {
				if (value[names[i]] != undefined) {
					value = value[names[i]];
				} else {
					return undefined;
				}
			}
			return value;
		}

		$(":input", t).not(":submit, :reset, :image,:button, [disabled]").each(function() {

			var ele = $(this);

			var manager;
			var ltype = ele.attr("ltype");
			var name = ele.attr("name");
			if (ltype == "select") {
				if (ele.attr("id")) name = ele.attr("id").replace(/-txt$/, "");
			}
			if (!name) return;
			var v = parseName(name, data);
			if (v === null || v=="null") v = "";
			if (v == undefined && bxpepei) { //下拉自选，无值时退出 13-4-15 下午11:11 lybide
				return;
			}
			if ((ele.is(":hidden")) && !ele.attr("ligeruiid")) {
				if (v !== undefined) ele.val(v);
				return;
			} else if (ele.is("textarea")) { //2013-5-27 上午10:20 lybide
				manager = ele.ligerTextBox();
			} else if (ele.is(":text")) {
				switch (ltype) {
				case "select":
					manager = ele.ligerComboBox();
					break;
				case "radioGroup":
					manager = ele.ligerRadioGroup();
					break;
				case "checkboxGroup":
					manager = ele.ligerCheckBoxGroup();
					break;
				case "spinner":
					manager = ele.ligerSpinner();
					break;
				case "date":
					manager = ele.ligerDateEditor();
					break;
				case "text":
					manager = ele.ligerTextBox();
					break;
				}
			} else if (ele.is(":radio")) {
				if (ltype == "radioGroup") {
					if (ele.attr("groupChild") == "true") {
						return;
					}
					manager = ele.ligerRadioGroup();
				} else {
					manager = ele.ligerRadio();
				}
			} else if (ele.is(":checkbox")) {
				if (ltype == "checkboxGroup") {
					if (ele.attr("groupChild") == "true") {
						return;
					}
					manager = ele.ligerCheckBoxGroup();
				} else {
					manager = ele.ligerCheckBox();
				}
			} else if (ele.is("select") && !ltype) { //2013-9-11 上午9:29 lybide 原生下拉框
				if (v !== undefined) {
					ele.val(v);
				}
				return;
			}
			if (manager) {
				if (v !== undefined) manager.setValue(v);
			} else {
				ele.val(v);
			}
		});
	};
	$.fn.setDivValues = function(data, t) {
		if (data == undefined) return;

		$("div.input", t).each(function() {
			var ele = $(this);
			var name = ele.attr("name");
			if (!name) return;
			var v = parseName(name, data);
			if (v === undefined || v=="null") return;
			$(this).setDivValue(v);
		});

	};
	$.fn.setDivValue = function(v) {
		var ele = $(this);
		var xtype = ele.attr("xtype");
		//        var name = ele.attr("name");
		//        if (!name)return;
		//        var v = parseName(name, data);
		//        if (v === undefined) return;
		if (v === null) {
			ele.html("&nbsp;");
			return;
		}
		if (xtype == "combobox" || xtype == "select" || xtype == "checkboxGroup" || xtype == "radioGroup") {
			var ligerData = ele.data("ligerui");
			if (ligerData.tree) {
				ligerData.data = ligerData.tree.data || ligerData.data;
			}
			var bindData = ligerData.data;
			if (bindData) {
				v = parseValues(v, ligerData);
			}
		} else if (xtype == "date") {
			if (isNaN(v)) {
				var flength = ele.data("format").length;
				if (v.length > flength) {
					v = v.substring(0, flength);
				}
			} else {
				v = $.kh.getFormatDate(new Date(v), ele.data("format"));
			}
		} else if (xtype == "spinner") { //2013-9-12 下午4:21 lybide
			var ligerData = ele.data("ligerui");
			///todo modify by jyl
			if(v!=''&&v!=null&&v!=undefined){
				if (ligerData.type == "int") {
					v = parseInt(v);
				} else if (ligerData.type == "float") {
					v = new Number(v).toFixed(ligerData.decimalplace || 2);
				}
			}
		}
		if (isNaN(v)) {
			ele.html(v == "" ? "&nbsp;": v.replace(/\n/g, "<br>"));
		} else {
			ele.html(v + "&nbsp;");
		}

	};

	$.transValues = function(datas) {
		var dataArray = [];
		$.each(datas,
		function(index, d) {
			if ($.isArray(d)) {
				$.each(d,
				function(i, dd) {
					dataArray.push({
						name: index,
						value: dd
					});
				});
			} else {
				dataArray.push({
					name: index,
					value: d
				});
			}
		});
		return dataArray;
	}

	$.fn.activeCurrentTab = function() { //邵林添加：激活当前元素所在的tab
		var currentObject = $(this);
		var divTabObj = currentObject.parents("div .navtab");
		if (divTabObj.length > 0) { //当前元素是否在一个Tab里面
			var ligerTab = divTabObj.ligerTab(); //实例化Tab；
			var selectedTabItemId = ligerTab.getSelectedTabItemID(); //当前选中的Tab的id
			if (currentObject.parents("div[tabid='" + selectedTabItemId + "']").get(0) === undefined) {
				//如果当前元素不在当前选中的Tab里面，则激活当前元素所在的Tab
				var thisTabItemId = currentObject.parents("div[tabid]").attr("tabid");
				ligerTab.selectTabItem(thisTabItemId);
			}
		}
	}
	$.fn.unmask = function() {
		var id = $(this).attr("mask_id");
		$("#" + id).hide();
		$("#" + id + "_loading").hide();
	}
	$.fn.mask = function(title) {
		var $this = this;
		title = title || "正在加载数据，请稍候……";
		this.id = "_global_mask_div";
		$(this).attr("mask_id", this.id);
		if ($("#" + this.id).size() > 0) {
			$("#" + this.id).show();
			$("#" + this.id + "_loading").html(title).show();
			//$("#"+this.id+"_text").html(title);
			return;
		}
		this.maskDiv = function() {
			var wnd = $(window),
			doc = $(document);
			if (wnd.height() > doc.height()) {
				wHeight = wnd.height();
			} else {
				wHeight = doc.height();
			}
			//创建遮罩背景
			$("body").append("<div id='" + this.id + "' style='width:100%;'></div>");
			//$("#" + this.id).height(wHeight).css({position:"absolute", top:"0px", left:"0px", background:"#fff", filter:"Alpha(opacity=90);", opacity:"0.3", zIndex:"10000", display:"block"});
			$("#" + this.id).height(wHeight).addClass("l-mask-bg");
		}
		this.sPosition = function(obj) {
			var w, h;
			var mw = obj.outerWidth(true);
			var mh = obj.outerHeight(true);
			var ww = $(window).width();
			var wh = $(window).height();
			var body = $("body");
			var bw = body.width();
			var bh = body.height();
			var st = body.scrollTop();
			var sl = body.scrollLeft();
			w = bw > ww ? bw: ww;
			h = bh > wh ? bh: wh;
			var top = 0;
			var left = 0;
			left = sl > ww ? sl: 0;
			top = st > wh ? st: 0;

			var mTop = parseInt(top + ((h - mh) / 2)); //计算上边距
			var mLeft = parseInt(left + ((w - mw) / 2)); //计算左边距
			return {
				w: w,
				h: h,
				top: mTop,
				left: mLeft
			}; //13-5-2 下午3:07 lybide
			var MyDiv_w = parseInt(obj.width());
			var MyDiv_h = parseInt(obj.height());
			var width = parseInt($(document).width());
			var height = parseInt($(window).height());
			var left = parseInt($(document).scrollLeft());
			var top = parseInt($(document).scrollTop());

			var Div_topposition = top + (height / 2) - (MyDiv_h / 2); //计算上边距
			var Div_leftposition = left + (width / 2) - (MyDiv_w / 2); //计算左边距
			return [Div_topposition, Div_leftposition];
		}
		//this.maskDiv();
		var obj = $("<div class='l-mask-text' id='" + this.id + "_loading'>" + title + "</div>");
		$("body").append(obj);
		obj.show();
		var pos = this.sPosition(obj);
		obj.css({
			top: pos.top + "px",
			left: pos.left + "px"
		});

		var mask = $("<div id='" + this.id + "' class='l-mask-bg'></div>");
		$("body").append(mask);
		mask.width(pos.w);
		mask.height(pos.h);
		var fsdw = 0;
		$(window).resize(function() { //13-5-2 下午3:50 lybide
			if ($("#" + $this.id).is(":visible") == true) {
				var pos = $this.sPosition(obj);
				obj.css({
					top: pos.top + "px",
					left: pos.left + "px"
				});
				mask.width(pos.w);
				mask.height(pos.h);

				fsdw++;
			}
		});
		return this;
	}

	//公用删除功能
	$.del = function(des, url, data, tips) {
		$.ligerDialog.confirm(des,
		function(yes) {
			if (yes) {
				$.post(url, data,function(data) {
					if (data.success) {
						Qm.refreshGrid();
						top.$.notice(tips || "删除成功！",3);
					} else {
						var msg = "删除失败！";
						if (data.msg) {
							msg += "<br>" + data.msg;
						} else if (data.data) {
							msg += "<br>" + data.data;
						}
						top.$.dialog.notice({
							icon: 'k/kui/images/icons/dialog/32X32/fail.png',
							content: msg
						});
					}
				},
				"json");
			}
		});
	};
})(jQuery);

(function($) {
	//13-1-30 上午10:55 lybide
	//控制详情页面，窗口改变时，页面相应元素一起改变高度，大小。
	//13-1-31 上午11:17 lybide
	var cSizeLoadCount = 0;
	$.cSize = function(arg) {
		if (PAGE_KEYS == "sysMain") {
			return;
		}
		if (arg && cSizeLoadCount > 0) {
			return;
		}
		cSizeLoadCount++; //alert(cSizeLoadCount);

		var body = $("body");
		var bigTab = $(">.navtab", body);//alert(bigTab.size());
		var nav;
		if (typeof liger !== "undefined") {
			//nav = $(".navtab").ligerTab();
			nav = $(".navtab").ligerGetTabManager();
		}
		var cs = (parseInt(body.css("paddingTop")) + parseInt(body.css("paddingBottom")) + parseInt(body.css("marginTop")) + parseInt(body.css("marginBottom")));
		var h1 = $(window).height() - cs;
		var titleTm = $(".title-tm:visible", body);
		if (titleTm.length > 0) {
			//h1-=titleTm.outerHeight(true) || 0;
			$.each(titleTm,
			function() {
				h1 -= $(this).outerHeight(true);
			})
		}
		var itemTm = $(".item-tm:visible", body);
		if (itemTm.length > 0) {
			$.each(itemTm,
			function() {
				var $this = $(this);
				h1 -= $this.outerHeight(true);
				//2013-5-25 下午3:31 lybide
				if ($this.children().is("fieldset")) { //有fieldset，fieldset有margin
					h1 -= parseInt($this.children().css("margin-top")) + parseInt($this.children().css("margin-bottom"));
				}
			})
		}

		var bigTabLen = bigTab.length;
		if (bigTabLen > 0) {
			if (nav) {
				nav.setHeight(h1);
			}
			//$(".l-tab-content-item").css({"overflow":"auto"});
			//h1=bigTab.find(".l-tab-content").height() || 0;
			h1 -= bigTab.find(":visible>.toolbar-tm").height() || 0;
			h1 -= bigTab.find(".l-tab-links").height() || 0;
			//$("li[tabid=tabitem1] a").html(h1);
			bigTab.find(":visible>.scroll-tm").height(h1).css({
				"overflow": "auto"
			});
		} else {
			var toolbarTm = $(".toolbar-tm", body);
			if (toolbarTm.length > 0) {
				h1 -= toolbarTm.outerHeight(true) || 0;
			}
			$(".scroll-tm").height(h1);
			//var nav = $(".navtab").ligerTab();
			//$("button[ligeruiid=save]").html(h1);
			if (nav) {
				nav.setHeight(h1);
				//$(".scroll-tm").css({"overflow":"hidden"});
				$(".l-tab-content-item").css({
					"overflow": "auto"
				});

			}
		}
		//解决>ie8滚动条的问题。2013-7-12 下午4:59 lybide
		var bc = BROWSER_INFO;
		if (bc.ie) {
			$("iframe").each(function(i) {
				$(this).parent(".scroll-tm,.l-tab-content-item").css({
					"overflow": "hidden"
				});
			});
		}
	};
})(jQuery);

//jQuery页面所有元素载入后执行事件
$(window).load(function() {
	//$(function(){
	$(window).resize(function() {
		$.cSize();
	});
	$.cSize(true);
});

function createPageTitle() {
	//列表标题与说明 2013-8-27 上午11:52 lybide
	//============================================================================
	var titleObjId="#titleElement";
	var titleObj = $(titleObjId);
	if (titleObj.length < 0) {
		return;
	}
	/*var note=$("#titleElementNote").html();//Qm.config.titleElement.note ||
	 var text=$("#titleElementText").html();//Qm.config.pageTitle Qm.config.titleElement.text ||
	 var icon=$("#titleElement").attr("icon");//k/kui/images/icons/32/shopping.png  Qm.config.titleElement.icon ||
	 if (!note || !text || !icon) {
	 return;
	 }*/
	pageTitleBuild(titleObjId)
	//============================================================================
}

//2014-2-17 11:00 lybide 初始化标题
function pageTitleBuild(titleObjId){
	var gtSize = function() {
		if (typeof Qm !== "undefined") {
			var g = Qm.getQmgrid();
			g._onResize();
		}
		$.cSize();
	}
	var titleObj = $(titleObjId);
	if (titleObj.length < 0) {
		return;
	}
	//alert(PAGE_KEYS);
	//var d1=$('<div class="item-tm" id="lPageMain1" style="cursor:default;"></div>');
	//d1.append(pageTitle({to:null,text:text,icon:icon,note:note,style:"1"})).children().append('<div id="plClose1" title="关闭页面标题" class="l-page-title1-bs" style="display:none;"><div></div></div>');
	var d1 = $('<div id="plClose1" title="关闭页面标题" class="l-page-title-bs" style="display:none;"><div></div></div>');
	titleObj.append(d1);
	titleObj.hover(function() {
			var $this = $(this);
			$("#plClose1").show(); //.animate({"width":"show","height":"show","opacity":"show"}, "400");
		},
		function() {
			var $this = $(this);
			$("#plClose1").hide();
		});

	var gts = $(".l-page-title-note", titleObj);
	if (gts.length > 0) {
		gts.parent().addClass("has-note");
	} else {
		$(".l-page-title", titleObj).addClass("no-note");
	}

	var gts = $(".l-page-title-icon", titleObj);
	if (gts.length > 0) {
		gts.parent().addClass("has-icon");
	} else {
		$(".l-page-title", titleObj).addClass("no-icon");
	}

	var gts = $(".l-page-title-text", titleObj);
	if (gts.length > 0) {
		gts.parent().addClass("has-text");
	} else {
		$(".l-page-title", titleObj).addClass("no-text");
	}

	//$(".l-page-title-note",titleObj).parent().addClass("has-note");
	//$(".l-page-title-icon",titleObj).parent().addClass("has-icon");
	//$(".l-page-title-text",titleObj).parent().addClass("has-text");
	//$("#__qmtoolbar").before(d1);
	//$("body").prepend(d1);
	var d2 = $('<div id="lPageDs1" title="打开页面标题" class="l-page-title-ds" style="display:none;"><div></div></div>').click(function() {
		$(titleObjId+",div[isTitle=true]").show();
		$("#lPageDs1").hide();
		gtSize();
	});
	$("body").prepend(d2);
	$("#plClose1").click(function() {
		$(titleObjId+",div[isTitle=true]").hide();
		$("#lPageDs1").show();
		gtSize();
		return;
	});
	//2017年07月02日 19:09:53 星期日 lybide for lasa
	if (titleObj.hasClass("hide")) {
		try{
			$("#plClose1").click();
		}catch (e){}
	}
}

//在页面上输出标题
//13-5-2 下午1:50 lybide
//pageTitle({to:"#title",text:"软件开发管理系统——编辑",note:'<a href="javascript:void(0);">点击操作1</a><a href="javascript:void(0);">点击操作2</a><a href="javascript:void(0);">点击操作3</a><a href="javascript:void(0);">点击操作4</a><a href="javascript:void(0);">点击操作5</a><a href="javascript:void(0);">点击操作6</a>这里是副标题，可带操作说明',icon:"k/kui/images/icons/32/places.png"});
function pageTitle(options) {
	var defaults = {
		text: "页面标题",
		note: "",
		icon: "",
		iconCls: "",
		to: "body",
		show:true,
		style: "" //采用模板，空或1，2，3，4，5，6
	};
	var o = $.extend({},defaults, options);
	var class1 = "l-page-title" + o.style;
	var lpt = $('<div class="' + class1 + '"><div class="' + class1 + '-div"></div></div>');
	if (o.text) {
		lpt.append('<div class="' + class1 + '-text"><h1>' + o.text + '</h1></div>');
	} else {
		//lpt.addClass("no-text");
	}
	if (o.note) {
		lpt.append('<div class="' + class1 + '-note">' + o.note + '</div>');
		//lpt.addClass("has-note");
	} else {
		//lpt.addClass("no-note");
	}
	if (o.icon || o.iconCls) {
		if (o.icon) {
			lpt.append('<div class="' + class1 + '-icon"><img src="' + o.icon + '" border="0"></div>');
		}
		if (o.iconCls) {
			lpt.append('<div class="' + class1 + '-icon '+iconCls+'"></div>');
		}
		//lpt.addClass("has-icon");
	} else {
		//lpt.addClass("no-icon");
	}
	if (o.to) {
		$(o.to).append(lpt);
		pageTitleBuild((o.to));
	}
	if (!o.show) {
		$("#plClose1").click();
	}

	return lpt;
};

var __tree_dlg;
//弹出新窗口选择树形数据
//data为备选的树形数据
function showTreeDialog(dialogTreeData, isCheckbox, isCheckParent, callback) {
	if (__tree_dlg) {
		__tree_dlg.show();
		$("#dialogComboTree").ligerGetTreeManager().clear();
	} else {
		__tree_dlg = $.ligerDialog.open({
			title: '选择',
			width: 350,
			height: 300,
			content: "<div id='dialogComboTree'></div>",
			buttons: [{
				text: '确定',
				onclick: function(item, dialog) {
					var treeManager = $("#dialogComboTree").ligerGetTreeManager();
					var rstData = isCheckbox ? treeManager.getChecked() : treeManager.getSelected();
					if (!isCheckParent && treeManager.hasChildren(rstData.data)) {
						$.ligerDialog.warn("请选择叶节点！");
						return;
					}
					if (rstData) {
						callback(rstData.data);
						dialog.hide();
					} else {
						$.ligerDialog.warn("您没有选择任何数据！");
						dialog.hide();
					}
				}
			},
			{
				text: '取消',
				onclick: function(item, dialog) {
					dialog.hide();
				}
			}]
		});
	}
	$("#dialogComboTree").ligerTree({
		data: dialogTreeData,
		checkbox: isCheckbox,
		idFieldName: 'id',
		parentIDFieldName: 'pid'
	});
}

var getDateBBUtil = function(d) {
	this.date = function() { //13-5-7 上午10:49 lybide
		var rd;
		if (d) {
			var ds = d.split('-');
			rd = new Date(ds[0], ds[1] - 1, ds[2]);
		} else {
			rd = new Date();
		}
		return rd;
	};

	this.getCurrentDate = function() {
		return this.date();
	};
	/***
	 * 获得当前时间
	 */
	/*this.getCurrentDate = function () {
		return new Date();
	};*/
	/***
	 * 获得本周起止时间
	 */
	this.getCurrentWeek = function() {
		//起止日期数组
		var startStop = new Array();
		//获取当前时间
		var currentDate = this.getCurrentDate();
		//返回date是一周中的某一天
		var week = currentDate.getDay();
		//返回date是一个月中的某一天
		var month = currentDate.getDate();

		//一天的毫秒数
		var millisecond = 1000 * 60 * 60 * 24;
		//减去的天数
		var minusDay = week != 0 ? week - 1 : 6;
		//alert(minusDay);
		//本周 周一
		var monday = new Date(currentDate.getTime() - (minusDay * millisecond));
		//本周 周日
		var sunday = new Date(monday.getTime() + (6 * millisecond));
		//添加本周时间
		startStop.push(monday); //本周起始时间
		//添加本周最后一天时间
		startStop.push(sunday); //本周终止时间
		//返回
		return startStop;
	};

	/***
	 * 获得本月的起止时间
	 */
	this.getCurrentMonth = function() {
		//起止日期数组
		var startStop = new Array();
		//获取当前时间
		var currentDate = this.getCurrentDate();
		//获得当前月份0-11
		var currentMonth = currentDate.getMonth();
		//获得当前年份4位年
		var currentYear = currentDate.getFullYear();
		//求出本月第一天
		var firstDay = new Date(currentYear, currentMonth, 1);

		//当为12月的时候年份需要加1
		//月份需要更新为0 也就是下一年的第一个月
		if (currentMonth == 11) {
			currentYear++;
			currentMonth = 0; //就为
		} else {
			//否则只是月份增加,以便求的下一月的第一天
			currentMonth++;
		}

		//一天的毫秒数
		var millisecond = 1000 * 60 * 60 * 24;
		//下月的第一天
		var nextMonthDayOne = new Date(currentYear, currentMonth, 1);
		//求出上月的最后一天
		var lastDay = new Date(nextMonthDayOne.getTime() - millisecond);

		//添加至数组中返回
		startStop.push(firstDay);
		startStop.push(lastDay);
		//返回
		return startStop;
	};

	/**
	 * 得到本季度开始的月份
	 * @param month 需要计算的月份
	 ***/
	this.getQuarterSeasonStartMonth = function(month) {
		var quarterMonthStart = 0;
		var spring = 0; //春
		var summer = 3; //夏
		var fall = 6; //秋
		var winter = 9; //冬
		//月份从0-11
		if (month < 3) {
			return spring;
		}

		if (month < 6) {
			return summer;
		}

		if (month < 9) {
			return fall;
		}

		return winter;
	};

	/**
	 * 获得该月的天数
	 * @param year年份
	 * @param month月份
	 * */
	this.getMonthDays = function(year, month) {
		//本月第一天 1-31
		var relativeDate = new Date(year, month, 1);
		//获得当前月份0-11
		var relativeMonth = relativeDate.getMonth();
		//获得当前年份4位年
		var relativeYear = relativeDate.getFullYear();

		//当为12月的时候年份需要加1
		//月份需要更新为0 也就是下一年的第一个月
		if (relativeMonth == 11) {
			relativeYear++;
			relativeMonth = 0;
		} else {
			//否则只是月份增加,以便求的下一月的第一天
			relativeMonth++;
		}
		//一天的毫秒数
		var millisecond = 1000 * 60 * 60 * 24;
		//下月的第一天
		var nextMonthDayOne = new Date(relativeYear, relativeMonth, 1);
		//返回得到上月的最后一天,也就是本月总天数
		return new Date(nextMonthDayOne.getTime() - millisecond).getDate();
	};

	/**
	 * 获得本季度的起止日期
	 */
	this.getCurrentSeason = function() {
		//起止日期数组
		var startStop = new Array();
		//获取当前时间
		var currentDate = this.getCurrentDate();
		//获得当前月份0-11
		var currentMonth = currentDate.getMonth();
		//获得当前年份4位年
		var currentYear = currentDate.getFullYear();
		//获得本季度开始月份
		var quarterSeasonStartMonth = this.getQuarterSeasonStartMonth(currentMonth);
		//获得本季度结束月份
		var quarterSeasonEndMonth = quarterSeasonStartMonth + 2;

		//获得本季度开始的日期
		var quarterSeasonStartDate = new Date(currentYear, quarterSeasonStartMonth, 1);
		//获得本季度结束的日期
		var quarterSeasonEndDate = new Date(currentYear, quarterSeasonEndMonth, this.getMonthDays(currentYear, quarterSeasonEndMonth));
		//加入数组返回
		startStop.push(quarterSeasonStartDate);
		startStop.push(quarterSeasonEndDate);
		//返回
		return startStop;
	};

	/***
	 * 得到本年的起止日期
	 *
	 */
	this.getCurrentYear = function() {
		//起止日期数组
		var startStop = new Array();
		//获取当前时间
		var currentDate = this.getCurrentDate();
		//获得当前年份4位年
		var currentYear = currentDate.getFullYear();

		//本年第一天
		var currentYearFirstDate = new Date(currentYear, 0, 1);
		//本年最后一天
		var currentYearLastDate = new Date(currentYear, 11, 31);
		//添加至数组
		startStop.push(currentYearFirstDate);
		startStop.push(currentYearLastDate);
		//返回
		return startStop;
	};

	/**
	 * 返回上一个月的第一天Date类型
	 * @param year 年
	 * @param month 月
	 **/
	this.getPriorMonthFirstDay = function(year, month) {
		//年份为0代表,是本年的第一月,所以不能减
		if (month == 0) {
			month = 11; //月份为上年的最后月份
			year--; //年份减1
			return new Date(year, month, 1);
		}
		//否则,只减去月份
		month--;
		return new Date(year, month, 1);;
	};

	/**
	 * 获得上一月的起止日期
	 * ***/
	this.getPreviousMonth = function() {
		//起止日期数组
		var startStop = new Array();
		//获取当前时间
		var currentDate = this.getCurrentDate();
		//获得当前月份0-11
		var currentMonth = currentDate.getMonth();
		//获得当前年份4位年
		var currentYear = currentDate.getFullYear();
		//获得上一个月的第一天
		var priorMonthFirstDay = this.getPriorMonthFirstDay(currentYear, currentMonth);
		//获得上一月的最后一天
		var priorMonthLastDay = new Date(priorMonthFirstDay.getFullYear(), priorMonthFirstDay.getMonth(), this.getMonthDays(priorMonthFirstDay.getFullYear(), priorMonthFirstDay.getMonth()));
		//添加至数组
		startStop.push(priorMonthFirstDay);
		startStop.push(priorMonthLastDay);
		//返回
		return startStop;
	};

	/**
	 * 获得上一周的起止日期
	 * **/
	this.getPreviousWeek = function() {
		//起止日期数组
		var startStop = new Array();
		//获取当前时间
		var currentDate = this.getCurrentDate();
		//返回date是一周中的某一天
		var week = currentDate.getDay();
		//返回date是一个月中的某一天
		var month = currentDate.getDate();
		//一天的毫秒数
		var millisecond = 1000 * 60 * 60 * 24;
		//减去的天数
		var minusDay = week != 0 ? week - 1 : 6;
		//获得当前周的第一天
		var currentWeekDayOne = new Date(currentDate.getTime() - (millisecond * minusDay));
		//上周最后一天即本周开始的前一天
		var priorWeekLastDay = new Date(currentWeekDayOne.getTime() - millisecond);
		//上周的第一天
		var priorWeekFirstDay = new Date(priorWeekLastDay.getTime() - (millisecond * 6));

		//添加至数组
		startStop.push(priorWeekFirstDay);
		startStop.push(priorWeekLastDay);

		return startStop;
	};

	/**
	 * 得到上季度的起始日期 有问题 删除
	 * year 这个年应该是运算后得到的当前本季度的年份
	 * month 这个应该是运算后得到的当前季度的开始月份
	 * */
	/*this.getPriorSeasonFirstDay = function(year, month) {
		var quarterMonthStart = 0;
		var spring = 0; //春
		var summer = 3; //夏
		var fall = 6; //秋
		var winter = 9; //冬
		//月份从0-11
		switch (month) { //季度的其实月份
		case spring:
			//如果是第一季度则应该到去年的冬季
			year--;
			month = winter;
			break;
		case summer:
			month = spring;
			break;
		case fall:
			month = summer;
			break;
		case winter:
			month = fall;
			break;

		}
		return new Date(year, month, 1);
	};*/

	/**
	 * 得到上季度的起始日期
	 * year 这个年应该是运算后得到的当前本季度的年份
	 * month 这个应该是运算后得到的当前季度的开始月份
	 * */
	this.getPriorSeasonFirstDay = function(year, month) {
		var cs = parseInt(month/3)+(parseInt(month%3)>0?1:0);
		if(cs==1){
			month = 9;
			year--;
		}else{
			month = (cs - 1) * 3 - 3;
		}
		return new Date(year, month, 1);
	};

	/**
	 * 得到上季度的起止日期
	 * **/
	this.getPreviousSeason = function() {
		//起止日期数组
		var startStop = new Array();
		//获取当前时间
		var currentDate = this.getCurrentDate();
		//获得当前月份0-11
		var currentMonth = currentDate.getMonth();
		//获得当前年份4位年
		var currentYear = currentDate.getFullYear();
		//上季度的第一天
		var priorSeasonFirstDay = this.getPriorSeasonFirstDay(currentYear, currentMonth);
		//上季度的最后一天
		var priorSeasonLastDay = new Date(priorSeasonFirstDay.getFullYear(), priorSeasonFirstDay.getMonth() + 2, this.getMonthDays(priorSeasonFirstDay.getFullYear(), priorSeasonFirstDay.getMonth() + 2));
		//添加至数组
		startStop.push(priorSeasonFirstDay);
		startStop.push(priorSeasonLastDay);
		return startStop;
	};

	/**
	 * 得到去年的起止日期
	 * **/
	this.getPreviousYear = function() {
		//起止日期数组
		var startStop = new Array();
		//获取当前时间
		var currentDate = this.getCurrentDate();
		//获得当前年份4位年
		var currentYear = currentDate.getFullYear();
		currentYear--;
		var priorYearFirstDay = new Date(currentYear, 0, 1);
		var priorYearLastDay = new Date(currentYear, 11, 31);
		//添加至数组
		startStop.push(priorYearFirstDay);
		startStop.push(priorYearLastDay);
		return startStop;
	};
};

//2015年8月17日 11:25:28 lybide
function pagesEnd(arg) {
	var pageStatus=$("head").attr("pageStatus");
	if (pageStatus!="detail") {
		var stm=$("div.scroll-tm");
		if (stm.length>0) {
			stm.scroll(function(e){
				pageMouseBindClick(e);
			});
		}
		var stm=$("div.l-tab-content-item");
		if (stm.length>0) {
			stm.scroll(function(e){
				pageMouseBindClick(e);
			});
		}
		//alert(document.body.scrollHeight>$(window).height());
		if (document.body.scrollHeight>$(window).height()) {
			$(window).scroll(function(e){
				pageMouseBindClick(e);
			});
		}
	}
};

function pageMouseBindClick(e) {
	/*var bws=$("body > .l-box-panel:visible");//2015年8月14日 18:14:57 lybide
	console.log(bws.size());
	if (bws.size()==0) {
		return;
	}*/
	//console.log($.ligerui.boxPanelIsShow);
	//2015年8月11日 10:18:31 监听 boxPanelIsShow 变量
	if (!$ || !$.ligerui || !$.ligerui.boxPanelIsShow) {
		return;
	}
	//console.log("有弹出的选择层，l-box-panel，需要隐藏");
	var obj = (e.target || e.srcElement);
	var mObj = $.ligerui.managers; //console.log($.ligerui.managers);
	var loop1=0;
	for (var iGts in mObj) {loop1++;
		var g = mObj[iGts];var p= g.options;
		if (g["type"] == "DateEditor" && g.dateeditor.is(":visible") && g.editorToggling == false) {
			//g.toggleDateEditor(true);
			g.dateeditor.stop().hide();g.editorToggling=true;$.ligerui.boxPanelIsShow=false;
		}
		//2013-5-17 下午10:49 lybide
		if (g["type"] == "ComboBox" && g.selectBox.is(":visible") && g.boxToggling == false) {
			//g._toggleSelectBox(true);
			g.selectBox.stop().hide();g.boxToggling=true;$.ligerui.boxPanelIsShow=false;
		}
	};
	//console.log(loop1);
};

//2014-7-23 下午4:05 lybide
$(function(){//绑定鼠标滚动事件
	//2013-5-17 下午5:45 lybide
	//2013-12-11 上午10:10 lybide 解决下拉框不选择项时，点击其它不会隐藏下拉框。添加 editorToggling boxToggling 判断
	//2015年8月17日 17:12:02 lybide
	var pageStatus=$("head").attr("pageStatus");
	if (pageStatus=="detail") {
		return;
	}
	$("body").bind("click.boxPanel", function (e) {
		/*if (e.which != 1) {
			return;
		}*/
		pageMouseBindClick(e);
		//console.log(loop1);
	});

});