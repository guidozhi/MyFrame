function openStarLayer()
{
	if (map==null) {
		alert('map initing...');
		return;
	}
	if (document.createElement('canvas').getContext) {
		/*var  mapStyle ={ 
				features: ["road", "building","water","land"],//隐藏地图上的poi
				style : "dark"  //设置地图风格为高端黑
			}
		map.setMapStyle(mapStyle);*/
	
		var BW            = 0,    //canvas width
			BH            = 0,    //canvas height
			ctx           = null,
			stars         = [],   //存储所有星星对象的数组
			timer         = null, //定时器
			timeLine      = null, //时间轴对象
			rs            = [],   //最新的结果
			isNowTimeData = false, //是否显示当前时间的定位情况
			py            = null, //偏移
			gridWidth     = 10000,//网格的大小
			isOverlay     = false,//是否叠加
			//gridWidth   = 1,//网格的大小
			canvas        = null; //偏移
	
		function Star(options){
			this.init(options);
		}
	
		Star.prototype.init = function(options) {
			this.x   = ~~(options.x);
			this.y   = ~~(options.y);
			this.initSize(options.size);
			this.color = options.color;
			if (~~(0.5 + Math.random() * 7) == 1) {
				this.size = 0;
			} else {
				this.size = this.maxSize;
			}
		}
	
		Star.prototype.initSize = function(size) {
			var size = ~~(size);
			this.maxSize = size > (map.getZoom()-7) ? (map.getZoom()-7) : size;
		}
	
		Star.prototype.render = function(i) {
			var p = this;

			if(p.x < 0 || p.y <0 || p.x > BW || p.y > BH) {
				return;
			}

			ctx.beginPath();
			var gradient = ctx.createRadialGradient(p.x, p.y, 0, p.x, p.y, p.size);
			gradient.addColorStop(0, "rgba(7,120,249,1)");
			gradient.addColorStop(1, "rgba(7,120,249,0.3)");
			/*if (p.color=="1")//超期
			{
				gradient.addColorStop(0, "rgba(255,0,0,1)");
				gradient.addColorStop(1, "rgba(255,0,0,0.3)");
			}
			else if (p.color=="2")//预警
			{
				gradient.addColorStop(0, "rgba(255,216,0,1)");
				gradient.addColorStop(1, "rgba(255,216,0,0.3)");
			}
			else if (p.color=="3")//正常
			{
				gradient.addColorStop(0, "rgba(0,255,0,1)");
				gradient.addColorStop(1, "rgba(0,255,0,0.3)");
			}*/
			ctx.fillStyle = gradient;
			ctx.arc(p.x, p.y, p.size, Math.PI*2, false);
			ctx.fill();
			if (~~(0.5 + Math.random() * 7) == 1) {
				p.size = 0;
			} else {
				p.size = p.maxSize;
			}
		}
	
		function render(){
			renderAction();
			setTimeout(render, 180);
		}
	
		function renderAction() {
			ctx.clearRect(0, 0, BW, BH);
			ctx.globalCompositeOperation = "lighter";
			for(var i = 0, len = stars.length; i < len; ++i){
				if (stars[i]) {
					stars[i].render(i);
				}
			}
		}
	
	
		// 复杂的自定义覆盖物
		function ComplexCustomOverlay(point){
		  this._point = point;
		}
		ComplexCustomOverlay.prototype = new BMap.Overlay();
		ComplexCustomOverlay.prototype.initialize = function(map){
		  this._map = map;
		  canvas = this.canvas = document.createElement("canvas");
		  canvas.id = "starLay";
		  canvas.style.cssText = "position:absolute;left:0;top:0;z-index:-99999;";
		  ctx = canvas.getContext("2d");
		  var size = map.getSize();
		  canvas.width = BW = size.width;
		  canvas.height = BH = size.height;
		  map.getPanes().labelPane.appendChild(canvas);
		  //map.getContainer().appendChild(canvas);
		  return this.canvas;
		}
		ComplexCustomOverlay.prototype.draw = function(){
			var map = this._map;
			var bounds = map.getBounds();
			var sw = bounds.getSouthWest();
			var ne = bounds.getNorthEast();
			var pixel = map.pointToOverlayPixel(new BMap.Point(sw.lng, ne.lat));
			py = pixel;
			if (rs.length > 0) {
				showStars(rs);
			}
		}
		var myCompOverlay = new ComplexCustomOverlay(new BMap.Point(104.06792346,30.65994285));
		map.addOverlay(myCompOverlay);
	
		var project = map.getMapType().getProjection();
		var bounds = map.getBounds();
		var sw = bounds.getSouthWest();
		var ne = bounds.getNorthEast();
		sw = project.lngLatToPoint(new BMap.Point(sw.lng, sw.lat));
		ne = project.lngLatToPoint(new BMap.Point(ne.lng, ne.lat));
	
		//左上角墨卡托坐标点
		var original = {
			x : sw.x,
			y : ne.y
		}
	
		/**
		 * 请求定位数据,并在地图上绘制出
		 * @param 请求的时间
		 * @param 成功后执行的回调函数
		 * 
		 */
		var requestMgr = {
			request: function (time, successCbk) {
				/*if (document.location.host === 'developer.baidu.com') {
					var url = "http://developer.baidu.com/map/jsdemo/data/data";
				} else {
					var url = "../data/data";
				}*/
				/*var url = "zjpt/gis/js/data1";
				var xhr = new XMLHttpRequest();
				xhr.onreadystatechange = function() {
					if( xhr.readyState == 4  && xhr.status == 200 ) {
						if (!isOverlay) {
							rs = JSON.parse(xhr.responseText);
						} else {
							rs = rs.concat(JSON.parse(xhr.responseText));
							if (rs.length > 10000) {
								rs.splice(0, rs.length - 10000);
							}
						}
						showStars(rs);
						if (successCbk) {
							successCbk();
						}
					}
				}
				xhr.open( "GET", url, true );
				xhr.send( null );*/
				//showStars(rs);
				rs = PUB_ZT_DATA;
				showStars(rs);
				if (successCbk) {
					successCbk();
				}
			}
		}
	
		//显示星星
		function showStars(rs) {
			stars.length = 0;
			var temp = {};
			for (var i = 0; i < rs.length; i++) {
				//var item = rs[i];
				/*var addNum = gridWidth / 2;
				var x = item[0] * gridWidth + addNum;
				var y = item[1] * gridWidth + addNum;*/
				//var point = project.pointToLngLat(new BMap.Pixel(x, y));
				
				var point = new BMap.Point(rs[i].JD,rs[i].WD);
				//var point = new BMap.Point(118.08154,43.016465);
				var px = map.pointToOverlayPixel(point);
					//create all stars
					var s = new Star({
						x: px.x - py.x, 
						y: px.y - py.y,
						size: (1+Math.random())*map.getZoom()/3,  //(1+Math.round(rs[i].EDCL))*map.getZoom()/5,
						color: rs[i].QYLX
					});
					stars.push(s);
				//}
			}
			canvas.style.left = py.x + "px";
			canvas.style.top = py.y + "px";
			renderAction();
		}
	
		render();
	
		function nowTimeCbk (time) {
			requestMgr.request(time, function(){
				if (isNowTimeData) {
					setTimeout(function(){
						if (isNowTimeData) {
							startCbk(nowTimeCbk);
						}
					}, 1000);
				}
			});
		}
		function startCbk(cbk){
			var now = new Date();
			var time = {
				hour   : now.getHours(),
				minute : now.getMinutes(),
				second : now.getSeconds()
			};
			if (cbk) {
				cbk(time);
			}
		};
		startCbk(nowTimeCbk);
	} else {
		alert('请在chrome、safari、IE9+以上浏览器查看本示例');
	}
}
