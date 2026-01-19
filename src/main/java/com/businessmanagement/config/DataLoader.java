package com.businessmanagement.config;

import com.businessmanagement.model.Customer;
import com.businessmanagement.model.Product;
import com.businessmanagement.model.ProductType;
import com.businessmanagement.model.Sale;
import com.businessmanagement.model.SaleStatus;
import com.businessmanagement.repository.CustomerRepository;
import com.businessmanagement.repository.ProductRepository;
import com.businessmanagement.repository.SaleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner loadSampleData(ProductRepository productRepository,
                                     CustomerRepository customerRepository,
                                     SaleRepository saleRepository) {
        return args -> {
            if (!productRepository.findAll().isEmpty()) {
                return;
            }

            Product ring = new Product();
            ring.setName("Brilliant Ring");
            ring.setType(ProductType.RING);
            ring.setDescription("18K gold ring with premium diamond cut.");
            ring.setImageUrl("https://images.unsplash.com/photo-1606305262640-0936a11f6f2c");
            ring.setPurchasePrice(450.0);
            ring.setSalePrice(780.0);
            productRepository.save(ring);

            Product necklace = new Product();
            necklace.setName("Elegant Necklace");
            necklace.setType(ProductType.NECKLACE);
            necklace.setDescription("Pearl necklace with handcrafted finish.");
            necklace.setImageUrl("https://images.unsplash.com/photo-1501200291282-d4e7da09d0d6");
            necklace.setPurchasePrice(300.0);
            necklace.setSalePrice(560.0);
            productRepository.save(necklace);

            Customer customerOne = new Customer();
            customerOne.setFullName("Leyla Karimova");
            customerOne.setEmail("leyla.karimova@example.com");
            customerOne.setPhoneNumber("+994 50 555 12 34");
            customerRepository.save(customerOne);

            Customer customerTwo = new Customer();
            customerTwo.setFullName("Emin Aliyev");
            customerTwo.setEmail("emin.aliyev@example.com");
            customerTwo.setPhoneNumber("+994 55 777 45 67");
            customerRepository.save(customerTwo);

            Sale saleOne = new Sale();
            saleOne.setProduct(ring);
            saleOne.setCustomer(customerOne);
            saleOne.setStatus(SaleStatus.COMPLETED);
            saleOne.setSaleDate(LocalDate.now().minusMonths(1));
            saleRepository.save(saleOne);

            Sale saleTwo = new Sale();
            saleTwo.setProduct(necklace);
            saleTwo.setCustomer(customerTwo);
            saleTwo.setStatus(SaleStatus.ACTIVE);
            saleTwo.setSaleDate(LocalDate.now());
            saleRepository.save(saleTwo);
        };
    }
}
