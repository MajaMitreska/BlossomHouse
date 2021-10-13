package mk.ukim.finki.wp.blossomhouse.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class CardPayment
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String cardOwner;

    public Long cardNumber;

    public String expirationDate;

    public String cvcCode;

    public CardPayment(String cardOwner, Long cardNumber, String expirationDate, String cvcCode) {
        this.cardOwner = cardOwner;
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.cvcCode = cvcCode;
    }

    public CardPayment() {

    }
}
