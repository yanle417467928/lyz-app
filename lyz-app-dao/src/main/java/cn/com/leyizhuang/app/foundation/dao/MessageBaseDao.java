package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.management.employee.SimpleEmployeeParam;
import cn.com.leyizhuang.app.foundation.pojo.management.store.SimpleStoreParam;
import cn.com.leyizhuang.app.foundation.pojo.management.store.StoreDO;
import cn.com.leyizhuang.app.foundation.pojo.message.MessageListDO;
import cn.com.leyizhuang.app.foundation.pojo.message.MessageMemberConference;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by 王浩 on 2018/7/23.
 * 消息管理DAO
 */
@Repository
public interface MessageBaseDao {

    //查询信息列表数据
    List<MessageListDO> queryList();

    /**
     * 根据关键字返回结果
     * @param keywords
     * @return
     */
    List<MessageListDO> queryByKeywords(@Param("keywords") String keywords, @Param("status") String status, @Param("cityId")  Long cityId);

    void save (MessageListDO messageListDO);

    void saveMemberConference(MessageMemberConference messageMemberConference);


    MessageListDO queryById(@Param("id") Long id);


    void update(MessageListDO messageListDO);

    void editMessage(MessageListDO messageListDO);

    List<AppCustomer> findCustomer(Long messageId);

    List<SimpleStoreParam> findStore(Long messageId);

    void deleteMemberConferenceByMessageBaseId4Customer(Long messageId);

    void deleteMemberConferenceByMessageBaseId4Stores(Long messageId);

    void deleteMemberConferenceByMessageBaseIdAllYG(Long messageId,String identityType);

    List<AppCustomer> FindAllCustomerId();


    List<StoreDO> FindAllStoreId();

    List<SimpleEmployeeParam> FindAllEmployeeId();

    //只查询配送员ID
    List<SimpleEmployeeParam> FindAllEmployeeIdOnlyPSY();

    //只查询导购ID
    List<SimpleEmployeeParam> FindAllEmployeeIdOnlyDG();

    //只查询装饰公司经理ID
    List<SimpleEmployeeParam> FindAllEmployeeIdOnlyJL();

    //只查询装饰公司员工ID
    List<SimpleEmployeeParam> FindAllEmployeeIdOnlyYG();







}
