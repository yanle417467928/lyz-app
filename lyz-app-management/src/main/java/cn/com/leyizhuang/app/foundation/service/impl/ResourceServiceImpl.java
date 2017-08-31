package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.ResourceDAO;
import cn.com.leyizhuang.app.foundation.pojo.AppAdminMenuDO;
import cn.com.leyizhuang.app.foundation.pojo.Resource;
import cn.com.leyizhuang.app.foundation.pojo.vo.ResourceVO;
import cn.com.leyizhuang.app.foundation.service.ResourceService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 资源API实现
 *
 * @author Richard
 * Created on 2017-08-02 14:17
 **/
@Service
public class ResourceServiceImpl implements ResourceService{

    @Autowired
    private ResourceDAO resourceDAO;

    @Override
    public PageInfo<Resource> queryPage(Integer page, Integer size) {
        PageHelper.startPage(page,size);
        List<Resource> resourceList = resourceDAO.queryList();
        return new PageInfo<>(resourceList);
    }

    @Override
        public List<Resource> queryByPid(Long i) {
        if(null != i ){
            return resourceDAO.queryByPid(i);
        }
        return null;
    }

    @Override
    public void save(Resource resource) {
        if (null != resource){
            resourceDAO.save(resource);
        }
    }

    @Override
    public Resource queryById(Long id) {
        if(null != id){
            return resourceDAO.queryById(id);
        }
        return null;
    }

    @Override
    public PageInfo<ResourceVO> queryPageVO(Integer page, Integer size) {
        PageHelper.startPage(page,size);
        List<ResourceVO> resourceList = resourceDAO.queryListVO();
        return new PageInfo<>(resourceList);
    }

    @Override
    public void update(ResourceVO resourceVO) {
        if(null != resourceVO){
            resourceDAO.updateVO(resourceVO);
        }
    }

    @Override
    public void update(Resource resource) {
        if(null != resource){
            resourceDAO.update(resource);
        }
    }

    @Override
    public Long countByPId(Long id) {
        if(null != id){
            return resourceDAO.countByPId(id);
        }
        return null;
    }

    @Override
    public void batchRemove(List<Long> longs) {
        if(null != longs && longs.size()>0){
            resourceDAO.batchRemove(longs);
        }
    }
}
