package app.ccb.domain.dtos.xml.wrapper;

import app.ccb.domain.dtos.xml.BankAccountDto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "bank-accounts")
@XmlAccessorType(XmlAccessType.FIELD)
public class BankAccountWrapper {

    @XmlElement(name = "bank-account")
    private List<BankAccountDto> list;


    public List<BankAccountDto> getList() {
        return list;
    }

    public void setList(List<BankAccountDto> list) {
        this.list = list;
    }
}
