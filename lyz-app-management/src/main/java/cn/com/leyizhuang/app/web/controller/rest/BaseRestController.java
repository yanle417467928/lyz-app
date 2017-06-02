package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.web.controller.BaseController;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;

/**
 * @author CrazyApeDX
 *         Created on 2017/5/16.
 */
public abstract class BaseRestController extends BaseController {

    private final static Logger LOG = LoggerFactory.getLogger(BaseRestController.class);

    protected String errorMsgToHtml(List<ObjectError> allErrors) {
        StringBuilder builder = new StringBuilder();
        allErrors.forEach(error -> builder.append(error.getDefaultMessage()).append("<br/>"));
        return builder.toString();
    }

    private Integer validationPage(Integer page) {
        if (page <= 0) {
            page = CommonGlobal.PAGEABLE_DEFAULT_PAGE;
        }
        return page;
    }

    private Integer validationSize(Integer size) {
        if (size < CommonGlobal.PAGEABLE_MIN_SIZE || size > CommonGlobal.PAGEABLE_MAX_SIZE) {
            size = CommonGlobal.PAGEABLE_DEFAULT_SIZE;
        }
        return size;
    }

    private Integer countPage(Integer offset, Integer size) {
        return (offset / size) + 1;
    }

    protected Integer getPage(Integer offset, Integer size) {
        Integer page = countPage(offset, size);
        return validationPage(page);
    }

    protected Integer getSize(Integer size) {
        return validationSize(size);
    }

    protected ResultDTO<?> actFor400(BindingResult result, String logMessage) {
        List<ObjectError> allErrors = result.getAllErrors();
        String clientMessage = errorMsgToHtml(allErrors);
        LOG.warn("{}：BindingResult = {}", logMessage, clientMessage);
        error400();
        return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,
                clientMessage, null);
    }
}
