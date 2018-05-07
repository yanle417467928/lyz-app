package cn.com.leyizhuang.app.foundation.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Jerry.Ren
 * create 2018-02-22 17:00
 * desc:
 **/

@Getter
@Setter
@ToString
public class WareHouseDO {

    private Long id;
    /**
     * 仓库编号
     */
    private String wareHouseNo;
    /**
     * 仓库名称
     */
    private String wareHouseName;
    /**
     * 城市id
     */
    private String cityId;
    /**
     * 城市名称
     */
    private String cityName;
    /**
     * 仓库类型
     */
    private String type;
    /**
     * 分公司id
     */
    private String sobId;

}
