package com.coderscampus.contractprogramming.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Project
{
  private Integer id;
  private String name;
  private Double cost;
  private Client client;
  
  @Id @GeneratedValue
  public Integer getId()
  {
    return id;
  }
  public void setId(Integer id)
  {
    this.id = id;
  }
  public String getName()
  {
    return name;
  }
  public void setName(String name)
  {
    this.name = name;
  }
  public Double getCost()
  {
    return cost;
  }
  public void setCost(Double cost)
  {
    this.cost = cost;
  }
  
  @ManyToOne
  public Client getClient()
  {
    return client;
  }
  public void setClient(Client client)
  {
    this.client = client;
  }
  @Override
  public String toString()
  {
    return "Project [id=" + id + ", name=" + name + ", cost=" + cost + "]";
  }
}
