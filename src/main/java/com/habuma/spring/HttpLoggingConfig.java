package com.habuma.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestClient;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Configuration
public class HttpLoggingConfig {

  private static final Logger LOGGER = LoggerFactory.getLogger(HttpLoggingConfig.class);

  @Bean
  RestClient.Builder restClientBuilder() {
    ClientHttpRequestInterceptor interceptor = (req, reqBody, ex) -> {
      ClientHttpResponse response = ex.execute(req, reqBody);
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("Request body: \n===========\n{}\n===========", new String(reqBody, StandardCharsets.UTF_8));

        final ClientHttpResponse responseCopy = new BufferingClientHttpResponseWrapper(response);
        InputStreamReader isr = new InputStreamReader(
            responseCopy.getBody(), StandardCharsets.UTF_8);
        String body = new BufferedReader(isr).lines()
            .collect(Collectors.joining("\n"));

        LOGGER.debug("Response body:\n===========\n{}\n===========", body);

        return responseCopy;
      } else {
        return response;
      }
    };

    return RestClient.builder()
        .requestInterceptor(interceptor);
  }

  final static class BufferingClientHttpResponseWrapper implements ClientHttpResponse {
    private final ClientHttpResponse response;
    @Nullable
    private byte[] body;

    BufferingClientHttpResponseWrapper(ClientHttpResponse response) {
      this.response = response;
    }

    @Override
    public HttpStatusCode getStatusCode() throws IOException {
      return this.response.getStatusCode();
    }

    @Override
    public String getStatusText() throws IOException {
      return this.response.getStatusText();
    }

    @Override
    public HttpHeaders getHeaders() {
      return this.response.getHeaders();
    }

    @Override
    public InputStream getBody() throws IOException {
      if (this.body == null) {
        this.body = StreamUtils.copyToByteArray(this.response.getBody());
      }
      return new ByteArrayInputStream(this.body);
    }

    @Override
    public void close() {
      this.response.close();
    }
  }
}