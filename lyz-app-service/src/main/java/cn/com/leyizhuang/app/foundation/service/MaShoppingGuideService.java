package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.vo.ShoppingGuideVO;

import java.util.List;

public interface MaShoppingGuideService {
        List<ShoppingGuideVO> findGuideListById(Long storeId);
}

