package mk.ukim.finki.wp.blossomhouse.service;

import mk.ukim.finki.wp.blossomhouse.model.CardPayment;

import java.util.Optional;

public interface CardPaymentService {

    Optional<CardPayment> save (String cardOwner, Long cardNumber, String expirationDate, String cvcCode);
}
