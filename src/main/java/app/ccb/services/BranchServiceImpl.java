package app.ccb.services;

import app.ccb.domain.dtos.json.BranchImportDto;
import app.ccb.domain.entities.Branch;
import app.ccb.repositories.BranchRepository;
import app.ccb.util.FileUtil;
import app.ccb.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class BranchServiceImpl implements BranchService {

    private final static String IMPORT_BRANCHES = "src/main/resources/files/json/branches.json";

    private final FileUtil fileUtil;
    private final ModelMapper modelMapper;
    private final BranchRepository branchRepository;
    private final Gson gson;
    private final ValidationUtil validationUtil;


    @Autowired
    public BranchServiceImpl(FileUtil fileUtil, ModelMapper modelMapper, BranchRepository branchRepository, Gson gson, ValidationUtil validationUtil) {
        this.fileUtil = fileUtil;
        this.modelMapper = modelMapper;
        this.branchRepository = branchRepository;
        this.gson = gson;
        this.validationUtil = validationUtil;
    }

    @Override
    public Boolean branchesAreImported() {


        return this.branchRepository.count() != 0;

    }

    @Override
    public String readBranchesJsonFile() throws IOException {

      return  this.fileUtil.readFile(IMPORT_BRANCHES);



    }

    @Override
    public String importBranches(String branchesJson) {
        BranchImportDto[] branchImportDtos = this.gson.fromJson(branchesJson,BranchImportDto[].class);

        StringBuilder sb = new StringBuilder();


        for (BranchImportDto branchImportDto : branchImportDtos) {

           if(!this.validationUtil.isValid(branchImportDto)){
               sb.append("Error: Incorrect Data!").append(System.lineSeparator());
               continue;
           }

            Branch branch = this.modelMapper.map(branchImportDto,Branch.class);
           this.branchRepository.saveAndFlush(branch);
           sb.append(String.format("Successfully imported Branch – %s.",branchImportDto
                   .getName()))
                   .append(System.lineSeparator());

        }
        return sb.toString().trim();
    }
}
