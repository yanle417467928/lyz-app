package cn.com.leyizhuang.app.foundation.pojo;

import com.github.pagehelper.PageInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author Richard
 * Created on 2017/5/6.
 */
@Getter
@Setter
@ToString
public class GridDataVO<Data> implements Serializable {

    private List<Data> rows;
    private Long total;

    public GridDataVO<Data> transform(PageInfo<Data> pageInfo) {
        this.setRows(pageInfo.getList());
        this.setTotal(pageInfo.getTotal());
        return this;
    }

    public GridDataVO<Data> transform(List<Data> rows, Long total) {
        this.setRows(rows);
        this.setTotal(total);
        return this;
    }

}
