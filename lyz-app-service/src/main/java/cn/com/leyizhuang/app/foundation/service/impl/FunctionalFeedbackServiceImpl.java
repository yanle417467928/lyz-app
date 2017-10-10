package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.FunctionalFeedbackDAO;
import cn.com.leyizhuang.app.foundation.pojo.FunctionalFeedbackDO;
import cn.com.leyizhuang.app.foundation.service.FunctionalFeedbackService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author GenerationRoad
 * @date 2017/10/10
 */
@Service
@Transactional
public class FunctionalFeedbackServiceImpl implements FunctionalFeedbackService {

    private FunctionalFeedbackDAO functionalFeedbackDAO;
    public FunctionalFeedbackServiceImpl(FunctionalFeedbackDAO functionalFeedbackDAO){
        this.functionalFeedbackDAO = functionalFeedbackDAO;
    }
    @Override
    public FunctionalFeedbackDO save(FunctionalFeedbackDO functionalFeedbackDO) {
        functionalFeedbackDO.setCreatorInfoByBusiness("FunctionalFeedbackServiceImpl", "save");
        return this.functionalFeedbackDAO.save(functionalFeedbackDO);
    }
}
