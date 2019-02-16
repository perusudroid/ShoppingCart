package com.perusudroid.shoppingcart.model;
import java.util.List;
/**
 * Awesome Pojo Generator
 * */
public class ProductResponse{
  private List<Data> data;
  private Integer status;
  public void setData(List<Data> data){
   this.data=data;
  }
  public List<Data> getData(){
   return data;
  }
  public void setStatus(Integer status){
   this.status=status;
  }
  public Integer getStatus(){
   return status;
  }
}