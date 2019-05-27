package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, String> {
  Optional<Country> findByCode(String code);
}
