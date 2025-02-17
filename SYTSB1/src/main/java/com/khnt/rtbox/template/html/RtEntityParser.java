package com.khnt.rtbox.template.html;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.json.JSONObject;
import org.springframework.web.context.ContextLoader;

import com.khnt.rtbox.config.bean.RtPage;
import com.khnt.rtbox.config.service.RtUI;
import com.khnt.rtbox.template.model.RtEntity;
import com.khnt.rtbox.template.model.enums.InputType;
import com.khnt.rtbox.tools.HtmlUtils;
import com.khnt.utils.StringUtil;

/**
 * @author ZQ
 * @version 2016年3月16日 下午10:11:56 类说明
 */
public class RtEntityParser {
	private RtEntity entity;
	public List<String> jsFuns = new ArrayList<String>();
	public List<String> hiddens = new ArrayList<String>();
	private String suffix;

	public RtEntityParser() {
	}

	public RtEntityParser(RtEntity rtEntity) {
		this.entity = rtEntity;
	}

	public RtEntityParser(String _rtEntity) throws Exception {
		if (_rtEntity == null || _rtEntity.trim().length() <= 0) {
			throw new Exception("RtEntityParser: rtEntity is null..");
		}
		// _rtEntity = HtmlUtils.trimHtmlTxt(_rtEntity, true);
		if (_rtEntity.contains("$")) {
			_rtEntity = _rtEntity.replace("$", "");
		}
		
		if(_rtEntity.contains("checkbox")){
			_rtEntity = _rtEntity.replace("/","\\");
		}
		
		JSONObject json = new JSONObject(_rtEntity);
		this.entity = new RtEntity();
		this.entity.setId(json.getString("id"));
		this.entity.setName(json.getString("name"));
		if (this.entity.getName() == null) {
			this.entity.setName(this.entity.getId());
		}
		this.entity.setType(json.getString("type"));
		if(json.has("bind")) {
			this.entity.setBind(json.getString("bind"));
		}
		if(json.has("js")) {
			this.entity.setJs(json.getString("js"));
		}
		if(json.has("hidden")) {
			if ("true".equalsIgnoreCase((json.getString("hidden"))) || "true".equalsIgnoreCase((json.getString("isHidden")))) {
				this.entity.setIsHidden(true);
			} else {
				this.entity.setIsHidden(false);
			}
		}
		if (json.has("nullable")&&"true".equalsIgnoreCase((json.getString("nullable")))) {
			this.entity.setNullable(true);
		} else {
			this.entity.setNullable(false);
		}
		if(json.has("nullable")) {
			this.entity.setSql(json.getString("nullable"));
		}
		
		if(json.has("code")) {
			this.entity.setCode(json.getString("code"));
			if ("true".equalsIgnoreCase((json.getString("tree")))) {
				this.entity.setTree(true);
			} else {
				this.entity.setTree(false);
			}
		}
		
		if(json.has("default")) {
			this.entity.setDefaultValue(json.getString("default"));
		}
		
		if (this.entity.getDefaultValue() == null&&json.has("defaultValue")) {
			this.entity.setDefaultValue(json.getString("defaultValue"));
		}
		if(json.has("attr")) {
			String _attr = json.get("attr").toString();
			if (_attr != null) {
				JSONObject attrJson = new JSONObject(_attr);
				LinkedHashMap<String, String> attr = new LinkedHashMap<String, String>();
				this.entity.setAttr(attr);
				@SuppressWarnings("rawtypes")
				Iterator it = attrJson.keys();
				while (it.hasNext()) {
					String key = (String) it.next();
					String value = attrJson.getString(key);
					attr.put(key, value);
				}
			}
		}
		if(json.has("ligerui")) {
			String _ligerui = json.get("ligerui").toString();
			if (_ligerui != null) {
				_ligerui = HtmlUtils.decode(_ligerui);
				JSONObject ligeruiJson = new JSONObject(_ligerui);
				LinkedHashMap<String, String> ligerui = new LinkedHashMap<String, String>();
				this.entity.setLigerui(ligerui);
				@SuppressWarnings("rawtypes")
				Iterator it = ligeruiJson.keys();
				while (it.hasNext()) {
					String key = (String) it.next();
					String value = ligeruiJson.getString(key);
					ligerui.put(key, value);
				}
			}
		}
		
	}

	/**
	 * 根据rtEntity 生成input 依赖 jquery validate/jquery LigerUI
	 * @param rtPage 
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public String createInput(RtPage rtPage) throws Exception {

		String id = entity.getId();// id
		String name = entity.getName();// name
		String type = entity.getType();// 显示类型
		String bind = entity.getBind();// jquery bind方法
		String js = entity.getJs();// 绑定方法名
		Boolean isHidden = entity.getIsHidden()==null?false:entity.getIsHidden();// 是否只读
		Boolean nullable = true;// 能否为空
		if (entity.getNullable() != null) {
			nullable = entity.getNullable();
		}

		String defaultValue = entity.getDefaultValue();
		LinkedHashMap<String, String> attr = entity.getAttr();// INPUT属性
		LinkedHashMap<String, String> ligerui = entity.getLigerui();// ligerui属性
		if (defaultValue == null) {
			defaultValue = "";
		}
		StringBuilder input = new StringBuilder();
		if (isHidden || "hidden".equalsIgnoreCase(type)) {
			input.append("<input ");
			input.append(" id=\"" + id + "\"");
			input.append(" name=\"" + name + "\"");
			input.append(" type=\"hidden\"");
			input.append(" value=\"" + defaultValue + "\"");
			input.append(" />");
			this.hiddens.add(input.toString());
			return "";
		} else if ("text".equalsIgnoreCase(type)) {
			input.append("<input ");
			input.append(" id=\"" + id + "\"");
			input.append(" name=\"" + name + "\"");
			input.append(" type=\"text\"");
			input.append(" ltype=\"text\"");
			input.append(" value=\"" + defaultValue + "\"");
			attribute(input, attr);
			ligerui(input, entity);
			if(id.contains("report_sn")){
				//报告编号不能改
				input.append(" readonly=\"readonly\"");
			}
			if (nullable) {
				input.append(" required");
			}
			input.append(" />");
		} else if ("textarea".equalsIgnoreCase(type)) {
			input.append("<textarea ");
			input.append(" id=\"" + id + "\"");
			input.append(" name=\"" + name + "\"");
			attribute(input, attr);
			ligerui(input, entity);

			input.append(" class=\"l-textarea\"");
			if (!nullable) {
				input.append(" required");
			}
			input.append(" >" + defaultValue + "");
			input.append(" </textarea>");
		} else if ("spinner".equalsIgnoreCase(type)) {
			input.append("<input ");
			input.append(" id=\"" + id + "\"");
			input.append(" name=\"" + name + "\"");
			input.append(" type=\"text\"");
			input.append(" ltype=\"spinner\"");
			input.append(" value=\"" + defaultValue + "\"");
			attribute(input, attr);
			ligerui(input, entity);

			if (!nullable) {
				input.append(" required");
			}
			input.append(" />");
		} else if ("date".equals(type)) {
			input.append("<input ");
			input.append(" id=\"" + id + "\"");
			input.append(" name=\"" + name + "\"");
			input.append(" type=\"text\"");
			input.append(" ltype=\"date\"");
			input.append(" value=\"" + defaultValue + "\"");
			attribute(input, attr);

			// controlWidth(120);//控制日期大小不能小于120

			ligerui(input, entity);

			if (!nullable) {
				input.append(" required");
			}
			input.append(" />");
		} else if ("select".equalsIgnoreCase(type)) {
			input.append("<input ");
			input.append(" id=\"" + id + "\"");
			input.append(" name=\"" + name + "\"");
			input.append(" type=\"text\"");
			input.append(" ltype=\"select\"");
			input.append(" value=\"" + defaultValue + "\"");
			attribute(input, attr);
			ligerui(input, entity);
			// 下拉框即可选有课输

			if (nullable) {
				input.append(" required");
			}
			if(id.contains("record__")){
				//检验项目及其内容的检验结果增加联动功能
				if("record___".equals(id)) {
					System.out.println(id);
				}
				if(id.length()>"record__".length()
						&&StringUtil.isNotEmpty(id.substring("record__".length(), id.length()))) {
					String[] nums = id.substring("record__".length(), id.length()).split("_");
					if(nums.length>0) {
						String num = nums[0];
						if(nums.length>2) {
							num = nums[0]+"_"+nums[1]+"_"+nums[2];
						}else if(nums.length>1) {
							num = nums[0]+"_"+nums[1];
						}
						
						input.append(" class=\"c"+num+"\"");
						input.append(" onchange=\"changeRecord(this,this.value)\"");
					}
					
				}
				
			}
			
			input.append(" />");
		} else if ("radio".equalsIgnoreCase(type)) {
			input.append("<input ");
			input.append(" id=\"" + id + "\"");
			input.append(" name=\"" + name + "\"");
			input.append(" type=\"radio\"");
			input.append(" ltype=\"radioGroup\"");
			input.append(" value=\"" + defaultValue + "\"");
			attribute(input, attr);
			ligerui(input, entity);

			if (nullable) {
				input.append(" required");
			}
			input.append(" />");
		}else if ("file".equalsIgnoreCase(type)) {
			//pingZhou2017/03/17
		      String width = "600pt";
		      String height = "400pt;";
		      	//图片宽
		      if(this.entity.getAttr().containsKey("width")){
		    	  width = Double.parseDouble(this.entity.getAttr().get("width"))*72/1.95+"pt";
		      }
		      	//图片高
		      if(this.entity.getAttr().containsKey("height")){
		    	  height = Double.parseDouble(this.entity.getAttr().get("height"))*72/2.54+"pt";
		      }
		      
			input.append("<div id='"+id+"' name='"+id+"' class='uploadPhoto' style='width: "+width+";height: "+height+";' align='center'></div> ");
			
		} else if (InputType.checkbox.toString().equalsIgnoreCase(type)) {
			if(rtPage!=null&&"1".equals(rtPage.getModelType())) {
				//原始记录
				// checkbox处理 pingZhou modify 20180621
				input.append("<div ");
				input.append(" id=\"" + id + "\"");
				input.append(" name=\"" + name + "\"");
				input.append(" type=\"text\"");
				input.append(" ltype=\"checkBox\"");
				input.append(" value=\"" + defaultValue + "\"");
				
				//input.append("<div ");

				input.append("class=\"checkboxDiv\" ");
				attribute(input, attr);
				ligerui(input, entity);

				input.append("> </div>");
				
				
			}else {
				//默认检验报告
				// checkbox处理 zq add 20170826
				input.append("<div ");

				input.append(" id=\"" + id + "\"");
				attribute(input, attr);
				input.append("> </div>");
				String data = entity.getLigerui().get("data");
				this.jsFuns.add("$('#" + id + "').ligerCheckBoxList({data:" + data + ",name:'" + id + "'});\n");
			}
			
		}else if ("checkItem".equals(type)) {
			if(rtPage!=null&&"1".equals(rtPage.getModelType())) {
				//原始记录
				// checkbox处理 pingZhou modify 20180621
				//input.append("<div ");
				input.append("<div ");
				input.append(" id=\"" + id + "\"");
				input.append(" name=\"" + name + "\"");
				input.append(" type=\"text\"");
				input.append(" ltype=\"checkBox\"");
				input.append(" value=\"" + defaultValue + "\"");
				
				input.append("class=\"checkboxDiv\" ");
				attribute(input, attr);
				String data ="[{'id':'1','text':''}]";
				input.append(" ligeru=\"{data:" + data + "}\" ");
				
				
				
				input.append("> </div>");
				
			}else {
				// checkItem处理pingZhou 20171020
				input.append("<div ");
	
				input.append(" id=\"" + id + "\"");
				attribute(input, attr);
				input.append("> </div>");
				String data ="[{'id':'1','text':''}]";
				this.jsFuns.add("$('#" + id + "').ligerCheckBoxList({data:" + data + ",name:'" + id + "'});\n");
			}
		}else if ("file".equalsIgnoreCase(type)) {
			// pingZhou2017/03/17
			String width = "600pt";
			String height = "400pt;";
			// 图片宽
			if (this.entity.getAttr().containsKey("width")) {
				width = Double.parseDouble(this.entity.getAttr().get("width")) * 72 / 2.75 + "pt";
			}
			// 图片高
			if (this.entity.getAttr().containsKey("height")) {
				height = Double.parseDouble(this.entity.getAttr().get("height")) * 72 / 2.54 + "pt";
			}

			input.append("<div id='" + id + "' name='" + id + "' class='uploadPhoto' style='width: " + width + ";height: " + height + ";' align='center'></div> ");

		} 
		else if ("print".equalsIgnoreCase(type)) {
			// pingZhou2017/04/28
			// 处理打印的内容，但是不能修改的内容
			/*
			 * input.append("<input "); input.append(" id=\"" + id + "\"");
			 * input.append(" name=\"" + name + "\"");
			 * input.append(" type=\"text\"");
			 * input.append(" value=\"\" readonly"); input.append(" />");
			 */

			if (id.contains("inspect_op")||id.contains("inspection_op") || id.contains("audit_op")) {
				input.append("<input ");
				input.append(" id=\"" + id + "\"");
				input.append(" name=\"" + name + "\"");
				input.append(" type=\"text\"");
				input.append(" ltype=\"select\"");
				input.append(" value=\"" + defaultValue + "\"");
				attribute(input, attr);
				ligerui(input, entity);
				ligerui.put("isTextBoxMode", "true");
				// ligerui.put("data", opUI.buildData("code", code, tree));

				if (nullable) {
					input.append(" required");
				}
				input.append(" />");
			}else if (id.contains("enter_op") || id.contains("sign_op")) {
				input.append("<input ");
				input.append(" id=\"" + id+ "_name\"");
				input.append(" name=\"" + name + "_name\"");
				input.append(" type=\"text\"");
				input.append(" ltype=\"text\"");
				input.append(" value=\"" + defaultValue + "\"");
				input.append(" readonly=\"readonly\"");
				attribute(input, attr);
				ligerui(input, entity);
				input.append(" />");
			} else{
				input.append("<input ");
				input.append(" id=\"" + id + "\"");
				input.append(" name=\"" + name + "\"");
				input.append(" type=\"text\"");
				input.append(" ltype=\"text\"");
				input.append(" value=\"" + defaultValue + "\"");
				attribute(input, attr);
				ligerui(input, entity);
				input.append(" readonly=\"readonly\"");
				
				input.append(" />");
				//input.append("<span id='" + id + "' name='" + id + "' ></span> ");
			}

		} else {
			throw new Exception("undefined input type..." + type);
		}

		// 是否有后缀名
		String suffix = ligerui == null ? null : ligerui.get("suffix");
		if (suffix != null) {
			suffix = HtmlUtils.decode(suffix);
			this.setSuffix(suffix);
			StringBuilder swap = new StringBuilder();
			String suffixWidth = ligerui.get("suffix");
			if (suffixWidth != null) {
				swap.append("<table border='0' class='l-text-suffix-wrap'><tr><td class='l-text-left'>").append(input).append("</td><td class='l-text-suffix'>").append(suffix)
						.append("</td></tr></table>");
			} else {
				swap.append("<table border='0' class='l-text-suffix-wrap'><tr><td class='l-text-left'>").append(input)
						.append("</td><td class='l-text-suffix' 'width:" + suffixWidth + "px'>").append(suffix).append("</td></tr></table>");
			}
			input = swap;
		}
		// bind JS
		if (js != null) {
			if (bind == null) {
				bind = "click";
			}
			String _js = "if($(\"#" + id + "\"))\n$(\"#" + id + "\").bind(\"" + bind + "\",function(){" + js + "(this);});\n";
			this.jsFuns.add(_js);
		}

		return input.toString();
	}

	public void controlWidth(int minWidth) {
		LinkedHashMap<String, String> ligeruiMap = entity.getLigerui();
		if (ligeruiMap != null) {
			String width = ligeruiMap.get("width");
			if (width != null && width.length() > 0) {
				try {
					int _width = Integer.parseInt(width);
					if (_width < minWidth) {
						ligeruiMap.put("width", "120");
					}
				} catch (Exception e) {
				}
			}
		}

	}

	public void attribute(StringBuilder input, LinkedHashMap<String, String> attr) {
		if (attr != null) {
			for (String key : attr.keySet()) {
				input.append(" " + key + "=\"" + attr.get(key) + "\"");
			}
		}
	}

	/**
	 * 处理ligerui里面的内容
	 * 
	 * @param input
	 * @param entity
	 * @throws Exception
	 */
	public void ligerui(StringBuilder input, RtEntity entity) throws Exception {
		LinkedHashMap<String, String> ligerui = entity.getLigerui();
		if (ligerui != null) {
			// 解析sql,tree
			String sql = entity.getSql();
			String code = entity.getCode();// 码表名称
			boolean tree = entity.getTree()==null?false:entity.getTree()==null;
			if (sql != null || code != null) {
				RtUI opUI = (RtUI) ContextLoader.getCurrentWebApplicationContext().getBean("rtUI");
				if (opUI != null) {
					String data = null;
					String _tree = ligerui.get("tree");
					if (_tree != null) {
						tree = true;
					}
					if (sql != null) {
						// SQL优先
						data = opUI.buildData("sql", sql, tree);
					} else if (code != null) {
						data = opUI.buildData("code", code, tree);
					}
					if (tree) {
						if (!_tree.contains("{")) {
							_tree = "{" + tree + "}";
						}
						JSONObject treeJson = new JSONObject(_tree);
						treeJson.put("data", data);
						ligerui.put("tree", treeJson.toString());
					} else {
						ligerui.put("data", opUI.buildData("code", code, tree));
					}
				}
			}

			else {
				String data = ligerui.get("data");
				if (data != null && data.contains("\"")) {
					data = data.replace("\"", "'");
					ligerui.put("data", data);
				}
			}

			// input.append(" ligerui='").append(Utils.toJsonString(ligerui)).append("'");
			input.append(" ligerui=\"").append(mapToJson(ligerui)).append("\"");
		}
	}

	private String mapToJson(LinkedHashMap<String, String> map) {
		StringBuilder ligerui = new StringBuilder("{");
		if (map != null) {
			int i = 0;
			for (String key : map.keySet()) {
				if (i != 0) {
					ligerui.append(",");
				}
				ligerui.append(key).append(":");
				if (isStringParam(key)) {
					ligerui.append("'" + map.get(key) + "'");
				} else {
					ligerui.append(map.get(key));
				}

				i++;
			}
		}
		ligerui.append("}");
		String lg = ligerui.toString();
		lg = lg.replace("+", " ");
		return lg;
	}

	private boolean isStringParam(String param) {
		boolean flag = false;
		if (param.equalsIgnoreCase("initValue")) {
			flag = true;
		} else if (param.equalsIgnoreCase("initText")) {
			flag = true;
		} else if (param.equalsIgnoreCase("valueField")) {
			flag = true;
		} else if (param.equalsIgnoreCase("textField")) {
			flag = true;
		} else if (param.equalsIgnoreCase("valueFieldID")) {
			flag = true;
		} else if (param.equalsIgnoreCase("split")) {
			flag = true;
		} else if (param.equalsIgnoreCase("url")) {
			flag = true;
		} else if (param.equalsIgnoreCase("css")) {
			flag = true;
		} else if (param.equalsIgnoreCase("ajaxType")) {
			flag = true;
		} else if (param.equalsIgnoreCase("valueFieldCssClass")) {
			flag = true;
		} else if (param.equalsIgnoreCase("emptyText")) {
			flag = true;
		} else if (param.equalsIgnoreCase("addRowButton")) {
			flag = true;
		} else if (param.equalsIgnoreCase("triggerIcon")) {
			flag = true;
		} else if (param.equalsIgnoreCase("dataParmName")) {
			flag = true;
		} else if (param.equalsIgnoreCase("detailUrl")) {
			flag = true;
		} else if (param.equalsIgnoreCase("detailDataParmName")) {
			flag = true;
		} else if (param.equalsIgnoreCase("detailParms")) {
			flag = true;
		} else if (param.equalsIgnoreCase("rowClsRender")) {
			flag = true;
		} else if (param.equalsIgnoreCase("format")) {
			flag = true;
		} else if (param.equalsIgnoreCase("suffix")) {
			flag = true;
		}
		return flag;
	}

	public RtEntity getEntity() {
		return entity;
	}

	public void setEntity(RtEntity entity) {
		this.entity = entity;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public List<String> getJsFuns() {
		return jsFuns;
	}

	public void setJsFuns(List<String> jsFuns) {
		this.jsFuns = jsFuns;
	}

	public static void main(String[] args) {
		String test = "${id:'123',ligerui:{id:'123',tree:{id:'232'}}}";
		RtEntityParser entityParser = null;
		try {
			entityParser = new RtEntityParser(test);
			System.out.println(entityParser.getEntity().getId());
			System.out.println(entityParser.getEntity().getLigerui().get("tree"));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
