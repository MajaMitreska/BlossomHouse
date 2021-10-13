package mk.ukim.finki.wp.blossomhouse.web.contoller;

import mk.ukim.finki.wp.blossomhouse.model.BlossomUser;
import mk.ukim.finki.wp.blossomhouse.model.exceptions.InvalidArgumentsException;
import mk.ukim.finki.wp.blossomhouse.service.CardPaymentService;
import mk.ukim.finki.wp.blossomhouse.service.EmailService;
import mk.ukim.finki.wp.blossomhouse.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
@RequestMapping("/payment-information")
public class PaymentInformationController {

    private final CardPaymentService cardPaymentService;
    private final EmailService emailService;
    private final UserService userService;

    public PaymentInformationController(CardPaymentService cardPaymentService, EmailService emailService, UserService userService) {
        this.cardPaymentService = cardPaymentService;
        this.emailService = emailService;
        this.userService = userService;
    }

    @GetMapping
    public String getPaymentInformationPage(Model model, HttpServletRequest request, @RequestParam (required = false) String error) {
        String username = request.getRemoteUser();
        BlossomUser user = new BlossomUser();
        if (username != null && username.length() > 50) {
            user = this.userService.saveGoogleUser(username);
        } else {
            user = this.userService.findByUsername(username);
        }
        if(error!=null && !error.isEmpty())
        {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }

        model.addAttribute("remoteUser", user.getUsername());
        model.addAttribute("bodyContent", "payment-information");

        return "master-template";
    }

    @PostMapping
    public String savePaymentInformation(@RequestParam String cardOwner ,
                                         @RequestParam Long cardNumber,
                                         @RequestParam String expDate,
                                         @RequestParam String cvc,
                                         HttpServletRequest request,
                                         Model model) {

        String username = request.getRemoteUser();
        BlossomUser user = new BlossomUser();
        if (username.length() > 50) {
            user = this.userService.findGoogleUserByUsername(username);
        } else {
            user = this.userService.findByUsername(username);
        }

        try {
            this.cardPaymentService.save(cardOwner, cardNumber, expDate, cvc);
            String personalizedMessage =
                    "\t Your order was successful!\n\n" +
                            "\t Dear " + user.getName() + " " + user.getSurname() + ", \n\n"
                            + "\t We have good news regarding Your recent Blossom House order! \n" +
                            "\t Your order was successful and our team has already started with " +
                            "the preparation process, so expect it at Your door very soon! \n\n"
                            + "\t Thank You for Your trust.\n"
                            + "\t If You have any further questions, please let us know! \n\n" +
                            "\t Sincerely, \n\t Blossom House team.";


            this.emailService.sendSimpleMessage(user.getEmail(),
                    "Successful order from Blossom House!",
                    personalizedMessage);
            return "redirect:/successful-order";
        }
        catch (InvalidArgumentsException exception)
        {
            return "redirect:/payment-information?error=" + exception.getMessage();
        }
    }
}
