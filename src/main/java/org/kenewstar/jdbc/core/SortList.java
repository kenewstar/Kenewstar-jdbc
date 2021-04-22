package org.kenewstar.jdbc.core;

import org.kenewstar.jdbc.function.FunctionColumn;
import org.kenewstar.jdbc.util.FunctionUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 排序参数类
 * @author kenewstar
 * @date 2020-08-08
 * @version 0.1
 */
public class SortList {


    private final List<Sort> sortList;

    public static class Sort {
        private String column;
        private Order order;

        private Sort() {}

        public static Sort buildSort() {
            return new Sort();
        }

        public Sort setColumn(String column) {
            this.column = column;
            return this;
        }

        public Sort setOrder(Order order) {
            this.order = order;
            return this;
        }

        public String getColumn() {
            return column;
        }

        public Order getOrder() {
            return order;
        }
    }

    public enum Order {
        // 降序
        DESC,
        // 升序
        ASC;
    }

    private SortList() {
        // 初始化集合
        sortList = new ArrayList<>();
    }


    public static SortList build() {
        return new SortList();
    }

    public <T, V> SortList addSort(FunctionColumn<T, V> column, Order order) {
        String columnName = FunctionUtil.getColumnName(column);

        sortList.add(Sort.buildSort()
                .setColumn(columnName)
                .setOrder(order));

        return this;
    }

    public List<Sort> getSortList() {
        return sortList;
    }


}
