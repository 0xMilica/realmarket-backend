package io.realmarket.propeler.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Data
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("BankTransferPayment")
@Entity(name = "BankTransferPayment")
public class BankTransferPayment extends Payment {

  private String accountNumber;

  private String routingNumber;

  private String proformaInvoiceUrl;
}
