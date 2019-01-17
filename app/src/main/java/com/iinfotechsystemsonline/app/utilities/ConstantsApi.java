package com.iinfotechsystemsonline.app.utilities;

/**
 * Created by APPUSER1 on 26/11/2017.
 */

public class ConstantsApi {
    public static final String BASE_URL = "http://192.168.1.2:3000";
   public static final String LOGIN_URL = BASE_URL + "/authentication/users/login";
//    public static final String LOGIN_URL = BASE_URL + "/auth/login";

    public static final String PROPERTY_URL = BASE_URL + "/properties/accounts";

    public static final String APPS_URL = BASE_URL + "/applications";
    public static final String CHANGE_PASSWORD_URL = "";
    public static final String URL_GET_LOCALITIES = BASE_URL + "/localities/list/";
    public static final String URL_GET_TOWNS = BASE_URL + "/towns/list";
    public static final String URL_GET_MAIN_CATEGORIES = BASE_URL + "/main-fees/categories/list";
    public static final String URL_FEES_CATEGORY_LIST = BASE_URL + "/fees-fixing/list";
    public static final String URL_PAYMENT = BASE_URL + "/payments/make-payment";
    public static final String PROPERTY_TYPES_URL = BASE_URL + "/properties/property-types";
    public static final String BUSINESS_TYPES_URL = BASE_URL + "/business-types";
    public static final String URL_GET_BUSINESSES= BASE_URL + "/bops/list";
    public static final String BUSINESS_REGISTRATION_URL  = BASE_URL + "/bops";
    public static final String BUSINESS_TYPE_CATEGORIES_URL = BASE_URL + "/business-types/categories/";
    public static final String GET_PROPERTIES_URL = BASE_URL + "/properties/list/search";
    public static final String URL_SAVE_LOCATION = BASE_URL + "/agents/locations";
    public static final String PROPERTY_REGISTRATION_URL = BASE_URL + "/properties/register";
    public static final String ADS_REGISTRATION_URL = BASE_URL + "/ads/add";
    public static final String ADS_FEE_LIST_URL = BASE_URL + "/ads/categories";
    public static final String MARKET_FEE_LIST_URL = BASE_URL + "/operating-markets";
    public static final String MARKET_REGISTRATION_URL= BASE_URL + "/markets/register";

    public static final String setFeesListUrl(String id){
        return MARKET_FEE_LIST_URL+"/"+id;
    }

    public static String setPropertyPaymentUrl(String id) {
        return String.format("%s/properties/%s/payments", BASE_URL, id);
    }

    public static String setPropertyListUrl(String agentId) {
//        https:
//196.62.32.236:9100/api/v1/agents/{agentId}/properties
//        https://196.62.32.236:9100/api/v1/agents/{agentId}/properties
        return String.format("%s/agents/%s/properties", BASE_URL, agentId);
    }



    public static String updatePropertyUrl(String propertyId) {
//        https:
//196.62.32.236:9100/api/v1/agents/{agentId}/properties
//        https://196.62.32.236:9100/api/v1/agents/{agentId}/properties
        return String.format("%s/properties/%s/update", BASE_URL, propertyId);
    }
}
