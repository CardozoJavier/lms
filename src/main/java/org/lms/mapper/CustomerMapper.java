package org.lms.mapper;

import org.lms.dto.response.CustomerResponseDTO;
import org.lms.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(source = "id", target = "userId")
    @Mapping(target = "firstName")
    @Mapping(target = "lastName")
    @Mapping(target = "email")
    @Mapping(target = "phoneNumber")
    @Mapping(target = "createdAt")
    @Mapping(target = "updatedAt")
    CustomerResponseDTO toDTO(Customer customer);
}
