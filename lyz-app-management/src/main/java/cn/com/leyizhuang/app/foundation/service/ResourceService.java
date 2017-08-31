package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.Resource;
import cn.com.leyizhuang.app.foundation.pojo.vo.ResourceVO;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 资源API
 *
 * @author Richard
 * Created on 2017-07-28 15:07
 **/
public interface ResourceService {
    PageInfo<Resource> queryPage(Integer page, Integer size);

    List<Resource> queryByPid(Long i);

    void save(Resource resource);

    Resource queryById(Long id);

    PageInfo<ResourceVO> queryPageVO(Integer page, Integer size);

    void update(ResourceVO resourceVO);

    void update(Resource resource);

    Long countByPId(Long id);

    void batchRemove(List<Long> longs);
}
