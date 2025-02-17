<%@page import="com.khnt.security.CurrentSessionUser" %>
<%@page import="com.khnt.security.util.SecurityUtil" %>
<%@ page language="java" import="java.text.SimpleDateFormat" pageEncoding="utf-8" %>
<%@ page import="java.util.Date" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head pageStatus="${param.status}">
    <title>收费</title>
    <!-- 每个页面引入，页面编码、引入js，页面标题 -->
    <%@include file="/k/kui-base-form.jsp" %>
    <script type="text/javascript" src="app/payment/payment_list.js"></script>
    <script type="text/javascript" src="app/payment/cw_bank_list.js"></script>
    <script type="text/javascript" src="app/payment/online_trade_list.js"></script>
    <%
        CurrentSessionUser user = SecurityUtil.getSecurityUser();
        String pageStatus = request.getParameter("status");
        String userid = user.getId();
    %>
    <script type="text/javascript">
        var pageStatus = "${param.status}";
        // 检验类别
        var checkType = <u:dict code="BASE_INSPECT_TYPE"/>;
        // 设备类别
        var deviceType =<u:dict code="device_classify"/>;
        // 收费状态
        var feeStatus =<u:dict code="PAYMENT_STATUS"/>;
        // 收费类型
        var advanceType =<u:dict code="ADVANCE_TYPE"/>;

        //定时请求 ，这里设置3秒循环
        //var timer1 = window.setInterval('Sign()', 3000);
        var timer;
        var flag = true; //用于控制是否继续发起轮询请求。
        //停止轮询
        //window.clearInterval(timer1);
        var userid = "<%=userid%>";
        $(function () {
            createInspectionInfoGrid();	// 初始化检验业务信息
            createCwBankInfoGrid();		// 初始化银行转账信息
            createOnlineInfoGrid();		// 初始化支付宝/微信交易信息
            //timer = window.setInterval('Sign()', 3000);
            //$("#form1").attr("action","payment/payInfo/getDetail.do?status=${param.status}&id="+api.data.id);
            $("#fk_inspection_info_id").val(api.data.id);

            $("#form1").initForm({
                toolbar: [{
                    text: '保存并推送',
                    id: 'save',
                    icon: 'save',
                    click: save
                }, {
                    text: '重新推送',
                    id: 'againSign',
                    icon: 'issued',
                    click: againSign
                }, {
                    text: '关闭',
                    id: 'close',
                    icon: 'cancel',
                    click: close
                }],
                toolbarPosition: "bottom",
                getSuccess: function (resp) {
                    inspectionInfoGrid.loadData({
                        Rows: resp.data["inspectionInfoDTOList"]
                    });
                    cwBankInfoGrid.loadData({
                        Rows: resp.data["cwBankDTOList"]
                    });
                },
                afterParse:function(){
                	$.post("payment/order/lockUserCid/queryLockUser.do",
                		{
                			"userId":userid
                		},
               			function(res){
                			if (res.success && res.data && res.data.cid != "") {
                                $("#cid").ligerGetComboBoxManager().setValue(res.data.cid);
                            }
                	});
                }
            });
           

        })


        function againSign() {
            $("#againSign").ligerGetButtonManager().setDisabled();
            setTimeout(function () {
                $("#againSign").ligerGetButtonManager().setEnabled();
            }, 3000);
            var cid = $("#cid").ligerComboBox().getValue();
            //收费类型
            var payType = $("#pay_type-txt").ligerComboBox().getValue();
            $.ajax({
                type: 'POST',
                url: 'payment/payInfo/queryPrintingPaySign1.do?fkInspectionInfoId=' + api.data.id,//打印时验证收费信息是否保存
                data: {},
                async: false,
                dataType: 'JSON',
                success: function (ress) {
                    $("body").unmask();
                    if (ress.success == true) {
                        var fkPayInfoId = ress.data.fkPayInfoId;
                        $.post("payment/payInfo/isNotPrintingPaySign.do", {fkPayInfoId: fkPayInfoId}, function (res) {
                            var imageFile = res.data;
                            if (imageFile != "该收费结算单未签字确认") {
                                $.ligerDialog.error("亲，你已保存签名，请勿反复推送！");
                            } else {
                                $.ajax({
                                    type: 'POST',
                                    url: 'payment/payInfo/queryAppSign.do?id=' + api.data.id + '&cid=' + cid + '&payType=' + payType,
                                    data: {},
                                    dataType: 'JSON',
                                    success: function (ress) {
                                        $("#againSign").ligerGetButtonManager().setEnabled();
                                        if (ress.success == true) {
                                            top.$.notice("推送成功！");
                                            timer = window.setInterval('Sign()', 3000);
                                        } else {
                                            $.ligerDialog.error(ress.msg);
                                        }
                                    }
                                })
                            }
                        })
                    } else {
                        $("#againSign").ligerGetButtonManager().setEnabled();
                        $.ligerDialog.error(ress.msg);
                    }
                }, error: function (XMLHttpRequest, textStatus, errorThrown) {
                    $("#againSign").ligerGetButtonManager().setEnabled();
                    flag = true;
                }
            })
        }

        function Sign() {
            if (flag) {
                flag = false;
                $.ajax({
                    type: 'POST',
                    url: 'payment/payInfo/queryPrintingPaySign.do?fkInspectionInfoId=' + api.data.id,//打印时验证收费信息是否保存
                    data: {},
                    dataType: 'JSON',
                    success: function (ress) {
                        if (ress.success == true) {
                            var fkPayInfoId = ress.data.fkPayInfoId;
                            $.post("payment/payInfo/isNotPrintingPaySign.do", {fkPayInfoId: fkPayInfoId}, function (res) {
                                var imageFile = res.data;
                                if (imageFile != "该收费结算单未签字确认") {
                                    var pathFile = imageFile;
                                    window.clearInterval(timer);
                                    top.$.dialog({
                                        width: 800,
                                        height: 500,
                                        lock: true,
                                        title: "打印缴费结算单",
                                        content: 'url:payment/payInfo/doPrint.do?id=' + api.data.id + '&pathFile=' + pathFile + '&type=2',	// 1 收费打印 2 已收费补打
                                        data: {"window": window, "qm": api.data.window.Qm}
                                    })
                                    api.data.window.refreshGrid();
                                    api.close();
                                } else {
                                    flag = true;
                                }
                            }).error(function (xhr, errorText, errorType) {
                                flag = true;
                            })
                        } else {
                            flag = true;
                        }
                    }, error: function (XMLHttpRequest, textStatus, errorThrown) {
                        flag = true;
                    }
                })
            }
        }

        function close() {
            api.close();
        }

        function _save() {
            if (checkBeforeSave()) {
                //验证表单数据
                if ($('#form1').validate().form()) {
                    $.ligerDialog.confirm("确定保存并推送消息？保存后无法修改！", function (yes) {
                        if (yes) {
                            $("#save").attr("disabled", "disabled");
                            var formData = $("#form1").getValues();
                            var datas = {};
                            datas = formData;
                            //验证grid
                            if (!validateGrid(inspectionInfoGrid)) {
                                return false;
                            }
                            //验证grid
                            if (!validateGrid(cwBankInfoGrid)) {
                                return false;
                            }
                            datas["inspectionInfoDTOList"] = inspectionInfoGrid.getData();
                            datas["cwBankDTOList"] = cwBankInfoGrid.getData();
                            datas["machinePayTradeList"] = onlineInfoGrid.getData();
                            //验证是否保存
                            $.post("payment/payInfo/isNotSave.do", {fkInspectionInfoId: api.data.id}, function (ress) {
                                if (ress.success == true) {
                                    $("body").mask("正在保存数据，请稍后！");
                                    $.ajax({
                                        url: "payment/payInfo/savePayInfo.do?status=${param.status}",
                                        type: "POST",
                                        datatype: "json",
                                        contentType: "application/json; charset=utf-8",
                                        data: $.ligerui.toJSON(datas),
                                        success: function (resp) {
                                            $("body").unmask();
                                            if (resp["success"]) {
                                                printPayInfo();
                                            } else {
                                                $("#save").removeAttr("disabled");
                                                $.ligerDialog.error(resp.msg);
                                            }
                                        },
                                        error: function (resp) {
                                            $("body").unmask();
                                            $("#save").removeAttr("disabled");
                                            $.ligerDialog.error(resp.msg);
                                        }
                                    });
                                } else {
                                    $.ligerDialog.error(ress.msg);
                                }
                            })
                        }
                    });
                }
            }
        }

        function save() {
            var pay_type = $("input[id='pay_type-txt']").ligerComboBox().getValue();
            var invoice_no = $("#invoice_no").val();	// 发票号
            if (pay_type != "3") {
                if ("" == invoice_no) {
                    $.ligerDialog.alert("请输入发票号！");
                    return;
                } else {
                    invoice_no = invoice_no.trim();	// 去空格
                    if (invoice_no.length == 10) {
                        invoice_no = invoice_no.substr(0, (invoice_no.length - 1));	// 截最后一位
                        $("#invoice_no").val(invoice_no);
                    }
                    $.getJSON("payment/payInfo/validateInvoices.do?invoice_no=" + invoice_no, function (resp) {
                        if (!resp.success) {
                            $.ligerDialog.alert("亲，系统未能识别发票号（" + invoice_no + "），请核实是否已作废！确认未作废请联系财务人员导入！", "提示");
                            return;
                        } else {
                            _save();
                        }
                    })
                }
            } else {
                _save();
            }
        }

        // 保存后推送
        function printPayInfo() {
            var cid = $("#cid").ligerComboBox().getValue();
            //收费类型
            var payType = $("#pay_type-txt").ligerComboBox().getValue();
            $.ajax({
                type: 'POST',
                url: 'payment/payInfo/queryAppSign.do?id=' + api.data.id + '&cid=' + cid + '&payType=' + payType,
                data: {},
                dataType: 'JSON',
                success: function (ress) {
                    if (ress) {
                        top.$.notice("推送成功！");
                        timer = window.setInterval('Sign()', 3000);
                    } else {
                        $.ligerDialog.error(ress.msg);
                    }
                }
            })
        }

        // 保存前验证数据
        function checkBeforeSave() {
            // 收费方式 1 现金缴费 2 银行转账 3 免收费 4 现金及转账 5 POS机刷卡 6 现金及POS机刷卡 7 转账及POS机刷卡
            var pay_type = $("input[id='pay_type-txt']").ligerComboBox().getValue();
            var pay_receive = $("#pay_receive").val();	// 应收总金额
            var pay_received = $("#pay_received").val();	// 实收总金额
            // if ( parseFloat(pay_receive) != parseFloat(pay_received) ) {
            //     $.ligerDialog.alert("实收金额不等于应收金额，请核实");
            //     return false;
            // }
            if ("3" != pay_type) {
                var remark = $("#remark").val();
                if (pay_received == 0) {
                    $.ligerDialog.alert("实收总金额为0，请核实！");
                    return false;
                } else {
                    if ("1" == pay_type) {
                        $("#cash_pay").val(pay_received);
                    } else if ("2" == pay_type) {
                        $("#remark").val(pay_received);
                    } else if ("4" == pay_type) {
                        var cash_pay = $("#cash_pay").val();	// 现金
                        if (parseFloat(cash_pay) <= 0) {
                            $.ligerDialog.alert("请输入现金金额！");
                            return false;
                        }
                        if (parseFloat(remark) <= 0) {
                            $.ligerDialog.alert("请输入转账金额！");
                            return false;
                        }
                    } else if ("5" == pay_type) {
                        $("#pos").val(pay_received);
                    } else if ("6" == pay_type) {	// 现金及POS机刷卡
                        var cash_pay = $("#cash_pay").val();
                        if (parseFloat(cash_pay) <= 0) {
                            $.ligerDialog.alert("请输入现金金额！");
                            return false;
                        }
                        var pos = $("#pos").val();
                        if (parseFloat(pos) <= 0) {
                            $.ligerDialog.alert("请输入POS机刷卡金额！");
                            return false;
                        }
                    } else if ("7" == pay_type) {	// 转账及POS机刷卡
                        if (parseFloat(remark) <= 0) {
                            $.ligerDialog.alert("请输入转账金额！");
                            return false;
                        }
                        var pos = $("#pos").val();
                        if (parseFloat(pos) <= 0) {
                            $.ligerDialog.alert("请输入POS机刷卡金额！");
                            return false;
                        }
                    } else if ("8" == pay_type) {
                        $("#hand_in").val(parseFloat(pay_received));	// 上缴财政
                    } else if ("9" == pay_type) {
                        $("#draft").val(parseFloat(pay_received));		// 承兑汇票
                    } else if ("10" == pay_type) {
                        $("#alipay_money").val(parseFloat(pay_received));		// 支付宝
                    } else if ("11" == pay_type) {
                        $("#weixin_money").val(parseFloat(pay_received));		// 微信
                    } else if ("12" == pay_type) {
                        $("#alipay_mobile").val(parseFloat(pay_received));		// 平板（支付宝）
                    } else if ("13" == pay_type) {
                        $("#wechat_mobile").val(parseFloat(pay_received));		// 平板（微信）
                    } else if ("14" == pay_type) {
                        if (parseFloat(remark) <= 0) {
                            $.ligerDialog.alert("请输入转账金额！");
                            return false;
                        }
                        if (parseFloat($("#alipay_mobile").val()) <= 0) {
                            $.ligerDialog.alert("请输入平板（支付宝）金额");
                            return false;
                        }
                    } else if ("15" == pay_type) {
                        if (parseFloat(remark) <= 0) {
                            $.ligerDialog.alert("请输入转账金额！");
                            return false;
                        }
                        if (parseFloat($("#wechat_mobile").val()) <= 0) {
                            $.ligerDialog.alert("请输入平板（微信）金额");
                            return false;
                        }
                    }
                }
                if (parseFloat(remark) > 0) {
                    if (cwBankInfoGrid.getData() == "" || cwBankInfoGrid.getData() == null) {
                        $("#save").removeAttr("disabled");
                        $.ligerDialog.alert("亲，转账业务请点击“转账”按钮，选择银行转账记录！");
                        return false;
                    } else {
                        // 验证银行转账本次收费金额合计是否等于实收总金额
                        var totalMoney = 0;		// 银行转账本次收费金额合计
                        var data = cwBankInfoGrid.getData();
                        for (var item in data) {
                            totalMoney += parseFloat(data[item].usedMoney);
                        }
                        if (parseFloat(totalMoney) != parseFloat(remark)) {
                            $("#save").removeAttr("disabled");
                            $.ligerDialog.alert("亲，银行转账中“本次收费金额”不等于缴费单中的“转账”金额，请检查这两项是否填写正确！");
                            return false;
                        }
                    }
                }
                var alipay_money = parseFloat($("#alipay_money").val());
                var weixin_money = parseFloat($("#weixin_money").val());
                if (alipay_money > 0 || weixin_money > 0) {
                    var pay_title = alipay_money > 0 ? "支付宝" : "微信";
                    if (onlineInfoGrid.getData() == "" || onlineInfoGrid.getData() == null) {
                        $("#save").removeAttr("disabled");
                        $.ligerDialog.alert("亲，" + pay_title + "业务请点击“" + pay_title + "”按钮，选择" + pay_title + "交易记录！");
                        return;
                    } else {
                        // 验证支付宝交易金额是否等于实收总金额
                        var totalMoney = 0;								// 交易金额
                        var pay_received = $("#pay_received").val();	// 实收总金额
                        var data = onlineInfoGrid.getData();
                        for (var item in data) {
                            totalMoney += parseFloat(data[item].buyer_pay_amount);
                        }
                        if (parseFloat(totalMoney) != parseFloat(pay_received)) {
                            $("#save").removeAttr("disabled");
                            $.ligerDialog.alert("亲，您所选的" + pay_title + "交易金额为" + totalMoney + "元，不等于本次应支付金额" + pay_received + "元，请检查" + pay_title + "交易记录是否选择无误！");
                            return;
                        }
                    }
                }
                if (onlineInfoGrid.getData().length > 1) {
                    $.ligerDialog.alert("亲，一次最多选择一个" + pay_title + "交易记录！");
                    $("#save").removeAttr("disabled");
                    return;
                }
            } else {
                $("#pay_receive").val("0");
                $("#pay_received").val("0");
            }
            return true;
        }

        function beforeSelect(pay_type) {
            if ('3' == pay_type) {
                return confirm("“免收费”操作将修改所有业务的金额为0，是否进行修改？");
            } else {
                return true;
            }
        }

        function change(pay_type) {
            var typeArrayReadonly = ["cash_pay", "remark", "pos", "hand_in", "draft", "alipay_money", "weixin_money", "alipay_mobile", "wechat_mobile"];
            var typeArray = [];
            var gridManager = [cwBankInfoGrid, onlineInfoGrid]; //页面上的grid
            var gridClean = [false, false]; //默认改变值都不进行清除。
            var pay_receive = $("#pay_received").val();	// 应收总金额
            //$("#pay_received").val(pay_receive);
            if (pay_receive == 0) {
                initInfo(pay_type);
            }
            if ("1" == pay_type) {
                typeArray = ["cash_pay"];
                gridClean = [true, true];
            } else if ("2" == pay_type) {
                typeArray = ["remark"];
                gridClean = [false, true];
            } else if ("3" == pay_type) {
                $("#pay_receive").val(0);
                $("#pay_received").val(0);
                gridClean = [true, true];
            } else if ("4" == pay_type) {	// 收费方式为“现金及转账”
                typeArray = ["cash_pay", "remark"];
                gridClean = [false, true];
            } else if ("5" == pay_type) {
                typeArray = ["pos"];
                gridClean = [true, true];
            } else if ("6" == pay_type) {
                typeArray = ["cash_pay", "pos"];
                gridClean = [true, true];
            } else if ("7" == pay_type) {
                typeArray = ["remark", "pos"];
                gridClean = [false, true];
            } else if ("8" == pay_type) {
                typeArray = ["hand_in"];
                gridClean = [true, true];
            } else if ("9" == pay_type) {
                typeArray = ["draft"];
                gridClean = [true, true];
            } else if ("10" == pay_type) {
                typeArray = ["alipay_money"];
                gridClean = [true, false];
            } else if ("11" == pay_type) {
                typeArray = ["weixin_money"];
                gridClean = [true, false];
            } else if ("12" == pay_type) {
                typeArray = ["alipay_mobile"];
                gridClean = [true, true];
            } else if ("13" == pay_type) {
                typeArray = ["wechat_mobile"];
                gridClean = [true, true];
            } else if ("14" == pay_type) {
                typeArray = ["remark", "alipay_mobile"];
                gridClean = [false, true];
            } else if ("15" == pay_type) {
                typeArray = ["remark", "wechat_mobile"];
                gridClean = [false, true];
            }
            for (var i = 0; i < typeArrayReadonly.length; i++) {
                if ($.inArray(typeArrayReadonly[i], typeArray) > -1) {
                    continue;
                }
                $("#" + typeArrayReadonly[i]).val(0);
                $("#" + typeArrayReadonly[i]).attr("readonly", "readonly");
                $("#" + typeArrayReadonly[i]).parent().addClass("l-text-readonly");
                $("#" + typeArrayReadonly[i]).parent().addClass("l-text-disabled");
            }
            for (var i = 0; i < typeArray.length; i++) {
                if (i == 0) {
                    $("#" + typeArray[i]).val(parseFloat(pay_receive));
                } else {
                    $("#" + typeArray[i]).val(0);
                }
                $("#" + typeArray[i]).removeAttr("readonly");
                $("#" + typeArray[i]).parent().removeClass("l-text-readonly");
                $("#" + typeArray[i]).parent().removeClass("l-text-disabled");
            }
            for (var i = 0; i < gridManager.length; i++) {
                if (gridClean[i]) {
                    gridManager[i].loadData({Rows: []});
                }
            }
        }

        function changeMoney(_this) {
            // 收费方式 1 现金缴费 2 银行转账 3 免收费 4 现金及转账 5 POS机刷卡 6 现金及POS机刷卡 7 转账及POS机刷卡 8 上缴财政 9 承兑汇票 10 支付宝 11 微信
            var pay_type = $("input[id='pay_type-txt']").ligerComboBox().getValue();
            var pay_receive = $("#pay_receive").val();
            if ("4" == pay_type) {
                balanceMoney(pay_receive, "cash_pay", "remark", _this);
            } else if ("6" == pay_type) {
                balanceMoney(pay_receive, "cash_pay", "pos", _this);
            } else if ("7" == pay_type) {
                balanceMoney(pay_receive, "remark", "pos", _this);
            } else if ("14" == pay_type) {
                balanceMoney(pay_receive, "remark", "alipay_mobile", _this);
            } else if ("15" == pay_type) {
                balanceMoney(pay_receive, "remark", "wechat_mobile", _this);
            } else {
                singlePay(pay_receive, _this);
            }
        }

        function singlePay(total, _this) {
            var _this_val = $(_this).val();
            if (parseFloat(_this_val) > parseFloat(total)) {
                $(_this).val(total);
                //$("#pay_received").val(total);
            } else if (parseFloat(_this_val) < 0) {
                $(_this).val(0);
                //$("#pay_received").val(0);
            } else {
                //$("#pay_received").val(_this_val);
            }
        }

        function balanceMoney(total, t1, t2, _this) {
            var id = $(_this).attr("id");
            if (parseFloat(total) - $(_this).val() < 0) {
                $(_this).val(parseFloat(total));
                if (id == t1) {
                    $("#" + t2).val(0);
                } else if (id == t2) {
                    $("#" + t1).val(0);
                }
                return;
            } else if ($(_this).val() < 0) {
                $(_this).val(0);
                if (id == t1) {
                    $("#" + t2).val(parseFloat(total));
                } else if (id == t2) {
                    $("#" + t1).val(parseFloat(total));
                }
                return;
            }
            if (id == t1) {
                $("#" + t1).val(parseFloat($(_this).val()));
                $("#" + t2).val(parseFloat(total) - $(_this).val());
            } else if (id == t2) {
                $("#" + t2).val(parseFloat($(_this).val()));
                $("#" + t1).val(parseFloat(total) - $(_this).val());
            }
        }

        // 选择缴费单位
        function selectPayCompany() {
            top.$.dialog({
                parent: api,
                width: 800,
                height: 550,
                lock: true,
                title: "选择缴费单位",
                content: 'url:app/payment/choose_company_list.jsp?com_ids=${param.company_ids}',
                data: {"parentWindow": window}
            });
        }

        function callBack(id, name) {
            $('#fk_company_id').val(id);	// 缴费单位ID
            $('#company_name').val(name);	// 缴费单位名称
        }


        // 选择合同
        function selectPact() {
            top.$.dialog({
                parent: api,
                width: 800,
                height: 550,
                lock: true,
                title: "选择缴费单位",
                content: 'url:app/pact/choose_pact_list.jsp?org_id=${param.org_id}',
                data: {"parentWindow": window}
            });
        }

        function callBackPact(id, name) {
            $('#pact_id').val(id);	// 缴费单位ID
            $('#pact_name').val(name);	// 缴费单位名称
        }

        function initInfo(pay_type) {
            $.getJSON("payment/payInfo/getDetail.do?status=${param.status}&id=" + api.data.id, function (resp) {
                if (resp.success) {
                    inspectionInfoGrid.loadData({
                        Rows: resp.data["inspectionInfoDTOList"]
                    });
                    $("#id").val(resp.data["id"]);	// 存在收费记录时，此处需手动设置ID值（重要）
                    $("#pay_receive").val(parseFloat(resp.data["pay_receive"]));	// 应收总金额
                    if(resp.data["pay_received"]==0){
            			 $("#pay_received").val(parseFloat(resp.data["pay_receive"]));	// 实收总金额
           			 }else{
           				 $("#pay_received").val(parseFloat(resp.data["pay_received"]));	// 实收总金额
           			 }
                   
                    if (api.data.com_ids.length == 32) {
                        $('#fk_company_id').val(resp.data["inspectionInfoDTOList"][0].com_id);	// 缴费单位ID
                        $('#company_name').val(resp.data["inspectionInfoDTOList"][0].com_name);	// 缴费单位名称
                    }

                }
            })
        }

        // 选择转账记录
        function selectTransfer() {
            // 收费方式 1 现金缴费 2 银行转账 3 免收费 4 现金及转账 5 POS机刷卡 6 现金及POS机刷卡 7 转账及POS机刷卡
            var pay_type = $("input[id='pay_type-txt']").ligerComboBox().getValue();
            if (pay_type == "") {
                $.ligerDialog.alert("亲，请先选择“收费方式”！");
                return;
            } else {
                if ("2" == pay_type || "4" == pay_type || "7" == pay_type || '14' == pay_type || '15' == pay_type) {
                    top.$.dialog({
                        parent: api,
                        width: 800,
                        height: 550,
                        lock: true,
                        title: "选择转账记录",
                        content: 'url:app/payment/choose_transfer_list.jsp',
                        data: {"parentWindow": window}
                    });
                } else {
                    $.ligerDialog.alert("亲，您选择的“收费方式”未涉及到转账业务哦，请核实！");
                    return;
                }
            }
        }

        function callBackTransfer(ids) {
            // 验证所选银行转账记录是否重复
            var data = cwBankInfoGrid.getData();
            for (var item in data) {
                if (ids.indexOf(data[item].id) != -1) {
                    $.ligerDialog.alert("亲，不能选择重复的“银行转账”哦，请核实！");
                    return;
                }
            }

            var useMoney = $("#remark").val();
            $.post("cw/bank/queryBankInfos.do?ids=" + ids + "&useMoney=" + useMoney, function (resp) {
                if (resp.success) {
                    cwBankInfoGrid.addRows(resp.list);
                    /*
                    cwBankInfoGrid.loadData({
                        Rows : data
                    });
                    */
                } else {
                    $.ligerDialog.error(resp.msg);
                }
            });
        }

        // 选择在线支付交易记录
        function selectOnlineTrade(type) {
            // 收费方式 10：支付宝 11：微信
            var trade_type = "alipay";
            var pay_title = "支付宝";
            var pay_type = $("input[id='pay_type-txt']").ligerComboBox().getValue();
            if (pay_type == "") {
                $.ligerDialog.alert("亲，请先选择“收费方式”！");
                return;
            } else {
                if ("10" == pay_type || "11" == pay_type) {
                    if ("11" == type) {
                        pay_title = "微信";
                        trade_type = "weixin";
                    }
                    top.$.dialog({
                        parent: api,
                        width: 800,
                        height: 550,
                        lock: true,
                        title: "选择" + pay_title + "交易记录",
                        content: 'url:app/payment/choose_online_trade_list.jsp?pay_type=' + trade_type,
                        data: {"parentWindow": window}
                    });
                } else {
                    $.ligerDialog.alert("亲，您选择的“收费方式”未涉及到" + pay_title + "业务哦，请核实！");
                    return;

                }
            }
        }

        function callBackOnlineTrade(id, pay_type, buyer_pay_amount) {
            if ("支付宝" == pay_type) {
                $("#fk_alipay_trade_id").val(id);
            } else if ("微信" == pay_type) {
                $("#fk_weixin_trade_id").val(id);
            }
            $.post("pay/trade/queryTradeInfos.do?id=" + id, function (resp) {
                if (resp.success) {
                    onlineInfoGrid.addRows(resp.list);
                } else {
                    $.ligerDialog.error(resp.msg);
                }
            });
        }

        String.prototype.trim = function () {
            return this.replace(/(^\s*)|(\s*$)/g, '');
        }

        function selectCourse() {
            var status = "add";
            top.$.dialog({
                width: 600,
                height: 400,
                lock: true,
                title: "添加设备",
                content: 'url:app/flow/addPanelComputer_detail.jsp',
                /* ok:function(w){
                    var datas = this.iframe.contentWindow.getSelectResult();
                        return true;
                    },
                cancel:true */
            });
        }
    </script>
</head>
<body <%if("add".equals(pageStatus)){%>onload="initInfo('0');"<%}%> >
<div class="navtab">
    <div title="缴费情况" tabId="payInfoTab" style="height: 400px">
        <form id="form1">
            <input type="hidden" name="id" id="id"/>
            <input type="hidden" name="fk_inspection_info_id" id="fk_inspection_info_id" value=""/>
            <input type="hidden" name="pay_no" id="pay_no"/>
            <input type="hidden" name="payment_status" id="payment_status"/>
            <input type="hidden" name="created_by" id="created_by"/>
            <input type="hidden" name="created_date" id="created_date"/>
            <input type="hidden" name="receive_man_name" id="receive_man_name"
                   <%if("add".equals(pageStatus)){%>value="<%=user.getName() %>"<%}%> />
            <fieldset class="l-fieldset">
                <legend class="l-legend">
                    <div>缴费单</div>
                </legend>
                <table border="1" cellpadding="3" cellspacing="0" width="" class="l-detail-table">
                    <c:choose>
                        <c:when test='${param.org_id=="100036"}'>

                            <tr>
                                <td class="l-t-td-left">缴费单位：</td>
                                <td class="l-t-td-right">
                                    <input name="fk_company_id" id="fk_company_id" type="hidden"/>
                                    <input name="company_name" id="company_name" type="text" ltype='text'
                                           validate="{required:true,maxlength:200}"
                                           ligerui="{value:'',iconItems:[{icon:'add',click:function(){selectPayCompany()}}]}"/>
                                    <!--  onclick="selectPayCompany()" -->
                                </td>


                                <td class="l-t-td-left">合同：</td>
                                <td class="l-t-td-right">
                                    <input type="hidden" name="pact_id" id="pact_id"/>
                                    <input name="pact_name" id="pact_name" type="text" ltype='text'
                                           validate="{required:false,maxlength:200}"
                                           ligerui="{value:'',iconItems:[{icon:'add',click:function(){selectPact()}}]}"/>
                                    <!--  onclick="selectPayCompany()" -->
                                </td>
                            </tr>

                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td class="l-t-td-left">缴费单位：</td>
                                <td class="l-t-td-right" colspan="3">
                                    <input name="fk_company_id" id="fk_company_id" type="hidden"/>
                                    <input name="company_name" id="company_name" type="text" ltype='text'
                                           validate="{required:true,maxlength:200}"
                                           ligerui="{value:'',iconItems:[{icon:'add',click:function(){selectPayCompany()}}]}"/>
                                    <!--  onclick="selectPayCompany()" -->
                                </td>
                            </tr>

                        </c:otherwise>
                    </c:choose>


                    <tr>
                        <td class="l-t-td-left">收费方式：</td>
                        <td class="l-t-td-right"><u:combo name="pay_type" code="PAY_TYPE" validate="required:true"
                                                          attribute="onBeforeSelect:beforeSelect,onSelected:change"/></td>
                        <td class="l-t-td-left">应收总金额：</td>
                        <td class="l-t-td-right">
                            <input name="pay_receive" id="pay_receive" type="text" ltype='float'
                                   ligerui="{readonly:true}" title="提示：此处不能修改金额，如需修改，请先提交金额修改申请！"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="l-t-td-left">实收总金额：</td>
                        <td class="l-t-td-right">
                            <input name="pay_received" id="pay_received" type="text" ltype='float'
                                   ligerui="{readonly:true}" title="提示：此处不能修改金额，如需修改，请先提交金额修改申请！"/>
                            <!-- validate="{required:true,maxlength:18}"  -->
                        </td>
                        <td class="l-t-td-left">现金：</td>
                        <td class="l-t-td-right">
                            <input name="cash_pay" id="cash_pay" type="text" ltype='float' ligerui="{maxlength:18}"
                                   onchange="changeMoney(this)"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="l-t-td-left">转账：</td>
                        <td class="l-t-td-right">
                            <input type="hidden" name="fk_cw_bank_id" id="fk_cw_bank_id"/>
                            <input name="remark" id="remark" type="text" ltype='float'
                                   ligerui="{maxlength:18, value:'',iconItems:[{icon:'add',click:function(){selectTransfer()}}]}"
                                   onchange="changeMoney(this)"/><!-- onclick="selectTransfer()"  -->
                        </td>
                        <td class="l-t-td-left">POS机刷卡：</td>
                        <td class="l-t-td-right">
                            <input name="pos" id="pos" type="text" ltype='float' ligerui="{maxlength:18}"
                                   onchange="changeMoney(this)"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="l-t-td-left">上缴财政：</td>
                        <td class="l-t-td-right">
                            <input name="hand_in" id="hand_in" type="text" ltype='float' ligerui="{maxlength:18}"
                                   onchange="changeMoney(this)"/>
                        </td>
                        <td class="l-t-td-left">承兑汇票：</td>
                        <td class="l-t-td-right">
                            <input name="draft" id="draft" type="text" ltype='float' ligerui="{maxlength:18}"
                                   onchange="changeMoney(this)"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="l-t-td-left">支付宝：</td>
                        <td class="l-t-td-right">
                            <input type="hidden" name="fk_alipay_trade_id" id="fk_alipay_trade_id"/>
                            <input name="alipay_money" id="alipay_money" type="text" ltype='float'
                                   ligerui="{maxlength:18, value:'',iconItems:[{icon:'add',click:function(){selectOnlineTrade('10')}}]}"
                                   onchange="changeMoney(this)"/><!-- onclick="selectTransfer()"  -->
                        </td>
                        <td class="l-t-td-left">微信：</td>
                        <td class="l-t-td-right">
                            <input type="hidden" name="fk_weixin_trade_id" id="fk_weixin_trade_id"/>
                            <input name="weixin_money" id="weixin_money" type="text" ltype='float'
                                   ligerui="{maxlength:18, value:'',iconItems:[{icon:'add',click:function(){selectOnlineTrade('11')}}]}"
                                   onchange="changeMoney(this)"/><!-- onclick="selectTransfer()"  -->
                        </td>
                    </tr>
                    <tr>
                        <td class="l-t-td-left">平板支付（支付宝）：</td>
                        <td class="l-t-td-right">
                            <input name="alipay_mobile" id="alipay_mobile" type="text" ltype='float'
                                   ligerui="{maxlength:18}" onchange="changeMoney(this)"/>
                        </td>
                        <td class="l-t-td-left">平板支付（微信）：</td>
                        <td class="l-t-td-right">
                            <input name="wechat_mobile" id="wechat_mobile" type="text" ltype='float'
                                   ligerui="{maxlength:18}" onchange="changeMoney(this)"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="l-t-td-left">缴费日期：</td>
                        <td class="l-t-td-right">
                            <input name="pay_date" id="pay_date" type="text" ltype='date' validate="{required:true}"
                                   ligerui="{format:'yyyy-MM-dd'}"
                                   value="<%=new SimpleDateFormat("yyyy-MM-dd").format(new Date())%>"/>
                        </td>
                        <td class="l-t-td-left">发票号：</td>
                        <td class="l-t-td-right">
                            <input name="invoice_no" id="invoice_no" type="text" ltype='text'
                                   validate="{required:false,maxlength:10}"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="l-t-td-left">设备选择：</td>
                        <td class="l-t-td-right">
                            <input name="cid" id="cid" ltype="select" validate="{required:true}"
                                   ligerui="{value:'',data:<u:dict code="SIGN_DEVICE"/>,iconItems:[{icon:'add',click:function(){selectCourse()}}]}"/>

                        </td>
                    </tr>
                </table>
            </fieldset>
            <fieldset class="l-fieldset">
                <legend class="l-legend">
                    <div>银行转账</div>
                </legend>
                <div id="cwBankInfoList"></div>
            </fieldset>
            <fieldset class="l-fieldset">
                <legend class="l-legend">
                    <div>支付宝/微信</div>
                </legend>
                <div id="onlineInfoList"></div>
            </fieldset>
            <fieldset class="l-fieldset">
                <legend class="l-legend">
                    <div>业务信息</div>
                </legend>
                <div id="inspectionInfoList"></div>
            </fieldset>
        </form>
    </div>
</div>
</body>
</html>
