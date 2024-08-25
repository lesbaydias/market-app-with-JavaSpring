package com.example.marketAppWithJavaSpring.service;

import com.example.marketAppWithJavaSpring.enums.ErrorMessage;
import com.example.marketAppWithJavaSpring.exceptions.ServiceException;
import com.example.marketAppWithJavaSpring.model.Sale;
import com.example.marketAppWithJavaSpring.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SaleService  {
    private final SaleRepository saleRepository;

    public List<Sale> getAllSalesProducts(){
        List<Sale> saleList = saleRepository.findAll();
        if(saleList.isEmpty())
            throw new ServiceException(
                    ErrorMessage.SALES_LIST_IS_EMPTY.getMessage(),
                    ErrorMessage.SALES_LIST_IS_EMPTY.getStatus()
            );
        return saleList;
    }

    @Transactional
    public Optional<Sale> getSaleByProduct(Long productId){
        return saleRepository.findSaleByProductProductId(productId);
    }
    public void save(Sale sale){
        saleRepository.save(sale);
    }
    @Transactional
    public void deleteByProductId(Long productId){
        saleRepository.deleteSaleByProductProductId(productId);
    }
}
