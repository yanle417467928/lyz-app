package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.vo.management.city.CityInventoryVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Jerry.Ren
 * Notes:
 * Created with IntelliJ IDEA.
 * Date: 2017/12/28.
 * Time: 13:46.
 */
@Repository
public interface MaCityAvailableItyDAO {
    /**
     * 查询库存分页
     *
     * @param keywords
     * @return
     */
    List<CityInventoryVO> findCityInventoryList(@Param("keywords") String keywords);
}
