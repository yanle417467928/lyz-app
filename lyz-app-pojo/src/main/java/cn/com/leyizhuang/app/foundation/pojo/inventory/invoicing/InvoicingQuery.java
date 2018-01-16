package cn.com.leyizhuang.app.foundation.pojo.inventory.invoicing;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Jerry.Ren
 * Notes:
 * Created with IntelliJ IDEA.
 * Date: 2018/1/15.
 * Time: 16:21.
 */

@Getter
@Setter
@ToString
public class InvoicingQuery {

    /**
     * 城市ID
     */
    private Long city;

    /**
     * 门店
     */
    private Long store;
    /**
     * 开始时间date格式
     */
    private String startDateTime;
    /**
     * 结束时间date格式
     */
    private String endDateTime;

}
