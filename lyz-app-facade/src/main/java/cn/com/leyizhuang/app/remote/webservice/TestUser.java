package cn.com.leyizhuang.app.remote.webservice;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Created on 2017-12-19 11:21
 **/
@Getter
@Setter
@ToString
public class TestUser implements Serializable {

    private static final long serialVersionUID = -5939599230753662529L;
    private String userId;
    private String username;
    private String age;
    private Date updateTime;
}
