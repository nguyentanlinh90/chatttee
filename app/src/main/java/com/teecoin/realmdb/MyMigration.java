package com.teecoin.realmdb;

import com.teecoin.utils.KeyStoreUtility;
import com.teecoin.utils.SecretKeyEncryption;

import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

public class MyMigration implements RealmMigration {

    //https://github.com/realm/realm-java/blob/master/examples/migrationExample/src/main/java/io/realm/examples/realmmigrationexample/model/Migration.java

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

        RealmSchema schema = realm.getSchema();

        if (oldVersion == 0) {
            schema.get("AccountModel").addField("language", String.class);
            schema.remove("ExchangeRateModel");
            oldVersion++;
        }

        if (oldVersion == 1) {
            if (!schema.contains("GoogleKeyConfigModel")) {
                schema.create("GoogleKeyConfigModel").addField("googleKey", String.class);
            }
            if (!schema.get("TransactionDetailModel").hasField("serial")) {
                schema.get("TransactionDetailModel").addField("serial", String.class);
            }

            oldVersion++;
        }
        if (oldVersion == 2) {
            if (!schema.get("FeeConfigModel").hasField("review_reward_amount")) {
                schema.get("FeeConfigModel").addField("review_reward_amount", String.class);
            }
            oldVersion++;
        }
        if (oldVersion == 3) {
            if (schema.get("AccountModel").hasField("phone_number")) {
                schema.get("AccountModel").removeField("phone_number");
            }
            if (schema.get("AccountModel").hasField("zipcode_country")) {
                schema.get("AccountModel").removeField("zipcode_country");
            }
            if (!schema.get("AccountModel").hasField("phone")) {
                schema.get("AccountModel").addField("phone", String.class);
            }
            if (!schema.get("AccountModel").hasField("country_code")) {
                schema.get("AccountModel").addField("country_code", String.class);
            }
            if (!schema.get("TransactionDetailModel").hasField("status")) {
                schema.get("TransactionDetailModel").addField("status", String.class);
            }
            oldVersion++;
        }
        if (oldVersion == 4) {
            if (!schema.get("FeeConfigModel").hasField("review_reward_amount_attachment")) {
                schema.get("FeeConfigModel").addField("review_reward_amount_attachment", String.class);
            }
            oldVersion++;
        }

        if (oldVersion == 5) {
            if (!schema.contains("TCLoggerEventModel")) {
                schema.create("TCLoggerEventModel")
                        .addField("timestamp", String.class)
                        .addField("event", String.class)
                        .addField("name", String.class)
                        .addField("data", String.class);
            }
            oldVersion++;
        }

        if (oldVersion == 6) {
            if (!schema.get("AccountModel").hasField("full_name")) {
                schema.get("AccountModel").addField("full_name", String.class);
            }

            if (!schema.get("AccountModel").hasField("gender")) {
                schema.get("AccountModel").addField("gender", String.class);
            }

            if (!schema.get("AccountModel").hasField("dob")) {
                schema.get("AccountModel").addField("dob", String.class);
            }
            oldVersion++;
        }

        if (oldVersion == 7) {
            if (!schema.get("AccountModel").hasField("provider")) {
                schema.get("AccountModel").addField("provider", String.class);
            }

            if (!schema.get("AccountModel").hasField("provider_id")) {
                schema.get("AccountModel").addField("provider_id", String.class);
            }

            if (!schema.get("AccountModel").hasField("have_password")) {
                schema.get("AccountModel").addField("have_password", boolean.class);
            }

            if (!schema.get("AccountModel").hasField("secret_key")) {
                schema.get("AccountModel").addField("secret_key", String.class)
                        .transform(new RealmObjectSchema.Function() {
                            @Override
                            public void apply(DynamicRealmObject obj) {
                                String decryptKey = KeyStoreUtility.getInstance().decryptString(obj.getString("secretSeed"));
                                obj.set("secret_key", SecretKeyEncryption.encrypt(decryptKey));
                            }
                        });
            }

            if (schema.get("AccountModel").hasField("secretSeed")) {
                schema.get("AccountModel").removeField("secretSeed");
            }
            oldVersion++;
        }
        if (oldVersion == 8) {
            if (!schema.get("TransactionDetailModel").hasField("icon")) {
                schema.get("TransactionDetailModel").addField("icon", String.class);
            }

            oldVersion++;
        }
        if (oldVersion == 9) {
            RealmObjectSchema account = schema.get("AccountModel");
            if(!schema.contains("Vendor")){
                RealmObjectSchema vendor = schema.create("Vendor")
                        .addField("id", String.class)
                        .addField("name", String.class)
                        .addField("email", String.class)
                        .addField("phone", String.class)
                        .addField("website", String.class)
                        .addField("bank_name", String.class)
                        .addField("bank_account_number", String.class);
                account.addRealmObjectField("vendor",vendor);
            }

            oldVersion++;
        }
        if (oldVersion == 10) {
            if (!schema.get("Vendor").hasField("address")) {
                schema.get("Vendor").addField("address", String.class);
            }
            if (!schema.get("Vendor").hasField("coinback_percentage")) {
                schema.get("Vendor").addField("coinback_percentage", String.class);
            }

            oldVersion++;
        }

        if (oldVersion == 11) {
            if (!schema.get("Vendor").hasField("logo")) {
                schema.get("Vendor").addField("logo", String.class);
            }

            if (schema.contains("GoogleKeyConfigModel")) {
                schema.remove("GoogleKeyConfigModel");
            }
            if (schema.contains("TCLoggerEventModel")) {
                schema.remove("TCLoggerEventModel");
            }

            oldVersion++;
        }
        if (oldVersion == 12) {
            if (!schema.get("FeeConfigModel").hasField("imageFloating")) {
                schema.get("FeeConfigModel").addField("imageFloating", String.class);
            }

            if (!schema.get("FeeConfigModel").hasField("urlFloating")) {
                schema.get("FeeConfigModel").addField("urlFloating", String.class);
            }
            if (!schema.get("FeeConfigModel").hasField("titleFloating")) {
                schema.get("FeeConfigModel").addField("titleFloating", String.class);
            }

            oldVersion++;
        }
    }
}
