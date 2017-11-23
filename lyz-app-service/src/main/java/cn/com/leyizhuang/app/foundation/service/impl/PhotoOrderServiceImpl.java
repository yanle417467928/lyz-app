package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.PhotoOrderDAO;
import cn.com.leyizhuang.app.foundation.pojo.PhotoOrderDO;
import cn.com.leyizhuang.app.foundation.service.PhotoOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author GenerationRoad
 * @date 2017/10/24
 */
@Service
@Transactional
public class PhotoOrderServiceImpl implements PhotoOrderService {

    @Autowired
    private PhotoOrderDAO photoOrderDAO;

    @Override
    public PhotoOrderDO save(PhotoOrderDO photoOrderDO) {
        if (null != photoOrderDO) {
            this.photoOrderDAO.save(photoOrderDO);
        }
        return photoOrderDO;
    }
}
