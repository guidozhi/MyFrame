/*
 * jQuery Autocomplete plugin 1.1
 *
 * Copyright (c) 2009 Jörn Zaefferer
 *
 * Dual licensed under the MIT and GPL licenses:
 *   http://www.opensource.org/licenses/mit-license.php
 *   http://www.gnu.org/licenses/gpl.html
 *
 * Revision: $Id: jquery.autocomplete.js 15 2009-08-22 10:30:27Z joern.zaefferer $

KHFrame
新增输入自动提示功能，可本地数据、服务器数据；可配置每多列数据；
需更新svn：99开发框架 k/*.*

1、本地数据
 

2、服务器数据，调用方法： xxx.jsp，程序会自动加参数：xxx.jsp?q=按键字符
 

本地数据代码示例：
<input name="name5" type="text" ligerui="{autocomplete:{data:['a中国','a美国','a英国','b成都','b房产'],option:{matchContains:true}}}">

远程数据代码示例：
<input name="name4" type="text" ligerui="{autocomplete:{url:'app/demo/zz_autocomplete_date.jsp',option:{setValueColumn:{'name1':'data1','name2':'data2','name3':'data3'},displayColumn:[{name:'data1',width:130},{name:'data2',width:80},{name:'data3'}]}}}"/>

远程jsp代码示例：
{
"success":true,
"data":[
{"data0":"Tachybaptus1","data1":"Tachybaptus1","data2":"ruficollis1","data3":"Podiceps1"},
{"data0":"Tachybaptus2","data1":"Tachybaptus2","data2":"ruficollis2","data3":"Podiceps2"},
{"data0":"Tachybaptus3","data1":"Tachybaptus3","data2":"ruficollis3","data3":"Podiceps3"},
{"data0":"Tachybaptus4","data1":"Tachybaptus4","data2":"ruficollis4","data3":"Podiceps4"},
{"data0":"Tachybaptus5","data1":"Tachybaptus5","data2":"ruficollis5","data3":"Podiceps5"},
{"data0":"Tachybaptus6","data1":"Tachybaptus6","data2":"ruficollis6","data3":"Podiceps6"},
{"data0":"Tachybaptus7","data1":"Tachybaptus7","data2":"ruficollis7","data3":"Podiceps7"}
]
}

input框接口说明 ：
ligerui="{autocomplete:{data:cities,option:{matchContains:true},result:function(event, data, formatted){alert(data+'=='+formatted)}}}"

data : 本地数据，数据行式：['a中国','a美国','a英国','b成都','b房产']
url : 为远程ajax地址
option : 参数键字对
result:function(event, data, formatted){alert('选择结果：'+data.to+'\n'+formatted);}选择完成后，自定义回调函数

option 参数键字对说明
minChars: 1,//在触发autoComplete前用户至少需要输入的字符数.Default: 1，如果设为0，在输入框内双击或者删除输入框内内容时显示列表
delay: 0,//击键后激活autoComplete的延迟时间(单位毫秒).Default: 远程为400 本地10
matchCase: false,//比较是否开启大小写敏感开关.使用缓存时比较重要.如果你理解上一个选项,这个也就不难理解,就好比foot要不要到FOO的缓存中去找.Default: false
matchSubset: true,//可不可以使用对服务器查询的缓存,如果缓存对foo的查询结果,那么如果用户输入foo就不需要再进行检索了,直接使用缓存.通常是打开这个选项以减轻服务器的负担以提高性能.只会在缓存长度大于1时有效.Default: true
matchContains: false,//决定比较时是否要在字符串内部查看匹配,如ba是否与foo bar中的ba匹配.使用缓存时比较重要.不要和autofill混用.Default: false
cacheLength: 10,//缓存的长度.即对从数据库中取到的结果集要缓存多少条记录.设成1为不缓存.Default: 10
max: 100,//下拉显示项目的个数.Default: 10
mustMatch: false,//如果设置为true,autoComplete只会允许匹配的结果出现在输入框,所有当用户输入的是非法字符时将会得不到下拉框.Default: false
extraParams: {},//为后台(一般是服务端的脚本)提供更多的参数.和通常的作法一样是使用一个键值对对象.如果传过去的值是{ bar:4 },将会被autocompleter解析成my_autocomplete_backend.php?q=foo&bar=4(假设当前用户输入了foo). Default: {}
selectFirst: true,//如果设置成true,在用户键入tab或return键时autoComplete下拉列表的第一个值将被自动选择,尽管它没被手工选中(用键盘或鼠标).当然如果用户选中某个项目,那么就用用户选中的值. Default: true
formatItem: function(row, i, max) { return row[0]; },//为每个要显示的项目使用高级标签.即对结果中的每一行都会调用这个函数,返回值将用LI元素包含显示在下拉列表中. Autocompleter会提供三个参数(row, i, max): 返回的结果数组, 当前处理的行数(即第几个项目,是从1开始的自然数), 当前结果数组元素的个数即项目的个数. Default:none, 表示不指定自定义的处理函数,这样下拉列表中的每一行只包含一个值.
formatResult:function(row, i, max) {return row.value;}和formatItem类似,但可以将将要输入到input文本框内的值进行格式化.同样有三个参数,和formatItem一样.Default: none,表示要么是只有数据,要么是使用formatItem提供的值.
formatMatch: null,//对每一行数据使用此函数格式化需要查询的数据格式. 返回值是给内部搜索算法使用的. 参数值row
autoFill: false,//要不要在用户选择时自动将用户当前鼠标所在的值填入到input框. Default: false
width: 0,//指定下拉框的宽度. Default: input元素的宽度
multiple: false,//是否允许输入多个值即多次使用autoComplete以输入多个值. Default: false
multipleSeparator: ", ",//如果是多选时,用来分开各个选择的字符. Default: ","
highlight: function(value, term) {
return value.replace(new RegExp("(?![^&;]+;)(?!<[^<>]*)(" + term.replace(/([\^\$\(\)\[\]\{\}\*\.\+\?\|\\])/gi, "\\$1") + ")(?![^<>]*>)(?![^&;]+;)", "gi"), "<strong>$1</strong>");
},
scroll: true,//当结果集大于默认高度时是否使用卷轴显示Default:true
scrollHeight: 180//自动完成提示的卷轴高度用像素大小表示Default:180


 */

;(function($) {

$.fn.extend({
	autocomplete: function(urlOrData, options) {
		var isUrl = typeof urlOrData == "string";
		if (!isUrl & typeof urlOrData[0]=="string") {//一般的字符串，进行处理，变成json
			var bbb=[];
			$.each(urlOrData,function(i,item){
				bbb.push({value:item});
			});
			urlOrData=bbb;
		}
		options = $.extend({}, $.Autocompleter.defaults, {
			url: isUrl ? urlOrData : null,
			data: isUrl ? null : urlOrData,
			delay: isUrl ? $.Autocompleter.defaults.delay : 400,
			max: options && !options.scroll ? 10 : 150,
			//todo 13-2-28 上午11:23 lybide move 13-3-1 上午11:09 lybide
			displayColumn:[{name:'data1',width:130},{name:'data2',width:130}],
			//setValueColumn:{"inputName1":"data1","inputName2":"data2","inputName3":"data3"},
			setValueColumn:null,
			dataType:"json",
			valueKey:null,//选择后，返回给input框的json对象名。如："data3"
			formatItem:function(row, i, max){//格式化每一个下拉数据
				var str1="";
				var rowData = row.value;
				if (rowData) {
					str1 += row.value;
				} else {
					//alert(JSON.stringify(options.displayColumn));;
					var dc = options.displayColumn;
					var isWidth=dc.length==1;
					for (var i = 0; i < dc.length; i++) {
						var w1=dc[i]["width"]+"";
						str1 += '<div' + (isWidth?"":(' style="width:'+(w1.toString().indexOf("%") || parseFloat(w1)!==NaN?w1+"px":w1)+';"')) + '>' + row[dc[i]["name"]] + '</div>';
					}
				}
				return str1;
			},
			formatMatch: function(row, i, max) {//匹配字段
				return " " + row.value;
			},
			formatResult: function(row) {//返回结果
				return row.value;
			},
			parse:function(data){
				var parsed = [];
				//var rows = data.split("\n");
				//var str1="var d="+data+"";eval(str1);
				rows= data.data;//alert(rows.length)
				//2013-5-31 下午10:58 lybide 值为空或长度为0时
				if (!rows || rows.length==0) {
					return parsed;
				}
				var one=options.valueKey;
				for (var i in rows[0]) {
					if(one)break;
					else
					one=i;
				}
				for (var i=0; i < rows.length; i++) {
					var row = rows[i];
					//if (row) {
						//var str1="var rs="+row+"";//alert(str1);
						//row = eval(str1);
						parsed.push({value:row[one],data:row,result:options.formatResult && options.formatResult(row, row[one]) || row[one]});
						/*parsed[parsed.length] = {
							data: row,
							value: row[0],
							result: options.formatResult && options.formatResult(row, row[0]) || row[0]
						};*/
					//}
				}
				//alert(liger.toJSON(parsed));
				return parsed;
			}
		}, options);

		// if highlight is set to false, replace it with a do-nothing function
		options.highlight = options.highlight || function(value) { return value; };

		// if the formatMatch option is not specified, then use formatItem for backwards compatibility
		options.formatMatch = options.formatMatch || options.formatItem;
		
		// 指定字符出现时，才发送请求
		options.trigerChar = options.trigerChar || "";
		
		return this.each(function() {
			new $.Autocompleter(this, options);
		});
	},
	result: function(handler) {
		return this.bind("result", handler);
	},
	search: function(handler) {
		return this.trigger("search", [handler]);
	},
	flushCache: function() {
		return this.trigger("flushCache");
	},
	setOptions: function(options){
		return this.trigger("setOptions", [options]);
	},
	unautocomplete: function() {
		return this.trigger("unautocomplete");
	}
});

$.Autocompleter = function(input, options) {

	var KEY = {
		UP: 38,
		DOWN: 40,
		DEL: 46,
		TAB: 9,
		RETURN: 13,
		ESC: 27,
		COMMA: 188,
		PAGEUP: 33,
		PAGEDOWN: 34,
		BACKSPACE: 8
	};

	// Create $ object for input element
	var $input = $(input).attr("autocomplete", "off").addClass(options.inputClass);

	var timeout;
	var previousValue = "";
	var cache = $.Autocompleter.Cache(options);
	var hasFocus = 0;
	var lastKeyPressCode;
	var config = {
		mouseDownOnSelect: false
	};
	var select = $.Autocompleter.Select(options, input, selectCurrent, config);

	var blockSubmit;

	// prevent form submit in opera when selecting with return key
	$.browser.opera && $(input.form).bind("submit.autocomplete", function() {
		if (blockSubmit) {
			blockSubmit = false;
			return false;
		}
	});

	// only opera doesn't trigger keydown multiple times while pressed, others don't work with keypress at all
	$input.bind(($.browser.opera ? "keypress" : "keyup") + ".autocomplete", function(event) {
		// a keypress means the input has focus
		// avoids issue where input had focus before the autocomplete was applied
		hasFocus = 1;
		// track last key pressed
		lastKeyPressCode = event.keyCode;
		switch(event.keyCode) {

			case KEY.UP:
				event.preventDefault();
				if ( select.visible() ) {
					select.prev();
				} else {
					onChange(0, true);
				}
				break;

			case KEY.DOWN:
				event.preventDefault();
				if ( select.visible() ) {
					select.next();
				} else {
					onChange(0, true);
				}
				break;

			case KEY.PAGEUP:
				event.preventDefault();
				if ( select.visible() ) {
					select.pageUp();
				} else {
					onChange(0, true);
				}
				break;

			case KEY.PAGEDOWN:
				event.preventDefault();
				if ( select.visible() ) {
					select.pageDown();
				} else {
					onChange(0, true);
				}
				break;

			// matches also semicolon
			case options.multiple && $.trim(options.multipleSeparator) == "," && KEY.COMMA:
			case KEY.TAB:
			case KEY.RETURN:
				if( selectCurrent() ) {
					// stop default to prevent a form submit, Opera needs special handling
					event.preventDefault();
					blockSubmit = true;
					return false;
				}
				break;

			case KEY.ESC:
				select.hide();
				break;

			default:
				clearTimeout(timeout);
				timeout = setTimeout(onChange, options.delay);
				break;
		}
	}).focus(function(){
		// track whether the field has focus, we shouldn't process any
		// results if the field no longer has focus
		hasFocus++;
	}).blur(function() {
		hasFocus = 0;
		if (!config.mouseDownOnSelect) {
			hideResults();
		}
	}).click(function() {
		// show select when clicking in a focused field
		if ( hasFocus++ > 1 && !select.visible() ) {
			onChange(0, true);
		}
	}).bind("search", function() {
		// TODO why not just specifying both arguments?
		var fn = (arguments.length > 1) ? arguments[1] : null;
		function findValueCallback(q, data) {
			var result;
			if( data && data.length ) {
				for (var i=0; i < data.length; i++) {
					if( data[i].result.toLowerCase() == q.toLowerCase() ) {
						result = data[i];
						break;
					}
				}
			}
			if( typeof fn == "function" ) fn(result);
			else $input.trigger("result", result && [result.data, result.value]);
		}
		$.each(trimWords($input.val()), function(i, value) {
			request(value, findValueCallback, findValueCallback);
		});
	}).bind("flushCache", function() {
		cache.flush();
	}).bind("setOptions", function() {
		$.extend(options, arguments[1]);
		// if we've updated the data, repopulate
		if ( "data" in arguments[1] )
			cache.populate();
	}).bind("unautocomplete", function() {
		select.unbind();
		$input.unbind();
		$(input.form).unbind(".autocomplete");
	});


	function selectCurrent() {
		var selected = select.selected();
		if( !selected )
			return false;

		var v = selected.result;
		previousValue = v;

		if ( options.multiple ) {
			var words = trimWords($input.val());
			if ( words.length > 1 ) {
				var seperator = options.multipleSeparator.length;
				var cursorAt = $(input).selection().start;
				var wordAt, progress = 0;
				$.each(words, function(i, word) {
					progress += word.length;
					if (cursorAt <= progress) {
						wordAt = i;
						return false;
					}
					progress += seperator;
				});
				words[wordAt] = v;
				// TODO this should set the cursor to the right position, but it gets overriden somewhere
				//$.Autocompleter.Selection(input, progress + seperator, progress + seperator);
				v = words.join( options.multipleSeparator );
			}
			v += options.multipleSeparator;
		}

		if (options.setValueColumn) {//todo 选择完成后，给其它输入框赋值 13-3-4 上午11:05 lybide
			//alert(JSON.stringify(options.setValueColumn));
			var data={};
			var svc=options.setValueColumn;
			for(var j in svc){
				data[j]=eval("selected.data."+svc[j]);
				//alert(j+":input框名字："+j+"====值为："+selected.data[svc[j]]);
			}
			//alert(JSON.stringify(data));
			/*for (d in data) {
				$("[name='"+d+"']").setValue(data[d]);
				$.ligerui.get(d).setValue(data[d])
			};*/
			$input.parents("form").setValues(data,null,true);
			//$("#form1").setValues(data);
		}

		$input.val(v);
		hideResultsNow();
		$input.trigger("result", [selected.data, selected.value]);
		return true;
	}

	function onChange(crap, skipPrevCheck) {
		if( lastKeyPressCode == KEY.DEL ) {
			select.hide();
			return;
		}

		var currentValue = $input.val();

		if ( !skipPrevCheck && currentValue == previousValue )
			return;

		previousValue = currentValue;

		currentValue = lastWord(currentValue);
		if (typeof currentValue=="undefined") {return;}//todo 13-3-1 下午4:26 lybide

		//2014-03-26 下午17:50 邹洪平
		var doRequest = true;
		// 检测是否出现指定字符才发送请求
		if(options.trigerChar != ""){
			var lastChar = currentValue.charAt(currentValue.length-1);
			if(lastChar!=options.trigerChar)doRequest=false;
		}
		//---end
		
		//长度足够、并且符合条件
		if ( currentValue.length >= options.minChars && doRequest) {
			$input.addClass(options.loadingClass);
			if (!options.matchCase)
				currentValue = currentValue.toLowerCase();
			request(currentValue, receiveData, hideResultsNow);
		} else {
			stopLoading();
			select.hide();
		}
	};

	function trimWords(value) {
		if (!value)
			return [""];
		if (!options.multiple)
			return [$.trim(value)];
		return $.map(value.split(options.multipleSeparator), function(word) {
			return $.trim(value).length ? $.trim(word) : null;
		});
	}

	function lastWord(value) {
		if ( !options.multiple )
			return value;
		var words = trimWords(value);
		if (words.length == 1)
			return words[0];
		var cursorAt = $(input).selection().start;
		if (cursorAt == value.length) {
			words = trimWords(value)
		} else {
			words = trimWords(value.replace(value.substring(cursorAt), ""));
		}
		return words[words.length - 1];
	}

	// fills in the input box w/the first match (assumed to be the best match)
	// q: the term entered
	// sValue: the first matching result
	function autoFill(q, sValue){
		// autofill in the complete box w/the first match as long as the user hasn't entered in more data
		// if the last user key pressed was backspace, don't autofill
		if( options.autoFill && (lastWord($input.val()).toLowerCase() == q.toLowerCase()) && lastKeyPressCode != KEY.BACKSPACE ) {
			// fill in the value (keep the case the user has typed)
			$input.val($input.val() + sValue.substring(lastWord(previousValue).length));
			// select the portion of the value not typed by the user (so the next character will erase)
			$(input).selection(previousValue.length, previousValue.length + sValue.length);
		}
	};

	function hideResults() {
		clearTimeout(timeout);
		timeout = setTimeout(hideResultsNow, 200);
	};

	function hideResultsNow() {
		var wasVisible = select.visible();
		select.hide();
		clearTimeout(timeout);
		stopLoading();
		if (options.mustMatch) {
			// call search and run callback
			$input.search(
				function (result){
					// if no value found, clear the input box
					if( !result ) {
						if (options.multiple) {
							var words = trimWords($input.val()).slice(0, -1);
							$input.val( words.join(options.multipleSeparator) + (words.length ? options.multipleSeparator : "") );
						}
						else {
							$input.val( "" );
							$input.trigger("result", null);
						}
					}
				}
			);
		}
	};

	function receiveData(q, data) {
		if ( data && data.length && hasFocus ) {
			stopLoading();
			select.display(data, q);
			autoFill(q, data[0].value);
			select.show();
		} else {
			hideResultsNow();
		}
	};

	function request(term, success, failure) {
		if (!options.matchCase)
			term = term.toLowerCase();
		var data = cache.load(term);
		// recieve the cached data
		if (data && data.length) {
			success(term, data);
		// if an AJAX url has been supplied, try loading the data now
		} else if( (typeof options.url == "string") && (options.url.length > 0) ){

			var extraParams = {
				timestamp: +new Date()
			};
			$.each(options.extraParams, function(key, param) {
				extraParams[key] = typeof param == "function" ? param() : (typeof param=="object"? $.ligerui.toJSON(param):param);
			});

			$.ajax({
				// try to leverage ajaxQueue plugin to abort previous requests
				mode: "abort",
				// limit abortion to this input
				port: "autocomplete" + input.name,
				dataType: options.dataType,
				url: options.url,
				data: $.extend({
					q: lastWord(term),
					limit: options.max
				}, extraParams),
				success: function(data) {
					var parsed = options.parse && options.parse(data) || parse(data);
					cache.add(term, parsed);
					success(term, parsed);
				}
			});
		} else {
			// if we have a failure, we need to empty the list -- this prevents the the [TAB] key from selecting the last successful match
			select.emptyList();
			failure(term);
		}
	};

	function parse(data) {
		var parsed = [];
		var rows = data.split("\n");
		for (var i=0; i < rows.length; i++) {
			var row = $.trim(rows[i]);
			if (row) {
				row = row.split("|");
				parsed[parsed.length] = {
					data: row,
					value: row[0],
					result: options.formatResult && options.formatResult(row, row[0]) || row[0]
				};
			}
		}
		return parsed;
	};

	function stopLoading() {
		$input.removeClass(options.loadingClass);
	};

};

$.Autocompleter.defaults = {
	inputClass: "ac_input",
	resultsClass: "ac_results",
	loadingClass: "ac_loading",
	minChars: 1,
	delay: 400,
	matchCase: false,
	matchSubset: true,
	matchContains: false,//是否每个字符都匹配，默认false
	cacheLength: 10,
	max: 100,//下拉显示最大行数
	mustMatch: false,
	extraParams: {},
	selectFirst: true,
	formatItem: function(row) { return row[0]; },
	formatMatch: null,
	autoFill: false,
	width: 0,
	multiple: false,
	multipleSeparator: ", ",
	highlight: function(value, term) {
		return value.replace(new RegExp("(?![^&;]+;)(?!<[^<>]*)(" + term.replace(/([\^\$\(\)\[\]\{\}\*\.\+\?\|\\])/gi, "\\$1") + ")(?![^<>]*>)(?![^&;]+;)", "gi"), "<strong>$1</strong>");
	},
    scroll: true,
    scrollHeight: 180
};

$.Autocompleter.Cache = function(options) {

	var data = {};
	var length = 0;

	function matchSubset(s, sub) {
		if (!options.matchCase)
			s = s.toLowerCase();
		var i = s.indexOf(sub);
		if (options.matchContains == "word"){
			i = s.toLowerCase().search("\\b" + sub.toLowerCase());
		}
		if (i == -1) return false;
		return i == 0 || options.matchContains;
	};

	function add(q, value) {
		if (length > options.cacheLength){
			flush();
		}
		if (!data[q]){
			length++;
		}
		data[q] = value;
	}

	function populate(){
		if( !options.data ) return false;
		// track the matches
		var stMatchSets = {},
			nullData = 0;

		// no url was specified, we need to adjust the cache length to make sure it fits the local data store
		if( !options.url ) options.cacheLength = 1;

		// track all options for minChars = 0
		stMatchSets[""] = [];

		// loop through the array and create a lookup structure
		for ( var i = 0, ol = options.data.length; i < ol; i++ ) {
			var rawValue = options.data[i];
			// if rawValue is a string, make an array otherwise just reference the array
			rawValue = (typeof rawValue == "string") ? [rawValue] : rawValue;

			var value = options.formatMatch(rawValue, i+1, options.data.length);
			if ( value === false )
				continue;

			var firstChar = value.charAt(0).toLowerCase();
			// if no lookup array for this character exists, look it up now
			if( !stMatchSets[firstChar] )
				stMatchSets[firstChar] = [];

			// if the match is a string
			var row = {
				value: value,
				data: rawValue,
				result: options.formatResult && options.formatResult(rawValue) || value
			};

			// push the current match into the set list
			stMatchSets[firstChar].push(row);

			// keep track of minChars zero items
			if ( nullData++ < options.max ) {
				stMatchSets[""].push(row);
			}
		};

		// add the data items to the cache
		$.each(stMatchSets, function(i, value) {
			// increase the cache size
			options.cacheLength++;
			// add to the cache
			add(i, value);
		});
	}

	// populate any existing data
	setTimeout(populate, 25);

	function flush(){
		data = {};
		length = 0;
	}

	return {
		flush: flush,
		add: add,
		populate: populate,
		load: function(q) {
			if (!options.cacheLength || !length)
				return null;
			/*
			 * if dealing w/local data and matchContains than we must make sure
			 * to loop through all the data collections looking for matches
			 */
			if( !options.url && options.matchContains ){
				// track all matches
				var csub = [];
				// loop through all the data grids for matches
				for( var k in data ){
					// don't search through the stMatchSets[""] (minChars: 0) cache
					// this prevents duplicates
					if( k.length > 0 ){
						var c = data[k];
						$.each(c, function(i, x) {
							// if we've got a match, add it to the array
							if (matchSubset(x.value, q)) {
								csub.push(x);
							}
						});
					}
				}
				return csub;
			} else
			// if the exact item exists, use it
			if (data[q]){
				return data[q];
			} else
			if (options.matchSubset) {
				for (var i = q.length - 1; i >= options.minChars; i--) {
					var c = data[q.substr(0, i)];
					if (c) {
						var csub = [];
						$.each(c, function(i, x) {
							if (matchSubset(x.value, q)) {
								csub[csub.length] = x;
							}
						});
						return csub;
					}
				}
			}
			return null;
		}
	};
};

$.Autocompleter.Select = function (options, input, select, config) {
	var CLASSES = {
		ACTIVE: "ac_over"
	};

	var listItems,
		active = -1,
		data,
		term = "",
		needsInit = true,
		element,
		list;

	// Create results
	function init() {
		if (!needsInit)
			return;
		element = $("<div/>")
		.hide()
		.addClass(options.resultsClass)
		.css("position", "absolute")
		.appendTo(document.body);

		list = $("<ul/>").appendTo(element).mouseover( function(event) {
			if(target(event).nodeName && target(event).nodeName.toUpperCase() == 'LI') {
	            active = $("li", list).removeClass(CLASSES.ACTIVE).index(target(event));
			    $(target(event)).addClass(CLASSES.ACTIVE);
	        }
		}).click(function(event) {
			$(target(event)).addClass(CLASSES.ACTIVE);
			select();
			// TODO provide option to avoid setting focus again after selection? useful for cleanup-on-focus
			input.focus();
			return false;
		}).mousedown(function() {
			config.mouseDownOnSelect = true;
		}).mouseup(function() {
			config.mouseDownOnSelect = false;
		});

		if( options.width > 0 )
			element.css("width", options.width);

		needsInit = false;
	}

	function target(event) {
		var element = event.target;
		while(element && element.tagName != "LI")
			element = element.parentNode;
		// more fun with IE, sometimes event.target is empty, just ignore it then
		if(!element)
			return [];
		return element;
	}

	function moveSelect(step) {
		listItems.slice(active, active + 1).removeClass(CLASSES.ACTIVE);
		movePosition(step);
        var activeItem = listItems.slice(active, active + 1).addClass(CLASSES.ACTIVE);
        if(options.scroll) {
            var offset = 0;
            listItems.slice(0, active).each(function() {
				offset += this.offsetHeight;
			});
            if((offset + activeItem[0].offsetHeight - list.scrollTop()) > list[0].clientHeight) {
                list.scrollTop(offset + activeItem[0].offsetHeight - list.innerHeight());
            } else if(offset < list.scrollTop()) {
                list.scrollTop(offset);
            }
        }
	};

	function movePosition(step) {
		active += step;
		if (active < 0) {
			active = listItems.size() - 1;
		} else if (active >= listItems.size()) {
			active = 0;
		}
	}

	function limitNumberOfItems(available) {
		return options.max && options.max < available
			? options.max
			: available;
	}

	function fillList() {
		list.empty();
		var max = limitNumberOfItems(data.length);
		for (var i=0; i < max; i++) {
			if (!data[i])
				continue;
			var formatted = options.formatItem(data[i].data, i+1, max, data[i].value, term);
			if ( formatted === false )
				continue;
			var li = $("<li/>").html( options.highlight(formatted, term) ).addClass(i%2 == 0 ? "ac_even" : "ac_odd").appendTo(list)[0];
			$.data(li, "ac_data", data[i]);
		}
		listItems = list.find("li");
		if ( options.selectFirst ) {
			listItems.slice(0, 1).addClass(CLASSES.ACTIVE);
			active = 0;
		}
		// apply bgiframe if available
		if ( $.fn.bgiframe )
			list.bgiframe();
	}

	return {
		display: function(d, q) {
			init();
			data = d;
			term = q;
			fillList();
		},
		next: function() {
			moveSelect(1);
		},
		prev: function() {
			moveSelect(-1);
		},
		pageUp: function() {
			if (active != 0 && active - 8 < 0) {
				moveSelect( -active );
			} else {
				moveSelect(-8);
			}
		},
		pageDown: function() {
			if (active != listItems.size() - 1 && active + 8 > listItems.size()) {
				moveSelect( listItems.size() - 1 - active );
			} else {
				moveSelect(8);
			}
		},
		hide: function() {
			element && element.hide();
			listItems && listItems.removeClass(CLASSES.ACTIVE);
			active = -1;
		},
		visible : function() {
			return element && element.is(":visible");
		},
		current: function() {
			return this.visible() && (listItems.filter("." + CLASSES.ACTIVE)[0] || options.selectFirst && listItems[0]);
		},
		show: function() {
			var offset = $(input).offset();
			element.css({
				width: typeof options.width == "string" || options.width > 0 ? options.width : $(input).width(),
				top: offset.top + input.offsetHeight,
				left: offset.left
			}).show();
            if(options.scroll) {
                list.scrollTop(0);
                list.css({
					maxHeight: options.scrollHeight,
					overflow: 'auto'
				});

                if($.browser.msie && typeof document.body.style.maxHeight === "undefined") {
					var listHeight = 0;
					listItems.each(function() {
						listHeight += this.offsetHeight;
					});
					var scrollbarsVisible = listHeight > options.scrollHeight;
                    list.css('height', scrollbarsVisible ? options.scrollHeight : listHeight );
					if (!scrollbarsVisible) {
						// IE doesn't recalculate width when scrollbar disappears
						listItems.width( list.width() - parseInt(listItems.css("padding-left")) - parseInt(listItems.css("padding-right")) );
					}
                }

            }
		},
		selected: function() {
			var selected = listItems && listItems.filter("." + CLASSES.ACTIVE).removeClass(CLASSES.ACTIVE);
			return selected && selected.length && $.data(selected[0], "ac_data");
		},
		emptyList: function (){
			list && list.empty();
		},
		unbind: function() {
			element && element.remove();
		}
	};
};

$.fn.selection = function(start, end) {
	if (start !== undefined) {
		return this.each(function() {
			if( this.createTextRange ){
				var selRange = this.createTextRange();
				if (end === undefined || start == end) {
					selRange.move("character", start);
					selRange.select();
				} else {
					selRange.collapse(true);
					selRange.moveStart("character", start);
					selRange.moveEnd("character", end);
					selRange.select();
				}
			} else if( this.setSelectionRange ){
				this.setSelectionRange(start, end);
			} else if( this.selectionStart ){
				this.selectionStart = start;
				this.selectionEnd = end;
			}
		});
	}
	var field = this[0];
	if ( field.createTextRange ) {
		var range = document.selection.createRange(),
			orig = field.value,
			teststring = "<->",
			textLength = range.text.length;
		range.text = teststring;
		var caretAt = field.value.indexOf(teststring);
		field.value = orig;
		this.selection(caretAt, caretAt + textLength);
		return {
			start: caretAt,
			end: caretAt + textLength
		}
	} else if( field.selectionStart !== undefined ){
		return {
			start: field.selectionStart,
			end: field.selectionEnd
		}
	}
};

})(jQuery);