/*
 FusionCharts JavaScript Library
 Copyright FusionCharts Technologies LLP
 License Information at <http://www.fusioncharts.com/license>
 @version fusioncharts/3.3.1-sr3.21100
*/
(function() {
	var g = FusionCharts(["private", "modules.renderer.js-lib"]);
	if (g !== void 0) {
		var h = window,
			m = "",
			U = "0",
			w = ".",
			S = document,
			ia = !! S.createElementNS && !! S.createElementNS("http://www.w3.org/2000/svg", "svg").createSVGRect,
			b = /msie/i.test(navigator.userAgent) && !window.opera,
			B = /\s+/g,
			e = /^#?/,
			r = /^rgba/i,
			x = /[#\s]/ig,
			$ = /\{br\}/ig,
			fa = /(^#[0-9A-F]{6}$)|(^#[0-9A-F]{3}$)/i,
			s = Math.abs,
			ka = Math.pow,
			G = Math.round,
			V = ka(2, -24),
			K = Object.prototype.toString,
			Y = S.documentElement.ontouchstart !== void 0,
			O = Math,
			s = O.abs,
			l = O.max,
			n = O.min,
			F = {
				pageX: 0,
				pageY: 0
			},
			aa = function(a) {
				var b = a.data,
					c = b.chart,
					d = c.paper,
					f = a.type,
					e = a.target || a.originalTarget || a.srcElement || a.relatedTarget || a.fromElement,
					p = Y && i(a) || F,
					z = c.elements.resizeBox,
					t = a.layerX || p.layerX,
					k = a.layerY || p.layerY,
					q = t - b.ox,
					u = k - b.oy,
					j = b.bBox,
					da = b.ox,
					A = b.oy,
					j = b.zoomX,
					N = b.zoomY,
					O = b.canvasY,
					ca = b.canvasX,
					ea = b.canvasW,
					v = b.canvasH,
					r = b.canvasX2,
					g = b.canvasY2,
					x = b.strokeWidth,
					q = b.chartPosLeft,
					u = b.chartPosTop,
					da = b.attr;
				t === void 0 && (t = (a.pageX || p.pageX) - q, k = (a.pageY || p.pageY) - u);
				switch (f) {
				case "dragstart":
					b.oy = k;
					b.ox = t;
					b.allowMove = !1;
					if (!z) z = c.elements.resizeBox = d.rect(c.layers.tracker).attr(da);
					if (t > ca && t < r && k > O && k < g) b.allowMove = !0;
					if (e && e.ishot) b.allowMove = !1;
					z.attr({
						x: 0,
						y: 0,
						width: 0,
						height: 0
					}).show();
					break;
				case "dragend":
					j = z.getBBox();
					a = {
						chart: c,
						selectionLeft: j.x,
						selectionTop: j.y,
						selectionHeight: j.height,
						selectionWidth: j.width
					};
					b.allowMove && b.callback(a);
					z.hide();
					delete b.oy;
					delete b.ox;
					break;
				default:
					if (!b.allowMove) break;
					q = t - b.ox;
					u = k - b.oy;
					da = b.ox;
					A = b.oy;
					q = -(da - n(da - (da - l(da + q, ca)), r));
					u = -(A - n(A - (A - l(A + u, O)), g));
					z.attr({
						x: (j ? n(da, da + q) : ca) + x * 0.5,
						y: (N ? n(A, A + u) : O) + x * 0.5,
						width: j ? s(q) : ea,
						height: N ? s(u) : v
					})
				}
			},
			D = function(a) {
				var b = a.target || a.originalTarget || a.srcElement || a.relatedTarget || a.fromElement,
					c = a.data,
					d = a.type,
					f = a.layerX,
					e = a.layerY;
				f === void 0 && (f = a.pageX - c.chartPosLeft, e = a.pageY - c.chartPosTop);
				if (d === "mousedown") b.ishot = f > c.canvasX && f < c.canvasX2 && e > c.canvasY && e < c.canvasY2;
				d === "mouseup" && setTimeout(function() {
					b.ishot = !1
				}, 1)
			},
			O = function() {
				var a = "innerWidth",
					b = "innerHeight",
					c = S.documentElement || S.body,
					d = c;
				"innerWidth" in h ? d = h : (a = "clientWidth", b = "clientHeight");
				return function() {
					return {
						width: d[a],
						height: d[b],
						scrollTop: c.scrollTop,
						scrollLeft: c.scrollLeft
					}
				}
			}(),
			M = function(a, b) {
				for (var c = {
					left: a.offsetLeft || 0,
					top: a.offsetTop || 0
				}, a = a.offsetParent; a;) c.left += a.offsetLeft || 0, c.top += a.offsetTop || 0, a !== S.body && a !== S.documentElement && !b && (c.left -= a.scrollLeft, c.top -= a.scrollTop), a = a.offsetParent;
				return c
			},
			Z = function(a, b) {
				return !a && a !== !1 && a !== 0 ? b : a
			},
			R = function() {
				var a, b, c;
				b = 0;
				for (c = arguments.length; b < c; b += 1) if ((a = arguments[b]) || !(a !== !1 && a !== 0)) return a;
				return m
			},
			v = function() {
				var a, b, c;
				b = 0;
				for (c = arguments.length; b < c; b += 1) if ((a = arguments[b]) || !(a !== !1 && a !== 0)) return a
			},
			C = {
				mousedown: "touchstart",
				mousemove: "touchmove",
				mouseup: "touchend"
			},
			f = function(a, b, c, d) {
				jQuery(a).bind(Y && C[b] || b, d, c)
			},
			j = function(a, b, c) {
				var d = S.removeEventListener ? "removeEventListener" : "detachEvent";
				S[d] && !a[d] && (a[d] = function() {});
				jQuery(a).unbind(Y && C[b] || b, c)
			},
			i = function(a) {
				var b = a.sourceEvent || a.originalEvent;
				return Y && b && b.touches && b.touches[0] || a
			},
			c = function() {
				var a, b, c;
				b = 0;
				for (c = arguments.length; b < c; b += 1) if ((a = arguments[b]) || !(a !== !1 && a !== 0)) if (!isNaN(a = Number(a))) return a
			},
			d = function(a, b) {
				a = !a && a !== !1 && a !== 0 ? NaN : Number(a);
				return isNaN(a) ? null : b ? s(a) : a
			},
			a = function(a) {
				return typeof a === "string" ? a.replace($, "<br />") : m
			},
			k = function(a, b, c) {
				var d, f;
				if (b instanceof Array) for (d = 0; d < b.length; d += 1) if (typeof b[d] !== "object") c && b[d] === void 0 || (a[d] = b[d]);
				else {
					if (a[d] === null || typeof a[d] !== "object") a[d] = b[d] instanceof Array ? [] : {};
					k(a[d], b[d], c)
				} else for (d in b) if (b[d] !== null && typeof b[d] === "object") if (f = K.call(b[d]), f === "[object Object]") {
					if (a[d] === null || typeof a[d] !== "object") a[d] = {};
					k(a[d], b[d], c)
				} else if (f === "[object Array]") {
					if (a[d] === null || !(a[d] instanceof Array)) a[d] = [];
					k(a[d], b[d], c)
				} else a[d] = b[d];
				else a[d] = b[d];
				return a
			},
			q = function(a, b, c) {
				if (typeof a !== "object" && typeof b !== "object") return null;
				if (typeof b !== "object" || b === null) return a;
				typeof a !== "object" && (a = b instanceof Array ? [] : {});
				k(a, b, c);
				return a
			},
			u = function(a, b) {
				var c;
				if (b instanceof Array) for (c = b.length - 1; c >= 0; c -= 1) typeof b[c] !== "object" ? b[c] === !0 && a && a.splice && a.splice(c, 1) : K.call(b[c]) === K.call(a[c]) && u(a[c], b[c]);
				else for (c in b) typeof b[c] !== "object" ? b[c] === !0 && a && a.splice && a.splice(c, 1) : K.call(b[c]) === K.call(a[c]) && u(a[c], b[c]);
				return a
			},
			N = function() {
				var a = /^@window_/g;
				return function(b, c) {
					var d = b.replace(/\[[\'\"]/g, ".").replace(/[\'\"]\]/g, m).replace(/\[/g, ".@window_").replace(/\]/g, m).split("."),
						f = h,
						e, p;
					p = m;
					var z, t, k;
					t = d.length;
					for (k = 0; k < t; k += 1) {
						z = d[k];
						e = f;
						if (z.match(a)) p = h[z.replace(a, m)], f = f[p];
						else if (f === void 0 || f === null) throw (p || z).replace(a, m) + " is not defined";
						else f = f[z];
						p = z
					}
					f && (typeof f.call === "function" || f === h.alert) ? f === h.alert ? f(c) : f.call(e, c) : setTimeout(function() {
						throw z.replace(a, m) + "() is not a function";
					}, 0)
				}
			}(),
			ca = function() {
				var a = "FusionChartslinkEval" + parseInt(+new Date, 10);
				return function(b) {
					try {
						h[a] = new Function(b), eval("window['" + a + "']();")
					} catch (c) {
						setTimeout(function() {
							throw c;
						}, 0)
					}
					ia ? delete h[a] : h[a] = null
				}
			}(),
			J = function() {
				if (Array.isArray) return Array.isArray;
				var a = Object.prototype.toString,
					b = a.call([]);
				return function(c) {
					return a.call(c) === b
				}
			}(),
			Ja = function(a, b) {
				a = Number(a);
				a = isNaN(a) ? 100 : a;
				b !== void 0 && (a = a * b / 100);
				return a % 101
			},
			pa = function(a, b, d) {
				var a = a.split(","),
					f;
				d !== void 0 && (d = c(d.split(",")[0]));
				a[0] = Ja(a[0], d);
				for (f = 1; f < b; f += 1) a[f] = a[0] * Ja(a[f], d) / 100;
				return a.join(",")
			},
			va = function(a, b, c) {
				var d = 0,
					f = 0,
					e = 0;
				c && c.match(r) && (c = c.split(","), d = c[0].slice(c[0].indexOf("(") + 1), f = c[1], e = c[2], !b && b !== 0 && (b = parseInt(c[3].slice(0, c[3].indexOf(")")) * 100, 10)));
				if (a) if (a.match(r)) c = a.split(","), d = c[0].slice(c[0].indexOf("(") + 1), f = c[1], e = c[2];
				else {
					a = a.replace(x, m).split(",")[0];
					switch (a.length) {
					case 3:
						a = a[0] + a[0] + a[1] + a[1] + a[2] + a[2];
						break;
					case 6:
						break;
					default:
						a = (a + "FFFFFF").slice(0, 6)
					}
					d = parseInt(a.slice(0, 2), 16);
					f = parseInt(a.slice(2, 4), 16);
					e = parseInt(a.slice(4, 6), 16)
				}!b && b != 0 && (b = 100);
				typeof b === "string" && (b = b.split(",")[0]);
				b = parseInt(b, 10) / 100;
				return "rgba(" + d + "," + f + "," + e + "," + b + ")"
			},
			X = function(a) {
				return a.replace(x, m).replace(e, "#")
			},
			Ca = function(a, b) {
				b = b < 0 || b > 100 ? 100 : b;
				b /= 100;
				var a = a.replace(x, m),
					c = parseInt(a, 16),
					d = Math.floor(c / 65536),
					f = Math.floor((c - d * 65536) / 256);
				return ("000000" + (d * b << 16 | f * b << 8 | (c - d * 65536 - f * 256) * b).toString(16)).slice(-6)
			},
			ma = function(a, b) {
				b = b < 0 || b > 100 ? 100 : b;
				b /= 100;
				var a = a.replace(x, m),
					c = parseInt(a, 16),
					d = Math.floor(c / 65536),
					f = Math.floor((c - d * 65536) / 256);
				return ("000000" + (256 - (256 - d) * b << 16 | 256 - (256 - f) * b << 8 | 256 - (256 - (c - d * 65536 - f * 256)) * b).toString(16)).slice(-6)
			},
			ta = {
				circle: "circle",
				triangle: "triangle",
				square: "square",
				diamond: "diamond",
				poly: "poly_",
				spoke: "spoke_"
			},
			Aa = {
				font: "font",
				fontFamily: "font-family",
				"font-family": "font-family",
				fontWeight: "font-weight",
				"font-weight": "font-weight",
				fontSize: "font-size",
				"font-size": "font-size",
				lineHeight: "line-height",
				"line-height": "line-height",
				textDecoration: "text-decoration",
				"text-decoration": "text-decoration",
				color: "color",
				whiteSpace: "white-space",
				"white-space": "white-space",
				padding: "padding",
				margin: "margin",
				background: "background",
				backgroundColor: "background-color",
				"background-color": "background-color",
				backgroundImage: "background-image",
				"background-image": "background-image",
				backgroundPosition: "background-position",
				"background-position": "background-position",
				backgroundPositionLeft: "background-position-left",
				"background-position-left": "background-position-left",
				backgroundPositionTop: "background-position-top",
				"background-position-top": "background-position-top",
				backgroundRepeat: "background-repeat",
				"background-repeat": "background-repeat",
				border: "border",
				borderColor: "border-color",
				"border-color": "border-color",
				borderStyle: "border-style",
				"border-style": "border-style",
				borderThickness: "border-thickness",
				"border-thickness": "border-thickness",
				borderTop: "border-top",
				"border-top": "border-top",
				borderTopColor: "border-top-color",
				"border-top-color": "border-top-color",
				borderTopStyle: "border-top-style",
				"border-top-style": "border-top-style",
				borderTopThickness: "border-top-thickness",
				"border-top-thickness": "border-top-thickness",
				borderRight: "border-right",
				"border-right": "border-right",
				borderRightColor: "border-right-color",
				"border-right-color": "border-right-color",
				borderRightStyle: "border-right-style",
				"border-right-style": "border-right-style",
				borderRightThickness: "border-right-thickness",
				"border-right-thickness": "border-right-thickness",
				borderBottom: "border-bottom",
				"border-bottom": "border-bottom",
				borderBottomColor: "border-bottom-color",
				"border-bottom-color": "border-bottom-color",
				borderBottomStyle: "border-bottom-style",
				"border-bottom-style": "border-bottom-style",
				borderBottomThickness: "border-bottom-thickness",
				"border-bottom-thickness": "border-bottom-thickness",
				borderLeft: "border-left",
				"border-left": "border-left",
				borderLeftColor: "border-left-color",
				"border-left-color": "border-left-color",
				borderLeftStyle: "border-left-style",
				"border-left-Style": "border-left-style",
				borderLeftThickness: "border-left-thickness",
				"border-left-thickness": "border-left-thickness"
			},
			Ka = function() {
				var a = document.createElement("span"),
					d, f = {
						lineHeight: !0,
						"line-height": !0
					},
					e = function() {
						return c(parseInt(a.style.fontSize, 10), 10) * 1.4 + "px"
					};
				a.innerHTML = "fy";
				d = window.getComputedStyle ?
				function() {
					var b = window.getComputedStyle(a, null);
					return b && b.getPropertyValue("line-height") ? b.getPropertyValue("line-height") : e.apply(this, arguments)
				} : a.currentStyle ?
				function() {
					return a.currentStyle.lineHeight
				} : e;
				return function(c) {
					var k, p = "";
					for (k in c)!f[k] && Aa[k] && (p += Aa[k] + " : " + c[k] + ";");
					b && !ia ? a.style.setAttribute("cssText", p) : a.setAttribute("style", p);
					k = d();
					parseFloat(k) || (k = e());
					return c.lineHeight = k
				}
			}(),
			bb = function() {
				var b = {
					top: {
						align: "center",
						verticalAlign: "top",
						textAlign: "center"
					},
					right: {
						align: "right",
						verticalAlign: "middle",
						textAlign: "left"
					},
					bottom: {
						align: "center",
						verticalAlign: "bottom",
						textAlign: "center"
					},
					left: {
						align: "left",
						verticalAlign: "middle",
						textAlign: "right"
					}
				},
					d = /([^\,^\s]+)\)$/g,
					f = function(a, b) {
						var d;
						if (/^(bar|bar3d)$/.test(a)) this.isBar = !0, this.yPos = "bottom", this.yOppPos = "top", this.xPos = "left", this.xOppPos = "right";
						d = parseInt(b.labelstep, 10);
						this.labelStep = d > 1 ? d : 1;
						this.showLabel = c(b.showlabels, b.shownames, 1);
						this.is3D = /3d$/.test(a)
					};
				f.prototype = {
					isBar: !1,
					yPos: "left",
					yOppPos: "right",
					xPos: "bottom",
					xOppPos: "top",
					addAxisGridLine: function(a, c, f, p, z, t, e, k) {
						var q = f === "" ? !1 : !0,
							u = p > 0 || t.match(d)[1] > 0 ? !0 : !1,
							j;
						if (q || u) {
							u || (t = "rgba(0,0,0,0)", p = 0.1);
							j = {
								isGrid: !0,
								width: p,
								dashStyle: z,
								color: t,
								value: c,
								zIndex: e === void 0 ? 2 : e
							};
							if (q) c = a.opposite ? k ? this.xOppPos : this.yOppPos : k ? this.xPos : this.yPos, c = b[c], j.label = {
								text: f,
								style: a.labels.style,
								textAlign: c.textAlign,
								align: c.align,
								verticalAlign: c.verticalAlign,
								rotation: 0,
								x: 0,
								y: 0
							};
							a.plotLines.push(j)
						}
						return j
					},
					addAxisAltGrid: function(a, b) {
						if (!this.is3D) {
							var d = c(a._lastValue, a.min),
								p = v(a._altGrid, !1);
							p && a.plotBands.push({
								isGrid: !0,
								color: a.alternateGridColor,
								to: b,
								from: d,
								zIndex: 1
							});
							a._lastValue = b;
							a._altGrid = !p
						}
					},
					addXaxisCat: function(a, c, d, p) {
						var z = b[a.opposite ? this.xOppPos : this.xPos],
							c = {
								isGrid: !0,
								width: 0.1,
								color: "rgba(0,0,0,0)",
								value: c,
								label: {
									text: p,
									style: a.labels.style,
									textAlign: z.textAlign,
									align: z.align,
									verticalAlign: z.verticalAlign,
									rotation: 0,
									x: 0,
									y: 0
								}
							};
						if (d % this.labelStep !== 0) c.stepped = !0, c.label.style = a.steppedLabels.style;
						a.plotLines.push(c)
					},
					addVline: function(b, d, f, p) {
						var z = p._FCconf,
							t = z.isBar,
							p = p.chart.plotBorderWidth,
							e = p % 2,
							k = z.divlineStyle,
							q = a(d.label),
							u = Boolean(c(d.showlabelborder, z.showVLineLabelBorder, 1)),
							j = Boolean(c(d.showlabelbackground, 1)),
							i = v(d.labelhalign, t ? "left" : "center"),
							N = v(d.labelvalign, t ? "middle" : "bottom").toLowerCase(),
							l = c(d.labelposition, 0),
							O = c(d.lineposition, 0.5),
							ca = c(d.showvlines, z.showVLines, 1),
							n = c(d.alpha, z.vLineAlpha, 80),
							Fa = v(d.color, z.vLineColor, "333333").replace(/^#?/, "#"),
							F = j ? v(d.labelbgcolor, z.vLineLabelBgColor, "333333").replace(/^#?/, "#") : m,
							r = Fa,
							g = c(d.thickness, z.vLineThickness, 1),
							x = g * 0.5,
							I = Boolean(Number(v(d.dashed, 0))),
							E = c(d.dashlen, 5),
							y = c(d.dashgap, 2),
							ha = z.smartLabel,
							W = parseInt(k.fontSize, 10) + 2,
							o = 0,
							Q = c(d.rotatelabel, z.rotateVLineLabels) ? 270 : 0,
							O = O < 0 || O > 1 ? 0.5 : O,
							l = l < 0 || l > 1 ? 0 : l;
						ha.setStyle(k);
						ha = ha.getOriSize(q);
						Fa = va(Fa, ca ? n : "0");
						if (t) {
							switch (N) {
							case "top":
								W -= ha.height + x + 2;
								break;
							case "middle":
								W -= ha.height * 0.5 + 1;
								break;
							default:
								W += x
							}
							d.labelhalign || (o -= ha.width * l)
						} else {
							switch (N) {
							case "top":
								W -= ha.height + 2 + (p || 1) * (1 - l) + l;
								break;
							case "middle":
								W -= ha.height * 0.5 + p * (1 - l * 2);
								break;
							default:
								W += (p - e) * l
							}
							switch (i) {
							case "left":
								o += g;
								break;
							case "right":
								o -= g + 1
							}
						}
						b.plotLines.push({
							isVline: !0,
							color: Fa,
							width: g,
							value: f - 1 + O,
							zIndex: c(d.showontop, z.showVLinesOnTop) ? 5 : 3,
							dashStyle: I ? Na(E, y, g) : void 0,
							label: {
								text: q,
								align: t ? "left" : "center",
								offsetScale: l,
								rotation: Q,
								y: W,
								x: o,
								textAlign: i,
								backgroundColor: F,
								borderWidth: ca && u ? "1px" : m,
								borderType: ca && u ? "solid" : m,
								borderColor: ca && u ? r : m,
								backgroundOpacity: ca && j ? v(d.labelbgalpha, z.vLineLabelBgAlpha) / 100 : 0,
								style: {
									color: ca ? r : Fa,
									fontSize: k.fontSize,
									fontFamily: k.fontFamily,
									lineHeight: k.lineHeight,
									backgroundColor: F
								}
							}
						})
					}
				};
				return f.prototype.constructor = f
			}();
		(function() {
			function a(b, c, d) {
				var f;
				if (c <= 0) return String(G(b));
				if (isNaN(c)) return b = b.toString(), b.length > 12 && b.indexOf(w) != -1 && (c = 12 - b.split(w)[0].length, f = ka(10, c), b = String(G(b * f) / f)), b;
				f = ka(10, c);
				b = String(G(b * f) / f);
				if (d == 1) {
					b.indexOf(w) == -1 && (b += ".0");
					d = b.split(w);
					c -= d[1].length;
					for (d = 1; d <= c; d++) b += U
				}
				return b
			}
			function b(a, c, d, f) {
				var e = Number(a);
				if (isNaN(e)) return m;
				var k = m,
					q = !1,
					u = m,
					j = m,
					i = u = 0,
					u = 0,
					i = a.length;
				a.indexOf(w) != -1 && (k = a.substring(a.indexOf(w) + 1, a.length), i = a.indexOf(w));
				e < 0 && (q = !0, u = 1);
				u = a.substring(u, i);
				a = u.length;
				e = f.length - 1;
				i = f[e];
				if (a < i) j = u;
				else for (; a >= i;) j = (a - i ? d : m) + u.substr(a - i, i) + j, a -= i, i = (e -= 1) <= 0 ? f[0] : f[e], a < i && (j = u.substring(a, 0) + j);
				k != m && (j = j + c + k);
				q == !0 && (j = "-" + j);
				return j
			}
			var d = {
				formatnumber: "1",
				formatnumberscale: "1",
				defaultnumberscale: m,
				numberscaleunit: ["K", "M"],
				numberscalevalue: [1E3, 1E3],
				numberprefix: m,
				numbersuffix: m,
				decimals: m,
				forcedecimals: U,
				yaxisvaluedecimals: "2",
				decimalseparator: w,
				thousandseparator: ",",
				thousandseparatorposition: [3],
				indecimalseparator: m,
				inthousandseparator: m,
				sformatnumber: "1",
				sformatnumberscale: U,
				sdefaultnumberscale: m,
				snumberscaleunit: ["K", "M"],
				snumberscalevalue: [1E3, 1E3],
				snumberprefix: m,
				snumbersuffix: m,
				sdecimals: "2",
				sforcedecimals: U,
				syaxisvaluedecimals: "2",
				xFormatNumber: U,
				xFormatNumberScale: U,
				xDefaultNumberScale: m,
				xNumberScaleUnit: ["K", "M"],
				xNumberScaleValue: [1E3, 1E3],
				xNumberPrefix: m,
				xNumberSuffix: m
			},
				f = {
					mscombidy2d: {
						formatnumberscale: "1"
					}
				},
				e = function(a, b, t) {
					var e, k, u, j, i, A, l, N, O, ca = b.name,
						n = q({}, d),
						Fa, F;
					(u = f[ca]) && (n = q(n, u));
					this.csConf = n;
					this.chartAPI = b;
					Z(a.numberscaleunit) && (e = a.numberscaleunit.split(","));
					if (k = Z(a.snumberscaleunit, a.numberscaleunit)) k = k.split(",");
					if (u = Z(a.xnumberscaleunit, a.numberscaleunit)) u = u.split(",");
					if (j = Z(a.ticknumberscaleunit, a.numberscaleunit)) j = j.split(",");
					if (i = Z(a.ynumberscaleunit, a.numberscaleunit)) i = i.split(",");
					Z(a.numberscalevalue) && (A = a.numberscalevalue.split(","));
					if (F = Z(a.snumberscalevalue, a.numberscalevalue)) F = F.split(",");
					if (l = Z(a.xnumberscalevalue, a.numberscalevalue)) l = l.split(",");
					if (N = Z(a.ticknumberscalevalue, a.numberscalevalue)) N = N.split(",");
					if (O = Z(a.ynumberscalevalue, a.numberscalevalue)) O = O.split(",");
					if (Z(a.thousandseparatorposition)) {
						Fa = a.thousandseparatorposition.split(",");
						for (var r = Fa.length, g, x = c(Fa[r]), x = x ? x : d.thousandseparatorposition[0]; r;) r -= 1, (g = c(Math.abs(Fa[r]))) ? x = g : g = x, Fa[r] = g
					}
					b || (b = {});
					r = c(a.scalerecursively, 0);
					g = c(a.sscalerecursively, r);
					var x = c(a.xscalerecursively, r),
						I = c(a.maxscalerecursion, -1),
						E = c(a.smaxscalerecursion, I),
						y = c(a.xmaxscalerecursion, I),
						ha = Z(a.scaleseparator, " "),
						W = Z(a.sscaleseparator, ha),
						o = Z(a.xscaleseparator, ha);
					if (!I || I == 0) I = -1;
					this.baseConf = e = {
						cacheStore: [],
						formatnumber: v(a.formatnumber, b.formatnumber, n.formatnumber),
						formatnumberscale: v(a.formatnumberscale, b.formatnumberscale, n.formatnumberscale),
						defaultnumberscale: R(a.defaultnumberscale, b.defaultnumberscale, n.defaultnumberscale),
						numberscaleunit: v(e, b.numberscaleunit, n.numberscaleunit).concat(),
						numberscalevalue: v(A, b.numberscalevalue, n.numberscalevalue).concat(),
						numberprefix: R(a.numberprefix, b.numberprefix, n.numberprefix),
						numbersuffix: R(a.numbersuffix, b.numbersuffix, n.numbersuffix),
						decimalprecision: parseInt(a.decimals === "auto" ? n.decimalprecision : v(a.decimals, a.decimalprecision, b.decimals, n.decimals, b.decimalprecision, n.decimalprecision), 10),
						forcedecimals: v(a.forcedecimals, b.forcedecimals, n.forcedecimals),
						decimalseparator: v(a.decimalseparator, b.decimalseparator, n.decimalseparator),
						thousandseparator: v(a.thousandseparator, b.thousandseparator, n.thousandseparator),
						thousandseparatorposition: v(Fa, b.thousandseparatorposition, n.thousandseparatorposition),
						indecimalseparator: R(a.indecimalseparator, b.indecimalseparator, n.indecimalseparator),
						inthousandseparator: R(a.inthousandseparator, b.inthousandseparator, n.inthousandseparator),
						scalerecursively: r,
						maxscalerecursion: I,
						scaleseparator: ha
					};
					this.Y = [];
					if (!t) {
						t = {
							cacheStore: [],
							formatnumber: e.formatnumber,
							formatnumberscale: e.formatnumberscale,
							defaultnumberscale: e.defaultnumberscale,
							numberscaleunit: e.numberscaleunit.concat(),
							numberscalevalue: e.numberscalevalue.concat(),
							numberprefix: e.numberprefix,
							numbersuffix: e.numbersuffix,
							decimalprecision: e.decimalprecision,
							forcedecimals: e.forcedecimals,
							decimalseparator: e.decimalseparator,
							thousandseparator: e.thousandseparator,
							thousandseparatorposition: e.thousandseparatorposition,
							indecimalseparator: e.indecimalseparator,
							inthousandseparator: e.inthousandseparator,
							scalerecursively: r,
							maxscalerecursion: I,
							scaleseparator: ha
						};
						if (!b.useScaleRecursively || (t.numberscalevalue && t.numberscalevalue.length) != (t.numberscaleunit && t.numberscaleunit.length)) t.scalerecursively = r = 0;
						A = {
							cacheStore: [],
							formatnumber: t.formatnumber,
							formatnumberscale: t.formatnumberscale,
							defaultnumberscale: t.defaultnumberscale,
							numberscaleunit: t.numberscaleunit.concat(),
							numberscalevalue: t.numberscalevalue.concat(),
							numberprefix: t.numberprefix,
							numbersuffix: t.numbersuffix,
							decimalprecision: parseInt(v(a.yaxisvaluedecimals, t.decimalprecision, 2)),
							forcedecimals: v(a.forceyaxisvaluedecimals, t.forcedecimals),
							decimalseparator: t.decimalseparator,
							thousandseparator: t.thousandseparator,
							thousandseparatorposition: t.thousandseparatorposition.concat(),
							indecimalseparator: t.indecimalseparator,
							inthousandseparator: t.inthousandseparator,
							scalerecursively: r,
							maxscalerecursion: I,
							scaleseparator: ha
						};
						F = {
							cacheStore: [],
							formatnumber: v(a.sformatnumber, b.sformatnumber, d.sformatnumber),
							formatnumberscale: v(a.sformatnumberscale, b.sformatnumberscale, d.sformatnumberscale),
							defaultnumberscale: R(a.sdefaultnumberscale, b.sdefaultnumberscale, t.defaultnumberscale),
							numberscaleunit: v(k, b.snumberscaleunit, d.snumberscaleunit).concat(),
							numberscalevalue: v(F, b.snumberscalevalue, d.snumberscalevalue).concat(),
							numberprefix: R(a.snumberprefix, b.snumberprefix, d.snumberprefix),
							numbersuffix: R(a.snumbersuffix, b.snumbersuffix, d.snumbersuffix),
							decimalprecision: parseInt(v(a.syaxisvaluedecimals, a.sdecimals, a.decimals, b.sdecimals, d.sdecimals), 10),
							forcedecimals: v(a.forcesyaxisvaluedecimals, a.sforcedecimals, a.forcedecimals, b.sforcedecimals, d.sforcedecimals),
							decimalseparator: v(a.decimalseparator, b.decimalseparator, d.decimalseparator),
							thousandseparator: v(a.thousandseparator, b.thousandseparator, d.thousandseparator),
							thousandseparatorposition: t.thousandseparatorposition.concat(),
							indecimalseparator: v(a.indecimalseparator, b.indecimalseparator, d.indecimalseparator),
							inthousandseparator: v(a.inthousandseparator, b.inthousandseparator, d.inthousandseparator),
							scalerecursively: g,
							maxscalerecursion: E,
							scaleseparator: W
						};
						k = q({}, F);
						k.decimalprecision = parseInt(v(a.sdecimals, a.decimals, a.syaxisvaluedecimals, b.sdecimals, d.sdecimals), 10);
						k.forcedecimals = v(a.sforcedecimals, a.forcedecimals, a.forcesyaxisvaluedecimals, b.sforcedecimals, d.sforcedecimals);
						k.cacheStore = [];
						if (!b.useScaleRecursively || (F.numberscalevalue && F.numberscalevalue.length) != (F.numberscaleunit && F.numberscaleunit.length)) F.scalerecursively = g = 0;
						if (/^(bubble|scatter|selectscatter)$/.test(ca)) A.formatnumber = v(a.yformatnumber, A.formatnumber), A.formatnumberscale = v(a.yformatnumberscale, A.formatnumberscale), A.defaultnumberscale = R(a.ydefaultnumberscale, A.defaultnumberscale), A.numberscaleunit = v(i, A.numberscaleunit), A.numberscalevalue = v(O, A.numberscalevalue), A.numberprefix = v(a.ynumberprefix, A.numberprefix), A.numbersuffix = v(a.ynumbersuffix, A.numbersuffix), t.formatnumber = v(a.yformatnumber, t.formatnumber), t.formatnumberscale = v(a.yformatnumberscale, t.formatnumberscale), t.defaultnumberscale = R(a.ydefaultnumberscale, t.defaultnumberscale), t.numberscaleunit = v(a.ynumberscaleunit, t.numberscaleunit.concat()), t.numberscalevalue = v(a.ynumberscalevalue, t.numberscalevalue.concat()), t.numberprefix = v(a.ynumberprefix, t.numberprefix), t.numbersuffix = v(a.ynumbersuffix, t.numbersuffix);
						if (/^(mscombidy2d|mscombidy3d)$/.test(ca)) F.formatnumberscale = c(a.sformatnumberscale, "1");
						if (/^(pie2d|pie3d|doughnut2d|doughnut3d|marimekko|pareto2d|pareto3d)$/.test(ca)) t.decimalprecision = v(a.decimals, "2");
						r && (t.numberscalevalue.push(1), t.numberscaleunit.unshift(t.defaultnumberscale), A.numberscalevalue.push(1), A.numberscaleunit.unshift(A.defaultnumberscale));
						g && (F.numberscalevalue.push(1), F.numberscaleunit.unshift(F.defaultnumberscale), k.numberscalevalue.push(1), k.numberscaleunit.unshift(k.defaultnumberscale));
						this.Y[0] = {
							yAxisLabelConf: A,
							dataLabelConf: t
						};
						this.Y[1] = {
							yAxisLabelConf: F,
							dataLabelConf: k
						};
						this.paramLabels = t;
						this.param1 = A;
						this.param2 = F;
						this.paramLabels2 = k
					}
					this.paramX = {
						cacheStore: [],
						formatnumber: v(a.xformatnumber, e.formatnumber),
						formatnumberscale: v(a.xformatnumberscale, e.formatnumberscale),
						defaultnumberscale: R(a.xdefaultnumberscale, e.defaultnumberscale),
						numberscaleunit: v(u, e.numberscaleunit.concat()),
						numberscalevalue: v(l, e.numberscalevalue.concat()),
						numberprefix: v(a.xnumberprefix, e.numberprefix),
						numbersuffix: v(a.xnumbersuffix, e.numbersuffix),
						decimalprecision: parseInt(v(a.xaxisvaluedecimals, a.xaxisvaluesdecimals, e.decimalprecision, 2), 10),
						forcedecimals: v(a.forcexaxisvaluedecimals, 0),
						decimalseparator: e.decimalseparator,
						thousandseparator: e.thousandseparator,
						thousandseparatorposition: e.thousandseparatorposition.concat(),
						indecimalseparator: e.indecimalseparator,
						inthousandseparator: e.inthousandseparator,
						scalerecursively: x,
						maxscalerecursion: y,
						scaleseparator: o
					};
					if (!b.useScaleRecursively || (this.paramX.numberscalevalue && this.paramX.numberscalevalue.length) != (this.paramX.numberscaleunit && this.paramX.numberscaleunit.length)) this.paramX.scalerecursively = x = 0;
					x && (this.paramX.numberscalevalue.push(1), this.paramX.numberscaleunit.unshift(this.paramX.defaultnumberscale));
					this.paramScale = {
						cacheStore: [],
						formatnumber: v(a.tickformatnumber, e.formatnumber),
						formatnumberscale: v(a.tickformatnumberscale, e.formatnumberscale),
						defaultnumberscale: R(a.tickdefaultnumberscale, e.defaultnumberscale),
						numberscaleunit: v(j, e.numberscaleunit.concat()),
						numberscalevalue: v(N, e.numberscalevalue.concat()),
						numberprefix: v(a.ticknumberprefix, e.numberprefix),
						numbersuffix: v(a.ticknumbersuffix, e.numbersuffix),
						decimalprecision: parseInt(v(a.tickvaluedecimals, e.decimalprecision, "2")),
						forcedecimals: v(a.forcetickvaluedecimals, e.forcedecimals, 0),
						decimalseparator: e.decimalseparator,
						thousandseparator: e.thousandseparator,
						thousandseparatorposition: e.thousandseparatorposition.concat(),
						indecimalseparator: e.indecimalseparator,
						inthousandseparator: e.inthousandseparator,
						scalerecursively: r,
						maxscalerecursion: I,
						scaleseparator: ha
					};
					r && (this.paramScale.numberscalevalue.push(1), this.paramScale.numberscaleunit.unshift(this.paramScale.defaultnumberscale));
					this.timeConf = {
						inputDateFormat: v(a.inputdateformat, a.dateformat),
						outputDateFormat: v(a.outputdateformat, a.inputdateformat, a.dateformat),
						days: ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"],
						months: ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"],
						daySuffix: ["st", "nd", "rd", "th", "th", "th", "th", "th", "th", "th", "th", "th", "th", "th", "th", "th", "th", "th", "th", "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th", "th", "st"]
					}
				};
			e.prototype = {
				cleaneValueCacheStore: {},
				percentStrCacheStore: {},
				dispose: function() {
					this.Y && delete this.Y;
					this.cleaneValueCacheStore && delete this.cleaneValueCacheStore;
					this.percentStrCacheStore && delete this.percentStrCacheStore;
					this.baseConf && delete this.baseConf;
					this.timeConf && delete this.timeConf;
					this.paramX && delete this.paramX;
					this.paramScale && delete this.paramScale
				},
				parseMLAxisConf: function(a, b) {
					var f = this.baseConf,
						e = this.csConf,
						k = this.chartAPI,
						q = c(a.scalerecursively, f.scalerecursively),
						u = c(a.maxscalerecursion, f.maxscalerecursion),
						j = Z(a.scaleseparator, f.scaleseparator),
						i, l, N, O, n, ca, b = c(b, this.Y.length);
					Z(a.numberscaleunit) && (i = a.numberscaleunit.split(","));
					Z(a.numberscalevalue) && (l = a.numberscalevalue.split(","));
					u || (u = -1);
					if (Z(a.thousandseparatorposition)) {
						N = a.thousandseparatorposition.split(",");
						O = N.length;
						for (ca = d.thousandseparatorposition[0]; O--;)(n = c(s(N[O]))) ? ca = n : n = ca, N[O] = n
					}
					f = {
						cacheStore: [],
						formatnumber: v(a.formatnumber, f.formatnumber),
						formatnumberscale: v(a.formatnumberscale, f.formatnumberscale),
						defaultnumberscale: R(a.defaultnumberscale, f.defaultnumberscale),
						numberscaleunit: v(i, f.numberscaleunit).concat(),
						numberscalevalue: v(l, f.numberscalevalue).concat(),
						numberprefix: R(a.numberprefix, f.numberprefix),
						numbersuffix: R(a.numbersuffix, f.numbersuffix),
						forcedecimals: v(a.forcedecimals, f.forcedecimals),
						decimalprecision: parseInt(a.decimals === "auto" ? e.decimalprecision : v(a.decimals, f.decimalprecision), 10),
						decimalseparator: v(a.decimalseparator, f.decimalseparator),
						thousandseparator: v(a.thousandseparator, f.thousandseparator),
						thousandseparatorposition: v(N, f.thousandseparatorposition),
						indecimalseparator: R(a.indecimalseparator, f.indecimalseparator),
						inthousandseparator: R(a.inthousandseparator, f.inthousandseparator),
						scalerecursively: q,
						maxscalerecursion: u,
						scaleseparator: j
					};
					if (!k.useScaleRecursively || (f.numberscalevalue && f.numberscalevalue.length) != (f.numberscaleunit && f.numberscaleunit.length)) f.scalerecursively = q = 0;
					k = {
						cacheStore: [],
						formatnumber: f.formatnumber,
						formatnumberscale: f.formatnumberscale,
						defaultnumberscale: f.defaultnumberscale,
						numberscaleunit: f.numberscaleunit.concat(),
						numberscalevalue: f.numberscalevalue.concat(),
						numberprefix: f.numberprefix,
						numbersuffix: f.numbersuffix,
						decimalprecision: parseInt(v(a.yaxisvaluedecimals, f.decimalprecision, 2)),
						forcedecimals: v(a.forceyaxisvaluedecimals, f.forcedecimals),
						decimalseparator: f.decimalseparator,
						thousandseparator: f.thousandseparator,
						thousandseparatorposition: f.thousandseparatorposition.concat(),
						indecimalseparator: f.indecimalseparator,
						inthousandseparator: f.inthousandseparator,
						scalerecursively: q,
						maxscalerecursion: u,
						scaleseparator: j
					};
					q && (f.numberscalevalue.push(1), f.numberscaleunit.unshift(f.defaultnumberscale), k.numberscalevalue.push(1), k.numberscaleunit.unshift(k.defaultnumberscale));
					this.Y[b] = {
						dataLabelConf: f,
						yAxisLabelConf: k
					}
				},
				percentValue: function(c) {
					var d = this.percentStrCacheStore[c];
					d === void 0 && (d = isNaN(this.paramLabels.decimalprecision) ? "2" : this.paramLabels.decimalprecision, d = this.percentStrCacheStore[c] = b(a(c, d, this.paramLabels.forcedecimals), this.paramLabels.decimalseparator, this.paramLabels.thousandseparator, this.paramLabels.thousandseparatorposition) + "%");
					return d
				},
				getCleanValue: function(a, b) {
					var c = this.cleaneValueCacheStore[a],
						d;
					if (c === void 0) {
						c = a;
						d = this.baseConf.indecimalseparator;
						var f = this.baseConf.inthousandseparator;
						c += m;
						Z(f) && (f = f.replace(/(\W)/ig, "\\$1"), c = c.toString().replace(RegExp(f, "g"), m));
						Z(d) && (c = c.replace(d, w));
						d = !isNaN(c = parseFloat(c)) && isFinite(c) ? c : NaN;
						this.cleaneValueCacheStore[a] = c = isNaN(d) ? null : b ? s(d) : d
					}
					return c
				},
				dataLabels: function(a, b) {
					var c = this.Y[b] || (b ? this.Y[1] : this.Y[0]),
						d, c = c && c.dataLabelConf || this.baseConf;
					d = c.cacheStore[a];
					d === void 0 && (d = c.cacheStore[a] = k(a, c));
					return d
				},
				yAxis: function(a, b) {
					var c = this.Y[b] || (b ? this.Y[1] : this.Y[0]),
						d, c = c && c.yAxisLabelConf || this.baseConf;
					d = c.cacheStore[a];
					d === void 0 && (d = c.cacheStore[a] = k(a, c));
					return d
				},
				xAxis: function(a) {
					var b = this.paramX.cacheStore[a];
					b === void 0 && (b = this.paramX.cacheStore[a] = k(a, this.paramX));
					return b
				},
				sYAxis: function(a) {
					var b = this.Y[1],
						c, b = b && b.yAxisLabelConf || this.baseConf;
					c = b.cacheStore[a];
					c === void 0 && (c = b.cacheStore[a] = k(a, b));
					return c
				},
				scale: function(a) {
					var b = this.paramScale.cacheStore[a];
					b === void 0 && (b = this.paramScale.cacheStore[a] = k(a, this.paramScale));
					return b
				},
				getCleanTime: function(a) {
					var b;
					this.timeConf.inputDateFormat && Date.parseExact && (b = Date.parseExact(a, this.timeConf.inputDateFormat));
					return b && b.getTime()
				},
				getDateValue: function(a) {
					a = a && /^dd/.test(this.timeConf.inputDateFormat) && a.replace(/(\d{1,2})\/(\d{1,2})\/(\d{2,4})/, "$2/$1/$3") || a;
					a = new Date(a);
					return {
						ms: a.getTime(),
						date: a
					}
				},
				getFormatedDate: function(a, b) {
					var c = typeof a === "object" && a || this.getDateValue(a).date,
						d = this.timeConf,
						f = v(b, d.outputDateFormat),
						e = c.getFullYear(),
						k = c.getMonth(),
						q = c.getDate(),
						u = c.getDay(),
						j = c.getMinutes(),
						i = c.getSeconds(),
						c = c.getHours();
					f.match(/dnl/) && (f = f.replace(/dnl/ig, d.days[u]));
					f.match(/dns/) && (f = f.replace(/dns/ig, d.days[u].substr(0, 3)));
					f.match(/dd/) && (f = f.replace(/dd/ig, q));
					f.match(/mnl/) && (f = f.replace(/mnl/ig, d.months[k]));
					f.match(/mns/) && (f = f.replace(/mns/ig, d.months[k].substr(0, 3)));
					f.match(/mm/) && (f = f.replace(/mm/ig, k + 1));
					f.match(/yyyy/) && (f = f.replace(/yyyy/ig, e));
					f.match(/yy/) && (f = f.replace(/yy/ig, (e % 1E3 % 100 + "").replace(/^(\d)$/, "0$1")));
					f.match(/hh12/) && (f = f.replace(/hh12/ig, c % 12 || 12));
					f.match(/hh/) && (f = f.replace(/hh/ig, c));
					f.match(/mn/) && (f = f.replace(/mn/ig, j));
					f.match(/ss/) && (f = f.replace(/ss/ig, i));
					f.match(/ampm/) && (f = f.replace(/ampm/ig, c < 12 ? "AM" : "PM"));
					f.match(/ds/) && (f = f.replace(/ds/ig, d.daySuffix[q]));
					return f
				}
			};
			e.prototype.constructor = e;
			var k = function(d, f) {
					if (d !== null) {
						var d = Number(d),
							e = d + m,
							k;
						k = f.formatnumberscale == 1 ? f.defaultnumberscale : m;
						var q;
						q = (q = e.split(".")[1]) ? q.length : f.forcedecimals ? "2" : m;
						if (f.formatnumberscale == 1) {
							var u, e = d;
							k = f.numberscalevalue;
							u = f.numberscaleunit;
							var j = {},
								i = f.defaultnumberscale,
								A = 0,
								N, l = [],
								O = [];
							if (f.scalerecursively) {
								for (A = 0; A < k.length; A++) if (N = c(k[A]) || 1E3, Math.abs(Number(e)) >= N && A < k.length - 1) i = e % N, e = (e - i) / N, i != 0 && (l.push(i), O.push(u[A]));
								else {
									l.push(e);
									O.push(u[A]);
									break
								}
								l.reverse();
								O.reverse();
								j.value = l;
								j.scale = O
							} else {
								if (k.length === u.length) for (A = 0; A < k.length; A++) if ((N = c(k[A]) || 1E3) && Math.abs(Number(e)) >= N) i = u[A] || m, e = Number(e) / N;
								else break;
								j.value = e;
								j.scale = i
							}
							u = j;
							d = e = u.value;
							k = u.scale
						}
						if (f.scalerecursively && f.formatnumberscale != 0) {
							k = u.value;
							u = u.scale;
							j = f.maxscalerecursion == -1 ? k.length : Math.min(k.length, f.maxscalerecursion);
							if (f.formatnumber == 1) {
								e = "";
								for (l = 0; l < j; l++) A = l == 0 ? k[l] : Math.abs(k[l]), N = String(A), l == j - 1 && (N = a(A, v(f.decimalprecision, q), f.forcedecimals)), e = e + b(N, f.decimalseparator, f.thousandseparator, f.thousandseparatorposition) + u[l] + (l < j - 1 ? f.scaleseparator : "")
							} else {
								e = "";
								for (l = 0; l < j; l++) e = e + String(l == 0 ? k[l] : Math.abs(k[l])) + u[l] + (l < j - 1 ? f.scaleseparator : "")
							}
							e = (f.numberprefix || m) + e + (f.numbersuffix || m);
							delete k;
							delete u
						} else f.formatnumber == 1 && (e = a(d, v(f.decimalprecision, q), f.forcedecimals), e = b(e, f.decimalseparator, f.thousandseparator, f.thousandseparatorposition)), e = (f.numberprefix || m) + e + k + (f.numbersuffix || m);
						return e
					}
				};
			return e
		})();
		var Va = function() {
				var a = function(a, c, f, d, e) {
						a = Math.abs(c - a);
						c = a / (f + 1);
						b(a / (f + 1)) > b(d) && (e && Number(c) / Number(d) < (d > 1 ? 2 : 0.5) && (d /= 10), c = (Math.floor(c / d) + 1) * d, a = c * (f + 1));
						return a
					},
					b = function(a) {
						var a = Math.abs(a),
							a = String(a),
							b = 0,
							c = a.indexOf(w);
						c != -1 && (b = a.length - c - 1);
						return b
					};
				return function(c, d, f, e, p, k, q, u) {
					var j, c = isNaN(c) == !0 || c == void 0 ? 0.1 : c,
						d = isNaN(d) == !0 || d == void 0 ? 0 : d;
					c == d && c == 0 && (c = 0.1);
					var k = typeof k === void 0 ? !0 : k,
						i = Math.max(Math.floor(Math.log(Math.abs(d)) / Math.LN10), Math.floor(Math.log(Math.abs(c)) / Math.LN10));
					j = Math.pow(10, i);
					Math.abs(c) / j < 2 && Math.abs(d) / j < 2 && (i--, j = Math.pow(10, i));
					i = Math.pow(10, Math.floor(Math.log(c - d) / Math.LN10));
					c - d > 0 && j / i >= 10 && (j = i);
					var i = (Math.floor(c / j) + 1) * j,
						l;
					d < 0 ? l = -1 * (Math.floor(Math.abs(d / j)) + 1) * j : k ? l = 0 : (l = Math.floor(Math.abs(d / j) - 1) * j, l = l < 0 ? 0 : l);
					(typeof p === void 0 || p) && c <= 0 && (i = 0);
					p = f == null || f == void 0 || f == m ? !1 : !0;
					k = e == null || e == void 0 || e == m || isNaN(Number(e)) ? !1 : !0;
					c = p == !1 || p == !0 && Number(f) < c && c - Number(f) > V ? i : Number(f);
					d = k == !1 || k == !0 && Number(e) > d && Number(e) - d > V ? l : Number(e);
					e = Math.abs(c - d);
					if (k == !1 && p == !1 && u == !0) if (c > 0 && d < 0) for (var u = !1, f = j > 10 ? j / 10 : j, p = a(d, c, q, f, !1) - (q + 1) * f, N, A, O, n; u == !1;) {
						if (p += (q + 1) * f, !(b(p / (q + 1)) > b(f))) if (N = p - e, k = p / (q + 1), l = Math.min(Math.abs(d), c), i = l == Math.abs(d) ? -1 : 1, q == 0) u = !0;
						else for (n = 1; n <= Math.floor((q + 1) / 2); n++) A = k * n, !(A - l > N) && A > l && (O = p - A, O / k == Math.floor(O / k) && A / k == Math.floor(A / k) && (e = p, c = i == -1 ? O : A, d = i == -1 ? -A : -O, u = !0))
					} else u = a(d, c, q, j, !0), N = u - e, e = u, c > 0 ? c += N : d -= N;
					else if (u == !0 && q > 0) {
						u = 0;
						for (f = 1;;) {
							N = q + u * f;
							N = N == 0 ? 1 : N;
							if (!(b(e / (N + 1)) > b(j))) break;
							u = f == -1 || u > q ? ++u : u;
							if (u > 25) {
								N = 0;
								break
							}
							f = u <= q ? f * -1 : 1
						}
						q = N
					}
					return {
						Max: c,
						Min: d,
						Range: e,
						interval: j,
						divGap: (c - d) / (q + 1)
					}
				}
			}(),
			Ra = function() {
				var a = function(a, b) {
						this.title.y = a.offsetHeight / 2;
						if (b !== void 0) this.title.text = b
					};
				a.prototype = {
					chart: {
						events: {},
						margin: [0, 0, 0, 0],
						backgroundColor: {
							FCcolor: {
								alpha: 0
							}
						}
					},
					credits: {
						href: "",
						text: "FusionCharts",
						enabled: !0
					},
					legend: {
						enabled: !1
					},
					title: {
						text: "",
						style: {
							fontFamily: "Verdana",
							fontSize: "10px",
							color: "#666666"
						}
					},
					plotOptions: {
						series: {}
					},
					series: [{}],
					exporting: {
						enabled: !1
					},
					nativeMessage: !0
				};
				return a.prototype.constructor = a
			}(),
			sa = {
				"true": {
					"true": {
						"true": "center",
						"false": "center"
					},
					"false": {
						"true": "center",
						"false": "center"
					}
				},
				"false": {
					"true": {
						"true": "right",
						"false": "left"
					},
					"false": {
						"true": "left",
						"false": "right"
					}
				}
			},
			Ma = function() {
				return function(b, d, f, k, u, j, p) {
					var z, t = f.trendStyle,
						i, P, l, N, O, A, n, ca, F, ea, r, g;
					parseInt(t.fontSize, 10);
					if (!(j ? !f.showVLines : !f.showTrendlines)) {
						z = 0;
						for (P = b.length; z < P; z += 1) if (b[z].line) {
							i = 0;
							for (l = b[z].line.length; i < l; i += 1) if (N = b[z].line[i], ea = f.numberFormatter.getCleanValue(v(N.startvalue, N.value, 0)), r = f.numberFormatter.getCleanValue(v(N.endvalue, v(N.startvalue, N.value, 0))), j ? ca = d : k && N.parentyaxis && /^s$/i.test(N.parentyaxis) ? (ca = d[1], g = 1) : ca = d[0], A = ca.max, n = ca.min, O = !1, A >= ea && A >= r && n <= ea && n <= r) {
								k && N.parentyaxis && /^s$/i.test(N.parentyaxis) ? O = v(N.valueonleft, f.trendlineValuesOnOpp) !== "1" : k || (O = v(N.valueonright, f.trendlineValuesOnOpp) === "1");
								A = Boolean(c(N.istrendzone, j ? 1 : 0));
								if (n = (j ? f.showVLineLabels : f.showTrendlineLabels) ? a(v(N.displayvalue, f.numberFormatter[j ? "xAxis" : "dataLabels"](O ? r : ea, g))) : m) {
									if (F = ea < r, O = {
										text: n,
										textAlign: u ? "center" : O ? "left" : "right",
										align: u ? sa[A][!p][F] : O ? "right" : "left",
										verticalAlign: u ? "bottom" : "middle",
										rotation: 0,
										x: 0,
										y: 0,
										style: t
									}, n = v(N.color, f.trendlineColor), N.alwaysVisible = A, n) O.style = q({}, t), O.style.color = n.replace(e, "#")
								} else O = void 0;
								n = v(N.tooltext);
								F = c(N.thickness, f.trendlineThickness, 1);
								A ? ca.plotBands.push({
									isTrend: !0,
									color: va(v(N.color, f.trendlineColor), v(N.alpha, f.trendlineAlpha, 40)),
									from: ea,
									to: r,
									label: O,
									zIndex: !f.is3d && v(N.showontop, f.showTrendlinesOnTop) === "1" ? 5 : 3,
									tooltext: n,
									alwaysVisible: N.alwaysVisible
								}) : ca.plotLines.push({
									isTrend: !0,
									color: va(v(N.color, f.trendlineColor, f.trendlineColor), v(N.alpha, f.trendlineAlpha, 99)),
									value: ea,
									to: r,
									width: F,
									dashStyle: v(N.dashed, f.trendlinesAreDashed) == "1" ? Na(c(N.dashlen, f.trendlinesDashLen), c(N.dashgap, f.trendlinesDashGap), F) : void 0,
									label: O,
									zIndex: !f.is3d && v(N.showontop, f.showTrendlinesOnTop) === "1" ? 5 : 3,
									tooltext: n
								})
							}
						}
					}
				}
			}(),
			Na = function(a, b, c, f) {
				return f || f === void 0 ? [a, b] : m
			},
			Wa = function() {},
			Ta = function(a, b, c) {
				var f, d = Ta[a];
				if (!d) d = function() {}, d.prototype = c instanceof Wa ? c : new Wa, d.prototype.constructor = d, d = Ta[a] = new d;
				if (c) d.base = c;
				d.name = a;
				for (f in b) switch (typeof b[f]) {
				case "object":
					if (b[f] instanceof Wa) {
						d[f] = b[f][f];
						break
					}
				default:
					d[f] = b[f];
					break;
				case "undefined":
					delete d[f]
				}
				return this instanceof
				Ta ? (a = function() {}, a.prototype = d, a.prototype.constructor = a, new a) : d
			};
		g.extend(g.hcLib, {
			BLANKSTRINGPLACEHOLDER: "#BLANK#",
			BLANKSTRING: m,
			COLOR_BLACK: "000000",
			COLOR_GLASS: "rgba(255, 255, 255, 0.3)",
			COLOR_WHITE: "FFFFFF",
			COLOR_TRANSPARENT: "rgba(0,0,0,0)",
			HASHSTRING: "#",
			BREAKSTRING: "<br />",
			STRINGSTRING: "string",
			OBJECTSTRING: "object",
			COMMASTRING: ",",
			ZEROSTRING: U,
			SAMPLESTRING: "Ay0",
			TESTSTR: "Ag",
			ONESTRING: "1",
			DECIMALSTRING: w,
			STRINGUNDEFINED: "undefined",
			POSITION_TOP: "top",
			POSITION_RIGHT: "right",
			POSITION_BOTTOM: "bottom",
			POSITION_LEFT: "left",
			POSITION_CENTER: "center",
			POSITION_MIDDLE: "middle",
			POSITION_START: "start",
			POSITION_END: "end",
			FC_CONFIG_STRING: "_FCconf",
			SHAPE_RECT: "rect",
			HUNDREDSTRING: "100",
			PXSTRING: "px",
			COMMASPACE: ", ",
			TEXTANCHOR: "text-anchor",
			regex: {
				stripWhitespace: B,
				dropHash: e,
				startsRGBA: r,
				cleanColorCode: x,
				breakPlaceholder: $,
				hexcode: /^#?[0-9a-f]{6}/i
			},
			fireEvent: function(a, b, c, f) {
				var d = jQuery.Event(b),
					e = "detached" + b;
				extend(d, c);
				a[b] && (a[e] = a[b], a[b] = null);
				jQuery(a).trigger(d);
				a[e] && (a[b] = a[e], a[e] = null);
				f && !d.isDefaultPrevented() && f(d)
			},
			addEvent: f,
			removeEvent: j,
			getTouchEvent: i,
			extend2: q,
			deltend: function(a, b) {
				if (typeof a !== "object" || typeof b !== "object") return null;
				u(a, b);
				return a
			},
			imprint: function(a, b, c) {
				var f;
				if (typeof a !== "object" || a === null) return b;
				if (typeof b !== "object" || b === null) return a;
				for (f in b) if (a[f] === void 0 || !c && a[f] === null) a[f] = b[f];
				return a
			},
			pluck: v,
			pluckNumber: c,
			getFirstDefinedValue: function() {
				var a, b, c;
				b = 0;
				for (c = arguments.length; b < c; b += 1) if ((a = arguments[b]) || !(a !== !1 && a !== 0 && a != m)) return a
			},
			createElement: function(a, b, c) {
				var a = S.createElement(a),
					f;
				for (f in b) a.setAttribute(f, b[f]);
				c && c.appendChild && c.appendChild(a);
				return a
			},
			hashify: function(a) {
				return a && a.replace(/^#?([a-f0-9]+)/ig, "#$1") || "none"
			},
			pluckFontSize: function() {
				var a, b, c;
				b = 0;
				for (c = arguments.length; b < c; b += 1) if ((a = arguments[b]) || !(a !== !1 && a !== 0)) if (!isNaN(a = Number(a))) return a < 1 ? 1 : a;
				return 1
			},
			getValidValue: Z,
			getPosition: M,
			getViewPortDimension: O,
			bindSelectionEvent: function(a, b, d) {
				var d = d || {},
					e = a.options.chart,
					k = a.container,
					u = e.zoomType,
					p = q({}, d.attr || {}),
					d = p["stroke-width"] = c(p.strokeWidth, p["stroke-width"], 1),
					z = M(k),
					d = {
						chart: a,
						zoomX: /x/.test(u),
						zoomY: /y/.test(u),
						canvasY: a.canvasTop,
						canvasX: a.canvasLeft,
						canvasW: a.canvasWidth,
						canvasH: a.canvasHeight,
						canvasX2: a.canvasLeft + a.canvasWidth,
						canvasY2: a.canvasTop + a.canvasHeight,
						strokeWidth: d,
						chartPosLeft: z.left,
						chartPosTop: z.top,
						attr: p,
						callback: b
					};
				p.stroke = R(p.stroke, "rgba(51,153,255,0.8)");
				p.fill = R(p.fill, "rgba(185,213,241,0.3)");
				p.ishot = !0;
				k && (j(k, "dragstart drag dragend", aa), f(k, "dragstart drag dragend", aa, d));
				e.link && (j(a.container, "mouseup mousedown", D), f(a.container, "mouseup mousedown", D, d))
			},
			createContextMenu: function(a) {
				var b = a.chart,
					c = b.smartLabel,
					f = a.labels,
					d = a.hover || {
						fill: "rgba(64, 64, 64, 1)"
					},
					e = a.attrs || {
						fill: "rgba(255, 255, 255, 1)"
					},
					k = f && f.style || {
						fontSize: "12px",
						color: "000000"
					},
					q = f && f.attrs || {},
					t = f && f.hover || {
						color: "FFFFFF"
					},
					u = a.items,
					j = a.position,
					i = a.verticalPadding || 5,
					N = a.horizontalPadding || 10,
					l = g.hcLib.Raphael,
					A = {},
					O, n, ca;
				if (b) O = M(b.container);
				else return !1;
				var v = function() {
						ca = this;
						var a = A.items,
							b = a.length,
							f = 0,
							d = 0,
							ha = 0,
							W = 0,
							o, t = A.group;
						if (!A.menuItems) A.menuItems = [];
						for (c.setStyle(k); b--;) o = a[b], o = c.getOriSize(o.text), ha || (ha = o.height + 2 * i), f += ha, d = Math.max(d, o.width + 2 * N);
						A.height = f;
						A.width = d;
						A.itemH = ha;
						ca.setSize(d + 5, f + 5);
						if (!t) t = A.group = ca.group("contextmenu-container");
						A.menuRect ? A.menuRect.attr({
							width: d,
							height: f
						}) : A.menuRect = ca.rect(0, 0, d, f, 0, t).shadow(!0).attr({
							"stroke-width": 1,
							fill: "rgba(255, 255, 255, 1)"
						});
						f = a.length;
						for (b = 0; b < f; b += 1) o = a[b], A.menuItems[b] ? A.menuItems[b].label.attr({
							text: o.text
						}) : (A.menuItems[b] = {}, A.menuItems[b].box = ca.rect(0, W, d, ha, 0, t).attr({
							stroke: "none"
						}).attr(e).click(J).hover(x, h), A.menuItems[b].label = ca.text(N / 2, W + ha / 2, o.text, t).attr({
							"text-anchor": "start"
						}).attr(q).css(k).click(J).hover(x, h), A.menuItems[b].box._itemIdx = b, A.menuItems[b].label._itemIdx = b, W += ha);
						for (; A.menuItems[b];) A.menuItems[b].box.remove(), A.menuItems[b].label.remove(), A.menuItems.splice(b, 1)
					},
					F = function(a) {
						var c = a.x,
							a = a.y,
							f = {
								x: c + O.left,
								y: a + O.top
							},
							d = A.width,
							e = A.height,
							W = b.chartHeight;
						c + d > b.chartWidth && c - d > 0 && (f.x -= d);
						a + e > W && a - e > 0 && (f.y -= e);
						return f
					},
					r = function() {
						A.hide()
					},
					x = function() {
						var a = A.menuItems[this._itemIdx];
						n && clearTimeout(n);
						a.box.attr(d);
						a.label.css(t)
					},
					h = function() {
						var a = A.menuItems[this._itemIdx];
						a.box.attr(e);
						a.label.css(k);
						n = setTimeout(A.hide, 800)
					},
					J = function(a) {
						var b = A.items[this._itemIdx];
						b.onclick && b.onclick.call(b, a);
						A.hide()
					};
				A.showItem = function(a) {
					var b = this.menuItems[a],
						c = this.height,
						f = this.itemH;
					if (b && b._isHidden) {
						c = this.height = c + f;
						this.menuRect.attr({
							height: c
						});
						b.box.show();
						b.label.show();
						b._isHidden = !1;
						b = F(j);
						this.left = b.x;
						this.top = b.y;
						for (a += 1; b = this.menuItems[a];) b.box.attr({
							y: b.box.attrs.y + f
						}), b.label.attr({
							y: b.label.attrs.y + f
						}), a += 1
					}
				};
				A.hideItem = function(a) {
					var b = this.menuItems[a],
						c = this.height,
						f = this.itemH;
					if (b && !b._isHidden) {
						b.box.hide();
						b.label.hide();
						c = this.height = c - f;
						this.menuRect.attr({
							height: c
						});
						b._isHidden = !0;
						b = F(j);
						this.left = b.x;
						this.top = b.y;
						for (a += 1; b = this.menuItems[a];) b.box.attr({
							y: b.box.attrs.y - f
						}), b.label.attr({
							y: b.label.attrs.y - f
						}), a += 1
					}
				};
				A.redraw = function() {
					var a = this.paper;
					this.items = u;
					a ? v.call(this.paper) : j && j.x !== void 0 && j.y !== void 0 ? (this.paper = l(0, 0, 100, 100), v.call(this.paper), a = F(j), this.left = a.x, this.top = a.y, this.paper.canvas.style.left = this.left + "px", this.paper.canvas.style.top = this.top + "px") : (this.paper = l(0, 0, 100, 100), v.call(this.paper))
				};
				A.show = function(a) {
					this.visible = !0;
					a && a.x !== void 0 && a.y !== void 0 ? (a = F(a), this.paper.canvas.style.left = a.x + "px", this.paper.canvas.style.top = a.y + "px") : (this.paper.canvas.style.left = this.left + "px", this.paper.canvas.style.top = this.top + "px");
					A.group.show();
					setTimeout(function() {
						l.click(r)
					}, 50)
					//todo by jyl add 放大解决按钮看不到的情况
					showZindex();
				};
				A.hide = function() {
					this.visible = !1;
					A.group.hide();
					A.paper.canvas.style.left = -A.width + "px";
					A.paper.canvas.style.top = -A.height + "px";
					l.unclick(r)
				};
				A.update = function(a) {
					if (a && a.length) this.items = a, this.redraw()
				};
				A.updatePosition = function(a) {
					var c = {
						left: O.left,
						top: O.top
					};
					O = M(b.container);
					a ? (j = a, a = F(a), this.left = a.x, this.top = a.y) : (this.left -= c.left - O.left, this.top -= c.top - O.top)
				};
				A.add = function(a) {
					var b = this.paper,
						c = this.menuItems,
						f = c.length;
					c[f] = {};
					c[f].box = b.rect(0, this.height, this.width, this.itemH, 0).attr(e).hover(x, h);
					A.menuItems[f].label = b.text(this.width / 2, this.height + this.itemH / 2, a.text).attr(q).css(k).hover(x, h);
					A.menuItems[f].box._itemIdx = f;
					A.menuItems[f].label._itemIdx = f;
					this.height += this.itemH;
					this.menuRect.attr({
						height: this.height
					})
				};
				A.removeItems = function() {
					for (var a = this.menuItems, b = a && a.length, c; b--;) c = a[b], c.box && c.box.remove && c.box.remove(), c.label && c.label.remove && c.label.remove();
					delete this.menuItems;
					delete this.items
				};
				A.setPosition = function(a) {
					a.x !== void 0 && a.y !== void 0 && this.paper.setViewBox(a.x, a.y, this.width, this.height)
				};
				A.destroy = function() {
					this.removeItems();
					this.menuRect.remove()
				};
				u && u.length && (A.redraw(), A.hide());
				return A
			},
			getDefinedColor: function(a, b) {
				return !a && a != 0 && a != !1 ? b : a
			},
			getFirstValue: R,
			getFirstColor: function(a) {
				a = a.split(",")[0];
				a = a.replace(B, m);
				a == m && (a = "000000");
				return a.replace(e, "#")
			},
			getColorCodeString: function(a, b) {
				var c = "",
					f, d, e = 0,
					k = b.split(",");
				for (d = k.length; e < d; e += 1) f = k[e].split("-"), c += f.length === 2 ? f[0].indexOf("dark") !== "-1" ? ma(a, 100 - parseInt(f[1], 10)) + "," : Ca(a, 100 - parseInt(f[1], 10)) + "," : k[e] + ",";
				return c.substring(0, c.length - 1)
			},
			pluckColor: function(a) {
				if (Z(a)) return a = a.split(",")[0], a = a.replace(B, m), a == m && (a = "000000"), a.replace(e, "#")
			},
			trimString: function(a) {
				for (var a = a.replace(/^\s\s*/, ""), b = /\s/, c = a.length; b.test(a.charAt(c -= 1)););
				return a.slice(0, c + 1)
			},
			getFirstAlpha: function(a) {
				a = parseInt(a, 10);
				if (isNaN(a) || a > 100 || a < 0) a = 100;
				return a
			},
			parsePointValue: d,
			parseUnsafeString: a,
			toPrecision: function(a, b) {
				var c = ka(10, b);
				return G(a * c) / c
			},
			hasTouch: Y,
			getSentenceCase: function(a) {
				a = a || m;
				return a.charAt(0).toUpperCase() + a.substr(1)
			},
			getCrispValues: function(a, b, c) {
				var f = c % 2 / 2,
					c = G(a + f) - f,
					a = G(a + b + f) - f - c;
				return {
					position: c,
					distance: a
				}
			},
			isArray: J,
			stubFN: function() {},
			falseFN: function() {
				return !1
			},
			stableSort: function(a, b) {
				var c = a.length,
					f;
				for (f = 0; f < c; f++) a[f].ss_i = f;
				a.sort(function(a, c) {
					var f = b(a, c);
					return f === 0 ? a.ss_i - c.ss_i : f
				});
				for (f = 0; f < c; f++) delete a[f].ss_i
			},
			hasSVG: ia,
			isIE: b,
			getLinkAction: function(a, b) {
				var f = function(a) {
						return a
					};
				return function() {
					var d = c((a.chart || a.map || {}).unescapelinks, 1),
						e = R(this.link, m),
						k = v(e, this.options && this.options.chart && this.options.chart.link || m, this.series && this.series.chart && this.series.chart.options && this.series.chart.options.chart && this.series.chart.options.chart.link || m),
						p = k,
						q, t, u, j, i, l, O, A, n, F;
					if (k !== void 0) {
						d && (k = h.decodeURIComponent ? h.decodeURIComponent(k) : unescape(k));
						k = k.replace(/^\s+/, m).replace(/\s+$/, m);
						if (k.search(/^[a-z]*\s*[\-\:]\s*/i) !== -1) i = k.split(/\s*[\-\:]\s*/)[0].toLowerCase(), F = i.length;
						setTimeout(function() {
							switch (i) {
							case "j":
								k = k.replace(/^j\s*\-/i, "j-");
								q = k.indexOf("-", 2);
								q === -1 ? N(k.slice(2)) : N(k.substr(2, q - 2).replace(/\s/g, m), k.slice(q + 1));
								break;
							case "javascript":
								ca(k.replace(/^javascript\s*\:/i, m));
								break;
							case "n":
								k.replace(/^n\s*\-/i, "n-");
								h.open(f(k.slice(2), d));
								break;
							case "f":
								k = k.replace(/^f\s*\-/i, "f-");
								q = k.indexOf("-", 2);
								q !== -1 ? (t = k.substr(2, q - 2)) && h.frames[t] ? h.frames[t].location = f(k.slice(q + 1), d) : h.open(f(k.slice(q + 1), d), t) : h.open(f(k.slice(2), d));
								break;
							case "p":
								k = k.replace(/p\s*\-/i, "p-");
								q = k.indexOf("-", 2);
								u = k.indexOf(",", 2);
								q === -1 && (q = 1);
								j = f(k.slice(q + 1), d);
								h.open(j, k.substr(2, u - 2), k.substr(u + 1, q - u - 1)).focus();
								break;
							case "newchart":
							case "newmap":
								k.charAt(F) === ":" && (q = k.indexOf("-", F + 1), n = k.substring(F + 1, q), F = q);
								q = k.indexOf("-", F + 1);
								l = k.substring(F + 1, q).toLowerCase();
								switch (l) {
								case "xmlurl":
								case "jsonurl":
									A = k.substring(q + 1, k.length);
									break;
								case "xml":
								case "json":
									var c = O = k.substring(q + 1, k.length),
										e = {
											chart: {}
										},
										v, c = c.toLowerCase();
									if (a.linkeddata) for (v = 0; v < a.linkeddata.length; v += 1) a.linkeddata[v].id.toLowerCase() === c && (e = a.linkeddata[v].linkedchart || a.linkeddata[v].linkedmap);
									A = e;
									l = "json"
								}
								g.raiseEvent("LinkedChartInvoked", {
									alias: n,
									linkType: l.toUpperCase(),
									data: A
								}, b);
								break;
							default:
								h.location.href = k
							}
							g.raiseEvent("linkclicked", {
								linkProvided: p,
								linkInvoked: k,
								linkAction: i && i.toLowerCase()
							}, b)
						}, 0)
					}
				}
			},
			graphics: {
				parseAlpha: pa,
				convertColor: va,
				getDarkColor: Ca,
				getLightColor: ma,
				mapSymbolName: function(a, b) {
					var c = ta.circle,
						a = d(a);
					a >= 3 && (c = (b ? ta.spoke : ta.poly) + a);
					return c
				},
				getColumnColor: function(a, b, c, f, d, e, k, q, t) {
					var u, j;
					u = a.split(",");
					j = b.split(",");
					e = e.split(",");
					k = k.split(",");
					a = a.replace(/\s/g, m).replace(/\,$/, m);
					t ? q = {
						FCcolor: {
							color: u[0],
							alpha: j[0]
						}
					} : d ? (a = u[0], j = j[0], q = {
						FCcolor: {
							color: Ca(a, 75) + "," + ma(a, 10) + "," + Ca(a, 90) + "," + ma(a, 55) + "," + Ca(a, 80),
							alpha: j + "," + j + "," + j + "," + j + "," + j,
							ratio: "0,11,14,57,18",
							angle: q ? "90" : "0"
						}
					}, e = [Ca(a, 70)]) : (b = pa(b, u.length), q = {
						FCcolor: {
							color: a,
							alpha: b,
							ratio: c,
							angle: q ? -f : f
						}
					});
					return [q, {
						FCcolor: {
							color: e[0],
							alpha: k[0]
						}
					}]
				},
				getAngle: function(a, b, c) {
					a = Math.atan(b / a) * 180 / Math.PI;
					c == 2 ? a = 180 - a : c == 3 ? a += 180 : c == 4 && (a = 360 - a);
					return a
				},
				parseColor: X,
				getValidColor: function(a) {
					return fa.test(X(a)) && a
				},
				HSBtoRGB: function(a) {
					var b = a[0],
						c = 0,
						f = 0,
						d = 0,
						e = [],
						e = a[1] / 100,
						a = a[2] / 100,
						k = b / 60 - Math.floor(b / 60),
						q = a * (1 - e),
						t = a * (1 - k * e),
						e = a * (1 - (1 - k) * e);
					switch (Math.floor(b / 60) % 6) {
					case 0:
						c = a;
						f = e;
						d = q;
						break;
					case 1:
						c = t;
						f = a;
						d = q;
						break;
					case 2:
						c = q;
						f = a;
						d = e;
						break;
					case 3:
						c = q;
						f = t;
						d = a;
						break;
					case 4:
						c = e;
						f = q;
						d = a;
						break;
					case 5:
						c = a, f = q, d = t
					}
					return e = [G(c * 255), G(f * 255), G(d * 255)]
				},
				RGBtoHSB: function(a) {
					var b = a[0],
						c = a[1],
						a = a[2],
						f = Math.max(Math.max(b, c), a),
						d = Math.min(Math.min(b, c), a),
						e = 0,
						k = 0;
					f == d ? e = 0 : f == b ? e = (60 * (c - a) / (f - d) + 360) % 360 : f == c ? e = 60 * (a - b) / (f - d) + 120 : f == a && (e = 60 * (b - c) / (f - d) + 240);
					k = f == 0 ? 0 : (f - d) / f;
					return [G(e), G(k * 100), G(f / 255 * 100)]
				},
				RGBtoHex: function(a) {
					return ("000000" + (a[0] << 16 | a[1] << 8 | a[2]).toString(16)).slice(-6)
				},
				HEXtoRGB: function(a) {
					var a = parseInt(a, 16),
						b = Math.floor(a / 65536),
						c = Math.floor((a - b * 65536) / 256);
					return [b, c, Math.floor(a - b * 65536 - c * 256)]
				}
			},
			setImageDisplayMode: function(a, b, c, f, d, e, k, q) {
				var t = q.width * (f / 100),
					f = q.height * (f / 100),
					q = {},
					u, j = e - d * 2;
				u = k - d * 2;
				var i = function(a, b, c, f, e, k) {
						var q = {};
						switch (a) {
						case "top":
							q.y = d;
							break;
						case "bottom":
							q.y = k - f - d;
							break;
						case "middle":
							q.y = (k - f) / 2
						}
						switch (b) {
						case "left":
							q.x = d;
							break;
						case "right":
							q.x = e - c - d;
							break;
						case "middle":
							q.x = (e - c) / 2
						}
						return q
					};
				switch (a) {
				case "center":
					q.width = t;
					q.height = f;
					q.y = k / 2 - f / 2;
					q.x = e / 2 - t / 2;
					break;
				case "stretch":
					q.width = e - d * 2;
					q.height = k - d * 2;
					q.y = d;
					q.x = d;
					break;
				case "tile":
					q.width = t;
					q.height = f;
					q.tileInfo = {};
					q.tileInfo.xCount = a = Math.ceil(j / t);
					q.tileInfo.yCount = u = Math.ceil(u / f);
					alignObj = i(b, c, t * a, f * u, e, k);
					q.y = alignObj.y;
					q.x = alignObj.x;
					break;
				case "fit":
					a = t / f > j / u ? j / t : u / f;
					q.width = t * a;
					q.height = f * a;
					alignObj = i(b, c, q.width, q.height, e, k);
					q.y = alignObj.y;
					q.x = alignObj.x;
					break;
				case "fill":
					a = t / f > j / u ? u / f : j / t;
					q.width = t * a;
					q.height = f * a;
					alignObj = i(b, c, q.width, q.height, e, k);
					q.y = alignObj.y;
					q.x = alignObj.x;
					break;
				default:
					alignObj = i(b, c, t, f, e, k), q.width = t, q.height = f, q.y = alignObj.y, q.x = alignObj.x
				}
				return q
			},
			setLineHeight: Ka,
			supportedStyle: Aa,
			getAxisLimits: Va,
			createTrendLine: Ma,
			getDashStyle: Na,
			axisLabelAdder: bb,
			chartAPI: Ta,
			createDialog: Ra,
			defaultPaletteOptions: {
				bgColor: ["CBCBCB,E9E9E9", "CFD4BE,F3F5DD", "C5DADD,EDFBFE", "A86402,FDC16D", "FF7CA0,FFD1DD"],
				bgAngle: [270, 270, 270, 270, 270],
				bgRatio: ["0,100", "0,100", "0,100", "0,100", "0,100"],
				bgAlpha: ["50,50", "60,50", "40,20", "20,10", "30,30"],
				canvasBgColor: ["FFFFFF", "FFFFFF", "FFFFFF", "FFFFFF", "FFFFFF"],
				canvasBgAngle: [0, 0, 0, 0, 0],
				canvasBgAlpha: ["100", "100", "100", "100", "100"],
				canvasBgRatio: [m, m, m, m, m],
				canvasBorderColor: ["545454", "545454", "415D6F", "845001", "68001B"],
				canvasBorderAlpha: [100, 100, 100, 90, 100],
				showShadow: [0, 1, 1, 1, 1],
				divLineColor: ["717170", "7B7D6D", "92CDD6", "965B01", "68001B"],
				divLineAlpha: [40, 45, 65, 40, 30],
				altHGridColor: ["EEEEEE", "D8DCC5", "99C4CD", "DEC49C", "FEC1D0"],
				altHGridAlpha: [50, 35, 10, 20, 15],
				altVGridColor: ["767575", "D8DCC5", "99C4CD", "DEC49C", "FEC1D0"],
				altVGridAlpha: [10, 20, 10, 15, 10],
				anchorBgColor: ["FFFFFF", "FFFFFF", "FFFFFF", "FFFFFF", "FFFFFF"],
				toolTipBgColor: ["FFFFFF", "FFFFFF", "FFFFFF", "FFFFFF", "FFFFFF"],
				toolTipBorderColor: ["545454", "545454", "415D6F", "845001", "68001B"],
				baseFontColor: ["555555", "60634E", "025B6A", "A15E01", "68001B"],
				borderColor: ["767575", "545454", "415D6F", "845001", "68001B"],
				borderAlpha: [50, 50, 50, 50, 50],
				legendBgColor: ["FFFFFF", "FFFFFF", "FFFFFF", "FFFFFF", "FFFFFF"],
				legendBorderColor: ["545454", "545454", "415D6F", "845001", "D55979"],
				plotGradientColor: ["FFFFFF", "FFFFFF", "FFFFFF", "FFFFFF", "FFFFFF"],
				plotBorderColor: ["333333", "8A8A8A", "FFFFFF", "FFFFFF", "FFFFFF"],
				plotFillColor: ["767575", "D8DCC5", "99C4CD", "DEC49C", "FEC1D0"],
				bgColor3D: ["FFFFFF", "FFFFFF", "FFFFFF", "FFFFFF", "FFFFFF"],
				bgAlpha3D: ["100", "100", "100", "100", "100"],
				bgAngle3D: [90, 90, 90, 90, 90],
				bgRatio3D: [m, m, m, m, m],
				canvasBgColor3D: ["DDE3D5", "D8D8D7", "EEDFCA", "CFD2D8", "FEE8E0"],
				canvasBaseColor3D: ["ACBB99", "BCBCBD", "C8A06C", "96A4AF", "FAC7BC"],
				divLineColor3D: ["ACBB99", "A4A4A4", "BE9B6B", "7C8995", "D49B8B"],
				divLineAlpha3D: [100, 100, 100, 100, 100],
				legendBgColor3D: ["F0F3ED", "F3F3F3", "F7F0E8", "EEF0F2", "FEF8F5"],
				legendBorderColor3D: ["C6CFB8", "C8C8C8", "DFC29C", "CFD5DA", "FAD1C7"],
				toolTipbgColor3D: ["FFFFFF", "FFFFFF", "FFFFFF", "FFFFFF", "FFFFFF"],
				toolTipBorderColor3D: ["49563A", "666666", "49351D", "576373", "681C09"],
				baseFontColor3D: ["49563A", "4A4A4A", "49351D", "48505A", "681C09"],
				anchorBgColor3D: ["FFFFFF", "FFFFFF", "FFFFFF", "FFFFFF", "FFFFFF"]
			}
		})
	}
})();
(function(g) {
	g.fn.drag = function(b, r, x) {
		var h = typeof b == "string" ? b : "",
			w = g.isFunction(b) ? b : g.isFunction(r) ? r : null;
		h.indexOf("drag") !== 0 && (h = "drag" + h);
		x = (b == w ? r : x) || {};
		return w ? this.bind(h, x, w) : this.trigger(h)
	};
	var h = g.event,
		m = h.dispatch || h.handle,
		U = "ontouchstart" in document.documentElement,
		w = U ? "touchstart" : "mousedown",
		S = U ? "touchmove touchend" : "mousemove mouseup",
		ia = function(b, r) {
			var x;
			if (!r.touchXY || !b.originalEvent) return b;
			x = b.originalEvent || b.sourceEvent;
			(x = x.changedTouches || x.touches) && x.length && g.extend(b, x[0]);
			return b
		},
		b = h.special,
		B = b.drag = {
			defaults: {
				which: 1,
				distance: 0,
				not: ":input",
				handle: null,
				relative: !1,
				drop: !1,
				click: !1,
				touchXY: !0
			},
			datakey: "dragdata",
			livekey: "livedrag",
			add: function(b) {
				var r = g.data(this, B.datakey) || g.data(this, B.datakey, g.extend({
					related: 0
				}, B.defaults)),
					x = b.data || {};
				r.related += 1;
				if (!r.live && b.selector) r.live = !0, h.add(this, "draginit." + B.livekey, B.delegate);
				g.each(B.defaults, function(b) {
					x[b] !== void 0 && (r[b] = x[b])
				})
			},
			remove: function() {
				(g.data(this, B.datakey) || {}).related -= 1
			},
			setup: function() {
				if (!g.data(this, B.datakey)) {
					var b = g.extend({
						related: 0
					}, B.defaults);
					g.data(this, B.datakey, b);
					h.add(this, w, B.init, b);
					this.attachEvent && this.attachEvent("ondragstart", B.dontstart)
				}
			},
			teardown: function() {
				(g.data(this, B.datakey) || {}).related || (g.removeData(this, B.datakey), h.remove(this, w, B.init), h.remove(this, "draginit", B.delegate), B.textselect(!0), this.detachEvent && this.detachEvent("ondragstart", B.dontstart))
			},
			init: function(e) {
				var r = e.data,
					x;
				if ((x = (x = e.originalEvent || e.sourceEvent) ? x.changedTouches || x.touches : []) && x.length) if (x.length > 1) return;
				else ia(e, r);
				else if (r.which > 0 && e.which != r.which) return;
				if (!g(e.target).is(r.not) && (!r.handle || g(e.target).closest(r.handle, e.currentTarget).length)) if (r.propagates = 1, r.interactions = [B.interaction(this, r)], r.target = e.target, r.pageX = e.pageX, r.pageY = e.pageY, r.dragging = null, x = B.hijack(e, "draginit", r), r.propagates) {
					if ((x = B.flatten(x)) && x.length) r.interactions = [], g.each(x, function() {
						r.interactions.push(B.interaction(this, r))
					});
					r.propagates = r.interactions.length;
					r.drop !== !1 && b.drop && b.drop.handler(e, r);
					B.textselect(!1);
					h.add(document, S, B.handler, r);
					if (!U) return !1
				}
			},
			interaction: function(b, r) {
				return {
					drag: b,
					callback: new B.callback,
					droppable: [],
					offset: g(b)[r.relative ? "position" : "offset"]() || {
						top: 0,
						left: 0
					}
				}
			},
			handler: function(e) {
				var r = e.data,
					g;
				if (!r.dragging && (e.type === "mousemove" || e.type === "touchmove")) {
					if (Math.pow(e.pageX - r.pageX, 2) + Math.pow(e.pageY - r.pageY, 2) < Math.pow(r.distance, 2)) return;
					e.target = r.target;
					B.hijack(e, "dragstart", r);
					if (r.propagates) r.dragging = !0
				}
				switch (e.type) {
				case "touchmove":
					g = e.originalEvent || e.sourceEvent, g = g.touches, r.dragging && (g && g.length > 1 || e.preventDefault(), ia(e, r));
				case "mousemove":
					if (r.dragging) {
						B.hijack(e, "drag", r);
						if (r.propagates) {
							r.drop !== !1 && b.drop && b.drop.handler(e, r);
							break
						}
						e.type = "mouseup"
					}
				case "touchend":
					r.dragging && ia(e, r);
				case "mouseup":
					if (h.remove(document, S, B.handler), r.dragging && (r.drop !== !1 && b.drop && b.drop.handler(e, r), B.hijack(e, "dragend", r)), B.textselect(!0), r.click === !1 && r.dragging) jQuery.event.triggered = !0, setTimeout(function() {
						jQuery.event.triggered = !1
					}, 20), r.dragging = !1
				}
			},
			delegate: function(b) {
				var r = [],
					x, w = g.data(this, "events") || {};
				g.each(w.live || [], function(w, s) {
					if (s.preType.indexOf("drag") === 0 && (x = g(b.target).closest(s.selector, b.currentTarget)[0])) h.add(x, s.origType + "." + B.livekey, s.origHandler, s.data), g.inArray(x, r) < 0 && r.push(x)
				});
				if (!r.length) return !1;
				return g(r).bind("dragend." + B.livekey, function() {
					h.remove(this, "." + B.livekey)
				})
			},
			hijack: function(b, r, x, h, w) {
				if (x) {
					var s = {
						event: b.originalEvent,
						type: b.type
					},
						ka = r.indexOf("drop") ? "drag" : "drop",
						G, V = h || 0,
						K, Y, h = !isNaN(h) ? h : x.interactions.length;
					b.type = r;
					b.sourceEvent = s.event;
					b.originalEvent = null;
					x.results = [];
					do
					if ((K = x.interactions[V]) && !(r !== "dragend" && K.cancelled)) {
						Y = B.properties(b, x, K);
						K.results = [];
						g(w || K[ka] || x.droppable).each(function(O, l) {
							G = (Y.target = l) ? m.call(l, b, Y) : null;
							if (G === !1) {
								if (ka == "drag") K.cancelled = !0, x.propagates -= 1;
								r == "drop" && (K[ka][O] = null)
							} else r == "dropinit" && K.droppable.push(B.element(G) || l);
							if (r == "dragstart") K.proxy = g(B.element(G) || K.drag)[0];
							K.results.push(G);
							delete b.result;
							if (r !== "dropinit") return G
						});
						x.results[V] = B.flatten(K.results);
						if (r == "dropinit") K.droppable = B.flatten(K.droppable);
						r == "dragstart" && !K.cancelled && Y.update()
					}
					while (++V < h);
					b.type = s.type;
					b.originalEvent = s.event;
					return B.flatten(x.results)
				}
			},
			properties: function(b, r, g) {
				var h = g.callback;
				h.drag = g.drag;
				h.proxy = g.proxy || g.drag;
				h.startX = r.pageX;
				h.startY = r.pageY;
				h.deltaX = b.pageX - r.pageX;
				h.deltaY = b.pageY - r.pageY;
				h.originalX = g.offset.left;
				h.originalY = g.offset.top;
				h.offsetX = b.pageX - (r.pageX - h.originalX);
				h.offsetY = b.pageY - (r.pageY - h.originalY);
				h.drop = B.flatten((g.drop || []).slice());
				h.available = B.flatten((g.droppable || []).slice());
				return h
			},
			element: function(b) {
				if (b && (b.jquery || b.nodeType == 1)) return b
			},
			flatten: function(b) {
				return g.map(b, function(b) {
					return b && b.jquery ? g.makeArray(b) : b && b.length ? B.flatten(b) : b
				})
			},
			textselect: function(b) {
				g(document)[b ? "unbind" : "bind"]("selectstart", B.dontstart).attr("unselectable", b ? "off" : "on").css("MozUserSelect", b ? "" : "none")
			},
			dontstart: function() {
				return !1
			},
			callback: function() {}
		};
	B.callback.prototype = {
		update: function() {
			b.drop && this.available.length && g.each(this.available, function(e) {
				b.drop.locate(this, e)
			})
		}
	};
	b.draginit = b.dragstart = b.dragend = B
})(jQuery);
(function(g) {
	function h(h) {
		var w = h || window.event,
			m = [].slice.call(arguments, 1),
			ia = 0,
			b = 0,
			B = 0,
			h = g.event.fix(w);
		h.type = "wheelchange";
		h.wheelDelta && (ia = h.wheelDelta / 120);
		h.detail && (ia = -h.detail / 3);
		B = ia;
		w.axis !== void 0 && w.axis === w.HORIZONTAL_AXIS && (B = 0, b = -1 * ia);
		w.wheelDeltaY !== void 0 && (B = w.wheelDeltaY / 120);
		w.wheelDeltaX !== void 0 && (b = -1 * w.wheelDeltaX / 120);
		m.unshift(h, ia, b, B);
		return g.event.handle.apply(this, m)
	}
	var m = ["DOMMouseScroll", "mousewheel"];
	g.event.special.wheelchange = {
		setup: function() {
			if (this.addEventListener) for (var g = m.length; g;) this.addEventListener(m[--g], h, !1);
			else this.onmousewheel = h
		},
		teardown: function() {
			if (this.removeEventListener) for (var g = m.length; g;) this.removeEventListener(m[--g], h, !1);
			else this.onmousewheel = null
		}
	};
	g.fn.extend({
		wheelchange: function(g) {
			return g ? this.bind("wheelchange", g) : this.trigger("wheelchange")
		},
		unwheelchange: function(g) {
			return this.unbind("wheelchange", g)
		}
	})
})(jQuery);
FusionCharts(["private", "modules.renderer.js-smartlabel", function() {
	var g = this.hcLib,
		h = g.isIE,
		m = g.hasSVG,
		U = Math.max,
		w = / HtmlUnit/.test(navigator.userAgent),
		S = document,
		ia = / AppleWebKit\//.test(navigator.userAgent),
		b = !! S.createElement("canvas").getContext,
		B = !(!b || !S.createElement("canvas").getContext("2d").measureText),
		e = function() {
			function e(b, j, i) {
				if (!b || !b.length) return 0;
				var c = i.getWidthFunction(),
					d = 0,
					a = 0,
					a = c(b),
					k = a / b.length,
					i = j,
					d = Math.ceil(j / k);
				if (a < j) return b.length - 1;
				if (d > b.length) i = j - a, d = b.length;
				for (; i > 0;) if (i = j - c(b.substr(0, d)), a = Math.floor(i / k)) d += a;
				else return d;
				for (; i < 0;) if (i = j - c(b.substr(0, d)), a = Math.floor(i / k)) d += a;
				else break;
				return d
			}
			function x(b, e) {
				e = e > 5 ? e : 5;
				this.maxContainers = e < 20 ? e : 20;
				this.last = this.first = null;
				this.containers = {};
				this.length = 0;
				this.rootNode = b;
				if (aa) {
					var i = document.createElementNS("http://www.w3.org/2000/svg", "svg");
					i.setAttributeNS("http://www.w3.org/2000/svg", "xlink", "http://www.w3.org/1999/xlink");
					i.setAttributeNS("http://www.w3.org/2000/svg", "height", "0");
					i.setAttributeNS("http://www.w3.org/2000/svg", "width", "0");
					this.svgRoot = i;
					this.rootNode.appendChild(i)
				}
			}
			function $(b, e, i) {
				if (!(typeof b === "undefined" || typeof b === "object")) {
					this.id = b;
					var c, d;
					typeof e === "string" && (e = S.getElementById(e));
					if (e && e.offsetWidth && e.offsetHeight) {
						if (e.appendChild) e.appendChild(c = document.createElement("div")), c.className = "_SmartLabel_Container", c.setAttribute("aria-hidden", "true"), c.setAttribute("role", "presentation")
					} else if ((b = document.getElementsByTagName("body")[0]) && b.appendChild) c = document.createElement("div"), c.className = "_SmartLabel_Container", c.setAttribute("aria-hidden", "true"), c.setAttribute("role", "presentation"), Z += 1, b.appendChild(c);
					c = this.parentContainer = c;
					c.innerHTML = F;
					if (w || !c.offsetHeight && !c.offsetWidth) aa = !0;
					c.innerHTML = "";
					for (d in ka) c.style[d] = ka[d];
					this.containerManager = new x(c, 10);
					this.showNoEllipses = !i;
					this.init = !0;
					this.style = {};
					this.setStyle()
				}
			}
			var fa = g.supportedStyle,
				s = {
					fontWeight: 1,
					"font-weight": 1,
					fontStyle: 1,
					"font-style": 1,
					fontSize: 1,
					"font-size": 1,
					fontFamily: 1,
					"font-family": 1
				},
				ka = {
					position: "absolute",
					top: "-9999em",
					whiteSpace: "nowrap",
					padding: "0px",
					width: "1px",
					height: "1px",
					overflow: "hidden"
				},
				G = ia ? 0 : 4.5,
				V = 0,
				K = /\b_SmartLabel\b/,
				Y = /\b_SmartLabelBR\b/,
				O = /(\<[^\<\>]+?\>)|(&(?:[a-z]+|#[0-9]+);|.)/ig,
				l = RegExp("\\<span[^\\>]+?_SmartLabel[^\\>]{0,}\\>(.*?)\\<\\/span\\>", "ig"),
				n = /<[^>][^<]*[^>]+>/i,
				F = "WgI",
				aa = !1,
				D = 0,
				M = 0,
				Z = 0,
				R, v, C;
			S.getElementsByClassName ? (R = "getElementsByClassName", v = "_SmartLabel", C = !0) : (R = "getElementsByTagName", v = "span", C = !1);
			x.prototype = {
				get: function(b) {
					var e = this.containers,
						i = this.length,
						c = this.maxContainers,
						d, a = "",
						k = "",
						k = this.getCanvasFont(b);
					for (d in fa) b[d] !== void 0 && (a += fa[d] + ":" + b[d] + ";");
					if (!a) return !1;
					if (e[a]) {
						if (a = e[a], this.first !== a) a.prev && (a.prev.next = a.next), a.next && (a.next.prev = a.prev), a.next = this.start, a.prev = null, this.last === a && (this.last = a.prev), this.start = a
					} else {
						if (i >= c) for (b = i - c + 1; b--;) this.removeContainer(this.last);
						a = this.addContainer(a, k)
					}
					return a
				},
				getCanvasFont: function(f) {
					var e, i = [];
					if (!b || !B) return !1;
					for (e in s) f[e] !== void 0 && i.push(f[e]);
					return i.join(" ")
				},
				setMax: function(b) {
					var e = this.length,
						b = b > 5 ? b : 5,
						b = b < 20 ? b : 20;
					if (b < e) {
						for (e -= b; e--;) this.removeContainer(this.last);
						this.length = b
					}
					this.maxContainers = b
				},
				addContainer: function(b, e) {
					var i, c;
					this.containers[b] = c = {
						next: null,
						prev: null,
						node: null,
						ellipsesWidth: 0,
						lineHeight: 0,
						dotWidth: 0,
						avgCharWidth: 4,
						keyStr: b,
						canvasStr: e,
						charCache: {}
					};
					c.next = this.start;
					c.next && (c.next.prev = c);
					this.start = c;
					this.length += 1;
					i = c.node = S.createElement("span");
					this.rootNode.appendChild(i);
					h && !m ? i.style.setAttribute("cssText", b) : i.setAttribute("style", b);
					i.setAttribute("aria-hidden", "true");
					i.setAttribute("role", "presentation");
					i.style.display = "inline-block";
					i.innerHTML = F;
					c.lineHeight = i.offsetHeight;
					c.avgCharWidth = i.offsetWidth / 3;
					aa ? (i = c.svgText = S.createElementNS("http://www.w3.org/2000/svg", "text"), i.setAttribute("style", b), this.svgRoot.appendChild(i), i.textContent = F, c.lineHeight = i.getBBox().height, c.avgCharWidth = (i.getBBox().width - G) / 3, i.textContent = "...", c.ellipsesWidth = i.getBBox().width - G, i.textContent = ".", c.dotWidth = i.getBBox().width - G) : e ? (i = c.canvas = S.createElement("canvas"), i.style.height = i.style.width = "0px", this.rootNode.appendChild(i), c.context = i = i.getContext("2d"), i.font = e, c.ellipsesWidth = i.measureText("...").width, c.dotWidth = i.measureText(".").width) : (i.innerHTML = "...", c.ellipsesWidth = i.offsetWidth, i.innerHTML = ".", c.dotWidth = i.offsetWidth, i.innerHTML = "");
					return c
				},
				removeContainer: function(b) {
					var e = b.keyStr;
					if (e && this.length && b) this.length -= 1, b.prev && (b.prev.next = b.next), b.next && (b.next.prev = b.prev), this.first === b && (this.first = b.next), this.last === b && (this.last = b.prev), delete this.containers[e], delete b
				}
			};
			x.prototype.constructor = x;
			$.prototype = {
				dispose: function() {
					var b = this.container,
						e;
					if (this.init) {
						if (b && (e = b.parentNode)) e.removeChild(b), delete this.container;
						delete this.id;
						delete this.style;
						delete this.parentContainer;
						delete this.showNoEllipses
					}
				},
				useEllipsesOnOverflow: function(b) {
					if (this.init) this.showNoEllipses = !b
				},
				getWidthFunction: function() {
					var b = this.context,
						e = this.container,
						i = this.containerObj.svgText;
					return i ?
					function(b) {
						var f;
						i.textContent = b;
						b = i.getBBox();
						f = b.width - G;
						if (f < 1) f = b.width;
						return f
					} : b ?
					function(c) {
						return b.measureText(c).width
					} : function(b) {
						e.innerHTML = b;
						return e.offsetWidth
					}
				},
				getSmartText: function(b, j, i, c) {
					if (!this.init) return !1;
					if (b === void 0 || b === null) b = "";
					var d = {
						text: b,
						maxWidth: j,
						maxHeight: i,
						width: null,
						height: null,
						oriTextWidth: null,
						oriTextHeight: null,
						oriText: b,
						isTruncated: !1
					},
						a = !1,
						k, q = 0,
						u, N, ca = -1,
						F = -1,
						g = -1;
					k = this.container;
					var h = this.context,
						x = 0,
						w = 0,
						s, B, G = [],
						m = 0,
						ka = this.showNoEllipses ? "" : "...",
						ca = this.lineHeight,
						fa = function(a) {
							for (var a = a.replace(/^\s\s*/, ""), b = /\s/, c = a.length; b.test(a.charAt(c -= 1)););
							return a.slice(0, c + 1)
						};
					B = this.getWidthFunction();
					if (k) {
						if (!aa) {
							k.innerHTML = b;
							d.oriTextWidth = N = k.offsetWidth;
							d.oriTextHeight = a = k.offsetHeight;
							if (a <= i && N <= j) return d.width = d.oriTextWidth = N, d.height = d.oriTextHeight = a, d;
							if (ca > i) return d.text = "", d.width = d.oriTextWidth = 0, d.height = d.oriTextHeight = 0, d
						}
						b = fa(b).replace(/(\s+)/g, " ");
						a = n.test(b);
						N = this.showNoEllipses ? j : j - V;
						if (a) {
							q = b.replace(O, "$2");
							b = b.replace(O, '$1<span class="_SmartLabel">$2</span>');
							b = b.replace(/(\<br\s*\/*\>)/g, "<span class='_SmartLabel _SmartLabelBR'>$1</span>");
							k.innerHTML = b;
							m = k[R](v);
							h = [];
							F = ca = -1;
							a = 0;
							for (B = m.length; a < B; a += 1) if (b = m[a], C || K.test(b.className)) if (fa = b.innerHTML, fa != "") {
								if (fa == " ") F = h.length;
								else if (fa == "-") ca = h.length;
								h.push({
									spaceIdx: F,
									dashIdx: ca,
									elem: b
								});
								G.push(fa)
							}
							delete m;
							m = 0;
							a = h.length;
							D = h[0].elem.offsetWidth;
							if (D > j) return d.text = "", d.width = d.oriTextWidth = d.height = d.oriTextHeight = 0, d;
							else D > N && !this.showNoEllipses && (N = j - 2 * M, N > D ? ka = ".." : (N = j - M, N > D ? ka = "." : (N = 0, ka = "")));
							if (c) for (; m < a; m += 1) b = h[m].elem, c = b.offsetLeft + b.offsetWidth, c > N && (s || (s = m), k.offsetWidth > j && (u = m, m = a));
							else for (; m < a; m += 1) if (b = h[m].elem, B = b.offsetHeight + b.offsetTop, c = b.offsetLeft + b.offsetWidth, G = null, c > N) {
								if (s || (s = m), c > j) F = h[m].spaceIdx, ca = h[m].dashIdx, F > g ? (h[F].elem.innerHTML = "<br/>", g = F) : ca > g ? (h[ca].elem.innerHTML = ca === m ? "<br/>-" : "-<br/>", g = ca) : b.parentNode.insertBefore(G = document.createElement("br"), b), b.offsetHeight + b.offsetTop > i ? (G ? G.parentNode.removeChild(G) : g === ca ? h[ca].elem.innerHTML = "-" : h[F].elem.innerHTML = " ", u = m, m = a) : s = null
							} else B > i && (u = m, m = a);
							if (u < a) {
								d.isTruncated = !0;
								s = s ? s : u;
								for (m = a - 1; m >= s; m -= 1) b = h[m].elem, b.parentNode.removeChild(b);
								for (; m >= 0; m -= 1) b = h[m].elem, Y.test(b.className) ? b.parentNode.removeChild(b) : m = 0
							}
							d.text = k.innerHTML.replace(l, "$1");
							if (d.isTruncated) d.text += ka, d.tooltext = q
						} else {
							G = b.split("");
							a = G.length;
							k = "";
							u = [];
							s = G[0];
							this.cache[s] ? D = this.cache[s].width : (D = B(s), this.cache[s] = {
								width: D
							});
							if (N > D) u = b.substr(0, e(b, N, this)).split(""), m = u.length;
							else if (D > j) return d.text = "", d.width = d.oriTextWidth = d.height = d.oriTextHeight = 0, d;
							else ka && (N = j - 2 * M, N > D ? ka = ".." : (N = j - M, N > D ? ka = "." : (N = 0, ka = "")));
							x = B(u.join(""));
							w = this.lineHeight;
							if (c) {
								for (; m < a; m += 1) if (s = u[m] = G[m], this.cache[s] ? D = this.cache[s].width : (D = B(s), this.cache[s] = {
									width: D
								}), x += D, x > N && (k || (k = u.slice(0, -1).join("")), x > j)) return d.text = fa(k) + ka, d.tooltext = d.oriText, d.width = B(d.text), d.height = this.lineHeight, d;
								d.text = u.join("");
								d.width = x;
								d.height = this.lineHeight
							} else {
								for (; m < a; m += 1) if (s = u[m] = G[m], s === " " && !h && (s = "&nbsp;"), this.cache[s] ? D = this.cache[s].width : (D = B(s), this.cache[s] = {
									width: D
								}), x += D, x > N && (k || (k = u.slice(0, -1).join("")), x > j)) if (F = b.substr(0, u.length).lastIndexOf(" "), ca = b.substr(0, u.length).lastIndexOf("-"), F > g ? (x = B(u.slice(g + 1, F).join("")), u.splice(F, 1, "<br/>"), g = F, c = F + 1) : ca > g ? (ca === u.length - 1 ? (x = B(u.slice(g + 1, F).join("")), u.splice(ca, 1, "<br/>-")) : (x = B(u.slice(g + 1, F).join("")), u.splice(ca, 1, "-<br/>")), g = ca, c = ca + 1) : (u.splice(u.length - 1, 1, "<br/>" + G[m]), c = m), w += this.lineHeight, w > i) return d.text = fa(k) + ka, d.tooltext = d.oriText, d.width = q, d.height = w - this.lineHeight, d;
								else q = U(q, x), k = null, s = e(b.substr(c), N, this), x = B(b.substr(c, s || 1)), u.length < c + s && (u = u.concat(b.substr(u.length, c + s - u.length).split("")), m = u.length - 1);
								q = U(q, x);
								d.text = u.join("");
								d.width = q;
								d.height = w
							}
							return d
						}
						d.height = k.offsetHeight;
						d.width = k.offsetWidth
					} else d.error = Error("Body Tag Missing!");
					return d
				},
				setStyle: function(b) {
					if (!this.init) return !1;
					if (b !== this.style || this.styleNotSet) {
						if (!b) b = this.style;
						this.style = b;
						(this.containerObj = b = this.containerManager.get(b)) ? (this.container = b.node, this.context = b.context, this.cache = b.charCache, this.lineHeight = b.lineHeight, V = b.ellipsesWidth, M = b.dotWidth, this.styleNotSet = !1) : this.styleNotSet = !0
					}
				},
				getTextSize: function(b, e, i) {
					if (!this.init) return !1;
					var c = {
						text: b,
						width: null,
						height: null,
						oriTextWidth: null,
						oriTextHeight: null,
						isTruncated: !1
					},
						d = this.container;
					if (d && (d.innerHTML = b, c.oriTextWidth = d.offsetWidth, c.oriTextHeight = d.offsetHeight, c.width = Math.min(c.oriTextWidth, e), c.height = Math.min(c.oriTextHeight, i), c.width < c.oriTextWidth || c.height < c.oriTextHeight)) c.isTruncated = !0;
					return c
				},
				getOriSize: function(b) {
					if (!this.init) return !1;
					var e = {
						text: b,
						width: null,
						height: null
					},
						i = this.container,
						c = this.getWidthFunction(),
						d = 0;
					if (aa) {
						b = b.split(/(\<br\s*\/*\>)/g);
						i = b.length;
						for (e.height = this.lineHeight * i; i--;) d = U(d, c(b[i]));
						e.width = d
					} else if (i) i.innerHTML = b, e.width = i.offsetWidth, e.height = i.offsetHeight;
					return e
				}
			};
			return $.prototype.constructor = $
		}();
	g.SmartLabelManager = e
}]);
FusionCharts(["private", "modules.renderer.js-numberformatter", function() {
	var g = this.hcLib,
		h = g.pluckNumber,
		m = g.extend2,
		U = g.getValidValue,
		h = g.pluckNumber,
		w = g.pluck,
		S = g.getFirstValue,
		ia = Math.abs,
		b = Math.pow,
		B = Math.round,
		e = "",
		r = "0",
		x = ".",
		$ = "-",
		fa = function(b) {
			return b && b.replace(/[-[\]{}()*+?.,\\^$|#\s]/g, "\\$&")
		};
	g.NumberFormatter = function() {
		function g(O, l, n) {
			var F;
			if (l <= 0) return B(O) + e;
			if (isNaN(l)) return O += e, O.length > 12 && O.indexOf(x) != -1 && (l = 12 - O.split(x)[0].length, F = b(10, l), O = B(O * F) / F + e), O;
			F = b(10, l);
			O = B(O * F) / F + e;
			if (n == 1) {
				O.indexOf(x) == -1 && (O += ".0");
				n = O.split(x);
				l -= n[1].length;
				for (n = 1; n <= l; n++) O += r
			}
			return O
		}
		function ka(b, l, n, F) {
			var g = Number(b);
			if (isNaN(g)) return e;
			var h = e,
				r = !1,
				s = e,
				m = e,
				v = s = 0,
				s = 0,
				v = b.length;
			b.indexOf(x) != -1 && (h = b.substring(b.indexOf(x) + 1, b.length), v = b.indexOf(x));
			g < 0 && (r = !0, s = 1);
			s = b.substring(s, v);
			b = s.length;
			g = F.length - 1;
			v = F[g];
			if (b < v) m = s;
			else for (; b >= v;) m = (b - v ? n : e) + s.substr(b - v, v) + m, b -= v, v = (g -= 1) <= 0 ? F[0] : F[g], b < v && (m = s.substring(b, 0) + m);
			h != e && (m = m + l + h);
			r == !0 && (m = $ + m);
			return m
		}
		var G = {
			formatnumber: "1",
			formatnumberscale: "1",
			defaultnumberscale: e,
			numberscaleunit: ["K", "M"],
			numberscalevalue: [1E3, 1E3],
			numberprefix: e,
			numbersuffix: e,
			decimals: e,
			forcedecimals: r,
			yaxisvaluedecimals: "2",
			decimalseparator: x,
			thousandseparator: ",",
			thousandseparatorposition: [3],
			indecimalseparator: e,
			inthousandseparator: e,
			sformatnumber: "1",
			sformatnumberscale: r,
			sdefaultnumberscale: e,
			snumberscaleunit: ["K", "M"],
			snumberscalevalue: [1E3, 1E3],
			snumberprefix: e,
			snumbersuffix: e,
			sdecimals: "2",
			sforcedecimals: r,
			syaxisvaluedecimals: "2",
			xFormatNumber: r,
			xFormatNumberScale: r,
			xDefaultNumberScale: e,
			xNumberScaleUnit: ["K", "M"],
			xNumberScaleValue: [1E3, 1E3],
			xNumberPrefix: e,
			xNumberSuffix: e
		},
			V = {
				mscombidy2d: {
					formatnumberscale: "1"
				}
			},
			K = function(b, e, n) {
				var F, g, r, s, x, B, v, C, f, j = e.name,
					i = m({}, G),
					c, d;
				(r = V[j]) && (i = m(i, r));
				this.csConf = i;
				this.chartAPI = e;
				U(b.numberscaleunit) && (F = b.numberscaleunit.split(","));
				if (g = U(b.snumberscaleunit, b.numberscaleunit)) g = g.split(",");
				if (r = U(b.xnumberscaleunit, b.numberscaleunit)) r = r.split(",");
				if (s = U(b.ticknumberscaleunit, b.numberscaleunit)) s = s.split(",");
				if (x = U(b.ynumberscaleunit, b.numberscaleunit)) x = x.split(",");
				U(b.numberscalevalue) && (B = b.numberscalevalue.split(","));
				if (d = U(b.snumberscalevalue, b.numberscalevalue)) d = d.split(",");
				if (v = U(b.xnumberscalevalue, b.numberscalevalue)) v = v.split(",");
				if (C = U(b.ticknumberscalevalue, b.numberscalevalue)) C = C.split(",");
				if (f = U(b.ynumberscalevalue, b.numberscalevalue)) f = f.split(",");
				if (U(b.thousandseparatorposition)) {
					c = b.thousandseparatorposition.split(",");
					for (var a = c.length, k, q = G.thousandseparatorposition[0]; a--;) k = parseInt(c[a], 10), k > 0 || (k = q), q = c[a] = k
				}
				e || (e = {});
				a = h(b.scalerecursively, 0);
				k = h(b.sscalerecursively, a);
				var q = h(b.xscalerecursively, a),
					u = h(b.maxscalerecursion, -1),
					N = h(b.smaxscalerecursion, u),
					ca = h(b.xmaxscalerecursion, u),
					J = U(b.scaleseparator, " "),
					Ja = U(b.sscaleseparator, J),
					ka = U(b.xscaleseparator, J);
				u || (u = -1);
				this.baseConf = F = {
					cacheStore: [],
					formatnumber: w(b.formatnumber, e.formatnumber, i.formatnumber),
					formatnumberscale: w(b.formatnumberscale, e.formatnumberscale, i.formatnumberscale),
					defaultnumberscale: S(b.defaultnumberscale, e.defaultnumberscale, i.defaultnumberscale),
					numberscaleunit: w(F, e.numberscaleunit, i.numberscaleunit).concat(),
					numberscalevalue: w(B, e.numberscalevalue, i.numberscalevalue).concat(),
					numberprefix: S(b.numberprefix, e.numberprefix, i.numberprefix),
					numbersuffix: S(b.numbersuffix, e.numbersuffix, i.numbersuffix),
					decimalprecision: parseInt(b.decimals === "auto" ? i.decimalprecision : w(b.decimals, b.decimalprecision, e.decimals, i.decimals, e.decimalprecision, i.decimalprecision), 10),
					forcedecimals: w(b.forcedecimals, e.forcedecimals, i.forcedecimals),
					decimalseparator: w(b.decimalseparator, e.decimalseparator, i.decimalseparator),
					thousandseparator: w(b.thousandseparator, e.thousandseparator, i.thousandseparator),
					thousandseparatorposition: w(c, e.thousandseparatorposition, i.thousandseparatorposition),
					indecimalseparator: S(b.indecimalseparator, e.indecimalseparator, i.indecimalseparator),
					inthousandseparator: S(b.inthousandseparator, e.inthousandseparator, i.inthousandseparator),
					scalerecursively: a,
					maxscalerecursion: u,
					scaleseparator: J
				};
				if (U(F.inthousandseparator)) this.baseConf._REGinthousandseparator = RegExp(fa(F.inthousandseparator), "g");
				if (U(F.indecimalseparator)) this.baseConf._REGindecimalseparator = RegExp(fa(F.indecimalseparator));
				this.Y = [];
				if (!n) {
					n = {
						cacheStore: [],
						formatnumber: F.formatnumber,
						formatnumberscale: F.formatnumberscale,
						defaultnumberscale: F.defaultnumberscale,
						numberscaleunit: F.numberscaleunit.concat(),
						numberscalevalue: F.numberscalevalue.concat(),
						numberprefix: F.numberprefix,
						numbersuffix: F.numbersuffix,
						decimalprecision: F.decimalprecision,
						forcedecimals: F.forcedecimals,
						decimalseparator: F.decimalseparator,
						thousandseparator: F.thousandseparator,
						thousandseparatorposition: F.thousandseparatorposition,
						indecimalseparator: F.indecimalseparator,
						inthousandseparator: F.inthousandseparator,
						scalerecursively: a,
						maxscalerecursion: u,
						scaleseparator: J
					};
					if (!e.useScaleRecursively || (n.numberscalevalue && n.numberscalevalue.length) != (n.numberscaleunit && n.numberscaleunit.length)) n.scalerecursively = a = 0;
					B = {
						cacheStore: [],
						formatnumber: n.formatnumber,
						formatnumberscale: n.formatnumberscale,
						defaultnumberscale: n.defaultnumberscale,
						numberscaleunit: n.numberscaleunit.concat(),
						numberscalevalue: n.numberscalevalue.concat(),
						numberprefix: n.numberprefix,
						numbersuffix: n.numbersuffix,
						decimalprecision: parseInt(w(b.yaxisvaluedecimals, n.decimalprecision, 2)),
						forcedecimals: w(b.forceyaxisvaluedecimals, n.forcedecimals),
						decimalseparator: n.decimalseparator,
						thousandseparator: n.thousandseparator,
						thousandseparatorposition: n.thousandseparatorposition.concat(),
						indecimalseparator: n.indecimalseparator,
						inthousandseparator: n.inthousandseparator,
						scalerecursively: a,
						maxscalerecursion: u,
						scaleseparator: J
					};
					d = {
						cacheStore: [],
						formatnumber: w(b.sformatnumber, e.sformatnumber, G.sformatnumber),
						formatnumberscale: w(b.sformatnumberscale, e.sformatnumberscale, G.sformatnumberscale),
						defaultnumberscale: S(b.sdefaultnumberscale, e.sdefaultnumberscale, n.defaultnumberscale),
						numberscaleunit: w(g, e.snumberscaleunit, G.snumberscaleunit).concat(),
						numberscalevalue: w(d, e.snumberscalevalue, G.snumberscalevalue).concat(),
						numberprefix: S(b.snumberprefix, e.snumberprefix, G.snumberprefix),
						numbersuffix: S(b.snumbersuffix, e.snumbersuffix, G.snumbersuffix),
						decimalprecision: parseInt(w(b.syaxisvaluedecimals, b.sdecimals, b.decimals, e.sdecimals, G.sdecimals), 10),
						forcedecimals: w(b.forcesyaxisvaluedecimals, b.sforcedecimals, b.forcedecimals, e.sforcedecimals, G.sforcedecimals),
						decimalseparator: w(b.decimalseparator, e.decimalseparator, G.decimalseparator),
						thousandseparator: w(b.thousandseparator, e.thousandseparator, G.thousandseparator),
						thousandseparatorposition: n.thousandseparatorposition.concat(),
						indecimalseparator: w(b.indecimalseparator, e.indecimalseparator, G.indecimalseparator),
						inthousandseparator: w(b.inthousandseparator, e.inthousandseparator, G.inthousandseparator),
						scalerecursively: k,
						maxscalerecursion: N,
						scaleseparator: Ja
					};
					g = m({}, d);
					g.decimalprecision = parseInt(w(b.sdecimals, b.decimals, b.syaxisvaluedecimals, e.sdecimals, G.sdecimals), 10);
					g.forcedecimals = w(b.sforcedecimals, b.forcedecimals, b.forcesyaxisvaluedecimals, e.sforcedecimals, G.sforcedecimals);
					g.cacheStore = [];
					if (!e.useScaleRecursively || (d.numberscalevalue && d.numberscalevalue.length) != (d.numberscaleunit && d.numberscaleunit.length)) d.scalerecursively = k = 0;
					if (/^(bubble|scatter|selectscatter)$/.test(j)) B.formatnumber = w(b.yformatnumber, B.formatnumber), B.formatnumberscale = w(b.yformatnumberscale, B.formatnumberscale), B.defaultnumberscale = S(b.ydefaultnumberscale, B.defaultnumberscale), B.numberscaleunit = w(x, B.numberscaleunit), B.numberscalevalue = w(f, B.numberscalevalue), B.numberprefix = w(b.ynumberprefix, B.numberprefix), B.numbersuffix = w(b.ynumbersuffix, B.numbersuffix), n.formatnumber = w(b.yformatnumber, n.formatnumber), n.formatnumberscale = w(b.yformatnumberscale, n.formatnumberscale), n.defaultnumberscale = S(b.ydefaultnumberscale, n.defaultnumberscale), n.numberscaleunit = w(b.ynumberscaleunit, n.numberscaleunit.concat()), n.numberscalevalue = w(b.ynumberscalevalue, n.numberscalevalue.concat()), n.numberprefix = w(b.ynumberprefix, n.numberprefix), n.numbersuffix = w(b.ynumbersuffix, n.numbersuffix);
					if (/^(mscombidy2d|mscombidy3d)$/.test(j)) d.formatnumberscale = h(b.sformatnumberscale, "1");
					if (/^(pie2d|pie3d|doughnut2d|doughnut3d|marimekko|pareto2d|pareto3d)$/.test(j)) n.decimalprecision = w(b.decimals, "2");
					a && (n.numberscalevalue.push(1), n.numberscaleunit.unshift(n.defaultnumberscale), B.numberscalevalue.push(1), B.numberscaleunit.unshift(B.defaultnumberscale));
					k && (d.numberscalevalue.push(1), d.numberscaleunit.unshift(d.defaultnumberscale), g.numberscalevalue.push(1), g.numberscaleunit.unshift(g.defaultnumberscale));
					this.Y[0] = {
						yAxisLabelConf: B,
						dataLabelConf: n
					};
					this.Y[1] = {
						yAxisLabelConf: d,
						dataLabelConf: g
					};
					this.paramLabels = n;
					this.param1 = B;
					this.param2 = d;
					this.paramLabels2 = g
				}
				this.paramX = {
					cacheStore: [],
					formatnumber: w(b.xformatnumber, F.formatnumber),
					formatnumberscale: w(b.xformatnumberscale, F.formatnumberscale),
					defaultnumberscale: S(b.xdefaultnumberscale, F.defaultnumberscale),
					numberscaleunit: w(r, F.numberscaleunit.concat()),
					numberscalevalue: w(v, F.numberscalevalue.concat()),
					numberprefix: w(b.xnumberprefix, F.numberprefix),
					numbersuffix: w(b.xnumbersuffix, F.numbersuffix),
					decimalprecision: parseInt(w(b.xaxisvaluedecimals, b.xaxisvaluesdecimals, F.decimalprecision, 2), 10),
					forcedecimals: w(b.forcexaxisvaluedecimals, 0),
					decimalseparator: F.decimalseparator,
					thousandseparator: F.thousandseparator,
					thousandseparatorposition: F.thousandseparatorposition.concat(),
					indecimalseparator: F.indecimalseparator,
					inthousandseparator: F.inthousandseparator,
					scalerecursively: q,
					maxscalerecursion: ca,
					scaleseparator: ka
				};
				if (!e.useScaleRecursively || (this.paramX.numberscalevalue && this.paramX.numberscalevalue.length) != (this.paramX.numberscaleunit && this.paramX.numberscaleunit.length)) this.paramX.scalerecursively = q = 0;
				q && (this.paramX.numberscalevalue.push(1), this.paramX.numberscaleunit.unshift(this.paramX.defaultnumberscale));
				this.paramScale = {
					cacheStore: [],
					formatnumber: w(b.tickformatnumber, F.formatnumber),
					formatnumberscale: w(b.tickformatnumberscale, F.formatnumberscale),
					defaultnumberscale: S(b.tickdefaultnumberscale, F.defaultnumberscale),
					numberscaleunit: w(s, F.numberscaleunit.concat()),
					numberscalevalue: w(C, F.numberscalevalue.concat()),
					numberprefix: w(b.ticknumberprefix, F.numberprefix),
					numbersuffix: w(b.ticknumbersuffix, F.numbersuffix),
					decimalprecision: parseInt(w(b.tickvaluedecimals, F.decimalprecision, "2")),
					forcedecimals: w(b.forcetickvaluedecimals, F.forcedecimals, 0),
					decimalseparator: F.decimalseparator,
					thousandseparator: F.thousandseparator,
					thousandseparatorposition: F.thousandseparatorposition.concat(),
					indecimalseparator: F.indecimalseparator,
					inthousandseparator: F.inthousandseparator,
					scalerecursively: a,
					maxscalerecursion: u,
					scaleseparator: J
				};
				a && (this.paramScale.numberscalevalue.push(1), this.paramScale.numberscaleunit.unshift(this.paramScale.defaultnumberscale));
				this.timeConf = {
					inputDateFormat: w(b.inputdateformat, b.dateformat, "mm/dd/yyyy"),
					outputDateFormat: w(b.outputdateformat, b.inputdateformat, b.dateformat, "mm/dd/yyyy"),
					days: ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"],
					months: ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"],
					daySuffix: ["", "st", "nd", "rd", "th", "th", "th", "th", "th", "th", "th", "th", "th", "th", "th", "th", "th", "th", "th", "th", "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th", "th", "st"]
				}
			};
		K.prototype = {
			cleaneValueCacheStore: {},
			percentStrCacheStore: {},
			dispose: function() {
				this.Y && delete this.Y;
				this.cleaneValueCacheStore && delete this.cleaneValueCacheStore;
				this.percentStrCacheStore && delete this.percentStrCacheStore;
				this.paramLabels && delete this.paramLabels;
				this.param1 && delete this.param1;
				this.param2 && delete this.param2;
				this.paramLabels2 && delete this.paramLabels2;
				this.csConf && delete this.csConf;
				this.chartAPI && delete this.chartAPI;
				this.baseConf && delete this.baseConf;
				this.timeConf && delete this.timeConf;
				this.paramX && delete this.paramX;
				this.paramScale && delete this.paramScale
			},
			parseMLAxisConf: function(b, e) {
				var n = this.baseConf,
					g = this.csConf,
					r = this.chartAPI,
					s = h(b.scalerecursively, n.scalerecursively),
					x = h(b.maxscalerecursion, n.maxscalerecursion),
					m = U(b.scaleseparator, n.scaleseparator),
					B, v, C, f, j, i, e = h(e, this.Y.length);
				U(b.numberscaleunit) && (B = b.numberscaleunit.split(","));
				U(b.numberscalevalue) && (v = b.numberscalevalue.split(","));
				x || (x = -1);
				if (U(b.thousandseparatorposition)) {
					C = b.thousandseparatorposition.split(",");
					f = C.length;
					for (i = G.thousandseparatorposition[0]; f--;)(j = h(ia(C[f]))) ? i = j : j = i, C[f] = j
				}
				n = {
					cacheStore: [],
					formatnumber: w(b.formatnumber, n.formatnumber),
					formatnumberscale: w(b.formatnumberscale, n.formatnumberscale),
					defaultnumberscale: S(b.defaultnumberscale, n.defaultnumberscale),
					numberscaleunit: w(B, n.numberscaleunit).concat(),
					numberscalevalue: w(v, n.numberscalevalue).concat(),
					numberprefix: S(b.numberprefix, n.numberprefix),
					numbersuffix: S(b.numbersuffix, n.numbersuffix),
					forcedecimals: w(b.forcedecimals, n.forcedecimals),
					decimalprecision: parseInt(b.decimals === "auto" ? g.decimalprecision : w(b.decimals, n.decimalprecision), 10),
					decimalseparator: w(b.decimalseparator, n.decimalseparator),
					thousandseparator: w(b.thousandseparator, n.thousandseparator),
					thousandseparatorposition: w(C, n.thousandseparatorposition),
					indecimalseparator: S(b.indecimalseparator, n.indecimalseparator),
					inthousandseparator: S(b.inthousandseparator, n.inthousandseparator),
					scalerecursively: s,
					maxscalerecursion: x,
					scaleseparator: m
				};
				if (!r.useScaleRecursively || (n.numberscalevalue && n.numberscalevalue.length) != (n.numberscaleunit && n.numberscaleunit.length)) n.scalerecursively = s = 0;
				r = {
					cacheStore: [],
					formatnumber: n.formatnumber,
					formatnumberscale: n.formatnumberscale,
					defaultnumberscale: n.defaultnumberscale,
					numberscaleunit: n.numberscaleunit.concat(),
					numberscalevalue: n.numberscalevalue.concat(),
					numberprefix: n.numberprefix,
					numbersuffix: n.numbersuffix,
					decimalprecision: parseInt(w(b.yaxisvaluedecimals, n.decimalprecision, 2)),
					forcedecimals: w(b.forceyaxisvaluedecimals, n.forcedecimals),
					decimalseparator: n.decimalseparator,
					thousandseparator: n.thousandseparator,
					thousandseparatorposition: n.thousandseparatorposition.concat(),
					indecimalseparator: n.indecimalseparator,
					inthousandseparator: n.inthousandseparator,
					scalerecursively: s,
					maxscalerecursion: x,
					scaleseparator: m
				};
				s && (n.numberscalevalue.push(1), n.numberscaleunit.unshift(n.defaultnumberscale), r.numberscalevalue.push(1), r.numberscaleunit.unshift(r.defaultnumberscale));
				this.Y[e] = {
					dataLabelConf: n,
					yAxisLabelConf: r
				}
			},
			percentValue: function(b) {
				var e = this.percentStrCacheStore[b];
				e === void 0 && (e = isNaN(this.paramLabels.decimalprecision) ? "2" : this.paramLabels.decimalprecision, e = this.percentStrCacheStore[b] = ka(g(b, e, this.paramLabels.forcedecimals), this.paramLabels.decimalseparator, this.paramLabels.thousandseparator, this.paramLabels.thousandseparatorposition) + "%");
				return e
			},
			getCleanValue: function(b, l) {
				var n = this.cleaneValueCacheStore[b];
				if (n === void 0) {
					var n = b,
						g = this.baseConf;
					n += e;
					g._REGinthousandseparator && (n = n.replace(g._REGinthousandseparator, e));
					g._REGindecimalseparator && (n = n.replace(g._REGindecimalseparator, x));
					n = parseFloat(n);
					n = isFinite(n) ? n : NaN;
					this.cleaneValueCacheStore[b] = n = isNaN(n) ? null : l ? ia(n) : n
				}
				return n
			},
			dataLabels: function(b, e) {
				var n = this.Y[e] || (e ? this.Y[1] : this.Y[0]),
					g, n = n && n.dataLabelConf || this.baseConf;
				g = n.cacheStore[b];
				g === void 0 && (g = n.cacheStore[b] = Y(b, n));
				return g
			},
			yAxis: function(b, e) {
				var n = this.Y[e] || (e ? this.Y[1] : this.Y[0]),
					g, n = n && n.yAxisLabelConf || this.baseConf;
				g = n.cacheStore[b];
				g === void 0 && (g = n.cacheStore[b] = Y(b, n));
				return g
			},
			xAxis: function(b) {
				var e = this.paramX.cacheStore[b];
				e === void 0 && (e = this.paramX.cacheStore[b] = Y(b, this.paramX));
				return e
			},
			sYAxis: function(b) {
				var e = this.Y[1],
					n, e = e && e.yAxisLabelConf || this.baseConf;
				n = e.cacheStore[b];
				n === void 0 && (n = e.cacheStore[b] = Y(b, e));
				return n
			},
			scale: function(b) {
				var e = this.paramScale.cacheStore[b];
				e === void 0 && (e = this.paramScale.cacheStore[b] = Y(b, this.paramScale));
				return e
			},
			getCleanTime: function(b) {
				var e;
				this.timeConf.inputDateFormat && Date.parseExact && (e = Date.parseExact(b, this.timeConf.inputDateFormat));
				return e && e.getTime()
			},
			getDateValue: function(b) {
				var e, n, g, b = /^dd/.test(this.timeConf.inputDateFormat) && b && b.replace(/(\d{1,2})\/(\d{1,2})\/(\d{2,4})/, "$2/$1/$3") || b;
				e = new Date(b);
				n = e.getTime();
				!n && b && /\:/.test(b) && (b = b.split(":"), n = h(b[0], 0), g = h(b[1], 0), b = h(b[2], 0), n = n > 23 ? n === 24 && g === 0 && b === 0 ? n : 23 : n, g = g > 59 ? 59 : g, b = b > 59 ? 59 : b, e = new Date, e.setHours(n), e.setMinutes(g), e.setSeconds(b), n = e.getTime());
				return {
					ms: n,
					date: e
				}
			},
			getFormatedDate: function(b, g) {
				var n = typeof b === "object" && b || new Date(b),
					h = this.timeConf,
					s = w(g, h.outputDateFormat),
					x = n.getFullYear(),
					m = n.getMonth(),
					B = n.getDate(),
					G = n.getDay(),
					v = n.getMinutes(),
					C = n.getSeconds(),
					n = n.getHours(),
					v = v > 9 ? e + v : r + v,
					C = C > 9 ? e + C : r + C,
					n = n > 9 ? e + n : r + n;
				s.match(/dnl/) && (s = s.replace(/dnl/ig, h.days[G]));
				s.match(/dns/) && (s = s.replace(/dns/ig, h.days[G] && h.days[G].substr(0, 3)));
				s.match(/dd/) && (s = s.replace(/dd/ig, B));
				s.match(/mnl/) && (s = s.replace(/mnl/ig, h.months[m]));
				s.match(/mns/) && (s = s.replace(/mns/ig, h.months[m] && h.months[m].substr(0, 3)));
				s.match(/mm/) && (s = s.replace(/mm/ig, m + 1));
				s.match(/yyyy/) && (s = s.replace(/yyyy/ig, x));
				s.match(/yy/) && (s = s.replace(/yy/ig, (x % 1E3 % 100 + "").replace(/^(\d)$/, "0$1")));
				s.match(/hh12/) && (s = s.replace(/hh12/ig, n % 12 || 12));
				s.match(/hh/) && (s = s.replace(/hh/ig, n));
				s.match(/mn/) && (s = s.replace(/mn/ig, v));
				s.match(/ss/) && (s = s.replace(/ss/ig, C));
				s.match(/ampm/) && (s = s.replace(/ampm/ig, n < 12 ? "AM" : "PM"));
				s.match(/ds/) && (s = s.replace(/ds/ig, h.daySuffix[B]));
				return s
			}
		};
		K.prototype.constructor = K;
		var Y = function(b, l) {
				if (b !== null) {
					var b = Number(b),
						n = b + e,
						r;
					r = l.formatnumberscale == 1 ? l.defaultnumberscale : e;
					var m;
					m = (m = n.split(x)[1]) ? m.length : l.forcedecimals ? "2" : e;
					if (l.formatnumberscale == 1) {
						var B, n = b;
						r = l.numberscalevalue;
						B = l.numberscaleunit;
						var G = {},
							K = l.defaultnumberscale,
							R = 0,
							v, C = [],
							f = [];
						if (l.scalerecursively) {
							for (R = 0; R < r.length; R++) if (v = h(r[R]) || 1E3, Math.abs(Number(n)) >= v && R < r.length - 1) K = n % v, n = (n - K) / v, K != 0 && (C.push(K), f.push(B[R]));
							else {
								C.push(n);
								f.push(B[R]);
								break
							}
							C.reverse();
							f.reverse();
							G.value = C;
							G.scale = f
						} else {
							if (r.length === B.length) for (R = 0; R < r.length; R++) if ((v = h(r[R]) || 1E3) && Math.abs(Number(n)) >= v) K = B[R] || e, n = Number(n) / v;
							else break;
							G.value = n;
							G.scale = K
						}
						B = G;
						b = n = B.value;
						r = B.scale
					}
					if (l.scalerecursively && l.formatnumberscale != 0) {
						r = B.value;
						B = B.scale;
						G = l.maxscalerecursion == -1 ? r.length : Math.min(r.length, l.maxscalerecursion);
						if (l.formatnumber == 1) {
							n = "";
							for (C = 0; C < G; C++) R = C == 0 ? r[C] : Math.abs(r[C]), v = R + e, C == G - 1 && (v = g(R, w(l.decimalprecision, m), l.forcedecimals)), n = n + ka(v, l.decimalseparator, l.thousandseparator, l.thousandseparatorposition) + B[C] + (C < G - 1 ? l.scaleseparator : "")
						} else {
							n = "";
							for (C = 0; C < G; C++) n = n + (C == 0 ? r[C] : Math.abs(r[C]) + e) + B[C] + (C < G - 1 ? l.scaleseparator : "")
						}
						n = (l.numberprefix || e) + n + (l.numbersuffix || e);
						delete r;
						delete B
					} else l.formatnumber == 1 && (n = g(b, w(l.decimalprecision, m), l.forcedecimals), n = ka(n, l.decimalseparator, l.thousandseparator, l.thousandseparatorposition)), n = (l.numberprefix || e) + n + r + (l.numbersuffix || e);
					return n
				}
			};
		return K
	}()
}]);
FusionCharts(["private", "modules.renderer.js-raphael", function() {
	var g = this.hcLib,
		h = "",
		m, U = window.Raphael,
		w, S = window.parent !== window,
		ia = navigator.userAgent.match(/(iPad|iPhone|iPod)/g) ? !0 : !1;
	(function(b) {
		var h = /[\.\/]/,
			e = function() {},
			r = function(b, e) {
				return b - e
			},
			x, m, w = {
				n: {}
			},
			s = g.eve = function(b, e) {
				var g = m,
					h = Array.prototype.slice.call(arguments, 2),
					B = s.listeners(b),
					w = 0,
					l, n = [],
					F = {},
					fa = [],
					D = x;
				x = b;
				for (var M = m = 0, U = B.length; M < U; M++)"zIndex" in B[M] && (n.push(B[M].zIndex), B[M].zIndex < 0 && (F[B[M].zIndex] = B[M]));
				for (n.sort(r); n[w] < 0;) if (l = F[n[w++]], fa.push(l.apply(e, h)), m) return m = g, fa;
				for (M = 0; M < U; M++) if (l = B[M], "zIndex" in l) if (l.zIndex == n[w]) {
					fa.push(l.apply(e, h));
					if (m) break;
					do
					if (w++, (l = F[n[w]]) && fa.push(l.apply(e, h)), m) break;
					while (l)
				} else F[l.zIndex] = l;
				else if (fa.push(l.apply(e, h)), m) break;
				m = g;
				x = D;
				return fa.length ? fa : null
			};
		s.listeners = function(b) {
			var b = b.split(h),
				e = w,
				g, s, r, m, l, n, x, aa = [e],
				D = [];
			r = 0;
			for (m = b.length; r < m; r++) {
				x = [];
				l = 0;
				for (n = aa.length; l < n; l++) {
					e = aa[l].n;
					g = [e[b[r]], e["*"]];
					for (s = 2; s--;) if (e = g[s]) x.push(e), D = D.concat(e.f || [])
				}
				aa = x
			}
			return D
		};
		s.on = function(b, g) {
			for (var s = b.split(h), r = w, m = 0, x = s.length; m < x; m++) r = r.n, !r[s[m]] && (r[s[m]] = {
				n: {}
			}), r = r[s[m]];
			r.f = r.f || [];
			m = 0;
			for (x = r.f.length; m < x; m++) if (r.f[m] == g) return e;
			r.f.push(g);
			return function(b) {
				if (+b == +b) g.zIndex = +b
			}
		};
		s.stop = function() {
			m = 1
		};
		s.nt = function(b) {
			if (b) return RegExp("(?:\\.|\\/|^)" + b + "(?:\\.|\\/|$)").test(x);
			return x
		};
		s.off = s.unbind = function(b, e) {
			var g = b.split(h),
				s, r, m, l, n, x, aa = [w];
			l = 0;
			for (n = g.length; l < n; l++) for (x = 0; x < aa.length; x += m.length - 2) {
				m = [x, 1];
				s = aa[x].n;
				if (g[l] != "*") s[g[l]] && m.push(s[g[l]]);
				else for (r in s) s.hasOwnProperty(r) && m.push(s[r]);
				aa.splice.apply(aa, m)
			}
			l = 0;
			for (n = aa.length; l < n; l++) for (s = aa[l]; s.n;) {
				if (e) {
					if (s.f) {
						x = 0;
						for (g = s.f.length; x < g; x++) if (s.f[x] == e) {
							s.f.splice(x, 1);
							break
						}!s.f.length && delete s.f
					}
					for (r in s.n) if (s.n.hasOwnProperty(r) && s.n[r].f) {
						m = s.n[r].f;
						x = 0;
						for (g = m.length; x < g; x++) if (m[x] == e) {
							m.splice(x, 1);
							break
						}!m.length && delete s.n[r].f
					}
				} else for (r in delete s.f, s.n) s.n.hasOwnProperty(r) && s.n[r].f && delete s.n[r].f;
				s = s.n
			}
		};
		s.once = function(b, e) {
			var g = function() {
					var r = e.apply(this, arguments);
					s.unbind(b, g);
					return r
				};
			return s.on(b, g)
		};
		s.version = "0.3.4";
		s.toString = function() {
			return "You are running Eve 0.3.4"
		};
		typeof module != "undefined" && module.exports ? module.exports = s : typeof define != "undefined" ? define("eve", [], function() {
			return s
		}) : b.eve = s
	})(g);
	m = g.eve;
	(function() {
		function b(a) {
			if (b._url) b._url = window.location.href.replace(/#.*?$/, h);
			if (b.is(a, "function")) return O ? a() : m.on("raphael.DOMload", a);
			else if (b.is(a, ta)) return b._engine.create[v](b, a.splice(0, 3 + b.is(a[0], ma))).add(a);
			else {
				var c = Array.prototype.slice.call(arguments, 0);
				if (b.is(c[c.length - 1], "function")) {
					var d = c.pop();
					return O ? d.call(b._engine.create[v](b, c)) : m.on("raphael.DOMload", function() {
						d.call(b._engine.create[v](b, c))
					})
				} else return b._engine.create[v](b, arguments)
			}
		}
		function g(b) {
			if (Object(b) !== b) return b;
			var a = new b.constructor,
				c;
			for (c in b) b[aa](c) && (a[c] = g(b[c]));
			return a
		}
		function e() {
			return this.hex
		}
		function r(b, a) {
			for (var c = [], d = 0, f = b.length; f - 2 * !a > d; d += 2) {
				var e = [{
					x: +b[d - 2],
					y: +b[d - 1]
				}, {
					x: +b[d],
					y: +b[d + 1]
				}, {
					x: +b[d + 2],
					y: +b[d + 3]
				}, {
					x: +b[d + 4],
					y: +b[d + 5]
				}];
				a ? d ? f - 4 == d ? e[3] = {
					x: +b[0],
					y: +b[1]
				} : f - 2 == d && (e[2] = {
					x: +b[0],
					y: +b[1]
				}, e[3] = {
					x: +b[2],
					y: +b[3]
				}) : e[0] = {
					x: +b[f - 2],
					y: +b[f - 1]
				} : f - 4 == d ? e[3] = e[2] : d || (e[0] = {
					x: +b[d],
					y: +b[d + 1]
				});
				c.push(["C", (-e[0].x + 6 * e[1].x + e[2].x) / 6, (-e[0].y + 6 * e[1].y + e[2].y) / 6, (e[1].x + 6 * e[2].x - e[3].x) / 6, (e[1].y + 6 * e[2].y - e[3].y) / 6, e[2].x, e[2].y])
			}
			return c
		}
		function x(b, a, c, d, e, f, y, o, E) {
			E == null && (E = 1);
			for (var E = (E > 1 ? 1 : E < 0 ? 0 : E) / 2, k = [-0.1252, 0.1252, -0.3678, 0.3678, -0.5873, 0.5873, -0.7699, 0.7699, -0.9041, 0.9041, -0.9816, 0.9816], ha = [0.2491, 0.2491, 0.2335, 0.2335, 0.2032, 0.2032, 0.1601, 0.1601, 0.1069, 0.1069, 0.0472, 0.0472], q = 0, W = 0; W < 12; W++) {
				var p = E * k[W] + E,
					u = p * (p * (-3 * b + 9 * c - 9 * e + 3 * y) + 6 * b - 12 * c + 6 * e) - 3 * b + 3 * c,
					p = p * (p * (-3 * a + 9 * d - 9 * f + 3 * o) + 6 * a - 12 * d + 6 * f) - 3 * a + 3 * d;
				q += ha[W] * va(u * u + p * p)
			}
			return E * q
		}
		function U(b, a, c, d, e, f, y, o, E) {
			if (!(E < 0 || x(b, a, c, d, e, f, y, o) < E)) {
				var k = 0.5,
					ha = 1 - k,
					W;
				for (W = x(b, a, c, d, e, f, y, o, ha); ca(W - E) > 0.01;) k /= 2, ha += (W < E ? 1 : -1) * k, W = x(b, a, c, d, e, f, y, o, ha);
				return ha
			}
		}
		function fa(a, c, d) {
			for (var a = b._path2curve(a), c = b._path2curve(c), e, f, y, o, E, k, ha, W, q, p, t = d ? 0 : [], Q = 0, Ea = a.length; Q < Ea; Q++) if (q = a[Q], q[0] == "M") e = E = q[1], f = k = q[2];
			else {
				q[0] == "C" ? (q = [e, f].concat(q.slice(1)), e = q[6], f = q[7]) : (q = [e, f, e, f, E, k, E, k], e = E, f = k);
				for (var i = 0, z = c.length; i < z; i++) if (p = c[i], p[0] == "M") y = ha = p[1], o = W = p[2];
				else {
					p[0] == "C" ? (p = [y, o].concat(p.slice(1)), y = p[6], o = p[7]) : (p = [y, o, y, o, ha, W, ha, W], y = ha, o = W);
					var na;
					var j = q,
						oa = p;
					na = d;
					var ja = b.bezierBBox(j),
						A = b.bezierBBox(oa);
					if (b.isBBoxIntersect(ja, A)) {
						for (var ja = x.apply(0, j), A = x.apply(0, oa), ja = ~~ (ja / 5), A = ~~ (A / 5), H = [], gb = [], P = {}, jb = na ? 0 : [], Pa = 0; Pa < ja + 1; Pa++) {
							var g = b.findDotsAtSegment.apply(b, j.concat(Pa / ja));
							H.push({
								x: g.x,
								y: g.y,
								t: Pa / ja
							})
						}
						for (Pa = 0; Pa < A + 1; Pa++) g = b.findDotsAtSegment.apply(b, oa.concat(Pa / A)), gb.push({
							x: g.x,
							y: g.y,
							t: Pa / A
						});
						for (Pa = 0; Pa < ja; Pa++) for (j = 0; j < A; j++) {
							var n = H[Pa],
								ab = H[Pa + 1],
								oa = gb[j],
								g = gb[j + 1],
								s = ca(ab.x - n.x) < 0.001 ? "y" : "x",
								r = ca(g.x - oa.x) < 0.001 ? "y" : "x",
								L;
							b: {
								L = n.x;
								var l = n.y,
									h = ab.x,
									v = ab.y,
									m = oa.x,
									qa = oa.y,
									ga = g.x,
									Xa = g.y;
								if (!(u(L, h) < N(m, ga) || N(L, h) > u(m, ga) || u(l, v) < N(qa, Xa) || N(l, v) > u(qa, Xa))) {
									var cb = (L - h) * (qa - Xa) - (l - v) * (m - ga);
									if (cb) {
										var hb = ((L * v - l * h) * (m - ga) - (L - h) * (m * Xa - qa * ga)) / cb,
											cb = ((L * v - l * h) * (qa - Xa) - (l - v) * (m * Xa - qa * ga)) / cb,
											da = +hb.toFixed(2),
											B = +cb.toFixed(2);
										if (!(da < +N(L, h).toFixed(2) || da > +u(L, h).toFixed(2) || da < +N(m, ga).toFixed(2) || da > +u(m, ga).toFixed(2) || B < +N(l, v).toFixed(2) || B > +u(l, v).toFixed(2) || B < +N(qa, Xa).toFixed(2) || B > +u(qa, Xa).toFixed(2))) {
											L = {
												x: hb,
												y: cb
											};
											break b
										}
									}
								}
								L = void 0
							}
							L && P[L.x.toFixed(4)] != L.y.toFixed(4) && (P[L.x.toFixed(4)] = L.y.toFixed(4), n = n.t + ca((L[s] - n[s]) / (ab[s] - n[s])) * (ab.t - n.t), oa = oa.t + ca((L[r] - oa[r]) / (g[r] - oa[r])) * (g.t - oa.t), n >= 0 && n <= 1 && oa >= 0 && oa <= 1 && (na ? jb++ : jb.push({
								x: L.x,
								y: L.y,
								t1: n,
								t2: oa
							})))
						}
						na = jb
					} else na = na ? 0 : [];
					if (d) t += na;
					else {
						ja = 0;
						for (A = na.length; ja < A; ja++) na[ja].segment1 = Q, na[ja].segment2 = i, na[ja].bez1 = q, na[ja].bez2 = p;
						t = t.concat(na)
					}
				}
			}
			return t
		}
		function s(b, a, c, d, e, f) {
			b != null ? (this.a = +b, this.b = +a, this.c = +c, this.d = +d, this.e = +e, this.f = +f) : (this.a = 1, this.c = this.b = 0, this.d = 1, this.f = this.e = 0)
		}
		function ka() {
			return this.x + j + this.y + j + this.width + " \u00d7 " + this.height
		}
		function G(b, a, c, d, e, f) {
			function y(b, a) {
				var Ha, c, d, e;
				d = b;
				for (c = 0; c < 8; c++) {
					e = ((k * d + E) * d + o) * d - b;
					if (ca(e) < a) return d;
					Ha = (3 * k * d + 2 * E) * d + o;
					if (ca(Ha) < 1.0E-6) break;
					d -= e / Ha
				}
				Ha = 0;
				c = 1;
				d = b;
				if (d < Ha) return Ha;
				if (d > c) return c;
				for (; Ha < c;) {
					e = ((k * d + E) * d + o) * d;
					if (ca(e - b) < a) break;
					b > e ? Ha = d : c = d;
					d = (c - Ha) / 2 + Ha
				}
				return d
			}
			var o = 3 * a,
				E = 3 * (d - a) - o,
				k = 1 - o - E,
				ha = 3 * c,
				q = 3 * (e - c) - ha,
				W = 1 - ha - q;
			return function(b, a) {
				var Ha = y(b, a);
				return ((W * Ha + q) * Ha + ha) * Ha
			}(b, 1 / (200 * f))
		}
		function V(b, a) {
			var c = [],
				d = {};
			this.ms = a;
			this.times = 1;
			if (b) {
				for (var e in b) b[aa](e) && (d[sa(e)] = b[e], c.push(sa(e)));
				c.sort(z)
			}
			this.anim = d;
			this.top = c[c.length - 1];
			this.percents = c
		}
		function K(a, d, e, f, y, E) {
			var e = sa(e),
				k, ha, q, W, p, u, t = a.ms,
				Q = {},
				Ea = {},
				j = {};
			if (f) {
				u = 0;
				for (oa = xa.length; u < oa; u++) {
					var z = xa[u];
					if (z.el.id == d.id && z.anim == a) {
						z.percent != e ? (xa.splice(u, 1), q = 1) : ha = z;
						d.attr(z.totalOrigin);
						break
					}
				}
			} else f = +Ea;
			u = 0;
			for (var oa = a.percents.length; u < oa; u++) if (a.percents[u] == e || a.percents[u] > f * a.top) {
				e = a.percents[u];
				p = a.percents[u - 1] || 0;
				t = t / a.top * (e - p);
				W = a.percents[u + 1];
				k = a.anim[e];
				break
			} else f && d.attr(a.anim[a.percents[u]]);
			if (k) {
				if (ha) ha.initstatus = f, ha.start = new Date - ha.ms * f;
				else {
					for (var ja in k) if (k[aa](ja) && (Ta[aa](ja) || d.ca[ja])) switch (Q[ja] = d.attr(ja), Q[ja] == null && (Q[ja] = Wa[ja]), Ea[ja] = k[ja], Ta[ja]) {
					case ma:
						j[ja] = (Ea[ja] - Q[ja]) / t;
						break;
					case "colour":
						Q[ja] = b.getRGB(Q[ja]);
						u = b.getRGB(Ea[ja]);
						j[ja] = {
							r: (u.r - Q[ja].r) / t,
							g: (u.g - Q[ja].g) / t,
							b: (u.b - Q[ja].b) / t
						};
						break;
					case "path":
						u = o(Q[ja], Ea[ja]);
						z = u[1];
						Q[ja] = u[0];
						j[ja] = [];
						u = 0;
						for (oa = Q[ja].length; u < oa; u++) {
							j[ja][u] = [0];
							for (var A = 1, N = Q[ja][u].length; A < N; A++) j[ja][u][A] = (z[u][A] - Q[ja][u][A]) / t
						}
						break;
					case "transform":
						u = d._;
						if (oa = na(u[ja], Ea[ja])) {
							Q[ja] = oa.from;
							Ea[ja] = oa.to;
							j[ja] = [];
							j[ja].real = !0;
							u = 0;
							for (oa = Q[ja].length; u < oa; u++) {
								j[ja][u] = [Q[ja][u][0]];
								A = 1;
								for (N = Q[ja][u].length; A < N; A++) j[ja][u][A] = (Ea[ja][u][A] - Q[ja][u][A]) / t
							}
						} else oa = d.matrix || new s, u = {
							_: {
								transform: u.transform
							},
							getBBox: function() {
								return d.getBBox(1)
							}
						}, Q[ja] = [oa.a, oa.b, oa.c, oa.d, oa.e, oa.f], gb(u, Ea[ja]), Ea[ja] = u._.transform, j[ja] = [(u.matrix.a - oa.a) / t, (u.matrix.b - oa.b) / t, (u.matrix.c - oa.c) / t, (u.matrix.d - oa.d) / t, (u.matrix.e - oa.e) / t, (u.matrix.f - oa.f) / t];
						break;
					case "csv":
						oa = i(k[ja])[c](l);
						z = i(Q[ja])[c](l);
						if (ja == "clip-rect") {
							Q[ja] = z;
							j[ja] = [];
							for (u = z.length; u--;) j[ja][u] = (oa[u] - Q[ja][u]) / t
						}
						Ea[ja] = oa;
						break;
					default:
						oa = [][C](k[ja]);
						z = [][C](Q[ja]);
						j[ja] = [];
						for (u = d.ca[ja].length; u--;) j[ja][u] = ((oa[u] || 0) - (z[u] || 0)) / t
					}
					u = k.easing;
					ja = b.easing_formulas[u];
					if (!ja) if ((ja = i(u).match(Va)) && ja.length == 5) {
						var Pa = ja;
						ja = function(b) {
							return G(b, +Pa[1], +Pa[2], +Pa[3], +Pa[4], t)
						}
					} else ja = H;
					u = k.start || a.start || +new Date;
					z = {
						anim: a,
						percent: e,
						timestamp: u,
						start: u + (a.del || 0),
						status: 0,
						initstatus: f || 0,
						stop: !1,
						ms: t,
						easing: ja,
						from: Q,
						diff: j,
						to: Ea,
						el: d,
						callback: k.callback,
						prev: p,
						next: W,
						repeat: E || a.times,
						origin: d.attr(),
						totalOrigin: y
					};
					xa.push(z);
					if (f && !ha && !q && (z.stop = !0, z.start = new Date - t * f, xa.length == 1)) return kb();
					if (q) z.start = new Date - z.ms * f;
					xa.length == 1 && mb(kb)
				}
				m("raphael.anim.start." + d.id, d, a)
			}
		}
		function Y(b) {
			for (var a = 0; a < xa.length; a++) xa[a].el.paper == b && xa.splice(a--, 1)
		}
		w = b;
		b.version = "2.1.0";
		b.eve = m;
		var O, l = /[, ]+/,
			n = {
				circle: 1,
				rect: 1,
				path: 1,
				ellipse: 1,
				text: 1,
				image: 1,
				group: 1
			},
			F = /\{(\d+)\}/g,
			aa = "hasOwnProperty",
			D = {
				doc: document,
				win: window
			},
			M = function() {};
		b.ca = M.prototype;
		var Z = {
			was: Object.prototype[aa].call(D.win, "Raphael"),
			is: D.win.Raphael
		},
			R = function() {
				this.ca = this.customAttributes = new M;
				this._CustomAttributes = function() {};
				this._CustomAttributes.prototype = this.ca
			},
			v = "apply",
			C = "concat",
			f = b._supportsTouch = "createTouch" in D.doc,
			j = " ",
			i = String,
			c = "split",
			d = "click dblclick mousedown mousemove mouseout mouseover mouseup touchstart touchmove touchend touchcancel" [c](j),
			a = b._touchMap = {
				mousedown: "touchstart",
				mousemove: "touchmove",
				mouseup: "touchend"
			},
			k = i.prototype.toLowerCase,
			q = Math,
			u = q.max,
			N = q.min,
			ca = q.abs,
			J = q.pow,
			Ja = q.cos,
			pa = q.sin,
			va = q.sqrt,
			X = q.PI,
			Ca = X / 180,
			ma = "number",
			ta = "array",
			Aa = Object.prototype.toString;
		b._ISURL = /^url\(['"]?([^\)]+?)['"]?\)$/i;
		var Ka = /^\s*((#[a-f\d]{6})|(#[a-f\d]{3})|rgba?\(\s*([\d\.]+%?\s*,\s*[\d\.]+%?\s*,\s*[\d\.]+%?(?:\s*,\s*[\d\.]+%?)?)\s*\)|hsba?\(\s*([\d\.]+(?:deg|\xb0|%)?\s*,\s*[\d\.]+%?\s*,\s*[\d\.]+(?:%?\s*,\s*[\d\.]+)?)%?\s*\)|hsla?\(\s*([\d\.]+(?:deg|\xb0|%)?\s*,\s*[\d\.]+%?\s*,\s*[\d\.]+(?:%?\s*,\s*[\d\.]+)?)%?\s*\))\s*$/i,
			bb = {
				NaN: 1,
				Infinity: 1,
				"-Infinity": 1
			},
			Va = /^(?:cubic-)?bezier\(([^,]+),([^,]+),([^,]+),([^\)]+)\)/,
			Ra = q.round,
			sa = parseFloat,
			Ma = parseInt,
			Na = i.prototype.toUpperCase,
			Wa = b._availableAttrs = {
				"arrow-end": "none",
				"arrow-start": "none",
				blur: 0,
				"clip-rect": "0 0 1e9 1e9",
				"clip-path": "",
				cursor: "default",
				cx: 0,
				cy: 0,
				fill: "#fff",
				"fill-opacity": 1,
				font: '10px "Arial"',
				"font-family": '"Arial"',
				"font-size": "10",
				"font-style": "normal",
				"font-weight": 400,
				gradient: 0,
				height: 0,
				href: "about:blank",
				"letter-spacing": 0,
				"line-height": 12,
				"vertical-align": "middle",
				opacity: 1,
				path: "M0,0",
				r: 0,
				rx: 0,
				ry: 0,
				src: "",
				stroke: "#000",
				"stroke-dasharray": "",
				"stroke-linecap": "butt",
				"stroke-linejoin": "butt",
				"stroke-miterlimit": 0,
				"stroke-opacity": 1,
				"stroke-width": 1,
				"shape-rendering": "default",
				target: "_blank",
				"text-anchor": "middle",
				visibility: "",
				title: "",
				transform: "",
				rotation: 0,
				width: 0,
				x: 0,
				y: 0
			},
			Ta = b._availableAnimAttrs = {
				blur: ma,
				"clip-rect": "csv",
				"clip-path": "path",
				cx: ma,
				cy: ma,
				fill: "colour",
				"fill-opacity": ma,
				"font-size": ma,
				height: ma,
				opacity: ma,
				path: "path",
				r: ma,
				rx: ma,
				ry: ma,
				stroke: "colour",
				"stroke-opacity": ma,
				"stroke-width": ma,
				transform: "transform",
				width: ma,
				x: ma,
				y: ma
			},
			Fa = /[\x09\x0a\x0b\x0c\x0d\x20\xa0\u1680\u180e\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000\u2028\u2029]*,[\x09\x0a\x0b\x0c\x0d\x20\xa0\u1680\u180e\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000\u2028\u2029]*/,
			$a = {
				hs: 1,
				rg: 1
			},
			eb = /,?([achlmqrstvxz]),?/gi,
			fb = /([achlmrqstvz])[\x09\x0a\x0b\x0c\x0d\x20\xa0\u1680\u180e\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000\u2028\u2029,]*((-?\d*\.?\d*(?:e[\-+]?\d+)?[\x09\x0a\x0b\x0c\x0d\x20\xa0\u1680\u180e\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000\u2028\u2029]*,?[\x09\x0a\x0b\x0c\x0d\x20\xa0\u1680\u180e\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000\u2028\u2029]*)+)/ig,
			Sa = /([rstm])[\x09\x0a\x0b\x0c\x0d\x20\xa0\u1680\u180e\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000\u2028\u2029,]*((-?\d*\.?\d*(?:e[\-+]?\d+)?[\x09\x0a\x0b\x0c\x0d\x20\xa0\u1680\u180e\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000\u2028\u2029]*,?[\x09\x0a\x0b\x0c\x0d\x20\xa0\u1680\u180e\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000\u2028\u2029]*)+)/ig,
			Za = /(-?\d*\.?\d*(?:e[\-+]?\d+)?)[\x09\x0a\x0b\x0c\x0d\x20\xa0\u1680\u180e\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000\u2028\u2029]*,?[\x09\x0a\x0b\x0c\x0d\x20\xa0\u1680\u180e\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000\u2028\u2029]*/ig;
		b._radial_gradient = /^x?r(?:\(([^\)]*?)\))?/;
		var p = {},
			z = function(b, a) {
				return sa(b) - sa(a)
			},
			t = function() {},
			H = function(b) {
				return b
			},
			P = b._rectPath = function(b, a, c, d, e) {
				if (e) return [["M", b + e, a], ["l", c - e * 2, 0], ["a", e, e, 0, 0, 1, e, e], ["l", 0, d - e * 2], ["a", e, e, 0, 0, 1, -e, e], ["l", e * 2 - c, 0], ["a", e, e, 0, 0, 1, -e, -e], ["l", 0, e * 2 - d], ["a", e, e, 0, 0, 1, e, -e], ["z"]];
				return [["M", b, a], ["l", c, 0], ["l", 0, d], ["l", -c, 0], ["z"]]
			},
			ga = function(b, a, c, d) {
				d == null && (d = c);
				return [["M", b, a], ["m", 0, -d], ["a", c, d, 0, 1, 1, 0, 2 * d], ["a", c, d, 0, 1, 1, 0, -2 * d], ["z"]]
			},
			qa = b._getPath = {
				group: function() {
					return !1
				},
				path: function(b) {
					return b.attr("path")
				},
				circle: function(b) {
					b = b.attrs;
					return ga(b.cx, b.cy, b.r)
				},
				ellipse: function(b) {
					b = b.attrs;
					return ga(b.cx, b.cy, b.rx, b.ry)
				},
				rect: function(b) {
					b = b.attrs;
					return P(b.x, b.y, b.width, b.height, b.r)
				},
				image: function(b) {
					b = b.attrs;
					return P(b.x, b.y, b.width, b.height)
				},
				text: function(b) {
					b = b._getBBox();
					return P(b.x, b.y, b.width, b.height)
				}
			},
			da = b.mapPath = function(b, a) {
				if (!a) return b;
				var c, d, e, f, y, E, k, b = o(b);
				e = 0;
				for (y = b.length; e < y; e++) {
					k = b[e];
					f = 1;
					for (E = k.length; f < E; f += 2) c = a.x(k[f], k[f + 1]), d = a.y(k[f], k[f + 1]), k[f] = c, k[f + 1] = d
				}
				return b
			};
		b.pick = function() {
			var b, a, c;
			a = 0;
			for (c = arguments.length; a < c; a += 1) if ((b = arguments[a]) || !(b !== !1 && b !== 0)) return b
		};
		b._g = D;
		b.type = D.win.ENABLE_RED_CANVAS && (D.win.CanvasRenderingContext2D || D.doc.createElement("canvas").getContext) ? "CANVAS" : D.win.SVGAngle || D.doc.implementation.hasFeature("http://www.w3.org/TR/SVG11/feature#BasicStructure", "1.1") ? "SVG" : "VML";
		if (b.type == "VML") {
			var A = D.doc.createElement("div");
			A.innerHTML = '<v:shape adj="1"/>';
			A = A.firstChild;
			A.style.behavior = "url(#default#VML)";
			if (!(A && typeof A.adj == "object")) return b.type = "";
			A = null
		}
		b.svg = !((b.vml = b.type == "VML") || (b.canvas = b.type == "CANVAS"));
		b._Paper = R;
		b.fn = R = R.prototype = b.prototype;
		b._id = 0;
		b._oid = 0;
		b.is = function(b, a) {
			a = k.call(a);
			if (a == "finite") return !bb[aa](+b);
			if (a == "array") return b instanceof Array;
			if (a === "object" && (b === void 0 || b === null)) return !1;
			return a == "null" && b === null || a == typeof b && b !== null || a == "object" && b === Object(b) || a == "array" && Array.isArray && Array.isArray(b) || Aa.call(b).slice(8, -1).toLowerCase() == a
		};
		b.angle = function(a, c, d, e, f, y) {
			if (f == null) {
				a -= d;
				c -= e;
				if (!a && !c) return 0;
				return (q.atan2(-c, -a) * 180 / X + 540) % 360
			} else return b.angle(a, c, f, y) - b.angle(d, e, f, y)
		};
		b.rad = function(b) {
			return b % 360 * Ca
		};
		b.deg = function(b) {
			return b * 180 / X % 360
		};
		b.snapTo = function(a, c, d) {
			d = b.is(d, "finite") ? d : 10;
			if (b.is(a, ta)) for (var e = a.length; e--;) {
				if (ca(a[e] - c) <= d) return a[e]
			} else {
				a = +a;
				e = c % a;
				if (e < d) return c - e;
				if (e > a - d) return c - e + a
			}
			return c
		};
		b.createUUID = function(b, a) {
			return function() {
				return "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(b, a).toUpperCase()
			}
		}(/[xy]/g, function(b) {
			var a = q.random() * 16 | 0;
			return (b == "x" ? a : a & 3 | 8).toString(16)
		});
		b.setWindow = function(a) {
			m("raphael.setWindow", b, D.win, a);
			D.win = a;
			D.doc = D.win.document;
			b._engine.initWin && b._engine.initWin(D.win)
		};
		var wa = function(a) {
				if (b.vml) {
					var c = /^\s+|\s+$/g,
						d;
					try {
						var e = new ActiveXObject("htmlfile");
						e.write("<body>");
						e.close();
						d = e.body
					} catch (f) {
						d = createPopup().document.body
					}
					var y = d.createTextRange();
					wa = ba(function(b) {
						try {
							d.style.color = i(b).replace(c, "");
							var a = y.queryCommandValue("ForeColor");
							return "#" + ("000000" + ((a & 255) << 16 | a & 65280 | (a & 16711680) >>> 16).toString(16)).slice(-6)
						} catch (Ha) {
							return "none"
						}
					})
				} else {
					var o = D.doc.createElement("i");
					o.title = "Rapha\u00ebl Colour Picker";
					o.style.display = "none";
					D.doc.body.appendChild(o);
					wa = ba(function(b) {
						o.style.color = b;
						return D.doc.defaultView.getComputedStyle(o, "").getPropertyValue("color")
					})
				}
				return wa(a)
			},
			Ia = function() {
				return "hsb(" + [this.h, this.s, this.b] + ")"
			},
			Ba = function() {
				return "hsl(" + [this.h, this.s, this.l] + ")"
			},
			ea = function() {
				return this.hex
			},
			ra = function(a, c, d) {
				if (c == null && b.is(a, "object") && "r" in a && "g" in a && "b" in a) d = a.b, c = a.g, a = a.r;
				if (c == null && b.is(a, "string")) d = b.getRGB(a), a = d.r, c = d.g, d = d.b;
				if (a > 1 || c > 1 || d > 1) a /= 255, c /= 255, d /= 255;
				return [a, c, d]
			},
			T = function(a, c, d, e) {
				a *= 255;
				c *= 255;
				d *= 255;
				a = {
					r: a,
					g: c,
					b: d,
					hex: b.rgb(a, c, d),
					toString: ea
				};
				b.is(e, "finite") && (a.opacity = e);
				return a
			};
		b.color = function(a) {
			var c;
			b.is(a, "object") && "h" in a && "s" in a && "b" in a ? (c = b.hsb2rgb(a), a.r = c.r, a.g = c.g, a.b = c.b, a.hex = c.hex) : b.is(a, "object") && "h" in a && "s" in a && "l" in a ? (c = b.hsl2rgb(a), a.r = c.r, a.g = c.g, a.b = c.b, a.hex = c.hex) : (b.is(a, "string") && (a = b.getRGB(a)), b.is(a, "object") && "r" in a && "g" in a && "b" in a ? (c = b.rgb2hsl(a), a.h = c.h, a.s = c.s, a.l = c.l, c = b.rgb2hsb(a), a.v = c.b) : (a = {
				hex: "none"
			}, a.r = a.g = a.b = a.h = a.s = a.v = a.l = -1));
			a.toString = ea;
			return a
		};
		b.hsb2rgb = function(a, b, c, d) {
			if (this.is(a, "object") && "h" in a && "s" in a && "b" in a) c = a.b, b = a.s, a = a.h, d = a.o;
			a *= 360;
			var e, f, y, a = a % 360 / 60;
			y = c * b;
			b = y * (1 - ca(a % 2 - 1));
			c = e = f = c - y;
			a = ~~a;
			c += [y, b, 0, 0, b, y][a];
			e += [b, y, y, b, 0, 0][a];
			f += [0, 0, b, y, y, b][a];
			return T(c, e, f, d)
		};
		b.hsl2rgb = function(a, b, c, d) {
			if (this.is(a, "object") && "h" in a && "s" in a && "l" in a) c = a.l, b = a.s, a = a.h;
			if (a > 1 || b > 1 || c > 1) a /= 360, b /= 100, c /= 100;
			a *= 360;
			var e, f, y, a = a % 360 / 60;
			y = 2 * b * (c < 0.5 ? c : 1 - c);
			b = y * (1 - ca(a % 2 - 1));
			c = e = f = c - y / 2;
			a = ~~a;
			c += [y, b, 0, 0, b, y][a];
			e += [b, y, y, b, 0, 0][a];
			f += [0, 0, b, y, y, b][a];
			return T(c, e, f, d)
		};
		b.rgb2hsb = function(a, b, c) {
			var c = ra(a, b, c),
				a = c[0],
				b = c[1],
				c = c[2],
				d, e;
			d = u(a, b, c);
			e = d - N(a, b, c);
			return {
				h: ((e == 0 ? 0 : d == a ? (b - c) / e : d == b ? (c - a) / e + 2 : (a - b) / e + 4) + 360) % 6 * 60 / 360,
				s: e == 0 ? 0 : e / d,
				b: d,
				toString: Ia
			}
		};
		b.rgb2hsl = function(a, b, c) {
			var c = ra(a, b, c),
				a = c[0],
				b = c[1],
				c = c[2],
				d, e, f;
			e = u(a, b, c);
			d = N(a, b, c);
			f = e - d;
			d = (e + d) / 2;
			return {
				h: ((f == 0 ? 0 : e == a ? (b - c) / f : e == b ? (c - a) / f + 2 : (a - b) / f + 4) + 360) % 6 * 60 / 360,
				s: f == 0 ? 0 : d < 0.5 ? f / (2 * d) : f / (2 - 2 * d),
				l: d,
				toString: Ba
			}
		};
		b._path2string = function() {
			return this.join(",").replace(eb, "$1")
		};
		var ba = b._cacher = function(a, b, c) {
				function d() {
					var e = Array.prototype.slice.call(arguments, 0),
						f = e.join("\u2400"),
						y = d.cache = d.cache || {},
						o = d.count = d.count || [];
					if (y[aa](f)) {
						a: for (var e = o, o = f, E = 0, k = e.length; E < k; E++) if (e[E] === o) {
							e.push(e.splice(E, 1)[0]);
							break a
						}
						return c ? c(y[f]) : y[f]
					}
					o.length >= 1E3 && delete y[o.shift()];
					o.push(f);
					y[f] = a[v](b, e);
					return c ? c(y[f]) : y[f]
				}
				return d
			};
		b._preload = function(a, b) {
			var c = D.doc.createElement("img");
			c.style.cssText = "position:absolute;left:-9999em;top:-9999em";
			c.onload = function() {
				b.call(this);
				this.onload = null;
				D.doc.body.removeChild(this)
			};
			c.onerror = function() {
				D.doc.body.removeChild(this)
			};
			D.doc.body.appendChild(c);
			c.src = a
		};
		b.getRGB = ba(function(a) {
			var d;
			a && b.is(a, "object") && "opacity" in a && (d = a.opacity);
			if (!a || (a = i(a)).indexOf("-") + 1) return {
				r: -1,
				g: -1,
				b: -1,
				hex: "none",
				error: 1,
				toString: e
			};
			if (a == "none") return {
				r: -1,
				g: -1,
				b: -1,
				hex: "none",
				toString: e
			};
			!($a[aa](a.toLowerCase().substring(0, 2)) || a.charAt() == "#") && (a = wa(a));
			var f, y, o, E;
			if (a = a.match(Ka)) {
				a[2] && (o = Ma(a[2].substring(5), 16), y = Ma(a[2].substring(3, 5), 16), f = Ma(a[2].substring(1, 3), 16));
				a[3] && (o = Ma((E = a[3].charAt(3)) + E, 16), y = Ma((E = a[3].charAt(2)) + E, 16), f = Ma((E = a[3].charAt(1)) + E, 16));
				a[4] && (E = a[4][c](Fa), f = sa(E[0]), E[0].slice(-1) == "%" && (f *= 2.55), y = sa(E[1]), E[1].slice(-1) == "%" && (y *= 2.55), o = sa(E[2]), E[2].slice(-1) == "%" && (o *= 2.55), a[1].toLowerCase().slice(0, 4) == "rgba" && (d = sa(E[3])), E[3] && E[3].slice(-1) == "%" && (d /= 100));
				if (a[5]) return E = a[5][c](Fa), f = sa(E[0]), E[0].slice(-1) == "%" && (f *= 2.55), y = sa(E[1]), E[1].slice(-1) == "%" && (y *= 2.55), o = sa(E[2]), E[2].slice(-1) == "%" && (o *= 2.55), (E[0].slice(-3) == "deg" || E[0].slice(-1) == "\u00b0") && (f /= 360), a[1].toLowerCase().slice(0, 4) == "hsba" && (d = sa(E[3])), E[3] && E[3].slice(-1) == "%" && (d /= 100), b.hsb2rgb(f, y, o, d);
				if (a[6]) return E = a[6][c](Fa), f = sa(E[0]), E[0].slice(-1) == "%" && (f *= 2.55), y = sa(E[1]), E[1].slice(-1) == "%" && (y *= 2.55), o = sa(E[2]), E[2].slice(-1) == "%" && (o *= 2.55), (E[0].slice(-3) == "deg" || E[0].slice(-1) == "\u00b0") && (f /= 360), a[1].toLowerCase().slice(0, 4) == "hsla" && (d = sa(E[3])), E[3] && E[3].slice(-1) == "%" && (d /= 100), b.hsl2rgb(f, y, o, d);
				a = {
					r: f,
					g: y,
					b: o,
					toString: e
				};
				a.hex = "#" + (16777216 | o | y << 8 | f << 16).toString(16).slice(1);
				b.is(d, "finite") && (a.opacity = d);
				return a
			}
			return {
				r: -1,
				g: -1,
				b: -1,
				hex: "none",
				error: 1,
				toString: e
			}
		}, b);
		b.tintshade = ba(function(a, c) {
			var d = b.getRGB(a),
				f;
			f = 255;
			c < 0 && (c *= -1, f = 0);
			c > 1 && (c = 1);
			f = c === 0 ? d : {
				r: f - (f - d.r) * c,
				g: f - (f - d.g) * c,
				b: f - (f - d.b) * c,
				toString: e
			};
			f.hex = b.rgb(f.r, f.g, f.b);
			d.error && (f.error = d.error);
			"opacity" in d ? (f.rgba = "rgba(" + [f.r, f.g, f.b, d.opacity].join(",") + ")", f.opacity = d.opacity) : f.rgba = "rgb(" + [f.r, f.g, f.b].join(",") + ")";
			return f
		}, b);
		b.hsb = ba(function(a, c, d) {
			return b.hsb2rgb(a, c, d).hex
		});
		b.hsl = ba(function(a, c, d) {
			return b.hsl2rgb(a, c, d).hex
		});
		b.rgb = ba(function(a, b, c) {
			return "#" + (16777216 | c | b << 8 | a << 16).toString(16).slice(1)
		});
		b.getColor = function(a) {
			var a = this.getColor.start = this.getColor.start || {
				h: 0,
				s: 1,
				b: a || 0.75
			},
				b = this.hsb2rgb(a.h, a.s, a.b);
			a.h += 0.075;
			if (a.h > 1) a.h = 0, a.s -= 0.2, a.s <= 0 && (this.getColor.start = {
				h: 0,
				s: 1,
				b: a.b
			});
			return b.hex
		};
		b.getColor.reset = function() {
			delete this.start
		};
		b.parsePathString = function(a) {
			if (!a) return null;
			var c = la(a);
			if (c.arr) return za(c.arr);
			var d = {
				a: 7,
				c: 6,
				h: 1,
				l: 2,
				m: 2,
				r: 4,
				q: 4,
				s: 4,
				t: 2,
				v: 1,
				z: 0
			},
				e = [];
			b.is(a, ta) && b.is(a[0], ta) && (e = za(a));
			e.length || i(a).replace(fb, function(a, b, c) {
				var f = [],
					a = b.toLowerCase();
				c.replace(Za, function(a, b) {
					b && f.push(+b)
				});
				a == "m" && f.length > 2 && (e.push([b][C](f.splice(0, 2))), a = "l", b = b == "m" ? "l" : "L");
				if (a == "r") e.push([b][C](f));
				else for (; f.length >= d[a];) if (e.push([b][C](f.splice(0, d[a]))), !d[a]) break
			});
			e.toString = b._path2string;
			c.arr = za(e);
			return e
		};
		b.parseTransformString = ba(function(a) {
			if (!a) return null;
			var c = [];
			b.is(a, ta) && b.is(a[0], ta) && (c = za(a));
			c.length || i(a).replace(Sa, function(a, b, d) {
				var e = [];
				k.call(b);
				d.replace(Za, function(a, b) {
					b && e.push(+b)
				});
				c.push([b][C](e))
			});
			c.toString = b._path2string;
			return c
		});
		var la = function(a) {
				var b = la.ps = la.ps || {};
				b[a] ? b[a].sleep = 100 : b[a] = {
					sleep: 100
				};
				setTimeout(function() {
					for (var c in b) b[aa](c) && c != a && (b[c].sleep--, !b[c].sleep && delete b[c])
				});
				return b[a]
			};
		b.findDotsAtSegment = function(a, b, c, d, e, f, y, E, o) {
			var k = 1 - o,
				ha = J(k, 3),
				W = J(k, 2),
				p = o * o,
				u = p * o,
				t = ha * a + W * 3 * o * c + k * 3 * o * o * e + u * y,
				ha = ha * b + W * 3 * o * d + k * 3 * o * o * f + u * E,
				W = a + 2 * o * (c - a) + p * (e - 2 * c + a),
				u = b + 2 * o * (d - b) + p * (f - 2 * d + b),
				Q = c + 2 * o * (e - c) + p * (y - 2 * e + c),
				p = d + 2 * o * (f - d) + p * (E - 2 * f + d),
				a = k * a + o * c,
				b = k * b + o * d,
				e = k * e + o * y,
				f = k * f + o * E,
				E = 90 - q.atan2(W - Q, u - p) * 180 / X;
			(W > Q || u < p) && (E += 180);
			return {
				x: t,
				y: ha,
				m: {
					x: W,
					y: u
				},
				n: {
					x: Q,
					y: p
				},
				start: {
					x: a,
					y: b
				},
				end: {
					x: e,
					y: f
				},
				alpha: E
			}
		};
		b.bezierBBox = function(a, c, d, e, f, y, o, E) {
			b.is(a, "array") || (a = [a, c, d, e, f, y, o, E]);
			a = W.apply(null, a);
			return {
				x: a.min.x,
				y: a.min.y,
				x2: a.max.x,
				y2: a.max.y,
				width: a.max.x - a.min.x,
				height: a.max.y - a.min.y
			}
		};
		b.isPointInsideBBox = function(a, b, c) {
			return b >= a.x && b <= a.x2 && c >= a.y && c <= a.y2
		};
		b.isBBoxIntersect = function(a, c) {
			var d = b.isPointInsideBBox;
			return d(c, a.x, a.y) || d(c, a.x2, a.y) || d(c, a.x, a.y2) || d(c, a.x2, a.y2) || d(a, c.x, c.y) || d(a, c.x2, c.y) || d(a, c.x, c.y2) || d(a, c.x2, c.y2) || (a.x < c.x2 && a.x > c.x || c.x < a.x2 && c.x > a.x) && (a.y < c.y2 && a.y > c.y || c.y < a.y2 && c.y > a.y)
		};
		b.pathIntersection = function(a, b) {
			return fa(a, b)
		};
		b.pathIntersectionNumber = function(a, b) {
			return fa(a, b, 1)
		};
		b.isPointInsidePath = function(a, c, d) {
			var e = b.pathBBox(a);
			return b.isPointInsideBBox(e, c, d) && fa(a, [
				["M", c, d],
				["H", e.x2 + 10]
			], 1) % 2 == 1
		};
		b._removedFactory = function(a) {
			return function() {
				m("raphael.log", null, "Rapha\u00ebl: you are calling to method \u201c" + a + "\u201d of removed object", a)
			}
		};
		var Qa = b.pathBBox = function(a) {
				var b = la(a);
				if (b.bbox) return b.bbox;
				if (!a) return {
					x: 0,
					y: 0,
					width: 0,
					height: 0,
					x2: 0,
					y2: 0
				};
				for (var a = o(a), c = 0, d = 0, e = [], f = [], y, E = 0, k = a.length; E < k; E++) y = a[E], y[0] == "M" ? (c = y[1], d = y[2], e.push(c), f.push(d)) : (c = W(c, d, y[1], y[2], y[3], y[4], y[5], y[6]), e = e[C](c.min.x, c.max.x), f = f[C](c.min.y, c.max.y), c = y[5], d = y[6]);
				a = N[v](0, e);
				y = N[v](0, f);
				e = u[v](0, e);
				f = u[v](0, f);
				f = {
					x: a,
					y: y,
					x2: e,
					y2: f,
					width: e - a,
					height: f - y
				};
				b.bbox = g(f);
				return f
			},
			za = function(a) {
				a = g(a);
				a.toString = b._path2string;
				return a
			},
			A = b._pathToRelative = function(a) {
				var c = la(a);
				if (c.rel) return za(c.rel);
				if (!b.is(a, ta) || !b.is(a && a[0], ta)) a = b.parsePathString(a);
				var d = [],
					e = 0,
					f = 0,
					y = 0,
					o = 0,
					E = 0;
				a[0][0] == "M" && (e = a[0][1], f = a[0][2], y = e, o = f, E++, d.push(["M", e, f]));
				for (var ha = a.length; E < ha; E++) {
					var W = d[E] = [],
						q = a[E];
					if (q[0] != k.call(q[0])) switch (W[0] = k.call(q[0]), W[0]) {
					case "a":
						W[1] = q[1];
						W[2] = q[2];
						W[3] = q[3];
						W[4] = q[4];
						W[5] = q[5];
						W[6] = +(q[6] - e).toFixed(3);
						W[7] = +(q[7] - f).toFixed(3);
						break;
					case "v":
						W[1] = +(q[1] - f).toFixed(3);
						break;
					case "m":
						y = q[1], o = q[2];
					default:
						for (var p = 1, u = q.length; p < u; p++) W[p] = +(q[p] - (p % 2 ? e : f)).toFixed(3)
					} else {
						d[E] = [];
						q[0] == "m" && (y = q[1] + e, o = q[2] + f);
						W = 0;
						for (p = q.length; W < p; W++) d[E][W] = q[W]
					}
					q = d[E].length;
					switch (d[E][0]) {
					case "z":
						e = y;
						f = o;
						break;
					case "h":
						e += +d[E][q - 1];
						break;
					case "v":
						f += +d[E][q - 1];
						break;
					default:
						e += +d[E][q - 2], f += +d[E][q - 1]
					}
				}
				d.toString = b._path2string;
				c.rel = za(d);
				return d
			},
			I = b._pathToAbsolute = function(a) {
				var c = la(a),
					d;
				if (c.abs) return za(c.abs);
				if (!b.is(a, ta) || !b.is(a && a[0], ta)) a = b.parsePathString(a);
				if (!a || !a.length) return d = ["M", 0, 0], d.toString = b._path2string, d;
				var e = 0,
					f = 0,
					y = 0,
					E = 0,
					o = 0;
				d = [];
				a[0][0] == "M" && (e = +a[0][1], f = +a[0][2], y = e, E = f, o++, d[0] = ["M", e, f]);
				for (var k = a.length == 3 && a[0][0] == "M" && a[1][0].toUpperCase() == "R" && a[2][0].toUpperCase() == "Z", ha, q = o, W = a.length; q < W; q++) {
					d.push(o = []);
					ha = a[q];
					if (ha[0] != Na.call(ha[0])) switch (o[0] = Na.call(ha[0]), o[0]) {
					case "A":
						o[1] = ha[1];
						o[2] = ha[2];
						o[3] = ha[3];
						o[4] = ha[4];
						o[5] = ha[5];
						o[6] = +(ha[6] + e);
						o[7] = +(ha[7] + f);
						break;
					case "V":
						o[1] = +ha[1] + f;
						break;
					case "H":
						o[1] = +ha[1] + e;
						break;
					case "R":
						for (var p = [e, f][C](ha.slice(1)), u = 2, t = p.length; u < t; u++) p[u] = +p[u] + e, p[++u] = +p[u] + f;
						d.pop();
						d = d[C](r(p, k));
						break;
					case "M":
						y = +ha[1] + e, E = +ha[2] + f;
					default:
						u = 1;
						for (t = ha.length; u < t; u++) o[u] = +ha[u] + (u % 2 ? e : f)
					} else if (ha[0] == "R") p = [e, f][C](ha.slice(1)), d.pop(), d = d[C](r(p, k)), o = ["R"][C](ha.slice(-2));
					else {
						p = 0;
						for (u = ha.length; p < u; p++) o[p] = ha[p]
					}
					switch (o[0]) {
					case "Z":
						e = y;
						f = E;
						break;
					case "H":
						e = o[1];
						break;
					case "V":
						f = o[1];
						break;
					case "M":
						y = o[o.length - 2], E = o[o.length - 1];
					default:
						e = o[o.length - 2], f = o[o.length - 1]
					}
				}
				d.toString = b._path2string;
				c.abs = za(d);
				return d
			},
			E = function(a, b, c, d, e, f) {
				var y = 1 / 3,
					o = 2 / 3;
				return [y * a + o * c, y * b + o * d, y * e + o * c, y * f + o * d, e, f]
			},
			y = function(a, b, d, e, f, o, E, k, ha, W) {
				var p = X * 120 / 180,
					u = Ca * (+f || 0),
					t = [],
					Q, ja = ba(function(a, b, c) {
						var d = a * Ja(c) - b * pa(c),
							a = a * pa(c) + b * Ja(c);
						return {
							x: d,
							y: a
						}
					});
				if (W) z = W[0], Q = W[1], o = W[2], Ea = W[3];
				else {
					Q = ja(a, b, -u);
					a = Q.x;
					b = Q.y;
					Q = ja(k, ha, -u);
					k = Q.x;
					ha = Q.y;
					Ja(Ca * f);
					pa(Ca * f);
					Q = (a - k) / 2;
					z = (b - ha) / 2;
					Ea = Q * Q / (d * d) + z * z / (e * e);
					Ea > 1 && (Ea = va(Ea), d *= Ea, e *= Ea);
					var Ea = d * d,
						oa = e * e,
						Ea = (o == E ? -1 : 1) * va(ca((Ea * oa - Ea * z * z - oa * Q * Q) / (Ea * z * z + oa * Q * Q))),
						o = Ea * d * z / e + (a + k) / 2,
						Ea = Ea * -e * Q / d + (b + ha) / 2,
						z = q.asin(((b - Ea) / e).toFixed(9));
					Q = q.asin(((ha - Ea) / e).toFixed(9));
					z = a < o ? X - z : z;
					Q = k < o ? X - Q : Q;
					z < 0 && (z = X * 2 + z);
					Q < 0 && (Q = X * 2 + Q);
					E && z > Q && (z -= X * 2);
					!E && Q > z && (Q -= X * 2)
				}
				if (ca(Q - z) > p) {
					var t = Q,
						oa = k,
						j = ha;
					Q = z + p * (E && Q > z ? 1 : -1);
					k = o + d * Ja(Q);
					ha = Ea + e * pa(Q);
					t = y(k, ha, d, e, f, 0, E, oa, j, [Q, t, o, Ea])
				}
				o = Q - z;
				f = Ja(z);
				p = pa(z);
				E = Ja(Q);
				Q = pa(Q);
				o = q.tan(o / 4);
				d = 4 / 3 * d * o;
				o *= 4 / 3 * e;
				e = [a, b];
				a = [a + d * p, b - o * f];
				b = [k + d * Q, ha - o * E];
				k = [k, ha];
				a[0] = 2 * e[0] - a[0];
				a[1] = 2 * e[1] - a[1];
				if (W) return [a, b, k][C](t);
				else {
					t = [a, b, k][C](t).join()[c](",");
					W = [];
					k = 0;
					for (ha = t.length; k < ha; k++) W[k] = k % 2 ? ja(t[k - 1], t[k], u).y : ja(t[k], t[k + 1], u).x;
					return W
				}
			},
			ha = function(a, b, c, d, e, f, o, y, E) {
				var k = 1 - E;
				return {
					x: J(k, 3) * a + J(k, 2) * 3 * E * c + k * 3 * E * E * e + J(E, 3) * o,
					y: J(k, 3) * b + J(k, 2) * 3 * E * d + k * 3 * E * E * f + J(E, 3) * y
				}
			},
			W = ba(function(a, b, c, d, e, f, o, y) {
				var E = e - 2 * c + a - (o - 2 * e + c),
					k = 2 * (c - a) - 2 * (e - c),
					W = a - c,
					q = (-k + va(k * k - 4 * E * W)) / 2 / E,
					E = (-k - va(k * k - 4 * E * W)) / 2 / E,
					p = [b, y],
					Q = [a, o];
				ca(q) > "1e12" && (q = 0.5);
				ca(E) > "1e12" && (E = 0.5);
				q > 0 && q < 1 && (q = ha(a, b, c, d, e, f, o, y, q), Q.push(q.x), p.push(q.y));
				E > 0 && E < 1 && (q = ha(a, b, c, d, e, f, o, y, E), Q.push(q.x), p.push(q.y));
				E = f - 2 * d + b - (y - 2 * f + d);
				k = 2 * (d - b) - 2 * (f - d);
				W = b - d;
				q = (-k + va(k * k - 4 * E * W)) / 2 / E;
				E = (-k - va(k * k - 4 * E * W)) / 2 / E;
				ca(q) > "1e12" && (q = 0.5);
				ca(E) > "1e12" && (E = 0.5);
				q > 0 && q < 1 && (q = ha(a, b, c, d, e, f, o, y, q), Q.push(q.x), p.push(q.y));
				E > 0 && E < 1 && (q = ha(a, b, c, d, e, f, o, y, E), Q.push(q.x), p.push(q.y));
				return {
					min: {
						x: N[v](0, Q),
						y: N[v](0, p)
					},
					max: {
						x: u[v](0, Q),
						y: u[v](0, p)
					}
				}
			}),
			o = b._path2curve = ba(function(a, b) {
				var c = !b && la(a);
				if (!b && c.curve) return za(c.curve);
				var d = I(a),
					e = b && I(b),
					f = {
						x: 0,
						y: 0,
						bx: 0,
						by: 0,
						X: 0,
						Y: 0,
						qx: null,
						qy: null
					},
					o = {
						x: 0,
						y: 0,
						bx: 0,
						by: 0,
						X: 0,
						Y: 0,
						qx: null,
						qy: null
					},
					k = function(a, b) {
						var c, d;
						if (!a) return ["C", b.x, b.y, b.x, b.y, b.x, b.y];
						!(a[0] in {
							T: 1,
							Q: 1
						}) && (b.qx = b.qy = null);
						switch (a[0]) {
						case "M":
							b.X = a[1];
							b.Y = a[2];
							break;
						case "A":
							a = ["C"][C](y[v](0, [b.x, b.y][C](a.slice(1))));
							break;
						case "S":
							c = b.x + (b.x - (b.bx || b.x));
							d = b.y + (b.y - (b.by || b.y));
							a = ["C", c, d][C](a.slice(1));
							break;
						case "T":
							b.qx = b.x + (b.x - (b.qx || b.x));
							b.qy = b.y + (b.y - (b.qy || b.y));
							a = ["C"][C](E(b.x, b.y, b.qx, b.qy, a[1], a[2]));
							break;
						case "Q":
							b.qx = a[1];
							b.qy = a[2];
							a = ["C"][C](E(b.x, b.y, a[1], a[2], a[3], a[4]));
							break;
						case "L":
							a = ["C"][C]([b.x, b.y, a[1], a[2], a[1], a[2]]);
							break;
						case "H":
							a = ["C"][C]([b.x, b.y, a[1], b.y, a[1], b.y]);
							break;
						case "V":
							a = ["C"][C]([b.x, b.y, b.x, a[1], b.x, a[1]]);
							break;
						case "Z":
							a = ["C"][C]([b.x, b.y, b.X, b.Y, b.X, b.Y])
						}
						return a
					},
					ha = function(a, b) {
						if (a[b].length > 7) {
							a[b].shift();
							for (var c = a[b]; c.length;) a.splice(b++, 0, ["C"][C](c.splice(0, 6)));
							a.splice(b, 1);
							p = u(d.length, e && e.length || 0)
						}
					},
					q = function(a, b, c, f, o) {
						if (a && b && a[o][0] == "M" && b[o][0] != "M") b.splice(o, 0, ["M", f.x, f.y]), c.bx = 0, c.by = 0, c.x = a[o][1], c.y = a[o][2], p = u(d.length, e && e.length || 0)
					},
					W = 0,
					p = u(d.length, e && e.length || 0);
				for (; W < p; W++) {
					d[W] = k(d[W], f);
					ha(d, W);
					e && (e[W] = k(e[W], o));
					e && ha(e, W);
					q(d, e, f, o, W);
					q(e, d, o, f, W);
					var Q = d[W],
						t = e && e[W],
						ja = Q.length,
						Ea = e && t.length;
					f.x = Q[ja - 2];
					f.y = Q[ja - 1];
					f.bx = sa(Q[ja - 4]) || f.x;
					f.by = sa(Q[ja - 3]) || f.y;
					o.bx = e && (sa(t[Ea - 4]) || o.x);
					o.by = e && (sa(t[Ea - 3]) || o.y);
					o.x = e && t[Ea - 2];
					o.y = e && t[Ea - 1]
				}
				if (!e) c.curve = za(d);
				return e ? [d, e] : d
			}, null, za);
		b._parseDots = ba(function(a) {
			for (var c = [], d = 0, e = a.length; d < e; d++) {
				var f = {},
					o = a[d].match(/^([^:]*):?([\d\.]*)/);
				f.color = b.getRGB(o[1]);
				if (f.color.error) return null;
				f.opacity = f.color.opacity;
				f.color = f.color.hex;
				o[2] && (f.offset = o[2] + "%");
				c.push(f)
			}
			d = 1;
			for (e = c.length - 1; d < e; d++) if (!c[d].offset) {
				a = sa(c[d - 1].offset || 0);
				o = 0;
				for (f = d + 1; f < e; f++) if (c[f].offset) {
					o = c[f].offset;
					break
				}
				o || (o = 100, f = e);
				o = sa(o);
				for (o = (o - a) / (f - d + 1); d < f; d++) a += o, c[d].offset = a + "%"
			}
			return c
		});
		var Q = b._tear = function(a, b) {
				a == b.top && (b.top = a.prev);
				a == b.bottom && (b.bottom = a.next);
				a.next && (a.next.prev = a.prev);
				a.prev && (a.prev.next = a.next)
			};
		b._tofront = function(a, b) {
			if (b.top === a) return !1;
			Q(a, b);
			a.next = null;
			a.prev = b.top;
			b.top.next = a;
			b.top = a;
			return !0
		};
		b._toback = function(a, b) {
			if (b.bottom === a) return !1;
			Q(a, b);
			a.next = b.bottom;
			a.prev = null;
			b.bottom.prev = a;
			b.bottom = a;
			return !0
		};
		b._insertafter = function(a, b, c, d) {
			Q(a, c);
			a.parent = d;
			b === d.top && (d.top = a);
			b.next && (b.next.prev = a);
			a.next = b.next;
			a.prev = b;
			b.next = a
		};
		b._insertbefore = function(a, b, c, d) {
			Q(a, c);
			a.parent = d;
			b === d.bottom && (d.bottom = a);
			b.prev && (b.prev.next = a);
			a.prev = b.prev;
			b.prev = a;
			a.next = b
		};
		var ja = b.toMatrix = function(a, b) {
				var c = Qa(a),
					d = {
						_: {
							transform: ""
						},
						getBBox: function() {
							return c
						}
					};
				gb(d, b);
				return d.matrix
			};
		b.transformPath = function(a, b) {
			return da(a, ja(a, b))
		};
		var gb = b._extractTransform = function(a, c) {
				if (c == null) return a._.transform;
				var c = i(c).replace(/\.{3}|\u2026/g, a._.transform || ""),
					d = b.parseTransformString(c),
					e = 0,
					f = 0,
					o = 0,
					E = 1,
					y = 1,
					k = a._,
					o = new s;
				k.transform = d || [];
				if (d) for (var f = 0, ha = d.length; f < ha; f++) {
					var q = d[f],
						W = q.length,
						p = i(q[0]).toLowerCase(),
						u = q[0] != p,
						Q = u ? o.invert() : 0,
						t;
					p == "t" && W == 3 ? u ? (W = Q.x(0, 0), p = Q.y(0, 0), u = Q.x(q[1], q[2]), Q = Q.y(q[1], q[2]), o.translate(u - W, Q - p)) : o.translate(q[1], q[2]) : p == "r" ? W == 2 ? (t = t || a.getBBox(1), o.rotate(q[1], t.x + t.width / 2, t.y + t.height / 2), e += q[1]) : W == 4 && (u ? (u = Q.x(q[2], q[3]), Q = Q.y(q[2], q[3]), o.rotate(q[1], u, Q)) : o.rotate(q[1], q[2], q[3]), e += q[1]) : p == "s" ? W == 2 || W == 3 ? (t = t || a.getBBox(1), o.scale(q[1], q[W - 1], t.x + t.width / 2, t.y + t.height / 2), E *= q[1], y *= q[W - 1]) : W == 5 && (u ? (u = Q.x(q[3], q[4]), Q = Q.y(q[3], q[4]), o.scale(q[1], q[2], u, Q)) : o.scale(q[1], q[2], q[3], q[4]), E *= q[1], y *= q[2]) : p == "m" && W == 7 && o.add(q[1], q[2], q[3], q[4], q[5], q[6]);
					k.dirtyT = 1;
					a.matrix = o
				}
				a.matrix = o;
				k.sx = E;
				k.sy = y;
				k.deg = e;
				k.dx = f = o.e;
				k.dy = o = o.f;
				E == 1 && y == 1 && !e && k.bbox ? (k.bbox.x += +f, k.bbox.y += +o) : k.dirtyT = 1
			},
			Ea = function(a) {
				var b = a[0];
				switch (b.toLowerCase()) {
				case "t":
					return [b, 0, 0];
				case "m":
					return [b, 1, 0, 0, 1, 0, 0];
				case "r":
					return a.length == 4 ? [b, 0, a[2], a[3]] : [b, 0];
				case "s":
					return a.length == 5 ? [b, 1, 1, a[3], a[4]] : a.length == 3 ? [b, 1, 1] : [b, 1]
				}
			},
			na = b._equaliseTransform = function(a, c) {
				for (var c = i(c).replace(/\.{3}|\u2026/g, a), a = b.parseTransformString(a) || [], c = b.parseTransformString(c) || [], d = u(a.length, c.length), e = [], f = [], o = 0, E, y, k, q; o < d; o++) {
					k = a[o] || Ea(c[o]);
					q = c[o] || Ea(k);
					if (k[0] != q[0] || k[0].toLowerCase() == "r" && (k[2] != q[2] || k[3] != q[3]) || k[0].toLowerCase() == "s" && (k[3] != q[3] || k[4] != q[4])) return;
					e[o] = [];
					f[o] = [];
					E = 0;
					for (y = u(k.length, q.length); E < y; E++) E in k && (e[o][E] = k[E]), E in q && (f[o][E] = q[E])
				}
				return {
					from: e,
					to: f
				}
			};
		b._getContainer = function(a, c, d, e) {
			var f;
			f = e == null && !b.is(a, "object") ? D.doc.getElementById(a) : a;
			if (f != null) {
				if (f.tagName) return c == null ? {
					container: f,
					width: f.style.pixelWidth || f.offsetWidth,
					height: f.style.pixelHeight || f.offsetHeight
				} : {
					container: f,
					width: c,
					height: d
				};
				return {
					container: 1,
					x: a,
					y: c,
					width: d,
					height: e
				}
			}
		};
		b.pathToRelative = A;
		b._engine = {};
		b.path2curve = o;
		b.matrix = function(a, b, c, d, e, f) {
			return new s(a, b, c, d, e, f)
		};
		(function(a) {
			function d(a) {
				return a[0] * a[0] + a[1] * a[1]
			}
			function e(a) {
				var b = va(d(a));
				a[0] && (a[0] /= b);
				a[1] && (a[1] /= b)
			}
			a.add = function(a, b, c, d, e, f) {
				var o = [
					[],
					[],
					[]
				],
					E = [
						[this.a, this.c, this.e],
						[this.b, this.d, this.f],
						[0, 0, 1]
					],
					b = [
						[a, c, e],
						[b, d, f],
						[0, 0, 1]
					];
				a && a instanceof s && (b = [
					[a.a, a.c, a.e],
					[a.b, a.d, a.f],
					[0, 0, 1]
				]);
				for (a = 0; a < 3; a++) for (c = 0; c < 3; c++) {
					for (d = e = 0; d < 3; d++) e += E[a][d] * b[d][c];
					o[a][c] = e
				}
				this.a = o[0][0];
				this.b = o[1][0];
				this.c = o[0][1];
				this.d = o[1][1];
				this.e = o[0][2];
				this.f = o[1][2]
			};
			a.invert = function() {
				var a = this.a * this.d - this.b * this.c;
				return new s(this.d / a, -this.b / a, -this.c / a, this.a / a, (this.c * this.f - this.d * this.e) / a, (this.b * this.e - this.a * this.f) / a)
			};
			a.clone = function() {
				return new s(this.a, this.b, this.c, this.d, this.e, this.f)
			};
			a.translate = function(a, b) {
				this.add(1, 0, 0, 1, a, b)
			};
			a.scale = function(a, b, c, d) {
				b == null && (b = a);
				(c || d) && this.add(1, 0, 0, 1, c, d);
				this.add(a, 0, 0, b, 0, 0);
				(c || d) && this.add(1, 0, 0, 1, -c, -d)
			};
			a.rotate = function(a, c, d) {
				var a = b.rad(a),
					c = c || 0,
					d = d || 0,
					e = +Ja(a).toFixed(9),
					a = +pa(a).toFixed(9);
				this.add(e, a, -a, e, c, d);
				this.add(1, 0, 0, 1, -c, -d)
			};
			a.x = function(a, b) {
				return a * this.a + b * this.c + this.e
			};
			a.y = function(a, b) {
				return a * this.b + b * this.d + this.f
			};
			a.get = function(a) {
				return +this[i.fromCharCode(97 + a)].toFixed(4)
			};
			a.toString = function() {
				return b.svg ? "matrix(" + [this.get(0), this.get(1), this.get(2), this.get(3), this.get(4), this.get(5)].join() + ")" : [this.get(0), this.get(2), this.get(1), this.get(3), 0, 0].join()
			};
			a.toMatrixString = function() {
				return "matrix(" + [this.get(0), this.get(1), this.get(2), this.get(3), this.get(4), this.get(5)].join() + ")"
			};
			a.toFilter = function() {
				return "progid:DXImageTransform.Microsoft.Matrix(M11=" + this.get(0) + ", M12=" + this.get(2) + ", M21=" + this.get(1) + ", M22=" + this.get(3) + ", Dx=" + this.get(4) + ", Dy=" + this.get(5) + ", sizingmethod='auto expand')"
			};
			a.offset = function() {
				return [this.e.toFixed(4), this.f.toFixed(4)]
			};
			a.split = function() {
				var a = {};
				a.dx = this.e;
				a.dy = this.f;
				var c = [
					[this.a, this.c],
					[this.b, this.d]
				];
				a.scalex = va(d(c[0]));
				e(c[0]);
				a.shear = c[0][0] * c[1][0] + c[0][1] * c[1][1];
				c[1] = [c[1][0] - c[0][0] * a.shear, c[1][1] - c[0][1] * a.shear];
				a.scaley = va(d(c[1]));
				e(c[1]);
				a.shear /= a.scaley;
				var f = -c[0][1],
					c = c[1][1];
				if (c < 0) {
					if (a.rotate = b.deg(q.acos(c)), f < 0) a.rotate = 360 - a.rotate
				} else a.rotate = b.deg(q.asin(f));
				a.isSimple = !+a.shear.toFixed(9) && (a.scalex.toFixed(9) == a.scaley.toFixed(9) || !a.rotate);
				a.isSuperSimple = !+a.shear.toFixed(9) && a.scalex.toFixed(9) == a.scaley.toFixed(9) && !a.rotate;
				a.noRotation = !+a.shear.toFixed(9) && !a.rotate;
				return a
			};
			a.toTransformString = function(a) {
				a = a || this[c]();
				return a.isSimple ? (a.scalex = +a.scalex.toFixed(4), a.scaley = +a.scaley.toFixed(4), a.rotate = +a.rotate.toFixed(4), (a.dx || a.dy ? "t" + [a.dx, a.dy] : "") + (a.scalex != 1 || a.scaley != 1 ? "s" + [a.scalex, a.scaley, 0, 0] : "") + (a.rotate ? "r" + [a.rotate, 0, 0] : "")) : "m" + [this.get(0), this.get(1), this.get(2), this.get(3), this.get(4), this.get(5)]
			}
		})(s.prototype);
		A = navigator.userAgent.match(/Version\/(.*?)\s/) || navigator.userAgent.match(/Chrome\/(\d+)/);
		R.safari = navigator.vendor == "Apple Computer, Inc." && (A && A[1] < 4 || navigator.platform.slice(0, 2) == "iP") || navigator.vendor == "Google Inc." && A && A[1] < 8 ?
		function() {
			var a = this.rect(-99, -99, this.width + 99, this.height + 99).attr({
				stroke: "none"
			});
			setTimeout(function() {
				a.remove()
			});
			return !0
		} : t;
		for (var oa = function() {
				this.returnValue = !1
			}, jb = function() {
				return this.originalEvent.preventDefault()
			}, cb = function() {
				this.cancelBubble = !0
			}, Pa = function() {
				return this.originalEvent.stopPropagation()
			}, ab = b.addEvent = function() {
				if (D.doc.addEventListener) return function(b, c, d, e) {
					var o = f && a[c] ? a[c] : c,
						E = function(o) {
							var E = D.doc.documentElement.scrollTop || D.doc.body.scrollTop,
								y = D.doc.documentElement.scrollLeft || D.doc.body.scrollLeft;
							if (f && a[aa](c)) for (var k = 0, q = o.targetTouches && o.targetTouches.length; k < q; k++) if (o.targetTouches[k].target == b) {
								q = o;
								o = o.targetTouches[k];
								o.originalEvent = q;
								o.preventDefault = jb;
								o.stopPropagation = Pa;
								break
							}
							return d.call(e, o, o.clientX + y, o.clientY + E)
						};
					b.addEventListener(o, E, !1);
					return function() {
						b.removeEventListener(o, E, !1);
						return !0
					}
				};
				else if (D.doc.attachEvent) return function(a, b, c, d) {
					var e = function(a) {
							var a = a || D.win.event,
								b = a.clientX + (D.doc.documentElement.scrollLeft || D.doc.body.scrollLeft),
								e = a.clientY + (D.doc.documentElement.scrollTop || D.doc.body.scrollTop);
							a.preventDefault = a.preventDefault || oa;
							a.stopPropagation = a.stopPropagation || cb;
							return c.call(d, a, b, e)
						};
					a.attachEvent("on" + b, e);
					return function() {
						a.detachEvent("on" + b, e);
						return !0
					}
				}
			}(), db = [], Xa = function(a) {
				for (var b = a.clientX, c = a.clientY, d = D.doc.documentElement.scrollTop || D.doc.body.scrollTop, e = D.doc.documentElement.scrollLeft || D.doc.body.scrollLeft, o, E = db.length; E--;) {
					o = db[E];
					if (f) for (var y = a.touches.length, k; y--;) {
						if (k = a.touches[y], k.identifier == o.el._drag.id) {
							b = k.clientX;
							c = k.clientY;
							(a.originalEvent ? a.originalEvent : a).preventDefault();
							break
						}
					} else a.preventDefault();
					var y = o.el.node,
						q = y.nextSibling,
						ha = y.parentNode,
						W = y.style.display;
					D.win.opera && ha.removeChild(y);
					y.style.display = "none";
					k = o.el.paper.getElementByPoint(b, c);
					y.style.display = W;
					D.win.opera && (q ? ha.insertBefore(y, q) : ha.appendChild(y));
					k && m("raphael.drag.over." + o.el.id, o.el, k);
					b += e;
					c += d;
					m("raphael.drag.move." + o.el.id, o.move_scope || o.el, b - o.el._drag.x, c - o.el._drag.y, b, c, a)
				}
			}, hb = function(a) {
				b.unmousemove(Xa).unmouseup(hb);
				for (var c = db.length, d; c--;) d = db[c], d.el._drag = {}, m("raphael.drag.end." + d.el.id, d.end_scope || d.start_scope || d.move_scope || d.el, a);
				db = []
			}, Ga = b.el = {}, t = d.length; t--;)(function(a) {
			b[a] = Ga[a] = function(c, d) {
				if (b.is(c, "function")) this.events = this.events || [], this.events.push({
					name: a,
					f: c,
					unbind: ab(this.shape || this.node || D.doc, a, c, d || this)
				});
				return this
			};
			b["un" + a] = Ga["un" + a] = function(b) {
				for (var c = this.events || [], d = c.length; d--;) if (c[d].name == a && c[d].f == b) {
					c[d].unbind();
					c.splice(d, 1);
					!c.length && delete this.events;
					break
				}
				return this
			}
		})(d[t]);
		Ga.data = function(a, c) {
			var d = p[this.id] = p[this.id] || {};
			if (arguments.length == 1) {
				if (b.is(a, "object")) {
					for (var e in a) a[aa](e) && this.data(e, a[e]);
					return this
				}
				m("raphael.data.get." + this.id, this, d[a], a);
				return d[a]
			}
			d[a] = c;
			m("raphael.data.set." + this.id, this, c, a);
			return this
		};
		Ga.removeData = function(a) {
			a == null ? p[this.id] = {} : p[this.id] && delete p[this.id][a];
			return this
		};
		var L = [],
			Oa = function() {
				this.untrack = ab(D.doc, "mouseup", pb, this)
			},
			pb = function() {
				this.untrack();
				this.untrack = null;
				return this.fn && this.fn.apply(this.scope || this.el, arguments)
			};
		Ga.mouseup = function(a, c, d) {
			if (!d) return b.mouseup.apply(this, arguments);
			L.push(d = {
				el: this,
				fn: a,
				scope: c
			});
			d.unbind = ab(this.shape || this.node || D.doc, "mousedown", Oa, d);
			return this
		};
		Ga.unmouseup = function(a) {
			for (var c = L.length, d; c--;) L[c].el === this && L[c].fn === a && (d = L[c], d.unbind(), d.untrack && d.untrack(), L.splice(c, 1));
			return d ? this : b.unmouseup.apply(this, arguments)
		};
		Ga.hover = function(a, b, c, d) {
			return this.mouseover(a, c).mouseout(b, d || c)
		};
		Ga.unhover = function(a, b) {
			return this.unmouseover(a).unmouseout(b)
		};
		var ib = [];
		Ga.drag = function(a, c, d, e, f, o) {
			function E(y) {
				(y.originalEvent || y).preventDefault();
				var k = D.doc.documentElement.scrollTop || D.doc.body.scrollTop,
					q = D.doc.documentElement.scrollLeft || D.doc.body.scrollLeft;
				this._drag.x = y.clientX + q;
				this._drag.y = y.clientY + k;
				this._drag.id = y.identifier;
				!db.length && b.mousemove(Xa).mouseup(hb);
				db.push({
					el: this,
					move_scope: e,
					start_scope: f,
					end_scope: o
				});
				c && m.on("raphael.drag.start." + this.id, c);
				a && m.on("raphael.drag.move." + this.id, a);
				d && m.on("raphael.drag.end." + this.id, d);
				m("raphael.drag.start." + this.id, f || e || this, y.clientX + q, y.clientY + k, y)
			}
			this._drag = {};
			ib.push({
				el: this,
				start: E
			});
			this.mousedown(E);
			return this
		};
		Ga.onDragOver = function(a) {
			a ? m.on("raphael.drag.over." + this.id, a) : m.unbind("raphael.drag.over." + this.id)
		};
		Ga.undrag = function() {
			for (var a = ib.length; a--;) ib[a].el == this && (this.unmousedown(ib[a].start), ib.splice(a, 1), m.unbind("raphael.drag.*." + this.id));
			!ib.length && b.unmousemove(Xa).unmouseup(hb)
		};
		Ga.follow = function(a, c, d) {
			if (a.removed || a.constructor !== b.el.constructor) return this;
			a.followers.push({
				el: this,
				stalk: d = {
					before: "insertBefore",
					after: "insertAfter"
				}[d],
				cb: c
			});
			d && this[d](a);
			return this
		};
		Ga.unfollow = function(a) {
			if (a.removed || a.constructor !== b.el.constructor) return this;
			for (var c = 0, d = a.followers.length; c < d; c++) if (a.followers[c].el === this) {
				a.followers.splice(c, 1);
				break
			}
			return this
		};
		var La = Array.prototype.splice;
		R.group = function() {
			var a;
			a = arguments;
			var c = a.length - 1,
				d = a[c];
			d && d.constructor === b.el.constructor ? (a[c] = void 0, La.call(a, c, 1)) : d = void 0;
			a = b._engine.group(this, a[0], d);
			this.__set__ && this.__set__.push(a);
			return a
		};
		R.circle = function() {
			var a;
			a = arguments;
			var c = a.length - 1,
				d = a[c];
			d && d.constructor === b.el.constructor ? (a[c] = void 0, La.call(a, c, 1)) : d = void 0;
			a = b._engine.circle(this, a[0] || 0, a[1] || 0, a[2] || 0, d);
			this.__set__ && this.__set__.push(a);
			return a
		};
		R.rect = function() {
			var a;
			a = arguments;
			var c = a.length - 1,
				d = a[c];
			d && d.constructor === b.el.constructor ? (a[c] = void 0, La.call(a, c, 1)) : d = void 0;
			a = b._engine.rect(this, a[0] || 0, a[1] || 0, a[2] || 0, a[3] || 0, a[4] || 0, d);
			this.__set__ && this.__set__.push(a);
			return a
		};
		R.ellipse = function() {
			var a;
			a = arguments;
			var c = a.length - 1,
				d = a[c];
			d && d.constructor === b.el.constructor ? (a[c] = void 0, La.call(a, c, 1)) : d = void 0;
			a = b._engine.ellipse(this, a[0] || 0, a[1] || 0, a[2] || 0, a[3] || 0, d);
			this.__set__ && this.__set__.push(a);
			return a
		};
		R.path = function() {
			var a, c = arguments,
				d = c.length - 1;
			(a = c[d]) && a.constructor === b.el.constructor ? (c[d] = void 0, La.call(c, d, 1)) : a = void 0;
			(c = c[0]) && !b.is(c, "string") && b.is(c[0], ta);
			a = b._engine.path(b.format[v](b, arguments), this, a);
			this.__set__ && this.__set__.push(a);
			return a
		};
		R.image = function() {
			var a;
			a = arguments;
			var c = a.length - 1,
				d = a[c];
			d && d.constructor === b.el.constructor ? (a[c] = void 0, La.call(a, c, 1)) : d = void 0;
			a = b._engine.image(this, a[0] || "about:blank", a[1] || 0, a[2] || 0, a[3] || 0, a[4] || 0, d);
			this.__set__ && this.__set__.push(a);
			return a
		};
		R.text = function() {
			var a;
			a = arguments;
			var c = a.length - 1,
				d = a[c];
			d && d.constructor === b.el.constructor ? (a[c] = void 0, La.call(a, c, 1)) : d = void 0;
			a = b._engine.text(this, a[0] || 0, a[1] || 0, i(a[2] || ""), d);
			this.__set__ && this.__set__.push(a);
			return a
		};
		R.set = function(a) {
			!b.is(a, "array") && (a = Array.prototype.splice.call(arguments, 0, arguments.length));
			var c = new Ua(a);
			this.__set__ && this.__set__.push(c);
			return c
		};
		R.setStart = function(a) {
			this.__set__ = a || this.set()
		};
		R.setFinish = function() {
			var a = this.__set__;
			delete this.__set__;
			return a
		};
		R.setSize = function(a, c) {
			return b._engine.setSize.call(this, a, c)
		};
		R.setViewBox = function(a, c, d, e, f) {
			return b._engine.setViewBox.call(this, a, c, d, e, f)
		};
		R.top = R.bottom = null;
		R.raphael = b;
		R.getElementByPoint = function(a, b) {
			var c = this.canvas,
				d = D.doc.elementFromPoint(a, b);
			if (D.win.opera && d.tagName == "svg") {
				var e;
				e = c.getBoundingClientRect();
				var f = c.ownerDocument,
					o = f.body,
					f = f.documentElement;
				e = {
					y: e.top + (D.win.pageYOffset || f.scrollTop || o.scrollTop) - (f.clientTop || o.clientTop || 0),
					x: e.left + (D.win.pageXOffset || f.scrollLeft || o.scrollLeft) - (f.clientLeft || o.clientLeft || 0)
				};
				o = c.createSVGRect();
				o.x = a - e.x;
				o.y = b - e.y;
				o.width = o.height = 1;
				e = c.getIntersectionList(o, null);
				e.length && (d = e[e.length - 1])
			}
			if (!d) return null;
			for (; d.parentNode && d != c.parentNode && !d.raphael;) d = d.parentNode;
			d == this.canvas.parentNode && (d = c);
			return d = d && d.raphael ? this.getById(d.raphaelid) : null
		};
		R.getById = function(a) {
			for (var b = this.bottom; b;) {
				if (b.id == a) return b;
				b = b.next
			}
			return null
		};
		R.forEach = function(a, b) {
			for (var c = this.bottom; c;) {
				if (a.call(b, c) === !1) break;
				c = c.next
			}
			return this
		};
		R.getElementsByPoint = function(a, b) {
			var c = this.set();
			this.forEach(function(d) {
				d.isPointInside(a, b) && c.push(d)
			});
			return c
		};
		Ga.isPointInside = function(a, c) {
			var d = this.realPath = this.realPath || qa[this.type](this);
			return b.isPointInsidePath(d, a, c)
		};
		Ga.getBBox = function(a) {
			if (this.removed) return {};
			var b = this._;
			if (a) {
				if (b.dirty || !b.bboxwt) this.realPath = qa[this.type](this), b.bboxwt = Qa(this.realPath), b.bboxwt.toString = ka, b.dirty = 0;
				return b.bboxwt
			}
			if (b.dirty || b.dirtyT || !b.bbox) {
				if (b.dirty || !this.realPath) b.bboxwt = 0, this.realPath = qa[this.type](this);
				b.bbox = Qa(da(this.realPath, this.matrix));
				b.bbox.toString = ka;
				b.dirty = b.dirtyT = 0
			}
			return b.bbox
		};
		Ga.clone = function() {
			if (this.removed) return null;
			var a = this.paper[this.type]().attr(this.attr());
			this.__set__ && this.__set__.push(a);
			return a
		};
		Ga.glow = function(a) {
			if (this.type == "text") return null;
			for (var a = a || {}, a = {
				width: (a.width || 10) + (+this.attr("stroke-width") || 1),
				fill: a.fill || !1,
				opacity: a.opacity || 0.5,
				offsetx: a.offsetx || 0,
				offsety: a.offsety || 0,
				color: a.color || "#000"
			}, b = a.width / 2, c = this.paper, d = c.set(), e = this.realPath || qa[this.type](this), e = this.matrix ? da(e, this.matrix) : e, f = 1; f < b + 1; f++) d.push(c.path(e).attr({
				stroke: a.color,
				fill: a.fill ? a.color : "none",
				"stroke-linejoin": "round",
				"stroke-linecap": "round",
				"stroke-width": +(a.width / b * f).toFixed(3),
				opacity: +(a.opacity / b).toFixed(3)
			}));
			return d.insertBefore(this).translate(a.offsetx, a.offsety)
		};
		var lb = function(a, c, d, e, f, o, y, E, k) {
				return k == null ? x(a, c, d, e, f, o, y, E) : b.findDotsAtSegment(a, c, d, e, f, o, y, E, U(a, c, d, e, f, o, y, E, k))
			},
			d = function(a, c) {
				return function(d, e, f) {
					for (var d = o(d), y, E, k, q, ha = "", W = {}, p = 0, u = 0, Q = d.length; u < Q; u++) {
						k = d[u];
						if (k[0] == "M") y = +k[1], E = +k[2];
						else {
							q = lb(y, E, k[1], k[2], k[3], k[4], k[5], k[6]);
							if (p + q > e) {
								if (c && !W.start) {
									y = lb(y, E, k[1], k[2], k[3], k[4], k[5], k[6], e - p);
									ha += ["C" + y.start.x, y.start.y, y.m.x, y.m.y, y.x, y.y];
									if (f) return ha;
									W.start = ha;
									ha = ["M" + y.x, y.y + "C" + y.n.x, y.n.y, y.end.x, y.end.y, k[5], k[6]].join();
									p += q;
									y = +k[5];
									E = +k[6];
									continue
								}
								if (!a && !c) return y = lb(y, E, k[1], k[2], k[3], k[4], k[5], k[6], e - p), {
									x: y.x,
									y: y.y,
									alpha: y.alpha
								}
							}
							p += q;
							y = +k[5];
							E = +k[6]
						}
						ha += k.shift() + k
					}
					W.end = ha;
					y = a ? p : c ? W : b.findDotsAtSegment(y, E, k[0], k[1], k[2], k[3], k[4], k[5], 1);
					y.alpha && (y = {
						x: y.x,
						y: y.y,
						alpha: y.alpha
					});
					return y
				}
			},
			nb = d(1),
			ob = d(),
			ua = d(0, 1);
		b.getTotalLength = nb;
		b.getPointAtLength = ob;
		b.getSubpath = function(a, b, c) {
			if (this.getTotalLength(a) - c < 1.0E-6) return ua(a, b).end;
			a = ua(a, c, 1);
			return b ? ua(a, b).end : a
		};
		Ga.getTotalLength = function() {
			if (this.type == "path") {
				if (this.node.getTotalLength) return this.node.getTotalLength();
				return nb(this.attrs.path)
			}
		};
		Ga.getPointAtLength = function(a) {
			if (this.type == "path") return ob(this.attrs.path, a)
		};
		Ga.getSubpath = function(a, c) {
			if (this.type == "path") return b.getSubpath(this.attrs.path, a, c)
		};
		d = b.easing_formulas = {
			linear: function(a) {
				return a
			},
			"<": function(a) {
				return J(a, 1.7)
			},
			">": function(a) {
				return J(a, 0.48)
			},
			"<>": function(a) {
				var b = 0.48 - a / 1.04,
					c = va(0.1734 + b * b),
					a = c - b,
					a = J(ca(a), 1 / 3) * (a < 0 ? -1 : 1),
					b = -c - b,
					b = J(ca(b), 1 / 3) * (b < 0 ? -1 : 1),
					a = a + b + 0.5;
				return (1 - a) * 3 * a * a + a * a * a
			},
			backIn: function(a) {
				return a * a * (2.70158 * a - 1.70158)
			},
			backOut: function(a) {
				a -= 1;
				return a * a * (2.70158 * a + 1.70158) + 1
			},
			elastic: function(a) {
				if (a == !! a) return a;
				return J(2, -10 * a) * pa((a - 0.075) * 2 * X / 0.3) + 1
			},
			bounce: function(a) {
				a < 1 / 2.75 ? a *= 7.5625 * a : a < 2 / 2.75 ? (a -= 1.5 / 2.75, a = 7.5625 * a * a + 0.75) : a < 2.5 / 2.75 ? (a -= 2.25 / 2.75, a = 7.5625 * a * a + 0.9375) : (a -= 2.625 / 2.75, a = 7.5625 * a * a + 0.984375);
				return a
			}
		};
		d.easeIn = d["ease-in"] = d["<"];
		d.easeOut = d["ease-out"] = d[">"];
		d.easeInOut = d["ease-in-out"] = d["<>"];
		d["back-in"] = d.backIn;
		d["back-out"] = d.backOut;
		var xa = [],
			mb = S && ia ?
		function(a) {
			setTimeout(a, 16)
		} : window.requestAnimationFrame || window.webkitRequestAnimationFrame || window.mozRequestAnimationFrame || window.oRequestAnimationFrame || window.msRequestAnimationFrame ||
		function(a) {
			setTimeout(a, 16)
		}, kb = function() {
			for (var a = +new Date, c = 0; c < xa.length; c++) {
				var d = xa[c];
				if (!d.el.removed && !d.paused) {
					var e = a - d.start,
						f = d.ms,
						o = d.easing,
						y = d.from,
						E = d.diff,
						k = d.to,
						q = d.el,
						ha = {},
						W, p = {},
						u;
					d.initstatus ? (e = (d.initstatus * d.anim.top - d.prev) / (d.percent - d.prev) * f, d.status = d.initstatus, delete d.initstatus, d.stop && xa.splice(c--, 1)) : d.status = (d.prev + (d.percent - d.prev) * (e / f)) / d.anim.top;
					if (!(e < 0)) if (e < f) {
						var Q = o(e / f),
							t;
						for (t in y) if (y[aa](t)) {
							switch (Ta[t]) {
							case ma:
								W = +y[t] + Q * f * E[t];
								break;
							case "colour":
								W = "rgb(" + [ya(Ra(y[t].r + Q * f * E[t].r)), ya(Ra(y[t].g + Q * f * E[t].g)), ya(Ra(y[t].b + Q * f * E[t].b))].join(",") + ")";
								break;
							case "path":
								W = [];
								e = 0;
								for (o = y[t].length; e < o; e++) {
									W[e] = [y[t][e][0]];
									k = 1;
									for (p = y[t][e].length; k < p; k++) W[e][k] = (+y[t][e][k] + Q * f * E[t][e][k]).toFixed(4);
									W[e] = W[e].join(j)
								}
								W = W.join(j);
								break;
							case "transform":
								if (E[t].real) {
									W = [];
									e = 0;
									for (o = y[t].length; e < o; e++) {
										W[e] = [y[t][e][0]];
										k = 1;
										for (p = y[t][e].length; k < p; k++) W[e][k] = y[t][e][k] + Q * f * E[t][e][k]
									}
								} else W = function(a) {
									return +y[t][a] + Q * f * E[t][a]
								}, W = [
									["m", W(0), W(1), W(2), W(3), W(4), W(5)]
								];
								break;
							case "csv":
								if (t == "clip-rect") {
									W = [];
									for (e = 4; e--;) W[e] = +y[t][e] + Q * f * E[t][e]
								}
								break;
							default:
								o = [][C](y[t]);
								W = [];
								for (e = q.ca[t].length; e--;) W[e] = +o[e] + Q * f * E[t][e]
							}
							ha[t] = W
						}
						q.attr(ha);
						(function(a, b, c) {
							setTimeout(function() {
								m("raphael.anim.frame." + a, b, c)
							})
						})(q.id, q, d.anim)
					} else {
						(function(a, c, d) {
							setTimeout(function() {
								m("raphael.anim.frame." + c.id, c, d);
								m("raphael.anim.finish." + c.id, c, d);
								b.is(a, "function") && a.call(c)
							})
						})(d.callback, q, d.anim);
						q.attr(k);
						xa.splice(c--, 1);
						if (d.repeat > 1 && !d.next) {
							for (u in k) k[aa](u) && (p[u] = d.totalOrigin[u]);
							d.el.attr(p);
							K(d.anim, d.el, d.anim.percents[0], null, d.totalOrigin, d.repeat - 1)
						}
						d.next && !d.stop && K(d.anim, d.el, d.next, null, d.totalOrigin, d.repeat)
					}
				}
			}
			b.svg && q && q.paper && q.paper.safari();
			xa.length && mb(kb)
		}, ya = function(a) {
			return a > 255 ? 255 : a < 0 ? 0 : a
		};
		Ga.animateWith = function(a, c, d, e, f, o) {
			if (this.removed) return o && o.call(this), this;
			d = d instanceof V ? d : b.animation(d, e, f, o);
			K(d, this, d.percents[0], null, this.attr());
			d = 0;
			for (e = xa.length; d < e; d++) if (xa[d].anim == c && xa[d].el == a) {
				xa[e - 1].start = xa[d].start;
				break
			}
			return this
		};
		Ga.onAnimation = function(a) {
			a ? m.on("raphael.anim.frame." + this.id, a) : m.unbind("raphael.anim.frame." + this.id);
			return this
		};
		V.prototype.delay = function(a) {
			var b = new V(this.anim, this.ms);
			b.times = this.times;
			b.del = +a || 0;
			return b
		};
		V.prototype.repeat = function(a) {
			var b = new V(this.anim, this.ms);
			b.del = this.del;
			b.times = q.floor(u(a, 0)) || 1;
			return b
		};
		b.animation = function(a, c, d, e) {
			if (a instanceof V) return a;
			if (b.is(d, "function") || !d) e = e || d || null, d = null;
			var a = Object(a),
				c = +c || 0,
				f = {},
				o, y;
			for (y in a) a[aa](y) && sa(y) != y && sa(y) + "%" != y && (o = !0, f[y] = a[y]);
			return o ? (d && (f.easing = d), e && (f.callback = e), new V({
				100: f
			}, c)) : new V(a, c)
		};
		Ga.animate = function(a, c, d, e) {
			if (this.removed) return e && e.call(this), this;
			a = a instanceof V ? a : b.animation(a, c, d, e);
			K(a, this, a.percents[0], null, this.attr());
			return this
		};
		Ga.setTime = function(a, b) {
			a && b != null && this.status(a, N(b, a.ms) / a.ms);
			return this
		};
		Ga.status = function(a, b) {
			var c = [],
				d = 0,
				e, f;
			if (b != null) return K(a, this, -1, N(b, 1)), this;
			else {
				for (e = xa.length; d < e; d++) if (f = xa[d], f.el.id == this.id && (!a || f.anim == a)) {
					if (a) return f.status;
					c.push({
						anim: f.anim,
						status: f.status
					})
				}
				if (a) return 0;
				return c
			}
		};
		Ga.pause = function(a) {
			for (var b = 0; b < xa.length; b++) if (xa[b].el.id == this.id && (!a || xa[b].anim == a) && m("raphael.anim.pause." + this.id, this, xa[b].anim) !== !1) xa[b].paused = !0;
			return this
		};
		Ga.resume = function(a) {
			for (var b = 0; b < xa.length; b++) if (xa[b].el.id == this.id && (!a || xa[b].anim == a)) {
				var c = xa[b];
				m("raphael.anim.resume." + this.id, this, c.anim) !== !1 && (delete c.paused, this.status(c.anim, c.status))
			}
			return this
		};
		Ga.stop = function(a) {
			for (var b = 0; b < xa.length; b++) xa[b].el.id == this.id && (!a || xa[b].anim == a) && m("raphael.anim.stop." + this.id, this, xa[b].anim) !== !1 && xa.splice(b--, 1);
			return this
		};
		m.on("raphael.remove", Y);
		m.on("raphael.clear", Y);
		Ga.toString = function() {
			return "Rapha\u00ebl\u2019s object"
		};
		Ga.toFront = function() {
			if (this.removed) return this;
			var a = b._engine.getNode(this),
				c = this.parent,
				d = this.followers,
				e;
			b._tofront(this, c) && c.canvas.appendChild(a);
			a = 0;
			for (c = d.length; a < c; a++)(e = d[a]).stalk && e.el[e.stalk](this);
			return this
		};
		Ga.toBack = function() {
			if (this.removed) return this;
			var a = b._engine.getNode(this),
				c = this.parent,
				d = this.followers,
				e;
			b._toback(this, c) && c.canvas.insertBefore(a, c.canvas.firstChild);
			a = 0;
			for (c = d.length; a < c; a++)(e = d[a]).stalk && e.el[e.stalk](this);
			return this
		};
		Ga.insertAfter = function(a) {
			if (this.removed) return this;
			var c = b._engine.getNode(this),
				d = b._engine.getLastNode(a),
				e = a.parent.canvas,
				f = this.followers,
				o;
			d.nextSibling ? e.insertBefore(c, d.nextSibling) : e.appendChild(c);
			b._insertafter(this, a, this.parent, a.parent);
			c = 0;
			for (d = f.length; c < d; c++)(o = f[c]).stalk && o.el[o.stalk](a);
			return this
		};
		Ga.insertBefore = function(a) {
			if (this.removed) return this;
			var c = b._engine.getNode(this),
				d = b._engine.getNode(a),
				e = this.followers,
				f;
			a.parent.canvas.insertBefore(c, d);
			b._insertbefore(this, a, this.parent, a.parent);
			this.parent = a.parent;
			c = 0;
			for (d = e.length; c < d; c++)(f = e[c]).stalk && f.el[f.stalk](a);
			return this
		};
		Ga.appendChild = function(a) {
			if (this.removed || this.type !== "group") return this;
			var c = this.followers,
				d, e, f;
			if (a.parent === this) return a.toFront(), this;
			e = b._engine.getNode(a);
			b._tear(a, a.parent);
			this.canvas.appendChild(e);
			a.parent = this;
			!this.bottom && (this.bottom = a);
			a.prev = this.top;
			a.next = null;
			this.top && (this.top.next = a);
			this.top = a;
			e = 0;
			for (f = c.length; e < f; e++)(d = c[e]).stalk && d.el[d.stalk](a);
			return this
		};
		Ga.removeChild = function(a) {
			if (this.removed || this.type !== "group" || a.parent !== this) return this;
			var c = b._engine.getNode(a),
				d = this.paper;
			b._tear(a, this);
			d.canvas.appendChild(c);
			this.parent = d;
			!d.bottom && (d.bottom = this);
			(this.prev = d.top) && (d.top.next = this);
			d.top = this;
			this.next = null;
			return this
		};
		var Ua = function(a) {
				this.items = [];
				this.length = 0;
				this.type = "set";
				if (a) for (var b = 0, c = a.length; b < c; b++) if (a[b] && (a[b].constructor == Ga.constructor || a[b].constructor == Ua)) this[this.items.length] = this.items[this.items.length] = a[b], this.length++
			},
			d = Ua.prototype;
		d.push = function() {
			for (var a, b, c = 0, d = arguments.length; c < d; c++) if ((a = arguments[c]) && (a.constructor == Ga.constructor || a.constructor == Ua)) b = this.items.length, this[b] = this.items[b] = a, this.length++;
			return this
		};
		d.pop = function() {
			this.length && delete this[this.length--];
			return this.items.pop()
		};
		d.forEach = function(a, b) {
			for (var c = 0, d = this.items.length; c < d; c++) if (a.call(b, this.items[c], c) === !1) break;
			return this
		};
		for (var Ya in Ga) Ga[aa](Ya) && (d[Ya] = function(a) {
			return function() {
				var b = arguments;
				return this.forEach(function(c) {
					c[a][v](c, b)
				})
			}
		}(Ya));
		d.attr = function(a, c) {
			if (a && b.is(a, ta) && b.is(a[0], "object")) for (var d = 0, e = a.length; d < e; d++) this.items[d].attr(a[d]);
			else {
				d = 0;
				for (e = this.items.length; d < e; d++) this.items[d].attr(a, c)
			}
			return this
		};
		d.clear = function() {
			for (; this.length;) this.pop()
		};
		d.splice = function(a, b) {
			var a = a < 0 ? u(this.length + a, 0) : a,
				b = u(0, N(this.length - a, isNaN(b) && this.length || b)),
				c = [],
				d = [],
				e = [],
				f;
			for (f = 2; f < arguments.length; f++) e.push(arguments[f]);
			for (f = 0; f < b; f++) d.push(this[a + f]);
			for (; f < this.length - a; f++) c.push(this[a + f]);
			var o = e.length;
			for (f = 0; f < o + c.length; f++) this.items[a + f] = this[a + f] = f < o ? e[f] : c[f - o];
			for (f = this.items.length = this.length -= b - o; this[f];) delete this[f++];
			return new Ua(d)
		};
		d.exclude = function(a) {
			for (var b = 0, c = this.length; b < c; b++) if (this[b] == a) return this.splice(b, 1), !0
		};
		d.animate = function(a, c, d, e) {
			(b.is(d, "function") || !d) && (e = d || null);
			var f = this.items.length,
				o = f,
				y = this,
				E;
			if (!f) return this;
			e && (E = function() {
				!--f && e.call(y)
			});
			d = b.is(d, "string") ? d : E;
			c = b.animation(a, c, d, E);
			for (a = this.items[--o].animate(c); o--;) this.items[o] && !this.items[o].removed && this.items[o].animateWith(a, c, c);
			return this
		};
		d.insertAfter = function(a) {
			for (var b = this.items.length; b--;) this.items[b].insertAfter(a);
			return this
		};
		d.getBBox = function() {
			for (var a = [], b = [], c = [], d = [], e = this.items.length; e--;) if (!this.items[e].removed) {
				var f = this.items[e].getBBox();
				a.push(f.x);
				b.push(f.y);
				c.push(f.x + f.width);
				d.push(f.y + f.height)
			}
			a = N[v](0, a);
			b = N[v](0, b);
			c = u[v](0, c);
			d = u[v](0, d);
			return {
				x: a,
				y: b,
				x2: c,
				y2: d,
				width: c - a,
				height: d - b
			}
		};
		d.clone = function(a) {
			for (var a = new Ua, b = 0, c = this.items.length; b < c; b++) a.push(this.items[b].clone());
			return a
		};
		d.toString = function() {
			return "Rapha\u00ebl\u2018s set"
		};
		b.registerFont = function(a) {
			if (!a.face) return a;
			this.fonts = this.fonts || {};
			var b = {
				w: a.w,
				face: {},
				glyphs: {}
			},
				c = a.face["font-family"],
				d;
			for (d in a.face) a.face[aa](d) && (b.face[d] = a.face[d]);
			this.fonts[c] ? this.fonts[c].push(b) : this.fonts[c] = [b];
			if (!a.svg) {
				b.face["units-per-em"] = Ma(a.face["units-per-em"], 10);
				for (var e in a.glyphs) if (a.glyphs[aa](e) && (c = a.glyphs[e], b.glyphs[e] = {
					w: c.w,
					k: {},
					d: c.d && "M" + c.d.replace(/[mlcxtrv]/g, function(a) {
						return {
							l: "L",
							c: "C",
							x: "z",
							t: "m",
							r: "l",
							v: "c"
						}[a] || "M"
					}) + "z"
				}, c.k)) for (var f in c.k) c[aa](f) && (b.glyphs[e].k[f] = c.k[f])
			}
			return a
		};
		R.getFont = function(a, c, d, e) {
			e = e || "normal";
			d = d || "normal";
			c = +c || {
				normal: 400,
				bold: 700,
				lighter: 300,
				bolder: 800
			}[c] || 400;
			if (b.fonts) {
				var f = b.fonts[a];
				if (!f) {
					var a = RegExp("(^|\\s)" + a.replace(/[^\w\d\s+!~.:_-]/g, "") + "(\\s|$)", "i"),
						o;
					for (o in b.fonts) if (b.fonts[aa](o) && a.test(o)) {
						f = b.fonts[o];
						break
					}
				}
				var y;
				if (f) {
					o = 0;
					for (a = f.length; o < a; o++) if (y = f[o], y.face["font-weight"] == c && (y.face["font-style"] == d || !y.face["font-style"]) && y.face["font-stretch"] == e) break
				}
				return y
			}
		};
		R.print = function(a, d, e, f, o, y, E) {
			var y = y || "middle",
				E = u(N(E || 0, 1), -1),
				k = i(e)[c](""),
				q = 0,
				W = 0,
				ha = "";
			b.is(f, e) && (f = this.getFont(f));
			if (f) for (var e = (o || 16) / f.face["units-per-em"], p = f.face.bbox[c](l), o = +p[0], Q = p[3] - p[1], t = 0, y = +p[1] + (y == "baseline" ? Q + +f.face.descent : Q / 2), p = 0, ja = k.length; p < ja; p++) {
				if (k[p] == "\n") W = z = q = 0, t += Q;
				else {
					var Ea = W && f.glyphs[k[p - 1]] || {},
						z = f.glyphs[k[p]];
					q += W ? (Ea.w || f.w) + (Ea.k && Ea.k[k[p]] || 0) + f.w * E : 0;
					W = 1
				}
				z && z.d && (ha += b.transformPath(z.d, ["t", q * e, t * e, "s", e, e, o, y, "t", (a - o) / e, (d - y) / e]))
			}
			return this.path(ha).attr({
				fill: "#000",
				stroke: "none"
			})
		};
		R.add = function(a) {
			if (b.is(a, "array")) for (var c = this.set(), d = 0, e = a.length, f; d < e; d++) f = a[d] || {}, n[aa](f.type) && c.push(this[f.type]().attr(f));
			return c
		};
		b.format = function(a, c) {
			var d = b.is(c, ta) ? [0][C](c) : arguments;
			a && b.is(a, "string") && d.length - 1 && (a = a.replace(F, function(a, b) {
				return d[++b] == null ? "" : d[b]
			}));
			return a || ""
		};
		b.fullfill = function() {
			var a = /\{([^\}]+)\}/g,
				b = /(?:(?:^|\.)(.+?)(?=\[|\.|$|\()|\[('|")(.+?)\2\])(\(\))?/g,
				c = function(a, c, d) {
					var e = d;
					c.replace(b, function(a, b, c, d, f) {
						b = b || d;
						e && (b in e && (e = e[b]), typeof e == "function" && f && (e = e()))
					});
					return e = (e == null || e == d ? a : e) + ""
				};
			return function(b, d) {
				return String(b).replace(a, function(a, b) {
					return c(a, b, d)
				})
			}
		}();
		b.ninja = function() {
			Z.was ? D.win.Raphael = Z.is : delete Raphael;
			return b
		};
		b.st = d;
		(function(a, c, d) {
			function e() {
				/in/.test(a.readyState) ? setTimeout(e, 9) : b.eve("raphael.DOMload")
			}
			if (a.readyState == null && a.addEventListener) a.addEventListener(c, d = function() {
				a.removeEventListener(c, d, !1);
				a.readyState = "complete"
			}, !1), a.readyState = "loading";
			e()
		})(document, "DOMContentLoaded");
		Z.was ? D.win.Raphael = b : Raphael = b;
		m.on("raphael.DOMload", function() {
			O = !0
		})
	})();
	window.Raphael.svg &&
	function(b) {
		var g = String,
			e = parseFloat,
			r = parseInt,
			h = Math,
			m = h.max,
			w = h.abs,
			s = h.pow,
			ka = h.sqrt,
			G = /[, ]+/,
			V = !(!/AppleWebKit/.test(b._g.win.navigator.userAgent) || /Chrome/.test(b._g.win.navigator.userAgent) && !(b._g.win.navigator.appVersion.match(/Chrome\/(\d+)\./)[1] < 29)),
			K = b.eve,
			U = {
				block: "M5,0 0,2.5 5,5z",
				classic: "M5,0 0,2.5 5,5 3.5,3 3.5,2z",
				diamond: "M2.5,0 5,2.5 2.5,5 0,2.5z",
				open: "M6,1 1,3.5 6,6",
				oval: "M2.5,0A2.5,2.5,0,0,1,2.5,5 2.5,2.5,0,0,1,2.5,0z"
			},
			O = b._shapeRenderingAttrs = {
				speed: "optimizeSpeed",
				crisp: "crispEdges",
				precision: "geometricPrecision"
			},
			l = {};
		b._url = /msie/i.test(navigator.userAgent) && !window.opera ? "" : window.location.href.replace(/#.*?$/, "");
		b.toString = function() {
			return "Your browser supports SVG.\nYou are running Rapha\u00ebl " + this.version
		};
		var n = b._createNode = function(a, c) {
				if (c) {
					typeof a == "string" && (a = n(a));
					for (var d in c) c.hasOwnProperty(d) && (d.substring(0, 6) == "xlink:" ? a.setAttributeNS("http://www.w3.org/1999/xlink", d.substring(6), g(c[d])) : a.setAttribute(d, g(c[d])))
				} else a = b._g.doc.createElementNS("http://www.w3.org/2000/svg", a);
				return a
			},
			F = {
				userSpaceOnUse: "userSpaceOnUse",
				objectBoundingBox: "objectBoundingBox"
			},
			aa = {
				pad: "pad",
				redlect: "reflect",
				repeat: "repeat"
			},
			D = function(a, c) {
				var d = "linear",
					f = a.id + c,
					j = 0.5,
					i = 0.5,
					r, l, v, G, C, R = a.node,
					D = a.paper,
					O = R.style,
					K = b._g.doc.getElementById(f);
				if (!K && D.defs) {
					c = g(c).replace(b._radial_gradient, function(a, b) {
						d = "radial";
						b = b && b.split(",") || [];
						G = b[5];
						C = b[6];
						var c = b[0],
							f = b[1],
							k = b[2],
							u = b[3],
							g = b[4],
							n = c && f,
							h;
						k && (r = /\%/.test(k) ? k : e(k));
						if (G === F.userSpaceOnUse) return n && (j = c, i = f), u && g && (l = u, v = g, n || (j = l, i = v)), "";
						n && (j = e(c), i = e(f), c = (i > 0.5) * 2 - 1, (h = s(j - 0.5, 2)) + s(i - 0.5, 2) > 0.25 && h < 0.25 && (i = ka(0.25 - h) * c + 0.5) && i !== 0.5 && (i = i.toFixed(5) - 1.0E-5 * c));
						u && g && (l = e(u), v = e(g), c = (v > 0.5) * 2 - 1, (h = s(l - 0.5, 2)) + s(v - 0.5, 2) > 0.25 && h < 0.25 && (v = ka(0.25 - h) * c + 0.5) && v !== 0.5 && (v = v.toFixed(5) - 1.0E-5 * c), n || (j = l, i = v));
						return ""
					});
					c = c.split(/\s*\-\s*/);
					if (d == "linear") {
						var K = c.shift(),
							V = K.match(/\((.*)\)/),
							M, V = V && V[1] && V[1].split(/\s*\,\s*/),
							K = -e(K);
						if (isNaN(K)) return null;
						V && V.length ? (V[0] in F ? (G = V.shift(), V[0] in aa && (C = V.shift())) : (V[4] && (G = V[4]), V[5] && (C = V[5])), M = [V[0] || "0%", V[1] || "0%", V[2] || "100%", V[3] || "0%"]) : (M = [0, 0, h.cos(b.rad(K)), h.sin(b.rad(K))], K = 1 / (m(w(M[2]), w(M[3])) || 1), M[2] *= K, M[3] *= K, M[2] < 0 && (M[0] = -M[2], M[2] = 0), M[3] < 0 && (M[1] = -M[3], M[3] = 0))
					}
					V = b._parseDots(c);
					if (!V) return null;
					f = f.replace(/[\(\)\s,\xb0#]/g, "_");
					a.gradient && f !== a.gradient.id && (D.defs.removeChild(a.gradient), delete a.gradient);
					if (!a.gradient) {
						K = n(d + "Gradient", {
							id: f
						});
						a.gradient = K;
						G in F && K.setAttribute("gradientUnits", g(G));
						C in aa && K.setAttribute("spreadMethod", g(C));
						d === "radial" ? (r !== void 0 && K.setAttribute("r", g(r)), l !== void 0 && v !== void 0 && (K.setAttribute("cx", g(l)), K.setAttribute("cy", g(v))), K.setAttribute("fx", g(j)), K.setAttribute("fy", g(i))) : n(K, {
							x1: M[0],
							y1: M[1],
							x2: M[2],
							y2: M[3],
							gradientTransform: a.matrix.invert()
						});
						D.defs.appendChild(K);
						D = 0;
						for (M = V.length; D < M; D++) K.appendChild(n("stop", {
							offset: V[D].offset ? V[D].offset : D ? "100%" : "0%",
							"stop-color": V[D].color || "#fff",
							"stop-opacity": V[D].opacity === void 0 ? 1 : V[D].opacity
						}))
					}
				}
				n(R, {
					fill: "url('" + b._url + "#" + f + "')",
					opacity: 1,
					"fill-opacity": 1
				});
				O.fill = "";
				O.opacity = 1;
				return O.fillOpacity = 1
			},
			M = function(a) {
				var b = a.getBBox(1);
				n(a.pattern, {
					patternTransform: a.matrix.invert() + " translate(" + b.x + "," + b.y + ")"
				})
			},
			Z = function(a, c, d) {
				if (a.type == "path") {
					for (var e = g(c).toLowerCase().split("-"), f = a.paper, j = d ? "end" : "start", i = a.node, s = a.attrs, r = s["stroke-width"], h = e.length, v = "classic", m, x, w = 3, F = 3, G = 5; h--;) switch (e[h]) {
					case "block":
					case "classic":
					case "oval":
					case "diamond":
					case "open":
					case "none":
						v = e[h];
						break;
					case "wide":
						F = 5;
						break;
					case "narrow":
						F = 2;
						break;
					case "long":
						w = 5;
						break;
					case "short":
						w = 2
					}
					v == "open" ? (w += 2, F += 2, G += 2, m = 1, x = d ? 4 : 1, e = {
						fill: "none",
						stroke: s.stroke
					}) : (x = m = w / 2, e = {
						fill: s.stroke,
						stroke: "none"
					});
					a._.arrows ? d ? (a._.arrows.endPath && l[a._.arrows.endPath]--, a._.arrows.endMarker && l[a._.arrows.endMarker]--) : (a._.arrows.startPath && l[a._.arrows.startPath]--, a._.arrows.startMarker && l[a._.arrows.startMarker]--) : a._.arrows = {};
					if (v != "none") {
						var h = "raphael-marker-" + v,
							C = "raphael-marker-" + j + v + w + F;
						b._g.doc.getElementById(h) ? l[h]++ : (f.defs.appendChild(n(n("path"), {
							"stroke-linecap": "round",
							d: U[v],
							id: h
						})), l[h] = 1);
						var R = b._g.doc.getElementById(C);
						R ? (l[C]++, w = R.getElementsByTagName("use")[0]) : (R = n(n("marker"), {
							id: C,
							markerHeight: F,
							markerWidth: w,
							orient: "auto",
							refX: x,
							refY: F / 2
						}), w = n(n("use"), {
							"xlink:href": "#" + h,
							transform: (d ? "rotate(180 " + w / 2 + " " + F / 2 + ") " : "") + "scale(" + w / G + "," + F / G + ")",
							"stroke-width": (1 / ((w / G + F / G) / 2)).toFixed(4)
						}), R.appendChild(w), f.defs.appendChild(R), l[C] = 1);
						n(w, e);
						f = m * (v != "diamond" && v != "oval");
						d ? (d = a._.arrows.startdx * r || 0, r = b.getTotalLength(s.path) - f * r) : (d = f * r, r = b.getTotalLength(s.path) - (a._.arrows.enddx * r || 0));
						e = {};
						e["marker-" + j] = "url('" + b._url + "#" + C + "')";
						if (r || d) e.d = Raphael.getSubpath(s.path, d, r);
						n(i, e);
						a._.arrows[j + "Path"] = h;
						a._.arrows[j + "Marker"] = C;
						a._.arrows[j + "dx"] = f;
						a._.arrows[j + "Type"] = v;
						a._.arrows[j + "String"] = c
					} else d ? (d = a._.arrows.startdx * r || 0, r = b.getTotalLength(s.path) - d) : (d = 0, r = b.getTotalLength(s.path) - (a._.arrows.enddx * r || 0)), a._.arrows[j + "Path"] && n(i, {
						d: Raphael.getSubpath(s.path, d, r)
					}), delete a._.arrows[j + "Path"], delete a._.arrows[j + "Marker"], delete a._.arrows[j + "dx"], delete a._.arrows[j + "Type"], delete a._.arrows[j + "String"];
					for (e in l) l.hasOwnProperty(e) && !l[e] && (a = b._g.doc.getElementById(e)) && a.parentNode.removeChild(a)
				}
			},
			R = {
				"": [0],
				none: [0],
				"-": [3, 1],
				".": [1, 1],
				"-.": [3, 1, 1, 1],
				"-..": [3, 1, 1, 1, 1, 1],
				". ": [1, 3],
				"- ": [4, 3],
				"--": [8, 3],
				"- .": [4, 3, 1, 3],
				"--.": [8, 3, 1, 3],
				"--..": [8, 3, 1, 3, 1, 3]
			},
			v = function(a, c, d) {
				var e = R[g(c).toLowerCase()];
				if (c = e || c !== void 0 && [].concat(c)) {
					var f = a.attrs["stroke-width"] || "1",
						d = {
							round: f,
							square: f,
							butt: 0
						}[a.attrs["stroke-linecap"] || d["stroke-linecap"]] || 0,
						j, i = j = c.length;
					if (e) for (; j--;) c[j] = c[j] * f + (j % 2 ? 1 : -1) * d;
					else for (j = 0; j < i; j += 2) c[j] -= d, c[j + 1] && (c[j + 1] += d), c[j] <= 0 && (c[j] = 0.1);
					b.is(c, "array") && n(a.node, {
						"stroke-dasharray": c.join(",")
					})
				}
			},
			C = b._setFillAndStroke = function(a, c) {
				if (a.paper.canvas) {
					var d = a.node,
						e = a.attrs,
						j = a.paper,
						i = d.style.visibility;
					d.style.visibility = "hidden";
					for (var s in c) if (c.hasOwnProperty(s) && b._availableAttrs.hasOwnProperty(s)) {
						var h = c[s];
						e[s] = h;
						switch (s) {
						case "blur":
							a.blur(h);
							break;
						case "href":
						case "title":
						case "target":
							var l = d.parentNode;
							if (l.tagName.toLowerCase() != "a") {
								if (h == "") break;
								var x = n("a");
								l.insertBefore(x, d);
								x.appendChild(d);
								l = x
							}
							s == "target" ? l.setAttributeNS("http://www.w3.org/1999/xlink", "show", h == "blank" ? "new" : h) : l.setAttributeNS("http://www.w3.org/1999/xlink", s, h);
							d.titleNode = l;
							break;
						case "cursor":
							d.style.cursor = h;
							break;
						case "transform":
							a.transform(h);
							break;
						case "rotation":
							b.is(h, "array") ? a.rotate.apply(a, h) : a.rotate(h);
							break;
						case "arrow-start":
							Z(a, h);
							break;
						case "arrow-end":
							Z(a, h, 1);
							break;
						case "clip-path":
							var F = !0;
						case "clip-rect":
							l = !F && g(h).split(G);
							a._.clipispath = !! F;
							if (F || l.length == 4) {
								a.clip && a.clip.parentNode.parentNode.removeChild(a.clip.parentNode);
								var x = n("clipPath"),
									C = n(F ? "path" : "rect");
								x.id = b.createUUID();
								n(C, F ? {
									d: h ? e["clip-path"] = b._pathToAbsolute(h) : b._availableAttrs.path,
									fill: "none"
								} : {
									x: l[0],
									y: l[1],
									width: l[2],
									height: l[3],
									transform: a.matrix.invert()
								});
								x.appendChild(C);
								j.defs.appendChild(x);
								n(d, {
									"clip-path": "url('" + b._url + "#" + x.id + "')"
								});
								a.clip = C
							}
							if (!h && (h = d.getAttribute("clip-path")))(h = b._g.doc.getElementById(h.replace(/(^url\(#|\)$)/g, ""))) && h.parentNode.removeChild(h), n(d, {
								"clip-path": ""
							}), delete a.clip;
							break;
						case "path":
							if (a.type == "path") n(d, {
								d: h ? e.path = b._pathToAbsolute(h) : b._availableAttrs.path
							}), a._.dirty = 1, a._.arrows && ("startString" in a._.arrows && Z(a, a._.arrows.startString), "endString" in a._.arrows && Z(a, a._.arrows.endString, 1));
							break;
						case "width":
							if (d.setAttribute(s, h), a._.dirty = 1, e.fx) s = "x", h = e.x;
							else break;
						case "x":
							e.fx && (h = -e.x - (e.width || 0));
						case "rx":
							if (s == "rx" && a.type == "rect") break;
						case "cx":
							d.setAttribute(s, h);
							a.pattern && M(a);
							a._.dirty = 1;
							break;
						case "height":
							if (d.setAttribute(s, h), a._.dirty = 1, e.fy) s = "y", h = e.y;
							else break;
						case "y":
							e.fy && (h = -e.y - (e.height || 0));
						case "ry":
							if (s == "ry" && a.type == "rect") break;
						case "cy":
							d.setAttribute(s, h);
							a.pattern && M(a);
							a._.dirty = 1;
							break;
						case "r":
							a.type == "rect" ? n(d, {
								rx: h,
								ry: h
							}) : d.setAttribute(s, h);
							a._.dirty = 1;
							break;
						case "src":
							a.type == "image" && d.setAttributeNS("http://www.w3.org/1999/xlink", "href", h);
							break;
						case "stroke-width":
							if (a._.sx != 1 || a._.sy != 1) h /= m(w(a._.sx), w(a._.sy)) || 1;
							j._vbSize && (h *= j._vbSize);
							V && h === 0 && (h = 1.0E-6);
							d.setAttribute(s, h);
							e["stroke-dasharray"] && v(a, e["stroke-dasharray"], c);
							a._.arrows && ("startString" in a._.arrows && Z(a, a._.arrows.startString), "endString" in a._.arrows && Z(a, a._.arrows.endString, 1));
							break;
						case "stroke-dasharray":
							v(a, h, c);
							break;
						case "fill":
							var R = g(h).match(b._ISURL);
							if (R) {
								var x = n("pattern"),
									ka = n("image");
								x.id = b.createUUID();
								n(x, {
									x: 0,
									y: 0,
									patternUnits: "userSpaceOnUse",
									height: 1,
									width: 1
								});
								n(ka, {
									x: 0,
									y: 0,
									"xlink:href": R[1]
								});
								x.appendChild(ka);
								(function(a) {
									b._preload(R[1], function() {
										var b = this.offsetWidth,
											c = this.offsetHeight;
										n(a, {
											width: b,
											height: c
										});
										n(ka, {
											width: b,
											height: c
										});
										j.safari()
									})
								})(x);
								j.defs.appendChild(x);
								n(d, {
									fill: "url('" + b._url + "#" + x.id + "')"
								});
								a.pattern = x;
								a.pattern && M(a);
								break
							}
							l = b.getRGB(h);
							if (l.error) {
								if ((a.type == "circle" || a.type == "ellipse" || g(h).charAt() != "r") && D(a, h)) {
									if ("opacity" in e || "fill-opacity" in e) if (l = b._g.doc.getElementById(d.getAttribute("fill").replace(/^url\(#|\)$/g, ""))) l = l.getElementsByTagName("stop"), n(l[l.length - 1], {
										"stop-opacity": ("opacity" in e ? e.opacity : 1) * ("fill-opacity" in e ? e["fill-opacity"] : 1)
									});
									e.gradient = h;
									e.fill = "none";
									break
								}
							} else delete c.gradient, delete e.gradient, !b.is(e.opacity, "undefined") && b.is(c.opacity, "undefined") && n(d, {
								opacity: e.opacity
							}), !b.is(e["fill-opacity"], "undefined") && b.is(c["fill-opacity"], "undefined") && n(d, {
								"fill-opacity": e["fill-opacity"]
							});
							l.hasOwnProperty("opacity") ? (n(d, {
								"fill-opacity": l.opacity > 1 ? l.opacity / 100 : l.opacity
							}), a._.opacitydirty = !0) : a._.opacitydirty && b.is(e["fill-opacity"], "undefined") && b.is(c["fill-opacity"], "undefined") && (d.removeAttribute("fill-opacity"), delete a._.opacitydirty);
						case "stroke":
							l = b.getRGB(h);
							d.setAttribute(s, l.hex);
							s == "stroke" && l.hasOwnProperty("opacity") && n(d, {
								"stroke-opacity": l.opacity > 1 ? l.opacity / 100 : l.opacity
							});
							s == "stroke" && a._.arrows && ("startString" in a._.arrows && Z(a, a._.arrows.startString), "endString" in a._.arrows && Z(a, a._.arrows.endString, 1));
							break;
						case "gradient":
							(a.type == "circle" || a.type == "ellipse" || g(h).charAt() != "r") && D(a, h);
							break;
						case "shape-rendering":
							e[s] = h = O[h] || h || "default";
							d.setAttribute(s, h);
							d.style.shapeRendering = h;
							break;
						case "line-height":
						case "vertical-align":
							break;
						case "visibility":
							h === "hidden" ? a.hide() : a.show();
							break;
						case "opacity":
							e.gradient && !e.hasOwnProperty("stroke-opacity") && n(d, {
								"stroke-opacity": h > 1 ? h / 100 : h
							});
						case "fill-opacity":
							if (e.gradient) {
								if (l = b._g.doc.getElementById(d.getAttribute("fill").replace(/^url\(#|\)$/g, ""))) l = l.getElementsByTagName("stop"), n(l[l.length - 1], {
									"stop-opacity": h
								});
								break
							}
						default:
							s == "font-size" && (h = r(h, 10) + "px"), l = s.replace(/(\-.)/g, function(a) {
								return a.substring(1).toUpperCase()
							}), d.style[l] = h, a._.dirty = 1, d.setAttribute(s, h)
						}
					}
					f(a, c);
					d.style.visibility = i
				}
			},
			f = function(a, c) {
				if (!(a.type != "text" || !c.hasOwnProperty("text") && !c.hasOwnProperty("font") && !c.hasOwnProperty("font-size") && !c.hasOwnProperty("x") && !c.hasOwnProperty("y") && !c.hasOwnProperty("line-height") && !c.hasOwnProperty("vertical-align"))) {
					var d = a.attrs,
						f = a.node,
						j = f.firstChild && b._g.doc.defaultView.getComputedStyle(f.firstChild, ""),
						j = j ? e(j.getPropertyValue("font-size")) : e(c["font-size"] || d["font-size"]) || 10,
						i = e(c["line-height"] || d["line-height"]) || j * 1.2,
						h = d.hasOwnProperty("vertical-align") ? d["vertical-align"] : "middle";
					isNaN(j) && (j = 10);
					isNaN(i) && (i = j * 1.2);
					h = h === "top" ? -0.5 : h === "bottom" ? 0.5 : 0;
					if (c.hasOwnProperty("text") && (c.text !== d.text || a._textdirty)) {
						for (d.text = c.text; f.firstChild;) f.removeChild(f.firstChild);
						for (var s = g(c.text).split(/\n|<br\s*?\/?>/ig), j = [], l, r = 0, v = s.length; r < v; r++) l = n("tspan"), r ? n(l, {
							dy: i,
							x: d.x
						}) : n(l, {
							dy: i * s.length * h,
							x: d.x
						}), s[r] || (l.setAttributeNS("http://www.w3.org/XML/1998/namespace", "xml:space", "preserve"), s[r] = " "), l.appendChild(b._g.doc.createTextNode(s[r])), f.appendChild(l), j[r] = l;
						a._textdirty = !1
					} else {
						j = f.getElementsByTagName("tspan");
						r = 0;
						for (v = j.length; r < v; r++) r ? n(j[r], {
							dy: i,
							x: d.x
						}) : n(j[0], {
							dy: i * j.length * h,
							x: d.x
						})
					}
					n(f, {
						x: d.x,
						y: d.y
					});
					a._.dirty = 1;
					f = a._getBBox();
					i = d.y - (f.y + f.height / 2);
					if (f.isCalculated) switch (d["vertical-align"]) {
					case "top":
						i = f.height * 0.75;
						break;
					case "bottom":
						i = -(f.height * 0.25);
						break;
					default:
						i = d.y - (f.y + f.height * 0.25)
					}
					i && b.is(i, "finite") && j[0] && n(j[0], {
						dy: i
					})
				}
			},
			j = function(a, c, d) {
				d = d || c;
				this.node = this[0] = a;
				a.raphael = !0;
				a.raphaelid = this.id = b._oid++;
				this.matrix = b.matrix();
				this.realPath = null;
				this.attrs = this.attrs || {};
				this.styles = this.styles || {};
				this.followers = this.followers || [];
				this.paper = c;
				this.ca = this.customAttributes = this.customAttributes || new c._CustomAttributes;
				this._ = {
					transform: [],
					sx: 1,
					sy: 1,
					deg: 0,
					dx: 0,
					dy: 0,
					dirty: 1
				};
				this.parent = d;
				!d.bottom && (d.bottom = this);
				(this.prev = d.top) && (d.top.next = this);
				d.top = this;
				this.next = null
			},
			i = b.el;
		j.prototype = i;
		i.constructor = j;
		b._engine.getNode = function(a) {
			a = a.node || a[0].node;
			return a.titleNode || a
		};
		b._engine.getLastNode = function(a) {
			a = a.node || a[a.length - 1].node;
			return a.titleNode || a
		};
		b._engine.path = function(a, b, c) {
			var d = n("path");
			c && c.canvas && c.canvas.appendChild(d) || b.canvas && b.canvas.appendChild(d);
			b = new j(d, b, c);
			b.type = "path";
			C(b, {
				fill: "none",
				stroke: "#000",
				path: a
			});
			return b
		};
		i.rotate = function(a, b, c) {
			if (this.removed) return this;
			a = g(a).split(G);
			a.length - 1 && (b = e(a[1]), c = e(a[2]));
			a = e(a[0]);
			c == null && (b = c);
			if (b == null || c == null) c = this.getBBox(1), b = c.x + c.width / 2, c = c.y + c.height / 2;
			this.transform(this._.transform.concat([
				["r", a, b, c]
			]));
			return this
		};
		i.scale = function(a, b, c, d) {
			var f;
			if (this.removed) return this;
			a = g(a).split(G);
			a.length - 1 && (b = e(a[1]), c = e(a[2]), d = e(a[3]));
			a = e(a[0]);
			b == null && (b = a);
			d == null && (c = d);
			if (c == null || d == null) f = this.getBBox(1);
			c = c == null ? f.x + f.width / 2 : c;
			d = d == null ? f.y + f.height / 2 : d;
			this.transform(this._.transform.concat([
				["s", a, b, c, d]
			]));
			return this
		};
		i.translate = function(a, b) {
			if (this.removed) return this;
			a = g(a).split(G);
			a.length - 1 && (b = e(a[1]));
			a = e(a[0]) || 0;
			this.transform(this._.transform.concat([
				["t", a, +b || 0]
			]));
			return this
		};
		i.transform = function(a) {
			var c = this._;
			if (a == null) return c.transform;
			b._extractTransform(this, a);
			this.clip && !c.clipispath && n(this.clip, {
				transform: this.matrix.invert()
			});
			this.pattern && M(this);
			this.node && n(this.node, {
				transform: this.matrix
			});
			if (c.sx != 1 || c.sy != 1) a = this.attrs.hasOwnProperty("stroke-width") ? this.attrs["stroke-width"] : 1, this.attr({
				"stroke-width": a
			});
			return this
		};
		i.hide = function() {
			!this.removed && this.paper.safari(this.node.style.display = "none");
			return this
		};
		i.show = function() {
			!this.removed && this.paper.safari(this.node.style.display = "");
			return this
		};
		i.remove = function() {
			if (!this.removed && this.parent.canvas) {
				var a = b._engine.getNode(this),
					c = this.paper,
					d = c.defs;
				c.__set__ && c.__set__.exclude(this);
				K.unbind("raphael.*.*." + this.id);
				for (this.gradient && d && d.removeChild(this.gradient); d = this.followers.pop();) d.el.remove();
				this.parent.canvas.removeChild(a);
				b._tear(this, c);
				for (d in this) this[d] = typeof this[d] === "function" ? b._removedFactory(d) : null;
				this.removed = !0
			}
		};
		i._getBBox = function() {
			var a = this.node,
				b = {},
				c = this.attrs,
				d, e;
			a.style.display === "none" && (this.show(), e = !0);
			try {
				if (b = a.getBBox(), this.type == "text") {
					if (b.x === void 0) b.isCalculated = !0, d = c["text-anchor"], b.x = (c.x || 0) - b.width * (d === "start" ? 0 : d === "middle" ? 0.5 : 1);
					if (b.y === void 0) b.isCalculated = !0, d = c["vertical-align"], b.y = (c.y || 0) - b.height * (d === "bottom" ? 1 : d === "middle" ? 0.5 : 0)
				}
			} catch (f) {} finally {
				b = b || {}
			}
			e && this.hide();
			return b
		};
		i.css = function(a, c) {
			if (this.removed) return this;
			if (c == null && b.is(a, "string")) {
				for (var d = a.split(G), e = {}, f = 0, j = d.length; f < j; f++) a = d[f], a in this.styles && (e[a] = this.styles[a]);
				return j - 1 ? e : e[d[0]]
			}
			if (c == null && b.is(a, "array")) {
				e = {};
				f = 0;
				for (j = a.length; f < j; f++) e[a[f]] = this.styles(a[f]);
				return e
			}
			c != null ? (d = {}, d[a] = c) : a != null && b.is(a, "object") && (d = a);
			e = {};
			for (j in d) f = j.replace(/\B([A-Z]{1})/g, "-$1").toLowerCase(), b._availableAttrs.hasOwnProperty(f) || f === "color" ? (f === "color" && this.type === "text" && (f = "fill"), e[f] = d[j], e.dirty = !0) : (K("raphael.css." + f + "." + this.id, this, d[j], f), this.node.style[f] = d[j], this.styles[f] = d[j]);
			f = 0;
			for (j = this.followers.length; f < j; f++) this.followers[f].el.css(d);
			e.hasOwnProperty("dirty") && (delete e.dirty, this.attr(e));
			return this
		};
		i.attr = function(a, c) {
			if (this.removed) return this;
			if (a == null) {
				var d = {},
					e;
				for (e in this.attrs) this.attrs.hasOwnProperty(e) && (d[e] = this.attrs[e]);
				d.gradient && d.fill == "none" && (d.fill = d.gradient) && delete d.gradient;
				d.transform = this._.transform;
				d.visibility = this.node.style.display === "none" ? "hidden" : "visible";
				return d
			}
			if (c == null && b.is(a, "string")) {
				if (a == "fill" && this.attrs.fill == "none" && this.attrs.gradient) return this.attrs.gradient;
				if (a == "transform") return this._.transform;
				if (a == "visibility") return this.node.style.display === "none" ? "hidden" : "visible";
				var d = a.split(G),
					f = {},
					j = 0;
				for (e = d.length; j < e; j++) a = d[j], f[a] = a in this.attrs ? this.attrs[a] : b.is(this.ca[a], "function") ? this.ca[a].def : b._availableAttrs[a];
				return e - 1 ? f : f[d[0]]
			}
			if (c == null && b.is(a, "array")) {
				f = {};
				j = 0;
				for (e = a.length; j < e; j++) f[a[j]] = this.attr(a[j]);
				return f
			}
			c != null ? (d = {}, d[a] = c) : a != null && b.is(a, "object") && (d = a);
			for (j in d) K("raphael.attr." + j + "." + this.id, this, d[j], j);
			var i = {};
			for (j in this.ca) if (this.ca[j] && d.hasOwnProperty(j) && b.is(this.ca[j], "function") && !this.ca["_invoked" + j]) {
				this.ca["_invoked" + j] = !0;
				e = this.ca[j].apply(this, [].concat(d[j]));
				delete this.ca["_invoked" + j];
				for (f in e) e.hasOwnProperty(f) && (d[f] = e[f]);
				this.attrs[j] = d[j];
				e === !1 && (i[j] = d[j], delete d[j])
			}
			C(this, d);
			var h, j = 0;
			for (e = this.followers.length; j < e; j++) h = this.followers[j], h.cb && !h.cb.call(h.el, d, this) || h.el.attr(d);
			for (f in i) d[f] = i[f];
			return this
		};
		i.blur = function(a) {
			if (+a !== 0) {
				var c = n("filter"),
					d = n("feGaussianBlur");
				this.attrs.blur = a;
				c.id = b.createUUID();
				n(d, {
					stdDeviation: +a || 1.5
				});
				c.appendChild(d);
				this.paper.defs.appendChild(c);
				this._blur = c;
				n(this.node, {
					filter: "url('" + b._url + "#" + c.id + "')"
				})
			} else this._blur && (this._blur.parentNode.removeChild(this._blur), delete this._blur, delete this.attrs.blur), this.node.removeAttribute("filter")
		};
		i.on = function(a, c) {
			if (this.removed) return this;
			var d = c;
			b._supportsTouch && (a = b._touchMap[a] || a === "click" && "touchstart" || a, d = function(a) {
				a.preventDefault();
				c()
			});
			this.node["on" + a] = d;
			return this
		};
		b._engine.group = function(a, b, c) {
			var d = n("g");
			c && c.canvas && c.canvas.appendChild(d) || a.canvas && a.canvas.appendChild(d);
			a = new j(d, a, c);
			a.type = "group";
			a.canvas = a.node;
			a.top = null;
			a.bottom = null;
			b && d.setAttribute("class", ["red", b, a.id].join("-"));
			return a
		};
		b._engine.circle = function(a, b, c, d, e) {
			var f = n("circle");
			e && e.canvas && e.canvas.appendChild(f) || a.canvas && a.canvas.appendChild(f);
			a = new j(f, a, e);
			a.attrs = {
				cx: b,
				cy: c,
				r: d,
				fill: "none",
				stroke: "#000"
			};
			a.type = "circle";
			n(f, a.attrs);
			return a
		};
		b._engine.rect = function(a, b, c, d, e, f, i) {
			var h = n("rect");
			i && i.canvas && i.canvas.appendChild(h) || a.canvas && a.canvas.appendChild(h);
			a = new j(h, a, i);
			a.attrs = {
				x: b,
				y: c,
				width: d,
				height: e,
				r: f || 0,
				rx: f || 0,
				ry: f || 0,
				fill: "none",
				stroke: "#000"
			};
			a.type = "rect";
			n(h, a.attrs);
			return a
		};
		b._engine.ellipse = function(a, b, c, d, e, f) {
			var i = n("ellipse");
			f && f.canvas && f.canvas.appendChild(i) || a.canvas && a.canvas.appendChild(i);
			a = new j(i, a, f);
			a.attrs = {
				cx: b,
				cy: c,
				rx: d,
				ry: e,
				fill: "none",
				stroke: "#000"
			};
			a.type = "ellipse";
			n(i, a.attrs);
			return a
		};
		b._engine.image = function(a, b, c, d, e, f, i) {
			var h = n("image");
			n(h, {
				x: c,
				y: d,
				width: e,
				height: f,
				preserveAspectRatio: "none"
			});
			h.setAttributeNS("http://www.w3.org/1999/xlink", "href", b);
			i && i.canvas && i.canvas.appendChild(h) || a.canvas && a.canvas.appendChild(h);
			a = new j(h, a, i);
			a.attrs = {
				x: c,
				y: d,
				width: e,
				height: f,
				src: b
			};
			a.type = "image";
			return a
		};
		b._engine.text = function(a, b, c, d, e) {
			var f = n("text");
			e && e.canvas && e.canvas.appendChild(f) || a.canvas && a.canvas.appendChild(f);
			a = new j(f, a, e);
			a.attrs = {
				x: b,
				y: c,
				"text-anchor": "middle",
				"vertical-align": "middle",
				text: d,
				stroke: "none",
				fill: "#000"
			};
			a.type = "text";
			a._textdirty = !0;
			C(a, a.attrs);
			return a
		};
		b._engine.setSize = function(a, b) {
			this.width = a || this.width;
			this.height = b || this.height;
			this.canvas.setAttribute("width", this.width);
			this.canvas.setAttribute("height", this.height);
			this._viewBox && this.setViewBox.apply(this, this._viewBox);
			return this
		};
		b._engine.create = function() {
			var a = b._getContainer.apply(0, arguments),
				c = a && a.container,
				d = a.x,
				e = a.y,
				f = a.width,
				a = a.height;
			if (!c) throw Error("SVG container not found.");
			var j = n("svg"),
				i, d = d || 0,
				e = e || 0,
				f = f || 512,
				a = a || 342;
			n(j, {
				height: a,
				version: 1.1,
				width: f,
				xmlns: "http://www.w3.org/2000/svg"
			});
			c == 1 ? (j.style.cssText = "overflow:hidden;-webkit-tap-highlight-color:rgba(0,0,0,0);-webkit-user-select:none;-moz-user-select:-moz-none;-khtml-user-select:none;-ms-user-select:none;user-select:none;-o-user-select:none;cursor:default;position:absolute;left:" + d + "px;top:" + e + "px", b._g.doc.body.appendChild(j), i = 1) : (j.style.cssText = "overflow:hidden;-webkit-tap-highlight-color:rgba(0,0,0,0);-webkit-user-select:none;-moz-user-select:-moz-none;-khtml-user-select:none;-ms-user-select:none;user-select:none;-o-user-select:none;cursor:default;position:relative", c.firstChild ? c.insertBefore(j, c.firstChild) : c.appendChild(j));
			c = new b._Paper;
			c.width = f;
			c.height = a;
			c.canvas = j;
			c.clear();
			c._left = c._top = 0;
			i && (c.renderfix = function() {});
			c.renderfix();
			return c
		};
		b._engine.setViewBox = function(a, b, c, d, e) {
			K("raphael.setViewBox", this, this._viewBox, [a, b, c, d, e]);
			var f = m(c / this.width, d / this.height),
				j = this.top,
				i;
			a == null ? (this._vbSize && (f = 1), delete this._vbSize, i = "0 0 " + this.width + " " + this.height) : (this._vbSize = f, i = a + " " + b + " " + c + " " + d);
			for (n(this.canvas, {
				viewBox: i,
				preserveAspectRatio: e ? "meet" : "xMinYMin"
			}); f && j;) i = "stroke-width" in j.attrs ? j.attrs["stroke-width"] : 1, j.attr({
				"stroke-width": i
			}), j._.dirty = 1, j._.dirtyT = 1, j = j.prev;
			this._viewBox = [a, b, c, d, !! e];
			return this
		};
		b.prototype.renderfix = function() {
			var a = this.canvas,
				b = a.style,
				c;
			try {
				c = a.getScreenCTM() || a.createSVGMatrix()
			} catch (d) {
				c = a.createSVGMatrix()
			}
			a = -c.e % 1;
			c = -c.f % 1;
			if (a || c) {
				if (a) this._left = (this._left + a) % 1, b.left = this._left + "px";
				if (c) this._top = (this._top + c) % 1, b.top = this._top + "px"
			}
		};
		b.prototype._desc = function(a) {
			var c = this.desc;
			if (c) for (; c.firstChild;) c.removeChild(c.firstChild);
			else this.desc = c = n("desc"), this.canvas.appendChild(c);
			c.appendChild(b._g.doc.createTextNode(b.is(a, "string") ? a : "Created with Red Rapha\u00ebl " + b.version))
		};
		b.prototype.clear = function() {
			K("raphael.clear", this);
			for (var a = this.canvas; a.firstChild;) a.removeChild(a.firstChild);
			this.bottom = this.top = null;
			this._desc(b.desc);
			a.appendChild(this.defs = n("defs"))
		};
		b.prototype.remove = function() {
			K("raphael.remove", this);
			this.canvas.parentNode && this.canvas.parentNode.removeChild(this.canvas);
			for (var a in this) this[a] = typeof this[a] == "function" ? b._removedFactory(a) : null;
			this.removed = !0
		};
		var c = b.st,
			d;
		for (d in i) i.hasOwnProperty(d) && !c.hasOwnProperty(d) && (c[d] = function(a) {
			return function() {
				var b = arguments;
				return this.forEach(function(c) {
					c[a].apply(c, b)
				})
			}
		}(d))
	}(window.Raphael);
	window.Raphael.vml &&
	function(b) {
		var h = String,
			e = parseFloat,
			g = Math,
			m = g.round,
			w = g.max,
			fa = g.min,
			s = g.sqrt,
			ka = g.abs,
			G = /[, ]+/,
			V = b.eve,
			K = {
				M: "m",
				L: "l",
				C: "c",
				Z: "x",
				m: "t",
				l: "r",
				c: "v",
				z: "x"
			},
			U = /([clmz]),?([^clmz]*)/gi,
			O = / progid:\S+Blur\([^\)]+\)/g,
			l = /-?[^,\s-]+/g,
			n = {
				path: 1,
				rect: 1,
				image: 1
			},
			F = {
				circle: 1,
				ellipse: 1
			},
			aa = function(c) {
				var d = /[ahqstv]/ig,
					a = b._pathToAbsolute;
				h(c).match(d) && (a = b._path2curve);
				d = /[clmz]/g;
				if (a == b._pathToAbsolute && !h(c).match(d)) return (c = h(c).replace(U, function(a, b, c) {
					var d = [],
						e = b.toLowerCase() == "m",
						f = K[b];
					c.replace(l, function(a) {
						e && d.length == 2 && (f += d + K[b == "m" ? "l" : "L"], d = []);
						d.push(m(a * 21600))
					});
					return f + d
				})) || "m0,0";
				for (var d = a(c), e, c = [], f = 0, j = d.length; f < j; f++) {
					a = d[f];
					e = d[f][0].toLowerCase();
					e == "z" && (e = "x");
					for (var i = 1, g = a.length; i < g; i++) e += m(a[i] * 21600) + (i != g - 1 ? "," : "");
					c.push(e)
				}
				return c.length ? c.join(" ") : "m0,0"
			},
			D = function(c, d, a) {
				var e = b.matrix();
				e.rotate(-c, 0.5, 0.5);
				return {
					dx: e.x(d, a),
					dy: e.y(d, a)
				}
			},
			M = function(b, d, a, e, f, j) {
				var i = b._,
					h = b.matrix,
					g = i.fillpos,
					b = b.node,
					s = b.style,
					n = 1,
					l = "",
					r = 21600 / d,
					v = 21600 / a;
				s.visibility = "hidden";
				if (d && a) {
					b.coordsize = ka(r) + " " + ka(v);
					s.rotation = j * (d * a < 0 ? -1 : 1);
					if (j) f = D(j, e, f), e = f.dx, f = f.dy;
					d < 0 && (l += "x");
					a < 0 && (l += " y") && (n = -1);
					s.flip = l;
					b.coordorigin = e * -r + " " + f * -v;
					if (g || i.fillsize) {
						e = (e = b.getElementsByTagName("fill")) && e[0];
						b.removeChild(e);
						if (g) f = D(j, h.x(g[0], g[1]), h.y(g[0], g[1])), e.position = f.dx * n + " " + f.dy * n;
						if (i.fillsize) e.size = i.fillsize[0] * ka(d) + " " + i.fillsize[1] * ka(a);
						b.appendChild(e)
					}
					s.visibility = ""
				}
			};
		b._url = "";
		b.toString = function() {
			return "Your browser doesn\u2019t support SVG. Falling down to VML.\nYou are running Rapha\u00ebl " + this.version
		};
		var Z = function(b, d, a) {
				for (var d = h(d).toLowerCase().split("-"), a = a ? "end" : "start", e = d.length, f = "classic", j = "medium", i = "medium"; e--;) switch (d[e]) {
				case "block":
				case "classic":
				case "oval":
				case "diamond":
				case "open":
				case "none":
					f = d[e];
					break;
				case "wide":
				case "narrow":
					i = d[e];
					break;
				case "long":
				case "short":
					j = d[e]
				}
				b = b.node.getElementsByTagName("stroke")[0];
				b[a + "arrow"] = f;
				b[a + "arrowlength"] = j;
				b[a + "arrowwidth"] = i
			},
			R = b._setFillAndStroke = function(c, d) {
				if (c.paper.canvas) {
					c.attrs = c.attrs || {};
					var a = c.node,
						k = c.attrs,
						j = a.style,
						i = n[c.type] && (d.x != k.x || d.y != k.y || d.width != k.width || d.height != k.height || d.cx != k.cx || d.cy != k.cy || d.rx != k.rx || d.ry != k.ry || d.r != k.r),
						g = F[c.type] && (k.cx != d.cx || k.cy != d.cy || k.r != d.r || k.rx != d.rx || k.ry != d.ry),
						s = c.type === "group",
						l;
					for (l in d) d.hasOwnProperty(l) && (k[l] = d[l]);
					if (i) k.path = b._getPath[c.type](c), c._.dirty = 1;
					d.href && (a.href = d.href);
					d.title && (a.title = d.title);
					d.target && (a.target = d.target);
					d.cursor && (j.cursor = d.cursor);
					"blur" in d && c.blur(d.blur);
					if (d.path && c.type == "path" || i) if (a.path = aa(~h(k.path).toLowerCase().indexOf("r") ? b._pathToAbsolute(k.path) : k.path), c.type == "image") c._.fillpos = [k.x, k.y], c._.fillsize = [k.width, k.height], M(c, 1, 1, 0, 0, 0);
					"transform" in d && c.transform(d.transform);
					if ("rotation" in d) j = d.rotation, b.is(j, "array") ? c.rotate.apply(c, j) : c.rotate(j);
					if ("shape-rendering" in d) a.style.antialias = d["shape-rendering"] !== "crisp";
					"visibility" in d && (d.visibility === "hidden" ? c.hide() : c.show());
					if (g) j = +k.cx, g = +k.cy, i = +k.rx || +k.r || 0, l = +k.ry || +k.r || 0, a.path = b.format("ar{0},{1},{2},{3},{4},{1},{4},{1}x", m((j - i) * 21600), m((g - l) * 21600), m((j + i) * 21600), m((g + l) * 21600), m(j * 21600));
					if ("clip-rect" in d) {
						j = h(d["clip-rect"]).split(G);
						if (j.length == 4) {
							j[0] = +j[0];
							j[1] = +j[1];
							j[2] = +j[2] + j[0];
							j[3] = +j[3] + j[1];
							i = s ? a : a.clipRect || b._g.doc.createElement("div");
							g = i.style;
							if (s) c.clip = j.slice(), i = c.matrix.offset(), i = [e(i[0]), e(i[1])], j[0] -= i[0], j[1] -= i[1], j[2] -= i[0], j[3] -= i[1], g.width = "10800px", g.height = "10800px";
							else if (!a.clipRect) g.top = "0", g.left = "0", g.width = c.paper.width + "px", g.height = c.paper.height + "px", a.parentNode.insertBefore(i, a), i.appendChild(a), a.clipRect = i;
							g.position = "absolute";
							g.clip = b.format("rect({1}px {2}px {3}px {0}px)", j)
						}
						if (!d["clip-rect"]) if (s && c.clip) a.style.clip = "rect(auto auto auto auto)", delete c.clip;
						else if (a.clipRect) a.clipRect.style.clip = "rect(auto auto auto auto)"
					}
					if (c.textpath) s = c.textpath.style, d.font && (s.font = d.font), d["font-family"] && (s.fontFamily = '"' + d["font-family"].split(",")[0].replace(/^['"]+|['"]+$/g, "") + '"'), d["font-size"] && (s.fontSize = d["font-size"]), d["font-weight"] && (s.fontWeight = d["font-weight"]), d["font-style"] && (s.fontStyle = d["font-style"]);
					"arrow-start" in d && Z(c, d["arrow-start"]);
					"arrow-end" in d && Z(c, d["arrow-end"], 1);
					if (d.opacity != null || d["stroke-width"] != null || d.fill != null || d.src != null || d.stroke != null || d["stroke-width"] != null || d["stroke-opacity"] != null || d["fill-opacity"] != null || d["stroke-dasharray"] != null || d["stroke-miterlimit"] != null || d["stroke-linejoin"] != null || d["stroke-linecap"] != null) {
						s = a.getElementsByTagName("fill");
						j = -1;
						s = s && s[0];
						!s && (s = f("fill"));
						if (c.type == "image" && d.src) s.src = d.src;
						d.fill && (s.on = !0);
						if (s.on == null || d.fill == "none" || d.fill === null) s.on = !1;
						if (s.on && d.fill) if (g = h(d.fill).match(b._ISURL)) s.parentNode == a && a.removeChild(s), s.rotate = !0, s.src = g[1], s.type = "tile", i = c.getBBox(1), s.position = i.x + " " + i.y, c._.fillpos = [i.x, i.y], b._preload(g[1], function() {
							c._.fillsize = [this.offsetWidth, this.offsetHeight]
						});
						else if (g = b.getRGB(d.fill), s.color = g.hex, s.src = "", s.type = "solid", g.error && (c.type in {
							circle: 1,
							ellipse: 1
						} || h(d.fill).charAt() != "r") && v(c, d.fill, s)) k.fill = "none", k.gradient = d.fill, s.rotate = !1;
						else if ("opacity" in g && !("fill-opacity" in d)) j = g.opacity;
						if (j !== -1 || "fill-opacity" in d || "opacity" in d) if (g = ((+k["fill-opacity"] + 1 || 2) - 1) * ((+k.opacity + 1 || 2) - 1) * ((+j + 1 || 2) - 1), g = fa(w(g, 0), 1), s.opacity = g, s.src) s.color = "none";
						a.appendChild(s);
						s = a.getElementsByTagName("stroke") && a.getElementsByTagName("stroke")[0];
						j = !1;
						!s && (j = s = f("stroke"));
						if (d.stroke && d.stroke != "none" || d["stroke-width"] || d["stroke-opacity"] != null || d["stroke-dasharray"] || d["stroke-miterlimit"] || d["stroke-linejoin"] || d["stroke-linecap"]) s.on = !0;
						(d.stroke == "none" || d.stroke === null || s.on == null || d.stroke == 0 || d["stroke-width"] == 0) && (s.on = !1);
						g = b.getRGB("stroke" in d ? d.stroke : k.stroke);
						s.on && d.stroke && (s.color = g.hex);
						g = ((+k["stroke-opacity"] + 1 || 2) - 1) * ((+k.opacity + 1 || 2) - 1) * ((+g.opacity + 1 || 2) - 1);
						i = (e(d["stroke-width"]) || 1) * 0.75;
						g = fa(w(g, 0), 1);
						d["stroke-width"] == null && (i = k["stroke-width"]);
						d["stroke-width"] && (s.weight = i);
						i && i < 1 && (g *= i) && (s.weight = 1);
						s.opacity = g;
						d["stroke-linejoin"] && (s.joinstyle = d["stroke-linejoin"]) || j && (j.joinstyle = "miter");
						s.miterlimit = d["stroke-miterlimit"] || 8;
						d["stroke-linecap"] && (s.endcap = d["stroke-linecap"] == "butt" ? "flat" : d["stroke-linecap"] == "square" ? "square" : "round");
						if (d["stroke-dasharray"]) g = {
							"-": "shortdash",
							".": "shortdot",
							"-.": "shortdashdot",
							"-..": "shortdashdotdot",
							". ": "dot",
							"- ": "dash",
							"--": "longdash",
							"- .": "dashdot",
							"--.": "longdashdot",
							"--..": "longdashdotdot"
						}, s.dashstyle = g.hasOwnProperty(d["stroke-dasharray"]) ? g[d["stroke-dasharray"]] : d["stroke-dasharray"].join && d["stroke-dasharray"].join(" ") || "";
						j && a.appendChild(s)
					}
					if (c.type == "text") {
						c.paper.canvas.style.display = "";
						a = c.paper.span;
						s = k.font && k.font.match(/\d+(?:\.\d*)?(?=px)/);
						g = k["line-height"] && (k["line-height"] + "").match(/\d+(?:\.\d*)?(?=px)/);
						j = a.style;
						k.font && (j.font = k.font);
						k["font-family"] && (j.fontFamily = k["font-family"]);
						k["font-weight"] && (j.fontWeight = k["font-weight"]);
						k["font-style"] && (j.fontStyle = k["font-style"]);
						s = e(k["font-size"] || s && s[0]) || 10;
						j.fontSize = s * 100 + "px";
						g = e(k["line-height"] || g && g[0]) || 12;
						k["line-height"] && (j.lineHeight = g * 100 + "px");
						c.textpath.string && (a.innerHTML = h(c.textpath.string).replace(/</g, "&#60;").replace(/&/g, "&#38;").replace(/\n/g, "<br>"));
						a = a.getBoundingClientRect();
						c.W = k.w = (a.right - a.left) / 100;
						c.H = k.h = (a.bottom - a.top) / 100;
						c.X = k.x;
						c.Y = k.y;
						switch (k["vertical-align"]) {
						case "top":
							c.bby = c.H / 2;
							break;
						case "bottom":
							c.bby = -c.H / 2;
							break;
						default:
							c.bby = 0
						}("x" in d || "y" in d || c.bby !== void 0) && (c.path.v = b.format("m{0},{1}l{2},{1}", m(k.x * 21600), m((k.y + (c.bby || 0)) * 21600), m(k.x * 21600) + 1));
						a = ["x", "y", "text", "font", "font-family", "font-weight", "font-style", "font-size", "line-height"];
						s = 0;
						for (j = a.length; s < j; s++) if (a[s] in d) {
							c._.dirty = 1;
							break
						}
						switch (k["text-anchor"]) {
						case "start":
							c.textpath.style["v-text-align"] = "left";
							c.bbx = c.W / 2;
							break;
						case "end":
							c.textpath.style["v-text-align"] = "right";
							c.bbx = -c.W / 2;
							break;
						default:
							c.textpath.style["v-text-align"] = "center", c.bbx = 0
						}
						c.textpath.style["v-text-kern"] = !0
					}
				}
			},
			v = function(c, d, a) {
				c.attrs = c.attrs || {};
				var f = Math.pow,
					j = "linear",
					i = ".5 .5";
				c.attrs.gradient = d;
				d = h(d).replace(b._radial_gradient, function(a, b) {
					j = "radial";
					var b = b && b.split(",") || [],
						c = b[3],
						d = b[4];
					c && d && (c = e(c), d = e(d), f(c - 0.5, 2) + f(d - 0.5, 2) > 0.25 && (d = s(0.25 - f(c - 0.5, 2)) * ((d > 0.5) * 2 - 1) + 0.5), i = c + " " + d);
					return ""
				});
				d = d.split(/\s*\-\s*/);
				if (j == "linear") {
					var g = d.shift(),
						g = -e(g);
					if (isNaN(g)) return null
				}
				d = b._parseDots(d);
				if (!d) return null;
				c = c.shape || c.node;
				if (d.length) {
					c.removeChild(a);
					a.on = !0;
					a.method = "none";
					a.color = d[0].color;
					a.color2 = d[d.length - 1].color;
					for (var l = [], n = 1, r = d[0].opacity === void 0 ? 1 : d[0].opacity, v = 0, m = d.length; v < m; v++) if (d[v].offset && l.push(d[v].offset + " " + d[v].color), d[v].opacity !== void 0) n = d[v].opacity;
					a.colors = l.length ? l.join() : "0% " + a.color;
					a.opacity = n;
					a["o:opacity2"] = r;
					j == "radial" ? (a.type = "gradientTitle", a.focus = "100%", a.focussize = "0 0", a.focusposition = i, a.angle = 0) : (a.type = "gradient", a.angle = (270 - g) % 360);
					c.appendChild(a)
				}
				return 1
			},
			C = function(c, d, a) {
				a = a || d;
				this.node = this[0] = c;
				c.raphael = !0;
				c.raphaelid = this.id = b._oid++;
				this.Y = this.X = 0;
				this.attrs = this.attrs || {};
				this.styles = this.styles || {};
				this.followers = this.followers || [];
				this.paper = d;
				this.ca = this.customAttributes = this.customAttributes || new d._CustomAttributes;
				this.matrix = b.matrix();
				this._ = {
					transform: [],
					sx: 1,
					sy: 1,
					dx: 0,
					dy: 0,
					deg: 0,
					dirty: 1,
					dirtyT: 1
				};
				this.parent = a;
				!a.bottom && (a.bottom = this);
				(this.prev = a.top) && (a.top.next = this);
				a.top = this;
				this.next = null
			},
			g = b.el;
		C.prototype = g;
		g.constructor = C;
		g.transform = function(c) {
			if (c == null) return this._.transform;
			var d = this.paper._viewBoxShift,
				a = d ? "s" + [d.scale, d.scale] + "-1-1t" + [d.dx, d.dy] : "",
				e;
			d && (e = c = h(c).replace(/\.{3}|\u2026/g, this._.transform || ""));
			b._extractTransform(this, a + c);
			var d = this.matrix.clone(),
				f = this.skew,
				c = this.node,
				a = ~h(this.attrs.fill).indexOf("-"),
				j = !h(this.attrs.fill).indexOf("url(");
			d.translate(-0.5, -0.5);
			j || a || this.type == "image" ? (f.matrix = "1 0 0 1", f.offset = "0 0", f = d.split(), a && f.noRotation || !f.isSimple ? (c.style.filter = d.toFilter(), d = this.getBBox(), a = this.getBBox(1), j = d.x2 && a.x2 && "x2" || "x", f = d.y2 && a.y2 && "y2" || "y", j = d[j] - a[j], d = d[f] - a[f], c.coordorigin = j * -21600 + " " + d * -21600, M(this, 1, 1, j, d, 0)) : (c.style.filter = "", M(this, f.scalex, f.scaley, f.dx, f.dy, f.rotate))) : (c.style.filter = "", f.matrix = h(d), f.offset = d.offset());
			e && (this._.transform = e);
			return this
		};
		g.rotate = function(b, d, a) {
			if (this.removed) return this;
			if (b != null) {
				b = h(b).split(G);
				b.length - 1 && (d = e(b[1]), a = e(b[2]));
				b = e(b[0]);
				a == null && (d = a);
				if (d == null || a == null) a = this.getBBox(1), d = a.x + a.width / 2, a = a.y + a.height / 2;
				this._.dirtyT = 1;
				this.transform(this._.transform.concat([
					["r", b, d, a]
				]));
				return this
			}
		};
		g.translate = function(b, d) {
			if (this.removed) return this;
			b = h(b).split(G);
			b.length - 1 && (d = e(b[1]));
			b = e(b[0]) || 0;
			d = +d || 0;
			this._.bbox && (this._.bbox.x += b, this._.bbox.y += d);
			this.transform(this._.transform.concat([
				["t", b, d]
			]));
			return this
		};
		g.scale = function(b, d, a, f) {
			if (this.removed) return this;
			b = h(b).split(G);
			b.length - 1 && (d = e(b[1]), a = e(b[2]), f = e(b[3]), isNaN(a) && (a = null), isNaN(f) && (f = null));
			b = e(b[0]);
			d == null && (d = b);
			f == null && (a = f);
			if (a == null || f == null) var j = this.getBBox(1);
			a = a == null ? j.x + j.width / 2 : a;
			f = f == null ? j.y + j.height / 2 : f;
			this.transform(this._.transform.concat([
				["s", b, d, a, f]
			]));
			this._.dirtyT = 1;
			return this
		};
		g.hide = function() {
			!this.removed && (this.node.style.display = "none");
			return this
		};
		g.show = function() {
			!this.removed && (this.node.style.display = "");
			return this
		};
		g._getBBox = function() {
			if (this.removed) return {};
			return {
				x: this.X + (this.bbx || 0) - this.W / 2,
				y: this.Y + (this.bby || 0) - this.H / 2,
				width: this.W,
				height: this.H
			}
		};
		g.remove = function() {
			if (!this.removed && this.parent.canvas) {
				var c, d = b._engine.getNode(this);
				this.paper.__set__ && this.paper.__set__.exclude(this);
				for (V.unbind("raphael.*.*." + this.id); c = this.followers.pop();) c.el.remove();
				this.shape && this.shape.parentNode.removeChild(this.shape);
				d.parentNode.removeChild(d);
				b._tear(this, this.paper);
				for (c in this) this[c] = typeof this[c] == "function" ? b._removedFactory(c) : null;
				this.removed = !0
			}
		};
		g.css = function(c, d) {
			if (this.removed) return this;
			if (d == null && b.is(c, "string")) {
				for (var a = c.split(G), e = {}, f = 0, j = a.length; f < j; f++) c = a[f], c in this.styles && (e[c] = this.styles[c]);
				return j - 1 ? e : e[a[0]]
			}
			if (d == null && b.is(c, "array")) {
				e = {};
				f = 0;
				for (j = c.length; f < j; f++) e[c[f]] = this.styles(c[f]);
				return e
			}
			d != null ? (a = {}, a[c] = d) : c != null && b.is(c, "object") && (a = c);
			e = {};
			for (j in a) f = j.replace(/\B([A-Z]{1})/g, "-$1").toLowerCase(), f === "color" && this.type === "text" && (f = "fill"), b._availableAttrs.hasOwnProperty(f) ? (e[f] = a[j], e.dirty = !0) : (V("raphael.css." + f + "." + this.id, this, a[j], f), a[j] != void 0 && (this.node.style[f] = a[j]), this.styles[f] = a[j]);
			f = 0;
			for (j = this.followers.length; f < j; f++) this.followers[f].el.css(a);
			e.hasOwnProperty("dirty") && (delete e.dirty, this.attr(e));
			return this
		};
		g.attr = function(c, d) {
			if (this.removed) return this;
			if (c == null) {
				var a = {},
					e;
				for (e in this.attrs) this.attrs.hasOwnProperty(e) && (a[e] = this.attrs[e]);
				a.gradient && a.fill == "none" && (a.fill = a.gradient) && delete a.gradient;
				a.transform = this._.transform;
				a.visibility = this.node.style.display === "none" ? "hidden" : "visible";
				return a
			}
			if (d == null && b.is(c, "string")) {
				if (c == "fill" && this.attrs.fill == "none" && this.attrs.gradient) return this.attrs.gradient;
				if (c == "visibility") return this.node.style.display === "none" ? "hidden" : "visible";
				var a = c.split(G),
					f = {},
					j = 0;
				for (e = a.length; j < e; j++) c = a[j], f[c] = c in this.attrs ? this.attrs[c] : b.is(this.ca[c], "function") ? this.ca[c].def : b._availableAttrs[c];
				return e - 1 ? f : f[a[0]]
			}
			if (this.attrs && d == null && b.is(c, "array")) {
				f = {};
				j = 0;
				for (e = c.length; j < e; j++) f[c[j]] = this.attr(c[j]);
				return f
			}
			d != null && (a = {}, a[c] = d);
			d == null && b.is(c, "object") && (a = c);
			for (j in a) V("raphael.attr." + j + "." + this.id, this, a[j], j);
			if (a) {
				var i = {};
				for (j in this.ca) if (this.ca[j] && a.hasOwnProperty(j) && b.is(this.ca[j], "function") && !this.ca["_invoked" + j]) {
					this.ca["_invoked" + j] = !0;
					e = this.ca[j].apply(this, [].concat(a[j]));
					delete this.ca["_invoked" + j];
					for (f in e) e.hasOwnProperty(f) && (a[f] = e[f]);
					this.attrs[j] = a[j];
					e === !1 && (i[j] = a[j], delete a[j])
				}
				if ("text" in a && this.type == "text") this.textpath.string = a.text.replace(/<br\s*?\/?>/ig, "\n");
				R(this, a);
				var g, j = 0;
				for (e = this.followers.length; j < e; j++) g = this.followers[j], g.cb && !g.cb.call(g.el, a, this) || g.el.attr(a);
				for (f in i) a[f] = i[f]
			}
			return this
		};
		g.blur = function(c) {
			var d = this.node.runtimeStyle,
				a = d.filter,
				a = a.replace(O, ""); + c !== 0 ? (this.attrs.blur = c, d.filter = a + "  progid:DXImageTransform.Microsoft.Blur(pixelradius=" + (+c || 1.5) + ")", d.margin = b.format("-{0}px 0 0 -{0}px", m(+c || 1.5))) : (d.filter = a, d.margin = 0, delete this.attrs.blur);
			return this
		};
		g.on = function(c, d) {
			if (this.removed) return this;
			this.node["on" + c] = function() {
				var a = b._g.win.event;
				a.target = a.srcElement;
				d(a)
			};
			return this
		};
		b._engine.getNode = function(b) {
			b = b.node || b[0].node;
			return b.clipRect || b
		};
		b._engine.getLastNode = function(b) {
			b = b.node || b[b.length - 1].node;
			return b.clipRect || b
		};
		b._engine.group = function(c, d, a) {
			var e = b._g.doc.createElement("div"),
				f = new C(e, c, a);
			e.style.cssText = "position:absolute;left:0;top:0;width:1px;height:1px";
			d && (e.className = ["red", d, f.id].join("-"));
			(a || c).canvas.appendChild(e);
			f.type = "group";
			f.canvas = f.node;
			f.transform = b._engine.group.transform;
			f.top = null;
			f.bottom = null;
			return f
		};
		b._engine.group.transform = function(c) {
			if (c == null) return this._.transform;
			var d = this.node.style,
				a = this.clip,
				f = this.paper._viewBoxShift,
				j = f ? "s" + [f.scale, f.scale] + "-1-1t" + [f.dx, f.dy] : "";
			f && (c = h(c).replace(/\.{3}|\u2026/g, this._.transform || ""));
			b._extractTransform(this, j + c);
			c = this.matrix;
			j = c.offset();
			f = e(j[0]) || 0;
			j = e(j[1]) || 0;
			d.left = f + "px";
			d.top = j + "px";
			d.zoom = (this._.tzoom = c.get(0)) + "";
			a && (d.clip = b.format("rect({1}px {2}px {3}px {0}px)", [a[0] - f, a[1] - j, a[2] - f, a[3] - j]));
			return this
		};
		b._engine.path = function(b, d, a) {
			var e = f("shape");
			e.style.cssText = "position:absolute;left:0;top:0;width:1px;height:1px";
			e.coordsize = "21600 21600";
			e.coordorigin = d.coordorigin;
			var j = new C(e, d, a),
				i = {
					fill: "none",
					stroke: "#000"
				};
			b && (i.path = b);
			j.type = "path";
			j.path = [];
			j.Path = "";
			R(j, i);
			(a || d).canvas.appendChild(e);
			b = f("skew");
			b.on = !0;
			e.appendChild(b);
			j.skew = b;
			return j
		};
		b._engine.rect = function(c, d, a, e, f, j, i) {
			var g = b._rectPath(d, a, e, f, j),
				c = c.path(g, i),
				i = c.attrs;
			c.X = i.x = d;
			c.Y = i.y = a;
			c.W = i.width = e;
			c.H = i.height = f;
			i.r = j;
			i.path = g;
			c.type = "rect";
			return c
		};
		b._engine.ellipse = function(b, d, a, e, f, j) {
			b = b.path(void 0, j);
			b.X = d - e;
			b.Y = a - f;
			b.W = e * 2;
			b.H = f * 2;
			b.type = "ellipse";
			R(b, {
				cx: d,
				cy: a,
				rx: e,
				ry: f
			});
			return b
		};
		b._engine.circle = function(b, d, a, e, f) {
			b = b.path(void 0, f);
			b.X = d - e;
			b.Y = a - e;
			b.W = b.H = e * 2;
			b.type = "circle";
			R(b, {
				cx: d,
				cy: a,
				r: e
			});
			return b
		};
		b._engine.image = function(c, d, a, e, f, j, i) {
			var g = b._rectPath(a, e, f, j),
				c = c.path(g, i).attr({
					stroke: "none"
				}),
				i = c.attrs,
				s = c.node,
				h = s.getElementsByTagName("fill")[0];
			i.src = d;
			c.X = i.x = a;
			c.Y = i.y = e;
			c.W = i.width = f;
			c.H = i.height = j;
			i.path = g;
			c.type = "image";
			h.parentNode == s && s.removeChild(h);
			h.rotate = !0;
			h.src = d;
			h.type = "tile";
			c._.fillpos = [a, e];
			c._.fillsize = [f, j];
			s.appendChild(h);
			M(c, 1, 1, 0, 0, 0);
			return c
		};
		b._engine.text = function(c, d, a, e, j) {
			var i = f("shape"),
				g = f("path"),
				s = f("textpath"),
				d = d || 0,
				a = a || 0;
			g.v = b.format("m{0},{1}l{2},{1}", m(d * 21600), m(a * 21600), m(d * 21600) + 1);
			g.textpathok = !0;
			s.string = h(e).replace(/<br\s*?\/?>/ig, "\n");
			s.on = !0;
			i.style.cssText = "position:absolute;left:0;top:0;width:1px;height:1px";
			i.coordsize = "21600 21600";
			i.coordorigin = "0 0";
			var l = new C(i, c, j),
				n = {
					fill: "#000",
					stroke: "none",
					text: e
				};
			l.shape = i;
			l.path = g;
			l.textpath = s;
			l.type = "text";
			l.attrs.text = h(e || "");
			l.attrs.x = d;
			l.attrs.y = a;
			l.attrs.w = 1;
			l.attrs.h = 1;
			R(l, n);
			i.appendChild(s);
			i.appendChild(g);
			(j || c).canvas.appendChild(i);
			c = f("skew");
			c.on = !0;
			i.appendChild(c);
			l.skew = c;
			return l
		};
		b._engine.setSize = function(c, d) {
			var a = this.canvas.style;
			this.width = c;
			this.height = d;
			c == +c && (c += "px");
			d == +d && (d += "px");
			a.width = c;
			a.height = d;
			a.clip = "rect(0 " + c + " " + d + " 0)";
			this._viewBox && b._engine.setViewBox.apply(this, this._viewBox);
			return this
		};
		b._engine.setViewBox = function(b, d, a, e, f) {
			V("raphael.setViewBox", this, this._viewBox, [b, d, a, e, f]);
			var j = this.width,
				i = this.height,
				g = 1 / w(a / j, e / i),
				s, h;
			f && (s = i / e, h = j / a, a * s < j && (b -= (j - a * s) / 2 / s), e * h < i && (d -= (i - e * h) / 2 / h));
			this._viewBox = [b, d, a, e, !! f];
			this._viewBoxShift = {
				dx: -b,
				dy: -d,
				scale: g
			};
			this.forEach(function(a) {
				a.transform("...")
			});
			return this
		};
		var f;
		b._engine.initWin = function(c) {
			var d = c.document;
			d.createStyleSheet().addRule(".rvml", "behavior:url(#default#VML)");
			try {
				!d.namespaces.rvml && d.namespaces.add("rvml", "urn:schemas-microsoft-com:vml"), f = b._createNode = function(a, b) {
					var c = d.createElement("<rvml:" + a + ' class="rvml">'),
						e;
					for (e in b) c[e] = h(b[e]);
					return c
				}
			} catch (a) {
				f = b._createNode = function(a, b) {
					var c = d.createElement("<" + a + ' xmlns="urn:schemas-microsoft.com:vml" class="rvml">'),
						e;
					for (e in b) c[e] = h(b[e]);
					return c
				}
			}
		};
		b._engine.initWin(b._g.win);
		b._engine.create = function() {
			var c = b._getContainer.apply(0, arguments),
				d = c.container,
				a = c.height,
				e = c.width,
				f = c.x,
				c = c.y;
			if (!d) throw Error("VML container not found.");
			var j = new b._Paper,
				i = j.canvas = b._g.doc.createElement("div"),
				g = i.style,
				f = f || 0,
				c = c || 0,
				e = e || 512,
				a = a || 342;
			j.width = e;
			j.height = a;
			e == +e && (e += "px");
			a == +a && (a += "px");
			j.coordsize = "21600000 21600000";
			j.coordorigin = "0 0";
			j.span = b._g.doc.createElement("span");
			j.span.style.cssText = "position:absolute;left:-9999em;top:-9999em;padding:0;margin:0;line-height:1;";
			i.appendChild(j.span);
			g.cssText = b.format("top:0;left:0;width:{0};height:{1};display:inline-block;cursor:default;position:relative;clip:rect(0 {0} {1} 0);overflow:hidden", e, a);
			d == 1 ? (b._g.doc.body.appendChild(i), g.left = f + "px", g.top = c + "px", g.position = "absolute") : d.firstChild ? d.insertBefore(i, d.firstChild) : d.appendChild(i);
			j.renderfix = function() {};
			return j
		};
		b.prototype.clear = function() {
			V("raphael.clear", this);
			this.canvas.innerHTML = "";
			this.span = b._g.doc.createElement("span");
			this.span.style.cssText = "position:absolute;left:-9999em;top:-9999em;padding:0;margin:0;line-height:1;display:inline;";
			this.canvas.appendChild(this.span);
			this.bottom = this.top = null
		};
		b.prototype.remove = function() {
			V("raphael.remove", this);
			this.canvas.parentNode.removeChild(this.canvas);
			for (var c in this) this[c] = typeof this[c] == "function" ? b._removedFactory(c) : null;
			return !0
		};
		var j = b.st,
			i;
		for (i in g) g.hasOwnProperty(i) && !j.hasOwnProperty(i) && (j[i] = function(b) {
			return function() {
				var d = arguments;
				return this.forEach(function(a) {
					a[b].apply(a, d)
				})
			}
		}(i))
	}(window.Raphael);
	g.Raphael = w;
	g.Raphael.desc = "";
	if (U && U !== w) window.Raphael = U;
	else if (window.Raphael === w) window.Raphael = void 0
}]);
FusionCharts(["private", "modules.renderer.js-raphaelshadow", function() {
	var g = this.hcLib,
		h = window,
		m = h.navigator.userAgent,
		U = h.Math,
		w = U.sqrt,
		S = h.parseFloat,
		ia = h.parseInt;
	/AppleWebKit/.test(m);
	/Safari/.test(m) && /Version\/[1-4]\./.test(m);
	/Chrome/.test(m);
	/msie/i.test(m);
	var h = h.SVGFilterElement || h.SVGFEColorMatrixElement && h.SVGFEColorMatrixElement.SVG_FECOLORMATRIX_TYPE_SATURATE === 2,
		b = g.Raphael,
		U = Math,
		B = b._createNode,
		e;
	if (b.svg) {
		if (h) b.el.dropshadow = function(e, g, h, s) {
			var r = this.node,
				m = this._.shadowFilter,
				V = this.paper.cacheShadows || (this.paper.cacheShadows = {}),
				K = "drop-shadow" + [e, g, h, s].join(" "),
				U;
			if (e === "none") {
				if (m) {
					m.use -= 1;
					this.node.removeAttribute("filter");
					if (!m.use) {
						K = m.hash;
						for (U in m) e = m[U], e.parentNode && e.parentNode.removeChild(e), delete m[U];
						delete V[K]
					}
					delete this._.shadowFilter
				}
			} else if (!(m && V[K] === m)) m = this.paper.defs.appendChild(B("filter", {
				id: b.createUUID(),
				width: "200%",
				height: "200%"
			})), s = b.color(s), s.error && (s = b.color("rgba(0,0,0,1)")), U = b.pick(s.opacity, 1), this._.shadowFilter = V[K] = {
				use: 1,
				filter: m,
				hash: K,
				offset: m.appendChild(B("feOffset", {
					result: "offOut",
					"in": "SourceGraphic",
					dx: S(e),
					dy: S(g)
				})),
				matrix: m.appendChild(B("feColorMatrix", {
					result: "matrixOut",
					"in": "offOut",
					type: "matrix",
					values: "0 0 0 0 " + s.r / 255 + " 0 0 0 0 " + s.g / 255 + " 0 0 0 0 " + s.b / 255 + " 0 0 0 " + U + " 0"
				})),
				blur: m.appendChild(B("feGaussianBlur", {
					result: "blurOut",
					"in": "matrixOut",
					stdDeviation: w(S(h))
				})),
				blend: m.appendChild(B("feComposite", {
					"in": "SourceGraphic",
					in2: "blurOut",
					operator: "over"
				}))
			}, r.setAttribute("filter", "url('" + b._url + "#" + m.id + "')");
			return this
		};
		var r = {
			"drop-shadow": "drop-shadow",
			stroke: "stroke",
			fill: "fill",
			"stroke-width": "stroke-width",
			"stroke-opacity": "stroke-opacity",
			"stroke-linecap": "stroke-linecap",
			"shape-rendering": "shape-rendering",
			transform: "transform"
		};
		e = function(b, e) {
			var g = this.__shadowscale,
				s = {},
				h, m;
			for (m in b) switch (r[m] && (s[m] = b[m], delete b[m]), m) {
			case "transform":
				h = e.matrix.clone();
				h.translate(this.__shadowx, this.__shadowy);
				this.transform(h.toTransformString());
				break;
			case "stroke-width":
				b[m] = ((s[m] || 1) + 6 - 2 * this.__shadowlevel) * g
			}
			this.attr(b);
			for (m in s) b[m] = s[m]
		};
		b.ca["drop-shadow"] = function(g, h, m, s, r, w) {
			var m = this._.shadows || (this._.shadows = []),
				B, K, U, O, l;
			if (!this.__shadowblocked) if (g === "none") for (; K = m.pop();) K.remove();
			else {
				s = b.color(s);
				s.error && (s = b.color("rgba(0,0,0,1)"));
				r instanceof Array ? (B = r[0], r = r[1]) : B = r;
				B = 1 / b.pick(B, 1);
				r = 1 / b.pick(r, 1);
				g = b.pick(g, 1) * B;
				h = b.pick(h, 1) * B;
				B = b.pick(s.opacity, 1) * 0.05;
				U = ia(this.attr("stroke-width") || 1, 10) + 6;
				O = this.matrix.clone();
				O.translate(g, h);
				for (l = 1; l <= 3; l++) K = (m[l - 1] || this.clone().follow(this, e, !w && "before")).attr({
					stroke: s.hex,
					"stroke-opacity": B * l,
					"stroke-width": (U - 2 * l) * r,
					transform: O.toTransformString(),
					"stroke-linecap": "round",
					"shape-rendering": "default",
					fill: "none"
				}), K.__shadowlevel = l, K.__shadowscale = r, K.__shadowx = g, K.__shadowy = h, w && w.appendChild(K), m.push(K)
			}
			return !1
		};
		b.el.shadow = function(e, g, h, s) {
			var m;
			h && h.constructor === b.el.constructor && (s = h, h = void 0);
			if (typeof e === "object") g && g.constructor === b.el.constructor && (s = g), g = e.opacity, h = e.scalefactor, m = !! e.useFilter, e = e.apply === void 0 ? !! g : e.apply;
			g === void 0 && (g = 1);
			if (this.dropshadow) if (m) return e && this.dropshadow(1, 1, 3, "rgb(64,64,64)") || this.dropshadow("none"), this;
			else this._.shadowFilter && this.dropshadow("none");
			return this.attr("drop-shadow", e ? [1, 1, 3, "rgba(64,64,64," + g + ")", h, s] : "none")
		}
	} else if (b.vml) b.ca["drop-shadow"] = function(e, g, h, s, m, r) {
		var w = this._.shadow,
			B, U;
		if (this.isShadow) return !1;
		if (e === "none") w && (this._.shadow = w.remove());
		else {
			if (!w) w = this._.shadow = this.clone(), r && r.appendChild(w.follow(this)) || w.follow(this, void 0, "before"), w.attr({
				fill: "none",
				"fill-opacity": 0.5,
				"stroke-opacity": 1
			}).isShadow = !0, w.attr("stroke-width") <= 0 && w.attr("stroke-width", 1);
			r = w.node.runtimeStyle;
			B = r.filter.replace(/ progid:\S+Blur\([^\)]+\)/g, "");
			s = b.color(s);
			s.error && (s = b.color("rgba(0,0,0,1)"));
			U = b.pick(s.opacity, 1) / 5;
			m = 1 / b.pick(m, 1);
			e = b.pick(e, 1) * m;
			g = b.pick(g, 1) * m;
			w.translate(e, g);
			r.filter = B + " progid:DXImageTransform.Microsoft.Blur(pixelRadius=" + S(h * 0.4) + " makeShadow=True Color=" + s.hex + " shadowOpacity='" + U + "');"
		}
		return !1
	}, b.el.shadow = function(e, g, h, s) {
		h && h.constructor === b.el.constructor && (s = h, h = void 0);
		if (typeof e === "object") g && g.type === "group" && (s = g), g = e.opacity, h = e.scalefactor, e = e.apply === void 0 ? !! g : e.apply;
		g === void 0 && (g = 1);
		return this.attr("drop-shadow", e || !g ? [1, 1, 5, "rgba(64,64,64," + g + ")", h, s] : "none")
	};
	else if (b.canvas) b.el.shadow = function() {
		return this
	}
}]);
FusionCharts(["private", "modules.renderer.js-raphaelshapes", function() {
	var g = this.hcLib.Raphael,
		h = "createTouch" in document,
		m = window,
		m = /msie/i.test(navigator.userAgent) && !m.opera,
		U = Math,
		w = U.cos,
		S = U.sin,
		ia = U.abs,
		b = U.pow,
		B = U.atan2,
		e = U.min,
		r = U.round,
		x = U.PI,
		$ = 2 * x,
		fa = parseInt,
		s = parseFloat,
		ka = String,
		G = "fill",
		G = "fill",
		V = b(2, -24),
		K = "rgba(192,192,192," + (m ? 0.002 : 1.0E-6) + ")",
		Y = g.eve,
		O = g.vml && 0.5 || 0,
		l = g._createNode,
		n = g._setFillAndStroke,
		F = g.el.constructor;
	g.crispBound = g._cacher(function(b, e, i, c, d) {
		var a = {},
			k, b = b || 0,
			e = e || 0,
			i = i || 0,
			c = c || 0,
			d = d || 0;
		k = d % 2 / 2 + O;
		a.x = r(b + k) - k;
		a.y = r(e + k) - k;
		a.width = r(b + i + k) - k - a.x;
		a.height = r(e + c + k) - k - a.y;
		a["stroke-width"] = d;
		a.width === 0 && i !== 0 && (a.width = 1);
		a.height === 0 && c !== 0 && (a.height = 1);
		return a
	}, g);
	g.el.crisp = function() {
		var b = this.attrs,
			e, i = this.attr(["x", "y", "width", "height", "stroke-width"]),
			i = g.crispBound(i.x, i.y, i.width, i.height, i["stroke-width"]);
		for (e in i) b[e] === i[e] && delete i[e];
		return this.attr(i)
	};
	g.fn.polypath = function() {
		var b = arguments,
			e = b.length - 1,
			i = b[e];
		i && i.constructor === g.el.constructor ? b[e] = void 0 : i = void 0;
		e = this.path(void 0, i);
		e.ca.polypath = g.fn.polypath.ca;
		arguments.length - !! i && e.attr("polypath", [b[0], b[1], b[2], b[3], b[4], b[5]]) || (e.attrs.polypath = [0, 0, 0, 0, 0, 0]);
		return e
	};
	g.fn.polypath.ca = function(b, e, i, c, d, a) {
		var k, h, l;
		k = [];
		b = fa(b, 10) || 0;
		e = s(e) || 0;
		i = s(i) || 0;
		c = s(c) || 0;
		d = d === null || isNaN(d) ? x * 0.5 : g.rad(d);
		a = a === null || isNaN(a) ? 0 : s(a);
		h = d;
		if (b > 2) switch (d = 2 * x / b, a) {
		case 0:
			for (a = 0; a < b; a++) k.push("L", e + c * w(-h), i + c * S(-h)), h += d;
			k[0] = "M";
			k.push("Z");
			break;
		case 1:
			for (a = 0; a < b; a++) k.push("M", e, i, "L", e + c * w(-h), i + c * S(-h)), h += d;
			break;
		default:
			d *= 0.5;
			l = c * w(d) * (1 - a);
			for (a = 0; a < b; a++) k.push("L", e + c * w(-h), i + c * S(-h)), h += d, k.push("L", e + l * w(-h), i + l * S(-h)), h += d;
			k[0] = "M";
			k.push("Z")
		} else c === 0 ? k.push("M", e, i, "L", e, i, "Z") : k.push("M", e - c, i, "A", c, c, 0, 0, 0, e + c, i, "A", c, c, 0, 0, 0, e - c, i, "Z");
		return {
			path: k
		}
	};
	g.fn.ringpath = function() {
		var b = arguments,
			e = b.length - 1,
			i = b[e];
		i && i.constructor === g.el.constructor ? b[e] = void 0 : i = void 0;
		e = this.path(void 0, i);
		e.ca.ringpath = g.fn.ringpath.ca;
		arguments.length - !! i && e.attr("ringpath", [b[0], b[1], b[2], b[3], b[4], b[5]]) || (e.attrs.ringpath = [0, 0, 0, 0, 0, 0]);
		return e
	};
	g.fn.ringpath.ca = function(b, e, i, c, d, a) {
		var k = a % $ - d % $,
			g = a - d,
			h;
		this._.ringangle = (d + a) * 0.5;
		ia(g) < V ? (g = w(d), d = S(d), i = ["M", b + i * g, e + i * d, "L", b + c * g, e + c * d, "Z"]) : (ia(g) > V && ia(g) % $ < V ? (i = ["M", b - i, e, "A", i, i, 0, 0, 0, b + i, e, "A", i, i, 0, 0, 0, b - i, e], c !== 0 && (i = i.concat(["M", b - c, e, "A", c, c, 0, 0, 1, b + c, e, "A", c, c, 0, 0, 1, b - c, e]))) : (g = w(d), d = S(d), h = w(a), a = S(a), k %= $, k < 0 && (k += $), k = k < x ? 0 : 1, i = ["M", b + i * g, e + i * d, "A", i, i, 0, k, 1, b + i * h, e + i * a, "L", b + c * h, e + c * a], c !== 0 && i.push("A", c, c, 0, k, 0, b + c * g, e + c * d)), i.push("Z"));
		return {
			path: i
		}
	};
	g.fn.cubepath = function() {
		var b = {
			"stroke-linejoin": "round",
			"shape-rendering": "precision",
			stroke: "none"
		},
			e = arguments,
			i = e.length - 1,
			c = e[i],
			d;
		c && c.constructor === g.el.constructor ? e[i] = void 0 : c = void 0;
		i = this.path(void 0, c).attr(b);
		d = this.path(void 0, c).attr(b);
		b = this.path(void 0, c).attr(b);
		b._.cubetop = i.follow(b, void 0, "before");
		b._.cubeside = d.follow(b, void 0, "before");
		for (var a in g.fn.cubepath.ca) b.ca[a] = g.fn.cubepath.ca[a];
		return b.attr("cubepath", [e[0] || 0, e[1] || 0, e[2] || 0, e[3] || 0, e[4] || 0, e[5] || 0])
	};
	g.fn.cubepath.ca = {
		cubepath: function(b, e, i, c, d, a) {
			var k = this._.cubetop,
				g = this._.cubeside;
			this.attr("path", ["M", b + i, e, "l", 0, c, -i, 0, 0, -c, "z"]);
			k.attr("path", ["M", b, e, "l", 1, 1, i - 1, 0, 0, -1, d, -a, -i, 0, "z"]);
			g.attr("path", ["M", b + i - 1, e + 1, "l", 0, c - 1, 1, 0, d, -a, 0, -c, -d, a]);
			return !1
		},
		"stroke-linejoin": function() {
			return {
				"stroke-linejoin": "round"
			}
		},
		"drop-shadow": function(b, e, i, c) {
			var d = this._.cubetop,
				a = this._.cubeside;
			this.dropshadow && (d.dropshadow(b, -e, i, c), a.dropshadow(b, -e, i, c));
			return !1
		},
		fill: function(b, e) {
			var i = this._.cubetop,
				c = this._.cubeside,
				d = this.attr("cubepath") || [0, 0, 0, 0, 0, 0],
				a = d[2],
				k = d[4],
				d = d[5],
				h, b = g.color(b);
			e ? (this.attr(G, b), i.attr(G, g.tintshade(b, -0.78).rgba), c.attr(G, g.tintshade(b, -0.65).rgba)) : (h = "opacity" in b ? "rgba(" + [b.r, b.g, b.b, b.opacity] + ")" : "rgb(" + [b.r, b.g, b.b] + ")", this.attr(G, [270, g.tintshade(h, 0.55).rgba, g.tintshade(h, -0.65).rgba].join("-")), c.attr(G, [270, g.tintshade(h, -0.75).rgba, g.tintshade(h, -0.35).rgba].join("-")), i.attr(G, [45 + g.deg(B(d, k + a)), g.tintshade(h, -0.78).rgba, g.tintshade(h, 0.22).rgba].join("-")));
			return !1
		}
	};
	g.ca["text-bound"] = function(b, e, i, c, d, a) {
		var c = this.paper,
			k = this._.textbound;
		if (this.type === "text") {
			if ((!e || e === "none") && (!b || b === "none")) return this._.textbound = k && k.unfollow(this).remove(), !1;
			(!i || !g.is(i, "finite")) && (i = 0);
			(!d || !g.is(d, "finite")) && (d = 0);
			!k && (k = this._.textbound = c.rect(0, 0, 0, 0, this.group).follow(this, g.ca["text-bound"].reposition, "before"));
			k.attr({
				stroke: e,
				"stroke-width": i,
				fill: b,
				"shape-rendering": i === 1 && "crisp" || "",
				r: d
			});
			a && k.attr("stroke-dasharray", a);
			g.ca["text-bound"].reposition.call(k, this.attrs, this);
			return !1
		}
	};
	g.ca["text-bound"].reposition = function(b, e) {
		var i = {},
			c, d, a, k, h;
		b.hasOwnProperty("visibility") && this.attr("visibility", b.visibility);
		if (b.hasOwnProperty("text-bound") || b.hasOwnProperty("x") || b.hasOwnProperty("y") || b.hasOwnProperty("text") || b.hasOwnProperty("text-anchor") || b.hasOwnProperty("text-align") || b.hasOwnProperty("font-size") || b.hasOwnProperty("line-height") || b.hasOwnProperty("vertical-align") || b.hasOwnProperty("transform")) {
			c = ka((e.attrs["text-bound"] || [])[3] || "0").split(/\s*\,\s*/g);
			d = s(c[0]) || 0;
			c = g.pick(s(c[1]), d);
			a = e.getBBox();
			k = a.width;
			h = a.height;
			if (!isNaN(k)) i.x = a.x - d, i.y = a.y - c, i.width = k + d * 2, i.height = h + c * 2;
			this.attr(i)
		}
	};
	g.fn.scroller = function(b, e, i, c, d, a, k) {
		var h = this.group("scroller", k),
			l = h.attrs,
			n = h._.scroller = {},
			d = d && "horizontal" || "vertical",
			m, r = {},
			v, w;
		n.track = this.rect(h).mousedown(function(a) {
			var b = l["scroll-position"],
				a = l["scroll-orientation"] === "horizontal" ? a.layerX || a.x : a.layerY || a.y,
				a = (a - n.anchorOffset) / n.trackLength;
			m = g.animation({
				"scroll-position": a
			}, 2E3 * ia(b - a), "easeIn");
			h.animate(m);
			Y("raphael.scroll.start." + h.id, h, b)
		}).mouseup(function() {
			this.stop(m);
			Y("raphael.scroll.end." + this.id, this, l["scroll-position"])
		}, h, !0);
		n.anchor = this.rect(h).drag(function() {
			r["scroll-position"] = v + arguments[w] / n.trackLength;
			h.animate(r, 0)
		}, function(a, b, d) {
			w = l["scroll-orientation"] === "horizontal" ? 0 : 1;
			Y("raphael.scroll.start." + h.id, h, v = l["scroll-position"]);
			d.stopPropagation()
		}, function() {
			Y("raphael.scroll.end." + h.id, h, v = l["scroll-position"])
		});
		for (var F in g.fn.scroller.fn) h[F] = g.fn.scroller.fn[F];
		for (F in g.fn.scroller.ca) h.ca[F] = g.fn.scroller.ca[F];
		l["scroll-orientation"] = d;
		l["stroke-width"] = 1;
		h.ca["scroll-repaint"] = h.ca["scroll-repaint-" + d];
		!g.is(a, "object") && (a = {});
		return h.attr({
			ishot: !0,
			"scroll-display-buttons": a.showButtons && "arrow" || "none",
			"scroll-display-style": a.displayStyleFlat && "flat" || "3d",
			"scroll-ratio": s(a.scrollRatio) || 1,
			"scroll-position": s(a.scrollPosition) || 0,
			"scroll-repaint": [b, e, i, c]
		})
	};
	g.fn.scroller.fn = {
		scroll: function(b, e) {
			var i = this._.scroller,
				e = e || this;
			i.callback = function() {
				return b.apply(e, arguments)
			};
			return this
		}
	};
	g.fn.scroller.ca = {
		"stroke-width": function() {
			return !1
		},
		"drop-shadow": function(b, e, i, c, d, a) {
			this._.scroller.track.attr("drop-shadow", [b, e, i, c, d, a]);
			return !1
		},
		"scroll-display-style": function(b) {
			var e = this.attrs,
				i = e["scroll-display-style"],
				c = e.fill,
				b = {
					flat: "flat",
					"3d": "3d",
					transparent: "transparent"
				}[b] || i;
			c && b !== i && (e["scroll-display-style"] = b, this.attr(G, c));
			return {
				"scroll-display-style": b
			}
		},
		"scroll-display-buttons": function(b) {
			var e = this,
				i = e.paper,
				c = e._.scroller,
				d = e.attrs,
				a = d["scroll-display-buttons"],
				k = d["scroll-repaint"],
				h, s, b = {
					none: "none",
					arrow: "arrow"
				}[b] || a;
			if (b !== a) d["scroll-display-buttons"] = b, b === "none" && c.start ? (c.arrowstart.remove(), delete c.arrowstart, c.arrowend.remove(), delete c.arrowend, c.start.remove(), delete c.start, c.end.remove(), delete c.end) : (c.arrowstart = i.polypath(e), c.arrowend = i.polypath(e), c.start = i.rect(e).mousedown(function() {
				var a;
				if ((a = d["scroll-position"]) !== 0) e.animate({
					"scroll-position": a - 0.1
				}, 100).animate(h = g.animation({
					"scroll-position": 0
				}, 4500 * a, "easeIn")), Y("raphael.scroll.start." + e.id, e, a)
			}).mouseup(function() {
				e.stop(h);
				Y("raphael.scroll.end." + e.id, e, d["scroll-position"])
			}, e, !0), c.end = i.rect(e).mousedown(function() {
				var a;
				if ((a = d["scroll-position"]) !== 1) e.animate({
					"scroll-position": a + 0.1
				}, 100).animate(s = g.animation({
					"scroll-position": 1
				}, 4500 * (1 - a), "easeIn")), Y("raphael.scroll.start." + e.id, e, a)
			}).mouseup(function() {
				e.stop(s);
				Y("raphael.scroll.end." + e.id, e, d["scroll-position"])
			}, e, !0), d.fill && e.attr(G, d.fill)), k && e.attr("scroll-repaint", k);
			return {
				"scroll-display-buttons": b
			}
		},
		"scroll-orientation": function(b) {
			var e = this.attrs,
				i = e["scroll-repaint"],
				c = e["scroll-orientation"],
				b = {
					horizontal: "horizontal",
					vertical: "vertical"
				}[b] || c;
			c !== b && (this.ca["scroll-repaint"] = this.ca["scroll-repaint-" + b], i && (i[2] += i[3], i[3] = i[2] - i[3], i[2] -= i[3], this.attr("scroll-repaint", i)), e.fill && this.attr(G, e.fill));
			return {
				"scroll-orientation": b
			}
		},
		"scroll-ratio": function(b) {
			var e = this.attrs,
				i = e["scroll-ratio"],
				c = e["scroll-repaint"],
				b = b > 1 ? 1 : b < 0.01 ? 0.01 : s(b);
			c && b !== i && (e["scroll-ratio"] = b, this.attr("scroll-repaint", c));
			return {
				"scroll-ratio": b
			}
		},
		"scroll-position": function(b, e) {
			var i = this.attrs,
				c = i["scroll-orientation"] === "horizontal",
				d = i["scroll-repaint"],
				a = i["scroll-position"],
				k = this._.scroller,
				g = k.anchor,
				b = b > 1 ? 1 : b < 0 ? 0 : s(b);
			isNaN(b) && (b = a);
			if (d && (a !== b || e)) a = k.start && k.start.attr(c && "width" || "height") || 0, c && g.attr("x", d[0] + a + (d[2] - 2 * a - g.attr("width")) * b + 0.5) || g.attr("y", d[1] + a + (d[3] - 2 * a - g.attr("height")) * b + 0.5), !e && i["scroll-ratio"] < 1 && (Y("raphael.scroll.change." + this.id, this, b), k.callback && k.callback(b));
			return {
				"scroll-position": b
			}
		},
		r: function(b) {
			var e = this._.scroller;
			e.track.attr("r", b);
			e.anchor.attr("r", this.attrs["scroll-display-buttons"] === "none" && b || 0);
			return !1
		},
		"scroll-repaint-horizontal": function(b, j, i, c) {
			var d = this.attrs,
				a = this._.scroller,
				k = d["scroll-ratio"],
				g = d["scroll-position"],
				h = 0,
				s = i * k,
				d = d["scroll-display-buttons"] === "none";
			i && (i -= 1);
			b && (b += 0.5);
			c && (c -= 1);
			j && (j += 0.5);
			a.track.attr({
				width: i,
				height: c,
				y: j,
				x: b
			}).crisp();
			d || (h = e(c, i * 0.5), s -= h * 2 * k, a.start.attr({
				width: h,
				height: c,
				x: b,
				y: j
			}), a.arrowstart.attr("polypath", [3, b + h * 0.5, j + c * 0.5, h * 0.25, 180]), a.end.attr({
				width: h,
				height: c,
				x: b + i - h,
				y: j
			}), a.arrowend.attr("polypath", [3, b + i - h * 0.5, j + h * 0.5, h * 0.25, 0]));
			a.trackLength = i - 2 * h - s;
			a.trackOffset = b + h + 0.5;
			a.anchorOffset = a.trackOffset + (s - 1) * 0.5;
			a.anchor.attr({
				height: c,
				width: s - 1,
				y: j,
				x: a.trackOffset + a.trackLength * g
			}).crisp()
		},
		"scroll-repaint-vertical": function(b, j, i, c) {
			var d = this.attrs,
				a = this._.scroller,
				k = d["scroll-ratio"],
				g = d["scroll-position"],
				h = 0,
				s = c * k,
				d = d["scroll-display-buttons"] === "none";
			i && (i -= 1);
			b && (b += 0.5);
			c && (c -= 1);
			j && (j += 0.5);
			a.track.attr({
				width: i,
				height: c,
				y: j,
				x: b
			}).crisp();
			d || (h = e(i, c * 0.5), s -= h * 2 * k, a.start.attr({
				width: i,
				height: h,
				x: b,
				y: j
			}), a.arrowstart.attr("polypath", [3, b + i * 0.5, j + h * 0.5, h * 0.25, 90]), a.end.attr({
				width: i,
				height: h,
				x: b,
				y: j + c - h
			}), a.arrowend.attr("polypath", [3, b + i * 0.5, j + c - h * 0.5, h * 0.25, -90]));
			a.trackLength = c - 2 * h - s;
			a.trackOffset = j + h + 0.5;
			a.anchorOffset = a.trackOffset + (s - 1) * 0.5;
			a.anchor.attr({
				height: s - 1,
				width: i,
				y: a.trackOffset + a.trackLength * g,
				x: b
			}).crisp()
		},
		fill: function(b) {
			var e = this.attrs,
				i = this._.scroller,
				c = e["scroll-repaint"],
				d = e["scroll-display-style"] === "flat",
				a = e["scroll-orientation"] === "horizontal",
				k = {
					stroke: "none"
				},
				s;
			if (h && c && (s = 16 - c[a && 3 || 2]) > 3) k.stroke = K, k["stroke-width"] = s;
			b = g.color(b);
			b.error && (b = "#000000");
			b = "opacity" in b ? "rgba(" + [b.r, b.g, b.b, b.opacity] + ")" : "rgb(" + [b.r, b.g, b.b] + ")";
			k.fill = d && b || [90 * a, g.tintshade(b, 0.15).rgba, b].join("-");
			k.stroke = g.tintshade(b, -0.75).rgba;
			i.track.attr(k);
			k.fill = d && g.tintshade(b, -0.6).rgba || [270 * a, g.tintshade(b, 0.3).rgba + ":40", g.tintshade(b, -0.7).rgba].join("-");
			k.stroke = g.tintshade(b, -0.6).rgba;
			i.anchor.attr(k);
			k.stroke = "none";
			if (e["scroll-display-buttons"] !== "none") k.fill = K, i.start.attr(k), i.end.attr(k), k.fill = g.tintshade(b, -0.4).rgba, i.arrowstart.attr(k), i.arrowend.attr(k);
			return !1
		}
	};
	var aa = Array.prototype.slice;
	g.fn.symbol = function() {
		var b = arguments,
			e = b.length - 1,
			i = b[e];
		i && i.constructor === g.el.constructor ? b[e] = void 0 : i = void 0;
		e = this.path(void 0, i);
		e.ca.symbol = g.fn.symbol.ca.symbol;
		return b.length === !! i + 0 ? e : e.attr("symbol", b)
	};
	g.fn.symbol.cache = {
		"": g._cacher(function(b, e, i, c) {
			return arguments.length > 3 ? ["M", b, e, "h", i, "v", c, "h", -i, "v", -c, "z"] : ["M", b - i, e - i, "h", i *= 2, "v", i, "h", -i, "v", -i, "z"]
		})
	};
	g.fn.symbol.ca = {
		symbol: function(b) {
			var e = g.is(b, "object") && arguments.length === 1 && !g.is(b, "function") ? b : arguments,
				i;
			e === b && (b = e[0]);
			e = (i = g.is(b, "function") && b || g.fn.symbol.cache[b] || g.fn.symbol.cache[""]) && i.apply(g, aa.call(e, 1));
			g.is(e, "array") || g.is(e, "string") ? this.attr("path", e) : e && this.attr(e)
		}
	};
	g.addSymbol = function(b, e) {
		var i = g.is(e, "function") && (i = {}, i[b] = e, i) || b,
			c = g.fn.symbol.cache,
			d = [],
			a;
		for (a in i) e = i[a], c[a] = g.is(e, "function") && g._cacher(e, g) || (d.push(a), e);
		for (; a = d.pop();) c[a] = c[c[a]]
	};
	g.fn.button = function(b, e, i, c, d, a) {
		a = this.group("button", a);
		a._.button = {
			bound: this.rect(a),
			tracker: this.rect(a).attr({
				fill: K,
				stroke: K,
				cursor: "pointer"
			}).data("compositeButton", a)
		};
		var k;
		!g.is(d, "object") && (d = {});
		for (k in g.fn.button.fn) a[k] = g.fn.button.fn[k];
		for (k in g.fn.button.ca) a.ca[k] = g.fn.button.ca[k];
		return a.attr({
			ishot: !0,
			"button-padding": [d.horizontalPadding, d.verticalPadding],
			"button-label": i,
			"button-symbol": c,
			"button-disabled": d.disabled,
			"button-symbol-position": d.symbolPosition,
			"button-symbol-padding": d.symbolPadding
		}).attr("button-repaint", [b, e, d.width, d.height, d.r])
	};
	g.fn.button.e = {
		hoverin: function() {
			var b = this._.button.hoverbackIn;
			b && b() === !1 || (this.attr("fill", "hover").hovered = !0)
		},
		hoverout: function() {
			var b = this._.button.hoverbackOut;
			b && b() === !1 || (this.attr("fill", (this.pressed || this.active) && "active" || "normal").hovered = !1)
		},
		mousedown: function() {
			this.attr("fill", "active").pressed = !0
		},
		mouseup: function() {
			var b = this._.button.callback;
			this.attr("fill", this.hovered && "hover" || this.active && "active" || "normal").pressed = !1;
			b()
		}
	};
	g.fn.button.fn = {
		buttonclick: function(b, e) {
			var i = this._.button,
				e = e || this;
			i.callback = function() {
				return b.apply(e, arguments)
			};
			return this
		},
		labelcss: function() {
			var b = this._.button,
				e = b.label;
			b.cssArg = arguments;
			e && e.css.apply(e, arguments);
			return this
		},
		buttonhover: function(b, e, i, c) {
			var d = this._.button,
				i = i || this,
				c = c || this;
			d.hoverbackIn = function() {
				return b.apply(i, arguments)
			};
			d.hoverbackOut = function() {
				return e.apply(c, arguments)
			};
			return this
		}
	};
	g.fn.button.ca = {
		"button-active": function(b) {
			this.attr("fill", (this.active = !! b) ? "active" : this.hovered && "hover" || "normal")
		},
		"button-disabled": function(b) {
			var e = this.paper,
				i = this._.button.tracker,
				c = e.button.e,
				b = ka(b);
			b === "disabled" || b === "true" || b === "1" ? i.attr("fill", "rgba(204,204,205,.5)").unmousedown(c.mousedown).unmouseup(c.mouseup).unhover(e.button.e.hoverin, e.button.e.hoverout) : i.attr("fill", K).mousedown(c.mousedown, this).mouseup(c.mouseup, this, !0).hover(e.button.e.hoverin, e.button.e.hoverout, this, this)
		},
		"button-label": function(b) {
			var e = this._.button,
				i = this.attrs,
				c = e.label,
				d = e.cssArg,
				a = this.attrs["button-repaint"],
				b = ka(b || "");
			if (b === "none") c && (e.label = c.remove());
			else if (b)!c && (c = e.label = this.paper.text(this).insertBefore(e.tracker)), c.attr({
				text: b,
				"text-anchor": "start",
				"vertical-align": "top"
			}), d && d.length && c.css.apply(c, d);
			a && i["button-label"] !== b && this.attr("button-repaint", a)
		},
		"button-symbol": function(b) {
			var e = this.attrs,
				i = this._.button,
				c = i.symbol,
				d = this.attrs["button-repaint"],
				b = ka(b || "");
			if (b === "none") c && (i.symbol = c.remove());
			else if (b && !c) i.symbol = this.paper.symbol(this).insertAfter(i.bound);
			d && e["button-symbol"] !== b && this.attr("button-repaint", d)
		},
		"button-symbol-position": function(b) {
			return {
				"button-symbol-position": {
					top: "top",
					right: "right",
					bottom: "bottom",
					left: "left",
					none: "none"
				}[ka(b).toLowerCase()] || "none"
			}
		},
		"button-symbol-padding": function(b) {
			return {
				"button-symbol-padding": s(b)
			}
		},
		"button-padding": function(b, e) {
			return {
				"button-padding": [b == null && (b = 5) || s(b), e == null && b || s(e)]
			}
		},
		"button-repaint": function(b, j, i, c, d) {
			var a = this._.button,
				k = a.bound,
				h = a.label,
				s = a.symbol,
				l = this.attrs,
				n = l["button-padding"],
				m = n[0],
				v = n[1],
				w;
			b == void 0 && (b = 0);
			j == void 0 && (j = 0);
			if (i == void 0 || c == void 0) w = h && h.getBBox() || {
				width: 0,
				height: 0
			}, i == void 0 && (i = m * 2 + w.width), c == void 0 && (c = v * 2 + w.height);
			k = g.crispBound(b, j, i, c, k.attr("stroke-width"));
			k.r = g.pick(d, r(e(c, i) * 0.1));
			b = k.x;
			j = k.y;
			i = k.width;
			c = k.height;
			h && h.attr({
				x: b + m,
				y: j + v
			});
			if (s) {
				!g.is(w = l["button-symbol-padding"], "finite") && (w = c * 0.2);
				d = (c - v) * 0.5;
				switch (l["button-symbol-position"] + (h && "+" || "-")) {
				case "right+":
					i += d * 2 + v;
					b = b + i - d - m;
					j += c * 0.5;
					break;
				case "left+":
					b = b + m + d;
					j += c * 0.5;
					h.attr("x", b + d + w);
					break;
				case "top+":
					b += i * 0.5;
					j = j + n[1] + d;
					h && h.attr("y", j + d + w);
					break;
				case "bottom+":
					c += d * 2 + w;
					b += i * 0.5;
					j = j + c - v - d;
					break;
				default:
					b += i * 0.5, j += c * 0.5
				}
				s.attr("symbol", [l["button-symbol"], b, j, d])
			}
			a.bound.attr(k);
			a.tracker.attr(k)
		},
		fill: function(b, e, i, c) {
			var d = this._.button,
				a = d.bound,
				k = d.symbol,
				h = d.label,
				s = {
					normal: d.gradient,
					active: d.gradientActive,
					hover: d.gradientHover
				}[b];
			if (!s) b = g.getRGB(b), b.error && (b = g.color("#cccccc")), b = "opacity" in b ? "rgba(" + [b.r, b.g, b.b, b.opacity] + ")" : "rgb(" + [b.r, b.g, b.b] + ")", d.gradient = [90, g.tintshade(b, -0.8).rgba + ":0", g.tintshade(b, 0.8).rgba + ":100"].join("-"), d.gradientActive = [270, g.tintshade(b, -0.8).rgba + ":0", g.tintshade(b, 0.8).rgba + ":100"].join("-"), c = g.getRGB(c), c.error && (c = b) || (c = "opacity" in c ? "rgba(" + [c.r, c.g, c.b, c.opacity] + ")" : "rgb(" + [c.r, c.g, c.b] + ")"), d.gradientHover = [90, g.tintshade(c, -0.9).rgba + ":0", g.tintshade(c, 0.7).rgba + ":100"].join("-"), i = i || g.tintshade(b, 0.2).rgba, e = e || g.tintshade(b, -0.2).rgba, d.symbolFill = i, d.labelFill = e, s = (this.pressed || this.active) && d.gradientActive || this.hovered && d.gradienthover || d.gradient;
			a.attr("fill", s);
			k && k.attr("fill", d.symbolFill);
			h && h.attr("fill", d.labelFill);
			return !1
		},
		stroke: function(b, e) {
			var i = this._.button,
				c = i.symbol,
				b = g.color(b);
			b.error && (b = g.color("#999999"));
			i.bound.attr("stroke", b);
			c && c.attr("stroke", e || b);
			return !1
		},
		"stroke-width": function(b, e) {
			var i = this._.button,
				c = i.symbol;
			i.bound.attr("stroke-width", b);
			i.tracker.attr("stroke-width", b);
			c && c.attr("stroke-width", e);
			return !1
		}
	};
	var D = {
		Q: "L",
		Z: "X",
		q: "l",
		z: "x",
		",": " "
	},
		M = /,?([achlmqrstvxz]),?/gi,
		Z, R = function() {
			return this.join(",").replace(M, Z)
		},
		v, C;
	if (g.svg) Z = "$1", v = function(b) {
		b ? typeof b === "string" ? b = b.replace(M, Z) : b.toString = R : b = "M0,0";
		this.node.setAttribute("d", b.toString());
		return this
	}, g._engine.litepath = function(b, e, i, c) {
		b = l("path");
		(c || e).canvas.appendChild(b);
		e = new F(b, e, c);
		e.type = "litepath";
		n(e, {
			fill: "none",
			stroke: "#000"
		});
		return e
	}, g._getPath.litepath = function(b) {
		return g.parsePathString(b.node.getAttribute("d"))
	};
	else if (g.vml) Z = function(b, e) {
		return D[e] || e
	}, C = function() {
		this._transform.apply(this, arguments);
		this._.bcoord && (this.node.coordsize = this._.bcoord);
		return this
	}, v = function(b) {
		b ? typeof b === "string" ? b = b.replace(M, Z) : b.toString = R : b = "M0,0";
		this.node.path = b;
		return this
	}, g._engine.litepath = function(b, e, i, c) {
		var b = l("shape"),
			d = b.style,
			a = new F(b, e, c);
		d.cssText = "position:absolute;left:0;top:0;width:21600px;height:21600px;";
		i = s(i);
		isNaN(i) ? b.coordsize = "21600 21600" : (a._.bzoom = i, d.width = "1px", d.height = "1px", b.coordsize = a._.bcoord = i + " " + i);
		b.coordorigin = e.coordorigin;
		a.type = "litepath";
		a._transform = a.transform;
		a.transform = C;
		g._setFillAndStroke(a, {
			fill: "none",
			stroke: "#000"
		});
		(c || e).canvas.appendChild(b);
		e = l("skew");
		e.on = !0;
		b.appendChild(e);
		a.skew = e;
		return a
	}, g._getPath.litepath = function(b) {
		return g.parsePathString(b.node.path || "")
	};
	g.fn.litepath = function(b, e, i) {
		e && e.constructor === F && (i = e, e = void 0);
		b && b.constructor === F && (i = b, b = "");
		e = g._engine.litepath(b, this, e, i);
		e.ca.litepath = v;
		b && e.attr("litepath", g.is(b, "array") ? [b] : b);
		this.__set__ && this.__set__.push(e);
		return e
	}
}]);
FusionCharts(["private", "modules.renderer.js-raphaelexport", function() {
	var g = this.hcLib,
		h = g.Raphael,
		m = g.pluckNumber,
		U = g.pluck,
		w = h._availableAttrs,
		S = /^matrix\(|\)$/g,
		ia = /\,/g,
		b = /\n|<br\s*?\/?>/ig,
		B = /[^\d\.]/ig,
		e = /[\(\)\s,\xb0#]/g,
		r = /group/ig,
		x = /&/g,
		$ = /"/g,
		fa = /'/g,
		s = /</g,
		ka = />/g,
		G = 0;
	(function(h) {
		var g = Math,
			Y = parseFloat,
			O = g.max,
			l = g.abs,
			n = g.pow,
			F = String,
			aa = /[, ]+/,
			D = [{
				reg: /xmlns\=\"http\:\/\/www.w3.org\/2000\/svg\"/ig,
				repStr: ""
			}, {
				reg: /^.*<svg /,
				repStr: '<svg xmlns:xlink="http://www.w3.org/1999/xlink" xmlns="http://www.w3.org/2000/svg" '
			}, {
				reg: /\/svg>.*$/,
				repStr: "/svg>"
			}, {
				reg: /\<desc\>[^\<]*\<\/desc\>/,
				repStr: ""
			}, {
				reg: /zIndex="[^"]+"/g,
				repStr: ""
			}, {
				reg: /url\((\\?[\'\"])[^#]+#/g,
				repStr: "url($1#"
			}, {
				reg: / href=/g,
				repStr: " xlink:href="
			}, {
				reg: /(id|class|width|height)=([^" >]+)/g,
				repStr: '$1="$2"'
			}, {
				reg: /:(path|rect)/g,
				repStr: "$1"
			}, {
				reg: /\<ima?ge? ([^\>]+?)[^\/]\>/gi,
				repStr: "<image $1 />"
			}, {
				reg: /\<\/ima?ge?\>/g,
				repStr: ""
			}, {
				reg: /style="([^"]+)"/g,
				repStr: function(b) {
					return b.toLowerCase()
				}
			}],
			M = {
				blur: function() {},
				transform: function() {},
				src: function(b, e) {
					e.attrSTR += ' xlink:href="' + e.attrs.src + '"'
				},
				path: function(b, e) {
					var g = e.attrs.path,
						g = h._pathToAbsolute(g || "");
					e.attrSTR += ' d="' + (g.toString && g.toString() || "").replace(ia, " ") + '"'
				},
				gradient: function(b, s, m) {
					var f = b.attrs.gradient,
						j = "linear",
						i, c, d = 0.5,
						a = 0.5,
						k = c = "",
						r = "";
					i = f.replace(e, "_");
					if (!m[i]) {
						f = F(f).replace(h._radial_gradient, function(b, e, c) {
							j = "radial";
							e && c && (d = Y(e), a = Y(c), b = (a > 0.5) * 2 - 1, n(d - 0.5, 2) + n(a - 0.5, 2) > 0.25 && (a = g.sqrt(0.25 - n(d - 0.5, 2)) * b + 0.5) && a != 0.5 && (a = a.toFixed(5) - 1.0E-5 * b));
							return ""
						});
						f = f.split(/\s*\-\s*/);
						if (j === "linear") {
							c = f.shift();
							c = -Y(c);
							if (isNaN(c)) return null;
							var u = [0, 0, g.cos(h.rad(c)), g.sin(h.rad(c))];
							c = 1 / (O(l(u[2]), l(u[3])) || 1);
							u[2] *= c;
							u[3] *= c;
							u[2] < 0 && (u[0] = -u[2], u[2] = 0);
							u[3] < 0 && (u[1] = -u[3], u[3] = 0)
						}
						f = h._parseDots(f);
						if (!f) return null;
						j === "radial" ? (c = '<radialGradient fx = "' + d + '" fy = "' + a + '" id = "' + i + '">', k = "</radialGradient>") : (c = '<linearGradient x1 = "' + u[0] + '" y1 = "' + u[1] + '" x2 = "' + u[2] + '" y2 = "' + u[3] + '" gradientTransform ="matrix(' + b.matrix.invert() + ')" id = "' + i + '">', k = "</linearGradient>");
						b = 0;
						for (u = f.length; b < u; b++) r += '<stop offset="' + (f[b].offset ? f[b].offset : b ? "100%" : "0%") + '" stop-color="' + (f[b].color || "#fff") + '" stop-opacity="' + (f[b].opacity === void 0 ? 1 : f[b].opacity) + '" />';
						m[i] = !0;
						m.str += c + r + k
					}
					s.attrSTR += " fill=\"url('#" + i + "')\""
				},
				fill: function(b, e) {
					var g = e.attrs,
						f = g.fill,
						j;
					if (!b.attrs.gradient) if (f = h.color(f), j = f.opacity, b.type === "text") e.styleSTR += "fill:" + f + "; stroke-opacity:0; ";
					else if (e.attrSTR += ' fill="' + f + '"', !g["fill-opacity"] && (j || j === 0)) e.attrSTR += ' fill-opacity="' + j + '"'
				},
				stroke: function(b, e) {
					var g = e.attrs,
						f, j;
					f = h.color(g.stroke);
					j = f.opacity;
					if (b.type !== "text" && (e.attrSTR += ' stroke="' + f + '"', !g["stroke-opacity"] && (j || j === 0))) e.attrSTR += ' stroke-opacity="' + j + '"'
				},
				"clip-rect": function(b, h, g) {
					var f = F(h.attrs["clip-rect"]),
						j = f.split(aa),
						f = f.replace(e, "_") + "__" + G++;
					j.length === 4 && (g[f] || (g[f] = !0, g.str += '<clipPath id="' + f + '"><rect x="' + j[0] + '" y="' + j[1] + '" width="' + j[2] + '" height="' + j[3] + '" transform="matrix(' + b.matrix.invert().toMatrixString().replace(S, "") + ')"/></clipPath>'), h.attrSTR += ' clip-path="url(#' + f + ')"')
				},
				cursor: function(b, e) {
					var h = e.attrs.cursor;
					h && (e.styleSTR += "cursor:" + h + "; ")
				},
				font: function(b, e) {
					e.styleSTR += "font:" + e.attrs.font.replace(/\"/ig, " ") + "; "
				},
				"font-size": function(b, e) {
					var h = U(e.attrs["font-size"], "10");
					h && h.replace && (h = h.replace(B, ""));
					e.styleSTR += "font-size:" + h + "px; "
				},
				"font-weight": function(b, e) {
					e.styleSTR += "font-weight:" + e.attrs["font-weight"] + "; "
				},
				"font-family": function(b, e) {
					e.styleSTR += "font-family:" + e.attrs["font-family"] + "; "
				},
				"line-height": function() {},
				"clip-path": function() {},
				visibility: function() {},
				"vertical-align": function() {},
				"text-anchor": function(b, e) {
					var h = e.attrs["text-anchor"] || "middle";
					b.type === "text" && (e.attrSTR += ' text-anchor="' + h + '"')
				},
				title: function() {},
				text: function(e, h) {
					var g = h.attrs,
						f = g.text,
						j = U(g["font-size"], g.font, "10"),
						i = U(g["line-height"]),
						c;
					j && j.replace && (j = j.replace(B, ""));
					j = m(j);
					i && i.replace && (i = i.replace(B, ""));
					i = m(i, j && j * 1.2);
					c = j ? j * 0.85 : i * 0.75;
					for (var j = g.x, d = U(g["vertical-align"], "middle").toLowerCase(), f = F(f).split(b), g = f.length, a = 0, d = d === "top" ? c : d === "bottom" ? c - i * g : c - i * g * 0.5; a < g; a++) h.textSTR += "<tspan ", c = (f[a] || "").replace(x, "&amp;").replace($, "&quot;").replace(fa, "&#39;").replace(s, "&lt;").replace(ka, "&gt;"), h.textSTR += a ? 'dy="' + i + '" x="' + j + '" ' : 'dy="' + d + '"', h.textSTR += ">" + c + "</tspan>"
				}
			},
			Z = function(b, e) {
				var h = "",
					f = {
						attrSTR: "",
						styleSTR: "",
						textSTR: "",
						attrs: b.attr()
					},
					g = b.isShadow,
					i = "",
					c = "",
					d, a, k = f.attrs;
				if (b.node.style.display !== "none" && !g) {
					for (d in k) if (d !== "gradient" && (w[d] !== void 0 || M[d])) if (M[d]) M[d](b, f, e);
					else f.attrSTR += " " + d + '="' + k[d] + '"';
					b.attrs.gradient && M.gradient(b, f, e);
					b.type === "rect" && k.r && (f.attrSTR += ' rx="' + k.r + '" ry="' + k.r + '"');
					for (a in b.styles) f.styleSTR += a + ":" + b.styles[a] + "; ";
					b.type === "image" && (f.attrSTR += ' preserveAspectRatio="none"');
					b.bottom && (i = Z(b.bottom, e));
					b.next && (c = Z(b.next, e));
					g = b.type;
					g.match(r) && (g = "g");
					h += "<" + g + ' transform="matrix(' + b.matrix.toMatrixString().replace(S, "") + ')" style="' + f.styleSTR + '"' + f.attrSTR + ">" + f.textSTR + i + "</" + g + ">" + c
				} else b.next && (h += Z(b.next, e));
				return h
			};
		h.fn.toSVG = function(b) {
			var e = "",
				g = {
					str: ""
				},
				f = 0,
				j = D.length,
				i = "";
			if (h.svg) {
				if (this.canvas && this.canvas.parentNode) for (e = this.canvas.parentNode.innerHTML; f < j; f += 1) g = D[f], e = e.replace(g.reg, g.repStr)
			} else e = '<svg style="overflow: hidden; position: relative;" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" width="' + this.width + '" version="1.1" height="' + this.height + '">', this.bottom && (i = Z(this.bottom, g)), e += "<defs>" + g.str + "</defs>" + i + "</svg>";
			b || (e = e.replace(/\<image [^\>]*\>/gi, ""));
			return e
		}
	})(h)
}]);
FusionCharts(["private", "modules.renderer.js-raphaeltooltip", function() {
	var g = window,
		h = document,
		m = this.hcLib,
		U = m.Raphael,
		w = U.eve,
		S = m.createElement,
		ia = m.addEvent,
		b = m.removeEvent,
		B = m.getPosition,
		e = m.hasTouch,
		r = m.getTouchEvent,
		x = g.Math,
		$ = x.ceil,
		fa = x.floor,
		s = g.screen.availHeight,
		ka = g.screen.availWidth,
		G = {
			"": 1,
			moz: 1,
			webkit: 1,
			o: 1,
			ms: 1
		},
		V = {
			borderRadius: "borderRadius",
			boxShadow: "boxShadow"
		},
		K = /\-([a-z])/ig,
		Y = function(b, e) {
			return e.toUpperCase()
		},
		O = function(b) {
			var e = l.forbiddenStyle,
				h, g, s;
			for (h in b) g = K.test(h) ? h.replace(K, Y) : h, b[h] !== void 0 && !e[g] && (this[g] = b[h]), U.vml && /color/ig.test(g) && (this[g] = U.getRGB(this[g]).toString());
			for (h in V) if (this[h]) for (s in G) this[s + h] = this[h]
		},
		l = m.toolTip = {
			elementId: "fusioncharts-tooltip-element",
			element: null,
			lastTarget: null,
			currentTarget: null,
			currentPaper: null,
			pointeroffset: 12,
			defaultStyle: m.extend2(O.prototype, {
				backgroundColor: "#ffffee",
				borderColor: "#000000",
				borderWidth: "1px",
				color: "#000000",
				fontSize: "10px",
				lineHeight: "12px",
				padding: "3px",
				borderStyle: "solid"
			}),
			defaultContainerStyle: {
				position: "absolute",
				textAlign: "left",
				margin: "0",
				zIndex: "999",
				pointer: "default",
				display: "block"
			},
			forbiddenStyle: {}
		},
		n = function(e) {
			b(window, "click", n);
			l.onhide.call(this, e)
		};
	if (U.svg) l.defaultContainerStyle.pointerEvents = "none", l.defaultStyle.borderRadius = "0", l.defaultStyle.boxShadow = "none";
	if (U.vml) l.forbiddenStyle.borderRadius = !0, l.forbiddenStyle.boxShadow = !0, l.defaultStyle.filter = "";
	l.setup = function() {
		var b = l.container,
			g = l.textElement,
			s = l.style,
			n = l.defaultContainerStyle,
			m = l.forbiddenStyle,
			r;
		if (!b) b = l.element = S("span"), (h.body || h.getElementsByTagName("body")[0]).appendChild(b), b.setAttribute("id", l.elementId), s = l.containerStyle = b.style, g = l.textElement = S("span"), b.appendChild(g), l.style = U.vml ? g.runtimeStyle : g.style, l.style.overflow = "hidden", l.style.display = "block", l.hidden = !1, l.hide();
		for (r in n)!m[r] && (s[r] = n[r]);
		ia(b, e && "touchstart" || "mouseover", l.onredraw);
		l.scatted = !0;
		w.on("raphael.drag.start.*", function() {
			l.scatted && (l.waitingScat = !0)
		});
		w.on("raphael.drag.move.*", function() {
			if (l.waitingScat) l.block(), l.waitingScat = !1
		});
		w.on("raphael.drag.end.*", function() {
			l.waitingScat = !1;
			l.scatted && l.unblock(!0)
		});
		w.on("raphael.remove", function() {
			if (l.currentPaper === this || l.currentTarget && l.currentTarget.paper === this) l.hide(), l.currentTarget = l.currentPaper = null
		})
	};
	l.restyle = function(b) {
		var e = l.style,
			h;
		for (h in b) e[h] = b[h]
	};
	l.onelement = function(b) {
		var h = b.data,
			g = h.paper;
		if (g.__tip_style) {
			l.hiding && (l.hiding = clearTimeout(l.hiding));
			if (l.currentPaper !== g) g.__tip_cp = g.canvas && B(g.canvas.parentNode, !0) || {}, l.restyle(g.__tip_style), l.currentPaper = g;
			l.lastTarget = l.currentTarget;
			l.currentTarget = h;
			l.scatted = h.__tip_scatted;
			l.onredraw.call(this, b);
			if (e) return ia(window, "click", n), !1
		}
	};
	l.onredraw = function(b) {
		l.redrawing && clearTimeout(l.redrawing);
		b = r(b);
		l.x = fa(b.pageX);
		l.y = fa(b.pageY);
		l.redrawing = setTimeout(l.redraw, 0)
	};
	l.onhide = function() {
		l.hiding = setTimeout(l.hide, 200)
	};
	l.redraw = function() {
		if (!l.blocked && l.currentTarget) {
			var b = l.currentTarget,
				e = b.paper,
				g = l.textElement,
				n = l.containerStyle,
				m = l.style,
				r = b.__tip_text,
				b = l.pointeroffset,
				v = e.__tip_cp,
				w = h.documentElement || h.body,
				f = w.scrollLeft,
				w = w.scrollTop,
				j = l.x,
				i = l.y,
				c, d = e.width,
				a = e.height,
				e = e.__tip_constrain;
			if (d < 100 || a < 100) e = !1;
			if (l.hidden) l.containerStyle.top = "-999em", l.show();
			if (r !== l.text) l.text = r, n.width = n.height = "", g.innerHTML = r, m.whiteSpace = "nowrap", r = $(m.pixelWidth || g.offsetWidth || 0), c = $(m.pixelHeight || g.offsetHeight || 0), (l.textWidthOverflow = r > d) ? (n.width = (d || 0) - b * 2 + "px", m.whiteSpace = "normal") : n.width = "", (l.textHeightOverflow = c > a) ? (n.height = (a || 0) - b * 2 + "px", m.whiteSpace = "normal") : n.height = "";
			r = $(m.pixelWidth || g.offsetWidth || 0);
			c = $(m.pixelHeight || g.offsetHeight || 0);
			e ? (l.textWidthOverflow ? j = v.left - f : j + b + r > v.left - f + d - b && (j = j - r - b), l.textHeightOverflow ? i = v.top - w : i + b + c > v.top - w + a - b && (i = i - c - b * 1.5)) : (f + ka < j + b + r && (j = j - r - b), w + s < i + b + c && (i = i - c - b * 1.5));
			n.left = (j + b || 0) + "px";
			n.top = (i + b || 0) + "px";
			l.hidden && l.show()
		}
	};
	l.hide = function() {
		l.hiding && (l.hiding = clearTimeout(l.hiding));
		l.containerStyle.display = "none";
		l.hidden = !0
	};
	l.show = function() {
		if (!l.blocked) l.hiding && (l.hiding = clearTimeout(l.hiding)), l.containerStyle.display = "inline", l.hidden = !1
	};
	l.block = function() {
		l.blocked = !0;
		l.containerStyle.display = "none"
	};
	l.unblock = function(b) {
		l.blocked = !1;
		b && (l.containerStyle.display = l.hidden && "none" || "inline")
	};
	U.fn.tooltip = function(b, e, g) {
		if (e) e = (e.opacity === void 0 ? 1 : e.opacity) * 0.4, U.svg ? b.boxShadow = "1px 1px 3px rgba(64,64,64," + e + ")" : b.filter = "progid:DXImageTransform.Microsoft.Shadow(Strength=2, Direction=135, Color='#404040', shadowOpacity='" + e / 2 + "')";
		this.__tip_style = new O(b);
		this.__tip_cp = this.canvas && B(this.canvas.parentNode, !0) || {};
		this.__tip_constrain = Boolean(g);
		return this
	};
	U.el.tooltip = function(g, h, s, n, m) {
		l.setup();
		U.el.tooltip = function(g, h, s, f, j) {
			h = this.node;
			s = g === !1 || g == void 0 || !(g + "");
			this.__tip_scatted = f == void 0 ? this.__tip_scatted : !f;
			this.__tip_scatted == void 0 && (this.__tip_scatted = !0);
			if (j != null) this.__tip_blocked = j;
			s ^ !this.__tip_text && (e ? (s ? b : ia)(h, "click", l.onelement, this) : s ? (b(h, "mouseover", l.onelement, this), b(h, "mousemove", l.onredraw, this), b(h, "mouseout", l.onhide, this)) : (ia(h, "mouseover", l.onelement, this), ia(h, "mousemove", l.onredraw, this), ia(h, "mouseout", l.onhide, this)));
			this.__tip_text = g;
			if (l.currentTarget === this && g !== l.text && !l.hidden) l[s ? "hide" : "redraw"]();
			return this
		};
		return U.el.tooltip.call(this, g, h, s, n, m)
	}
}]);
FusionCharts(["private", "modules.renderer.js-base", function() {
	var g = this,
		h = g.hcLib,
		m = window,
		U = m.document,
		w = h.BLANKSTRING,
		S = h.createTrendLine,
		ia = m.location.protocol === "https:" ? "https://export.api3.fusioncharts.com/" : "http://export.api3.fusioncharts.com/",
		b = h.pluck,
		B = h.getValidValue,
		e = h.pluckNumber,
		r = h.defaultPaletteOptions,
		x = h.getFirstValue,
		$ = h.getDefinedColor,
		fa = h.parseUnsafeString,
		s = h.FC_CONFIG_STRING,
		ka = h.extend2,
		G = h.getDashStyle,
		V = h.toPrecision,
		K = h.regex.dropHash,
		Y = h.HASHSTRING,
		O = h.getSentenceCase,
		l = h.addEvent,
		m = Math,
		n = m.min,
		F = m.max,
		aa = m.ceil,
		D = m.floor,
		M = m.log,
		Z = m.pow,
		R = h.graphics.getColumnColor,
		v = h.getFirstColor,
		C = h.setLineHeight,
		f = h.pluckFontSize,
		j = h.getFirstAlpha,
		i = h.graphics.getDarkColor,
		c = h.graphics.getLightColor,
		d = h.graphics.convertColor,
		a = h.COLOR_TRANSPARENT,
		k = h.POSITION_CENTER,
		q = h.POSITION_TOP,
		u = h.POSITION_BOTTOM,
		N = h.POSITION_RIGHT,
		ca = h.POSITION_LEFT,
		J = h.chartAPI,
		Ja = h.titleSpaceManager,
		m = h.placeLegendBlockBottom,
		pa = h.placeLegendBlockRight,
		va = h.graphics.mapSymbolName,
		m = J.singleseries,
		pa = J.multiseries,
		X = h.COMMASTRING,
		Ca = h.STRINGUNDEFINED,
		ma = h.ZEROSTRING,
		ta = h.ONESTRING,
		Aa = h.HUNDREDSTRING,
		Ka = h.PXSTRING,
		bb = h.COMMASPACE,
		Va = !/fusioncharts\.com$/i.test(location.hostname),
		Ra = h.CREDIT_STRING = "",
		sa = h.BLANKSTRINGPLACEHOLDER,
		Ma = h.BGRATIOSTRING,
		Na = h.COLOR_WHITE,
		Wa = h.TESTSTR,
		Ta = h.graphics.getAngle,
		Fa = h.axisLabelAdder,
		$a = h.falseFN,
		eb = h.SmartLabelManager,
		fb = h.NumberFormatter,
		Sa = h.getLinkAction,
		Za = h.getAxisLimits,
		p = h.createDialog,
		z = function(a, b) {
			return a > 0 ? M(a) / M(b || 10) : null
		},
		t = h.hasTouch = document.documentElement.ontouchstart !== void 0;
	h.removeEvent = function(a, b, e) {
		var d = U.removeEventListener ? "removeEventListener" : "detachEvent";
		U[d] && !a[d] && (a[d] = function() {});
		jQuery(a).unbind(b, e)
	};
	var H = h.fireEvent = function(a, b, e, d) {
			var c = jQ.Event(b),
				f = "detached" + b;
			ka(c, e);
			a[b] && (a[f] = a[b], a[b] = null);
			jQuery(a).trigger(c);
			a[f] && (a[b] = a[f], a[f] = null);
			d && !c.isDefaultPrevented() && d(c)
		},
		P = {
			fontWeight: {
				1: "bold",
				0: "normal"
			},
			fontStyle: {
				1: "italic",
				0: "normal"
			},
			textDecoration: {
				1: "underline",
				0: "none"
			}
		},
		ga = {
			font: function(a, b) {
				b.style.fontFamily = a
			},
			size: function(a, b) {
				if (a) b.style.fontSize = f(a) + Ka
			},
			color: function(a, b, e) {
				b.style.color = a && a.replace && a.replace(K, Y) || w;
				if (e) b.color = b.style.color
			},
			bgcolor: function(a, b) {
				b.style.backgroundColor = a && a.replace && a.replace(K, Y) || w
			},
			bordercolor: function(a, b) {
				b.style.border = "1px solid";
				b.style.borderColor = a && a.replace && a.replace(K, Y) || w
			},
			ishtml: w,
			leftmargin: function(a, b) {
				b.style.marginLeft = e(a, 0) + Ka
			},
			letterspacing: function(a, b) {
				b.style.letterSpacing = e(a, 0) + Ka
			},
			bold: function(a, b) {
				b.style.fontWeight = P.fontWeight[a] || ""
			},
			italic: function(a, b) {
				b.style.fontStyle = P.fontStyle[a] || ""
			},
			underline: function(a, b) {
				b.style.textDecoration = P.textDecoration[a] || ""
			}
		},
		qa = {
			chart2D: {
				bgColor: "bgColor",
				bgAlpha: "bgAlpha",
				bgAngle: "bgAngle",
				bgRatio: "bgRatio",
				canvasBgColor: "canvasBgColor",
				canvasBaseColor: "canvasBaseColor",
				divLineColor: "divLineColor",
				legendBgColor: "legendBgColor",
				legendBorderColor: "legendBorderColor",
				toolTipbgColor: "toolTipbgColor",
				toolTipBorderColor: "toolTipBorderColor",
				baseFontColor: "baseFontColor",
				anchorBgColor: "anchorBgColor"
			},
			chart3D: {
				bgColor: "bgColor3D",
				bgAlpha: "bgAlpha3D",
				bgAngle: "bgAngle3D",
				bgRatio: "bgRatio3D",
				canvasBgColor: "canvasBgColor3D",
				canvasBaseColor: "canvasBaseColor3D",
				divLineColor: "divLineColor3D",
				divLineAlpha: "divLineAlpha3D",
				legendBgColor: "legendBgColor3D",
				legendBorderColor: "legendBorderColor3D",
				toolTipbgColor: "toolTipbgColor3D",
				toolTipBorderColor: "toolTipBorderColor3D",
				baseFontColor: "baseFontColor3D",
				anchorBgColor: "anchorBgColor3D"
			}
		},
		da = function() {
			var a = {},
				b, e = function() {
					var e, d, c, f, ha = 0,
						p;
					for (e in a) if (ha += 1, d = a[e], c = d.jsVars, f = (d = d.ref) && d.parentNode) {
						if (p = f.offsetWidth, f = f.offsetHeight, !c.resizeLocked && (c._containerOffsetW !== p || c._containerOffsetH !== f)) d.resize && d.resize(), c._containerOffsetW = p, c._containerOffsetH = f
					} else delete a[e], ha -= 1;
					ha || (b = clearInterval(b))
				};
			return function(d) {
				var c = d.jsVars,
					f = d.ref && d.ref.parentNode || {};
				c._containerOffsetW = f.offsetWidth;
				c._containerOffsetH = f.offsetHeight;
				a[d.id] = d;
				b || (b = setInterval(e, 300))
			}
		}(),
		A = {
			getExternalInterfaceMethods: function() {
				var a = J[this.jsVars.type],
					a = a && a.eiMethods,
					b = "saveAsImage,print,exportChart,getXML,hasRendered,signature,cancelExport,getSVGString,";
				if (typeof a === "string") b += a + X;
				else if (a !== void 0 || a !== null) for (var e in a) b += e + X;
				return b.substr(0, b.length - 1)
			},
			drawOverlayButton: function(a) {
				var b = this.jsVars,
					e = b.$overlayButton;
				if (a && a.show) {
					if (!e) e = b.$overlayButton = jQuery("<span>"), e.click(function() {
						g.raiseEvent("OverlayButtonClick", a, b.fcObj)
					});
					e.text(a.message ? a.message : "Back");
					b.overlayButtonMessage = e.text();
					e.css({
						border: "1px solid " + (a.borderColor ? a.borderColor.replace(K, Y) : "#7f8975"),
						backgroundColor: a.bgColor ? a.bgColor.replace(K, Y) : "#edefec",
						fontFamily: a.font ? a.font : "Verdana",
						color: "#" + a.fontColor ? a.fontColor : "49563a",
						fontSize: (a.fontSize ? a.fontSize : "10") + Ka,
						padding: (a.padding ? a.padding : "3") + Ka,
						fontWeight: parseInt(a.bold, 10) === 0 ? "normal" : "bold",
						position: "absolute",
						top: "0",
						right: "0",
						_cursor: "hand",
						cursor: "pointer"
					});
					b.hcObj.container.appendChild(e[0]);
					b.overlayButtonActive = !0
				} else if (e) e.detach(), b.overlayButtonActive = !1, delete b.overlayButtonMessage
			},
			print: function() {
				return this.jsVars.hcObj && this.jsVars.hcObj.hasRendered && this.jsVars.hcObj.print()
			},
			exportChart: function(a) {
				var b = this.jsVars.hcObj;
				if (b && b.options && b.options.exporting && b.options.exporting.enabled) return b.exportChart(a);
				return !1
			},
			getSVGString: function() {
				return this.jsVars && this.jsVars.hcObj && this.jsVars.hcObj.paper && this.jsVars.hcObj.paper.toSVG()
			},
			resize: function() {
				var a = this.jsVars,
					b = a.container,
					e = a.fcObj,
					d = a.hcObj;
				d && (d && d.destroy && d.destroy(), h.createChart(a.fcObj, b, a.type, void 0, void 0, !1), delete a.isResizing, h.raiseEvent("resized", {
					width: e.width,
					height: e.height,
					prevWidth: a.width,
					prevHeight: a.height
				}, e, [e.id]))
			},
			lockResize: function(a) {
				return this.jsVars.resizeLocked = a === void 0 && !0 || a
			},
			showChartMessage: function(a, b, e) {
				var d = this.jsVars,
					c = d.hcObj;
				d.msgStore[a] && (a = d.msgStore[a]);
				b && c && c.hasRendered ? a ? c.showMessage(a, e) : c.hideLoading() : (c && c.destroy && c.destroy(), h.createChart(d.fcObj, d.container, d.type, void 0, a));
				return a
			},
			signature: function() {
				return "FusionCharts/3.3.1 (XT)"
			}
		};
	h.createChart = function(a, b, e, d, c, f, i) {
		var t = a.jsVars,
			k = t.msgStore,
			j, z = J[e],
			s, l = function(c) {
				var f = {
					renderer: "javascript"
				},
					o = t.fcObj,
					p = o.width,
					i = o.height,
					Q = z && z.eiMethods,
					k = t.$overlayButton;
				b.jsVars = a.jsVars;
				t.container = b;
				t.hcObj = c;
				t.type = e;
				t.width = b.offsetWidth;
				t.height = b.offsetHeight;
				t.instanceAPI = s;
				if (c.hasRendered) {
					g.extend(b, A);
					if (Q && typeof Q !== "string") for (var j in Q) b[j] = Q[j];
					t.overlayButtonActive && k && (k.text(t.overlayButtonMessage), c.container.appendChild(k[0]))
				}
				d && (d({
					success: c.hasRendered,
					ref: b,
					id: a.id
				}), c.hasRendered && (p = Number((p && p.match && p.match(/^\s*(\d*\.?\d*)\%\s*$/) || [])[1]), i = Number((i && i.match && i.match(/^\s*(\d*\.?\d*)\%\s*$/) || [])[1]), (p || i) && o.ref && o.ref.parentNode && da(o), h.raiseEvent("loaded", {
					type: e,
					renderer: "javascript"
				}, a, [a.id]), h.raiseEvent("rendered", {
					renderer: "javascript"
				}, o, [o.id])));
				if (c.hasRendered && t.previousDrawCount < t.drawCount) f.width = t.width, f.height = t.height, f.drawCount = t.drawCount, f.drawingLatency = s.drawingLatency, f.displayingMessage = t.hasNativeMessage, h.raiseEvent("drawcomplete", f, o, [o.id])
			};
		a.__state.dataReady = !1;
		t.instanceAPI && t.instanceAPI.dispose && t.instanceAPI.dispose();
		s = z ? new J(e) : new J("stub");
		s.chartInstance = a;
		if (c !== void 0) {
			if (typeof c === "string") c = new p(b, c), t.hasNativeMessage = !0
		} else!z || !z.init || z && z.name === "stub" ? (c = new p(b, k.ChartNotSupported), t.hasNativeMessage = !0) : t.message ? (c = new p(b, t.message), t.hasNativeMessage = !0) : t.loadError ? (c = new p(b, k.LoadDataErrorText), t.hasNativeMessage = !0) : t.stallLoad ? (c = new p(b, k.XMLLoadingText), t.hasNativeMessage = !0) : (c = a.getChartData(FusionChartsDataFormats.JSON, !0), j = c.data, c.error instanceof Error ? (c = new p(b, k.InvalidXMLText), t.hasNativeMessage = !0, i || h.raiseEvent("dataxmlinvalid", {}, t.fcObj, [t.fcObj.id])) : (i || h.raiseEvent("dataloaded", {}, t.fcObj, [t.fcObj.id]), c = s.init(b, j, a, l), t.previousDrawCount = t.drawCount, t.drawCount += 1, c.series.length === 0 ? (c = new p(b, k.ChartNoDataText), t.hasNativeMessage = !0, i || h.raiseEvent("nodatatodisplay", {}, t.fcObj, [t.fcObj.id])) : (a.__state.dataReady = !0, t.hasNativeMessage = !1, delete t.message)));
		if (!c) c = new p(b, "Error rendering chart {0x01}"), t.hasNativeMessage = !0;
		c.chart = c.chart || {};
		c.chart.renderTo = b;
		c.credits = c.credits || {};
		c.credits.enabled = z && z.creditLabel === !0 ? !0 : !1;
		if (f === !1) c.chart.animation = !1, (c.plotOptions || (c.plotOptions = {})) && (c.plotOptions.series || (c.plotOptions.series = {})), c.plotOptions.series.animation = !1;
		if (b.style) c.chart.containerBackgroundColor = t.transparent ? "transparent" : a.options.containerBackgroundColor || "#ffffff";
		return s.draw(c, l)
	};
	var wa = h.HCstub = function(a, b, d, c) {
			var a = a.chart,
				f = e(a.charttopmargin, c.charttopmargin, 15),
				p = e(a.chartrightmargin, c.chartrightmargin, 15),
				t = e(a.chartbottommargin, c.chartbottommargin, 15),
				i = e(a.chartleftmargin, c.chartleftmargin, 15),
				g = f + t,
				h = i + p;
			d *= 0.7;
			b *= 0.7;
			g > d && (f -= (g - d) * f / g, t -= (g - d) * t / g);
			h > b && (i -= (h - b) * i / h, p -= (h - b) * p / h);
			b = {
				_FCconf: {
					0: {
						stack: {}
					},
					1: {
						stack: {}
					},
					x: {
						stack: {}
					},
					oriCatTmp: [],
					noWrap: !1,
					marginLeftExtraSpace: 0,
					marginRightExtraSpace: 0,
					marginBottomExtraSpace: 0,
					marginTopExtraSpace: 0,
					marimekkoTotal: 0
				},
				chart: {
					alignTicks: !1,
					renderTo: w,
					ignoreHiddenSeries: !1,
					events: {},
					reflow: !1,
					spacingTop: f,
					spacingRight: p,
					spacingBottom: t,
					spacingLeft: i,
					marginTop: f,
					marginRight: p,
					marginBottom: t,
					marginLeft: i,
					borderRadius: 0,
					plotBackgroundColor: "#FFFFFF",
					style: {},
					animation: !e(a.defaultanimation, a.animation, 1) ? !1 : {
						duration: e(a.animationduration, 1) * 500
					}
				},
				colors: ["AFD8F8", "F6BD0F", "8BBA00", "FF8E46", "008E8E", "D64646", "8E468E", "588526", "B3AA00", "008ED6", "9D080D", "A186BE", "CC6600", "FDC689", "ABA000", "F26D7D", "FFF200", "0054A6", "F7941C", "CC3300", "006600", "663300", "6DCFF6"],
				credits: {
					href: "http://www.fusioncharts.com?BS=FCHSEvalMark",
					text: Ra,
					enabled: !0
				},
				global: {},
				labels: {
					items: []
				},
				lang: {},
				legend: {
					enabled: !0,
					symbolWidth: 12,
					borderRadius: 1,
					backgroundColor: "#FFFFFF",
					initialItemX: 0,
					title: {
						text: w,
						x: 0,
						y: 0,
						padding: 2
					},
					scroll: {},
					itemStyle: {}
				},
				loading: {},
				plotOptions: {
					series: {
						pointPadding: 0,
						borderColor: "#333333",
						events: {},
						animation: !e(a.animation, a.defaultanimation, 1) ? !1 : {
							duration: e(a.animationduration, 1) * 1E3
						},
						states: {
							hover: {
								enabled: !1
							},
							select: {
								enabled: !1
							}
						},
						dataLabels: {
							enabled: !0,
							color: "#555555",
							style: {},
							formatter: function() {
								return this.point.showPercentValues ? c.numberFormatter.percentValue(this.percentage) : this.point.displayValue
							}
						},
						point: {
							events: {}
						}
					},
					area: {
						states: {
							hover: {
								enabled: !1
							}
						},
						marker: {
							lineWidth: 1,
							radius: 3,
							states: {
								hover: {
									enabled: !1
								},
								select: {
									enabled: !1
								}
							}
						}
					},
					radar: {
						states: {
							hover: {
								enabled: !1
							}
						},
						marker: {
							lineWidth: 1,
							radius: 3,
							states: {
								hover: {
									enabled: !1
								},
								select: {
									enabled: !1
								}
							}
						}
					},
					areaspline: {
						states: {
							hover: {
								enabled: !1
							}
						},
						marker: {
							lineWidth: 1,
							radius: 3,
							states: {
								hover: {
									enabled: !1
								},
								select: {
									enabled: !1
								}
							}
						}
					},
					line: {
						shadow: !0,
						states: {
							hover: {
								enabled: !1
							}
						},
						marker: {
							lineWidth: 1,
							radius: 3,
							states: {
								hover: {
									enabled: !1
								},
								select: {
									enabled: !1
								}
							}
						}
					},
					scatter: {
						states: {
							hover: {
								enabled: !1
							}
						},
						marker: {
							lineWidth: 1,
							radius: 3,
							states: {
								hover: {
									enabled: !1
								},
								select: {
									enabled: !1
								}
							}
						}
					},
					bubble: {
						states: {
							hover: {
								enabled: !1
							}
						},
						marker: {
							lineWidth: 1,
							radius: 3,
							states: {
								hover: {
									enabled: !1
								},
								select: {
									enabled: !1
								}
							}
						}
					},
					spline: {
						states: {
							hover: {
								enabled: !1
							}
						},
						marker: {
							lineWidth: 1,
							radius: 3,
							states: {
								hover: {
									enabled: !1
								},
								select: {
									enabled: !1
								}
							}
						}
					},
					pie: {
						size: "80%",
						allowPointSelect: !0,
						cursor: "pointer",
						point: {
							events: {
								legendItemClick: a.interactivelegend === ma ? $a : function() {
									this.slice()
								}
							}
						}
					},
					pie3d: {
						size: "80%",
						allowPointSelect: !0,
						cursor: "pointer",
						point: {
							events: {
								legendItemClick: a.interactivelegend === ma ? $a : function() {
									this.slice()
								}
							}
						}
					},
					column: {},
					floatedcolumn: {},
					column3d: {},
					bar: {},
					bar3d: {}
				},
				point: {},
				series: [],
				subtitle: {
					text: w,
					style: {}
				},
				symbols: [],
				title: {
					text: w,
					style: {}
				},
				toolbar: {},
				tooltip: {
					style: {}
				},
				xAxis: {
					steppedLabels: {
						style: {}
					},
					labels: {
						x: 0,
						style: {},
						enabled: !1
					},
					lineWidth: 0,
					plotLines: [],
					plotBands: [],
					title: {
						style: {},
						text: w
					},
					tickWidth: 0,
					scroll: {
						enabled: !1
					}
				},
				yAxis: [{
					startOnTick: !1,
					endOnTick: !1,
					title: {
						style: {},
						text: w
					},
					tickLength: 0,
					labels: {
						x: 0,
						style: {}
					},
					plotBands: [],
					plotLines: []
				}, {
					tickLength: 0,
					gridLineWidth: 0,
					startOnTick: !1,
					endOnTick: !1,
					title: {
						style: {},
						text: w
					},
					labels: {
						x: 0,
						style: {},
						enabled: !1,
						formatter: function() {
							return this.value !== sa ? this.value : w
						}
					},
					opposite: !0,
					plotBands: [],
					plotLines: []
				}],
				exporting: {
					buttons: {
						exportButton: {},
						printButton: {
							enabled: !1
						}
					}
				}
			};
			if (a.palettecolors && typeof a.palettecolors === "string") b.colors = a.palettecolors.split(X);
			return c.hcJSON = b
		},
		Ia = h.placeVerticalAxis = function(a, b, d, c, f, p, t, i, g, h) {
			var k = d[s],
				j = k.smartLabel,
				z, A, l, n, m = 0,
				i = k.marginRightExtraSpace,
				H = k.marginLeftExtraSpace,
				r = {},
				P = {},
				q = {},
				u = a.plotLines,
				qa = a.plotBands,
				k = b.verticalAxisValuesPadding,
				ga = !isNaN(b.fixedValuesPadding) ? b.fixedValuesPadding : 0,
				v = k - ga,
				da = b.verticalAxisValuesPadding,
				ea = b.verticalAxisNamePadding,
				x = b.verticalAxisNameWidth,
				wa = b.rotateVerticalAxisName,
				Ia = a.offset ? a.offset : 0,
				ra = 0,
				Ba = 0,
				ba = 0,
				G = 0,
				I = 0,
				Da = 0,
				T = 0,
				la, O, za, C, k = 2,
				T = t ? i + 8 : H + 4,
				K = function(a, e) {
					var d, c;
					if (a && a.label && B(a.label.text) !== void 0) {
						za = a.label;
						if (za.style && za.style !== O) O = za.style, j.setStyle(O);
						z = j.getOriSize(a.label.text);
						c = (d = z.width) ? d + 2 : 0;
						if (a.isGrid) {
							if (r[e] = {
								width: d,
								height: z.height,
								label: za
							}, G <= c) G = c, b.lYLblIdx = e
						} else a.isTrend && (t && za.textAlign === ca || za.textAlign === N ? (P[e] = {
							width: d,
							height: z.height,
							label: za
						}, I = F(I, c)) : (q[e] = {
							width: d,
							height: z.height,
							label: za
						}, Da = F(Da, c)))
					}
				},
				D = function(b, e) {
					var d, c = e ? m : m + b;
					A = A || {};
					return c > 0 ? (wa ? (c < A.height && (A = j.getSmartText(a.title.text, f, c)), d = A.height) : (c < A.width && (A = j.getSmartText(a.title.text, c, f)), d = A.width), a.title.text = A.text, A.tooltext && (a.title.originalText = A.tooltext), e ? c - d + b : c - d) : (a.title.text = w, 0)
				},
				ka = function(a, b, e) {
					for (var d in a) a[d].label.x = b, a[d].label.y = e
				};
			la = 0;
			for (ba = qa.length; la < ba; la += 1) K(qa[la], la);
			la = 0;
			for (ba = u.length; la < ba; la += 1) K(u[la], la);
			if (a.title && a.title.text != w) O = a.title.style, j.setStyle(O), l = j.getOriSize(Wa).height, wa ? (A = j.getSmartText(a.title.text, f, p), m = A.height, n = l) : (a.title.rotation = 0, A = j.getSmartText(a.title.text, x !== void 0 ? x : p, f), m = A.width, n = 20);
			Da > 0 && (Ba = Da + da);
			g && (c = e(c.chart.maxlabelwidthpercent, 0), c >= 1 && c <= 100 && (g = c * g / 100, G > g && (G = g)));
			ra = F(I, G);
			ra += ra ? v + ga : 0;
			m > 0 && (ra += m + ea + T);
			(function() {
				if (Ba + ra > p) {
					C = Ba + ra - p;
					if (Ba) if (da >= C) {
						da -= C;
						return
					} else C -= da, da = 0;
					if (v + ea >= C) ea >= C ? ea -= C : (v -= C - ea, ea = 0);
					else {
						C -= v + ea;
						ea = v = 0;
						if (Da > 20) if (I > G) if (Da - I >= C) {
							Da -= C;
							return
						} else if (I - Da >= C) {
							I -= C;
							return
						} else if (I > Da ? (C -= I - Da, I = Da) : (C -= Da - I, Da = I), 2 * (I - G) >= C) {
							Da -= C / 2;
							I -= C / 2;
							return
						} else C -= 2 * (I - G), Da = I = G;
						else if (Da - 20 >= C) {
							Da -= C;
							return
						} else C -= Da - 20, Da = 20;
						if (I > G) if (I - G >= C) {
							I -= C;
							return
						} else C -= I - G, I = G;
						m - n >= C ? m -= C : (C -= m - n, m = n, Da >= C ? Da = 0 : (C -= Da, Da = 0, m >= C ? m = 0 : (C -= m, m = 0, G >= C && (G -= C, I = G))))
					}
				}
			})();
			ba = function(a, b) {
				var e, d = 0,
					c = b ? Da - 2 : Da + a - 2;
				if (Da > 0) {
					for (la in q) if (za = q[la].label, q[la].width > c) {
						if (za.style && za.style !== O) O = za.style, j.setStyle(O);
						e = j.getSmartText(za.text, c, f, !0);
						za.text = e.text;
						e.tooltext && (za.originalText = e.tooltext);
						q[la].height = e.height;
						d = Math.max(d, e.width)
					} else d = Math.max(d, q[la].width);
					return b ? c - d + a : c - d
				} else {
					for (la in q) q[la].label.text = w;
					return 0
				}
			}(0, !0);
			ba = D(ba, !0);
			ba = function(a) {
				var b = 0,
					e = Math.max(G, I) + a - 2;
				if (e > 0) {
					for (la in r) if (za = r[la].label, r[la].width > e) {
						if (za.style && za.style !== O) O = za.style, j.setStyle(O);
						a = j.getSmartText(za.text, e, f, !0);
						za.text = a.text;
						a.tooltext && (za.originalText = a.tooltext);
						r[la].height = a.height;
						b = Math.max(b, a.width)
					} else b = Math.max(b, r[la].width);
					for (la in P) if (za = P[la].label, P[la].width > e) {
						if (za.style && za.style !== O) O = za.style, j.setStyle(O);
						a = j.getSmartText(za.text, e, f, !0);
						za.text = a.text;
						a.tooltext && (za.originalText = a.tooltext);
						P[la].height = a.height;
						b = Math.max(b, a.width)
					} else b = Math.max(b, P[la].width);
					return e - b
				} else {
					for (la in r) r[la].label.text = w;
					for (la in P) P[la].label.text = w;
					return 0
				}
			}(ba);
			ba = D(ba);
			g = b.verticalAxisNamePadding - ea;
			ba && g && (ba > g ? (ea += g, ba -= g) : (ea += ba, ba = 0));
			g = b.verticalAxisValuesPadding - (v + ga);
			ba && g && (ba > g ? (v += g, ba -= g) : (v += ba, ba = 0));
			g = b.verticalAxisValuesPadding - da;
			ba && g && (ba > g ? (da += g, ba -= g) : (da += ba, ba = 0));
			Da > 0 && (Ba = Da + da);
			ra = F(I, G);
			ra += ra ? v + ga : 0;
			m > 0 && (ra += m + ea + T);
			g = F(I, G);
			g += g > 0 ? v + ga : 0;
			m > 0 ? (wa ? m < A.height && (A = j.getSmartText(a.title.text, f, m)) : (m < A.width && (A = j.getSmartText(a.title.text, m, f)), a.title.y = -((A.height - l) / 2)), a.title.text = A.text, A.tooltext && (a.title.originalText = A.tooltext), a.title.margin = g + ea + T + (wa ? m - l : m / 2)) : a.title.text = w;
			l = -(v + ga + Ia + H + 2);
			i = i + da + Ia + 2;
			T = F(I, G);
			a.labels.style && (k = parseInt(a.labels.style.fontSize, 10) * 0.35);
			t ? (Da > 0 && ka(q, l, k), T > 0 && (ka(r, i, k), ka(P, i, k))) : (Da > 0 && ka(q, i, k), T > 0 && (ka(r, l, k), ka(P, l, k)));
			a.labels._textY = k;
			a.labels._righttX = i;
			a.labels._leftX = l;
			h ? (d.chart.marginLeft += t ? Ba : ra - h, d.chart.marginRight += t ? ra - h : Ba) : (d.chart.marginLeft += t ? Ba : ra, d.chart.marginRight += t ? ra : Ba);
			return Ba + ra
		},
		Ja = h.titleSpaceManager = function(a, b, d, c) {
			var f = b.chart,
				p = fa(f.caption),
				t = fa(f.subcaption),
				i = b = e(f.captionpadding, 10),
				g = a[s].smartLabel,
				h = !1,
				k, j = 0,
				z, A, l = 0,
				m = 0,
				r = 0,
				H = 0,
				P = a.title,
				q = a.subtitle,
				l = e(f.plotBorderWidth, 1),
				l = e(f.canvasborderthickness, 1);
			b < l && (b = l);
			if (p !== w) z = P.style, r = e(parseInt(z.fontHeight, 10), parseInt(z.lineHeight, 10), 12), e(parseInt(z.fontSize, 10), 10);
			if (t !== w) A = q.style, H = e(parseInt(A.fontHeight, 10), parseInt(A.lineHeight, 10), 12), e(parseInt(A.fontSize, 10), 10);
			if (r > 0 || H > 0) {
				j = r + H + b;
				j > c ? (l = j - c, h = !0, l < b ? b -= l : (l -= b, b = 0, H > l ? (m = H - l + 10, H = 0) : (l -= H, H = 0, r > l && (m = r - l), r = 0))) : m = c - j;
				if (r > 0) g.setStyle(z), r += m, k = g.getSmartText(p, d, r), m = r - k.height, r = k.height, P.text = k.text, P.height = k.height, k.tooltext && (P.originalText = k.tooltext);
				if (H > 0) g.setStyle(A), H += m, d = g.getSmartText(t, d, H), m = H - d.height, H = d.height, q.text = d.text, q.height = d.height, d.tooltext && (P.originalText = k.tooltext);
				h && m > 0 && (b += n(i - b, m));
				j = r + H + b;
				a.chart.marginTop += j
			}
			return j
		},
		Ba = h.stepYAxisNames = function(a, b, e, d, c, p) {
			for (var t = 0, i = d.plotLines, g = [], k, h = d.plotLines.length, b = b[s].smartLabel, j = parseFloat(f(e.basefontsize, 10)), z; t < h; t += 1) e = i[t], e.isGrid && e.label && e.label.text && (g.push(e), e.value === 0 && (k = g.length - 1));
			if (h = g.length) if (d.labels.style ? b.setStyle(d.labels.style) : g[0].label && g[0].label.style && b.setStyle(d.labels.style), t = b.getOriSize("W").height, p || (t += j * 0.4), a /= h - 1, a < t) {
				p = Math.max(1, Math.ceil(t / a));
				for (t = a = k; t < h; t += 1) {
					e = g[t];
					if (t === c) {
						if ((t - a) % p && z) z.label.text = "";
						a = c
					}
					if (e && e.label)(t - a) % p ? e.label.text = w : z = e
				}
				for (t = a = k; t >= 0; t -= 1) {
					e = g[t];
					if (t === c) {
						if ((a - t) % p && z) z.label.text = "";
						a = c
					}
					if (e && e.label)(a - t) % p ? e.label.text = w : z = e
				}
			}
		},
		ea = h.placeHorizontalAxis = function(a, b, d, c, f, p, t) {
			var i = d[s],
				g = c && c.chart || {},
				h, j, z, A, l, m, n, H, r, P, q = 0,
				qa = 0,
				v = 10,
				ga = 1,
				da = 0,
				ea = da = 0,
				x = 0,
				wa = !1,
				ba = !1,
				ra = !1,
				G = e(g.labelstep, 0),
				Ia = e(g.xaxisminlabelwidth, 0),
				Ba = b.labelDisplay,
				I = b.rotateLabels,
				la = b.horizontalLabelPadding,
				za = i.marginBottomExtraSpace;
			r = d.chart.marginLeft;
			var T = d.chart.marginRight,
				C = i.smartLabel,
				O = i.plotBorderThickness,
				ka = b.catCount,
				K = b.slantLabels,
				D = f / (a.max - a.min),
				Qa = 0,
				fa = 0,
				ea = {
					w: 0,
					h: 0
				},
				R = c && c.chart || {},
				c = e(R.updateinterval, R.refreshinterval) * 1E3,
				R = R.datastreamurl,
				U = Boolean(this.realtimeEnabled && c && R !== void 0);
			if (a.labels.style) m = a.labels.style, C.setStyle(m), H = C.getOriSize("W"), v = C.lineHeight, n = H.width + 4, P = C.getOriSize("WWW").width + 4;
			var V, M, J, S = [],
				c = [],
				Y = 0,
				Z = 0,
				aa, ma, ta, ia, R = b.horizontalAxisNamePadding;
			M = 0;
			var X = b.staggerLines,
				$ = Qa,
				sa = !1,
				pa = !1;
			if (a.title && a.title.text != w) m = a.title.style, C.setStyle(m), da = C.getOriSize("W").height, a.title.rotation = 0, A = C.getSmartText(a.title.text, f, p), qa = A.height;
			r != parseInt(g.chartleftmargin, 10) && (h = !0);
			T != parseInt(g.chartrightmargin, 10) && (J = !0);
			g.canvaspadding !== void 0 && g.canvaspadding !== "" && (pa = !0);
			M = f - t;
			switch (Ba) {
			case "none":
				wa = ra = !0;
				I && (q = K ? 300 : 270, H = v, v = n, n = H);
				break;
			case "rotate":
				q = K ? 300 : 270;
				H = v;
				v = n;
				n = H;
				wa = !0;
				break;
			case "stagger":
				ba = wa = !0;
				t = Math.floor((p - da) / v);
				t < X && (X = t);
				break;
			default:
				I && (q = K ? 300 : 270, H = v, v = n, n = H)
			}
			i.isBar && (wa = !0);
			g = 0;
			t = a.plotLines;
			if (typeof d._FCconf.isXYPlot === Ca && !i.isBar) {
				for (aa = t.length; g < aa; g += 1)(j = t[g]) && (j.isGrid ? S.push(j) : j.isTrend && c.push(j));
				I = a.plotBands;
				g = 0;
				for (aa = I.length; g < aa; g += 1)(j = I[g]) && c.push(j);
				I = S.length - 1;
				aa = S.length;
				ba && (X > aa ? X = aa : X < 2 && (X = 2));
				if (aa) {
					a.scroll && a.scroll.viewPortMin && a.scroll.viewPortMax ? (z = a.scroll.viewPortMin, V = a.scroll.viewPortMax, J = h = !1) : (z = a.min, V = a.max);
					g = (S[I].value - S[0].value) * D;
					ma = g / (ka - 1);
					ta = (S[0].value - z) * D;
					ia = (V - S[I].value) * D;
					Ba === "auto" ? ma < P && (q = K ? 300 : 270, H = v, v = n, n = H, wa = !0) : Ba === "stagger" && (ma *= X);
					this.defaultSeriesType !== "line" && (this.defaultSeriesType === "area" ? i.drawFullAreaBorder && (O > ta && (z = a.min -= O / (2 * D), ta += (S[0].value - z) * D), O > ia && (V = a.max += O / (2 * D), ia += (V - S[I].value) * D)) : (O > ta && (z = a.min -= O / (2 * D), ta += (S[0].value - z) * D), O > ia && (V = a.max += O / (2 * D), ia += (V - S[I].value) * D)));
					n < Ia && (n = Ia);
					ga = !ba && !ra ? Math.max(1, G, Math.ceil(n / ma)) : Math.max(1, G);
					if (i.x) i.x.stepValue = ga;
					ma *= ga;
					r = (ta + r) * 2;
					if ((l = t[0].label) && l.text) l.style && C.setStyle(l.style), P = q === 270 ? Math.min(ma, C.getOriSize(l.text).height + 4) : Math.min(ma, C.getOriSize(l.text).width + 4), P > r && (ra || (Y = (P - r) / 2), h || (pa && (Y = 0), ma -= Y / (ka - 1), H = ma * (ka - 1), D = ma, r = (g - H) / D, V = a.max += r, z = a.min -= r, Y = 0, g = H, ta = (S[0].value - z) * D, ia = (V - S[I].value) * D));
					r = (ia + T) * 2;
					if ((l = t[I].label) && l.text) l.style && C.setStyle(l.style), P = q === 270 ? Math.min(ma, C.getOriSize(l.text).height + 4) : Math.min(ma, C.getOriSize(l.text).width + 4), P > r && (ra || (Z = (P - r) / 2), J || (pa && (Z = 0), ma -= Z / (ka - 1), H = ma * (ka - 1), D = ma, r = (g - H) / D, Z = 0, g = H, ta = (S[0].value - z) * D, ia = (V - S[I].value) * D));
					g = Y + Z;
					if (g > 0) {
						M > g ? (T = (T = Z * f / (Z + f)) ? T + 4 : 0, d.chart.marginRight += T, f -= T, T = (T = Y * f / (Y + f)) ? T + 4 : 0, d.chart.marginLeft += T, f -= T, D = f / (a.max - a.min)) : Y < Z ? M >= Z && J ? (T = (T = Z * f / (Z + f)) ? T + 4 : 0, d.chart.marginRight += T, f -= T, D = f / (a.max - a.min)) : h && (T = (T = Y * f / (Y + f)) ? T + 4 : 0, d.chart.marginLeft += T, f -= T, D = f / (a.max - a.min)) : M >= Y && h ? (T = (T = Y * f / (Y + f)) ? T + 4 : 0, d.chart.marginLeft += T, f -= T, D = f / (a.max - a.min)) : J && (T = (T = Z * f / (Z + f)) ? T + 4 : 0, d.chart.marginRight += T, f -= T, D = f / (a.max - a.min));
						g = (S[I].value - S[0].value) * D;
						ma = g / (ka - 1);
						ba && (ma *= X);
						ga = !ba && !ra ? q ? Math.max(1, G, Math.ceil(v / ma)) : Math.max(1, G, Math.ceil(n / ma)) : Math.max(1, G);
						if (i.x) i.x.stepValue = ga;
						ma *= ga
					}
					for (z = 0; z < aa; z += 1) {
						j = S[z];
						if (z % ga && j.label) {
							if (j.stepped = !0, j.label.style = a.steppedLabels.style, !U) continue
						} else j.stepped = !1, j.label.style = a.labels.style;
						if (j && j.label && B(j.label.text) !== void 0) {
							l = j.label;
							if (l.style && l.style !== m) m = l.style, C.setStyle(m);
							if (q && ra) h = C.getOriSize(l.text), ea.w = F(ea.w, h.width + 4), ea.h = F(ea.h, h.height);
							else if (!ra) h = q || ba ? C.getOriSize(l.text) : C.getSmartText(l.text, ma - 4, p, wa), ea.w = F(ea.w, h.width + 4), ea.h = F(ea.h, h.height)
						}
					}
				}
				z = 0;
				for (aa = c.length; z < aa; z += 1) if ((j = c[z]) && j.label && B(j.label.text) !== void 0) {
					l = j.label;
					if (l.style && l.style !== m) m = l.style, C.setStyle(m);
					h = C.getOriSize(l.text);
					l.verticalAlign === u ? Qa = F(Qa, h.height) : fa = F(fa, h.height)
				}
				a.scroll && a.scroll.enabled && !q && !ra && (r = ea.w / 2, d.chart.marginLeft < r && (T = r - d.chart.marginLeft, M > T && (f -= T, M -= T, d.chart.marginLeft += T)), d.chart.marginRight < r && (T = r - d.chart.marginRight, M > T && (f -= T, M -= T, d.chart.marginRight += T)))
			} else {
				var G = {},
					va, Ba = Z = 0,
					O = pa = null,
					ka = {},
					sa = !0,
					D = f / (a.max - a.min),
					Ia = function(b, e, c) {
						var f, o, y, g, p, t;
						t = b.plotObj;
						p = b.labelTextWidth;
						if (!p) {
							l = t.label;
							if (l.style && l.style !== m) m = l.style, C.setStyle(m);
							p = C.getOriSize(l.text).width + 4;
							b.oriWidth = p;
							p > va && (p = va);
							b.labelTextWidth = p;
							b.leftEdge = t.value * D - p / 2;
							b.rightEdge = t.value * D + p / 2;
							if (c) p = Math.min(p, 2 * (j.value - a.min) * D + d.chart.marginLeft), b.labelTextWidth = p
						}
						if (typeof e !== Ca) {
							c = e.plotObj;
							l = c.label;
							if (l.style && l.style !== m) m = l.style, C.setStyle(m);
							e.oriWidth ? y = e.oriWidth : (y = C.getOriSize(l.text).width + 4, e.oriWidth = y);
							y > va && (y = va);
							e.labelTextWidth = y;
							e.leftEdge = c.value * D - y / 2;
							e.rightEdge = c.value * D + y / 2;
							f = t.value * D;
							o = f + p / 2;
							g = c.value * D;
							y = g - y / 2;
							if (y < o) if (f + n < g - n) o -= y, f = g - f, b.labelTextWidth = o > f ? Math.min(p, f) : Math.max(n, p - o / 2), e.labelTextWidth = 2 * (f - b.labelTextWidth / 2), b.leftEdge = t.value * D - b.labelTextWidth / 2, b.rightEdge = t.value * D + b.labelTextWidth / 2, e.leftEdge = c.value * D - e.labelTextWidth / 2, e.rightEdge = c.value * D + e.labelTextWidth / 2;
							else return e.labelTextWidth = 0, c.label.text = w, !1
						} else if (c) p = Math.min(p, 2 * (a.max - j.value) * D + d.chart.marginRight), b.labelTextWidth = p, b.leftEdge = t.value * D - p / 2, b.rightEdge = t.value * D + p / 2;
						b.nextCat = e;
						return !0
					};
				ba ? X > aa ? X = aa : X < 2 && (X = 2) : X = 1;
				for (aa = t.length; g < aa; g += 1) if ((j = t[g]) && j.label && typeof j.label.text !== Ca) j.isGrid ? (Y = {
					plotObj: j
				}, j.isCat && (I = g % X, G[I] || (G[I] = []), pa ? (O = Y, G[I].push(O)) : (O = pa = Y, G[I].push(pa))), S.push(Y)) : j.isTrend && c.push({
					plotObj: j
				});
				I = a.plotBands;
				g = 0;
				for (aa = I.length; g < aa; g += 1)(j = I[g]) && j.isTrend && j.label && typeof j.label.text !== Ca && c.push({
					plotObj: j
				});
				if (S.length) if (!ra && !q) if (i.distributedColumns) {
					g = 0;
					for (aa = S.length; g < aa; g += 1) if (z = S[g], J = g % X, j = z.plotObj, j.label && j.isCat) {
						g - X >= 0 ? (h = S[g - X], J = h.plotObj.value * D + h.plotObj._weight * D / 2) : (h = null, J = a.min * D - r);
						g + X < aa ? (H = S[g + X], H = H.plotObj.value * D - H.plotObj._weight * D / 2) : (H = null, H = a.max * D + T);
						l = j.label;
						if (l.style && l.style !== m) m = l.style, C.setStyle(m);
						Y = j.value * D;
						M = Y - j._weight * D / 2;
						Y += j._weight * D / 2;
						X > 1 ? (h = M - J, J = Y + H, J = Y - M + Math.min(h, J)) : J = Y - M;
						l = j.label;
						l.style && l.style !== m && C.setStyle(l.style);
						J < n && n < C.getOriSize(l.text).width ? (j.label.text = w, z.labelTextWidth = 0) : (z.labelTextWidth = J, h = C.getSmartText(l.text, J - 4, p, wa), J = h.width + 4, z.labelTextWidth = J, ea.h = Math.max(ea.h, h.height))
					}
				} else {
					aa = S.length;
					I = S.length - 1;
					(g = (S[I].plotObj.value - S[0].plotObj.value) * D) ? (va = g * 0.1, U = Math.max(g * 0.2, g / aa)) : U = va = f;
					for (z in G) {
						g = 0;
						for (P = G[z].length; g < P;) {
							for (Y = g + 1; !Ia(G[z][g], G[z][Y]);) Y += 1;
							g = Y
						}
					}
					pa && (Ba = (pa.plotObj.value - a.min) * D + r - pa.labelTextWidth / 2);
					j = S[0].plotObj;
					if (!pa || j !== pa.plotObj) {
						l = j.label;
						if (l.style && l.style !== m) m = l.style, C.setStyle(m);
						P = C.getOriSize(l.text).width + 4;
						Y = (j.value - a.min) * D + r;
						pa && (g = Ba - Y, P = g < P && g > n / 2 ? g * 2 : 0);
						S[0].labelTextWidth = P;
						P > 0 && (H = Y - P / 2);
						H < Ba && (Ba = H)
					}
					if (O) P = O.labelTextWidth, Z = (a.max - O.plotObj.value) * D + T - P / 2;
					j = S[I].plotObj;
					if (!O || j !== O.plotObj) {
						l = j.label;
						if (l.style && l.style !== m) m = l.style, C.setStyle(m);
						P = C.getOriSize(l.text).width + 4;
						Y = (a.max - j.value) * D + T;
						O && (g = Y - Z, P = g < P && g > n / 2 ? g * 2 : 0);
						S[I].labelTextWidth = P;
						P > 0 && (H = Y - P / 2);
						H < Z && (Z = H)
					}
					Y = Ba < 0 ? -Ba : 0;
					Z = Z < 0 ? -Z : 0;
					g = Y + Z;
					if (g > 0) for (z in M > g ? (T = (T = Z * f / (Z + f)) ? T + 4 : 0, d.chart.marginRight += T, f -= T, T = (T = Y * f / (Y + f)) ? T + 4 : 0, d.chart.marginLeft += T, f -= T, D = f / (a.max - a.min)) : Y < Z ? M >= Z && J ? (T = (T = Z * f / (Z + f)) ? T + 4 : 0, d.chart.marginRight += T, f -= T, D = f / (a.max - a.min)) : h && (T = (T = Y * f / (Y + f)) ? T + 4 : 0, d.chart.marginLeft += T, f -= T, D = f / (a.max - a.min)) : M >= Y && h ? (T = (T = Y * f / (Y + f)) ? T + 4 : 0, d.chart.marginLeft += T, f -= T, D = f / (a.max - a.min)) : J && (T = (T = Z * f / (Z + f)) ? T + 4 : 0, d.chart.marginRight += T, f -= T, D = f / (a.max - a.min)), T = d.chart.marginRight, r = d.chart.marginLeft, g = (S[I].plotObj.value - S[0].plotObj.value) * D, va = g * 0.1, U = Math.max(g * 0.2, g / aa), G) {
						g = 0;
						for (P = G[z].length; g < P;) {
							for (Y = g + 1; !Ia(G[z][g], G[z][Y], !0);) Y += 1;
							g = Y
						}
						z += 1
					}
					g = 0;
					for (aa = S.length; g < aa; g += 1) if (z = S[g], J = g % X, j = z.plotObj, j.label) if (j.isCat) z.labelTextWidth && (ka[J] = z);
					else {
						H = (h = ka[J]) ? h.nextCat : G[J] ? G[J][0] : null;
						h = null;
						if (g >= X) {
							J = g - X;
							for (h = S[J]; !h.labelTextWidth;) if (J >= X) J -= X, h = S[J];
							else {
								h = null;
								break
							}
						}
						J = h ? h.rightEdge : a.min * D - r;
						H = H ? H.leftEdge : a.max * D + T;
						l = j.label;
						if (l.style && l.style !== m) m = l.style, C.setStyle(m);
						P = C.getOriSize(l.text).width + 4;
						M = j.value * D - P / 2;
						if (i.isBar && g == aa - 1 && h) {
							if (J > M) h.plotObj.label.text = w, h.labelTextWidth = 0, J = h.leftEdge
						} else if (J > M || H < M + P) {
							j.label.text = w;
							z.labelTextWidth = 0;
							continue
						}
						J = Math.max(J, M);
						Y = j.value * D;
						J = 2 * Math.min(Y - J, H - Y);
						J.toFixed && (J = J.toFixed(2));
						l = j.label;
						l.style && l.style !== m && C.setStyle(l.style);
						J < n && n < C.getOriSize(l.text).width ? (j.label.text = w, z.labelTextWidth = 0) : (z.labelTextWidth = J, h = C.getSmartText(l.text, J - 4, p, wa), J = h.width + 4, z.labelTextWidth = J, z.leftEdge = Y - J / 2, z.rightEdge = Y + J / 2, ea.h = Math.max(ea.h, h.height))
					}
					h = T = J = r = null;
					g = 0;
					for (aa = S.length; g < aa; g += 1) if (z = S[g], j = z.plotObj, J = g % X, j.isCat && z.labelTextWidth) {
						h = T = null;
						Y = j.value * D;
						if (g >= X) {
							J = g - X;
							for (h = S[J]; !h.labelTextWidth;) if (J > X) J -= X, h = S[J];
							else {
								h = null;
								break
							}
						}
						h = h ? Y - h.rightEdge : Y - a.min * D + d.chart.marginLeft;
						if (g + X < aa) {
							r = g + X;
							for (T = S[r]; !T.labelTextWidth;) if (r + X < aa - 1) r += X, T = S[r];
							else {
								T = null;
								break
							}
						}
						J = T ? T.leftEdge - Y : a.max * D + d.chart.marginRight - Y;
						J = Math.min(h, J) * 2;
						J > U && (J = U);
						if (J > z.oriWidth) J = z.oriWidth;
						z.labelTextWidth = J;
						l = j.label;
						l.style && l.style !== m && C.setStyle(l.style);
						h = C.getSmartText(l.text, J - 4, p, wa);
						z.labelTextWidth = h.width + 4;
						ea.h = Math.max(ea.h, h.height);
						z.rightEdge = Y + z.labelTextWidth / 2
					}
				} else if (q) {
					g = 0;
					for (aa = S.length; g < aa; g += 1) if ((j = S[g].plotObj) && j.label && j.label.text) {
						l = j.label;
						if (l.style && l.style !== m) m = l.style, C.setStyle(m);
						z = 1;
						if (g + z < aa) for (T = S[z + g].plotObj; T && (T.value - j.value) * D < n;) if (j.isCat) {
							if (T.label) {
								T.label.text = w;
								z += 1;
								if (z + g >= aa - 1) break;
								T = t[z + g].plotObj
							}
						} else if (T.isCat) {
							j.label.text = w;
							j = T;
							g += z - 1;
							l = j.label;
							if (l.style && l.style !== m) m = l.style, C.setStyle(m);
							break
						}
						ea.w = Math.max(ea.w, C.getOriSize(l.text).width + 4)
					}
				}
				z = 0;
				for (aa = c.length; z < aa; z += 1) if ((j = c[z].plotObj) && j.label && B(j.label.text) !== void 0) {
					l = j.label;
					if (l.style && l.style !== m) m = l.style, C.setStyle(m);
					h = C.getOriSize(l.text);
					l.verticalAlign === u ? Qa = F(Qa, h.height) : fa = F(fa, h.height)
				}
			}
			if (ra) {
				if (M = v, q) M = ea.w
			} else M = q ? ea.w : ba ? X * v : ea.h;
			M > 0 && ($ += la + M);
			qa > 0 && ($ += qa + R);
			ea = la - 4;
			$ = fa + $ + 2;
			H = 0;
			$ > p && (g = $ - p, R > g ? (R -= g, g = 0) : (g -= R, R = 0, ea > g ? (ea -= g, differnece = 0) : (g -= ea, ea = 0), la = ea + 4), fa > g ? (fa -= g, g = 0) : (fa > 0 && (g -= fa, fa = 0), g > 0 && (Qa > g ? (Qa -= g, g = 0) : (Qa > 0 && (g -= Qa, Qa = 0), g > 0 && ((H = qa - da) > g ? (qa -= g, g = 0) : (g -= H, qa = da, g > 0 && ((H = M - v) > g ? (M -= g, g = 0) : (g -= H, M = v, g > 0 && (g -= qa + R, qa = 0, g > 0 && (g -= M, M = 0, g > 0 && (la -= g)))))))))));
			la += za;
			var T = i.is3d ? -d.chart.xDepth : 0,
				p = M + la,
				Aa, $ = T;
			r = v * 0.5;
			da = v + la;
			aa = S.length;
			ea = 0;
			if (sa) if (q) {
				ma = N;
				Aa = K ? la + 8 : la + 4;
				aa = S.length;
				for (z = 0; z < aa; z += 1) if ((j = S[z].plotObj) && j.label && B(j.label.text) !== void 0) {
					l = j.label;
					if (l.style && l.style !== m) m = l.style, C.setStyle(m);
					g = 1;
					h = C.getSmartText(l.text, M - 4, n, wa);
					l.text = h.text;
					h.tooltext && (l.originalText = h.tooltext);
					$ = T + r / 2;
					l.y = Aa;
					l.x = $;
					l.rotation = q;
					l.textAlign = ma;
					ea += 1
				}
			} else {
				sa = M;
				ma = k;
				Aa = da;
				for (z = 0; z < aa; z += ga) if ((j = S[z].plotObj) && j.label && B(j.label.text) !== void 0) {
					l = j.label;
					if (l.style && l.style !== m) m = l.style, C.setStyle(m);
					if (!ra) h = C.getSmartText(l.text, S[z].labelTextWidth - 4, sa, wa), l.text = h.text, h.tooltext && (l.originalText = h.tooltext), ba && (Aa = da + ea % X * v);
					l.y = Aa;
					l.x = $;
					l.rotation = q;
					l.textAlign = ma;
					ea += 1
				}
			} else {
				q ? (sa = ma, g = M - 4, ma = N, Aa = K ? la + 8 : la + 4) : ba ? (sa = v, g = ma - 4, ma = k) : (sa = M, g = ma - 4, ma = k, Aa = da);
				for (z = 0; z < aa; z += ga) if ((j = S[z]) && j.label && B(j.label.text) !== void 0) {
					l = j.label;
					if (l.style && l.style !== m) m = l.style, C.setStyle(m);
					if (!ra) h = C.getSmartText(l.text, g, sa, wa), l.text = h.text, h.tooltext && (l.originalText = h.tooltext), ba && (Aa = da + ea % X * v), q && ($ = T + r / 2);
					l.y = Aa;
					l.x = $;
					l.rotation = q;
					l.textAlign = ma;
					ea += 1
				}
				b._labelY = da;
				b._labelX = T;
				b._yShipment = Aa;
				b._isStagger = ba;
				b._rotation = q;
				b._textAlign = ma;
				b._adjustedPx = r / 2;
				b._staggerLines = X;
				b._labelHeight = v
			}
			aa = c.length;
			for (z = q = b = 0; z < aa; z += 1) if ((j = c[z].plotObj ? c[z].plotObj : c[z]) && j.label && B(j.label.text) !== void 0) {
				l = j.label;
				if (l.style && l.style !== m) m = l.style, C.setStyle(m);
				l.verticalAlign === u ? (h = C.getSmartText(l.text, f, Qa, !0), q = Math.max(q, h.height), l.text = h.text, h.tooltext && (l.originalText = h.tooltext), l.y = p + C.getOriSize(l.text).height, l.x = $) : (h = C.getSmartText(l.text, f, fa, !0), b = Math.max(b, h.height), l.text = h.text, h.tooltext && (l.originalText = h.tooltext), l.y = -(fa - C.getOriSize("W").height + la + 2))
			}
			if (qa > 0) C.setStyle(a.title.style), A = C.getSmartText(a.title.text, f, qa), a.title.text = A.text, A.tooltext && (a.title.originalText = A.tooltext), a.title.margin = p + q + R;
			$ = q;
			if (M > 0) i.horizontalAxisHeight = la + M - za, $ += i.horizontalAxisHeight;
			qa > 0 && ($ += x = qa + R);
			d.chart.marginBottom += $;
			b > 0 && (d.chart.marginTop += b, $ += b);
			if (a.opposite) {
				a.title.margin -= M - (A && A.height || 0) + la;
				$ -= x;
				d.chart.marginTop += $;
				d.chart.marginBottom -= $;
				d.xAxis.opposite = 1;
				aa = t.length;
				for (g = 0; g < aa; g += 1) if ((j = t[g]) && j.isGrid && (l = j.label) && l.text !== void 0) l.textAlign = ca, l.y -= Aa + la + 4
			}
			return $
		},
		ra = h.configureLegendOptions = function(a, c, f, g, o) {
			var g = a.legend,
				p = a.chart,
				t = p.paletteIndex,
				h = p.is3D ? qa.chart3D : qa.chart2D,
				i = p.useRoundEdges,
				j = e(c.legendiconscale, 1),
				k = (parseInt(g.itemStyle.fontSize, 10) || 10) + 1,
				z = a.chart.defaultSeriesType,
				l = 3;
			if (j <= 0 || j > 5) j = 1;
			g.padding = 4;
			k <= 0 && (k = 1);
			o -= 8;
			k *= j;
			l *= j;
			k = Math.min(k, o);
			k <= 0 && (l = k = 0);
			g.symbolWidth = k;
			g.symbolPadding = l;
			g.textPadding = 4;
			g.legendHeight = o = k + 2 * l;
			g.rowHeight = Math.max(parseInt(g.itemStyle.lineHeight, 10) || 12, o);
			f ? (g.align = N, g.verticalAlign = "middle", g.layout = "vertical") : g.x = (p.marginLeft - p.spacingLeft - p.marginRight + p.spacingRight) / 2;
			f = b(c.legendbordercolor, r[h.legendBorderColor][t]);
			o = e(c.legendborderalpha, 100);
			p = e(c.legendbgalpha, 100);
			g.backgroundColor = d(b(c.legendbgcolor, r[h.legendBgColor][t]), p);
			g.borderColor = d(f, o);
			g.borderWidth = e(c.legendborderthickness, !i || c.legendbordercolor ? 1 : 0);
			g.shadow = Boolean(e(c.legendshadow, 1));
			if (g.shadow) g.shadow = {
				enabled: g.shadow,
				opacity: F(o, p) / 100
			};
			g.reversed = Boolean(e(c.reverselegend, 0));
			if (/^pie|pie3d$/.test(z)) g.reversed = !g.reversed;
			g.style = {
				padding: 4
			};
			Boolean(e(c.interactivelegend, 1)) ? g.symbolStyle = {
				_cursor: "hand",
				cursor: "pointer"
			} : (a.legend.interactiveLegend = !1, g.itemStyle.cursor = "default", g.itemHoverStyle = {
				cursor: "inherit"
			});
			g.borderRadius = e(c.legendborderradius, i ? 3 : 0);
			g.legendAllowDrag = Boolean(e(c.legendallowdrag, 0));
			g.title.text = fa(x(c.legendcaption, w));
			g.legendScrollBgColor = v(b(c.legendscrollbgcolor, r.altHGridColor[a.chart.paletteIndex]));
			g.legendScrollBarColor = b(c.legendscrollbarcolor, f);
			g.legendScrollBtnColor = b(c.legendscrollbtncolor, f);
			g.title.style = ka({
				fontWeight: "bold"
			}, g.itemStyle)
		},
		pa = h.placeLegendBlockRight = function(a, b, c, d, f) {
			ra(a, b.chart, !0, f, c);
			var g = 0,
				p = a.series,
				t, h = a[s],
				i = h.smartLabel,
				j = a.legend,
				k, z = j.textPadding,
				l = j.title.padding,
				m = j.symbolWidth + 2 * j.symbolPadding,
				A = d * 2,
				H = 0,
				b = e(b.chart.legendpadding, 7) + j.borderWidth / 2 + e(b.chart.canvasborderthickness, 1),
				r = 2 * j.padding,
				n = {
					width: r,
					height: r
				},
				P = !1,
				q = [];
			c -= r;
			f && (p = p && p[0] && p[0].data);
			if (typeof p === Ca || typeof p.length === Ca) return 0;
			else {
				f = p.length;
				for (g = 0; g < f; g += 1) if ((t = p[g]) && t.showInLegend !== !1) t.__i = g, q.push(t);
				q.sort(function(a, b) {
					return a.legendIndex - b.legendIndex || a.__i - b.__i
				});
				f = q.length
			}
			k = c - m - 2 - z;
			k < 0 && (k = 0);
			i.setStyle(j.itemStyle);
			j.reversed && q.reverse();
			for (g = 0; g < f; g += 1) if (t = q[g], P = !0, t._legendX = 0, t._legendY = n.height, k === 0) n.height += t._legendH = m, t.name = w;
			else {
				p = i.getSmartText(t.name, k, A);
				t.name = p.text;
				p.tooltext && (t.originalText = p.tooltext);
				if (p.height < m) t._legendTestY = (m - p.height) / 2;
				n.height += t._legendH = Math.max(p.height, m);
				H = Math.max(p.width, H)
			}
			if (P) {
				j.itemWidth = H + m + 2 + z;
				j.width = j.itemWidth + r;
				if (j.title.text !== w) {
					i.setStyle(j.title.style);
					p = i.getSmartText(j.title.text, c, A);
					j.title.text = p.text;
					p.tooltext && (j.title.originalText = p.tooltext);
					g = p.width + r;
					if (j.width < g) j.initialItemX = (g - j.width) / 2, j.width = g;
					j.initialItemY = p.height + l;
					n.height += j.initialItemY
				}
				j.height = j.totalHeight = n.height;
				if (j.height > d) j.height = d, j.scroll.enabled = !0, j.scroll.flatScrollBars = h.flatScrollBars, j.scroll.scrollBar3DLighting = h.scrollBar3DLighting, j.width += (j.scroll.scrollBarWidth = 10) + (j.scroll.scrollBarPadding = 2);
				b = Math.min(j.width + b, c);
				a.chart.marginRight += b;
				return b
			} else return j.enabled = !1, 0
		},
		m = h.placeLegendBlockBottom = function(a, b, c, d, f) {
			ra(a, b.chart, !1, f, c);
			var g = 0,
				p = a.series,
				t, h = a[s],
				j = h.smartLabel,
				i = a.legend,
				k, z = i.textPadding;
			t = i.title.padding;
			var l, m = i.symbolPadding;
			k = i.legendHeight;
			var A = b.chart;
			l = 0;
			var H = d * 2,
				r = i.rowHeight,
				n = [],
				P = e(A.minimisewrappinginlegend, 0),
				A = e(parseInt(A.legendnumcolumns, 10), 0),
				q = 0,
				u = 0,
				v = 0,
				qa = g = 0,
				ea = 2 * i.padding,
				b = e(b.chart.legendpadding, 7) + i.borderWidth / 2 + 1,
				ga = {
					width: ea,
					height: ea
				},
				da = !1,
				T, wa = [];
			A < 0 && (A = 0);
			c -= ea;
			j.setStyle(i.itemStyle);
			g = j.getOriSize(Wa).height;
			b = Math.min(b, d - g - 8);
			d -= b;
			f && (p = p && p[0] && p[0].data);
			if (typeof p === Ca || typeof p.length === Ca) return 0;
			else {
				f = p.length;
				for (g = 0; g < f; g += 1) if ((T = p[g]) && T.showInLegend !== !1) T.__i = g, wa.push(T);
				wa.sort(function(a, b) {
					return a.legendIndex - b.legendIndex || a.__i - b.__i
				});
				f = wa.length
			}
			j.setStyle(i.itemStyle);
			for (g = 0; g < f; g += 1) da = !0, p = j.getOriSize(wa[g].name), q = Math.max(q, p.width), u += p.width, v += 1;
			g = u / v;
			if (da) {
				g += k + 2 + z;
				q += k + 2 + z;
				A > 0 && v < A && (A = v);
				A > 0 && (qa = c / A) > g ? qa > q && (qa = q) : c > q && (P || g * 1.5 > q) ? (A = Math.floor(c / q), v < A && (A = v), qa = q) : c >= 2 * g ? (A = Math.floor(c / g), v < A && (A = v), qa = Math.floor(c / A), qa > q && (qa = q)) : (A = 1, qa = c);
				i.itemWidth = qa;
				k = qa - k - 2 - z;
				k < 0 && (m = k = z = 0);
				i.symbolPadding = m;
				i.textPadding = z;
				i.width = qa * A + ea;
				if (i.title.text !== w) {
					j.setStyle(i.title.style);
					p = j.getSmartText(i.title.text, c, H);
					i.title.text = p.text;
					p.tooltext && (i.title.originalText = p.tooltext);
					l = p.width + ea;
					if (i.width < l) i.initialItemX = (l - i.width) / 2, i.width = l;
					i.initialItemY = l = p.height + t
				}
				j.setStyle(i.itemStyle);
				z = 0;
				i.reversed && wa.reverse();
				for (g = 0; g < f; g += 1) {
					t = wa[g];
					if (k === 0) n[z] = !0, t.name = w, c = 1;
					else {
						p = j.getSmartText(t.name, k, H);
						t.name = p.text;
						for (p.tooltext && (t.originalText = p.tooltext); n[z] === !0;) z += 1;
						m = p.height / r;
						P = z;
						for (c = 0; c < m; c += 1, P += A) n[P] = !0;
						if (p.height < r) t._legendTestY = (r - p.height) / 2
					}
					m = parseInt(z / A, 10);
					p = z % A;
					t._legendX = p * qa;
					t._legendY = m * r + ea;
					t._legendH = c * r;
					z += 1
				}
				ga.height += Math.ceil(n.length / A) * r + l;
				i.height = i.totalHeight = ga.height;
				i.rowHeight = r;
				i.legendNumColumns = A;
				if (i.height > d) i.height = d, i.scroll.enabled = !0, i.scroll.flatScrollBars = h.flatScrollBars, i.scroll.scrollBar3DLighting = h.scrollBar3DLighting, i.width += (i.scroll.scrollBarWidth = 10) + (i.scroll.scrollBarPadding = 2);
				b += i.height;
				a.chart.marginBottom += b;
				return b
			} else return i.enabled = !1, 0
		},
		T = function(a, b) {
			return a.value - b.value
		},
		ba = h.adjustVerticalAxisTitle = function(a, b, e) {
			if (b && b.text) {
				var c = b.text,
					d = a[s].smartLabel,
					f = 2 * Math.min(a.chart.marginTop, a.chart.marginBottom) + e,
					g = e + a.chart.marginTop + a.chart.marginBottom;
				b.style && d.setStyle(b.style);
				c = d.getOriSize(c);
				if (b.centerYAxisName === void 0) b.centerYAxisName = !0;
				if (b.rotation == "0") {
					if (c.height > f) b.y = (g / 2 - (e / 2 + a.chart.marginTop)) / 2, b.centerYAxisName = !1
				} else if (c.width > f) b.y = g / 2 - (e / 2 + a.chart.marginTop), b.centerYAxisName = !1
			}
		},
		la = h.adjustVerticalCanvasMargin = function(a, b, c, d) {
			var f = b.chart,
				g = b = 0,
				p = 0,
				t = e(f.canvastopmargin, 0),
				f = e(f.canvasbottommargin, 0),
				i = t / (t + f),
				h = a.chart.marginTop,
				j = a.chart.marginBottom;
			f > j && (b += f - j);
			t > h && (b += t - h);
			b > c ? t > h && f > j ? (g = c * i, p = c * (1 - i)) : t > h ? g = c : p = c : b > 0 && (f > j && (p = f - j), t > h && (g = t - h));
			g && (a.chart.marginTop += g);
			p && (a.chart.marginBottom += p, d && d.title && (d.title.margin += p));
			return g + p
		},
		Qa = h.adjustHorizontalCanvasMargin = function(a, b, c, d, f) {
			var g = b.chart,
				b = e(g.canvasleftmargin, 0),
				g = e(g.canvasrightmargin, 0),
				p = b / (b + g),
				t = 0,
				h = a.chart.marginLeft,
				i = a.chart.marginRight,
				j = 0,
				k = 0;
			b > h && (t += b - h);
			g > i && (t += g - i);
			t > c ? b > h && g > i ? (j = c * p, k = c * (1 - p)) : g > i ? k = c : j = c : t > 0 && (b > h && (j = b - h), g > i && (k = g - i));
			j && (a.chart.marginLeft += j, d && d.title && (d.title.margin += j));
			k && (a.chart.marginRight += k, f && f.title && (f.title.margin += k));
			return k + j
		};
	J("base", {
		useScaleRecursively: !0,
		tooltipConstraint: "chart",
		rendererId: "root",
		draw: function(a, b) {
			var e = this,
				c = e.renderer,
				d = e.chartInstance.jsVars,
				f = "reinit",
				g = new Date;
			e.drawingLatency = e.drawingLatency || 0;
			if (!c) d._lastpaper && (d._lastpaper = d._lastpaper.dispose()), f = "init", c = e.renderer = new J("renderer." + e.rendererId);
			return c[f](e, a, function() {
				d._lastpaper = c;
				e.drawingLatency = new Date - g;
				b && b.apply(this, arguments)
			})
		},
		init: function(a, b, c) {
			var d = this.chartInstance || c,
				f = d.jsVars,
				c = f._reflowData || (f._reflowData = {}),
				g = f._reflowClean,
				p, t;
			this.dataObj = b = ka({}, b);
			t = b.chart = b.chart || b.graph || b.map || {};
			delete b.graph;
			delete b.map;
			if (c && !this.stateless) p = c.hcJSON, delete c.hcJSON, ka(this, c, !0), this.preReflowAdjustments && this.preReflowAdjustments.call(this), c.hcJSON = p;
			this.containerElement = a;
			this.config = {};
			this.smartLabel = new eb(d.id, document.getElementsByTagName("body")[0] || a, e(t.useellipseswhenoverflow, t.useellipsewhenoverflow, 1));
			this.linkClickFN = Sa(b, d);
			this.numberFormatter = new fb(b.chart, this);
			if (!this.standaloneInit) return new h.createDialog(a, f.msgStore.ChartNotSupported);
			a = this.chart(a.offsetWidth || parseFloat(a.style.width), a.offsetHeight || parseFloat(a.style.height), d);
			c && !this.stateless && (c.hcJSON && ka(a, c.hcJSON, !0), this.postReflowAdjustments && this.postReflowAdjustments.call(this), g && this.cleanedData && (this.cleanedData(this, g), this.cleanedData(c, g)));
			return a
		},
		chart: function(a, g) {
			var y;
			var p = this.name,
				t = this.dataObj,
				o = t.chart,
				h, j, k, z, l, m, A = this.defaultSeriesType,
				H, n, P, ea, ga, da, T, L;
			h = wa(t, a, g, this);
			L = h.chart;
			T = h.xAxis;
			H = h[s];
			this.postHCJSONCreation && this.postHCJSONCreation.call(this, h);
			h.labels.smartLabel = m = H.smartLabel = this.smartLabel;
			H.width = a;
			H.height = g;
			n = h.plotOptions;
			H.isDual = this.isDual;
			H.numberFormatter = this.numberFormatter;
			H.axisGridManager = new Fa(A, o);
			L.is3D = j = H.is3d = /3d$/.test(A);
			L.isBar = z = H.isBar = this.isBar;
			k = /^pie/.test(A);
			da = o.useroundedges == 1;
			ga = j ? qa.chart3D : qa.chart2D;
			L.events.click = h.plotOptions.series.point.events.click = this.linkClickFN;
			L.defaultSeriesType = A;
			var x = o.palette > 0 && o.palette < 6 ? o.palette : e(this.paletteIndex, 1);
			x -= 1;
			L.paletteIndex = x;
			L.usePerPointLabelColor = o.colorlabelsfromplot == ta;
			L.useRoundEdges = da && !j && !this.distributedColumns && this.defaultSeriesType !== "pie";
			if (b(o.clickurl) !== void 0) L.link = o.clickurl, L.style.cursor = "pointer", h.plotOptions.series.point.events.click = function() {
				L.events.click.call({
					link: o.clickurl
				})
			};
			P = b(o.basefont, "Verdana");
			var ba = f(o.basefontsize, 10),
				I = b(o.basefontcolor, r[ga.baseFontColor][x]),
				ra = b(o.outcnvbasefont, P);
			l = f(o.outcnvbasefontsize, ba);
			var Ba = l + Ka,
				Ia = b(o.outcnvbasefontcolor, I).replace(/^#?([a-f0-9]+)/ig, "#$1"),
				la, D, O = ba;
			ba += Ka;
			I = I.replace(/^#?([a-f0-9]+)/ig, "#$1");
			H.trendStyle = H.outCanvasStyle = {
				fontFamily: ra,
				color: Ia,
				fontSize: Ba
			};
			la = C(H.trendStyle);
			H.inCanvasStyle = {
				fontFamily: P,
				fontSize: ba,
				color: I
			};
			D = C(H.inCanvasStyle);
			H.divlineStyle = {
				fontFamily: P,
				fontSize: ba,
				color: I,
				lineHeight: D
			};
			T.labels.style = {
				fontFamily: ra,
				fontSize: Ba,
				lineHeight: la,
				color: Ia
			};
			T.steppedLabels.style = {
				fontFamily: ra,
				fontSize: Ba,
				lineHeight: la,
				color: Ia,
				visibility: "hidden"
			};
			h.yAxis[0].labels.style = {
				fontFamily: ra,
				fontSize: Ba,
				lineHeight: la,
				color: Ia
			};
			h.yAxis[1].labels.style = {
				fontFamily: ra,
				fontSize: Ba,
				lineHeight: la,
				color: Ia
			};
			h.legend.itemStyle = {
				fontFamily: ra,
				fontSize: Ba,
				lineHeight: la,
				color: Ia
			};
			h.legend.itemHiddenStyle = {
				fontFamily: ra,
				fontSize: Ba,
				lineHeight: la
			};
			h.plotOptions.series.dataLabels.style = {
				fontFamily: P,
				fontSize: ba,
				lineHeight: D,
				color: I
			};
			h.plotOptions.series.dataLabels.color = h.plotOptions.series.dataLabels.style.color;
			h.tooltip.style = {
				fontFamily: P,
				fontSize: ba,
				lineHeight: D,
				color: I
			};
			h.title.style = {
				fontFamily: ra,
				color: Ia,
				fontSize: l + 3 + Ka,
				fontWeight: e(o.captionfontbold) === 0 ? "normal" : "bold"
			};
			C(h.title.style);
			h.subtitle.style = {
				fontFamily: ra,
				color: Ia,
				fontSize: l + e(this.subTitleFontSizeExtender, 1) + Ka,
				fontWeight: B(this.subTitleFontWeight, "bold")
			};
			C(h.subtitle.style);
			T.title.style = {
				fontFamily: ra,
				color: Ia,
				fontSize: Ba,
				fontWeight: e(o.subcaptionfontbold) === 0 ? "normal" : "bold"
			};
			ba = C(T.title.style);
			h.yAxis[0].title.style = {
				fontFamily: ra,
				color: Ia,
				fontSize: Ba,
				lineHeight: ba,
				fontWeight: "bold"
			};
			h.yAxis[1].title.style = {
				fontFamily: ra,
				color: Ia,
				fontSize: Ba,
				lineHeight: ba,
				fontWeight: "bold"
			};
			L.overlapColumns = e(o[z && "overlapbars" || "overlapcolumns"], j ? 0 : 1);
			h.orphanStyles = {
				defaultStyle: {
					style: ka({}, H.inCanvasStyle)
				},
				connectorlabels: {
					style: ka({}, h.plotOptions.series.dataLabels)
				},
				vyaxisname: {
					style: ka({}, h.yAxis[0].title.style)
				}
			};
			h.plotOptions.series.dataLabels.tlLabelStyle = {
				fontFamily: B(o.tlfont, P),
				color: v(B(o.tlfontcolor, I)),
				fontSize: f(o.tlfontsize, O) + "px"
			};
			C(h.plotOptions.series.dataLabels.tlLabelStyle);
			h.plotOptions.series.dataLabels.trLabelStyle = {
				fontFamily: B(o.trfont, P),
				color: v(B(o.trfontcolor, I)),
				fontSize: f(o.trfontsize, O) + "px"
			};
			C(h.plotOptions.series.dataLabels.trLabelStyle);
			h.plotOptions.series.dataLabels.blLabelStyle = {
				fontFamily: B(o.blfont, P),
				color: v(B(o.blfontcolor, I)),
				fontSize: f(o.blfontsize, O) + "px"
			};
			C(h.plotOptions.series.dataLabels.blLabelStyle);
			h.plotOptions.series.dataLabels.brLabelStyle = {
				fontFamily: B(o.brfont, P),
				color: v(B(o.brfontcolor, I)),
				fontSize: f(o.brfontsize, O) + "px"
			};
			C(h.plotOptions.series.dataLabels.brLabelStyle);
			this.parseStyles(h);
			delete h.xAxis.labels.style.backgroundColor;
			delete h.xAxis.labels.style.borderColor;
			delete h.yAxis[0].labels.style.backgroundColor;
			delete h.yAxis[0].labels.style.borderColor;
			delete h.yAxis[1].labels.style.backgroundColor;
			delete h.yAxis[1].labels.style.borderColor;
			H.showTooltip = e(o.showtooltip, this.showtooltip, 1);
			H.tooltipSepChar = b(o.tooltipsepchar, this.tooltipsepchar, bb);
			H.showValues = e(o.showvalues, this.showValues, 1);
			H.seriesNameInToolTip = e(o.seriesnameintooltip, 1);
			H.showVLines = e(o.showvlines, 1);
			H.showVLinesOnTop = e(o.showvlinesontop, 0);
			H.showVLineLabels = e(o.showvlinelabels, this.showVLineLabels, 1);
			H.showVLineLabelBorder = e(o.showvlinelabelborder, 1);
			H.rotateVLineLabels = e(o.rotatevlinelabels, 0);
			H.vLineColor = b(o.vlinecolor, "333333");
			H.vLineThickness = b(o.vlinethickness, 1);
			H.vLineAlpha = e(o.vlinealpha, 80);
			H.vLineLabelBgColor = b(o.vlinelabelbgcolor, "ffffff");
			H.vLineLabelBgAlpha = e(o.vlinelabelbgalpha, j ? 50 : 100);
			H.trendlineColor = b(o.trendlinecolor, "333333");
			H.trendlineThickness = b(o.trendlinethickness, 1);
			H.trendlineAlpha = e(o.trendlinealpha);
			H.showTrendlinesOnTop = b(o.showtrendlinesontop, 0);
			H.trendlineValuesOnOpp = b(o.trendlinevaluesonopp, o.trendlinevaluesonright, 0);
			H.trendlinesAreDashed = e(o.trendlinesaredashed, 0);
			H.trendlinesDashLen = e(o.trendlinedashlen, 5);
			H.trendlinesDashGap = e(o.trendlinedashgap, 2);
			H.showTrendlines = e(o.showtrendlines, 1);
			H.showTrendlineLabels = e(o.showtrendlinelabels, this.showTrendlineLabels, 1);
			H.flatScrollBars = e(o.flatscrollbars, 0);
			H.scrollBar3DLighting = e(o.scrollbar3dlighting, 1);
			h.plotOptions.series.connectNullData = e(o.connectnulldata, 0);
			L.backgroundColor = {
				FCcolor: {
					color: b(o.bgcolor, r[ga.bgColor][x]),
					alpha: b(o.bgalpha, r[ga.bgAlpha][x]),
					angle: b(o.bgangle, r[ga.bgAngle][x]),
					ratio: b(o.bgratio, r[ga.bgRatio][x])
				}
			};
			L.borderRadius = e(o.borderradius, 0);
			L.rotateValues = e(o.rotatevalues, 0);
			L.placeValuesInside = e(o.placevaluesinside, 0);
			L.valuePosition = b(o.valueposition, "auto");
			L.valuePadding = e(o.valuepadding, 2);
			L.borderColor = d(b(o.bordercolor, j ? "#666666" : r.borderColor[x]), b(o.borderalpha, j ? "100" : r.borderAlpha[x]));
			P = e(o.showborder, j ? 0 : 1);
			L.borderWidth = P ? e(o.borderthickness, 1) : 0;
			L.plotBorderColor = d(b(o.canvasbordercolor, r.canvasBorderColor[x]), b(o.canvasborderalpha, r.canvasBorderAlpha[x]));
			if (o.showcanvasborder !== "0" && (ea = Boolean(b(o.canvasborderthickness, da ? 0 : 1)), o.showaxislines === "1" || o.showxaxisline === "1" || o.showyaxisline === "1")) o.showcanvasborder !== "1" && (ea = 0);
			L.plotBorderWidth = j || !ea ? 0 : e(o.canvasborderthickness, this.canvasborderthickness, L.useRoundEdges ? 1 : 2);
			L.bgSWF = b(o.bgimage, o.bgswf);
			L.bgSWFAlpha = e(o.bgimagealpha, o.bgswfalpha, 100);
			da = b(o.bgimagedisplaymode, "none").toLowerCase();
			P = B(o.bgimagevalign, w).toLowerCase();
			I = B(o.bgimagehalign, w).toLowerCase();
			da == "tile" || da == "fill" || da == "fit" ? (P != q && P != "middle" && P != u && (P = "middle"), I != ca && I != "middle" && I != N && (I = "middle")) : (P != q && P != "middle" && P != u && (P = q), I != ca && I != "middle" && I != N && (I = ca));
			L.bgImageDisplayMode = da;
			L.bgImageVAlign = P;
			L.bgImageHAlign = I;
			L.bgImageScale = e(o.bgimagescale, 100);
			L.logoURL = B(o.logourl);
			L.logoPosition = b(o.logoposition, "tl").toLowerCase();
			L.logoAlpha = e(o.logoalpha, 100);
			L.logoLink = B(o.logolink);
			L.logoScale = e(o.logoscale, 100);
			L.logoLeftMargin = e(o.logoleftmargin, 0);
			L.logoTopMargin = e(o.logotopmargin, 0);
			da = L.toolbar = {
				button: {}
			};
			P = da.button;
			P.scale = e(o.toolbarbuttonscale, 1.15);
			P.width = e(o.toolbarbuttonwidth, 15);
			P.height = e(o.toolbarbuttonheight, 15);
			P.radius = e(o.toolbarbuttonradius, 2);
			P.spacing = e(o.toolbarbuttonspacing, 5);
			P.fill = d(b(o.toolbarbuttoncolor, "ffffff"));
			P.labelFill = d(b(o.toolbarlabelcolor, "cccccc"));
			P.symbolFill = d(b(o.toolbarsymbolcolor, "ffffff"));
			P.hoverFill = d(b(o.toolbarbuttonhovercolor, "ffffff"));
			P.stroke = d(b(o.toolbarbuttonbordercolor, "bbbbbb"));
			P.symbolStroke = d(b(o.toolbarsymbolbordercolor, "9a9a9a"));
			P.strokeWidth = e(o.toolbarbuttonborderthickness, 1);
			P.symbolStrokeWidth = e(o.toolbarsymbolborderthickness, 1);
			I = P.symbolPadding = e(o.toolbarsymbolpadding, 5);
			P.symbolHPadding = e(o.toolbarsymbolhpadding, I);
			P.symbolVPadding = e(o.toolbarsymbolvpadding, I);
			I = da.position = b(o.toolbarposition, "tr").toLowerCase();
			switch (I) {
			case "tr":
			case "tl":
			case "br":
			case "bl":
				break;
			default:
				I = "tr"
			}
			P = da.hAlign = (w + o.toolbarhalign).toLowerCase() === "left" ? "l" : I.charAt(1);
			y = da.vAlign = (w + o.toolbarvalign).toLowerCase() === "bottom" ? "b" : I.charAt(0), I = y;
			da.hDirection = e(o.toolbarhdirection, P === "r" ? -1 : 1);
			da.vDirection = e(o.toolbarvdirection, I === "b" ? -1 : 1);
			da.vMargin = e(o.toolbarvmargin, 6);
			da.hMargin = e(o.toolbarhmargin, 10);
			da.x = e(o.toolbarx, P === "l" ? 0 : a);
			da.y = e(o.toolbary, I === "t" ? 0 : g);
			Ia = b(o.divlinecolor, r[ga.divLineColor][x]);
			Ba = b(o.divlinealpha, j ? r.divLineAlpha3D[x] : r.divLineAlpha[x]);
			da = e(o.divlinethickness, 1);
			P = Boolean(e(o.divlineisdashed, this.divLineIsDashed, 0));
			I = e(o.divlinedashlen, 4);
			ra = e(o.divlinedashgap, 2);
			h.yAxis[0].gridLineColor = d(Ia, Ba);
			h.yAxis[0].gridLineWidth = da;
			h.yAxis[0].gridLineDashStyle = P ? G(I, ra, da) : void 0;
			h.yAxis[0].alternateGridColor = z ? d(b(o.alternatevgridcolor, r.altVGridColor[x]), e(o.showalternatevgridcolor, 1) === 1 ? b(o.alternatevgridalpha, r.altVGridAlpha[x]) : ma) : d(b(o.alternatehgridcolor, r.altHGridColor[x]), o.showalternatehgridcolor == 0 ? 0 : b(o.alternatehgridalpha, r.altHGridAlpha[x]));
			O = e(o.vdivlinethickness, 1);
			ba = Boolean(e(o.vdivlineisdashed, 0));
			l = e(o.vdivlinedashlen, 4);
			la = e(o.vdivlinedashgap, 2);
			T.gridLineColor = d(b(o.vdivlinecolor, r[ga.divLineColor][x]), b(o.vdivlinealpha, r.divLineAlpha[x]));
			T.gridLineWidth = O;
			T.gridLineDashStyle = ba ? G(l, la, O) : void 0;
			T.alternateGridColor = d(b(o.alternatevgridcolor, r.altVGridColor[x]), o.showalternatehgridcolor === "1" ? b(o.alternatevgridalpha, r.altVGridAlpha[x]) : 0);
			O = b(o.canvasbgcolor, r[ga.canvasBgColor][x]);
			ba = b(o.canvasbgalpha, r.canvasBgAlpha[x]);
			b(o.showcanvasbg, ta) == ma && (ba = "0");
			h.plotOptions.series.shadow = e(o.showshadow, o.showcolumnshadow, this.defaultPlotShadow, r.showShadow[x]);
			if (this.inversed) h.yAxis[0].reversed = !0, h.yAxis[1].reversed = !0;
			if (this.isStacked) this.distributedColumns ? (H.showStackTotal = Boolean(e(o.showsum, 1)), l = e(o.usepercentdistribution, 1), la = e(o.showpercentvalues, 0), D = e(o.showpercentintooltip, l, 0), H.showXAxisPercentValues = e(o.showxaxispercentvalues, 1)) : (H.showStackTotal = Boolean(e(this.showSum, o.showsum, 0)), l = e(this.stack100percent, o.stack100percent, 0), la = e(o.showpercentvalues, l, 0), D = e(o.showpercentintooltip, la)), H.showPercentValues = la, H.showPercentInToolTip = D, l ? (H.isValueAbs = !0, n[A].stacking = "percent", H[0].stacking100Percent = !0) : n[A].stacking = "normal";
			if (this.isDual) {
				if (o.primaryaxisonleft === "0") h.yAxis[0].opposite = !0, h.yAxis[1].opposite = !1;
				h.yAxis[0].showAlways = !0;
				h.yAxis[1].showAlways = !0
			}
			if (L.useRoundEdges) {
				h.plotOptions.series.shadow = e(o.showshadow, o.showcolumnshadow, 1);
				h.plotOptions.series.borderRadius = 1;
				h.tooltip.style.borderRadius = "2px";
				L.plotBorderRadius = 3;
				if (!ea) L.plotBorderWidth = 0;
				L.plotShadow = h.plotOptions.series.shadow ? {
					enabled: !0,
					opacity: ba / 100
				} : 0
			}
			if (e(o.use3dlighting, 1) === 1) h.legend.lighting3d = !0;
			h.plotOptions.series.userMaxColWidth = z ? o.maxbarheight : e(o.maxcolwidth, this.maxColWidth);
			h.plotOptions.series.maxColWidth = Math.abs(e(h.plotOptions.series.userMaxColWidth, 50)) || 1;
			h.title.text = fa(o.caption);
			h.subtitle.text = fa(o.subcaption);
			if (e(o.showtooltip, this.showtooltip) == 0) h.tooltip.enabled = !1;
			A = h.tooltip.style;
			A.backgroundColor = d(b(A.backgroundColor, o.tooltipbgcolor, r.toolTipBgColor[x]), b(o.tooltipbgalpha, 100));
			A.borderColor = d(b(A.borderColor, o.tooltipbordercolor, r.toolTipBorderColor[x]), b(o.tooltipborderalpha, 100));
			h.tooltip.shadow = e(o.showtooltipshadow, o.showshadow, 1) ? {
				enabled: !0,
				opacity: F(e(o.tooltipbgalpha, 100), e(o.tooltipborderalpha, 100)) / 100
			} : !1;
			h.tooltip.constrain = e(o.constraintooltip, 1);
			A.borderWidth = e(o.tooltipborderthickness, 1) + "px";
			if (o.tooltipborderradius) A.borderRadius = e(o.tooltipborderradius, 1) + "px";
			A.padding = e(o.tooltippadding, this.tooltippadding, 3) + "px";
			if (o.tooltipcolor) A.color = v(o.tooltipcolor);
			H.userPlotSpacePercent = h.plotOptions.series.userPlotSpacePercent = o.plotspacepercent;
			A = e(o.plotspacepercent, 20) % 100;
			H.plotSpacePercent = h.plotOptions.series.groupPadding = A / 200;
			j && !k ? (L.series2D3Dshift = p === "mscombi3d" ? !0 : Boolean(e(o.use3dlineshift, 0)), L.canvasBaseColor3D = b(o.canvasbasecolor, r.canvasBaseColor3D[x]), L.canvasBaseDepth = e(o.canvasbasedepth, 10), L.canvasBgDepth = e(o.canvasbgdepth, 3), L.showCanvasBg = Boolean(e(o.showcanvasbg, 1)), L.showCanvasBase = Boolean(e(o.showcanvasbase, 1)), z ? (L.xDepth = 5, L.yDepth = 5, L.showCanvasBg && (H.marginTopExtraSpace += L.canvasBgDepth), H.marginLeftExtraSpace += L.yDepth + (L.showCanvasBase ? L.canvasBaseDepth : 0), H.marginBottomExtraSpace += 5) : (L.xDepth = 10, L.yDepth = 10, L.showCanvasBg && (H.marginRightExtraSpace += L.canvasBgDepth), H.marginBottomExtraSpace += L.yDepth + (L.showCanvasBase ? L.canvasBaseDepth : 0)), O = O.split(X)[0], ba = ba.split(X)[0], L.use3DLighting = Boolean(e(o.use3dlighting, 1)), L.plotBackgroundColor = L.use3DLighting ? {
				FCcolor: {
					color: i(O, 85) + X + c(O, 55),
					alpha: ba + X + ba,
					ratio: Ma,
					angle: Ta(a - (L.marginLeft + L.marginRight), g - (L.marginTop + L.marginBottom), 1)
				}
			} : d(O, ba), L.canvasBgColor = d(i(O, 80), ba), k = b(o.zeroplanecolor, o.divlinecolor, r[ga.divLineColor][x]), z = b(o.zeroplanealpha, o.divlinealpha, r.divLineAlpha[x]), L.zeroPlaneColor = d(k, z), L.zeroPlaneBorderColor = d(b(o.zeroplanebordercolor, k), e(o.zeroplaneshowborder, 1) ? z : 0)) : (L.is3D = !1, L.plotBackgroundColor = {
				FCcolor: {
					color: O,
					alpha: ba,
					angle: b(o.canvasbgangle, r.canvasBgAngle[x]),
					ratio: b(o.canvasbgratio, r.canvasBgRatio[x])
				}
			});
			this.parseExportOptions(h);
			this.preSeriesAddition && this.preSeriesAddition(h, t, a, g);
			this.series && this.series(t, h, p, a, g);
			this.postSeriesAddition(h, t, a, g);
			this.spaceManager(h, t, a, g);
			this.postSpaceManager && this.postSpaceManager(h, t, a, g);
			p = e(o.drawquadrant, 0);
			if (H.isXYPlot && p && (l = T.min, la = T.max, n = h.yAxis[0].min, ea = h.yAxis[0].max, D = e(o.quadrantxval, (l + la) / 2), O = e(o.quadrantyval, (n + ea) / 2), O >= n && O <= ea && D >= l && D <= la)) {
				var A = d(b(o.quadrantlinecolor, L.plotBorderColor), b(o.quadrantlinealpha, Aa)),
					ba = e(o.quadrantlinethickness, L.plotBorderWidth),
					za = e(o.quadrantlineisdashed, 0),
					J = e(o.quadrantlinedashLen, 4),
					K = e(o.quadrantlinedashgap, 2);
				z = B(o.quadrantlabeltl, w);
				p = B(o.quadrantlabeltr, w);
				t = B(o.quadrantlabelbl, w);
				k = B(o.quadrantlabelbr, w);
				ga = e(o.quadrantlabelpadding, 3);
				za = za ? G(J, K, ba) : void 0;
				T.plotLines.push({
					color: A,
					value: D,
					width: ba,
					dashStyle: za,
					zIndex: 3
				});
				h.yAxis[0].plotLines.push({
					color: A,
					value: O,
					width: ba,
					dashStyle: za,
					zIndex: 3
				});
				ba = a - L.marginRight - L.marginLeft;
				za = g - L.marginTop - L.marginBottom;
				A = H.inCanvasStyle;
				parseInt(A.fontSize, 10);
				l = ba / (la - l) * (D - l);
				la = ba - l;
				ea = za / (ea - n) * (O - n);
				n = za - ea;
				l -= ga;
				la -= ga;
				n -= ga;
				ea -= ga;
				O = ga + Ka;
				D = za - ga + Ka;
				za = ga + Ka;
				ga = ba - ga + Ka;
				m.setStyle(A);
				n > 0 && (z !== w && l > 0 && (z = m.getSmartText(z, l, n), h.labels.items.push({
					html: z.text,
					zIndex: 3,
					vAlign: q,
					style: {
						left: za,
						top: O,
						fontSize: A.fontSize,
						lineHeight: A.lineHeight,
						fontFamily: A.fontFamily,
						color: A.color
					}
				})), p !== w && la > 0 && (z = m.getSmartText(p, la, n), h.labels.items.push({
					html: z.text,
					textAlign: N,
					vAlign: q,
					zIndex: 3,
					style: {
						left: ga,
						top: O,
						fontSize: A.fontSize,
						lineHeight: A.lineHeight,
						fontFamily: A.fontFamily,
						color: A.color
					}
				})));
				ea > 0 && (t !== w && l > 0 && (z = m.getSmartText(t, l, ea), h.labels.items.push({
					html: z.text,
					vAlign: u,
					zIndex: 3,
					style: {
						left: za,
						top: D,
						fontSize: A.fontSize,
						lineHeight: A.lineHeight,
						fontFamily: A.fontFamily,
						color: A.color
					}
				})), k !== w && la > 0 && (z = m.getSmartText(k, la, ea), h.labels.items.push({
					html: z.text,
					textAlign: N,
					vAlign: u,
					zIndex: 3,
					style: {
						left: ga,
						top: D,
						fontSize: A.fontSize,
						lineHeight: A.lineHeight,
						fontFamily: A.fontFamily,
						color: A.color
					}
				})))
			}
			if (this.hasVDivLine && (p = e(o.numvdivlines, 0) + 1, p > 1)) {
				H = H.x.catCount - 1;
				m = T.max;
				p = H / p;
				t = !0;
				k = T.min;
				var M;
				T.scroll && !isNaN(T.scroll.viewPortMax) && (m = T.scroll.viewPortMax);
				Ia = b(o.vdivlinecolor, Ia);
				Ba = e(o.vdivlinealpha, Ba);
				O = e(o.vdivlinethickness, da);
				ba = e(o.vdivlineisdashed, P);
				l = e(o.vdivlinedashlen, I);
				la = e(o.vdivlinedashgap, ra);
				(da = e(o.showalternatevgridcolor, 0)) && (M = d(b(o.alternatevgridcolor, r.altVGridColor[x]), b(o.alternatevgridalpha, r.altVGridAlpha[x])));
				for (x = p; x < H; x += p, t = !t) t && da && T.plotBands.push({
					isNumVDIV: !0,
					color: M,
					from: k,
					to: x,
					zIndex: 1
				}), T.plotLines.push({
					isNumVDIV: !0,
					width: O,
					color: d(Ia, Ba),
					dashStyle: ba ? G(l, la, O) : void 0,
					value: x,
					zIndex: 1
				}), k = x;
				t && da && T.plotBands.push({
					isNumVDIV: !0,
					color: M,
					from: k,
					to: m,
					zIndex: 1
				})
			}
			if (j && L.xDepth > L.marginLeft) L.marginLeft = L.xDepth;
			window.console && window.console.log && window.FC_DEV_ENVIRONMENT && console.log(h);
			return h
		},
		parseExportOptions: function(a) {
			var c, d = this.dataObj.chart,
				f = this.chartInstance.jsVars.transparent ? "" : this.chartInstance.options.containerBackgroundColor || "#ffffff",
				g = navigator.userAgent.match(/(iPad|iPhone|iPod)/g);
			a.exporting.enabled = e(d.exportenabled, 0);
			a.exporting.bgcolor = f;
			a.exporting.exporttargetwindow = b(d.exporttargetwindow, g ? "_blank" : "_self");
			a.exporting.exportaction = d.exportaction && d.exportaction.toString().toLowerCase() === "save" && "save" || "download";
			c = O(a.exporting.exportaction);
			a.exporting.exportfilename = b(d.exportfilename, "FusionCharts");
			a.exporting.exporthandler = b(d.html5exporthandler, d.exporthandler, ia);
			a.exporting.exportparameters = b(d.exportparameters, "");
			a.exporting.exportformat = b(d.exportformat, "PNG");
			a.exporting.exportatclient = e(d.exportatclient, 0);
			a.exporting.exportformats = function(a) {
				var b = {
					JPG: c + " as JPEG image",
					PNG: c + " as PNG image",
					PDF: c + " as PDF document"
				},
					e, d, f, g = 0;
				if (a) {
					a = a.split("|");
					for (g = 0; g < a.length; g++) f = (d = a[g].split("=")) && d[0].toUpperCase() || "", d = d && d[1] || "", b[f] && (e || (e = {})) && (e[f] = d || b[f])
				}
				return e || b
			}(d.exportformats);
			a.exporting.buttons.printButton.enabled = d.showprintmenuitem == "1";
			a.exporting.buttons.exportButton.enabled = d.exportenabled == "1" ? d.exportshowmenuitem != "0" : !1
		},
		defaultSeriesType: w,
		paletteIndex: 1,
		creditLabel: Va,
		placeTitle: Ja,
		placeLegendBlockBottom: m,
		placeLegendBlockRight: pa,
		placeHorizontalAxis: ea,
		placeVerticalAxis: Ia,
		placeHorizontalCanvasMarginAdjustment: Qa,
		placeVerticalCanvasMarginAdjustment: la,
		placeHorizontalXYSpaceManager: function(a, c, d, f) {
			var g = a[s],
				p, h, t, j = c.chart,
				i, k, z, l, A, H, m = a.chart,
				P = g.marginLeftExtraSpace,
				n = g.marginTopExtraSpace,
				r = g.marginBottomExtraSpace,
				q = g.marginRightExtraSpace;
			t = d - (P + q + m.marginRight + m.marginLeft);
			var da = f - (r + m.marginBottom + m.marginTop),
				ga = t * 0.3,
				d = da * 0.3;
			p = t - ga;
			f = da - d;
			i = b(j.legendposition, u).toLowerCase();
			a.legend.enabled && i === N && (p -= this.placeLegendBlockRight(a, c, p / 2, da));
			l = e(j.xaxisnamepadding, 5);
			A = e(j.labelpadding, 2);
			H = j.rotatexaxisname !== ma;
			k = b(j.showplotborder, g.is3d ? ma : ta) === ta;
			k = g.plotBorderThickness = k ? g.is3d ? 1 : e(j.plotborderthickness, 1) : 0;
			z = F(e(m.plotBorderWidth, 1), 0);
			h = F(z, k / 2);
			A < h && (A = h);
			if (!g.isDual && m.marginRight < z && j.chartrightmargin === void 0 && (h = z - m.marginRight, t > ga + h)) m.marginRight = z, t -= h, ga = t * 0.3, p = t - ga;
			h = g.x;
			h.verticalAxisNamePadding = l;
			h.verticalAxisValuesPadding = A;
			h.rotateVerticalAxisName = H;
			h.verticalAxisNameWidth = e(j.xaxisnamewidth);
			p -= Ia(a.xAxis, h, a, c, da, p, !1, !1, t);
			p -= Qa(a, c, p, a.xAxis);
			t = p + ga;
			a.legend.enabled && i !== N && (f -= this.placeLegendBlockBottom(a, c, t, f / 2));
			f -= Ja(a, c, t, f / 2);
			h = g[0];
			h.horizontalAxisNamePadding = e(j.yaxisnamepadding, 5);
			h.horizontalLabelPadding = e(j.yaxisvaluespadding, 2);
			h.labelDisplay = "auto";
			h.staggerLines = e(j.staggerlines, 2);
			h.slantLabels = e(j.slantlabels, 0);
			h.horizontalLabelPadding = h.horizontalLabelPadding < z ? z : h.horizontalLabelPadding;
			this.xAxisMinMaxSetter(a, c, t);
			p = a.xAxis;
			A = p.plotLines;
			z = f / (p.max - p.min);
			A && A.length && (l = (A[0].value - p.min) * z, A = (p.max - A[A.length - 1].value) * z, g.isBar && (k > l && (p.min -= (k - l) / (2 * z)), k > A && (p.max += (k - A) / (2 * z))));
			f -= this.placeHorizontalAxis(a.yAxis[0], h, a, c, t, f, ga);
			f -= la(a, c, f, a.yAxis[0]);
			Ba(d + f, a, j, a.xAxis, g.x.lYLblIdx, !0);
			ba(a, a.xAxis.title, f);
			if (a.legend.enabled && i === N) {
				a = a.legend;
				c = d + f;
				if (a.height > c) a.height = c, a.scroll.enabled = !0, c = (a.scroll.scrollBarWidth = 10) + (a.scroll.scrollBarPadding = 2), a.width += c, m.marginRight += c;
				a.y = 20
			}
			m.marginLeft += P;
			m.marginTop += n;
			m.marginBottom += r;
			m.marginRight += q
		},
		placeVerticalXYSpaceManager: function(a, c, d, f) {
			var g = a[s],
				p, h, t = !0,
				j = 0,
				i = c.chart,
				k = !1,
				z, l, A, H = a.chart,
				m = g.marginLeftExtraSpace,
				P = g.marginTopExtraSpace,
				n = g.marginBottomExtraSpace,
				r = g.marginRightExtraSpace;
			p = d - (m + r + H.marginRight + H.marginLeft);
			var q = f - (n + H.marginBottom + H.marginTop),
				ga = p * 0.3,
				f = q * 0.3,
				da = p - ga,
				d = q - f,
				j = g.drawFullAreaBorder = e(i.drawfullareaborder, 1),
				ea = b(i.legendposition, u).toLowerCase();
			z = e(i.yaxisnamepadding, 5);
			l = e(i.yaxisvaluespadding, i.labelypadding, 2);
			h = b(i.showplotborder, g.is3d ? ma : ta) === ta;
			h = g.plotBorderThickness = h ? g.is3d ? 1 : e(i.plotborderthickness, 1) : 0;
			A = F(e(H.plotBorderWidth, 1), 0);
			h = F(A, h / 2);
			this.defaultSeriesType === "area" && !j && (h = A);
			l < A && (l = A);
			if (!g.isDual && H.marginRight < A && i.chartrightmargin === void 0 && (j = A - a.chart.marginRight, p > ga + j)) H.marginRight = A, p -= j, ga = p * 0.3, da = p - ga;
			a.legend.enabled && ea === N && (da -= this.placeLegendBlockRight(a, c, da / 2, q));
			j = i.rotateyaxisname !== ma;
			if (g.isDual) k = !0, p = g[1], p.verticalAxisNamePadding = z, p.verticalAxisValuesPadding = l, p.rotateVerticalAxisName = j, p.verticalAxisNameWidth = e(i.syaxisnamewidth), t = a.yAxis[1].opposite, da -= Ia(a.yAxis[1], p, a, c, q, da / 2, t, k);
			p = g[0];
			p.verticalAxisNamePadding = z;
			p.verticalAxisValuesPadding = l;
			p.rotateVerticalAxisName = j;
			p.verticalAxisNameWidth = e(k ? i.pyaxisnamewidth : i.yaxisnamewidth);
			da -= Ia(a.yAxis[0], p, a, c, q, da, !t, k);
			da -= Qa(a, c, da, a.yAxis[0], a.yAxis[1]);
			t = da + ga;
			a.legend.enabled && ea !== N && (d -= this.placeLegendBlockBottom(a, c, t, d / 2));
			d -= Ja(a, c, t, d / 2);
			p = g.x;
			p.horizontalAxisNamePadding = e(i.xaxisnamepadding, 5);
			p.horizontalLabelPadding = e(i.labelpadding, i.labelxpadding, 2);
			p.labelDisplay = b(i.labeldisplay, "auto").toLowerCase();
			p.rotateLabels = e(i.rotatelabels, i.rotatexaxislabels, 0);
			p.staggerLines = e(i.staggerlines, 2);
			p.slantLabels = e(i.slantlabels, i.slantlabel, 0);
			if (p.horizontalLabelPadding < h) p.horizontalLabelPadding = h;
			this.xAxisMinMaxSetter(a, c, t);
			d -= this.placeHorizontalAxis(a.xAxis, p, a, c, t, d, ga);
			d -= la(a, c, d, a.xAxis);
			k && (Ba(f + d, a, i, a.yAxis[1], g[1].lYLblIdx), ba(a, a.yAxis[1].title, d));
			Ba(f + d, a, i, a.yAxis[0], g[0].lYLblIdx);
			ba(a, a.yAxis[0].title, d);
			if (a.legend.enabled && ea === N && (a = a.legend, c = f + d, a.height > c && a.type !== "gradient")) a.height = c, a.scroll.enabled = !0, c = (a.scroll.scrollBarWidth = 10) + (a.scroll.scrollBarPadding = 2), a.width += c, H.marginRight += c;
			H.marginLeft += m;
			H.marginTop += P;
			H.marginBottom += n;
			H.marginRight += r
		},
		placeVerticalAxisTitle: ba,
		spaceManager: function() {
			return this.placeVerticalXYSpaceManager.apply(this, arguments)
		},
		axisMinMaxSetter: function(b, c, d, f, g, p, h, t) {
			d = c.stacking100Percent ? Za(99, 1, 100, 0, g, p, h, t) : Za(e(c.max, d), e(c.min, f), d, f, g, p, h, t);
			b.min = Number(V(d.Min, 10));
			b.max = Number(V(d.Max, 10));
			b.tickInterval = Number(V(d.divGap, 10));
			c.numdivlines = Math.round((b.max - b.min) / b.tickInterval) - 1;
			if (d.Range / d.divGap <= 2) b.alternateGridColor = a;
			this.highValue = c.max;
			this.lowValue = c.min;
			delete c.max;
			delete c.min
		},
		configurePlotLines: function(c, f, g, p, o, h, t, i, j, k, z) {
			var l;
			l = g.min;
			var A = g.max,
				H = g.tickInterval,
				m = k ? "xAxis" : p.stacking100Percent ? "percentValue" : "yAxis",
				P = l,
				n = 1,
				q = g.gridLineColor,
				da = g.gridLineWidth,
				u = g.gridLineDashStyle,
				ga = l < 0 && A > 0 ? !0 : !1,
				ea = l === 0,
				qa = A === 0,
				v = e(p.showzeroplanevalue, c.showzeroplanevalue) === 0,
				x = !0,
				ba = 1,
				I = e(c.numdivlines, 0) > 0,
				wa = f[s].axisGridManager,
				B = f.chart.paletteIndex,
				z = e(z, j ? 1 : 0);
			delete g._altGrid;
			delete g._lastValue;
			if (k && !p.catOccupied) p.catOccupied = {};
			if (ga && (!k || !p.catOccupied[0])) if (k ? (x = e(c.showvzeroplane, 1), f = e(c.showvzeroplanevalue, h), I = e(c.vzeroplanethickness, 1), c = I > 0 ? d(b(c.vzeroplanecolor, q), b(c.vzeroplanealpha, c.vdivlinealpha, r.divLineAlpha[B])) : a) : (B = e(c.divlinealpha, r.divLineAlpha[B]), f = e(p.showzeroplanevalue, c.showzeroplanevalue, h), this.defaultZeroPlaneHighlighted === !1 ? (x = e(p.showzeroplane, c.showzeroplane, !(this.defaultZeroPlaneHidden && !I)), I = da) : (I = da === 1 ? 2 : da, ba = 5, B *= 2), I = e(p.zeroplanethickness, c.zeroplanethickness, I), c = I > 0 ? d(b(p.zeroplanecolor, c.zeroplanecolor, q), b(p.zeroplanealpha, c.zeroplanealpha, B)) : a), x) x = f ? i[m](0, z) : w, (ba = wa.addAxisGridLine(g, 0, x, I, u, c, ba, k)) && (ba.isZeroPlane = !0);
			if (o === 1 && (!k || !p.catOccupied[l])) x = ea && v ? w : i[m](l, z), (ba = wa.addAxisGridLine(g, l, x, 0.1, void 0, a, 1, k)) && (ba.isMinLabel = !0);
			da <= 0 && (da = 0.1, q = a);
			for (l = Number(V(P + H, 10)); l < A; l = Number(V(l + H, 10)), n += 1) {
				ga && P < 0 && l > 0 && !j && (wa.addAxisAltGrid(g, 0), n += 1);
				if (l !== 0 && (!k || !p.catOccupied[l])) x = h === 1 && n % t === 0 ? i[m](l, z) : w, wa.addAxisGridLine(g, l, x, da, u, q, 2, k);
				P = l;
				j || wa.addAxisAltGrid(g, l)
			}
			j || wa.addAxisAltGrid(g, A);
			if (o === 1 && n % t === 0 && (!k || !p.catOccupied[A])) x = qa && v ? w : i[m](A, z), (ba = wa.addAxisGridLine(g, A, x, 0.1, u, a, 2, k)) && (ba.isMaxLabel = !0);
			if (this.realtimeEnabled) g.labels._enabled = g.labels.enabled, g._gridLineWidth = g.gridLineWidth, g._alternateGridColor = g.alternateGridColor;
			g.labels.enabled = !1;
			g.gridLineWidth = 0;
			g.alternateGridColor = a;
			g.plotLines.sort(T)
		},
		xAxisMinMaxSetter: function(b, c, d) {
			var f = b[s],
				g = f.x,
				p = c.chart,
				h = g.min = e(g.min, 0),
				t = g.max = e(g.max, g.catCount - 1),
				i, j = 0,
				k = 0,
				z = b.chart.defaultSeriesType,
				l = /^(column|column3d|bar|bar3d|floatedcolumn|sparkwinloss|boxandwhisker2d|dragcolumn)$/.test(z),
				A = /^(line|area|spline|areaspline)$/.test(z),
				z = /^(scatter|bubble|candlestick|dragnode)$/.test(z),
				H = b.xAxis,
				m = H.scroll,
				P = i = Math.min(e(p.canvaspadding, 0), d / 2 - 10);
			if (g.adjustMinMax) {
				var t = h = !e(p.setadaptivexmin, 1),
					n = e(this.numVDivLines, p.numvdivlines, 4),
					r = p.adjustvdiv !== ma,
					q = e(p.showxaxisvalues, p.showxaxisvalue, 1),
					da = e(p.showvlimits, q),
					q = e(p.showvdivlinevalue, p.showvdivlinevalues, q);
				this.axisMinMaxSetter(H, g, p.xaxismaxvalue, p.xaxisminvalue, h, t, n, r);
				h = H.min;
				t = H.max;
				g.requiredAutoNumericLabels && (n = e(parseInt(p.xaxisvaluesstep, 10), 1), this.configurePlotLines(p, b, H, g, da, q, n < 1 ? 1 : n, f.numberFormatter, !1, !0));
				H.plotLines.sort(T)
			}
			H.labels.enabled = !1;
			H.gridLineWidth = 0;
			H.alternateGridColor = a;
			if ((l || f.isScroll) && !f.hasNoColumn) k = j = 0.5;
			f.is3d && (P += e(b.chart.xDepth, 0));
			b = (d - (P + i)) / (t - h + (j + k));
			H.min = h - (j + P / b);
			H.max = t + (k + i / b);
			if (m && m.enabled) j = m.vxLength, k = H.max - H.min, m.viewPortMin = H.min, m.viewPortMax = H.max, m.scrollRatio = j / k, m.flatScrollBars = f.flatScrollBars, m.scrollBar3DLighting = f.scrollBar3DLighting, H.max = H.min + j;
			A && H.min === H.max && (H.min -= 0.5, H.max += 0.5);
			z && c.vtrendlines && S(c.vtrendlines, H, f, !1, !0, !0)
		},
		postSeriesAddition: function(a) {
			var c = a[s],
				d = c.isBar,
				f = c.is3d,
				g = a.chart.rotateValues && !d ? 270 : 0,
				p = c[0],
				h = p && p.stacking100Percent,
				t = c.showPercentValues || c.showPercentInToolTip;
			if (this.isStacked && (c.showStackTotal || h || t)) {
				var j = c.plotSpacePercent,
					i = a.chart.defaultSeriesType,
					z, l, A = 1 - j,
					A = 1 - 2 * j,
					j = c.numberFormatter,
					H, m, P, n, r, da, u, ga, ea, qa, v, x = a.series,
					w, T, ba = ka({}, a.plotOptions.series.dataLabels.style),
					I = parseFloat(ba.fontSize),
					wa = !p.stacking100Percent;
				ba.color = a.plotOptions.series.dataLabels.color;
				l = p.stack;
				for (z in l) {
					p = l[z].length;
					H = A / p;
					P = -(A - H) / 2;
					if (h || t) {
						qa = [];
						ga = 0;
						for (n = x.length; ga < n; ga += 1) r = x[ga], !r.yAxis && b(r.type, i) === z && qa.push(r)
					}
					for (m = 0; m < p; m += 1, P += H) {
						u = l[z][m];
						if (h || t) {
							v = [];
							ga = 0;
							for (n = qa.length; ga < n; ga += 1) r = qa[ga], e(r.columnPosition, 0) === m && v.push(r.data)
						}
						if (u && u.length) {
							da = 0;
							for (r = u.length; da < r; da += 1) if (ga = u[da]) if (ea = (ga.n || 0) + (ga.p || 0), c.showStackTotal && (n = da, n += P, ga = ea < 0 ? ga.n : ga.p, a.xAxis.plotLines.push({
								value: n,
								width: 0,
								isVline: wa,
								isTrend: !wa,
								zIndex: 4,
								_isStackSum: 1,
								_catPosition: da,
								_stackIndex: m,
								label: {
									align: k,
									textAlign: !f && g === 270 ? ea < 0 ? N : ca : d ? ea < 0 ? N : ca : k,
									offsetScale: wa ? ga : void 0,
									offsetScaleIndex: 0,
									rotation: g,
									style: ba,
									verticalAlign: q,
									y: d ? 0 : ea < 0 ? g === 270 ? 4 : I : -4,
									x: 0,
									text: j.yAxis(ea)
								}
							})), h || t) {
								ga = 0;
								for (n = v.length; ga < n; ga += 1) if (w = v[ga][da]) if (T = ea && (w.y || 0) / ea * 100, j.percentValue(T), w.y || w.y === 0) {
									if (h && (w.y = T, w.previousY || w.previousY === 0)) w.previousY = w.previousY / ea * 100;
									if (w.showPercentValues) w.displayValue = j.percentValue(T);
									if (w.showPercentInToolTip) w.toolText = w.toolText + parseInt(T * 100, 10) / 100 + "%"
								}
							}
						}
					}
				}
			}
		},
		styleMapForFont: ga,
		styleApplicationDefinition_font: function(a, b, c) {
			var e, d, f = !1,
				g, p, h = this.styleMapForFont;
			switch (b) {
			case "caption":
				e = a.title;
				break;
			case "datalabels":
				e = a.xAxis.labels;
				break;
			case "datavalues":
				e = a.plotOptions.series.dataLabels;
				f = !0;
				break;
			case "tldatavalues":
				e = {
					style: a.plotOptions.series.dataLabels.tlLabelStyle
				};
				break;
			case "trdatavalues":
				e = {
					style: a.plotOptions.series.dataLabels.trLabelStyle
				};
				break;
			case "bldatavalues":
				e = {
					style: a.plotOptions.series.dataLabels.blLabelStyle
				};
				break;
			case "brdatavalues":
				e = {
					style: a.plotOptions.series.dataLabels.brLabelStyle
				};
				break;
			case "subcaption":
				e = a.subtitle;
				break;
			case "tooltip":
				e = a.tooltip;
				break;
			case "trendvalues":
				e = {
					style: a[s].trendStyle
				};
				break;
			case "xaxisname":
				e = a.xAxis.title;
				break;
			case "yaxisname":
			case "pyaxisname":
			case "axistitle":
				e = [];
				b = 0;
				for (g = a.yAxis.length; b < g; b += 1) e.push(a.yAxis[b].title);
				break;
			case "yaxisvalues":
				e = [];
				b = 0;
				for (g = a.yAxis.length; b < g; b += 1) e.push(a.yAxis[b].labels);
				break;
			case "vlinelabels":
				e = {
					style: a[s].divlineStyle
				};
				break;
			case "legend":
				e = {
					style: a.legend.itemStyle
				};
				break;
			default:
				(e = a.orphanStyles[b]) || (a.orphanStyles[b] = e = {
					text: "",
					style: {}
				})
			}
			if (typeof e === "object") if (e instanceof Array) {
				b = 0;
				for (g = e.length; b < g; b += 1) {
					p = e[b];
					for (d in c) if (a = d.toLowerCase(), typeof h[a] === "function") h[a](c[d], p, f);
					C(p.style)
				}
			} else {
				for (d in c) if (a = d.toLowerCase(), typeof h[a] === "function") h[a](c[d], e, f);
				C(e.style)
			}
		},
		parseStyles: function(a) {
			var b, c, e, d = {},
				f, g = this.dataObj;
			if (g.styles && g.styles.definition instanceof Array && g.styles.application instanceof
			Array) {
				for (b = 0; b < g.styles.definition.length; b += 1) c = g.styles.definition[b], c.type && c.name && this["styleApplicationDefinition_" + c.type.toLowerCase()] && (d[c.name.toLowerCase()] = c);
				for (b = 0; b < g.styles.application.length; b += 1) {
					c = g.styles.application[b].styles && g.styles.application[b].styles.split(X) || [];
					for (f = 0; f < c.length; f += 1) if (e = c[f].toLowerCase(), d[e] && g.styles.application[b].toobject) this["styleApplicationDefinition_" + d[e].type.toLowerCase()](a, g.styles.application[b].toobject.toLowerCase(), d[e])
				}
			}
		},
		dispose: function() {
			var a;
			this.disposing = !0;
			this.renderer && this.renderer.dispose();
			this.numberFormatter && this.numberFormatter.dispose();
			this.smartLabel && this.smartLabel.dispose();
			for (a in this) delete this[a];
			delete this.disposing;
			this.disposed = !0
		}
	});
	J("stub", {
		standaloneInit: !0
	}, J.base);
	J("barbase", {
		spaceManager: function() {
			return this.placeHorizontalXYSpaceManager.apply(this, arguments)
		}
	}, J.base);
	J("singleseries", {
		series: function(a, b, c) {
			var e = a.data || a.dataset && a.dataset[0] && a.dataset[0].data;
			if (e && e.length > 0 && e instanceof Array) b.legend.enabled = !1, c = this.point(c, {
				data: [],
				colorByPoint: !0
			}, e, a.chart, b), c instanceof Array ? b.series = b.series.concat(c) : b.series.push(c), this.configureAxis(b, a), a.trendlines && S(a.trendlines, b.yAxis, b[s], !1, this.isBar)
		},
		defaultSeriesType: w,
		configureAxis: function(a, c) {
			var f = a[s],
				g = a.xAxis,
				p = c.chart,
				h = a.chart.is3D,
				t, j, i, k, z, l, A, H, m, P, r, q, ga = 0,
				da, u, ea = this.numberFormatter,
				qa = e(p.syncaxislimits, 0),
				v;
			g.title.text = fa(p.xaxisname);
			v = e(parseInt(p.yaxisvaluesstep, 10), parseInt(p.yaxisvaluestep, 10), 1);
			v = v < 1 ? 1 : v;
			t = a.yAxis[0];
			j = f[0];
			if (f.isDual) if (i = ea.getCleanValue(p.pyaxismaxvalue), k = ea.getCleanValue(p.pyaxisminvalue), t.title.text = fa(p.pyaxisname), qa && !j.stacking100Percent) {
				q = f[1];
				r = e(q.max);
				q = e(q.min);
				if (r !== void 0 && q !== void 0) j.min = n(j.min, q), j.max = F(j.max, r);
				r = ea.getCleanValue(p.syaxismaxvalue);
				q = ea.getCleanValue(p.syaxisminvalue);
				q !== null && (k = k !== null ? n(k, q) : q);
				r !== null && (i = i !== null ? F(i, r) : r)
			} else qa = 0;
			else i = ea.getCleanValue(p.yaxismaxvalue), k = ea.getCleanValue(p.yaxisminvalue), t.title.text = fa(p.yaxisname);
			A = e(this.isStacked ? 0 : this.setAdaptiveYMin, p.setadaptiveymin, this.defSetAdaptiveYMin, 0);
			l = z = !A;
			H = e(f.numdivlines, p.numdivlines, this.numdivlines, 4);
			m = p.adjustdiv !== ma;
			P = e(this.showYAxisValues, p.showyaxisvalues, p.showyaxisvalue, 1);
			r = e(p.showlimits, P);
			q = e(p.showdivlinevalue, p.showdivlinevalues, P);
			if (!h) ga = e(p.showaxislines, p.drawAxisLines, 0), t.showLine = e(p.showyaxisline, ga), g.showLine = e(p.showxaxisline, ga), da = d(b(p.axislinecolor, "#000000")), g.lineColor = d(b(p.xaxislinecolor, da)), t.lineColor = d(b(p.yaxislinecolor, da)), u = e(p.axislinethickness, 1), g.lineThickness = e(p.xaxislinethickness, u), t.lineThickness = e(p.yaxislinethickness, u);
			this.axisMinMaxSetter(t, j, i, k, z, l, H, m);
			this.configurePlotLines(p, a, t, j, r, q, v, f.numberFormatter, !1);
			if (t.reversed && t.min >= 0) a.plotOptions.series.threshold = t.max;
			if (f.isDual) {
				t = a.yAxis[1];
				j = f[1];
				r = e(p.showsecondarylimits, r);
				q = e(p.showdivlinesecondaryvalue, P);
				qa ? (g = a.yAxis[0], t.min = g.min, t.max = g.max, t.tickInterval = g.tickInterval, delete j.max, delete j.min) : (i = ea.getCleanValue(p.syaxismaxvalue), k = ea.getCleanValue(p.syaxisminvalue), A = e(p.setadaptivesymin, A), l = z = !A, this.axisMinMaxSetter(t, j, i, k, z, l, H, m));
				if (!h) t.showLine = e(p.showsyaxisline, ga), t.lineColor = d(b(p.syaxislinethickness, da)), t.lineThickness = e(p.syaxislinethickness, u);
				this.configurePlotLines(p, a, t, j, r, q, v, f.numberFormatter, !0);
				t.title.text = fa(p.syaxisname)
			}
		},
		pointValueWatcher: function(a, c, d, f, g, p, h) {
			if (c !== null) {
				var a = a[s],
					t, d = e(d, 0);
				a[d] || (a[d] = {});
				d = a[d];
				if (f) this.distributedColumns && (a.marimekkoTotal += c), f = d.stack, g = e(g, 0), p = e(p, 0), h = b(h, Ca), f[h] || (f[h] = []), h = f[h], h[p] || (h[p] = []), p = h[p], p[g] || (p[g] = {}), g = p[g], c >= 0 ? g.p ? (t = g.p, c = g.p += c) : g.p = c : g.n ? (t = g.n, c = g.n += c) : g.n = c;
				d.max = d.max > c ? d.max : c;
				d.min = d.min < c ? d.min : c;
				return t
			}
		},
		getPointStub: function(a, c, d, f) {
			var f = f[s],
				c = c === null ? c : f.numberFormatter.dataLabels(c),
				g = B(fa(a.tooltext)),
				p = B(fa(a.displayvalue)),
				d = f.showTooltip ? g !== void 0 ? g : c === null ? !1 : d !== w ? d + f.tooltipSepChar + c : c : w,
				f = e(a.showvalue, f.showValues) ? p !== void 0 ? p : c : w,
				a = b(a.link);
			return {
				displayValue: f,
				toolText: d,
				link: a
			}
		}
	}, J.base);
	J("multiseries", {
		series: function(a, b, c) {
			var d, f, g = b[s],
				p;
			b.legend.enabled = Boolean(e(a.chart.showlegend, 1));
			if (a.dataset && a.dataset.length > 0) {
				this.categoryAdder(a, b);
				d = 0;
				for (f = a.dataset.length; d < f; d += 1) {
					p = {
						visible: !! e(a.dataset[d].visible, 1),
						data: []
					};
					if (!this.isStacked) p.numColumns = f;
					p = this.point(c, p, a.dataset[d], a.chart, b, g.oriCatTmp.length, d);
					p instanceof Array ? b.series = b.series.concat(p) : b.series.push(p)
				}
				this.configureAxis(b, a);
				a.trendlines && !this.isLog && S(a.trendlines, b.yAxis, g, !1, this.isBar, void 0, this.inversed)
			}
		},
		categoryAdder: function(a, b) {
			var c, d = 0,
				f = b[s],
				g = f.axisGridManager,
				p = a.chart,
				h = b.xAxis,
				t, f = f.x,
				j;
			if (a.categories && a.categories[0] && a.categories[0].category) {
				if (a.categories[0].font) b.xAxis.labels.style.fontFamily = a.categories[0].font;
				if ((c = e(a.categories[0].fontsize)) !== void 0) c < 1 && (c = 1), b.xAxis.labels.style.fontSize = c + Ka, C(b.xAxis.labels.style);
				if (a.categories[0].fontcolor) b.xAxis.labels.style.color = a.categories[0].fontcolor.split(X)[0].replace(/^\#?/, "#");
				var i = b[s].oriCatTmp,
					k = a.categories[0].category;
				for (c = 0; c < k.length; c += 1) k[c].vline ? g.addVline(h, k[c], d, b) : (j = e(k[c].showlabel, p.showlabels, 1), t = fa(x(a.categories[0].category[c].label, a.categories[0].category[c].name)), g.addXaxisCat(h, d, d, j ? t : w), i[d] = x(fa(a.categories[0].category[c].tooltext), t), d += 1)
			}
			f.catCount = d
		},
		getPointStub: function(a, c, d, f, g, p, h) {
			var t, f = f[s],
				j, i, c = c === null ? c : this.numberFormatter.dataLabels(c, h),
				k, z = B(fa(a.tooltext)),
				h = f.tooltipSepChar;
			f.showTooltip ? z !== void 0 ? g = z : c === null ? g = !1 : (f.seriesNameInToolTip && (k = x(g && g.seriesname)), g = k ? k + h : w, g += d ? d + h : w, f.showPercentInToolTip ? i = !0 : g += c) : g = !1;
			e(a.showvalue, p) ? B(a.displayvalue) !== void 0 ? t = fa(a.displayvalue) : f.showPercentValues ? j = !0 : t = c : t = w;
			a = b(a.link);
			return {
				displayValue: t,
				toolText: g,
				link: a,
				showPercentValues: j,
				showPercentInToolTip: i
			}
		}
	}, J.singleseries);
	var za = function(a, b) {
			return a - b
		};
	J("xybase", {
		hideRLine: function() {
			var a = this.chart.series[this.index + 1];
			a && a.hide && a.hide()
		},
		showRLine: function() {
			var a = this.chart.series[this.index + 1];
			a && a.show && a.show()
		},
		getRegressionLineSeries: function(a, b, c) {
			var e, d, f, g;
			g = a.sumXY;
			var p = a.sumX,
				h = a.sumY;
			d = a.xValues;
			f = a.sumXsqure;
			e = a.yValues;
			a = a.sumYsqure;
			b ? (d.sort(za), e = d[0], d = d[d.length - 1], g = (c * g - p * h) / (c * f - Math.pow(p, 2)), f = !isNaN(g) ? g * (e - p / c) + h / c : h / c, c = !isNaN(g) ? g * (d - p / c) + h / c : h / c, c = [{
				x: e,
				y: f
			}, {
				x: d,
				y: c
			}]) : (e.sort(za), f = e[0], e = e[e.length - 1], g = (c * g - p * h) / (c * a - Math.pow(h, 2)), d = !isNaN(g) ? g * (f - h / c) + p / c : p / c, c = !isNaN(g) ? g * (e - h / c) + p / c : p / c, c = [{
				x: d,
				y: f
			}, {
				x: c,
				y: e
			}]);
			return c
		},
		pointValueWatcher: function(a, b, c, e) {
			var d = a[s];
			if (b !== null) a = d[0], a.max = a.max > b ? a.max : b, a.min = a.min < b ? a.min : b;
			if (c !== null) a = d.x, a.max = a.max > c ? a.max : c, a.min = a.min < c ? a.min : c;
			e && (c = c || 0, b = b || 0, e.sumX += c, e.sumY += b, e.sumXY += c * b, e.sumXsqure += Math.pow(c, 2), e.xValues.push(c), e.sumYsqure += Math.pow(b, 2), e.yValues.push(b))
		}
	}, J.multiseries);
	J("scrollbase", {
		postSeriesAddition: function() {
			var a = this.hcJSON,
				c = a.xAxis.scroll,
				d = a[s],
				f = d.width,
				g = d.x.catCount,
				p = this.dataObj.chart;
			d.isScroll = !0;
			a.chart.hasScroll = !0;
			if (this.isStacked) h = 1;
			else {
				var h = 0,
					j = 0,
					i, k = a.series,
					z, A = a.chart.defaultSeriesType;
				for (i = k.length; j < i; j++) z = b(k[j].type, A), z === "column" && (h += 1);
				h < 1 && (h = 1)
			}
			g *= h;
			f = e(p.numvisibleplot, Math.floor(f / this.avgScrollPointWidth));
			if (c && f >= 2 && f < g) c.enabled = !0, c.vxLength = f / h, c.startPercent = p.scrolltoend === ta ? 1 : 0, c.padding = e(p.scrollpadding, a.chart.plotBorderWidth / 2), c.height = e(p.scrollheight, 16), c.buttonWidth = e(p.scrollbtnwidth, p.scrollheight, 16), c.buttonPadding = e(p.scrollbtnpadding, 0), c.color = v(b(p.scrollcolor, r.altHGridColor[a.chart.paletteIndex])), d.marginBottomExtraSpace += c.padding + c.height;
			if (t || e(p.enabletouchscroll, 0)) a.chart.zoomType = "x", a.chart.nativeZoom = !1, a.chart.selectionMarkerFill = "rgba(255,255,255,0)", (a.callbacks || (a.callbacks = [])).push(function(a) {
				l(a, "selectionstart selectiondrag", J.scrollbase.performTouchScroll, {})
			})
		},
		performTouchScroll: function(a) {
			var b = this.xAxis[0].scroller,
				c = b.config;
			a.isOutsidePlot !== !0 && H(b.elements.anchor.element, a.type === "selectionstart" ? "dragstart" : "drag", {
				pageX: -(c.trackLength / (c.width / c.scrollRatio) * (a.chartX || 1)),
				pageY: -a.chartY
			})
		}
	}, J.multiseries);
	J("logbase", {
		isLog: !0,
		isValueAbs: !0,
		configureAxis: function(c, f) {
			var g = c[s],
				p = g.axisGridManager,
				h = this.numberFormatter,
				t = c.series,
				j = c.xAxis,
				i = c.yAxis[0],
				k = g[0],
				l = f.chart,
				A = !e(l.showlimits, l.showyaxisvalues, 1),
				H = !e(l.showdivlinevalues, l.showyaxisvalues, 1),
				m = e(l.base, l.logbase, 10),
				P = e(l.yaxismaxvalue),
				n = e(l.yaxisminvalue),
				q = e(l.showminordivlinevalues) === 1,
				ga = b(l.minordivlinecolor, i.gridLineColor, r.divLineColor[c.chart.paletteIndex]),
				da = e(l.minordivlinealpha, l.divlinealpha, r.divLineAlpha[c.chart.paletteIndex]),
				u = [i, void 0, void 0, e(l.divlinethickness, 2), i.gridLineDashStyle, i.gridLineColor, 2],
				ga = [i, void 0, void 0, e(l.minordivlinethickness, 1), i.gridLineDashStyle, d(b(l.minordivlinecolor, ga), e(l.minordivlinealpha, da / 2)), 2],
				da = q || da && ga[3],
				ea;
			m <= 0 && (m = 10);
			P <= 0 && (P = void 0);
			n <= 0 && (n = void 0);
			P = this.getLogAxisLimits(k.max || m, k.min || 1, P, n, m, da ? l.numminordivlines : 0);
			j.title.text = fa(l.xaxisname);
			ka(i, {
				title: {
					text: fa(l.yaxisname)
				},
				labels: {
					enabled: !1
				},
				gridLineWidth: 0,
				alternateGridColor: a,
				reversed: l.invertyaxis === "1",
				max: z(P.Max, m),
				min: z(P.Min, m)
			});
			for (j = t.length; j--;) if (l = t[j]) {
				l.threshold = i.min;
				for (ea = (l = l.data) && l.length || 0; ea--;) n = l[ea], n.y = z(n.y, m)
			}
			delete k.max;
			delete k.min;
			k.isLog = !0;
			if (i.reversed && i.min >= 0) c.plotOptions.series.threshold = i.max;
			f.trendlines && S(f.trendlines, [{
				max: P.Max,
				min: P.Min,
				plotLines: i.plotLines,
				plotBands: i.plotBands
			}], g);
			for (j = i.plotLines.length; j--;) n = i.plotLines[j], n.value && (n.value = z(n.value, m)), n.from && (n.from = z(n.from, m)), n.to && (n.to = z(n.to, m));
			for (j = i.plotBands.length; j--;) n = i.plotBands[j], n.from && (n.from = z(n.from, m)), n.to && (n.to = z(n.to, m));
			for (j = P.divArr.length; j--;) {
				n = P.divArr[j];
				if (n.ismajor) u[1] = z(n.value, m), u[2] = h.yAxis(n.value), p.addAxisGridLine.apply(p, u);
				else if (da || n.isextreme) ga[1] = z(n.value, m), ga[2] = q || n.isextreme ? h.yAxis(n.value) : w, p.addAxisGridLine.apply(p, ga);
				l = i.plotLines[i.plotLines.length - 1];
				if (n.isextreme) {
					if (l.width = 0.1, A) l.label.text = w
				} else if (H && l.label) l.label.text = w
			}
		},
		getLogAxisLimits: function(a, b, c, e, d, f) {
			var g = function(a) {
					return a == null || a == void 0 || a == "" || isNaN(a) ? !1 : !0
				},
				p = 0,
				h = [],
				t, j, i, k, z, l;
			g(c) && Number(c) >= a ? a = Number(c) : (c = d > 1 ? aa(M(a) / M(d)) : D(M(a) / M(d)), a = Z(d, c), j = c);
			j || (j = d > 1 ? aa(M(a) / M(d)) : D(M(a) / M(d)));
			g(e) && Number(e) <= b ? b = Number(e) : (c = d > 1 ? D(M(b) / M(d)) : aa(M(b) / M(d)), b = Z(d, c), t = c);
			t || (t = d > 1 ? D(M(b) / M(d)) : aa(M(b) / M(d)));
			e = Number(String(M(d) / M(10)));
			f = Number(f) || (D(e) == e ? 8 : 4);
			d > 1 ? (i = j, k = t) : d > 0 && d < 1 && (i = t, k = j);
			e = j;
			for (t = i; t >= k; --t) if (i = Z(d, e), b <= i && a >= i && (h[p++] = {
				value: i,
				ismajor: !0
			}), t != k) {
				j = d > 1 ? -1 : 1;
				i = Z(d, e) - Z(d, e + j);
				c = i / (f + 1);
				for (g = 1; g <= f; ++g) i = Z(d, e + j) + c * g, b <= i && a >= i && (h[p++] = {
					value: i,
					ismajor: !1
				});
				d > 1 ? e-- : e++
			}
			for (var A in h) for (var H in h[A]) if (H == "value") {
				if (!z) z = h[A][H] == b && (h[A].isextreme = !0);
				if (!l) l = h[A][H] == a && (h[A].isextreme = !0)
			}
			z || (h[p++] = {
				value: b,
				ismajor: !0,
				isextreme: !0
			});
			l || (h[p] = {
				value: a,
				ismajor: !0,
				isextreme: !0
			});
			return {
				Max: a,
				Min: b,
				divArr: h
			}
		},
		pointValueWatcher: function(a, b, c) {
			a = a[s];
			c = e(c, 0);
			if (b > 0) a[c] || (a[c] = {}), c = a[c], c.max = c.max > b ? c.max : b, c.min = c.min < b ? c.min : b
		}
	}, J.mslinebase);
	m = J.singleseries;
	pa = J.multiseries;
	J("column2dbase", {
		point: function(a, c, d, f, g) {
			var a = d.length,
				p = g[s],
				h = p.axisGridManager,
				t = g.xAxis,
				j = g.chart.paletteIndex,
				p = p.x,
				i = g.colors,
				k = g.colors.length,
				z = /3d$/.test(g.chart.defaultSeriesType),
				l = this.isBar,
				A = b(f.showplotborder, z ? ma : ta) === ta ? z ? 1 : e(f.plotborderthickness, 1) : 0,
				H = g.chart.useRoundEdges,
				m = e(f.plotborderalpha, f.plotfillalpha, 100),
				n = b(f.plotbordercolor, r.plotBorderColor[j]).split(X)[0],
				j = X + (e(f.useplotgradientcolor, 1) ? $(f.plotgradientcolor, r.plotGradientColor[j]) : w),
				P = 0,
				q = Boolean(e(f.use3dlighting, 1)),
				ga = g[s].numberFormatter,
				da, u = e(f.plotborderdashed, 0),
				ea = e(f.plotborderdashlen, 5),
				v = e(f.plotborderdashgap, 4),
				qa, T, ba, I, wa, B, ra, la, Ba;
			for (ba = T = 0; T < a; T += 1) ra = d[T], ra.vline ? h.addVline(t, ra, P, g) : (qa = ga.getCleanValue(ra.value), Ba = e(ra.showlabel, f.showlabels, 1), I = fa(x(ra.label, ra.name)), h.addXaxisCat(t, P, P, Ba ? I : w), P += 1, da = b(ra.color, i[ba % k]) + j.replace(/,+?$/, ""), wa = b(ra.alpha, f.plotfillalpha, Aa), B = b(ra.ratio, f.plotfillratio), la = b(360 - f.plotfillangle, l ? 180 : 90), qa < 0 && (la = l ? 180 - la : 360 - la), Ba = {
				opacity: wa / 100
			}, wa = R(da, wa, B, la, H, n, b(ra.alpha, m) + w, l, z), da = e(ra.dashed, u) ? G(b(ra.dashlen, ea), b(ra.dashgap, v), A) : void 0, c.data.push(ka(this.getPointStub(ra, qa, I, g), {
				y: qa,
				shadow: Ba,
				color: wa[0],
				borderColor: wa[1],
				borderWidth: A,
				use3DLighting: q,
				dashStyle: da,
				tooltipConstraint: this.tooltipConstraint
			})), this.pointValueWatcher(g, qa), ba += 1);
			p.catCount = P;
			return c
		},
		defaultSeriesType: "column"
	}, m);
	J("linebase", {
		defaultSeriesType: "line",
		hasVDivLine: !0,
		defaultPlotShadow: 1,
		point: function(a, c, d, f, g) {
			var p, h, t, j, i, k, z, l, A, H, m, P, n, q, ga, da, u, ea, qa, T, ba, wa, I, ra, B, a = g.chart,
				la = d.length,
				Ba = g.xAxis;
			p = g[s];
			var Ia = p.axisGridManager,
				C = 0,
				D = p.x,
				O = g.chart.paletteIndex,
				F = g[s].numberFormatter;
			ga = v(b(f.linecolor, f.palettecolors, r.plotFillColor[O]));
			da = b(f.linealpha, Aa);
			P = e(f.linethickness, this.lineThickness, 4);
			n = Boolean(e(f.linedashed, 0));
			l = e(f.linedashlen, 5);
			A = e(f.linedashgap, 4);
			c.color = {
				FCcolor: {
					color: ga,
					alpha: da
				}
			};
			c.lineWidth = P;
			c.step = b(this.stepLine, c.step);
			c.drawVerticalJoins = Boolean(e(c.drawVerticalJoins, f.drawverticaljoins, 1));
			c.useForwardSteps = Boolean(e(c.useForwardSteps, f.useforwardsteps, 1));
			q = e(f.drawanchors, f.showanchors);
			for (i = h = 0; h < la; h += 1) j = d[h], j.vline ? Ia.addVline(Ba, j, C, g) : (p = F.getCleanValue(j.value), k = e(j.showlabel, f.showlabels, 1), t = fa(x(j.label, j.name)), Ia.addXaxisCat(Ba, C, C, k ? t : w), C += 1, H = v(b(j.color, ga)), m = b(j.alpha, da), k = e(j.dashed, n) ? G(l, A, P) : void 0, z = {
				opacity: m / 100
			}, ea = e(j.anchorsides, f.anchorsides, 0), B = e(j.anchorstartangle, f.anchorstartangle, 90), ba = e(j.anchorradius, f.anchorradius, this.anchorRadius, 3), T = v(b(j.anchorbordercolor, f.anchorbordercolor, ga)), qa = e(j.anchorborderthickness, f.anchorborderthickness, this.anchorBorderThickness, 1), wa = v(b(j.anchorbgcolor, f.anchorbgcolor, r.anchorBgColor[O])), I = b(j.anchoralpha, f.anchoralpha, Aa), ra = b(j.anchorbgalpha, f.anchorbgalpha, I), u = q === void 0 ? m != 0 : !! q, c.data.push(ka(this.getPointStub(j, p, t, g), {
				y: p,
				color: {
					FCcolor: {
						color: H,
						alpha: m
					}
				},
				shadow: z,
				dashStyle: k,
				valuePosition: b(j.valueposition, a.valuePosition),
				marker: {
					enabled: !! u,
					fillColor: {
						FCcolor: {
							color: wa,
							alpha: ra * I / 100 + w
						}
					},
					lineColor: {
						FCcolor: {
							color: T,
							alpha: I
						}
					},
					lineWidth: qa,
					radius: ba,
					startAngle: B,
					symbol: va(ea)
				},
				tooltipConstraint: this.tooltipConstraint
			})), this.pointValueWatcher(g, p), i += 1);
			D.catCount = C;
			return c
		},
		defaultZeroPlaneHighlighted: !1
	}, m);
	J("area2dbase", {
		defaultSeriesType: "area",
		hasVDivLine: !0,
		point: function(a, c, d, f, g) {
			var a = g.chart,
				p = d.length,
				h = g.xAxis,
				t = g[s],
				i = g.chart.paletteIndex,
				k = t.axisGridManager,
				t = t.x,
				z = g[s].numberFormatter,
				l = 0,
				A, H, m, P, n, q, ga, da, u, ea, qa, T, ba, I, wa, ra, la, Ba, Ia, C, D, O, F;
			n = b(f.plotfillcolor, f.areabgcolor, B(f.palettecolors) ? g.colors[0] : r.plotFillColor[i]).split(X)[0];
			O = X + (e(f.useplotgradientcolor, 1) ? $(f.plotgradientcolor, r.plotGradientColor[i]) : w);
			q = b(f.plotfillalpha, f.areaalpha, this.isStacked ? Aa : "90");
			ga = e(f.plotfillangle, 270);
			da = b(f.plotbordercolor, f.areabordercolor, B(f.palettecolors) ? g.colors[0] : r.plotBorderColor[i]).split(X)[0];
			u = f.showplotborder == ma ? ma : b(f.plotborderalpha, f.plotfillalpha, f.areaalpha, Aa);
			A = e(f.plotborderangle, 270);
			H = Boolean(e(f.plotborderdashed, 0));
			m = e(f.plotborderdashlen, 5);
			ba = e(f.plotborderdashgap, 4);
			la = e(f.plotborderthickness, f.areaborderthickness, 1);
			F = c.fillColor = {
				FCcolor: {
					color: n + O.replace(/,+?$/, ""),
					alpha: q,
					ratio: Ma,
					angle: ga
				}
			};
			c.lineWidth = la;
			c.dashStyle = H ? G(m, ba, la) : void 0;
			c.lineColor = {
				FCcolor: {
					color: da,
					alpha: u,
					ratio: Aa,
					angle: A
				}
			};
			c.step = b(this.stepLine, c.step);
			c.drawVerticalJoins = Boolean(e(c.drawVerticalJoins, f.drawverticaljoins, 1));
			c.useForwardSteps = Boolean(e(c.useForwardSteps, f.useforwardsteps, 1));
			la = Boolean(e(f.drawanchors, f.showanchors, 1));
			for (Ia = H = 0; H < p; H += 1) ba = d[H], ba.vline ? k.addVline(h, ba, l, g) : (A = z.getCleanValue(ba.value), P = e(ba.showlabel, f.showlabels, 1), m = fa(x(ba.label, ba.name)), k.addXaxisCat(h, l, l, P ? m : w), l += 1, P = e(ba.anchorsides, f.anchorsides, 0), T = e(ba.anchorstartangle, f.anchorstartangle, 90), ea = e(ba.anchorradius, f.anchorradius, 3), qa = v(b(ba.anchorbordercolor, f.anchorbordercolor, da)), Ba = e(ba.anchorborderthickness, f.anchorborderthickness, 1), I = v(b(ba.anchorbgcolor, f.anchorbgcolor, r.anchorBgColor[i])), wa = b(ba.anchoralpha, f.anchoralpha, this.anchorAlpha, ma), ra = b(ba.anchorbgalpha, f.anchorbgalpha, wa), C = B(ba.color), D = e(ba.alpha), C = C !== void 0 || D !== void 0 ? {
				FCcolor: {
					color: C ? v(C) + O : n,
					alpha: void 0 === D ? j(D) + w : q,
					ratio: Ma,
					angle: ga
				}
			} : F, D = {
				opacity: Math.max(D, u) / 100,
				inverted: !0
			}, c.data.push(ka(this.getPointStub(ba, A, m, g), {
				y: A,
				shadow: D,
				color: C,
				valuePosition: b(ba.valueposition, a.valuePosition),
				marker: {
					enabled: la,
					fillColor: {
						FCcolor: {
							color: I,
							alpha: ra * wa / 100 + w
						}
					},
					lineColor: {
						FCcolor: {
							color: qa,
							alpha: wa
						}
					},
					lineWidth: Ba,
					radius: ea,
					symbol: va(P),
					startAngle: T
				},
				tooltipConstraint: this.tooltipConstraint,
				previousY: this.pointValueWatcher(g, A)
			})), Ia += 1);
			t.catCount = l;
			return c
		}
	}, m);
	var I = h.getDataParser = {
		column: function(a, c, d) {
			var f = a[s],
				g = c.borderWidth;
			return function(p, h, t) {
				var i = j(b(p.alpha, c.alpha)).toString(),
					k = {
						opacity: i / 100
					},
					z = c.isBar,
					l = c.fillAangle,
					i = R(b(p.color, c.color) + c.plotgradientcolor, i, b(p.ratio, c.ratio), t < 0 ? z ? 180 - l : 360 - l : l, c.isRoundEdges, c.plotBorderColor, Math.min(i, j(c.plotBorderAlpha)).toString(), z, c.is3d),
					z = e(p.dashed, c.dashed) ? G(b(p.dashlen, c.dashLen), b(p.dashgap, c.dashGap), g) : void 0,
					p = d.getPointStub(p, t, f.oriCatTmp[h], a, c, c.showValues, c.yAxis);
				p.y = t;
				p.shadow = k;
				p.color = i[0];
				p.borderColor = i[1];
				p.borderWidth = g;
				p.use3DLighting = c.use3DLighting;
				p.dashStyle = z;
				p.tooltipConstraint = d.tooltipConstraint;
				return p
			}
		},
		line: function(a, c, d) {
			var f = a[s];
			return function(g, p, h) {
				var t = b(g.alpha, c.lineAlpha),
					j = {
						opacity: t / 100
					},
					i = b(g.anchoralpha, c.anchorAlpha),
					p = d.getPointStub(g, h, f.oriCatTmp[p], a, c, c.showValues, c.yAxis);
				p.y = h;
				p.shadow = j;
				p.dashStyle = e(g.dashed, c.lineDashed) ? G(c.lineDashLen, c.lineDashGap, c.lineThickness) : void 0;
				p.color = {
					FCcolor: {
						color: v(b(g.color, c.lineColor)),
						alpha: t
					}
				};
				p.valuePosition = b(g.valueposition, c.valuePosition);
				p.marker = {
					enabled: c.drawAnchors === void 0 ? t != 0 : !! c.drawAnchors,
					fillColor: {
						FCcolor: {
							color: v(b(g.anchorbgcolor, c.anchorBgColor)),
							alpha: (b(g.anchorbgalpha, c.anchorBgAlpha) * i / 100).toString()
						}
					},
					lineColor: {
						FCcolor: {
							color: v(b(g.anchorbordercolor, c.anchorBorderColor)),
							alpha: i
						}
					},
					lineWidth: e(g.anchorborderthickness, c.anchorBorderThickness),
					radius: e(g.anchorradius, c.anchorRadius),
					symbol: va(e(g.anchorsides, c.anchorSides)),
					startAngle: b(g.anchorstartangle, c.anchorAngle)
				};
				return p
			}
		},
		area: function(a, c, d) {
			var f = a[s];
			return function(g, p, h) {
				var t = b(g.alpha, c.fillAlpha),
					j = {
						opacity: Math.max(t, c.lineAlpha) / 100,
						inverted: !0
					},
					i = b(g.anchoralpha, c.anchorAlpha),
					p = d.getPointStub(g, h, f.oriCatTmp[p], a, c, c.showValues, c.yAxis);
				p.y = h;
				p.shadow = j;
				p.color = {
					FCcolor: {
						color: v(b(g.color, c.fillColor)),
						alpha: t
					}
				};
				p.valuePosition = b(g.valueposition, c.valuePosition);
				p.marker = {
					enabled: c.drawAnchors,
					fillColor: {
						FCcolor: {
							color: v(b(g.anchorbgcolor, c.anchorBgColor)),
							alpha: (b(g.anchorbgalpha, c.anchorBgAlpha) * i / 100).toString()
						}
					},
					lineColor: {
						FCcolor: {
							color: v(b(g.anchorbordercolor, c.anchorBorderColor)),
							alpha: i
						}
					},
					lineWidth: e(g.anchorborderthickness, c.anchorBorderThickness),
					radius: e(g.anchorradius, c.anchorRadius),
					symbol: va(e(g.anchorsides, c.anchorSides)),
					startAngle: b(g.anchorstartangle, c.anchorAngle)
				};
				p.events = {
					click: c.getLink
				};
				return p
			}
		}
	};
	J("mscolumn2dbase", {
		point: function(a, c, d, f, g, p, h, t, j) {
			var o;
			var a = !1,
				i = d.data || [],
				k = g[s],
				z = b(c.type, this.defaultSeriesType),
				l = b(c.isStacked, g.plotOptions[z] && g.plotOptions[z].stacking),
				A = b(this.isValueAbs, k.isValueAbs, !1),
				H = e(c.yAxis, 0),
				m = g[s].numberFormatter,
				P = g.chart.paletteIndex,
				n, q;
			q = g._FCconf.isBar;
			if (!l) c.columnPosition = e(j, t, h);
			c.name = B(d.seriesname);
			if (e(d.includeinlegend) === 0 || c.name === void 0) c.showInLegend = !1;
			c.color = b(d.color, g.colors[h % g.colors.length]).split(X)[0].replace(/^#?/g, "#");
			j = /3d$/.test(g.chart.defaultSeriesType);
			q = b(360 - f.plotfillangle, q ? 180 : 90);
			n < 0 && (q = 360 - q);
			o = c._dataParser = I.column(g, {
				seriesname: c.name,
				color: b(d.color, g.colors[h % g.colors.length]),
				alpha: b(d.alpha, f.plotfillalpha, Aa),
				plotgradientcolor: X + (e(f.useplotgradientcolor, 1) ? $(f.plotgradientcolor, r.plotGradientColor[P]) : w),
				ratio: b(d.ratio, f.plotfillratio),
				fillAangle: q,
				isRoundEdges: g.chart.useRoundEdges,
				plotBorderColor: b(f.plotbordercolor, j ? Na : r.plotBorderColor[P]).split(X)[0],
				plotBorderAlpha: f.showplotborder == ma || j && f.showplotborder != ta ? ma : b(f.plotborderalpha, Aa),
				isBar: this.isBar,
				is3d: j,
				dashed: e(d.dashed, f.plotborderdashed, 0),
				dashLen: e(d.dashlen, f.plotborderdashlen, 5),
				dashGap: e(d.dashgap, f.plotborderdashgap, 4),
				borderWidth: b(f.plotborderthickness, ta),
				showValues: e(d.showvalues, k.showValues),
				yAxis: H,
				use3DLighting: e(f.use3dlighting, 1),
				_sourceDataset: d
			}, this), d = o;
			for (f = 0; f < p; f += 1)(h = i[f]) ? (n = m.getCleanValue(h.value, A), n === null ? c.data.push({
				y: null
			}) : (a = !0, h = d(h, f, n), c.data.push(h), h.previousY = this.pointValueWatcher(g, n, H, l, f, t, z))) : c.data.push({
				y: null
			});
			if (!a && !this.realtimeEnabled) c.showInLegend = !1;
			return c
		},
		defaultSeriesType: "column"
	}, pa);
	J("mslinebase", {
		hasVDivLine: !0,
		point: function(a, c, d, f, g, p, h) {
			var o;
			var a = !1,
				t, j;
			t = g.chart;
			var i = d.data || [];
			j = g[s];
			var k = b(c.type, this.defaultSeriesType),
				z = b(c.isStacked, g.plotOptions[k] && g.plotOptions[k].stacking),
				l = b(this.isValueAbs, j.isValueAbs, !1),
				A = e(c.yAxis, 0),
				H = this.numberFormatter,
				h = v(b(d.color, f.linecolor, g.colors[h % g.colors.length])),
				m = b(d.alpha, f.linealpha, Aa),
				P = e(f.showshadow, this.defaultPlotShadow, 1),
				n = e(d.drawanchors, d.showanchors, f.drawanchors, f.showanchors),
				q = e(d.anchorsides, f.anchorsides, 0),
				ga = e(d.anchorstartangle, f.anchorstartangle, 90),
				da = e(d.anchorradius, f.anchorradius, 3),
				u = v(b(d.anchorbordercolor, f.anchorbordercolor, h)),
				ea = e(d.anchorborderthickness, f.anchorborderthickness, 1),
				qa = v(b(d.anchorbgcolor, f.anchorbgcolor, r.anchorBgColor[g.chart.paletteIndex])),
				T = b(d.anchoralpha, f.anchoralpha, Aa),
				ba = b(d.anchorbgalpha, f.anchorbgalpha, T);
			c.name = B(d.seriesname);
			if (e(d.includeinlegend) === 0 || c.name === void 0 || m == 0 && n !== 1) c.showInLegend = !1;
			c.marker = {
				fillColor: {
					FCcolor: {
						color: qa,
						alpha: ba * T / 100 + w
					}
				},
				lineColor: {
					FCcolor: {
						color: u,
						alpha: T + w
					}
				},
				lineWidth: ea,
				radius: da,
				symbol: va(q),
				startAngle: ga
			};
			c.color = {
				FCcolor: {
					color: h,
					alpha: m
				}
			};
			c.shadow = P ? {
				opacity: P ? m / 100 : 0
			} : !1;
			c.step = b(this.stepLine, c.step);
			c.drawVerticalJoins = Boolean(e(c.drawVerticalJoins, f.drawverticaljoins, 1));
			c.useForwardSteps = Boolean(e(c.useForwardSteps, f.useforwardsteps, 1));
			c.lineWidth = e(d.linethickness, f.linethickness, 2);
			o = c._dataParser = I.line(g, {
				seriesname: c.name,
				lineAlpha: m,
				anchorAlpha: T,
				showValues: e(d.showvalues, j.showValues),
				yAxis: A,
				lineDashed: Boolean(e(d.dashed, f.linedashed, 0)),
				lineDashLen: e(d.linedashlen, f.linedashlen, 5),
				lineDashGap: e(d.linedashgap, f.linedashgap, 4),
				lineThickness: c.lineWidth,
				lineColor: h,
				valuePosition: b(d.valueposition, t.valuePosition),
				drawAnchors: n,
				anchorBgColor: qa,
				anchorBgAlpha: ba,
				anchorBorderColor: u,
				anchorBorderThickness: ea,
				anchorRadius: da,
				anchorSides: q,
				anchorAngle: ga,
				_sourceDataset: d
			}, this), t = o;
			for (f = 0; f < p; f += 1)(j = i[f]) ? (d = H.getCleanValue(j.value, l), d === null ? c.data.push({
				y: null
			}) : (a = !0, j = t(j, f, d), c.data.push(j), j.previousY = this.pointValueWatcher(g, d, A, z, f, 0, k))) : c.data.push({
				y: null
			});
			if (!a && !this.realtimeEnabled) c.showInLegend = !1;
			return c
		},
		defaultSeriesType: "line",
		defaultPlotShadow: 1,
		defaultZeroPlaneHighlighted: !1
	}, pa);
	J("msareabase", {
		hasVDivLine: !0,
		point: function(a, c, d, f, g, p, h) {
			var o;
			var a = !1,
				t = g.chart,
				j = d.data || [],
				i = g[s],
				k = b(c.type, this.defaultSeriesType),
				z = b(c.isStacked, g.plotOptions[k] && g.plotOptions[k].stacking),
				l = b(this.isValueAbs, i.isValueAbs, !1),
				A = g.chart.paletteIndex,
				H = e(c.yAxis, 0),
				m = g[s].numberFormatter,
				P = b(d.color, f.plotfillcolor, g.colors[h % g.colors.length]).split(X)[0].replace(/^#?/g, "#").split(X)[0],
				n = b(d.alpha, f.plotfillalpha, f.areaalpha, this.areaAlpha, 70),
				q = e(f.plotfillangle, 270),
				h = b(d.plotbordercolor, f.plotbordercolor, f.areabordercolor, this.isRadar ? g.colors[h % g.colors.length] : "666666").split(X)[0],
				ga = b(d.showplotborder, f.showplotborder) == ma ? ma : b(d.plotborderalpha, f.plotborderalpha, d.alpha, f.plotfillalpha, f.areaalpha, "95"),
				da = e(f.plotborderangle, 270),
				u = e(d.anchorsides, f.anchorsides, 0),
				ea = e(d.anchorstartangle, f.anchorstartangle, 90),
				qa = e(d.anchorradius, f.anchorradius, 3),
				T = v(b(d.anchorbordercolor, f.anchorbordercolor, P)),
				ba = e(d.anchorborderthickness, f.anchorborderthickness, 1),
				x = v(b(d.anchorbgcolor, f.anchorbgcolor, r.anchorBgColor[A])),
				wa = e(d.anchoralpha, f.anchoralpha, this.anchorAlpha, 0),
				ra = e(d.anchorbgalpha, f.anchorbgalpha, wa);
			this.isRadar || (P += X + (e(f.useplotgradientcolor, 1) ? $(f.plotgradientcolor, r.plotGradientColor[A]) : w), P = P.replace(/,+?$/, ""));
			c.step = b(this.stepLine, c.step);
			c.drawVerticalJoins = Boolean(e(c.drawVerticalJoins, f.drawverticaljoins, 1));
			c.useForwardSteps = Boolean(e(c.useForwardSteps, f.useforwardsteps, 1));
			c.name = b(d.seriesname);
			if (e(d.includeinlegend) === 0 || c.name === void 0) c.showInLegend = !1;
			c.fillColor = {
				FCcolor: {
					color: P,
					alpha: n,
					ratio: Ma,
					angle: q
				}
			};
			c.color = P;
			c.shadow = {
				opacity: e(f.showshadow, 1) ? ga / 100 : 0
			};
			c.lineColor = {
				FCcolor: {
					color: h,
					alpha: ga,
					ratio: Aa,
					angle: da
				}
			};
			c.lineWidth = b(d.plotborderthickness, f.plotborderthickness, 1);
			c.dashStyle = Boolean(e(d.dashed, f.plotborderdashed, 0)) ? G(e(d.dashlen, f.plotborderdashlen, 5), e(d.dashgap, f.plotborderdashgap, 4), c.lineWidth) : void 0;
			c.marker = {
				fillColor: {
					FCcolor: {
						color: x,
						alpha: ra * wa / 100 + w
					}
				},
				lineColor: {
					FCcolor: {
						color: T,
						alpha: wa + w
					}
				},
				lineWidth: ba,
				radius: qa,
				symbol: va(u),
				startAngle: ea
			};
			o = c._dataParser = I.area(g, {
				seriesname: c.name,
				lineAlpha: ga,
				anchorAlpha: wa,
				showValues: e(d.showvalues, i.showValues),
				yAxis: H,
				fillColor: P,
				fillAlpha: n,
				valuePosition: b(d.valueposition, t.valuePosition),
				drawAnchors: Boolean(e(f.drawanchors, f.showanchors, 1)),
				anchorBgColor: x,
				anchorBgAlpha: ra,
				anchorBorderColor: T,
				anchorBorderThickness: ba,
				anchorRadius: qa,
				anchorSides: u,
				anchorAngle: ea,
				getLink: this.linkClickFN,
				_sourceDataset: d
			}, this), d = o;
			for (t = 0; t < p; t += 1)(i = j[t]) ? (f = i ? m.getCleanValue(i.value, l) : null, f === null ? c.data.push({
				y: null
			}) : (a = !0, i = d(i, t, f), c.data.push(i), i.previousY = this.pointValueWatcher(g, f, H, z, t, 0, k))) : c.data.push({
				y: null
			});
			if (!a && !this.realtimeEnabled) c.showInLegend = !1;
			return c
		},
		defaultSeriesType: "area",
		defaultPlotShadow: 0
	}, pa);
	J("scatterbase", {
		showValues: 0,
		defaultPlotShadow: 0,
		rendererId: "cartesian",
		defaultSeriesType: "scatter",
		point: function(a, c, f, g, p, h, t) {
			if (f.data) {
				var i, k, z, l, A, H, m, P, n, q, ga, da, u, ea, qa, T, ba, x = !1,
					wa;
				z = e(f.drawline, g.drawlines, 0);
				l = e(f.drawprogressioncurve, 0);
				var a = f.data,
					h = a.length,
					I = e(f.showvalues, p[s].showValues),
					ra = this.numberFormatter,
					la = e(f.showregressionline, g.showregressionline, 0);
				c.zIndex = 1;
				c.name = B(f.seriesname);
				if (e(f.includeinlegend) === 0 || c.name === void 0) c.showInLegend = !1;
				if (z || l) k = v(b(f.color, p.colors[t % p.colors.length])), z = b(f.alpha, Aa), l = e(f.linethickness, g.linethickness, 2), A = Boolean(e(f.linedashed, f.dashed, g.linedashed, 0)), H = e(f.linedashlen, g.linedashlen, 5), m = e(f.linedashgap, g.linedashgap, 4), c.color = d(b(f.linecolor, g.linecolor, k), e(f.linealpha, g.linealpha, z)), c.lineWidth = l, c.dashStyle = A ? G(H, m, l) : void 0;
				z = Boolean(e(f.drawanchors, f.showanchors, g.drawanchors, g.showanchors, 1));
				l = e(f.anchorsides, g.anchorsides, t + 3);
				A = e(f.anchorradius, g.anchorradius, 3);
				t = v(b(f.anchorbordercolor, f.color, g.anchorbordercolor, k, p.colors[t % p.colors.length]));
				k = e(f.anchorborderthickness, g.anchorborderthickness, 1);
				H = v(b(f.anchorbgcolor, g.anchorbgcolor, r.anchorBgColor[p.chart.paletteIndex]));
				m = b(f.anchoralpha, f.alpha, g.anchoralpha, Aa);
				n = b(f.anchorbgalpha, f.alpha, g.anchorbgalpha, m);
				b(f.anchorstartangle, g.anchorstartangle, 90);
				c.marker = {
					fillColor: this.getPointColor(H, Aa),
					lineColor: {
						FCcolor: {
							color: t,
							alpha: m + w
						}
					},
					lineWidth: k,
					radius: A,
					symbol: va(l)
				};
				if (la) {
					c.events = {
						hide: this.hideRLine,
						show: this.showRLine
					};
					var Ba = {
						sumX: 0,
						sumY: 0,
						sumXY: 0,
						sumXsqure: 0,
						sumYsqure: 0,
						xValues: [],
						yValues: []
					},
						C = e(f.showyonx, g.showyonx, 1),
						Ia = v(b(f.regressionlinecolor, g.regressionlinecolor, t)),
						D = e(f.regressionlinethickness, g.regressionlinethickness, k),
						g = j(e(f.regressionlinealpha, g.regressionlinealpha, m)),
						Ia = d(Ia, g)
				}
				for (i = 0; i < h; i += 1)(P = a[i]) ? (g = ra.getCleanValue(P.y), ba = ra.getCleanValue(P.x), g === null ? c.data.push({
					y: null,
					x: ba
				}) : (x = !0, wa = this.getPointStub(P, g, ra.xAxis(ba), p, f, I), q = e(P.anchorsides, l), ga = e(P.anchorradius, A), da = v(b(P.anchorbordercolor, t)), u = e(P.anchorborderthickness, k), ea = v(b(P.anchorbgcolor, H)), qa = b(P.anchoralpha, P.alpha, m), T = b(P.anchorbgalpha, n), c.data.push({
					y: g,
					x: ba,
					displayValue: wa.displayValue,
					toolText: wa.toolText,
					link: wa.link,
					marker: {
						enabled: z,
						fillColor: {
							FCcolor: {
								color: ea,
								alpha: T * qa / 100 + w
							}
						},
						lineColor: {
							FCcolor: {
								color: da,
								alpha: qa
							}
						},
						lineWidth: u,
						radius: ga,
						symbol: va(q),
						startAngle: b(P.anchorstartangle, 90)
					}
				}), this.pointValueWatcher(p, g, ba, la && Ba))) : c.data.push({
					y: null
				});
				la && (f = this.getRegressionLineSeries(Ba, C, h), this.pointValueWatcher(p, f[0].y, f[0].x), this.pointValueWatcher(p, f[1].y, f[1].x), p = {
					type: "line",
					color: Ia,
					showInLegend: !1,
					lineWidth: D,
					enableMouseTracking: !1,
					marker: {
						enabled: !1
					},
					data: f,
					zIndex: 0
				}, c = [c, p])
			}
			if (!x) c.showInLegend = !1;
			return c
		},
		categoryAdder: function(a, c) {
			var f, g = 0,
				p, h = c[s].x,
				t, j = c.xAxis,
				i, z;
			z = a.chart;
			var l = parseInt(z.labelstep, 10),
				A = e(z.showlabels, 1),
				H = b(z.xaxislabelmode, "categories").toLowerCase(),
				m = c[s].numberFormatter;
			c._FCconf.isXYPlot = !0;
			l = l > 1 ? l : 1;
			h.catOccupied = {};
			if (H !== "auto" && a.categories && a.categories[0] && a.categories[0].category) {
				z = a.categories[0];
				if (z.font) c.xAxis.labels.style.fontFamily = z.font;
				if ((p = e(z.fontsize)) !== void 0) p < 1 && (p = 1), c.xAxis.labels.style.fontSize = p + Ka, C(c.xAxis.labels.style);
				if (z.fontcolor) c.xAxis.labels.style.color = z.fontcolor.split(X)[0].replace(/^\#?/, "#");
				f = b(z.verticallinecolor, r.divLineColor[c.chart.paletteIndex]);
				p = e(z.verticallinethickness, 1);
				t = e(z.verticallinealpha, r.divLineAlpha[c.chart.paletteIndex]);
				var P = e(z.verticallinedashed, 0),
					n = e(z.verticallinedashlen, 4),
					q = e(z.verticallinedashgap, 2),
					ga = d(f, t),
					da, ea, qa;
				for (f = 0; f < z.category.length; f += 1) i = z.category[f], t = m.getCleanValue(i.x), t !== null && !i.vline && (h.catOccupied[t] = !0, qa = e(i.showlabel, i.showname, A), da = e(i.showverticalline, i.showline, i.sl, 0), ea = e(i.linedashed, P), i = qa === 0 || g % l !== 0 ? w : fa(x(i.label, i.name)), j.plotLines.push({
					isGrid: !0,
					isCat: !0,
					width: da ? p : 0,
					color: ga,
					dashStyle: G(n, q, p, ea),
					value: t,
					label: {
						text: i,
						style: j.labels.style,
						align: k,
						verticalAlign: u,
						textAlign: k,
						rotation: 0,
						x: 0,
						y: 0
					}
				}), this.pointValueWatcher(c, null, t), g += 1);
				if (H === "mixed") h.requiredAutoNumericLabels = e(this.requiredAutoNumericLabels, 1)
			} else h.requiredAutoNumericLabels = e(this.requiredAutoNumericLabels, 1);
			h.adjustMinMax = !0
		},
		getPointColor: function(a, b) {
			var d, e, a = v(a),
				b = j(b);
			d = c(a, 70);
			e = i(a, 50);
			return {
				FCcolor: {
					gradientUnits: "objectBoundingBox",
					cx: 0.4,
					cy: 0.4,
					r: "100%",
					color: d + X + e,
					alpha: b + X + b,
					ratio: Ma,
					radialGradient: !0
				}
			}
		}
	}, J.xybase);
	J("mscombibase", {
		series: function(a, c, d) {
			var f, g, p, h, t = a.chart,
				i, j = [],
				k = [],
				z = [],
				l, A, H = c[s],
				m = this.isDual,
				P = 0;
			c.legend.enabled = Boolean(e(a.chart.showlegend, 1));
			if (a.dataset && a.dataset.length > 0) {
				this.categoryAdder(a, c);
				h = H.oriCatTmp.length;
				f = 0;
				for (g = a.dataset.length; f < g; f += 1) switch (p = a.dataset[f], l = m && b(p.parentyaxis, "p").toLowerCase() === "s" ? !0 : !1, i = {
					visible: !! e(p.visible, 1),
					legendIndex: f,
					data: []
				}, l ? (i.yAxis = 1, A = x(p.renderas, this.secondarySeriesType)) : A = x(p.renderas, this.defaultSeriesType), A = A.toLowerCase(), A) {
				case "line":
				case "spline":
					i.type = "line";
					j.push(J.mslinebase.point.call(this, d, i, p, t, c, h, f));
					break;
				case "area":
				case "splinearea":
					i.type = "area";
					c.chart.series2D3Dshift = !0;
					z.push(J.msareabase.point.call(this, d, i, p, t, c, h, f));
					break;
				case "column":
				case "column3d":
					k.push(J.mscolumn2dbase.point.call(this, d, i, a.dataset[f], t, c, h, f, void 0, P));
					P += 1;
					break;
				default:
					l ? (i.type = "line", j.push(J.mslinebase.point.call(this, d, i, p, t, c, h, f))) : (k.push(J.mscolumn2dbase.point.call(this, d, i, a.dataset[f], t, c, h, f, void 0, P)), P += 1)
				}
				t.areaovercolumns !== "0" ? (c.chart.areaOverColumns = !0, c.series = c.series.concat(k, z, j)) : (c.chart.areaOverColumns = !1, c.series = c.series.concat(z, k, j));
				if (k.length === 0) H.hasNoColumn = !0;
				else if (!this.isStacked) {
					d = 0;
					for (f = k.length; d < f; d += 1) k[d].numColumns = f
				}
				this.configureAxis(c, a);
				a.trendlines && S(a.trendlines, c.yAxis, c[s], m, this.isBar)
			}
		}
	}, J.mscolumn2dbase)
}]);
FusionCharts(["private", "modules.renderer.js-renderer", function() {
	var g = this,
		h = g.hcLib,
		m = h.Raphael,
		U = h.chartAPI,
		w = window,
		S = /msie/i.test(navigator.userAgent) && !w.opera,
		ia = document,
		b = m.type === "VML",
		B = h.BLANKSTRING,
		e = "crisp",
		r = "rgba(192,192,192," + (S ? 0.002 : 1.0E-6) + ")",
		x = Math.round,
		$ = h.stubFN,
		fa = {
			pageX: 0,
			pageY: 0
		},
		s = parseFloat,
		ka = parseInt,
		G = h.extend2,
		V = h.addEvent,
		K = h.removeEvent,
		Y = h.pluck,
		O = h.pluckNumber,
		l = h.graphics.HEXtoRGB,
		n = h.setImageDisplayMode,
		F = h.falseFN,
		aa = h.FC_CONFIG_STRING,
		D = /\s\bx\b=['"][^'"]+?['"]/ig,
		M = /\s\by\b=['"][^'"]+?['"]/ig,
		Z = h.isArray,
		R = h.each = function(a, b, c, d) {
			var e;
			c || (c = a);
			d || (d = {});
			if (Z(a)) for (e = 0; e < a.length; e += 1) {
				if (b.call(c, a[e], e, a, d) === !1) return e
			} else if (!(a === null || a === void 0)) for (e in a) if (b.call(c, a[e], e, a, d) === !1) return e
		},
		v = h.createElement,
		C = h.createContextMenu,
		f = h.toRaphaelColor = function() {
			var a = {};
			return function(b) {
				var c = (b = b || this) && b.FCcolor || b,
					d = c.color,
					e = c.ratio,
					f = c.angle,
					g = c.alpha,
					h = c.r,
					i = c.cx,
					j = c.cy,
					k = c.fx,
					m = c.fy,
					n = c.gradientUnits,
					r = c.x1,
					q = c.y1,
					u = c.x2,
					v = c.y2,
					w = 1,
					x, I, E, y;
				if (typeof b === "string") return a[y = "~" + b] || (a[y] = b.replace(/^#?([a-f0-9]{3,6})/ig, "#$1"));
				d = d || B;
				if (!d) return x;
				y = [d, g, e, f, h, i, j, n, k, m, r, u, q, v].join("_").replace(/[\(\)\s,\xb0#]/g, "_");
				if (a[y]) return a[y];
				e = e && (e + B).split(",") || [];
				g = (g || g === 0) && (g + B).split(",") || [];
				if (d = d.split(",")) if (x = B, d.length === 1) E = d[0].replace(/^#?([a-f0-9]{3,6})/ig, "$1"), x = g.length ? "rgba(" + l(E).join(",") + "," + s(g[0]) * 0.01 + ")" : E.replace(/^#?([a-f0-9]{3,6})/ig, "#$1");
				else {
					b = 0;
					for (I = d.length; b < I; b++) E = d[b].replace(/^#?([a-f0-9]{3,6})/ig, "$1"), isNaN(e[b]) || (e[b] = s(e[b]), E += ":" + e[b], isNaN(e[b + 1]) || (e[b + 1] = s(e[b + 1]) + e[b])), !isNaN(g[b]) && g[b] !== B && (w = g[b] * 0.01), d[b] = "rgba(" + l(E).join(",") + "," + w + ")", isNaN(e[b]) || (d[b] = d[b] + ":" + e[b]);
					x += d.join("-");
					if (h !== void 0 || k !== void 0 || i !== void 0 || c.radialGradient) x = "xr(" + [k, m, h, i, j, n].join(",") + ")" + x;
					else {
						x = "-" + x;
						if (r !== void 0 || q !== void 0 || u !== void 0 || v !== void 0) x = "(" + [r, q, u, v, n].join(",") + ")" + x;
						f === void 0 && (f = 0);
						x = 360 - s(f) % 360 + x
					}
				}
				return a[y] = x
			}
		}();
	h.gradientify = function() {
		return function() {
			return ""
		}
	}();
	var j = h.hasTouch,
		i = j ? 10 : 3,
		c = h.getSentenceCase,
		d = h.getCrispValues,
		a = h.getValidValue,
		O = h.pluckNumber,
		k = h.getFirstValue,
		q = h.regex.dropHash,
		u = h.HASHSTRING,
		N = function(a) {
			return a !== J && a !== null
		},
		ca = function(a, b) {
			a[1] === a[4] && (a[1] = a[4] = x(a[1]) + b % 2 / 2);
			a[2] === a[5] && (a[2] = a[5] = x(a[2]) + b % 2 / 2);
			return a
		},
		J, Ja = document.documentMode === 8 ? "visible" : "",
		e = "crisp",
		pa = Math,
		va = pa.sin,
		X = pa.cos,
		Ca = pa.atan2,
		x = pa.round,
		ma = pa.min,
		ta = pa.max,
		Aa = pa.abs,
		Ka = pa.ceil,
		bb = pa.floor,
		Va = pa.PI,
		Ra = Va / 2,
		sa = 2 * Va,
		Ma = Va + Ra,
		Na = h.getFirstColor,
		Wa = h.graphics.getDarkColor,
		Ta = h.graphics.getLightColor,
		r = "rgba(192,192,192," + (S ? 0.002 : 1.0E-6) + ")",
		Fa = h.POSITION_TOP,
		$a = h.POSITION_BOTTOM,
		eb = h.POSITION_RIGHT,
		fb = h.POSITION_LEFT;
	m.ca.ishot = function(a) {
		if (this.removed) return !1;
		var b = this.node,
			a = a || "";
		b.ishot = a;
		switch (this.type) {
		case "group":
			for (b = this.bottom; b;) b.attr("ishot", a), b = b.next;
			break;
		case "text":
			if (m.svg) for (b = b.getElementsByTagName("tspan")[0]; b;) b.ishot = a, b = b.nextSibling
		}
		return !1
	};
	m.addSymbol({
		printIcon: function(a, b, c) {
			var d = c * 0.75,
				e = c * 0.5,
				f = c * 0.33,
				g = x(a - c) + 0.5,
				h = x(b - c) + 0.5,
				i = x(a + c) + 0.5,
				c = x(b + c) + 0.5,
				j = x(a - d) + 0.5,
				k = x(b - d) + 0.5,
				d = x(a + d) + 0.5,
				l = x(b + e) + 0.5,
				m = x(a + e) + 0.5,
				n = x(b + f) + 0.5,
				a = x(a - e) + 0.5,
				f = x(b + f + f) + 0.5;
			return ["M", j, h, "L", d, h, d, k, j, k, "Z", "M", g, k, "L", g, l, j, l, j, b, d, b, d, l, i, l, i, k, "Z", "M", j, b, "L", j, c, d, c, d, b, "Z", "M", m, n, "L", a, n, "M", m, f, "L", a, f]
		},
		exportIcon: function(a, b, c) {
			var d = c * 0.66,
				e = d * 0.5,
				f = x(a - c) + 0.5,
				g = x(b - c) + 0.5,
				h = x(a + c) + 0.5,
				c = x(b + c) - 0.5,
				i = x(a - e) + 0.5,
				j = b < c - 3 ? c - 3 : x(b) + 0.5,
				e = x(a + e) - 0.5,
				k = x(a + d) - 0.5,
				d = x(a - d) + 0.5;
			return ["M", f, j, "L", f, c, h, c, h, j, h, c, f, c, "Z", "M", a, c - 1, "L", d, b, i, b, i, g, e, g, e, b, k, b, "Z"]
		}
	});
	h.rendererRoot = U("renderer.root", {
		standaloneInit: !1,
		isRenderer: !0,
		inited: !1,
		callbacks: [],
		init: function(a, b, c) {
			var o;
			var d = this,
				e = d.container = b.chart.renderTo,
				f = b.tooltip,
				h = d.layer,
				i;
			d.options = b;
			d.logic = a;
			d.definition = a.dataObj;
			d.smartLabel = a.smartLabel;
			d.numberFormatter = a.numberFormatter;
			d.fusionCharts = a.chartInstance;
			d.linkClickFN = a.linkClickFN;
			e.innerHTML = B;
			o = d.paper = m(e, e.offsetWidth || a.width, e.offsetHeight || a.height), e = o;
			g.core.options._useSVGDescTag !== !1 && e._desc && (i = a.friendlyName || "Vector image", d.definition && d.definition.chart && d.definition.chart.caption && (i += ' with caption "' + d.definition.chart.caption + '"'), e._desc(i));
			d.chartWidth = e.width;
			d.chartHeight = e.height;
			if (!d.elements) d.elements = {};
			if (!h) h = d.layers = {}, h.background = h.background || e.group("background"), h.dataset = h.dataset || e.group("dataset").insertAfter(h.background), h.tracker = h.tracker || e.group("hot").insertAfter(h.dataset);
			f && f.enabled !== !1 && e.tooltip(f.style, f.shadow, f.constrain);
			d.setMargins();
			d.drawBackground();
			d.drawButtons();
			d.drawGraph();
			b.legend && b.legend.enabled && d.drawLegend();
			d.drawCaption();
			d.drawLogo();
			d.setChartEvents();
			d.drawLabels && d.drawLabels();
			R(b.callbacks, function(a) {
				a.apply(d, this)
			}, [a]);
			R(d.callbacks, function(a) {
				a.apply(d, this)
			}, [a]);
			d.hasRendered = !0;
			c && c(d)
		},
		reinit: function(a, b, c) {
			this.hasRendered || this.init(b, c)
		},
		dispose: function() {
			this.disposing = !0;
			this.paper && (this.paper.remove(), delete this.paper);
			this.exportIframe && (this.exportIframe.parentNode.removeChild(this.exportIframe), delete this.exportIframe);
			delete this.disposing;
			this.disposed = !0
		},
		onContainerClick: function(a) {
			var b = a.target || a.originalTarget || a.srcElement || a.relatedTarget || a.fromElement,
				a = a.data;
			(!b || !b.ishot || !a) && a.linkClickFN.call(a, a)
		},
		setChartEvents: function() {
			var a = this.options.chart.link,
				b = this.container;
			K(b, "click", this.onContainerClick);
			if (a) this.link = a, V(b, "click", this.onContainerClick, this);
			this.paper.canvas.style.cursor = m.svg ? a && "pointer" || "default" : a && "hand" || "default"
		},
		onOverlayMessageClick: function() {
			var a = this.elements;
			m.animation({
				opacity: 0
			}, 1E3);
			a.messageText && a.messageText.hide();
			a.messageVeil && a.messageVeil.hide()
		},
		showMessage: function(a, b) {
			var c = this.paper,
				d = this.options.chart,
				e = this.elements,
				f = e.messageText,
				g = e.messageVeil,
				h = c.width,
				i = c.height;
			if (!g) g = e.messageVeil = c.rect(0, 0, h, i).attr({
				fill: "rgba(0,0,0,0.2)",
				stroke: "none"
			});
			g.show().toFront().attr("cursor", b ? "pointer" : "default")[b ? "click" : "unclick"](this.onOverlayMessageClick, this);
			if (!f) f = e.messageText = c.text(h / 2, i / 2, B).attr({
				fill: "rgba(255,255,255,1)",
				"font-family": "Verdana",
				"font-size": 10,
				"line-height": 14,
				ishot: !0
			});
			a = a || B;
			this.smartLabel.setStyle({
				"line-height": "14px",
				"font-family": "Verdana",
				"font-size": "10px"
			});
			c = this.smartLabel.getSmartText(a, h - (d.spacingRight || 0) - (d.spacingLeft || 0), i - (d.spacingTop || 0) - (d.spacingBotton || 0));
			f.attr({
				text: c.text,
				ishot: !0,
				cursor: b ? "pointer" : "default"
			})[b ? "click" : "unclick"](this.onOverlayMessageClick, this).show().toFront()
		},
		drawButtons: function() {
			var a = this,
				b = a.logic.rendererId === "zoomline",
				c = a.paper,
				d = a.elements,
				e = a.toolbar || (a.toolbar = []),
				f = a.menus || (a.menus = []),
				g = a.layers,
				h = a.options,
				i = h[aa],
				i = i && i.outCanvasStyle || a.logic.outCanvasStyle || {},
				j = h.chart.toolbar || {},
				k = j.hDirection,
				l = b ? 1 : j.vDirection,
				n = j.button || {};
			e.count = 0;
			var s = n.scale,
				r = n.width * n.scale,
				q = n.height * n.scale,
				u = k * (n.spacing * n.scale + r),
				v = n.radius;
			e.y || (e.y = (b ? 0 : j.y) + j.vMargin * l + ma(0, q * l));
			e.x || (e.x = j.x + j.hMargin * k - ta(0, r * k));
			var j = (b = h.exporting) && b.buttons || {},
				h = j.exportButton && j.exportButton.enabled !== !1,
				j = j.printButton && j.printButton.enabled !== !1,
				x, w = g.buttons || (g.buttons = c.group("buttons"));
			e.add = function(a, b, d) {
				var d = typeof d === "string" ? {
					tooltip: d
				} : d || {},
					f = e.count === 0 ? u - k * n.spacing * n.scale : u,
					f = d.x || (e.x += f),
					g = d.tooltip || "";
				e.push(a = c.button(f, d.y || e.y, J, a, {
					width: r,
					height: q,
					r: v,
					id: e.count++,
					verticalPadding: n.symbolHPadding * s,
					horizontalPadding: n.symbolHPadding
				}, w).attr({
					ishot: !0,
					fill: [n.fill, n.labelFill, n.symbolFill, n.hoverFill],
					stroke: [n.stroke, n.symbolStroke],
					"stroke-width": [n.strokeWidth, n.symbolStrokeWidth]
				}).tooltip(g).buttonclick(b));
				return a
			};
			if (h) f.push(x = d.exportMenu = C({
				chart: a,
				labels: {
					style: i,
					hover: {
						color: "rgba(255, 255, 255, 1)"
					}
				},
				attrs: {
					fill: "rgba(255, 255, 255, 1)"
				},
				hover: {
					fill: m.tintshade(i.color, 0.7)
				},
				items: function(b) {
					var c = [],
						d;
					for (d in b) c.push({
						text: b[d],
						onclick: function(b) {
							return function() {
								a.logic.chartInstance.exportChart({
									exportFormat: b
								})
							}
						}(d)
					});
					return c
				}(b.exportformats)
			})), d.exportButton = e.add("exportIcon", function(a, b) {
				return function() {
					x.visible ? x.hide() : x.show({
						x: a,
						y: b + 1
					})
				}
			}(e.x + r, e.y + q), {
				tooltip: "Export chart"
			});
			if (j) d.printButton = e.add("printIcon", function() {
				a.print()
			}, {
				tooltip: "Print chart"
			})
		},
		setMargins: function() {
			var a = this.paper,
				b = this.options.chart || {};
			this.canvasBorderWidth = b.plotBorderWidth || 0;
			this.canvasTop = x(b.marginTop) || 0;
			this.canvasLeft = x(b.marginLeft) || 0;
			this.canvasWidth = x(a.width - (b.marginLeft || 0) - (b.marginRight || 0));
			this.canvasHeight = x(a.height - (b.marginTop || 0) - (b.marginBottom || 0));
			this.canvasRight = this.canvasLeft + this.canvasWidth;
			this.canvasBottom = this.canvasTop + this.canvasHeight
		},
		drawBackground: function() {
			var a = this.paper,
				b = this.layers,
				c = this.elements,
				d = b.background || (b.background = a.group("background")),
				b = c.background || (c.background = a.rect(d)),
				e = this.options.chart || {},
				g = s(e.borderWidth) || 0,
				h = g * 0.5,
				i = e.borderWidth || 0,
				j = this.chartHeight,
				k = this.chartWidth,
				l = c.backgroundImage,
				m = e.bgSWF,
				r = e.bgSWFAlpha / 100,
				q = e.bgImageDisplayMode,
				u = e.bgImageVAlign,
				v = e.bgImageHAlign,
				x = e.bgImageScale,
				w = i + "," + i + "," + (k - i * 2) + "," + (j - i * 2),
				B, I, E, y, C, W, o;
			a.canvas.style.backgroundColor = e.containerBackgroundColor;
			b.attr({
				x: h,
				y: h,
				width: a.width - g,
				height: a.height - g,
				stroke: e.borderColor,
				"stroke-width": g,
				fill: f(e.backgroundColor)
			});
			if (m) B = new Image, C = E = 1, l = [], B.onload = function() {
				I = n(q, u, v, x, i, k, j, B);
				I["clip-rect"] = w;
				if (I.tileInfo) {
					E = I.tileInfo.xCount;
					C = W = I.tileInfo.yCount;
					o = I.y;
					for (delete I.tileInfo; E;) if (W -= 1, y ? (l[void 0] = y.clone().attr({
						x: I.x,
						y: I.y
					}), d.appendChild(l[void 0])) : l[void 0] = y = a.image(m, d).attr(I).css({
						opacity: r
					}), I.y += I.height, W === 0) W = C, E -= 1, I.x += I.width, I.y = o
				} else l[0] = a.image(m, d), l[0].attr(I).css({
					opacity: r
				}).attr({
					visibility: Ja,
					"clip-rect": w
				})
			}, B.src = m, c.backgroundImage = l
		},
		drawGraph: function() {
			var a = this,
				b = a.paper,
				d = a.plots = a.elements.plots,
				e = a.logic,
				f = a.layers,
				g = a.options,
				h = a.elements,
				i = g.chart,
				g = a.datasets = g.series,
				j = k(i.rendererId, i.defaultSeriesType),
				l = f.background,
				l = f.dataset = f.dataset || b.group("dataset").insertAfter(l);
			f.tracker = f.tracker || b.group("hot").insertAfter(l);
			var m, n;
			a.drawCanvas();
			a.drawAxes();
			if (!d) d = a.plots = a.plots || [], h.plots = d;
			f = 0;
			for (h = g.length; f < h; f++) {
				b = g[f] || {};
				l = b.updatePlot = "updatePlot" + c(Y(b.type, b.plotType, j));
				l = a[l];
				m = b.drawPlot = "drawPlot" + c(Y(b.type, b.plotType, j));
				m = a[m] || a.drawPlot;
				if (!(n = d[f])) d.push(n = {
					index: f,
					items: [],
					data: b.data || [],
					name: b.name,
					userID: b.userID,
					setVisible: function(a, b) {
						return function(c) {
							var f = d[a],
								g, h = {
									hcJSON: {
										series: []
									}
								},
								p = h.hcJSON.series[a] || (h.hcJSON.series[a] = {}),
								i = e.chartInstance.jsVars._reflowData;
							g = (c = k(c, !f.visible)) ? "visible" : "hidden";
							R(f.graphics, function(a) {
								a.attr("visibility", g)
							});
							f.visible = c;
							b.visible = c;
							p.visible = c;
							G(i, h, !0)
						}
					}(f, b),
					legendClick: function(b) {
						return function(c, e) {
							a["legendClick" + j] && a["legendClick" + j](d[b], c, e) || a.legendClick && a.legendClick(d[b], c, e)
						}
					}(f),
					realtimeUpdate: function(b, c, e) {
						return function(f, g) {
							c.call(a, d[b], e, {
								numUpdate: f,
								hasAxisChanged: g
							})
						}
					}(f, l || m, b)
				}), b.plot = n, b.legendClick = n.legendClick, b.setVisible = n.setVisible;
				m.call(a, n, b)
			}
			i.hasScroll && (a.drawScroller(), a.finalizeScrollPlots())
		},
		drawPlot: $,
		drawCanvas: $,
		drawAxes: $,
		drawScroller: function() {},
		drawLegend: function() {
			var p;
			var a = this,
				b = a.options,
				d = a.paper,
				e = b.chart || {},
				f = b.legend,
				g = f.scroll,
				b = {
					elements: {}
				},
				h = b.elements,
				i = a.layers.legend,
				j = h.box,
				k = h.caption,
				l = h.elementGroup,
				n = f.layout === "vertical",
				s = e.marginTop,
				r = e.marginBottom,
				q = e.spacingBottom,
				u = e.spacingLeft,
				v = e.spacingRight,
				x = d.width,
				w = d.height,
				s = a.canvasTop,
				I = f.width,
				E = f.height,
				y = f.borderRadius,
				C = f.backgroundColor,
				W = f.borderColor,
				o = f.borderWidth || 0,
				Q = o * 0.5,
				D = o * 0.5 + 2,
				e = O(f.padding, 4),
				F = e * 0.5,
				N, na, oa, J, M, ka, K;
			n ? (n = x - v - I, s = s + (w - r - s - E) * 0.5 + (f.y || 0)) : (n = u + (x - u - v - I) * 0.5 + (f.x || 0), s = w - q - E);
			r = m.crispBound(n, s, I, E, o);
			n = r.x;
			s = r.y;
			I = r.width;
			E = r.height;
			if (!i) i = a.layers.legend = d.group("legend").insertBefore(a.layers.tracker).translate(n, s);
			f.legendAllowDrag && (na = n, oa = s, i.css({
				cursor: "move"
			}).drag(function(a, b) {
				J = ka + a;
				M = K + b;
				J + I + D > x && (J = x - I - D);
				M + E + D > w && (M = w - E - D);
				J < D && (J = D);
				M < D && (M = D);
				i.translate(J - na, M - oa);
				na = J;
				oa = M
			}, function() {
				ka = na;
				K = oa
			}));
			if (!j) j = h.box = d.rect(i);
			j.attr({
				x: 0,
				y: 0,
				width: I,
				height: E,
				r: y,
				stroke: W,
				"stroke-width": o,
				fill: C || "none",
				ishot: f.legendAllowDrag
			}).shadow(f && f.shadow);
			g && g.enabled ? (N = E - e, j = "," + I + "," + N, l = h.elementGroup = d.group("legenditems", i).attr({
				"clip-rect": "0," + F + j
			}), p = h.scroller || (h.scroller = d.scroller(I - 10 + F - o, Q, 10, E - o, !1, {
				scrollPosition: g.scrollPosition || 0,
				scrollRatio: (N + e) / f.totalHeight,
				showButtons: !1,
				displayStyleFlat: g.flatScrollBars
			}, i)), g = p, g.attr("fill", f.legendScrollBgColor).scroll(function(b) {
				l.transform(["T", 0, (N - f.totalHeight) * b]);
				G(a.fusionCharts.jsVars._reflowData, {
					hcJSON: {
						legend: {
							scroll: {
								position: b
							}
						}
					}
				}, !0)
			})) : l = h.elementGroup = i;
			if (f.title && f.title.text !== B) {
				if (!k) k = h.caption = d.text(l);
				k.attr({
					text: f.title.text,
					title: f.title.originalText || "",
					x: I * 0.5,
					y: e,
					fill: f.title.style.color,
					"vertical-align": "top"
				}).css(f.title.style)
			}
			this["draw" + c(f.type || "point") + "LegendItem"](b)
		},
		drawPointLegendItem: function(a) {
			var t;
			var p;
			var b = this.paper,
				c = this.options,
				d = c.series,
				e = c.chart.defaultSeriesType,
				c = c.legend,
				g = c.legendHeight,
				h = c.symbolPadding,
				i = c.textPadding || 2,
				j = O(c.padding, 4),
				k = c.itemHiddenStyle,
				l = c.itemStyle,
				m = l.color,
				k = k && k.color || "#CCCCCC",
				n = c.symbolWidth,
				s = c.itemWidth,
				q = c.interactiveLegend !== !1,
				u = a.elements,
				v = u.elementGroup,
				a = a.item = [],
				u = u.item = [],
				x = [],
				w = {
					line: !0,
					spline: !0,
					scatter: !0,
					bubble: !0,
					dragnode: !0,
					zoomline: !0
				},
				I, E, y, B, C, o, D, ja, N, J, na, oa, M, ka;
			J = 0;
			for (oa = d.length; J < oa; J += 1) if ((y = d[J]) && y.showInLegend !== !1) if (o = y.type || e, y.legendType === "point") {
				y = y.data || [];
				na = 0;
				for (ja = y.length; na < ja; na += 1) if (C = y[na] || {}, C.showInLegend !== !1) C._legendType = o, x.push(C)
			} else switch (y._legendType = o, o) {
			case "pie":
			case "pie3d":
			case "funnel":
			case "pyramid":
				x = y.data;
				break;
			default:
				x.push(y)
			}
			x.sort(function(a, b) {
				return (a.legendIndex || 0) - (b.legendIndex || 0) || a.__i - b.__i
			});
			c.reversed && x.reverse();
			d = c.initialItemX || 0;
			e = c.initialItemY || 0;
			J = 0;
			for (oa = x.length; J < oa; J += 1) if (x[J].showInLegend !== !1) if (na = {
				elements: {},
				hiddenColor: k,
				itemTextColor: m
			}, a.push(na), u.push(na.elements), I = x[J], D = d + I._legendX + j, ja = e + I._legendY - j, N = I._legendH, E = I._legendType || o, y = I.visible !== !1, B = na.itemLineColor = f(I.color || {}), C = q ?
			function(a) {
				return function() {
					a.legendClick()
				}
			}(I) : F, I.plot.legend = na, na.elements.legendItemBackground = b.rect(D, ja, s, N, 0, v).click(C).attr({
				fill: f(I.legendFillColor || r),
				"stroke-width": 1,
				stroke: f(I.legendBorderColor || "none"),
				cursor: l.cursor || "pointer",
				ishot: q
			}), na.elements.legendItemText = b.text(D + g + i - 2, ja + (I._legendTestY || 0), I.name, v).css(l).click(C).attr({
				fill: y ? m : k,
				"vertical-align": "top",
				"text-anchor": "start",
				cursor: l.cursor || "pointer",
				title: I.originalText || "",
				ishot: q
			}), w[E]) {
				E = ja + h + n * 0.5;
				if (I.lineWidth) ka = na.elements.legendItemLine = b.path(["M", D + h, E, "L", D + h + n, E], v).click(C).attr({
					"stroke-width": I.lineWidth,
					stroke: y ? B : k,
					cursor: l.cursor || "pointer",
					ishot: q
				});
				if (I && (M = I.marker) && M.enabled !== !1) na.symbolStroke = f(Y(M.lineColor && (M.lineColor.FCcolor && M.lineColor.FCcolor.color.split(",")[0] || M.lineColor), B)), M.fillColor && M.fillColor.FCcolor ? (E = G({}, M.fillColor), E.FCcolor.alpha = "100") : E = Y(M.fillColor, B), na.symbolColor = f(E), I = n * 0.5, D = D + h + I, E = ja + h + I, ka && (I *= 0.6), ja = M.symbol.split("_"), B = ja[0] === "spoke" ? 1 : 0, p = ja[1] ? na.elements.legendItemSymbol = b.polypath(ja[1], D, E, I, M.startAngle, B, v) : na.elements.legendItemSymbol = b.circle(D, E, I, v), ja = p, ja.click(C).attr({
					cursor: l.cursor || "pointer",
					stroke: y ? na.symbolStroke : k,
					fill: y ? na.symbolColor : k,
					"stroke-width": 1,
					ishot: q
				})
			} else ja = this.getSymbolPath(D + h, ja + h, n, n, E, I), na.symbolColor = f(ja.color), na.symbolStroke = f(ja.strokeColor), t = na.elements.legendItemSymbol = b.path(ja.path, v).click(C).attr({
				"stroke-width": ja.strokeWidth,
				stroke: y ? na.symbolStroke : k,
				fill: y ? na.symbolColor : k,
				cursor: l.cursor || "pointer",
				ishot: q
			}), ja = t;
			c.reversed && x.reverse()
		},
		drawCaption: function() {
			var o;
			var a = this.options.chart,
				b = this.options.title,
				c = this.options.subtitle,
				d = this.paper,
				e = this.elements,
				f = this.layers,
				g = f.caption,
				h = e.caption,
				i = e.subcaption,
				j = b && b.text,
				k = c && c.text,
				l = (this.canvasLeft || 0) + O(this.canvasWidth, d.width) / 2,
				m = b.x,
				n = c && c.x;
			if ((j || k) && !g) g = f.caption = d.group("caption"), f.tracker ? g.insertBefore(f.tracker) : g.insertAfter(f.dataset);
			if (j) {
				if (!h) h = e.caption = d.text(g);
				if (m === void 0) m = l, b.align = "middle";
				h.css(b.style).attr({
					text: b.text,
					fill: b.style.color,
					x: m,
					y: b.y || a.spacingTop || 0,
					"text-anchor": b.align || "middle",
					"vertical-align": "top",
					visibility: "visible",
					title: b.originalText || ""
				})
			} else if (h) o = e.caption = h.remove(), h = o;
			if (k) {
				if (!i) i = e.subcaption = d.text(g);
				if (n === void 0) n = l, c.align = "middle";
				i.css(c.style).attr({
					text: c.text,
					title: c.originalText || "",
					fill: c.style.color,
					x: n,
					y: j ? h.attrs.y + h.getBBox().height + 2 : b.y || a.spacingTop || 0,
					"text-anchor": c.align || "middle",
					"vertical-align": "top",
					visibility: "visible"
				})
			} else if (i) e.subcaption = i.remove();
			if (!j && !k && g) f.caption = g.remove()
		},
		drawLogo: function() {
			var a = this.paper,
				b = this.elements,
				c = this.options,
				d = c.credits,
				e = c.chart || {},
				f = e.borderWidth || 0,
				g = this.chartHeight,
				h = this.chartWidth,
				i = b.logoImage,
				j = this.layers.tracker,
				k = e.logoURL,
				l = e.logoAlpha / 100,
				m = e.logoPosition,
				s = e.logoLink,
				r = e.logoScale,
				q = e.logoLeftMargin,
				u = e.logoTopMargin,
				v = f + "," + f + "," + (h - f * 2) + "," + (g - f * 2),
				c = {
					tr: {
						vAlign: Fa,
						hAlign: eb
					},
					bl: {
						vAlign: $a,
						hAlign: fb
					},
					br: {
						vAlign: $a,
						hAlign: eb
					},
					cc: {
						vAlign: "middle",
						hAlign: "middle"
					}
				},
				x, w;
			this.logic && d.enabled && a.text().attr({
				text: d.text,
				x: 6,
				y: g - 4,
				"vertical-align": $a,
				"text-anchor": "start",
				fill: "rgba(0,0,0,0.5)",
				title: d.title || ""
			}).css({
				fontSize: 9,
				fontFamily: "Verdana",
				cursor: "pointer",
				_cursor: "hand"
			}).click(function() {
				e.events.click.call({
					link: d.href
				})
			});
			if (k) x = new Image, (m = c[m]) || (m = {
				vAlign: Fa,
				hAlign: fb
			}), x.onload = function() {
				w = n("none", m.vAlign, m.hAlign, r, f, h, g, x);
				i = a.image(k);
				j.appendChild(i);
				w["clip-rect"] = v;
				i.attr(w).translate(q, u).css({
					opacity: l
				});
				s && i.css({
					cursor: "pointer",
					_cursor: "hand"
				}).click(function() {
					e.events.click.call({
						link: s
					})
				})
			}, x.src = k, b.logoImage = i
		},
		legendClick: function(a, b, c) {
			var d = a.legend,
				e = d && d.elements,
				f = e && e.legendItemText,
				h = e && e.legendItemSymbol,
				e = e && e.legendItemLine,
				i = d && d.hiddenColor,
				j = d && d.itemLineColor,
				k = d && d.itemTextColor,
				l = d && d.symbolColor,
				m = d && d.symbolStroke,
				d = Y(b, !a.visible);
			a.setVisible(b);
			c !== !0 && (eventArgs = {
				datasetName: a.name,
				datasetIndex: a.index,
				id: a.userID,
				visible: d
			}, g.raiseEvent("legenditemclicked", eventArgs, this.logic.chartInstance));
			d ? (h && h.attr({
				fill: l || j,
				stroke: m
			}), f && f.attr({
				fill: k
			}), e && e.attr({
				stroke: j
			})) : (h && h.attr({
				fill: i,
				stroke: i
			}), f && f.attr({
				fill: i
			}), e && e.attr({
				stroke: i
			}));
			if ((a = this.datasets && this.datasets[a.index] && this.datasets[a.index].relatedSeries) && a instanceof Array && a.length > 0) for (b = a.length; b--;) c = parseFloat(a[b]), (c = this.plots[c]) && c.legend && c.legendClick.call(c, d, !1)
		},
		exportChart: function(a) {
			var b = this.elements,
				c = b.printButton,
				d = b.exportButton,
				e = this.fusionCharts,
				f = e.id,
				i = this.paper,
				j = this.options,
				a = typeof a === "object" &&
			function(a) {
				var b = {},
					c;
				for (c in a) b[c.toLowerCase()] = a[c];
				return b
			}(a) || {}, k = G(G({}, j.exporting), a), l = (k.exportformat || "png").toLowerCase(), a = k.exporthandler, m = (k.exportaction || B).toLowerCase(), b = k.exporttargetwindow || B, n = k.exportfilename, s = k.exportparameters, r;
			if (!j.exporting || !j.exporting.enabled || !a) return !1;
			c && c.attrs.visibility != "hidden" && c.attr({
				visibility: "hidden"
			});
			d && d.attrs.visibility != "hidden" && d.attr({
				visibility: "hidden"
			});
			j = i.toSVG();
			c && c.attr({
				visibility: "visible"
			});
			d && d.attr({
				visibility: "visible"
			});
			j = j.replace(/(\sd\s*=\s*["'])[M\s\d\.]*(["'])/ig, "$1M 0 0 L 0 0$2");
			l === "pdf" && (j = j.replace(/<(\b[^<>s\s]+\b)[^\>]+?opacity\s*=\s*['"][^1][^\>]+?(\/>|>[\s\r\n]*?<\/\1>)/ig, function(a, b) {
				var c = D.exec(a) || B,
					d = M.exec(a) || B;
				return a + "<" + b + c + d + ' opacity="1" stroke-opacity="1" fill="#cccccc" stroke-width="0" r="0" height="0.5" width="0.5" d="M 0 0 L 1 1" />'
			}));
			c = {
				charttype: e.src,
				stream: j,
				stream_type: "svg",
				meta_bgColor: k.bgcolor || "",
				meta_DOMId: e.id,
				meta_width: i.width,
				meta_height: i.height,
				parameters: ["exportfilename=" + n, "exportformat=" + l, "exportaction=" + m, "exportparameters=" + s].join("|")
			};
			if (m === "download") {
				if (/webkit/ig.test(navigator.userAgent) && b === "_self" && (b = d = f + "export_iframe", !this.exportIframe)) this.exportIframe = d = v("IFRAME", {
					name: d,
					width: "1px",
					height: "1px"
				}, ia.body), d.style.cssText = "position:absolute;left:-10px;top:-10px;";
				a = v("form", {
					method: "POST",
					action: a,
					target: b,
					style: "display:none;"
				}, ia.body);
				for (r in c) v("input", {
					type: "hidden",
					name: r,
					value: c[r]
				}, a);
				a.submit();
				ia.body.removeChild(a);
				a = void 0
			} else r = new g.ajax(function(a) {
				var b = {};
				a.replace(RegExp("([^?=&]+)(=([^&]*))?", "g"), function(a, c, d, e) {
					b[c] = e
				});
				h.raiseEvent("exported", b, e, [b])
			}, function(a) {
				a = {
					statusCode: 0,
					statusMessage: "failure",
					error: a,
					DOMId: f,
					width: i.width,
					height: i.height
				};
				h.raiseEvent("exported", a, e, [a])
			}), r.post(a, c);
			return !0
		},
		print: function() {
			var a = this,
				b = a.container,
				c = a.elements,
				d = c.printButton,
				e = c.exportButton,
				f = [],
				g = b.parentNode,
				c = ia.body,
				h = c.childNodes;
			if (!a.isPrinting) a.isPrinting = !0, R(h, function(a, b) {
				if (a.nodeType == 1) f[b] = a.style.display, a.style.display = "none"
			}), d && d.attrs.visibility != "hidden" && d.attr({
				visibility: "hidden"
			}), e && e.attrs.visibility != "hidden" && e.attr({
				visibility: "hidden"
			}), c.appendChild(b), w.print(), setTimeout(function() {
				d && d.attr({
					visibility: "visible"
				});
				e && e.attr({
					visibility: "visible"
				});
				g.appendChild(b);
				R(h, function(a, b) {
					if (a.nodeType == 1) a.style.display = f[b]
				});
				a.isPrinting = !1
			}, 1E3)
		},
		getSymbolPath: function(a, b, c, d, e, f) {
			var g = ["M"],
				h, i, j, k;
			h = (f.color && Na(typeof f.color === "string" ? f.color : f.color.FCcolor.color) || B).replace(q, "");
			i = Ta(h, 40);
			k = Wa(h, 60).replace(q, u);
			h = {
				FCcolor: {
					color: h + "," + h + "," + i + "," + h + "," + h,
					ratio: "0,30,30,30,10",
					angle: 0,
					alpha: "100,100,100,100,100"
				}
			};
			switch (e) {
			case "column":
			case "dragcolumn":
			case "column3d":
				i = c * 0.25;
				j = i * 0.5;
				e = d * 0.7;
				f = d * 0.4;
				g = g.concat([a, b + d, "l", 0, -e, i, 0, 0, e, "z", "m", i + j, 0, "l", 0, -d, i, 0, 0, d, "z", "m", i + j, 0, "l", 0, -f, i, 0, 0, f, "z"]);
				h.FCcolor.angle = 270;
				break;
			case "bar":
			case "bar3d":
				i = c * 0.3;
				j = c * 0.6;
				e = d / 4;
				f = e / 2;
				g = g.concat([a, b, "L", a + j, b, a + j, b + e, a, b + e, "Z", "M", a, b + e + f, "L", a + c, b + e + f, a + c, b + e + f + e, a, b + 2 * e + f, "Z", "M", a, b + 2 * (e + f), "L", a + i, b + 2 * (e + f), a + i, b + d, a, b + d, "Z"]);
				break;
			case "area":
			case "area3d":
			case "areaspline":
			case "dragarea":
				e = d * 0.6;
				f = d * 0.2;
				d *= 0.8;
				g = g.concat([a, b + d, "L", a, b + e, a + c * 0.3, b + f, a + c * 0.6, b + e, a + c, b + f, a + c, b + d, "Z"]);
				h.FCcolor.angle = 270;
				break;
			case "pie":
			case "pie3d":
				i = c / 2;
				j = c * 0.7;
				e = d / 2;
				g = g.concat([a + i, b + e, "L", a + j, b, "A", i, e, 0, 0, 0, a, b + e, "L", a + i, b + e, "M", a + i, b + e, "L", a, b + e, "A", i, e, 0, 0, 0, a + j, b + d, "L", a + i, b + e, "M", a + i, b + e, "L", a + j, b + d, "A", i + 1, e + 1, 0, 0, 0, a + j, b, "Z"]);
				break;
			case "boxandwhisker2d":
				g = g.concat([a, b, "L", a + c, b, a + c, b + d, a, b + d, "Z"]);
				h = f.color;
				k = "#000000";
				break;
			default:
				g = g.concat([a, b, "L", a + c, b, a + c, b + d, a, b + d, "Z"]), h.FCcolor.angle = 270, h.FCcolor.ratio = "0,70,30"
			}
			return {
				path: g,
				color: h,
				strokeWidth: 0.5,
				strokeColor: k
			}
		}
	});
	var Sa = function(a, b, c, d) {
			this.axisData = a || {};
			b = this.renderer = b;
			a = b.paper;
			this.globalOptions = b.options;
			var e = b.layers,
				b = c ? "y-axis" : "x-axis",
				f = this.layerAboveDataset = e.layerAboveDataset,
				g = this.layerBelowDataset = e.layerBelowDataset,
				e = f.bands || (f.bands = []),
				h = e.length,
				i = g.bands || (g.bands = []),
				j = i.length,
				k = f.lines || (f.lines = []),
				l = k.length,
				m = g.lines || (g.lines = []),
				n = m.length,
				f = f.labels || (f.labels = []),
				s = f.length,
				g = g.labels || (g.labels = []),
				r = g.length;
			this.isVertical = c;
			this.topBandGroup = this.topBandGroup || a.group(b + "-bands", this.layerAboveDataset);
			this.belowBandGroup = this.belowBandGroup || a.group(b + "-bands", this.layerBelowDataset);
			e.push(this.topBandGroup);
			h && e[h].insertAfter(e[h - 1]);
			i.push(this.belowBandGroup);
			j && i[j].insertAfter(i[j - 1]);
			this.topLineGroup = this.topLineGroup || a.group(b + "-lines", this.layerAboveDataset);
			this.belowLineGroup = this.belowLineGroup || a.group(b + "-lines", this.layerBelowDataset);
			this.topLabelGroup = this.topLabelGroup || a.group(b + "-labels", this.layerAboveDataset);
			this.belowLabelGroup = this.belowLabelGroup || a.group(b + "-labels", this.layerBelowDataset);
			k.push(this.topLineGroup);
			l && k[l].insertAfter(k[l - 1]);
			m.push(this.belowLineGroup);
			n && m[n].insertAfter(m[n - 1]);
			f.push(this.topLabelGroup);
			s && f[s].insertAfter(f[s - 1]);
			g.push(this.belowLabelGroup);
			r && g[r].insertAfter(g[r - 1]);
			this.isReverse = d;
			this.configure()
		};
	Sa.prototype = {
		configure: function() {
			var a = this.axisData,
				b = this.renderer,
				c = this.isVertical,
				d = this.isReverse,
				e = b.options,
				f = e.chart,
				g = f.marginBottom,
				f = f.marginRight,
				h = b.canvasTop,
				i = b.canvasLeft,
				j = this.min = a.min,
				j = this.span = (this.max = a.max) - j,
				i = this.startX = O(a.startX, i),
				h = this.startY = O(a.startY, h),
				k = this.endX = O(a.endX, b.canvasRight),
				a = this.endY = O(a.endY, b.canvasBottom);
			this.startPixel = d ? c ? a : k : c ? h : i;
			c = this.pixelRatio = c ? (a - h) / j : (k - i) / j;
			this.pixelValueRatio = d ? -c : c;
			d = this.relatedObj = {};
			d.marginObj = {
				top: h,
				right: f,
				bottom: g,
				left: i
			};
			d.canvasObj = {
				x: i,
				y: h,
				w: k - i,
				h: a - h,
				toX: k,
				toY: a
			};
			this.primaryOffset = this.secondaryOffset = 0;
			this.cache = {
				lowestVal: 0,
				highestVal: 0,
				indexArr: [],
				hashTable: {}
			};
			this.elements = this.elements || {};
			if (this.belowBandGroup) b.elements.axes = b.elements.axes || {}, b.elements.axes.belowBandGroup = this.belowBandGroup, e && e.chart && e.chart.hasScroll && this.belowBandGroup.attr({
				"clip-rect": b.elements["clip-canvas"]
			});
			this.poi = {}
		},
		draw: function() {
			var a = this.axisData,
				b = a && a.plotLines || [],
				c = a && a.plotBands || [],
				d = a && a.showLine,
				e = a && a.tickLength,
				f = a && a.tickWidth;
			a && a.title && this.drawAxisName();
			b && b.length > 0 && this.drawPlotLine();
			c && c.length > 0 && this.drawPlotBands();
			!isNaN(e) && e != 0 && !isNaN(f) && f != 0 && this.drawTicks();
			d && this.drawLine()
		},
		scroll: function() {},
		setOffset: function(a, b) {
			var c = h ? this.startY : this.startX,
				d = h ? this.endY : this.endX,
				e = this.cache.hashTable,
				f = this.primaryOffset = a || this.primaryOffset,
				g = this.secondaryOffset = b || this.secondaryOffset,
				h = this.isVertical,
				i, j, k, l = [this.topLabelGroup, this.belowLabelGroup, this.topLineGroup, this.belowLineGroup, this.topBandGroup, this.belowBandGroup],
				m, n, s;
			m = 0;
			for (n = l.length; m < n; m += 1) if (k = l[m]) i = h ? g : f, j = h ? f : g, k.attr({
				transform: "t" + i + "," + j
			});
			if (!h) for (s in e) if (m = parseFloat(s) + f, m < c || m > d) {
				g = e[s];
				m = 0;
				for (n = g.elements.length; m < n; m += 1) h = g.elements[m], h.attr("visibility") === "visible" && h.attr({
					visibility: "hidden"
				})
			} else if (m > c && m < d) {
				g = e[s];
				m = 0;
				for (n = g.elements.length; m < n; m += 1) h = g.elements[m], h.attr("visibility") === "hidden" && h.attr({
					visibility: "visible"
				})
			}
		},
		update: function() {},
		drawTicks: function() {
			var a = this.axisData,
				b = this.renderer.paper,
				c = this.min,
				d = this.max,
				e = this.isVertical,
				f = this.layerBelowDataset,
				f = this.tickGroup = this.tickGroup || b.group("axis-ticks", f),
				g = this.relatedObj.canvasObj,
				h = a.offset,
				i = a.opposite,
				j = a.showAxis,
				k = a.tickInterval,
				l = a.tickLength,
				m = a.tickWidth,
				a = a.tickColor,
				n = c;
			if (e && j) {
				c = this.getAxisPosition(c);
				e = this.getAxisPosition(d);
				g = !i ? g.x - h : g.toX + h;
				for (b.path(["M", g, c, "L", g, e], f).attr({
					stroke: a,
					"stroke-width": m
				}); bb(n) <= d;) h = this.getAxisPosition(n), c = !i ? g - l : g + l, b.path(["M", g, h, "L", c, h], f).attr({
					stroke: a,
					"stroke-width": m
				}), n += k
			}
		},
		getAxisPosition: function(a, b) {
			var c;
			b ? c = (a - this.startPixel) / this.pixelValueRatio + this.min : (a = this.axisData.reversed ? this.min + (this.max - a) : a, c = this.startPixel + (a - this.min) * this.pixelValueRatio);
			return c
		},
		drawPlotLine: function() {
			for (var a = this.renderer, b = a.paper, c = this.isVertical, d = +!c, g = this.belowLineGroup, h = this.topLineGroup, j = this.belowLabelGroup, k = this.topLabelGroup, l = this.axisData.plotLines || [], m = this.lines = this.lines || [], n = this.labels = this.labels || [], s = this.relatedObj.canvasObj, q = this.globalOptions || {}, u = this.elements || {}, v = this.cache || {}, x = v.hashTable, w = v.indexArr, C = c ? this.startY : this.startX, D = c ? this.endY : this.endX, I = this.primaryOffset, E = parseFloat(a.canvasBorderWidth) || 0, y, G = (a.tooltip || {}).enabled !== !1, W, o, Q, F, N, J, na, oa, M, ka, K, S, Y, fa, R, U, L = 0, aa = 0, V, Z, ma, X, $, ia, ua, xa, pa = q.chart.xDepth || 0, sa, ya, Aa, va, Ha, Da, Ja, Ca, Ka, Ma, Ta, Va = ta(l.length, ta(m.length, n.length)), Sa, Na, Ra, Wa, v = [], Za, q = 0; q < Va; q += 1) {
				o = Q = F = null;
				X = J = "visible";
				o = m[q];
				Q = n[q];
				Ka = (na = l[q]) && na.width;
				K = (y = na && na.label) && y.style;
				if (!o && na) {
					if (ya = na.zIndex > 3 ? h : g, Ka > 0.1) o = m[q] = b.path(ya).css(na.style), u.lines = u.lines || [], u.lines.push(o)
				} else if (!na && (o || Q)) o && o.remove(), Q && Q.remove(), m && (m[q] = null), n && (n[q] = null), u && u.lines && (u.lines[q] = null), u && u.labels && (u.labels[q] = null);
				if (na) {
					if (!Q && y && !na.stepped) {
						if (y.text != B && y.text != " ") {
							Q = na.zIndex >= 3 ? k : j;
							Q = n[q] = b.text(Q).css(K);
							if (o) o.label = Q;
							u.labels = u.labels || [];
							u.labels.push(Q)
						}
					} else if (Q) if (y) if (y.text === B || y.text === " ") Q.isRotationSet = !1, Q.remove(), delete Q, n && (n[q] = null), u && u.labels && (u.labels[q] = null);
					else {
						if (na && na.stepped) Q.isRotationSet = !1, Q.remove(), delete Q, n && (n[q] = null), u && u.labels && (u.labels[q] = null)
					} else Q.isRotationSet = !1, Q.remove(), delete Q, n && (n[q] = null), u && u.labels && (u.labels[q] = null);
					if (!o && !Q) o = Q = null;
					else if (na.value !== null) {
						if (na) va = na.isVline, Ha = na.isTrend, Da = na.isGrid, W = na.tooltext, Ja = na.value, Ca = na.color, Ma = na.dashStyle, Ta = Ha ? na.to : null, oa = na._isStackSum;
						if (y) M = y.text, ka = K && K.color, fa = y.offsetScaleIndex || 0, R = y.offsetScale, S = K && K.fontSize, Y = K && K.lineHeight, U = y.rotation, L = y.x || 0, aa = y.y || 0, V = y.align, Z = y.verticalAlign, ma = y.textAlign, sa = (sa = y && y.borderWidth) ? sa.indexOf("px") !== -1 ? sa.replace("px", "") : 1 : 1;
						S && (Na = S, Na.indexOf("px") !== -1 && (Na = Na.replace("px", ""), Na = parseFloat(Na)));
						Y && (Ra = Y, Ra.indexOf("px") !== -1 && (Ra = Ra.replace("px", ""), Ra = parseFloat(Ra)));
						c ? (K = this.getAxisPosition(Ja), N = Ha ? this.getAxisPosition(Ta) || K : K, Aa = K !== N ? !0 : !1, ya = ["M", s.x, K, "L", s.toX, N], va ? a.logic.isBar && (ua = a.yAxis[fa], !oa && !isNaN(R) && R >= 0 && R <= 1 && (R = ua.min + (ua.max - ua.min) * R), ia = ua.getAxisPosition(O(R, Ja)) + L) : ia = y ? $ = this.axisData.isOpposite || V === "right" ? s.toX + L : s.x + L : $ = this.axisData.isOpposite ? s.toX : s.x) : ($ = Ha ? this.getAxisPosition(Ja) : this.getAxisPosition(Ja) || 0, ia = Ha ? this.getAxisPosition(Ta) || $ : $, !Ha && !va && pa > 0 && ($ += pa, ia += pa, D += pa), Aa = $ !== ia ? !0 : !1, ya = ["M" + $, s.y, "L", ia, s.toY], J = $ + I < C || $ + I > D ? "hidden" : J, va ? (ua = a.yAxis[fa], !oa && !isNaN(R) && R >= 0 && R <= 1 && (R = ua.min + (ua.max - ua.min) * (1 - R)), K = ua.getAxisPosition(O(R, Ja)) + aa, K -= E + parseFloat(sa)) : this.axisData.opposite || Z === "top" && !Da ? (K = s.y + aa, Sa = "bottom") : K = s.toY + aa, N = K);
						if (Q) {
							if (y && y.backgroundColor) y.labelBgClr = f({
								color: y.backgroundColor,
								alpha: y.backgroundOpacity * 100
							});
							if (y && y.borderColor) y.labelBorderClr = f({
								color: y.borderColor,
								alpha: "100"
							});
							xa = Ha ? V === "left" ? K : N : N;
							ua = ia - +!va * d * pa;
							ua = ia - +!va * d * pa + d * (L || 0);
							Wa = Na ? Na * 0.2 : 2;
							X = !c ? ia + I < C || ia + I > D ? "hidden" : X : X;
							N = ma === "left" ? "start" : ma === "right" ? "end" : "middle";
							oa ? (Sa = "bottom", xa += Na * 0.4, v.push(Q)) : d && this.axisData.opposite ? (Sa = $a, N = U ? "start" : "middle") : Sa = Z;
							/\n|<br\s*?\/?>/ig.test(M) && !U && Da && (Sa = d && this.axisData.opposite && !U ? "middle" : Fa, xa -= Ra);
							Q.attr({
								transform: " "
							});
							Q.attr({
								text: M,
								fill: ka || Ca,
								"text-bound": y && [y.labelBgClr, y.labelBorderClr, sa, Wa],
								title: y && (y.originalText || ""),
								x: ua,
								y: xa,
								"text-anchor": N,
								"vertical-align": Sa,
								visibility: X
							});
							U && Q.rotate(U, ua, xa);
							X = c ? xa.toString() : ia.toString();
							(y = x[X]) ? y.elements.push(Q) : (y = {
								counter: q,
								keyProp: X,
								elements: [Q]
							}, x[X] = y, w.push(y))
						}
						o && (o.attr({
							path: ca(ya, Ka),
							stroke: Ca,
							"stroke-width": Ka,
							"shape-rendering": !Aa && Ka >= 1 ? e : void 0,
							"stroke-dasharray": Ma ? Ma : void 0,
							visibility: J
						}), G && W && Ka < i && J && (F = b.path(ya).attr({
							stroke: r,
							"stroke-width": i,
							ishot: !0
						})), F = F || o, G && F.tooltip(W), X = c ? K.toString() : $.toString(), (y = x[X]) ? y.elements.push(o) : (y = {
							counter: q,
							keyProp: X,
							elements: [o]
						}, x[X] = y, w.push(y)));
						if (na && na.isMinLabel) this.poi.min = {
							label: Q,
							index: q,
							line: o
						};
						else if (na && na.isMaxLabel) this.poi.max = {
							label: Q,
							index: q,
							line: o
						};
						else if (na && na.isZeroPlane) this.poi.zero = {
							label: Q,
							index: q,
							line: o
						};
						o = Q = null
					}
				}
			}
			b = v.length;
			Za = O(a.options.plotOptions.series.animation.duration, 0);
			if (b > 0) for (q = 0; q < b; q += 1)(a = v[q]) &&
			function(a) {
				a.hide();
				setTimeout(function() {
					a.show()
				}, Za)
			}(a)
		},
		drawPlotBands: function() {
			var a = this.renderer,
				b = a.paper,
				c = this.isVertical,
				d = a.options.chart.hasScroll,
				e = this.belowBandGroup,
				g = this.topBandGroup,
				h = this.belowLabelGroup,
				i = this.topLabelGroup,
				j = this.axisData.plotBands || [],
				k = this.bands = this.bands || [],
				l = this.bandLabels = this.bandLabels || [],
				m = this.relatedObj.canvasObj,
				n = this.elements || {},
				s = this.cache || {},
				q = s.hashTable,
				s = s.indexArr,
				r = c ? this.startY : this.startX,
				u = c ? this.endY : this.endX,
				v = this.primaryOffset,
				a = (a.tooltip || {}).enabled !== !1,
				x, w, E, y, B, C, o, D, G, F, N, J, M, K, ka, R, S, Y, fa, U, aa, L, V, X, Z, ma, ca, $, ia, ua, xa, pa, sa, ya, va = ta(j.length, k.length);
			for (ya = 0; ya < va; ya += 1) {
				w = y = "visible";
				E = k[ya];
				B = l[ya];
				M = (J = (C = j[ya]) && C.label) && J.style;
				if (!E && C) {
					if (E = C.zIndex > 3 ? g : e, pa = C.zIndex > 3 ? i : h, E = k[ya] = b.rect(E), n.bands = n.bands || [], n.bands[ya] = E, J && J.text) B = l[ya] = E.label = b.text(pa).css(M), n.labels = n.labels || [], n.labels[ya] = B
				} else if (!C && E) {
					n.labels && (l[ya] = n.labels[ya] = null);
					E.label && E.label.remove();
					k[ya] = n.bands[ya] = null;
					E.remove();
					delete E;
					continue
				}
				if (C) x = C.tooltext, o = C.to, D = C.from, G = C.value, F = C.width, N = C.color;
				if (J) Y = M && M.fontSize, fa = M && M.lineHeight, K = J.borderWidth, ka = J.align, R = J.x, S = J.y, L = J.text, V = J.originalText, X = M && M.color, Z = J.backgroundColor, ma = J.backgroundOpacity, ca = J.borderColor, U = J.textAlign, aa = J.verticalAlign, $ = J.borderType;
				Y && (C = Y, C.indexOf("px") !== -1 && (C = C.replace("px", ""), parseFloat(C)));
				fa && (C = fa, C.indexOf("px") !== -1 && (C = C.replace("px", ""), parseFloat(C)));
				sa = this.getAxisPosition(O(o, G));
				xa = this.getAxisPosition(O(D, G));
				M = c ? m.x : xa;
				C = c ? sa : m.y;
				pa = c ? m.w : (!this.axisData.reversed ? sa - xa : xa - sa) || F || 1;
				sa = c ? xa - sa || 1 : m.h;
				xa = M + pa;
				pa = Aa(pa);
				sa < 0 && (sa = Aa(sa), C -= sa);
				y = d ? !0 : !c ? M + v > u || xa + v < r ? "hidden" : y : y;
				E && (E.attr({
					x: M,
					y: C,
					width: pa,
					height: sa,
					fill: f(N),
					"stroke-width": 0,
					visibility: y
				}), a && x && E.tooltip(x));
				if (B && J) {
					(y = K) && y.indexOf("px") !== -1 && y.replace("px", "");
					y = c ? ka === "right" ? m.toX + R : m.x + R : M + pa / 2;
					E = c ? C + sa / 2 : m.toY + S;
					w = !c ? y + v < r || y + v > u ? "hidden" : w : w;
					if (Z) ia = J.labelBgClr = f({
						color: Z,
						alpha: ma * 100
					});
					if (ca) ua = J.labelBorderClr = f({
						color: ca,
						alpha: "100"
					});
					U = U === "left" ? "start" : U === "right" ? "end" : "center";
					B.attr({
						text: L,
						title: V || "",
						fill: X,
						"text-bound": [ia, ua, K, Y * 0.2, $ === "solid" ? !1 : !0],
						x: y,
						y: E,
						"text-anchor": U,
						"vertical-align": aa,
						visibility: w
					});
					J = c ? E.toString() : y.toString();
					(w = q[J]) ? w.elements.push(B) : (w = {
						counter: ya,
						keyProp: J,
						elements: [B]
					}, q[J] = w, s.push(w))
				}
			}
		},
		drawAxisName: function() {
			var o;
			var a = this.axisData,
				b = a.title || {},
				c = b && b.style,
				d = b.align,
				e = b.centerYAxisName || !1,
				g = this.renderer.paper,
				h = this.isVertical,
				i = this.relatedObj.canvasObj,
				j = O(a.offset, 0) + O(b.margin, 0),
				k = b.text || "",
				l = this.name || void 0,
				a = a.opposite,
				m = this.layerBelowDataset,
				m = m.nameGroup = m.nameGroup || g.group("axis-name", m),
				n = O(b.rotation, !a ? 270 : 90),
				s = h ? a ? i.toX + j : i.x - j : (i.x + i.toX) / 2,
				q, r, u, v, x;
			if (c) r = c.color, u = f({
				color: c.backgroundColor,
				alpha: 100
			}), v = f({
				color: c.borderColor,
				alpha: 100
			}), (o = (c = c.border) && c.split(" "), c = o) && c.length > 0 && (x = c[0].indexOf("px") != -1 ? parseFloat(c[0].replace("px", "")) : parseFloat(c[0]));
			e = h ? d === "low" ? i.toY : e ? (i.y + i.toY) / 2 : this.renderer.chartHeight / 2 : i.toY + j;
			!l && k ? l = this.name = g.text(m).css(b.style) : !k && l && l.remove();
			if (!isNaN(n) && n && h) q = b.style.fontSize, q = q.indexOf("px") != -1 ? q.replace("px", "") : q, s = a ? s + parseFloat(q) : s - parseFloat(q);
			l && l.attr({
				text: k,
				title: b.originalText || "",
				fill: r || "#000000",
				"text-bound": [u, v, x, q * 0.1],
				"text-anchor": d === "low" ? !a ? "start" : "end" : "middle",
				"vertical-align": h ? !n ? "middle" : "top" : a ? $a : "top",
				transform: h ? "t" + s + "," + e + "r" + n : "t" + s + "," + e
			});
			this.elements.name = l
		},
		drawLine: function() {
			var a = this.axisData,
				b = this.renderer.paper,
				c = this.min,
				d = this.max,
				e = this.isVertical,
				f = a.opposite,
				g = this.layerBelowDataset,
				g = this.lineGroup = this.lineGroup || b.group("axis-lines", g),
				h = a.lineColor,
				a = a.lineThickness,
				i = this.relatedObj.canvasObj,
				j;
			e ? (c = this.getAxisPosition(c), d = this.getAxisPosition(d), e = j = !f ? i.x : i.toX) : (e = i.x, j = i.toX, c = d = !f ? i.toY : i.y);
			this.elements.axisLine = b.path(["M", e, c, "L", j, d], g).attr({
				stroke: h,
				"stroke-width": a
			})
		},
		realtimeUpdateX: function(a) {
			if (a > 0) {
				for (var b = this.axisData.plotBands, c = this.min + a, d, e = b.length; e--;) if ((d = b[e]) && !d.isNumVDIV) d.value < c || d.from < c || d.to < c ? b.splice(e, 1) : (d.value !== void 0 && (d.value -= a), d.from !== void 0 && (d.from -= a), d.to !== void 0 && (d.to -= a));
				this.drawPlotLine();
				this.drawPlotBands()
			}
		},
		realtimeUpdateY: function(a, b) {
			var c = this.axisData,
				d = this.min = c.min = a,
				c = this.span = (this.max = c.max = b) - d,
				c = this.pixelRatio = this.isVertical ? this.relatedObj.canvasObj.h / c : this.relatedObj.canvasObj.w / c;
			this.pixelValueRatio = this.isReverse ? -c : c;
			this.drawPlotLine();
			this.drawPlotBands()
		}
	};
	Sa.prototype.constructor = Sa;
	var Za = function(a, b, c, d) {
			return Ca(b - c[1] - d.top, a - c[0] - d.left)
		};
	U("renderer.cartesian", {
		drawCanvas: function() {
			var p;
			var a = this.options.chart || {},
				c = a.plotBackgroundColor,
				d = this.paper,
				e = this.elements,
				g = e.canvas,
				h = e.canvas3DBase,
				i = e.canvas3dbaseline,
				h = e.canvasBorder,
				j = e.canvasBg,
				k = this.canvasTop,
				l = this.canvasLeft,
				n = this.canvasWidth,
				s = this.canvasHeight,
				r = O(a.plotBorderRadius, 0),
				j = a.plotBorderWidth,
				v = j * 0.5,
				x = a.plotBorderColor,
				w = a.isBar,
				C = a.is3D,
				B = a.use3DLighting,
				D = a.showCanvasBg,
				I = a.canvasBgDepth,
				E = a.showCanvasBase,
				y = a.canvasBaseColor3D,
				G = a.canvasBaseDepth,
				F = a.plotShadow,
				o = b && j === 0 && F && F.enabled,
				Q = a.xDepth || 0,
				a = a.yDepth || 0,
				J = this.layers,
				M = J.background,
				N = J.dataset;
			J.tracker = J.tracker || d.group("hot").insertAfter(N);
			J.datalabels = J.datalabels || d.group("datalabels").insertAfter(N);
			p = J.canvas = J.canvas || d.group("canvas").insertAfter(M), J = p;
			if (!h) e.canvasBorder = d.rect(l - v, k - v, n + j, s + j, r, J).attr({
				"stroke-width": j,
				stroke: x,
				"stroke-linejoin": j > 2 ? "round" : "miter"
			}).shadow(F);
			e["clip-canvas"] = [ta(0, l - Q), ta(0, k - a), ta(1, n + Q * 2), ta(1, s + a * 2)];
			e["clip-canvas-init"] = [ta(0, l - Q), ta(0, k - a), 1, ta(1, s + a * 2)];
			if (C) {
				if (D) j = w ? e.canvasBg = d.path(["M", l, ",", k, "L", l + I * 1.2, ",", k - I, ",", l + n - I, ",", k - I, ",", l + n, ",", k, "Z"], J) : e.canvasBg = d.path(["M", l + n, ",", k, "L", l + n + I, ",", k + I * 1.2, ",", l + n + I, ",", k + s - I, ",", l + n, ",", k + s, "Z"], J), j.attr({
					"stroke-width": 0,
					stroke: "none",
					fill: f(c)
				});
				if (E) {
					h = w ? e.canvas3DBase = d.cubepath(l - Q - G - 1, k + a + 1, G, s, Q + 1, a + 1, J) : e.canvas3DBase = d.cubepath(l - Q - 1, k + s + a + 1, n, G, Q + 1, a + 1, J);
					h.attr({
						stroke: "none",
						"stroke-width": 0,
						fill: [y.replace(q, u), !B]
					});
					if (!i) i = e.canvas3dbaseline = d.path(void 0, J);
					i.attr({
						path: w ? ["M", l, k, "V", s + k] : ["M", l, k + s, "H", n + l],
						stroke: m.tintshade(y.replace(q, u), 0.05).rgba
					})
				}
			}
			if (!g && c) e.canvas = d.rect(l, k, n, s, r, J).attr({
				"stroke-width": 0,
				stroke: "none",
				fill: f(c)
			}).shadow(o)
		},
		drawAxes: function() {
			var a = this.logic,
				b = this.options,
				c = this.paper,
				d = this.layers,
				e = d.dataset,
				f = d.layerBelowDataset = d.layerBelowDataset || c.group("axisbottom"),
				g = d.layerAboveDataset = d.layerAboveDataset || c.group("axistop"),
				c = this.xAxis = [],
				d = this.yAxis = [];
			f.insertBefore(e);
			g.insertAfter(e);
			if (b.xAxis && b.xAxis.length) {
				e = 0;
				for (f = b.xAxis.length; e < f; e += 1) c[e] = this.xAxis[e] = new Sa(b.xAxis[e], this, a.isBar)
			} else c[0] = this.xAxis[0] = new Sa(b.xAxis, this, a.isBar);
			if (b.yAxis) {
				e = 0;
				for (f = b.yAxis.length; e < f; e += 1) d[e] = this.yAxis[e] = new Sa(b.yAxis[e], this, !a.isBar, !a.isBar)
			}
			e = 0;
			for (f = d.length; e < f; e += 1) d[e].draw();
			e = 0;
			for (f = c.length; e < f; e += 1) c[e].draw()
		},
		drawScroller: function() {
			var a = this,
				b = a.options,
				c = a.paper,
				d = a.layers,
				e = a.xAxis["0"] || {},
				f = (e.axisData || {}).scroll || {},
				g = a.canvasTop,
				h = a.canvasLeft,
				i = a.canvasWidth,
				j = a.canvasHeight,
				k = ma(a.canvasBorderWidth, 2),
				l, m, n, s, q, r, u, v, w, E, y, C, B, o, D, F = d.dataset,
				J = d.datalabels,
				M = d.tracker;
			s = d.layerAboveDataset;
			var N, K;
			if (f.enabled) N = d.scroll = d.scroll || c.group("scroll").insertAfter(s), s = f.scrollRatio, b = O(b[aa].xAxisScrollPos, f.startPercent), q = f.viewPortMax, r = f.viewPortMin, m = f.vxLength, u = Ka(m), v = f.buttonWidth, w = f.height, E = f.padding, y = f.color, C = f.flatScrollBars, m = f.windowedCanvasWidth = e.getAxisPosition(m), l = f.fullCanvasWidth = e.getAxisPosition(q - r) - m, n = x(b * l), B = a.fusionCharts.jsVars._reflowData, o = {
				hcJSON: {
					_FCconf: {
						xAxisScrollPos: 0
					}
				}
			}, D = o.hcJSON._FCconf, d.scroller = c.scroller(h - k, g + j + E, i + k * 2, w, !0, {
				showButtons: !0,
				displayStyleFlat: C,
				buttonWidth: v,
				scrollRatio: s,
				scrollPosition: b
			}, N).data("fullCanvasWidth", l).data("windowedCanvasWidth", m).attr({
				"scroll-display-style": C,
				fill: y
			}).scroll(function(b) {
				n = -x(b * l);
				F && F.transform(["T", n, 0]);
				J && J.transform(["T", n, 0]);
				M && M.transform(["T", n, 0]);
				e.setOffset && e.setOffset(n);
				scrollStateObj = {
					position: b,
					direction: b - f.lastPos || 0,
					vxLength: u
				};
				D.xAxisScrollPos = f.lastPos = b;
				G(B, o, !0);
				if (scrollStateObj.direction !== 0) for (K = 0; K < a.datasets.length; K++) a[a.datasets[K].drawPlot + "Scroll"] && a[a.datasets[K].drawPlot + "Scroll"].call(a, a.plots[K], a.datasets[K], scrollStateObj)
			});
			return f.enabled
		},
		finalizeScrollPlots: function() {
			var a = this,
				b = a.container,
				c = a.elements,
				d = a.layers,
				e = d.scroller,
				f = d.dataset,
				g = d.datalabels,
				d = d.tracker,
				i, k = {},
				l, m = a.xAxis["0"] || {},
				n = (m.axisData || {}).scroll || {},
				s = O(a.options[aa].xAxisScrollPos, n.startPercent),
				q = n.fullCanvasWidth;
			n.enabled && (f.attr({
				"clip-rect": c["clip-canvas"]
			}), g.attr({
				"clip-rect": c["clip-canvas"]
			}), d.attr({
				"clip-rect": c["clip-canvas"]
			}), c = function(b) {
				var c = a.elements.canvas,
					d = i.left,
					f = i.top,
					g = b.type,
					t = j && h.getTouchEvent(b) || fa,
					d = b.layerX || t.layerX || (b.pageX || t.pageX) - d,
					b = b.layerY || t.layerY || (b.pageY || t.pageY) - f;
				switch (g) {
				case "dragstart":
					l = c.isPointInside(d, b);
					k.ox = l && d || null;
					if (!l) return !1;
					break;
				case "dragend":
					l = !1;
					k = {};
					break;
				default:
					if (!l) break;
					c = d - k.ox;
					k.ox = d;
					k.scrollPosition = e.attrs["scroll-position"] - c / q;
					e.attr({
						"scroll-position": k.scrollPosition
					})
				}
			}, j && (i = h.getPosition(b), b && (K(b, "dragstart drag dragend", c), V(b, "dragstart drag dragend", c))), s > 0 && (b = -x(s * q), f && f.transform(["T", b, 0]), g && g.transform(["T", b, 0]), d && d.transform(["T", b, 0]), m.setOffset && m.setOffset(b)))
		},
		drawPlotColumn: function(a, b, d) {
			var e = this,
				g = a.data,
				h = g.length,
				j = a.items,
				k = a.graphics || (a.graphics = []),
				l = e.paper,
				n = e.logic,
				q = e.layers,
				u = e.options,
				v = e.elements,
				w = u.chart,
				C = (u.tooltip || {}).enabled !== !1,
				B = e.definition.chart,
				D = u.plotOptions.series,
				G = e.xAxis[b.xAxis || 0],
				F = e.yAxis[b.yAxis || 0],
				I = F.axisData.reversed,
				E = n.isLog,
				y = n.is3D,
				ha = n.isStacked,
				W = n.isWaterfall,
				o = n.isCandleStick,
				Q = Y(G.axisData.scroll, {}),
				M = d || {},
				K = Q.enabled,
				u = O(M.position, u[aa].xAxisScrollPos, Q.startPercent),
				R = M.vxLength || Ka(Q.vxLength),
				na = M.scrollStart || ta(0, x((h - R) * u) - 1) || 0,
				M = M.scrollEnd || ma(h, na + R + 2) || h,
				Q = w.canvasBorderOpacity = m.color(w.plotBorderColor).opacity,
				u = e.canvasBorderWidth,
				Q = w.isCanvasBorder = Q !== 0 && u > 0,
				oa;
			c(n.name);
			var n = d !== J ? 0 : isNaN(+D.animation) && D.animation.duration || D.animation * 1E3,
				S = b.numColumns || 1,
				fa = b.columnPosition || 0,
				u = w.use3DLighting,
				U = b.visible === !1 ? "hidden" : "visible",
				V = w.overlapColumns,
				X = G.getAxisPosition(0),
				X = G.getAxisPosition(1) - X,
				Z = B && B.plotspacepercent,
				ca = O(B && B.plotpaddingpercent),
				B = D.groupPadding,
				Ga = D.maxColWidth,
				B = (1 - Z * 0.01) * X || ma(X * (1 - B * 2), Ga * S),
				Z = B / 2;
			B /= S;
			var L = ma(B - 1, S > 1 ? !V && ca === J ? 4 : ca > 0 ? B * ca / 100 : 0 : 0),
				fa = fa * B - Z + L / 2,
				Oa = F.max,
				$ = F.min,
				S = Oa > 0 && $ >= 0,
				V = Oa <= 0 && $ < 0,
				ca = Oa > 0 && $ < 0,
				Z = V || I && S ? Oa : E || S ? $ : 0;
			oa = F.yBasePos = F.getAxisPosition(Z);
			var Ga = O(w.useRoundEdges, 0),
				ia = D.dataLabels.style,
				La = q.dataset = q.dataset || l.group("dataset-orphan"),
				pa = q.datalabels = q.datalabels || l.group("datalabels").insertAfter(La),
				q = q.tracker,
				sa = e.canvasTop,
				va = e.canvasLeft,
				ua = e.canvasWidth,
				xa = e.canvasBottom,
				Ja = e.canvasRight,
				Ca, ya, Ua;
			parseInt(ia.lineHeight, 10);
			var Ya, Ha, Da, Fa, Ma, Na;
			!d && pa.hide();
			B -= L;
			K && na > M - R - 2 && (na = ta(0, M - R - 2));
			if (ha) Na = La.shadows || (La.shadows = l.group("shadows", La).toBack());
			L = La.column || (La.column = l.group("columns", La));
			!o && !y && !K && (L.attrs["clip-rect"] || L.attr({
				"clip-rect": v["clip-canvas"]
			}));
			W && L.toBack();
			if (y) {
				ya = w.xDepth || 0;
				Ua = w.yDepth || 0;
				d = L.negative = L.negative || l.group("negative-values", L);
				R = L.column = L.column || l.group("positive-values", L);
				Fa = L.zeroPlane;
				if (!Fa && $ < 0 && Oa >= 0) Fa = L.zeroPlane = l.group("zero-plane", L).insertBefore(R), Ca = w.zeroPlaneColor, v.zeroplane = l.cubepath(va - ya, oa + Ua, ua, 1, ya, Ua, Fa).attr({
					fill: [Ca, !u],
					stroke: "none",
					"stroke-width": 1
				});
				if (!(Fa = d.data("categoryplots"))) d.data("categoryplots", Array(h)), Fa = d.data("categoryplots");
				if (!(Ca = R.data("categoryplots"))) R.data("categoryplots", Array(h)), Ca = R.data("categoryplots");
				for (v = 0; v < h; v += 1) Fa[v] = Fa[v] || l.group(d), Ca[v] = Ca[v] || l.group(R)
			} else Ma = L;
			for (v = na; v < M; v += 1) {
				w = g[v];
				na = w.y;
				h = w.toolText;
				d = R = null;
				if (na === null) {
					if (ia = j[v]) d = ia.graphic, y || d.attr({
						height: 0
					})
				} else {
					Ya = !1;
					$ = O(w.x, v);
					Ha = w.link;
					Oa = s(w.borderWidth) || 0;
					La = w._FCW * X;
					$ = G.getAxisPosition(w._FCX) || G.getAxisPosition($) + fa;
					ia = w.previousY;
					Da = F.getAxisPosition(ia || Z);
					ua = F.getAxisPosition(na + (ia || 0));
					L = Aa(ua - Da);
					La = La || B;
					if (y) {
						na < 0 && (ua = Da, Ya = !0);
						Ma = na < 0 ? Fa : Ca;
						if (!(ia = j[v])) ia = j[v] = {
							index: v,
							value: na,
							graphic: l.cubepath(Ma[v]),
							dataLabel: null,
							tracker: null,
							hot: null
						};
						d = ia.graphic;
						d.attr({
							cubepath: [$ - ya, oa + Ua, La, 0, ya, Ua],
							fill: [f(w.color), !u],
							stroke: Oa && f(w.borderColor) || "NONE",
							"stroke-width": Oa,
							visibility: U
						}).shadow(D.shadow && w.shadow, Na).animate({
							cubepath: [$ - ya, ua + Ua, La, L, ya, Ua]
						}, n, "normal", function() {
							pa.show()
						}).data("BBox", {
							height: L,
							width: La,
							x: $,
							y: ua
						});
						if (Ha || C) {
							!ha && L < i && (ua -= (i - L) / 2, L = i);
							if (!ia.tracker) ia.tracker = l.cubepath(q);
							R = ia.tracker;
							R.attr({
								cubepath: [$ - ya, ua + Ua, La, L, ya, Ua],
								cursor: Ha ? "pointer" : "",
								stroke: Oa && r || "NONE",
								"stroke-width": Oa,
								fill: r,
								ishot: !! Ha,
								visibility: U
							}).click(function() {
								var a = this.data("link");
								a && e.linkClickFN.call({
									link: a
								}, e)
							}).tooltip(h).data("link", Ha);
							R._.cubetop.click(function() {
								var a = this.data("link");
								a && e.linkClickFN.call({
									link: a
								}, e)
							}).tooltip(h).data("link", Ha);
							R._.cubeside.click(function() {
								var a = this.data("link");
								a && e.linkClickFN.call({
									link: a
								}, e)
							}).tooltip(h).data("link", Ha)
						}
						ha && Ya && (d.toBack(), R && R.toBack())
					} else {
						Ya = !1;
						if (!E && !I && na < 0 || !E && I && na > 0) ua = Da, Ya = !0;
						I && !ca && na > 0 && (ua = Da - L, Ya = !1);
						W && na < 0 && N(ia) && (ua -= L, Ya = !0);
						!o && !K && (ka(ua) <= sa && (L -= sa - ua - +Q, ua = sa - +Q), x(ua + L) >= xa && (L -= x(ua + L) - xa + + !! Oa + +Q), Oa <= 1 && (x($) <= va && (La += $, $ = va - Oa / 2 + + !! Oa - +Q, La -= $), x($ + La) >= Ja && (La = Ja - $ + Oa / 2 - + !! Oa + +Q)));
						Da = m.crispBound($, ua, La, L, Oa);
						$ = Da.x;
						ua = Da.y;
						La = Da.width;
						L = Da.height;
						if (!o && Q && (!N(ia) || W && ia === na && na === w._FCY)) if (V && !I && !E) oa = ua - (sa - Oa / 2), L += oa, oa = ua -= oa;
						else if (E || S || I && V) L = xa - ua + Oa / 2, oa = ua + L;
						W && ia && Oa > 0 && D.connectorOpacity !== 0 && D.connectorWidth === 1 && D.connectorDashStyle && (L -= 1, na < 0 && (ua += 1));
						L <= 1 && (L = 1, ua += na < 0 ? 0 : -L);
						b._columnWidth = La;
						if (!(ia = j[v])) if (ia = j[v] = {
							index: v,
							value: na,
							width: La,
							graphic: l.rect(Ma),
							valueBelowPlot: Ya,
							dataLabel: null,
							tracker: null
						}, d = ia.graphic, d.attr({
							x: $,
							y: oa,
							width: La,
							height: 0,
							r: Ga,
							fill: f(w.color),
							stroke: f(w.borderColor),
							"stroke-width": Oa,
							"stroke-dasharray": w.dashStyle,
							"stroke-linejoin": "miter",
							visibility: U
						}).shadow(D.shadow && w.shadow, Na).animate({
							y: ua,
							height: L || 1
						}, n, "normal", function() {
							pa.show()
						}).data("BBox", Da), Ha || C) {
							if (!ia.tracker) ia.tracker = l.rect(q);
							!ha && L < i && (ua -= (i - L) / 2, L = i);
							R = ia.tracker;
							R.attr({
								x: $,
								y: ua,
								width: La,
								height: L,
								r: Ga,
								cursor: Ha ? "pointer" : "",
								stroke: r,
								"stroke-width": Oa,
								fill: r,
								ishot: !! Ha,
								visibility: U
							}).click(function() {
								var a = this.data("link");
								a && e.linkClickFN.call({
									link: a
								}, e)
							}).tooltip(h).data("link", Ha)
						}
					}
					Ha = e.drawPlotColumnLabel(a, b, v, $, ua)
				}
				Ha && k.push(Ha);
				d && k.push(d);
				R && k.push(R);
				e.drawTracker && e.drawTracker.call(e, a, b, v)
			}
			a.visible = b.visible !== !1;
			return a
		},
		drawPlotColumnScroll: function(a, b, c) {
			var d = a.data.length,
				e = a.items,
				f;
			f = c.vxLength;
			var g = ta(0, x((d - f) * c.position) - 1) || 0,
				d = ma(d, g + f + 2) || d;
			g > d - f - 2 && (g = ta(0, d - f - 2));
			c.scrollEnd = d;
			for (f = g; f < d; f++) if (!e[f]) {
				c.scrollStart = f;
				this.drawPlotColumn(a, b, c);
				break
			}
		},
		drawPlotColumnLabel: function(a, b, c, d, e, f) {
			var o;
			var d = this.options,
				g = this.logic,
				h = d.chart,
				i = this.paper,
				j = this.layers,
				d = d.plotOptions.series.dataLabels.style,
				k = h.rotateValues === 1 ? 270 : 0,
				l = this.canvasHeight,
				m = this.canvasTop,
				n = a.data[c],
				s = a.items[c],
				q = h.valuePadding + 2,
				r = s.graphic,
				a = s.dataLabel,
				c = Y(s.valueBelowPlot, n.y < 0),
				u = g.isStacked,
				g = g.is3D,
				v = h.xDepth || 0,
				w = h.yDepth || 0,
				x = n.displayValue,
				b = b.visible === !1 ? "hidden" : "visible",
				y = h.placeValuesInside,
				h = !1,
				f = f || j.datalabels;
			N(x) && x !== B && n.y !== null ? (a ? k && a.rotate(360 - k) : (a = s.dataLabel = i.text().attr({
				text: x
			}).css(d), h = !0), j = a.getBBox(), r = r.data("BBox"), i = r.height, o = s = k ? j.width : j.height, j = o, j += q, q = s * 0.5 + q, r = r.x + r.width * 0.5, l = c ? m + l - (e + i) : e - m, u ? (e = e + i * 0.5 + (w || 0), r -= v) : y ? i >= j ? (e += c ? i - q : q, n._valueBelowPoint = 1, g && (r -= v, e += w)) : l >= j ? (e += c ? i + q : -q, g && c && (r -= v, e += w)) : (e += c ? i - q : q, n._valueBelowPoint = 1, g && (r -= v, e += w)) : l >= j ? (e += c ? i + q : -q, g && c && (r -= v, e += w)) : (e += c ? i - q : q, n._valueBelowPoint = 1, g && (r -= v, e += w)), a.attr({
				x: r,
				y: e,
				visibility: b
			}), k && a.attr("transform", "T0,0,R" + k), h && f.appendChild(a), h && Y(d.backgroundColor, d.borderColor) !== B && a.attr({
				"text-bound": [d.backgroundColor, d.borderColor, 1, 2]
			})) : a && a.attr({
				text: B
			});
			return a
		},
		drawPlotFloatedcolumn: function(a, b) {
			this.drawPlotColumn.call(this, a, b)
		},
		drawPlotColumn3d: function(a, b) {
			this.drawPlotColumn.call(this, a, b)
		},
		drawPlotBar: function(a, b) {
			var d = this,
				e = a.data,
				g = e.length,
				h = a.items,
				j = a.graphics = [],
				k = d.paper,
				l = d.logic,
				n = d.layers,
				q = d.options,
				u = d.elements,
				v = q.chart,
				w = (q.tooltip || {}).enabled !== !1,
				B, C = d.definition.chart,
				q = q.plotOptions.series,
				D = d.xAxis[b.xAxis || 0],
				G = d.yAxis[b.yAxis || 0],
				F = l.is3D,
				I = l.isStacked,
				E = v.canvasBorderOpacity = m.color(v.plotBorderColor).opacity,
				y = d.canvasBorderWidth,
				E = v.isCanvasBorder = E !== 0 && y > 0;
			c(l.name);
			var l = isNaN(+q.animation) && q.animation.duration || q.animation * 1E3,
				M = b.numColumns || 1,
				W = b.columnPosition || 0,
				y = v.use3DLighting,
				o = b.visible === !1 ? "hidden" : "visible",
				Q = v.overlapColumns,
				N = D.getAxisPosition(0),
				N = D.getAxisPosition(1) - N,
				K = C && C.plotspacepercent,
				C = O(C && C.plotpaddingpercent),
				R = q.groupPadding,
				n = q.maxColWidth,
				K = (1 - K * 0.01) * N || ma(N * (1 - R * 2), n * M),
				N = K / 2;
			K /= M;
			var Q = ma(K - 1, M > 1 ? !Q && C === J ? 4 : C > 0 ? K * C / 100 : 0 : 0),
				M = K - Q,
				W = W * K - N + Q / 2,
				S = G.max,
				oa = G.min,
				Q = S < 0 && oa < 0 ? S : S > 0 && oa > 0 ? oa : 0,
				C = G.getAxisPosition(Q),
				N = O(v.useRoundEdges, 0),
				Y = d.canvasTop,
				K = d.canvasLeft,
				fa = d.canvasHeight,
				R = d.canvasRight,
				U, aa, V, Z, X, ca, L, $, ia, n = d.layers;
			X = n.dataset = n.dataset || k.group("dataset-orphan");
			var sa = n.datalabels = n.datalabels || k.group("datalabels").insertAfter(X),
				n = n.tracker,
				pa, ta, va, Ca, ua;
			sa.hide();
			if (I) Ca = X.shadows || (X.shadows = k.group("shadows", X).toBack());
			L = X.column = X.column || k.group("bars", X);
			if (F) {
				U = v.xDepth || 0;
				aa = v.yDepth || 0;
				X = L.negative = L.negative || k.group("negative-values", L);
				ca = L.column = L.column || k.group("positive-values", L);
				ta = L.zeroPlane;
				if (!ta && oa < 0 && S >= 0) ta = L.zeroPlane = k.group("zero-plane", L).insertBefore(ca), ia = v.zeroPlaneColor, u.zeroplane = k.cubepath(C - U, Y + aa, 1, fa, U, aa, ta).attr({
					fill: [ia, !y],
					stroke: "none",
					"stroke-width": 0
				});
				if (!(ta = X.data("categoryplots"))) X.data("categoryplots", Array(g)), ta = X.data("categoryplots");
				if (!(ia = ca.data("categoryplots"))) ca.data("categoryplots", Array(g)), ia = ca.data("categoryplots");
				for (u = 0; u < g; u += 1) ta[u] = ta[u] || k.group(X), ia[u] = ia[u] || k.group(ca)
			} else L.attrs["clip-rect"] || L.attr({
				"clip-rect": u["clip-canvas"]
			}), va = L;
			u = 0;
			for (Y = g - 1; u < g; u += 1, Y -= 1) {
				fa = e[u];
				S = fa.y;
				pa = oa = null;
				if (S === null) {
					if (Z = h[u]) pa = Z.graphic, F || pa.attr({
						width: 0
					})
				} else {
					L = O(fa.x, u);
					X = fa.link;
					B = fa.toolText;
					ca = s(fa.borderWidth) || 0;
					L = D.getAxisPosition(L) + W;
					V = fa.previousY;
					Z = G.getAxisPosition(V || Q);
					$ = G.getAxisPosition(S + (V || 0));
					V = Aa($ - Z);
					S > 0 && ($ = Z);
					if (F) {
						va = S < 0 ? ta : ia;
						if (!(Z = h[u])) Z = h[u] = {
							index: u,
							value: S,
							graphic: k.cubepath(va[Y]),
							dataLabel: null,
							tracker: null
						};
						pa = Z.graphic;
						pa.attr({
							cubepath: [C - U, L + aa, 0, M, U, aa],
							fill: [f(fa.color), !y],
							stroke: ca && f(fa.borderColor) || "NONE",
							"stroke-width": ca,
							"stroke-dasharray": fa.dashStyle,
							cursor: X ? "pointer" : "",
							visibility: o
						}).shadow(q.shadow && fa.shadow, Ca).animate({
							cubepath: [$ - U, L + aa, V, M, U, aa]
						}, l, "normal", function() {
							sa.show()
						}).data("BBox", {
							height: M,
							width: V,
							x: $,
							y: L
						});
						if (X || w) {
							!I && V < i && ($ -= (i - V) / 2, V = i);
							if (!Z.tracker) Z.tracker = k.cubepath(n);
							oa = Z.tracker;
							oa.attr({
								cubepath: [$ - U, L + aa, V, M, U, aa],
								cursor: X ? "pointer" : "",
								stroke: ca && r || "NONE",
								"stroke-width": ca,
								fill: r,
								ishot: !! X
							}).click(function() {
								var a = this.data("link");
								a && d.linkClickFN.call({
									link: a
								}, d)
							}).tooltip(B).data("link", X);
							oa._.cubetop.click(function() {
								var a = this.data("link");
								a && d.linkClickFN.call({
									link: a
								}, d)
							}).tooltip(B).data("link", X);
							oa._.cubeside.click(function() {
								var a = this.data("link");
								a && d.linkClickFN.call({
									link: a
								}, d)
							}).tooltip(B).data("link", X)
						}
						if (!I || I && S < 0) pa.toBack(), oa && oa.toBack()
					} else {
						ka($) <= K && (V += $, $ = K - ca / 2 + + !! ca - +E, v.xAxisLineVisible && !E && ($ -= 1), V -= $);
						x($ + V) >= R && ($ -= ca / 2 + +!ca, V = R - $ + ca / 2 - + !! ca + +E);
						ua = m.crispBound($, L, V, M, ca);
						$ = ua.x;
						L = ua.y;
						V = ua.width;
						M = ua.height;
						V <= 1 && (V = 1, $ += S < 0 ? -V : 0);
						if (!(Z = h[u])) Z = h[u] = {
							index: u,
							value: S,
							height: M,
							graphic: k.rect(va),
							dataLabel: null,
							tracker: null
						};
						pa = Z.graphic;
						pa.attr({
							x: C,
							y: L,
							width: 0,
							height: M,
							r: N,
							fill: f(fa.color),
							stroke: f(fa.borderColor),
							"stroke-width": ca,
							"stroke-dasharray": fa.dashStyle,
							"stroke-linejoin": "miter",
							cursor: X ? "pointer" : "",
							visibility: o
						}).shadow(q.shadow && fa.shadow, Ca).animate({
							x: $,
							width: V || 1
						}, l, "normal", function() {
							sa.show()
						}).data("BBox", ua);
						if (X || w) {
							!I && V < i && ($ -= (i - V) / 2, V = i);
							if (!Z.tracker) Z.tracker = k.rect(n);
							oa = Z.tracker;
							oa.attr({
								x: $,
								y: L,
								width: V,
								height: M,
								r: N,
								cursor: X ? "pointer" : "",
								stroke: r,
								"stroke-width": ca,
								fill: r,
								ishot: !! X
							}).click(function() {
								var a = this.data("link");
								a && d.linkClickFN.call({
									link: a
								}, d)
							}).tooltip(B).data("link", X)
						}
					}
					B = d.drawPlotBarLabel(a, b, u, $, L)
				}
				B && j.push(B);
				pa && j.push(pa);
				oa && j.push(oa);
				d.drawTracker && d.drawTracker.call(d, a, b, u)
			}
			a.visible = b.visible !== !1;
			return a
		},
		drawPlotBarLabel: function(a, b, c, d, e, f) {
			var g = this.options,
				h = this.logic,
				i = g.chart,
				j = this.paper,
				k = this.layers,
				g = g.plotOptions.series.dataLabels.style,
				l = this.canvasLeft,
				n = this.canvasWidth,
				m = a.data[c],
				s = a.items[c],
				q = i.valuePadding + 2,
				r = s.graphic,
				a = s.dataLabel,
				c = m.y < 0,
				u = h.isStacked,
				h = h.is3D,
				v = i.xDepth || 0,
				w = i.yDepth || 0,
				x = m.displayValue,
				b = b.visible === !1 ? "hidden" : "visible",
				i = i.placeValuesInside,
				y = !1,
				f = f || k.datalabels;
			if (N(x) && x !== B && m.y !== null) {
				if (!a) a = s.dataLabel = j.text(), y = !0;
				a.attr({
					text: x,
					title: m.originalText || "",
					fill: g.color
				}).css(g);
				j = a.getBBox();
				k = r.data("BBox");
				r = k.height;
				m = k.width;
				k = s = j.width;
				k += q;
				e += r * 0.5;
				r = d + (c ? 0 : m);
				q = s * 0.5 + q;
				d = c ? d - l : l + n - (d + m);
				u ? (r += (c ? m : -m) * 0.5, r -= h ? v : 0, e += h ? w : 0) : (i ? m >= k ? (r += c ? q : -q, h && (r -= v, e += w)) : (r += c ? -q : q, h && c && (r -= v)) : d >= k ? (r += c ? -q : q, h && c && (r -= v, e += v)) : (r += c ? q : -q, h && (r -= v, e += w)), r > l + n && (r = l + n - j.width * 0.5 - 4), r < l && (r = l + j.width * 0.5 + 4));
				a.attr({
					x: r,
					y: e,
					visibility: b
				});
				y && f.appendChild(a);
				y && Y(g.backgroundColor, g.borderColor) !== B && a.attr({
					"text-bound": [g.backgroundColor, g.borderColor, 1, 2]
				})
			} else a && a.attr({
				text: B
			});
			return a
		},
		drawPlotBar3d: function(a, b) {
			this.drawPlotBar.call(this, a, b)
		},
		drawPlotLine: function(a, b) {
			var p;
			var e = this,
				g = e.paper,
				h = e.elements,
				j = e.options,
				k = j.chart,
				l = e.logic,
				n = j.plotOptions.series,
				q = a.items,
				u = a.graphics = a.graphics || [],
				v, w = e.xAxis[b.xAxis || 0],
				x = e.yAxis[b.yAxis || 0],
				C = l.multisetRealtime || l.dragExtended,
				B = l.isWaterfall,
				D, G, F;
			D = 0;
			var I = (j.tooltip || {}).enabled !== !1,
				E, j = isNaN(+n.animation) && n.animation.duration || n.animation * 1E3,
				y = k.xDepth || 0,
				J = k.yDepth || 0,
				M = k.series2D3Dshift,
				l = e.logic,
				o = b.step,
				Q = b.drawVerticalJoins,
				N = b.useForwardSteps;
			c(l.name);
			var l = a.data,
				K = b.visible === !1 ? "hidden" : "visible",
				R, S = l.length,
				fa = w.getAxisPosition(0);
			v = w.getAxisPosition(1) - fa;
			var fa = v * S,
				ka = w.axisData.scroll || {},
				k = k.hasScroll || !1,
				Y = n.connectNullData,
				V, U, X, Z, aa, $, L = null,
				ca = n.connectorWidth = s(b.lineWidth),
				ia = b.color;
			n.connectorOpacity = m.color(ia).opacity;
			var pa, sa, ta = n.connectorDashStyle = b.dashStyle,
				va, Aa, ua, xa = e.layers;
			R = xa.dataset = xa.dataset || g.group("dataset-orphan");
			var Ca = xa.datalabels = xa.datalabels || g.group("datalabels").insertAfter(R),
				Ja = xa.tracker,
				xa = h["clip-canvas-init"].slice(0),
				h = h["clip-canvas"].slice(0),
				ya = x.axisData.reversed;
			G = x.max;
			F = x.min;
			G = x.getAxisPosition(G > 0 && F > 0 ? !ya ? F : G : G < 0 && F < 0 ? !ya ? G : F : !ya ? 0 : G) + (M ? J : 0);
			var ya = [],
				Fa, Ka, Ha, Da;
			x.yBasePos = G;
			if (B) D = (D = e.definition.chart) && D.plotspacepercent, G = n.groupPadding, F = n.maxColWidth, D = (1 - D * 0.01) * v || ma(v * (1 - G * 2), F * 1), D /= 2;
			Ca.hide();
			Fa = R.line || (R.line = g.group("line-connector", R));
			Ka = R.anchors || (R.anchors = g.group("line-anchors", R));
			Ka.hide();
			Ha = R.anchorShadows || (R.anchorShadows = g.group("anchor-shadows", R).toBack());
			Ha.hide();
			for (R = 0; R < S; R += 1) {
				V = l[R];
				aa = V.y;
				X = V.previousY || 0;
				E = V.toolText;
				F = U = Aa = G = null;
				v = q[R] = {
					index: R,
					value: null,
					graphic: null,
					connector: null,
					dataLabel: null,
					shadowGroup: Ha,
					tracker: null
				};
				if (aa === null) Y === 0 && (L = null);
				else {
					Z = O(V.x, R);
					U = V.link;
					b.relatedSeries === "boxandwhisker" && b.pointStart && (Z += b.pointStart);
					aa = x.getAxisPosition(aa + X) + (M ? J : 0);
					Z = w.getAxisPosition(Z) - y;
					Z = d(Z, ca, ca).position;
					aa = d(aa, ca, ca).position;
					if ((va = V.marker) && va.enabled) if (Aa = va.symbol.split("_"), ua = Aa[0] === "spoke" ? 1 : 0, X = va.radius, p = v.graphic = g.polypath(Aa[1] || 2, Z, aa, X, va.startAngle, ua, Ka).attr({
						fill: f(va.fillColor),
						"stroke-width": va.lineWidth,
						stroke: f(va.lineColor),
						cursor: U ? "pointer" : "",
						visibility: K
					}), Aa = p, U || I) X < i && (X = i), G = v.tracker = g.circle(Z, aa, X, Ja).attr({
						cursor: U ? "pointer" : "",
						stroke: r,
						"stroke-width": va.lineWidth,
						fill: r,
						ishot: !! U,
						visibility: K
					}).click(function() {
						var a = this.data("link");
						a && e.linkClickFN.call({
							link: a
						}, e)
					}).tooltip(E).data("link", U);
					Da = Da !== [f(V.color || ia), V.dashStyle || ta].join(":");
					if (L !== null) {
						if ((C || B || !ya.join("")) && ya.push("M", $, L), B && ya.push("m", -D, 0), o ? N ? (ya.push("H", Z), B && ya.push("h", D), Q ? ya.push("V", aa) : ya.push("m", 0, aa - L)) : (Q && ya.push("V", aa), ya.push("M", $, aa, "H", Z)) : ya.push("L", Z, aa), C || Da) F = v.connector = g.path(ya, Fa).attr({
							"stroke-dasharray": sa,
							"stroke-width": ca,
							stroke: pa,
							"stroke-linecap": "round",
							"stroke-linejoin": ca > 2 ? "round" : "miter",
							visibility: K
						}).shadow(n.shadow && V.shadow), ya = []
					} else!C && ya.push("M", Z, aa);
					U = v.dataLabel = e.drawPlotLineLabel(a, b, R, Z, aa);
					$ = Z;
					L = aa;
					pa = f(V.color || ia);
					sa = V.dashStyle || ta;
					Da = [pa, sa].join(":")
				}
				U && u.push(U);
				Aa && u.push(Aa);
				F && u.push(F);
				G && u.push(G);
				e.drawTracker && e.drawTracker.call(e, a, b, R)
			}!C && ya.join("") && (F = g.path(ya, Fa).attr({
				"stroke-dasharray": sa,
				"stroke-width": ca,
				stroke: pa,
				"stroke-linecap": "round",
				"stroke-linejoin": ca > 2 ? "round" : "miter",
				visibility: K
			}).shadow(n.shadow && V.shadow)) && u.push(F);
			if (k) g = ka.startPercent, h[2] = fa + xa[0], g === 1 && (xa[0] = h[2], h[0] = 0);
			g = m.animation({
				"clip-rect": h
			}, j, k ? "easeIn" : "normal", function() {
				Fa.attr({
					"clip-rect": null
				});
				Ha.show();
				Ka.show();
				Ca.show()
			});
			Fa.attr({
				"clip-rect": xa
			}).animate(B ? g.delay(j) : g);
			a.visible = b.visible !== !1;
			return a
		},
		drawPlotArea: function(a, b) {
			var p;
			var c = this,
				d = c.paper,
				e = c.options,
				g = e.chart,
				h = c.logic,
				j = e.plotOptions.series,
				k = c.elements,
				l = a.items,
				n = a.graphics = a.graphics || [],
				m = c.xAxis[b.xAxis || 0],
				s = c.yAxis[b.yAxis || 0],
				q = s.axisData.reversed,
				u = g.xDepth || 0,
				v = g.yDepth || 0,
				w = h.isStacked,
				x = (e.tooltip || {}).enabled !== !1,
				C, e = isNaN(+j.animation) && j.animation.duration || j.animation * 1E3,
				B = g.series2D3Dshift,
				h = c.definition.chart.drawfullareaborder === "0",
				E = a.data,
				y = b.visible === !1 ? "hidden" : "visible",
				D, G = E.length,
				o = m.getAxisPosition(0),
				o = (m.getAxisPosition(1) - o) * G,
				F = m.axisData.scroll || {},
				g = g.hasScroll || !1,
				J = j.connectNullData,
				M, N, K, R, S, fa = s.max,
				V = s.min,
				U = s.getAxisPosition(fa > 0 && V < 0 ? 0 : !q && fa > 0 && V >= 0 ? V : fa) + (B ? v : 0),
				ka = null,
				X, Y, q = b.lineWidth,
				fa = b.dashStyle,
				aa = f(b.fillColor),
				V = f(b.lineColor),
				L = 0,
				Z, ca, $, ia = [],
				ma = [],
				pa = null,
				sa = [],
				ua = c.layers;
			D = ua.dataset = ua.dataset || d.group("dataset-orphan");
			var ta = ua.datalabels = ua.datalabels || d.group("datalabels").insertAfter(D),
				pa = ua.tracker,
				ua = k["clip-canvas-init"].slice(0),
				k = k["clip-canvas"].slice(0),
				va, Aa, ya, Ca, Fa;
			s.yBasePos = U;
			ta.hide();
			if (w) Ca = D.shadows || (D.shadows = d.group("shadows", D).toBack());
			Aa = D.area = D.area || d.group("area", D);
			va = D.arealine = D.arealine || d.group("area-connector", D);
			ya = D.areaanchors = D.areaanchors || d.group("area-anchors", D);
			ya.hide();
			for (D = 0; D < G; D += 1) {
				M = E[D];
				R = M.y;
				C = O(M.x, D);
				X = m.getAxisPosition(C) - u;
				ca = Fa = $ = null;
				if (R === null) J === 0 && (ka = null, L > 0 && (L === 1 ? ia.splice(-8, 8) : (ia = ia.concat(ma), ia.push("Z")), ma = [])), l[D] = {
					chart: c,
					index: D,
					value: R
				};
				else {
					N = M.link;
					C = M.toolText;
					K = M.previousY;
					S = (S = s.getAxisPosition(K) || null) || U;
					Y = s.getAxisPosition(R + (K || 0)) + (B ? v : 0);
					if ((Z = M.marker) && Z.enabled) if (ca = Z.symbol.split("_"), K = Z.radius, ca = d.polypath(ca[1] || 2, X, Y, K, Z.startAngle, 0, ya).attr({
						fill: f(Z.fillColor),
						"stroke-width": Z.lineWidth,
						stroke: f(Z.lineColor),
						cursor: N ? "pointer" : "",
						visibility: y
					}), N || x)!w && K < i && (K = i), $ = d.circle(X, Y, K, pa).attr({
						cursor: N ? "pointer" : "",
						stroke: r,
						"stroke-width": Z.lineWidth,
						fill: r,
						ishot: !! N,
						visibility: y
					}).click(function() {
						var a = this.data("link");
						a && c.linkClickFN.call({
							link: a
						}, c)
					}).tooltip(C).data("link", N);
					ka === null ? (sa.push("M", X, ",", Y), ia.push("M", X, ",", S), L = 0) : sa.push("L", X, ",", Y);
					ia.push("L", X, ",", Y);
					ma.unshift("L", X, ",", S);
					L++;
					ka = Y;
					l[D] = {
						chart: c,
						index: D,
						value: R,
						graphic: ca,
						dataLabel: Fa,
						tracker: $
					};
					Fa = c.drawPlotLineLabel(a, b, D, X, Y)
				}
				Fa && n.push(Fa);
				ca && n.push(ca);
				$ && n.push($);
				c.drawTracker && c.drawTracker.call(c, a, b, D)
			}
			L > 0 && (L === 1 ? ia.splice(-8, 8) : (ia = ia.concat(ma), ia.push("Z")));
			(pa = a.graphic = d.path(ia, Aa).attr({
				fill: aa,
				"stroke-dasharray": fa,
				"stroke-width": h ? 0 : q,
				stroke: V,
				"stroke-linecap": "round",
				"stroke-linejoin": q > 2 ? "round" : "miter",
				visibility: y
			}).shadow(j.shadow && M.shadow, Ca)) && n.push(pa);
			if (g) j = F.startPercent, k[2] = o + ua[0], j === 1 && (ua[0] = k[2], k[0] = 0);
			j = Aa.attr({
				"clip-rect": ua
			}).animate({
				"clip-rect": k
			}, e, g ? "easeIn" : "normal", function() {
				Aa.attr({
					"clip-rect": null
				});
				ya.show();
				ta.show()
			});
			Ca && Ca.attr({
				"clip-rect": ua
			}).animateWith(Aa, j, {
				"clip-rect": k
			}, e, g ? "easeIn" : "normal", function() {
				Ca.attr({
					"clip-rect": null
				})
			});
			if (h) p = a.connector = d.path(sa, va).attr({
				"stroke-dasharray": fa,
				"stroke-width": q,
				stroke: V,
				"stroke-linecap": "round",
				"stroke-linejoin": q > 2 ? "round" : "miter",
				visibility: y
			}), d = p, va.attr({
				"clip-rect": ua
			}).animateWith(Aa, j, {
				"clip-rect": k
			}, e, g ? "easeIn" : "normal", function() {
				va.attr({
					"clip-rect": null
				})
			}), d && n.push(d);
			a.visible = b.visible !== !1;
			return a
		},
		drawPlotScatter: function(a, b) {
			var p;
			var c = this,
				d = c.options,
				e = d.plotOptions.series,
				g = c.paper,
				h = c.elements,
				j = a.items,
				k = a.graphics = a.graphics || [],
				l = c.xAxis[b.xAxis || 0],
				n = c.yAxis[b.yAxis || 0],
				m = a.data,
				s = b.visible === !1 ? "hidden" : "visible",
				q = (d.tooltip || {}).enabled !== !1,
				u, d = isNaN(+e.animation) && e.animation.duration || e.animation * 1E3,
				v, w, x, C, B, E, y, D, G, o = b.lineWidth,
				F = o > 0,
				J = b.color,
				M = b.dashStyle,
				O = e.connectNullData,
				K = [],
				N, R, S, fa, V = c.layers,
				X = V.dataset || (V.dataset = g.group("dataset-orphan")),
				U = V.datalabels || (V.datalabels = g.group("datalabels").insertAfter(X)),
				ka = V.tracker,
				Y;
			U.hide();
			V = X.line || (X.line = g.group("connector", X));
			X = X.anchor || (X.anchor = g.group("anchor", X));
			v = 0;
			for (w = m.length; v < w; v += 1) {
				x = m[v];
				N = x.marker;
				D = G = S = Y = fa = null;
				E = x.y;
				B = x.x;
				if (E !== null && B !== null) {
					if (N && N.enabled && (C = x.link, u = x.toolText, R = N.radius, G = n.getAxisPosition(E), D = l.getAxisPosition(B), S = N.symbol.split("_"), S = g.polypath(S[1] || 2, D, G, R, N.startAngle, 0, X).attr({
						fill: f(N.fillColor),
						"stroke-width": N.lineWidth,
						stroke: f(N.lineColor),
						cursor: C ? "pointer" : "",
						visibility: s
					}).shadow(e.shadow && x.shadow), C || q)) R < i && (R = i), fa = g.circle(D, G, R, ka).attr({
						cursor: C ? "pointer" : "",
						stroke: r,
						"stroke-width": N.lineWidth,
						fill: r,
						ishot: !! C
					}).tooltip(u).data("link", C).click(function() {
						var a = this.data("link");
						a && c.linkClickFN.call({
							link: a
						}, c)
					});
					F && ((y === void 0 || y === null && O === 0) && D && G && K.push("M", D, ",", G), D && G && K.push("L", D, ",", G), y = G);
					j[v] = {
						index: v,
						x: B,
						y: E,
						value: E,
						graphic: S,
						dataLabel: Y,
						tracker: fa
					};
					Y = c.drawPlotLineLabel(a, b, v, D, G)
				} else F && O === 0 && (y = null), j[v] = {
					chart: c,
					index: v,
					x: B,
					y: E
				};
				Y && k.push(Y);
				S && k.push(S);
				fa && k.push(fa);
				c.drawTracker && c.drawTracker.call(c, a, b, v)
			}
			if (K.length) p = a.graphic = g.path(K, V).attr({
				"stroke-dasharray": M,
				"stroke-width": o,
				stroke: J,
				"stroke-linecap": "round",
				"stroke-linejoin": o > 2 ? "round" : "miter",
				visibility: s
			}).shadow(e.shadow && x.shadow), e = p, V.attr({
				"clip-rect": h["clip-canvas-init"]
			}).animate({
				"clip-rect": h["clip-canvas"]
			}, d, "normal"), k.push(e);
			X.attr({
				opacity: 0
			}).animate({
				opacity: 1
			}, d, "normal", function() {
				U.show()
			});
			a.visible = b.visible !== !1;
			return a
		},
		drawPlotLineLabel: function(b, c, d, e, f, g) {
			var h = this.options,
				i = h.chart,
				j = this.paper,
				k = this.layers,
				h = h.plotOptions.series.dataLabels.style,
				l = i.rotateValues === 1 ? 270 : 0,
				n = this.canvasHeight,
				m = this.canvasTop,
				s = b.data,
				q = s[d],
				r = b.items[d],
				u = a(q.valuePosition, "auto").toLowerCase(),
				b = this.logic.defaultSeriesType,
				i = i.valuePadding + 2,
				c = c.visible === !1 ? "hidden" : "visible",
				v = !1,
				w = r.dataLabel,
				g = g || k.datalabels;
			switch (u) {
			case "above":
				d = 0;
				break;
			case "below":
				d = 1;
				break;
			default:
				k = s[d - 1] || {}, s = s[d + 1] || {}, d = !d ? 0 : k.y > q.y ? 1 : (k.y == null && s.y) > q.y ? 1 : 0
			}
			k = q.displayValue;
			if (N(k) && k !== B) {
				w ? l && w.rotate(360 - l) : (w = r.dataLabel = j.text().attr({
					text: k
				}).css(h), v = !0);
				w.attr({
					title: q.originalText || "",
					fill: h.color
				});
				j = w.getBBox();
				r = k = l ? j.width : j.height;
				r += i;
				j = f - m;
				n = m + n - f;
				r += 4;
				m = k * 0.5 + i;
				if (!/bubble/i.test(b)) if (d) n > r ? (f += m, q._valueBelowPoint = 1) : j > r && (f -= m);
				else if (j > r) f -= m;
				else if (n > r) f += m, q._valueBelowPoint = 1;
				w.attr({
					x: e,
					y: f,
					visibility: c
				});
				l && w.attr("transform", "T0,0,R" + l);
				v && g.appendChild(w);
				v && Y(h.backgroundColor, h.borderColor) !== B && w.attr({
					"text-bound": [h.backgroundColor, h.borderColor, 1, 2]
				})
			} else w && w.attr({
				text: B
			});
			return w
		},
		drawLabels: function() {
			for (var a = this.paper, b = this.options, c = (b = b.labels && b.labels.items && b.labels.items) && b.length, d = this.layers.layerAboveDataset, e = this.elements.quadran || (this.elements.quadran = []), f = this.canvasTop, g = this.canvasLeft, h = {
				right: "end",
				left: "start",
				undefined: "start"
			}, j, i; c--;) i = b[c], j = i.style, N(i.html) && i.html !== B && (e[c] = a.text(d).attr({
				text: i.html,
				x: parseInt(j.left, 10) + g,
				y: parseInt(j.top, 10) + f,
				fill: j.color,
				"text-anchor": h[i.textAlign],
				"vertical-align": i.vAlign
			}).css(j))
		}
	}, U["renderer.root"]);
	U("renderer.piebase", {
		drawCaption: function() {
			var o;
			var a = this.options.chart,
				b = this.options.title,
				c = this.options.subtitle,
				d = this.paper,
				e = this.elements,
				f = this.layers,
				g = f.caption,
				h = e.caption,
				i = e.subcaption,
				j = b && b.text,
				k = c && c.text,
				l = d.width / 2,
				n = b.x,
				m = c && c.x;
			if ((j || k) && !g) g = f.caption = d.group("caption"), f.tracker ? g.insertBefore(f.tracker) : g.insertAfter(f.dataset);
			if (j) {
				if (!h) h = e.caption = d.text(g);
				if (n === void 0) n = l, b.align = "middle";
				h.css(b.style).attr({
					text: b.text,
					fill: b.style.color,
					x: n,
					y: b.y || a.spacingTop || 0,
					"text-anchor": b.align || "middle",
					"vertical-align": "top",
					visibility: "visible",
					title: b.originalText || ""
				})
			} else if (h) o = e.caption = h.remove(), h = o;
			if (k) {
				if (!i) i = e.subcaption = d.text(g);
				if (m === void 0) m = l, c.align = "middle";
				i.css(c.style).attr({
					text: c.text,
					title: c.originalText || "",
					fill: c.style.color,
					x: m,
					y: j ? h.attrs.y + h.getBBox().height + 2 : b.y || a.spacingTop || 0,
					"text-anchor": c.align || "middle",
					"vertical-align": "top",
					visibility: "visible"
				})
			} else if (i) e.subcaption = i.remove();
			if (!j && !k && g) f.caption = g.remove()
		},
		redrawDataLabels: function(a) {
			var b = a.elements.plots[0];
			a.placeDataLabels(!0, b.items, b);
			return {}
		},
		plotGraphicClick: function() {
			var a = this.graphic && this || this.data("plotItem"),
				b = a.seriesData,
				c = a.chart,
				d, e, f, g, h, i, j;
			if (!b.isRotating && !b.singletonCase) return d = a.graphic, e = a.connector, f = a.dataLabel, b = a.sliced, g = a.slicedTranslation, h = a.connectorPath, i = (b ? -1 : 1) * a.transX, j = (b ? -1 : 1) * a.transY, d.animate({
				transform: b ? "t0,0" : g
			}, 200, "easeIn"), f && f.x && f.animate({
				x: f.x + (b ? 0 : i)
			}, 200, "easeIn"), h && (h[1] += i, h[2] += j, h[4] += i, h[6] += i, e.animate({
				path: h
			}, 200, "easeIn")), b = a.sliced = !b, a = {
				hcJSON: {
					series: []
				}
			}, a.hcJSON.series[0] = {
				data: []
			}, G(c.logic.chartInstance.jsVars._reflowData, a, !0), b
		},
		plotDragStart: function(a, b, c) {
			var d = this.data("plotItem"),
				e = d.chart,
				d = d.seriesData;
			if (e.options.series[0].enableRotation) a = Za.call(c, a, b, d.pieCenter, d.chartPosition), d.dragStartAngle = a, e._pierotateActive = !0
		},
		plotDragEnd: function() {
			var a = this.data("plotItem"),
				b = a.chart,
				c = {
					hcJSON: {
						series: [{
							startAngle: -b.datasets[0].startAngle * 180 / Va
						}]
					}
				};
			b.disposed || (G(b.logic.chartInstance.jsVars._reflowData, c, !0), b.rotate(a.seriesData, b.options.series[0]));
			setTimeout(function() {
				a.seriesData.isRotating = !1
			}, 0)
		},
		plotDragMove: function(a, b, c, d, e) {
			var a = this.data("plotItem"),
				f = a.chart,
				g = a.seriesData,
				h = f.options.series;
			if (h[0].enableRotation && !g.singletonCase && (g.isRotating = !0, c = Za.call(e, c, d, g.pieCenter, g.chartPosition), h[0].startAngle += c - g.dragStartAngle, g.dragStartAngle = c, g.moveDuration = 0, c = (new Date).getTime(), !g._lastTime || g._lastTime + g.timerThreshold < c)) setTimeout(function() {
				f.rotate(g, h[0])
			}, 0), g._lastTime = c
		},
		plotMouseDown: function() {
			this.data("plotItem").seriesData.isRotating = !1
		},
		plotMouseUp: function() {
			var a = this.data("plotItem"),
				b = a.chart,
				c = a.seriesData;
			!c.isRotating && b.linkClickFN.call({
				link: c.data[a.index].link
			}, b);
			m._supportsTouch && !c.isRotating && b.plotGraphicClick.call(a)
		},
		legendClick: function(a, b, c) {
			var d = a.chart;
			d.elements.plots[0].isRotating = !1;
			d.plotGraphicClick.call(a.graphic);
			c !== !0 && (eventArgs = {
				datasetName: a.label,
				datasetIndex: a.originalIndex,
				id: a.userID,
				visible: b,
				label: a.label,
				value: a.value,
				percentValue: a.percentage,
				tooltext: a.toolText,
				link: a.link,
				sliced: !a.sliced
			}, g.raiseEvent("legenditemclicked", eventArgs, d.logic.chartInstance))
		},
		placeDataLabels: function() {
			var a = function(a, b) {
					return a.point.value - b.point.value
				},
				b = function(a, b) {
					return a.angle - b.angle
				},
				c = ["start", "start", "end", "end"],
				d = [-1, 1, 1, -1],
				e = [1, 1, -1, -1];
			return function(f, g, h, i) {
				var j = this.options.plotOptions,
					l = j.pie,
					n = this.canvasLeft + this.canvasWidth * 0.5,
					m = this.canvasTop + this.canvasHeight * 0.5,
					s = this.smartLabel,
					q = j.series.dataLabels,
					r = q.style,
					j = O(Ka(parseFloat(r.lineHeight)), 12),
					u = k(q.placeInside, !1),
					v = q.skipOverlapLabels,
					w = q.manageLabelOverflow,
					x = q.connectorPadding,
					C = q.distance;
				k(q.softConnector, !0);
				var y = i && i.metrics || [n, m, l.size, l.innerSize || 0],
					D = y[1],
					B = y[0],
					i = y[2] * 0.5,
					o = [
						[],
						[],
						[],
						[]
					],
					G = this.canvasLeft,
					F = this.canvasTop,
					l = this.canvasWidth,
					C = h.labelsRadius || (h.labelsRadius = i + C),
					m = n = parseInt(r.fontSize, 10),
					J = m / 2,
					x = [x, x, -x, -x],
					h = h.labelsMaxInQuadrant || (h.labelsMaxInQuadrant = bb(C / m)),
					q = q.isSmartLineSlanted,
					y = y[3] / 2,
					M, N, K, S, V, fa, Y, U, ka, Z, aa, L, ca, $, ia;
				f || s.setStyle(r);
				if (g.length == 1 && !y) {
					if (y = g[0], ($ = y.dataLabel) && $.show(), y.slicedTranslation = [G, F], $) $.attr({
						visibility: Ja,
						align: "middle",
						transform: "t" + B + "," + (D + J - 2)
					}), $.x = B
				} else if (u) {
					var Ca = y + (i - y) / 2;
					R(g, function(a) {
						($ = a.dataLabel) && $.show();
						if ($) {
							var b = a.angle;
							aa = D + Ca * va(b) + J - 2;
							Y = B + Ca * X(b);
							$.x = Y;
							$._x = Y;
							$.y = aa;
							if (a.sliced) a = a.slicedTranslation, b = a[1] - F, Y += a[0] - G, aa += b;
							$.attr({
								visibility: Ja,
								align: "middle",
								transform: "t" + Y + "," + aa
							})
						}
					})
				} else {
					R(g, function(a) {
						($ = a.dataLabel) && $.show();
						$ && (L = a.angle % sa, L < 0 && (L = sa + L), ia = L >= 0 && L < Ra ? 1 : L < Va ? 2 : L < Ma ? 3 : 0, o[ia].push({
							point: a,
							angle: L
						}))
					});
					for (g = f = 4; g--;) {
						if (v && (r = o[g].length - h, r > 0)) {
							o[g].sort(a);
							u = o[g].splice(0, r);
							r = 0;
							for (S = u.length; r < S; r += 1) y = u[r].point, y.dataLabel.attr({
								visibility: "hidden"
							}), y.connector && y.connector.attr({
								visibility: "hidden"
							})
						}
						o[g].sort(b)
					}
					g = ta(o[0].length, o[1].length, o[2].length, o[3].length);
					ca = ta(ma(g, h) * m, C + m);
					o[1].reverse();
					for (o[3].reverse(); f--;) {
						u = o[f];
						S = u.length;
						v || (m = S > h ? ca / S : n, J = m / 2);
						y = S * m;
						r = ca;
						for (g = 0; g < S; g += 1, y -= m) K = Aa(ca * va(u[g].angle)), r < K ? K = r : K < y && (K = y), r = (u[g].oriY = K) - m;
						M = c[f];
						S = ca - (S - 1) * m;
						r = 0;
						for (g = u.length - 1; g >= 0; g -= 1, S += m) {
							y = u[g].point;
							L = u[g].angle;
							V = y.sliced;
							$ = y.dataLabel;
							K = Aa(ca * va(L));
							K < r ? K = r : K > S && (K = S);
							r = K + m;
							ka = (K + u[g].oriY) / 2;
							K = B + e[f] * C * X(pa.asin(ka / ca));
							ka *= d[f];
							ka += D;
							Z = D + i * va(L);
							fa = B + i * X(L);
							(f < 2 && K < fa || f > 1 && K > fa) && (K = fa);
							Y = K + x[f];
							aa = ka - J - 2;
							U = Y + x[f];
							$.x = U;
							$._x = U;
							w && (N = f > 1 ? U - this.canvasLeft : this.canvasLeft + l - U, N = s.getSmartText(y.labelText, N, j), $.attr({
								text: N.text,
								title: N.tooltext || ""
							}));
							$.y = aa;
							if (V) V = y.transX, N = y.transY, Y += V, K += V, fa += V, Z += N, U += V;
							$.attr({
								visibility: Ja,
								"text-anchor": M,
								vAlign: "middle",
								x: U,
								y: ka
							});
							if (U = y.connector) y.connectorPath = y = ["M", fa, Z, "L", q ? K : fa, ka, Y, ka], U.attr({
								path: y,
								visibility: Ja
							})
						}
					}
				}
			}
		}()
	}, U["renderer.root"])
}]);
(function() {
	var g = FusionCharts(["private", "modules.renderer.js-interface"]);
	if (g !== void 0) {
		var h = g.hcLib,
			m = g.renderer.getRenderer("javascript"),
			U = h.hasModule,
			w = h.loadModule,
			S = h.moduleCmdQueue,
			ia = h.executeWaitingCommands,
			b = h.injectModuleDependency,
			B = h.moduleDependencies,
			e = h.getDependentModuleName,
			r = h.eventList = {
				loaded: "FC_Loaded",
				dataloaded: "FC_DataLoaded",
				rendered: "FC_Rendered",
				drawcomplete: "FC_DrawComplete",
				resized: "FC_Resized",
				dataxmlinvalid: "FC_DataXMLInvalid",
				nodatatodisplay: "FC_NoDataToDisplay",
				exported: "FC_Exported"
			};
		h.raiseEvent = function(b, e, h, m, w, x) {
			var B = r[b];
			g.raiseEvent(b, e, h, w, x);
			B && typeof window[B] === "function" && setTimeout(function() {
				window[B].apply(window, m)
			}, 0)
		};
		var x = function(b) {
				var m, r, w, x = {},
					B;
				for (m in g.core.items) if (m = g.core.items[m], w = m.chartType(), (r = m.jsVars) && r.waitingModule && m.__state.rendering && h.needsModule(b, w)) r.waitingModuleError = !0, r = e(w).concat(r.userModules), r.length && (r = r[r.length - 1], x[r] = h.moduleCmdQueue[r]);
				for (B in x) ia(x[B]);
				g.raiseError(g.core, "11171116151", "run", "HC-interface~renderer.load", "Unable to load required modules and resources: " + b)
			},
			$ = function(b, e, h) {
				g.hcLib.createChart(b, e, "stub", h, b.jsVars.msgStore.ChartNotSupported)
			};
		B.charts = g.extend(B.charts || {}, {
			column2d: 0,
			column3d: 0,
			bar2d: 0,
			bar3d: 0,
			pie2d: 0,
			pie3d: 0,
			line: 0,
			bar2d: 0,
			area2d: 0,
			doughnut2d: 0,
			doughnut3d: 0,
			pareto2d: 0,
			pareto3d: 0,
			mscolumn2d: 0,
			mscolumn3d: 0,
			msline: 0,
			msarea: 0,
			msbar2d: 0,
			msbar3d: 0,
			stackedcolumn2d: 0,
			marimekko: 0,
			stackedcolumn3d: 0,
			stackedarea2d: 0,
			stackedcolumn2dline: 0,
			stackedcolumn3dline: 0,
			stackedbar2d: 0,
			stackedbar3d: 0,
			msstackedcolumn2d: 0,
			mscombi2d: 0,
			mscombi3d: 0,
			mscolumnline3d: 0,
			mscombidy2d: 0,
			mscolumn3dlinedy: 0,
			stackedcolumn3dlinedy: 0,
			msstackedcolumn2dlinedy: 0,
			scatter: 0,
			bubble: 0,
			ssgrid: 0,
			scrollcolumn2d: 0,
			scrollcolumn3d: 0,
			scrollline2d: 0,
			scrollarea2d: 0,
			scrollstackedcolumn2d: 0,
			scrollcombi2d: 0,
			scrollcombidy2d: 0,
			zoomline: 0
		});
		B.powercharts = g.extend(B.powercharts || {}, {
			spline: 0,
			splinearea: 0,
			msspline: 0,
			mssplinearea: 0,
			multiaxisline: 0,
			multilevelpie: 0,
			waterfall2d: 0,
			msstepline: 0,
			inversemsline: 0,
			inversemscolumn2d: 0,
			inversemsarea: 0,
			errorbar2d: 0,
			errorscatter: 0,
			errorline: 0,
			logmsline: 0,
			logmscolumn2d: 0,
			radar: 0,
			dragnode: 0,
			candlestick: 0,
			selectscatter: 0,
			dragcolumn2d: 0,
			dragline: 0,
			dragarea: 0,
			boxandwhisker2d: 0,
			kagi: 0,
			heatmap: 0
		});
		B.widgets = g.extend(B.widgets || {}, {
			angulargauge: 0,
			bulb: 0,
			cylinder: 0,
			drawingpad: 0,
			funnel: 0,
			hbullet: 0,
			hled: 0,
			hlineargauge: 0,
			vlineargauge: 0,
			pyramid: 0,
			realtimearea: 0,
			realtimecolumn: 0,
			realtimeline: 0,
			realtimelinedy: 0,
			realtimestackedarea: 0,
			realtimestackedcolumn: 0,
			sparkcolumn: 0,
			sparkline: 0,
			sparkwinloss: 0,
			thermometer: 0,
			vbullet: 0,
			gantt: 0,
			vled: 0
		});
		B.maps = g.extend(B.maps || {}, {});
		g.extend(m, {
			render: function(r, s) {
				var w = this.chartType(),
					x = this.jsVars,
					B = this.__state,
					K = h.chartAPI,
					Y;
				Y = e(w).concat(x.userModules);
				if (x.isResizing) x.isResizing = clearTimeout(x.isResizing);
				x.hcObj && x.hcObj.destroy && x.hcObj.destroy();
				if (K[w]) {
					if (K[B.lastRenderedType] && B.lastRenderedType !== w) for (var O in K[B.lastRenderedType].eiMethods) delete this[O];
					B.lastRenderedType = w;
					B.lastRenderedSrc = this.src;
					delete x.waitingModule;
					delete x.waitingModuleError;
					delete x.drLoadAttempted;
					g.hcLib.createChart(this, r, w, s)
				} else {
					if (U(Y)) if (x.drLoadAttempted) {
						g.raiseError(this, 11112822001, "run", "HC-interface~renderer.render", "Chart runtimes not loaded even when resource is present");
						$(this, r, s);
						return
					} else b(w) && (Y = e(w).concat(x.userModules)), x.drLoadAttempted = !0;
					else if (Y.length) {
						if (x.waitingModuleError) {
							$(this, r, s);
							delete x.waitingModule;
							delete x.waitingModuleError;
							return
						}
					} else {
						$(this, r, s);
						return
					}(w = S[Y[Y.length - 1]]) ? (w.push({
						cmd: "render",
						obj: this,
						args: arguments
					}), x.waitingModule || (g.hcLib.createChart(this, r, "stub", void 0, x.msgStore.PBarLoadingText || x.msgStore.LoadingText), m.load.call(this))) : (g.raiseError(this, 12080515551, "run", "HC-interface~renderer.render", "Unregistered module in dependentModule definition."), g.hcLib.createChart(this, r, "stub", void 0, x.msgStore.RenderChartErrorText))
				}
			},
			update: function(b) {
				var e = this.ref,
					h = this.jsVars;
				h.hcObj && h.hcObj.destroy && h.hcObj.destroy();
				if (h.isResizing) h.isResizing = clearTimeout(h.isResizing);
				b.error === void 0 ? (delete h.stallLoad, delete h.loadError, this.isActive() && (this.src !== this.__state.lastRenderedSrc ? this.render() : g.hcLib.createChart(this, h.container, h.type))) : (this.isActive() && typeof e.showChartMessage === "function" && e.showChartMessage("InvalidXMLText"), delete h.loadError)
			},
			resize: function(b) {
				var e = this.ref,
					h, m = this.jsVars;
				if (e && e.resize) {
					if (m.isResizing) m.isResizing = clearTimeout(m.isResizing);
					m.isResizing = setTimeout(function() {
						h = g.normalizeCSSDimension(b.width, b.height, e);
						if (b.width !== void 0) e.style.width = h.width;
						if (b.height !== void 0) e.style.height = h.height;
						e.resize();
						delete m.isResizing
					}, 0)
				}
			},
			dispose: function() {
				var b;
				b = this.jsVars;
				var e = b.hcObj || {};
				if (b.isResizing) b.isResizing = clearTimeout(b.isResizing);
				b.instanceAPI && b.instanceAPI.dispose && b.instanceAPI.dispose();
				if (b = this.ref) g.purgeDOM(b), b.parentNode && b.parentNode.removeChild(b);
				h.cleanupWaitingCommands(this);
				return e && e.destroy && e.destroy()
			},
			load: function() {
				var b = this.jsVars,
					m = this.chartType(),
					r = g.hcLib.chartAPI[m],
					m = e(m).concat(b.userModules),
					B = m[m.length - 1];
				if (r || !m || m && m.length === 0) delete b.waitingModule;
				else if (!b.waitingModule) b.waitingModule = !0, delete b.waitingModuleError, w(m, function() {
					delete b.waitingModule;
					ia(h.moduleCmdQueue[B])
				}, x, this)
			}
		})
	}
})();