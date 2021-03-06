package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import javax.websocket.server.PathParam;
import java.util.List;

/**
 * Created by caiyu on 2017/12/8.
 */
@Repository
public interface CashCouponDAO {
    /**
     * 获取现金券信息
     *
     * @param cusCashCouponId
     * @return
     */
    CustomerCashCoupon findCusCashCouponByCouponId(@Param("cusCashCouponId") Long cusCashCouponId);

    /**
     * 添加现金券模板
     *
     * @param cashCoupon 现金券
     */
    void addCashCoupon(CashCoupon cashCoupon);

    /**
     * 给顾客添加现金券
     *
     * @param customerCashCoupon
     */
    void addCustomerCashCoupon(CustomerCashCoupon customerCashCoupon);

    /**
     * 根据id返回现金券
     */
    CustomerCashCoupon findCustomerCashCouponById(@Param("id") Long id);

    /**
     * 根据idList 返回现金券结果
     */
    List<CustomerCashCoupon> queryCustomerCashCouponByIdList(@Param("ids") List<Long> ids);

    /**
     * id查询
     */
    CashCoupon queryById(@Param("id") Long id);

    /**
     * 根据关键字返回结果
     *
     * @param keywords
     * @return
     */
    List<CashCoupon> queryByKeywords(@Param("keywords") String keywords, @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 新增门店关联数据 cp_cash_coupon_store
     */
    void addCashCouponStores(List<CashCouponStore> list);

    /**
     * 新增公司关联数据 cp_cash_coupon_company
     */
    void addCashCouponCompany(List<CashCouponCompany> list);

    /**
     * 新增品牌关联数据 cp_cash_coupon_brand
     */
    void addCashCouponBrand(List<CashCouponBrand> list);

    /**
     * 新增商品关联数据 cp_cash_coupon_goods
     */
    void addCashCouponGoods(List<CashCouponGoods> list);

    List<CashCouponStore> queryStoreByCcid(@Param("ccid") Long ccid);

    List<CashCouponCompany> queryCompanyByCcid(@Param("ccid") Long ccid);

    List<CashCouponBrand> queryBrandByCcid(@Param("ccid") Long ccid);

    List<CashCouponGoods> queryGoodsByCcid(@Param("ccid") Long ccid);

    List<Long> queryStoreIdsByCcid(@Param("ccid") Long ccid);

    List<String> queryCompanyFlagsByCcid(@Param("ccid") Long ccid);

    List<Long> queryBrandIdsByCcid(@Param("ccid") Long ccid);

    List<Long> queryGoodsIdsByCcid(@Param("ccid") Long ccid);

    /**
     * 更新cashCoupon
     *
     * @param
     */
    void updateCashCoupon(CashCoupon cashCoupon);

    void deleteCashCouponByid(@Param("ccid") Long ccid);

    void deleteStoreByccid(@Param("ccid") Long ccid);

    void deleteCompanyByccid(@Param("ccid") Long ccid);

    void deleteBrandByccid(@Param("ccid") Long ccid);

    void deleteGoodsByccid(@Param("ccid") Long ccid);

    /**
     * 取消订单修改现金券信息
     *
     * @param customerCashCoupon
     */
    void updateCustomerCashCoupon(CustomerCashCoupon customerCashCoupon);
}
