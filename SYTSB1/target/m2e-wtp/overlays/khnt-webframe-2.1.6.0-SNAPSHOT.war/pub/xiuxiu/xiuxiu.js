var swfobject = function() {
	var D = "undefined", r = "object", S = "Shockwave Flash", W = "ShockwaveFlash.ShockwaveFlash", q = "application/x-shockwave-flash", R = "SWFObjectExprInst", x = "onreadystatechange", O = window, j = document, t = navigator, T = false, U = [ h ], o = [], N = [], I = [], l, Q, E, B, J = false, a = false, n, G, m = true, M = function() {
		var aa = typeof j.getElementById != D
				&& typeof j.getElementsByTagName != D
				&& typeof j.createElement != D, ah = t.userAgent.toLowerCase(), Y = t.platform
				.toLowerCase(), ae = Y ? /win/.test(Y) : /win/.test(ah), ac = Y ? /mac/
				.test(Y)
				: /mac/.test(ah), af = /webkit/.test(ah) ? parseFloat(ah
				.replace(/^.*webkit\/(\d+(\.\d+)?).*$/, "$1")) : false, X = !+"\v1", ag = [
				0, 0, 0 ], ab = null;
		if (typeof t.plugins != D && typeof t.plugins[S] == r) {
			ab = t.plugins[S].description;
			if (ab
					&& !(typeof t.mimeTypes != D && t.mimeTypes[q] && !t.mimeTypes[q].enabledPlugin)) {
				T = true;
				X = false;
				ab = ab.replace(/^.*\s+(\S+\s+\S+$)/, "$1");
				ag[0] = parseInt(ab.replace(/^(.*)\..*$/, "$1"), 10);
				ag[1] = parseInt(ab.replace(/^.*\.(.*)\s.*$/, "$1"), 10);
				ag[2] = /[a-zA-Z]/.test(ab) ? parseInt(ab.replace(
						/^.*[a-zA-Z]+(.*)$/, "$1"), 10) : 0
			}
		} else {
			if (typeof O.ActiveXObject != D) {
				try {
					var ad = new ActiveXObject(W);
					if (ad) {
						ab = ad.GetVariable("$version");
						if (ab) {
							X = true;
							ab = ab.split(" ")[1].split(",");
							ag = [ parseInt(ab[0], 10), parseInt(ab[1], 10),
									parseInt(ab[2], 10) ]
						}
					}
				} catch (Z) {
				}
			}
		}
		return {
			w3 : aa,
			pv : ag,
			wk : af,
			ie : X,
			win : ae,
			mac : ac
		}
	}(), k = function() {
		if (!M.w3) {
			return
		}
		if ((typeof j.readyState != D && j.readyState == "complete")
				|| (typeof j.readyState == D && (j.getElementsByTagName("body")[0] || j.body))) {
			f()
		}
		if (!J) {
			if (typeof j.addEventListener != D) {
				j.addEventListener("DOMContentLoaded", f, false)
			}
			if (M.ie && M.win) {
				j.attachEvent(x, function() {
					if (j.readyState == "complete") {
						j.detachEvent(x, arguments.callee);
						f()
					}
				});
				if (O == top) {
					(function() {
						if (J) {
							return
						}
						try {
							j.documentElement.doScroll("left")
						} catch (X) {
							setTimeout(arguments.callee, 0);
							return
						}
						f()
					})()
				}
			}
			if (M.wk) {
				(function() {
					if (J) {
						return
					}
					if (!/loaded|complete/.test(j.readyState)) {
						setTimeout(arguments.callee, 0);
						return
					}
					f()
				})()
			}
			s(f)
		}
	}();
	function f() {
		if (J) {
			return
		}
		try {
			var Z = j.getElementsByTagName("body")[0].appendChild(C("span"));
			Z.parentNode.removeChild(Z)
		} catch (aa) {
			return
		}
		J = true;
		var X = U.length;
		for (var Y = 0; Y < X; Y++) {
			U[Y]()
		}
	}
	function K(X) {
		if (J) {
			X()
		} else {
			U[U.length] = X
		}
	}
	function s(Y) {
		if (typeof O.addEventListener != D) {
			O.addEventListener("load", Y, false)
		} else {
			if (typeof j.addEventListener != D) {
				j.addEventListener("load", Y, false)
			} else {
				if (typeof O.attachEvent != D) {
					i(O, "onload", Y)
				} else {
					if (typeof O.onload == "function") {
						var X = O.onload;
						O.onload = function() {
							X();
							Y()
						}
					} else {
						O.onload = Y
					}
				}
			}
		}
	}
	function h() {
		if (T) {
			V()
		} else {
			H()
		}
	}
	function V() {
		var X = j.getElementsByTagName("body")[0];
		var aa = C(r);
		aa.setAttribute("type", q);
		var Z = X.appendChild(aa);
		if (Z) {
			var Y = 0;
			(function() {
				if (typeof Z.GetVariable != D) {
					var ab = Z.GetVariable("$version");
					if (ab) {
						ab = ab.split(" ")[1].split(",");
						M.pv = [ parseInt(ab[0], 10), parseInt(ab[1], 10),
								parseInt(ab[2], 10) ]
					}
				} else {
					if (Y < 10) {
						Y++;
						setTimeout(arguments.callee, 10);
						return
					}
				}
				X.removeChild(aa);
				Z = null;
				H()
			})()
		} else {
			H()
		}
	}
	function H() {
		var ag = o.length;
		if (ag > 0) {
			for (var af = 0; af < ag; af++) {
				var Y = o[af].id;
				var ab = o[af].callbackFn;
				var aa = {
					success : false,
					id : Y
				};
				if (M.pv[0] > 0) {
					var ae = c(Y);
					if (ae) {
						if (F(o[af].swfVersion) && !(M.wk && M.wk < 312)) {
							w(Y, true);
							if (ab) {
								aa.success = true;
								aa.ref = z(Y);
								ab(aa)
							}
						} else {
							if (o[af].expressInstall && A()) {
								var ai = {};
								ai.data = o[af].expressInstall;
								ai.width = ae.getAttribute("width") || "0";
								ai.height = ae.getAttribute("height") || "0";
								if (ae.getAttribute("class")) {
									ai.styleclass = ae.getAttribute("class")
								}
								if (ae.getAttribute("align")) {
									ai.align = ae.getAttribute("align")
								}
								var ah = {};
								var X = ae.getElementsByTagName("param");
								var ac = X.length;
								for (var ad = 0; ad < ac; ad++) {
									if (X[ad].getAttribute("name")
											.toLowerCase() != "movie") {
										ah[X[ad].getAttribute("name")] = X[ad]
												.getAttribute("value")
									}
								}
								P(ai, ah, Y, ab)
							} else {
								p(ae);
								if (ab) {
									ab(aa)
								}
							}
						}
					}
				} else {
					w(Y, true);
					if (ab) {
						var Z = z(Y);
						if (Z && typeof Z.SetVariable != D) {
							aa.success = true;
							aa.ref = Z
						}
						ab(aa)
					}
				}
			}
		}
	}
	function z(aa) {
		var X = null;
		var Y = c(aa);
		if (Y && Y.nodeName == "OBJECT") {
			if (typeof Y.SetVariable != D) {
				X = Y
			} else {
				var Z = Y.getElementsByTagName(r)[0];
				if (Z) {
					X = Z
				}
			}
		}
		return X
	}
	function A() {
		return !a && F("6.0.65") && (M.win || M.mac) && !(M.wk && M.wk < 312)
	}
	function P(aa, ab, X, Z) {
		a = true;
		E = Z || null;
		B = {
			success : false,
			id : X
		};
		var ae = c(X);
		if (ae) {
			if (ae.nodeName == "OBJECT") {
				l = g(ae);
				Q = null
			} else {
				l = ae;
				Q = X
			}
			aa.id = R;
			if (typeof aa.width == D
					|| (!/%$/.test(aa.width) && parseInt(aa.width, 10) < 310)) {
				aa.width = "310"
			}
			if (typeof aa.height == D
					|| (!/%$/.test(aa.height) && parseInt(aa.height, 10) < 137)) {
				aa.height = "137"
			}
			j.title = j.title.slice(0, 47) + " - Flash Player Installation";
			var ad = M.ie && M.win ? "ActiveX" : "PlugIn", ac = "MMredirectURL="
					+ encodeURI(window.location).toString()
							.replace(/&/g, "%26")
					+ "&MMplayerType="
					+ ad
					+ "&MMdoctitle=" + j.title;
			if (typeof ab.flashvars != D) {
				ab.flashvars += "&" + ac
			} else {
				ab.flashvars = ac
			}
			if (M.ie && M.win && ae.readyState != 4) {
				var Y = C("div");
				X += "SWFObjectNew";
				Y.setAttribute("id", X);
				ae.parentNode.insertBefore(Y, ae);
				ae.style.display = "none";
				(function() {
					if (ae.readyState == 4) {
						ae.parentNode.removeChild(ae)
					} else {
						setTimeout(arguments.callee, 10)
					}
				})()
			}
			u(aa, ab, X)
		}
	}
	function p(Y) {
		if (M.ie && M.win && Y.readyState != 4) {
			var X = C("div");
			Y.parentNode.insertBefore(X, Y);
			X.parentNode.replaceChild(g(Y), X);
			Y.style.display = "none";
			(function() {
				if (Y.readyState == 4) {
					Y.parentNode.removeChild(Y)
				} else {
					setTimeout(arguments.callee, 10)
				}
			})()
		} else {
			Y.parentNode.replaceChild(g(Y), Y)
		}
	}
	function g(ab) {
		var aa = C("div");
		if (M.win && M.ie) {
			aa.innerHTML = ab.innerHTML
		} else {
			var Y = ab.getElementsByTagName(r)[0];
			if (Y) {
				var ad = Y.childNodes;
				if (ad) {
					var X = ad.length;
					for (var Z = 0; Z < X; Z++) {
						if (!(ad[Z].nodeType == 1 && ad[Z].nodeName == "PARAM")
								&& !(ad[Z].nodeType == 8)) {
							aa.appendChild(ad[Z].cloneNode(true))
						}
					}
				}
			}
		}
		return aa
	}
	function u(ai, ag, Y) {
		var X, aa = c(Y);
		if (M.wk && M.wk < 312) {
			return X
		}
		if (aa) {
			if (typeof ai.id == D) {
				ai.id = Y
			}
			if (M.ie && M.win) {
				var ah = "";
				for ( var ae in ai) {
					if (ai[ae] != Object.prototype[ae]) {
						if (ae.toLowerCase() == "data") {
							ag.movie = ai[ae]
						} else {
							if (ae.toLowerCase() == "styleclass") {
								ah += ' class="' + ai[ae] + '"'
							} else {
								if (ae.toLowerCase() != "classid") {
									ah += " " + ae + '="' + ai[ae] + '"'
								}
							}
						}
					}
				}
				var af = "";
				for ( var ad in ag) {
					if (ag[ad] != Object.prototype[ad]) {
						af += '<param name="' + ad + '" value="' + ag[ad]
								+ '" />'
					}
				}
				aa.outerHTML = '<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"'
						+ ah + ">" + af + "</object>";
				N[N.length] = ai.id;
				X = c(ai.id)
			} else {
				var Z = C(r);
				Z.setAttribute("type", q);
				for ( var ac in ai) {
					if (ai[ac] != Object.prototype[ac]) {
						if (ac.toLowerCase() == "styleclass") {
							Z.setAttribute("class", ai[ac])
						} else {
							if (ac.toLowerCase() != "classid") {
								Z.setAttribute(ac, ai[ac])
							}
						}
					}
				}
				for ( var ab in ag) {
					if (ag[ab] != Object.prototype[ab]
							&& ab.toLowerCase() != "movie") {
						e(Z, ab, ag[ab])
					}
				}
				aa.parentNode.replaceChild(Z, aa);
				X = Z
			}
		}
		return X
	}
	function e(Z, X, Y) {
		var aa = C("param");
		aa.setAttribute("name", X);
		aa.setAttribute("value", Y);
		Z.appendChild(aa)
	}
	function y(Y) {
		var X = c(Y);
		if (X && X.nodeName == "OBJECT") {
			if (M.ie && M.win) {
				X.style.display = "none";
				(function() {
					if (X.readyState == 4) {
						b(Y)
					} else {
						setTimeout(arguments.callee, 10)
					}
				})()
			} else {
				X.parentNode.removeChild(X)
			}
		}
	}
	function b(Z) {
		var Y = c(Z);
		if (Y) {
			for ( var X in Y) {
				if (typeof Y[X] == "function") {
					Y[X] = null
				}
			}
			Y.parentNode.removeChild(Y)
		}
	}
	function c(Z) {
		var X = null;
		try {
			X = j.getElementById(Z)
		} catch (Y) {
		}
		return X
	}
	function C(X) {
		return j.createElement(X)
	}
	function i(Z, X, Y) {
		Z.attachEvent(X, Y);
		I[I.length] = [ Z, X, Y ]
	}
	function F(Z) {
		var Y = M.pv, X = Z.split(".");
		X[0] = parseInt(X[0], 10);
		X[1] = parseInt(X[1], 10) || 0;
		X[2] = parseInt(X[2], 10) || 0;
		return (Y[0] > X[0] || (Y[0] == X[0] && Y[1] > X[1]) || (Y[0] == X[0]
				&& Y[1] == X[1] && Y[2] >= X[2])) ? true : false
	}
	function v(ac, Y, ad, ab) {
		if (M.ie && M.mac) {
			return
		}
		var aa = j.getElementsByTagName("head")[0];
		if (!aa) {
			return
		}
		var X = (ad && typeof ad == "string") ? ad : "screen";
		if (ab) {
			n = null;
			G = null
		}
		if (!n || G != X) {
			var Z = C("style");
			Z.setAttribute("type", "text/css");
			Z.setAttribute("media", X);
			n = aa.appendChild(Z);
			if (M.ie && M.win && typeof j.styleSheets != D
					&& j.styleSheets.length > 0) {
				n = j.styleSheets[j.styleSheets.length - 1]
			}
			G = X
		}
		if (M.ie && M.win) {
			if (n && typeof n.addRule == r) {
				n.addRule(ac, Y)
			}
		} else {
			if (n && typeof j.createTextNode != D) {
				n.appendChild(j.createTextNode(ac + " {" + Y + "}"))
			}
		}
	}
	function w(Z, X) {
		if (!m) {
			return
		}
		var Y = X ? "visible" : "hidden";
		if (J && c(Z)) {
			c(Z).style.visibility = Y
		} else {
			v("#" + Z, "visibility:" + Y)
		}
	}
	function L(Y) {
		var Z = /[\\\"<>\.;]/;
		var X = Z.exec(Y) != null;
		return X && typeof encodeURIComponent != D ? encodeURIComponent(Y) : Y
	}
	var d = function() {
		if (M.ie && M.win) {
			window.attachEvent("onunload", function() {
				var ac = I.length;
				for (var ab = 0; ab < ac; ab++) {
					I[ab][0].detachEvent(I[ab][1], I[ab][2])
				}
				var Z = N.length;
				for (var aa = 0; aa < Z; aa++) {
					y(N[aa])
				}
				for ( var Y in M) {
					M[Y] = null
				}
				M = null;
				for ( var X in swfobject) {
					swfobject[X] = null
				}
				swfobject = null
			})
		}
	}();
	return {
		registerObject : function(ab, X, aa, Z) {
			if (M.w3 && ab && X) {
				var Y = {};
				Y.id = ab;
				Y.swfVersion = X;
				Y.expressInstall = aa;
				Y.callbackFn = Z;
				o[o.length] = Y;
				w(ab, false)
			} else {
				if (Z) {
					Z({
						success : false,
						id : ab
					})
				}
			}
		},
		getObjectById : function(X) {
			if (M.w3) {
				return z(X)
			}
		},
		embedSWF : function(ab, ah, ae, ag, Y, aa, Z, ad, af, ac) {
			var X = {
				success : false,
				id : ah
			};
			if (M.w3 && !(M.wk && M.wk < 312) && ab && ah && ae && ag && Y) {
				w(ah, false);
				K(function() {
					ae += "";
					ag += "";
					var aj = {};
					if (af && typeof af === r) {
						for ( var al in af) {
							aj[al] = af[al]
						}
					}
					aj.data = ab;
					aj.width = ae;
					aj.height = ag;
					var am = {};
					if (ad && typeof ad === r) {
						for ( var ak in ad) {
							am[ak] = ad[ak]
						}
					}
					if (Z && typeof Z === r) {
						for ( var ai in Z) {
							if (typeof am.flashvars != D) {
								am.flashvars += "&" + ai + "=" + Z[ai]
							} else {
								am.flashvars = ai + "=" + Z[ai]
							}
						}
					}
					if (F(Y)) {
						var an = u(aj, am, ah);
						if (aj.id == ah) {
							w(ah, true)
						}
						X.success = true;
						X.ref = an
					} else {
						if (aa && A()) {
							aj.data = aa;
							P(aj, am, ah, ac);
							return
						} else {
							w(ah, true)
						}
					}
					if (ac) {
						ac(X)
					}
				})
			} else {
				if (ac) {
					ac(X)
				}
			}
		},
		switchOffAutoHideShow : function() {
			m = false
		},
		ua : M,
		getFlashPlayerVersion : function() {
			return {
				major : M.pv[0],
				minor : M.pv[1],
				release : M.pv[2]
			}
		},
		hasFlashPlayerVersion : F,
		createSWF : function(Z, Y, X) {
			if (M.w3) {
				return u(Z, Y, X)
			} else {
				return undefined
			}
		},
		showExpressInstall : function(Z, aa, X, Y) {
			if (M.w3 && A()) {
				P(Z, aa, X, Y)
			}
		},
		removeSWF : function(X) {
			if (M.w3) {
				y(X)
			}
		},
		createCSS : function(aa, Z, Y, X) {
			if (M.w3) {
				v(aa, Z, Y, X)
			}
		},
		addDomLoadEvent : K,
		addLoadEvent : s,
		getQueryParamValue : function(aa) {
			var Z = j.location.search || j.location.hash;
			if (Z) {
				if (/\?/.test(Z)) {
					Z = Z.split("?")[1]
				}
				if (aa == null) {
					return L(Z)
				}
				var Y = Z.split("&");
				for (var X = 0; X < Y.length; X++) {
					if (Y[X].substring(0, Y[X].indexOf("=")) == aa) {
						return L(Y[X].substring((Y[X].indexOf("=") + 1)))
					}
				}
			}
			return ""
		},
		expressInstallCallback : function() {
			if (a) {
				var X = c(R);
				if (X && l) {
					X.parentNode.replaceChild(l, X);
					if (Q) {
						w(Q, true);
						if (M.ie && M.win) {
							l.style.display = "block"
						}
					}
					if (E) {
						E(B)
					}
				}
				a = false
			}
		}
	}
}();
window.xiuxiu = window.xiuxiu || {};
xiuxiu.minFlashVersion = "10.2.0";
xiuxiu.lightEditor = xiuxiu.lightEditor
		|| {
			url : "pub/xiuxiu/swf/PhotoEditor.swf?version=201410201425",
			base : "pub/xiuxiu/",
			width : "528",
			height : "385",
			type : 1
		};
xiuxiu.lightPuzzle = xiuxiu.lightPuzzle
		|| {
			url : "pub/xiuxiu/swf/CollagePlugin.swf?version=201305211645",
			base : "pub/xiuxiu/",
			width : "528",
			height : "470",
			type : 2
		};
xiuxiu.richEditor = xiuxiu.richEditor || {
	url : "pub/xiuxiu/swf/Main.swf?v=2.6.106",
	base : "pub/xiuxiu/",
	width : "100%",
	height : "100%",
	type : 3
};
xiuxiu.daitoutie = xiuxiu.daitoutie
		|| {
			url : "pub/xiuxiu/swf/FacePhoto.swf?version=201304251419",
			base : "pub/xiuxiu/",
			width : "568",
			height : "478",
			type : 4
		};
xiuxiu.avatarEditor = xiuxiu.avatarEditor
		|| {
			url : "pub/xiuxiu/swf/avatar.swf?version=201410201414",
			base : "pub/xiuxiu/swf/",
			width : "700",
			height : "435",
			type : 5
		};
xiuxiu.batchEditor = xiuxiu.batchEditor
		|| {
			url : "pub/xiuxiu/swf/plugin.swf?version=201405281142",
			base : "pub/xiuxiu/",
			width : "700",
			height : "435",
			type : 6
		};
xiuxiu.uploadType = 1;
xiuxiu.defaultUploadDataFieldName = "Filedata";
xiuxiu.uploadDataFieldName = "Filedata";
xiuxiu.defaultID = "xiuxiuEditor";
xiuxiu.swfs = [];
xiuxiu.unstatAPI = [];
xiuxiu.flashvars = {};
xiuxiu.flashvars.defaultvars = {
	source : "plugin",
	initFun : "xiuxiu._init",
	changeFlashHeightFun : "xiuxiu._setHeight",
	mouseWheelFun : "xiuxiu._mouseWheel",
	imageLoadedFun : "xiuxiu._imageLoaded",
	uploadFun : "xiuxiu._upload",
	beforeUploadFun : "xiuxiu._beforeUpload",
	uploadResponseFun : "xiuxiu._uploadResponse",
	batchUploadResponseFun : "xiuxiu._batchUploadResponseFun",
	batchUploadCompleteFun : "xiuxiu._batchUploadCompleteFun",
	saveBase64ImageFun : "xiuxiu._saveBase64Image",
	browseFun : "xiuxiu._browse",
	browseCancelFun : "xiuxiu._browseCancel",
	closePhotoEditorFun : "xiuxiu._close",
	debugFun : "xiuxiu._debug"
};
xiuxiu.params = {
	menu : "false",
	scale : "noScale",
	allowFullscreen : "true",
	allowScriptAccess : "always",
	bgcolor : "#FFFFFF",
	wmode : "window"
};
xiuxiu.embedSWF = function(b, c, f, m, e) {
	var a;
	xiuxiu.container = b;
	if (document.getElementById(xiuxiu.container)) {
		document.getElementById(xiuxiu.container).innerHTML = '<p><a href="http://www.adobe.com/go/getflashplayer"><img alt="Get Adobe Flash player" src="http://wwwimages.adobe.com/www.adobe.com/images/shared/download_buttons/get_flash_player.gif"></a></p>'
	}
	e = e || xiuxiu.defaultID;
	xiuxiu.editorType = Number(c) || xiuxiu.lightEditor.type;
	xiuxiu.attributes = {};
	xiuxiu.attributes.name = xiuxiu.attributes.id = e;
	xiuxiu.flashvars.objectID = xiuxiu.attributes.id;
	if (xiuxiu.editorType == xiuxiu.lightPuzzle.type) {
		a = xiuxiu.lightPuzzle.url;
		xiuxiu.params.base = xiuxiu.lightPuzzle.base;
		f = f || xiuxiu.lightPuzzle.width;
		m = m || xiuxiu.lightPuzzle.height
	} else {
		if (xiuxiu.editorType == xiuxiu.richEditor.type) {
			a = xiuxiu.richEditor.url;
			xiuxiu.params.base = xiuxiu.richEditor.base;
			f = f || xiuxiu.richEditor.width;
			m = m || xiuxiu.richEditor.height
		} else {
			if (xiuxiu.editorType == xiuxiu.daitoutie.type) {
				a = xiuxiu.daitoutie.url;
				xiuxiu.params.base = xiuxiu.daitoutie.base;
				f = f || xiuxiu.daitoutie.width;
				m = m || xiuxiu.daitoutie.height
			} else {
				if (xiuxiu.editorType == xiuxiu.avatarEditor.type) {
					a = xiuxiu.avatarEditor.url;
					xiuxiu.params.base = xiuxiu.avatarEditor.base;
					f = f || xiuxiu.avatarEditor.width;
					m = m || xiuxiu.avatarEditor.height
				} else {
					if (xiuxiu.editorType == xiuxiu.batchEditor.type) {
						a = xiuxiu.batchEditor.url;
						xiuxiu.params.base = xiuxiu.batchEditor.base;
						f = f || xiuxiu.batchEditor.width;
						m = m || xiuxiu.batchEditor.height
					} else {
						a = xiuxiu.lightEditor.url;
						xiuxiu.params.base = xiuxiu.lightEditor.base;
						f = f || xiuxiu.lightEditor.width;
						m = m || xiuxiu.lightEditor.height;
						xiuxiu.editorType = xiuxiu.lightEditor.type
					}
				}
			}
		}
	}
	var h = {};
	for ( var k in xiuxiu.flashvars.defaultvars) {
		h[k] = xiuxiu.flashvars.defaultvars[k]
	}
	if (xiuxiu.flashvars[xiuxiu.defaultID]) {
		for (k in xiuxiu.flashvars[xiuxiu.defaultID]) {
			h[k] = xiuxiu.flashvars[xiuxiu.defaultID][k]
		}
	}
	if (xiuxiu.flashvars[e]) {
		for (k in xiuxiu.flashvars[e]) {
			h[k] = xiuxiu.flashvars[e][k]
		}
	}
	var l = {};
	for (k in xiuxiu.params) {
		l[k] = xiuxiu.params[k]
	}
	if (xiuxiu.single) {
		var d = xiuxiu.swfs.length;
		for (var j = 0; j < d; j++) {
			swfobject.removeSWF(xiuxiu.swfs[j].id)
		}
		xiuxiu.swfs = []
	}
	var g = xiuxiu.minFlashVersion || "10.2.0";
	swfobject.embedSWF(a, xiuxiu.container, f, m, g,
			"pub/xiuxiu/swf/expressInstall.swf", h, l,
			xiuxiu.attributes);
	xiuxiu.swfs.push({
		id : xiuxiu.attributes.id,
		type : xiuxiu.editorType
	});
	xiuxiu._apiTrack("embedSWF_" + c + "_" + f + "_" + m, e)
};
xiuxiu.remove = function(c) {
	c = c || (xiuxiu.swfs.length > 0 ? xiuxiu.swfs[0].id : xiuxiu.defaultID);
	swfobject.removeSWF(c);
	var a = xiuxiu.swfs.length;
	for (var b = a - 1; b > -1; b--) {
		if (xiuxiu.swfs[b].id == c) {
			xiuxiu.swfs.splice(b, 1)
		}
	}
};
xiuxiu._init = function(a) {
	xiuxiu.setUploadURL(xiuxiu.uploadURL, a);
	xiuxiu.setUploadArgs(xiuxiu.uploadArgs, a);
	xiuxiu.setUploadType(xiuxiu.uploadType, a);
	xiuxiu.setUploadDataFieldName(xiuxiu.uploadDataFieldName, a);
	xiuxiu.setCropPresets(xiuxiu.cropPresets, a);
	if (xiuxiu.onInit && typeof (xiuxiu.onInit) == "function") {
		xiuxiu.onInit(a);
		xiuxiu._apiTrack("onInit", a)
	}
};
xiuxiu._imageLoaded = function(a, b) {
	if (xiuxiu.onImageLoaded && typeof (xiuxiu.onImageLoaded) == "function") {
		xiuxiu.onImageLoaded(a, b);
		xiuxiu._apiTrack("onImageLoaded", b)
	}
};
xiuxiu._upload = function(a) {
	if (xiuxiu.onUpload && typeof (xiuxiu.onUpload) == "function") {
		xiuxiu.onUpload(a);
		xiuxiu._apiTrack("onUpload", a)
	}
};
xiuxiu._beforeUpload = function(a, b) {
	if (xiuxiu.onBeforeUpload && typeof (xiuxiu.onBeforeUpload) == "function") {
		xiuxiu._apiTrack("onBeforeUpload", b);
		return xiuxiu.onBeforeUpload(a, b)
	} else {
		return true
	}
};
xiuxiu._uploadResponse = function(a, b) {
	if (xiuxiu.onUploadResponse
			&& typeof (xiuxiu.onUploadResponse) == "function") {
		xiuxiu.onUploadResponse(a, b);
		xiuxiu._apiTrack("onUploadResponse", b, true)
	}
};
xiuxiu._batchUploadResponseFun = function(b, a, c) {
	if (xiuxiu.onBatchUploadResponse
			&& typeof (xiuxiu.onBatchUploadResponse) == "function") {
		return xiuxiu.onBatchUploadResponse(b, a, c)
	} else {
		return {
			"continue" : true,
			success : true
		}
	}
};
xiuxiu._batchUploadCompleteFun = function(a) {
	if (xiuxiu.onBatchUploadComplete
			&& typeof (xiuxiu.onBatchUploadComplete) == "function") {
		xiuxiu.onBatchUploadComplete(a);
		xiuxiu._apiTrack("onBatchUploadComplete", a, true)
	}
};
xiuxiu._saveBase64Image = function(b, d, a, c) {
	if (xiuxiu.onSaveBase64Image
			&& typeof (xiuxiu.onSaveBase64Image) == "function") {
		xiuxiu.onSaveBase64Image(b, d, a, c);
		xiuxiu._apiTrack("onSaveBase64Image", c)
	}
};
xiuxiu._browse = function(c, b, a, d) {
	if (xiuxiu.onBrowse && typeof (xiuxiu.onBrowse) == "function") {
		xiuxiu.onBrowse(c, b, a, d);
		xiuxiu._apiTrack("onBrowse_" + c + "_" + b + "_" + a, d)
	}
};
xiuxiu._browseCancel = function(a) {
	if (xiuxiu.onBrowseCancel && typeof (xiuxiu.onBrowseCancel) == "function") {
		xiuxiu.onBrowseCancel(a);
		xiuxiu._apiTrack("onBrowseCancel", a)
	}
};
xiuxiu._close = function(a) {
	if (xiuxiu.onClose && typeof (xiuxiu.onClose) == "function") {
		xiuxiu.onClose(a);
		xiuxiu._apiTrack("onClose", a, true)
	}
};
xiuxiu._debug = function(a, b) {
	if (xiuxiu.onDebug && typeof (xiuxiu.onDebug) == "function") {
		xiuxiu.onDebug(a, b);
		xiuxiu._apiTrack("onDebug", b)
	}
};
xiuxiu.loadPhoto = function(b, c, f, e) {
	var d = e || {};
	d.uploadType = xiuxiu.uploadType;
	d.base64 = Boolean(c);
	if (b) {
		d.images = d.imageURL = b
	}
	if (xiuxiu.uploadURL) {
		d.uploadURL = xiuxiu.uploadURL
	}
	if (xiuxiu.uploadArgs) {
		d.uploadArgs = xiuxiu.uploadArgs
	}
	if (xiuxiu.uploadDataFieldName) {
		d.uploadDataFieldName = xiuxiu.uploadDataFieldName
	}
	if (xiuxiu.getEditor(f)) {
		var a = xiuxiu._getEditorTypeByID(f);
		if (a == xiuxiu.richEditor.type) {
			if (b) {
				d.loadImageChannel = d.loadImageChannel || "main";
				xiuxiu.getEditor(f).loadImages(b, d)
			}
		} else {
			xiuxiu.getEditor(f).loadPhoto(d)
		}
	}
	xiuxiu._apiTrack("loadPhoto_" + typeof (b) + "_" + Boolean(c), f)
};
xiuxiu.upload = function(a, b, c, d) {
	xiuxiu.uploadURL = a || xiuxiu.uploadURL;
	xiuxiu.uploadArgs = b || xiuxiu.uploadArgs;
	c = c || xiuxiu.uploadType;
	if ((c == 1 || c == 3) && xiuxiu.getEditor(d)) {
		xiuxiu.getEditor(d).upload(xiuxiu.uploadURL, xiuxiu.uploadArgs, c)
	}
	xiuxiu._apiTrack("upload", d)
};
xiuxiu.reset = function() {
	xiuxiu.uploadURL = "";
	xiuxiu.uploadArgs = null;
	xiuxiu.uploadType = 1;
	xiuxiu.uploadDataFieldName = xiuxiu.defaultUploadDataFieldName
};
xiuxiu.getEditor = function(a) {
	a = a || (xiuxiu.swfs.length > 0 ? xiuxiu.swfs[0].id : xiuxiu.defaultID);
	return xiuxiu._getFlash(a)
};
xiuxiu.setUploadURL = function(a, b) {
	xiuxiu.uploadURL = a;
	if (xiuxiu.uploadURL && xiuxiu.getEditor(b) && xiuxiu.getEditor(b).setParam) {
		xiuxiu.getEditor(b).setParam("uploadURL", a)
	}
	if (a) {
		xiuxiu._apiTrack("setUploadURL", b)
	}
};
xiuxiu.setUploadArgs = function(a, b) {
	xiuxiu.uploadArgs = a;
	if (xiuxiu.uploadArgs && xiuxiu.getEditor(b)
			&& xiuxiu.getEditor(b).setParam) {
		xiuxiu.getEditor(b).setParam("uploadArgs", a)
	}
	if (a) {
		xiuxiu._apiTrack("setUploadArgs", b)
	}
};
xiuxiu.setUploadType = function(a, b) {
	xiuxiu.uploadType = a;
	if (xiuxiu.uploadType && xiuxiu.getEditor(b)
			&& xiuxiu.getEditor(b).setParam) {
		xiuxiu.getEditor(b).setParam("uploadType", xiuxiu.uploadType)
	}
	xiuxiu._apiTrack("setUploadType_" + xiuxiu.uploadType, b)
};
xiuxiu.setUploadDataFieldName = function(a, b) {
	xiuxiu.uploadDataFieldName = a;
	if (xiuxiu.uploadDataFieldName && xiuxiu.getEditor(b)
			&& xiuxiu.getEditor(b).setParam) {
		xiuxiu.getEditor(b).setParam("uploadDataFieldName", a)
	}
	xiuxiu._apiTrack("setUploadDataFieldName_" + a, b)
};
xiuxiu.setCropPresets = function(a, b) {
	xiuxiu.cropPresets = a;
	if (a && xiuxiu.getEditor(b) && xiuxiu.getEditor(b).setParam) {
		xiuxiu.getEditor(b).setParam("cropPresets", a)
	}
	if (a) {
		xiuxiu._apiTrack("setCropPresets_" + typeof (a), b)
	}
};
xiuxiu.setLaunchVars = xiuxiu.setFlashvars = function(a, b, c) {
	c = c || xiuxiu.defaultID;
	xiuxiu.flashvars[c] = xiuxiu.flashvars[c] || {};
	if (a == "cropPresets") {
		if (b.constructor == Array) {
			xiuxiu.flashvars[c][a] = encodeURIComponent(xiuxiu.stringify(b))
		} else {
			xiuxiu.flashvars[c][a] = b
		}
	} else {
		if (a == "customMenu") {
			if (b.constructor == Array) {
				xiuxiu.flashvars[c][a] = encodeURIComponent(xiuxiu.stringify(b))
			} else {
				xiuxiu.flashvars[c][a] = b
			}
		} else {
			if (a == "avatarPreview") {
				if (b.constructor == Object) {
					xiuxiu.flashvars[c][a] = encodeURIComponent(xiuxiu
							.stringify(b))
				} else {
					xiuxiu.flashvars[c][a] = b
				}
			} else {
				if (a == "batchPresets") {
					xiuxiu.flashvars[c][a] = encodeURIComponent(xiuxiu
							.stringify(b))
				} else {
					xiuxiu.flashvars[c][a] = b
				}
			}
		}
	}
	xiuxiu._apiTrack("setLaunchVars_" + a + "_" + typeof (b))
};
xiuxiu._setHeight = function(a, b) {
	if (xiuxiu.getEditor(b)) {
		xiuxiu.getEditor(b).height = a
	}
	xiuxiu._apiTrack("setHeight", b)
};
xiuxiu._mouseWheel = function(b, a) {
	if (xiuxiu.getEditor(a) && xiuxiu.getEditor(a).mouseWheel) {
		xiuxiu.getEditor(a).mouseWheel(b)
	}
};
xiuxiu.uploadStart = function(a, b) {
	if (xiuxiu.getEditor(b) && xiuxiu.getEditor(b).uploadStart) {
		xiuxiu.getEditor(b).uploadStart(a)
	}
	xiuxiu._apiTrack("uploadStart", b)
};
xiuxiu.uploadResponse = function(a, b) {
	if (xiuxiu.getEditor(b) && xiuxiu.getEditor(b).uploadResponse) {
		xiuxiu.getEditor(b).uploadResponse(a)
	}
	xiuxiu._apiTrack("uploadResponse", b)
};
xiuxiu.uploadFail = function(a, b) {
	if (xiuxiu.getEditor(b) && xiuxiu.getEditor(b).uploadFail) {
		xiuxiu.getEditor(b).uploadFail(a)
	}
	xiuxiu._apiTrack("uploadFail", b)
};
xiuxiu._getFlash = function(b) {
	var a = document.getElementById(b);
	if (!a) {
		if (navigator.appName.indexOf("Microsoft") != -1) {
			a = window[b]
		} else {
			a = document[b]
		}
	}
	return a
};
xiuxiu._getEditorTypeByID = function(e) {
	var a = xiuxiu.swfs.length;
	e = e || (a > 0 ? xiuxiu.swfs[0].id : xiuxiu.defaultID);
	var d;
	var c = xiuxiu.lightEditor.type;
	for (var b = 0; b < a; b++) {
		d = xiuxiu.swfs[b];
		if (e == d.id) {
			return d.type
		}
	}
	return c
};
xiuxiu._apiTrack = function(a, c, b) {
	if (a) {
		a += ("_" + c);
		xiuxiu.unstatAPI.push(a)
	}
	if (xiuxiu.getEditor(c) && xiuxiu.getEditor(c).apiTrack) {
		if (xiuxiu.unstatAPI.length > 0) {
			xiuxiu.getEditor(c).apiTrack(xiuxiu.unstatAPI, Boolean(b));
			xiuxiu.unstatAPI = []
		}
	}
};
(function(a) {
	window.xiuxiu = window.xiuxiu || {};
	window.xiuxiu.stringify = a()
})(function() {
	var d = Object.prototype.toString, b = function(e) {
		return d.call(e) === "[object Array]"
	}, a = function(e) {
		return d.call(e) === "[object Object]"
	};
	function c(k) {
		var j = [], g, e, f, h = false;
		if (b(k)) {
			j.push("[");
			for (g = 0, e = k.length; g < e; g++) {
				f = c(k[g]);
				if (f !== 1) {
					h = true;
					j.push(f);
					j.push(",")
				}
			}
			if (h) {
				j.pop()
			}
			j.push("]")
		} else {
			if (a(k)) {
				j.push("{");
				for (g in k) {
					f = c(k[g]);
					if (f !== 1) {
						h = true;
						j.push('"' + g + '":');
						j.push(f);
						j.push(",")
					}
				}
				if (h) {
					j.pop()
				}
				j.push("}")
			} else {
				if (typeof k === "string") {
					j.push('"' + k + '"')
				} else {
					if (typeof k === "function") {
						return 1
					} else {
						j.push(k)
					}
				}
			}
		}
		return j.join("")
	}
	return c
});