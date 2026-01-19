package com.businessmanagement.controller;

import com.businessmanagement.model.Customer;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final SaleRepository saleRepository;

    public CustomerController(CustomerRepository customerRepository,
                              ProductRepository productRepository,
                              SaleRepository saleRepository) {
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.saleRepository = saleRepository;
    }

    @GetMapping
    public String listCustomers(Model model) {
        model.addAttribute("customers", customerRepository.findAll());
        return "customers";
    }

    @GetMapping("/new")
    public String newCustomer(Model model) {
        model.addAttribute("customer", new Customer());
        return "customer-form";
    }

    @PostMapping
    public String createCustomer(@Valid @ModelAttribute("customer") Customer customer,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "customer-form";
        }
        customerRepository.save(customer);
        return "redirect:/customers";
    }

    @GetMapping("/{id}")
    public String viewCustomer(@PathVariable("id") Long id, Model model) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        model.addAttribute("customer", customer);
        model.addAttribute("sales", saleRepository.findByCustomerId(id));
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("statuses", SaleStatus.values());
        model.addAttribute("saleRequest", new SaleRequest());
        return "customer-detail";
    }

    @PostMapping("/{id}/sales")
    public String addSale(@PathVariable("id") Long id,
                          @Valid @ModelAttribute("saleRequest") SaleRequest saleRequest,
                          BindingResult bindingResult,
                          Model model) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        if (bindingResult.hasErrors()) {
            model.addAttribute("customer", customer);
            model.addAttribute("sales", saleRepository.findByCustomerId(id));
            model.addAttribute("products", productRepository.findAll());
            model.addAttribute("statuses", SaleStatus.values());
            return "customer-detail";
        }

        Sale sale = new Sale();
        sale.setCustomer(customer);
        sale.setProduct(productRepository.findById(saleRequest.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found")));
        sale.setStatus(saleRequest.getStatus());
        sale.setSaleDate(saleRequest.getSaleDate());
        saleRepository.save(sale);

        return "redirect:/customers/" + id;
    }
}
