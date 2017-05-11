package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.AppAdminMenuDO;
import cn.com.leyizhuang.common.foundation.dao.BaseDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * App后台管理中心菜单Dao层接口
 *
 * @author Richard
 *         Created on 2017-05-08 14:38
 **/
@Repository
public interface AppAdminMenuDAO extends BaseDAO<AppAdminMenuDO> {

    List<AppAdminMenuDO> queryByParentId(Long parentId);
}
