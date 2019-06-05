package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.FileDto;
import io.realmarket.propeler.api.dto.ShareholderDto;
import io.realmarket.propeler.model.Shareholder;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ShareholderService {

  Shareholder createShareholder(String campaignName, ShareholderDto shareholderDto);

  List<Shareholder> patchShareholderOrder(String campaignName, List<Long> order);

  List<Shareholder> getShareholders(String campaignName);

  Shareholder patchShareholder(
      String campaignName, Long shareholderId, ShareholderDto shareholderDto);

  void deleteShareholder(String campaignName, Long shareholderId);

  void uploadPicture(String campaignName, Long shareholderId, MultipartFile picture);

  FileDto downloadPicture(String campaignName, Long shareholderId);

  void deletePicture(String campaignName, Long shareholderId);
}
