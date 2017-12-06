package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.goods.PhysicalClassify;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaPhysicalClassifyDAO {
    List<PhysicalClassify> findPhysicalClassifyList();
}
