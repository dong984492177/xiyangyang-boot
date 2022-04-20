package com.ywt.console.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ywt.common.bean.PageWrapper;
import com.ywt.common.response.DefaultResponseDataWrapper;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: huangchaoyang
 * @Description: 代码分页工具类
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
public class PageUtils {

    public static<T> IPage<T> pageList(final List<T> target, Integer pageNo, Integer pageSize){
        IPage<T> page = new Page<>();
        int size = target.size();
        int pageCount = size / pageSize;
        int fromIndex = pageSize * (pageNo - 1);
        int toIndex = fromIndex + pageSize;
        if (toIndex >= size) {
            toIndex = size;
        }
        if (pageNo > pageCount + 1) {
            fromIndex = 0;
            toIndex = 0;
        }
        page.setRecords(target.subList(fromIndex, toIndex));
        page.setTotal(size);
        return page;
    }

    public static <T>  void page(final List<T> target, DefaultResponseDataWrapper<List<T>> res, Integer pageNo, Integer pageSize, PageWrapper wrapper){

        int size = target.size();
        wrapper.setTotal(size);
        wrapper.setPageSize(pageSize);
        int pageTotal=0;
        if(size<pageSize){
            wrapper.setPageTotal(1);
            wrapper.setPageNo(1);
            wrapper.setTotal(size);
            res.setData(target);
            wrapper.setPageTotal(1);
            res.setPage(wrapper);
        }else{
            List<T> result = new ArrayList<>();

            if((pageNo-1)*pageSize+1<size){
                int len = (pageNo-1)*pageSize;

                if(size<=len){
                    for(int i= len;i<size;i++) {
                        result.add(target.get(i));
                    }
                    pageTotal =pageNo;
                }else{
                    if(size<=pageNo*pageSize) {
                        for (int i = len; i < size; i++) {
                            result.add(target.get(i));
                        }
                    }else{
                        for (int i = len; i <pageNo*pageSize; i++) {
                            result.add(target.get(i));
                        }
                    }
                    pageTotal = size%pageSize>0?(size/pageSize)+1:size/pageSize;
                }

            }
            if(!ObjectUtils.isEmpty(result)){
                res.setData(result);
                wrapper.setPageNo(pageNo);
                wrapper.setPageTotal(pageTotal);
            }

            res.setPage(wrapper);
            res.setData(result);

        }

    }
}
