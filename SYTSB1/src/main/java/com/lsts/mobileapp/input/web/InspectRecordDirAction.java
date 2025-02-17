package com.lsts.mobileapp.input.web;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.khnt.core.crud.web.SpringSupportAction;
import com.khnt.utils.FileUtil;
import com.lsts.mobileapp.input.bean.InspectRecordDir;
import com.lsts.mobileapp.input.bean.RecordModelDir;
import com.lsts.mobileapp.input.service.InspectRecordDirService;

import net.sf.json.JSONArray;

@Controller
@RequestMapping("inspectRecordDirAction")
public class InspectRecordDirAction extends SpringSupportAction<InspectRecordDir, InspectRecordDirService> {

	@Autowired
	InspectRecordDirService recordDirService;
	
	/**
	 * 获取模板目录
	 * author pingZhou
	 * @param rtCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping("getRecordModelDir")
	public HashMap<String, Object> getRecordModelDir(String rtCode){
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		try {
			List<RecordModelDir> list = recordDirService.getRecordModelDir(rtCode,null);
			map.put("dirList", list);
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "查询失败！");
			map.put("success", false);
		}
		
		return map;
	}
	
	/**
	 * 获取模板信息(包括目录)
	 * author pingZhou
	 * @param rtCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping("getRecordModel")
	public HashMap<String, Object> getRecordModel(String rtCode){
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		try {
			recordDirService.getRecordModel(map,rtCode);
			
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "查询失败！");
			map.put("success", false);
		}
		
		return map;
	}
	@ResponseBody
	@RequestMapping("getRecordModelByReportId")
	public HashMap<String, Object> getRecordModelByReportId(String id){
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		try {
			recordDirService.getRecordModelByReportId(map,id);
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "查询失败！");
			map.put("success", false);
		}
		
		return map;
	}
	
	/**
	 * 获取模板信息(包括目录)
	 * author pingZhou
	 * @param rtCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping("getFileContent")
	public HashMap<String, Object> getFileContent(String filePath){
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		try {
			String fileContent = recordDirService.getFileContent(filePath);
			
			map.put("fileContent", fileContent);
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "查询失败！");
			map.put("success", false);
		}
		
		return map;
	}
	
	

	/**
	 * 新报告工具报告打包下载
	 * author pingZhou
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("downloadFileBusIds")
	@ResponseBody
	public void downloadBusIds(HttpServletRequest request, HttpServletResponse response, String id) throws Exception {
		try {
			if(id==null) {
				id = request.getParameter("id");
			}
			
			File zipFile = this.recordDirService.downloadBusIds(id);
			String dateName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			
		
			FileUtil.download(response, zipFile,"file_"+id+".zip", null);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	
	} 

	/**
	 * 获取原始记录目录
	 * author pingZhou
	 * @param rtCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping("getRecordDir")
	public String getRecordDir(String rtCode,String id){
		
		JSONArray dirs= recordDirService.getRecordDir(rtCode,id);
		
		
		return dirs.toString();
	}
	
}
