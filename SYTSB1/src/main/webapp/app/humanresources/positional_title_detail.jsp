<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib uri="http://bpm.khnt.com" prefix="bpm" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head pageStatus="${param.pageStatus}">
    <title></title>

    <%@include file="/k/kui-base-form.jsp" %>
    <script type="text/javascript" src="pub/bpm/js/util.js"></script>
    <script type="text/javascript" src="app/finance/js/jquery.autocomplete.js"></script>
    <script type="text/javascript" src="app/common/lodop/LodopFuncs.js"></script>
    <link rel="Stylesheet" href="app/finance/css/jquery.autocomplete.css"/>
    <link type="text/css" rel="stylesheet" href="app/finance/css/form_detail.css"/>
    <script type="text/javascript" src="app/humanresources/js/doc_order.js"></script>

    <script type="text/javascript" src="pub/fileupload/js/fileupload.js"></script>
    <script type="text/javascript">
        var permission = "${param.permission}";
        var empStatus = "${param.status}";
        if (permission == "permission") {
            empStatus = "3";
        }
        if (permission == "formal") {
            empStatus = "4";
        }
        var uploadId = "";
        var contract = "${param.contract}";
        var renew = "${param.renew}";
        var work;
        var photo;
        var name_old = "";
        var org_id_old = "";
        $(function () {
            var status = "${param.pageStatus}";
            var tbar = [{text: '提交', id: 'save', icon: 'save', click: saveAdd},
                {text: '关闭', id: 'close', icon: 'cancel', click: close}];
            if (status == "detail") {
                var tbar = [{text: '打印', id: 'print', icon: 'print', click: print}];
            }
            $("#formObj").initForm({ //参数设置示例
                showToolbar: true,
                toolbar: tbar,
                toolbarPosition: "bottom",
                getSuccess: function (res) {
                    if (res.attachs != null && res.attachs != undefined) {
                        //showAttachFile2(res.attachs);
                    }
                    if (res.employeePrinter != null && res.employeePrinter != undefined) {
                        /* if(res.employeePrinter.printer_name!="" && res.employeePrinter.printer_name!=null){
                            if("detail" == status){
                                document.getElementById("printer_name").innerHTML=res.employeePrinter.printer_name;
                            }else{
                                $("#printer_name").val(res.employeePrinter.printer_name);
                            }
                        }
                        if(res.employeePrinter.printer_name_tags!="" && res.employeePrinter.printer_name_tags!=null){
                            if("detail" == status){
                                document.getElementById("printer_name_tags").innerHTML=res.employeePrinter.printer_name_tags;
                            }else{
                                $("#printer_name_tags").val(res.employeePrinter.printer_name_tags);
                            }
                        } */
                    }
                    if (res.emp.empPhoto != null && res.emp.empPhoto != "") {
                        photo = "upload/" + res.emp.empPhoto;
                        $("#image").attr("src", photo);
                        $("#image").show();
                    }
                    $("#form1").setValues(res.emp);
                    $("#employeeId").val(res.emp.id);
                    $("#fkEmployeeId").val(res.emp.id);
                    $("#fkEmployeeId_contract").val(res.emp.id);
                    name_old = res.emp.empName;
                    org_id_old = res.emp.workDepartment;
                    if (status == "detail") {
                        if (res.work != null || res.work != "") {
                            createJrxxGrid(res.work)
                        }

                    }
                    if (renew == "renew") {
                        $("#formObj").transform("detail");
                    }
                    ageAndBirthday();
                },
                afterParse: function (formObj) {//form表单完成渲染后，回调函数

                }
            });
            $("body").append('<object style="height:1px;" id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0><embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0></embed></object>');
            $("#form_contract").initForm({ //参数设置示例
                success: function (res) {
                    $("#conId").val(res.id);
                    top.$.dialog.notice({
                        content: '保存成功'
                    });
                    api.data.window.Qm.refreshGrid();
                },
                getSuccess: function (res) {
                    $("#form_contract").setValues(res.con)
                    if (res.con.documentName != null && res.con.documentName != undefined) {
                        showAttachFile(res.con);
                    }
                },
                afterParse: function (formObj) {//form表单完成渲染后，回调函数

                }
            });
            $("#form_upload").initForm({

                toolbar: [{
                    text: '提交',
                    id: 'saveUp',
                    icon: 'save',
                    click: saveUp
                }, {
                    text: '关闭',
                    id: 'close',
                    icon: 'cancel',
                    click: close
                }],
                toolbarPosition: "bottom",//参数设置示例
                success: function (res) {
                    if (res.success) {
                        top.$.dialog.notice({
                            content: '保存成功'
                        });
                    } else {
                        top.$.dialog.notice({
                            content: '请选择要上传的文件！'
                        });
                    }
                    //api.data.window.Qm.refreshGrid();

                }
            });
            if (permission != "apply" && status != "detail") {
                //自动检索部门
                /* $('#workDepartmentName').autocomplete("employee/basic/searchOrg.do", {
                    max: 12,    //列表里的条目数
                    minChars: 1,    //自动完成激活之前填入的最小字符
                    width: 200,     //提示的宽度，溢出隐藏
                    scrollHeight: 300,   //提示的高度，溢出显示滚动条
                    scroll: false,   //当结果集大于默认高度时是否使用卷轴显示
                    matchContains: true,    //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
                    autoFill: false,    //自动填充
                    formatItem: function(row, i, max) {
                    //alert(row);
                        return row.orgName;
                    },
                    formatMatch: function(row, i, max) {
                        return row.orgName;
                    },
                    formatResult: function(row) {
                        return row.orgName;
                    }
                }).result(function(event, row, formatted) {
                         $("#workDepartment").val(row.orgId);
                }); */
                var receiptUploadConfig = {
                    fileSize: "50mb",	//文件大小限制
                    businessId: "",	//业务ID
                    buttonId: "procufilesBtn",		//上传按钮ID
                    container: "procufilesDIV",	//上传控件容器ID
                    title: "文件",	//文件选择框提示
                    extName: "doc",	//文件扩展名限制
                    workItem: "",	//页面多点上传文件标识符号
                    fileNum: 1,	//限制上传文件数目
                    callback: function (file) {	//上传成功后回调函数,实现页面文件显示，交与业务自行后续处理
                        var id = $("#conId").val();
                        if (renew == "renew") {
                            id = "";
                        }
                        $.ajax({
                            url: "contractAction/saveFile.do?id=" + id + "&documentId=" + file[0].path,
                            type: "POST",
                            data: "&documentName=" + file[0].name,
                            dataType: 'json',
                            async: false,
                            success: function (data) {
                                if (data.success) {
                                    addAttachFile(file);
                                    /* editor(file[0].path,file[0].name,"add"); */
                                } else {
                                    $.ligerDialog.warn(data.msg);
                                }
                            },
                            error: function () {
                                $.ligerDialog.warn("提交失败！");
                            }
                        });
                    }
                };
                var receiptUploader = new KHFileUploader(receiptUploadConfig);
            }
            if (status != "detail") {
                //上传图片
                var oneUploadConfig = {
                    fileSize: "10mb",//文件大小限制
                    businessId: "",//业务ID
                    buttonId: "onefileBtn",//上传按钮ID
                    container: "onefileDIV",//上传控件容器ID
                    attType: "",//文件存储类型；1:数据库，0:磁盘，默认为磁盘
                    title: "图片选择",//文件选择框提示
                    extName: "jpg,jpeg,gif,bmp,png",//文件扩展名限制
                    fileNum: 1,//限制上传文件选择数目 弹出选择框 可以选择文件数量限制
                    workItem: "one",//页面多点上传文件标识符号
                    callback: function (files) {//上传成功后回调函数,实现页面文件显示，交与业务自行后续处理
                        $("#empPhoto").val(files[0].path);
                        $("#image").attr("src", "upload/" + files[0].path)
                        $("#image").show();
                        addOneFile(files);
                    }
                };
                var oneUploader = new KHFileUploader(oneUploadConfig);
            }
            //上传附件
            var oneUploadConfig2 = {
                fileSize: "50mb",//文件大小限制
                businessId: "",//业务ID
                buttonId: "onefileBtn2",//上传按钮ID
                container: "onefileDIV2",//上传控件容器ID
                attType: "",//文件存储类型；1:数据库，0:磁盘，默认为磁盘
                title: "图片选择",//文件选择框提示
                extName: "jpg,jpeg,gif,bmp,png",//文件扩展名限制
                fileNum: 1,//限制上传文件选择数目 弹出选择框 可以选择文件数量限制
                workItem: "one",//页面多点上传文件标识符号
                callback: function (files) {//上传成功后回调函数,实现页面文件显示，交与业务自行后续处理
                    //显示图片
                    $("#image_upload").removeAttr("src")
                    $("#image_upload").attr("src", "upload/" + files[(files.length - 1)].path)
                    $("#uploadName").val(files[(files.length - 1)].name)
                    $("#uploadPath").val(files[(files.length - 1)].path)
                    $("#uploadId").val(files[(files.length - 1)].id)
                    /* $("#procufiles").attr("href","fileupload/downloadByFilePath.do?path="+files[0].path+"&fileName="+files[0].name); */
                    $("#procufiles").attr("onclick", "view()");
                    addOneFile(files);
                }
            };

            var oneUploader = new KHFileUploader(oneUploadConfig2);
            // 合同上传

            if (permission != "apply" && status != "detail") {

                // 电子签名上传
                /* 	var receiptUploadConfig1 = {
                            fileSize : "50mb",	//文件大小限制
                            businessId : "",	//业务ID
                            buttonId : "procufilesBtn2",		//上传按钮ID
                            container : "procufilesDIV2",	//上传控件容器ID
                            title : "图片",	//文件选择框提示
                            extName : "jpg,gif,png,bmp",	//文件扩展名限制
                            workItem : "",	//页面多点上传文件标识符号
                            fileNum : 1,	//限制上传文件数目
                            callback : function(file){	//上传成功后回调函数,实现页面文件显示，交与业务自行后续处理
                                addAttachFile2(file);
                            }
                    };
                    var receiptUploader= new KHFileUploader(receiptUploadConfig1);
                    */
            }


            if (status == "detail") {
                $("#onefileBtn").hide();
                $("#onefileBtn2").hide();
            }
            var workColumn = [{
                display: '主键',
                name: 'id',
                isSort: false,
                hide: true
            }, {
                display: '开始时间',
                name: 'workStartDate',
                width: '15%',
                align: "center",
                isSort: false,
                type: 'date',
                format: "yyyy-MM-dd",
                editor: {
                    type: 'date'
                }
            }, {
                display: '结束时间',
                name: 'workStopDate',
                width: '15%',
                align: 'center',
                isSort: false,
                type: 'date',
                format: "yyyy-MM-dd",
                editor: {
                    type: 'date'
                }
            }, {
                display: '所在单位',
                name: 'workUnit',
                width: '20 %',
                isSort: false,
                align: 'center',
                textModel: true,
                editor: {
                    type: 'text'
                }
            }
                , {
                    display: '担任职务',
                    name: 'workPosition',
                    width: '20 %',
                    isSort: false,
                    align: 'center',
                    textModel: true,
                    editor: {
                        type: 'text'
                    }
                }
                , {
                    display: '从事工作',
                    name: 'workOn',
                    width: '20%',
                    isSort: false,
                    align: 'center',
                    textModel: true,
                    editor: {
                        type: 'text'
                    }
                }];
            if (status != "detail") {
                workColumn.push({
                    display: "<a class='l-a iconfont l-icon-add' href='javascript:work.addEditRow();'><span>增加</span></a>",
                    width: 30,
                    isSort: false,
                    align: "center",
                    render: function (item, index) {
                        return "<a class='l-a l-icon-del' href='javascript:deleteRow(work," + index + ")'><span>删除</span></a>";
                    }
                });

                // 构造简历信息栏
                work = $("#work").ligerGrid({
                    columns: workColumn,
                    usePager: false,
                    enabledEdit: status != "detail",
                    /* data : {
                        Rows : []
                    }, */
                    dataAction: "local",
                    url: 'workExperienceAction/detailWork.do?id=${param.id}'
                });

            }
        });

        //添加单文件处理
        function addOneFile(files) {
            if (files) {
                //判断单个文件是否存在
                var uploadFileId = $("#empPhoto").val();
                if (uploadFileId) {
                    //删除该id对应的
                    $.getJSON('fileupload/deleteAtt.do?id=' + uploadFileId, function (resp) {
                        if (resp.success) {
                            $("#" + uploadFileId).remove();
                        }
                    });
                }
            }
        }

        function close() {
            api.close();
        }

        function saveAdd() {
            var empSex = $("input[name='_empSex-txt']").ligerComboBox().getValue();
            var empPhone = $("#empPhone").val();
            var empIdCard = $("#empIdCard").val();
            var empTitleNum = $("#empTitleNum").val();
            var printer_name = $("#printer_name").val();
            var printer_name_tags = $("#printer_name_tags").val();
            var uploadFiles = $("#uploadFiles").val();
            if ("${param.pageStatus}" == "add" && "${param.status}" != "" && "${param.status}" != "undefind" && "${param.status}" != null) {
                $.ajax({
                    url: "employeeBaseAction/checkInfo.do",
                    type: "POST",
                    data: {"empPhone": empPhone, "empIdCard": empIdCard, "empTitleNum": empTitleNum},
                    success: function (data) {
                        if (data.success) {
                            if (empSex == "" || empSex == null) {
                                $.ligerDialog.error("请填写性别！");
                                return;
                            }
                            if (empPhone == "" || empPhone == null) {
                                $.ligerDialog.error("请填写联系电话！");
                                return;
                            }
                            if ($("#form1").validate().form()) {
                                $("body").mask("正在保存数据，请稍后！");
                                var formData = $("#form1").getValues();
                                formData.workExperience = work.getData();
                                var employee = $.ligerui.toJSON(formData);
                                $.ajax({
                                    url: "employeeBaseAction/saveBasic.do",
                                    type: "POST",
                                    data: {
                                        "employee": employee,
                                        "permission": permission,
                                        "status": empStatus,
                                        "printer_name": printer_name,
                                        "printer_name_tags": printer_name_tags,
                                        "uploadFiles": uploadFiles,
                                        "name_old": name_old,
                                        "org_id_old": org_id_old
                                    },
                                    success: function (data, stats) {
                                        $("body").unmask();
                                        if (data["success"]) {
                                            $("#empId").val(data.id);
                                            $("#createDate").val(data.createDate);
                                            if ($("#fkEmployeeId").val() == null || $("#fkEmployeeId").val() == "") {
                                                $("#fkEmployeeId").val(data.id)
                                            }

                                            if ($("#fkEmployeeId_contract").val() == null || $("#fkEmployeeId_contract").val() == "") {
                                                $("#fkEmployeeId_contract").val(data.id)
                                            }

                                            if ($("#employeeId").val() == null || $("#employeeId").val() == "") {
                                                $("#employeeId").val(data.id);
                                            }
                                            top.$.dialog.notice({
                                                content: '提交成功'
                                            });
                                            api.data.window.Qm.refreshGrid();
                                        } else {
                                            $.ligerDialog.error('提示：' + data.message);
                                        }
                                    },
                                    error: function (data) {
                                        $("body").unmask();
                                        $.ligerDialog.error('保存数据失败！');
                                    }
                                });

                            }
                        } else {
                            $.ligerDialog.error('提示：</br>' + data.msg + "。请核实！");
                        }
                    },
                    error: function (data) {
                        $.ligerDialog.error(data.message);
                    }
                });
            } else {
                if (empSex == "" || empSex == null) {
                    $.ligerDialog.error("请填写性别！");
                    return;
                }
                if (empPhone == "" || empPhone == null) {
                    $.ligerDialog.error("请填写联系电话！");
                    return;
                }
                if ($("#form1").validate().form()) {
                    $("body").mask("正在保存数据，请稍后！");
                    var formData = $("#form1").getValues();
                    formData.workExperience = work.getData();
                    var employee = $.ligerui.toJSON(formData);
                    $.ajax({
                        url: "employeeBaseAction/saveBasic.do",
                        type: "POST",
                        data: {
                            "employee": employee,
                            "permission": permission,
                            "status": empStatus,
                            "printer_name": printer_name,
                            "printer_name_tags": printer_name_tags,
                            "uploadFiles": uploadFiles,
                            "name_old": name_old,
                            "org_id_old": org_id_old
                        },
                        success: function (data, stats) {
                            $("body").unmask();
                            if (data["success"]) {
                                $("#empId").val(data.id);
                                $("#createDate").val(data.createDate);
                                if ($("#fkEmployeeId").val() == null || $("#fkEmployeeId").val() == "") {
                                    $("#fkEmployeeId").val(data.id)
                                }

                                if ($("#fkEmployeeId_contract").val() == null || $("#fkEmployeeId_contract").val() == "") {
                                    $("#fkEmployeeId_contract").val(data.id)
                                }

                                if ($("#employeeId").val() == null || $("#employeeId").val() == "") {
                                    $("#employeeId").val(data.id);
                                }
                                top.$.dialog.notice({
                                    content: '提交成功'
                                });
                                api.data.window.Qm.refreshGrid();
                            } else {
                                $.ligerDialog.error('提示：' + data.message);
                            }
                        },
                        error: function (data) {
                            $("body").unmask();
                            $.ligerDialog.error('保存数据失败！');
                        }
                    });

                }
            }
        }

        //提交合同信息
        function saveCon() {

        }

        // 删除行
        function deleteRow(obj, index) {
            $.ligerDialog.confirm("确定删除该数据？", function (yes) {
                if (yes) {
                    obj.deleteRow(index);
                }
            });
        }

        //detail页面加载兼任信息
        function createJrxxGrid(data) {
            var jrxx_html = '<div class="l-grid-body l-grid-body2 l-scroll"><div class="l-grid-body-inner"><table class="l-detail-table b-dyn-table decimal-table"><tr class="l-t-td-title" style="height:30px;font-weight:normal">'
                + '<td class="l-t-td-title" style="width:16%">开始时间</td>'
                + '<td class="l-t-td-title" style="width:16%">结束时间</td>'
                + '<td class="l-t-td-title" style="width:14%">所在单位</td>'
                + '<td class="l-t-td-title" style="width:24%">担任职务</td>'
                + '<td class="l-t-td-title" style="width:32%">从事工作</td>';
            if (data && data.length > 0) {
                for (var i = 0; i < data.length; i++) {
                    var workStartDate = "";
                    var workStopDate = "";
                    var workUnit = "";
                    var workPosition = "";
                    var workOn = "";
                    if (data[i].workStartDate != null) {
                        workStartDate = data[i].workStartDate.substring(0, 10);
                    }
                    if (data[i].workStopDate != null) {
                        workStopDate = data[i].workStopDate.substring(0, 10);
                    }
                    if (data[i].workUnit != null) {
                        workUnit = data[i].workUnit;
                    }
                    if (data[i].workPosition != null) {
                        workPosition = data[i].workPosition;
                    }
                    if (data[i].workOn != null) {
                        workOn = data[i].workOn;
                    }


                    jrxx_html += '<tr class="l-t-td-right center" style="height:30px">'
                        + '<td class="l-grid-row-cell" style="text-align:center;height:30px">' + workStartDate
                        + '</td><td class="l-grid-row-cell" style="text-align:center">' + workStopDate
                        + '</td><td class="l-grid-row-cell" style="text-align:center">' + workUnit
                        + '</td><td class="l-grid-row-cell" style="text-align:center">' + workPosition
                        + '</td><td class="l-grid-row-cell" style="text-align:center">' + workOn
                }
            } else {
                jrxx_html += '<tr><td colspan="6">无数据</td></tr>';
            }
            jrxx_html += '</table></div></div>';
            $("#work").html(jrxx_html);
        }

        var status = "";

        //添加合同附件
        function addAttachFile(files) {
            status = "add";
            if ("${param.pageStatus}" == "detail") {
                $("#procufilesBtn").hide();
            }
            if (files) {
                var attContainerId = "procufiles";
                $.each(files, function (i) {
                    var file = files[i];

                    $("#procufiles3").append("<li id='" + file.id + "'>" +
                        "<div><a href='#' onclick='editor(\"" + file.path + "\",\"" + file.name + "\",\"" + status + "\");return false'>" + file.name + "</a></div>" +
                        "<div class='l-icon-close progress' onclick='deleteUploadFile(\"" + file.id + "\",\"" + file.path + "\",this,getUploadFile)'>&nbsp;</div>" +
                        "</li>");
                });
                //getUploadFile();
            }
        }

        // 将上传的合同附件id写入隐藏框中
        function getUploadFile() {
            var attachId = "";
            var i = 0;
            $("#procufiles li").each(function () {
                attachId += (i == 0 ? "" : ",") + this.id;
                i = i + 1;
            });
            if (attachId != "") {
                attachId = attachId.substring(0, attachId.length);
            }
            $("#uploadFiles").val(attachId);
        }

        //编辑word文档
        function editor(docId, docName, status) {
            var type = "";
            var id = $("#conId").val()
            var documentDoc = $("#documentDoc").val()
            var doc = "0";
            if (documentDoc != "" && documentDoc != null) {
                doc = "1";
            }
            if (status == "") {
                type = "modify";
            } else {
                type = "add";
            }
            //打开生成报告页面
            openContentDoc({
                docId: docId,
                doc: doc,
                status: "draft",
                id: id,
                type: type,
                window: window,
                title: docName,
                tbar: {
                    edit: true,
                    print: true,
                    layout: true
                }
            });

        }

        // 显示合同
        function showAttachFile(file) {
            if ("${param.pageStatus}" == "detail") {
                $("#procufilesBtn").hide();
            }
            if (file) {
                //详情
                var attContainerId = "procufiles3";
                if ("${param.pageStatus}" == "detail") {
                    //显示附件
                    $("#" + attContainerId).append("<li id='" + file.id + "'>" +
                        "<div><a href='#' onclick='editor(\"" + file.documentId + "\",\"" + file.documentName + "\",\"" + status + "\");return false'>" + file.documentName + "</a></div>" +
                        "</li>");
                }
                //修改
                else if ("${param.pageStatus}" == "modify") {

                    $("#" + attContainerId).append("<li id='" + file.id + "'>" +
                        "<div><a href='#' onclick='editor(\"" + file.documentId + "\",\"" + file.documentName + "\",\"" + status + "\");return false'>" + file.documentName + "</a></div>" +
                        "<div class='l-icon-close progress' onclick='deleteUploadFile(\"" + file.id + "\",\"" + file.documentId + "\",this,getUploadFile)'>&nbsp;</div>" +
                        "</li>");
                    getUploadFile();
                }
            }
        }

        /*  //添加电子签名
           function addAttachFile2(files){
               if("${param.pageStatus}"=="detail"){
    			$("#procufilesBtn2").hide();
    		}
    		if(files){
    			var attContainerId="procufiles4";
    			$.each(files,function(i){
    				var file=files[i];
    				$("#"+attContainerId).append("<li id='"+file.id+"'>"+
    						"<div><a href='fileupload/downloadByFilePath.do?path="+file.path+"&fileName="+file.name+"'><image style='width:60px;height:60px' src='upload/"+file.path+"'></a></div>"+
    						"<div class='l-icon-close progress' onclick='deleteUploadFile(\""+file.id+"\",\""+file.path+"\",this,getUploadFile)'>&nbsp;</div>"+
    						"</li>");
    			});
    			getUploadFile2();
    		}
    	}
    	// 显示电子签名
        function showAttachFile2(files){
        	if("${param.pageStatus}"=="detail"){
    			$("#procufilesBtn2").hide();
    		}
    		if(files){
    			//详情
    			var attContainerId="procufiles4";
    			if("${param.pageStatus}"=="detail"){
    				$.each(files,function(i){
    					var file=files[i];
    					 //显示附件
    					$("#"+attContainerId).append("<li id='"+file.id+"'>"+
    											"<div><a href='fileupload/downloadByFilePath.do?path="+file.filePath+"&fileName="+file.fileName+"'><image style='width:60px;height:60px' src='upload/"+file.filePath+"'></a></div>"+
    											"</li>");
    				});
    			}
    			//修改
    			else if("${param.pageStatus}"=="modify"){
    				$.each(files,function(i){
    					var file=files[i];
    					$("#"+attContainerId).append("<li id='"+file.id+"'>"+
    							"<div><a href='fileupload/downloadByFilePath.do?path="+file.filePath+"&fileName="+file.fileName+"'><image style='width:60px;height:60px' src='upload/"+file.filePath+"'></a></div>"+
    							"<div class='l-icon-close progress' onclick='deleteUploadFile(\""+file.id+"\",\""+file.filePath+"\",this,getUploadFile)'>&nbsp;</div>"+
    							"</li>");
    				});
    				getUploadFile2();
    			}
    		}
        }
         
      	// 将上传的电子签名id写入隐藏框中
    	function getUploadFile2(){
    		var attachId="";
    		var i=0;
    		$("#procufiles4 li").each(function(){
    			attachId+=(i==0?"":",")+this.id;
    			i=i+1;
    		});
    		if(attachId!=""){
    			attachId=attachId.substring(0,attachId.length);
    		}
    		$("#uploadFiles").val(attachId);
    	} */
        //选择部门
        function chooseOrg() {
            top.$.dialog({
                width: 800,
                height: 450,
                lock: true,
                parent: api,
                title: "选择人员",
                content: 'url:app/common/org_choose.jsp',
                cancel: true,
                ok: function () {
                    var p = this.iframe.contentWindow.getSelectedPerson();
                    if (!p) {
                        top.$.notice("请选择人员！", 3, "k/kui/images/icons/dialog/32X32/hits.png");
                        return false;
                    }
                    $("#workDepartment").val(p.id);
                    $("#workDepartmentName").val(p.name);
                }
            });
        }

        function saveUp() {
        }

        function view() {
            top.$.dialog({
                width: 1200,
                height: 800,
                lock: true,
                parent: api,
                data: {
                    window: window
                },
                title: "图片",
                content: 'url:app/humanresources/preview.jsp?id=' + uploadId
            });
        }

        function print() {

            //获取当前网址
            var curWwwPath = window.document.location.href;
            //获取主机地址之后的目录
            var pathName = window.document.location.pathname;
            var pos = curWwwPath.indexOf(pathName);
            //获取主机地址
            var localhostPaht = curWwwPath.substring(0, pos);
            //获取带"/"的项目名
            var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
            var realPath = localhostPaht;

            if ('/app' != projectName) {//如果当前运行环境不是正式服务器，早需要添加项目名
                realPath = localhostPaht + projectName;
            }
            $("#table7").hide();
            $("#div_work").hide();
            $("#image").hide();
            var LODOP = getLodop(document.getElementById('LODOP_OB'), document.getElementById('LODOP_EM'));
            var strBodyStyle = "<style>" + $("#pstyle").html() + "</style>";
            LODOP.PRINT_INIT("打印应聘人员信息");
            LODOP.ADD_PRINT_HTM('0mm', '6mm', "188mm", "280mm", strBodyStyle + $("#formObj").html());
            var sign0 = "<img border='0' style='height:200px;width:160px' src='" + realPath + "/" + photo + "'/>";
            LODOP.ADD_PRINT_IMAGE("205px", "545px", "160px", "220px", sign0);
            //LODOP.PREVIEW();
            LODOP.PRINT();
            $("#table7").show();
            $("#div_work").show();
            $("#image").show();
        }

        function ageAndBirthday() {
            if ("${param.pageStatus}" == "detail") {
                /*  var IDcardNum = $("#empIdCard").text().trim(); */
                var IDcardNum = $.trim($("#empIdCard").text())
            } else {
                var IDcardNum = $("#empIdCard").val();
            }
            if (IDcardNum.length == 18) {
                //获取年龄
                var myDate = new Date();
                var month = myDate.getMonth() + 1;
                var day = myDate.getDate();
                var age = myDate.getFullYear() - IDcardNum.substring(6, 10) - 1;
                if (IDcardNum.substring(10, 12) < month || IDcardNum.substring(10, 12) == month && IDcardNum.substring(12, 14) <= day) {
                    age++;
                }
                $("#age").html(age);
                $("#age").val(age);
                //获取出生日期
                birth = IDcardNum.substring(6, 10) + "年" + IDcardNum.substring(10, 12) + "月" + IDcardNum.substring(12, 14) + "日";
                $("#birthDate1").html(birth);
                $("#birthDate1").val(birth);
            } else {
                $("#age").val("");
                $("#birthDate1").val("");
            }
        }

    </script>
    <style type="text/css" media="print" id="pstyle">
        * {
            font-family: "宋体";
            font-size: 12px;
            letter-spacing: normal;
        }

        * {
            font-family: "宋体";
            font-size: 12px;
            letter-spacing: normal;

        }

        h1 {
            font-family: 宋体;
            font-size: 6mm;
            text-align: center;
            margin: 10px 0 0 0;
        }

        table {
            margin: 0 auto;
            width: 700px;
        }

        table td {
            height: 30px;
        }

        .l-detail-table td, .l-detail-table {
            border-collapse: collapse;
            border: 1px solid black;
        }

        /* .l-detail-table {
            padding:5px;
            border:0px solid #CFE3F8;
            border-top:0px;
            border-left:0px;
            word-break:break-all;
            table-layout:fixed;
        } */
        #table1, #table2 {
            padding: 5px;
            border: 0px solid #CFE3F8;
            border-top: 0px;
            border-left: 0px;
            word-break: break-all;
            table-layout: fixed;
        }

        #table3, #tabl4, #table5, #table6 {
            border-collapse: collapse;
            border: 1px solid black;
        }

        .l-t-td-left {
            text-align: center;
        }

        .fieldset-caption {
            margin: 15px 0px 2px 0px;
        }

        .l-t-td-title {
        }
    </style>
    <style>
        .l-panel {
            border: 1px solid #84A0C4;
            position: relative;
            text-align: left;
            width: auto;
            margin: 0 10px;
        }
    </style>
</head>
<body>
<div class="navtab" style="height: 662px;margin:0 auto;border:1px solid #ccc;">
    <c:if test='${param.positionTitle=="positionTitle" }'>
        <div title="职称聘用" id="form4">
            <%@ include file="position_title_base.jsp" %>
        </div>
    </c:if>
    <c:if test='${param.positionTitle=="positionTitle" && param.renew=="renew"}'>
        <div title="基本信息" id="form1">
            <%@ include file="registration_base.jsp" %>
        </div>
    </c:if>
</div>
</body>
</html>