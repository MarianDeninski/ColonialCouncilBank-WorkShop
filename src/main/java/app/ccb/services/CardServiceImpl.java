package app.ccb.services;

import app.ccb.domain.dtos.xml.CardDto;
import app.ccb.domain.dtos.xml.wrapper.CardDtoWrapper;
import app.ccb.domain.entities.BankAccount;
import app.ccb.domain.entities.Card;
import app.ccb.repositories.BankAccountRepository;
import app.ccb.repositories.CardRepository;
import app.ccb.util.ValidationUtil;
import app.ccb.util.FileUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;

@Service
public class CardServiceImpl implements CardService {

    private final static String IMPORT_CARDS = "src/main/resources/files/xml/cards.xml";

    private final CardRepository cardRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final FileUtil fileUtil;
    private final BankAccountRepository bankAccountRepository;


    public CardServiceImpl(CardRepository cardRepository, ModelMapper modelMapper, ValidationUtil validationUtil, FileUtil fileUtil, BankAccountRepository bankAccountRepository) {
        this.cardRepository = cardRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.fileUtil = fileUtil;
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    public Boolean cardsAreImported() {

        return this.cardRepository.count() != 0;

    }

    @Override
    public String readCardsXmlFile() throws IOException {

        return this.fileUtil.readFile(IMPORT_CARDS);
    }

    @Override
    public String importCards() throws JAXBException {

        StringBuilder sb = new StringBuilder();
        JAXBContext jaxbContext = JAXBContext.newInstance(CardDtoWrapper.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        CardDtoWrapper cardDtoWrapper = (CardDtoWrapper) unmarshaller.unmarshal(new File(IMPORT_CARDS));

        for (CardDto cardDto : cardDtoWrapper.getCards()) {

            if (!this.validationUtil.isValid(cardDto)) {
                sb.append("Error: Incorrect Data!").append(System.lineSeparator());
                continue;
            }
            BankAccount bankAccount = this.bankAccountRepository.findAllByAccountNumber(cardDto.getAccountNumber());
            Card card = this.modelMapper.map(cardDto,Card.class);
            card.setBankAccount(bankAccount);

            sb.append(String.format("Successfully imported Card - %s.",cardDto.getCardNumber())).append(System.lineSeparator());
            this.cardRepository.saveAndFlush(card);

        }

        return sb.toString().trim();
    }
}
