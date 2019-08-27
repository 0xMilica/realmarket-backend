package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.BlockchainMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlockchainMessageRepository extends JpaRepository<BlockchainMessage, Long> {
  List<BlockchainMessage> findAllByOrderByIdAsc();
}
