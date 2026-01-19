package com.businessmanagement.service;

import com.businessmanagement.model.Sale;
import com.businessmanagement.model.SaleStatus;
import com.businessmanagement.repository.SaleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {
    private final SaleRepository saleRepository;

    public DashboardService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    public double totalRevenue() {
        return saleRepository.findAll().stream()
                .filter(sale -> sale.getStatus() == SaleStatus.COMPLETED)
                .mapToDouble(sale -> sale.getProduct().getSalePrice())
                .sum();
    }

    public long activeSales() {
        return saleRepository.findAll().stream()
                .filter(sale -> sale.getStatus() == SaleStatus.ACTIVE)
                .count();
    }

    public Map<String, Double> monthlyRevenue(int monthsBack) {
        LocalDate cutoff = LocalDate.now().minusMonths(monthsBack - 1L).withDayOfMonth(1);
        List<Sale> sales = saleRepository.findAll().stream()
                .filter(sale -> sale.getStatus() == SaleStatus.COMPLETED)
                .filter(sale -> sale.getSaleDate() != null && !sale.getSaleDate().isBefore(cutoff))
                .collect(Collectors.toList());

        Map<YearMonth, Double> aggregated = new LinkedHashMap<>();
        sales.forEach(sale -> {
            YearMonth month = YearMonth.from(sale.getSaleDate());
            aggregated.merge(month, sale.getProduct().getSalePrice(), Double::sum);
        });

        return aggregated.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(
                        entry -> formatMonth(entry.getKey()),
                        Map.Entry::getValue,
                        (first, second) -> second,
                        LinkedHashMap::new
                ));
    }

    public Map<Integer, Double> yearlyRevenue() {
        return saleRepository.findAll().stream()
                .filter(sale -> sale.getStatus() == SaleStatus.COMPLETED)
                .filter(sale -> sale.getSaleDate() != null)
                .collect(Collectors.groupingBy(
                        sale -> sale.getSaleDate().getYear(),
                        Collectors.summingDouble(sale -> sale.getProduct().getSalePrice())
                ))
                .entrySet().stream()
                .sorted(Comparator.comparingInt(Map.Entry::getKey))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (first, second) -> second,
                        LinkedHashMap::new
                ));
    }

    private String formatMonth(YearMonth month) {
        String name = month.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
        return name + " " + month.getYear();
    }
}
