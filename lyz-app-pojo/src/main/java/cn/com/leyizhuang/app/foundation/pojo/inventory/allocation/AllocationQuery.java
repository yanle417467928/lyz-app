package cn.com.leyizhuang.app.foundation.pojo.inventory.allocation;

import cn.com.leyizhuang.app.core.constant.AllocationTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Jerry.Ren
 * Notes:门店调拨查询参数类
 * Created with IntelliJ IDEA.
 * Date: 2018/1/3.
 * Time: 10:48.
 */
@Getter
@Setter
@ToString
public class AllocationQuery {

    /**
     * 城市ID
     */
    private Long city;
    /**
     * 门店调出ID
     */
    private Long formName;
    /**
     * 门店调入ID
     */
    private Long toName;
    /**
     * 状态
     */
    private Integer statusNumber;
    /**
     * 状态枚举格式
     */
    private String allocationTypeEnum;
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

    public void transformAttribute() {
        if ("-1".equals(this.city.toString())) {
            this.city = null;
        }
        if ("-1".equals(this.formName.toString())) {
            this.formName = null;
        }
        if ("-1".equals(this.toName.toString())) {
            this.toName = null;
        }
        if ("-1".equals(this.statusNumber.toString())) {
            this.allocationTypeEnum = null;
        } else {
            this.allocationTypeEnum = AllocationTypeEnum.getName(this.statusNumber);
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
    }
}