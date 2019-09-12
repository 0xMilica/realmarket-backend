package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.Investment;
import io.realmarket.propeler.model.Person;
import io.realmarket.propeler.model.enums.CampaignStateName;
import io.realmarket.propeler.model.enums.InvestmentStateName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvestmentRepository extends JpaRepository<Investment, Long> {

  List<Investment> findAllByCampaign(Campaign campaign);

  List<Investment> findAllByCampaignAndPerson(Campaign campaign, Person person);

  @Query(
      "SELECT c FROM Campaign c WHERE id IN (SELECT i.campaign FROM Investment i WHERE person = :person) ORDER BY modified_date DESC")
  Page<Campaign> findInvestedCampaign(@Param("person") Person person, Pageable pageable);

  @Query(
      "SELECT c FROM Campaign c LEFT JOIN CampaignState cs ON c.campaignState.id = cs.id WHERE cs.name = :state AND c.id IN (SELECT i.campaign FROM Investment i WHERE person = :person) ORDER BY modified_date DESC")
  Page<Campaign> findInvestedCampaignByState(
      @Param("person") Person person, @Param("state") CampaignStateName state, Pageable pageable);

  @Query(
      "SELECT c FROM Campaign c LEFT JOIN CampaignState cs ON c.campaignState.id = cs.id WHERE cs.name = 'SUCCESSFUL' OR cs.name = 'UNSUCCESSFUL' AND c.id IN (SELECT i.campaign FROM Investment i WHERE person = :person) ORDER BY modified_date DESC")
  Page<Campaign> findAllCompletedCampaigns(@Param("person") Person person, Pageable pageable);

  @Query(
      "SELECT DISTINCT i FROM Investment i JOIN Payment p ON p.investment.id = i.id LEFT JOIN InvestmentState ist ON i.investmentState.id = ist.id WHERE (:state = null AND ist.name IN ('OWNER_APPROVED', 'PAID', 'EXPIRED')) OR ist.name = :state")
  Page<Investment> findAllPaymentInvestment(
      @Param("state") InvestmentStateName state, Pageable pageable);
}
