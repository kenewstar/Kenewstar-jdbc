package org.kenewstar.jdbc.core.page;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kenewstar
 * @version 1.0
 * @date 2021/4/18
 */
public class Page<E> extends ArrayList<E> {

    private static final long serialVersionUID = 1L;

    /**     页码,从 1 开始   */
    private int pageNum;

    /**     每页大小    */
    private int pageSize;

    /**     总记录数    */
    private long total;

    /**     总页数     */
    private long pages;

    /**     当前页的数据列表    */
    private List<E> rows;

    public Page() {
        super();
    }

    public Page(int pageNum, int pageSize) {
        this(pageNum, pageSize, 0L, 0L);
    }

    public Page(int pageNum, int pageSize, long total, long pages) {
        this();
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
        this.pages = pages;
    }

    public Page<E> setPageNum(int pageNum) {
        this.pageNum = pageNum;
        return this;
    }

    public Page<E> setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public Page<E> setTotal(long total) {
        this.total = total;
        return this;
    }

    public Page<E> setPages(long pages) {
        this.pages = pages;
        return this;
    }

    public Page<E> setRows(List<E> rows) {
        this.rows = rows;
        return this;
    }


    public int getPageNum() {
        return pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public long getTotal() {
        return total;
    }

    public long getPages() {
        return pages;
    }

    public List<?> getRows() {
        return rows;
    }

    @Override
    public String toString() {
        return "Page{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", total=" + total +
                ", pages=" + pages +
                ", rows=" + rows +
                '}';
    }
}
