package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.*;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Created by caiyu on 2017/12/8.
 */
public interface CashCouponService {
    /**
     *  获取现金券信息
     * @param couponId
     * @return
     */
    CashCoupon findCashCouponByOrderNumber(Long couponId);

    /**
     * 添加现金券模板
     * @param cashCoupon    现金券
     */
    void addCashCoupon(CashCoupon cashCoupon);

    /**
     * 给顾客添加现金券
     * @param customerCashCoupon
     */
    void addCustomerCashCoupon(CustomerCashCoupon customerCashCoupon);

    /**
     * 现金券 gridData
     */
    PageInfo<CashCoupon> queryPage(Integer page, Integer size, String keywords);

    /**
     * 新增现金券
     * @param cashCoupon
     * @param cashCouponCompanys
     * @param cashCouponBrands
     * @param cashCouponGoods
     * @param cashCouponStores
     */
    void saveCashCouponTemplate(CashCoupon cashCoupon, List<String> cashCouponCompanys,
                                List<String> cashCouponBrands, List<CashCouponGoods> cashCouponGoods,
                                List<CashCouponStore> cashCouponStores);

    /**
     * 更新现金券
     * @param cashCoupon
     * @param cashCouponCompanies
     * @param cashCouponBrands
     * @param cashCouponGoods
     * @param cashCouponStores
     */
    void updateCashCouponTemplate(CashCoupon cashCoupon, List<String> cashCouponCompanies,
                                  List<String> cashCouponBrands, List<CashCouponGoods> cashCouponGoods,
                                  List<CashCouponStore> cashCouponStores);

    /**
     * 删除现金券
     * @param ids
     */
    void deleteCashCouponTemplate(List<Long> ids);

    CashCoupon queryById(Long id);

    List<CashCouponStore> queryStoreByCcid(Long ccid);

    List<CashCouponCompany> queryCompanyByCcid(Long ccid);

    List<CashCouponBrand> queryBrandByCcid(Long ccid);

    List<CashCouponGoods> queryGoodsByCcid(Long ccid);
}
