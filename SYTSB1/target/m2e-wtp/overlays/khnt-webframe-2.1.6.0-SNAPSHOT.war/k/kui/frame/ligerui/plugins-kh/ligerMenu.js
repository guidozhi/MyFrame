﻿/**
 * jQuery ligerUI 1.3.2 ligerMenu
 *
 * http://ligerui.com
 *
 * Author daomi 2015 [ gd_star@163.com ]
 *
 */
(function($) {
	$.ligerMenu = function(options) {
		return $.ligerui.run.call(null, "ligerMenu", arguments);
	};

	$.ligerDefaults.Menu = {
		width: 120,
		top: 0,
		left: 0,
		cls: null,
		items: null,
		onshowEnd: null, //todo 菜单显示完成回调函数 13-1-25 下午3:50 lybide	
		onhideEnd: null, //todo 菜单隐藏完成回调函数 13-1-25 下午3:50 lybide
		shadow: true
	};

	$.ligerMethos.Menu = {};

	$.ligerui.controls.Menu = function(options) {
		$.ligerui.controls.Menu.base.constructor.call(this, null, options);
	};
	$.ligerui.controls.Menu.ligerExtend($.ligerui.core.UIComponent, {
		__getType: function() {
			return 'Menu';
		},
		__idPrev: function() {
			return 'Menu';
		},
		_extendMethods: function() {
			return $.ligerMethos.Menu;
		},
		_render: function() {
			var g = this, p = this.options;
			g.menuItemCount = 0;
			//全部菜单
			g.menus = {};
			//顶级菜单
			g.menu = g.createMenu();
			g.element = g.menu[0];
			g.menu.css({top: p.top, left: p.left, width: p.width});
			p.cls && g.menu.addClass(p.cls);

			p.items && $(p.items).each(function(i, item) {
				g.addItem(item);
			});

			$(document).bind('click.menu', function() {
				g.hide(); //todo 隐藏菜单 13-1-25 下午4:25 lybide 添加
				for (var menuid in g.menus) {
					var menu = g.menus[menuid];
					if (!menu) return;
					menu.hide();
					if (menu.shadow) menu.shadow.hide();
				}
			});
			g.set(p);
		},
		show: function(options, menu) {
			var g = this, p = this.options;
			if (menu == undefined) menu = g.menu;
			if (options && options.left != undefined) {
				menu.css({left: options.left});
			}
			if (options && options.top != undefined) {
				menu.css({top: options.top});
			}
			menu.show();
			g.updateShadow(menu);
			p.onshowEnd && p.onshowEnd(g, p);//todo 2016年11月30日 11:51:35 星期三 lybide
		},
		updateShadow: function(menu) {
			var g = this, p = this.options;
			if (!p.shadow) return;
			menu.shadow.css({
				left: menu.css('left'),
				top: menu.css('top'),
				width: menu.outerWidth(),
				height: menu.outerHeight()
			});
			if (menu.is(":visible")) {
				menu.shadow.show();
			} else {
				menu.shadow.hide();
			}
		},
		hide: function(menu) {
			var g = this, p = this.options;
			if (menu == undefined) menu = g.menu;
			g.hideAllSubMenu(menu);
			menu.hide();
			g.updateShadow(menu);
			p.onhideEnd && p.onhideEnd(g, p);//todo 2016年11月30日 11:51:51 星期三 lybide
		},
		toggle: function() {
			var g = this, p = this.options;
			g.menu.toggle();
			g.updateShadow(g.menu);
		},
		removeItem: function(itemid) {
			var g = this, p = this.options;
			$("> .l-menu-item[menuitemid=" + itemid + "]", g.menu.items).remove();
		},
		setEnabled: function(itemid) {
			var g = this, p = this.options;
			$("> .l-menu-item[menuitemid=" + itemid + "]", g.menu.items).removeClass("l-menu-item-disable");
		},
		setMenuText: function(itemid, text) {
			var g = this, p = this.options;
			$("> .l-menu-item[menuitemid=" + itemid + "] >.l-menu-item-text:first", g.menu.items).html(text);
		},
		setDisabled: function(itemid) {
			var g = this, p = this.options;
			$("> .l-menu-item[menuitemid=" + itemid + "]", g.menu.items).addClass("l-menu-item-disable");
		},
		isEnable: function(itemid) {
			var g = this, p = this.options;
			return !$("> .l-menu-item[menuitemid=" + itemid + "]", g.menu.items).hasClass("l-menu-item-disable");
		},
		getItemCount: function() {
			var g = this, p = this.options;
			return $("> .l-menu-item", g.menu.items).length;
		},
		addItem: function(item, menu) {
			var g = this, p = this.options;
			if (!item) return;
			if (menu == undefined) menu = g.menu;

			if (item.line) {
				menu.items.append('<div class="l-menu-item-line"></div>');
				return;
			}
			var ditem = $('<div class="l-menu-item"><div class="l-menu-item-text"></div> </div>');
			var itemcount = $("> .l-menu-item", menu.items).length;
			menu.items.append(ditem);
			ditem.attr("ligeruimenutemid", ++g.menuItemCount);
			item.id && ditem.attr("menuitemid", item.id).attr("id", item.id);//todo 添加id属性 2017年06月09日 16:05:00 星期五 lybide for lasa
			item.text && $(">.l-menu-item-text:first", ditem).html(item.text);
			item.icon && ditem.prepend('<div class="l-menu-item-icon iconfont l-icon-' + item.icon + '"></div>');
			//item.img && ditem.prepend('<div class="l-menu-item-icon"><img style="width:16px;height:16px;margin:2px;" src="' + item.img + '" /></div>');
			item.img && ditem.prepend('<div class="l-menu-item-icon" style="background:url(' + item.img + ') no-repeat center center;"></div>'); //todo lybide add
			if (item.disable || item.disabled) {
				ditem.addClass("l-menu-item-disable");
			}
			if (item.children) {
				ditem.append('<div class="l-menu-item-arrow"></div>');
				var newmenu = g.createMenu(ditem.attr("ligeruimenutemid"));
				g.menus[ditem.attr("ligeruimenutemid")] = newmenu;
				newmenu.width(p.width);
				newmenu.hover(null, function() {
					if (!newmenu.showedSubMenu) {
						g.hide(newmenu);
					}
				});
				$(item.children).each(function() {
					g.addItem(this, newmenu);
				});
			}
			item.click && ditem.click(function() {
				if ($(this).hasClass("l-menu-item-disable")) return;
				item.click(item, itemcount);
			});
			item.dblclick && ditem.dblclick(function() {
				if ($(this).hasClass("l-menu-item-disable")) return;
				item.dblclick(item, itemcount);
			});

			var menuover = $("> .l-menu-over:first", menu);
			ditem.hover(function() {
				if ($(this).hasClass("l-menu-item-disable")) return;
				var itemtop = $(this).offset().top;
				var top = itemtop - menu.offset().top;
				menuover.css({top: top});
				g.hideAllSubMenu(menu);
				if (item.children) {
					var ligeruimenutemid = $(this).attr("ligeruimenutemid");
					if (!ligeruimenutemid) return;
					if (g.menus[ligeruimenutemid]) {
						g.show({top: itemtop, left: $(this).offset().left + $(this).width() - 5}, g.menus[ligeruimenutemid]);
						menu.showedSubMenu = true;
					}
				}
			}, function() {
				if ($(this).hasClass("l-menu-item-disable")) return;
				var ligeruimenutemid = $(this).attr("ligeruimenutemid");
				if (item.children) {
					var ligeruimenutemid = $(this).attr("ligeruimenutemid");
					if (!ligeruimenutemid) return;
				}
				;
			});
		},
		hideAllSubMenu: function(menu) {
			var g = this, p = this.options;
			if (menu == undefined) menu = g.menu;
			$("> .l-menu-item", menu.items).each(function() {
				if ($("> .l-menu-item-arrow", this).length > 0) {
					var ligeruimenutemid = $(this).attr("ligeruimenutemid");
					if (!ligeruimenutemid) return;
					g.menus[ligeruimenutemid] && g.hide(g.menus[ligeruimenutemid]);
				}
			});
			menu.showedSubMenu = false;
		},
		createMenu: function(parentMenuItemID) {
			var g = this, p = this.options;
			var menu = $('<div class="l-menu" style="display:none"><div class="l-menu-yline"></div><div class="l-menu-over"><div class="l-menu-over-l"></div> <div class="l-menu-over-r"></div></div><div class="l-menu-inner"></div></div>');
			parentMenuItemID && menu.attr("ligeruiparentmenuitemid", parentMenuItemID);
			menu.items = $("> .l-menu-inner:first", menu);
			menu.appendTo('body');
			if (p.shadow) {
				menu.shadow = $('<div class="l-menu-shadow"></div>').insertAfter(menu);
				g.updateShadow(menu);
			}
			menu.hover(null, function() {
				if (!menu.showedSubMenu) {
					$("> .l-menu-over:first", menu).css({top: -30});
				}
			});
			if (parentMenuItemID) {
				g.menus[parentMenuItemID] = menu;
			} else {
				g.menus[0] = menu;
			}
			return menu;
		}
	});
	//旧写法保留
	$.ligerui.controls.Menu.prototype.setEnable = $.ligerui.controls.Menu.prototype.setEnabled;
	$.ligerui.controls.Menu.prototype.setDisable = $.ligerui.controls.Menu.prototype.setDisabled;


})(jQuery);