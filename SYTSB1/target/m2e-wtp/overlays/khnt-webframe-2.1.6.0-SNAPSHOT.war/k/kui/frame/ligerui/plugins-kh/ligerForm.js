﻿/**
 * jQuery ligerUI 1.1.9 ligerForm
 *
 * http://ligerui.com
 *
 * Author daomi 2012 [ gd_star@163.com ]
 *
 */
(function($) {
	$.fn.ligerForm = function() {
		return $.ligerui.run.call(this, "ligerForm", arguments);
	};

	$.ligerDefaults = $.ligerDefaults || {};
	$.ligerDefaults.Form = {
		//控件宽度
		inputWidth: "",
		//todo lybide 200px - 100%
		//标签宽度
		labelWidth: 90,
		//间隔宽度
		space: 40,
		rightToken: '：',
		//标签对齐方式
		labelAlign: 'left',
		//控件对齐方式
		align: 'left',
		//字段
		fields: [],
		//创建的表单元素是否附加ID
		appendID: true,
		//生成表单元素ID的前缀
		prefixID: "",
		//json解析函数
		toJSON: $.ligerui.toJSON
	};

	//@description 默认表单编辑器构造器扩展(如果创建的表单效果不满意 建议重载)
	//@param {jinput} 表单元素jQuery对象 比如input、select、textarea
	$.ligerDefaults.Form.editorBulider = function(jinput) {
		//这里this就是form的ligerui对象
		var g = this,
			p = this.options;
		//todo add by ttaomeng
		var attroptions = {};
		if (jinput.attr("validate")) {
			try {
				attroptions = $.trim(jinput.attr("validate"));
				if (attroptions.indexOf('{') < 0) {
					attroptions = "{" + attroptions + "}";
				}
				eval("attroptions = " + attroptions + ";");
				//if (attroptions.required && (jinput.is("select") || jinput.is(":text") || jinput.is(":password") || jinput.is("textarea"))) {
				//todo 2013-6-21 下午4:03 lybide 删除
				if (attroptions.required && (!jinput.is(":hidden"))) {
					//jinput.addClass("requiredstar");
					//for (key in attroptions) {jinput.attr(key,attroptions[key]);};
				}
				if (attroptions) { //todo 2013-5-25 下午2:18 lybide
					jinput.attr(attroptions);
					jinput.removeAttr("validate");
				}
				if (attroptions.required && (!jinput.is(":hidden"))) {
					ligerElementRequired(jinput, jinput.val()); //todo 2013-6-21 下午4:03 lybide
				}
				/*if (attroptions.required) {alert(jinput.parent().parent().html());
				 jinput.parent().append('<div class="l-required"><div class="l-required-div">*</div></div>');
				 }*/
			} catch (e) {}
		}
		var inputOptions = {};
		if (p.inputWidth) inputOptions.width = p.inputWidth;
		//todo 不渲染input框 2017年01月11日 10:04:41 星期三 lybide
		var ltype = jinput.attr("ltype");
		if (ltype=="no-apply") {
			return;
		}
		if (jinput.is("select")) {
			var ltype = jinput.attr("ltype");
			if (ltype) { //todo 2013-9-10 下午4:17 lybide
				jinput.ligerComboBox(inputOptions);
			}
		} else if (jinput.is(":text") || jinput.is(":password")) {
			var ltype = jinput.attr("ltype");
			switch (ltype) {
			case "select":
			case "combobox":
				//todo modified by ttaomeng
				//inputOptions.width="100%";
				jinput.ligerComboBox(inputOptions);
				break;
			case "spinner":
				jinput.ligerSpinner(inputOptions);
				break;
			case "date":
				jinput.ligerDateEditor(inputOptions);
				break;
			case "float":
			case "number":
				inputOptions.number = true;
				//inputOptions.width="100%";
				jinput.ligerTextBox(inputOptions);
				break;
			case "int":
			case "digits":
				inputOptions.digits = true;
			default:
				//inputOptions.width="100%";
				jinput.ligerTextBox(inputOptions);
				break;
			}
		}
		//todo modified by ttaomeng
		else if (jinput.is(":radio")) {
			var ltype = jinput.attr("ltype");
			if (ltype == "radioGroup") {
				var obj = jinput.ligerRadioGroup(inputOptions);
				if (attroptions.required) obj.wrapper.addClass("requiredstar");
			} else jinput.ligerRadio(inputOptions);
		} else if (jinput.is(":checkbox")) {
			var ltype = jinput.attr("ltype");
			if (ltype == "checkboxGroup") {
				var obj = jinput.ligerCheckBoxGroup(inputOptions);
				if (attroptions.required) obj.wrapper.addClass("requiredstar");
			} else jinput.ligerCheckBox(inputOptions);
		} else if (jinput.is("textarea")) {
			//jinput.addClass("l-textarea");
			jinput.ligerTextBox(inputOptions); //todo 封装textarea lybide 13-1-16 下午3:28 lybide
			//if(jinput.attr("readonly")) {jinput.addClass("l-text-disabled");}
		}
	}

	//表单组件
	$.ligerui.controls.Form = function(element, options) {
		$.ligerui.controls.Form.base.constructor.call(this, element, options);
	};

	$.ligerui.controls.Form.ligerExtend($.ligerui.core.UIComponent, {
		__getType: function() {
			return 'Form'
		},
		__idPrev: function() {
			return 'Form';
		},
		_init: function() {
			$.ligerui.controls.Form.base._init.call(this);
		},
		_render: function() {
			var g = this,
				p = this.options;
			var jform = $(this.element);
			//自动创建表单
			if (p.fields && p.fields.length) {
				if (!jform.hasClass("l-form")) jform.addClass("l-form");
				var out = [];
				var appendULStartTag = false;
				$(p.fields).each(function(index, field) {
					var name = field.name || field.id;
					if (!name) return;
					if (field.type == "hidden") {
						out.push('<input type="hidden" id="' + name + '" name="' + name + '" />');
						return;
					}
					var newLine = field.renderToNewLine || field.newline;
					if (newLine == null) newLine = true;
					if (field.merge) newLine = false;
					if (field.group) newLine = true;
					if (newLine) {
						if (appendULStartTag) {
							out.push('</ul>');
							appendULStartTag = false;
						}
						if (field.group) {
							out.push('<div class="l-group');
							if (field.groupicon) out.push(' l-group-hasicon');
							out.push('">');
							if (field.groupicon) out.push('<img src="' + field.groupicon + '" />');
							out.push('<span>' + field.group + '</span></div>');
						}
						out.push('<ul>');
						appendULStartTag = true;
					}
					//append label
					out.push(g._buliderLabelContainer(field));
					//append input
					out.push(g._buliderControlContainer(field));
					//append space
					out.push(g._buliderSpaceContainer(field));
				});
				if (appendULStartTag) {
					out.push('</ul>');
					appendULStartTag = false;
				}
				jform.append(out.join(''));
			}
			//生成ligerui表单样式
			$("input,select,textarea", jform).each(function() {
				p.editorBulider.call(g, $(this));
			});
		},
		//标签部分
		_buliderLabelContainer: function(field) {
			var g = this,
				p = this.options;
			var label = field.label || field.display;
			var labelWidth = field.labelWidth || field.labelwidth || p.labelWidth;
			var labelAlign = field.labelAlign || p.labelAlign;
			if (label) label += p.rightToken;
			var out = [];
			out.push('<li style="');
			if (labelWidth) {
				out.push('width:' + labelWidth + 'px;');
			}
			if (labelAlign) {
				out.push('text-align:' + labelAlign + ';');
			}
			out.push('">');
			if (label) {
				out.push(label);
			}
			out.push('</li>');
			return out.join('');
		},
		//控件部分
		_buliderControlContainer: function(field) {
			var g = this,
				p = this.options;
			a
			var width = field.width || p.inputWidth;
			var align = field.align || field.textAlign || field.textalign || p.align;
			var out = [];
			out.push('<li style="');
			if (width) {
				out.push('width:' + width + 'px;');
			}
			if (align) {
				out.push('text-align:' + align + ';');
			}
			out.push('">');
			out.push(g._buliderControl(field));
			out.push('</li>');
			return out.join('');
		},
		//间隔部分
		_buliderSpaceContainer: function(field) {
			var g = this,
				p = this.options;
			var spaceWidth = field.space || field.spaceWidth || p.space;
			var out = [];
			out.push('<li style="');
			if (spaceWidth) {
				out.push('width:' + spaceWidth + 'px;');
			}
			out.push('">');
			out.push('</li>');
			return out.join('');
		},
		_buliderControl: function(field) {
			var g = this,
				p = this.options;
			var width = field.width || p.inputWidth;
			var name = field.name || field.id;
			var out = [];
			if (field.comboboxName && field.type == "select") {
				out.push('<input type="hidden" id="' + p.prefixID + name + '" name="' + name + '" />');
			}
			if (field.textarea || field.type == "textarea") {
				out.push('<textarea ');
			} else if (field.type == "checkbox") {
				out.push('<input type="checkbox" ');
			} else if (field.type == "radio") {
				out.push('<input type="radio" ');
			} else if (field.type == "password") {
				out.push('<input type="password" ');
			} else {
				out.push('<input type="text" ');
			}
			if (field.cssClass) {
				out.push('class="' + field.cssClass + '" ');
			}
			if (field.type) {
				out.push('ltype="' + field.type + '" ');
			}
			if (field.attr) {
				for (var attrp in field.attr) {
					out.push(attrp + '="' + field.attr[attrp] + '" ');
				}
			}
			if (field.comboboxName && field.type == "select") {
				out.push('name="' + field.comboboxName + '"');
				if (p.appendID) {
					out.push(' id="' + p.prefixID + field.comboboxName + '" ');
				}
			} else {
				out.push('name="' + name + '"');
				if (p.appendID) {
					out.push(' id="' + name + '" ');
				}
			}
			//参数
			var fieldOptions = $.extend({
				width: width - 2
			}, field.options || {});
			out.push(" ligerui='" + p.toJSON(fieldOptions) + "' ");
			//验证参数
			if (field.validate) {
				out.push(" validate='" + p.toJSON(field.validate) + "' ");
			}
			out.push(' />');
			return out.join('');
		}
	});
})(jQuery);