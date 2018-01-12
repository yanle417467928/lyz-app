package cn.com.leyizhuang.app.core.bean;

import com.github.pagehelper.PageInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author CrazyApeDX
 *         Created on 2017/5/6.
 */
@Getter
@Setter
@ToString
public class GridDataVO<Data> implements Serializable {

    private List<Data> list;
    private Long total;
    private Integer pages;
    private Integer size;
    private Integer pageNum;
    private Integer pageSize;

    public GridDataVO<Data> transform(List<Data> list,PageInfo pageInfo) {
        this.setList(list);
        this.setTotal(pageInfo.getTotal());
        this.setPages(pageInfo.getPages());
        this.setSize(pageInfo.getSize());
        this.setPageNum(pageInfo.getPageNum());
        this.setPageSize(pageInfo.getPageSize());
        return this;
    }

    public GridDataVO<Data> transform(PageInfo<Data> pageInfo) {
        this.setList(pageInfo.getList());
        this.setTotal(pageInfo.getTotal());
        this.setPages(pageInfo.getPages());
        this.setSize(pageInfo.getSize());
        this.setPageNum(pageInfo.getPageNum());
        this.setPageSize(pageInfo.getPageSize());
        return this;
    }

}
