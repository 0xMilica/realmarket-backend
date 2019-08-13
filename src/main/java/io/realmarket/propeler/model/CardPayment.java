package io.realmarket.propeler.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Data
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("CardPayment")
@Entity(name = "CardPayment")
public class CardPayment extends Payment {

  private String sessionToken;
}
