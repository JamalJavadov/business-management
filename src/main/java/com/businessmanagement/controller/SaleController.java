package com.businessmanagement.controller;

import com.businessmanagement.model.Sale;
import com.businessmanagement.model.SaleStatus;
import com.businessmanagement.repository.CustomerRepository;
import com.businessmanagement.repository.ProductRepository;
import com.businessmanagement.repository.SaleRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;

@Controller
@RequestMapping("/sales")
public class SaleController {
    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    public SaleController(SaleRepository saleRepository,
                          ProductRepository productRepository,
                          CustomerRepository customerRepository) {
        this.saleRepository = saleRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    @GetMapping
    public String listSales(Model model) {
        model.addAttribute("sales", saleRepository.findAll());
        return "sales";
    }

    @GetMapping("/new")
    public String newSale(Model model) {
        Sale sale = new Sale();
        sale.setStatus(SaleStatus.ACTIVE);
        sale.setSaleDate(LocalDate.now());
        model.addAttribute("sale", sale);
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("customers", customerRepository.findAll());
        model.addAttribute("statuses", SaleStatus.values());
        return "sale-form";
    }

    @PostMapping
    public String createSale(@Valid @ModelAttribute("sale") Sale sale,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("products", productRepository.findAll());
            model.addAttribute("customers", customerRepository.findAll());
            model.addAttribute("statuses", SaleStatus.values());
            return "sale-form";
        }
        saleRepository.save(sale);
        return "redirect:/sales";
    }
}
