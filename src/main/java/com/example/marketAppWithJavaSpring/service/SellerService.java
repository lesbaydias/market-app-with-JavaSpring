package com.example.marketAppWithJavaSpring.service;

import com.example.marketAppWithJavaSpring.enums.ErrorMessage;
import com.example.marketAppWithJavaSpring.exceptions.ServiceException;
import com.example.marketAppWithJavaSpring.model.Seller;
import com.example.marketAppWithJavaSpring.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class SellerService {
    private final SellerRepository sellerRepository;

    public List<Seller> findAll(){
        return sellerRepository.findAll();
    }

    @Transactional
    public Seller findByUsername(String username){
        return sellerRepository.findByUsername(username).orElseThrow(() ->
                new ServiceException(
                        String.format(ErrorMessage.SELLER_IS_NOT_FOUND.getMessage(), username),
                        ErrorMessage.SELLER_IS_NOT_FOUND.getStatus()
                )
        );
    }

    public Optional<Seller> findByUsernameWithoutThrow(String  username){
        return sellerRepository.findByUsername(username);
    }

    public Optional<Seller> getSellerByEmail(String email){
        return sellerRepository.findByEmail(email);
    }

    public Optional<Seller> getSellerByPhoneNumber(String phoneNumber){
        return sellerRepository.findByPhoneNumber(phoneNumber);
    }
    public void save(Seller seller){
        sellerRepository.save(seller);
    }

    @Transactional
    @Modifying
    public void deleteSeller(Long sellerId){
        Seller seller = sellerRepository.findById(sellerId).orElseThrow(()->
                new ServiceException(
                        String.format(ErrorMessage.SELLER_IS_NOT_FOUND.getMessage(), sellerId),
                        ErrorMessage.SELLER_IS_NOT_FOUND.getStatus()
                )
        );
        sellerRepository.deleteById(seller.getSellerId());
    }
}
