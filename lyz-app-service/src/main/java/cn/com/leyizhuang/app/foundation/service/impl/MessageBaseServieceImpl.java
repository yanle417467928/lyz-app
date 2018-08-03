package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.ActStatusType;
import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.dao.MessageBaseDao;
import cn.com.leyizhuang.app.foundation.dao.MessageStoresDAO;
import cn.com.leyizhuang.app.foundation.pojo.management.employee.SimpleEmployeeParam;
import cn.com.leyizhuang.app.foundation.pojo.management.store.SimpleStoreParam;
import cn.com.leyizhuang.app.foundation.pojo.management.store.StoreDO;
import cn.com.leyizhuang.app.foundation.pojo.message.MessageListDO;
import cn.com.leyizhuang.app.foundation.pojo.message.MessageMemberConference;
import cn.com.leyizhuang.app.foundation.pojo.message.MessageStoreDO;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.service.MessageBaseService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 消息推送类
 * Created by 王浩 on 2018/7/25.
 */
@Service
public class MessageBaseServieceImpl implements MessageBaseService {
    private static final Logger logger = LoggerFactory.getLogger(AppActServiceImpl.class);


    @Autowired
    private MessageBaseDao messageBaseDao;

    @Override
    public List<MessageListDO> queryList() {
        return messageBaseDao.queryList();
    }

    @Override
    public PageInfo<MessageListDO> queryPageVO(Integer page, Integer size, String keywords, String status, Long cityId) {
        PageHelper.startPage(page, size);
        List<MessageListDO> MessageListDOList = messageBaseDao.queryByKeywords(keywords, status, cityId);
        return new PageInfo<>(MessageListDOList);
    }

    @Transactional
    public void save(MessageListDO messageListDO, List<Long> storeList, ArrayList<Long> customerIds) {
        {
            messageListDO.setStatus(ActStatusType.NEW);
            messageListDO.setCreateTime(LocalDateTime.now());
            messageBaseDao.save(messageListDO);
            //选择了会员的保存
            if (null != customerIds && customerIds.size() > 0) {
                for (Long userId : customerIds) {
                    MessageMemberConference messageMemberConference = new MessageMemberConference();
                    messageMemberConference.setUserId(userId);
                    messageMemberConference.setMessageId(messageListDO.getId());
                    messageMemberConference.setCreateTime(new Date());
                    messageMemberConference.setIsRead(false);
                    messageMemberConference.setIdentityType(messageMemberConference.getIdentityType());
                    messageBaseDao.saveMemberConference(messageMemberConference);
                }
            }
            // 选择了门店的保存

            if (null != storeList && storeList.size() > 0) {
                for (Long storeId : storeList) {
                    MessageMemberConference messageMemberConference = new MessageMemberConference();
                    messageMemberConference.setStoreId(storeId);
                    messageMemberConference.setMessageId(messageListDO.getId());
                    messageMemberConference.setCreateTime(new Date());
                    messageMemberConference.setIsRead(false);
                    messageMemberConference.setIdentityType(messageMemberConference.getIdentityType());
                    messageBaseDao.saveMemberConference(messageMemberConference);
                }
            }
            //选择全部的保存
            if (messageListDO.getScope().equals("ALL")) {
                //保存全部的会员
                List<AppCustomer> AllCustomerList = messageBaseDao.FindAllCustomerId();
                for (int i = 0; i < AllCustomerList.size(); i++) {
                    MessageMemberConference messageMemberConference = new MessageMemberConference();
                    messageMemberConference.setUserId(AllCustomerList.get(i).getCusId());
                    messageMemberConference.setMessageId(messageListDO.getId());
                    messageMemberConference.setCreateTime(new Date());
                    messageMemberConference.setIsRead(false);
                    messageMemberConference.setIdentityType(null);
                    messageBaseDao.saveMemberConference(messageMemberConference);
                }
                //保存全部的门店
                List<StoreDO> AllStoreList = messageBaseDao.FindAllStoreId();
                for (int i = 0; i < AllStoreList.size(); i++) {
                    MessageMemberConference messageMemberConference = new MessageMemberConference();
                    messageMemberConference.setStoreId(AllStoreList.get(i).getStoreId());
                    messageMemberConference.setMessageId(messageListDO.getId());
                    messageMemberConference.setCreateTime(new Date());
                    messageMemberConference.setIsRead(false);
                    messageMemberConference.setIdentityType(null);
                    messageBaseDao.saveMemberConference(messageMemberConference);
                }

                //保存全部的会员
                List<SimpleEmployeeParam> AllEmployeeList = messageBaseDao.FindAllEmployeeId();
                for (int i = 0; i < AllEmployeeList.size(); i++) {
                    MessageMemberConference messageMemberConference = new MessageMemberConference();
                    messageMemberConference.setStoreId(AllEmployeeList.get(i).getId());
                    messageMemberConference.setMessageId(messageListDO.getId());
                    messageMemberConference.setCreateTime(new Date());
                    messageMemberConference.setIsRead(false);
                    messageMemberConference.setIdentityType(null);
                    messageBaseDao.saveMemberConference(messageMemberConference);
                }


            }
            //只保存全部门店
            if (messageListDO.getScope().equals("MD")) {
                List<StoreDO> AllStoreList = messageBaseDao.FindAllStoreId();
                for (int i = 0; i < AllStoreList.size(); i++) {
                    MessageMemberConference messageMemberConference = new MessageMemberConference();
                    messageMemberConference.setStoreId(AllStoreList.get(i).getStoreId());
                    messageMemberConference.setMessageId(messageListDO.getId());
                    messageMemberConference.setCreateTime(new Date());
                    messageMemberConference.setIsRead(false);
                    messageMemberConference.setIdentityType(null);
                    messageBaseDao.saveMemberConference(messageMemberConference);
                }

            }

            //选择了全部会员
            if (messageListDO.getScope().equals("HY")) {
                List<AppCustomer> AllCusList = messageBaseDao.FindAllCustomerId();
                for (int i = 0; i < AllCusList.size(); i++) {
                    MessageMemberConference messageMemberConference = new MessageMemberConference();
                    messageMemberConference.setUserId(AllCusList.get(i).getCusId());
                    messageMemberConference.setMessageId(messageListDO.getId());
                    messageMemberConference.setCreateTime(new Date());
                    messageMemberConference.setIsRead(false);
                    messageMemberConference.setIdentityType(messageMemberConference.getIdentityType());
                    messageBaseDao.saveMemberConference(messageMemberConference);
                }

            }

            //选择了全部员工
            if (messageListDO.getScope().equals("YG") && messageListDO.getIdentityType() == null) {
                List<SimpleEmployeeParam> allEmployeeList = messageBaseDao.FindAllEmployeeId();
                for (int i = 0; i < allEmployeeList.size(); i++) {
                    MessageMemberConference messageMemberConference = new MessageMemberConference();
                    messageMemberConference.setUserId(allEmployeeList.get(i).getId());
                    messageMemberConference.setMessageId(messageListDO.getId());
                    messageMemberConference.setCreateTime(new Date());
                    messageMemberConference.setIsRead(false);
                    messageMemberConference.setIdentityType(allEmployeeList.get(i).getIdentityType());
                    messageBaseDao.saveMemberConference(messageMemberConference);
                }
            }
            //选择了全部员工，再选择了身份类型为导购的
            if (messageListDO.getScope().equals("YG") && messageListDO.getIdentityType().equals("SELLER")) {
                List<SimpleEmployeeParam> AllEmployeeDgList = messageBaseDao.FindAllEmployeeIdOnlyDG();
                for (int i = 0; i < AllEmployeeDgList.size(); i++) {
                    MessageMemberConference messageMemberConference = new MessageMemberConference();
                    messageMemberConference.setUserId(AllEmployeeDgList.get(i).getId());
                    messageMemberConference.setMessageId(messageListDO.getId());
                    messageMemberConference.setCreateTime(new Date());
                    messageMemberConference.setIsRead(false);
                    messageMemberConference.setIdentityType(AllEmployeeDgList.get(i).getIdentityType());
                    messageBaseDao.saveMemberConference(messageMemberConference);


                }

            }

            if (messageListDO.getScope().equals("YG") && messageListDO.getIdentityType().equals("DELIVERY_CLERK")) {
                List<SimpleEmployeeParam> AllEmployeePsyList = messageBaseDao.FindAllEmployeeIdOnlyPSY();
                for (int i = 0; i < AllEmployeePsyList.size(); i++) {
                    MessageMemberConference messageMemberConference = new MessageMemberConference();
                    messageMemberConference.setUserId(AllEmployeePsyList.get(i).getId());
                    messageMemberConference.setMessageId(messageListDO.getId());
                    messageMemberConference.setCreateTime(new Date());
                    messageMemberConference.setIsRead(false);
                    messageMemberConference.setIdentityType(messageMemberConference.getIdentityType());
                    messageBaseDao.saveMemberConference(messageMemberConference);


                }

            }
            if (messageListDO.getScope().equals("YG") && messageListDO.getIdentityType().equals("DECORATE_MANAGER")) {
                List<SimpleEmployeeParam> allEmployeeJlList = messageBaseDao.FindAllEmployeeIdOnlyJL();
                for (int i = 0; i < allEmployeeJlList.size(); i++) {
                    MessageMemberConference messageMemberConference = new MessageMemberConference();
                    messageMemberConference.setUserId(allEmployeeJlList.get(i).getId());
                    messageMemberConference.setMessageId(messageListDO.getId());
                    messageMemberConference.setCreateTime(new Date());
                    messageMemberConference.setIsRead(false);
                    messageMemberConference.setIdentityType(allEmployeeJlList.get(i).getIdentityType());
                    messageBaseDao.saveMemberConference(messageMemberConference);


                }

            }

            if (messageListDO.getScope().equals("YG") && messageListDO.getIdentityType().equals("DECORATE_EMPLOYEE")) {
                List<SimpleEmployeeParam> AllEmployeeYgList = messageBaseDao.FindAllEmployeeIdOnlyYG();
                for (int i = 0; i < AllEmployeeYgList.size(); i++) {
                    MessageMemberConference messageMemberConference = new MessageMemberConference();
                    messageMemberConference.setUserId(AllEmployeeYgList.get(i).getId());
                    messageMemberConference.setMessageId(messageListDO.getId());
                    messageMemberConference.setCreateTime(new Date());
                    messageMemberConference.setIsRead(false);
                    messageMemberConference.setIdentityType(AllEmployeeYgList.get(i).getIdentityType());
                    messageBaseDao.saveMemberConference(messageMemberConference);


                }

            }

        }

    }


    public void inValid(Long id) {


        MessageListDO messageListDO = messageBaseDao.queryById(id);
        messageListDO.setStatus(ActStatusType.INVALID);

        messageBaseDao.update(messageListDO);

    }


    /**
     * 发布消息
     *
     * @param id
     */
    public void publishMessage(Long id) {
        MessageListDO messageListDO = messageBaseDao.queryById(id);
        messageListDO.setStatus(ActStatusType.PUBLISH);

        messageBaseDao.update(messageListDO);
    }

    public void saveMessage(MessageListDO messageListDO) {
        messageListDO.setCreateTime(LocalDateTime.now());
        messageBaseDao.save(messageListDO);

    }

    public void editMessage(MessageListDO messageListDO) {

        messageBaseDao.editMessage(messageListDO);

    }

    public ModelMap getModelMapByActBaseId(ModelMap map, Long id) {
        MessageListDO messageListDO = messageBaseDao.queryById(id);
        map.addAttribute("messageListDO", messageListDO);
        return map;
    }

    @Override
    public List<AppCustomer> findCustomer(Long actId) {
        if (actId != null) {
            return messageBaseDao.findCustomer(actId);
        }
        return null;
    }

    @Override
    public List<SimpleStoreParam> findStore(Long actId) {
        if (actId != null) {
            return messageBaseDao.findStore(actId);
        }
        return null;
    }


    @Transactional
    public void edit(MessageListDO messageListDO, List<Long> storeList, ArrayList<Long> customerIds) {
        {
            messageBaseDao.editMessage(messageListDO);


            //修改会员促销表，先删除旧记录在新增

            if (null != customerIds && customerIds.size() > 0) {
                messageBaseDao.deleteMemberConferenceByMessageBaseId4Customer(messageListDO.getId());
                for (Long userId : customerIds) {
                    MessageMemberConference messageMemberConference = new MessageMemberConference();
                    messageMemberConference.setUserId(userId);
                    messageMemberConference.setMessageId(messageListDO.getId());
                    messageMemberConference.setCreateTime(new Date());
                    messageMemberConference.setIsRead(false);
                    messageMemberConference.setIdentityType(AppIdentityType.CUSTOMER);
                    messageBaseDao.saveMemberConference(messageMemberConference);
                }
            }

            // 修改门店 先删除旧的记录

            if (null != storeList && storeList.size() > 0) {
                messageBaseDao.deleteMemberConferenceByMessageBaseId4Stores(messageListDO.getId());
                for (Long storeId : storeList) {
                    MessageMemberConference messageMemberConference = new MessageMemberConference();
                    messageMemberConference.setStoreId(storeId);
                    messageMemberConference.setMessageId(messageListDO.getId());
                    messageMemberConference.setCreateTime(new Date());
                    messageMemberConference.setIsRead(false);
                    messageMemberConference.setIdentityType(messageMemberConference.getIdentityType());
                    messageMemberConference.setIdentityType(null);
                    messageBaseDao.saveMemberConference(messageMemberConference);
                }
            }


        }
    }


}

