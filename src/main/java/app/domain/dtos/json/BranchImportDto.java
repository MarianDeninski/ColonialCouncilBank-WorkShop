package app.ccb.domain.dtos.json;

import com.google.gson.annotations.Expose;

import javax.validation.constraints.NotNull;

public class BranchImportDto {

    @Expose
    private String name;

    @NotNull(message = "There is no name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
