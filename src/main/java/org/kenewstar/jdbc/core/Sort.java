package org.kenewstar.jdbc.core;

import org.kenewstar.jdbc.function.FunctionColumn;
import org.kenewstar.jdbc.util.FunctionUtil;

/**
 * 排序参数类
 * @author kenewstar
 * @date 2020-08-08
 * @version 0.1
 */
public class Sort {

    /**
     * 排序的列名
     */
    private String column;
    /**
     * 排序方式
     */
    private Order order;
    /**
     * 降序
     */
    public static final Order DESC = Order.DESC;
    /**
     * 升序
     */
    public static final Order ASC = Order.ASC;

    private Sort() { }

    private Sort(String column) {
        this.column = column;
    }
    private Sort(String column, Order order) {
        this.column = column;
        this.order = order;
    }

    public static Sort buildSort() {
        return new Sort();
    }

    public static <T, V> Sort buildSort(FunctionColumn<T, V> column) {
        return new Sort(FunctionUtil.getColumnName(column));
    }

    public static <T, V> Sort buildSort(FunctionColumn<T, V> column, Order order) {
        return new Sort(FunctionUtil.getColumnName(column), order);
    }

    /**
     * 排序方式枚举类
     * @author kenewstar
     */
    private enum Order {
        //降序,升序
        DESC, ASC;

    }
    public String getColumn() {
        return column;
    }

    public Order getOrder() {
        return order;
    }

    public Sort setOrder(Order order) {
        this.order = order;
        return this;
    }
}
