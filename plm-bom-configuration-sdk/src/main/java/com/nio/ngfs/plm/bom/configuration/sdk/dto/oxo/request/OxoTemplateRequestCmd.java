package com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author wangchao.wang
 */
@Getter
@Setter
@ToString
public class OxoTemplateRequestCmd {

    private String oxoTitle;

    private String changeContent;

    private String url;

    private List<SalesOptionName> salesOptionNames;

    private List<DriveHandOptionCode> driveHandOptionCodes;


    private List<RegionOptionCode> regionOptionCodes;


    private List<OxoInfo> templates;


    private List<HeadTitle> headTitles;




    @Getter
    @Setter
    public static class  HeadTitle{
        private String headTitleSize;
        private String headTitle;
    }


    @Getter
    @Setter
    public static class SalesOptionName{
        private String salesOptionName;

        private String color;
    }

    @Getter
    @Setter
    public static class DriveHandOptionCode{
        private String driveHandOptionCode;

        private String driveHandOptionSize;

        private String color;
    }


    @Getter
    @Setter
    public static class RegionOptionCode{
        private String regionOptionCode;

        private String regionCodeSize;

        private String color;
    }


    @Getter
    @Setter
    public static class OxoInfo{

        private String changeType;


        private String featureCode;


        private String disPlayName;

        private String name;


        private String library;


        private String color;


        private List<PackageOption> packageOptions;


    }

    @Getter
    @Setter
    public static class PackageOption{



        private String  packageOption;


        private Integer packSize;


        private String color;

    }
}
