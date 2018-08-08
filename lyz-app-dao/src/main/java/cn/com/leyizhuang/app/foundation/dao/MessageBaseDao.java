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


    void delete4Message(Long messageId);

    List<AppCustomer> FindAllCustomerId();


    List<SimpleEmployeeParam> FindAllEmployeeId(String identityType);

    //根据门店ID查员工
    List<SimpleEmployeeParam> FindEmployeeByStoreId(Long id);


    //根据门店ID查会员
    List<AppCustomer> FindCustomerByStoreId(Long id);

    //根据Id查员工
    List<SimpleEmployeeParam> FindEmployeeById(Long id);

    //批量保存

    void saveBatch(@Param("list") List<MessageMemberConference> listAll);














}
