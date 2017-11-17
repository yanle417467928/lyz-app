package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.vo.ShoppingGuideVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaShoppingGuideDAO {
    List<ShoppingGuideVO> findGuideListById(Long storeId);
}
