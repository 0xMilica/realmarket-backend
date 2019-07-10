package io.realmarket.propeler.api.controller;

import io.realmarket.propeler.api.dto.FundraisingProposalDto;
import io.realmarket.propeler.api.dto.FundraisingProposalResponseDto;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Api(value = "/fundraisingProposals")
public interface FundraisingProposalController {

  @ApiOperation(
      value = "Propose fundraising campaign idea.",
      httpMethod = "POST",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "fundraisingProposalDto",
        value = "FundraisingProposal's ID",
        required = true,
        dataType = "FundraisingProposalDto",
        paramType = "body",
        example =
            "{\n"
                + "    \"firstName\": \"Мићо\",\n"
                + "    \"lastName\": \"Јовић\",\n"
                + "    \"companyName\": \"Параћинка Ваљево\",\n"
                + "    \"website\": \"https://zutisokodsljive.rs\",\n"
                + "    \"email\": \"backimornar@zutisokodsljive.rs\",\n"
                + "    \"phoneNumber\": \"+381212233369\",\n"
                + "    \"previouslyRaised\": \"-25.00\",\n"
                + "    \"fundingGoals\":\"1000000\"\n"
                + "  }")
  })
  @ApiResponses({
    @ApiResponse(
        code = 200,
        message = "Fundraising proposal successfully made.",
        examples =
            @Example(
                @ExampleProperty(
                    mediaType = APPLICATION_JSON_VALUE,
                    value =
                        "{\n"
                            + "    \"id\": \"1\",\n"
                            + "    \"firstName\": \"Мићо\",\n"
                            + "    \"lastName\": \"Јовић\",\n"
                            + "    \"companyName\": \"Параћинка Ваљево\",\n"
                            + "    \"website\": \"https://zutisokodsljive.rs\",\n"
                            + "    \"email\": \"backimornar@zutisokodsljive.rs\",\n"
                            + "    \"phoneNumber\": \"+381212233369\",\n"
                            + "    \"previouslyRaised\": \"-25.00\",\n"
                            + "    \"fundingGoals\":\"1000000\"\n"
                            + "    \"proposalState\":\"PENDING\"\n"
                            + "  }")))
  })
  ResponseEntity<FundraisingProposalResponseDto> makeFundraisingProposal(
      FundraisingProposalDto fundraisingProposalDto);
}
