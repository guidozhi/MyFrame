//在浏览器外壳下，详情页面中有api的，重新定义api 2016年7月11日 16:16:47
if (typeof parent.IS_SHELL_PAGE_WINDOW_BSFRAME!="undefined") {
	if(api==undefined){
		api={};
		api.close=function(){
			window.open("about:blank","_top").close();
		}
		api.size=function(){
			
		}
		api.position=function(){

		}
	}
}
/**
 * @version 0.1 beta
 * @author ttaomeng@qq.com
 */
function getLength(value){
    if(!value)return 0;
    value=value.toString();
    var re = /[\x00-\xff]/g;
    var len = value.length;
    var array = value.match(re);
    if (array == null) {
        array = "";
    }
    return len * 2 - array.length;
}
function validateGrid(grid) { //校验 required,iskey,maxlength
    var data = grid.getData();
    var columns = grid.get("columns");
    for (var i in columns) {
        var array = [];
        var name = columns[i].name;
        for (var d in data) {
            var value = data[d][name];
            if (columns[i].required) {
                if (value == "" || value == null) {
                    $.ligerDialog.error(columns[i]["display"] + "不能为空！");
                    return false;
                }
            }
            if (columns[i].maxlength) {
                if (value != "" && value != null) {
                    if (getLength(value) > columns[i].maxlength) {
                        $.ligerDialog.error(columns[i]["display"] + "超出最大长度 " + (columns[i].maxlength) + "！");
                        return false;
                    }
                }
            }
            if (columns[i].isKey) {
                for (var a in array) {
                    if (value == array[a]) {
                        $.ligerDialog.error(columns[i]["display"] + "不能重复！");
                        return false;
                    }
                }
                array.push(value);
            }
        }
    }
    return true;
}

function ClsIDCard() {
	this.Valid = false;
	this.ID15 = '';
	this.ID18 = '';
	this.Local = '';


	// 校验身份证有效性
	this.IsValid = function(CardNo) {
		this.ID15 = '';
		this.ID18 = '';
		this.Local = '';
		CardNo = CardNo.replace(" ", "");
		var strCardNo;
		if (CardNo.length == 18) {
			pattern = /^\d{17}(\d|x|X)$/;
			if (pattern.exec(CardNo) == null)
				return;
			strCardNo = CardNo.toUpperCase();
		} else {
			pattern = /^\d{15}$/;
			if (pattern.exec(CardNo) == null)
				return;
			strCardNo = CardNo.substr(0, 6) + '19' + CardNo.substr(6, 9)
			strCardNo += this.GetVCode(strCardNo);
		}
		this.Valid = this.CheckValid(strCardNo);
		return this.Valid;
	};

	// 返回生日字符串，格式如下，1981-10-10
	this.GetBirthDate = function() {
		var BirthDate = '';
		if (this.Valid)
			BirthDate = this.GetBirthYear() + '-' + this.GetBirthMonth() + '-' + this.GetBirthDay();
		return BirthDate;
	};

	// 返回生日中的年，格式如下，1981
	this.GetBirthYear = function() {
		var BirthYear = '';
		if (this.Valid)
			BirthYear = this.ID18.substr(6, 4);
		return BirthYear;
	};

	// 返回生日中的月，格式如下，10
	this.GetBirthMonth = function() {
		var BirthMonth = '';
		if (this.Valid)
			BirthMonth = this.ID18.substr(10, 2);
		//if (BirthMonth.charAt(0) == '0')
		//	BirthMonth = BirthMonth.charAt(1);
		return BirthMonth;
	};

	// 返回生日中的日，格式如下，10
	this.GetBirthDay = function() {
		var BirthDay = '';
		if (this.Valid)
			BirthDay = this.ID18.substr(12, 2);
		return BirthDay;
	};

	// 返回性别，1：男，0：女
	this.GetSex = function() {
		var Sex = '';
		if (this.Valid)
			Sex = this.ID18.charAt(16) % 2;

		return Sex==0?2:1;
	};

	// 返回15位身份证号码
	this.Get15 = function() {
		var ID15 = '';
		if (this.Valid)
			ID15 = this.ID15;
		return ID15;
	};

	// 返回18位身份证号码
	this.Get18 = function() {
		var ID18 = '';
		if (this.Valid)
			ID18 = this.ID18;
		return ID18;
	};

	// 返回所在省，例如：上海市、浙江省
	this.GetLocal = function() {
		var Local = '';
		if (this.Valid)
			Local = this.Local;
		return Local;
	};

	this.GetVCode = function(CardNo17) {
		var Wi = new Array(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1);
		var Ai = new Array('1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2');
		var cardNoSum = 0;
		for ( var i = 0; i < CardNo17.length; i++)
			cardNoSum += CardNo17.charAt(i) * Wi[i];
		var seq = cardNoSum % 11;
		return Ai[seq];
	};

	this.CheckValid = function(CardNo18) {
		if (this.GetVCode(CardNo18.substr(0, 17)) != CardNo18.charAt(17))
			return false;
		if (!this.IsDate(CardNo18.substr(6, 8)))
			return false;
		var aCity = {
			11 : "北京", 12 : "天津", 13 : "河北", 14 : "山西", 15 : "内蒙古", 21 : "辽宁", 22 : "吉林", 23 : "黑龙江 ",
			31 : "上海", 32 : "江苏", 33 : "浙江", 34 : "安徽", 35 : "福建", 36 : "江西", 37 : "山东", 41 : "河南",
			42 : "湖北 ", 43 : "湖南", 44 : "广东", 45 : "广西", 46 : "海南", 50 : "重庆", 51 : "四川", 52 : "贵州",
			53 : "云南", 54 : "西藏 ", 61 : "陕西", 62 : "甘肃", 63 : "青海", 64 : "宁夏", 65 : "新疆", 71 : "台湾",
			81 : "香港", 82 : "澳门", 91 : "国外"
		};
		if (aCity[parseInt(CardNo18.substr(0, 2))] == null)
			return false;
		this.ID18 = CardNo18;
		this.ID15 = CardNo18.substr(0, 6) + CardNo18.substr(8, 9);
		this.Local = aCity[parseInt(CardNo18.substr(0, 2))];
		return true;
	};

	this.IsDate = function(strDate) {
		var r = strDate.match(/^(\d{1,4})(\d{1,2})(\d{1,2})$/);
		if (r == null)
			return false;
		var d = new Date(r[1], r[2] - 1, r[3]);
		return (d.getFullYear() == r[1] && (d.getMonth() + 1) == r[2] && d.getDate() == r[3]);
	};
}

(function($) {
	$.kh.validte = function(idcard) {
		var idInfo = {
			birthday : "", gender : 0, city : "", age : 0
		};
		var rto = {
			idInfo : idInfo, result : true, msg : ""
		};

		var idCard = new ClsIDCard();
		if (!idCard.IsValid(idcard)) {
			rto.result = false;
		}else{
			rto.idInfo.birthday = idCard.GetBirthDate();
			rto.idInfo.city=idCard.GetLocal();
			rto.idInfo.gender = idCard.GetSex();
			rto.idInfo.age = new Date().getFullYear() - idCard.GetBirthYear();
			rto.result = true;
		}
		return rto;
	};

	if ($.validator) {
		$.validator.addMethod("idno", function(value, element, param) {
			if (!value)
				return true;
			return $.kh.validte(value).result;
		}, "身份证号码不正确");
		$.validator.addMethod("ltTo", function(value, element, param) {
			var target = $(param).unbind(".validate-ltTo").bind(
					"blur.validate-ltTo", function() {
						$(element).valid();
					});
			return value < target.val();
		}, "请输入一个较小的值");
		$.validator.addMethod("gtTo", function(value, element, param) {
			var target = $(param).unbind(".validate-ltTo").bind(
					"blur.validate-ltTo", function() {
						$(element).valid();
					});
			return value > target.val();
		}, "请输入一个较大的值");
		//$.metadata.setType("attr", "validate");
		$.validator.setDefaults({
			ignore : "",
			//验证提示。
			errorPlacement : function(lable, element) {
				//if (!element.attr("required")) {return;}
				if (element.hasClass("l-textarea")) {
					element.parents(".l-text").addClass("l-text-invalid");
				} else if (element.hasClass("l-text-field")) {
					element.parents(".l-text").addClass("l-text-invalid");
				} else if (element.hasClass("l-radio-group") || element.hasClass("l-checkbox-group")) {
					element.parent().addClass("l-text-invalid");
				}
				$(element).removeAttr("title").ligerHideTip();
				$(element).attr("title", lable.html()).ligerTip({
					auto : true, position : 'bottom', contentCls : 'error',
					distanceX : -4, distanceY : 4
				});
			},
			success : function(lable, ele) {
				var element = $(ele);
				element.removeClass("requiredstar");
				if (element.hasClass("l-textarea")) {
					element.parents(".l-text").removeClass("l-text-invalid");
				} else if (element.hasClass("l-text-field")) {
					element.parents(".l-text").removeClass("l-text-invalid");
				} else if (element.hasClass("l-radio-group") || element.hasClass("l-checkbox-group")) {
					element.parent().removeClass("l-text-invalid").removeClass("requiredstar");
				}
				$(element).removeAttr("title").ligerHideTip();
			}
		});
	}
	$.fn.parseForm = function(key) {
		$("body").mask("正在初始化数据，请稍候……");
		var $this = $(this);
		var options = $this.data("formOptions");
		if ($this.data("formType") == "formList") {
			$this.parseFormList(options,key);//alert("渲染一对多form");
		} else {
			$this.parseFormSingle(options,key);//alert("渲染单个form表单");
		}
		return $this;
	}
	$.fn.initForm = function(options) {
		var $this = $(this);
		if ($("form").not("[autoParse='false']").size() > 1)
			options = $.extend(options, {
				toolbarPosition : options.toolbarPosition || "top"
			});
		$this.data("formOptions", options);
		return $this;
	}
	$.fn.initFormList = $.fn.bindFormList = function(options) {
		var $this = $(this);
		$this.data("formOptions", options);
		$this.data("formType", "formList");
		return $this;
	}
	$.fn.parseFormSingle = function(options,key) {
		var $this = $(this);
		var config = c = $.extend({
			transformOnly:false,
			autoClose : $("form").not("[autoParse='false']").size() == 1,
			status : $this.attr("pageStatus") || $("head").attr("pageStatus"),
			action : $this.attr("action"), actionParam : {},
			showToolbar:$this.attr("showToolbar") || false,//true 只有在 status == "detail" 的时候此参数有用 13-4-7 下午5:25 lybide
			//format : $this.attr("format") || "true",//form格式化开关，13-4-7 下午5:03 lybide
			afterParse:function(formObj){//form表单完成渲染后，回调函数
				//console.log($("#form1").data());
			},
			getSuccess : function() {
			}, success : function(data) {
				if (data.success) {
					top.$.notice('操作成功！');
					$this.setValues(data.data);// alert(api.data.window.qmUserConfig)
					var aw = api.data.window;
					if (api && aw.Qm) {
						aw.Qm.refreshGrid();
						if (config.autoClose) {
							api.close();
						}
					}
				} else {
                    var content="操作失败！";
                    if(data.msg){
                        content=data.msg;
                    }else if(data.data){
                        content=data.data;
                    }
					top.$.dialog.notice({
						icon : 'k/kui/images/icons/dialog/32X32/fail.png', content : content
					});
				}
			}, toolbar : [],extToolbar:[]
		}, options);

		if (!$.kh.isNull(config.getAction)) {//todo 13-3-28 上午11:00 lybide
			$this.attr("getAction",config.getAction);
		}

		//var format= c.format.toLowerCase()=="true"?true:false;
		c.showToolbar= c.showToolbar.toString().toLowerCase()=="true"?true:false;

		//是否给form表单添加滚动层 13-1-14 下午2:02 lybide
		var scrollTm=$this.parent(".l-layout-content").attr("position")===undefined?true:false;//是否有框架

		//alert(key+"=form的类型");//13-4-9 下午2:38 lybide
		if (key==1) {//直接就是form
			if ($(".scroll-tm").length>0) {
				scrollTm=false;
			}
		} else if (key==2) {//tab效果，里面的form
			if ($this.parent(".l-tab-content-item").length>0) {//13-4-9 下午1:53 lybide
				scrollTm=true;
			}
		}

		if (scrollTm) {
			var formWrap = $this.wrap("<div class='scroll-tm' style='overflow-y: auto'></div>").parent();
		} else {
			var formWrap=$this.parent();
		}

		var ids=this.attr("id") || "form"+Math.random().toString(16).substring(2);//2013-5-30 下午11:31 lybide 给按钮等加一个id值
		var status = config.status;//alert(status+"==="+config.showToolbar);
		var toolbar;//alert(status != "detail");
		if (status != "detail" || config.showToolbar) {
			if (config.toolbar != null) {
				if (config.toolbarPosition == "top") {
					if (config.toolbar.length == 0)
						config.toolbar = [
							{
								id:ids+"otSave",text : "保存", scale : "small", icon : "save",
								click : _save
							}
						];
                    if(config.extToolbar.length>0){
                        config.toolbar=config.toolbar.concat(config.extToolbar);
                    }

					//if (toolbar.length>0) {
					if (scrollTm) {
						toolbar = $("<div class='toolbar-tm'><div class='toolbar-tm-top'></div></div>");
						toolbar.insertBefore(formWrap);
					} else {
						toolbar=$(".toolbar-tm");
					}
					toolbar.children().ligerToolBar({
						items : config.toolbar
					});
				} else {
					// api.button({id:"ok1",name:"保存",callback:_save});
					// api.button({id:"cancel1",name:"关闭",callback:_close});

					if (config.toolbar.length == 0)
						config.toolbar = [
								{
									id:ids+"opSave",text : "保存", scale : "small", icon : "save",
									click : _save
								},
								{
									id:ids+"opClose",text : "关闭", scale : "small", icon : "cancel",
									click : _close
								}
						];
                    if(config.extToolbar.length>0){
                        config.toolbar=config.toolbar.concat(config.extToolbar);
                    }
					//toolbar=$(".toolbar-tm");
					//if (toolbar.length>0) {
					if (scrollTm) {
						toolbar = $("<div class='toolbar-tm'><div class='toolbar-tm-bottom'></div></div>");
						toolbar.insertAfter(formWrap);

					} else {
						toolbar=$(".toolbar-tm");
					}
					var ct = toolbar.children();
					$.each(config.toolbar, function(i, item) {//alert($this.ligerButton(item));
						$('<a class="l-button-warp l-button"></a>').appendTo(ct).ligerButton(item);
					});
				}
			}
		}
		if (scrollTm) {
			formWrap.height(_parseHeight());// alert($(window).width()+"="+$(window).height()+"====");
		}



		$this.validate({
			submitHandler : function() {
				$("body").mask("正在保存数据，请稍候……");
				$.post($this.attr("action") || config["action"], $.transValues($
						.extend($this.getValues(),
								_getParaValues(config.actionParam))),
						function(data) {
							$("body").unmask();
							config.success(data); // todo 事件
                            //config.onAfterSave();
						}, "json");
			}
		});
		$this.data("initForm", true);
		//$this.data("defaultValues", $this.getValues());

		$this.transform(status);
		$("body").unmask();
		if (status == "edit" || status == "modify" || status == "detail") {
			$("body").mask("正在初始化数据，请稍候……");
			$this.setValues(config.getSuccess);
		}
		config.afterParse($this);
		try{jsEndTime("单个form表单渲染完成");pagesEnd("form",1)}catch (e){}


		function _parseHeight() {
			var tbHeight = toolbar ? toolbar.innerHeight() : 0;
			if (formWrap.parent()[0].tagName.toLowerCase() == "body") {
				var windowHeight = $(window).height();
				windowHeight -= parseInt(formWrap.parent().css('paddingTop'));
				windowHeight -= parseInt(formWrap.parent().css('paddingBottom'));
				return (windowHeight - tbHeight);
			} else {
				var contentHeight = formWrap.parent().height() || 0;
				return (contentHeight - tbHeight);
			}
			return "100%";
		}

		function _save() {
			$this.submit();
	}

	function _close() {
		//if (typeof api=="undefined") {
		//	parent.window.close();
		//} else {
			api.close();
		//}

	}

	function _getParaValues(param) {
		// 检查关联字段
		var para = {};
		$.each(param, function(i, el) {
			var ele = $(el);
			if (ele.length > 0) {
				para[i] = ele.val();
			}
		});
		return para;
	}
		return $this;
	}

	$.fn.parseFormList = function(options) {
		var $this = $(this);
		$this.validate({
			submitHandler : function(form) {
				// 检查关联字段
				var para = {};
				var canSumit = true;
				$.each(config.actionParam, function(i, el) {
					var v = $(el).val();
					if (v == "") {
						var tab = $(el).parents(".l-tab-content-item");
						var tabid = tab.attr("tabid");
						var title = $("[tabid=" + tabid + "]>a",
								tab.parents(".l-tab")).html();
						$.ligerDialog.warn("请先提交 " + title + " 信息！");
						$(el).parents(".l-tab").ligerGetTabManager().selectTabItem(
								tabid);
						canSumit = false;
						return false;
					}
					para[i] = v;
				});
				if (!canSumit)
					return;
				var datas = $.extend($this.getValues(), para);
				$("body").mask("正在保存数据，请稍候……");
				$.post($this.attr("action") || config["action"], $
						.transValues(datas), function(data) {
					if (data.success) {
						var row = grid.getSelectedRow();
						$.extend(datas, data.data);
						if (row) {
							grid.updateRow(row, datas);
							grid.unselect(row);
						} else {
							grid.addRow(datas);
						}
                        //if (config.onAfterSave($this,data)) {//2013-6-26 下午3:33 lybide
							config.onAfterSave($this,data);
							_resetValues();
							config.success(data);
						//};
					} else {
						var content="操作失败！";
	                    if(data.msg){
	                        content=data.msg;
	                    }
						$.ligerDialog.warn(content);
					}
					$("body").unmask();
				}, "json");
			}
		});

		var config = $.extend({
			status : $this.attr("pageStatus") || $("head").attr("pageStatus"),
			opType : "auto", // 数据保存模式，发现actionParam的关联信息为空时会自动调用opTypeFun，默认为auto
			opTypeFun : _save, // 自动保存调用方法，opType为atuo时才会用到,默认为save
			action : $this.attr("action"), // 保存数据或其它操作的action
			actionParam : {
				"fkId" : $("#form1>[name='id']")
			}, // 保存时会在当前表单上附加此数据，如：{fkId:$("#form1>#id")}会把from1下的name为id的值带上去,可以是一个对象或选择器
			success : function() {
			}, delAction : $this.attr("delAction"), // 删除数据的action
			delSuccess : function() {
			}, delActionParam : {
				ids : "id"
			}, // 默认为选择行的id
			getAction : $this.attr("getAction"), // 取数据的action
			getSuccess : function() {},
			getActionParam : {}, // 取数据时附带的参数，一般只在需要动态取特定值时用到
            onBeforeDel:function (){return true},
			columns : [// 此部分配置同grid
			],
            onSelectRow : function(rowdata, rowindex) {
				$this.setValues(rowdata);
			},
            onUnSelectRow : function(rowdata, rowindex) {
				_resetValues();
			},
            onLoaded : function() {
				config.getSuccess(this.currentData);
				this.toggleLoading.ligerDefer(this, 10, [
					false
				]);
			},
            onBeforeSave:function(form,grid){return true;},
            onBeforeDelete:function(row,form,grid){return true;},
            onAfterSave:function(g,data){},
			save:function(){},
			afterParse:function(formObj){//form表单完成渲染后，回调函数

			},
            extToolbar:[],
            toolbar : [
					{
						id:"otSave",text : '保存', click : _save, icon : 'save'
					}, {
						line : true
					}, {
						id:"otDel",text : '删除', click : _deleteRow, icon : 'delete'
					}
			],
            usePager : false, frozen : false, enabledEdit : false,
			allowUnSelectRow : true, isScroll : true,
			// delayLoad:true,
			rownumbers : true,
			// url:$this.attr("getAction"),

			width : "auto", height : "100%"
		}, options);
        if(config.extToolbar.length>0){
            config.toolbar=config.toolbar.concat(config.extToolbar);
        }
		var status = config.status;
		$this.transform(status);


		function _parseValues(value, data) {
			if (value == undefined)
				return "";
			var str = [];
			var values = value.toString().split(",");
			for ( var i = 0; i < values.length; i++) {
				getText(values[i],data,str)
			}
			return str;
		}
        function getText(value,data,array){
            $.each(data, function(index, item) {
                if (value == item["id"]) {
                    array.push(item["text"]);
                    return false;
                } else if(item["children"]){
                    getText(value,item["children"],array);
                }
            });
        }

		if (status == "detail") {
			var selects = $("[xtype=combobox],[xtype=select],[xtype=radioGroup],[xtype=checkboxGroup]",$this);
			var dates = $("[xtype=date]", $this);
			var spinner = $("[ltype=spinner]", $this);
			$.each(config.columns, function(i, column) {
				column.isSort = column.isSort ? column.isSort : false;
				selects.each(function() {
					var ele = $(this);
					var name = ele.attr("name");
					if (!name)
						return;
					if (name == column.name) {
						column.render = function(item) {
							var ligerData = ele.data("ligerui") || {};
							var url = ligerData.url;
                            if (ligerData.tree) {
                                if (ligerData.tree.url) {
                                    url = ligerData.tree.url;
                                }
                                else {
                                    ligerData.data=ligerData.tree.data;
                                }
                            }
							if (!ligerData.data && url) {
								$.ajax({
									url : url || ligerData.tree.url,
									dataType : 'json',
									async : false,
									success : function(resData) {
										// ele.data("data",
										// resData);
										ligerData.data = resData;
										ligerData.onSuccess.call(null, resData,ligerData.initValue || ele.val())
									}
								});
							}
                            return _parseValues(item[name], ligerData.data);
						}
						return false;
					}
				});
				dates.each(function () {
					var ele = $(this);
					var name = ele.attr("name");
					//console.log(name);
					if (name == column.name) {
						column.render = function (item) {
							return item[name] == null ? "" : $.kh.getFormatDate(item[name], ele.data("format"));
						}
						return false;
					}
				});
				spinner.each(function() {//2013-9-12 下午6:03 lybide
					/*var ele = $(this);
					//var date = ele.ligerSpinner();
					var name = ele.attr("name");
					if (name == column.name) {
						column.render = function(item) {
							return item[name] == null ? "" : date._getVerifyValue(item[name]);
						}
						return false;
					}*/
				});
			});
		} else {
			var selects = $("[ltype=select],[ltype=radioGroup],[ltype=checkboxGroup]",$this);
			var dates = $("[ltype=date]", $this);
			var spinner = $("[ltype=spinner]", $this);
			$.each(config.columns, function(i, column) {
				column.isSort = column.isSort ? column.isSort : false;
				selects.each(function() {
					var combo = $(this);
					var name = combo.attr("name");
                    var ltype = combo.attr("ltype");
                    if (ltype == "select") {
                        combo = $(this).ligerComboBox();
                        name = combo.get("valueFieldID")?combo.get("valueFieldID"):name;
                    }
                    if (name == column.name) {
                        if (ltype == "radioGroup") {
                            combo = $(this).ligerRadioGroup();
                        } else if (ltype == "checkboxGroup") {
                            combo = $(this).ligerCheckBoxGroup()

                        }
                        if (!combo.get("textModel")) {
                            column.render = function (item) {
                                var data = combo.data||combo.get("data");
                                if (combo.get("tree")) {
                                    data = combo.treeManager.getData();
                                }
                                return _parseValues(item[name], data);
                            }
                        }
						return false;
					}
				});
				dates.each(function() {
					var ele = $(this);
					var date = ele.ligerDateEditor();
					var name = ele.attr("name");
					if (name == column.name) {
						column.render = function(item) {
							return item[name] == null ? "" : $.kh.getFormatDate(item[name], date.options.format);//date.getFormatDate(new Date(item[name]));
						}
						return false;
					}
				});
				spinner.each(function() {//2013-9-12 下午6:03 lybide
					var ele = $(this);
					var date = ele.ligerSpinner();
					var name = ele.attr("name");
					if (name == column.name) {
						column.render = function(item) {
							return item[name] == null ? "" : date._getVerifyValue(item[name]);
						}
						return false;
					}
				});
			});
		}
		if (status != "detail" || config.showToolbar) {
			var toolbar = $("<div class='toolbar-tm'><div class='toolbar-tm-top'></div></div>");
			$this.before(toolbar);
			toolbar.children().ligerToolBar({
				items : config.toolbar
			});
		}
		if ((status == "edit" || status == "modify" || status == "detail")
				&& config["getAction"]) {
			config.url = config["getAction"];
		} else {
			var root = config["root"] || "Rows";
			var data = {};
			data[root] = [];
			config.data = data;
		}
		var gridDiv = $("<div></div>");
		if (config.id)
			gridDiv.attr("id", config.id);
		$this.after(gridDiv);
		config.toolbar = undefined;
		var grid = gridDiv.ligerGrid(config);

		$this.data("defaultValues", $this.getValues());
		$this.data("initForm", true);
		config.afterParse($this);
		try{jsEndTime("一对多单表渲染完成");pagesEnd("form",2);}catch (e){}

		function _resetValues() {
			if (status == "detail") {
				$("div.input", $this).each(function() {
					$(this).html("&nbsp;");
				});
			} else {
				$this.setValues($this.data("defaultValues"));
			}
		}

		function _save() {
			var s=config.onBeforeSave($this,grid);
			if (!s) {
				return;
			}
			$this.submit();

		}

		function _getParaValues(param) {
			// 检查关联字段
			var para = {};
			$.each(param, function(i, el) {
				var ele = $(el);
				if (ele.length > 0) {
					para[i] = ele.val();
				}
			});
			return para;
		}

		function _getDelParaValues(row, param) {
			// 检查关联字段
			var para = {};
			$.each(param, function(i, el) {
				para[i] = row[el];
			});
			return para;
		}

		function _deleteRow() {
			var row = grid.getSelected();
			if (row) {
				var s=config.onBeforeDelete(row,$this,grid);
				if (!s) {
					return;
				}
                if (config.onBeforeDel(row, row['__index'])) {
                    $.ligerDialog.confirm(kui.DEL_MSG,
                        function (yes) {
                            if (yes) {
                                var url = config["delAction"];
                                var data = _getDelParaValues(row,
                                    config["delActionParam"]);
                                $.post(url, data, function (data) {
                                    if (data.success) {
                                        grid.deleteSelectedRow();
                                        _resetValues();
                                        config.delSuccess(data);
                                    } else {
                                        var content="删除失败！";
                                        if(data.msg){
                                            content=data.msg;
                                        }
                                        $.ligerDialog.error(content);
                                    }
                                }, "json");
                            }
                        })
                }
			} else {
				$.ligerDialog.warn('请先选择需要删除的数据！');
			}
		}
		$("body").unmask();
		return $this;
	};

	// 表单校验 todo
	$.fn.formInit = function(optionsObj) {
		return this.each(function() {
			/*var $this = $(this);
			$this.validate({
				submitHandler : function() {
					// $(".l-text,.l-textarea",
					// $this).ligerHideTip();
					var options = {
						type : 'post',
						beforeSubmit : function(formData, jqForm, options) {
							if (typeof optionsObj.beforeSubmit != "undefined") {
								return optionsObj.beforeSubmit(formData, jqForm, options)
							}
						}, success : function(responseText) {
							optionsObj.success(responseText);
						}
					};
					// var form = $(formStr);
					$this.ajaxSubmit(options);
				}
			});*/
		});
	}

	//$.fn.reset = function() {
	//	var $this=$(this);
	//	$this.setValues($this.data("defaultValues"));
	//}

	$.fn.transform = function(status) {
		var $this = $(this);
        $this.attr("pageStatus",status);
		if (status == "detail") {
			$(":input", $this)
			//.not(":hidden")
			.not("type=[hidden]")//todo 解决tab第二或以上input框不初始化的效果 13-5-2 下午10:45 lybide
			.each(
				function() {
					var jinput = $(this);//alert(jinput.attr("name"))
					var initValue = jinput.val();
					var ltype = jinput.attr("ltype");
					var id = jinput.attr("id");
					var name = jinput.attr("name");

					var ligerui = parseLigerui(jinput);
					if (ligerui)
						initValue = initValue || ligerui.initValue;

					if (ltype == "select") {
						if (ligerui.textModel || ligerui.isTextBoxMode)
							ltype = "text";
						else if (ligerui.valueFieldID)
							name = ligerui.valueFieldID;
					}
					var div, ele;
					/*if (ligerui && ligerui.suffixDetail) {//2013-5-10 下午2:16 lybide
						ligerui.suffix=ligerui.suffixDetail;
					}*/
					if (ligerui && ligerui.suffix) {
						div = $("<table border='0' class='l-text-suffix-wrap'><tr><td class='l-text-left'><div class='input' xtype='"
								+ ltype
								+ "' name='"
								+ name
								+ "'"
								+ (id ? (" id='" + id + "'") : "")
								+ ">&nbsp;</div></td><td class='l-text-suffix'"
								+ (ligerui.suffixWidth ? " style='width:"
										+ ligerui.suffixWidth + "px'"
										: "")
								+ ">"
								+ ligerui.suffix
								+ "</td></tr></table>");
						ele = $(".input", div);
					} else {
						div = $("<div class='input' xtype='" + ltype
								+ "' name='" + name + "'"
								+ (id ? (" id='" + id + "'") : "")
								+ ">&nbsp;</div>");
						if (ligerui && ligerui.lineWrap === false)
							div.addClass("lineWrap");
						ele = div;
					}

					switch (ltype) {
					case "radioGroup":
						bindData(ele, ligerui);
						break;
					case "checkboxGroup":
						bindData(ele, ligerui);
						break;
					case "select":
						bindData(ele, ligerui);
						break;
					case "combobox":
						bindData(ele, ligerui);
						break;
					case "date": {
						div.data("format", "yyyy-MM-dd");
						if (ligerui && ligerui.format) {
							ele.data("format", ligerui.format);
						}
					}
						break;
					case "text":
					case "spinner":
						bindData(ele, ligerui);
						break;
					case "float":
					case "number":
					case "int":
					case "digits":
						break;
					default: {
						if (!jinput.is("textarea")) {
							jinput.hide();
							return;
						}
					}
						break;
					}
					if (initValue!="" && initValue!==undefined) {
						ele.setDivValue(initValue);
					}
					jinput.replaceWith($("<div class='input-warp'></div>").wrapInner(div));
				}
			);
		} else {
			$this.ligerForm();
		}
		function bindData(div, ligerui) {
			if (ligerui) {
				div.data("ligerui", ligerui);
				var url = ligerui.url;
				if (ligerui.tree && ligerui.tree.url) {
					url = ligerui.tree.url;
				}
				if (url) {
					$.ajax({
						url : url || ligerui.tree.url, async : false,
						dataType : "json", success : function(resData) {
							ligerui.data = resData;
							div.data("ligerui", ligerui);
							if (ligerui.onSuccess)
								ligerui.onSuccess.call(null, resData, null)
						}
					});
				}
			}
		}

		function parseLigerui(ele) {
			var attroptions = ele.attr("ligerui");
			if (attroptions) {
				try {
					if (attroptions.indexOf('{') != 0)
						attroptions = "{" + attroptions + "}";
					eval("attroptions = " + attroptions + ";");
				} catch (e) {
					alert("解析ligerui内容出错，请检查方法是否正确")
				}
			}
			return attroptions;
		}
		return $this;
	}
})(jQuery);

jQuery.event.add(window, "load", function() {
	initDetailPage();
});

$(function(){//13-4-7 下午4:26 lybide
	//initDetailPage();
});
var detailNav;
function initDetailPage(){
	//$("body").mask("正在初始化数据，请稍候……");
	//$.ligerui.win.mask();
	// setTimeout(function(){
	var forms = $("form").not("[autoParse='false']");
	if (forms.size() == 1) {
		forms.parseForm(1);
	}
	var nav=detailNav = $(".navtab").ligerTab({
		showSwitchInTab:false,showSwitch:true,//2017年07月26日 17:21:02 星期三 lybide 显示切换窗口按钮
		width : "auto", height : "100%",
		// heightDiff:30,
		changeHeightOnResize : true, onAfterSelectTabItem : function(tabid) { // 初始化form
			var form = $(".l-tab-content-item[tabid=" + tabid + "]>form");
			if (form.size() > 0) {
				if (!form.data("initForm")) {
					form.parseForm(2);
				}
			}
			$.cSize(true);
			//2013-6-19 下午5:25 lybide 每次都进行一对多表大小调整
			if (form.data("formType") == "formList") {
				form.next().ligerGrid()._onResize();
			}
		}
	});
	if (nav) {//alert("初始化第一个tab")
		var tabid = nav.getSelectedTabItemID();
		var form = $(".l-tab-content-item[tabid=" + tabid + "]>form");
		if (form.size() > 0) {
			if (!form.data("initForm")) {
				form.parseForm(2);
			}
		} else {
			var tbHeight = $(".scroll-tm").height() || 0;
			nav.setHeight(tbHeight);
			// $(".scroll-tm").css("overflow-y","");
			// $(".l-tab-content-item").addClass("scroll-tm").css("overflow-y","auto");
		}
	}
	/*$(window).resize(function() {
		$.cSize();
	});*/
	$.cSize();
	// },1000)
}

//动态修改码表
function sys_modify_codetable(id, code, field) {
  top.$.dialog({
      width : 650,
      height : 400,
      lock : true,
      parent : parent.api||api,//2013-6-3 下午10:24 lybide
      title : "码表值管理",
      content : "url:pub/codetable/codetable_user_manage.jsp?id=" + id,
      ok : true,
      close : function() {
          $.getJSON("pub/codetablevalue/getTextValue.do?code=" + code, function(resp) {
              if (resp.success){
                  var combo = $("#" + field + "-txt").ligerGetComboBoxManager();
                  if(combo.options.tree){
                      var sv = combo.getValue();
                      combo.treeManager.clear();
                      combo.setTree({data:resp.data,checkbox:false});
                      combo.selectValue(sv);
                  }
                  else
                      combo.setData(resp.data);
                  $.getJSON("pub/codetable/refreshCache.do",function(resp){
                  	if(!resp.success)
                  		top.$.dialog.tips('码表刷新失败，请尝试手动刷新！',5,"k/kui/images/icons/dialog/32X32/hits.png",null,0)
                  });
              }
          });
      }
  });
}

/**
 * 将使用$(form).values()方法获取到的数据转换为纯json对象。
 * @param formValus
 */
function transFormDataToJSON(formValus){
	if(!formValus)return;
	var jsonData = {};
	$.each(formValus,function(field,value){
		var obj = transFieldJsonObj(field,value);
		if(jsonData[obj.name]){
			jsonData[obj.name] = transMultiFieldNameJSON(jsonData[obj.name],obj.val);
		}else{
			jsonData[obj.name] = obj.val;
		}
	});
	return jsonData;
}

/**
 * 逐级解析对象的子对象
 * @param temp
 * @param obj
 * @returns obj
 */
function transMultiFieldNameJSON(temp,obj){
	var rst = temp;
	$.each(obj,function(n,v){
		if(temp[n]){
			rst[n] = transMultiFieldNameJSON(temp[n],v);
		}else{
			rst[n]=v;
		}
	});
	return rst;
}

/**
 * 将多级字段名称转化为JSON对象,如将“a.b.c.id=value”转化为a={b:{c{id:"value"}}}
 * @param fieldName
 * @return 返回对象格式：{name:"字段名称",val:"转换后的对象"}，
 * 			如field=fielda.b.c.id，value=xxxx，则返回:{name:'a",val:{b:{c{id:"xxxx"}}}}
 */
function transFieldJsonObj(field,value){
	var fs = field.split(".");
	if(fs.length==1){
		return {name:field,val:value};
	}
	var nf = "",ends = "";
	for(var i = 1; i < fs.length; i++){
		nf += '{"'+fs[i] + '":';
		ends += '}';
	}

	var obj = $.parseJSON(nf + '"' + value + '"' + ends);
	return {name:fs[0],val:obj}
}