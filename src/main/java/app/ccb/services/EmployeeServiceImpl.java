package app.ccb.services;

import app.ccb.domain.dtos.json.EmployeesDto;
import app.ccb.domain.entities.Client;
import app.ccb.domain.entities.Employee;
import app.ccb.repositories.BranchRepository;
import app.ccb.repositories.EmployeeRepository;
import app.ccb.util.FileUtil;
import app.ccb.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {


    private final static String IMPORT_EMPLOYEES = "src/main/resources/files/json/employees.json";

    private final EmployeeRepository employeeRepository;
    private final FileUtil fileUtil;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final BranchRepository branchRepository;


    public EmployeeServiceImpl(FileUtil fileUtil, ModelMapper modelMapper, EmployeeRepository employeeRepository, Gson gson, ValidationUtil validationUtil, BranchRepository branchRepository) {
        this.fileUtil = fileUtil;
        this.modelMapper = modelMapper;
        this.employeeRepository = employeeRepository;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.branchRepository = branchRepository;
    }

    @Override
    public Boolean employeesAreImported() {

        return this.employeeRepository.count() != 0;

    }

    @Override
    public String readEmployeesJsonFile() throws IOException {

        return this.fileUtil.readFile(IMPORT_EMPLOYEES);
    }

    @Override
    public String importEmployees(String employees) {

        EmployeesDto[] employeesDtos = this.gson.fromJson(employees,EmployeesDto[].class);
        StringBuilder sb = new StringBuilder();

        for (EmployeesDto employeesDto : employeesDtos) {
            Employee employee = new Employee();

            if(!this.validationUtil.isValid(employeesDto)){
                sb.append("Error: Incorrect Data!").append(System.lineSeparator());
                continue;

            }
            sb.append(String.format("Successfully imported Employee - %s.",employeesDto.getBranchName()))
                    .append(System.lineSeparator());

            employee.setFirstName(employeesDto.getFullName().split("\\s+")[0]);
            employee.setLastName(employeesDto.getFullName().split("\\s+")[1]);
            employee.setSalary(employeesDto.getSalary());
            employee.setStartedOn(LocalDate.parse(employeesDto.getStartedOn()));
            employee.setBranch(this.branchRepository.findAllByName(employeesDto.getBranchName()));

            this.employeeRepository.saveAndFlush(employee);

        }
        return sb.toString().trim();
    }

    @Override
    public String exportTopEmployees() {

        StringBuilder sb = new StringBuilder();

        List<Employee> employeesList = this.employeeRepository.getAllEmployees();

        for (Employee employee : employeesList) {

            sb.append(String.format("Full Name: %s %s\nSalary: %.2f\nStarted On: %s\nClients:\n",employee.getFirstName()
                    ,employee.getLastName(),employee.getSalary(),employee.getStartedOn()));

            for (Client client : employee.getClients()) {

                sb.append(client.getFullName()).append(System.lineSeparator());
            }

            sb.append(System.lineSeparator());
        }

        return sb.toString().trim();
    }
}
