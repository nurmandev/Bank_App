package com.bank.bankapp.services;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.bank.bankapp.dto.EmailDetails;
import com.bank.bankapp.entity.Transaction;
import com.bank.bankapp.entity.User;
import com.bank.bankapp.repository.TransactionRepository;
import com.bank.bankapp.repository.UserRepository;
import com.itextpdf.awt.geom.Rectangle;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class BankStatement {
    private TransactionRepository transactionRepository;
    private UserRepository userRepository;
    private EmailService emailService;
    private static final String FILE = "C:\\Users\\HI-TECH\\Downloads\\MyStatement.pdf" ;
   
    /**
     * retrive list of Transaction within a date range given an account number
     * generate a pdf file of transactions
     * send the file via email
     * @throws DocumentException 
     */
    
     public List<Transaction>generateStatement(String accountNumber, String startDate, String endDate) 
     throws FileNotFoundException, DocumentException{


      LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
      LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);

      System.out.println("First log..............");
System.out.println(start);
System.out.println(end);

      List<Transaction> transactionList = transactionRepository.findAll().stream()
            .filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
            .filter(transaction -> {
                LocalDate createdAt = transaction.getCreatedAt();
                return createdAt != null && !createdAt.isBefore(start) && !createdAt.isAfter(end);
            })
            .collect(Collectors.toList());

      System.out.println("Second log..............");
      System.out.println(start);
      System.out.println(end);
      System.out.println(transactionList.toString());
    


        User user = userRepository.findByAccountNumber(accountNumber);
        String customerName = user.getFirstName() + " " + user.getLastName();
        // Rectangle statementSize = new Rectangle(PageSize.A4);
        // Document document = new Document(statementSize);
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        log.info("Setting size of Document");
        OutputStream outputStream = new FileOutputStream(FILE);
        PdfWriter.getInstance(document, outputStream);
        document.open();

        PdfPTable bankInfoTable = new PdfPTable(1);
        PdfPCell bankName = new PdfPCell(new Phrase("Nurman Bank"));
        bankName.setBorder(0);
        bankName.setBackgroundColor(BaseColor.PINK);
        bankName.setPadding(20f);

        PdfPCell bankAddress = new PdfPCell(new Phrase("247, Alimi Road Kwara Nigeria"));
        bankAddress.setBorder(0);
        bankInfoTable.addCell(bankName);
        bankInfoTable.addCell(bankAddress);

        PdfPTable statementInfo = new PdfPTable(2);
        PdfPCell customerInfo = new PdfPCell(new Phrase("Start Date: " + startDate));
        customerInfo.setBorder(0);
        PdfPCell statement = new PdfPCell(new Phrase("STATEMENT OF ACCOUNT"));
        statement.setBorder(0);
        PdfPCell stopDate = new PdfPCell(new Phrase("End Date: " + endDate));
        stopDate.setBorder(0);
        PdfPCell name = new PdfPCell(new Phrase("Customer Name: "+ customerName));
        name.setBorder(0);
        PdfPCell space = new PdfPCell();
        PdfPCell address = new PdfPCell(new Phrase("Customer Address: " + user.getAddress()));
        address.setBorder(0);


        PdfPTable transactionTable = new PdfPTable(4);
        PdfPCell date = new PdfPCell(new Phrase("Date"));
        date.setBorder(0);
        PdfPCell transactionType = new PdfPCell(new Phrase("TRANSACTION TYPE"));
        transactionType.setBackgroundColor(BaseColor.BLACK);
        transactionType.setBorder(0);
        PdfPCell transactionAmount = new PdfPCell(new Phrase("TRANSACTION AMOUNT"));
        transactionAmount.setBackgroundColor(BaseColor.GREEN);
        transactionAmount.setBorder(0);
        PdfPCell status = new PdfPCell(new Phrase("STATUS"));
        status.setBackgroundColor(BaseColor.GRAY);
        status.setBorder(0);

      transactionTable.addCell(date);
      transactionTable.addCell(transactionType);
      transactionTable.addCell(transactionAmount);
      transactionTable.addCell(status);

      transactionList.forEach(transaction -> {
          transactionTable.addCell(new Phrase(transaction.getCreatedAt().toString()));
          transactionTable.addCell(new Phrase(transaction.getTransactionType()));
          transactionTable.addCell(new Phrase(transaction.getAmount().toString()));
          transactionTable.addCell(new Phrase(transaction.getStatus()));
        });

      statementInfo.addCell(customerInfo);
      statementInfo.addCell(statement);
      statementInfo.addCell(endDate);
      statementInfo.addCell(name);
      statementInfo.addCell(space);
      statementInfo.addCell(address);

      document.add(bankInfoTable);
      document.add(statement);
      document.add(transactionTable);

      document.close();

      EmailDetails emailDetails = EmailDetails.builder()
        .recipient(user.getEmail())
        .subject("STATEMENT OF ACCOUNT")
        .messageBody("Kindly find your requested account statement attached!")
        .attachment(FILE)
      .build();

      emailService.sendEmailAlert(emailDetails);

      return transactionList;

     }

   
}
