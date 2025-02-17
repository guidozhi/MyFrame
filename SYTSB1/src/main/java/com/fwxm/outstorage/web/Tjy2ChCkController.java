package com.fwxm.outstorage.web;

import com.fwxm.material.bean.GoodsType;
import com.fwxm.material.dao.GoodsTypeDao;
import com.fwxm.outstorage.bean.Tjy2ChCk;
import com.fwxm.outstorage.service.Tjy2ChCkManager;
import com.khnt.core.crud.web.SpringSupportAction;
import com.khnt.core.crud.web.support.JsonWrapper;
import com.khnt.core.exception.KhntException;
import com.khnt.rbac.impl.bean.Org;
import com.khnt.rbac.impl.dao.OrgDao;
import com.khnt.utils.StringUtil;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("chck/")
public class Tjy2ChCkController extends SpringSupportAction<Tjy2ChCk, Tjy2ChCkManager> {

    @Autowired
    private Tjy2ChCkManager tjy2ChCkManager;

    @Autowired
    private GoodsTypeDao goodsTypeDao;

    @Autowired
    private OrgDao orgDao;

    /**
     * 出库
     *
     * @param entity
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "outstorage")
    @ResponseBody
    public HashMap<String, Object> outStorage(@RequestBody Tjy2ChCk entity) throws Exception {
        if (StringUtil.isEmpty(entity.getId())) {
            throw new KhntException("请选择一张单据进行出库");
        }
        tjy2ChCkManager.outStorage(entity, entity.getCkyjtype());
        return JsonWrapper.successWrapper("");
    }

    /**
     * 根据ID删除 未出库的单据
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "deleteOrder")
    @ResponseBody
    public HashMap<String, Object> deleteOrder(HttpServletRequest request) throws Exception {
        tjy2ChCkManager.deleteOrder(request);
        return JsonWrapper.successWrapper("");
    }


    @RequestMapping(value = "download")
    @ResponseBody
    public void downLoadCkDetail(HttpServletRequest request, HttpServletResponse response) {
        try {
            DecimalFormat df = new DecimalFormat("0.00");
            String startTimeString = request.getParameter("startTime");
            String endTimeString = request.getParameter("endTime");
            String orgId = request.getParameter("orgId");
            String id=request.getParameter("id");
            Org org = orgDao.get(orgId);
            String unitId = request.getParameter("unitId");
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM");
            Date startTime = null;
            if (StringUtil.isNotEmpty(startTimeString)) {
                startTime = sf.parse(startTimeString);
            }
            Date endTime = null;
            if (StringUtil.isNotEmpty(endTimeString)) {
                endTime = sf.parse(endTimeString);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(endTime);
                calendar.add(Calendar.MONTH, 1);
                endTime = calendar.getTime();
            }
            HSSFWorkbook workbook = new HSSFWorkbook();
            List<GoodsType> goodsTypes = goodsTypeDao.getAllTypes(orgId, unitId);

            //===================设置标题样式 开始===========================
            //获取样式
            HSSFCellStyle titleStyle = workbook.createCellStyle();
            //居中对齐
            titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            titleStyle.setWrapText(true);
            //设置字体
            HSSFFont font = workbook.createFont();
            font.setFontHeightInPoints((short) 20); // 字体高度
            font.setFontName(" 黑体 "); // 字体
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            titleStyle.setFont(font);
            //===================设置标题样式 结束===========================

            //===================设置金额样式 开始===========================
            //获取样式
            HSSFCellStyle jeStyle = workbook.createCellStyle();
            //居中对齐
            jeStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            jeStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            jeStyle.setWrapText(true);
            jeStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
            jeStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
            jeStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
            jeStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
            //设置字体
            HSSFFont jeFont = workbook.createFont();
            jeFont.setFontHeightInPoints((short) 11); // 字体高度
            jeFont.setFontName(" 宋体 "); // 字体
            jeFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
            jeStyle.setFont(jeFont);
            //===================设置金额样式 结束===========================

            //===================设置普通样式 开始===========================
            //获取样式
            HSSFCellStyle cellstype = workbook.createCellStyle();
            //居中对齐
            cellstype.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cellstype.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            cellstype.setWrapText(true);
            cellstype.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
            cellstype.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
            cellstype.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
            cellstype.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
            //设置字体
            HSSFFont font1 = workbook.createFont();
            font1.setFontHeightInPoints((short) 11); // 字体高度
            font1.setFontName(" 宋体 "); // 字体
            cellstype.setFont(font1);
            //===================设置普通样式 结束===========================

            //===================设置列名样式 开始===========================
            HSSFCellStyle cellstype_header = workbook.createCellStyle();
            //居中对齐
            cellstype_header.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cellstype_header.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            cellstype_header.setWrapText(true);
            cellstype_header.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
            cellstype_header.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
            cellstype_header.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
            cellstype_header.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
            //设置字体
            HSSFFont font_header = workbook.createFont();
            font_header.setFontHeightInPoints((short) 15); // 字体高度
            font_header.setFontName(" 宋体 "); // 字体
            font_header.setBoldweight(Font.BOLDWEIGHT_BOLD);
            cellstype_header.setFont(font_header);
            //===================设置列名样式 结束===========================

            boolean createEmpty = true;
            
            String title = "";
            if(StringUtil.isNotEmpty(startTimeString)){
            	title=startTimeString + "~" + endTimeString + " " + org.getOrgName();
            }else{
            	title=org.getOrgName();
            }
            for (int i = 0; i < goodsTypes.size(); i++) {
                GoodsType type = goodsTypes.get(i);
                List<Object[]> goodsInformation = tjy2ChCkManager.getGoodsForDepartment(orgId, startTime, endTime, type.getLx_name(),id);
                if (CollectionUtils.isEmpty(goodsInformation)) {
                    continue;
                }
                createEmpty = false;
                HSSFSheet sheet = workbook.createSheet(type.getLx_name());// 创建一个Excel的Sheet
                
                
                workbook.setRepeatingRowsAndColumns(workbook.getNumberOfSheets()-1, 0, 11, 0, 1);//设置重复打印表头
                HSSFPrintSetup printSetup = sheet.getPrintSetup();
                printSetup.setLandscape(true);//设置横向打印
                printSetup.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);        //A4纸张打印
                printSetup.setScale((short) 70);//缩放比例
                sheet.setMargin(HSSFSheet.BottomMargin,( double ) 2 );// 页边距（下）
                sheet.setMargin(HSSFSheet.LeftMargin,( double ) 0.5 );// 页边距（左）
                sheet.setMargin(HSSFSheet.RightMargin,( double ) 0.1 );// 页边距（右）
                sheet.setMargin(HSSFSheet.TopMargin,( double ) 0.5 );// 页边距（上）
                Footer footer = sheet.getFooter();
                footer.setCenter( "第" + HeaderFooter.page() + "页，共 " + HeaderFooter.numPages()+"页" );
                
                
                //设置列宽
                sheet.setColumnWidth(0, 6000); //部门
                sheet.setColumnWidth(1, 4000); //日期
                sheet.setColumnWidth(2, 8000); //名称
                sheet.setColumnWidth(3, 6000); //规格
                sheet.setColumnWidth(4, 2000); //数量
                sheet.setColumnWidth(5, 2000); //单位
                sheet.setColumnWidth(6, 3000); //单价
                sheet.setColumnWidth(7, 4000); //金额
                sheet.setColumnWidth(8, 3000); //金额小计
                sheet.setColumnWidth(9, 4000); //经办人
                sheet.setColumnWidth(10, 4000); //负责人
                sheet.setColumnWidth(11, 4000); //备注


                HSSFRow row0 = sheet.createRow(0); // 创建第一行
                row0.setHeight((short)800);//setHeightInPoints(40);
                HSSFCell cell0_0 = row0.createCell(0);
                cell0_0.setCellValue(title + type.getLx_name() + "出库记录");
                cell0_0.setCellStyle(titleStyle);
                String[] headerArray = {"部门", "日期", "名称", "规格", "数量", "单位", "单价", "总金额", "金额小计", "经办人", "负责人签字", "备注"};
                for (int j = 1; j < headerArray.length; j++) {
                    HSSFCell cell = row0.createCell(j);
                    cell.setCellStyle(titleStyle);
                }
                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headerArray.length - 1));

                HSSFRow row1 = sheet.createRow(1); // 创建第2行
                row1.setHeight((short)800);//setHeightInPoints(40);
                for (int index = 0; index < headerArray.length; index++) {
                    HSSFCell cell1_X = row1.createCell(index);
                    cell1_X.setCellValue(headerArray[index]);
                    cell1_X.setCellStyle(cellstype_header);
                }

                int startIndex = 2;
                double amount = 0d;
                double xjHj = 0;
                double xjHjs = 0;
//                double djHj = 0;
//                double djHjs = 0;
                String[] columnArray = {"部门", "日期", "名称", "规格", "数量", "单位", "单价", "金额", "", "经办人", "负责人签字", "备注"};
                int mergeStartIndex = 2;
                String orgName="";
                for (int j = 0; j < goodsInformation.size(); j++) {
                    Object[] objects = goodsInformation.get(j);
                    orgName = objects[0].toString();
//                    HSSFRow row = sheet.createRow(startIndex + j);
                    HSSFRow row = sheet.createRow(sheet.getLastRowNum()+1);
                    row.setHeight((short)400);//setHeightInPoints(20);

                    int sl=goodsInformation.size();
                    int s=j%25;
                    int size=j+1;
                   boolean fy= (size%25==0) || size==sl;//是否该分页
                   
                    for (int k = 0; k < columnArray.length; k++) {
                        HSSFCell cell = row.createCell(k);

                        if (StringUtil.isEmpty(columnArray[k])) {
                            //空的就不用填，后面计算的时候再merge
                            cell.setCellStyle(jeStyle);
                            cell.setCellValue(df.format(Double.valueOf(objects[k - 1] != null ? objects[k - 1].toString() : "0")));
                        } else {
                            if (k == 6 || k == 7) {
                                Double rowMoney = Double.valueOf(objects[k] != null ? objects[k].toString() : "0");
                                cell.setCellValue(df.format(rowMoney));
                                cell.setCellStyle(jeStyle);
                                if (k == 6) {
                                    //单价
//                                    djHj += rowMoney;
//                                    djHjs += rowMoney;
                                }
                                if (k == 7) {
                                    amount += rowMoney;
                                    xjHj += rowMoney;
                                    xjHjs += rowMoney;
                                    //金额小计 = 金额合计（按部门划分）//或者该分页了
                                    if (j + 1 >= goodsInformation.size() || !orgName.equals(goodsInformation.get(j + 1)[0].toString()) || fy) {
                                        //表示循环到最后一行了 或者是 下一行开始是另一个部门了。
                                    	HSSFRow xjRow = sheet.getRow(mergeStartIndex);
//                                    	if(xjRow!=null){
                                    		 HSSFCell cell8 = xjRow.createCell(8);
                                             cell8.setCellStyle(jeStyle);
                                             cell8.setCellValue(df.format(amount));
                                             int ys=sheet.getLastRowNum();
//                                             sheet.addMergedRegion(new CellRangeAddress(mergeStartIndex, startIndex + j, 8, 8));
                                             sheet.addMergedRegion(new CellRangeAddress(mergeStartIndex, ys, 8, 8));
                                             if(!fy){
                                             	mergeStartIndex = sheet.getLastRowNum()+1;//startIndex + j + 1; //下一次merge的开始行号
                                             }else{
                                            	 mergeStartIndex = sheet.getLastRowNum()+2;//startIndex + j + 1; //下一次merge的开始行号
                                             }
                                             amount = 0d;
//                                    	}
                                       
                                    }
                                }
                            } else {
                                cell.setCellStyle(cellstype);
                                cell.setCellValue(objects[k] != null ? objects[k].toString() : "");
                            }
                        }
                    }
              	   if(fy){
              		   int aa=sheet.getLastRowNum()+1;
              		 HSSFRow rows = sheet.createRow(aa);
                     for (int k = 0; k < columnArray.length; k++) {
                         HSSFCell cell = rows.createCell(k);
                         cell.setCellStyle(cellstype);
                         if (k == 0) {
                             cell.setCellValue("合计");
                             if(aa%27==0){
                            	 sheet.setRowBreak(aa);//设置分页预览虚线
                             }
                             
                         } else if (k == 6) {
                             //cell.setCellStyle(jeStyle);
                             //cell.setCellValue(df.format(djHjs));
//                             djHjs=0;
                         } else if (k == 7) {
                             cell.setCellStyle(jeStyle);
                             cell.setCellValue(df.format(xjHjs));
                             xjHjs=0;
                         } else {
                             cell.setCellValue("");
                         }
                     }
              	   }
                    
                    if(j==sl-1 &&  s==0 && j!=0){
                    	sheet.setMargin(HSSFSheet.BottomMargin,( double ) 0 );// 页边距（下）
                    }
                }
                HSSFRow row = sheet.createRow(sheet.getLastRowNum()+1);
                for (int k = 0; k < columnArray.length; k++) {
                    HSSFCell cell = row.createCell(k);
                    cell.setCellStyle(cellstype);
                    if (k == 0) {
                        cell.setCellValue("总计");
                    } //else if (k == 6) {
//                        cell.setCellStyle(jeStyle);
//                        cell.setCellValue(df.format(djHj));
//                    }
                    else if (k == 7) {
                        cell.setCellStyle(jeStyle);
                        cell.setCellValue(df.format(xjHj));
                    }
                }
                
                
            }
            if (createEmpty) {
                HSSFSheet sheet = workbook.createSheet();
                sheet.setColumnWidth(0, 15000);
                HSSFRow row = sheet.createRow(0);
                row.setHeight((short)1000);//setHeightInPoints(50);
                HSSFCell cell = row.createCell(0);
                titleStyle.setWrapText(false);
                cell.setCellStyle(titleStyle);
                cell.setCellValue("未找到任何存货的出库记录");
            }
            response.setContentType("application/vnd.ms-excel");
            response.addHeader("Content-Disposition",
                    "attachment;filename=" + new String((title + "出库记录.xls").getBytes("gb2312"), "ISO8859-1"));
            OutputStream outp = response.getOutputStream();
            workbook.write(response.getOutputStream());
            outp.flush();
            outp.close();
        } catch (Exception e) {
        	e.printStackTrace();
            System.out.println(e);
        }
    }

    @RequestMapping(value = "getchcktotalamount")
    @ResponseBody
    public HashMap<String, Object> getChCkTotalAmount(String ids) throws Exception {
        return tjy2ChCkManager.getChCkTotalAmount(ids);
    }

    @RequestMapping(value = "getckdbhandids")
    @ResponseBody
    public HashMap<String, Object> getCkdbhAndIds(HttpServletRequest request) throws Exception {
        String id = request.getParameter("id");
        if (StringUtil.isNotEmpty(id)) {
            List<Tjy2ChCk> tjy2ChCks = tjy2ChCkManager.getTjy2ChCksByBxId(id);
            if (!CollectionUtils.isEmpty(tjy2ChCks)) {
                StringBuffer ckdbhs = new StringBuffer("");
                StringBuffer ckdIds = new StringBuffer("");
                for (int i = 0; i < tjy2ChCks.size(); i++) {
                    if (i == 0) {
                        ckdbhs.append(tjy2ChCks.get(i).getCkdbh());
                        ckdIds.append(tjy2ChCks.get(i).getId());
                    } else {
                        ckdbhs.append(",").append(tjy2ChCks.get(i).getCkdbh());
                        ckdIds.append(",").append(tjy2ChCks.get(i).getId());
                    }
                }
                Map<String, Object> result = new HashMap<String, Object>();
                result.put("ckdbhs", ckdbhs.toString());
                result.put("ckdids", ckdIds.toString());
                return JsonWrapper.successWrapper(result);
            }
        }
        return JsonWrapper.successWrapper();
    }
}
