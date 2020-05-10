package com.productshop.productshop.repository;

import com.productshop.productshop.entity.Basket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasketRepository extends JpaRepository<Basket, Long> {

    Basket findFirstByUserIdAndDeletedFalseOrderByUserIdDesc(Long IdUser);
}
