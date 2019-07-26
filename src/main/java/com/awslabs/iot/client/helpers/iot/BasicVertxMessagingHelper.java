package com.awslabs.iot.client.helpers.iot;

import com.amazonaws.AmazonClientException;
import com.awslabs.iot.client.data.ClientCertFilename;
import com.awslabs.iot.client.data.ClientPrivateKeyFilename;
import com.awslabs.iot.client.helpers.iot.interfaces.MessagingHelper;
import com.awslabs.iot.client.helpers.iot.interfaces.VertxMessagingHelper;
import io.vavr.control.Try;
import io.vertx.core.Vertx;
import io.vertx.core.net.PemKeyCertOptions;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.MqttClientOptions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class BasicVertxMessagingHelper implements VertxMessagingHelper {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(BasicVertxMessagingHelper.class);
    @Inject
    ClientCertFilename clientCertFilename;
    @Inject
    ClientPrivateKeyFilename clientPrivateKeyFilename;
    @Inject
    Vertx vertx;
    @Inject
    MessagingHelper messagingHelper;

    @Inject
    public BasicVertxMessagingHelper() {
    }

    @Override
    public MqttClient getClient() {
        Try.of(() -> {
            messagingHelper.doSetupIfNecessary();
            return null;
        })
                .onFailure(throwable -> {
                    if (throwable instanceof AmazonClientException) {
                        if (throwable.getMessage().contains("Unable to load AWS credentials from any provider in the chain")) {
                            log.error("Couldn't load your credentials from any provider in the chain.");
                            log.error("If you're using a credentials file is your .aws/credentials file configured?");
                            log.error("If you're using EC2 instance profiles is your instance profile assigned to this instance?");
                        }
                    }

                    throw new RuntimeException(throwable);
                });

        PemKeyCertOptions pemKeyCertOptions = new PemKeyCertOptions()
                .setCertPath(clientCertFilename.getClientCertFilename())
                .setKeyPath(clientPrivateKeyFilename.getClientPrivateKeyFilename());
        MqttClientOptions mqttClientOptions = new MqttClientOptions()
                .setPemKeyCertOptions(pemKeyCertOptions)
                .setAutoGeneratedClientId(true)
                .setSsl(true);

        MqttClient mqttClient = MqttClient.create(vertx, mqttClientOptions);
        mqttClient.connect(messagingHelper.getEndpointPort(), messagingHelper.getEndpointAddress(), s -> {
        });

        return mqttClient;
    }
}