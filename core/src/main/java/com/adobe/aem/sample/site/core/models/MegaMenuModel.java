package com.adobe.aem.sample.site.core.models;

import com.adobe.aem.sample.site.core.pojo.MenuModelPoJo;
import com.day.cq.wcm.api.Page;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MegaMenuModel to compute the menu links
 */
@Model(adaptables = {Resource.class,
    SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class MegaMenuModel {

  /**
   * The AEM_SAMPLE_SITE_CONTENT_READER
   */
  private static final String AEM_SAMPLE_SITE_CONTENT_READER = "aemsamplesite-content-reader";

  /**
   * The HIDE_IN_NAV
   */
  private static final String HIDE_IN_NAV = "hideInNav";
  /**
   * The NAV_TITLE
   */
  private static final String NAV_TITLE = "navTitle";
  /**
   * The JCR_TITLE
   */
  private static final String JCR_TITLE = "jcr:title";
  /**
   * The HIDE_ALL_SUBPAGES_IN_NAVE
   */
  private static final String HIDE_ALL_SUBPAGES_IN_NAVE = "hideAllSubpagesInNav";
  /**
   * The HIDE_IN_NAV
   */
  private static final boolean FALSE = false;
  /**
   * The logger - Logger Object
   */
  private final Logger logger = LoggerFactory.getLogger(MegaMenuModel.class);
  /**
   * The pagePath - Contains path of page
   */
  @ValueMapValue
  @Default(values = "")
  private String pagePath;

  /**
   * The pagePath - Contains path of page
   */
  @ValueMapValue
  @Default(intValues = 3)
  @Named("depth")
  private int depth;

  /**
   * The resourceResolverFactory - ResourceResolverFactory object
   */
  @OSGiService
  private ResourceResolverFactory resourceResolverFactory;
  /**
   * The menuModelPagesList - Object of List type MenuModelPoJo
   */
  private List<MenuModelPoJo> menuModelPagesList;

  /**
   * This method is invoked on the object initialization.
   */
  @PostConstruct
  protected void init() {
    logger.info("Start of MegaMenuModel init method");
    logger.info("PagePath : {}", pagePath);
    ResourceResolver resourceResolver = getResourceResolver();
    if (resourceResolver != null && StringUtils.isNotBlank(pagePath)) {
      Resource pageResource = resourceResolver.getResource(pagePath);
      if (pageResource != null) {
        Page rootPage = pageResource.adaptTo(Page.class);
        menuModelPagesList = setChildrenInList(rootPage, 0);
      }
    }
    logger.info("End of MegaMenuModel init method with menuModelPagesList : {}",
        menuModelPagesList);
  }

  /**
   * Method is used to set page properties and page children list in MenuModelPoJo.
   *
   * @param rootPage - current page on which the processing will be done
   * @param currentDepth    - depth of current page
   * @return menuModelPoJoList - contains list of MenuModelPoJo objects.
   */
  private List<MenuModelPoJo> setChildrenInList(Page rootPage, int currentDepth) {
    List<MenuModelPoJo> menuModelPoJoList = new ArrayList<>();
    if (rootPage != null) {
      Iterator<Page> pageChildren = rootPage.listChildren();
      currentDepth++;
      while (pageChildren.hasNext()) {
        Page childPage = pageChildren.next();
        ValueMap valueMap = childPage.getProperties();
        if (!valueMap.get(HIDE_IN_NAV, FALSE)) {
          String title = valueMap.get(NAV_TITLE, StringUtils.EMPTY);
          if (StringUtils.isBlank(title)) {
            title = valueMap.get(JCR_TITLE, StringUtils.EMPTY);
          }
          String path = childPage.getPath() + ".html"; 
          MenuModelPoJo menuModelPoJo = new MenuModelPoJo();
          menuModelPoJo.setTitle(title );
          menuModelPoJo.setPath(path);
          if (currentDepth < depth && !valueMap.get(HIDE_ALL_SUBPAGES_IN_NAVE, FALSE)) {
            menuModelPoJo.setChildPages(setChildrenInList(childPage, currentDepth));
          }
          menuModelPoJoList.add(menuModelPoJo);
        }
      }
    }
    return menuModelPoJoList;
  }

  /**
   * Method is used to get list of MenuModelPoJo objects.
   *
   * @return menuModelPagesList - contains list of MenuModelPoJo objects.
   */
  public List<MenuModelPoJo> getMegaMenuPagesList() {
    return menuModelPagesList;
  }

  /**
   * Method is used to fetch ResourceResolver object with service user.
   *
   * @return resourceResolver - ResourceResolver object
   */
  private ResourceResolver getResourceResolver() {
    ResourceResolver resourceResolver = null;
    final Map<String, Object> params = new HashMap<>();
    params.put(ResourceResolverFactory.SUBSERVICE, AEM_SAMPLE_SITE_CONTENT_READER);
    try {
      resourceResolver = resourceResolverFactory.getServiceResourceResolver(params);
    } catch (LoginException e) {
      logger.error("Error while getting resource resolver: {}", e.getMessage());
    }
    logger.debug("end of getResourceResolver method with resourceResolver: {}", resourceResolver);
    return resourceResolver;
  }
}
