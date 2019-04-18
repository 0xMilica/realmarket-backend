package io.realmarket.propeler.api.controller.impl;

import io.realmarket.propeler.api.controller.CampaignTopicController;
import io.realmarket.propeler.api.dto.CampaignTopicDto;
import io.realmarket.propeler.api.dto.FilenameDto;
import io.realmarket.propeler.service.CampaignTopicImageService;
import io.realmarket.propeler.service.CampaignTopicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/campaigns/{campaignName}/topics/{topicType}")
@Slf4j
public class CampaignTopicControllerImpl implements CampaignTopicController {

  private final CampaignTopicService campaignTopicService;
  private final CampaignTopicImageService campaignTopicImageService;

  @Autowired
  public CampaignTopicControllerImpl(
      CampaignTopicService campaignTopicService,
      CampaignTopicImageService campaignTopicImageService) {
    this.campaignTopicService = campaignTopicService;
    this.campaignTopicImageService = campaignTopicImageService;
  }

  @PostMapping
  @PreAuthorize("hasAuthority('ROLE_ENTREPRENEUR')")
  public ResponseEntity<String> createCampaignTopic(
      @PathVariable String campaignName,
      @PathVariable String topicType,
      @Valid @RequestBody CampaignTopicDto campaignTopicDto) {
    campaignTopicService.createCampaignTopic(campaignName, topicType, campaignTopicDto);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @GetMapping
  @PreAuthorize("hasAuthority('ROLE_ENTREPRENEUR')")
  public ResponseEntity<CampaignTopicDto> getCampaignTopic(
      @PathVariable String campaignName, @PathVariable String topicType) {
    return ResponseEntity.ok(campaignTopicService.getCampaignTopic(campaignName, topicType));
  }

  @PostMapping(value = "/images")
  @PreAuthorize("hasAuthority('ROLE_ENTREPRENEUR')")
  public ResponseEntity<FilenameDto> uploadCampaignTopicImage(
      @PathVariable String campaignName,
      @PathVariable String topicType,
      @RequestParam("picture") MultipartFile picture,
      HttpServletRequest request) {

    return new ResponseEntity<>(
        campaignTopicImageService.uploadImage(request, campaignName, topicType, picture),
        HttpStatus.CREATED);
  }

  @PutMapping
  @PreAuthorize("hasAuthority('ROLE_ENTREPRENEUR')")
  public ResponseEntity updateCampaignTopic(
      @PathVariable String campaignName,
      @PathVariable String topicType,
      @Valid @RequestBody CampaignTopicDto campaignTopicDto) {
    campaignTopicService.updateCampaignTopic(campaignName, topicType, campaignTopicDto);
    return ResponseEntity.ok().build();
  }
}
