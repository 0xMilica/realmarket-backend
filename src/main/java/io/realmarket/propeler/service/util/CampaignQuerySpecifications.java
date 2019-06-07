package io.realmarket.propeler.service.util;

import io.realmarket.propeler.model.Campaign;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class CampaignQuerySpecifications {
  public static Specification<Campaign> isNotDeleted() {
    return new Specification<Campaign>() {
      @Override
      public Predicate toPredicate(
          Root<Campaign> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        System.out.println(root.get("campaignState"));
        return criteriaBuilder.equal(root.get("campaignState"), 8);
      }
    };
  }
}
