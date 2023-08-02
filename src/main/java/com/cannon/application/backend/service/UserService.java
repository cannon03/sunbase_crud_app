package com.cannon.application.backend.service;

import com.cannon.application.backend.dto.UseroDto.UserDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;

import org.springframework.http.*;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
public class UserService {

    public static String access_token="";
    public String getBearer() {
        String o = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment_auth.jsp";

        String details = "{\n" +
                "\"login_id\" : \"test@sunbasedata.com\",\n" +
                "\"password\" :\"Test@123\"\n" +
                "}";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set(("Authorization"), String.format("Bearer %s", access_token));
        try {

            HttpEntity<String> httpEntity = new HttpEntity<String>(details, httpHeaders);
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<String> response = restTemplate.exchange(o, HttpMethod.POST, httpEntity, String.class);
            JsonNode x=new ObjectMapper().readTree(response.getBody().toString()).get("access_token");
            access_token=x.asText();
            return access_token;
        } catch (Exception exception) {
            System.out.println("Exception occurred: " + exception);
            return "error occurred";
        }
    }


    public List<UserDto> getUsers() {
        String o="https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=get_customer_list";
        HttpRequest httpRequest=HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(o))
                .setHeader(("Authorization"),String.format("Bearer %s",access_token))
                .build();
        HttpClient httpClient=HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();

        try {
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper=new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
            String json=httpResponse.body();
            List<UserDto> userDtos=objectMapper.readValue(json, new TypeReference<List<UserDto>>() {
            });
            return userDtos;

        }
        catch(Exception e){
            throw new RuntimeException("Error occurred ",e);
        }
    }

    public ResponseEntity<String> removeUser(String uuid) {

        String o="https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=delete&uuid="+uuid;
        HttpHeaders httpHeaders= new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set(("Authorization"),String.format("Bearer %s",access_token));
        RestTemplate restTemplate=new RestTemplate();
        HttpEntity<String> httpEntity=new HttpEntity<String>("",httpHeaders);
        return restTemplate.exchange(o,HttpMethod.POST,httpEntity,String.class);

    }

    public ResponseEntity<String> createUser(UserDto userDto) {
        String o="https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=create";
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Accept",MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.set(("Authorization"),String.format("Bearer %s",access_token));


            HttpEntity<UserDto> httpEntity=new HttpEntity<UserDto>(userDto,httpHeaders);
            RestTemplate restTemplate=new RestTemplate();
        try {

            return restTemplate.exchange(o,HttpMethod.POST,httpEntity,String.class);

        }catch (Exception exception){

            return new ResponseEntity<String>("Exception:"+exception,HttpStatus.BAD_REQUEST);
        }


    }

    public ResponseEntity<String> editUser(String uuid, UserDto userDto) {
        String o="https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=update&uuid="+uuid;
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set(("Authorization"),String.format("Bearer %s",access_token));
        HttpEntity<UserDto> httpEntity=new HttpEntity<UserDto>(userDto,httpHeaders);
        RestTemplate restTemplate=new RestTemplate();
        try {

            return restTemplate.exchange(o,HttpMethod.POST,httpEntity,String.class);

        }catch (Exception exception){

            return new ResponseEntity<String>("Exception:"+exception,HttpStatus.BAD_REQUEST);
        }



    }
}
