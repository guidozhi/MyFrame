<%@page import="com.khnt.certificate.util.CertificateUtil" %>
<%@page import="java.security.cert.X509Certificate" %>
<%@page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true" %>
<%
    String phone = (String) session.getAttribute("Phone");
    String account = (String) session.getAttribute("Account");
    String businessId = (String) session.getAttribute("businessId");
    String processId = (String) session.getAttribute("processId");
    String activityId = (String) session.getAttribute("activityId");
    String Name = (String) session.getAttribute("Name");
    System.out.println(phone);
    System.out.println(Name);
    System.out.println(account);
    boolean isCertError = false;
    Object userName = "";
    if (request.isSecure()) {
        X509Certificate cert = CertificateUtil.extractClientCertificate(request);
        try {
            userName = CertificateUtil.extractPrincipal(cert, "CN=(.*?),");
        } catch (Exception e) {
            isCertError = true;
        }
    }
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head pageKeys="login">
    <%@include file="/k/kui-base.jsp" %>
    <title>Loading...</title>
    <meta HTTP-EQUIV="Page-Exit" CONTENT="blendTrans(Duration=0.5)">
    <script type="text/javascript" src="app/js/jQuery.md5.js"></script>
    <script type="text/javascript">
        loadCoreLibrary("login");
        var LOGIN_CERT = {
            uname: "<%=userName%>",//用户账号 admin
            isCertErr: <%=isCertError%>//是否证书读取
        };
        $(function () {
            if ( "<%=account%>" != "null" && "<%=account%>" != undefined ) {
                $.post("j_spring_security_check", {
                    j_password: $.md5("password"),
                    j_username: "<%=account%>"
                }, function (data) {
                    if ( data["success"] ) {
                        if ( "<%=businessId%>" != "" && "<%=businessId%>" != "null" && "<%=businessId%>" != "undefined" ) {
                            window.location = "${pageContext.request.contextPath}/app/fwxm/recipients/wx/ch_wx_lq_handle_view.jsp?id=<%=businessId%>&pageStatus=check&processId=<%=processId%>&activityId=<%=activityId%>";
                        } else {
                            window.location = "${pageContext.request.contextPath}/app/fwxm/recipients/wx/ch_wx_lq_handle_list.jsp";
                        }
                    } else {
                        alert("登录失败，请确认系统中你的电话号码是否正确！");
                    }
                })
            } else {
                alert("未获取到用户，请确认系统中你的电话号码是否正确！");
            }
        })
    </script>
</head>
<body>
<h1 style="padding:5mm 0 2mm 0;font-family:宋体;font-size:14mm;text-align:center;margin-top: 50%;">正在加载中...</h1></br>
</body>
</html>
