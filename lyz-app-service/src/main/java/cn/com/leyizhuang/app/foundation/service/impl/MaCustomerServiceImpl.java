package cn.com.leyizhuang.app.foundation.service.impl;


import cn.com.leyizhuang.app.core.constant.AppCustomerLightStatus;
import cn.com.leyizhuang.app.foundation.dao.MaCustomerDAO;
import cn.com.leyizhuang.app.foundation.pojo.management.Customer;
import cn.com.leyizhuang.app.foundation.service.MaCustomerService;
import cn.com.leyizhuang.app.foundation.vo.CustomerVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MaCustomerServiceImpl implements MaCustomerService {

    @Resource
    private MaCustomerDAO maCustomerDAO;

    @Override
    public PageInfo<CustomerVO> queryPageVO(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<CustomerVO> CustmoerList = maCustomerDAO.findAllVO();
        for (CustomerVO customerVO : CustmoerList) {
            if ("GREEN".equals(customerVO.getLight())) {
                customerVO.setLight(AppCustomerLightStatus.GREEN.getValue());
            } else if ("YELLOW".equals(customerVO.getLight())) {
                customerVO.setLight(AppCustomerLightStatus.YELLOW.getValue());
            } else if ("RED".equals(customerVO.getLight())) {
                customerVO.setLight(AppCustomerLightStatus.RED.getValue());
            } else if ("CLOSE".equals(customerVO.getLight())) {
                customerVO.setLight(AppCustomerLightStatus.CLOSE.getValue());
            }
        }
        return new PageInfo<>(CustmoerList);
    }

    @Override
    public CustomerVO queryCustomerVOById(Long cusId) {
        if (cusId != null) {
            CustomerVO customerVO = maCustomerDAO.queryCustomerVOById(cusId);
            if ("GREEN".equals(customerVO.getLight())) {
                customerVO.setLight(AppCustomerLightStatus.GREEN.getValue());
            } else if ("YELLOW".equals(customerVO.getLight())) {
                customerVO.setLight(AppCustomerLightStatus.YELLOW.getValue());
            } else if ("RED".equals(customerVO.getLight())) {
                customerVO.setLight(AppCustomerLightStatus.RED.getValue());
            } else if ("CLOSE".equals(customerVO.getLight())) {
                customerVO.setLight(AppCustomerLightStatus.CLOSE.getValue());
            }
            return customerVO;
        }
        return null;
    }

    @Override
    public PageInfo<CustomerVO> queryCustomerVOByCityId(Integer page, Integer size, Long cityId) {
        PageHelper.startPage(page, size);
        List<CustomerVO> CustmoerList = maCustomerDAO.queryCustomerVOByCityId(cityId);
        for (CustomerVO customerVO : CustmoerList) {
            if ("GREEN".equals(customerVO.getLight())) {
                customerVO.setLight(AppCustomerLightStatus.GREEN.getValue());
            } else if ("YELLOW".equals(customerVO.getLight())) {
                customerVO.setLight(AppCustomerLightStatus.YELLOW.getValue());
            } else if ("RED".equals(customerVO.getLight())) {
                customerVO.setLight(AppCustomerLightStatus.RED.getValue());
            } else if ("CLOSE".equals(customerVO.getLight())) {
                customerVO.setLight(AppCustomerLightStatus.CLOSE.getValue());
            }
        }
        return new PageInfo<>(CustmoerList);
    }


    @Override
    public PageInfo<CustomerVO> queryCustomerVOByStoreId(Integer page, Integer size, Long storeId) {
        PageHelper.startPage(page, size);
        List<CustomerVO> CustmoerList = maCustomerDAO.queryCustomerVOByStoreId(storeId);
        for (CustomerVO customerVO : CustmoerList) {
            if ("GREEN".equals(customerVO.getLight())) {
                customerVO.setLight(AppCustomerLightStatus.GREEN.getValue());
            } else if ("YELLOW".equals(customerVO.getLight())) {
                customerVO.setLight(AppCustomerLightStatus.YELLOW.getValue());
            } else if ("RED".equals(customerVO.getLight())) {
                customerVO.setLight(AppCustomerLightStatus.RED.getValue());
            } else if ("CLOSE".equals(customerVO.getLight())) {
                customerVO.setLight(AppCustomerLightStatus.CLOSE.getValue());
            }
        }
        return new PageInfo<>(CustmoerList);
    }

    @Override
    public PageInfo<CustomerVO> queryCustomerVOByGuideId(Integer page, Integer size, Long guideId) {
        PageHelper.startPage(page, size);
        List<CustomerVO> CustmoerList = maCustomerDAO.queryCustomerVOByGuideId(guideId);
        for (CustomerVO customerVO : CustmoerList) {
            if ("GREEN".equals(customerVO.getLight())) {
                customerVO.setLight(AppCustomerLightStatus.GREEN.getValue());
            } else if ("YELLOW".equals(customerVO.getLight())) {
                customerVO.setLight(AppCustomerLightStatus.YELLOW.getValue());
            } else if ("RED".equals(customerVO.getLight())) {
                customerVO.setLight(AppCustomerLightStatus.RED.getValue());
            } else if ("CLOSE".equals(customerVO.getLight())) {
                customerVO.setLight(AppCustomerLightStatus.CLOSE.getValue());
            }
        }
        return new PageInfo<>(CustmoerList);
    }

    @Override
    public PageInfo<CustomerVO> queryCustomerVOByPhone(Integer page, Integer size, Long queryCusInfo) {
        PageHelper.startPage(page, size);
        List<CustomerVO> CustmoerList = maCustomerDAO.queryCustomerVOByPhone(queryCusInfo);
        for (CustomerVO customerVO : CustmoerList) {
            if ("GREEN".equals(customerVO.getLight())) {
                customerVO.setLight(AppCustomerLightStatus.GREEN.getValue());
            } else if ("YELLOW".equals(customerVO.getLight())) {
                customerVO.setLight(AppCustomerLightStatus.YELLOW.getValue());
            } else if ("RED".equals(customerVO.getLight())) {
                customerVO.setLight(AppCustomerLightStatus.RED.getValue());
            } else if ("CLOSE".equals(customerVO.getLight())) {
                customerVO.setLight(AppCustomerLightStatus.CLOSE.getValue());
            }
        }
        return new PageInfo<>(CustmoerList);
    }

    @Override
    public PageInfo<CustomerVO> queryCustomerVOByName(Integer page, Integer size, String queryCusInfo) {
        PageHelper.startPage(page, size);
        List<CustomerVO> CustmoerList = maCustomerDAO.queryCustomerVOByName(queryCusInfo);
        for (CustomerVO customerVO : CustmoerList) {
            if ("GREEN".equals(customerVO.getLight())) {
                customerVO.setLight(AppCustomerLightStatus.GREEN.getValue());
            } else if ("YELLOW".equals(customerVO.getLight())) {
                customerVO.setLight(AppCustomerLightStatus.YELLOW.getValue());
            } else if ("RED".equals(customerVO.getLight())) {
                customerVO.setLight(AppCustomerLightStatus.RED.getValue());
            } else if ("CLOSE".equals(customerVO.getLight())) {
                customerVO.setLight(AppCustomerLightStatus.CLOSE.getValue());
            }
        }
        return new PageInfo<>(CustmoerList);
    }

    @Override
    public void saveCustomer(Customer customer) {
        if (null != customer) {
            maCustomerDAO.save(customer);
        }
    }

    @Override
    public Boolean isExistPhoneNumber(Long moblie) {
        return maCustomerDAO.isExistPhoneNumber(moblie);
    }
}

