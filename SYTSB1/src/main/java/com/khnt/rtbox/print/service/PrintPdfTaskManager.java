package com.khnt.rtbox.print.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.khnt.base.Factory;
import com.khnt.core.crud.manager.impl.EntityManageImpl;
import com.khnt.core.exception.KhntException;
import com.khnt.pub.fileupload.bean.Attachment;
import com.khnt.pub.fileupload.service.AttachmentManager;
import com.khnt.rtbox.config.service.RtDirManager;
import com.khnt.rtbox.print.bean.PrintPdfLog;
import com.khnt.rtbox.print.bean.PrintPdfTask;
import com.khnt.rtbox.print.dao.PrintPdfTaskDao;
import com.khnt.rtbox.print.model.PrintJsTask;
import com.khnt.rtbox.template.constant.RtPath;
import com.khnt.rtbox.tools.PdfUtils;
import com.khnt.utils.StringUtil;
import com.lsts.inspection.service.InspectionVersionService;
import com.lsts.mobileapp.input.bean.InspectRecordDir;
import com.lsts.mobileapp.input.dao.InspectRecordDirDao;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/*******************************************************************************
 * <p>
 * 
 * </p>
 * 
 * @ClassName PrintPdfTaskManager
 * @JDK 1.5
 * @author CODER_V3.0
 * @date 2018-07-27 14:02:49
 * 
 *       ZQ ADD，20180727 JSP打印PDF
 */
@Service("printPdfTaskManager")
public class PrintPdfTaskManager extends EntityManageImpl<PrintPdfTask, PrintPdfTaskDao> {
	@Autowired
	PrintPdfTaskDao printPdfTaskDao;
	@Autowired
	RtDirManager rtDirManager;
	@Autowired
	InspectRecordDirDao recordDirDao;
	@Autowired
	AttachmentManager attachmentManager;
	@Autowired
	PrintPdfLogManager printPdfLogManager;
	@Autowired
	InspectionVersionService inspectionVersionService;

	public static String RT_PRINT_PDF_HOST = Factory.getSysPara().getProperty("RT_PRINT_PDF_HOST");

	private static String PROCESS_KEY = "pdfProcessing";
	private static String PROCESS_KEY_RECORD = "pdfRecordProcessing";

	/**
	 * 保存打印任务
	 * 
	 * @param fkBusinessId
	 *            INSPECTION_INFO ID
	 */
	private void doSaveTask(String fkBusinessId, String host) throws Exception {

		PrintPdfTask task = initTask(fkBusinessId);

		this.print(task, host);
	}

	/**
	 * 打印任务 定时打印调用时host为null
	 * 
	 * @param task
	 * @param host
	 * @throws Exception
	 */
	public void print(PrintPdfTask task, String host) throws Exception {
		WebApplicationContext aplicationContext = ContextLoader.getCurrentWebApplicationContext();
		if (StringUtil.isNotEmpty(host)) {
			if (!host.trim().endsWith("/")) {
				host += "/";
			}
			task.setPrintHost(host);

		} else {
			host = task.getPrintHost();
		}
		if (StringUtil.isEmpty(task.getReportTplNo())) {
			throw new Exception("打印任务保存失败：未找到RTBOX模板编号");
		}
		if (StringUtil.isEmpty(task.getReportSn())) {
			throw new Exception("打印任务保存失败：未找到报告编号:");
		}

		String fkBusinessId = task.getFkInspectionInfoId();
		// 判断该任务是否处于打印中
		@SuppressWarnings("unchecked")
		Set<String> processing = (Set<String>) aplicationContext.getServletContext().getAttribute(PROCESS_KEY);
		if (processing == null) {
			processing = new HashSet<String>();
		}
		if (!processing.contains(fkBusinessId)) {
			processing.add(fkBusinessId);
			aplicationContext.getServletContext().setAttribute(PROCESS_KEY, processing);
		} else {
			throw new Exception("当前业务ID:" + fkBusinessId + "已有打印任务正在执行，请稍后再试....");
		}

		String printBatch = null;
		InputStream inputStream = null;
		List<Map<String, String>> pages = null;
		JSONArray array = null;
		String code = null;
		try {

			code = this.rtDirManager.getDir(fkBusinessId, task.getReportTplNo());
			task.setStatus("waitPrint");// 等待打印
			if (StringUtil.isEmpty(task.getId())) {
				task.setCreateTime(new Date());
				task.setTryCount(0);
			}

			try {
				array = JSONArray.fromString(code);
				pages = new ArrayList<Map<String, String>>();
				JSONObject dir = array.getJSONObject(0);
				JSONArray children = dir.getJSONArray("children");
				if (children == null || children.length() <= 0) {
					throw new Exception("空的目录结构");
				}

				for (int i = 0; i < children.length(); i++) {
					Map<String, String> map = new ConcurrentHashMap<String, String>();
					JSONObject child = children.getJSONObject(i);
					String pageName = child.getString("pageName");
					String pageCode = child.getString("code");
					map.put("pageName", pageName);
					map.put("pageCode", pageCode);
					pages.add(map);
					if(child.has("children")) {
						JSONArray childChildren = child.getJSONArray("children");
						for (int j = 0; j < childChildren.length(); j++) {
							Map<String, String> mapc = new ConcurrentHashMap<String, String>();
							JSONObject childc = childChildren.getJSONObject(j);
							String pageNamec = childc.getString("pageName");
							String pageCodec = childc.getString("code");
							mapc.put("pageName", pageNamec);
							mapc.put("pageCode", pageCodec);
							pages.add(mapc);
						}
					}
					// System.out.println(pageName);
				}
			} catch (Exception e) {
				// task.setStatus("failure");// 失败
				task.setRemark("目录转换出错，未进入打印程序");
				// this.save(task);

				throw e;
			}

			if (pages != null) {
				task.setReportDir(listToString(pages, "pageName"));// 打印目录

				String savePath = RtPath.basePath + "tmp/" + task.getInspectDate() + "/" + task.getReportSn() + "/parts/";
				File file = new File(savePath);
				if (!file.exists()) {
					file.mkdirs();
				}

				printBatch = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(task.getCreateTime());// 打印批次

				log.info("1.按分页打印PDF");
				ExecutorService fixedThreadPool = Executors.newFixedThreadPool(pages.size() > 10 ? 10 : pages.size());
				String failReason = null;
				// List<String> result=new ArrayList<String>();
				Vector<Future<PrintPdfLog>> resultFuture = new Vector<Future<PrintPdfLog>>();
				List<PrintPdfLog> logs = new ArrayList<PrintPdfLog>();
				String[] partPdfs = new String[pages.size()];
				for (int i = 0; i < pages.size(); i++) {
					Map<String, String> map = pages.get(i);
					String pageName = map.get("pageName");
					String pageCode = map.get("pageCode");
					String pageUrl = pageName.split("__")[0];
					if(pageUrl.indexOf(".html")==-1) {
						pageUrl = pageUrl + ".html";
					}
					if (pageName.contains("?")) {
						pageUrl += "&";
					} else {
						pageUrl += "?";
					}
					String codeExt = "";
					if(pageCode.split("__").length>1) {
						codeExt = pageCode.split("__")[1];
					} 

					String url = host + RtPath.tplPageDir + "/show_right.jsp?code="+task.getReportTplNo() + "&pageName="+pageUrl+"&code_ext="+codeExt+"&fk_report_id=" + task.getReportTplId()
					+"&pageCode="+pageCode+ "&info_id=" + task.getFkInspectionInfoId() + "&pageStatus=detail&mobile=2";
			
					/*String url = host + task.getReportTplUrl() + pageUrl + "fk_report_id=" + task.getReportTplId()
							+ "&fk_inspection_info_id=" + task.getFkInspectionInfoId() + "&pageStatus=detail&mobile=2";*/

					PrintJsTask jsTask = new PrintJsTask(task, pageCode, url, savePath + pageCode + ".pdf", printBatch);
					// PrintPdfLog printPdfLog = jsTask.printPdf();
					// logs.add(printPdfLog);
					// partPdfs[i] = printPdfLog.getPagePath();
					Future<PrintPdfLog> future = fixedThreadPool.submit(jsTask);
					resultFuture.add(future);

				}
				try {
					// 等待计算结果，最长等待timeout秒，timeout秒后中止任务
					for (Future<PrintPdfLog> future : resultFuture) {
						PrintPdfLog result = future.get(15000, TimeUnit.SECONDS);
						if (result != null) {
							logs.add(result);
						}
					}
				} catch (InterruptedException e) {
					failReason = "主线程在等待计算结果时被中断！";
				} catch (ExecutionException e) {
					failReason = "主线程等待计算结果，但计算抛出异常！";
				} catch (TimeoutException e) {
					failReason = "主线程等待计算结果超时，因此中断任务线程！";
					fixedThreadPool.shutdownNow();
				} catch (KhntException e) {
					failReason = "子线程报错，因此中断任务线程！";
					fixedThreadPool.shutdownNow();
				}
				fixedThreadPool.shutdown();
				log.debug("main thread finished!!");
				System.out.println(failReason);

				this.save(task);
				logs = sort(pages, logs);
				// for (PrintPdfLog log : logs) {
				boolean printFlag = true;
				for (int i = 0; i < pages.size(); i++) {
					PrintPdfLog log = logs.get(i);
					if (!"0".equals(log.getResult())) {
						printFlag = false;
					}
					this.printPdfLogManager.save(log);
					partPdfs[i] = log.getPagePath();
				}
				if (!printFlag) {
					// task.setStatus("failure");// 失败
					// task.setTryCount(task.getTryCount() + 1);// 尝试次数
					// task.setPrintTime(new Date());
					task.setRemark("打印失败,打印报告时发生错误，不能继续合成PDF，打印批次" + printBatch + ",打印日志共" + pages.size() + "条");
					// this.save(task);

					throw new Exception("打印报告时发生错误，不能继续合成PDF");
				}

				log.info("2.合并分页PDF");
				if (!RtPath.basePath.endsWith("/")) {
					RtPath.basePath = RtPath.basePath + "/";
				}

				if (StringUtil.isEmpty(task.getPdfPath())) { // 第一次保存的时候会借助inspect_date生成，第2次尝试重新打印的时候不需要
					String pdfSavePath = RtPath.basePath + "tmp/" + task.getInspectDate() + "/" + task.getReportSn() + ".pdf";
					task.setPdfPath(pdfSavePath);
				}

				int startPage = this.getStartPage(fkBusinessId);// 获取配置的第一页

				PdfUtils.mergePdfFilesMarkFoot(partPdfs, task.getPdfPath(), startPage);
				File pdfFile = new File(task.getPdfPath());
				inputStream = new FileInputStream(pdfFile);

				log.info("3.插入数据库附件");
				

				//保留历史版本
				inspectionVersionService.copyOldAtt(task.getFkInspectionInfoId(), "report");
				
				
				Attachment attachment = new Attachment();
				// attachment.setFilePath(filePath);
				attachment.setBusinessId(task.getId());
				attachment.setFileName(pdfFile.getName());
				attachment.setFileSize(pdfFile.length());
				attachment.setFileType("application/octet-stream");
				attachment.setUploadTime(new Date());
				String folder = task.getInspectDate() + File.separator + task.getReportSn();
				System.out.println("folder:" + folder);
				
				String fname = task.getReportSn() + ".pdf";
				attachmentManager.saveAttached(inputStream, attachment, "disk", true, folder, fname);

				log.info("4.保存成功状态");
				task.setStatus("success");// 成功
				task.setPdfAtt(attachment.getId());
				task.setTryCount(task.getTryCount() + 1);// 尝试次数
				task.setRemark("打印成功，打印批次" + printBatch + ",打印日志共" + pages.size() + "条");
				task.setPrintTime(new Date());
				this.save(task);
				int pageNum = pages.size();
				log.info("5.完善修改tzsb_inspection_info中PDF打印状态，附件ID");
				this.printPdfTaskDao.createSQLQuery("update tzsb_inspection_info set pdf_export_ps=?,pdf_export_att=?,report_page_num=? where id =?", "1", attachment.getId(),
						pageNum,fkBusinessId).executeUpdate();

			}
		} catch (Exception e) {
			e.printStackTrace();

			log.info(" 打印出错 修改tzsb_inspection_info中PDF打印状态，附件ID");
			this.printPdfTaskDao.createSQLQuery("update tzsb_inspection_info set pdf_export_ps=?,pdf_export_att=? where id =?", "0", null, fkBusinessId)
					.executeUpdate();

			task.setStatus("failure");// 失败
			task.setPrintTime(new Date());
			task.setTryCount(task.getTryCount() + 1);// 尝试次数
			if (StringUtil.isEmpty(task.getRemark())) {
				task.setRemark("打印出错:" + e.getMessage());
			}
			this.save(task);

			throw e;
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
			this.clean(fkBusinessId);
		}

	}

	/*
	 * 清除打印任务
	 */
	private void clean(String fkBusinessId) {
		WebApplicationContext aplicationContext = ContextLoader.getCurrentWebApplicationContext();
		@SuppressWarnings("unchecked")
		Set<String> processing = (Set<String>) aplicationContext.getServletContext().getAttribute(PROCESS_KEY);
		try {
			processing.remove(fkBusinessId);
		} catch (Exception e) {
		}
		aplicationContext.getServletContext().setAttribute(PROCESS_KEY, processing);

	}
	

	/*
	 * 清除打印任务
	 */
	private void cleanRecord(String fkBusinessId) {
		WebApplicationContext aplicationContext = ContextLoader.getCurrentWebApplicationContext();
		@SuppressWarnings("unchecked")
		Set<String> processing = (Set<String>) aplicationContext.getServletContext().getAttribute(PROCESS_KEY_RECORD);
		try {
			processing.remove(fkBusinessId);
		} catch (Exception e) {
		}
		aplicationContext.getServletContext().setAttribute(PROCESS_KEY_RECORD, processing);

	}

	/**
	 * 排序
	 * 
	 * @param pages
	 * @param logs
	 * @return
	 */
	private List<PrintPdfLog> sort(List<Map<String, String>> pages, List<PrintPdfLog> logs) {
		List<PrintPdfLog> returnLogs = new ArrayList<PrintPdfLog>();
		for (int i = 0; i < pages.size(); i++) {
			Map<String, String> map = pages.get(i);
			String pageCode = map.get("pageCode");
			for (PrintPdfLog log : logs) {
				if (log.getPageNo().equals(pageCode)) {
					returnLogs.add(log);
					break;
				}
			}

		}
		return returnLogs;
	}

	@SuppressWarnings("unchecked")
	private PrintPdfTask initTask(String fkBusinessId) throws Exception {
		List<Map<String, Object>> list = this.printPdfTaskDao
				.createSQLQuery(
						"select a.id,a.report_sn,a.report_type, to_char(a.advance_time,'yyyy-MM-dd') inspect_date,c.rt_code From tzsb_inspection_info a,base_reports b,rt_template c "
								+ "where a.report_type=b.id and b.rtbox_id=c.id and a.id=? ", fkBusinessId).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
				.list();
		if (list == null || list.isEmpty()) {

			throw new Exception("未找到相关报告");
		}
		Map<String, String> lowerMap = new HashMap<String, String>();
		Map<String, Object> map = list.get(0);
		for (String key : map.keySet()) {
			lowerMap.put(key.toLowerCase(), String.valueOf(map.get(key)));
		}

		PrintPdfTask task = new PrintPdfTask();
		// tzsb_inspection_info ID
		task.setFkInspectionInfoId(fkBusinessId);
		// tzsb_inspection_info REPORT_SN
		task.setReportSn(String.valueOf(lowerMap.get("report_sn")));
		task.setReportType(null);
		task.setInspectDate(String.valueOf(lowerMap.get("inspect_date")).replaceAll("-", ""));// 检验日期
		// tzsb_inspection_info FK_REPORT_ID
		task.setReportTplId(String.valueOf(lowerMap.get("fk_report_id")));
		// RTBOX模板编号
		task.setReportTplNo(String.valueOf(lowerMap.get("rt_code")));
		// RTBOX模板存放地址
		//task.setReportTplUrl(String.valueOf(lowerMap.get("out_put_jsp_file_path")));

		return task;
	}

	@SuppressWarnings("unchecked")
	private int getStartPage(String fkBusinessId) {
		int defaultStart = 2;
		List<Object> list = this.printPdfTaskDao.createSQLQuery(
				"select c.page_index from tzsb_inspection_info a,base_reports b,BASE_REPORTS_ITME c "
						+ "		where a.report_type=b.id and b.id=c.fk_reports_id and c.is_start_page='1' and a.id=? ", fkBusinessId).list();
		if (list != null && !list.isEmpty()) {
			Object obj = list.get(0);
			if (obj == null) {
				return defaultStart;
			}
			if (obj instanceof String) {
				int v = Integer.parseInt((String) obj);
				return v;
			} else if (obj instanceof Integer) {
				int v = Integer.parseInt(obj.toString()) ;
				return v;
			} else if (obj instanceof BigDecimal) {
				int v = ((BigDecimal) obj).intValue();
				return v;
			}
		}
		return defaultStart;
	}

	private String listToString(List<Map<String, String>> list, String key) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			String pageName = list.get(i).get(key);
			sb.append(pageName).append(",");
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	/**
	 * 根据报告ID指定打印任务开始n打印
	 *
	 * @param id
	 */
	public void printTaskId(String fkInfoId) {

	}

	/**
	 * 打印任务从Action前端 来
	 * 
	 * @param request
	 * @param fkBusinessId
	 * @throws Exception
	 */
	public void saveTask(String fkBusinessId, HttpServletRequest request) throws Exception {
		String path = request.getContextPath();
		String basePath = RT_PRINT_PDF_HOST;
		if (StringUtil.isEmpty(basePath)) {
			basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
		}
		basePath += path + "/";
		this.doSaveTask(fkBusinessId, basePath);
	}
	
	

	/**
	 * 打印原始记录任务从Action前端 来
	 * 
	 * @param request
	 * @param fkBusinessId
	 * @throws Exception
	 */
	public void saveRecordTask(String fkBusinessId, HttpServletRequest request) throws Exception {
		String path = request.getContextPath();
		String basePath = RT_PRINT_PDF_HOST;
		if (StringUtil.isEmpty(basePath)) {
			basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
		}
		basePath += path + "/";
		this.doSaveRecordTask(fkBusinessId, basePath);
	}

	/**
	 * 保存原始记录打印任务
	 * 
	 * @param fkBusinessId
	 *            INSPECTION_INFO ID
	 */
	private void doSaveRecordTask(String fkBusinessId, String host) throws Exception {

		PrintPdfTask task = initRecordTask(fkBusinessId);

		this.printRecord(task, host);
	}
	

	/**
	 * 初始化原始记录任务
	 * author pingZhou
	 * @param fkBusinessId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private PrintPdfTask initRecordTask(String fkBusinessId) throws Exception {
		List<Map<String, Object>> list = this.printPdfTaskDao
				.createSQLQuery(
						"select a.id,a.report_sn,a.report_type, to_char(a.advance_time,'yyyy-MM-dd') inspect_date,c.rt_code,c.out_put_jsp_file_path From tzsb_inspection_info a,base_reports b,rt_page c "
								+ "where a.report_type=b.id and b.record_model_id=c.id and a.id=? ", fkBusinessId).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
				.list();
		if (list == null || list.isEmpty()) {

			throw new Exception("未找到相关报告");
		}
		Map<String, String> lowerMap = new HashMap<String, String>();
		Map<String, Object> map = list.get(0);
		for (String key : map.keySet()) {
			lowerMap.put(key.toLowerCase(), String.valueOf(map.get(key)));
		}

		PrintPdfTask task = new PrintPdfTask();
		// tzsb_inspection_info ID
		task.setFkInspectionInfoId(fkBusinessId);
		// tzsb_inspection_info REPORT_SN
		task.setReportSn(String.valueOf(lowerMap.get("report_sn")));
		task.setReportType("record");
		task.setInspectDate(String.valueOf(lowerMap.get("inspect_date")));// 检验日期
		// tzsb_inspection_info FK_REPORT_ID
		task.setReportTplId(String.valueOf(lowerMap.get("fk_report_id")));
		// RTBOX模板编号
		task.setReportTplNo(String.valueOf(lowerMap.get("rt_code")));
		// RTBOX模板存放地址
		task.setReportTplUrl(String.valueOf(lowerMap.get("out_put_jsp_file_path")));

		return task;
	}
	
	/**
	 * 打印原始记录任务 定时打印调用时host为null
	 * 
	 * @param task
	 * @param host
	 * @throws Exception
	 */
	public void printRecord(PrintPdfTask task, String host) throws Exception {
		if (StringUtil.isNotEmpty(host)) {
			if (!host.trim().endsWith("/")) {
				host += "/";
			}
			task.setPrintHost(host);

		} else {
			host = task.getPrintHost();
		}
		if (StringUtil.isEmpty(task.getReportTplNo())) {
			throw new Exception("打印任务保存失败：未找到RTBOX模板编号");
		}
		if (StringUtil.isEmpty(task.getReportSn())) {
			throw new Exception("打印任务保存失败：未找到报告编号:");
		}

		String fkBusinessId = task.getFkInspectionInfoId();
		// 判断该任务是否处于打印中
		WebApplicationContext aplicationContext = ContextLoader.getCurrentWebApplicationContext();
		@SuppressWarnings("unchecked")
		Set<String> processing = (Set<String>) aplicationContext.getServletContext().getAttribute(PROCESS_KEY_RECORD);
		//if (processing == null) {
			processing = new HashSet<String>();
		//}
		if (!processing.contains(fkBusinessId+"_record")) {
			processing.add(fkBusinessId+"_record");
			aplicationContext.getServletContext().setAttribute(PROCESS_KEY_RECORD, processing);
		} else {
			throw new Exception("当前业务ID:" + fkBusinessId + "已有打印原始记录任务正在执行，请稍后再试....");
		}

		String printBatch = null;
		InputStream inputStream = null;
		List<Map<String, String>> pages = null;

		try {
			
			try {

			task.setStatus("waitPrint");// 等待打印
			if (StringUtil.isEmpty(task.getId())) {
				task.setCreateTime(new Date());
				task.setTryCount(0);
			}
			
			List<InspectRecordDir> dirs = recordDirDao.getDirByBid(fkBusinessId);
			pages = new ArrayList<Map<String, String>>();
			for (int i = 0; i < dirs.size(); i++) {
				InspectRecordDir dir = dirs.get(i);
				Map<String, String> map = new ConcurrentHashMap<String, String>();
				String pagePath = dir.getPagePath();
				String pageName = pagePath.substring(pagePath.indexOf("index"), pagePath.length());
				String pageCode = dir.getPageCode();
				map.put("pageName", pageName);
				map.put("pageCode", pageCode);
				pages.add(map);
			}
			
		} catch (Exception e) {
				// task.setStatus("failure");// 失败
				task.setRemark("目录转换出错，未进入打印程序");
				// this.save(task);

				throw e;
		}

	if (pages != null) {
				task.setReportDir(listToString(pages, "pageName"));// 打印目录

				String savePath = RtPath.basePath + "tmp/" + task.getInspectDate() + "/record/" + task.getReportSn() + "/parts/";
				File file = new File(savePath);
				if (!file.exists()) {
					file.mkdirs();
				}

				printBatch = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(task.getCreateTime());// 打印批次

				log.info("1.按分页打印PDF");
				ExecutorService fixedThreadPool = Executors.newFixedThreadPool(pages.size() > 10 ? 10 : pages.size());
				String failReason = null;
				// List<String> result=new ArrayList<String>();
				Vector<Future<PrintPdfLog>> resultFuture = new Vector<Future<PrintPdfLog>>();
				List<PrintPdfLog> logs = new ArrayList<PrintPdfLog>();
				String[] partPdfs = new String[pages.size()];
				for (int i = 0; i < pages.size(); i++) {
					Map<String, String> map = pages.get(i);
					String pageName = map.get("pageName");
					String pageCode = map.get("pageCode");
					String pageUrl = pageName.split("__")[0];
					if(pageUrl.indexOf(".html")==-1) {
						pageUrl = pageUrl + ".html";
					}
					if (pageName.contains("?")) {
						pageUrl += "&";
					} else {
						pageUrl += "?";
					}
					String codeExt = "";
					if(pageCode.split("__").length>1) {
						codeExt = pageCode.split("__")[1];
					} 
					String url = host + RtPath.tplRecordPageDir + "/show_right.jsp?code="+task.getReportTplNo() + "&pageName="+pageUrl+"&code_ext="+codeExt
					+"&pageCode="+pageCode+ "&info_id=" + task.getFkInspectionInfoId() + "&pageStatus=detail&mobile=2";
			
					/*String url = host + task.getReportTplUrl() + pageUrl + "fk_report_id=" + task.getReportTplId()
							+ "&fk_inspection_info_id=" + task.getFkInspectionInfoId() + "&pageStatus=detail&mobile=2";*/

					PrintJsTask jsTask = new PrintJsTask(task, pageCode, url, savePath + pageCode + ".pdf", printBatch);
					// PrintPdfLog printPdfLog = jsTask.printPdf();
					// logs.add(printPdfLog);
					// partPdfs[i] = printPdfLog.getPagePath();
					Future<PrintPdfLog> future = fixedThreadPool.submit(jsTask);
					resultFuture.add(future);

				}
				try {
					// 等待计算结果，最长等待timeout秒，timeout秒后中止任务
					for (Future<PrintPdfLog> future : resultFuture) {
						PrintPdfLog result = future.get(15000, TimeUnit.SECONDS);
						if (result != null) {
							logs.add(result);
						}
					}
				} catch (InterruptedException e) {
					failReason = "主线程在等待计算结果时被中断！";
				} catch (ExecutionException e) {
					failReason = "主线程等待计算结果，但计算抛出异常！";
				} catch (TimeoutException e) {
					failReason = "主线程等待计算结果超时，因此中断任务线程！";
					fixedThreadPool.shutdownNow();
				} catch (KhntException e) {
					failReason = "子线程报错，因此中断任务线程！";
					fixedThreadPool.shutdownNow();
				}
				fixedThreadPool.shutdown();
				log.debug("main thread finished!!");
				System.out.println(failReason);

				this.save(task);
				logs = sort(pages, logs);
				// for (PrintPdfLog log : logs) {
				boolean printFlag = true;
				for (int i = 0; i < pages.size(); i++) {
					PrintPdfLog log = logs.get(i);
					if (!"0".equals(log.getResult())) {
						printFlag = false;
					}
					this.printPdfLogManager.save(log);
					partPdfs[i] = log.getPagePath();
				}
				if (!printFlag) {
					// task.setStatus("failure");// 失败
					// task.setTryCount(task.getTryCount() + 1);// 尝试次数
					// task.setPrintTime(new Date());
					task.setRemark("打印失败,打印报告时发生错误，不能继续合成PDF，打印批次" + printBatch + ",打印日志共" + pages.size() + "条");
					// this.save(task);

					throw new Exception("打印报告时发生错误，不能继续合成PDF");
				}

				log.info("2.合并分页PDF");
				if (!RtPath.basePath.endsWith("/")) {
					RtPath.basePath = RtPath.basePath + "/";
				}

				if (StringUtil.isEmpty(task.getPdfPath())) { // 第一次保存的时候会借助inspect_date生成，第2次尝试重新打印的时候不需要
					String pdfSavePath = RtPath.basePath + "tmp/" + task.getInspectDate() + "/record/" + task.getReportSn() + ".pdf";
					task.setPdfPath(pdfSavePath);
				}

				int startPage = this.getStartPage(fkBusinessId);// 获取配置的第一页

				PdfUtils.mergePdfFilesMarkFoot(partPdfs, task.getPdfPath(), startPage);
				File pdfFile = new File(task.getPdfPath());
				inputStream = new FileInputStream(pdfFile);

				log.info("3.插入数据库附件");
				

				//保留历史版本
				inspectionVersionService.copyOldAtt(task.getFkInspectionInfoId(), "record");
				
				
				Attachment attachment = new Attachment();
				// attachment.setFilePath(filePath);
				attachment.setBusinessId(task.getId());
				attachment.setFileName(pdfFile.getName());
				attachment.setFileSize(pdfFile.length());
				attachment.setFileType("application/octet-stream");
				attachment.setUploadTime(new Date());
				String folder = task.getInspectDate() + File.separator + task.getReportSn()+"/record";
				System.out.println("folder:" + folder);
				
				String fname = task.getReportSn() + ".pdf";
				attachmentManager.saveAttached(inputStream, attachment, "disk", true, folder, fname);

				log.info("4.保存成功状态");
				task.setStatus("success");// 成功
				task.setPdfAtt(attachment.getId());
				task.setTryCount(task.getTryCount() + 1);// 尝试次数
				task.setRemark("打印成功，打印批次" + printBatch + ",打印日志共" + pages.size() + "条");
				task.setPrintTime(new Date());
				this.save(task);
				int pageNum = pages.size();
				log.info("5.完善修改tzsb_inspection_info中PDF打印状态，附件ID");
				this.printPdfTaskDao.createSQLQuery("update tzsb_inspection_info set pdf_export_record=?,pdf_export_record_att=?,record_page_num=? where id =?", "1", attachment.getId(),
						pageNum,fkBusinessId).executeUpdate();

			}
		} catch (Exception e) {
			e.printStackTrace();

			log.info(" 打印出错 修改tzsb_inspection_info中PDF打印状态，附件ID");
			this.printPdfTaskDao.createSQLQuery("update tzsb_inspection_info set pdf_export_ps=?,pdf_export_att=? where id =?", "0", null, fkBusinessId)
					.executeUpdate();

			task.setStatus("failure");// 失败
			task.setPrintTime(new Date());
			task.setTryCount(task.getTryCount() + 1);// 尝试次数
			if (StringUtil.isEmpty(task.getRemark())) {
				task.setRemark("打印出错:" + e.getMessage());
			}
			this.save(task);

			throw e;
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
			this.cleanRecord(fkBusinessId);
		}

	}
	

}
