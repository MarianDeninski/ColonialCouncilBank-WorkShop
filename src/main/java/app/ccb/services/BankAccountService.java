package app.ccb.services;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public interface BankAccountService {

    Boolean bankAccountsAreImported();

    String readBankAccountsXmlFile() throws JAXBException, IOException;

    String importBankAccounts() throws JAXBException;
}
