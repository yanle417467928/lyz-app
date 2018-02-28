package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SupportHotlineResponse {

    private Long id;
    //热线名称
    private String name;
    //热线电话
    private  String SupportHotline;

}
