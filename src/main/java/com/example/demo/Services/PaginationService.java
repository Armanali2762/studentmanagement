package com.example.demo.Services;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

@Service
public interface PaginationService {

    Map<String, Object> getData(Long id, HttpServletRequest request);

    Map<String, Object> getHeader(Long id, HttpServletRequest request);

    Map<String, Object> getTableHeader(String id, HttpServletRequest request);

    Map<String, Object> getDefaultData(HttpServletRequest request);

}
