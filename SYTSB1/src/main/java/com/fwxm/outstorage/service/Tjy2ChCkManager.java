package com.fwxm.outstorage.service;

import com.fwxm.material.bean.Goods;
import com.fwxm.material.bean.GoodsAndOrder;
import com.fwxm.material.dao.GoodsAndOrderDao;
import com.fwxm.material.dao.GoodsDao;
import com.fwxm.outstorage.bean.OutStorageParam;
import com.fwxm.outstorage.bean.Tjy2ChCk;
import com.fwxm.outstorage.bean.Tjy2ChCkStatus;
import com.fwxm.outstorage.bean.Tjy2ChCkType;
import com.fwxm.outstorage.dao.Tjy2ChCkDao;
import com.fwxm.outstorage.inf.OutStorageExtends;
import com.fwxm.outstorage.inf.OutStorageInf;
import com.khnt.core.crud.manager.impl.EntityManageImpl;
import com.khnt.core.crud.web.support.JsonWrapper;
import com.khnt.core.exception.KhntException;
import com.khnt.security.CurrentSessionUser;
import com.khnt.security.util.SecurityUtil;
import com.khnt.utils.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class Tjy2ChCkManager extends EntityManageImpl<Tjy2ChCk, Tjy2ChCkDao> {
    @Autowired
    private Tjy2ChCkDao tjy2ChCkDao;

    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private GoodsAndOrderDao goodsAndOrderDao;

    @Autowired
    private List<OutStorageExtends> outStorageExtends;

    /**
     * @param entity id为对应的单号id,type为所对应的单号类型
     * @param type
     * @return
     * @throws Exception
     */
    public synchronized HashMap<String, Object> outStorage(Tjy2ChCk entity, String type) throws Exception {
        log.debug("开始出库");
        OutStorageInf outStorageInf = getOutStorageImpl(type);

        log.debug("获取出库执行类：" + outStorageInf.getClass().getName());
        String orderId = entity.getId();//entity.getId() 这里传过来的是依据的单号ID
        boolean canOutStorage = outStorageInf.canOutStorage(orderId);
        if (!canOutStorage) {
            log.debug("单据无法出库，单号Id：【" + orderId + "】,单号类型：" + type);
            throw new KhntException("该单据无法进行出库，请刷新后重试或联系管理员处理");
        }
        outStorageInf.startOutStorage(orderId);
        log.debug("开始出库，单号Id：【" + orderId + "】,单号类型：" + type);
        List<GoodsAndOrder> realyGoods = entity.getGoods(); //实际要扣除的库存数量。
        String orderNum = outStorageInf.getOrderNumber(entity.getId()); //出库依据的单号(人眼可识别)
        log.debug("单号Num：【" + orderNum + "】");
        entity.setCkyjdh(orderNum);
        entity.setCkyjtype(type); //出库依据的类型

        CurrentSessionUser user = SecurityUtil.getSecurityUser();
        entity.setId(null);
        entity.setGoods(null);
        entity.setCreateId(user.getId());
        entity.setCreateName(user.getName());
        entity.setCreateOrgId(user.getDepartment().getId());
        entity.setCreateOrgName(user.getDepartment().getOrgName());
        entity.setCreateUnitId(user.getUnit().getId());
        entity.setCreateUnitName(user.getUnit().getOrgName());
        entity.setCkdbh(getCKBH(entity.getLqsj()));
        entity.setCkr(user.getName());
        Date now = new Date();
        entity.setCreateTime(now);
        entity.setStatus(Tjy2ChCkStatus.YCK); //已出库
        entity.setBxStatus(Tjy2ChCkStatus.BX_WBX); //未报销

        tjy2ChCkDao.save(entity);
        List<GoodsAndOrder> copies = new ArrayList<GoodsAndOrder>();
        JSONArray a = JSONArray.fromCollection(realyGoods);
        copies = JSONArray.toList(a, GoodsAndOrder.class);
        for (GoodsAndOrder gad : realyGoods) {
            Goods aGoods = goodsDao.get(gad.getGoods().getId());
            if (aGoods.getSl() < gad.getSl()) {
                throw new KhntException("【" + aGoods.getWpmc() + "】库存数量只有【" + aGoods.getSl() + "】不足【" + gad.getSl() + "】，请重新填写数量");
            } else {
                aGoods.setSl(aGoods.getSl() - gad.getSl());
                if (Tjy2ChCkType.TH.getKey().equals(type)) {
                    //退货单应扣去初始数量。
                    aGoods.setCssl(aGoods.getCssl() - gad.getSl());
                }
                goodsDao.save(aGoods);
            }
            gad.setType("CK");
            gad.setCrk_type(type);
            gad.setCreate_time(now);
            gad.setStatus("1");
            gad.setFk_order_id(entity.getId());
            if (Tjy2ChCkType.LQ.getKey().equals(type)) {
                gad.setSjlqsl(gad.getSl()); //初始化领取数量。
            }
            goodsAndOrderDao.save(gad);
        }
        JSONObject object = new JSONObject();
        object.put(OutStorageParam.ID, entity.getId());
        object.put(OutStorageParam.CKDBH, entity.getCkdbh());
        object.put(OutStorageParam.CK_TIME, entity.getCreateTime());
        object.put(OutStorageParam.LQ_TIME, entity.getLqsj());
        object.put(OutStorageParam.CK_ID, entity.getId());
        object.put(OutStorageParam.SQ_TIME, entity.getLqSqsj());
        outStorageInf.finishOutStorage(orderId, copies, object);
        log.debug("出库结束，单号Id：【" + orderId + "】,单号类型：" + type);
        return JsonWrapper.successWrapper(entity);
    }

    public OutStorageInf getOutStorageImpl(String type) {
        OutStorageInf outStorageExtend = null;
        for (OutStorageExtends outi : outStorageExtends) {
            if (outi.getType().equals(type)) {
                outStorageExtend = outi;
                break;
            }
        }
        return outStorageExtend;
    }

    private synchronized String getCKBH(Date cksj) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String bh= "CK"+sdf.format(cksj);
        Map<String,Object> mapBean=tjy2ChCkDao.getBeanByYear(bh);
        String newbh="";
        if(mapBean!=null){
        	Object object = mapBean.get("CKDBH");
	        int no= Integer.parseInt(String.valueOf(object ))+1;
		    newbh=bh+String.format("%03d", no);
	     }else{
	    	 newbh=bh+"001";
	     }
        return newbh;
    }

    /**
     * orgId 领取部门ID
     *
     * @param orgId
     * @param startTime
     * @param endTime
     * @param type
     * @return
     * @throws Exception
     */
    public List<Object[]> getGoodsForDepartment(String orgId, Date startTime, Date endTime, String type,String id) throws Exception {
        String sql = "select out.lqbm,to_char(out.CREATE_TIME,'yyyy-mm-dd') CREATE_TIME,goods.wpmc,goods.ggjxh,ORDERS.sl,goods.dw,goods.je as dj,ORDERS.sl*goods.je as je,'' as jexj,OUT.LQR,OUT.BMFZR,ORDERS.BZ,goods.sybm_id from TJY2_CH_OUT out left join TJY2_CH_GOODSANDORDER ORDERS on OUT.id = ORDERS.FK_ORDER_ID left join TJY2_CH_GOODS goods on ORDERS.fk_goods_id = goods.id where 1=1 ";
        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtil.isNotEmpty(orgId)) {
            sql += " and out.create_org_id = :orgId";
            params.put("orgId", orgId);
        }
        if (startTime != null) {
            sql += " and out.create_time >= :startTime";
            params.put("startTime", startTime);
        }
        if (endTime != null) {
            sql += " and out.create_time < :endTime";
            params.put("endTime", endTime);
        }
        if (StringUtil.isNotEmpty(type)) {
            sql += " and goods.wplx =:type";
            params.put("type", type);
        }
        if(StringUtil.isNotEmpty(id)){
        	sql+= " AND out.id in ('"+id.trim().replaceAll(",", "','")+"')";
        }
        sql += " order by out.lqbm,out.bmfzr,out.lqr ";
        Query query = this.tjy2ChCkDao.createSQLQuery(sql);
        for (String key : params.keySet()) {
            query.setParameter(key, params.get(key));
        }
        return query.list();
    }

    public void deleteOrder(HttpServletRequest request) throws Exception {
        String orderId = request.getParameter("orderId");
        String type = request.getParameter("type");
        if (StringUtil.isEmpty(orderId)) {
            throw new KhntException("请选择要删除的单据");
        }
        if (StringUtil.isEmpty(type)) {
            throw new KhntException("请提供单据类型");
        }
        OutStorageInf inf = getOutStorageImpl(type);
        inf.deleteOutStorage(orderId);
    }

    public HashMap<String, Object> getChCkTotalAmount(String ids) throws Exception {
        List<Tjy2ChCk> tjy2ChCksList = getTjy2ChCkByIds(ids);
        Double totalAmount = 0d;
        for (Tjy2ChCk tjy2ChCk : tjy2ChCksList) {
            List<GoodsAndOrder> goodsAndOrders = tjy2ChCk.getGoods();
            for (GoodsAndOrder goods : goodsAndOrders) {
                Double amount = goods.getSjlqsl() * goods.getGoods().getJe();
                totalAmount += amount;
            }
        }
        return JsonWrapper.successWrapper(totalAmount);
    }

    public List<Tjy2ChCk> getTjy2ChCkByIds(String ids) {
        String[] idsArray = ids.split(",");
        Query query = tjy2ChCkDao.createQuery("from Tjy2ChCk where id in :ids");
        query.setParameterList("ids", idsArray);
        List<Tjy2ChCk> tjy2ChCksList = query.list();
        return tjy2ChCksList;
    }

    public void updateBxidsToEmpty(String ids) {
        Query query = tjy2ChCkDao.createSQLQuery("update TJY2_CH_OUT set bx_id = null,bx_status='0' where bx_id=?", ids);
        query.executeUpdate();
    }

    public List<Tjy2ChCk> getTjy2ChCksByBxId(String id) {
        Query query = tjy2ChCkDao.createQuery("from Tjy2ChCk where bxId = ?", id);
        return query.list();
    }


    public void updateBxStatus(String bxid, String status) {
        Query query = tjy2ChCkDao.createSQLQuery("update TJY2_CH_OUT set bx_status = ? where bx_id=?", status, bxid);
        query.executeUpdate();
    }
}
