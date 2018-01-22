package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.ItyReturningDAO;
import cn.com.leyizhuang.app.foundation.dao.ReturnOrderDAO;
import cn.com.leyizhuang.app.foundation.pojo.inventory.returning.Returning;
import cn.com.leyizhuang.app.foundation.pojo.inventory.returning.ReturningVO;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderBaseInfo;
import cn.com.leyizhuang.app.foundation.service.ItyReturningService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Jerry.Ren
 * Notes:
 * Created with IntelliJ IDEA.
 * Date: 2018/1/16.
 * Time: 18:23.
 */

@Service
public class ItyReturningServiceImpl implements ItyReturningService {

    private ItyReturningDAO ityReturningDAO;

    private ReturnOrderDAO returnOrderDAO;

    @Override
    public PageInfo<ReturningVO> queryPage(Integer offset, Integer size, String keywords) {
        PageHelper.startPage(offset, size);

        List<ReturnOrderBaseInfo> returnOrderBaseInfoList = returnOrderDAO.findReturnOrderList(keywords);

        return new PageInfo<>();
    }

    @Override
    public Returning queryReturningById(Long id) {
        return null;
    }
}
