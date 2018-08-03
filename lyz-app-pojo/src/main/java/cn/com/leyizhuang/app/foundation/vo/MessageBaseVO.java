package cn.com.leyizhuang.app.foundation.vo;

import cn.com.leyizhuang.app.core.constant.ActStatusType;
import cn.com.leyizhuang.app.foundation.pojo.message.MessageListDO;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王浩 on 2018/7/25.
 * 消息视图
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MessageBaseVO {
    private Long id;

    //消息标题
    private  String title;

    //消息详情
    private  String detailed;


    //身份类型
    private  String identityType;


    //创建时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    //城市ID
    private Long cityId;

    //开始时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime beginTime;

    //结束时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    // 消息状态
    private String status;

    //推送范围
    private String scope;

    //消息类型
    private String messageType;


    public static final MessageBaseVO transform(MessageListDO messageListDO) {
        if (messageListDO != null) {
            MessageBaseVO messageBaseVO = new MessageBaseVO();
            messageBaseVO.setId(messageListDO.getId());
            messageBaseVO.setTitle(messageListDO.getTitle());
            messageBaseVO.setDetailed(messageListDO.getDetailed());
            messageBaseVO.setIdentityType(messageListDO.getIdentityType());
            if (messageListDO.getStatus() != null) {
                if (LocalDateTime.now().isAfter(messageListDO.getEndTime())) {
                    // 促销过期
                    messageBaseVO.setStatus("过期");
                } else {
                    messageBaseVO.setStatus(messageListDO.getStatus().getDescription());
                }
            } else {
                messageBaseVO.setStatus(ActStatusType.NEW.getDescription());
            }
            messageBaseVO.setCreateTime(messageListDO.getCreateTime());
            messageBaseVO.setCityId(messageListDO.getCityId());
            messageBaseVO.setBeginTime(messageListDO.getBeginTime());
            messageBaseVO.setEndTime(messageListDO.getEndTime());
            messageBaseVO.setScope(messageListDO.getScope());
            messageBaseVO.setMessageType(messageListDO.getMessageType());
            return messageBaseVO;
        } else {
            return null;
        }

    }
    public static final List<MessageBaseVO> transform(List<MessageListDO> list) {
        List<MessageBaseVO> vOList;
        if (null != list && list.size() > 0) {
            vOList = new ArrayList<>(list.size());
            list.forEach(cityDeliveryTime -> vOList.add(transform(cityDeliveryTime)));
        } else {
            vOList = new ArrayList<>(0);
        }
        return vOList;
    }

}


