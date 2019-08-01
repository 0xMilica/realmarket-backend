package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.UserKYC;
import io.realmarket.propeler.model.UserKYCDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserKYCDocumentRepository extends JpaRepository<UserKYCDocument, Long> {
  List<UserKYCDocument> findAllByUserKYC(UserKYC userKYC);
}
