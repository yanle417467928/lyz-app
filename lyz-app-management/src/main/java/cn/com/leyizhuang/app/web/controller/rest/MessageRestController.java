package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.core.constant.ActStatusType;
import cn.com.leyizhuang.app.foundation.dao.MessageStoresDAO;
import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.message.MessageListDO;
import cn.com.leyizhuang.app.foundation.pojo.message.MessageMemberConference;
import cn.com.leyizhuang.app.foundation.pojo.message.MessageStoreDO;
import cn.com.leyizhuang.app.foundation.service.MessageBaseService;
import cn.com.leyizhuang.app.foundation.vo.MessageBaseVO;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import cn.com.leyizhuang.app.foundation.dao.MessageBaseDao;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王浩 on 2018/7/20.
 * 消息管理数据控制器
 */

@RestController
@RequestMapping(value = MessageRestController.PRE_URL, produces = "application/json;charset=utf8")
public class MessageRestController extends BaseRestController {

    protected final static String PRE_URL = "/rest/message";

    private final Logger logger = LoggerFactory.getLogger(AppAdminStoreInventoryRestController.class);

    @Resource
    private MessageBaseService messageBaseService;


    @Resource
    private MessageBaseDao messageBaseDao;


    /**
     * 分页查询
     *
     * @param offset
     * @param size
     * @param keywords
     * @return
     */
    @GetMapping(value = "/page/grid")
    public GridDataVO<MessageBaseVO> getActBaseList(Integer offset, Integer size, String keywords, String status, Long cityId) {
        GridDataVO<MessageBaseVO> gridDataVO = new GridDataVO<>();
        Integer page = getPage(offset, size);

        PageInfo<MessageListDO> messagebasePage = messageBaseService.queryPageVO(page, size, keywords, status, cityId);
        List<MessageListDO> messageListDOList = messagebasePage.getList();
        List<MessageBaseVO> messageListVOList = MessageBaseVO.transform(messageListDOList);
        gridDataVO.transform(messageListVOList, messagebasePage.getTotal());
        return gridDataVO;
    }



    @PostMapping(value = "/save")
    public ResultDTO<?> save(@Valid MessageListDO messageListDO,   String stores,BindingResult result,String people) throws IOException {
        if (!result.hasErrors()) {
            ObjectMapper objectMapper = new ObjectMapper();
            JavaType javaType3 = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, Long.class);
            JavaType javaType4 = objectMapper.getTypeFactory().constructParametricType(ArrayList.class,Long.class);
            ArrayList<Long> storeList =  objectMapper.readValue(stores, javaType3);
            ArrayList<Long> customerIds =  objectMapper.readValue(people, javaType4);
            messageBaseService.save(messageListDO,storeList,customerIds);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "保存成功", null);
        } else {
            return actFor400(result,"提交的数据有误");
        }
    }


    @PutMapping(value = "/invalid")
    public ResultDTO<?> invalid(String ids) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType javaType1 = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, Long.class);
        List<Long> idList = objectMapper.readValue(ids, javaType1);

        for (Long id : idList) {
            messageBaseService.inValid(id);
        }

        return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "失效成功", null);

    }


    @PutMapping(value = "/publish")
    public ResultDTO<?> publish(String ids) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        JavaType javaType1 = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, Long.class);
        List<Long> idList = objectMapper.readValue(ids, javaType1);
        for (Long id : idList) {
            messageBaseService.publishMessage(id);
        }
        return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "发布成功", null);

    }

    @PostMapping(value = "/edit")
    public ResultDTO<?> edit(@Valid MessageListDO messageListDO,String stores,BindingResult result,String people) throws IOException {
        if (!result.hasErrors()) {
            ObjectMapper objectMapper = new ObjectMapper();
            JavaType javaType3 = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, Long.class);
            JavaType javaType4 = objectMapper.getTypeFactory().constructParametricType(ArrayList.class,Long.class);
            ArrayList<Long> storeList =  objectMapper.readValue(stores, javaType3);
            ArrayList<Long> customerIds =  objectMapper.readValue(people, javaType4);


            if(messageListDO.getStatus().equals(ActStatusType.PUBLISH.getValue())){
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "已经发布，不允许修改", null);
            }

            messageBaseService.edit(messageListDO,storeList,customerIds);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "修改成功", null);
        } else {
            return actFor400(result,"提交的数据有误");
        }
    }


}
