package mk.ukim.finki.wp.blossomhouse.service.implementation;

import mk.ukim.finki.wp.blossomhouse.model.CardPayment;
import mk.ukim.finki.wp.blossomhouse.model.exceptions.InvalidArgumentsException;
import mk.ukim.finki.wp.blossomhouse.repository.CardPaymentRepository;
import mk.ukim.finki.wp.blossomhouse.service.CardPaymentService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CardPaymentServiceImpl implements CardPaymentService {

    private final CardPaymentRepository cardPaymentRepository;

    public CardPaymentServiceImpl(CardPaymentRepository cardPaymentRepository) {
        this.cardPaymentRepository = cardPaymentRepository;
    }


    @Override
    public Optional<CardPayment> save(String cardOwner, Long cardNumber, String expirationDate, String cvcCode)
    {
        if ( cardOwner!=null && !cardOwner.isEmpty() && cardNumber!=null  && expirationDate!=null && !expirationDate.isEmpty() && cvcCode!=null && !cvcCode.isEmpty())
        {
            CardPayment cardPayment = new CardPayment(cardOwner, cardNumber, expirationDate, cvcCode);
            return Optional.of(this.cardPaymentRepository.save(cardPayment));
        }
        else
        {
            throw new InvalidArgumentsException();
        }


    }
}
