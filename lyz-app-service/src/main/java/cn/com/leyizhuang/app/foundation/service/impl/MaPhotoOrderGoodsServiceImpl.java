package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.dao.MaPhotoOrderGoodsDAO;
import cn.com.leyizhuang.app.foundation.pojo.PhotoOrderGoodsDO;
import cn.com.leyizhuang.app.foundation.service.MaPhotoOrderGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/2/23
 */
@Service
@Transactional
public class MaPhotoOrderGoodsServiceImpl implements MaPhotoOrderGoodsService {

    @Autowired
    private MaPhotoOrderGoodsDAO maPhotoOrderGoodsDAO;

    @Override
    public int batchSave(List<PhotoOrderGoodsDO> photoOrderGoodsDOList) {
        return this.maPhotoOrderGoodsDAO.batchSave(photoOrderGoodsDOList);
    }

    @Override
    public List<PhotoOrderGoodsDO> findPhotoOrderGoodsByPhotoOrderNo(String photoOrderNo) {
        if (StringUtils.isBlank(photoOrderNo)){
            return null;
        }else{
            return this.maPhotoOrderGoodsDAO.findPhotoOrderGoodsByPhotoOrderNo(photoOrderNo);
        }
    }
}
