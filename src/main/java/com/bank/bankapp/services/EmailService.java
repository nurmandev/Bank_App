package com.bank.bankapp.services;

import com.bank.bankapp.dto.EmailDetails;

public interface EmailService {
    void sendEmailAlert(EmailDetails emailDetails);
}
