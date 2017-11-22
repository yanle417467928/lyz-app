package cn.com.leyizhuang.app.web.controller.rest;


import cn.com.leyizhuang.app.foundation.service.MaShoppingGuideService;
import cn.com.leyizhuang.app.foundation.vo.ShoppingGuideVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = MaShoppingGuideRestController.PRE_URL,produces = "application/json;charset=utf-8")
public class MaShoppingGuideRestController extends  BaseRestController {

    protected static  final String  PRE_URL = "/rest/employees";

    private final Logger logger = LoggerFactory.getLogger(MaShoppingGuideRestController.class);

    @Autowired
    private MaShoppingGuideService maShoppingGuideService;

    /**
     * 根据门店ID查询导购
     * @param storeId
     * @return
     */
    @GetMapping(value = "/findGuidesListById/{storeId}")
    public List<ShoppingGuideVO> findGuidesListById(@PathVariable("storeId") Long storeId){
      return   maShoppingGuideService.findGuideListById(storeId);
    }

}
