package cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Jerry.Ren
 * create 2018-02-06 17:16
 * desc: 仓库整转零
 **/

@Getter
@Setter
@ToString
public class WtaWarehouseWholeSplitToUnit {

    /**
     * 分公司ID
     */
    private Long companyId;
    /**
     * 零售产品编码
     */
    private String c_d_gcode;

}
