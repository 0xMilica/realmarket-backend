package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.CampaignInvestorDto;
import io.realmarket.propeler.api.dto.FileDto;
import io.realmarket.propeler.model.CampaignInvestor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CampaignInvestorService {

  CampaignInvestor createCampaignInvestor(
      String campaignName, CampaignInvestorDto campaignInvestorDto);

  List<CampaignInvestor> patchCampaignInvestorOrder(String campaignName, List<Long> order);

  List<CampaignInvestor> getCampaignInvestors(String campaignName);

  CampaignInvestor patchCampaignInvestor(
      String campaignName, Long investorId, CampaignInvestorDto campaignInvestorDto);

  void deleteCampaignInvestor(String campaignName, Long investorId);

  void uploadPicture(String campaignName, Long investorId, MultipartFile picture);

  FileDto downloadPicture(String campaignName, Long investorId);

  void deletePicture(String campaignName, Long investorId);
}
