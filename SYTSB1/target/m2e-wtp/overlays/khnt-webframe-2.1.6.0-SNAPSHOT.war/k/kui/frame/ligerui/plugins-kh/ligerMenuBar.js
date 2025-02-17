﻿/**
 * jQuery ligerUI 1.3.2 ligerMenuBar
 *
 * http://ligerui.com
 *
 * Author daomi 2015 [ gd_star@163.com ]
 *
 */
(function($) {
	$.fn.ligerMenuBar = function(options) {
		return $.ligerui.run.call(this, "ligerMenuBar", arguments);
	};
	$.fn.ligerGetMenuBarManager = function() {
		return $.ligerui.run.call(this, "ligerGetMenuBarManager", arguments);
	};

	$.ligerDefaults.MenuBar = {};

	$.ligerMethos.MenuBar = {};

	$.ligerui.controls.MenuBar = function(element, options) {
		$.ligerui.controls.MenuBar.base.constructor.call(this, element, options);
	};
	$.ligerui.controls.MenuBar.ligerExtend($.ligerui.core.UIComponent, {
		__getType: function() {
			return 'MenuBar';
		},
		__idPrev: function() {
			return 'MenuBar';
		},
		_extendMethods: function() {
			return $.ligerMethos.MenuBar;
		},
		_render: function() {
			var g = this, p = this.options;
			g.menubar = $(this.element);
			if (!g.menubar.hasClass("l-menubar")) g.menubar.addClass("l-menubar");
			if (p && p.items) {
				$(p.items).each(function(i, item) {
					g.addItem(item);
				});
			}
			$(document).click(function() {
				$(".l-panel-btn-selected", g.menubar).removeClass("l-panel-btn-selected");
			});
			g.set(p);
		},
		addItem: function(item) {
			var g = this, p = this.options;
			var ditem = $('<div class="l-menubar-item l-panel-btn"><span></span><div class="l-panel-btn-l"></div><div class="l-panel-btn-r"></div><div class="l-menubar-item-down"></div></div>');
			g.menubar.append(ditem);
			item.id && ditem.attr("menubarid", item.id);
			item.text && $("span:first", ditem).html(item.text);
			//todo 添加 item.icon item.img 2016年11月30日 15:49:28 星期三 lybide
			item.icon && ditem.prepend('<div class="l-menu-item-icon iconfont l-icon-' + item.icon + '"></div>').addClass("l-toolbar-item-hasicon");
			item.img && ditem.prepend('<div class="l-menu-item-icon" style="background:url(' + item.img + ') no-repeat center center;"></div>').addClass("l-toolbar-item-hasicon");
			item.disable && ditem.addClass("l-menubar-item-disable");
			item.click && ditem.click(function() {
				item.click(item);
			});
			if (item.menu) {
				var menu = $.ligerMenu(item.menu);
				ditem.hover(function() {
					g.actionMenu && g.actionMenu.hide();
					var left = $(this).offset().left;
					var top = $(this).offset().top + $(this).height();
					menu.show({top: top, left: left});
					g.actionMenu = menu;
					$(this).addClass("l-panel-btn-over l-panel-btn-selected").siblings(".l-menubar-item").removeClass("l-panel-btn-selected");
				}, function() {
					$(this).removeClass("l-panel-btn-over");
				});
			}
			else {
				ditem.hover(function() {
					$(this).addClass("l-panel-btn-over");
				}, function() {
					$(this).removeClass("l-panel-btn-over");
				});
				$(".l-menubar-item-down", ditem).remove();
			}

		},
		//todo add by lybide 2015/3/16 11:35 lybide
		_setEnabled: function(itemId, enable) {
			var g = this, p = this.options;

			var ditem = $("body > .l-menu div[menuitemid=" + itemId + "]");
			if (enable === true) { //启用
				ditem.attr("disabled", false);
				ditem.removeClass("l-menu-item-disable");
			} else { //禁用
				ditem.attr("disabled", true);
				ditem.addClass("l-menu-item-disable");
			}
			return false;
		},
		setEnabled: function(items) {
			var g = this;
			$.each(items, function(i, item) {
				g._setEnabled(i, item);
			});
		}
	});

})(jQuery);