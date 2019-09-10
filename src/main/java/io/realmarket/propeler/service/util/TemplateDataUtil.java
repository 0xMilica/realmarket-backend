package io.realmarket.propeler.service.util;

import io.realmarket.propeler.api.dto.ContractRequestDto;
import io.realmarket.propeler.model.*;
import io.realmarket.propeler.service.CampaignService;
import io.realmarket.propeler.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
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
  public static final String INVOICE_ID = "invoiceId";
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
  public static final String COMPANY_NAME = "companyName";
  public static final String IS_COMPANY = "isCompany";
  public static final String COUNTRY = "country";
  public static final String SHORT_NAME_DESCRIPTION = "shortNameDesc";
  public static final String LONG_NAME_DESCRIPTION = "longNameDesc";
  public static final String TERMS_OF_USE = "Terms Of Use";
  public static final String NOTES_AND_DISCLAIMERS = "Notes & Disclaimers";
  public static final String BANK_TRANSFER_PAYMENT_TYPE = "Bank transfer";

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

  public Map<String, Object> getInvoiceData(Investment investment, String paymentMethod) {
    Map<String, Object> data = getGeneralInvoiceData(investment, paymentMethod);

    data.put(INVOICE_ID, generateInvoiceId(investment, investment.getPaymentDate()));

    return data;
  }

  public Map<String, Object> getProformaInvoiceData(Investment investment) {
    Map<String, Object> data = getGeneralInvoiceData(investment, BANK_TRANSFER_PAYMENT_TYPE);

    data.put(IS_PROFORMA, true);
    data.put(INVOICE_ID, generateInvoiceId(investment, investment.getCreationDate()));

    return data;
  }

  public Map<String, Object> getContractData(ContractRequestDto contractRequestDto) {
    HashMap<String, Object> data = new HashMap<>();

    // TODO upon adding a first contract type, update this method. Also, for each type of contract,
    // new method should be written
    Person entrepreneur =
        Person.builder()
            .firstName("Dummy")
            .lastName("Dummic")
            .companyName("Dummylax")
            .address("DumDum Avenue 32")
            .countryOfResidence(Country.builder().name("Dummystan").code("DMB").build())
            .city("Dummity")
            .build();
    Map<String, Object> entrepreneurData = getData(entrepreneur);
    data.put("entrepreneur", entrepreneurData);

    Person platform =
        Person.builder()
            .companyName("Realmarket")
            .address("Modena 1")
            .city("Novi Sad")
            .countryOfResidence(Country.builder().name("Serbia").code("SRB").build())
            .build();
    Map<String, Object> platformData = getData(platform);
    platformData.put(VAT_NO, "1234123412341234");
    platformData.put(BANK_NAME, this.bankName);
    platformData.put(BANK_ACCOUNT_NO, this.accountNumber);
    platformData.put(IBAN, this.ibanCode);
    platformData.put(SWIFT_CODE, this.swift);
    data.put("platform", platformData);

    data.put(POST_SCRIPTUM, preparePS());

    return data;
  }

  public Map<String, Object> getData(Person person) {
    HashMap<String, Object> data = new HashMap<>();
    data.put(FIRST_NAME, person.getFirstName());
    data.put(LAST_NAME, person.getLastName());
    Auth auth = person.getAuth();
    if (auth == null) {
      if (person.getCompanyName() == null) {
        data.put(NAME, person.getFirstName() + " " + person.getLastName());
      } else {
        data.put(NAME, person.getCompanyName());
        data.put(COMPANY_NAME, person.getCompanyName());
      }
      data.put(ADDRESS, person.getAddress());
      data.put(
          POSTAL_NO,
          DUMMY_POSTAL_NO); // TODO Postal number hardcoded. Change to real value when added to the
      // model!
      if (person.getCompanyIdentificationNumber() == null) {
        data.put(IS_COMPANY, false);
      } else {
        data.put(IS_COMPANY, true);
        data.put(COMPANY_NAME, person.getCompanyName());
        data.put(VAT_NO, "1234123412341234"); // TODO Fill with actual data when available
      }
      data.put(CITY, person.getCity());
    } else {
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
          data.put(COMPANY_NAME, company.getName());
          break;
        case ROLE_CORPORATE_INVESTOR:
          data.put(NAME, person.getCompanyName());
          data.put(COMPANY_NAME, person.getCompanyName());
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

  private Map<String, Object> getGeneralInvoiceData(Investment investment, String paymentMethod) {
    Map<String, Object> data = new HashMap<>();

    data.put(PAYMENT_METHOD, paymentMethod);

    Person seller = investment.getCampaign().getCompany().getAuth().getPerson();
    Person buyer = investment.getPerson();
    data.put(SELLER, getData(seller));
    data.put(BUYER, getData(buyer));

    data.put(CURRENCY, investment.getCurrency().trim());
    Instant creationInstant = Instant.now(); // TODO Get intended value when implemented
    data.put(ISSUE_DATE, getDateFromInstant(creationInstant));
    data.put(DUE_DATE, getDateFromInstant(creationInstant.plusMillis(invoiceDueDuration)));
    data.putAll(
        getPricingData(
            investment.getInvestedAmount(), investment.getCampaign().getUrlFriendlyName()));

    data.put(POST_SCRIPTUM, preparePS());

    return data;
  }

  // It is presumed that investment's invested amount already includes VAT. Prices are calculated with that in mind.
  private Map<String, Object> getPricingData(BigDecimal investedAmount, String campaignName) {
    HashMap<String, Object> data = new HashMap<>();

    BigDecimal vat = this.vatPercent;
    BigDecimal quantity =
        campaignService.convertMoneyToPercentageOfEquity(campaignName, investedAmount);
    BigDecimal netPrice =
        investedAmount
            .multiply(BigDecimal.ONE)
            .divide(BigDecimal.ONE.add(this.vatPercent), MathContext.DECIMAL128);

    data.put(ITEM_QUANTITY, quantity.setScale(2, RoundingMode.HALF_UP));
    data.put(ITEM_NET_PRICE, netPrice.setScale(2, RoundingMode.HALF_UP));
    data.put(ITEM_VAT, vat.multiply(netPrice).setScale(2, RoundingMode.HALF_UP));
    data.put(ITEM_TOTAL, investedAmount.setScale(2, RoundingMode.HALF_UP));
    data.put(FULL_TOTAL, investedAmount.setScale(2, RoundingMode.HALF_UP));

    return data;
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

  private String generateInvoiceId(Investment investment, Instant i) {
    LocalDateTime date = LocalDateTime.ofInstant(i, ZoneId.of(localeTimezone));

    return investment.getCurrency() + "/" + date.getYear() + "-" + investment.getId();
  }
}
