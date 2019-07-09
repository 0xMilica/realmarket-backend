package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.FundraisingProposal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FundraisingProposalRepository extends JpaRepository<FundraisingProposal, Long> {}
