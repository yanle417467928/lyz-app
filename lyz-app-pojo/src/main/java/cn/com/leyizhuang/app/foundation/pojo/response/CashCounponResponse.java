package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 顾客现金券接口返回对象
 *
 * @author Richard
 * Created on 2017-09-19 13:57
 **/
@Getter
@Setter
@ToString
public class CashCounponResponse implements Serializable{

    private Double denomination;

    private Date effectiveStartTime;

    private Date effectiveEndTime;

    private String description;
}
