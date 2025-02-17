//外部公共接口,使用照片(div对象,回调函数,业务状态)
function photogragh(container, fun, status) {
	var app_base_url = "";
	if($("base").size()==1) app_base_url = $("base").attr("href");
	
	var pageStatus;
	if (status) {
		pageStatus = status;
	} else {
		pageStatus = $("head").attr("pageStatus");
	}
	
	var isEditStatus = pageStatus == "add" || pageStatus == "edit" || pageStatus == "modify";
	var func = fun || function() {
	};
	var photo = container + "_photo";// 图片显示
	var uploadContainer = container + "_uploadContainer";// 按钮显示

	var pickfiles = container + "_pickfiles";// 上传按钮
	var dopicture = container + "_dopicture";// 拍照
	var deletePic = container + "_deletePic";// 清空

	this.idn = null;
	var _this = this;
	this.imageId = null;
	this.imagePath = null;
	this.uploader = null;

	var htmlStr = '<div id="_photo_ctr" style="text-align:center;position:relative;"><img id="' + photo + '" src="' + app_base_url 
			+ 'pub/photograph/img/no_idno_pic.gif" style="width:124px;height:148px;border:1px #cccccc solid;margin-top:1px;"/>';
	if(isEditStatus){
		htmlStr += '<div style="text-align:center;position:absolute;bottom:3px;left:0;width:100%;" id="' + uploadContainer + '"><input id="' + pickfiles
			+ '" type="button" class="l-button" style="width:40px;margin:0;height:22px;border-radius:0;" value="上传" /><input id="' + dopicture
			+ '" type="button" class="l-button" style="width:40px;margin:0;height:22px;border-left:none;border-right:none;border-radius:0;" value="拍照" /><input id="' + deletePic
			+ '" type="button" class="l-button" style="width:40px;margin:0;height:22px;border-radius:0;" value="清空" /></div></div>';
	}else{
		htmlStr += "</div>";
	}
	
	$("#" + container + "").html(htmlStr);
	
	if (isEditStatus) {
		// 上传图片
		var myUploadConfig = {
			fileSize : "1mb",// 文件大小限制
			buttonId : pickfiles,// 上传按钮ID
			container : uploadContainer,// 上传控件容器ID
			title : "图片",// 文件选择框提示
			busUniqueName : this.idn,
			extName : "jpg,gif,bpm,png,jpeg,JPG,GIF,BPM,PNG,JPEG",// 文件扩展名限制
			fileNum : 1,// 限制上传文件数目
			callback : function(file) {// 上传成功后回调函数,实现页面文件显示，交与业务自行后续处理
				if (file) {
					_this.uploadCallback(file[0].id);
				}
			}
		};
		this.uploader = new KHFileUploader(myUploadConfig);

		// 调用摄像头拍照
		$("#"+dopicture).click(function (){
            top.$.dialog({
                width: 300,
                height: 415,
                max: false,
                min: false,
                lock: true,
                resize: false,
                parent : api,
                lock: true,
                data: {callback : function(json){
                    if(json){
                        _this.uploadCallback(json.data);
                    }
                },idn:_this.idn},
                title: "拍照",
                content: 'url:pub/photograph/photograph.jsp?pageStatus=detail'
            });
        });
		
		// 删除图片
		$("#" + deletePic).click(function() {
			if(_this.imageId){
				deleteUploadFile(_this.imageId,_this.imagePath,"",function(){
					$("#" + photo).attr("src", app_base_url + "pub/photograph/img/no_idno_pic.gif");
					func("");
				});
			}else{
				$("#" + photo).attr("src", app_base_url + "pub/photograph/img/no_idno_pic.gif");
				func("");
			}
		});
		//$("#_photo_ctr").hover(function(){$("#"+uploadContainer).show()},function(){$("#"+uploadContainer).hide()});
	}

	// 根据身份证号,设置div框里显示图片
	this.setIdn = function(idn, callback) {
		// 检查身份证号对应的是否有照片(有:true,无:false)
		if(idn){
			_this.idn = idn;
			if(pageStatus!="detail" && _this.uploader != null){
				_this.uploader.setParams({busUniqueName:idn});
			}

            $("#" + photo).attr("src",app_base_url + "pub/personImg/download.do?idn=" + idn + "&_="+Math.random());
            
			$.getJSON(app_base_url + 'pub/personImg/existIdnImg.do?idn=' + idn, function(data) {
				if(data.result){
					if(data.hasOwnProperty("imgId")) _this.imageId = data.imgId;
				}
				if (callback){
					callback(data.result);
				}
			});
		}else{//zhp 20150522添加 
            $("#" + photo).attr("src", app_base_url + "pub/photograph/img/no_idno_pic.gif");
		}
	}

	/**
	 * 上传照片成功回调函数,这将会触发身份证号码和照片之间的关联。
	 * 前提是身份证设置了身份证号码，如果没有设置，那么将交给返回给调用者自行处理
	 */
	this.uploadCallback = function(imgId) {
		this.imageId = imgId;
		$("#" + photo).attr("src", app_base_url + "fileupload/downloadByObjId.do?id=" + imgId + "&_="+Math.random());
		func(imgId);
	}

	// 设置是否显示上传按钮(true:显示,false:不显示),不设置时，默认显示上传按钮
	this.setPictureButton = function(isShowBtn) {
		if (!isShowBtn) {
			$("#" + uploadContainer).hide();
		}
	}

	// 根据图片的名字,设置div框里显示图片
	this.setPictureByPath = function(filepath) {
		if (filepath) {
			this.imagePath = filepath;
			$("#" + photo).attr("src", app_base_url + "fileupload/downloadByFilePath.do?path=" + filepath);
		}
	}

	// 根据图片的ID,设置div框里显示图片
	this.setPictureById = function(id) {
		if (id) {
			this.imageId = id;
			$("#" + photo).attr("src", app_base_url + "fileupload/downloadByObjId.do?id=" + id);
		}
	}
}
