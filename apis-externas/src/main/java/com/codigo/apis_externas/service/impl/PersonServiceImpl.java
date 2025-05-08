package com.codigo.apis_externas.service.impl;

import com.codigo.apis_externas.aggregates.constants.Constants;
import com.codigo.apis_externas.aggregates.response.ReniecResponse;
import com.codigo.apis_externas.aggregates.response.ResponseBase;
import com.codigo.apis_externas.client.ClientReniec;
import com.codigo.apis_externas.entity.PersonEntity;
import com.codigo.apis_externas.exception.ConsultaReniecException;
import com.codigo.apis_externas.repository.PersonRepository;
import com.codigo.apis_externas.service.PersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final ClientReniec clientReniec;
    private final PersonRepository personRepository;

    @Value("${value.token}")
    private String token;

    @Override
    public ResponseBase<ReniecResponse> findByDni(String dni) {
        log.info("Buscando Información para el DNI: {}", dni);
        ReniecResponse reniecResponse = executionReniec(dni);
        return buildResponse(2004, "Todo OK!!", Optional.of(reniecResponse));
    }

    private ReniecResponse executionReniec(String dni) {
        log.info("Ejecutando consulta a RENIEC API para el DNI: {}", dni);
        String tokenOk = "Bearer " + token;

        try {
            ReniecResponse response = clientReniec.getPerson(dni, tokenOk);
            if (response == null || response.getNumeroDocumento() == null) {
                throw new ConsultaReniecException("Respuesta inválida de RENIEC o DNI no encontrado.");
            }
            return response;
        } catch (Exception e) {
            log.error("Error al consultar RENIEC para el DNI {}: {}", dni, e.getMessage());
            throw new ConsultaReniecException("Error al consultar RENIEC", e);
        }
    }

    @Override
    public ResponseBase<PersonEntity> registerPerson(String dni) {
        log.info("Registrando Persona con DNI: {}", dni);

        ReniecResponse reniecResponse = executionReniec(dni);

        PersonEntity personEntity = buildPersonEntity(reniecResponse);
        PersonEntity personSave = personRepository.save(personEntity);

        return buildResponse(2001, "Todo OK!!", Optional.of(personSave));
    }

    @Override
    public ResponseBase<List<PersonEntity>> findPersonActive() {
        List<PersonEntity> listPersonActive = personRepository.findAllByStatus(Constants.STATUS_ACTIVE);
        return buildResponse(2001, "Todo OK!!", Optional.of(listPersonActive));
    }

    private <T> ResponseBase<T> buildResponse(int code, String message, Optional<T> optional) {
        ResponseBase<T> responseBase = new ResponseBase<>();
        responseBase.setCode(code);
        responseBase.setMessage(message);
        responseBase.setEntity(optional);
        return responseBase;
    }

    private PersonEntity buildPersonEntity(ReniecResponse reniecResponse) {
        return PersonEntity.builder()
                .names(reniecResponse.getNombres())
                .fullName(reniecResponse.getNombreCompleto())
                .lastName(reniecResponse.getApellidoPaterno())
                .motherLastName(reniecResponse.getApellidoMaterno())
                .typeDocument(reniecResponse.getTipoDocumento())
                .numberDocument(reniecResponse.getNumeroDocumento())
                .checkDigit(reniecResponse.getDigitoVerificador())
                .status(Constants.STATUS_ACTIVE)
                .userCreated(Constants.USER_ADMIN)
                .dateCreated(new Timestamp(System.currentTimeMillis()))
                .build();
    }
}
