package cn.com.leyizhuang.app.foundation.pojo.inventory.invoicing;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
     * 开始时间
     */
    private String startTime;
    /**
     * 开始时间date格式
     */
    private Date startDateTime;
    /**
     * 结束时间
     */
    private String endTime;
    /**
     * 结束时间date格式
     */
    private Date endDateTime;

    public InvoicingQuery transformAttribute() {
        if ("-1".equals(this.city.toString())) {
            this.city = null;
        }
        if ("-1".equals(this.store.toString())) {
            this.store = null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (null != startTime) {
                this.startDateTime = sdf.parse(this.startTime);
            }
            if (null != endTime) {
                this.endDateTime = sdf.parse(this.endTime);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return this;
    }
}
