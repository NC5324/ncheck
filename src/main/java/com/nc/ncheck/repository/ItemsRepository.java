package com.nc.ncheck.repository;

import com.nc.ncheck.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemsRepository extends JpaRepository<Item, Long> {
}
