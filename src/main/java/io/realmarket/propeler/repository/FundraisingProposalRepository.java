package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.FundraisingProposal;
import io.realmarket.propeler.model.RequestState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FundraisingProposalRepository extends JpaRepository<FundraisingProposal, Long> {

  Page<FundraisingProposal> findByRequestState(Pageable pageable, RequestState requestState);
}
