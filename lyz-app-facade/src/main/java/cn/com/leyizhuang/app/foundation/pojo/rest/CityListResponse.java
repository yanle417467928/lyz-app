package cn.com.leyizhuang.app.foundation.pojo.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 员工登录接口返回对象
 *
 * @author Richard
 * Created on 2017-09-19 13:57
 **/
@Getter
@Setter
@ToString
public class CityListResponse implements Serializable{

    private Long cityId;

    private String cityName;
}
