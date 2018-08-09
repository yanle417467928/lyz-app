package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.vo.management.city.CityInventoryVO;
import com.github.pagehelper.PageInfo;

/**
 * @author Jerry.Ren
 * Notes:
 * Created with IntelliJ IDEA.
 * Date: 2017/12/28.
 * Time: 13:45.
 */

public interface MaCityAvailableItyService {
    /**
     * 分页查询
     *
     * @param page
     * @param size
     * @param keywords
     * @return
     */
    PageInfo<CityInventoryVO> queryPageVO(Integer page, Integer size, String keywords,Long cityId);
}
