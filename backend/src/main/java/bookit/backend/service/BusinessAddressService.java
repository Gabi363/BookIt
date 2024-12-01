package bookit.backend.service;

import bookit.backend.model.dto.BusinessAddressDto;
import bookit.backend.model.entity.Business;
import bookit.backend.model.entity.BusinessAddress;
import bookit.backend.model.request.CreateAddressRequest;
import bookit.backend.repository.BusinessAddressRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
@Transactional
@RequiredArgsConstructor
public class BusinessAddressService {

    private final BusinessAddressRepository addressRepository;
    private final ModelMapper modelMapper;

    public Optional<BusinessAddressDto> createAddress(CreateAddressRequest request, Business business) {
        BusinessAddress address = BusinessAddress.builder()
                .business(business)
                .city(request.getCity())
                .street(request.getStreet())
                .localNumber(request.getLocalNumber())
                .postCode(request.getPostCode())
                .build();

        addressRepository.save(address);
        return Optional.of(modelMapper.map(address, BusinessAddressDto.class));
    }

    public void updateAddress(CreateAddressRequest request, Business business) {
        BusinessAddress address = addressRepository.findById(business.getId()).orElse(null);
        if(address == null) return;

        address.setCity(request.getCity());
        address.setStreet(request.getStreet());
        address.setLocalNumber(request.getLocalNumber());
        address.setPostCode(request.getPostCode());
        addressRepository.save(address);
    }
}
