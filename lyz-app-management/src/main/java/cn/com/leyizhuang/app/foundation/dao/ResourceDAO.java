package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.Resource;
import cn.com.leyizhuang.app.foundation.pojo.Role;
import cn.com.leyizhuang.app.foundation.pojo.vo.ResourceVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 资源DAO
 *
 * @author Richard
 * Created on 2017-07-28 15:27
 **/
@Repository
public interface ResourceDAO {


    List<Resource> queryList();

    List<Resource> queryByPid(@Param(value = "pid") Long i);

    void save(Resource resource);

    Resource queryById(Long id);

    List<ResourceVO> queryListVO();

    void updateVO(ResourceVO resourceVO);

    void update(Resource resource);

    Long countByPId(Long id);

    void batchRemove(List<Long> longs);
}
