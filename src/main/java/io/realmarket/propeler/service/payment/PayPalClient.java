package io.realmarket.propeler.service.payment;

import com.braintreepayments.http.HttpResponse;
import com.ibm.cloud.objectstorage.services.kms.model.NotFoundException;
import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import com.paypal.orders.Order;
import com.paypal.orders.OrdersCaptureRequest;
import com.paypal.orders.OrdersGetRequest;
import io.realmarket.propeler.service.exception.PayPalOrderAlreadyCapturedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import static io.realmarket.propeler.service.exception.util.ExceptionMessages.PAY_PAL_ORDER_ALREADY_CAPTURED;
import static io.realmarket.propeler.service.exception.util.ExceptionMessages.PAY_PAL_ORDER_DOES_NOT_EXIST;

@Slf4j
@Service
public class PayPalClient {

  @Value("${payPal.client-id}")
  private String clientId;

  @Value("${payPal.client-secret}")
  private String clientSecret;

  private PayPalHttpClient client;

  public PayPalHttpClient client() {
    return this.client;
  }

  @EventListener(ApplicationReadyEvent.class)
  public void createContext() {
    // change to live for production
    PayPalEnvironment environment = new PayPalEnvironment.Sandbox(clientId, clientSecret);
    client = new PayPalHttpClient(environment);
  }

  public Order getOrder(String orderId) {
    OrdersGetRequest request = new OrdersGetRequest(orderId);
    HttpResponse<Order> response;
    try {
      response = client().execute(request);
    } catch (Exception e) {
      log.info("No order for orderId: {}", orderId);
      throw new NotFoundException(PAY_PAL_ORDER_DOES_NOT_EXIST);
    }
    return response.result();
  }

  public void captureRequest(String orderId) {
    OrdersCaptureRequest request = new OrdersCaptureRequest(orderId);
    try {
      client().execute(request);
    } catch (Exception e) {
      log.info("Order with id: {} already captured.", orderId);
      throw new PayPalOrderAlreadyCapturedException(PAY_PAL_ORDER_ALREADY_CAPTURED);
    }
  }
}
