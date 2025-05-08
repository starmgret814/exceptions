package com.codigo.apis_externas.client;

import com.codigo.apis_externas.aggregates.response.ReniecResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "client-reniec", url = "https://api.apis.net.pe/v2/reniec/")
public interface ClientReniec {

    //https://api.apis.net.pe/v2/reniec/dni?numero=46027897
    @GetMapping(value = "/dni", produces = "application/json")
    ReniecResponse getPerson(@RequestParam("numero") String numero,
                             @RequestHeader("Authorization") String authorization);
}
