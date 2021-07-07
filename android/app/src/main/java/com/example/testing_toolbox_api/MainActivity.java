package com.example.testing_toolbox_api;

import androidx.annotation.NonNull;
import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;

import ch.fhnw.geiger.localstorage.StorageException;
import eu.cybergeiger.communication.Declaration;
import eu.cybergeiger.communication.DeclarationMismatchException;
import ch.fhnw.geiger.localstorage.db.GenericController;
import eu.cybergeiger.communication.LocalApi;
import eu.cybergeiger.communication.LocalApiFactory;
import ch.fhnw.geiger.localstorage.db.data.Node;
import ch.fhnw.geiger.localstorage.db.data.NodeImpl;

public class MainActivity extends FlutterActivity {
    private static final String CHANNEL = "com.geiger.dev/communicationAPi";
    private LocalApi localApi;
    private GenericController genericController;

    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);
        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL)
                .setMethodCallHandler(
                        (call, result) -> {
                            // Note: this method is invoked on the main thread.
                            if (call.method.equals("storage")) {
                                int storageMsg = storage();

                                if (storageMsg == 1) {
                                    result.success(storageMsg);
                                } else {
                                    result.error("UNAVAILABLE", "Storage not created.", null);
                                }
                            } else {
                                result.notImplemented();
                            }
                        }
                );
    }

    private int storage(){
        try{
            this.localApi = LocalApiFactory.getLocalApi("", LocalApi.MASTER, Declaration.DO_NOT_SHARE_DATA);
            this.genericController = (GenericController) localApi.getStorage();

            //create node
            Node userUUIdNode = new NodeImpl("328161f6-89bd-49f6-user-5a375ff56ana", ":Users");
            //add node to database
            this.genericController.add(userUUIdNode);
            this.genericController.update(userUUIdNode);

            return 1;
        }
        catch (StorageException | DeclarationMismatchException se){
            se.printStackTrace();
            return  0;
        }


    }



}
