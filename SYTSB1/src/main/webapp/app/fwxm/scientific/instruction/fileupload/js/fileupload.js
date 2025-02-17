//Version 1.1.1
document.write('<link rel="stylesheet" type="text/css" href="' + $("base").attr("href") + 'pub/fileupload/css/fileupload.css" />');

//创建一个上传实例
function KHFileUploader(config){
	if(!window.plupload){
		$("head").append('<script type="text/javascript" src="pub/fileupload/js/plupload.js"></script>' +
			'<script type="text/javascript" src="pub/fileupload/js/plupload.flash.js"></script>' +
			'<script type="text/javascript" src="pub/fileupload/js/plupload.html5.js"></script>');
	}
	
	this.fileUploaderRuntime = null;
	this.uploadRuntime = "html5,flash";
	this.uploadedFiles = [];
	var khUploader = this;
	
	//默认的配置
	this.uploadConfig = {
		fileId : "",//文件ID
		fileSize : "10mb",//文件大小限制
		businessId : "",//业务ID
		busUniqueName : "",//唯一业务名称
		folder: "",//业务指定文件存储目录
		buttonId : "pickfiles",//上传按钮ID
		container : "container",//上传控件容器ID
		fname : "",//自定义的存储文件名称
		title : "*",//文件选择框提示
		extName : "*",//文件扩展名限制
		saveDB : true,//是否往数据库写信息
		attType : "",//文件存储类型；1:数据库，0:磁盘，默认为磁盘
		workItem : "",//页面多点上传文件标识符号
		fileNum : 1,//限制上传文件数目
		remember: "0",//是否记住上传后保存。
		finishSave: function(files){},//保存调用的方法
		callback : function(files){},//上传成功后回调函数
		fileUploaded : function(file){},//单文件上传回调
		uploadProgress : function(file){},//上传进度回调，file={id:'',name:'',size:123,percent:33,status:''}
		filesAdded : function(files){},//添加文件事件,如果限制上传一个文件，files为单个文件，否则为选择的文件数组
		queueChanged : null,//上传队列发生变化,
		startAction : function(uploader){},//外部启动上传事件
		onError : function(errCoee){},//错误信息
		uploadError : function(file){}//上传失败
	};
	if(config){
		this.uploadConfig = $.extend(this.uploadConfig,config);
		/*$.each(config,function(k,v){
			if(v) khUploader.uploadConfig[k] = v;
		});*/
	}
	this.fileUploaderRuntime = new plupload.Uploader({
		runtimes : this.uploadRuntime,
		browse_button : this.uploadConfig.buttonId,
		container: this.uploadConfig.container,
		max_file_size : this.uploadConfig.fileSize,
		url : kFrameConfig.base + "fileupload/upload.do",
		flash_swf_url : kFrameConfig.base + 'pub/fileupload/js/plupload.flash.swf?ddd='+Math.random(),
		filters : [
			{title : this.uploadConfig.title, extensions : this.uploadConfig.extName}
		],
		multipart_params: {
			businessId: this.uploadConfig.businessId,
			saveDB: this.uploadConfig.saveDB,
			attType: this.uploadConfig.attType,
			folder: this.uploadConfig.folder,
			busUniqueName: this.uploadConfig.busUniqueName,
			workItem: this.uploadConfig.workItem,
			fileId: this.uploadConfig.fileId,
			fname: this.uploadConfig.fname
		}
	});
	
	this.setParams = function(params){
		var mparams = this.fileUploaderRuntime.settings.multipart_params;
		mparams = $.extend(mparams,params);
		this.fileUploaderRuntime.settings.multipart_params = mparams;
		
		/*if(param.businessId)
			url += "&businessId=" + param.businessId;
		if(param.saveDB)
			url += "&saveDB=" + param.saveDB;
		if(param.folder)
			url += "&folder=" + param.folder;
		if(param.attType)
			url += "&attType=" + param.attType;
		if(param.workItem)
			url += "&workItem=" + param.workItem;
		if(param.busUniqueName)
			url += "&busUniqueName=" + param.busUniqueName;
		if(param.fname)
			url += "&fname=" + param.fname;
		.url = url;*/
	}
	
	this.start = function(){
		khUploader.fileUploaderRuntime.start();
	}
	this.destory = function(){
		khUploader.fileUploaderRuntime.destroy();
	}
	this.destroy = this.destory;
	
	// ----------------------------------以下为绑定事件----------------------------------------
	
	//外部触发上传
	this.uploadConfig.startAction(this.fileUploaderRuntime);
	
	//添加文件事件
	this.fileUploaderRuntime.bind('FilesAdded', function(uploader, files) {
		if(khUploader.uploadConfig.fileNum > 0 && files.length > khUploader.uploadConfig.fileNum){
			$.ligerDialog.alert("选择的文件太多，超过了限制！<br/>提示：您最多可上传【<b style='color:red'>" + khUploader.uploadConfig.fileNum + "</b>】个文件！");
			return false;
		}
		showProcessMsg(khUploader);
		if(khUploader.uploadConfig.filesAdded)
			return khUploader.uploadConfig.filesAdded(files);
	});

	//文件列表发生变化
	this.fileUploaderRuntime.bind('QueueChanged',function(uploader) {//上传队列发生变化
		if(khUploader.uploadConfig.queueChanged)
			khUploader.uploadConfig.queueChanged(uploader.files);
		else uploader.start();
	});
	
	//单个文件上传完成
	this.fileUploaderRuntime.bind('FileUploaded', function(uploader,file,resObj) {
		var json = $.parseJSON(resObj.response);
		if(json.success){//上传成功
			var fobj = json.data;
			fobj.upId = file.id;
			// 将上传的结果id放入某个指定的input元素中
			if(khUploader.uploadConfig["resultIdField"]){
			    var rstField = $("#"+khUploader.uploadConfig["resultIdField"]);
			    rstField.val((rstField.val() + "," + fobj.id).replace(/^,|,$/),"");
			}
			//{"id":json.data.id,"name":file.name,"path":json.data.path,"workItem":json.data.workItem,"upId":file.id};
			khUploader.uploadedFiles.push(fobj);
			khUploader.uploadConfig.fileUploaded(fobj);
		}
		else{//上传失败
			khUploader.fileUploaderRuntime.stop();
			khUploader.uploadConfig.uploadError(file);
			khUploader.fileUploaderRuntime.refresh();
			$.ligerDialog.error(json.msg||"");
			file["extStatus"]=plupload.FAILED;
			
			//将已上传的文件删除
			$.each(khUploader.uploadedFiles,function(){
				$.getJSON('fileupload/deleteAtt.do?id='+this.id);
			});
		}
	});

    //所有文件上传完成
    this.fileUploaderRuntime.bind("UploadComplete",function(uploader,files){
        $("body").unmask();
        khUploader.uploadConfig.callback(khUploader.uploadedFiles);
        khUploader.uploadedFiles = [];
        //上传文成处理事件 调用保存事件
        if(khUploader.uploadConfig.autoSave==1&&$("#issave").attr("checked")=="checked"){
            khUploader.uploadConfig.onComplete(khUploader.uploadedFiles);
        }
    });
    //文件上传进度变化
    window._isAutoSave=false;
    this.fileUploaderRuntime.bind("UploadProgress",function(uploader,file){
        khUploader.uploadConfig.uploadProgress(file);
        if(file["extStatus"] && file["extStatus"]==plupload.FAILED){
            $("body").unmask();
        	return;
        }
        var html = "正在上传【" + file.name + "】，完成 <span style='color: red; '>" + file.percent + "%</span>";
        if($("#_uploadProgressDiv").size()==0){
            $("#processDiv").html(html)
        }
        //todo 对原来代码注入性太强
        $("#_global_mask_div").show();
        $("#_global_mask_div_loading").show();
        $("#_uploadProgressDiv").html(html);
    });
    
	//发生错误时
	this.fileUploaderRuntime.bind('Error', function(uploader,error) {
		if(error.code == plupload.FILE_SIZE_ERROR){
			$.ligerDialog.alert("所选的文件【" + error.file.name + "】太大，超过了限制(" + uploader.settings.max_file_size/1024 + "KB)！");
		}
		else if(error.code == plupload.FILE_EXTENSION_ERROR){
			$.ligerDialog.alert("文件类型错误！<br/>提示：可以选择的文件类型为:"+uploader.settings.filters[0].extensions);
		}
		else if(error.code == plupload.INIT_ERROR){
			//$.ligerDialog.alert("页面加载错误！<br/>您可以刷新本页面尝试解决问题。");
		}
		else{
			$.ligerDialog.alert("发生错误：" + error.code);
		}
		uploader.refresh();
        $("#_uploadProgressDiv").empty();
        $("body").unmask();
	});
	
	this.fileUploaderRuntime.init();
}

function KHKuiFileuploader(myConfig){
	if(!myConfig || !myConfig.fileContainer){
		$.ligerDialog.alert("请指定附件容器！参数名称：fileContainer");
		return;
	}
	myConfig.fileUploaded = function(file){//单个回调函数
		if(file){
			$("#" + file.upId).find(".progress").addClass("l-icon-close").click(function(){
				deleteUploadFile(file.id,file.path,this,function(){
					if(myConfig.delCallback){
						myConfig.delCallback(file);
					}
				});
			});
			$("#" + file.upId).find("a").attr("href",kFrameConfig.base + "fileupload/downloadByFilePath.do?path="+ file.path + "&fileName=" + file.name);
		}
	};
	
	myConfig.uploadProgress = function(file){//上传进度回调
		if(file.percent==100)
			$("#" + file.id).find(".progress").html("&nbsp;");
		else
			$("#" + file.id).find(".progress").html("<span>"+ file.percent + "%<span>");
	};
	
	myConfig.filesAdded = function(file){//添加文件到上传队列事件
		var htmlStr = "";
		if($.isArray(file)){
			$.each(file,function(i){
				htmlStr += '<li id="' + file[i].id + '"><div><a href="javascript:void();">' + file[i].name 
					+ '</a></div><div class="progress">&nbsp;</div></li>';
			});
		}
		else{
			htmlStr = '<li id="' + file.id + '"><div><a href="javascript:void();">' + file.name 
				+ '</a></div><div class="progress">&nbsp;</div></li>';
		}
		$("#" + myConfig.fileContainer).find(".file_list").append(htmlStr);
	};
	
	myConfig.container = myConfig.fileContainer + "_container";
	myConfig.buttonId = myConfig.fileContainer + "_pickfiles";
	
	//创建上传区域
	$("#" + myConfig.fileContainer).append('<p id="' + myConfig.container + '"><a class="l-button" id="' + myConfig.buttonId 
			+ '"><span class="l-button-main"><span class="l-button-text">选择文件</span></span></a></p>'
			+ '<div class="l-upload-ok-list"><ul class="file_list"></ul></div>');
	return new KHFileUploader(myConfig);
}

//删除服务器文件
function deleteUploadFile(id, path, obj, callback) {
    $.ligerDialog.confirm("确定删除文件？<p style='color:red'>注意：删除后不可恢复！</p>", function(flag) {
		if(flag){
			$("body").mask("正在删除，请稍后……");
		    $.getJSON('fileupload/deleteAtt.do',{"id":id,"path":path}, function(resp) {
		    	if(resp.success){
		    		if(obj){
		    			$(obj).parent().remove();
		    		}
		    		if(callback){
		    			callback(id,path);
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

function viewAttachment(id,name,container){
	var fname = name.toLowerCase();
	var regex = /\.(jpg|gif|png|bmp)$/gi;
	if(fname.match(regex) != null){
		var allFile = container.children();
		var previewData = {fisrt:0,images:[]};
		$.each(allFile, function(i) {
			var $id = $(this).attr("id");
			var $fname = $(this).attr("fname");
			if ($id == id) {
				previewData.first = (previewData.images.length == 0 ? 0 : previewData.images.length);
			}
			if($id == id || $fname.match(regex) != null){
				previewData.images.push({
					id : $id,
					name : $fname
				});
			}
		});
		top.winOpen({
			title: name,
			width: $(top).width(),
			height: $(top).height(),
			parent: window["api"]?window["api"]:null,
			resize: false,
			max: false,
			min: false,
			content: "url:pub/fileupload/preview.jsp",
			data: previewData
		});
	}
	else if(fname.match(/\.pdf$/gi) != null && !($.browser.msie && $.browser.version < 9.0)){		
        top.$.dialog({
            width: $(top).width(),
            height: $(top).height(),
            title: name,
            lock: false,
            reSize: false,
            content: "url:pub/pdfjs/viewer.jsp?fid=" + id
        });
	}
	else window.location = kFrameConfig.base + "fileupload/downloadByObjId.do?id=" + id;
}

//打开office文档
function openOfficeDocument(fid,fname,status){
    top.$.dialog({
        width: $(top).width(),
        height: $(top).height(),
        title: fname,
        lock: false,
        reSize: false,
        content: "url:pub/fileupload/ntko_editor.jsp",
        data: {fid:fid,status:status}
    });
}

// 在线播放视频
function playVideoOnline(fid,fname){
	top.$.dialog({
        width: $(top).width(),
        height: $(top).height(),
        title: fname,
        lock: false,
        reSize: false,
        content: "url:pub/fileupload/video_player.jsp?fid=" + fid
    });
}

/**
 * 生成文件列表。
 * 这里要求文件数组的元素格式必须为{id:"",name:""}
 * @param fs 文件数组
 * @param edit 是否可编辑
 * @param ctrId 容器ID
 * @param preview 是否以预览图展示
 */
function createFilesView(fs,edit,ctrId,preview,callback){
    createFileViewList({
        files:fs,edit:edit,ctrId:ctrId,preview:preview,callback:callback
    })
}
/**
 * 创建一组文件列表视图，使用json格式参数
 * @param options
 */
function createFileViewList(options){
	$.each(options.files,function(){
	    var $ops = $.extend({fid:this.id,fname:this.name},options);
	    createFileViewItem($ops);
	});
}

/**
 * 生成单个文件视图
 * 
 * @param fid 文件id
 * @param fname 文件名
 * @param edit 是否可编辑
 * @param ctrId 容器ID
 * @param preview 是否以预览图展示
 */
function createFileView(fid,fname,edit,ctrId,preview,callback){
    createFileViewItem({
        fid:fid,fname:fname,edit:edit,ctrId:ctrId,preview:preview,modify:true,callback:callback
    });
}

/**
 * 生成单个文件视图,采用json参数
 * 
 * @param options
 */
function createFileViewItem(options){
    var ctr = $(".l-upload-ok-list");
    if(options.ctrId) ctr = $("#" + options.ctrId);
    var fitem = $("<li id='" + options.fid + "' fname='" + options.fname + "' class='"+(options.preview?"preview":"not-preview") + "'></li>").appendTo(ctr);
    var ofpreview = options.fname.match(/\.(doc|docx|xls|xlsx)$/i) ;//&& kFrameConfig.previewOfficeDocument;
    var isVedio = options.fname.match(/\.(avi|mp4|wmv|mkv|rmvb)$/i);
    var fcontainer = $("<a class='f-link'></a>").appendTo(fitem);
    if(!ofpreview){
    	fcontainer.click(function(){
    		viewAttachment(options.fid,options.fname,ctr);
    	});
    }
    if(options.preview){
    	var previewImgAddr;
    	if(options.previewImg) previewImgAddr = options.previewImg;
    	else previewImgAddr = getPreviewImageAddr(options.fid,options.fname,true);
        fcontainer.append("<img width='48' height='48' src='" + previewImgAddr + "' />");
    }
    else{
        fcontainer.append("<img width='16' height='16' class='fname-icon' src='" + getPreviewImageAddr(options.fid,options.fname,false) + "' />" + options.fname);
    }
    if(options.edit){
        var d = $('<div class="l-icon-close progress">&nbsp;</div>');
        d.click(function(){
            deleteUploadFile(options.fid,"","",function(){
                $("#" + options.fid).remove();
                if(options.callback) options.callback(options.fid);
            });
        });
        d.appendTo(fitem)
    }
    if(options.preview || ofpreview || isVedio){
        fitem.on("mouseenter",function(){
            var tips = $(".upload-fname-tips");
            if(tips.size()==0){
                tips = $('<div class="upload-fname-tips" id="tips"><div class="tips-arrow-bg"></div><div class="tips-arrow"><div class="tips-arrow inner"></div></div>'+
                            '<div class="tips-head"></div><div class="tips-content"><div class="edit"><span class="l-icon l-icon-edit"></span><span class="text">编辑</span></div>'+
                            '<div class="view"><span class="l-icon l-icon-detail"></span><span class="text">查看</span></div>' + 
                            '<div class="down"><span class="l-icon l-icon-save"></span><span class="text">下载</span></div></div></div>');
            }
            tips.css("left","-100px").appendTo(fitem);
            $(".upload-fname-tips .tips-head").text(options.fname);
            if(ofpreview || isVedio){
                tips.removeClass("no-button");
                $(".upload-fname-tips .tips-content").show();
                if(options.modify && ofpreview){
                    tips.find(".tips-content .edit").show().off("click").on("click",function(){
                    	alert(options.fname);
                        openOfficeDocument(options.fid,options.fname,"edit");
                    });
                }else{
                    tips.find(".tips-content .edit").hide().off("click");
                }
                tips.find(".tips-content .view").show().off("click").on("click",function(){
                	if(ofpreview)
                		openOfficeDocument(options.fid,options.fname,"detail");
                	else
                		playVideoOnline(options.fid,options.fname);
                });
                tips.find(".tips-content .down").show().off("click").on("click",function(){
                    window.open($("base").attr("href") + "fileupload/download.do?id=" + options.fid);
                });
            }else{
                tips.addClass("no-button");
                $(".upload-fname-tips .tips-content").hide();
            }
            tips.show().css("bottom",options.preview?(ofpreview?"63px":"62px"):(ofpreview?"33px":"32px"));
            if(fitem.offset().left + fitem.width()/2-99 + tips.width()>$(window).width()){
                tips.css("left",fitem.width()-198+"px"); 
                tips.find(".tips-arrow").css("left","160px");
                tips.find(".inner").css("left","-10px");
            }else if(fitem.offset().left < 100){
                tips.css("left","0px");
                tips.find(".tips-arrow").css("left",fitem.width()/2-10 + "px");
                tips.find(".inner").css("left","-10px");
            }else{
                tips.css("left",(fitem.width()/2-99)+"px");
                tips.find(".tips-arrow").css("left","45%");
                tips.find(".inner").css("left","-10px");
            }
       });
       fitem.on("mouseleave",function(){
           $(".upload-fname-tips").off("mouseout").hide();
       });
    }
}

//获取与文件后缀名匹配的小图标地址
function getPreviewImageAddr(fid,fname,preview){
	if(preview && fname.match(/\.(jpg|gif|png|bmp)$/gi) != null){
		return kFrameConfig.base + "fileupload/previewImage.do?id=" + fid;
	}else{
		var suffixIdx = fname.lastIndexOf(".");
		var suffix = suffixIdx==-1?"file":fname.substring(suffixIdx).toLowerCase().substring(1);
		// 从系统参数【文件后缀图标集合】中查找
		if(suffixIdx > -1 && window["kFrameConfig"] && kFrameConfig["suffixIcon"] && kFrameConfig.suffixIcon.indexOf(suffix)==-1)
	            suffix = "file";
		return "k/kui/images/file-type/"+(preview?48:16)+"/" + suffix + ".png";
	}
}

/**
 * 获取业务附件
 * @param _bussid
 * @param _callback
 */
function getBusinessAttachments(_bussid,_callback){
	$.getJSON("fileupload/busFiles.do?businessId=" + _bussid,function(resp){
		if(resp.success){
			_callback(resp.data);
		}else{
			$.ligerDialog.error("获取附件失败！");
		}
	});
}
function showProcessMsg(khUploader){
	var html = "<div id='processDiv'></div>";
	if($("#_uploadProgressDiv").size()==0){
        if (khUploader.uploadConfig.autoSave == 1) {
        	html+="<br><br><div id='_isSaveDiv' style='z-index:200'><input type='checkbox' id='issave'><label for='issave' style='margin-left:2px'>上传完成后，是否保存？</label></div>";
        }
    }
	$("#_global_mask_div_loading").html(html);
}
