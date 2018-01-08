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
    private String city;
    /**
     * 门店调出ID
     */
    private String selectFromName;
    /**
     * 门店调入ID
     */
    private String selectToName;
    /**
     * 状态
     */
    private Integer selectStatus;
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
        if ("-1".equals(this.selectFromName.toString())) {
            this.selectFromName = null;
        }
        if ("-1".equals(this.selectToName.toString())) {
            this.selectToName = null;
        }
        if ("-1".equals(this.selectStatus.toString())) {
            this.allocationTypeEnum = null;
        } else {
            this.allocationTypeEnum = AllocationTypeEnum.getName(this.selectStatus);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            this.startDateTime = sdf.parse(this.startTime);
            this.endDateTime = sdf.parse(this.endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}