package com.coderscampus.contractprogramming.web;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.coderscampus.contractprogramming.domain.Client;
import com.coderscampus.contractprogramming.repositories.ClientRepository;
import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;

@Controller
public class ClientBookingController
{
  @Autowired
  private ClientRepository clientRepo;

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public String indexPage(ModelMap model)
  {
    model.put("client", new Client());
    return "index";
  }

  @RequestMapping(value = "/", method = RequestMethod.POST)
  public String indexPagePost(@ModelAttribute Client client)
  {
    System.out.println(client);
    clientRepo.save(client);
    System.out.println(client);
    return "redirect:/deposit?email=" + client.getEmail();
  }

  @RequestMapping(value = "/deposit", method = RequestMethod.GET)
  public String depositPage(@RequestParam String email, ModelMap model)
  {
    Set<Client> clients = clientRepo.findByEmail(email);
    if (clients != null && clients.size() > 0)
    {
      Client client = clients.iterator().next();
      model.put("client", client);
    }
    else
    {
      model.put("client", new Client());
    }
    return "deposit";
  }
  
  @RequestMapping(value="/deposit", method=RequestMethod.POST)
  public String depositPagePost (@RequestParam(name="stripeToken") String token, @RequestParam String stripeEmail) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException
  {
    // Set your secret key: remember to change this to your live secret key in
    // production
    // See your keys here: https://dashboard.stripe.com/account/apikeys
    Stripe.apiKey = "sk_test_Bsbu9k4pO0R5qjqmIgNNGpKF";

    // Create a Customer:
    Map<String, Object> customerParams = new HashMap<String, Object>();
    customerParams.put("email", stripeEmail);
    customerParams.put("source", token);
    Customer customer = Customer.create(customerParams);

    Set<Client> clients = clientRepo.findByEmail(stripeEmail);
    
    Iterator<Client> itr = clients.iterator();
    
    Optional<Client> clientOpt = itr.hasNext() ? Optional.of(itr.next()) : Optional.empty();
    Client client = null;
    
    if (clientOpt.isPresent())
    {
      client = clientOpt.get();
    }
    else
    {
      throw new IllegalArgumentException("There was no client with email address: " + stripeEmail + " found.");
    }
    
    client.setStripeId(customer.getId());
    clientRepo.save(client);
    
    // Charge the Customer instead of the card:
    Map<String, Object> chargeParams = new HashMap<String, Object>();
    chargeParams.put("amount", 1000);
    chargeParams.put("currency", "usd");
    chargeParams.put("customer", customer.getId());
    Charge.create(chargeParams);
    
    return "redirect:/thankyou?email="+stripeEmail;
  }
  
  @RequestMapping(value="/thankyou", method=RequestMethod.GET)
  public String thankYouPage (@RequestParam String email, ModelMap model)
  {
    model.put("email", email);
    return "thankyou";
  }
  
  @RequestMapping(value="/thankyou", method=RequestMethod.POST)
  public String thankYouPagePost (@RequestParam String email, ModelMap model) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException
  {
    Set<Client> clients = clientRepo.findByEmail(email);
    
    Iterator<Client> itr = clients.iterator();
    
    if (itr.hasNext())
    {
      Client client = itr.next();
      if (!StringUtils.isEmpty(client.getStripeId()))
      {
       // YOUR CODE (LATER): When it's time to charge the customer again, retrieve the customer ID.
        Map<String, Object> chargeParams = new HashMap<String, Object>();
        chargeParams.put("amount", 780000);
        chargeParams.put("currency", "usd");
        chargeParams.put("customer", client.getStripeId());
        Charge.create(chargeParams);
      }
    }
    return "redirect:thankyou2";
  }
  
  @RequestMapping(value="/thankyou2", method=RequestMethod.GET)
  public String thankYouPage2 (ModelMap model)
  {
    return "thankyou2";
  }
  
}
