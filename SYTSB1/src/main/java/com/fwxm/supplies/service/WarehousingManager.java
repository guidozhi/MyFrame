package com.fwxm.supplies.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HeaderFooter;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Footer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fwxm.material.bean.Goods;
import com.fwxm.material.bean.GoodsAndOrder;
import com.fwxm.material.bean.GoodsType;
import com.fwxm.material.bean.Supplier;
import com.fwxm.material.dao.GoodsAndOrderDao;
import com.fwxm.material.dao.GoodsDao;
import com.fwxm.material.dao.GoodsTypeDao;
import com.fwxm.material.dao.SupplierDao;
import com.fwxm.supplies.Number2CN;
import com.fwxm.supplies.bean.Warehousing;
import com.fwxm.supplies.bean.Zdbq;
import com.fwxm.supplies.dao.WarehousingDao;
import com.khnt.core.crud.manager.impl.EntityManageImpl;
import com.khnt.security.CurrentSessionUser;
import com.khnt.utils.StringUtil;
import com.lsts.employee.dao.EmployeesDao;
import com.lsts.finance.bean.Fybxd;
import com.lsts.finance.service.FybxdService;
import com.lsts.finance.web.MoneyConvertAction;

@Service("warehousingManager")
public class WarehousingManager extends EntityManageImpl<Warehousing, WarehousingDao>{
	@Autowired
	WarehousingDao warehousingDao; 
	@Autowired
	GoodsDao goodsDao;
	@Autowired
	GoodsAndOrderDao goodsAndOrderDao;
	@Autowired
	GoodsTypeDao goodsTypeDao;
	@Autowired
	SupplierDao supplierDao;
	@Autowired
	ZdbqManager zdbqManager;
	@Autowired
	private EmployeesDao employeesDao;
	@Autowired
	FybxdService fybxdService;
	@Autowired
    MoneyConvertAction moneyConvertAction;

	public final static String CW_FY_REGISTER = "REGISTER";
	
	
	public synchronized void fybxSave(Fybxd fybxd,CurrentSessionUser user,String rkids[],String rkdh)throws Exception{
		//保存费用报销表
		 Map<String, Object> paramMap=new HashMap<String, Object>();
		 paramMap.put("rkdh", rkdh);
		Fybxd bxBean =fybxdService.saveTask1(fybxd, user,paramMap);
		for (int i = 0; i < rkids.length; i++) {
			Warehousing bean=this.get(rkids[i]);
			bean.setFybxd_id(bxBean.getId());
			bean.setBz_zt(null);
			warehousingDao.save(bean);
		}
	}
	
	
	/**
	 * 批量提交
	 * @param ids
	 * @throws Exception 
	 */
	public void beanPlTj(String ids) throws Exception{
		String[] beanid=ids.split(",");
		for (String id : beanid) {
			Warehousing bean=this.get(id);
			bean.setState("2");
			List<GoodsAndOrder> listo=bean.getGoodsAndOrder();
			for (GoodsAndOrder goodsAndOrder : listo) {
				goodsAndOrder.setStatus("2");
				Goods g=goodsAndOrder.getGoods();
				g.setState("2");
			}
			this.save(bean);
		}
	}
	
	public HashMap<String, Object> saveBean(CurrentSessionUser curUser,String warehousing) throws Exception{	
  		HashMap<String, Object> map=new HashMap<String, Object>();

		JSONObject json= JSON.parseObject(warehousing);
		Warehousing entity=JSON.parseObject(warehousing, Warehousing.class);
		boolean save=StringUtil.isEmpty(entity.getId());//是否修改
		if(save){
			entity.setCreate_org_id(curUser.getDepartment().getId());
			entity.setCreate_org_name(curUser.getDepartment().getOrgName());
			entity.setCreate_time(new Date());
			entity.setCreate_user_id(curUser.getId());
			entity.setCreate_user_name(curUser.getName());
			//设置入库单编号  RK20181012001
			 SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		     Date date = new Date();
		     String bh= "RK"+sdf.format(date);
			 Map<String,Object> mapBean=warehousingDao.getBeanByYear(bh);
			 if(mapBean!=null){
				 Object object = mapBean.get("WAREHOUSING_NUM");
				 int no= Integer.parseInt(String.valueOf(object ))+1;
			     entity.setWarehousing_num(bh+String.format("%03d", no));
		     }else{
		    	 entity.setWarehousing_num(bh+"001");
		     }
			 entity.setState("1");
		}


		//保存存货表
		warehousingDao.save(entity);
		
		
		
		//保存物资表
		JSONArray goodsArray=json.getJSONArray("goods");
		for (int i = 0; i < goodsArray.size(); i++) {
			boolean addGoods=true;//判断是否新加的物资
			JSONObject goodsObject=goodsArray.getJSONObject(i);
			Goods goods=JSON.parseObject(goodsObject.toString(), Goods.class);
			if(StringUtil.isNotEmpty(goods.getId())){
				addGoods=false;
			}
			if(StringUtil.isEmpty(goods.getId())){
				goods.setCreate_time(new Date());
				goods.setCreate_user_id(curUser.getId());
				goods.setCreate_user_name(curUser.getName());
				goods.setCreate_org_id(curUser.getDepartment().getId());
				goods.setCreate_org_name(curUser.getDepartment().getOrgName());
				goods.setCreate_unit_id(curUser.getUnit().getId());
				goods.setCreate_unit_name(curUser.getUnit().getOrgName());
				goods.setSl(goods.getCssl());//新增物资库存等于初始数量
			}
			goods.setRk_time(json.getDate("rk_time"));
			goods.setWarehousing_num(entity.getWarehousing_num());
			goods.setFk_warehousing_id(entity.getId());
			goods.setState(entity.getState());
			String lxId=goodsObject.getString("lx_id");//物品类型
			if(StringUtil.isNotEmpty(lxId)){
				GoodsType type=goodsTypeDao.get(lxId);
				goods.setWplx(type.getLx_name());
				goods.setGoodstype(type);
			}
			if(StringUtil.isNotEmpty(goodsObject.getString("fk_gys_id"))){
				Supplier supplier=supplierDao.get(goodsObject.getString("fk_gys_id"));
				goods.setSupplier(supplier);
			}
			goodsDao.save(goods);

			
			
			

			//保存中间表
			GoodsAndOrder goodsAndOrder=new GoodsAndOrder();
			if(!addGoods){
			goodsAndOrder=goodsAndOrderDao.getGoodsAndOrderByOrderIdAndGoodsId(entity.getId(), goods.getId());
			}
			if(goodsAndOrder!=null){
				goodsAndOrder.setFk_order_id(entity.getId());
				goodsAndOrder.setSl(goods.getSl());
				goodsAndOrder.setType("RK");
				goodsAndOrder.setCrk_type("CG");
				goodsAndOrder.setFk_goods_id(goods.getId());
				goodsAndOrder.setCreate_time(new Date());
				goodsAndOrder.setStatus(entity.getState());
				goodsAndOrder.setBz(goodsObject.getString("bzs"));
				goodsAndOrder.setFph(goodsObject.getString("fph"));
				goodsAndOrderDao.save(goodsAndOrder);
			}
			
			//自动补全表
			zdbqManager.delText(goods.getWpmc(), curUser.getDepartment().getId(), "1",curUser);
			zdbqManager.delText(goods.getGgjxh(), curUser.getDepartment().getId(), "2",curUser);
			
		}

		
		map.put("success", "true");
		
  		return map;
		
	}
	

	public List<GoodsAndOrder> getGoodsByRkId(String id){
		return goodsAndOrderDao.getBeanByOrderId(id);
	}
	
	public void deleteWarehoesing(String ids){
			String[] id=ids.split(",");
			for (String delById : id) {
	  			Warehousing entity=warehousingDao.get(delById);
	  			entity.setState("99");//删除
	  			//删除物资和订单中间表
	  			List<GoodsAndOrder> list=entity.getGoodsAndOrder();
	  			for (GoodsAndOrder goodsAndOrder : list) {
					goodsAndOrder.setStatus("99");
					Goods g=goodsAndOrder.getGoods();
					g.setState("99");
				}
				entity.setGoodsAndOrder(list);
				warehousingDao.save(entity);
			}
	}
	public void deleteWarehoesingByGoodsId(String[] goodsId){
		  for (String id : goodsId) {
			  if(StringUtil.isNotEmpty(id)){
					//删除物资表
					Goods goods=goodsDao.get(id);
					goodsDao.remove(goods);
					//删除物资和订单中间表
					GoodsAndOrder goodsAndOrder=goodsAndOrderDao.getGoodsAndOrderByOrderIdAndGoodsId(goods.getFk_warehousing_id(),goods.getId());
					goodsAndOrderDao.remove(goodsAndOrder);
					}
		}
			
	}
	
	public List<Map<String, String>> getGoodsTypeByOrderId(String rkId,String orgId){
		return goodsDao.getGoodsTypeByOrderId(rkId,orgId);
	}
	
	public List<Warehousing> getWarehousingByFybxId(String fybxId){
		return warehousingDao.getWarehousingByFybxId(fybxId);
	}
	/**
	 * 导出入库单
	 * @param id
	 * @param orgId
	 * @return
	 */
	public HSSFWorkbook outRk(String id,String orgId){
		HSSFWorkbook workbook = new HSSFWorkbook();//创建工作薄
//        Warehousing bean=warehousingDao.get(id);
        //查询入库单的类型；
        List<Map<String, String>> list=this.getGoodsTypeByOrderId(id,orgId);
        if(!list.isEmpty()){
        	 for (int i = 0; i < list.size(); i++) {
             	Map<String, String> map=list.get(i);
                 HSSFSheet sheet = workbook.createSheet();// 创建一个Excel的Sheet

                 workbook.setRepeatingRowsAndColumns(i, 0, 7, 0, 1);//设置重复打印表头
                 HSSFPrintSetup printSetup = sheet.getPrintSetup();
                 printSetup.setLandscape(true);//设置横向打印
                 printSetup.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);        //A4纸张打印
                 printSetup.setScale((short) 100);//缩放比例
                 sheet.setMargin(HSSFSheet.BottomMargin,( double ) 2 );// 页边距（下）
                 sheet.setMargin(HSSFSheet.LeftMargin,( double ) 0.5 );// 页边距（左）
                 sheet.setMargin(HSSFSheet.RightMargin,( double ) 0.1 );// 页边距（右）
                 sheet.setMargin(HSSFSheet.TopMargin,( double ) 0.5 );// 页边距（上）
                 Footer footer = sheet.getFooter();
                 footer.setCenter( "第" + HeaderFooter.page() + "页，共 " + HeaderFooter.numPages()+"页" );
                 
                 
                 
                 
                 
                 workbook.setSheetName(i, map.get("LX_NAME"));//设置Sheet的名称
                 //设置列宽
                 sheet.setColumnWidth(0, 8000); //品名
                 sheet.setColumnWidth(1, 6000); //规格
                 sheet.setColumnWidth(2, 4000); //单位
                 sheet.setColumnWidth(3, 3000); //数量
                 sheet.setColumnWidth(4, 3000); //单价
                 sheet.setColumnWidth(5, 3000); //总金额
                 sheet.setColumnWidth(6, 3000); //发票号
                 sheet.setColumnWidth(7, 6000); //备注
               //获取样式
                 HSSFCellStyle cellstype1 = workbook.createCellStyle();
                 //居中对齐
                 cellstype1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                 cellstype1.setWrapText(true);
                 //设置字体
                 HSSFFont font = workbook.createFont();
                 font.setFontHeightInPoints((short) 20); // 字体高度
                 font.setFontName(" 黑体 "); // 字体
                 font.setBoldweight(Font.BOLDWEIGHT_BOLD);
                 cellstype1.setFont(font);
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
                 


                 HSSFCellStyle cellstype_header = workbook.createCellStyle();
                 //居中对齐
                 cellstype_header.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                 cellstype_header.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                 cellstype_header.setWrapText(true);
                 cellstype_header.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
                 cellstype_header.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
                 cellstype_header.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
                 cellstype_header.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
                 cellstype_header.setWrapText(true);
                 

                 HSSFCellStyle cellstype_header1 = workbook.createCellStyle();
                 //居左对齐
                 cellstype_header1.setAlignment(HSSFCellStyle.ALIGN_LEFT);
                 cellstype_header1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                 cellstype_header1.setWrapText(true);
                 
                 
                 //设置字体
                 HSSFFont font_header = workbook.createFont();
                 font_header.setFontHeightInPoints((short) 12); // 字体高度
                 font_header.setFontName(" 宋体 "); // 字体
//                 font_header.setBoldweight(Font.BOLDWEIGHT_BOLD);
                 cellstype_header.setFont(font_header);
                 cellstype_header1.setFont(font_header);
                 HSSFRow row0 = sheet.createRow(0); // 创建第一行
                 row0.setHeight((short)560);
                 HSSFCell cell10 = row0.createCell(0);
                 cell10.setCellValue(map.get("LX_NAME")+"入库单");
                 cell10.setCellStyle(cellstype1);
                 for (int j = 1; j < 8; j++) {
                     HSSFCell cell = row0.createCell(j);
                     cell.setCellStyle(cellstype1);
                 }
                 sheet.addMergedRegion(new CellRangeAddress(0, (short) 0, 0, (short) 7));

                 HSSFRow row1 = sheet.createRow(1); // 创建第2行
                 row1.setHeightInPoints(35);
                 HSSFCell cell0 = row1.createCell(0);
                 cell0.setCellValue("品名");
                 cell0.setCellStyle(cellstype_header);
                 HSSFCell cell1 = row1.createCell(1);
                 cell1.setCellValue("规格");
                 cell1.setCellStyle(cellstype_header);
                 HSSFCell cell2 = row1.createCell(2);
                 cell2.setCellValue("单位");
                 cell2.setCellStyle(cellstype_header);
                 HSSFCell cell3 = row1.createCell(3);
                 cell3.setCellValue("数量");
                 cell3.setCellStyle(cellstype_header);
                 HSSFCell cell4 = row1.createCell(4);
                 cell4.setCellValue("单价");
                 cell4.setCellStyle(cellstype_header);
                 HSSFCell cell5 = row1.createCell(5);
                 cell5.setCellValue("金额");
                 cell5.setCellStyle(cellstype_header);
                 HSSFCell cell6 = row1.createCell(6);
                 cell6.setCellValue("发票号");
                 cell6.setCellStyle(cellstype_header);
                 HSSFCell cell7 = row1.createCell(7);
                 cell7.setCellValue("备注");
                 cell7.setCellStyle(cellstype_header);

//                 List<Goods> goodslist= this.getgoodsByRkIdAndTypeId(bean.getId(), map.get("FK_GOODSTYPE_ID"));
                 List<Map<String, Object>> goodslist= warehousingDao.getGoods(id, map.get("ID"));
                 double money = 0;
                 double zj = 0;//总计
                 //查询数据列
                 for (int j = 0; j < goodslist.size(); j++) {
//              	   Goods gbean=goodslist.get(j);
                	 
                 	Map<String, Object> gmap=goodslist.get(j);
                 	Set<String> set = gmap.keySet();
                 	if(set != null && !set.isEmpty()) {
                 	for(String key : set) {
                 	if(gmap.get(key) == null) { gmap.put(key, ""); }
                 	}
                 	}
              	   HSSFRow row3 = sheet.createRow(sheet.getLastRowNum()+1);
              	   row3.setHeightInPoints(35);
             	   HSSFCell cell30 = row3.createCell(0);//列
             	   cell30.setCellValue(String.valueOf(gmap.get("WPMC")));
             	   cell30.setCellStyle(cellstype_header);
             	   HSSFCell cell31 = row3.createCell(1);//列
             	   cell31.setCellValue(String.valueOf(gmap.get("GGJXH")));
             	   cell31.setCellStyle(cellstype_header);
             	   HSSFCell cell32 = row3.createCell(2);//列
             	   cell32.setCellValue(String.valueOf(gmap.get("DW")));
             	   cell32.setCellStyle(cellstype_header);
             	   HSSFCell cell33 = row3.createCell(3);//列
             	   cell33.setCellValue(String.valueOf(gmap.get("CSSL")));
             	   cell33.setCellStyle(cellstype_header);
             	   HSSFCell cell34 = row3.createCell(4);//列
             	   double d1= Double.parseDouble(String.valueOf(gmap.get("JE")));
        	       BigDecimal md1 = new BigDecimal(d1);
        	       md1.setScale(2,BigDecimal.ROUND_HALF_UP);
             	   cell34.setCellValue(md1.doubleValue());
             	   cell34.setCellStyle(cellstype_header);
             	   HSSFCell cell35 = row3.createCell(5);//列
             	   
             	  
               	 BigDecimal md2 = new BigDecimal(Float.valueOf(String.valueOf(gmap.get("CSSL"))));
               	 BigDecimal m=md1.multiply(md2);//单价乘数量
               	 m.setScale(2,BigDecimal.ROUND_HALF_UP);
             	   money=money+m.doubleValue();
             	   zj=zj+m.doubleValue();
             	   cell35.setCellValue(m.doubleValue());//总金额
             	   cell35.setCellStyle(cellstype_header);
             	   HSSFCell cell37 = row3.createCell(7);//列
             	  cell37.setCellValue(String.valueOf(gmap.get("GBZ")));
             	 cell37.setCellStyle(cellstype_header);
             	   HSSFCell cell36 = row3.createCell(6);//列
             	   cell36.setCellValue(String.valueOf(gmap.get("FPH")));
             	   cell36.setCellStyle(cellstype_header);
             	   int size=j+1;
             	   if((size%8==0) || size==goodslist.size() ){
             		  HSSFRow row6 = sheet.createRow(sheet.getLastRowNum()+1);
                      row6.setHeightInPoints(35);
                 	    HSSFCell cell60 = row6.createCell(0);//列
                 	    cell60.setCellValue("合计");
                 	    cell60.setCellStyle(cellstype_header);
                 	    HSSFCell cell61 = row6.createCell(1);//列
                 	    BigDecimal sumMoney = new BigDecimal(money);
                 	    cell61.setCellValue(sumMoney.doubleValue());
                 	    cell61.setCellStyle(cellstype_header);
                 	    sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum(),sheet.getLastRowNum(), 1, 5));//起始行号，终止行号， 起始列号，终止列号
                 	   for (int y = 2; y < 8; y++) {
                           HSSFCell cell62 = row6.createCell(y);
                           cell62.setCellStyle(cellstype_header);
                       }
                	   money=0.00;
                   if(j==(goodslist.size()-1)){
                	   HSSFRow rowzj6 = sheet.createRow(sheet.getLastRowNum()+1);
                	   rowzj6.setHeightInPoints(35);
                  	   HSSFCell cellzj60 = rowzj6.createCell(0);//列
                  	   cellzj60.setCellValue("总计");
                  	   cellzj60.setCellStyle(cellstype_header);
                  	   HSSFCell cellzj61 = rowzj6.createCell(1);//列
                	    BigDecimal zjMoney = new BigDecimal(zj);
                  	   cellzj61.setCellValue(zjMoney.doubleValue());
                  	   cellzj61.setCellStyle(cellstype_header);
                  	   sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum(),sheet.getLastRowNum(), 1, 5));//起始行号，终止行号， 起始列号，终止列号
                	  
              	    
                  	   for (int y = 2; y < 8; y++) {
                            HSSFCell cell62 = rowzj6.createCell(y);
                            cell62.setCellStyle(cellstype_header);
                        }
                	   
                	   HSSFRow row4 = sheet.createRow(sheet.getLastRowNum()+1);
                       row4.setHeightInPoints(35);
                	    HSSFCell cell40 = row4.createCell(0);//列
                	    cell40.setCellValue("总计大写");
                	    cell40.setCellStyle(cellstype_header);
                	    HSSFCell cell41 = row4.createCell(1);//列
                	    cell41.setCellValue(Number2CN.number2CNMontrayUnit(zjMoney));
                	    cell41.setCellStyle(cellstype_header);
                	    sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum(),sheet.getLastRowNum(), 1, 5));//起始行号，终止行号， 起始列号，终止列号
                	   for (int y = 2; y < 8; y++) {
                          HSSFCell cell = row4.createCell(y);
                          cell.setCellStyle(cellstype_header);
                      }
                   }
                	   
                   HSSFRow row5 = sheet.createRow(sheet.getLastRowNum()+1);
                   row5.setHeightInPoints(35);
               	   HSSFCell cell50 = row5.createCell(0);//列
               	   cell50.setCellValue("部门负责人：");
               	   cell50.setCellStyle(cellstype_header1);
               	   HSSFCell cell51 = row5.createCell(1);//列
               	   cell51.setCellStyle(cellstype_header1);
               	   HSSFCell cell52 = row5.createCell(2);//列
               	   cell52.setCellValue("验收：");
               	   cell52.setCellStyle(cellstype_header1);
               	   HSSFCell cell53 = row5.createCell(3);//列
               	   cell53.setCellStyle(cellstype_header1);
               	   HSSFCell cell54 = row5.createCell(4);//列
               	   cell54.setCellValue("采购：");
               	   cell54.setCellStyle(cellstype_header1);
               	   HSSFCell cell55 = row5.createCell(5);//列
               	   cell55.setCellStyle(cellstype_header1);
               	   HSSFCell cell56 = row5.createCell(6);//列
               	   cell56.setCellValue("库管：");
               	   cell56.setCellStyle(cellstype_header1);
             	   }
             	   
                 }
          	   
             }
             
        }
       
        
		return workbook;
	}
	public List<Zdbq> getZdbq(String org,String type){
		return zdbqManager.getZdbqList(org, type);
	}
	
	public HSSFWorkbook outTz(String rkId,CurrentSessionUser curUser,String startTime,String endTime){
		  HSSFWorkbook workbook = new HSSFWorkbook();//创建工作薄
		  HSSFSheet sheet = workbook.createSheet();// 创建一个Excel的Sheet
		  Footer footer = sheet.getFooter();
          footer.setCenter( "第" + HeaderFooter.page() + "页，共 " + HeaderFooter.numPages()+"页" );
          workbook.setSheetName(0, "存货管理台账");//设置Sheet的名称
          //设置列宽
          sheet.setColumnWidth(0, 2000); //序号
          sheet.setColumnWidth(1, 6000); //品名
          sheet.setColumnWidth(2, 4000); //规格及型号
          sheet.setColumnWidth(3, 3000); //单位
          sheet.setColumnWidth(4, 4000); //入库日期
          sheet.setColumnWidth(5, 3000); //数量（入库情况）
          sheet.setColumnWidth(6, 3000); //单价（入库情况）
          sheet.setColumnWidth(7, 3000); //金额（入库情况）
          sheet.setColumnWidth(8, 4000); //出库日期
          sheet.setColumnWidth(9, 3000); //数量(出库情况)
          sheet.setColumnWidth(10, 3000); //单价(出库情况)
          sheet.setColumnWidth(11, 3000); //金额(出库情况)
          sheet.setColumnWidth(12, 4000); //领用人(出库情况)
          sheet.setColumnWidth(13, 3000); //数量（结存）
          sheet.setColumnWidth(14, 3000); //金额（结存）
          sheet.setColumnWidth(15, 5000); //存放地点
          sheet.setColumnWidth(16, 5000); //备注
          //获取样式
          HSSFCellStyle cellstype1 = workbook.createCellStyle();
          //居中对齐
          cellstype1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
          cellstype1.setWrapText(true);
        //设置字体
          HSSFFont font = workbook.createFont();
          font.setFontHeightInPoints((short) 20); // 字体高度
          font.setFontName(" 黑体 "); // 字体
          font.setBoldweight(Font.BOLDWEIGHT_BOLD);
          cellstype1.setFont(font);
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
          cellstype_header.setFont(font_header);
          HSSFRow row0 = sheet.createRow(0); // 创建第一行
          row0.setHeight((short)560);
          HSSFCell cell01 = row0.createCell(0);
          cell01.setCellValue("使用部门："+curUser.getDepartment().getOrgName());
          cell01.setCellStyle(cellstype1);
          sheet.addMergedRegion(new CellRangeAddress(0, (short) 0, 0, (short) 3));//起始行号，终止行号， 起始列号，终止列号
          HSSFCell cell04 = row0.createCell(4);
          cell04.setCellValue("存货管理台账");
          cell04.setCellStyle(cellstype1);
          sheet.addMergedRegion(new CellRangeAddress(0, (short) 0, 4, (short) 12));//起始行号，终止行号， 起始列号，终止列号
          
          HSSFRow row1 = sheet.createRow(1); // 创建第二行
          row1.setHeightInPoints(20);
          HSSFCell cell10 = row1.createCell(0);
          cell10.setCellValue("序号");
          cell10.setCellStyle(cellstype_header);
          sheet.addMergedRegion(new CellRangeAddress(1, (short)2, 0, (short) 0));//起始行号，终止行号， 起始列号，终止列号
          HSSFCell cell11 = row1.createCell(1);
          cell11.setCellValue("品名");
          cell11.setCellStyle(cellstype_header);
          sheet.addMergedRegion(new CellRangeAddress(1, (short)2, 1, (short) 1));//起始行号，终止行号， 起始列号，终止列号
          HSSFCell cell12 = row1.createCell(2);
          cell12.setCellValue("规格型号");
          cell12.setCellStyle(cellstype_header);
          sheet.addMergedRegion(new CellRangeAddress(1, (short)2, 2, (short) 2));//起始行号，终止行号， 起始列号，终止列号
          HSSFCell cell13 = row1.createCell(3);
          cell13.setCellValue("单位");
          cell13.setCellStyle(cellstype_header);
          sheet.addMergedRegion(new CellRangeAddress(1, (short)2, 3, (short) 3));//起始行号，终止行号， 起始列号，终止列号
          HSSFCell cell14 = row1.createCell(4);
          cell14.setCellValue("入库情况");
          cell14.setCellStyle(cellstype_header);
          for(int i=5;i<=7;i++){
        	  HSSFCell cell15 = row1.createCell(i);
        	  cell15.setCellStyle(cellstype_header);
          }
          sheet.addMergedRegion(new CellRangeAddress(1, (short)1, 4, (short) 7));//起始行号，终止行号， 起始列号，终止列号
          HSSFCell cell15 = row1.createCell(8);
          cell15.setCellValue("出库情况");
          cell15.setCellStyle(cellstype_header);
          sheet.addMergedRegion(new CellRangeAddress(1, (short)1, 8, (short) 12));//起始行号，终止行号， 起始列号，终止列号
          for(int i=9;i<=12;i++){
        	  HSSFCell cell19 = row1.createCell(i);
        	  cell19.setCellStyle(cellstype_header);
          }
          HSSFCell cell16 = row1.createCell(13);
          cell16.setCellValue("结存");
          cell16.setCellStyle(cellstype_header);
          sheet.addMergedRegion(new CellRangeAddress(1, (short)1, 13, (short) 14));//起始行号，终止行号， 起始列号，终止列号
          HSSFCell cell114 = row1.createCell(14);
          cell114.setCellStyle(cellstype_header);
          HSSFCell cell17 = row1.createCell(15);
          cell17.setCellValue("存放地点");
          cell17.setCellStyle(cellstype_header);
          sheet.addMergedRegion(new CellRangeAddress(1, (short)2, 15, (short) 15));//起始行号，终止行号， 起始列号，终止列号
          HSSFCell cell18 = row1.createCell(16);
          cell18.setCellValue("备注");
          cell18.setCellStyle(cellstype_header);
          sheet.addMergedRegion(new CellRangeAddress(1, (short)2, 16, (short) 16));//起始行号，终止行号， 起始列号，终止列号
          
          HSSFRow row2 = sheet.createRow(2); // 创建第三行
          row2.setHeightInPoints(20);
          for (int i = 0; i <= 3; i++) {
        	  HSSFCell cell20 = row2.createCell(i);
        	  cell20.setCellStyle(cellstype_header);
          }
          HSSFCell cell24 = row2.createCell(4);
          cell24.setCellValue("入库日期");
          cell24.setCellStyle(cellstype_header);
          HSSFCell cell25 = row2.createCell(5);
          cell25.setCellValue("数量");
          cell25.setCellStyle(cellstype_header);
          HSSFCell cell26 = row2.createCell(6);
          cell26.setCellValue("单价");
          cell26.setCellStyle(cellstype_header);
          HSSFCell cell27 = row2.createCell(7);
          cell27.setCellValue("金额");
          cell27.setCellStyle(cellstype_header);
          HSSFCell cell28 = row2.createCell(8);
          cell28.setCellValue("出库日期");
          cell28.setCellStyle(cellstype_header);
          HSSFCell cell29 = row2.createCell(9);
          cell29.setCellValue("数量");
          cell29.setCellStyle(cellstype_header);
          HSSFCell cell210 = row2.createCell(10);
          cell210.setCellValue("单价");
          cell210.setCellStyle(cellstype_header);
          HSSFCell cell211 = row2.createCell(11);
          cell211.setCellValue("金额");
          cell211.setCellStyle(cellstype_header);
          HSSFCell cell212 = row2.createCell(12);
          cell212.setCellValue("领用人");
          cell212.setCellStyle(cellstype_header);
          HSSFCell cell213 = row2.createCell(13);
          cell213.setCellValue("数量");
          cell213.setCellStyle(cellstype_header);
          HSSFCell cell214 = row2.createCell(14);
          cell214.setCellValue("金额");
          cell214.setCellStyle(cellstype_header);
          HSSFCell cell215 = row2.createCell(15);
          cell215.setCellStyle(cellstype_header);
          HSSFCell cell216 = row2.createCell(16);
          cell216.setCellStyle(cellstype_header);
          //查询数据
//          List<Goods> entity=goodsDao.getgoodsByRkId(rkId,startTime,endTime);
          List<Map<String, Object>> tzdata=warehousingDao.getTzData(rkId, startTime, endTime,curUser.getDepartment().getId());
     	 for (Map<String, Object> map : tzdata) {
     		HSSFRow row3 = sheet.createRow(sheet.getLastRowNum()+1); // 创建数据行
     		 row3.setHeightInPoints(25);
        	 HSSFCell cell31 = row3.createCell(0);
        	 cell31.setCellValue(sheet.getLastRowNum()-2);
        	 cell31.setCellStyle(cellstype_header);
        	 HSSFCell cell32 = row3.createCell(1);
        	 cell32.setCellValue(String.valueOf(map.get("WPMC")));
        	 cell32.setCellStyle(cellstype_header);
        	 HSSFCell cell33= row3.createCell(2);
        	 cell33.setCellValue(String.valueOf(map.get("GGJXH")));
        	 cell33.setCellStyle(cellstype_header);
        	 HSSFCell cell34= row3.createCell(3);
        	 cell34.setCellValue(String.valueOf(map.get("DW")));
        	 cell34.setCellStyle(cellstype_header);
        	 HSSFCell cell35= row3.createCell(4);
        	 cell35.setCellValue(String.valueOf(map.get("RK_TIME")));
        	 cell35.setCellStyle(cellstype_header);
        	 HSSFCell cell36= row3.createCell(5);
        	 cell36.setCellValue(String.valueOf(map.get("CSSL")));
        	 cell36.setCellStyle(cellstype_header);
        	 HSSFCell cell37= row3.createCell(6);
        	 cell37.setCellValue(String.valueOf(map.get("JE")));
        	 cell37.setCellStyle(cellstype_header);
        	 HSSFCell cell38= row3.createCell(7);
        	 BigDecimal md1 = new BigDecimal(Float.valueOf(String.valueOf(map.get("JE"))));
        	 BigDecimal md2 = new BigDecimal(Float.valueOf(String.valueOf(map.get("CSSL"))));
        	 BigDecimal m=md1.multiply(md2);//单价乘数量 
        	 m.setScale(2,BigDecimal.ROUND_HALF_UP);//保留2位小数
        	 cell38.setCellValue(m.doubleValue());
        	 cell38.setCellStyle(cellstype_header);
        	 HSSFCell cell39 = row3.createCell(8);
			 cell39.setCellValue(String.valueOf(map.get("LQSJ")));
			 cell39.setCellStyle(cellstype_header);
			 HSSFCell cell310 = row3.createCell(9);

        	 BigDecimal md3 = new BigDecimal(Float.valueOf(String.valueOf(map.get("SJLQSL"))));
        	 BigDecimal m2=md1.multiply(md3);//单价乘数量 
        	 m2.setScale(2,BigDecimal.ROUND_HALF_UP);//保留2位小数
			 cell310.setCellValue(String.valueOf(map.get("SJLQSL")));
			 cell310.setCellStyle(cellstype_header);
			 HSSFCell cell311 = row3.createCell(10);
			 cell311.setCellValue(String.valueOf(map.get("JE")));
			 cell311.setCellStyle(cellstype_header);
			 HSSFCell cell312 = row3.createCell(11);
			 cell312.setCellValue(m2.doubleValue());
			 cell312.setCellStyle(cellstype_header);
			 HSSFCell cell313 = row3.createCell(12);
			 cell313.setCellValue(String.valueOf(map.get("LQR")));
			 cell313.setCellStyle(cellstype_header);
			 HSSFCell cell314 = row3.createCell(13);
			 BigDecimal m3=md2.subtract(md3);
			 m3.setScale(2,BigDecimal.ROUND_HALF_UP);//保留2位小数
			 cell314.setCellValue(m3.doubleValue());
			 cell314.setCellStyle(cellstype_header);
			 BigDecimal m4= m3.multiply(md1);
			 m4.setScale(2,BigDecimal.ROUND_HALF_UP);//保留2位小数
			 HSSFCell cell315 = row3.createCell(14);
			 cell315.setCellValue(m4.doubleValue());
			 cell315.setCellStyle(cellstype_header);
			 HSSFCell cell316 = row3.createCell(15);
			 cell316.setCellStyle(cellstype_header);
			 HSSFCell cell317 = row3.createCell(16);
			 BigDecimal md4 = new BigDecimal(Float.valueOf(String.valueOf(map.get("SL"))));
			 BigDecimal m5= md4.subtract(md3);
			 cell317.setCellValue("出库"+String.valueOf(map.get("SJLQSL"))+"退还"+m5.intValue());
			 cell317.setCellStyle(cellstype_header);
			 
			 
			 
			}
//          for (Goods goods : entity) {
//        	 HSSFRow row3 = sheet.createRow(sheet.getLastRowNum()+1); // 创建数据行
//        	 row3.setHeightInPoints(20);
//        	 HSSFCell cell31 = row3.createCell(0);
//        	 cell31.setCellValue(sheet.getLastRowNum()-2);
//        	 cell31.setCellStyle(cellstype_header);
//        	 HSSFCell cell32 = row3.createCell(1);
//        	 cell32.setCellValue(goods.getWpmc());
//        	 cell32.setCellStyle(cellstype_header);
//        	 HSSFCell cell33= row3.createCell(2);
//        	 cell33.setCellValue(goods.getGgjxh());
//        	 cell33.setCellStyle(cellstype_header);
//        	 HSSFCell cell34= row3.createCell(3);
//        	 cell34.setCellValue(goods.getDw());
//        	 cell34.setCellStyle(cellstype_header);
//        	 HSSFCell cell35= row3.createCell(4);
//        	 cell35.setCellValue(goods.getRk_time());
//        	 cell35.setCellStyle(cellstype_header);
//        	 HSSFCell cell36= row3.createCell(5);
//        	 cell36.setCellValue(goods.getCssl());
//        	 cell36.setCellStyle(cellstype_header);
//        	 HSSFCell cell37= row3.createCell(6);
//        	 cell37.setCellValue(goods.getJe());
//        	 cell37.setCellStyle(cellstype_header);
//        	 HSSFCell cell38= row3.createCell(7);
//        	 cell38.setCellValue(goods.getJe()*goods.getCssl());
//        	 cell38.setCellStyle(cellstype_header);
        	
        	 
//        	 List<Map<String, Object>> out=goodsDao.getOutGoodsById(goods.getId());
//        	 int o=out.size();//1
//        	 int a=sheet.getLastRowNum();//3
//        	 int start=sheet.getLastRowNum()-out.size()+1;

//        	 for (int i = 0; i < out.size(); i++) {
//        		 Map<String, Object> map=out.get(i);
//    			 BigDecimal md2 = new BigDecimal(Float.valueOf(String.valueOf(map.get("SJLQSL"))));
//    			 BigDecimal md1 = new BigDecimal(Float.valueOf(String.valueOf(map.get("JE"))));
//               	 BigDecimal m=md1.multiply(md2);//单价乘数量
//        		 if(start==sheet.getLastRowNum()){
//        			 HSSFCell cell39 = row3.createCell(8);
//        			 cell39.setCellValue(String.valueOf(map.get("LQSJ")));
//        			 cell39.setCellStyle(cellstype_header);
//        			 HSSFCell cell310 = row3.createCell(9);
//        			 cell310.setCellValue(String.valueOf(map.get("SJLQSL")));
//        			 cell310.setCellStyle(cellstype_header);
//        			 HSSFCell cell311 = row3.createCell(10);
//        			 cell311.setCellValue(String.valueOf(map.get("JE")));
//        			 cell311.setCellStyle(cellstype_header);
//        			 HSSFCell cell312 = row3.createCell(11);
//        			 cell312.setCellValue(m.doubleValue());
//        			 cell312.setCellStyle(cellstype_header);
//        			 HSSFCell cell313 = row3.createCell(12);
//        			 cell313.setCellValue(String.valueOf(map.get("LQR")));
//        			 cell313.setCellStyle(cellstype_header);
//        			 HSSFCell cell314 = row3.createCell(13);
//        			 cell314.setCellValue(String.valueOf(map.get("	")));
//        			 cell314.setCellStyle(cellstype_header);
//        		 }else{
//        			 HSSFRow row4 = sheet.createRow(sheet.getLastRowNum()); // 创建数据行
//        			 row4.setHeightInPoints(20);
//        			 HSSFCell cell39 = row4.createCell(8);
//        			 cell39.setCellValue(String.valueOf(map.get("LQSJ")));
//        			 cell39.setCellStyle(cellstype_header);
//        		 }
//        		 if(i==out.size()-1){
//        			 for (int j = 0; j <= 7; j++) {
//        				 sheet.addMergedRegion(new CellRangeAddress(start, sheet.getLastRowNum(),j, (short) j));//起始行号，终止行号， 起始列号，终止列号
//        				 
//					}
//        			 
//        		 }
//        		 
//			}
        	 //
//          }
          
          
          
          
		  return workbook;
	}
	
	public HSSFWorkbook outYsb(String rkId,CurrentSessionUser curUser,String tel){
//		Warehousing bean= this.get(rkId);
        //查询入库单的类型；
        List<Map<String, String>> list=this.getGoodsTypeByOrderId(rkId,curUser.getDepartment().getId());

        HSSFWorkbook workbook = new HSSFWorkbook();//创建工作薄
        
        for (int i = 0; i < list.size(); i++) {
        	Map<String, String> map=list.get(i);
            HSSFSheet sheet = workbook.createSheet();// 创建一个Excel的Sheet
            workbook.setRepeatingRowsAndColumns(i, 0, 8, 0, 5);//设置重复打印表头
            HSSFPrintSetup printSetup = sheet.getPrintSetup();
            printSetup.setLandscape(true);//设置横向打印
            printSetup.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);        //A4纸张打印
            printSetup.setScale((short) 80);//缩放比例
            sheet.setMargin(HSSFSheet.BottomMargin,( double ) 2 );// 页边距（下）
            sheet.setMargin(HSSFSheet.LeftMargin,( double ) 0.5 );// 页边距（左）
            sheet.setMargin(HSSFSheet.RightMargin,( double ) 0.1 );// 页边距（右）
            sheet.setMargin(HSSFSheet.TopMargin,( double ) 0.5 );// 页边距（上）
            Footer footer = sheet.getFooter();
            footer.setCenter( "第" + HeaderFooter.page() + "页，共 " + HeaderFooter.numPages()+"页" );
            
            
            
            workbook.setSheetName(i, map.get("LX_NAME"));//设置Sheet的名称
            //设置列宽
            sheet.setColumnWidth(0, 8000); //产品名称
            sheet.setColumnWidth(1, 8000); //供应商
            sheet.setColumnWidth(2, 6000); //规格及型号
            sheet.setColumnWidth(3, 2000); //单位
            sheet.setColumnWidth(4, 2000); //数量
            sheet.setColumnWidth(5, 3500); //单价
            sheet.setColumnWidth(6, 3500); //总金额
            sheet.setColumnWidth(7, 5000); //使用部门
            sheet.setColumnWidth(8, 5000); //验收人签字
            //获取样式
            HSSFCellStyle cellstype1 = workbook.createCellStyle();
            //居中对齐
            cellstype1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cellstype1.setWrapText(true);
//            cellstype1.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
            //设置字体
            HSSFFont font = workbook.createFont();
            font.setFontHeightInPoints((short) 20); // 字体高度
            font.setFontName(" 黑体 "); // 字体
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            cellstype1.setFont(font);
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
//            font_header.setBoldweight(Font.BOLDWEIGHT_BOLD);
            cellstype_header.setFont(font_header);
            HSSFRow row0 = sheet.createRow(0); // 创建第一行
            row0.setHeight((short)560);
            HSSFCell cell10 = row0.createCell(0);
            cell10.setCellValue(map.get("LX_NAME")+"验收表");
            cell10.setCellStyle(cellstype1);
            for (int j = 1; j < 9; j++) {
                HSSFCell cell = row0.createCell(j);
                cell.setCellStyle(cellstype1);
            }
            sheet.addMergedRegion(new CellRangeAddress(0, (short) 0, 0, (short) 8));
            
            
            HSSFRow row1 = sheet.createRow(1); // 创建第2行
            row1.setHeightInPoints(35);
            HSSFCell cell0 = row1.createCell(0);
            cell0.setCellValue("收货单位");
            cell0.setCellStyle(cellstype_header);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 8));//起始行号，终止行号， 起始列号，终止列号
            HSSFCell cell1 = row1.createCell(1);
            cell1.setCellValue("四川省特种设备检验研究院");//bean.getShdw()
            cell1.setCellStyle(cellstype_header);
            
            
            
            HSSFRow row2 = sheet.createRow(2); // 创建第3行
            row2.setHeightInPoints(35);
            HSSFCell cell00 = row2.createCell(0);
            cell00.setCellValue("地址");
            cell00.setCellStyle(cellstype_header);
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 1, 8));//起始行号，终止行号， 起始列号，终止列号
            HSSFCell cell01 = row2.createCell(1);
            cell01.setCellValue("成都市东风路二段北二巷4号");//bean.getDz()
            cell01.setCellStyle(cellstype_header);
            for (int j = 2; j < 9; j++) {
                HSSFCell cell = row1.createCell(j);
                cell.setCellStyle(cellstype_header);
                HSSFCell cell2 = row2.createCell(j);
                cell2.setCellStyle(cellstype_header);
            }
            
            HSSFRow row3 = sheet.createRow(3); // 创建第4行
            row3.setHeightInPoints(35);
            HSSFCell cell30 = row3.createCell(0);//列
            cell30.setCellValue("联系人姓名");
            cell30.setCellStyle(cellstype_header);
            
            
            sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 2));//起始行号，终止行号， 起始列号，终止列号
            HSSFCell cell31 = row3.createCell(3);
            cell31.setCellValue("联系人部门");
            cell31.setCellStyle(cellstype_header);
            sheet.addMergedRegion(new CellRangeAddress(3, 3, 3, 5));//起始行号，终止行号， 起始列号，终止列号
            HSSFCell cell32 = row3.createCell(6);
            cell32.setCellValue("电话");
            cell32.setCellStyle(cellstype_header);
            sheet.addMergedRegion(new CellRangeAddress(3, 3, 6, 8));//起始行号，终止行号， 起始列号，终止列号
            

            
            HSSFRow row4 = sheet.createRow(4); // 创建第5行
            row4.setHeightInPoints(35);
            HSSFCell cell40 = row4.createCell(0);//列
            cell40.setCellValue(curUser.getUserName());
            cell40.setCellStyle(cellstype_header);
            sheet.addMergedRegion(new CellRangeAddress(4, 4, 0, 2));//起始行号，终止行号， 起始列号，终止列号终止列号
            HSSFCell cell41 = row4.createCell(3);
            cell41.setCellValue(curUser.getDepartment().getOrgName());
            cell41.setCellStyle(cellstype_header);
            sheet.addMergedRegion(new CellRangeAddress(4, 4, 3, 5));//起始行号，终止行号， 起始列号，终止列号
            HSSFCell cell42 = row4.createCell(6);
            
            cell42.setCellValue(tel);
            cell42.setCellStyle(cellstype_header);
            sheet.addMergedRegion(new CellRangeAddress(4, 4, 6, 8));//起始行号，终止行号， 起始列号，终止列号
            
            for (int j = 1; j < 3; j++) {
                HSSFCell cell = row3.createCell(j);
                cell.setCellStyle(cellstype_header);
                HSSFCell cell4 = row4.createCell(j);
                cell4.setCellStyle(cellstype_header);
            }
            for (int j = 4; j < 6; j++) {
                HSSFCell cell = row3.createCell(j);
                cell.setCellStyle(cellstype_header);
                HSSFCell cell4 = row4.createCell(j);
                cell4.setCellStyle(cellstype_header);
            }for (int j = 7; j < 9; j++) {
                HSSFCell cell = row3.createCell(j);
                cell.setCellStyle(cellstype_header);
                HSSFCell cell4 = row4.createCell(j);
                cell4.setCellStyle(cellstype_header);
            }
            
            HSSFRow row5 = sheet.createRow(5); // 创建第6行
            row5.setHeightInPoints(35);
            String [] title={"产品名称","供应商","规格型号","单位","数量","单价（元）","金额（元）","使用部门","验收人签字"};
            for (int j = 0; j < title.length; j++) {
            	HSSFCell cell50 = row5.createCell(j);//列
                cell50.setCellValue(title[j]);
                cell50.setCellStyle(cellstype_header);
			}

           List<Map<String, Object>> goodslist= warehousingDao.getYsGoods(rkId,map.get("ID"));//
           Double num=0.00;
           Double zje=0.00;
           Double zj=0.00;//总计
           
           //.setRepeatingRowsAndColumns(i, 0, 8, 6+goodslist.size(), 7+goodslist.size());
           //查询数据列
           for (int j = 0; j < goodslist.size(); j++) {
               Map<String, Object> gbean=goodslist.get(j);
          	 Set<String> set = gbean.keySet();
           	 if(set != null && !set.isEmpty()) {
           	 for(String key : set) {
           	 if(gbean.get(key) == null) { gbean.put(key, ""); }
           	 }
           	 }
            	   BigDecimal b1 = new BigDecimal(num);
            	   BigDecimal b2 = new BigDecimal(Float.parseFloat(gbean.get("SL").toString()));
            	   num=b1.add(b2).doubleValue();
            	   double dje=Double.parseDouble(gbean.get("JE").toString());
            	   zje=zje+dje*Float.parseFloat(gbean.get("SL").toString());
            	   zj=zj+dje*Float.parseFloat(gbean.get("SL").toString());
            	   HSSFRow row6 = sheet.createRow(sheet.getLastRowNum()+1);
            	   row6.setHeightInPoints(35);
            	   HSSFCell cell60 = row6.createCell(0);//列
            	   cell60.setCellValue(gbean.get("WPMC").toString());
            	   cell60.setCellStyle(cellstype_header);
            	   HSSFCell cell61 = row6.createCell(1);//列
            	   cell61.setCellValue(gbean.get("GYSMC").toString());
            	   cell61.setCellStyle(cellstype_header);
            	   HSSFCell cell62 = row6.createCell(2);//列
            	   cell62.setCellValue(gbean.get("GGJXH").toString());
            	   cell62.setCellStyle(cellstype_header);
            	   HSSFCell cell63 = row6.createCell(3);//列
            	   cell63.setCellValue(gbean.get("DW").toString());
            	   cell63.setCellStyle(cellstype_header);
            	   HSSFCell cell64 = row6.createCell(4);//列
            	   cell64.setCellValue(gbean.get("SL").toString());
            	   cell64.setCellStyle(cellstype_header);
            	   HSSFCell cell65 = row6.createCell(5);//列
            	   cell65.setCellValue(gbean.get("JE").toString());
            	   cell65.setCellStyle(cellstype_header);
            	   HSSFCell cell66 = row6.createCell(6);//列
            	   cell66.setCellValue(dje*Float.parseFloat(gbean.get("SL").toString()));
            	   cell66.setCellStyle(cellstype_header);
            	   HSSFCell cell67 = row6.createCell(7);//列
            	   cell67.setCellValue(gbean.get("LQBM").toString());//使用部门
            	   cell67.setCellStyle(cellstype_header);
            	   HSSFCell cell68 = row6.createCell(8);//列
            	   cell68.setCellValue("");
            	   cell68.setCellStyle(cellstype_header);
          		    int size=j+1;
               	   if(size%7==0 || size==goodslist.size()){
               		  HSSFRow row7 = sheet.createRow(sheet.getLastRowNum()+1);
                      row7.setHeightInPoints(35);
                 	  HSSFCell cell70 = row7.createCell(0);//列
                 	   cell70.setCellValue("合计");
                 	   cell70.setCellStyle(cellstype_header);
             
                 	   HSSFCell cell71 = row7.createCell(1);//列
                 	   cell71.setCellStyle(cellstype_header);
                 	   HSSFCell cell72 = row7.createCell(2);//列
                 	   cell72.setCellStyle(cellstype_header);
                 	   HSSFCell cell73 = row7.createCell(3);//列
                 	   cell73.setCellStyle(cellstype_header);
                 	   HSSFCell cell77 = row7.createCell(7);//列
                 	   cell77.setCellStyle(cellstype_header);
                 	   HSSFCell cell78 = row7.createCell(8);//列
                 	   cell78.setCellStyle(cellstype_header);
                 	   
                 	   
                 	   HSSFCell cell74 = row7.createCell(4);//列
                 	   cell74.setCellStyle(cellstype_header);
                 	   HSSFCell cell75 = row7.createCell(5);//列
                 	   cell75.setCellStyle(cellstype_header);
                 	   HSSFCell cell76 = row7.createCell(6);//列
                 	   cell76.setCellValue(zje);
                 	   cell76.setCellStyle(cellstype_header);
                 	   
                 	  zje=0.00;
                 	  
                 	  if(size==goodslist.size()){
                 		  HSSFRow rowzj7=sheet.createRow(sheet.getLastRowNum()+1);
                 		  rowzj7.setHeightInPoints(35);
			              HSSFCell cellzj70 = rowzj7.createCell(0);//列
			              cellzj70.setCellValue("总计");
			              cellzj70.setCellStyle(cellstype_header);
			              for (int y = 1; y < 6; y++) {
			            	  HSSFCell cell = rowzj7.createCell(y);
		                      cell.setCellStyle(cellstype_header);
			              }
			               	HSSFCell cellzj76 = rowzj7.createCell(6);//列
			               	cellzj76.setCellValue(zj);
			               	cellzj76.setCellStyle(cellstype_header);
			               	HSSFCell cellzj77 = rowzj7.createCell(7);//列
			               	cellzj77.setCellStyle(cellstype_header);
			               	HSSFCell cellzj78 = rowzj7.createCell(8);//列
			               	cellzj78.setCellStyle(cellstype_header);
                 	  }
                 	  
                 	   //最后一行
             
                        HSSFRow row8 = sheet.createRow(sheet.getLastRowNum()+1);
                        row8.setHeightInPoints(35);
                 	   HSSFCell cell80 = row8.createCell(0);//列
                 	   cell80.setCellValue("经办人：");
                 	   cell80.setCellStyle(cellstype_header);
                 	   HSSFCell cell81 = row8.createCell(1);//列
                 	   cell81.setCellValue("");
                 	   cell81.setCellStyle(cellstype_header);
                        sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum(), sheet.getLastRowNum(), 1, 4));//起始行号，终止行号， 起始列号，终止列号
                 	   for (int y = 2; y < 5; y++) {
                            HSSFCell cell = row8.createCell(y);
                            cell.setCellStyle(cellstype_header);
                        }
             
                 	   HSSFCell cell85 = row8.createCell(5);//列
                 	   cell85.setCellValue("时间：");
                 	   cell85.setCellStyle(cellstype_header);
                 	   sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum(),sheet.getLastRowNum(), 6, 8));//起始行号，终止行号， 起始列号，终止列号
                 	   for (int y = 6; y < 9; y++) {
                            HSSFCell cell = row8.createCell(y);
                            cell.setCellStyle(cellstype_header);
                        }
               	   }
            	   
           }
           
		}
       
        
        return workbook;
	}
	
	/**
	 * 是否可以出库
	 * @param goodsId
	 * @return
	 */
	public Boolean sfOut(String goodsId){
		boolean ck=true;//已经出库
		if(StringUtil.isNotEmpty(goodsId)){
			Goods goods=goodsDao.get(goodsId);
			Float f=goods.getCssl()-goods.getSl();
			if(f==0){
				ck= false;//没有出库
			}
		}
		return ck;
	}
	/**
	 * 根据入库单号查询
	 * @param rkbh
	 * @return
	 */
	public List<Warehousing> getWarehousingByRkBh(String[] rkbh){
		return warehousingDao.getWarehousingByRkBh(rkbh);
	}
}
