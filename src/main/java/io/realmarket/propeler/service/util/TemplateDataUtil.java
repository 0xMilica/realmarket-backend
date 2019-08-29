package io.realmarket.propeler.service.util;

import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.Company;
import io.realmarket.propeler.model.Investment;
import io.realmarket.propeler.model.Person;
import io.realmarket.propeler.service.CampaignService;
import io.realmarket.propeler.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class TemplateDataUtil {

  public static final String NAME = "name";
  public static final String FIRST_NAME = "firstName";
  public static final String LAST_NAME = "lastName";
  public static final String SELLER = "seller";
  public static final String BUYER = "buyer";
  public static final String BUYER_NAME = "buyerName";
  public static final String BUYER_ADDRESS = "buyerAddress";
  public static final String BUYER_CITY = "buyerCity";
  public static final String BUYER_COUNTRY = "buyerCountry";
  public static final String PRO_FORMA_ID = "proFormaId";
  public static final String CURRENCY = "currency";
  public static final String ISSUE_DATE = "issueDate";
  public static final String DUE_DATE = "dueDate";
  public static final String PAYMENT_METHOD = "paymentMethod";
  public static final String POST_SCRIPTUM = "ps";
  public static final String ITEM_QUANTITY = "itemQuantity";
  public static final String ITEM_NET_PRICE = "itemNetPrice";
  public static final String ITEM_VAT = "itemVAT";
  public static final String ITEM_TOTAL = "itemTotal";
  public static final String FULL_TOTAL = "fullTotal";
  public static final String IS_PROFORMA = "isProforma";
  public static final String ADDRESS = "address";
  public static final String POSTAL_NO = "postalNo";
  public static final String CITY = "city";
  public static final String VAT_NO = "VATNo";
  public static final String BANK_NAME = "bankName";
  public static final String BANK_ACCOUNT_NO = "bankAccountNo";
  public static final String IBAN = "IBAN";
  public static final String SWIFT_CODE = "SWIFTCode";
  public static final String COMPANY = "company";
  public static final String IS_COMPANY = "isCompany";
  public static final String COUNTRY = "country";
  public static final String SHORT_NAME_DESCRIPTION = "shortNameDesc";
  public static final String LONG_NAME_DESCRIPTION = "longNameDesc";
  public static final String TERMS_OF_USE = "Terms Of Use";
  public static final String NOTES_AND_DISCLAIMERS = "Notes & Disclaimers";

  private static final String DUMMY_TEXT =
      "In publishing and graphic design, lorem ipsum is a placeholder text commonly used to "
          + "demonstrate the visual form of a document without relying on meaningful content. Replacing the actual "
          + "content with placeholder text allows designers to design the form of the content before the content "
          + "itself has been produced.";
  private static final String DUMMY_POSTAL_NO =
      "21460"; // TODO Postal number hardcoded. Remove when real value is introduced!

  private final CompanyService companyService;
  private final CampaignService campaignService;

  private DateTimeFormatter formatter;

  @Value("${app.locale.timezone}")
  private String localeTimezone;

  @Value("${app.bank.account-number}")
  private String accountNumber;

  @Value("${app.bank.name}")
  private String bankName;

  @Value("${app.bank.SWIFT}")
  private String swift;

  @Value("${app.bank.IBAN}")
  private String ibanCode;

  @Value("${app.payment.VAT}")
  private BigDecimal vatPercent;

  @Value("${app.investment.weekInMillis}")
  private Long invoiceDueDuration;

  @Value("${app.locale.currency.code}")
  private String currencyCode;

  @Autowired
  public TemplateDataUtil(
      @Value("${app.locale.language}") String localeLanguage,
      @Value("${app.locale.country}") String localeCountry,
      @Value("${app.locale.timezone}") String localeTimezone,
      CompanyService companyService,
      CampaignService campaignService) {
    this.companyService = companyService;
    this.campaignService = campaignService;
    this.formatter =
        DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
            .withLocale(Locale.forLanguageTag(localeLanguage + "_" + localeCountry))
            .withZone(ZoneId.of(localeTimezone));
  }

  public Map<String, Object> getData(
      Investment investment, String paymentMethod, boolean isProforma) {
    Person seller = investment.getCampaign().getCompany().getAuth().getPerson();
    Person buyer = investment.getPerson();
    LocalDateTime date;
    if (isProforma) {
      date = LocalDateTime.ofInstant(investment.getCreationDate(), ZoneId.of(localeTimezone));
    } else {
      date = LocalDateTime.ofInstant(investment.getPaymentDate(), ZoneId.of(localeTimezone));
    }
    String proformaInvoiceId =
        investment.getCurrency() + "/" + date.getYear() + "-" + investment.getId();
    BigDecimal quantity =
        campaignService.convertMoneyToPercentageOfEquity(
            investment.getCampaign().getUrlFriendlyName(), investment.getInvestedAmount());
    BigDecimal netPrice = investment.getInvestedAmount();
    BigDecimal vat = this.vatPercent;
    BigDecimal total = investment.getInvestedAmount();
    HashMap<String, Object> data = new HashMap<>();
    data.put(SELLER, getData(seller));
    data.put(BUYER, getData(buyer));
    data.put(PRO_FORMA_ID, proformaInvoiceId);
    data.put(CURRENCY, investment.getCurrency().trim());
    Instant creationInstant = Instant.now(); // TODO Get intended value when implemented
    data.put(ISSUE_DATE, getDateFromInstant(creationInstant));
    data.put(DUE_DATE, getDateFromInstant(creationInstant.plusMillis(invoiceDueDuration)));
    data.put(PAYMENT_METHOD, paymentMethod);

    data.put(ITEM_QUANTITY, quantity.setScale(2, RoundingMode.HALF_UP));
    data.put(ITEM_NET_PRICE, netPrice.setScale(2, RoundingMode.HALF_UP));
    data.put(ITEM_VAT, vat.multiply(netPrice).setScale(2, RoundingMode.HALF_UP));
    data.put(ITEM_TOTAL, total.add(vat.multiply(netPrice)).setScale(2, RoundingMode.HALF_UP));
    data.put(FULL_TOTAL, total.add(vat.multiply(netPrice)).setScale(2, RoundingMode.HALF_UP));

    data.put(POST_SCRIPTUM, preparePS());

    data.put(IS_PROFORMA, isProforma);

    return data;
  }

  public Map<String, Object> getData(Person person) {
    HashMap<String, Object> data = new HashMap<>();
    data.put(FIRST_NAME, person.getFirstName());
    data.put(LAST_NAME, person.getLastName());
    Auth auth = person.getAuth();
    switch (auth.getUserRole().getName()) {
      case ROLE_ENTREPRENEUR:
        Company company = companyService.findByAuthOrThrowException(auth);
        data.put(NAME, company.getName());
        data.put(ADDRESS, company.getAddress());
        data.put(
            POSTAL_NO,
            DUMMY_POSTAL_NO); // TODO Postal number hardcoded. Change to real value when added to
        // the model!
        data.put(CITY, company.getCity());
        data.put(VAT_NO, company.getTaxIdentifier());
        data.put(BANK_NAME, this.bankName);
        data.put(BANK_ACCOUNT_NO, this.accountNumber);
        data.put(IBAN, this.ibanCode);
        data.put(SWIFT_CODE, this.swift);
        data.put(COMPANY, getData(company));
        break;
      case ROLE_CORPORATE_INVESTOR:
        data.put(NAME, person.getCompanyName());
        data.put(IS_COMPANY, true);
        data.put(ADDRESS, person.getAddress());
        data.put(
            POSTAL_NO,
            DUMMY_POSTAL_NO); // TODO Postal number hardcoded. Change to real value when added to
        // the model!
        data.put(CITY, person.getCity());
        data.put(VAT_NO, "1234123412341234"); // TODO Fill with actual data when available
        break;
      case ROLE_INDIVIDUAL_INVESTOR:
        data.put(NAME, person.getFirstName() + " " + person.getLastName());
        data.put(IS_COMPANY, false);
        data.put(ADDRESS, person.getAddress());
        data.put(
            POSTAL_NO,
            DUMMY_POSTAL_NO); // TODO Postal number hardcoded. Change to real value when added to
        // the model!
        data.put(CITY, person.getCity());
        break;
      default:
        break;
    }
    if (person.getCountryForTaxation() == null) {
      data.put(COUNTRY, person.getCountryOfResidence().getName());
    } else {
      data.put(COUNTRY, person.getCountryForTaxation().getName());
    }
    return data;
  }

  public Map<String, Object> getData(Company company) {
    HashMap<String, Object> data = new HashMap<>();
    data.put(NAME, company.getName());
    return data;
  }

  public Map<String, Object> getOffPlatformInvoiceData(Investment investment, boolean isProforma) {
    Person seller = investment.getCampaign().getCompany().getAuth().getPerson();
    LocalDateTime date;
    Person buyer = investment.getPerson();
    if (isProforma) {
      date = LocalDateTime.ofInstant(investment.getCreationDate(), ZoneId.of(localeTimezone));
    } else {
      date = LocalDateTime.ofInstant(investment.getPaymentDate(), ZoneId.of(localeTimezone));
    }
    String proformaInvoiceId =
        investment.getCurrency() + "/" + date.getYear() + "-" + investment.getId();
    BigDecimal quantity =
        campaignService.convertMoneyToPercentageOfEquity(
            investment.getCampaign().getUrlFriendlyName(), investment.getInvestedAmount());
    BigDecimal netPrice = investment.getInvestedAmount();
    BigDecimal vat = this.vatPercent;
    BigDecimal total = investment.getInvestedAmount();
    HashMap<String, Object> data = new HashMap<>();
    data.put(SELLER, getData(seller));
    if (buyer.getCompanyName() == null) {
      data.put(BUYER_NAME, buyer.getFirstName() + " " + buyer.getLastName());
    } else {
      data.put(BUYER_NAME, buyer.getCompanyName());
    }
    data.put(BUYER_ADDRESS, buyer.getAddress());
    data.put(BUYER_CITY, buyer.getCity());
    data.put(BUYER_COUNTRY, buyer.getCountryOfResidence().getName());
    data.put(PRO_FORMA_ID, proformaInvoiceId);
    data.put(CURRENCY, investment.getCurrency().trim());
    Instant creationInstant = Instant.now();
    data.put(ISSUE_DATE, getDateFromInstant(creationInstant));
    data.put(DUE_DATE, getDateFromInstant(creationInstant.plusMillis(invoiceDueDuration)));
    data.put(PAYMENT_METHOD, "Bank transfer");

    data.put(ITEM_QUANTITY, quantity.setScale(2, RoundingMode.HALF_UP));
    data.put(ITEM_NET_PRICE, netPrice.setScale(2, RoundingMode.HALF_UP));
    data.put(ITEM_VAT, vat.multiply(netPrice).setScale(2, RoundingMode.HALF_UP));
    data.put(ITEM_TOTAL, total.add(vat.multiply(netPrice)).setScale(2, RoundingMode.HALF_UP));
    data.put(FULL_TOTAL, total.add(vat.multiply(netPrice)).setScale(2, RoundingMode.HALF_UP));

    data.put(POST_SCRIPTUM, preparePS());

    data.put(IS_PROFORMA, isProforma);

    return data;
  }

  public List<Map<String, Object>> getData(List<Pair<String, String>> ps) {
    return ps.stream()
        .map(
            p -> {
              HashMap<String, Object> onePS = new HashMap<>();
              onePS.put(SHORT_NAME_DESCRIPTION, p.getFirst());
              onePS.put(LONG_NAME_DESCRIPTION, p.getSecond());
              return onePS;
            })
        .collect(Collectors.toList());
  }

  private List<Map<String, Object>> preparePS() {
    ArrayList<Pair<String, String>> ps = new ArrayList<>();
    ps.add(Pair.of(TERMS_OF_USE, DUMMY_TEXT));
    ps.add(Pair.of(NOTES_AND_DISCLAIMERS, DUMMY_TEXT));
    return getData(ps);
  }

  private String getDateFromInstant(Instant i) {
    return this.formatter.format(LocalDate.from(i.atZone(ZoneId.of(this.localeTimezone))));
  }
}
