package com.productshop.productshop.repository;

import com.productshop.productshop.entity.Picture;
import com.productshop.productshop.entity.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PictureRepository extends CrudRepository<Picture,Long> {

    List<Picture> getAllByProduct(Product product);
}
