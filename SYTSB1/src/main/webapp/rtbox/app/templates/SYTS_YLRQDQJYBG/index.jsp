<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://khnt.com/tags/ui" prefix="u"%>
<%
	String basePath = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()+request.getServletPath().substring(0,request.getServletPath().lastIndexOf("/")+1);   
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>CTJB-0204-BG压力容器定期检验报告</title>
<%@include file="/rtbox/public/base.jsp" %>
<meta name="keywords" content="报表工具" />
<meta name="description" content="报表工具">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Theme CSS -->
<link rel="stylesheet" type="text/css" href="rtbox/app/templates/default/assets/skin/default_skin/css/theme.css">

<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
  <script src="rtbox/app/templates/default/assets/js/html5shiv.js"></script>
  <script src="rtbox/app/templates/default/assets/js/respond.min.js"></script>
<![endif]-->
<script type="text/javascript"> var relColumn='fk_report_id=${param.fk_report_id}&fk_inspection_info_id=${param.fk_inspection_info_id}';</script>

<script type="text/javascript">

var markOptions = <u:dict code="problem_nature_type"></u:dict>;
var fk_report_id="${param.fk_report_id}";
var input="${param.input}";
var info_id="${param.id}";
var pageStatus="${param.pageStatus}";
var rt_code="${param.code}";
var status = "${param.status}";
var check_op ="${param.check_op}";
var singleAudit = "${param.singleAudit}";
var org_id = "${param.org_id}";
var basePath="<%=basePath%>";
function saveRight(){
	try {
		rightFrame.submitForm();
	} catch (e) {
		// TODO: handle exception
	}
}
function fontSizes(){
	var objS = document.getElementById("fontSizes");
	var index = objS.selectedIndex;
	var grade = objS.options[index].value;
	rightFrame.fontSizes(grade);
}
function markColor(){
	rightFrame.markColor();
}
function fontFamilys(){
	var objS = document.getElementById("fontFamilys");
	var index = objS.selectedIndex
	var grade = objS.options[index].value;
	rightFrame.fontFamilys(grade);
}
function sup(){
	rightFrame.sup();
}
function sub(){
	rightFrame.sub();
}
function fontBold(){
	rightFrame.fontBold();
}
function fontItalic(){
	rightFrame.fontItalic();
}
function copyToRow(){
	var obj = rightFrame.bindInputEl;
	rightFrame.copyToRow(obj);
}
function sequence(){
	var obj = rightFrame.bindInputEl;
	//rightFrame.sequence(obj);
	var data = {};
	$.ligerDialog.open({ 
		title: '填写序号规则', 
		width: 350,
		height: 200, 
		url: "rtbox/app/templates/default/fill_sequence.jsp", 
		buttons: [
            {
                text: '确定', onclick: function (item, dialog) {
                    var data = dialog.frame.getData();
 					var reg = /^[0-9]*$/;
                    if(reg.test(data.initValue)){
                    	rightFrame.sequence(obj,data.initValue,data.prefix,data.suffix);
                    	dialog.hide();
                    }else{
                    	top.$.notice("初始序号必须为正整数！");
                    	return; 
                    }
                }
            },
            {
                text: '取消', onclick: function (item, dialog) {
                    dialog.hide();
                }
            }
        ]
    });
	
}

function drawMarker(){
	var obj = rightFrame.focus_element;
	rightFrame.label(obj);
}

$(function() {
	if(document.getElementById('tsColor')!=undefined){
		document.getElementById('tsColor').onchange = function(){
			rightFrame.tsColor(this.value);
		};
	}
	
	//详情时影藏按钮工具栏
	if(status=="detail"){
		$("#treeH").css("padding-top","0px");
	}
	
});
</script>
<script type="text/javascript" src="rtbox/app/templates/default/tpl01.js"></script>
<!-- <script type="text/javascript" src="rtbox/app/templates/default/tpl02.js"></script>  -->
<script type="text/javascript" src="/k/kui/frame/main.js"></script> 
<style type="text/css">
	
	.test11 td{ padding:10px 0; }
	.input11{ height: 26px; line-height: 26px; border:1px solid #ddd;   }
	.testl{ text-align: right; padding-right: 10px;  }

	/*2017-09-06 09:32:23*/
	
	.bzhu { position: relative; width: 200px;   }
	.flag { /*border: 1px dashed #e22118;*/ position: relative; padding-left:5px;  }
	.bzhu span{ position: absolute; left: -1px; top:-1px;border:4px solid transparent; border-top:4px solid #e22118; border-left:4px solid #e22118;  }
	.bzhu_box{ 
		border: 1px solid #e22118; 
		position: absolute; 
		top:26px; 
		display: none;
		width: 250px; 
		min-height: 100px; 
		padding: 5px;
		z-index:100;
		-webkit-box-shadow: 2px 3px 0px 0px rgba(0,0,0,.15);
		box-shadow:2px 3px 0px 0px rgba(0,0,0,.15);
		background-color:#fff;
	}
	.bzhu:hover .bzhu_box,
	.bzhu:focus .bzhu_box,
	.bzhu:active .bzhu_box,
	.bzhu.active .bzhu_box{
		display: block;
	}




</style>
</head>

<body class="dashboard-page">

<!-- Start: Theme Preview Pane --> 

<!-- End: Theme Preview Pane --> 

<!-- Start: Main -->
<div id="main"> 
	  <c:if test="${param.status!='detail'}">
	<!-- Start: Header -->
	<div class="navbar navbar-fixed-top navbar-shadow ">
		<div class="navbar-branding dark bg-hui">
       <span class="caret caret-tp"></span>
      </div>
        <ul class="nav navbar-nav navbar-left">
         <c:if test="${param.pageStatus!='detail'||(param.pageStatus==''||param.pageStatus==null)}">
			<li>
				<div class="navbar-btn btn-group"> <a href="javascript:saveRight();"> <span class="edui-save btn btn-sm" title="提交"></span> </a> </div>
			</li>
			<li>
				<div class="navbar-btn btn-group"> <a href="JavaScript:dir();" > <span class="edui-for-Catalog btn btn-sm" title="目录"></span> </a> </div>
			</li>
			
			 <li>
				<div class="navbar-btn btn-group"> <a href="javascript:void(0);" > <select  id="fontSizes" onchange="fontSizes();">
                    <option value="6">5</option><option value="7">5.5</option><option value="8">6.5</option><option value="10">7.5</option>
                    <option value="12">9</option><option value="14">10.5</option><option value="16">12</option><option value="18">14</option>
                    <option value="20">15</option><option value="21">16</option><option value="24">18</option><option value="29">22</option>
                    <option value="32">24</option><option value="34">26</option><option value="48">36</option><option value="56">42</option>
    </select> </a> </div>
			</li>
			<li>
				<div class="navbar-btn btn-group"> <a href="javascript:void(0);" > <select  id="fontFamilys" onchange="fontFamilys();">
                    <option value="宋体">宋体</option><option value="黑体">黑体</option><option value="微软雅黑">微软雅黑</option><option value="微软正黑体">微软正黑体</option>
                    <option value="新宋体">新宋体</option><option value="新细明体">新细明体</option><option value="细明体">细明体</option><option value="标楷体">标楷体</option>
                    <option value="仿宋">仿宋</option><option value="楷体">楷体</option>
                    
                    
    </select> </a> </div>
			</li>
			<li>
				<div class="navbar-btn btn-group"> <input type="color" id="tsColor" > </div>
			</li>

			<li>
				<div class="navbar-btn btn-group"> <a href="javascript:void(0);" onclick="window.rightFrame.sup();"> <span class="edui-for-superscript btn btn-sm" title="上标"></span> </a> </div>
			</li>

			<li>
				<div class="navbar-btn btn-group"> <a href="javascript:void(0);" onclick="window.rightFrame.sub();"> <span class="edui-for-subscript btn btn-sm" title="下标"></span> </a> </div>
			</li>

			<li>
				<div class="navbar-btn btn-group"> <a href="javascript:void(0);"  onclick="fontBold();"  > <span class=" btn btn-sm" title="加粗"></span> </a> </div>
			</li>

			<li>
				<div class="navbar-btn btn-group"> <a href="javascript:void(0);" onclick="fontItalic();"> <span class="edui-for-italic btn btn-sm" title="倾斜"></span> </a> </div>
			</li>

			<li>
				<div class="navbar-btn btn-group"> <a href="#" > <span class="edui-for-print btn btn-sm" title="打印"></span> </a> </div>
			</li>

			<li>
				<div class="navbar-btn btn-group"> <a href="JavaScript:sf();" > <span class="edui-for-spechars btn btn-sm" title="特殊字符"></span> </a> </div>
			</li>
			


			<li class="menu-divider hidden-xs"> <i class="fa fa-circle"></i> </li>
			
			<li>
				<div class="navbar-btn btn-group"> 
					<a href="JavaScript:copyToRow();" > 
						<span class="edui-for-Copyrow btn btn-sm" title="复制内容到整行"></span> 
					</a> 
				</div>
			</li>
			<li>
				<div class="navbar-btn btn-group"> 
					<a href="JavaScript:sequence();" > 
						<span class="edui-for-insertorderedlist btn btn-sm" title="生成序号"></span> 
					</a> 
				</div>
			</li>
			</c:if>
			 <c:if test="${param.pageStatus=='detail'}">
				<li>
					<div class="navbar-btn btn-group"> <a href="javascript:void(0);" onclick="markColor();"> <span class="edui_fontcolor btn btn-sm" title="上色"></span> </a> </div>
				</li>
				<li>
					<div class="navbar-btn btn-group"> 
						<a href="JavaScript:drawMarker();" > 
							<span class="edui-for-Tagging btn btn-sm" title="标注"></span> 
						</a> 
					</div>
				</li>
			</c:if>
		</ul>
	</div>
	<!-- End: Header --> 
</c:if>	

	
	<!-- Start: Sidebar -->
	<div id="sidebar_left" class="nano nano-light affix sidebar-light"> 
		
		<!-- Start: Sidebar Left Content -->
		<div class="sidebar-left-content nano-content" id="treeH"> 
			
			<!-- Start: Sidebar Header -->
			<div class="sidebar-header"> 
				
				<div class="sidebar-widget author-widget">
					 <ul id="tree1"></ul>
				</div>

				
				
				
				
			</div>
			<!-- End: Sidebar Header --> 
			
		</div>
		<!-- End: Sidebar Left Content --> 
		
	</div>
	<!-- End: Sidebar Left --> 
	
	<!-- Start: Content-Wrapper -->
	<div id="content_wrapper">
		<div  class="cont_center">
<!-- 		<iframe id="rightFrame" frameborder="0" name="rightFrame" width="100%" height="100%" scrolling="no" src="test.html" /></iframe> -->
		<iframe marginwidth="0" id="rightFrame" name="rightFrame" marginheight="0" frameborder="0" valign=top src="" name="rtree"
	                width=1024 height=900 scrolling="no" allowTransparency></iframe>
		</div>


	</div>
	<!-- End: Content-Wrapper --> 
	
</div>
<!-- End: Main --> 

<!-- BEGIN: PAGE SCRIPTS --> 

<!-- jQuery --> 
<!-- <script src="vendor/jquery/jquery-1.11.1.min.js"></script>  -->
<!-- <script src="vendor/jquery/jquery_ui/jquery-ui.min.js"></script>  -->

<!-- Theme Javascript --> 
<script src="rtbox/app/templates/default/assets/js/demo/demo.js"></script> 
<script src="rtbox/app/templates/default/assets/js/main.js"></script> 

<!-- END: PAGE SCRIPTS -->

</body>
<script type="text/javascript">

	function sf(){
		try{
			var fsEl = window.rightFrame.focus_element;
			share.data("focus_element",fsEl);
			top.$.dialog({
	    		width:500,
	    		height:420,
	    		lock:false,
	    		title:"特殊字符",
	    		content:"url:rtbox/app/templates/default/font/fonts.jsp",
	    		data : {
					"window" : window
				}
	    	});
		}catch(err){
			console.log(err.description);
		}
		
	}
</script> 
</html>
