package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.UserKYC;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserKYCRepository extends JpaRepository<UserKYC, Long> {}
