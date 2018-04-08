package cn.com.leyizhuang.app.foundation.pojo;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author GenerationRoad
 * @date 2018/4/8
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AreaManagementDO implements Serializable{
    private Long id;
    //地区名称
    private String areaName;
    //地区编码
    private String code;
    //等级
    private String level;
    //上级编码
    private String parentCode;
    //状态
    private Boolean status;
    //创建时间
    private Date createTime;

}
