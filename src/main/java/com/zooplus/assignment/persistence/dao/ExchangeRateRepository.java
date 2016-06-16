package com.zooplus.assignment.persistence.dao;


import com.zooplus.assignment.persistence.model.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate,Long> {
}
