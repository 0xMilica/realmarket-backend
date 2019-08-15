package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.FileDto;
import io.realmarket.propeler.api.dto.ShareholderRequestDto;
import io.realmarket.propeler.model.Shareholder;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ShareholderService {

  Shareholder createShareholder(ShareholderRequestDto shareholderRequestDto);

  List<Shareholder> patchShareholderOrder(List<Long> order);

  List<Shareholder> getShareholders();

  List<Shareholder> getShareholders(Long companyId);

  Shareholder patchShareholder(Long shareholderId, ShareholderRequestDto shareholderRequestDto);

  void deleteShareholder(Long shareholderId);

  void uploadPicture(Long shareholderId, MultipartFile picture);

  FileDto downloadPicture(Long shareholderId);

  FileDto downloadPublicPicture(Long companyId, Long shareholderId);

  void deletePicture(Long shareholderId);
}
