package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 获取顾客签到明细接口返回对象
 *
 * @author Richard
 * Created on 2017-12-14 17:32
 **/
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CustomerSignDetailResponse<Content> implements Serializable{


    private static final long serialVersionUID = -6631708612837160074L;

    /**
     * 记录总条数
     */
    private Long count;

    /**
     * 总页数
     */
    private Integer totalPage;

    /**
     * 单页条数
     */
    private Integer numsPerPage;

    /**
     * 当前页数
     */
    private Integer currentPage;

    /**
     * 顾客签到日志简要信息
     */
    List<Content> data;

}
