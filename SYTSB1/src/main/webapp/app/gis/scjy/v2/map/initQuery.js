//今天被检验设备
var todayDevices = {};

$(function(){
	initCount();
	quartz();
});
var rtCache = {};//缓存各部门出的报告
var rtCacheInterval = null;
var rtCacheOverlays = {'d1':[],'d2':[],'d3':[],'d4':[],'d5':[],'d6':[],'d7':[],'d8':[],'d9':[],'d10':[],'d11':[],'d12':[],'d13':[],'d14':[],'d15':[],'d16':[]}
var wxCache = {};//缓存各部门出的报告
var wxCacheInterval = null;
var wxCacheOverlays = [];
//更新统计数据
function htmlCount(obj){
	$("#m-ds1-num").html(obj.annual[0]);
	$("#m-ds2-num").html(obj.yesterday[0]);
	$("#m-ds3-num").html(obj.today[0]);
}
//下方总数统计（初始化）
function initCount(){
	$.post("gis/device/static/initCount.do",{},function(res){
		if(res.success){
			var count = res.data;
			htmlCount(count);
			//初始化今日5分钟前的设备
			var devices = count.todayDevices;
			for(var i=0;i<devices.length;i++){
				todayDevices[devices[i].ID]= devices[i];
			}
		}else{
			alert("loading failure");
		}
	});
}
function countWeixin(){
	$.post("gis/device/static/queryWxCount.do",{},function(res){
		if(res.success){
			var count = res.wxc;
			$("#m-ds0-num").html(count[0]);
		}
	});
}
//查询最新检验的设备
function queryTodayList(){
	$.post("gis/device/static/queryTodayList.do",{},function(data){
			if(data.success){
				var items = data.data;
				var addDevices = [];
				if(items.length>0){
					for(var i=0;i<items.length;i++){
						if(!todayDevices[items[i].ID]){
							addDevices.push(items[i]);
							todayDevices[items[i].ID]= items[i];
						}
					}
					if(addDevices.length>0){
						mapAddDevices(addDevices);
					}
				}
			}
		}
	);
}

function queryRtCache(){
	$.ajax({
		url:"gis/device/static/rtCache.do",
		type:"post",
		data:{},
		dataType:"json",
		success:function(data){
			if(data.success){
				rtCache = {};
				for(var i=1;i<=16;i++){
					if(data['d'+i].length>0){
						rtCache['d'+i]=data['d'+i];	
					}
				}
			}
		}
	});
}
function queryWxCache(){
	$.ajax({
		url:"gis/device/static/wxCache.do",
		type:"post",
		data:{},
		dataType:"json",
		success:function(data){
			if(data.success){
				wxCache = data.data;
			}
		}
	});
}
function startShowRtCache(num,speed){
	//清除定时器
	if(rtCacheInterval){
		clearInterval(rtCacheInterval);
		rtCacheInterval = null;
	}
	rtCacheInterval = setInterval(function(){
		$.each(rtCacheOverlays,function(key,value){
			if(value.length>=num){
				var delNum = Math.floor(Math.random()*num+1);
				for(var i=0;i<delNum;i++){
					map.removeOverlay(value[0]);
					value.shift();
				}
			}
		});
		$.each(rtCache,function(key,value){
			showNotRepeat(rtCache,key,0);
		});
		
		/*$.each(rtCache,function(key,value){
			var jn = Math.floor(Math.random()*rtCache[key].length);
			var flag = true;
			for(var i=0;i<rtCacheOverlays[key].length;i++){
				if(rtCache[key][jn].ENTER_OP_NAME == rtCacheOverlays[key][i]._element.ENTER_OP_NAME){
					console.log(rtCache[key][jn].ENTER_OP_NAME+"又重复了！")
					flag=false;
					break;
				}else{
					console.log("report did not repeat！")
				}
			}
			if(flag){
				queryStar(rtCache[key][jn],{"type":"report","box":"rtCacheOverlays","cbox":key});
			}
		});*/
	}, speed);
}
function showNotRepeat(cache,key,num){
		var jn = Math.floor(Math.random()*cache[key].length);
		var flag = true;
		if(rtCacheOverlays[key]){
			for(var i=0;i<rtCacheOverlays[key].length;i++){
				if(cache[key][jn].ENTER_OP_NAME == rtCacheOverlays[key][i]._element.ENTER_OP_NAME){
					console.log(rtCache[key][jn].ENTER_OP_NAME+"又重复了！")
					flag=false;
					num++;
					if(num < 11){
						showNotRepeat(cache,key,num);
					}
					break;
				}
			}
		}
		if(flag){
			queryStar(rtCache[key][jn],{"type":"report","box":"rtCacheOverlays","cbox":key});
		}
}
function stopShowRtCache(){
	if(rtCacheInterval){
		clearInterval(rtCacheInterval);
		rtCacheInterval = null;
	}
	$.each(rtCacheOverlays,function(key,value){
		for(var i=0;i<value.length;i++){
			map.removeOverlay(value[i]);
		}
		value.length=0;
	});
}
function startShowWxCache(num,speed){
	if(wxCacheInterval){
		clearInterval(wxCacheInterval);
		wxCacheInterval = null;
	}
	wxCacheInterval = setInterval(function(){
		if(wxCacheOverlays.length>=num){
			var delNum = Math.floor(Math.random()*num+1);
			for(var i=0;i<delNum;i++){
				map.removeOverlay(wxCacheOverlays[0]);
				wxCacheOverlays.shift();
			}
		}
		var jn = Math.floor(Math.random()*wxCache.length);
		var flag = true;
		for(var i=0;i<wxCacheOverlays.length;i++){
			if(wxCache[jn].ID == wxCacheOverlays[i]._index){
				flag = false;
				break;
			}else{
				console.log("微信没重复！");
			}
		}
		if(flag){
			queryStar(wxCache[jn],{"type":"weixin","box":"wxCacheOverlays"});
		}
	}, speed);
}
function stopShowWxCache(){
	if(wxCacheInterval){
		clearInterval(wxCacheInterval);
		wxCacheInterval = null;
	}
	for(var i=0;i<wxCacheOverlays.length;i++){
		map.removeOverlay(wxCacheOverlays[i]);
	}
	wxCacheOverlays.length=0;
}
//定时器
var isFlush = false;
function quartz() {
    var now = new Date(); // 得到当前时间
    
    var hr = now.getHours();
    var mn = now.getMinutes();
    var sc = now.getSeconds();
    
    if(hr== 0 && mn == 0 && sc < 10 && !isFlush){
    	initCount();
    	isFlush = true;
    	setTimeout(function(){
    		isFlush = false;
    	},11000);
    }
    setTimeout(quartz,1000); //在1秒后再次执行
}

