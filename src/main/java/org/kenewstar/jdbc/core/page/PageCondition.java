package org.kenewstar.jdbc.core.page;

import org.kenewstar.jdbc.core.SortList;

import java.util.List;

/**
 * 封装分页查询条件
 * @author kenewstar
 * @date 2020-08-08
 * @version 0.1
 */
public class PageCondition {
    /**
     * 每页记录数
     */
    private Integer pageSize;
    /**
     * 第几页(页码)
     * 页码从 0 开始
     */
    private Integer pageNumber;
    /**
     * 封装排序条件
     */
    private SortList sorts;

    /**
     * 无参构造
     */
    public PageCondition(){}

    /**
     * 只进行分页操作，不进行排序操作
     * @param pageSize 每页记录数
     * @param pageNumber 页码的大小
     */
    public PageCondition(Integer pageSize, Integer pageNumber) {
        this(pageSize,pageNumber,null);
    }
    /**
     * 全参构造
     * @param pageSize 每页记录数
     * @param pageNumber 页码大小
     * @param sorts 排序参数
     */
    public PageCondition(Integer pageSize, Integer pageNumber, SortList sorts) {
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
        this.sorts = sorts;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public PageCondition setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public PageCondition setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
        return this;
    }

    public SortList getSort() {
        return sorts;
    }

    public PageCondition setSort(SortList sorts) {
        this.sorts = sorts;
        return this;
    }

}
