package com.gavin.model;

import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> {

    long totalElements;

    int totalPages;

    int currentPage;

    List<T> contents;

}
