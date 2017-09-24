package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.Resource;
import cn.com.leyizhuang.app.foundation.pojo.vo.ResourceVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    List<Resource> selectByType(@Param(value = "type") int type);

    ResourceVO queryVOById(@Param(value = "id") Long id);

    List<Long> queryParentIdsByIds(@Param(value = "ids") String[] resourceIds);
}
