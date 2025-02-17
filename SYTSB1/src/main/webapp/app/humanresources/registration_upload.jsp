<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
<head>
</head>
<body>
<form name="form_upload" id="form_upload" method="post"
				action="uploadAction/saveUpload.do">
				<input type="hidden" name="id" id="id" />
				 <input type="hidden" name="fkEmployeeId" id="fkEmployeeId"  />
				 <input type="hidden" name="uploadName" id="uploadName" />
				 <input type="hidden" name="uploadPath" id="uploadPath" />
				 <input type="hidden" name="uploadId" id="uploadId" />
				<table cellpadding="3" cellspacing="0" class="l-detail-table">
			             <tr>
			                 <td class="l-t-td-left">选择上传的类型</td>
			                 <c:if test='${param.permission!="apply" }'>
			                    <td class="l-t-td-right" colspan="2"><input type="radio" name="uploadType"  id="uploadType" ltype="radioGroup"
					ligerui="{value:'附件',data: [ { text:'专业技能证书', id:'专业技能证书' }, { text:'学位证', id:'学位证' }, { text:'身份证', id:'身份证' }, { text:'毕业证', id:'毕业证' }, { text:'其他', id:'其他' }] }"/></td>
		                 <!--  <td class="l-t-td-right"><u:combo  name="uploadType"  code="TJY2_RL_UPLOAD_TYPE_PER" validate="required:true"></u:combo></td> -->
			             </c:if>
			                <c:if test='${param.permission=="apply" }'>
		                   <td class="l-t-td-right"><input type="radio" name="uploadType"  id="uploadType" ltype="radioGroup" validate="required:true"
					ligerui="{value:'附件',data: [ { text:'学位证', id:'学位证' }, { text:'身份证', id:'身份证' }, { text:'毕业证', id:'毕业证' }, { text:'其他', id:'其他' }] }"/></td>
			             </c:if>
						      <td class="l-t-td-right" >
						      <span id="onefileDIV2">
								<a class="l-button" id="onefileBtn2"><span
									class="l-button-main"><span class="l-button-text">选择文件</span>
								</span>
								</a>
                  </span></td>
                  </tr>	
                  <tr >
                  <td class="l-t-td-left"></td>
                     <td class="l-t-td-right" colspan="2" height="230px">
                     <a id="procufiles">
                       <img id="image_upload" style="width:150px;height: 200px ;padding-left: 150px"></img>
                     </a>
                     </td>
                  
                  </tr>
                  
				</table>
			</form>
			<script type="text/javascript">
			    if($("#uploadPath").val()!=null&&$("#uploadPath").val()!=""){
				   alert($("#uploadPath").val())
			   } 
			  
			
				$(function() {
					$("#form_upload")
							.initFormList(
									{
										root : 'list',
										getAction : "uploadAction/detailUpload.do?empId=${param.id}",
										//getActionParam:{},	//取数据时附带的参数，一般只在需要动态取特定值时用到
										actionParam : {
										}, //保存时会在当前表单上附加此数据，如：{fkId:$("#form1>#id")}会把Form1下的id为id的值带上去,可以是一个对象或选择器
										delAction : 'uploadAction/deleteUpload.do', //删除数据的action
										delActionParam : {
											id:'id',
											uploadId:'uploadId',
											uploadPath:'uploadPath'
											
										}, //默认为选择行的id 
										columns : [ {
											display : 'empId',
											name : 'fkEmployeeId',
											width : '1%',
											hide : true
										}, {
											display : 'id',
											name : 'id',
											width : '10%',
											hide : true 
										},
										{
											display : 'uploadId',
											name : 'uploadId',
											width : '10%',
											hide : true 
										},
										{
											display : 'uploadPath',
											name : 'uploadPath',
											width : '10%',
											hide : true 
										},{
											display : '上传类型',
											name : 'uploadType',
											width : '30%'
										},  {
											display : '文件名称',
											name : 'uploadName',
											width : '40%'
										}],
										  onSelectRow : function(rowdata, rowindex) {
											  $("#image_upload").attr("src","fileupload/downloadByFilePath.do?path="+rowdata.uploadPath);
											  uploadId=rowdata.uploadId;
											  //$("#procufiles").attr("src","app/archives/preview.jsp?id="+rowdata.uploadId);
											  $("#procufiles").attr("onclick","view()");
											  $("#form_upload").setValues(rowdata);
											  
												
											},
											 success: function (data, stats) {
									                if (data["success"]) {
									                		top.$.notice("保存成功！");
									             
									                        
									                } else {
									                	$.ligerDialog.error('请先保存个人信息！');
									                }
									            }
									});
				});
			
			</script>
</body>
</html>