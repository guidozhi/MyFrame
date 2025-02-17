var api = this.window;
//加载后的图片控件前缀，使用过程中会根据触发的控件改变值
var receiptfiles = "receiptfiles";
//储存图片相关信息，包括字段名，宽，高，附件id
var pictures = [];

//图片控件相关参数
var canvas = null;
var context = null;
var slider = null;
var image = new Image()
/**
 * 图片处理 v2.0 
 * 加入了图片的大小编辑功能，保留了图片原有的大小比例
 * 引入此js后，对所有class为uploadPhoto的控件进行初始化，使其能够完成上传图片，调整图片的功能
 * 此js建立在jQuery和公司框架的基础上
 * 
 * 使用此功能需要引入js（head最后引入）
 * <script type="text/javascript" src="pub/fileupload/js/fileupload.js"></script>
 * <script type="text/javascript" src="rtbox/app/templates/default/tpl_photo.js"></script> 
 * 
 *在对应位置添加标记控件 <div name="pictureo1" class="uploadPhoto" style="width: 500px;height: 500px;"></div>
 * 
 * 解释权归pingZhou2017/03/09
 */
function initPicture(){
	
	//循环处理图片标记类
	var n = 1;
    //处理所有图片标记类
	$(".uploadPhoto").each(
			function() {
				//实例 控件处应写的内容
				/*
				 * 
				 * <div name="pictureo1" class="uploadPhoto" style="width: 500px;height: 500px;"></div>
				 * 
				 * */
				//取出字段名，方便保存
				var name = $(this).attr("id")
				var widthD = $(this).css("width");
				var heightD = $(this).css("height");
				var width = $(this).css("width").substring(0,widthD.length-2);
				var height = $(this).css("height").substring(0,heightD.length-2);
				
				//存控件参数
				var picture ={};
				picture["name"]=name;
				picture["width"]=width;
				picture["height"]=height;
				picture["fileid"]="";
				pictures[n-1] = picture;
				//alert($(this).parent("td").html())
				$(this).parent("td").attr("align",'center');
				//alert("加载控件的参数为-----name："+name+"---width:"+width+"--height:"+height)
				var tab = '<table cellpadding="3" cellspacing="0" style="width: 100%;">'
					+'<tr style="display:none;">'
					+'<td class="l-t-td-left">浏览：</td>'
					+'<td  class="l-t-td-right" colspan="3" id="receiptfiles'+n+'DIV">'
					+'<a class="l-button3"  id="receiptfiles'+n+'Btn"  onmouseover="changeUploadBtn(this)">'
					+'<span id="receiptfiles'+n+'O"/></span>'
					+'<div class="l-upload-ok-list"><ul id="receiptfiles'+n+'"></ul></div>'
					+'</td>'
					+'</tr> '
					+'<tr>'
					+'<td  class="l-t-td-right" colspan="4">'
					+'	<input name="'+name+'" id="receiptfiles'+n+'AttchNames" type="hidden" class="receiptfiles'+n+'"/>'
					+'<a id="del'+n+'" style="display:none;float:left;" href="javascript:clearCanvasFile('+n+',this)"><img src="rtbox/app/templates/default/img/emblem-unreadable.png"></a>'
					//+'<a id="cs" href="https://daohang.qq.com/?fr=hmpage">1234532345</a>'
					
					
					//v2.0修改 
					//修改了图片控件
					+'<div align="center">'
					+'<a href="javascript:changePicture(\'receiptfiles'+n+'\');" id="receiptfiles'+n+'A">'
					+' <canvas id="canvas'+n+'" style="display:block;margin:0 auto;border:1px solid #aaa;width: '
					+width+'px;height: '+(height-50)+'px;"';
				if(pageStatus==undefined){
					tab = tab + 'onmouseover="changeCanvas(this)"';
				}else if(pageStatus!=undefined&&pageStatus!="detail"){
					tab = tab + 'onmouseover="changeCanvas(this)"';
				}
				tab = tab + '>'
			   	+' 您的浏览器尚不支持canvas'
			   	+' </canvas>'
			   	+'</a>'
			    +'<input type="range" id="canvas'+n+'-range" min="0.5" max="1.0" step="0.01" value="1.0"'
			    + 'style="display:none;margin:0px auto;width:'+width+'px"  onmouseout="outCanvas(this)"/>'
			    +'</div>'
				
				//+'<img src="" title="点击选择图片" alt="点击选择图片" id="receiptfiles'+n+'P" style="width: '+width+'px;height: '+height+'px;"/>'
				
				+'</td>'
				+'</tr>'+'</table>';
				
				$(this).append(tab);
				new KHFileUploader({
		    			fileSize : "10mb",//文件大小限制
		    			folder:"supervise/order",
		    			businessId : "",//业务ID
		    			buttonId : "receiptfiles"+n+"Btn",//上传按钮ID
		    			container : "receiptfiles"+n+"DIV",//上传控件容器ID
		    			title : "请选择附件",//文件选择框提示
		    			extName : "png,jpg,gif,bmp",//文件扩展名限制
		    			saveDB : true,//是否往数据库写信息
		    			attType : "",//文件存储类型；1:数据库，0:磁盘，默认为磁盘
		    			fileNum : 1,//限制上传文件数目
		    			callback : function(files){//上传成功后回调函数,实现页面文件显示，交与业务自行后续处理
		    				
		    				addAttachFile(files);
		    			}
		    		});
				n++;
			}) 
	
}
//<span id="receiptfiles'+n+'O">+</span>
function deleteFileUp(){
		$("#"+receiptfiles+"Btn").show();
		//$("#procufilesBtn").show();
		$("#"+receiptfiles+"P").attr("src","");
		getUploadFile();
	}

	//将上传的所有文件id写入隐藏框中
	function getUploadFile(){
		var attachId="";
		var i=0;
		$("#"+receiptfiles+" li").each(function(){
			attachId+=(i==0?"":",")+this.id;
			i=i+1;
		});
		if(i>=1){
			$("#"+receiptfiles+"Btn").hide();
		}

		$("#"+receiptfiles+"AttchNames").val(attachId);
	}

/**
 * 图片上传成功后处理及显示
 * @param files
 */
function addAttachFile(files){
		if("${param.status}"=="detail"){
			$("#receiptfilesBtn").hide();
		}
		if(files){
			$.each(files,function(i){
				var data=files[i];
				var c = receiptfiles.substring(receiptfiles.length-1,receiptfiles.length);
				if(pictures[c-1]!=undefined){
					var fileid = pictures[c-1]["fileid"];
					//alert(fileid)
					if(fileid!=""){
						//删除之前上传的附件
						$.getJSON('fileupload/deleteAtt.do?id='+fileid);
					}
				}else{
					pictures[c-1] = {};
				}
				//$("#"+receiptfiles+"P").attr("src","fileupload/downloadByObjId.do?id="+data.id);
				//v2.0改变显示图片方法

				$("#"+receiptfiles+"AttchNames").val(data.id);
				pictures[c-1]["fileid"] = data.id;

				changeCanvasBack(c,data.id);
				/*createFileView(data.id,data.name,$("head").attr("pageStatus")=="detail"?false:true,receiptfiles,true,function(fid){
					deleteFileUp();
				})*/
				
				//alert($("#"+receiptfiles+"AttchNames").val())
				
				//getUploadFile();
			})
		}
	}


/**
 * 当一页页面有多处需要上传图片时，要切换控件
 * @param up
 */
function changeUploadBtn(up){
		var id = $(up).attr("id");
		receiptfiles = id.substring(0,id.length-3);
	}	

/**
 * 图片点击事件
 * @param up
 * @returns
 */
function changePicture(up){
	if(pageStatus!=undefined&&pageStatus=="detail"){
		return;
	}
	top.$.dialog({
		width : 300,
		height : 100,
		lock : true,
		title : "图片",
		content : 'url:rtbox/public/picture.jsp?status=add',
		data : {
			"window" : window,
			"up":up
		}
	});
	
	
	
	
}

function changeCallback(type,up){
	receiptfiles = up;
	if(type==1){
		$("#"+receiptfiles+'O').click();
	}else{
		var n = up.substr(12,up.length);
		var imageId=$("#receiptfiles"+n+'AttchNames').val();
 		//var imageId;
		RtDroc(null,imageId,function(result){
			if(result){
				//console.log("result.imageId:"+result.imageId);
				var c = receiptfiles.substring(receiptfiles.length-1,receiptfiles.length);
				if(pictures[c-1]!=undefined){
					var fileid = pictures[c-1]["fileid"];
					//alert(fileid)
					if(fileid!=""){
						//删除之前上传的附件
						$.getJSON('fileupload/deleteAtt.do?id='+fileid);
					}
				}else{
					pictures[c-1] = {};
				}
				
				//$("#"+receiptfiles+"P").attr("src","fileupload/downloadByObjId.do?id="+data.id);
				//v2.0改变显示图片方法

				$("#"+receiptfiles+"AttchNames").val(result.imageId);
				pictures[c-1]["fileid"] = result.imageId;

				changeCanvasBack(c,result.imageId);
			}
		});
	}
}


//触发改变图片控件事件
function changeCanvas(img){
	var fileId = "";
	var id = $(img).attr("id");
	var n = id.substring(id.length-1,id.length);
	canvas = document.getElementById(id)
	context = canvas.getContext("2d")
	slider = document.getElementById(id+"-range")  
	$("#"+id+"-range").show();
	var n = id.substring(id.length-1,id.length);
	//如果之前没有上传图片，则不执行后面的画图
	if($("#receiptfiles"+n+'AttchNames').val()==""){
		return;
	}else{
		fileId = $("#receiptfiles"+n+'AttchNames').val();
		//alert("#receiptfiles"+n+'AttchNames------'+fileId)
	}
	var image1 = new Image()
	image1.src =  $("base").attr("href")+"fileupload/downloadByObjId.do?id="+fileId;
	var w = $(img).css("width");
	var h = $(img).css("height");
		canvas.width = w.substring(0,w.length-2);
        canvas.height =  h.substring(0,h.length-2);
		 var scale = slider.value
		image1.onload = function(){
		
			 drawImageByScaleWH( scale,image1,n )
            slider.onmousemove = function(){
                scale = slider.value
                drawImageByScaleWH( scale,image1,n )
            }
   		 } 
}

//长传图片显示事件
function changeCanvasBack(n,fileId){
	var id = "canvas"+n;
	canvas = document.getElementById(id)
	context = canvas.getContext("2d")
	slider = document.getElementById(id+"-range")  
	$("#"+id+"-range").show();
	var n = id.substring(id.length-1,id.length);
	//如果之前没有上传图片，则不执行后面的画图
	/*if($("#receiptfiles"+n+'AttchNames').val()!=""){
		return;
	}*/
	var image1 = new Image()
	image1.src = $("base").attr("href")+"fileupload/downloadByObjId.do?id="+fileId;
	//alert( $("base").attr("href")+"fileupload/downloadByObjId.do?id="+fileId)
		 var scale = slider.value
		image1.onload = function(){
			 var w = $("#canvas"+n).css("width");
			var h = $("#canvas"+n).css("height");
			canvas.width = w.substring(0,w.length-2);
			canvas.height =  h.substring(0,h.length-2);
			drawImageByScaleWH( scale,image1,n )
            slider.onmousemove = function(){
                scale = slider.value
                drawImageByScaleWH( scale,image1,n )
            }
			$("#del"+n).show();
   		 } 
}

/**
 * 删除文件，清空画布
 * @param n
 * @returns
 */
function clearCanvasFile(n,delA){
	var fileid= $("#receiptfiles"+n+"AttchNames").val();
	deleteUploadFile(fileid,function(){
		var id = "canvas"+n;
		$("#receiptfiles"+n+"AttchNames").val("");
		$("#del"+n).hide();
		var c=document.getElementById(id);  
	    var cxt=c.getContext("2d");
	    cxt.clearRect(0,0,c.width,c.height);
	    var c = receiptfiles.substring(receiptfiles.length-1,receiptfiles.length);
	    //删除内容
	    pictures.splice(c-1,1);
	})
	
}  

//删除服务器文件
function deleteUploadFile(id, callback) {
    $.ligerDialog.confirm("确定删除文件？<p style='color:red'>注意：删除后不可恢复！</p>", function(flag) {
		if(flag){
			$("body").mask("正在删除，请稍后……");
		    $.getJSON('fileupload/deleteAtt.do',{"id":id}, function(resp) {
		    	if(resp.success){
		    		alert(resp.success)
		    		if(callback){
		    			callback(id);
		    		}
					$("body").unmask();
		    	}
		    	else{
					$("body").unmask();
		    		alert("删除失败！");
		    	}
		    });
		}
    });
}

//不编辑图片时影藏比例条
function outCanvas(img){
	$(img).hide();
}

function drawImageByScale(scale,images ){
	var b1 = canvas.width/images.width;
	var b2 = canvas.height/images.height;
	var bl = 0;
	if(b1<b2){
		bl = canvas.width / images.width;
	}else{
		bl = canvas.height / images.height;
	}
	
	
	var h = (images.height)*bl;
	var w = (images.width)*bl;
	
    var imageWidth = w * scale
    var imageHeight = h * scale
    
    //记录大小信息
    var c = receiptfiles.substring(receiptfiles.length-1,receiptfiles.length);
    //alert(c)
    pictures[c-1]["width"] = imageWidth;
    pictures[c-1]["height"] = imageHeight;
    pictures[c-1]["scale"] = scale;
    
    //var sx = imageWidth / 2 - canvas.width / 2
    //var sy = imageHeight / 2 - canvas.height / 2

    //context.drawImage( image , sx , sy , canvas.width , canvas.height , 0 , 0 , canvas.width , canvas.height )
    x = canvas.width /2 - imageWidth / 2
    y = canvas.height / 2 - imageHeight / 2
   // alert(imageWidth+"---"+imageHeight+"---"+x+"-----"+y)
    context.clearRect( 0 , 0 , canvas.width , canvas.height )
    context.drawImage( images , x , y , imageWidth , imageHeight )
}

//加载时图片显示事件
function changeCanvasInit(n,fileId,w,h){
	var id = "canvas"+n;
	canvas = document.getElementById(id)
	context = canvas.getContext("2d")
	slider = document.getElementById(id+"-range")  
	$("#"+id+"-range").show();
	var n = id.substring(id.length-1,id.length);
	$("#receiptfiles"+n+'AttchNames').val(fileId);
	//如果之前没有上传图片，则不执行后面的画图
	/*if($("#receiptfiles"+n+'AttchNames').val()!=""){
		return;
	}*/
	var image1 = new Image()
	image1.src = $("base").attr("href")+"fileupload/downloadByObjId.do?id="+fileId;
	//alert( $("base").attr("href")+"fileupload/downloadByObjId.do?id="+fileId)
		 var scale = slider.value
		image1.onload = function(){
			 var w = $("#canvas"+n).css("width");
			var h = $("#canvas"+n).css("height");
			canvas.width = w.substring(0,w.length-2);
			canvas.height =  h.substring(0,h.length-2);
			drawImageByScaleWH( scale,image1,n )
			 slider.onmousemove = function(){
                scale = slider.value
                drawImageByScaleWH( scale,image1,n )
            }
   		 } 
}

//显示修改过大小的图片
function drawImageByScaleWH( scale,images,n){
	var b1 = canvas.width/images.width;
	var b2 = canvas.height/images.height;
	var bl = 0;
	if(b1<b2){
		bl = canvas.width / images.width;
	}else{
		bl = canvas.height / images.height;
	}
	
	
	var h = (images.height)*bl;
	var w = (images.width)*bl;
	
    var imageWidth = w * scale
    var imageHeight = h * scale
    
    //记录大小信息
    //var c = receiptfiles.substring(receiptfiles.length-1,receiptfiles.length);
    //alert(n+"----vs")
    pictures[n-1]["width"] = imageWidth;
    pictures[n-1]["height"] = imageHeight;
    pictures[n-1]["scale"] = scale;
    
    //var sx = imageWidth / 2 - canvas.width / 2
    //var sy = imageHeight / 2 - canvas.height / 2

    //context.drawImage( image , sx , sy , canvas.width , canvas.height , 0 , 0 , canvas.width , canvas.height )
    x = canvas.width /2 - imageWidth / 2
    y = canvas.height / 2 - imageHeight / 2
   // alert(imageWidth+"---"+imageHeight+"---"+x+"-----"+y)
    context.clearRect( 0 , 0 , canvas.width , canvas.height )
    context.drawImage( images , x , y , imageWidth , imageHeight )
}


function loadImage(i){
	var l = pictures.length;
	var n = (i-0+1);
	var id = "canvas"+n;
	var fileId = pictures[i]["fileid"];
	canvas = document.getElementById(id)
	context = canvas.getContext("2d")
	slider = document.getElementById(id+"-range")  
	$("#"+id+"-range").show();
	var n = id.substring(id.length-1,id.length);
	$("#receiptfiles"+n+'AttchNames').val(fileId);
	//如果之前没有上传图片，则不执行后面的画图
	/*if($("#receiptfiles"+n+'AttchNames').val()!=""){
		return;
	}*/
	var image1 = new Image();
	image1.src = $("base").attr("href")+"fileupload/downloadByObjId.do?id="+fileId;
	//alert( $("base").attr("href")+"fileupload/downloadByObjId.do?id="+fileId)
		 var scale = slider.value
		image1.onload = function(){
			 var w = $("#canvas"+n).css("width");
			var h = $("#canvas"+n).css("height");
			canvas.width = w.substring(0,w.length-2);
			canvas.height =  h.substring(0,h.length-2);


			var b1 = canvas.width/image1.width;
			var b2 = canvas.height/image1.height;
			var bl = 0;
			if(b1<b2){
				bl = canvas.width / image1.width;
			}else{
				bl = canvas.height / image1.height;
			}
			
			
			var h = (image1.height)*bl;
			var w = (image1.width)*bl;
			
		    var imageWidth = w * scale
		    var imageHeight = h * scale
		    
		    //记录大小信息
		   x = canvas.width /2 - imageWidth / 2
		    y = canvas.height / 2 - imageHeight / 2

		    context.clearRect( 0 , 0 , canvas.width , canvas.height )
		    context.drawImage( image1 , x , y , imageWidth , imageHeight )
			
			
			 slider.onmousemove = function(){
                scale = slider.value
                drawImageByScaleWH( scale,image1,n )
            }
   		 }
		 //详情时不能修改比例
		 if(pageStatus!=undefined&&pageStatus=="detail"){
			 $("#"+id+"-range").hide();
		 }
	if((i+1)<l){
		i++;
		setTimeout(
				function(){loadImage(i);}
				,3000);
		
	}
	
}

