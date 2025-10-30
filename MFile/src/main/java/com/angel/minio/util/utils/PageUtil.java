package com.angel.minio.util.utils;

import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * @Author yangb
 * @Date 2022/11/3 19:00
 * @Version 1.0
 */
public class PageUtil {

    private PageUtil(){

    }
    //手动分页 自定义方法
    public static PageInfo pageInfo(Integer pageNo, Integer pageSize1, List list){
        try {
            // 手动封装PageInfo参数进行分页
            PageInfo<Map<String, Object>> realPageInfo = new PageInfo<Map<String, Object>>(list);
            int start = 0;
            int end = 0;
            int totalPages = 0;
            int totalRecord = 0;
            int pageSize = 0;
            int size = 0;
            int number = 0;
            size = pageSize1;
            number = pageNo;
            pageSize = pageSize1;
            totalRecord = list.size();
            //设置总数
            realPageInfo.setTotal(totalRecord);
            //设置每页的显示条数
            realPageInfo.setPageSize(size);
            //设置要显示的是第几页的数据
            realPageInfo.setPageNum(number);
            realPageInfo.setSize(totalRecord);
            //计算获取对应的要显示的数据
            if (totalRecord % pageSize == 0) {
                totalPages = totalRecord / pageSize;
            } else {
                totalPages = totalRecord / pageSize + 1;
            }
            realPageInfo.setPages(totalPages);
            //初始边界值计算
            if (number == 1) {
                start = 0;
                realPageInfo.setHasPreviousPage(false);
                realPageInfo.setPrePage(0);
                realPageInfo.setIsFirstPage(true);
            } else {
                start = realPageInfo.getPageSize() * (realPageInfo.getPageNum() - 1);
                realPageInfo.setHasPreviousPage(true);
                realPageInfo.setPrePage(number - 1);
                realPageInfo.setIsFirstPage(false);
            }
            realPageInfo.setStartRow((number - 1) * pageSize);
            //结束边界值计算
            if ((start + realPageInfo.getPageSize() > realPageInfo.getTotal())) {
                end = totalRecord;
                realPageInfo.setHasNextPage(false);
                realPageInfo.setIsLastPage(true);
                realPageInfo.setEndRow(totalRecord);
            } else {
                end = start + realPageInfo.getPageSize();
                realPageInfo.setHasNextPage(true);
                realPageInfo.setNextPage(number + 1);
                realPageInfo.setIsLastPage(false);
                realPageInfo.setEndRow((number) * pageSize);
            }
            if (start < end && end <= totalRecord) {
                realPageInfo.setList(list.subList(start, end));
            }
            else{
                /**
                 * 这里处理一下 超出页码就赋值NULL
                 */
                realPageInfo.setList(null);
            }
            if (realPageInfo.getSize() == 0) {
                realPageInfo.setStartRow(0);
                realPageInfo.setEndRow(0);
            } else {
                realPageInfo.setStartRow(realPageInfo.getStartRow() + 1);
                realPageInfo.setEndRow(realPageInfo.getStartRow() - 1 + realPageInfo.getSize());
            }
            realPageInfo.setPages(totalPages);
            realPageInfo.setNavigateLastPage(totalPages > number ? number + 1 : totalPages);
            return realPageInfo;
        }catch (Exception ex){
            return null;
        }

    }
}
