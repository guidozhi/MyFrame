window.Qm = {};
var Ext = {};
Ext.onReady = mui;
mui.toJSON = JSON.stringify;
window.Qm = {
    version: "1.3.1.1.9",
    versionDetail: {major: 1, minor: 2, patch: "1.1.9", plugin: "mui"},
///////////////////以下函数可以在自己的页面调用//////////////////
    /**
     * public 设置工具条按钮状态
     */
    setTbar: function () {
    },
    /**
     * public 查询数据
     */
    searchData: function () {
    },
    /**
     * public 刷新表格数据
     */
    refreshGrid: function () {
    },
    /**
     * 得到选中行数
     */
    getSelectedCount: function () {
    },
    /**
     * 取值
     * @param name
     */
    getValueByName: function (name) {
    },
    /**
     * public
     */
    getValuesByName: function (name) {
    },
    /**
     * public 刷新页面
     */
    refreshPage: function () {
    },
////////////////////////////////////////////////////////////////
    pagingUrlPre: "qm?__method=",
    config: {},
    userConfig: {},
    separator: {line: true},
    getColumnValueType: function (obj) {
        var type = "string";
        if (obj) {
            if (isArray(obj.binddata)) {
                if (obj.binddata.length > 0) {
                    type = "multiple";
                    for (var i in obj.binddata) {
                        if (obj.binddata[i].children) {
                            type = "multipletree";
                            break;
                        }
                    }
                }
            } else {
                type = obj.datatype;
            }
        }
        return type;
    },
    parseSearchGroup: function (group) {
        for (var i in group) {
            Qm.parseSearchItem(group[i]);
        }
    },
    parseSearchItem: function (obj) {
        if (obj.group) {
            Qm.parseSearchGroup(obj.group);
        }
        var col = Qm.config.columnsInfo[obj.name];
        var valueType = Qm.getColumnValueType(col);
        if(col){
            obj["columndisplay"] = col["columndisplay"];
        }
        if (valueType == "multiple") {
            if (col.binddata.length > 0 && col.binddata[0].id) {
                if (obj.xtype == "radioGroup") {
                    col.binddata.unshift({id: "", text: "全部"});
                }
            } else {
                if (obj.xtype == "radioGroup") {
                    col.binddata.unshift(["", "全部"]);
                }
            }

            obj.xtype = "combo";
            obj.data = Qm.parseComboData(col.binddata);
        }
        else if (valueType == "multipletree") {
            obj.xtype = "comboxtree";
            obj.tree = {data: col.binddata, checkbox: false};
            obj.data=Qm.parseComboTreeData(col.binddata);
        }
        else if (valueType == "date") {
            obj.xtype = "date";
            obj.format = Qm.getDateFormat(col.formater);
            if (/mm/.test(obj.format)) {
                obj.showTime = true;
            }
        }
        else if (valueType == "number") {
            obj.xtype = "number";
            if (!obj.value && !obj.initValue)
//                obj.value="";
                obj.type = "int";
        } else {
            obj.xtype = "text";
        }

    },
    getSearchItemInput: function (item) {
        var divStr = '';
        if (item.xtype == "combo" || item.xtype == "comboxtree") {
            divStr = '<select name="' + item.name + '" id="' + item.id + '" data-iconpos="left">'
            for (var d in item.data) {
                divStr += '<option value="' + item.data[d]["id"] + '">' + item.data[d]["text"] + '</option>';
            }
            divStr += '</select>';
        } else if (item.xtype == "radioGroup") {
            divStr += " type='radio' name='" + item.id + "-txt'";
        } else if (item.xtype == "checkboxGroup") {
            divStr += " type='checkbox' name='" + item.id + "-txt'";
        } else if (item.xtype == "date") {
            divStr += "<input type='date' data-iconpos='left' id='" + item.id + "' data-role='date' placeholder='请输入" + item["columndisplay"] + "' name='" + item["name"] + "'/>";
        } else {
            var placeholder="请输入"+item["columndisplay"];
            if(item["placeholder"]){
                placeholder=item["placeholder"];
            }
            var defaultValue = "";
            if(item["value"]){
            	defaultValue = item["value"];
            }
            divStr +='<form id="searchForm"><input id="searchKey" name="'+item.name+'" value="'+defaultValue+'" type="search" class="mui-input-clear" placeholder="'+placeholder+'"></form>';
        }
        return divStr;
    },
    getDateFormat: function (formater) {//实现前后台日期格式转换
        return formater == "" ? "yyyy-MM-dd" : formater.replace("HH", "hh");
    },
    parseStyleWidth: function (width) {
        var cWidth = width;
        if (this.config.sp_defaults.layout == "column") {
            if (!cWidth) {//2013-6-3 下午4:41 lybide
                return "";
            }
            if (!isNaN(cWidth)) {
                if (cWidth <= 1) {
                    cWidth = cWidth * 100 + "%";
                } else if (cWidth > 1) {
                    cWidth = cWidth + "px";
                } else {
                    cWidth = "";
                }
            }
        } else if (this.config.sp_defaults.layout == "float") {
            if (!isNaN(cWidth)) {
                if (cWidth == 1) {
                    cWidth = "auto";
                } else if (cWidth > 1) {
                    cWidth = cWidth + "px";
                } else {
                    cWidth = "";
                }
            }
        }
        return cWidth;
    },
    parseComboData: function (data) {
        if (data) {
            if (data.length > 0 && !$.isArray(data[0])) {
                return data;
            }
            var json = [];
            for (var i = 0; i < data.length; i++) {
                json.push({id: data[i][0], text: data[i][1]});
            }
            return json;
        }
    },
    parseComboTreeData: function (data) {
        if (data) {
            var json = [];
            for (var i = 0; i < data.length; i++) {
                json.push({id: data[i]["id"], text: data[i]["text"]});
                if(data[i].children){
                    $.merge(json, this.parseComboTreeData(data[i].children));
                }
            }
            return json;
        }
    },
    //构建工具条
    buildToolbar: function () {
        var tools = this.userConfig.tbar || [];
        if (tools.length > 0) {
            for (var i = 0; i < tools.length; i++) {
                if (typeof tools[i] == "string") {
                    if (tools[i] == "-") {
                        tools[i] = this.separator;
                    }

                }
            }
        }
        return tools;

    },
    getParams: function () {
        var params = [];
        mui.each(this.config.searchItems, function (index, items) {
            var item = items;
            if (items.group) {
                for (var i in items.group) {
                    item = items.group[i];
                    params.push({
                        "logic": item.logic || "and",
                        "label": item.fieldLabel,
                        "name": item.name,
                        "compare": item.compare,
                        "dataType": item.dataType,
                        "value": item.ele.getValue()
                    });
                }
            } else {
                params.push({
                    "logic": item.logic || "and",
                    "label": item.fieldLabel,
                    "name": item.name,
                    "compare": item.compare,
                    "dataType": item.dataType,
                    "value": item.ele.getValue()
                });
            }
        });
        return params;
    },
    getRow: function (a, b, c, d, e) {
        return a;
    },
    getThisValue: function (a, b, c, d, e) {
        return c;
    },
    advance: function () {
        if(!Qm.qmBuilder){
            Qm.qmBuilder=new Qm.QmBuilder();
        }
        Qm.qmBuilder.show();
    },
    setSessionAdvancedCondition:function(con){
        this.config.sessionAdvancedCondition=con;
    },
    getSessionAdvancedCondition:function(){
        return this.config.sessionAdvancedCondition;
    },
    setSearchPara:function(con){
        this.config.searchPara=con;
    },
    getSearchPara:function(){
        return this.config.searchPara;
    },
    init: function () {
        //Qm.config.qmUserConfig = Qm.userConfig;
        if (Qm.userConfig.title) {
            Qm.config.pageTitle = Qm.userConfig.title;
        }

        var columns = Qm.config.columnsInfo;
        var cmArr = [];
        var j = 0;
        var width = 0;
        var width1 = 0;
        for (var v in columns) {
            width += columns[v].width;
            if (columns[v].visible != "0")
                width1 += columns[v].width;
            cmArr[j] = {
                display: columns[v].columndisplay,
                name: columns[v].columm,
                align: columns[v].align,
                width: columns[v].width,
                hide: (columns[v].visible == "0"),
                sortable: true,
                resizable: Qm.config.selfhood || false,
                isAllowHide: (columns[v].config == "1")
            };
            if (columns[v].formater != "" && columns[v].datatype != "date") {
                cmArr[j].render = columns[v].formater;
            }
            j++;
        }

        Qm.config = mui.extend(true, {
            pageid: "",
            pageSize: Qm.config.pagesize,

            defaultSearchConditionName: "defaultSearchCondition",
            manmadeSearchConditionName: "manmadeSearchCondition",
            sessionConditionName: "sessionCondition",
            sessionAdvancedConditionName: "sessionAdvancedCondition",
            searchParaName: "searchPara",
            sortInfoName: "sortInfo",
            sortInfo: [],

            defaultSearchCondition: [],
            manmadeSearchCondition: [],
            searchPara: [],
            advancedSearchPara: [],

            sessionCondition: [],
            sessionAdvancedCondition: [],

            searchItems: []

        }, Qm.config, Qm.userConfig);


        if (Qm.config.sortInfo.length > 0) {
            var sortInfo = Qm.config.sortInfo;
            mui.extend(Qm.config, {sortName: sortInfo[0].field, sortOrder: sortInfo[0].direction.toLowerCase()});
        }
    }
};
(function ($) {
	mui.plusReady(function(){
		try {
            Qm.userConfig = qmUserConfig || {};
        } catch (e) {
        }
        var QmView = plus.webview.getWebviewById("__QM_INDEX");
		//添加弹出菜单
		if (Qm.userConfig.popBar && Qm.userConfig.popBar.length > 0) {
			if(Qm.userConfig.popBar.length>1){
				var topPopover = document.getElementById("topPopover");
				if(!topPopover){
					var popoverheight = Qm.userConfig.popBar.length * 48;
					if(popoverheight > plus.screen.resolutionHeight){
						popoverheight = plus.screen.resolutionHeight -80;
					}
					var popover = '<div id="topPopover" class="mui-popover" style="position:fixed;top:16px;right:6px;width:120px;height:'+popoverheight+'px"><div class="mui-popover-arrow" style="left: auto;right: 6px;"></div><div class="mui-scroll-wrapper">'
								  +'<div class="mui-scroll"><ul id="popview" class="mui-table-view">';
					mui.each(Qm.userConfig.popBar,function(index,item){
						var temp = isNull(item.text)?"":item.text;
						if(!isNull(item.icon)){
							temp = '<span class="mui-icon mui-icon-'+item.icon+'" style="font-size:16px">'+temp+'</span>'
						}
						popover += '<li class="mui-table-view-cell" index='+index+'>'+temp+'</li>';
					})
					popover += '</ul></div></div></div>';
					document.body.appendChild(parseDom(popover)[0]);
					popoverView();
				}
				//添加付
				var rightBtn = '<a id="navRightBtn" class="mui-icon mui-icon-bars mui-pull-right"></a>';
				QmView.evalJS("setRightBar('"+rightBtn+"')");
			}else{
				var rightBtn = '<a id="navRightBtn" class="mui-icon mui-icon-'+Qm.userConfig.popBar[0].icon+' mui-pull-right"></a>';
				QmView.evalJS("setRightBar('"+rightBtn+"','0')");
			}
		}
		
		var pullrefresh = document.getElementById("pullrefresh");
		if(!pullrefresh){
			var container = "<div id='pullrefresh' class='mui-content mui-scroll-wrapper'><div class='mui-scroll'>";
			if (Qm.config.listModel == "listview"){
				container +="<ul id='__qm_list' class='mui-table-view mui-table-view-chevron'></ul>";
			}else{}
			container +="</div></div>";
			document.body.appendChild(parseDom(container)[0]);
			mui.init({
				swipeBack: false,
				gestureConfig:{
					longtap: true, //默认为false
				    hold:true,//默认为false，不监听
				},
				pullRefresh: {
					container: '#pullrefresh',
					down: {
						callback: onPullDownWithOutPage
					},
					up: {
						contentrefresh: '正在加载...',
						callback: onPullUp
					}
				}
			});
            if(!isNull(Qm.userConfig.title)){
            	var temp = '<span>'+Qm.userConfig.title+'</span>';
            	var search = '<a style="position: absolute;right:20px;width:16px"><span id="btnSearch" class="mui-icon mui-icon-search"></span></a>'
            	if(Qm.userConfig.popBar && Qm.userConfig.popBar.length > 0){
            		search = '<a style="position: absolute;right:60px;width:16px"><span id="btnSearch" class="mui-icon mui-icon-search"></span></a>'
            	}
            	if (Qm.userConfig.sp_fields && Qm.userConfig.sp_fields.length > 0) {
            		var field = Qm.userConfig.sp_fields[0];
                    if (!field["id"]) {
                        field["id"] = "__qm_search_id_0";
                    }
                    Qm.parseSearchItem(field);
                    var searchItem = Qm.getSearchItemInput(field);
            		QmView.evalJS("setSearchListenWithTitle('"+search+"','"+searchItem+"')");
            	}
            	QmView.evalJS("setTitle('"+temp+"')");
            }else{
            	var search = '<a style="position: absolute;right:20px;width:16px"><span id="btnSearch" class="mui-icon mui-icon-closeempty"></span></a>'
            	if(Qm.userConfig.popBar && Qm.userConfig.popBar.length > 0){
            		search = '<a style="position: absolute;right:60px;width:16px"><span id="btnSearch" class="mui-icon mui-icon-closeempty"></span></a>'
            	}
            	if (Qm.userConfig.sp_fields && Qm.userConfig.sp_fields.length > 0) {
                    var field = Qm.userConfig.sp_fields[0];
                    if (!field["id"]) {
                        field["id"] = "__qm_search_id_0";
                    }
                    Qm.parseSearchItem(field);
                    var searchItem = Qm.getSearchItemInput(field);
                    /*var searchInfo = '<div><table style="position:absolute;width:80%;left:10%"><tr><td><div class="mui-input-row mui-search">'+
                    				searchItem +
                    				'</div></td><td style="width:10px"><a id="_qmSearch"><span class="mui-icon mui-icon-search"></span></a></td>'+
                    				'</tr></table>'+
                    				'<a id="_qmSerQuery" class="mui-icon mui-icon-gear mui-pull-right"></a></div>';*/
                    /*var searchInfo = '<div><div class="mui-title">'+
    								searchItem +
    								'</div><a id="_qmSearch" class="mui-icon mui-icon-search mui-pull-right"></a></div>';*/
                    QmView.evalJS("setSerachInfo('"+searchItem+"','"+search+"')");
                }
            }
            //加载数据
            getData(1,setUpStatus);
            //设置查询placeholder
		}
	});
	
	/**
     * public 设置工具条状态
     */
    Qm.setTbar = function (status) {
    	Qm.config.tbarStatus = status;
    }
    /**
     * public 得到ligerui原生Grid
     */
    Qm.getQmgrid = function () {
        return mui("#__qm_list");
    }
    Qm.setCondition = function (con) { //todo
    }
    /**
     * public 查询数据
     */
    Qm.searchData = function (value) {
        var para=getParams(value);
        Qm.setSearchPara(para);
        getData(1,setUpStatus);
    }
    /**
     * public 刷新表格数据
     */
    Qm.refreshGrid = function () {
        getData(1,setUpStatus);
    }

    Qm.getSelectedCount = function () {
        return 1;
    }

    Qm.getValueByName = function (name) {
        if (Qm.config.currentData) {
            return Qm.config.currentData[name];
        }
        return "";
    }

    /**
     * public
     */
    Qm.getValuesByName = function (name) {
        var arr = [];
        if (Qm.config.currentData) {
            arr[0] = Qm.config.currentData[name];
        }
        return arr;
    }

    /**
     * public 刷新页面
     */
    Qm.refreshPage = function () {
        window.location.reload();
    }
    Qm.listClick = function () {
    	var index = this.index;
        Qm.config.currentData = Qm.config.data[index];
        if (Qm.userConfig.tbar) {
        	var btnArray = [];
        	if(Qm.userConfig.listeners.seclectChange){
        		Qm.userConfig.listeners.seclectChange();
        	}
        	if (Qm.userConfig.tbar.length > 1) {
        		mui.each(Qm.userConfig.tbar, function (index, item) {
        			if(Qm.config.tbarStatus[item.id]!=false){
        				btnArray.push({title:item.text,index:index});
        			}
                });
        		plus.nativeUI.actionSheet( {
        			cancel:"取消",
        			buttons:btnArray
        		},function(e){
        			if(e.index>0){
        				var ind = btnArray[e.index-1].index;
            			Qm.userConfig.tbar[ind].click();
        			}
        		})
        	}else{
        		 Qm.userConfig.tbar[0].click();
        	}
        }
    }
    
    Qm.listLongClick = function(){
    	if(Qm.userConfig.listeners.longtap){
    		var index = this.index;
            Qm.config.currentData = Qm.config.data[index];
    		Qm.userConfig.listeners.longtap();
    	}
    }
    
    Qm.listSwipeLeft = function(){
    	if(Qm.userConfig.listeners.swipeleft){
    		var index = this.index;
            Qm.config.currentData = Qm.config.data[index];
    		Qm.userConfig.listeners.swipeleft();
    	}
    }
    Qm.listswipeRight = function(){
    	if(Qm.userConfig.listeners.swiperight){
    		var index = this.index;
            Qm.config.currentData = Qm.config.data[index];
    		Qm.userConfig.listeners.swiperight();
    	}
    }
    Qm.listHold = function(){
    	if(Qm.userConfig.listeners.hold){
    		var index = this.index;
            Qm.config.currentData = Qm.config.data[index];
    		Qm.userConfig.listeners.hold();
    	}
    }
    
    window.addEventListener('searchEvent', function(event) {
		var searchKey = event.detail.searchKey;
		Qm.searchData(searchKey);
	});
    
    window.addEventListener('navRigthBtnEvent', function(event) {
		var type = event.detail.type;
		if(type==0){
			Qm.userConfig.popBar[0].click();
		}else{
			mui("#topPopover").popover("toggle");
		}
	});
})(mui);


function beforeScrollStart(event, data) {
    var title = "";
    if (Qm.config.currentPage == 1 || Qm.config.currentPage == Qm.config.pages) {
        title = Qm.config.note;
    } else {
        title = "释放加载数据 " + Qm.config.note;
    }
    data.iscrollview.options.pullDownPulledText = data.iscrollview.options.pullUpPulledText = title;
}

function onPullDownWithOutPage(){
	var data = mui('#pullrefresh');
    getData(1, function(){
    	data.pullRefresh().endPulldownToRefresh(); 
    	if (Qm.config.currentPage == Qm.config.pages || Qm.config.pages ==0) {
        	data.pullRefresh().disablePullupToRefresh();
        }else{
        	data.pullRefresh().enablePullupToRefresh();
        	data.pullRefresh().refresh(true);
        }
    });
}

function onPullDown() {
	var data = mui('#pullrefresh');
    if (Qm.config.currentPage == 1) {
    	data.pullRefresh().endPulldownToRefresh(); 
    	return;
    }
    var page = countPage(-1);
    getData(page, function(){
    	data.pullRefresh().endPulldownToRefresh(); 
    	data.pullRefresh().enablePullupToRefresh();
    	data.pullRefresh().refresh(true);
    });
}

function onPullUp() {
	var data = mui('#pullrefresh');
    if (Qm.config.currentPage == Qm.config.pages) {
    	data.pullRefresh().endPullupToRefresh(true); 
    	data.pullRefresh().disablePullupToRefresh();
    	return;
    }
    var page = countPage(1);
    getData(page, setUpStatus);
}
function setUpStatus(){
	var data = mui('#pullrefresh');
	if (Qm.config.currentPage == Qm.config.pages || Qm.config.pages ==0) {
    	data.pullRefresh().endPullupToRefresh(true); 
    	data.pullRefresh().disablePullupToRefresh();
    }else{
    	data.pullRefresh().endPullupToRefresh();
    	data.pullRefresh().enablePullupToRefresh();
    	data.pullRefresh().refresh(true);
    }
}
function countPage(n) {
    if (!Qm.config.currentPage) {
        Qm.config.currentPage = 1;
    }
    var page = parseInt(Qm.config.currentPage);
    page = isNaN(page) ? 1 : page;
    page += n;
    if (page < 1)page = 1;
    if (page > Qm.config.pages)page = Qm.config.pages;
    Qm.config.currentPage = page;
    return page;
}

function getParams(value) {
    var params = [];
    if (Qm.userConfig.sp_fields && Qm.userConfig.sp_fields.length > 0) {
        mui.each(Qm.userConfig.sp_fields, function (index, items) {
            var item = items;
            if (items.group) {
                for (var i in items.group) {
                    item = items.group[i];
                    if (item.value)
                        params.push({
                            "logic": item.logic || "and",
                            "label": item.fieldLabel,
                            "name": item.name,
                            "compare": item.compare,
                            "dataType": item.dataType,
                            "value": item.value||value
                        });
                }
            } else {
                params.push({
                    "logic": item.logic || "and",
                    "label": "",
                    "name": item.name,
                    "compare": item.compare,
                    "dataType": item.dataType,
                    "value": item.value||value
                });
            }
        });
    }
    return params;
}
function getData(page, callback) {
    var rows = parseInt(Qm.config.pagesize);
    var start = (page - 1) * rows;
    var o = [];
    o.push({name: "pageid", value: Qm.config.pageid});
    o.push({name: "start", value: start});
    o.push({name: "pagesize", value: Qm.config.pagesize});
    o.push({name: "searchPara", value: mui.toJSON(Qm.getSearchPara())});
    o.push({name: "defaultSearchCondition", value: mui.toJSON(Qm.config.defaultSearchCondition)});
    o.push({name: "sessionAdvancedCondition", value:mui.toJSON(Qm.getSessionAdvancedCondition())});
    o.push({name: "sortInfo",value:mui.toJSON(Qm.config.sortInfo)});//增加排序条件
    var wt=plus.nativeUI.showWaiting();
    mui.getJSON(Qm.pagingUrlPre + "q", o, function (res) {
    	wt.close();
        var total = res["total"];
        Qm.config.pages = parseInt((total + Qm.config.pageSize - 1) / Qm.config.pageSize);
        Qm.config.currentPage = res.page;
        //Qm.config.note = "第 " + Qm.config.currentPage + "/" + Qm.config.pages + " 页，共 " + total + " 条";
        //Qm.config.data = res.rows;翻页方式
        //append 方式
        var length = isNull(Qm.config.data)?0:Qm.config.data.length;
        //判断长度是否存在
        if(page==1){
        	length = 0;
        	Qm.config.data = res.rows;
        }else{
        	Qm.config.data = Qm.config.data.concat(res.rows);
        }
        //append方式结束
        if (Qm.config.listModel == "listview") {
        	var list = document.getElementById("__qm_list");
        	//append方式 翻页方式只需要去掉if
        	if(page==1){
        		list.innerHTML="";
        	}
            if (res.rows.length > 0) {
            	//for (var i = 0; i < res.rows.length; i++) {翻页方式
            	//append方式
                for (var i = length; i < length+res.rows.length; i++) {
                	//var li = parseDom(nano(Qm.config.listModelTpl, res.rows[i]))[0];翻页
                	var li = parseDom(nano(Qm.config.listModelTpl, Qm.config.data[i]))[0];//append
                	li.index = i;
                	list.appendChild(li);
                	li.addEventListener("tap",Qm.listClick);
                	li.addEventListener("longtap",Qm.listLongClick);
                	li.addEventListener("swipeleft",Qm.listSwipeLeft);
                	li.addEventListener("swiperight",Qm.listswipeRight);
                }
            }else{
            	list.innerHTML = '<li style="text-align:center;overflow:hidden;position:relative;-webkit-touch-callout: none;padding: 11px 15px;">没有数据！</li>';
            }
        } else {
        	//加载数据
        }
        if(callback){
        	callback();
        }
    });
}
function popoverView(){
	mui('#topPopover .mui-scroll-wrapper').scroll();
	mui('body').on('shown', '.mui-popover', function(e) {
		mui('.mui-popover').off("tap");
		mui('.mui-popover').on('tap', 'li', function() {
			mui("#topPopover").popover("toggle");
			if(Qm.userConfig.popBar[this.getAttribute("index")].click){
				Qm.userConfig.popBar[this.getAttribute("index")].click();
			}
		});
	});
	mui('body').on('hidden', '.mui-popover', function(e) {});
}


function nano(template, data) {
    return template.replace(/\{([\w\.]*)\}/g, function (str, key) {
        var keys = key.split("."), v = data[keys.shift()];
        for (var i = 0, l = keys.length; i < l; i++) v = v[keys[i]];
        return (typeof v !== "undefined" && v !== null) ? v : "";
    });
}
function parseNote() {
    if (Qm.config.note) {
        var label = $("#__qm_iscroll_content .iscroll-pull-label");
        label.html("<span style='color: blue;'>" + Qm.config.note + "</span>");
    }
}

function parseDom(arg){
	var objE = document.createElement("div"); 
	objE.innerHTML = arg; 
	return objE.childNodes; 
}

isNull = function(s) { //检查值是否为空
	return s == "" || s === undefined || s === null;
}

var isArray = function(obj) { 
	return Object.prototype.toString.call(obj) === '[object Array]'; 
}
/////////////////////////////////////////////////////////