package com.sh.beer.market.application.task.basevehicle;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 导入Base Vehicle历史数据
 * @author
 * @date 2023/9/4
 */

@Component
@RequiredArgsConstructor
public class ImportBaseVehicleTask {

    /*private static final int BATCH_SIZE = 50;
    private final BomsBaseVehicleDao bomsBaseVehicleDao;
    private final BaseVehicleIdGenerator baseVehicleIdGenerator;
    public ImportBaseVehicleRespDto execute(MultipartFile file) {
        //读取历史数据
        List<BaseVehicleHistory> baseVehicleHistoryList = readData(file);
        //校验数据
        checkData(baseVehicleHistoryList);
        //解析数据
        List<BomsBaseVehicleEntity> baseVehicleList = buildBaseVehicle(baseVehicleHistoryList);
        //批量保存到数据库
        for (List<BomsBaseVehicleEntity> partitionList : Lists.partition(baseVehicleList, BATCH_SIZE)) {
            bomsBaseVehicleDao.saveOrUpdateBatch(partitionList);
        }
        return new ImportBaseVehicleRespDto();
    }

    *//**
     * 读取历史数据
     * @param file
     * @return
     *//*
    private List<BaseVehicleHistory> readData(MultipartFile file) {
    try (InputStream inputStream = file.getInputStream();
         ExcelReader excelReader = EasyExcel.read(new BufferedInputStream(inputStream)).excelType(ExcelTypeEnum.XLSX).build()) {
         BaseVehicleReadListener readListener = new BaseVehicleReadListener();
         //读取Sheet 0
         ReadSheet readSheet = EasyExcel.readSheet(0).head(BaseVehicleHistory.class)
                 .registerReadListener(readListener)
                 .headRowNumber(1).build();
         excelReader.read(Lists.newArrayList(readSheet));
         return readListener.getBaseVehicleHistoryList();
    } catch (Exception e) {
        throw new BusinessException(ConfigErrorCode.EXCEL_UPLOAD_ERROR,e.getMessage());
    }
    }

    *//**
     * 校验数据
     * @param baseVehicleHistoryList
     *//*
    private void checkData(List<BaseVehicleHistory> baseVehicleHistoryList) {
        baseVehicleHistoryList.forEach(history -> {
            PreconditionUtil.checkNotBlank(history.getBaseVehicleId(),"Base Vehicle Id is blank");
            PreconditionUtil.checkNotBlank(history.getStatus(),"Status is blank");
            PreconditionUtil.checkNotBlank(history.getMaturity(),"Maturity is blank");
            PreconditionUtil.checkNotBlank(history.getDriveHand(),"Drive hand is blank");
            PreconditionUtil.checkNotBlank(history.getSalesVersion(),"Sales version is blank");
            PreconditionUtil.checkNotBlank(history.getRegionOptionCode(),"Region option code is blank");
            PreconditionUtil.checkNotBlank(history.getUpdateUser(),"Update user is blank");
            PreconditionUtil.checkNotBlank(history.getUpdateTime(),"Update time is blank");
            PreconditionUtil.checkNotBlank(history.getCreateTime(),"Create time is blank");
            PreconditionUtil.checkNotBlank(history.getCreateUser(),"Create user is blank");
            PreconditionUtil.checkNotBlank(history.getModelCode(),"Model code is blank");
            PreconditionUtil.checkNotBlank(history.getModelYear(),"Model year is blank");

        });
    }

    *//**
     * 解析Base Vehicle
     * @param baseVehicleHistoryList
     * @return
     *//*
    private List<BomsBaseVehicleEntity> buildBaseVehicle(List<BaseVehicleHistory> baseVehicleHistoryList) {

        return baseVehicleHistoryList.stream().map(history->{
            BomsBaseVehicleEntity entity = new BomsBaseVehicleEntity();
            entity.setBaseVehicleId(baseVehicleIdGenerator.createBaseVehicleId(RedisKeyConstant.BASE_VEHICLE_ID_KEY));
            entity.setModelCode(history.getModelCode());
            entity.setModelYear(history.getModelYear());
            entity.setMaturity(history.getMaturity());
            entity.setStatus(history.getStatus());
            entity.setDriveHand(history.getDriveHand());
            entity.setRegionOptionCode(history.getRegionOptionCode());
            entity.setSalesVersion(history.getSalesVersion());
            entity.setCreateUser(history.getCreateUser());
            entity.setUpdateUser(history.getUpdateUser());
            entity.setCreateTime(DateUtils.parseDate(history.getCreateTime(),DateUtils.YYYY_MM_DD_HH_MM_SS));
            entity.setUpdateTime(DateUtils.parseDate(history.getUpdateTime(),DateUtils.YYYY_MM_DD_HH_MM_SS));
            return entity;
        }).toList();
    }

    private static class BaseVehicleReadListener implements ReadListener<BaseVehicleHistory> {

        private final List<BaseVehicleHistory> baseVehicleHistoryList = Lists.newArrayList();

        @Override
        public void invoke(BaseVehicleHistory baseVehicleHistory, AnalysisContext analysisContext) {
            baseVehicleHistoryList.add(baseVehicleHistory);
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        }

        public List<BaseVehicleHistory> getBaseVehicleHistoryList() {return baseVehicleHistoryList;}
    }*/

}
