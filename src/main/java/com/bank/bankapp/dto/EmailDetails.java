package com.bank.bankapp.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailDetails {
	private String recipient;
    private String messageBody;
    private String subject;
    private String attachment;

}
