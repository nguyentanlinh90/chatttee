package com.teecoin.base;

import com.facebook.stetho.Stetho;
import com.teecoin.BuildConfig;
import com.teecoin.realmdb.MyMigration;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import core.base.BaseApplication;
import io.realm.Realm;
import io.realm.RealmConfiguration;


public class TCApplication extends BaseApplication {


    private static final int schemaVersionChanged = 1;

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
        realmConfig();

        if (BuildConfig.DEBUG) {
            Stetho.initialize(
                    Stetho.newInitializerBuilder(this)
                            .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                            .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                            .build());
        }

    }

    private void realmConfig() {
        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(1) // Must be bumped when the schema changes
                .migration(new MyMigration()) // Migration to run instead of throwing an exception
                .build();
        Realm.setDefaultConfiguration(config);

//        RealmConfiguration config = new RealmConfiguration.Builder()
//                .deleteRealmIfMigrationNeeded()
//                .build();
//        Realm.setDefaultConfiguration(config);
        // https://stackoverflow.com/questions/49892481/android-realm-version-update-error
    }

}
