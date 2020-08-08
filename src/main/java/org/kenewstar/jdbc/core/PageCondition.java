package org.kenewstar.jdbc.core;

import java.util.List;

/**
 * 封装分页查询条件
 * @author kenewstar
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
    private List<Sort> sorts;

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
    public PageCondition(Integer pageSize, Integer pageNumber, List<Sort> sorts) {
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
        this.sorts = sorts;
    }

    //========= getter && setter ===================//

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public List<Sort> getSort() {
        return sorts;
    }

    public void setSort(List<Sort> sorts) {
        this.sorts = sorts;
    }

}
