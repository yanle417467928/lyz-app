package cn.com.leyizhuang.app.foundation.service;


import cn.com.leyizhuang.app.foundation.pojo.ProcessDO;
import cn.com.leyizhuang.app.foundation.pojo.response.ProcessResponse;

import java.util.List;

/**
 * Created by caiyu on 2017/9/20.
 */
public interface ProcessService {
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

    List<ProcessDO> findAllProcessAndGoods();

    void saveProcessAndGoods(Long pID, Long gID);

    //查询所有工序目录
    List<ProcessResponse> queryAllList();
}
