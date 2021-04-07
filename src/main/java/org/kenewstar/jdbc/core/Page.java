package org.kenewstar.jdbc.core;

import java.util.List;

/**
 * 封装分页查询结果
 * @author kenewstar
 * @date 2020-08-08
 * @version 0.1
 */
public class Page<T> {

    /**
     * 总页数
     */
    private Integer totalPages;
    /**
     * 总记录数
     */
    private Integer totalRecords;
    /**
     * 当前页码
     */
    private Integer currentPage;
    /**
     * 每页的记录数
     */
    private Integer pageSize;
    /**
     * 当前页的数据内容
     */
    private List<T> contents;

    //==========getter && setter=================//

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(Integer totalRecords) {
        this.totalRecords = totalRecords;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getContents() {
        return contents;
    }

    public void setContents(List<T> contents) {
        this.contents = contents;
    }
}
