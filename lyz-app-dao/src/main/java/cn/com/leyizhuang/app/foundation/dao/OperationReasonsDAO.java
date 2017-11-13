package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.response.CancelReasonsResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/11/13
 */
@Repository
public interface OperationReasonsDAO {

    List<CancelReasonsResponse> findAll();
}
