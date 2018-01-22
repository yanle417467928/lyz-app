package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.inventory.returning.Returning;
import cn.com.leyizhuang.app.foundation.pojo.inventory.returning.ReturningVO;
import com.github.pagehelper.PageInfo;

/**
 * @author Jerry.Ren
 * Notes:
 * Created with IntelliJ IDEA.
 * Date: 2018/1/16.
 * Time: 18:23.
 */

public interface ItyReturningService {
    /**
     * 分页
     *
     * @param offset
     * @param size
     * @param keywords
     * @return
     */
    PageInfo<ReturningVO> queryPage(Integer offset, Integer size, String keywords);

    /**
     * 查询退单详情
     *
     * @param id
     * @return
     */
    Returning queryReturningById(Long id);
}
