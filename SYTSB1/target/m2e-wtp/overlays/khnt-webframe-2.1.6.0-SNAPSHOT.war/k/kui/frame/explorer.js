/**
 * Created by lybide on 2014/11/12.
 */

//没有外壳时，初始化相应函数 2017年07月27日 12:06:16 星期四 lybide
function externalFunction() {
	var data=["WriteIni","PopupMessageEx","GetLastErrorMessage","DelTempFiles","UpdateFrame","ShowFrameWindow","SetWindowText","SetTrayMenu","SetWindowState","SetWindowPos","ReadIni","HideFrameWindow","Quit"];
	for (var i = 0, l=data.length; i < l; i++) {
		eval("var dadd=typeof window.external."+data[i]+"==\"undefined\"");
		if (dadd) {
			eval("window.external."+data[i]+"=function(){return true;}");
		}
	}
}
//externalFunction();//所有浏览器调试专用，在正式外壳程序中，必须关闭此函数。开启会造成ie浏览器崩溃。2017年07月27日 12:18:30 星期四 lybide

//外壳程序 2014/11/12 16:09 lybide
$(function () {
	createMainWindowButton();
});

function kShellShowAllMsgbox(){
	openExtDialog("pub/message/user_message_all.jsp?all=yes","查看消息",1000,550);
}

function kShellShowMsgbox(arg) {
	var sysMsgSwitch=kui["sysMsgSwitch"]!==false ? true : kui["sysMsgSwitch"];
	if (!sysMsgSwitch) {
		return;
	}
	//alert("查看消息");
	openExtMsgbox("pub/explorer/message.jsp","testkey");
	return;
	var ret = window.external.PopupMessageEx("testkey", kui["base"] + "pub/explorer/message.jsp", 2, 250, 150);
	if (!ret) {
		alert("WinKH_E1："+window.external.GetLastErrorMessage());
	}
};

function kShellClearCache(arg) {
	//alert("清除缓存");
	window.external.DelTempFiles();
};

function kShellIndividualitySetting(arg) {
	//alert("个性设置");
	/*
	 方法名称： PopupMessageEx()
	 函数原型：window.external.PopupMessageEx(key, url, style, width, height, timer);
	 参数说明： key -- 弹窗的查询关键字， 外壳内置一个map对象， 可以管理和容纳多个弹窗，该参数用于查询和控制弹窗
	 url - 弹窗的URL。
	 style - 样式表。 1-居中显示， 2-圆角显示。
	 */
	var ret = window.external.PopupMessageEx("w_user_individuality_setting", kui["base"] + "pub/explorer/user_individuality_setting.jsp", 1, 500, 380);
	if (!ret) {
		alert("WinKH_E2："+window.external.GetLastErrorMessage());
	}
};

function kShellSoftUpdate(arg) {
	window.external.UpdateFrame();
};

//显示主窗口 2014/12/3 9:47 lybide
function kShellShowFrame(arg) {
	window.external.ShowFrameWindow();
};

//外壳程序添加 关闭，最大化，最小化。2014年10月13日 15:51:19 星期一 lybide
function createMainWindowButton() {
	var b = getkIsShell();
	if (!b) {
		//return;
	}
	if (b) {
		//window.external.SetWindowState(2);
		if (PAGE_KEYS == "sysMain" || PAGE_KEYS == "login") {
			//window.external.SetWindowState(2);
			var ret = window.external.SetWindowText(kui["name"]);
			if (!ret) {
				alert("WinKH_E3："+window.external.GetLastErrorMessage());
			}
		} else {
			return;
		}
	}
	if (PAGE_KEYS == "login" && b) {
		var trayMenu = '[' +
			//'{"type": "cmd", "text": "显示主窗口", "value": "showframe"}, ' +
			'{"type": "js", "text": "显示主窗口", "value": "kShellShowFrame"}, ' +
			'{"type": "", "text": "", "value": ""}, ' +
			'{"type": "js", "text": "清除缓存", "value": "kShellClearCache"}, ' +
			'{"type": "js", "text": "软件升级", "value": "kShellSoftUpdate"}, ' +
			'{"type": "", "text": "", "value": ""}, ' +
			'{"type": "cmd", "text": "退出系统", "value": "quit"}' +
		']';
		//var json_string = JSON.stringify(trayMenu);
		var json_string=trayMenu;
		var ret = window.external.SetTrayMenu(json_string);
		if (!ret) {
			alert("WinKH_E4："+window.external.GetLastErrorMessage());
		}
	}
	//2014/11/12 21:41 lybide
	if (PAGE_KEYS == "sysMain" && b) {
		var sysMsgSwitch=kui["sysMsgSwitch"]!==false ? true : kui["sysMsgSwitch"];
		if (!sysMsgSwitch) {
			
		}
		var trayMenu = '[';
			//trayMenu+='{"type": "cmd", "text": "显示主窗口", "value": "showframe"}, ';
		trayMenu+='{"type": "js", "text": "显示主窗口", "value": "kShellShowFrame"}, ';
		trayMenu+='{"type": "", "text": "", "value": ""}, ';
		if (sysMsgSwitch) {
			trayMenu+='{"type": "js", "text": "查看最新消息", "value": "kShellShowMsgbox"}, ';
			trayMenu+='{"type": "js", "text": "查看历史消息", "value": "kShellShowAllMsgbox"}, ';
		}
		trayMenu+='{"type": "js", "text": "个性设置", "value": "kShellIndividualitySetting"}, ';
		trayMenu+='{"type": "js", "text": "清除缓存", "value": "kShellClearCache"}, ';
		trayMenu+='{"type": "js", "text": "软件升级", "value": "kShellSoftUpdate"}, ';
		var bbb=kui["trayMenuRightExt"];//alert(JSON.stringify(kui["trayMenuRightExt"]));
		if (bbb) {
			trayMenu+='{"type": "", "text": "", "value": ""}, ';
			for (var i = 0, l=bbb.length; i < l; i++) {
				var item=bbb[i];
				trayMenu+='{"type": "js", "text": "'+item["text"]+'", "value": "exeTrayMenuRightExt'+i+'"}, ';
				var str1='window["exeTrayMenuRightExt'+i+'"]=function(){openExtDialog("'+item["url"]+'","'+item["text"]+'","'+(item["width"] || "")+'","'+(item["height"] || "")+'");}';
				//alert(str1)
				eval(str1);
			}
		}
		trayMenu+='{"type": "", "text": "", "value": ""}, ';
		trayMenu+='{"type": "cmd", "text": "退出系统", "value": "quit"}';
		trayMenu+=']';
		//var json_string = JSON.stringify(trayMenu);
		var json_string=trayMenu;
		var ret = window.external.SetTrayMenu(json_string);
		if (!ret) {
			alert("WinKH_E5："+window.external.GetLastErrorMessage());
		}
	}

	var bt1 = $('<div id="mTopbtn" class="windows-btn"><div id="mTopsm" class="windows-small" title="最小化">最小化</div><div id="mTopla" class="windows-large" title="还原" wState="1">还原</div><div id="mTopcl" class="windows-close" title="关闭">关闭</div></div>');
	$("html").addClass("window-is-explorer");
	$("body").append(bt1);
	if (typeof L_MENU_SKIN_NAME != "undefined") {
		if (L_MENU_SKIN_NAME == "1") {//default
			$("#mSystemItem").css({"right": "0px", "top": "25px"});
		} else if (L_MENU_SKIN_NAME == "outlook") {//default2

		} else if (L_MENU_SKIN_NAME == "tree") {//tree
			$("#mSystemItem").css({"right": "0px", "top": "25px"});
		}
	}

	//$("#sysMain").addClass("border-1");
	//$("#sysMain").width($(window).width()-2);
	//$("#sysMain").height($(window).height()-2);

	//最小化效果
	$("#mTopsm").hover(
		function () {
			bt1.addClass("windows-smallover");
		},
		function () {
			bt1.removeClass("windows-smallover");
		}
	).click(function () {
		var _this = $(this);
		window.external.SetWindowState(1);
		return;
	});
	//向下还原效果
	$("#mTopla").hover(
		function () {
			bt1.addClass("windows-largeover");
		},
		function () {
			bt1.removeClass("windows-largeover");
		}
	).click(function () {
		var _this = $(this);
		var w1 = screen.availWidth, h1 = screen.availHeight;
		var wState = _this.attr("wState") == "1" ? "0" : "1";//alert(wState);
		if (wState == "0") {
			//还原
			_this.attr("title", "最大化");
			var zdw1 = 990, zdh1 = 600;
			window.external.SetWindowPos((w1-zdw1)/2, (h1-zdh1)/2, zdw1, zdh1);
			//window.external.SetWindowState(0);
			//window.external.CenterWindow();
			$("#mTopbtn").addClass("restore");
		} else {
			//最大化
			_this.attr("title", "还原");
			//alert(234234234);
			window.external.SetWindowPos(0, 0, w1, h1);
			//window.external.SetWindowState(2);
			$("#mTopbtn").removeClass("restore");
		}
		_this.attr("wState", wState);
		//$(window).resize();
	});
	// 关闭效果
	$("#mTopcl").hover(
		function () {
			bt1.addClass("windows-closeover");
		},
		function () {
			bt1.removeClass("windows-closeover");
		}
	).click(function () {
				var softCloseStatus = window.external.ReadIni("user", "softCloseStatus", "confing.user.ini");
				if (softCloseStatus=="1") {
					window.external.HideFrameWindow();
				} else {
					if (window.confirm("是否确认退出")) {
						window.external.Quit();
			}
		}
	});
}

function Base64() {

	// private property
	_keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";

	// public method for encoding
	this.encode = function (input) {
		var output = "";
		var chr1, chr2, chr3, enc1, enc2, enc3, enc4;
		var i = 0;
		input = _utf8_encode(input);
		while (i < input.length) {
			chr1 = input.charCodeAt(i++);
			chr2 = input.charCodeAt(i++);
			chr3 = input.charCodeAt(i++);
			enc1 = chr1 >> 2;
			enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
			enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
			enc4 = chr3 & 63;
			if (isNaN(chr2)) {
				enc3 = enc4 = 64;
			} else if (isNaN(chr3)) {
				enc4 = 64;
			}
			output = output +
			_keyStr.charAt(enc1) + _keyStr.charAt(enc2) +
			_keyStr.charAt(enc3) + _keyStr.charAt(enc4);
		}
		return output;
	}

	// public method for decoding
	this.decode = function (input) {
		var output = "";
		var chr1, chr2, chr3;
		var enc1, enc2, enc3, enc4;
		var i = 0;
		input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");
		while (i < input.length) {
			enc1 = _keyStr.indexOf(input.charAt(i++));
			enc2 = _keyStr.indexOf(input.charAt(i++));
			enc3 = _keyStr.indexOf(input.charAt(i++));
			enc4 = _keyStr.indexOf(input.charAt(i++));
			chr1 = (enc1 << 2) | (enc2 >> 4);
			chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
			chr3 = ((enc3 & 3) << 6) | enc4;
			output = output + String.fromCharCode(chr1);
			if (enc3 != 64) {
				output = output + String.fromCharCode(chr2);
			}
			if (enc4 != 64) {
				output = output + String.fromCharCode(chr3);
			}
		}
		output = _utf8_decode(output);
		return output;
	}

	// private method for UTF-8 encoding
	_utf8_encode = function (string) {
		string = string.replace(/\r\n/g, "\n");
		var utftext = "";
		for (var n = 0; n < string.length; n++) {
			var c = string.charCodeAt(n);
			if (c < 128) {
				utftext += String.fromCharCode(c);
			} else if ((c > 127) && (c < 2048)) {
				utftext += String.fromCharCode((c >> 6) | 192);
				utftext += String.fromCharCode((c & 63) | 128);
			} else {
				utftext += String.fromCharCode((c >> 12) | 224);
				utftext += String.fromCharCode(((c >> 6) & 63) | 128);
				utftext += String.fromCharCode((c & 63) | 128);
			}

		}
		return utftext;
	}

	// private method for UTF-8 decoding
	_utf8_decode = function (utftext) {
		var string = "";
		var i = 0;
		var c = c1 = c2 = 0;
		while (i < utftext.length) {
			c = utftext.charCodeAt(i);
			if (c < 128) {
				string += String.fromCharCode(c);
				i++;
			} else if ((c > 191) && (c < 224)) {
				c2 = utftext.charCodeAt(i + 1);
				string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
				i += 2;
			} else {
				c2 = utftext.charCodeAt(i + 1);
				c3 = utftext.charCodeAt(i + 2);
				string += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
				i += 3;
			}
		}
		return string;
	}
}