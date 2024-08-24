package com.example.demo.Controllers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Services.PaginationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("pagination")
public class PaginationController {

    private final PaginationService paginationService;

    @GetMapping("/data/{id}")
    public Map<String, Object> getData(@PathVariable Long id, HttpServletRequest request) {
        return paginationService.getData(id, request);
    }

    @GetMapping("/header/{id}")
    public Map<String, Object> getHeader(@PathVariable Long id, HttpServletRequest request) {
        return paginationService.getHeader(id, request);
    }

    @GetMapping("/table-header/{id}")
    public Map<String, Object> getTableHeader(@PathVariable String id, HttpServletRequest request) {
        return paginationService.getTableHeader(id, request);
    }

    @GetMapping("/default-data")
    public Map<String, Object> getDefaultData(HttpServletRequest request) {
        return paginationService.getDefaultData(request);
    }
}
