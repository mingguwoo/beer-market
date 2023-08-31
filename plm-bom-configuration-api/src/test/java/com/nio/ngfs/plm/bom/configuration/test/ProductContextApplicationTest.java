package com.nio.ngfs.plm.bom.configuration.test;

import com.nio.ngfs.plm.bom.configuration.application.service.ProductContextApplicationService;
import com.nio.ngfs.plm.bom.configuration.domain.facade.ProductContextFacade;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontext.ProductContextAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontext.event.SyncProductContextEvent;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author bill.wang
 * @date 2023/8/23
 */
public class ProductContextApplicationTest extends AbstractTest {

    @Resource
    private ProductContextApplicationService productContextApplicationService;


    @Resource
    private ProductContextFacade productContextFacade;
    @Test
    public void test(){
        List<ProductContextAggr> testList = new ArrayList<>();
        ProductContextAggr one = new ProductContextAggr();
        one.setModelYear("G1.5");
        one.setOptionCode("19aa");
        one.setModelCode("es5");
        one.setFeatureCode("1900");
    ProductContextAggr two = new ProductContextAggr();
    two.setModelYear("G1.5");
    two.setOptionCode("19aa");
    two.setModelCode("es5");
    two.setFeatureCode("1900");
    testList.add(one);
        testList.add(two);
        testList = testList.stream().distinct().toList();
        System.out.println(testList.size());
    }

    @Test
    public void execute(){
        productContextApplicationService.addProductContext("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");
//        productContextApplicationService.addProductContext("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");
    }

    @Test
    public void testSync(){
        ProductContextAggr aggr1 = new ProductContextAggr();
        aggr1.setFeatureCode("firstFeature");
        aggr1.setModelCode("ES6");
        aggr1.setOptionCode("firstOption");
        aggr1.setModelYear("G1.2");
        ProductContextAggr aggr2 = new ProductContextAggr();
        aggr2.setFeatureCode("secondFeature");
        aggr2.setModelCode("ES6");
        aggr2.setOptionCode("secondOption");
        aggr2.setModelYear("G1.2");
        ProductContextAggr aggr3 = new ProductContextAggr();
        aggr3.setFeatureCode("firstFeature");
        aggr3.setModelCode("ES6");
        aggr3.setOptionCode("thirdOption");
        aggr3.setModelYear("G1.2");
        List<ProductContextAggr> aggrList = new ArrayList<>();
        aggrList.add(aggr1);
        aggrList.add(aggr2);
        aggrList.add(aggr3);
        SyncProductContextEvent event = new SyncProductContextEvent(aggrList);
        productContextFacade.syncAddProductContextToEnovia(event);
    }
}
