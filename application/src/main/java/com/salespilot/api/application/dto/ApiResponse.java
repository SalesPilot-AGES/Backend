package com.salespilot.api.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Classe genérica para padronizar as respostas de todos os endpoints.
 * Encapsula o conteúdo da resposta e uma mensagem amigável para o usuário.
 *
 * @param <T> O tipo do conteúdo da resposta
 */
@JsonPropertyOrder({"content", "message"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        @JsonProperty("content")
        T content,
        @JsonProperty("message")
        String message) {
    /**
     * Cria uma resposta de sucesso com a mensagem fornecida.
     *
     * @param content O conteúdo da resposta
     * @param message A mensagem de sucesso
     * @return Uma instância de ApiResponse
     */
    public static <T> ApiResponse<T> success(T content, String message) {
        return new ApiResponse<>(content, message);
    }

    /**
     * Cria uma resposta com conteúdo nulo e apenas a mensagem.
     *
     * @param message A mensagem
     * @return Uma instância de ApiResponse com content nulo
     */
    public static <T> ApiResponse<T> message(String message) {
        return new ApiResponse<>(null, message);
    }
}
