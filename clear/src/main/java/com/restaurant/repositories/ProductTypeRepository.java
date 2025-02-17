package com.restaurant.repositories;

import com.restaurant.entities.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductTypeRepository extends JpaRepository<ProductType, Long> {
    ProductType findByTypeName(String typeName);
}
