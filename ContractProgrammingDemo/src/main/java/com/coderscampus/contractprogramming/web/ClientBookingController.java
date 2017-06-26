package com.coderscampus.contractprogramming.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.coderscampus.contractprogramming.ClientRepository;
import com.coderscampus.contractprogramming.domain.Client;

@Controller
public class ClientBookingController
{
  @Autowired
  private ClientRepository clientRepo;
  
  @RequestMapping(value="/", method=RequestMethod.GET)
  public String indexPage (ModelMap model)
  {
    model.put("client", new Client());
    return "index";
  }
  
  @RequestMapping(value="/", method=RequestMethod.POST)
  public String indexPagePost (@ModelAttribute Client client)
  {
    System.out.println(client);
    clientRepo.save(client);
    System.out.println(client);
    return "redirect:/";
  }
}
