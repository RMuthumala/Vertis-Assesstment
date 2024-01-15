package com.adobe.aem.sample.site.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.adobe.aem.sample.site.core.pojo.MenuModelPoJo;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.util.List;
import java.util.Objects;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
class MegaMenuModelTest {

  private static final AemContext context = new AemContext();

  private MegaMenuModel megaMenuModel;


  @Test
  void getMenuModelPagesList() {
    context.load(false)
        .json("/com/adobe/aem/sample/site/core/models/MegaMenuModel/en.json",
            "/content/aemsamplesite/us/en3");
    Resource res = context.load(true)
        .json("/com/adobe/aem/sample/site/core/models/MegaMenuModel/megaMenuModel.json",
            "/content/aemsamplesite/us/en/firstPage1");
    context.currentResource(res);
    megaMenuModel = Objects.requireNonNull(context.currentResource())
        .adaptTo(MegaMenuModel.class);
    assertNotNull(megaMenuModel);
    assertNotNull(megaMenuModel.getMegaMenuPagesList());
    List<MenuModelPoJo> megaMenuPagesList = megaMenuModel.getMegaMenuPagesList();
    assertEquals(4, megaMenuPagesList.size());
    assertNull(megaMenuPagesList.get(0).getChildPages());
    assertNotNull(megaMenuPagesList.get(2).getChildPages());
    assertEquals(1, megaMenuPagesList.get(2).getChildPages().size());
    assertNotNull(megaMenuPagesList.get(2).getChildPages().get(0));
    assertEquals(1, megaMenuPagesList.get(2).getChildPages().get(0).getChildPages().size());
    assertNotNull(megaMenuPagesList.get(3).getChildPages());
    assertEquals(0, megaMenuPagesList.get(3).getChildPages().size());
  }

  @Test
  void getMenuModelPagesListTestCase() {
    context.load(false)
        .json("/com/adobe/aem/sample/site/core/models/MegaMenuModel/megaMenuModelPage.json",
            "/content/aemsamplesite/us/en1");
    Resource res = context.load(true)
        .json("/com/adobe/aem/sample/site/core/models/MegaMenuModel/megaMenuModel1.json",
            "/content/aemsamplesite/us/en/firstPage2");
    context.currentResource(res);
    megaMenuModel = Objects.requireNonNull(context.currentResource())
        .adaptTo(MegaMenuModel.class);
    assertNotNull(megaMenuModel);
    assertNotNull(megaMenuModel.getMegaMenuPagesList());
    List<MenuModelPoJo> megaMenuPagesList = megaMenuModel.getMegaMenuPagesList();
    assertEquals(3, megaMenuPagesList.size());
    assertEquals(0, megaMenuPagesList.get(0).getChildPages().size());
    assertNotNull(megaMenuPagesList.get(1).getChildPages());
    assertEquals(1, megaMenuPagesList.get(1).getChildPages().size());
    assertNotNull(megaMenuPagesList.get(2).getChildPages());
    assertEquals(2, megaMenuPagesList.get(2).getChildPages().size());
    assertNotNull(megaMenuPagesList.get(2).getChildPages().get(0).getChildPages());
    assertEquals(2, megaMenuPagesList.get(2).getChildPages().get(0).getChildPages().size());
    assertNull(megaMenuPagesList.get(2).getChildPages().get(1).getChildPages());
  }

  @Test
  void getMenuModelPagesListWithFirstPage() {
    context.load(false)
        .json("/com/adobe/aem/sample/site/core/models/MegaMenuModel/firstPage.json",
            "/content/aemsamplesite/us/en2");
    Resource res = context.load(true)
        .json("/com/adobe/aem/sample/site/core/models/MegaMenuModel/megaMenuModel2.json",
            "/content/aemsamplesite/us/en/first/page3");
    context.currentResource(res);
    megaMenuModel = Objects.requireNonNull(context.currentResource())
        .adaptTo(MegaMenuModel.class);
    assertNotNull(megaMenuModel);
    assertNotNull(megaMenuModel.getMegaMenuPagesList());
    List<MenuModelPoJo> megaMenuPagesList = megaMenuModel.getMegaMenuPagesList();
    assertEquals(3, megaMenuPagesList.size());
    assertEquals(1, megaMenuPagesList.get(0).getChildPages().size());
    assertNotNull(megaMenuPagesList.get(1).getChildPages());
    assertEquals(1, megaMenuPagesList.get(1).getChildPages().size());
    assertNull(megaMenuPagesList.get(2).getChildPages());
  }
}