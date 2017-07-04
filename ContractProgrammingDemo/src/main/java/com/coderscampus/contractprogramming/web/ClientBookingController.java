package com.coderscampus.contractprogramming.web;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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

    // Charge the user's card:
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("amount", 1000);
    params.put("currency", "usd");
    params.put("description", "Example charge");
    params.put("source", token);

    Charge charge = Charge.create(params); 
    return "redirect:/thankyou";
  }
}
