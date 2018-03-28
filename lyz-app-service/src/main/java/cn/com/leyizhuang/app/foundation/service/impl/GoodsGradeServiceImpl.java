package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.dao.GoodsDAO;
import cn.com.leyizhuang.app.foundation.dao.GoodsGradeDAO;
import cn.com.leyizhuang.app.foundation.dto.GoodsDTO;
import cn.com.leyizhuang.app.foundation.pojo.GoodsGrade;
import cn.com.leyizhuang.app.foundation.pojo.GoodsPrice;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.management.goods.GoodsBrand;
import cn.com.leyizhuang.app.foundation.pojo.response.*;
import cn.com.leyizhuang.app.foundation.service.GoodsGradeService;
import cn.com.leyizhuang.app.foundation.service.GoodsService;
import cn.com.leyizhuang.app.foundation.service.MaGoodsBrandService;
import cn.com.leyizhuang.app.foundation.vo.OrderGoodsVO;
import cn.com.leyizhuang.app.foundation.vo.management.MaBuyProductCouponGoodsResponse;
import cn.com.leyizhuang.app.foundation.vo.management.goods.MaGoodsVO;
import cn.com.leyizhuang.common.foundation.pojo.dto.HqAppGoodsGradeDTO;
import cn.com.leyizhuang.common.util.AssertUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * @author liuh
 * @date 2018/03/27
 */
@Service
public class GoodsGradeServiceImpl implements GoodsGradeService {

    @Autowired
    private GoodsGradeDAO goodsGradeDAO;

    @Override
    public GoodsGrade queryBySkuAndSobId(String sku, String sobId) {
        return goodsGradeDAO.queryBySkuAndSobId(sku, sobId);
    }

    @Override
    public void addGoodsGrade(GoodsGrade goodsGrade) {
        goodsGradeDAO.addGoodsGrade(goodsGrade);
    }

    @Override
    public void updateGoodsGrade(GoodsGrade goodsGrade) {
        goodsGradeDAO.updateGoodsGrade(goodsGrade);
    }

}
