package com.adobe.aem.sample.site.core.pojo;

import java.util.List;

public class MenuModelPoJo {

  private String title;
  private String path;

  private List<MenuModelPoJo> childPagesList;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public List<MenuModelPoJo> getChildPages() {
    return childPagesList;
  }

  public void setChildPages(List<MenuModelPoJo> childPagesList) {
    this.childPagesList = childPagesList;
  }
}
