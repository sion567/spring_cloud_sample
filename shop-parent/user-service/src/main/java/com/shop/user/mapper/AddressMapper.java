package com.shop.user.mapper;

import com.shop.dubbo.api.address.AddressResponse;
import com.shop.user.entity.Address;
import com.shop.user.service.dto.AddressCommand;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AddressMapper {

    @Mapping(target = "isDefault", qualifiedByName = "booleanToInteger")
    AddressResponse toDTO(Address address);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "phone", source = "receiverPhone")
    Address toEntity(AddressCommand request);

    @Mapping(target = "isDefault", qualifiedByName = "integerToBoolean")
    AddressCommand toCommand(AddressResponse request);

    @Named("booleanToInteger")
    default Integer booleanToInteger(Boolean value) {
        return value != null && value ? 1 : 0;
    }

    @Named("integerToBoolean")
    default Boolean integerToBoolean(Integer value) {
        return value != null && value == 1;
    }
}
