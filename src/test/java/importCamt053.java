import net.tjeerd.camt053parser.Camt053Parser;
import net.tjeerd.camt053parser.model.*;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.stream.Collectors;

public class importCamt053 {
    public static void main(String[] args) {
        Camt053Parser camt053Parser = new Camt053Parser();
        final String CAMT053FILENAME = "camt053file.xml";

        try {
            FileInputStream fileInputStream = new FileInputStream(new File(CAMT053FILENAME));
            Document camt053Document = camt053Parser.parse(fileInputStream);

            System.out.println("Bank statement sequence number: " + camt053Document.getBkToCstmrStmt().getStmt().get(0).getElctrncSeqNb().intValue());
            System.out.println("Bank statement account IBAN: " + camt053Document.getBkToCstmrStmt().getStmt().get(0).getAcct().getId().getIBAN());
            for (int i = 0; i < 80; i++) System.out.print("-");
            System.out.println();

            List<AccountStatement2> accountStatement2List = camt053Document.getBkToCstmrStmt().getStmt();

            // Get all statements (usually one per bank statement)
            for (AccountStatement2 accountStatement2 : accountStatement2List) {

                // Get a list of all payment entries
                for (ReportEntry2 reportEntry2 : accountStatement2.getNtry()) {
                    System.out.println("Credit or debit: " + reportEntry2.getCdtDbtInd());
                    System.out.println("Booking date: " + reportEntry2.getBookgDt().getDt().toGregorianCalendar().getTime());

                    List<EntryDetails1> entryDetails1List = reportEntry2.getNtryDtls();

                    // Get payment details of the entry
                    for (EntryDetails1 entryDetails1 : entryDetails1List) {
                        // This is NOT a batch, but individual payments
                        if (entryDetails1.getBtch() == null) {
                            if (CreditDebitCode.DBIT == reportEntry2.getCdtDbtInd()) {
                                // Outgoing (debit) payments, show recipient (creditors) information, money was transferred from the bank (debtor) to a client (creditor)
                                System.out.println("Creditor name: " + entryDetails1.getTxDtls().get(0).getRltdPties().getCdtr().getNm());
                                System.out.println("Creditor IBAN: " + entryDetails1.getTxDtls().get(0).getRltdPties().getCdtrAcct().getId().getIBAN());
                                System.out.println("Creditor amount: " + entryDetails1.getTxDtls().get(0).getAmtDtls().getTxAmt().getAmt().getValue());
                                System.out.println("Creditor remittance information (payment description): " + entryDetails1.getTxDtls().get(0).getRmtInf().getUstrd().stream().collect(Collectors.joining(",")));
                            }
                            if (CreditDebitCode.CRDT == reportEntry2.getCdtDbtInd()) {
                                // Incoming (credit) payments, show origin (debtor) information, money was transferred from a client (debtor) to the bank (creditor)
                                System.out.println("Debtor name: " + entryDetails1.getTxDtls().get(0).getRltdPties().getDbtr().getNm());
                                System.out.println("Debtor IBAN: " + entryDetails1.getTxDtls().get(0).getRltdPties().getDbtrAcct().getId().getIBAN());
                                System.out.println("Debtor amount: " + entryDetails1.getTxDtls().get(0).getAmtDtls().getTxAmt().getAmt().getValue());
                                System.out.println("Debtor remittance information (payment description): " + entryDetails1.getTxDtls().get(0).getRmtInf().getUstrd().stream().collect(Collectors.joining(",")));
                            }
                        } else {
                            // This is an entry about an outgoing batch payment
                            if (CreditDebitCode.DBIT == reportEntry2.getCdtDbtInd()) {
                                System.out.println("Batch creditor total amount: " + entryDetails1.getBtch().getTtlAmt().getValue());
                                for (EntryTransaction2 entryTransaction2 : entryDetails1.getTxDtls()) {
                                    // Outgoing (debit) payments, show recipient (creditor) information, money was transferred from the bank (debtor) to a client (creditor)
                                    System.out.println("Batch creditor name: " + entryTransaction2.getRltdPties().getCdtr().getNm());
                                    System.out.println("Batch creditor IBAN: " + entryTransaction2.getRltdPties().getCdtrAcct().getId().getIBAN());
                                    System.out.println("Batch creditor amount: " + entryTransaction2.getAmtDtls().getTxAmt().getAmt().getValue());
                                    System.out.println("Batch creditor remittance information: " + entryTransaction2.getRmtInf().getUstrd());
                                }
                            }
                        }

                        for (int i = 0; i < 80; i++) System.out.print("-");
                        System.out.println();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
