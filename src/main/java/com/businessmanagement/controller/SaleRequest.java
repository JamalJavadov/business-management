package com.businessmanagement.controller;

import com.businessmanagement.model.SaleStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class SaleRequest {
    @NotNull
    private Long productId;

    @NotNull
    private SaleStatus status;

    private LocalDate saleDate;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public SaleStatus getStatus() {
        return status;
    }

    public void setStatus(SaleStatus status) {
        this.status = status;
    }

    public LocalDate getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDate saleDate) {
        this.saleDate = saleDate;
    }
}
