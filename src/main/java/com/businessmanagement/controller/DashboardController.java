package com.businessmanagement.controller;

import com.businessmanagement.repository.CustomerRepository;
import com.businessmanagement.repository.ProductRepository;
import com.businessmanagement.repository.SaleRepository;
import com.businessmanagement.service.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final SaleRepository saleRepository;
    private final DashboardService dashboardService;

    public DashboardController(ProductRepository productRepository,
                               CustomerRepository customerRepository,
                               SaleRepository saleRepository,
                               DashboardService dashboardService) {
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.saleRepository = saleRepository;
        this.dashboardService = dashboardService;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("productCount", productRepository.count());
        model.addAttribute("customerCount", customerRepository.count());
        model.addAttribute("saleCount", saleRepository.count());
        model.addAttribute("activeSales", dashboardService.activeSales());
        model.addAttribute("totalRevenue", dashboardService.totalRevenue());
        model.addAttribute("monthlyRevenue", dashboardService.monthlyRevenue(6));
        model.addAttribute("yearlyRevenue", dashboardService.yearlyRevenue());
        return "index";
    }
}
