package com.api.security.creditcard;

import com.api.persistence.db.orm.DataSourceConverter;

/**
 * @author RTerrell
 * 
 */
public class CreditCardFactory extends DataSourceConverter {
    public static final String TYPE_VISA = "VISA";

    public static final String TYPE_MASTERCARD = "MASTERCARD";

    public static final String TYPE_AMEX = "AMEX";

    public static final String TYPE_DISCOVER = "DISCOVER";

    public static final String TYPE_DINERS = "DINERSCLUB";

    private static CreditCardProcessingDriver ccProcessor = null;

    public static CreditCardProcessingDriver getInstance() {
        try {
            if (ccProcessor == null) {
                ccProcessor = new CreditCardProcessingDriver();
            }
            return ccProcessor;
        } catch (Exception e) {
            return null;
        }
    }

    public static AddressVerification getAddressInstance() {
        try {
            AddressVerification obj = new AddressVerification();
            return obj;
        } catch (Exception e) {
            return null;
        }
    }

    public static AddressVerification getAddressInstance(String firstName,
            String lastName, String addr1, String addr2, String city,
            String state, String zip, String zipExt, String country,
            String phone) {
        AddressVerification obj = CreditCardFactory.getAddressInstance();
        if (obj == null) {
            return null;
        }
        obj.setFirstName(firstName);
        obj.setLastName(lastName);
        obj.setAddr1(addr1);
        obj.setAddr2(addr2);
        obj.setCity(city);
        obj.setState(state);
        obj.setZip(zip);
        obj.setZipExt(zipExt);
        obj.setCountry(country);
        obj.setPhone(phone);
        return obj;
    }

    public static CreditCardIdentification getIdentificationInstance() {
        try {
            CreditCardIdentification obj = new CreditCardIdentification();
            return obj;
        } catch (Exception e) {
            return null;
        }
    }

    public static CreditCardIdentification getIdentificationInstance(String id,
            String expiryDate, String verificationCode, String xactAmount,
            String type) {
        CreditCardIdentification obj = CreditCardFactory
                .getIdentificationInstance();
        if (obj == null) {
            return null;
        }
        obj.setId(id);
        obj.setExpiryDate(expiryDate);
        obj.setVerificationCode(verificationCode);
        obj.setType(type);
        obj.setXactAmount(xactAmount);
        return obj;
    }

    public static CreditCardProcessingDriver getDriver() {
        try {
            CreditCardProcessingDriver obj = new CreditCardProcessingDriver();
            return obj;
        } catch (Exception e) {
            return null;
        }
    }

    public static CreditCardProcessingDriver getDriver(
            AddressVerification addr, CreditCardIdentification id) {
        CreditCardProcessingDriver obj = CreditCardFactory.getDriver();
        obj.setCreditCardAddr(addr);
        obj.setCreditCardId(id);
        return obj;
    }

    public static CreditCard getVisaInstance() {
        try {
            CreditCard api = new VisaProcessor();
            return api;
        } catch (Exception e) {
            return null;
        }
    }

    public static CreditCard getMasterCardInstance() {
        try {
            CreditCard api = new MasterCardProcessor();
            return api;
        } catch (Exception e) {
            return null;
        }
    }

    public static CreditCard getDiscoverInstance() {
        try {
            CreditCard api = new DiscoverProcessor();
            return api;
        } catch (Exception e) {
            return null;
        }
    }

    public static CreditCard getAmexInstance() {
        try {
            CreditCard api = new AmericanExpressProcessor();
            return api;
        } catch (Exception e) {
            return null;
        }
    }

    public static CreditCard getDinersClubInstance() {
        try {
            CreditCard api = new DinersClubProcessor();
            return api;
        } catch (Exception e) {
            return null;
        }
    }
}
