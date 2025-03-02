package com.restaurant.repositories;

import com.restaurant.entities.AllTables;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllTablesRepository extends JpaRepository<AllTables, Integer> {
}
