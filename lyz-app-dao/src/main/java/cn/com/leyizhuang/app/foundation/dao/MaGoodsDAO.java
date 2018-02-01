package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.management.goods.GoodsShippingInfo;
import cn.com.leyizhuang.app.foundation.pojo.management.order.MaOrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.vo.management.goods.GoodsResponseVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaGoodsDAO {

    void saveGoodsShippingInfo(GoodsShippingInfo goodsShippingInfo);

    List<GoodsResponseVO> findGoodsByCidAndCusId(@Param("cusId") Long cusId, @Param("list") List<Long> cids);

    List<GoodsResponseVO> findGoodsByCidAndEmpId(@Param("empId") Long cusId, @Param("list") List<Long> cids);

    GoodsDO findGoodsById(Long id);

}

