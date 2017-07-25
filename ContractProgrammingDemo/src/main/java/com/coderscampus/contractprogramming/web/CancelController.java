package com.coderscampus.contractprogramming.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.coderscampus.contractprogramming.domain.Client;
import com.coderscampus.contractprogramming.repositories.ClientRepository;
import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Customer;
import com.stripe.model.CustomerSubscriptionCollection;
import com.stripe.model.Subscription;

@Controller
@RequestMapping("/cancel")
public class CancelController
{
  @Autowired
  private ClientRepository clientRepo;
  
  @RequestMapping(value="", method=RequestMethod.GET)
  public String cancelGet (@RequestParam String email, ModelMap model)
  {
    model.put("email", email);
    return "cancel";
  }
  
  @RequestMapping(value="", method=RequestMethod.POST)
  public @ResponseBody String cancelPost (@RequestParam String email, ModelMap model) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException
  {
    Set<Client> clients = clientRepo.findByEmail(email);
    
    Optional<Client> clientOpt = clients.stream().filter(c -> !StringUtils.isEmpty(c.getStripeId())).findFirst();
    
    if (clientOpt.isPresent())
    {
      Client client = clientOpt.get();
      Stripe.apiKey = "sk_test_Bsbu9k4pO0R5qjqmIgNNGpKF";

      Customer stripeCustomer = Customer.retrieve(client.getStripeId());
      
      CustomerSubscriptionCollection subscriptionsCollection = stripeCustomer.getSubscriptions();
      
      List<Subscription> subscriptions = subscriptionsCollection.getData();
      
      subscriptions.stream().forEach(subscription -> {
          Map<String, Object> params = new HashMap<>();
          params.put("at_period_end", true);
          
          try
          {
            subscription.cancel(params);
          } catch (AuthenticationException | InvalidRequestException
              | APIConnectionException | CardException | APIException e)
          {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        });
    }
    // cancel the stripe recurring billing
    System.out.println("we're inside of the cancel post method, and the email address is: " + email);
    return "{}";
  }
}
