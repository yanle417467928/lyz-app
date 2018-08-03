package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.message.MessageStoreDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * table: Message_stores
 * Created by panjie on 2017/11/22.
 */
@Repository
public interface MessageStoresDAO {

    void save(MessageStoreDO messageBaseDO);

    void update(MessageStoreDO messageBaseDO);

    List<MessageStoreDO> queryList();

    List<MessageStoreDO> queryListByMessageBaseId(@Param("MessageBaseId") Long messageBaseId);

    MessageStoreDO queryById();

    void deleteByMessageBaseId(@Param("MessageBaseId") Long messageBaseId);
}
