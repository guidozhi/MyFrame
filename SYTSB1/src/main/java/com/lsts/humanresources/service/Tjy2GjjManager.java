package com.lsts.humanresources.service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.khnt.core.crud.manager.impl.EntityManageImpl;
import com.khnt.core.crud.web.support.JsonWrapper;
import com.khnt.security.CurrentSessionUser;
import com.khnt.security.util.SecurityUtil;
import com.lsts.humanresources.Tjy2Ywfwbgzqrb.service.Tjy2YwfwbgzqrbManager;
import com.lsts.humanresources.bean.Tjy2Gjj;
import com.lsts.humanresources.dao.Tjy2GjjDao;
import com.lsts.log.service.SysLogService;
import com.lsts.qualitymanage.bean.TaskAllot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("Tjy2GjjManager")
public class Tjy2GjjManager extends EntityManageImpl<Tjy2Gjj, Tjy2GjjDao> {
    private Logger logger = LoggerFactory.getLogger(Tjy2YwfwbgzqrbManager.class);
    @Autowired
    Tjy2GjjDao tjy2GjjDao;
    @Autowired
    private SysLogService logService;

    public HashMap<String, Object> saveBasic(HttpServletRequest request, CurrentSessionUser user, Tjy2Gjj tjy2Gjj)
            throws Exception {

        HashMap<String, Object> wrapper = new HashMap<String, Object>();
        int ybgzrnrz_nx = 0;
        if (tjy2Gjj.getYbgzryrzNx() != null && tjy2Gjj.getYbgzryrzNx().length() > 0) {

            ybgzrnrz_nx = Integer.parseInt(tjy2Gjj.getYbgzryrzNx());
        }
        int zrzlrz_nx = 0;
        if (tjy2Gjj.getZrzlrzNx() != null && tjy2Gjj.getZrzlrzNx().length() > 0) {

            zrzlrz_nx = Integer.parseInt(tjy2Gjj.getZrzlrzNx());
        }

        int fzrz_nx = 0;
        if (tjy2Gjj.getFzrzNx() != null && tjy2Gjj.getFzrzNx().length() > 0) {
            fzrz_nx = Integer.parseInt(tjy2Gjj.getFzrzNx());
        }
        int zzrz_nx = 0;
        if (tjy2Gjj.getZzrzNx() != null && tjy2Gjj.getZzrzNx().length() > 0) {
            zzrz_nx = Integer.parseInt(tjy2Gjj.getZzrzNx());
        }

        int gcsrz_nx = 0;
        if (tjy2Gjj.getGcsrzNx() != null && tjy2Gjj.getGcsrzNx().length() > 0) {
            gcsrz_nx = Integer.parseInt(tjy2Gjj.getGcsrzNx());
        }
        int gjgcsrz_nx = 0;
        if (tjy2Gjj.getGjgcsrzNx() != null && tjy2Gjj.getGjgcsrzNx().length() > 0) {
            gjgcsrz_nx = Integer.parseInt(tjy2Gjj.getGjgcsrzNx());
        }
        int jysrz_nx = 0;
        if (tjy2Gjj.getJysrzNx() != null && tjy2Gjj.getJysrzNx().length() > 0) {
            jysrz_nx = Integer.parseInt(tjy2Gjj.getJysrzNx());
        }

        int jyhgznx = 0;
        if (tjy2Gjj.getJyhgzNx() != null && tjy2Gjj.getJyhgzNx().length() > 0) {
            jyhgznx = Integer.parseInt(tjy2Gjj.getJyhgzNx());
        }


        tjy2Gjj.setYbgzryrzNx(String.valueOf(jyhgznx - zrzlrz_nx - fzrz_nx - zzrz_nx));
        tjy2Gjj.setZjdyjfJs(String.valueOf((jyhgznx - zrzlrz_nx - fzrz_nx - zzrz_nx) * 60));
        tjy2Gjj.setYbgzryrzNx_a(String.valueOf(jyhgznx - gcsrz_nx - gjgcsrz_nx));
        tjy2Gjj.setZjdyjfJs_a(String.valueOf((jyhgznx - gcsrz_nx - gjgcsrz_nx) * 60));
        tjy2Gjj.setYbgzryrzNx_b(String.valueOf(jyhgznx - jysrz_nx));
        tjy2Gjj.setZjdyjfJs_b(String.valueOf((jyhgznx - jysrz_nx) * 60));
        int yzjfjs = 0;
        int zjfjs1 = (jyhgznx - zrzlrz_nx - fzrz_nx - zzrz_nx) * 60 + zrzlrz_nx * 80 + fzrz_nx * 100 + zzrz_nx * 120;
        int zjfjs2 = (jyhgznx - gcsrz_nx - gjgcsrz_nx) * 60 + gcsrz_nx * 100 + gjgcsrz_nx * 120;
        int zjfjs3 = (jyhgznx - jysrz_nx) * 60 + (jysrz_nx * 100);
        if (zjfjs1 < zjfjs2) {
            yzjfjs = zjfjs2;
            if (yzjfjs < zjfjs3) {
                yzjfjs = zjfjs3;
            }
        } else {
            yzjfjs = zjfjs1;
            if (yzjfjs < zjfjs3) {
                yzjfjs = zjfjs3;
            }
        }
        int syqmyjfjs = 0;
        if (tjy2Gjj.getSyqmyjyJs() != null && tjy2Gjj.getSyqmyjyJs().length() > 0) {
            syqmyjfjs = Integer.parseInt(tjy2Gjj.getSyqmyjyJs());
        }
        int zjfjs = 0;
        zjfjs = yzjfjs + syqmyjfjs;
        tjy2Gjj.setZjfJs(String.valueOf(zjfjs));
        Double tzhyjfje1 = zjfjs * 0.24;
        DecimalFormat fmt = new DecimalFormat("0.#");
        String tzhyjfje = fmt.format(tzhyjfje1);
        tjy2Gjj.setTzhyjfJe(tzhyjfje);
        //个人缴费金额
        Double mqjfJe1 = tzhyjfje1 * 0.5;
        String mqjfJe = fmt.format(mqjfJe1);
        tjy2Gjj.setMqjfJe(mqjfJe);
//  		tjy2Gjj.setCjr(SecurityUtil.getSecurityUser().getName());
        tjy2Gjj.setCjSj(new Date());
        if (tjy2Gjj.getId() == null || tjy2Gjj.getId().equals("")) {
            tjy2Gjj.setBrqr("0");
        }

        tjy2GjjDao.save(tjy2Gjj);
        // 写入系统日志
        try {
            logService.setLogs(tjy2Gjj.getId(), "公积金修改", tjy2Gjj.getName() + "公积金的最新修改人", user.getId(), user.getName(),
                    new Date(), request.getRemoteAddr());
        } catch (Exception e) {
            System.out.println("@@@@@@@@@@@@@@@@@公积金修改操作失败！！@@@@@@@@@@@@@@@@@@@@@@@");
            e.printStackTrace();
        }

        return JsonWrapper.successWrapper(tjy2Gjj.getId());
    }

    public String getId(String id) {

        List Tjy2Gjjlist = tjy2GjjDao.getId(id);
        return (String) Tjy2Gjjlist.get(0);


    }

    public List<Tjy2Gjj> updateBasicBySystem() throws Exception {
        Calendar now = Calendar.getInstance();
        Calendar inDatebase = Calendar.getInstance();
        Calendar inDatebaseTemp = Calendar.getInstance();
        int yearNow = now.get(Calendar.YEAR);//获取当前年份
        int monthNow = now.get(Calendar.MONTH) + 1;//获取当前月份
        int dayNow = now.get(Calendar.DAY_OF_MONTH);//获取当前号数
        List<Tjy2Gjj> list = null;
        System.out.println("------------------------------------------当前年份: " + yearNow);
        System.out.println("------------------------------------------当前月份: " + monthNow);
        System.out.println("------------------------------------------当前日: " + dayNow);
        if (monthNow == 1 && dayNow == 1) {
            list = tjy2GjjDao.getGjjs();
            if (list.size() > 0 && list != null) {
                for (Tjy2Gjj tjy2Gjj : list) {
                    Date jysj = tjy2Gjj.getJySj();//进院时间
                    Date zrzlrzSjA = tjy2Gjj.getZrzlrzSjA();//主任助理任职时间
                    Date fzrzSj = tjy2Gjj.getFzrzSj();//副职任职时间
                    Date zzrzSj = tjy2Gjj.getZzrzSj();//正职任职时间
                    Date gcsrzSj = tjy2Gjj.getGcsrzSj();//工程师任职时间
                    Date gjgcsrzSj = tjy2Gjj.getGjgcsrzSj();//高级工程师任职时间
                    Date jysrz_Sj = tjy2Gjj.getJysrz_Sj();//检验师任职时间

                    String jyhgzNx;//进院后工作年限

                    String zrzlrzNx;//主任助理任职年限（年）
                    String zjdyjfJb;//增加的月缴费基数
                    String fzrzNx;//副职任职年限（年）
                    String zjdyfJsC;//增加的月缴费基数
                    String zzrzNx;//正职任职年限（年）
                    String zjdyfJsD;//增加的月缴费基数

                    String gcsrzNx;//工程师任职年限（年）
                    String zjdyfJsE;//增加的月缴费基数
                    String gjgcsrzNx;//高级工程师任职年限
                    String zjdyfJsF;//增加的缴费基数

                    String jysrzNx;//检验师任职年限
                    String zjdyfJsG;//增加的缴费基数
	  				    
	  				  /*String ybgzryrzNx;//一般工作人员任职年限（年）
	  				  String ybgzryrzNx_a;//一般工作人员任职年限（年）
	  				  String ybgzryrzNx_b;//一般工作人员任职年限（年）
	  				  String zjdyjfJs;//一般工作人员增加的月缴费基数
	  				  String zjdyjfJs_a;//一般工作人员增加的月缴费基数
	  				  String zjdyjfJs_b;//一般工作人员增加的月缴费基数  
	  */
                    if (jysj != null) {
                        inDatebase.setTime(jysj);
                        int year = inDatebase.get(Calendar.YEAR);//获取年份
                        int month = inDatebase.get(Calendar.MONTH) + 1;//获取月份
                        int day = inDatebase.get(Calendar.DAY_OF_MONTH);//获取号数
                        //更新进院后工作年限和第一个缴费基数
                        int caching = 0;
                        System.out.println("------------------------------------------进院年份：" + year);
                        System.out.println("------------------------------------------进院月份：" + month);
                        System.out.println("------------------------------------------进院日：" + day);
                        if (yearNow == year) {
                            caching = 0;
                        } else if (yearNow > year) {
                            if (monthNow > month) {
                                caching = yearNow - year;
                            } else if (monthNow == month) {
                                if (dayNow >= day) {
                                    caching = yearNow - year;
                                } else {
                                    caching = yearNow - year - 1;
                                }
                            } else {
                                caching = yearNow - year - 1;
                            }
                        }
                        jyhgzNx = String.valueOf(caching);
                        tjy2Gjj.setJyhgzNx(jyhgzNx);
                    }
                    //主任助理任职时间
                    if (zrzlrzSjA != null) {
                        if (zrzlrzSjA.getTime() <= jysj.getTime()) {
                            inDatebase.setTime(jysj);
                        } else {
                            inDatebase.setTime(zrzlrzSjA);
                        }
                        int year = inDatebase.get(Calendar.YEAR);//获取年份
                        int month = inDatebase.get(Calendar.MONTH) + 1;//获取月份
                        int day = inDatebase.get(Calendar.DAY_OF_MONTH);//获取号数

                        //更新主任助理任职年限和缴费基数
                        int caching = 0;
                        if (fzrzSj == null && zzrzSj == null) {
                            if (yearNow == year) {
                                caching = 0;
                            } else if (yearNow > year) {
                                if (monthNow > month) {
                                    caching = yearNow - year;
                                } else if (monthNow == month) {
                                    if (dayNow >= day) {
                                        caching = yearNow - year;
                                    } else {
                                        caching = yearNow - year - 1;
                                    }
                                } else {
                                    caching = yearNow - year - 1;
                                }
                            }
                        } else if (fzrzSj != null) {
                            inDatebaseTemp.setTime(fzrzSj);
                            int yearTemp = inDatebaseTemp.get(Calendar.YEAR);//获取年份
                            int monthTemp = inDatebaseTemp.get(Calendar.MONTH) + 1;//获取月份
                            int dayTemp = inDatebaseTemp.get(Calendar.DAY_OF_MONTH);//获取号数
		  				    /*if(yearTemp == year){
		  						  caching=0;
		  					  }else if(yearTemp>year){
		  						  if(monthTemp>month){
		  							  caching=yearTemp-year;
		  						  }else if(monthTemp==month){
		  							  if(dayTemp>=day){
		  								  caching=yearTemp-year;
		  							  }else{
		  								  caching=yearTemp-year-1;
		  							  }
		  						  }else{
		  							  caching=yearTemp-year-1;
		  						  }
		  					  }*/
                            if (yearTemp == year) {
                                caching = 1;
                            } else if (yearTemp > year) {
                                caching = yearTemp - year;
                            }
                        } else if (fzrzSj == null && zzrzSj != null) {
                            inDatebaseTemp.setTime(zzrzSj);
                            int yearTemp = inDatebaseTemp.get(Calendar.YEAR);//获取年份
                            int monthTemp = inDatebaseTemp.get(Calendar.MONTH) + 1;//获取月份
                            int dayTemp = inDatebaseTemp.get(Calendar.DAY_OF_MONTH);//获取号数
		  				    /*if(yearTemp == year){
		  						  caching=0;
		  					  }else if(yearTemp>year){
		  						  if(monthTemp>month){
		  							  caching=yearTemp-year;
		  						  }else if(monthTemp==month){
		  							  if(dayTemp>=day){
		  								  caching=yearTemp-year;
		  							  }else{
		  								  caching=yearTemp-year-1;
		  							  }
		  						  }else{
		  							  caching=yearTemp-year-1;
		  						  }
		  					  }*/
                            if (yearTemp == year) {
                                caching = 1;
                            } else if (yearTemp > year) {
                                caching = yearTemp - year;
                            }
                        }
                        zrzlrzNx = String.valueOf(caching);
                        zjdyjfJb = String.valueOf(caching * 80);
                        tjy2Gjj.setZrzlrzNx(zrzlrzNx);
                        tjy2Gjj.setZjdyjfJb(zjdyjfJb);
                    }
                    //副职任职时间
                    if (fzrzSj != null) {
                        if (fzrzSj.getTime() <= jysj.getTime()) {
                            inDatebase.setTime(jysj);
                        } else {
                            inDatebase.setTime(fzrzSj);
                        }
                        int year = inDatebase.get(Calendar.YEAR);//获取年份
                        int month = inDatebase.get(Calendar.MONTH) + 1;//获取月份
                        int day = inDatebase.get(Calendar.DAY_OF_MONTH);//获取号数
                        //更新副职任职年限和缴费基数
                        int caching = 0;
                        if (zzrzSj == null) {
                            if (yearNow == year) {
                                caching = 0;
                            } else if (yearNow > year) {
                                if (monthNow > month) {
                                    caching = yearNow - year;
                                } else if (monthNow == month) {
                                    if (dayNow >= day) {
                                        caching = yearNow - year;
                                    } else {
                                        caching = yearNow - year - 1;
                                    }
                                } else {
                                    caching = yearNow - year - 1;
                                }
                            }
                        } else if (zzrzSj != null) {
                            inDatebaseTemp.setTime(zzrzSj);
                            int yearTemp = inDatebaseTemp.get(Calendar.YEAR);//获取年份
                            int monthTemp = inDatebaseTemp.get(Calendar.MONTH) + 1;//获取月份
                            int dayTemp = inDatebaseTemp.get(Calendar.DAY_OF_MONTH);//获取号数
		  				    /*if(yearTemp == year){
		  						  caching=0;
		  					  }else if(yearTemp>year){
		  						  if(monthTemp>month){
		  							  caching=yearTemp-year;
		  						  }else if(monthTemp==month){
		  							  if(dayTemp>=day){
		  								  caching=yearTemp-year;
		  							  }else{
		  								  caching=yearTemp-year-1;
		  							  }
		  						  }else{
		  							  caching=yearTemp-year-1;
		  						  }
		  					  }*/
                            if (yearTemp == year) {
                                caching = 1;
                            } else if (yearTemp > year) {
                                caching = yearTemp - year;
                            }
                        }

                        fzrzNx = String.valueOf(caching);
                        zjdyfJsC = String.valueOf(caching * 100);
                        tjy2Gjj.setFzrzNx(fzrzNx);
                        tjy2Gjj.setZjdyfJsC(zjdyfJsC);
                    }
                    //正职任职时间
                    if (zzrzSj != null) {
                        if (zzrzSj.getTime() <= jysj.getTime()) {
                            inDatebase.setTime(jysj);
                        } else {
                            inDatebase.setTime(zzrzSj);
                        }
                        int year = inDatebase.get(Calendar.YEAR);//获取年份
                        int month = inDatebase.get(Calendar.MONTH) + 1;//获取月份
                        int day = inDatebase.get(Calendar.DAY_OF_MONTH);//获取号数
                        //更新正职任职年限和缴费基数
                        int caching = 0;
                        if (yearNow == year) {
                            caching = 0;
                        } else if (yearNow > year) {
                            if (monthNow > month) {
                                caching = yearNow - year;
                            } else if (monthNow == month) {
                                if (dayNow >= day) {
                                    caching = yearNow - year;
                                } else {
                                    caching = yearNow - year - 1;
                                }
                            } else {
                                caching = yearNow - year - 1;
                            }
                        }
                        zzrzNx = String.valueOf(caching);
                        zjdyfJsD = String.valueOf(caching * 120);
                        tjy2Gjj.setZzrzNx(zzrzNx);
                        tjy2Gjj.setZjdyfJsD(zjdyfJsD);
                    }
                    //工程师任职时间
                    if (gcsrzSj != null) {
                        if (gcsrzSj.getTime() <= jysj.getTime()) {
                            inDatebase.setTime(jysj);
                        } else {
                            inDatebase.setTime(gcsrzSj);
                        }
                        int year = inDatebase.get(Calendar.YEAR);//获取年份
                        int month = inDatebase.get(Calendar.MONTH) + 1;//获取月份
                        int day = inDatebase.get(Calendar.DAY_OF_MONTH);//获取号数
                        //更新工程师任职年限和缴费基数
                        int caching = 0;
                        if (gjgcsrzSj == null) {
                            if (yearNow == year) {
                                caching = 0;
                            } else if (yearNow > year) {
                                if (monthNow > month) {
                                    caching = yearNow - year;
                                } else if (monthNow == month) {
                                    if (dayNow >= day) {
                                        caching = yearNow - year;
                                    } else {
                                        caching = yearNow - year - 1;
                                    }
                                } else {
                                    caching = yearNow - year - 1;
                                }
                            }
                        } else if (gjgcsrzSj != null) {
                            inDatebaseTemp.setTime(gjgcsrzSj);
                            int yearTemp = inDatebaseTemp.get(Calendar.YEAR);//获取年份
                            int monthTemp = inDatebaseTemp.get(Calendar.MONTH) + 1;//获取月份
                            int dayTemp = inDatebaseTemp.get(Calendar.DAY_OF_MONTH);//获取号数
		  				    /*if(yearTemp == year){
		  						  caching=0;
		  					  }else if(yearTemp>year){
		  						  if(monthTemp>month){
		  							  caching=yearTemp-year;
		  						  }else if(monthTemp==month){
		  							  if(dayTemp>=day){
		  								  caching=yearTemp-year;
		  							  }else{
		  								  caching=yearTemp-year-1;
		  							  }
		  						  }else{
		  							  caching=yearTemp-year-1;
		  						  }
		  					  }*/
                            if (yearTemp == year) {
                                caching = 1;
                            } else if (yearTemp > year) {
                                caching = yearTemp - year;
                            }
                        }

                        gcsrzNx = String.valueOf(caching);
                        zjdyfJsE = String.valueOf(caching * 100);
                        tjy2Gjj.setGcsrzNx(gcsrzNx);
                        tjy2Gjj.setZjdyfJsE(zjdyfJsE);
                    }
                    //高级工程师任职时间
                    if (gjgcsrzSj != null) {
                        if (gjgcsrzSj.getTime() <= jysj.getTime()) {
                            inDatebase.setTime(jysj);
                        } else {
                            inDatebase.setTime(gjgcsrzSj);
                        }
                        int year = inDatebase.get(Calendar.YEAR);//获取年份
                        int month = inDatebase.get(Calendar.MONTH) + 1;//获取月份
                        int day = inDatebase.get(Calendar.DAY_OF_MONTH);//获取号数
                        //更新高级工程师任职年限和缴费基数
                        int caching = 0;
                        if (yearNow == year) {
                            caching = 0;
                        } else if (yearNow > year) {
                            if (monthNow > month) {
                                caching = yearNow - year;
                            } else if (monthNow == month) {
                                if (dayNow >= day) {
                                    caching = yearNow - year;
                                } else {
                                    caching = yearNow - year - 1;
                                }
                            } else {
                                caching = yearNow - year - 1;
                            }
                        }
                        gjgcsrzNx = String.valueOf(caching);
                        zjdyfJsF = String.valueOf(caching * 120);
                        tjy2Gjj.setGjgcsrzNx(gjgcsrzNx);
                        tjy2Gjj.setZjdyfJsF(zjdyfJsF);
                    }
                    //检验师任职时间
                    if (jysrz_Sj != null) {
                        if (jysrz_Sj.getTime() <= jysj.getTime()) {
                            inDatebase.setTime(jysj);
                        } else {
                            inDatebase.setTime(jysrz_Sj);
                        }
                        int year = inDatebase.get(Calendar.YEAR);//获取年份
                        int month = inDatebase.get(Calendar.MONTH) + 1;//获取月份
                        int day = inDatebase.get(Calendar.DAY_OF_MONTH);//获取号数
                        //更新检验师任职年限和缴费基数
                        int caching = 0;
                        if (yearNow == year) {
                            caching = 0;
                        } else if (yearNow > year) {
                            if (monthNow > month) {
                                caching = yearNow - year;
                            } else if (monthNow == month) {
                                if (dayNow >= day) {
                                    caching = yearNow - year;
                                } else {
                                    caching = yearNow - year - 1;
                                }
                            } else {
                                caching = yearNow - year - 1;
                            }
                        }
                        jysrzNx = String.valueOf(caching);
                        zjdyfJsG = String.valueOf(caching * 100);
                        tjy2Gjj.setJysrzNx(jysrzNx);
                        tjy2Gjj.setZjdyfJsG(zjdyfJsG);
                    }
                    int ybgzrnrz_nx = 0;
                    if (tjy2Gjj.getYbgzryrzNx() != null && tjy2Gjj.getYbgzryrzNx().length() > 0) {
                        ybgzrnrz_nx = Integer.parseInt(tjy2Gjj.getYbgzryrzNx());
                    }
                    int zrzlrz_nx = 0;
                    if (tjy2Gjj.getZrzlrzNx() != null && tjy2Gjj.getZrzlrzNx().length() > 0) {

                        zrzlrz_nx = Integer.parseInt(tjy2Gjj.getZrzlrzNx());
                    }

                    int fzrz_nx = 0;
                    if (tjy2Gjj.getFzrzNx() != null && tjy2Gjj.getFzrzNx().length() > 0) {
                        fzrz_nx = Integer.parseInt(tjy2Gjj.getFzrzNx());
                    }
                    int zzrz_nx = 0;
                    if (tjy2Gjj.getZzrzNx() != null && tjy2Gjj.getZzrzNx().length() > 0) {
                        zzrz_nx = Integer.parseInt(tjy2Gjj.getZzrzNx());
                    }

                    int gcsrz_nx = 0;
                    if (tjy2Gjj.getGcsrzNx() != null && tjy2Gjj.getGcsrzNx().length() > 0) {
                        gcsrz_nx = Integer.parseInt(tjy2Gjj.getGcsrzNx());
                    }
                    int gjgcsrz_nx = 0;
                    if (tjy2Gjj.getGjgcsrzNx() != null && tjy2Gjj.getGjgcsrzNx().length() > 0) {
                        gjgcsrz_nx = Integer.parseInt(tjy2Gjj.getGjgcsrzNx());
                    }
                    int jysrz_nx = 0;
                    if (tjy2Gjj.getJysrzNx() != null && tjy2Gjj.getJysrzNx().length() > 0) {
                        jysrz_nx = Integer.parseInt(tjy2Gjj.getJysrzNx());
                    }

                    int jyhgznx = 0;
                    if (tjy2Gjj.getJyhgzNx() != null && tjy2Gjj.getJyhgzNx().length() > 0) {
                        jyhgznx = Integer.parseInt(tjy2Gjj.getJyhgzNx());
                    }


                    tjy2Gjj.setYbgzryrzNx(String.valueOf(jyhgznx - zrzlrz_nx - fzrz_nx - zzrz_nx));
                    tjy2Gjj.setZjdyjfJs(String.valueOf((jyhgznx - zrzlrz_nx - fzrz_nx - zzrz_nx) * 60));
                    tjy2Gjj.setYbgzryrzNx_a(String.valueOf(jyhgznx - gcsrz_nx - gjgcsrz_nx));
                    tjy2Gjj.setZjdyjfJs_a(String.valueOf((jyhgznx - gcsrz_nx - gjgcsrz_nx) * 60));
                    tjy2Gjj.setYbgzryrzNx_b(String.valueOf(jyhgznx - jysrz_nx));
                    tjy2Gjj.setZjdyjfJs_b(String.valueOf((jyhgznx - jysrz_nx) * 60));
                    int yzjfjs = 0;
                    int zjfjs1 =
							(jyhgznx - zrzlrz_nx - fzrz_nx - zzrz_nx) * 60 + zrzlrz_nx * 80 + fzrz_nx * 100 + zzrz_nx * 120;
                    int zjfjs2 = (jyhgznx - gcsrz_nx - gjgcsrz_nx) * 60 + gcsrz_nx * 100 + gjgcsrz_nx * 120;
                    int zjfjs3 = (jyhgznx - jysrz_nx) * 60 + (jysrz_nx * 100);
                    if (zjfjs1 < zjfjs2) {
                        yzjfjs = zjfjs2;
                        if (yzjfjs < zjfjs3) {
                            yzjfjs = zjfjs3;
                        }
                    } else {
                        yzjfjs = zjfjs1;
                        if (yzjfjs < zjfjs3) {
                            yzjfjs = zjfjs3;
                        }
                    }
                    int syqmyjfjs = 0;
                    if (tjy2Gjj.getSyqmyjyJs() != null && tjy2Gjj.getSyqmyjyJs().length() > 0) {
                        syqmyjfjs = Integer.parseInt(tjy2Gjj.getSyqmyjyJs());
                    }
                    int zjfjs = 0;
                    zjfjs = yzjfjs + syqmyjfjs;
                    tjy2Gjj.setZjfJs(String.valueOf(zjfjs));
                    Double tzhyjfje1 = zjfjs * 0.24;
                    DecimalFormat fmt = new DecimalFormat("0.#");
                    String tzhyjfje = fmt.format(tzhyjfje1);
                    tjy2Gjj.setTzhyjfJe(tzhyjfje);
                    //个人缴费金额
                    Double mqjfJe1 = tzhyjfje1 * 0.5;
                    String mqjfJe = fmt.format(mqjfJe1);
                    tjy2Gjj.setMqjfJe(mqjfJe);
//	  		  		tjy2Gjj.setCjr(SecurityUtil.getSecurityUser().getName());
                    tjy2Gjj.setCjSj(new Date());
                    if (tjy2Gjj.getId() == null || tjy2Gjj.getId().equals("")) {
                        tjy2Gjj.setBrqr("0");
                    }

                    tjy2GjjDao.save(tjy2Gjj);
                    // 写入系统日志
                    try {
                        logService.setLogs(tjy2Gjj.getId(), "公积金年度自更新", "公积金的最新修改人", "", "系统",
                                new Date(), "");
                    } catch (Exception e) {
                        System.out.println("@@@@@@@@@@@@@@@@@公积金修改系统更新操作失败！！@@@@@@@@@@@@@@@@@@@@@@@");
                        e.printStackTrace();
                    }
                }
            }
        }
        return list;
    }

    public List<Tjy2Gjj> updateBasicByHand() throws Exception {
        Calendar now = Calendar.getInstance();
        Calendar inDatebase = Calendar.getInstance();
        Calendar inDatebaseTemp = Calendar.getInstance();
        int yearNow = now.get(Calendar.YEAR);//获取当前年份
        int monthNow = 1;//赋值更新月份
        int dayNow = 1;//赋值更新日
        List<Tjy2Gjj> list = null;
        System.out.println("------------------------------------------当前年份: " + yearNow);
        System.out.println("------------------------------------------当前月份: " + monthNow);
        System.out.println("------------------------------------------当前日: " + dayNow);
        if (monthNow == 1 && dayNow == 1) {
            list = tjy2GjjDao.getGjjs();
            if (list.size() > 0 && list != null) {
                for (Tjy2Gjj tjy2Gjj : list) {
                    Date jysj = tjy2Gjj.getJySj();//进院时间
                    Date zrzlrzSjA = tjy2Gjj.getZrzlrzSjA();//主任助理任职时间
                    Date fzrzSj = tjy2Gjj.getFzrzSj();//副职任职时间
                    Date zzrzSj = tjy2Gjj.getZzrzSj();//正职任职时间
                    Date gcsrzSj = tjy2Gjj.getGcsrzSj();//工程师任职时间
                    Date gjgcsrzSj = tjy2Gjj.getGjgcsrzSj();//高级工程师任职时间
                    Date jysrz_Sj = tjy2Gjj.getJysrz_Sj();//检验师任职时间

                    String jyhgzNx;//进院后工作年限

                    String zrzlrzNx;//主任助理任职年限（年）
                    String zjdyjfJb;//增加的月缴费基数
                    String fzrzNx;//副职任职年限（年）
                    String zjdyfJsC;//增加的月缴费基数
                    String zzrzNx;//正职任职年限（年）
                    String zjdyfJsD;//增加的月缴费基数

                    String gcsrzNx;//工程师任职年限（年）
                    String zjdyfJsE;//增加的月缴费基数
                    String gjgcsrzNx;//高级工程师任职年限
                    String zjdyfJsF;//增加的缴费基数

                    String jysrzNx;//检验师任职年限
                    String zjdyfJsG;//增加的缴费基数
	  				    
	  				  /*String ybgzryrzNx;//一般工作人员任职年限（年）
	  				  String ybgzryrzNx_a;//一般工作人员任职年限（年）
	  				  String ybgzryrzNx_b;//一般工作人员任职年限（年）
	  				  String zjdyjfJs;//一般工作人员增加的月缴费基数
	  				  String zjdyjfJs_a;//一般工作人员增加的月缴费基数
	  				  String zjdyjfJs_b;//一般工作人员增加的月缴费基数  
	  */
                    if (jysj != null) {
                        inDatebase.setTime(jysj);
                        int year = inDatebase.get(Calendar.YEAR);//获取年份
                        int month = inDatebase.get(Calendar.MONTH) + 1;//获取月份
                        int day = inDatebase.get(Calendar.DAY_OF_MONTH);//获取号数
                        //更新进院后工作年限和第一个缴费基数
                        int caching = 0;
                        System.out.println("------------------------------------------进院年份：" + year);
                        System.out.println("------------------------------------------进院月份：" + month);
                        System.out.println("------------------------------------------进院日：" + day);
                        if (yearNow == year) {
                            caching = 0;
                        } else if (yearNow > year) {
                            if (monthNow > month) {
                                caching = yearNow - year;
                            } else if (monthNow == month) {
                                if (dayNow >= day) {
                                    caching = yearNow - year;
                                } else {
                                    caching = yearNow - year - 1;
                                }
                            } else {
                                caching = yearNow - year - 1;
                            }
                        }
                        jyhgzNx = String.valueOf(caching);
                        tjy2Gjj.setJyhgzNx(jyhgzNx);
                    }
                    //主任助理任职时间
                    if (zrzlrzSjA != null) {
                        if (zrzlrzSjA.getTime() <= jysj.getTime()) {
                            inDatebase.setTime(jysj);
                        } else {
                            inDatebase.setTime(zrzlrzSjA);
                        }
                        int year = inDatebase.get(Calendar.YEAR);//获取年份
                        int month = inDatebase.get(Calendar.MONTH) + 1;//获取月份
                        int day = inDatebase.get(Calendar.DAY_OF_MONTH);//获取号数

                        //更新主任助理任职年限和缴费基数
                        int caching = 0;
                        if (fzrzSj == null && zzrzSj == null) {
                            if (yearNow == year) {
                                caching = 0;
                            } else if (yearNow > year) {
                                if (monthNow > month) {
                                    caching = yearNow - year;
                                } else if (monthNow == month) {
                                    if (dayNow >= day) {
                                        caching = yearNow - year;
                                    } else {
                                        caching = yearNow - year - 1;
                                    }
                                } else {
                                    caching = yearNow - year - 1;
                                }
                            }
                        } else if (fzrzSj != null) {
                            inDatebaseTemp.setTime(fzrzSj);
                            int yearTemp = inDatebaseTemp.get(Calendar.YEAR);//获取年份
                            int monthTemp = inDatebaseTemp.get(Calendar.MONTH) + 1;//获取月份
                            int dayTemp = inDatebaseTemp.get(Calendar.DAY_OF_MONTH);//获取号数
		  				    /*if(yearTemp == year){
		  						  caching=0;
		  					  }else if(yearTemp>year){
		  						  if(monthTemp>month){
		  							  caching=yearTemp-year;
		  						  }else if(monthTemp==month){
		  							  if(dayTemp>=day){
		  								  caching=yearTemp-year;
		  							  }else{
		  								  caching=yearTemp-year-1;
		  							  }
		  						  }else{
		  							  caching=yearTemp-year-1;
		  						  }
		  					  }*/
                            if (yearTemp == year) {
                                caching = 1;
                            } else if (yearTemp > year) {
                                caching = yearTemp - year;
                            }
                        } else if (fzrzSj == null && zzrzSj != null) {
                            inDatebaseTemp.setTime(zzrzSj);
                            int yearTemp = inDatebaseTemp.get(Calendar.YEAR);//获取年份
                            int monthTemp = inDatebaseTemp.get(Calendar.MONTH) + 1;//获取月份
                            int dayTemp = inDatebaseTemp.get(Calendar.DAY_OF_MONTH);//获取号数
		  				    /*if(yearTemp == year){
		  						  caching=0;
		  					  }else if(yearTemp>year){
		  						  if(monthTemp>month){
		  							  caching=yearTemp-year;
		  						  }else if(monthTemp==month){
		  							  if(dayTemp>=day){
		  								  caching=yearTemp-year;
		  							  }else{
		  								  caching=yearTemp-year-1;
		  							  }
		  						  }else{
		  							  caching=yearTemp-year-1;
		  						  }
		  					  }*/
                            if (yearTemp == year) {
                                caching = 1;
                            } else if (yearTemp > year) {
                                caching = yearTemp - year;
                            }
                        }
                        zrzlrzNx = String.valueOf(caching);
                        zjdyjfJb = String.valueOf(caching * 80);
                        tjy2Gjj.setZrzlrzNx(zrzlrzNx);
                        tjy2Gjj.setZjdyjfJb(zjdyjfJb);
                    }
                    //副职任职时间
                    if (fzrzSj != null) {
                        if (fzrzSj.getTime() <= jysj.getTime()) {
                            inDatebase.setTime(jysj);
                        } else {
                            inDatebase.setTime(fzrzSj);
                        }
                        int year = inDatebase.get(Calendar.YEAR);//获取年份
                        int month = inDatebase.get(Calendar.MONTH) + 1;//获取月份
                        int day = inDatebase.get(Calendar.DAY_OF_MONTH);//获取号数
                        //更新副职任职年限和缴费基数
                        int caching = 0;
                        if (zzrzSj == null) {
                            if (yearNow == year) {
                                caching = 0;
                            } else if (yearNow > year) {
                                if (monthNow > month) {
                                    caching = yearNow - year;
                                } else if (monthNow == month) {
                                    if (dayNow >= day) {
                                        caching = yearNow - year;
                                    } else {
                                        caching = yearNow - year - 1;
                                    }
                                } else {
                                    caching = yearNow - year - 1;
                                }
                            }
                        } else if (zzrzSj != null) {
                            inDatebaseTemp.setTime(zzrzSj);
                            int yearTemp = inDatebaseTemp.get(Calendar.YEAR);//获取年份
                            int monthTemp = inDatebaseTemp.get(Calendar.MONTH) + 1;//获取月份
                            int dayTemp = inDatebaseTemp.get(Calendar.DAY_OF_MONTH);//获取号数
		  				    /*if(yearTemp == year){
		  						  caching=0;
		  					  }else if(yearTemp>year){
		  						  if(monthTemp>month){
		  							  caching=yearTemp-year;
		  						  }else if(monthTemp==month){
		  							  if(dayTemp>=day){
		  								  caching=yearTemp-year;
		  							  }else{
		  								  caching=yearTemp-year-1;
		  							  }
		  						  }else{
		  							  caching=yearTemp-year-1;
		  						  }
		  					  }*/
                            if (yearTemp == year) {
                                caching = 1;
                            } else if (yearTemp > year) {
                                caching = yearTemp - year;
                            }
                        }

                        fzrzNx = String.valueOf(caching);
                        zjdyfJsC = String.valueOf(caching * 100);
                        tjy2Gjj.setFzrzNx(fzrzNx);
                        tjy2Gjj.setZjdyfJsC(zjdyfJsC);
                    }
                    //正职任职时间
                    if (zzrzSj != null) {
                        if (zzrzSj.getTime() <= jysj.getTime()) {
                            inDatebase.setTime(jysj);
                        } else {
                            inDatebase.setTime(zzrzSj);
                        }
                        int year = inDatebase.get(Calendar.YEAR);//获取年份
                        int month = inDatebase.get(Calendar.MONTH) + 1;//获取月份
                        int day = inDatebase.get(Calendar.DAY_OF_MONTH);//获取号数
                        //更新正职任职年限和缴费基数
                        int caching = 0;
                        if (yearNow == year) {
                            caching = 0;
                        } else if (yearNow > year) {
                            if (monthNow > month) {
                                caching = yearNow - year;
                            } else if (monthNow == month) {
                                if (dayNow >= day) {
                                    caching = yearNow - year;
                                } else {
                                    caching = yearNow - year - 1;
                                }
                            } else {
                                caching = yearNow - year - 1;
                            }
                        }
                        zzrzNx = String.valueOf(caching);
                        zjdyfJsD = String.valueOf(caching * 120);
                        tjy2Gjj.setZzrzNx(zzrzNx);
                        tjy2Gjj.setZjdyfJsD(zjdyfJsD);
                    }
                    //工程师任职时间
                    if (gcsrzSj != null) {
                        if (gcsrzSj.getTime() <= jysj.getTime()) {
                            inDatebase.setTime(jysj);
                        } else {
                            inDatebase.setTime(gcsrzSj);
                        }
                        int year = inDatebase.get(Calendar.YEAR);//获取年份
                        int month = inDatebase.get(Calendar.MONTH) + 1;//获取月份
                        int day = inDatebase.get(Calendar.DAY_OF_MONTH);//获取号数
                        //更新工程师任职年限和缴费基数
                        int caching = 0;
                        if (gjgcsrzSj == null) {
                            if (yearNow == year) {
                                caching = 0;
                            } else if (yearNow > year) {
                                if (monthNow > month) {
                                    caching = yearNow - year;
                                } else if (monthNow == month) {
                                    if (dayNow >= day) {
                                        caching = yearNow - year;
                                    } else {
                                        caching = yearNow - year - 1;
                                    }
                                } else {
                                    caching = yearNow - year - 1;
                                }
                            }
                        } else if (gjgcsrzSj != null) {
                            inDatebaseTemp.setTime(gjgcsrzSj);
                            int yearTemp = inDatebaseTemp.get(Calendar.YEAR);//获取年份
                            int monthTemp = inDatebaseTemp.get(Calendar.MONTH) + 1;//获取月份
                            int dayTemp = inDatebaseTemp.get(Calendar.DAY_OF_MONTH);//获取号数
		  				    /*if(yearTemp == year){
		  						  caching=0;
		  					  }else if(yearTemp>year){
		  						  if(monthTemp>month){
		  							  caching=yearTemp-year;
		  						  }else if(monthTemp==month){
		  							  if(dayTemp>=day){
		  								  caching=yearTemp-year;
		  							  }else{
		  								  caching=yearTemp-year-1;
		  							  }
		  						  }else{
		  							  caching=yearTemp-year-1;
		  						  }
		  					  }*/
                            if (yearTemp == year) {
                                caching = 1;
                            } else if (yearTemp > year) {
                                caching = yearTemp - year;
                            }
                        }

                        gcsrzNx = String.valueOf(caching);
                        zjdyfJsE = String.valueOf(caching * 100);
                        tjy2Gjj.setGcsrzNx(gcsrzNx);
                        tjy2Gjj.setZjdyfJsE(zjdyfJsE);
                    }
                    //高级工程师任职时间
                    if (gjgcsrzSj != null) {
                        if (gjgcsrzSj.getTime() <= jysj.getTime()) {
                            inDatebase.setTime(jysj);
                        } else {
                            inDatebase.setTime(gjgcsrzSj);
                        }
                        int year = inDatebase.get(Calendar.YEAR);//获取年份
                        int month = inDatebase.get(Calendar.MONTH) + 1;//获取月份
                        int day = inDatebase.get(Calendar.DAY_OF_MONTH);//获取号数
                        //更新高级工程师任职年限和缴费基数
                        int caching = 0;
                        if (yearNow == year) {
                            caching = 0;
                        } else if (yearNow > year) {
                            if (monthNow > month) {
                                caching = yearNow - year;
                            } else if (monthNow == month) {
                                if (dayNow >= day) {
                                    caching = yearNow - year;
                                } else {
                                    caching = yearNow - year - 1;
                                }
                            } else {
                                caching = yearNow - year - 1;
                            }
                        }
                        gjgcsrzNx = String.valueOf(caching);
                        zjdyfJsF = String.valueOf(caching * 120);
                        tjy2Gjj.setGjgcsrzNx(gjgcsrzNx);
                        tjy2Gjj.setZjdyfJsF(zjdyfJsF);
                    }
                    //检验师任职时间
                    if (jysrz_Sj != null) {
                        if (jysrz_Sj.getTime() <= jysj.getTime()) {
                            inDatebase.setTime(jysj);
                        } else {
                            inDatebase.setTime(jysrz_Sj);
                        }
                        int year = inDatebase.get(Calendar.YEAR);//获取年份
                        int month = inDatebase.get(Calendar.MONTH) + 1;//获取月份
                        int day = inDatebase.get(Calendar.DAY_OF_MONTH);//获取号数
                        //更新检验师任职年限和缴费基数
                        int caching = 0;
                        if (yearNow == year) {
                            caching = 0;
                        } else if (yearNow > year) {
                            if (monthNow > month) {
                                caching = yearNow - year;
                            } else if (monthNow == month) {
                                if (dayNow >= day) {
                                    caching = yearNow - year;
                                } else {
                                    caching = yearNow - year - 1;
                                }
                            } else {
                                caching = yearNow - year - 1;
                            }
                        }
                        jysrzNx = String.valueOf(caching);
                        zjdyfJsG = String.valueOf(caching * 100);
                        tjy2Gjj.setJysrzNx(jysrzNx);
                        tjy2Gjj.setZjdyfJsG(zjdyfJsG);
                    }
                    int ybgzrnrz_nx = 0;
                    if (tjy2Gjj.getYbgzryrzNx() != null && tjy2Gjj.getYbgzryrzNx().length() > 0) {
                        ybgzrnrz_nx = Integer.parseInt(tjy2Gjj.getYbgzryrzNx());
                    }
                    int zrzlrz_nx = 0;
                    if (tjy2Gjj.getZrzlrzNx() != null && tjy2Gjj.getZrzlrzNx().length() > 0) {

                        zrzlrz_nx = Integer.parseInt(tjy2Gjj.getZrzlrzNx());
                    }

                    int fzrz_nx = 0;
                    if (tjy2Gjj.getFzrzNx() != null && tjy2Gjj.getFzrzNx().length() > 0) {
                        fzrz_nx = Integer.parseInt(tjy2Gjj.getFzrzNx());
                    }
                    int zzrz_nx = 0;
                    if (tjy2Gjj.getZzrzNx() != null && tjy2Gjj.getZzrzNx().length() > 0) {
                        zzrz_nx = Integer.parseInt(tjy2Gjj.getZzrzNx());
                    }

                    int gcsrz_nx = 0;
                    if (tjy2Gjj.getGcsrzNx() != null && tjy2Gjj.getGcsrzNx().length() > 0) {
                        gcsrz_nx = Integer.parseInt(tjy2Gjj.getGcsrzNx());
                    }
                    int gjgcsrz_nx = 0;
                    if (tjy2Gjj.getGjgcsrzNx() != null && tjy2Gjj.getGjgcsrzNx().length() > 0) {
                        gjgcsrz_nx = Integer.parseInt(tjy2Gjj.getGjgcsrzNx());
                    }
                    int jysrz_nx = 0;
                    if (tjy2Gjj.getJysrzNx() != null && tjy2Gjj.getJysrzNx().length() > 0) {
                        jysrz_nx = Integer.parseInt(tjy2Gjj.getJysrzNx());
                    }

                    int jyhgznx = 0;
                    if (tjy2Gjj.getJyhgzNx() != null && tjy2Gjj.getJyhgzNx().length() > 0) {
                        jyhgznx = Integer.parseInt(tjy2Gjj.getJyhgzNx());
                    }


                    tjy2Gjj.setYbgzryrzNx(String.valueOf(jyhgznx - zrzlrz_nx - fzrz_nx - zzrz_nx));
                    tjy2Gjj.setZjdyjfJs(String.valueOf((jyhgznx - zrzlrz_nx - fzrz_nx - zzrz_nx) * 60));
                    tjy2Gjj.setYbgzryrzNx_a(String.valueOf(jyhgznx - gcsrz_nx - gjgcsrz_nx));
                    tjy2Gjj.setZjdyjfJs_a(String.valueOf((jyhgznx - gcsrz_nx - gjgcsrz_nx) * 60));
                    tjy2Gjj.setYbgzryrzNx_b(String.valueOf(jyhgznx - jysrz_nx));
                    tjy2Gjj.setZjdyjfJs_b(String.valueOf((jyhgznx - jysrz_nx) * 60));
                    int yzjfjs = 0;
                    int zjfjs1 = (jyhgznx - zrzlrz_nx - fzrz_nx - zzrz_nx) * 60 + zrzlrz_nx * 80 + fzrz_nx * 100 + zzrz_nx * 120;
                    int zjfjs2 = (jyhgznx - gcsrz_nx - gjgcsrz_nx) * 60 + gcsrz_nx * 100 + gjgcsrz_nx * 120;
                    int zjfjs3 = (jyhgznx - jysrz_nx) * 60 + (jysrz_nx * 100);
                    if (zjfjs1 < zjfjs2) {
                        yzjfjs = zjfjs2;
                        if (yzjfjs < zjfjs3) {
                            yzjfjs = zjfjs3;
                        }
                    } else {
                        yzjfjs = zjfjs1;
                        if (yzjfjs < zjfjs3) {
                            yzjfjs = zjfjs3;
                        }
                    }
                    int syqmyjfjs = 0;
                    if (tjy2Gjj.getSyqmyjyJs() != null && tjy2Gjj.getSyqmyjyJs().length() > 0) {
                        syqmyjfjs = Integer.parseInt(tjy2Gjj.getSyqmyjyJs());
                    }
                    int zjfjs = 0;
                    zjfjs = yzjfjs + syqmyjfjs;
                    tjy2Gjj.setZjfJs(String.valueOf(zjfjs));
                    Double tzhyjfje1 = zjfjs * 0.24;
                    DecimalFormat fmt = new DecimalFormat("0.#");
                    String tzhyjfje = fmt.format(tzhyjfje1);
                    tjy2Gjj.setTzhyjfJe(tzhyjfje);
                    //个人缴费金额
                    Double mqjfJe1 = tzhyjfje1 * 0.5;
                    String mqjfJe = fmt.format(mqjfJe1);
                    tjy2Gjj.setMqjfJe(mqjfJe);
//	  		  		tjy2Gjj.setCjr(SecurityUtil.getSecurityUser().getName());
                    tjy2Gjj.setCjSj(new Date());
                    if (tjy2Gjj.getId() == null || tjy2Gjj.getId().equals("")) {
                        tjy2Gjj.setBrqr("0");
                    }

                    tjy2GjjDao.save(tjy2Gjj);
                    // 写入系统日志
                    try {
                        logService.setLogs(tjy2Gjj.getId(), "公积金年度自更新", "公积金的最新修改人", "", "系统",
                                new Date(), "");
                    } catch (Exception e) {
                        System.out.println("@@@@@@@@@@@@@@@@@公积金修改系统更新操作失败！！@@@@@@@@@@@@@@@@@@@@@@@");
                        e.printStackTrace();
                    }
                }
            }
        }
        return list;
    }

    //删除公积金信息
    public void delete(String id) {
        // TODO 自动生成的方法存根
        Tjy2Gjj gjj = tjy2GjjDao.get(id);
        gjj.setData_status("99");
        tjy2GjjDao.save(gjj);
    }

    /**
     * 设置离职标记
     *
     * @param nameId employee_base.id
     */
    public void setDimissionIdent(String nameId) {
        Tjy2Gjj gjj = this.tjy2GjjDao.getGjjByEmp(nameId);
        if (gjj != null) {
            gjj.setData_status("98");
            this.tjy2GjjDao.save(gjj);
        } else {
            this.logger.error("该员工没有公积金信息,TJY2_RL_EMPLOYEE_BASE.id = {}", nameId);
        }
    }

    /**
     * 设置基本信息关键字修改标记
     *
     * @param nameId employee_base.id
     */
    public void setModifyIdent(String nameId) {
        Tjy2Gjj gjj = this.tjy2GjjDao.getGjjByEmp(nameId);
        if (gjj != null) {
            gjj.setBrqr("0");
            gjj.setData_status("97");
            this.tjy2GjjDao.save(gjj);
        } else {
            this.logger.error("该员工没有公积金信息,TJY2_RL_EMPLOYEE_BASE.id = {}", nameId);
        }
    }
}
