package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.dao.GoodsPriceDAO;
import cn.com.leyizhuang.app.foundation.pojo.GoodsPrice;
import cn.com.leyizhuang.app.foundation.pojo.response.GiftListResponseGoods;
import cn.com.leyizhuang.app.foundation.service.GoodsPriceService;
import cn.com.leyizhuang.app.foundation.vo.GoodsPriceVO;
import cn.com.leyizhuang.common.util.AssertUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/10/26
 */
@Service
@Transactional
public class GoodsPriceServiceImpl implements GoodsPriceService {

    @Autowired
    private GoodsPriceDAO goodsPriceDAO;

    @Override
    public void save(GoodsPrice goodsPrice) {
        this.goodsPriceDAO.save(goodsPrice);
    }

    @Override
    public void modify(GoodsPrice goodsPrice) {
        this.goodsPriceDAO.modify(goodsPrice);
    }

    @Override
    public void update(GoodsPrice goodsPrice) {
        this.goodsPriceDAO.update(goodsPrice);
    }

    @Override
    public void updateByEbs(GoodsPrice goodsPrice) {
        this.goodsPriceDAO.updateByEbs(goodsPrice);
    }

    @Override
    public void delete(GoodsPrice goodsPrice) {
        this.goodsPriceDAO.delete(goodsPrice.getPriceLineId());
    }

    @Override
    public GoodsPrice findGoodsPrice(Long priceLineId) {
        return this.goodsPriceDAO.findByPriceLineId(priceLineId);
    }

    @Override
    public PageInfo<GoodsPriceVO> queryPage(Integer page, Integer size, Long storeId, String keywords) {
        PageHelper.startPage(page, size);
        List<GoodsPriceVO> priceList = this.goodsPriceDAO.findByStoreId(storeId, keywords);
        return new PageInfo<>(priceList);
    }

    @Override
    public Double findGoodsRetailPriceByGoodsIDAndStoreID(Long goodsID, Long storeID) {
        return this.goodsPriceDAO.findGoodsRetailPriceByGoodsIDAndStoreID(goodsID, storeID);
    }

    @Override
    public List<GiftListResponseGoods> findGoodsPriceListByGoodsIdsAndUserIdAndIdentityType(
            List<Long> goodsIdList, Long userId, AppIdentityType identityType) {
        if (goodsIdList != null && goodsIdList.size() > 0 && userId != null && identityType != null) {
            if (identityType.getValue() == AppIdentityType.CUSTOMER.getValue()) {
                return goodsPriceDAO.findCustomerGoodsPriceListByGoodsIdsAndUserIdAndIdentityType(goodsIdList, userId);
            } else {
                return goodsPriceDAO.findEmployeeGoodsPriceListByGoodsIdsAndUserIdAndIdentityType(goodsIdList, userId);
            }
        }
        return null;
    }

    @Override
    public GoodsPrice findGoodsPriceByGoodsIDAndStoreID(Long goodsID, Long storeID,Long cusId) {
        return goodsPriceDAO.findGoodsPriceByGoodsIDAndStoreID(goodsID, storeID,cusId);
    }

    @Override
    public GoodsPrice findGoodsPriceByGoodsIDAndStoreIDAndEmpId(Long goodsID, Long storeID,Long empId) {
        return goodsPriceDAO.findGoodsPriceByGoodsIDAndStoreID(goodsID, storeID,empId);
    }

    @Override
    public GoodsPrice findGoodsPriceByTypeAndStoreIDAndSku(String  priceType, Long storeID,String sku) {
        return goodsPriceDAO.findGoodsPriceByTypeAndStoreIDAndSku(priceType, storeID,sku);
    }

    @Override
    public List<GiftListResponseGoods> findGoodsPriceListByGoodsIdsAndUserId(
            List<Long> goodsIdList, Long userId, AppIdentityType identityType) {
        if (goodsIdList != null && goodsIdList.size() > 0 && userId != null && identityType != null) {
            if (identityType.getValue() == AppIdentityType.CUSTOMER.getValue()) {
                return goodsPriceDAO.findCustomerGoodsPriceListByGoodsIdsAndUserId(goodsIdList, userId);
            }
        }
        return null;
    }

    @Override
    public List<GoodsPrice> findGoodsPriceListByStoreIdAndSkuList(Long storeId, List<String> internalCodeList) {
        if (null != storeId && AssertUtil.isNotEmpty(internalCodeList)){
            return goodsPriceDAO.findGoodsPriceListByStoreIdAndSkuList(storeId,internalCodeList);
        }
        return null;
    }

    @Override
    public List<GoodsPrice> findGoodsPriceListByStoreIdAndPriceType(Long storeId, String priceType) {
        return this.goodsPriceDAO.findGoodsPriceListByStoreIdAndPriceType(storeId, priceType);
    }

    @Override
    public List<GoodsPrice> findGoodsPriceListByStoreIdAndSkuAndpriceType(Long storeId,String priceType,String sku) {
        return this.goodsPriceDAO.findGoodsPriceListByStoreIdAndSkuAndpriceType(storeId,priceType,sku);
    }

    @Override
    public void saveBackupsGoodsPrice(GoodsPrice goodsPrice) {
         goodsPriceDAO.saveBackupsGoodsPrice(goodsPrice);
    }

    @Override
    public void delGoodsPriceListByStoreIdAndSkuAndpriceType(Long storeId,String priceType,String sku) {
       goodsPriceDAO.delGoodsPriceListByStoreIdAndSkuAndpriceType(storeId,priceType,sku);
    }
}
