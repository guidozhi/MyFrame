package com.lsts.relation.web;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.khnt.core.crud.web.SpringSupportAction;
import com.khnt.core.crud.web.support.JsonWrapper;
import com.khnt.utils.StringUtil;
import com.lsts.inspection.bean.ReportRecordParse;
import com.lsts.inspection.service.ReportRecordParseService;
import com.lsts.relation.bean.RelationInfoDTO;

/**
 * 报告与原始记录对应关系管理控制器
 * 
 * @ClassName RelationInfoAction
 * @JDK 1.8
 * @author GaoYa
 * @date 2018-06-20 下午05:08:00
 */
@Controller
@RequestMapping("relation/info")
public class RelationInfoAction extends
		SpringSupportAction<ReportRecordParse, ReportRecordParseService> {
	@Autowired
	private ReportRecordParseService reportRecordParseManager;
	

	// 保存对应关系信息
	@RequestMapping(value = "saveBasic")
	@ResponseBody
	public HashMap<String, Object> saveBasic(HttpServletRequest request,
			@RequestBody RelationInfoDTO relationInfoDTO) throws Exception {
		HashMap<String, Object> wrapper = new HashMap<String, Object>();
		try {
			reportRecordParseManager.saveBasic(
					relationInfoDTO, request);
			wrapper.put("success", true);
		} catch (Exception e) {
			log.debug(e.toString());
			wrapper.put("success", false);
			wrapper.put("msg", "保存失败！");
			e.printStackTrace();
		}
		return wrapper;
	}

	// 根据报告id获取报告和原始记录对应关系信息
	@RequestMapping(value = "getRelations")
	@ResponseBody
	public HashMap<String, Object> getRelations(HttpServletRequest request) throws Exception {
		String report_id = request.getParameter("report_id");
		try {
			return reportRecordParseManager.getRelations(report_id);
		} catch (Exception e) {
			log.debug(e.toString());
			return JsonWrapper.failureWrapperMsg("读取对应关系信息失败！");
		}
	}
	
	// 删除对应关系信息
	@RequestMapping(value = "del")
	@ResponseBody
	public HashMap<String, Object> del(HttpServletRequest request, String id) throws Exception {
		reportRecordParseManager.del(request, id);
		return JsonWrapper.successWrapper(id);
	}
		
	// 根据对应关系记录ids获取对应关系记录
	@RequestMapping(value = "getRelationsByIds")
	@ResponseBody
	public HashMap<String, Object> getRelationsByIds(HttpServletRequest request) throws Exception {
		String ids = request.getParameter("ids");
		try {
			return reportRecordParseManager.getRelationsByIds(ids);
		} catch (Exception e) {
			log.debug(e.toString());
			return JsonWrapper.failureWrapperMsg("读取对应关系信息失败！");
		}
	}

	// 批量修改对应关系信息
	@RequestMapping(value = "saveBasic2")
	@ResponseBody
	public HashMap<String, Object> saveBasic2(HttpServletRequest request, @RequestBody RelationInfoDTO relationInfoDTO)
			throws Exception {
		HashMap<String, Object> wrapper = new HashMap<String, Object>();
		try {
			reportRecordParseManager.saveBasic2(relationInfoDTO, request);
			wrapper.put("success", true);
		} catch (Exception e) {
			log.debug(e.toString());
			wrapper.put("success", false);
			wrapper.put("msg", "保存失败！");
			e.printStackTrace();
		}
		return wrapper;
	}
	// 查漏
	@RequestMapping(value = "checkItems")
	@ResponseBody
	public HashMap<String, Object> checkItems(String report_id, String itemNames) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			String return_items = "";
			String[] items = itemNames.split(",");
			for (int i = 0; i < items.length; i++) {
				List<ReportRecordParse> parse = reportRecordParseManager.getRelationInfos(report_id, items[i]);
				if(parse == null) {
					if(StringUtil.isNotEmpty(return_items)) {
						return_items += ",";
					}
					return_items += items[i];
				}
			}
			map.put("success", true);
			map.put("return_items", return_items);
		} catch (Exception e) {
			log.debug(e.toString());
			map.put("success", false);
			map.put("msg", "查漏请求超时，请稍后再试！");
		}
		return map;
	}
}
