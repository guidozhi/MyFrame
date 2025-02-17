var LC_HISTORY = new Array();

/**
 * 与后台方法通讯，获取图表XML
 * @param chartid 在配置中定义的图表ID
 * @param target 用于展现图表的DIV的id
 * @param params 参数（JSON格式）
 * @param type 参数（type类型）//码表对应的值
 * @return
 * @throws Exception
 */
function RenderChart(chartid,target,params,type,callback)
{
	var p = (params=="")?"{}":params;
	//$("body").mask("正在加载....");
	$.post("lchart/view/getChartXml.do",{chartid:chartid,param:p,rnd:Math.random()}, function(res) {
		//$("body").unmask();
		if (res.success) {
			var xml = res.data.xml.replace(/\&quot;/g, "'");
			pushHisXml(chartid,xml);
			// flash显示
			var chartType = $.kh.isNull(type)?res.data.type:type;
			var chart = new FusionCharts("pub/chart/FusionCharts/Charts/"+chartType+".swf", chartid, "100%", "100%", "0", "1");
			chart.setDataXML(xml);
			chart.setTransparent(true);
			chart.render(target);
			/*//Html5 SVG显示
			var chart = new FusionCharts({
			  "type": chartType,
			  "id": chartid,
			  "renderAt": target,
			  "width": "100%",
			  "height": "100%",
			  "dataFormat": "xml",
			  "dataSource": res.data.xml
			});
			chart.render();*/
			var oldchartId = $("#"+target).parent().attr("id");
			if($.kh.isNull(oldchartId)){
				oldchartId = target+"_pid";
				$("#"+target).parent().attr("id",oldchartId)
			}
			if($("#"+target).parent().css("position")=="static"){
				$("#"+target).parent().css("position","relative");
			}
			var html = "<div style='z-index:188;position:absolute;right:5px;top:5px;'>";
			if(res.data.chartInfo.IS_SWICH=='1'){
				html +="<div class='chart_type_switch' title='切换'><img src='pub/chart/css/icons/switch.png'/></div>";
			}
			if(res.data.chartInfo.IS_BIG=='1'){
				html +="<div class='zoom-opr zoom-in' title='放大'><img src='pub/chart/css/icons/zoom-in.jpg' class='zoom-btn' onClick='big(\""+target+"\",\""+oldchartId+"\")' /></div>"; 
			}
			html += "</div>";
			// 动态显示放大按钮
			$("#"+target).parent().bind("mouseover",function(){
				$(this).find(".zoom-opr").show();
				$(this).find(".chart_type_switch").show();
			})
			$("#"+target).parent().bind("mouseout",function(){
				$(this).find(".zoom-opr").hide();
				$(this).find(".chart_type_switch").hide();
			})
			var $html = $(html);
			$html.find(".chart_type_switch img").click(function(){
				 swich(target,res.data.chartInfo.SWICH_TYPES,res.data.chartInfo.SWICH_TYPES_TXT,chartid,params,xml,callback);
			})
			$("#"+target).parent().append($html);
			if(callback){
				callback(xml);
			}
		}
		else
		{
			alert("数据处理错误");
		}
	});
}


function swich(target,type,type_txt,chartid,params,xml,callback){
	if(!$.kh.isNull(type)){
		if($(".switch_div").length<1){
			var html = "<div class='switch_div' id='rMenu'></div>";
			$("body").append(html);
		}
		$(".switch_div ul").remove();
		var ul = "<ul>"
		var typearr = type.split(",");
		var typetextarr = type_txt.split(",");
		for(var i in typearr){
			ul+= "<li code='"+typearr[i]+"'>"+typetextarr[i]+"</li>"
		}
		ul +="</ul>"
		$ul = $(ul);
		$ul.find("li").click(function(){
			_swich($(this),chartid,target,params,xml,callback);
		});
		$(".switch_div").append($ul);
		var offset = $("#"+target).parent().find(".chart_type_switch").offset();
		$(".switch_div").css({"top":(offset.top-$("body").scrollTop())+"px", "left":(offset.left-80)+"px"}).show();
		$("body").bind("mousedown", onBodyMouseDown);
	}else{
		$.ligerDialog.alert("还未设置切换图表类别！");
	}
}

function _swich(obj,chartid,target,params,xml,callback){
	var chartRef = FusionCharts(chartid);
	//xml = chartRef.getXML();
	chartRef.dispose();
	var chart = new FusionCharts("pub/chart/FusionCharts/Charts/"+$(obj).attr("code")+".swf", chartid, "100%", "100%", "0", "1");
	chart.setDataXML(xml);
	chart.setTransparent(true);
	chart.render(target);
	$("#rMenu").hide();
}

function onBodyMouseDown(event){
	if (!(event.target.id == "rMenu" || $(event.target).parents("#rMenu").length>0)) {
		$("#rMenu").hide();
	}
}

function showZindex(){
	$("g[class*='contextmenu-container'] ").each(function(){
		$(this).parent().css("z-index",199)
	})
}
function big(obj,oldid){
	var html = "<div id='bigdiv' style='z-index:199;overflow:auto;background-color: #ffffff;position:absolute;left:0px;top:0px;width: 100%;height:100%'>"
		+ "<div class='zoom-opr zoom-out' title='还原'><img src='pub/chart/css/icons/zoom-out.jpg' onclick='closeB(\""+obj+"\",\""+oldid+"\")' /></div></div>";
	$("#bigdiv").remove();
	$("body").append(html);
	$("#bigdiv").show();
	$("#bigdiv").append($("#"+obj));
}
function closeB(obj,oldid){
	$("#"+oldid).append($("#"+obj));
	$("#bigdiv").hide();
}

/**
无下层图表数据后对外的接口，需要时覆盖此方法即可
*/
function LC_NoSubChart(chartid,params)
{
	var wObj = top.$.dialog({
		id: "chart_sub",
		icon: "k/kui/images/icons/dialog/32X32/i.png",
		width: 120,
		height: 80,
		parent: api,
		title: "系统提示",
		//cancel: true,
		//ok: false,
		content: '已经是最下层！'
	});
}

/**
无上层图表数据后对外的接口，需要时覆盖此方法即可
*/
function LC_NoTopChart(chartid)
{
	var wObj = top.$.dialog({
		id: "chart_top",
		icon: "k/kui/images/icons/dialog/32X32/i.png",
		width: 120,
		height: 80,
		parent: api,
		title: "系统提示",
		//cancel: true,
		//ok: false,
		content: '已经是最顶层！'
	});
}

/**
使用自定义按钮实现上钻时调用此方法
参数名：chartId(调用名称)
*/
function LC_GotoBack(cid)
{
	gotoBack(cid);
}

/**
点击图表后对外的接口，需要时覆盖此方法即可
参数名：chartId(调用名称)
*/
function LC_ClickChart(params)
{
	
}

function RefreshChart(chartid,params)
{
	$.post("lchart/view/getChartXml.do",{chartid:chartid,param:params}, function(res) {
		if (res.success) {
			if(!$.kh.isNull(res.data)){
				if (res.data.xml!="0")
				{
					pushHisXml(chartid,res.data.xml=="null"?"":res.data.xml.replace(/\&quot;/g, "'"));
					var chartRef = FusionCharts(chartid);
					chartRef.setDataXML(res.data.xml=="null"?"":res.data.xml.replace(/\&quot;/g, "'"));
				}
				else
				{
					LC_NoSubChart(chartid,params);
					return;
				}
			}else{
				var chartRef = FusionCharts(chartid);
				chartRef.setDataXML("");
			}
		}
		else
		{
			alert("数据处理错误");
			return;
		}
	});
}

function RefreshChartByXml(chartid,xml)
{
	var chartRef = FusionCharts(chartid);
	chartRef.setDataXML(xml);
}

function testAlert(p){
	//var ps = $.parseJSON(p);
	alert(p);
}

function gotoURL(p)
{
	var ps = p.split(",,");
	var url = ps[0];
	var linkType = ps[1];
	var param = ps[2];
	if (linkType=="_self")
	{
		window.location=url;
	}
	else if (linkType=="_top")
	{
		var pr = $.parseJSON(param);
		var w = (pr.width==undefined)? $(window).width():pr.width;
		var h = (pr.height==undefined)? $(window).height():pr.height;
		var t = (pr.title==undefined)?"":pr.title;
		
		var wObj = top.$.dialog({
			id: "chart_open",
			width: w,
			height: h,
			parent: api,
			title: t,
			data:{param:pr},
			content: 'url:'+url
		});
	}
	else  //_blank
	{
		window.open(url,"blank");
	}
	LC_ClickChart(param);
}

function pushHisXml(cid,xml)
{
	try
	{
		var hisNum = -1;
		$.each(LC_HISTORY,function(i,n){
			if (n.cid==cid)
			{
				if (hisNum<parseInt(n.his))
				{
					hisNum = n.his;
				}
			}
		});	
		var hisObj = new LCHistory(cid,hisNum+1,xml);
		LC_HISTORY.push(hisObj);
	}
	catch(e)
	{
		alert(e);	
	}
}

function gotoCharts(p)
{
	var ps = p.split(",,");
	var chartid = ps[0];
	var cids = ps[1];
	var linkType = ps[2];
	var param = ps[3];
	
	if (linkType=="_top")
	{
		var pr = $.parseJSON(param);
		var charts = cids.split(",");
		var w = (pr.width==undefined)? 600:pr.width;
		var h = (pr.height==undefined)? 400:pr.height;
		var t = (pr.title==undefined)?"":pr.title;
		var wObj = top.$.dialog({
			//id: "chart_open",
			width: parseInt(w),
			height: parseInt(h),
			parent: api,
			title: t,
			max: false,
    		min: false,
    		data:{param:pr,chartId:charts},
			//cancel: true,
			//ok: false,
			content: "url:pub/chart/lchart_win.jsp?c="+charts[0]
		});
	}
	else  //_selft _blank
	{
		var charts = cids.split(",");
		for (var i=0;i<charts.length;i++)
		{
			RefreshChart(charts[i],param);
		}
	}
	LC_ClickChart(param);
}

function gotoBack(cid)
{
	try
	{
		var xml = "";	
		var hisNum = -1;
		var hisIndex = 0;
		$.each(LC_HISTORY,function(i,n){
			if (n.cid==cid)
			{
				if (hisNum<parseInt(n.his)) 
				{
					hisNum = n.his;
					hisIndex = i;
				}
			}
		});
		if (hisNum>0)
		{
			LC_HISTORY.splice(hisIndex,1);
			hisNum = -1;
			$.each(LC_HISTORY,function(i,n){
				if (n.cid==cid)
				{
					if (hisNum<parseInt(n.his))
					{
						hisNum = n.his;
						xml = n.xml;
					}
				}
			});
			RefreshChartByXml(cid,xml);
		}
		else
		{
			LC_NoTopChart(cid);
		}
	}
	catch(e)
	{
		//alert(e);	
	}
}

function LCHistory(cid,his,xml)
{
	this.cid = cid;
	this.his = his;
	this.xml = xml;
}
