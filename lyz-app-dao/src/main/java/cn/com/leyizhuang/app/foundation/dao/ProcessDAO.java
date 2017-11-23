package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.ProcessDO;
import cn.com.leyizhuang.app.foundation.pojo.response.ProcessResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.UserGoodsResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by caiyu on 2017/9/20.
 */
@Repository
public interface ProcessDAO {
    //查询所有工序目录
    List<ProcessDO> queryList();

    //根据工序包ID查找
    ProcessDO selectById(Long id);

    //根据工序包名查找
    ProcessDO selectByProcessName(String processName);

    //修改工序包
    void update(ProcessDO processDO);

    //删除工序包
    void delete(Long id);

    //保存
    void save(ProcessDO processDO);

    //查询所有工序包并带出对应商品
    List<ProcessDO> findAllProcessAndGoods();

    //保存Goods（商品）与Process（工序包）对应关系
    void saveProcessAndGoods(@Param("gid") Long gID, @Param("pid") Long pID);

    //根据工序包名称查询对应的商品（暂时没有返回数据）
    void findProcessAndGoodsByName(@Param("processName") String processName);

    //查询所有工序目录/
    List<ProcessResponse> queryAllList();

    List<UserGoodsResponse> queryByProcessIdAndEmployeeId(@Param("userId") Long userId, @Param("processId") Long processId);

    List<UserGoodsResponse> queryByProcessIdAndCustomerId(@Param("userId") Long userId, @Param("processId") Long processId);
}
