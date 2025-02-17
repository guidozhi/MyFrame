/**
 *报告领取
 */
var rtShowNew = false;//判断正在显示的是不是最新数据
var rtNewCache = [];//缓存新出的报告
var rtOverlays = [];//缓存新出的报告图标
var rtSequence = 0;
var rtInterval = null;
//清除报告领取图标
function clearRtOverlays(){
	if(rtOverlays.length>0){
		for(var i=0;i<rtOverlays.length;i++){
			map.removeOverlay(rtOverlays[i]);
		}
	}
	rtOverlays.length = 0;
}
//初始化时填充缓存，当有新的时候重新查询
function rtQuery(){
	$.post("gis/device/rtQuery.do",{minute:1},function(data){
			if(data.success){
				//滚动字幕
				if(data.data2.length>0){
					var con = data.data2;
					console.log(con.length);
					for(var i=0;i<con.length;i++){
						changeScrollContent("抓取"+con[i].COMPANY_NAME+"的新增"+con[i].DEVICE_NAME+"报告"+con[i].NUMB+"份");
					}
				}
				/*//气球标识
				var items = data.data;
				var len = checkDevices(items);
				if(items.length>0&&len>0){
					rtShowNew = true;
					rtNewCache = data.data;
					//清空
					clearRtOverlays();
					stopShowRtCache();
					//清除定时器
					clearInterval(rtInterval);
					//更新通知栏
					for(var i=0;i<rtNewCache.length;i++){
						notice(rtNewCache[i],{type:'report',isNew:true});
					}
					//顺序显示微信查询
					rtSequence = 0;
					rtInterval = setInterval(function(){
							if(rtOverlays.length >= 8){
								map.removeOverlay(rtOverlays[0]);
								rtOverlays.shift();
							}
							var flag = true;
							for(var i=0;i<rtOverlays.length;i++){
								if(rtNewCache[rtSequence].ID == rtOverlays[i]._index)
									flag = false;
								break;
							}
							if(flag){
								queryStar(rtNewCache[i],{"type":"report","isNew":true});
							}
							//当最后一个显示完时清除定时器
							rtSequence++;
							if(rtSequence==rtNewCache.length){
								clearInterval(rtInterval);
							}
					},3000);
				}else if(items.length==0&&!isClientQuery){
					if(rtShowNew){//没有显示新的才启动
						clearRtOverlays();
						queryRtCache();
						startShowRtCache(10,12000);
					}
					rtShowNew=false;
				}*/
			}
			
		}
	);
}

function checkDevices(items){
	if(items.length==0){
		return 0;
	}
	var num = 0;
	for(var i=0;i<items.length;i++){
		//有些没坐标
		if(!items[i].LONGITUDE || items[i].LONGITUDE == '' || items[i].LONGITUDE == null 
				||!items[i].LATITUDE || items[i].LATITUDE == '' || items[i].LATITUDE == null)
		{
			var obj = items[i];
			myGeo.getPoint(obj.DEVICE_USE_PLACE, function(point,obj){
				if (point) {
					obj.LONGITUDE = point.lng;
					obj.LATITUDE = point.lat;
					//反写到base_device_document
					writePointToDocument(obj.ID,obj.LONGITUDE,obj.LATITUDE);
					num++;
				}
			}, "成都市");
		}else{
			num++;
		}
	}
	return num;
}