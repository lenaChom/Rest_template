package com.example.rest_template.rest;

import com.example.rest_template.model.User;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
public class RestService {
    private static final String API_URL = "http://94.198.50.185:7081/api/users";
    private static String sessionId;

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();

        //Получение списка всех пользователей
        ResponseEntity<String> response = restTemplate.getForEntity(API_URL, String.class);
        HttpHeaders headers = response.getHeaders();
        sessionId = headers.getFirst(HttpHeaders.SET_COOKIE);

        System.out.println("Список всех пользователей:");
        System.out.println(response.getBody());

        //Сохранение пользователя
        User newUser = new User(3L,"James","Brown",(byte) 30);

        HttpHeaders postHeaders = new HttpHeaders();
        postHeaders.set(HttpHeaders.COOKIE, sessionId);
        HttpEntity<User> postRequest = new HttpEntity<>(newUser, postHeaders);
        ResponseEntity<String> postResponse = restTemplate.postForEntity(API_URL, postRequest, String.class);

        System.out.println("Результат добавления пользователя:");
        System.out.println(postRequest.getBody());

        //Изменение пользователя
        User updatedUser = new User(3L, "Thomas", "Shelby", (byte) 30);
        HttpHeaders updateHeaders = new HttpHeaders();
        updateHeaders.set(HttpHeaders.COOKIE, sessionId);
        String userJson = "{\"id\":" + updatedUser.getId() + ", \"name\":\"" + updatedUser.getName() + "\", \"lastName\":\"" + updatedUser.getLastName() + "\", \"age\":" + updatedUser.getAge() + "}";
        updateHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> updateRequest = new HttpEntity<>(userJson, updateHeaders);
        ResponseEntity<String> updateResponse = restTemplate.exchange(API_URL, HttpMethod.PUT, updateRequest, String.class);

        System.out.println("Пользователь обновлен: ");
        System.out.println(updateRequest.getBody());

        //Удаление пользователя
        HttpHeaders deleteHeaders = new HttpHeaders();
        deleteHeaders.set(HttpHeaders.COOKIE, sessionId);
        HttpEntity<String> deleteRequest = new HttpEntity<>(deleteHeaders);
        ResponseEntity<String> deleteResponse =restTemplate.exchange(API_URL + "/3", HttpMethod.DELETE, deleteRequest, String.class);



        String generalString = postResponse.getBody() + updateResponse.getBody()+ deleteResponse.getBody();
        System.out.println("Общая строка = "+ generalString);
        System.out.println("Длина общей строки = "+ generalString.length());
    }
}
