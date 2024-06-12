package mapper;

import model.dto.CreatCustomerDto;
import model.dto.CustomerDto;
import model.entity.Customer;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Random;

public class Mapper {
    public static Customer fromCreatCustomerDtoToCustomer(CreatCustomerDto creatCustomerDto){
        if(creatCustomerDto==null){
            return null;
        }
        return Customer.builder()
                .id(new Random().nextInt(99999))
                .name(creatCustomerDto.name())
                .email(creatCustomerDto.email())
                .password(creatCustomerDto.password())
                .isDeleted(false)
                .createdDate(Date.valueOf(LocalDate.now()))
                .build();
    }
}
