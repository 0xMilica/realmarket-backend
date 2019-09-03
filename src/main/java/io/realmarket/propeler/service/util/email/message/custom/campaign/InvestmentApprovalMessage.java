package io.realmarket.propeler.service.util.email.message.custom.campaign;

import io.realmarket.propeler.service.util.email.message.AbstractEmailMessage;
import lombok.Data;

import java.util.List;
import java.util.Map;

import static io.realmarket.propeler.service.util.email.Parameters.*;

@Data
public class InvestmentApprovalMessage extends AbstractEmailMessage {

  private String frontendServiceUrlPath;

  public InvestmentApprovalMessage(
      List<String> addressList,
      Map<String, Object> contentMap,
      String subject,
      String templateName,
      String frontendServiceUrlPath) {
    super(addressList, contentMap, subject, templateName);
    this.frontendServiceUrlPath = frontendServiceUrlPath;
  }

  @Override
  public Map<String, Object> getData() {
    String investmentId = Long.toString((Long) contentMap.get(INVESTMENT_ID));
    String paymentLink =
        String.format("%s/investor/payment/%s", frontendServiceUrlPath, investmentId);
    Map<String, Object> data = getBasicEmailData();
    data.putAll(contentMap);
    data.put(CHECK_CIRCLE, CHECK_CIRCLE);
    data.put(PAYMENT_LINK, paymentLink);

    return data;
  }
}
