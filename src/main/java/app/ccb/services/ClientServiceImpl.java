package app.ccb.services;

import app.ccb.domain.dtos.json.ClientsDto;
import app.ccb.domain.entities.Card;
import app.ccb.domain.entities.Client;
import app.ccb.domain.entities.Employee;
import app.ccb.repositories.ClientRepository;
import app.ccb.repositories.EmployeeRepository;
import app.ccb.util.FileUtil;
import app.ccb.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {

    private final static String IMPORT_CLIENTS = "src/main/resources/files/json/clients.json";

    private final ClientRepository clientRepository;
    private final FileUtil fileUtil;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final EmployeeRepository employeeRepository;


    public ClientServiceImpl(ClientRepository clientRepository, FileUtil fileUtil,
                             ModelMapper modelMapper, Gson gson,
                             ValidationUtil validationUtil, EmployeeRepository employeeRepository) {
        this.clientRepository = clientRepository;
        this.fileUtil = fileUtil;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Boolean clientsAreImported() {

        return this.clientRepository.count() != 0;

    }

    @Override
    public String readClientsJsonFile() throws IOException {

        return this.fileUtil.readFile(IMPORT_CLIENTS);
    }

    @Override
    public String importClients(String clients) {

        ClientsDto[] clientsDtos = this.gson.fromJson(clients,ClientsDto[].class);

        StringBuilder sb = new StringBuilder();

        for (ClientsDto clientsDto : clientsDtos) {

            Client client;

            if(!this.validationUtil.isValid(clientsDto)){

                sb.append("Error: Incorrect Data!").append(System.lineSeparator());
                continue;

            }

            client = this.clientRepository.findByFullName(clientsDto.getFirstName()+" "+clientsDto.getLastName()).orElse(null);
            if(client==null){

                client = new Client();
                client.setFullName(clientsDto.getFirstName()+" "+clientsDto.getLastName());
                client.setAge(clientsDto.getAge());
                sb.append(String.format("Successfully imported Client - %s %s.",clientsDto.getFirstName(),clientsDto.getLastName()))
                        .append(System.lineSeparator());
                this.clientRepository.saveAndFlush(client);
            }
            Employee here = this.employeeRepository.findByFullName(clientsDto.getAppointedEmployee());
            client.getEmployees()
                    .add((Employee)here);

        }

        return sb.toString().trim();
    }

    @Override
    public String exportFamilyGuy() {

        List<Client> clients = this.clientRepository.getallClients();



        StringBuilder sb = new StringBuilder();
        for (Client client : clients) {

            sb.append("Full Name:"+client.getFullName()).append(System.lineSeparator());
            sb.append("Age:"+client.getAge()).append(System.lineSeparator());
            sb.append("Bank Account:"+client.getBankAccount().getAccountNumber()).append(System.lineSeparator());

            for (Card card : client.getBankAccount().getCards()) {

                sb.append(" Card Number:"+card.getCardNumber()).append(System.lineSeparator());
                sb.append(" Card Status:"+card.getCardStatus()).append(System.lineSeparator());


            }

            break;

        }

        return sb.toString().trim();
    }
}
