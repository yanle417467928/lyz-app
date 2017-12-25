package cn.com.leyizhuang.app.foundation.vo.management.city;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 城市VO
 *
 * @author liuh
 * Created on 2017-11-03 14:14
 **/
@ToString
@Getter
@Setter
public class CityVO {
    private Long cityId;
    // 城市名称（唯一）
    private String name;
    // 城市编码（唯一）
    private String code;
    // 所属分公司名称
    private String structureTitle;
    //是否生效
    private Boolean enable;
}
