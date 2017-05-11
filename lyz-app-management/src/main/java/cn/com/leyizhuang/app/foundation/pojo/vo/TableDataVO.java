package cn.com.leyizhuang.app.foundation.pojo.vo;

import com.github.pagehelper.PageInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author Richard
 *         Created on 2017/5/6.
 */
@Getter
@Setter
@ToString
public class TableDataVO<Data> implements Serializable {

    private List<Data> rows;
    private Long total;

    public TableDataVO<Data> transform(PageInfo<Data> pageInfo) {
        this.setRows(pageInfo.getList());
        this.setTotal(pageInfo.getTotal());
        return this;
    }

}
