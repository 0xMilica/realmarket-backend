package io.realmarket.propeler.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "CampaignDocumentsAccessRequest")
@Table(name = "campaign_documents_access_request")
public class CampaignDocumentsAccessRequest {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "CAMPAIGN_DOCUMENTS_ACCESS_REQUEST_SEQ")
  @SequenceGenerator(
      name = "CAMPAIGN_DOCUMENTS_ACCESS_REQUEST_SEQ",
      sequenceName = "CAMPAIGN_DOCUMENTS_ACCESS_REQUEST_SEQ",
      allocationSize = 1)
  private Long id;

  @JoinColumn(
      name = "campaignId",
      foreignKey = @ForeignKey(name = "campaign_documents_access_request_fk_on_campaign"))
  @ManyToOne(fetch = FetchType.LAZY)
  private Campaign campaign;

  @JoinColumn(
      name = "authId",
      foreignKey = @ForeignKey(name = "campaign_documents_access_request_fk_on_auth"))
  @ManyToOne(fetch = FetchType.LAZY)
  private Auth auth;

  @JoinColumn(
      name = "requestStateId",
      foreignKey = @ForeignKey(name = "campaign_documents_access_request_fk_on_request_state"))
  @NotNull
  @ManyToOne
  private RequestState requestState;
}
