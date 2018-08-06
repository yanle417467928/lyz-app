package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.management.store.SimpleStoreParam;
import cn.com.leyizhuang.app.foundation.pojo.message.MessageListDO;
import cn.com.leyizhuang.app.foundation.pojo.message.MessageMemberConference;
import cn.com.leyizhuang.app.foundation.pojo.message.MessageStoreDO;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import com.github.pagehelper.PageInfo;
import org.springframework.ui.ModelMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王浩 on 2018/7/25.
 */
public interface MessageBaseService {

    List<MessageListDO> queryList();

    PageInfo<MessageListDO> queryPageVO(Integer page, Integer size , String keywords, String status, Long cityId);

    void save (MessageListDO messageListDO, List<Long> storeList, ArrayList<Long> customerIds);

    //void save(ActBaseDO baseDO, List<ActGoodsMappingDO> goodsList, List<ActGiftDetailsDO> giftList, Double subAmount, List<ActStoreDO> storeList,Double discount, ArrayList<Long> customerIds);

    void inValid(Long id);

    void publishMessage(Long id);

    void saveMessage (MessageListDO messageListDO);

    void editMessage (MessageListDO messageListDO);

    ModelMap getModelMapByActBaseId(ModelMap map, Long id);

    List<AppCustomer> findCustomer(Long actId);

    List<SimpleStoreParam> findStore(Long messageId);

    void edit (MessageListDO messageListDO, List<Long> storeList, ArrayList<Long> customerIds);
}
