package com.example.marketAppWithJavaSpring.repository;

import com.example.marketAppWithJavaSpring.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p from Product p where p.category.categoryTitle = :category")
    List<Product> findByCategory(String category);

    @Query("SELECT p.productName, AVG(r.rating) " +
            "FROM Product p JOIN Review r " +
            "ON r.product.productId = p.productId " +
            "GROUP BY p.productId " +
            "ORDER BY AVG(r.rating) DESC ")
    List<Object[]> findByAllProductWithRating();

    Optional<Product> findProductsByProductId(Long id);

    @Query("select p from Product p " +
            "inner join Seller s " +
            "on p.seller.sellerId = s.sellerId where s.sellerId = :id")
    List<Product> findSellerWithAllProducts(Long id);

    @Transactional
    @Modifying
    void deleteProductByProductId(Long productId);

    Optional<Product> findProductsByProductNameAndSellerSellerId(String productName, Long sellerId);

}
