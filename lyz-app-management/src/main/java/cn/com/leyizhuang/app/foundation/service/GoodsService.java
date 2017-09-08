package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.GoodsDO;
import cn.com.leyizhuang.common.foundation.service.BaseService;
import com.github.pagehelper.PageInfo;

/**
 * @author GenerationRoad
 * @date 2017/9/6
 */
public interface GoodsService extends BaseService<GoodsDO> {

    PageInfo<GoodsDO> queryPage(Integer page, Integer size);

}
