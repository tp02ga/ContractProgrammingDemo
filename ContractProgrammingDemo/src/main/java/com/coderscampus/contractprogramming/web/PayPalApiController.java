package com.coderscampus.contractprogramming.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.coderscampus.contractprogramming.filter.LogRequestResponseFilter;

@Controller
@RequestMapping("/paypalApi")
public class PayPalApiController
{
  @RequestMapping(value="create-payment", method=RequestMethod.POST)
  public @ResponseBody Object createPayment ()
  {
    RestTemplate rt = new RestTemplate();
    
    ClientHttpRequestInterceptor ri = new LogRequestResponseFilter();
    List<ClientHttpRequestInterceptor> ris = new ArrayList<ClientHttpRequestInterceptor>();
    ris.add(ri);
    rt.setInterceptors(ris);
    rt.setRequestFactory(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
    
    MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
    
    headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
    headers.add("Authorization", "Basic QVNpU0ZibVpuRm85TWNHQ2FqOUZ6Q0NnNjRGWklneXdrMFhKQ1dXaUNJZTlQQURIREtJczNUWWMweUlvaFUzbUFnaUxqbmh3X2ZCdWdtNGU6RU9HN01oVXE5OXd6aU5lV0syemd2aVV1VnpJNTQxc2VWd08tY1BpbW02ZXZ1VGVlakJjczFBVWxyM2QtT1hTUmJtR1c4dlBOODV1WkVUSWQ=");
    headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
    String body = "grant_type=client_credentials";
    
    HttpEntity<?> requestEntity = new HttpEntity<String>(body, headers);
    
    ResponseEntity<String> response = rt.exchange("https://api.sandbox.paypal.com/v1/oauth2/token", 
        HttpMethod.POST, requestEntity, String.class);
    
    System.out.println(response);
    
    return null;
  }
  
}
