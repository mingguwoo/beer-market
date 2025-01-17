package com.sh.beer.market.test;


/**
 * @author
 * @date 2023/8/23
 */
public class ProductContextApplicationTest extends AbstractTest {

    /*@Resource
    private ProductContextApplicationService productContextApplicationService;
    @Resource
    private OxoVersionSnapshotDomainService oxoVersionSnapshotDomainService;


    @Resource
    private ProductContextFacade productContextFacade;

    @Resource
    private ProductConfigFacadeImpl productConfigFacade;


    @Test
    public void test(){
        SyncSelectPcOptionDto dto = new SyncSelectPcOptionDto();
        dto.setPcId("Pegasus G1.1-0011");
        dto.setOptionCode("AD01");
        dto.setFeatureCode("AD00");
        productConfigFacade.syncSelectPcOptionToEnovia(dto);
    }

    @Test
    public void execute(){
        //OxoListRespDto OxoListRespDto = oxoVersionSnapshotDomainService.resolveSnapShot("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");
        //OxoListRespDto OxoListRespDto = oxoVersionSnapshotDomainService.resolveSnapShot("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");
        //OxoListRespDto OxoListRespDto = oxoVersionSnapshotDomainService.resolveSnapShot("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");
        String snapShot = "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";
        OxoListRespDto oxoListRespDto = oxoVersionSnapshotDomainService.resolveSnapShot(snapShot);
        productContextApplicationService.addProductContext(oxoListRespDto,"");
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
//        SyncProductContextEvent event = new SyncProductContextEvent(aggrList);
//        productContextFacade.syncAddProductContextToEnovia(event);
    }*/
}
