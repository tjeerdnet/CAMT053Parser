# CAMT053Parser
CAMT053Parser - a CAMT.053 XML format based bank statement parser

The CAMT053 parser supports [camt.053.001.02](http://www.iso20022.org/message_archive.page#Bank2CustomerCashManagement) and will
read a CAMT.053 XML formatted input stream and returns a document model holding the bank statement.

The test folder contains an example [importCamt053.java](/src/test/java/importCamt053.java) on how to read the XML file and
print entries (transactions) of the bank statement.

The example program displays the IBAN, amount, remittance information (payment description) and name of the creditor/debtor for:
- outgoing payments (debit)
- incoming payments (credit)
- batch outgoing payments (debit)

More information can be retrieved from the read document, but it is recommended to read the specifications.
