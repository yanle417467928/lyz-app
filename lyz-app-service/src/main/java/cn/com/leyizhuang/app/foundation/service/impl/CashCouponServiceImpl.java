package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.AppCashCouponType;
import cn.com.leyizhuang.app.foundation.dao.CashCouponDAO;
import cn.com.leyizhuang.app.foundation.dao.MaGoodsBrandDAO;
import cn.com.leyizhuang.app.foundation.pojo.*;
import cn.com.leyizhuang.app.foundation.pojo.management.goods.GoodsBrand;
import cn.com.leyizhuang.app.foundation.service.CashCouponService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by caiyu on 2017/12/8.
 */
@Service
public class CashCouponServiceImpl implements CashCouponService{
    @Resource
    private CashCouponDAO cashCouponDAO;

    @Resource
    private MaGoodsBrandDAO maGoodsBrandDAO;

    @Override
    public CustomerCashCoupon findCusCashCouponByCouponId(Long cusProductCouponId) {
        return cashCouponDAO.findCusCashCouponByCouponId(cusProductCouponId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addCashCoupon(CashCoupon cashCoupon) {
        cashCouponDAO.addCashCoupon(cashCoupon);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addCustomerCashCoupon(CustomerCashCoupon customerCashCoupon) {

    }

    @Override
    public PageInfo<CashCoupon> queryPage(Integer page, Integer size, String keywords) {

        PageHelper.startPage(page, size);
        List<CashCoupon> list = cashCouponDAO.queryByKeywords(keywords);
        return new PageInfo<>(list);
    }

    @Override
    public CashCoupon queryById(Long id){
        if (id == null){
         return null;
        }
        return cashCouponDAO.queryById(id);
    }

    /**
     * 持久化现金券模版数据
     * @param cashCoupon
     * @param cashCouponCompanies
     * @param cashCouponBrands
     * @param cashCouponGoods
     * @param cashCouponStores
     */
    @Override
    @Transactional
    public void saveCashCouponTemplate(CashCoupon cashCoupon, List<String> cashCouponCompanies,
                                       List<String> cashCouponBrands, List<CashCouponGoods> cashCouponGoods,
                                       List<CashCouponStore> cashCouponStores){

        AppCashCouponType type = cashCoupon.getType();
        cashCoupon.setCreateTime(new Date());
        cashCoupon.setRemainingQuantity(cashCoupon.getInitialQuantity());
        cashCouponDAO.addCashCoupon(cashCoupon);

        if (type.equals(AppCashCouponType.GENERAL)){

        }else if (type.equals(AppCashCouponType.COMPANY)){
            List<CashCouponCompany> cashCouponCompanyList = new ArrayList<>();

            for (String company : cashCouponCompanies){
                CashCouponCompany cashCouponCompany = new CashCouponCompany();
                cashCouponCompany.setCcid(cashCoupon.getId());
                cashCouponCompany.setCompanyFlag(company);
                if (company.equals("LYZ")){
                    cashCouponCompany.setCompanyName("乐易装");
                }else if (company.equals("HR")){
                    cashCouponCompany.setCompanyName("华润");
                }else if (company.equals("YR")){
                    cashCouponCompany.setCompanyName("盈润");
                }
                cashCouponCompanyList.add(cashCouponCompany);
            }

            cashCouponDAO.addCashCouponCompany(cashCouponCompanyList);
        }else if (type.equals(AppCashCouponType.BRAND)){
            List<GoodsBrand> brandList = maGoodsBrandDAO.findGoodsBrandByIdList(cashCouponBrands);
            List<CashCouponBrand> cashCouponBrandList = new ArrayList<>();
            for (GoodsBrand brand : brandList){
                CashCouponBrand cashCouponBrand = new CashCouponBrand();
                cashCouponBrand.setCcid(cashCoupon.getId());
                cashCouponBrand.setBrandId(brand.getBrdId());
                cashCouponBrand.setBrandName(brand.getBrandName());
                cashCouponBrandList.add(cashCouponBrand);
            }
            cashCouponDAO.addCashCouponBrand(cashCouponBrandList);

        }else if (type.equals(AppCashCouponType.GOODS)){
            for (CashCouponGoods goods : cashCouponGoods){
                goods.setCcid(cashCoupon.getId());
            }
            cashCouponDAO.addCashCouponGoods(cashCouponGoods);
        }

        if (cashCoupon.getIsSpecifiedStore()){
            // 指定门店
            for (CashCouponStore store : cashCouponStores){
                store.setCcid(cashCoupon.getId());
            }
            cashCouponDAO.addCashCouponStores(cashCouponStores);
        }

    }
    /**
     * 持久化现金券模版数据
     * @param cashCoupon
     * @param cashCouponCompanies
     * @param cashCouponBrands
     * @param cashCouponGoods
     * @param cashCouponStores
     */
//    现金券模版一旦生成后不能修改条件和结果，否则会影响到已发送的券
//    @Override
//    @Transactional
//    public void updateCashCouponTemplate(CashCoupon cashCoupon, List<String> cashCouponCompanies,
//                                       List<String> cashCouponBrands, List<CashCouponGoods> cashCouponGoods,
//                                       List<CashCouponStore> cashCouponStores){
//
//        AppCashCouponType type = cashCoupon.getType();
//        cashCouponDAO.updateCashCoupon(cashCoupon);
//
//        if (type.equals(AppCashCouponType.GENERAL)){
//
//        }else if (type.equals(AppCashCouponType.COMPANY)){
//            cashCouponDAO.deleteCompanyByccid(cashCoupon.getId());
//
//            List<CashCouponCompany> cashCouponCompanyList = new ArrayList<>();
//
//            for (String company : cashCouponCompanies){
//                CashCouponCompany cashCouponCompany = new CashCouponCompany();
//                cashCouponCompany.setCcid(cashCoupon.getId());
//                cashCouponCompany.setCompanyFlag(company);
//                if (company.equals("LYZ")){
//                    cashCouponCompany.setCompanyName("乐易装");
//                }else if (company.equals("HR")){
//                    cashCouponCompany.setCompanyName("华润");
//                }else if (company.equals("YR")){
//                    cashCouponCompany.setCompanyName("盈润");
//                }
//                cashCouponCompanyList.add(cashCouponCompany);
//            }
//
//            cashCouponDAO.addCashCouponCompany(cashCouponCompanyList);
//        }else if (type.equals(AppCashCouponType.BRAND)){
//            cashCouponDAO.deleteBrandByccid(cashCoupon.getId());
//
//            List<GoodsBrand> brandList = maGoodsBrandDAO.findGoodsBrandByIdList(cashCouponBrands);
//            List<CashCouponBrand> cashCouponBrandList = new ArrayList<>();
//            for (GoodsBrand brand : brandList){
//                CashCouponBrand cashCouponBrand = new CashCouponBrand();
//                cashCouponBrand.setCcid(cashCoupon.getId());
//                cashCouponBrand.setBrandId(brand.getBrdId());
//                cashCouponBrand.setBrandName(brand.getBrandName());
//                cashCouponBrandList.add(cashCouponBrand);
//            }
//            cashCouponDAO.addCashCouponBrand(cashCouponBrandList);
//
//        }else if (type.equals(AppCashCouponType.GOODS)){
//            cashCouponDAO.deleteGoodsByccid(cashCoupon.getId());
//
//            for (CashCouponGoods goods : cashCouponGoods){
//                goods.setCcid(cashCoupon.getId());
//            }
//            cashCouponDAO.addCashCouponGoods(cashCouponGoods);
//        }
//
//        if (cashCoupon.getIsSpecifiedStore()){
//            cashCouponDAO.deleteStoreByccid(cashCoupon.getId());
//
//            // 指定门店
//            for (CashCouponStore store : cashCouponStores){
//                store.setCcid(cashCoupon.getId());
//            }
//            cashCouponDAO.addCashCouponStores(cashCouponStores);
//        }
//
//    }

    /**
     * 删除现金券模版
     * @param ids
     */
    @Transactional
    public void deleteCashCouponTemplate(List<Long> ids){
        if (ids != null || ids.size() != 0){
            for (Long id : ids){
                CashCoupon cashCoupon = cashCouponDAO.queryById(id);
                cashCoupon.setStatus(false);
                cashCouponDAO.updateCashCoupon(cashCoupon);
            }
        }
    }

    @Override
    public CustomerCashCoupon findCustomerCashCouponById(Long id){
        if (id == null){
            return null;
        }
        return cashCouponDAO.findCustomerCashCouponById(id);
    }

    @Override
    public List<CashCouponStore> queryStoreByCcid(Long ccid){
        if(ccid == null){
            return null;
        }
        return cashCouponDAO.queryStoreByCcid(ccid);
    }

    @Override
    public List<CashCouponCompany> queryCompanyByCcid(Long ccid){
        if(ccid == null){
            return null;
        }
        return cashCouponDAO.queryCompanyByCcid(ccid);
    }

    @Override
    public List<CashCouponBrand> queryBrandByCcid(Long ccid){
        if(ccid == null){
            return null;
        }
        return cashCouponDAO.queryBrandByCcid(ccid);
    }

    @Override
    public List<CashCouponGoods> queryGoodsByCcid(Long ccid){
        if(ccid == null){
            return null;
        }
        return cashCouponDAO.queryGoodsByCcid(ccid);
    }

    @Override
    public List<Long> queryStoreIdsByCcid(Long ccid){
        if(ccid == null){
            return null;
        }
        return cashCouponDAO.queryStoreIdsByCcid(ccid);
    }

    @Override
    public List<String> queryCompanysByCcid(Long ccid){
        if(ccid == null){
            return null;
        }
        return cashCouponDAO.queryCompanyFlagsByCcid(ccid);
    }

    @Override
    public List<Long> queryBrandIdsByCcid(Long ccid){
        if(ccid == null){
            return null;
        }
        return cashCouponDAO.queryBrandIdsByCcid(ccid);
    }

    @Override
    public List<Long> queryGoodsIdsByCcid(Long ccid){
        if(ccid == null){
            return null;
        }
        return cashCouponDAO.queryGoodsIdsByCcid(ccid);
    }

    @Override
    public void updateCustomerCashCoupon(CustomerCashCoupon customerCashCoupon) {
        cashCouponDAO.updateCustomerCashCoupon(customerCashCoupon);
    }

}
