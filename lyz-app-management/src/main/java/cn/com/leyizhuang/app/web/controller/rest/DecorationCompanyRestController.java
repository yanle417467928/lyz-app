package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.foundation.pojo.DecorationCompanyDO;
import cn.com.leyizhuang.app.foundation.pojo.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.vo.DecorationCompanyVO;
import cn.com.leyizhuang.app.foundation.pojo.vo.GoodsVO;
import cn.com.leyizhuang.app.foundation.pojo.vo.GridDataVO;
import cn.com.leyizhuang.app.foundation.service.DecorationCompanyService;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/9/19
 */
@RestController
@RequestMapping(value = DecorationCompanyRestController.PRE_URL,produces = "application/json;charset=utf-8")
public class DecorationCompanyRestController extends BaseRestController {

    protected final static String PRE_URL = "/rest/company";

    private final Logger logger = LoggerFactory.getLogger(DecorationCompanyRestController.class);

    @Autowired
    private DecorationCompanyService decorationCompanyServiceImpl;

    @GetMapping(value = "/page/grid")
    public GridDataVO<DecorationCompanyVO> restGoodsPageGird(Integer offset, Integer size, String keywords){
        size = getSize(size);
        Integer page = getPage(offset, size);

        PageInfo<DecorationCompanyDO> companyDOPage = this.decorationCompanyServiceImpl.queryPage(page,size);
        List<DecorationCompanyDO> companyDOList = companyDOPage.getList();
        List<DecorationCompanyVO> decorationCompanyVOList = DecorationCompanyVO.transform(companyDOList);
        return new GridDataVO<DecorationCompanyVO>().transform(decorationCompanyVOList,companyDOPage.getTotal());
    }

}
