package cn.com.leyizhuang.app.foundation.pojo.city;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 城市
 *
 * @author Richard
 * Created on 2017-09-21 14:14
 **/
@ToString
@Getter
@Setter
public class City {

    private Long cityId;

    /**
     * 城市名称（唯一）
     */
    private String name;

    /**
     * 城市名称拼音
     */
    private String spell;

    /**
     * 城市名称首字母
     */
    private String briefSpell;

    /**
     * 城市编码（唯一）
     */
    private String number;

    /**
     * 所属分公司（组织架构的一种）ID
     */
    private Long structureId;

    /**
     * 所属分公司名称
     */
    private String structureTitle;

    /**
     * 是否生效
     */
    private Boolean enable;

    /**
     * 失效时间
     */
    private Date enableFalseTime;


}
