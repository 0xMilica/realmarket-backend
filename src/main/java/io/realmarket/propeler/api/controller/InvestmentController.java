package io.realmarket.propeler.api.controller;

import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;

@Api(value = "/investments")
public interface InvestmentController {
    @ApiOperation(
            value = "Revoke investment",
            httpMethod = "DELETE")
    @ApiImplicitParam(
            name = "investID",
            value = "Campaign's investments id",
            required = true,
            dataType = "Long")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Campaign investment successfully revoked."),
            @ApiResponse(code = 400, message = "Insufficient privileges."),
            @ApiResponse(code = 404, message = "Campaign investment does not exists.")
    })
    ResponseEntity<Void> revokeInvestment(Long investID);
}
