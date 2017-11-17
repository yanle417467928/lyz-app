package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.MaShoppingGuideDAO;
import cn.com.leyizhuang.app.foundation.service.MaShoppingGuideService;
import cn.com.leyizhuang.app.foundation.vo.ShoppingGuideVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaShoppingGuideServiceImpl implements MaShoppingGuideService {

    @Autowired
    private MaShoppingGuideDAO maEmployeeDAO;
        @Override
        public List<ShoppingGuideVO> findGuideListById(Long storeId){

            return  maEmployeeDAO.findGuideListById(storeId);
        }
    }


