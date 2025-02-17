package com.lsts.finance.service;



import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.khnt.base.Factory;
import com.khnt.base.SysParaInf;
import com.khnt.core.crud.manager.impl.EntityManageImpl;
import com.khnt.core.crud.web.support.JsonWrapper;
import com.khnt.pub.fileupload.service.AttachmentManager;
import com.khnt.security.CurrentSessionUser;
import com.lsts.finance.bean.ImportSalary;
import com.lsts.finance.bean.Salary;
import com.lsts.finance.dao.ImportSalaryDao;
import com.lsts.finance.dao.SalaryDao;
import com.lsts.humanresources.dao.EmployeeBaseDao;



/**
 * 实体Manager，继承自泛型类EntityManageImpl，同时声明泛型的运行时bean和dao为Demo,DemoDao。<br/>
 * 通过这样的继承方式，自动获得了对Demo实体的crud操作<br/>
 * 注解@Service声明了该类为一个spring对象
 */
@Service("salaryManager")
public class SalaryManager extends EntityManageImpl<Salary, SalaryDao> {
	private DataFormatter formatter;
	private FormulaEvaluator evaluator;
	// 必须提供Demo实体的dao对象，使用注解@Autowired标识为自动装配
	@Autowired
	private SalaryDao salaryDao;
	@Autowired
	private EmployeeBaseDao employeeBaseDao;
	@Autowired
	private ImportSalaryDao importSalaryDao;
	@Autowired
	private AttachmentManager attachmentManager;
	    
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    /**
		 * 保存导入的财务数据
		 * 
		 * @param file
		 * @return
	     * @throws ParseException 
		 */
		public String saveBankData(String imid ,String files, CurrentSessionUser user) throws ParseException {
			JSONArray array = JSONArray.fromObject(files);
			String repData="";
			String[] fileName = new String[array.length()];// 文件名
			String[] filePath = new String[array.length()];// 文件路径
//			String[] result= new String[array.length()];
			for (int i = 0; i < array.length(); i++) {
				fileName[i] = array.getJSONObject(i).getString("name");
				filePath[i] = attachmentManager.download(array.getJSONObject(i).getString("id")).getFilePath();
				repData = repData + saveDate( imid,fileName[i], filePath[i], user);
			}
			return repData;
		}

		//联级删除
		public HashMap<String, Object> deleteimid(String imid) throws ParseException {
			
			List<Salary> message =	salaryDao.getimId(imid);
			for(int i=0;i<message.size();i++){
				String im = message.get(i).getId();
				
				
				Session session = salaryDao.getHibernateTemplate().getSessionFactory()
						.getCurrentSession();
				for(String id:im.split(",")){
					Salary ywhbsgz = salaryDao.get(id);
					session.delete(ywhbsgz);
				
				}
				 JsonWrapper.successWrapper();
			}
			
			return JsonWrapper.successWrapper();
		}
		/**
		 *  导出查询数据
		 */
		
public List<Salary> getsal(String imid) throws ParseException {
			
			List<Salary> message =	salaryDao.getimId(imid);
			return message;
			
}
		
		/**
		 * 根据导入的文件名
		 * 
		 * @param file
		 * @throws ParseException 
		 * @throws IOException
		 */
		public String saveDate(String imid ,String fileName, String filePath, CurrentSessionUser user) throws ParseException {
			String repData="";
			/* fileName = this.getSystemFilePath()+File.separator+filePath; */
			File bankfile = new File(filePath); // 创建文件对象  
			Workbook bankWb=null;
			try {
				bankWb = WorkbookFactory.create(bankfile);
			} catch (InvalidFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    Sheet bankSheet = bankWb.getSheetAt(0);//获得sheet
		    
		    List<ImportSalary> imp =   importSalaryDao.getim(imid);
		    
		    double hj=0;
		    for (int i=5;i<=bankSheet.getLastRowNum();i++) {
		    	Row row = bankSheet.getRow(i);//行
		    	if(row!=null && !"null".equals(row)) {
		    		if(row.getCell(1)!=null && !"null".equals(row.getCell(1))) {
			    		Salary cwBankDetail = new Salary();
			    		//去除空盒
			    		String name = row.getCell(1).getStringCellValue();
			    		String names = name.replace(" ", "");
			    		String mobilePhones="";
			    		if(row.getCell(2)!=null){
   	   	    				if(row.getCell(2).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
	   	   	    				DecimalFormat format = new DecimalFormat("#");  
	   	   	    				Number number = row.getCell(2).getNumericCellValue();  
	   	   	    				mobilePhones = format.format(number);
   	   	    				}else if(row.getCell(2).getCellType()==HSSFCell.CELL_TYPE_STRING){
   	   	    					mobilePhones = row.getCell(2).getStringCellValue().replace(" ", "");
   	   	    				}
   	   	    			}
			    		System.out.println(name);
			    		String empId="";
						try {
							empId = employeeBaseDao.getEmpId(names, mobilePhones);
							cwBankDetail.setFkEmployeeBaseId(empId);
				    		cwBankDetail.setName(names);//姓名
				    		cwBankDetail.setTelePhone(mobilePhones);//手机号
					    	cwBankDetail.setCreaterId(user.getId());
					    	cwBankDetail.setCreaterName(user.getName());
					    	if(row.getCell(3)!=null) {
					    		if(row.getCell(3).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
	   	   	    					cwBankDetail.setJbgzGwgz(new BigDecimal(Float.toString((float)(Math.round(row.getCell(3).getNumericCellValue()*100))/100)));//基本工资岗位工资
	   	   	    				}else if(row.getCell(3).getCellType()==HSSFCell.CELL_TYPE_STRING){
	   	   	    					cwBankDetail.setJbgzGwgz(new BigDecimal(row.getCell(3).getStringCellValue()).setScale(2, BigDecimal.ROUND_HALF_UP));//基本工资岗位工资
	   	   	    				}else if(row.getCell(3).getCellType()==HSSFCell.CELL_TYPE_FORMULA) {
	   	   	    					cwBankDetail.setJbgzGwgz(new BigDecimal(Float.toString((float)(Math.round(row.getCell(3).getNumericCellValue()*100))/100)));//基本工资岗位工资
	   	   	    				}else if(row.getCell(3).getCellType()==HSSFCell.CELL_TYPE_BLANK) {
	   	   	    					cwBankDetail.setJbgzGwgz(null);//基本工资岗位工资
	   	   	    				}
					    	}
					    	System.out.println("基本工资岗位工资:"+row.getCell(3));
					    	
					    	if(row.getCell(4)!=null) {
					    		if(row.getCell(4).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
	   	   	    					cwBankDetail.setJbgzXjgz(new BigDecimal(Float.toString((float)(Math.round(row.getCell(4).getNumericCellValue()*100))/100)));//基本工资薪级工资
	   	   	    				}else if(row.getCell(4).getCellType()==HSSFCell.CELL_TYPE_STRING){
	   	   	    					cwBankDetail.setJbgzXjgz(new BigDecimal(row.getCell(4).getStringCellValue()).setScale(2, BigDecimal.ROUND_HALF_UP));//基本工资薪级工资
	   	   	    				}else if(row.getCell(4).getCellType()==HSSFCell.CELL_TYPE_FORMULA){
	   	   	    					cwBankDetail.setJbgzXjgz(new BigDecimal(Float.toString((float)(Math.round(row.getCell(4).getNumericCellValue()*100))/100)));//基本工资薪级工资
	   	   	    				}else if(row.getCell(4).getCellType()==HSSFCell.CELL_TYPE_BLANK){
	   	   	    					cwBankDetail.setJbgzXjgz(null);//基本工资薪级工资
	   	   	    				}
					    	}
					    	System.out.println("基本工资薪级工资:"+row.getCell(4));
					    	
					    	if(row.getCell(5)!=null) {
					    		if(row.getCell(5).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
	   	   	    					cwBankDetail.setJbgzBlbt(new BigDecimal(Float.toString((float)(Math.round(row.getCell(5).getNumericCellValue()*100))/100)));//基本工资保留补贴
	   	   	    				}else if(row.getCell(5).getCellType()==HSSFCell.CELL_TYPE_STRING){
	   	   	    					cwBankDetail.setJbgzBlbt(new BigDecimal(row.getCell(5).getStringCellValue()).setScale(2, BigDecimal.ROUND_HALF_UP));//基本工资保留补贴
	   	   	    				}else if(row.getCell(5).getCellType()==HSSFCell.CELL_TYPE_FORMULA){
	   	   	    					cwBankDetail.setJbgzBlbt(new BigDecimal(Float.toString((float)(Math.round(row.getCell(5).getNumericCellValue()*100))/100)));//基本工资保留补贴
	   	   	    				}else if(row.getCell(5).getCellType()==HSSFCell.CELL_TYPE_BLANK){
	   	   	    					cwBankDetail.setJbgzBlbt(null);//基本工资保留补贴
	   	   	    				}
					    	}
					    	System.out.println("基本工资保留补贴:"+row.getCell(5));
					    	
					    	if(row.getCell(6)!=null) {
					    		if(row.getCell(6).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
	   	   	    					cwBankDetail.setJbgzDz(new BigDecimal(Float.toString((float)(Math.round(row.getCell(6).getNumericCellValue()*100))/100)));//基本工资独子
	   	   	    				}else if(row.getCell(6).getCellType()==HSSFCell.CELL_TYPE_STRING){
	   	   	    					cwBankDetail.setJbgzDz(new BigDecimal(row.getCell(6).getStringCellValue()).setScale(2, BigDecimal.ROUND_HALF_UP));//基本工资独子
	   	   	    				}else if(row.getCell(6).getCellType()==HSSFCell.CELL_TYPE_FORMULA){
	   	   	    					cwBankDetail.setJbgzDz(new BigDecimal(Float.toString((float)(Math.round(row.getCell(6).getNumericCellValue()*100))/100)));//基本工资独子
	   	   	    				}else if(row.getCell(6).getCellType()==HSSFCell.CELL_TYPE_BLANK){
	   	   	    					cwBankDetail.setJbgzDz(null);//基本工资独子
	   	   	    				}
					    	}
					    	System.out.println("基本工资独子:"+row.getCell(6));
					    	
					    	if(row.getCell(7)!=null) {
					    		if(row.getCell(7).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
	   	   	    					cwBankDetail.setJbgzLt(new BigDecimal(Float.toString((float)(Math.round(row.getCell(7).getNumericCellValue()*100))/100)));//基本工资粮贴
	   	   	    				}else if(row.getCell(7).getCellType()==HSSFCell.CELL_TYPE_STRING){
	   	   	    					cwBankDetail.setJbgzLt(new BigDecimal(row.getCell(7).getStringCellValue()).setScale(2, BigDecimal.ROUND_HALF_UP));//基本工资粮贴
	   	   	    				}else if(row.getCell(7).getCellType()==HSSFCell.CELL_TYPE_FORMULA){
	   	   	    					cwBankDetail.setJbgzLt(new BigDecimal(Float.toString((float)(Math.round(row.getCell(7).getNumericCellValue()*100))/100)));//基本工资粮贴
	   	   	    				}else if(row.getCell(7).getCellType()==HSSFCell.CELL_TYPE_BLANK){
	   	   	    					cwBankDetail.setJbgzLt(null);//基本工资粮贴
	   	   	    				}
					    	}
					    	System.out.println("基本工资粮贴:"+row.getCell(7));
					    	
					    	if(row.getCell(8)!=null) {
					    		if(row.getCell(8).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
	   	   	    					cwBankDetail.setJbgzBfx(new BigDecimal(Float.toString((float)(Math.round(row.getCell(8).getNumericCellValue()*100))/100)));//基本工资补发项
	   	   	    				}else if(row.getCell(8).getCellType()==HSSFCell.CELL_TYPE_STRING){
	   	   	    					cwBankDetail.setJbgzBfx(new BigDecimal(row.getCell(8).getStringCellValue()).setScale(2, BigDecimal.ROUND_HALF_UP));//基本工资补发项
	   	   	    				}else if(row.getCell(8).getCellType()==HSSFCell.CELL_TYPE_FORMULA){
	   	   	    					cwBankDetail.setJbgzBfx(new BigDecimal(Float.toString((float)(Math.round(row.getCell(8).getNumericCellValue()*100))/100)));//基本工资补发项
	   	   	    				}else if(row.getCell(8).getCellType()==HSSFCell.CELL_TYPE_BLANK){
	   	   	    					cwBankDetail.setJbgzBfx(null);//基本工资补发项
	   	   	    				}
					    	}
					    	System.out.println("基本工资补发项:"+row.getCell(8));
					    	
					    	if(row.getCell(9)!=null) {
					    		if(row.getCell(9).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
	   	   	    					cwBankDetail.setJbgzXj(new BigDecimal(Float.toString((float)(Math.round(row.getCell(9).getNumericCellValue()*100))/100)));//基本工资小计
	   	   	    				}else if(row.getCell(9).getCellType()==HSSFCell.CELL_TYPE_STRING){
	   	   	    					cwBankDetail.setJbgzXj(new BigDecimal(row.getCell(9).getStringCellValue()).setScale(2, BigDecimal.ROUND_HALF_UP));//基本工资小计
	   	   	    				}else if(row.getCell(9).getCellType()==HSSFCell.CELL_TYPE_FORMULA){
	   	   	    					cwBankDetail.setJbgzXj(new BigDecimal(Float.toString((float)(Math.round(row.getCell(9).getNumericCellValue()*100))/100)));//基本工资小计
	   	   	    				}else if(row.getCell(9).getCellType()==HSSFCell.CELL_TYPE_BLANK){
	   	   	    					cwBankDetail.setJbgzXj(null);//基本工资小计
	   	   	    				}
					    	}
					    	System.out.println("基本工资小计:"+row.getCell(9));
					    	
					    	if(row.getCell(10)!=null) {
					    		if(row.getCell(10).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
	   	   	    					cwBankDetail.setJxgzJcjxMy(new BigDecimal(Float.toString((float)(Math.round(row.getCell(10).getNumericCellValue()*100))/100)));//绩效工资及其他 基础绩效 每月
	   	   	    				}else if(row.getCell(10).getCellType()==HSSFCell.CELL_TYPE_STRING){
	   	   	    					cwBankDetail.setJxgzJcjxMy(new BigDecimal(row.getCell(10).getStringCellValue()).setScale(2, BigDecimal.ROUND_HALF_UP));//绩效工资及其他 基础绩效 每月
	   	   	    				}else if(row.getCell(10).getCellType()==HSSFCell.CELL_TYPE_FORMULA){
	   	   	    					cwBankDetail.setJxgzJcjxMy(new BigDecimal(Float.toString((float)(Math.round(row.getCell(10).getNumericCellValue()*100))/100)));//绩效工资及其他 基础绩效 每月
	   	   	    				}else if(row.getCell(10).getCellType()==HSSFCell.CELL_TYPE_BLANK){
	   	   	    					cwBankDetail.setJxgzJcjxMy(null);//绩效工资及其他 基础绩效 每月
	   	   	    				}
					    	}
					    	System.out.println("绩效工资及其他 基础绩效 每月:"+row.getCell(10));
					    	
					    	if(row.getCell(11)!=null) {
					    		if(row.getCell(11).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
	   	   	    					cwBankDetail.setJxgzJcjxBf(new BigDecimal(Float.toString((float)(Math.round(row.getCell(11).getNumericCellValue()*100))/100)));//绩效工资及其他 基础绩效 补发
	   	   	    				}else if(row.getCell(11).getCellType()==HSSFCell.CELL_TYPE_STRING){
	   	   	    					cwBankDetail.setJxgzJcjxBf(new BigDecimal(row.getCell(11).getStringCellValue()).setScale(2, BigDecimal.ROUND_HALF_UP));//绩效工资及其他 基础绩效 补发
	   	   	    				}else if(row.getCell(11).getCellType()==HSSFCell.CELL_TYPE_FORMULA){
	   	   	    					cwBankDetail.setJxgzJcjxBf(new BigDecimal(Float.toString((float)(Math.round(row.getCell(11).getNumericCellValue()*100))/100)));//绩效工资及其他 基础绩效 补发
	   	   	    				}else if(row.getCell(11).getCellType()==HSSFCell.CELL_TYPE_BLANK){
	   	   	    					cwBankDetail.setJxgzJcjxBf(null);//绩效工资及其他 基础绩效 补发
	   	   	    				}
					    	}
					    	System.out.println("绩效工资及其他 基础绩效 补发:"+row.getCell(11));
					    	
					    	if(row.getCell(12)!=null) {
					    		if(row.getCell(12).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
	   	   	    					cwBankDetail.setJxgzJdjx(new BigDecimal(Float.toString((float)(Math.round(row.getCell(12).getNumericCellValue()*100))/100)));//绩效工资及其他 季度绩效
	   	   	    				}else if(row.getCell(12).getCellType()==HSSFCell.CELL_TYPE_STRING){
	   	   	    					cwBankDetail.setJxgzJdjx(new BigDecimal(row.getCell(12).getStringCellValue()).setScale(2, BigDecimal.ROUND_HALF_UP));//绩效工资及其他 季度绩效
	   	   	    				}else if(row.getCell(12).getCellType()==HSSFCell.CELL_TYPE_FORMULA){
	   	   	    					cwBankDetail.setJxgzJdjx(new BigDecimal(Float.toString((float)(Math.round(row.getCell(12).getNumericCellValue()*100))/100)));//绩效工资及其他 季度绩效
	   	   	    				}else if(row.getCell(12).getCellType()==HSSFCell.CELL_TYPE_BLANK){
	   	   	    					cwBankDetail.setJxgzJdjx(null);//绩效工资及其他 季度绩效
	   	   	    				}
					    	}
					    	System.out.println("绩效工资及其他 季度绩效:"+row.getCell(12));
					    	
					    	if(row.getCell(13)!=null) {
					    		if(row.getCell(13).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
	   	   	    					cwBankDetail.setJxgzBtZcbt(new BigDecimal(Float.toString((float)(Math.round(row.getCell(13).getNumericCellValue()*100))/100)));//绩效工资及其他 补贴 驻场补贴
	   	   	    				}else if(row.getCell(13).getCellType()==HSSFCell.CELL_TYPE_STRING){
	   	   	    					cwBankDetail.setJxgzBtZcbt(new BigDecimal(row.getCell(13).getStringCellValue()).setScale(2, BigDecimal.ROUND_HALF_UP));//绩效工资及其他 补贴 驻场补贴
	   	   	    				}else if(row.getCell(13).getCellType()==HSSFCell.CELL_TYPE_FORMULA){
	   	   	    					cwBankDetail.setJxgzBtZcbt(new BigDecimal(Float.toString((float)(Math.round(row.getCell(13).getNumericCellValue()*100))/100)));//绩效工资及其他 补贴 驻场补贴
	   	   	    				}else if(row.getCell(13).getCellType()==HSSFCell.CELL_TYPE_BLANK){
	   	   	    					cwBankDetail.setJxgzBtZcbt(null);//绩效工资及其他 补贴 驻场补贴
	   	   	    				}
					    	}
					    	System.out.println("绩效工资及其他 补贴 驻场补贴:"+row.getCell(13));
					    	
					    	if(row.getCell(14)!=null) {
					    		if(row.getCell(14).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
	   	   	    					cwBankDetail.setJxgzBtQt(new BigDecimal(Float.toString((float)(Math.round(row.getCell(14).getNumericCellValue()*100))/100)));//绩效工资及其他 补贴  其他
	   	   	    				}else if(row.getCell(14).getCellType()==HSSFCell.CELL_TYPE_STRING){
	   	   	    					cwBankDetail.setJxgzBtQt(new BigDecimal(row.getCell(14).getStringCellValue()).setScale(2, BigDecimal.ROUND_HALF_UP));//绩效工资及其他 补贴  其他
	   	   	    				}else if(row.getCell(14).getCellType()==HSSFCell.CELL_TYPE_FORMULA){
	   	   	    					cwBankDetail.setJxgzBtQt(new BigDecimal(Float.toString((float)(Math.round(row.getCell(14).getNumericCellValue()*100))/100)));//绩效工资及其他 补贴  其他
	   	   	    				}else if(row.getCell(14).getCellType()==HSSFCell.CELL_TYPE_BLANK){
	   	   	    					cwBankDetail.setJxgzBtQt(null);//绩效工资及其他 补贴  其他
	   	   	    				}
					    	}
					    	System.out.println("绩效工资及其他 补贴  其他:"+row.getCell(14));
					    	
					    	if(row.getCell(15)!=null) {
					    		if(row.getCell(15).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
	   	   	    					cwBankDetail.setJxgzBy(new BigDecimal(Float.toString((float)(Math.round(row.getCell(15).getNumericCellValue()*100))/100)));//绩效工资及其他 备用
	   	   	    				}else if(row.getCell(15).getCellType()==HSSFCell.CELL_TYPE_STRING){
	   	   	    					cwBankDetail.setJxgzBy(new BigDecimal(row.getCell(15).getStringCellValue()).setScale(2, BigDecimal.ROUND_HALF_UP));//绩效工资及其他 备用
	   	   	    				}else if(row.getCell(15).getCellType()==HSSFCell.CELL_TYPE_FORMULA){
	   	   	    					cwBankDetail.setJxgzBy(new BigDecimal(Float.toString((float)(Math.round(row.getCell(15).getNumericCellValue()*100))/100)));//绩效工资及其他 备用
	   	   	    				}else if(row.getCell(15).getCellType()==HSSFCell.CELL_TYPE_BLANK){
	   	   	    					cwBankDetail.setJxgzBy(null);//绩效工资及其他 备用
	   	   	    				}
					    	}
					    	System.out.println("绩效工资及其他 备用:"+row.getCell(15));
					    	
					    	if(row.getCell(16)!=null) {
					    		if(row.getCell(16).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
	   	   	    					cwBankDetail.setJxgzXj(new BigDecimal(Float.toString((float)(Math.round(row.getCell(16).getNumericCellValue()*100))/100)));//绩效工资及其他 小计
	   	   	    				}else if(row.getCell(16).getCellType()==HSSFCell.CELL_TYPE_STRING){
	   	   	    					cwBankDetail.setJxgzXj(new BigDecimal(row.getCell(16).getStringCellValue()).setScale(2, BigDecimal.ROUND_HALF_UP));//绩效工资及其他 小计
	   	   	    				}else if(row.getCell(16).getCellType()==HSSFCell.CELL_TYPE_FORMULA){
	   	   	    					cwBankDetail.setJxgzXj(new BigDecimal(Float.toString((float)(Math.round(row.getCell(16).getNumericCellValue()*100))/100)));//绩效工资及其他 小计
	   	   	    				}else if(row.getCell(16).getCellType()==HSSFCell.CELL_TYPE_BLANK){
	   	   	    					cwBankDetail.setJxgzXj(null);//绩效工资及其他 小计
	   	   	    				}
					    	}
					    	System.out.println("绩效工资及其他 小计:"+row.getCell(16));
					    	
					    	if(row.getCell(17)!=null) {
					    		if(row.getCell(17).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
	   	   	    					cwBankDetail.setKkxZynjMy(new BigDecimal(Float.toString((float)(Math.round(row.getCell(17).getNumericCellValue()*100))/100)));//扣款项 职业年金 每月
	   	   	    				}else if(row.getCell(17).getCellType()==HSSFCell.CELL_TYPE_STRING){
	   	   	    					cwBankDetail.setKkxZynjMy(new BigDecimal(row.getCell(17).getStringCellValue()).setScale(2, BigDecimal.ROUND_HALF_UP));//扣款项 职业年金 每月
	   	   	    				}else if(row.getCell(17).getCellType()==HSSFCell.CELL_TYPE_FORMULA){
	   	   	    					cwBankDetail.setKkxZynjMy(new BigDecimal(Float.toString((float)(Math.round(row.getCell(17).getNumericCellValue()*100))/100)));//扣款项 职业年金 每月
	   	   	    				}else if(row.getCell(17).getCellType()==HSSFCell.CELL_TYPE_BLANK){
	   	   	    					cwBankDetail.setKkxZynjMy(null);//扣款项 职业年金 每月
	   	   	    				}
					    	}
					    	System.out.println("扣款项 职业年金 每月:"+row.getCell(17));
					    	
					    	if(row.getCell(18)!=null) {
					    		if(row.getCell(18).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
	   	   	    					cwBankDetail.setKkxZynjBk(new BigDecimal(Float.toString((float)(Math.round(row.getCell(18).getNumericCellValue()*100))/100)));//扣款项 职业年金 补扣
	   	   	    				}else if(row.getCell(18).getCellType()==HSSFCell.CELL_TYPE_STRING){
	   	   	    					cwBankDetail.setKkxZynjBk(new BigDecimal(row.getCell(18).getStringCellValue()).setScale(2, BigDecimal.ROUND_HALF_UP));//扣款项 职业年金 补扣
	   	   	    				}else if(row.getCell(18).getCellType()==HSSFCell.CELL_TYPE_FORMULA){
	   	   	    					cwBankDetail.setKkxZynjBk(new BigDecimal(Float.toString((float)(Math.round(row.getCell(18).getNumericCellValue()*100))/100)));//扣款项 职业年金 补扣
	   	   	    				}else if(row.getCell(18).getCellType()==HSSFCell.CELL_TYPE_BLANK){
	   	   	    					cwBankDetail.setKkxZynjBk(null);//扣款项 职业年金 补扣
	   	   	    				}
					    	}
					    	System.out.println("扣款项 职业年金 补扣:"+row.getCell(18));
					    	
					    	if(row.getCell(19)!=null) {
					    		if(row.getCell(19).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
	   	   	    					cwBankDetail.setKkxYlbxMy(new BigDecimal(Float.toString((float)(Math.round(row.getCell(19).getNumericCellValue()*100))/100)));//扣款项 医疗保险 每月
	   	   	    				}else if(row.getCell(19).getCellType()==HSSFCell.CELL_TYPE_STRING){
	   	   	    					cwBankDetail.setKkxYlbxMy(new BigDecimal(row.getCell(19).getStringCellValue()).setScale(2, BigDecimal.ROUND_HALF_UP));//扣款项 医疗保险 每月
	   	   	    				}else if(row.getCell(19).getCellType()==HSSFCell.CELL_TYPE_FORMULA){
	   	   	    					cwBankDetail.setKkxYlbxMy(new BigDecimal(Float.toString((float)(Math.round(row.getCell(19).getNumericCellValue()*100))/100)));//扣款项 医疗保险 每月
	   	   	    				}else if(row.getCell(19).getCellType()==HSSFCell.CELL_TYPE_BLANK){
	   	   	    					cwBankDetail.setKkxYlbxMy(null);//扣款项 医疗保险 每月
	   	   	    				}
					    	}
					    	System.out.println("扣款项 医疗保险 每月:"+row.getCell(19));
					    	
					    	if(row.getCell(20)!=null) {
					    		if(row.getCell(20).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
	   	   	    					cwBankDetail.setKkxYlbxBf(new BigDecimal(Float.toString((float)(Math.round(row.getCell(20).getNumericCellValue()*100))/100)));//扣款项 医疗保险 补发
	   	   	    				}else if(row.getCell(20).getCellType()==HSSFCell.CELL_TYPE_STRING){
	   	   	    					cwBankDetail.setKkxYlbxBf(new BigDecimal(row.getCell(20).getStringCellValue()).setScale(2, BigDecimal.ROUND_HALF_UP));//扣款项 医疗保险 补发
	   	   	    				}else if(row.getCell(20).getCellType()==HSSFCell.CELL_TYPE_FORMULA){
	   	   	    					cwBankDetail.setKkxYlbxBf(new BigDecimal(Float.toString((float)(Math.round(row.getCell(20).getNumericCellValue()*100))/100)));//扣款项 医疗保险 补发
	   	   	    				}else if(row.getCell(20).getCellType()==HSSFCell.CELL_TYPE_BLANK){
	   	   	    					cwBankDetail.setKkxYlbxBf(null);//扣款项 医疗保险 补发
	   	   	    				}
					    	}
					    	System.out.println("扣款项 医疗保险 补发:"+row.getCell(20));
					    	
					    	if(row.getCell(21)!=null) {
					    		if(row.getCell(21).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
	   	   	    					cwBankDetail.setKkxOldbxMy(new BigDecimal(Float.toString((float)(Math.round(row.getCell(21).getNumericCellValue()*100))/100)));//扣款项 养老保险 每月
	   	   	    				}else if(row.getCell(21).getCellType()==HSSFCell.CELL_TYPE_STRING){
	   	   	    					cwBankDetail.setKkxOldbxMy(new BigDecimal(row.getCell(21).getStringCellValue()).setScale(2, BigDecimal.ROUND_HALF_UP));//扣款项 养老保险 每月
	   	   	    				}else if(row.getCell(21).getCellType()==HSSFCell.CELL_TYPE_FORMULA){
	   	   	    					cwBankDetail.setKkxOldbxMy(new BigDecimal(Float.toString((float)(Math.round(row.getCell(21).getNumericCellValue()*100))/100)));//扣款项 养老保险 每月
	   	   	    				}else if(row.getCell(21).getCellType()==HSSFCell.CELL_TYPE_BLANK){
	   	   	    					cwBankDetail.setKkxOldbxMy(null);//扣款项 养老保险 每月
	   	   	    				}
					    	}
					    	System.out.println("扣款项 养老保险 每月:"+row.getCell(21));
					    	
							if(row.getCell(22)!=null) {
								if(row.getCell(22).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
	   	   	    					cwBankDetail.setKkxOldbxBf(new BigDecimal(Float.toString((float)(Math.round(row.getCell(22).getNumericCellValue()*100))/100)));//扣款项 养老保险 补发
	   	   	    				}else if(row.getCell(22).getCellType()==HSSFCell.CELL_TYPE_STRING){
	   	   	    					cwBankDetail.setKkxOldbxBf(new BigDecimal(row.getCell(22).getStringCellValue()).setScale(2, BigDecimal.ROUND_HALF_UP));//扣款项 养老保险 补发
	   	   	    				}else if(row.getCell(22).getCellType()==HSSFCell.CELL_TYPE_FORMULA){
	   	   	    					cwBankDetail.setKkxOldbxBf(new BigDecimal(Float.toString((float)(Math.round(row.getCell(22).getNumericCellValue()*100))/100)));//扣款项 养老保险 补发
	   	   	    				}else if(row.getCell(22).getCellType()==HSSFCell.CELL_TYPE_BLANK){
	   	   	    					cwBankDetail.setKkxOldbxBf(null);//扣款项 养老保险 补发
	   	   	    				}				    		
							}
					    	System.out.println("扣款项 养老保险 补发:"+row.getCell(22));
					    	
					    	if(row.getCell(23)!=null) {
					    		if(row.getCell(23).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
	   	   	    					cwBankDetail.setKkxSyMy(new BigDecimal(Float.toString((float)(Math.round(row.getCell(23).getNumericCellValue()*100))/100)));//扣款项 失业 每月
	   	   	    				}else if(row.getCell(23).getCellType()==HSSFCell.CELL_TYPE_STRING){
	   	   	    					cwBankDetail.setKkxSyMy(new BigDecimal(row.getCell(23).getStringCellValue()).setScale(2, BigDecimal.ROUND_HALF_UP));//扣款项 失业 每月
	   	   	    				}else if(row.getCell(23).getCellType()==HSSFCell.CELL_TYPE_FORMULA){
	   	   	    					cwBankDetail.setKkxSyMy(new BigDecimal(Float.toString((float)(Math.round(row.getCell(23).getNumericCellValue()*100))/100)));//扣款项 失业 每月
	   	   	    				}else if(row.getCell(23).getCellType()==HSSFCell.CELL_TYPE_BLANK){
	   	   	    					cwBankDetail.setKkxSyMy(null);//扣款项 失业 每月
	   	   	    				}
					    	}
					    	System.out.println("扣款项 失业 每月:"+row.getCell(23));
					    	
					    	if(row.getCell(24)!=null) {
					    		if(row.getCell(24).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
	   	   	    					cwBankDetail.setKkxSyBf(new BigDecimal(Float.toString((float)(Math.round(row.getCell(24).getNumericCellValue()*100))/100)));//扣款项 失业 补发
	   	   	    				}else if(row.getCell(24).getCellType()==HSSFCell.CELL_TYPE_STRING){
	   	   	    					cwBankDetail.setKkxSyBf(new BigDecimal(row.getCell(24).getStringCellValue()).setScale(2, BigDecimal.ROUND_HALF_UP));//扣款项 失业 补发
	   	   	    				}else if(row.getCell(24).getCellType()==HSSFCell.CELL_TYPE_FORMULA){
	   	   	    					cwBankDetail.setKkxSyBf(new BigDecimal(Float.toString((float)(Math.round(row.getCell(24).getNumericCellValue()*100))/100)));//扣款项 失业 补发
	   	   	    				}else if(row.getCell(24).getCellType()==HSSFCell.CELL_TYPE_BLANK){
	   	   	    					cwBankDetail.setKkxSyBf(null);//扣款项 失业 补发
	   	   	    				}
					    	}
					    	System.out.println("扣款项 失业 补发:"+row.getCell(24));
					    	
					    	if(row.getCell(25)!=null) {
					    		if(row.getCell(25).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
	   	   	    					cwBankDetail.setKkxGjjMy(new BigDecimal(Float.toString((float)(Math.round(row.getCell(25).getNumericCellValue()*100))/100)));//扣款项 公积金 每月
	   	   	    				}else if(row.getCell(25).getCellType()==HSSFCell.CELL_TYPE_STRING){
	   	   	    					cwBankDetail.setKkxGjjMy(new BigDecimal(row.getCell(25).getStringCellValue()).setScale(2, BigDecimal.ROUND_HALF_UP));//扣款项 公积金 每月
	   	   	    				}else if(row.getCell(25).getCellType()==HSSFCell.CELL_TYPE_FORMULA){
	   	   	    				cwBankDetail.setKkxGjjMy(new BigDecimal(Float.toString((float)(Math.round(row.getCell(25).getNumericCellValue()*100))/100)));//扣款项 公积金 每月
	   	   	    				}else if(row.getCell(25).getCellType()==HSSFCell.CELL_TYPE_BLANK){
	   	   	    					cwBankDetail.setKkxGjjMy(null);//扣款项 公积金 每月
	   	   	    				}
					    	}
					    	System.out.println("扣款项 公积金 每月:"+row.getCell(25));
					    	
					    	if(row.getCell(26)!=null) {
					    		if(row.getCell(26).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
	   	   	    					cwBankDetail.setKkxGjjBf(new BigDecimal(Float.toString((float)(Math.round(row.getCell(26).getNumericCellValue()*100))/100)));//扣款项 公积金 补发
	   	   	    				}else if(row.getCell(26).getCellType()==HSSFCell.CELL_TYPE_STRING){
	   	   	    					cwBankDetail.setKkxGjjBf(new BigDecimal(row.getCell(26).getStringCellValue()).setScale(2, BigDecimal.ROUND_HALF_UP));//扣款项 公积金 补发
	   	   	    				}else if(row.getCell(26).getCellType()==HSSFCell.CELL_TYPE_FORMULA){
	   	   	    					cwBankDetail.setKkxGjjBf(new BigDecimal(Float.toString((float)(Math.round(row.getCell(26).getNumericCellValue()*100))/100)));//扣款项 公积金 补发
	   	   	    				}else if(row.getCell(26).getCellType()==HSSFCell.CELL_TYPE_BLANK){
	   	   	    					cwBankDetail.setKkxGjjBf(null);//扣款项 公积金 补发
	   	   	    				}
					    	}
					    	System.out.println("扣款项 公积金 补发:"+row.getCell(26));
					    	
					    	if(row.getCell(27)!=null) {
					    		if(row.getCell(27).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
	   	   	    					cwBankDetail.setKkxGhjf(new BigDecimal(Float.toString((float)(Math.round(row.getCell(27).getNumericCellValue()*100))/100)));//扣款项 工会经费
	   	   	    				}else if(row.getCell(27).getCellType()==HSSFCell.CELL_TYPE_STRING){
	   	   	    					cwBankDetail.setKkxGhjf(new BigDecimal(row.getCell(27).getStringCellValue()).setScale(2, BigDecimal.ROUND_HALF_UP));//扣款项 工会经费
	   	   	    				}else if(row.getCell(27).getCellType()==HSSFCell.CELL_TYPE_FORMULA){
	   	   	    					cwBankDetail.setKkxGhjf(new BigDecimal(Float.toString((float)(Math.round(row.getCell(27).getNumericCellValue()*100))/100)));//扣款项 工会经费
	   	   	    				}else if(row.getCell(27).getCellType()==HSSFCell.CELL_TYPE_BLANK){
	   	   	    					cwBankDetail.setKkxGhjf(null);//扣款项 工会经费
	   	   	    				}
					    	}
					    	System.out.println("扣款项 工会经费:"+row.getCell(27));
					    	
					    	if(row.getCell(28)!=null) {
					    		if(row.getCell(28).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
	   	   	    					cwBankDetail.setKkxSds(new BigDecimal(Float.toString((float)(Math.round(row.getCell(28).getNumericCellValue()*100))/100)));//扣款项 所得税
	   	   	    				}else if(row.getCell(28).getCellType()==HSSFCell.CELL_TYPE_STRING){
	   	   	    					cwBankDetail.setKkxSds(new BigDecimal(row.getCell(28).getStringCellValue()).setScale(2, BigDecimal.ROUND_HALF_UP));//扣款项 所得税
	   	   	    				}else if(row.getCell(28).getCellType()==HSSFCell.CELL_TYPE_FORMULA){
	   	   	    					cwBankDetail.setKkxSds(new BigDecimal(Float.toString((float)(Math.round(row.getCell(28).getNumericCellValue()*100))/100)));//扣款项 所得税
	   	   	    				}else if(row.getCell(28).getCellType()==HSSFCell.CELL_TYPE_BLANK){
	   	   	    					cwBankDetail.setKkxSds(null);//扣款项 所得税
	   	   	    				}
					    	}
					    	System.out.println("扣款项 所得税:"+row.getCell(28));
					    	
					    	if(row.getCell(29)!=null) {
					    		if(row.getCell(29).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
	   	   	    					cwBankDetail.setKkxBy(new BigDecimal(Float.toString((float)(Math.round(row.getCell(29).getNumericCellValue()*100))/100)));//扣款项 备用
	   	   	    				}else if(row.getCell(29).getCellType()==HSSFCell.CELL_TYPE_STRING){
	   	   	    					cwBankDetail.setKkxBy(new BigDecimal(row.getCell(29).getStringCellValue()).setScale(2, BigDecimal.ROUND_HALF_UP));//扣款项 备用
	   	   	    				}else if(row.getCell(29).getCellType()==HSSFCell.CELL_TYPE_FORMULA){
	   	   	    					cwBankDetail.setKkxBy(new BigDecimal(Float.toString((float)(Math.round(row.getCell(29).getNumericCellValue()*100))/100)));//扣款项 备用
	   	   	    				}else if(row.getCell(29).getCellType()==HSSFCell.CELL_TYPE_BLANK){
	   	   	    					cwBankDetail.setKkxBy(null);//扣款项 备用
	   	   	    				}
					    	}
					    	System.out.println("扣款项 备用:"+row.getCell(29));
					    	
					    	if(row.getCell(30)!=null) {
					    		if(row.getCell(30).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
	   	   	    					cwBankDetail.setKkxXj(new BigDecimal(Float.toString((float)(Math.round(row.getCell(30).getNumericCellValue()*100))/100)));//扣款项 小计
	   	   	    				}else if(row.getCell(30).getCellType()==HSSFCell.CELL_TYPE_STRING){
	   	   	    					cwBankDetail.setKkxXj(new BigDecimal(row.getCell(30).getStringCellValue()).setScale(2, BigDecimal.ROUND_HALF_UP));//扣款项 小计
	   	   	    				}else if(row.getCell(30).getCellType()==HSSFCell.CELL_TYPE_FORMULA){
	   	   	    					cwBankDetail.setKkxXj(new BigDecimal(Float.toString((float)(Math.round(row.getCell(30).getNumericCellValue()*100))/100)));//扣款项 小计
	   	   	    					/*cwBankDetail.setKkxXj(new BigDecimal(row.getCell(30).getNumericCellValue()).setScale(2, BigDecimal.ROUND_HALF_UP));//扣款项 小计
*/	   	   	    				}else if(row.getCell(30).getCellType()==HSSFCell.CELL_TYPE_BLANK){
	   	   	    					cwBankDetail.setKkxXj(null);//扣款项 小计
	   	   	    				}
					    	}
					    	System.out.println("扣款项 小计:"+row.getCell(30));
					    	
					    	if(row.getCell(31)!=null) {
					    		if(row.getCell(31).getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
	   	   	    					cwBankDetail.setFsalary(new BigDecimal(Float.toString((float)(Math.round(row.getCell(31).getNumericCellValue()*100))/100)));//扣款项 应发合计
	   	   	    				}else if(row.getCell(31).getCellType()==HSSFCell.CELL_TYPE_STRING){
	   	   	    					cwBankDetail.setFsalary(new BigDecimal(row.getCell(31).getStringCellValue()).setScale(2, BigDecimal.ROUND_HALF_UP));//扣款项 应发合计
	   	   	    				}else if(row.getCell(31).getCellType()==HSSFCell.CELL_TYPE_FORMULA){
	   	   	    					cwBankDetail.setFsalary(new BigDecimal(Float.toString((float)(Math.round(row.getCell(31).getNumericCellValue()*100))/100)));//扣款项 应发合计
	   	   	    				}else if(row.getCell(31).getCellType()==HSSFCell.CELL_TYPE_BLANK){
	   	   	    					cwBankDetail.setFsalary(null);//扣款项 应发合计
	   	   	    				}
					    	}
					    	System.out.println("扣款项 应发合计:"+row.getCell(31));
					    	//合计总和
					    	
					    	double hj1 = row.getCell(31).getNumericCellValue();
					    	hj=hj+hj1;
					    	cwBankDetail.setImportId(imid);
					    	salaryDao.save(cwBankDetail);
						} catch (Exception e) {
							e.printStackTrace();
							return name;
						}
		    		}
		    	}
		    }
		String hjs = String.valueOf(hj);
		if(imp!=null && imp.size()>0) {
			ImportSalary importSalary = imp.get(0);
			BigDecimal gzhj = parseBigDecimal(hjs);
			importSalary.setTotalWages(gzhj);
		    importSalaryDao.save(imp.get(0));
		}
			return repData;
		}
		
		/**
		 * 日期转换
		 * @param st
		 * @return
		 * @throws ParseException
		 */
		@SuppressWarnings("finally")
		public Date parseDate(String st) throws ParseException {
			Date dt=null;
			try {
//				if (st.indexOf("至")>-1){
//					return null;
//				}
//				if (st.indexOf("今")>-1){
//					return null;
//				}
				dt = sdf.parse(sdf.format(sdf.parse(st.trim().replaceAll("[./—]", "-"))));
			} finally {
				return dt;
			}
		}
		
		/**
		 * 数值转换
		 * @param st
		 * @return
		 */
		@SuppressWarnings("finally")
		public BigDecimal parseBigDecimal(String st) {
			BigDecimal bg=null;
			try {
				bg = new BigDecimal(st);
			} finally {
				return bg;
			}
		}
		
		public String getSystemFilePath() {
			
			SysParaInf sp = Factory.getSysPara();
			String attachmentPath = sp.getProperty("attachmentPath");
			String attachmentPathType = sp.getProperty("attachmentPathType", "relative");
			
			if ("relative".equals(attachmentPathType)) {
				return Factory.getWebRoot() + File.separator + attachmentPath;
			}
			return attachmentPath;
		}
		
		
		
		
		
		
//		
//		
//		
//		
//		
//		
//		Log log = LogFactory.getLog(getClass());
//
//		/**
//		 * 导入成功后业务后续处理
//		 * 
//		 * @param result
//		 *            导入的数据
//		 */
//		public void importHandle(List<Object> result) {
//			log.debug("本批次数据总数为" + result.size());
//			// 检查数据包装方式
//			Object obj = result.get(0);
//			if (obj instanceof Map)// Hashmap包装
//				mapImport(result);
//			else
//				// bean包装
//				beanImport(result);
//		}
//
//		/**
//		 * 业务Bean方式导入
//		 * 
//		 * 将数据分批次进行导入，每一批次100行数据
//		 */
//		protected void beanImport(List<Object> beans) {
//			log.debug("Entity Bean方式导入数据");
//			List<Salary> batchData = new ArrayList<Salary>();
//			for (int i = 0; i < beans.size(); i++) {
//				batchData.add((Salary) beans.get(i));
//			}
//			salaryDao.saveBatch(batchData);
//		}
//
//		/**
//		 * Map方式导入数据。
//		 * 
//		 * 需要将Map转换成业务 entity bean
//		 */
//		@SuppressWarnings("unchecked")
//		protected void mapImport(List<Object> maps) {
//			log.debug("Map方式导入数据");
//			List<Salary> batchData = new ArrayList<Salary>();
//			for (int i = 0; i < maps.size(); i++) {
//				Map<String, Object> map = (Map<String, Object>) maps.get(i);
//				Salary bean = new Salary();
//				Date date = (Date) map.get("t_date");
//				Long longVal = (Long) map.get("t_long");
//				String str = (String) map.get("t_string");
//				String bool = (String) map.get("t_bool");
//				Integer intVal = (Integer) map.get("t_int");
//				Float floatVal = (Float) map.get("t_float");
//
////				bean.setStr(str);
////				bean.setDate(date);
////				bean.setBool(bool == "1" ? true : false);
////				bean.setIntVal(intVal == null ? 0 : intVal);
////				bean.setLongVal(longVal == null ? 0 : longVal);
////				bean.setFloatVal(floatVal == null ? 0 : floatVal);
//
//				batchData.add(bean);
//			}
//			salaryDao.saveBatch(batchData);
//		}
//
//		@SuppressWarnings("unchecked")
//		@Override
//		public void importValidate(Object rowObj) throws ValidateException {
////			if (rowObj instanceof Salary) {
////				Salary dbb = (Salary) rowObj;
////				if(100==dbb.getId())throw new ValidateException("第2列数据不允许为100");
////			}
////			else {
////				Map<String, Object> rm = (Map<String, Object>) rowObj;
////				if(rm.get("t_int").equals(100))throw new ValidateException("第2列数据不允许为100");
////			}
//		}
//
//		/**
//		 * 业务自行处理数据源。
//		 * 
//		 * 这里，通过importTask.getSource()方法获取数据源对象，转换成合适的格式，解析出要导入的数据总数，
//		 * 并通过参数importTask对象的setTotal( int)方法设置数据总数以便返回给前端进行进度跟踪。
//		 * 
//		 * 在每处理完一行数据之后，调用importTask.finish(1)，更新数据导入进度。
//		 * 
//		 * @param source
//		 *            导入的数据源，格式由业务自行决定
//		 * @param importTask
//		 *            数据导入任务信息
//		 */
//		public void doBusImport(ImportTask importTask) throws Exception {
//			log.debug("业务自行解析数据并导入");
//			Workbook wb = (Workbook) importTask.getSource();
//			// 获取到下一个待处理的Excel数据行
//			Sheet s = wb.getSheetAt(0);
//			int total = s.getLastRowNum();
//			log.debug("Excel中的数据总行数为" + (total - 1));
//			importTask.setTotal(total);
//
//			List<Salary> batchData = new ArrayList<Salary>();
//
//			// 数据从第二行开始
//			for (int i = 1; i <= total; i++) {
//				Row row = s.getRow(i);
//				Salary tbb = new Salary();
////				tbb.setStr(row.getCell(0).getStringCellValue());
////				tbb.setBool(row.getCell(1).getBooleanCellValue());
////				tbb.setDate(row.getCell(2).getDateCellValue());
////				tbb.setIntVal(new Double(row.getCell(3).getNumericCellValue()).intValue());
////				tbb.setLongVal(new Double(row.getCell(4).getNumericCellValue()).longValue());
////				tbb.setFloatVal(new Double(row.getCell(4).getNumericCellValue()).floatValue());
//				batchData.add(tbb);
//
//				// 测试数据导入过程中发生意外
//				if (i == 5000) {
//					// 如果要终止本次操作，直接抛出KhntException，并附上错误提示！
//					throw new KhntException("第106行数据不正确！");
//
//					// 如果不终止操作，在这里标记一个错误行,然后继续下一行
//					// importTask.error(1);
//					// continue;
//				}
//
//				// 一批100个已满或者循环到最后一个时，提交这一批数据
//				if (batchData.size() == 100 || i == total) {
//					log.debug("一批数据已满，保存这一批");
//					salaryDao.saveBatch(batchData);
//
//					// 清空这一批数据，重新装入
//					batchData.clear();
//
//					// 返回进度，标志为每批次的数据量
//					importTask.finish(i == total ? total % 100 : 100);
//
//					log.debug("当前导入进度：" + i);
//				}
//			}
//
//			// 如果导入过程中有错误的数据，可以将这些错误的数据汇总起来，形成一个文件返回给用户
//			// 调用 importTask.completeSuccess方法完成本次导入，并将错误的数据汇总为一个File作为参数。
//			// 如果没有错误数据，不需要调用此方法
//			// importTask.completeSuccess(file);
//		}
//
//		/**
//		 * 通用数据导出时，业务提供要导出的数据集合。
//		 * 
//		 * @param parameter
//		 *            业务数据查询条件
//		 * @return 要导出的数据集合，该集合的元素对象必须与配置模板中设定的数据包装类型一致。
//		 *         比如配置为BusinessBean，则这里的List<Object>实际为List<BusinessBean>
//		 *         配置为haspMap或者数据库表，则这里的List<Object>实际为List<HashMap>，
//		 *         hashmap的key就是列信息中的字段名称
//		 */
//		public List<Object> exportHandle(String parameters) {
//			log.debug("导出数据的业务查询参数:" + parameters);
//
//			// 这里我们前端传来的参数格式是json object
//			JSONObject jo = JSONObject.fromString(parameters);
//			String strVal = jo.getString("strVal");
//
//			// 查询数据并返回
//			return this.salaryDao.getData(strVal, 1000);
//		}
//
//		/**
//		 * 业务自行处理数据导出，返回一个数据导出任务。该任务必须设置导出的数据结果和文件名称。
//		 * 
//		 * @param parameter
//		 *            业务数据查询条件
//		 */
//		public ExportTask doBusExport(String parameters) throws Exception {
//			log.debug("导出数据的业务查询参数:" + parameters);
//
//			// 这里我们前端传来的参数格式是json object
//			JSONObject jo = JSONObject.fromString(parameters);
//			String strVal = jo.getString("strVal");
//
//			// 查询数据
//			List<Object> datas = this.salaryDao.getData(strVal, 1000);
//
//			// 使用SXSSFWorkbook处理海量数据
//			Workbook wb = new SXSSFWorkbook();
//			Sheet st = wb.createSheet("演示数据导出结果");
//			for (Object obj : datas) {
//				Salary bean = (Salary) obj;
//				Row r0 = st.createRow(0);
//				r0.createCell(0).setCellValue("string");
//				r0.createCell(1).setCellValue("bool");
//				r0.createCell(2).setCellValue("date");
//				r0.createCell(3).setCellValue("int");
//				r0.createCell(4).setCellValue("long");
//				r0.createCell(5).setCellValue("flaot");
//
//				Row r1 = st.createRow(1);
////				r1.createCell(0).setCellValue(bean.getStr());
////				r1.createCell(1).setCellValue(bean.isBool());
////				r1.createCell(2).setCellValue(bean.getDate());
////				r1.createCell(3).setCellValue(bean.getIntVal());
////				r1.createCell(4).setCellValue(bean.getLongVal());
////				r1.createCell(5).setCellValue(bean.getFloatVal());
//			}
//
//			// 构造一个数据导出对象，将excelworkbook和文件名称返回给平台组件，由组件将文件下载给客户端
//			ExportTask et = new ExcelExportTask("演示数据导出.xlsx", wb);
//
//			return et;
//		}
//		
//		
//		
//		
	/**
	 * 根据id获取工资信息
	 * @param id
	 * @return
	 * @throws ParseException
	 */
	public Salary getSalary(String id) throws ParseException {
		Salary salary =	salaryDao.getSalary(id);
		return salary;
	}
}
