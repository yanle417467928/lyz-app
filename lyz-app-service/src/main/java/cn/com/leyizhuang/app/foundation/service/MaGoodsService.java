package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.vo.management.goods.GoodsResponseVO;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/1/30
 */
public interface MaGoodsService {

    List<GoodsResponseVO> findGoodsByCidAndCusId(Long cusId, List<Long> cids);

    List<GoodsResponseVO> findGoodsByCidAndEmpId(Long empId, List<Long> cids);

    GoodsDO findGoodsById(Long id);

}
