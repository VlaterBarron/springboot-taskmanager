package com.vlater.taskmanager.dto.response;

import com.vlater.taskmanager.model.Task;
import org.springframework.data.domain.Page;

import java.util.List;

public class PagedResponse<T> {
    private List<T> list;
    private int page;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public static <T> PagedResponse<T> createPagedResponse(List<T> content, Page<Task> page) {
        PagedResponse<T> res = new PagedResponse<>();
        res.setList(content);
        res.setPage(page.getNumber());
        res.setPageSize(page.getSize());
        res.setTotalPages(page.getTotalPages());
        res.setTotalElements(page.getTotalElements());
        res.setLast(page.isLast());
        return res;
    }
}
