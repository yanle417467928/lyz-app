package cn.com.leyizhuang.app.foundation.vo;

import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.foundation.pojo.activity.ActBaseDO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 促销视图
 * Created by panjie on 2017/11/22.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ActBaseVO {


    private Long id;

    // 促销代号
    private String actCode;

    // 创建时间
    private LocalDateTime createTime;

    // 标题
    private String title;

    // 活动开始时间
    private LocalDateTime beginTime;

    // 活动结束时间
    private LocalDateTime endTime;

    // 城市名称
    private String cityName;

    // 活动目标对象
    private String actTarget;

    // 促销基本类型
    private ActBaseType baseType;

    // 结果类型
    private ActPromotionType promotionType;

    // 条件类型
    private ActConditionType conditionType;

    // 是否叠加
    private Boolean isDouble;

    // 是否可退货
    private Boolean isReturnable;

    // 促销状态
    private String status;

    // 排序号
    private Integer sortId;

    private String type;

    // 专供类型
    private String rankCode;

    // 促销范围
    private String scope;

    public static final ActBaseVO transform(ActBaseDO actBaseDO) {
        if (actBaseDO != null) {
            ActBaseVO actBaseVO = new ActBaseVO();

            actBaseVO.setId(actBaseDO.getId());
            actBaseVO.setActCode(actBaseDO.getActCode());
            actBaseVO.setCreateTime(actBaseDO.getCreateTime());
            actBaseVO.setTitle(actBaseDO.getTitle());
            actBaseVO.setBeginTime(actBaseDO.getBeginTime());
            actBaseVO.setEndTime(actBaseDO.getEndTime());
            actBaseVO.setCityName(actBaseDO.getCityName());

            String target = actBaseDO.getActTarget();
            target = target.replace("0", "导购")
                    .replace("2", "装饰公司经理")
                    .replace("6", "顾客");

            actBaseVO.setActTarget(target);

            actBaseVO.setBaseType(actBaseDO.getBaseType());
            actBaseVO.setPromotionType(actBaseDO.getPromotionType());
            actBaseVO.setConditionType(actBaseDO.getConditionType());
            actBaseVO.setIsDouble(actBaseDO.getIsDouble());
            actBaseVO.setIsReturnable(actBaseDO.getIsReturnable());
            actBaseVO.setSortId(actBaseDO.getSortId());
            if (actBaseDO.getStatus() != null) {
                if (LocalDateTime.now().isAfter(actBaseDO.getEndTime())) {
                    // 促销过期
                    actBaseVO.setStatus("过期");
                } else {
                    actBaseVO.setStatus(actBaseDO.getStatus().getDescription());
                }
            } else {
                actBaseVO.setStatus(ActStatusType.NEW.getDescription());
            }

            actBaseVO.setType(actBaseDO.getBaseType().getDescription() + "_" + actBaseDO.getConditionType().getDescription() + "_" + actBaseDO.getPromotionType().getDescription());
            actBaseVO.setScope(actBaseDO.getScope());
            actBaseVO.setRankCode(actBaseDO.getRankCode());
            return actBaseVO;
        } else {
            return null;
        }

    }

    public static final List<ActBaseVO> transform(List<ActBaseDO> list) {
        List<ActBaseVO> vOList;
        if (null != list && list.size() > 0) {
            vOList = new ArrayList<>(list.size());
            list.forEach(cityDeliveryTime -> vOList.add(transform(cityDeliveryTime)));
        } else {
            vOList = new ArrayList<>(0);
        }
        return vOList;
    }

}
