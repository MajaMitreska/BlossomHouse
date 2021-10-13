package mk.ukim.finki.wp.blossomhouse.service;

public interface EmailService {
    void sendSimpleMessage(String to, String subject, String text);
}
