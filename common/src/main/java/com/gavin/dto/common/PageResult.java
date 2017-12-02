package com.gavin.dto.common;

import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> {

    long totalRecords;

    int totalPages;

    int currentPage;

    List<T> records;

}
