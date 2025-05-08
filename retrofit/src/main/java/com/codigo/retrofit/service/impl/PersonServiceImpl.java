package com.codigo.retrofit.service.impl;

import com.codigo.retrofit.aggregates.response.ReniecResponse;
import com.codigo.retrofit.retrofit.ClientReniecService;
import com.codigo.retrofit.retrofit.ClientRetrofit;
import com.codigo.retrofit.service.PersonService;
import com.codigo.retrofit.aggregates.exception.ConsultaReniecException;
import com.codigo.retrofit.aggregates.response.ApiErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.Objects;

@Service
@Log4j2
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final ClientReniecService retrofitPreConfig =
            ClientRetrofit.getRetrofit()
                    .create(ClientReniecService.class);

    @Value("${token.api}")
    private String token;

    @Override
    public ReniecResponse findByDni(String dni) {
        try {
            Response<ReniecResponse> executeReniec = preparedClient(dni).execute();
            if (executeReniec.isSuccessful() && Objects.nonNull(executeReniec.body())) {
                return executeReniec.body();
            } else {
                throw new ConsultaReniecException("Error al consultar Reniec. Código: " + executeReniec.code());
            }
        } catch (IOException e) {
            log.error("Error durante la comunicación con el servicio Reniec: {}", e.getMessage(), e);
            throw new ConsultaReniecException("Error durante la comunicación con el servicio Reniec", e);
        } catch (Exception e) {
            log.error("Error al consultar RENIEC para el DNI {}: {}", dni, e.getMessage());
            throw new ConsultaReniecException("Error al consultar RENIEC", e);
        }
    }

    private Call<ReniecResponse> preparedClient(String dni) {
        return retrofitPreConfig.findReniec("Bearer " + token, dni);
    }
}
