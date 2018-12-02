package app.ccb.services;

import app.ccb.domain.dtos.xml.BankAccountDto;
import app.ccb.domain.dtos.xml.wrapper.BankAccountWrapper;
import app.ccb.domain.entities.BankAccount;
import app.ccb.domain.entities.Client;
import app.ccb.repositories.BankAccountRepository;
import app.ccb.repositories.ClientRepository;
import app.ccb.util.FileUtil;
import app.ccb.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
public class BankAccountServiceImpl implements BankAccountService {

    private final static String IMPORT_BANKACCOUNTS = "src/main/resources/files/xml/bank-accounts.xml";

    private final BankAccountRepository bankAccountRepository;
    private final ModelMapper modelMapper;
    private final ClientRepository clientRepository;
    private final FileUtil fileIOUtil;
    private final ValidationUtil validationUtil;

    @Autowired
    public BankAccountServiceImpl(BankAccountRepository bankAccountRepository,
                                  ModelMapper modelMapper,
                                  ClientRepository clientRepository,
                                  FileUtil fileIOUtil, ValidationUtil validationUtil) {
        this.bankAccountRepository = bankAccountRepository;
        this.modelMapper = modelMapper;
        this.clientRepository = clientRepository;
        this.fileIOUtil = fileIOUtil;
        this.validationUtil = validationUtil;
    }


    @Override
    public Boolean bankAccountsAreImported() {

        return this.bankAccountRepository.count() != 0;
    }

    @Override
    public String readBankAccountsXmlFile() throws IOException {
        return this.fileIOUtil.readFile(IMPORT_BANKACCOUNTS);
    }

    @Override
    public String importBankAccounts() throws JAXBException {

        StringBuilder sb = new StringBuilder();
        JAXBContext context = JAXBContext.newInstance(BankAccountWrapper.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        BankAccountWrapper bankAccountImportRootDto = (BankAccountWrapper) unmarshaller
                .unmarshal(new File(IMPORT_BANKACCOUNTS));


        for (BankAccountDto bankAccountImportDto :bankAccountImportRootDto.getList()) {

            if(!this.validationUtil.isValid(bankAccountImportDto)){

                sb.append("Error: Incorrect Data!").append(System.lineSeparator());
                continue;
            }

            System.out.println(bankAccountImportDto);
            Client entity = this.clientRepository.findByFullName(bankAccountImportDto.getClient()).orElse(null);

            if(entity ==null){
                sb.append("Error: Incorrect Data!").append(System.lineSeparator());
                continue;
            }

            BankAccount bankAccount = this.modelMapper.map(bankAccountImportDto,BankAccount.class);
            bankAccount.setClient(entity);

            sb.append(String.format("Successfully imported Bank Account - %s", bankAccountImportDto.getAccountNumber())).append(System.lineSeparator());

            this.bankAccountRepository.saveAndFlush(bankAccount);

        }

        return sb.toString().trim();
    }
}
