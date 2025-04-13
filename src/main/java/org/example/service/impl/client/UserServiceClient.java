package org.example.service.impl.client;


import com.nimbusds.jose.shaded.gson.Gson;
import org.example.constant.ResponseConstant;
import org.example.dto.CommonException;
import org.example.dto.RestApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Type;
import java.util.Objects;

@Service
public class UserServiceClient<T> {
    private static final Logger log = LoggerFactory.getLogger(UserServiceClient.class);
    @Autowired
    private WebClient webClient;
    @Autowired
    private MessageSource messageSource;

    public T getUserById(Integer userId, Class<T> type, String refId) {
            return convertResponse(
                    Objects.requireNonNull(webClient.get()
                        .uri("/users/{id}", userId)
                        .retrieve()
                        .onStatus(response -> !response.is2xxSuccessful(), clientResponse ->
                                Mono.error(CommonException.builder().code(ResponseConstant.ERR_DATA_NOT_FOUND)
                                        .exception(String.format(messageSource.getMessage(ResponseConstant.ERR_INVALID_INPUT, null, null), "User")).build()))
                        .bodyToMono(RestApiResponse.class)
                        .block()), type, refId);
    }

    private T convertResponse(RestApiResponse response, Class<T> type, String refId){
        log.info("parsing response from getUserById for refId {}", refId);
        return new Gson().fromJson(response.getData().toString(), type);
    }

}
