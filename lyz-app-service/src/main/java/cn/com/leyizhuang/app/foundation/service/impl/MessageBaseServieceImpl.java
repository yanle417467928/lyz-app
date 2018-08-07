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
    public void save(MessageListDO messageListDO, ArrayList<Long> storeList, ArrayList<Long> customerIds, ArrayList<Long> employeeIds) {

        List<MessageMemberConference> listAll = new ArrayList<MessageMemberConference>();
        messageListDO.setStatus(ActStatusType.NEW);
        messageListDO.setCreateTime(LocalDateTime.now());
        messageBaseDao.save(messageListDO);
        String[] strings = null;
        //推送范围为全部的保存
        if (messageListDO.getScope().equals("ALL")) {
            if (messageListDO.getIdentityType() != null && !"".equals(messageListDO.getIdentityType())) {
                String r = messageListDO.getIdentityType();
                strings = r.split(",");
            }
            for (int j = 0; j < strings.length; j++) {
                for (AppIdentityType appIdentityType : AppIdentityType.values()) {
                    //保存全部员工 单选多选都可用
                    if (appIdentityType.toString().equals(strings[j]) && appIdentityType != AppIdentityType.CUSTOMER) {
                        List<SimpleEmployeeParam> AllEmployeeDgList = messageBaseDao.FindAllEmployeeId(appIdentityType.toString());

                        if (AllEmployeeDgList != null && AllEmployeeDgList.size() > 0) {
                            for (int i = 0; i < AllEmployeeDgList.size(); i++) {
                                MessageMemberConference messageMemberConference = new MessageMemberConference();
                                messageMemberConference.setUserId(AllEmployeeDgList.get(i).getId());
                                messageMemberConference.setMessageId(messageListDO.getId());
                                messageMemberConference.setCreateTime(new Date());
                                messageMemberConference.setIsRead(false);
                                messageMemberConference.setIdentityType(appIdentityType);
                                //messageBaseDao.saveMemberConference(messageMemberConference);
                                listAll.add(messageMemberConference);
                            }
                        }

                    }//保存全部会员 单选多选都可用
                    if (appIdentityType.toString().equals(strings[j]) && appIdentityType == AppIdentityType.CUSTOMER) {
                        List<AppCustomer> appCustomers = messageBaseDao.FindAllCustomerId();

                        if (appCustomers != null && appCustomers.size() > 0) {

                            for (int i = 0; i < appCustomers.size(); i++) {
                                MessageMemberConference messageMemberConference = new MessageMemberConference();
                                messageMemberConference.setUserId(appCustomers.get(i).getCusId());
                                messageMemberConference.setMessageId(messageListDO.getId());
                                messageMemberConference.setCreateTime(new Date());
                                messageMemberConference.setIsRead(false);
                                messageMemberConference.setIdentityType(appIdentityType);
                                // messageBaseDao.saveMemberConference(messageMemberConference);
                                listAll.add(messageMemberConference);
                            }
                        }

                    }
                }
            }
        }
        //推送范围为自定的
        if (messageListDO.getScope().equals("ZDY")) {
            if (messageListDO.getIdentityType().equals("") || messageListDO.getIdentityType() == null) {
                //选择了会员的保存
                if (null != customerIds && customerIds.size() > 0) {
                    for (Long userId : customerIds) {
                        MessageMemberConference messageMemberConference = new MessageMemberConference();
                        messageMemberConference.setUserId(userId);
                        messageMemberConference.setMessageId(messageListDO.getId());
                        messageMemberConference.setCreateTime(new Date());
                        messageMemberConference.setIsRead(false);
                        messageMemberConference.setIdentityType(AppIdentityType.CUSTOMER);
                        // messageBaseDao.saveMemberConference(messageMemberConference);
                        listAll.add(messageMemberConference);
                    }
                }
                // 选择了门店的保存
                if (null != storeList && storeList.size() > 0) {
                    for (Long storeId : storeList) {
                        List<SimpleEmployeeParam> emlist = messageBaseDao.FindEmployeeByStoreId(storeId);
                        MessageMemberConference messageMemberConference = new MessageMemberConference();
                        for (int i = 0; i < emlist.size(); i++) {
                            messageMemberConference.setUserId(emlist.get(i).getId());
                            messageMemberConference.setMessageId(messageListDO.getId());
                            messageMemberConference.setCreateTime(new Date());
                            messageMemberConference.setIsRead(false);
                            messageMemberConference.setIdentityType(emlist.get(i).getIdentityType());
                            // messageBaseDao.saveMemberConference(messageMemberConference);
                            listAll.add(messageMemberConference);
                        }

                        List<AppCustomer> cuslits = messageBaseDao.FindCustomerByStoreId(storeId);
                        if (cuslits != null && cuslits.size() > 0) {
                            for (int i = 0; i < cuslits.size(); i++) {
                                messageMemberConference.setUserId(cuslits.get(i).getCusId());
                                messageMemberConference.setMessageId(messageListDO.getId());
                                messageMemberConference.setCreateTime(new Date());
                                messageMemberConference.setIsRead(false);
                                messageMemberConference.setIdentityType(AppIdentityType.CUSTOMER);
                                // messageBaseDao.saveMemberConference(messageMemberConference);
                                listAll.add(messageMemberConference);
                            }
                        }

                    }
                }
                //选择了员工的保存
                if (null != employeeIds && employeeIds.size() > 0) {
                    for (Long employeeId : employeeIds) {
                        MessageMemberConference messageMemberConference = new MessageMemberConference();
                        List<SimpleEmployeeParam> emlist = messageBaseDao.FindEmployeeById(employeeId);
                        for (int i = 0; i < emlist.size(); i++) {
                            messageMemberConference.setUserId(emlist.get(i).getId());
                            messageMemberConference.setMessageId(messageListDO.getId());
                            messageMemberConference.setCreateTime(new Date());
                            messageMemberConference.setIsRead(false);
                            messageMemberConference.setIdentityType(emlist.get(i).getIdentityType());
                            //  messageBaseDao.saveMemberConference(messageMemberConference);
                            listAll.add(messageMemberConference);
                        }
                    }
                }
            }
        }
        if (listAll.size() > 0) {
            messageBaseDao.saveBatch(listAll);

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

  /*  @Override
    public List<SimpleStoreParam> findStore(Long actId) {
        if (actId != null) {
            return messageBaseDao.findStore(actId);
        }
        return null;
    }
*/

    @Transactional
    public void edit(MessageListDO messageListDO, ArrayList<Long> storeList, ArrayList<Long> customerIds, ArrayList<Long> employeeIds) {
        messageBaseDao.delete4Message(messageListDO.getId());
        messageBaseDao.editMessage(messageListDO);
        List<MessageMemberConference> listAll = new ArrayList<MessageMemberConference>();
        messageListDO.setStatus(ActStatusType.NEW);
        messageListDO.setCreateTime(LocalDateTime.now());
        String[] strings = null;
        //推送范围为全部的保存
        if (messageListDO.getScope().equals("ALL")) {
            if (messageListDO.getIdentityType() != null && !"".equals(messageListDO.getIdentityType())) {
                String r = messageListDO.getIdentityType();
                strings = r.split(",");
            }
            for (int j = 0; j < strings.length; j++) {
                for (AppIdentityType appIdentityType : AppIdentityType.values()) {
                    //保存全部员工 单选多选都可用
                    if (appIdentityType.toString().equals(strings[j]) && appIdentityType != AppIdentityType.CUSTOMER) {
                        List<SimpleEmployeeParam> AllEmployeeDgList = messageBaseDao.FindAllEmployeeId(appIdentityType.toString());

                        if (AllEmployeeDgList != null && AllEmployeeDgList.size() > 0) {

                            for (int i = 0; i < AllEmployeeDgList.size(); i++) {
                                MessageMemberConference messageMemberConference = new MessageMemberConference();
                                messageMemberConference.setUserId(AllEmployeeDgList.get(i).getId());
                                messageMemberConference.setMessageId(messageListDO.getId());
                                messageMemberConference.setCreateTime(new Date());
                                messageMemberConference.setIsRead(false);
                                messageMemberConference.setIdentityType(appIdentityType);
                                //messageBaseDao.saveMemberConference(messageMemberConference);
                                listAll.add(messageMemberConference);
                            }
                        }

                    }//保存全部会员 单选多选都可用
                    if (appIdentityType.toString().equals(strings[j]) && appIdentityType == AppIdentityType.CUSTOMER) {
                        List<AppCustomer> appCustomers = messageBaseDao.FindAllCustomerId();
                        if (appCustomers != null && appCustomers.size() > 0) {

                            for (int i = 0; i < appCustomers.size(); i++) {
                                MessageMemberConference messageMemberConference = new MessageMemberConference();
                                messageMemberConference.setUserId(appCustomers.get(i).getCusId());
                                messageMemberConference.setMessageId(messageListDO.getId());
                                messageMemberConference.setCreateTime(new Date());
                                messageMemberConference.setIsRead(false);
                                messageMemberConference.setIdentityType(appIdentityType);
                                // messageBaseDao.saveMemberConference(messageMemberConference);
                                listAll.add(messageMemberConference);
                            }
                        }

                    }
                }
            }
        }
        //推送范围为自定的
        if (messageListDO.getScope().equals("ZDY")) {
            if (messageListDO.getIdentityType().equals("") || messageListDO.getIdentityType() == null) {
                //选择了会员的保存
                if (null != customerIds && customerIds.size() > 0) {
                    for (Long userId : customerIds) {
                        MessageMemberConference messageMemberConference = new MessageMemberConference();
                        messageMemberConference.setUserId(userId);
                        messageMemberConference.setMessageId(messageListDO.getId());
                        messageMemberConference.setCreateTime(new Date());
                        messageMemberConference.setIsRead(false);
                        messageMemberConference.setIdentityType(AppIdentityType.CUSTOMER);
                        // messageBaseDao.saveMemberConference(messageMemberConference);
                        listAll.add(messageMemberConference);
                    }
                }
                // 选择了门店的保存
                if (null != storeList && storeList.size() > 0) {
                    for (Long storeId : storeList) {
                        List<SimpleEmployeeParam> emlist = messageBaseDao.FindEmployeeByStoreId(storeId);
                        MessageMemberConference messageMemberConference = new MessageMemberConference();
                        for (int i = 0; i < emlist.size(); i++) {
                            messageMemberConference.setUserId(emlist.get(i).getId());
                            messageMemberConference.setMessageId(messageListDO.getId());
                            messageMemberConference.setCreateTime(new Date());
                            messageMemberConference.setIsRead(false);
                            messageMemberConference.setIdentityType(emlist.get(i).getIdentityType());
                            // messageBaseDao.saveMemberConference(messageMemberConference);
                            listAll.add(messageMemberConference);
                        }

                        List<AppCustomer> cuslits = messageBaseDao.FindCustomerByStoreId(storeId);
                        if (cuslits != null && cuslits.size() > 0) {
                            for (int i = 0; i < emlist.size(); i++) {
                                messageMemberConference.setUserId(cuslits.get(i).getCusId());
                                messageMemberConference.setMessageId(messageListDO.getId());
                                messageMemberConference.setCreateTime(new Date());
                                messageMemberConference.setIsRead(false);
                                messageMemberConference.setIdentityType(AppIdentityType.CUSTOMER);
                                // messageBaseDao.saveMemberConference(messageMemberConference);
                                listAll.add(messageMemberConference);
                            }
                        }

                    }
                }
                //选择了员工的保存
                if (null != employeeIds && employeeIds.size() > 0) {
                    for (Long employeeId : employeeIds) {
                        MessageMemberConference messageMemberConference = new MessageMemberConference();
                        List<SimpleEmployeeParam> emlist = messageBaseDao.FindEmployeeById(employeeId);
                        for (int i = 0; i < emlist.size(); i++) {
                            messageMemberConference.setUserId(emlist.get(i).getId());
                            messageMemberConference.setMessageId(messageListDO.getId());
                            messageMemberConference.setCreateTime(new Date());
                            messageMemberConference.setIsRead(false);
                            messageMemberConference.setIdentityType(emlist.get(i).getIdentityType());
                            //  messageBaseDao.saveMemberConference(messageMemberConference);
                            listAll.add(messageMemberConference);
                        }
                    }
                }
            }
        }
        if (listAll.size() > 0) {
            messageBaseDao.saveBatch(listAll);

        }


    }
}




