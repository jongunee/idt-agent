package kr.smic.idt.agent.config;

import java.io.File;

import de.fraunhofer.iosb.ilt.faaast.service.assetconnection.AssetConnectionConfig;
import de.fraunhofer.iosb.ilt.faaast.service.assetconnection.opcua.OpcUaAssetConnectionConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import de.fraunhofer.iosb.ilt.faaast.service.config.CoreConfig;
import de.fraunhofer.iosb.ilt.faaast.service.config.ServiceConfig;
import de.fraunhofer.iosb.ilt.faaast.service.endpoint.http.HttpEndpointConfig;
import de.fraunhofer.iosb.ilt.faaast.service.endpoint.opcua.OpcUaEndpointConfig;
import de.fraunhofer.iosb.ilt.faaast.service.messagebus.internal.MessageBusInternalConfig;
import de.fraunhofer.iosb.ilt.faaast.service.persistence.memory.PersistenceInMemoryConfig;
import kr.smic.idt.agent.config.FaaastProperties.CoreProperties;
import kr.smic.idt.agent.config.FaaastProperties.EndpointProperties.HttpEndpointProperties;
import kr.smic.idt.agent.config.FaaastProperties.EndpointProperties.OpcuaEndpointProperties;
import kr.smic.idt.agent.config.FaaastProperties.ModelProperties;
import kr.smic.idt.agent.config.FaaastProperties.AssetConnectionProperties.OpcuaAssetConnectionProperties;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(FaaastProperties.class)
public class FaaastConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(FaaastConfiguration.class);

    private FaaastProperties properties;

    public FaaastConfiguration(FaaastProperties properties) {
        this.properties = properties;

        LOGGER.info(properties.toString());
    }

    public ServiceConfig getFaaastServiceConfig() {
        CoreProperties core = properties.getCore();
        ModelProperties model = properties.getModel();
        HttpEndpointProperties httpEndpoint = properties.getEndpoint().getHttp();
        OpcuaEndpointProperties opcuaEndpoint = properties.getEndpoint().getOpcua();
        OpcuaAssetConnectionProperties opcuaAssetConnection = properties.getAssetconnection().getOpcuaasset();

        return new ServiceConfig.Builder()
                .core(new CoreConfig.Builder()
                        .requestHandlerThreadPoolSize(core.getRequestHandlerThreadPoolSize())
                        .build()
                )
                .persistence(new PersistenceInMemoryConfig.Builder()
                        .initialModel(new File(model.getPath()))
                        .decoupleEnvironment(model.getDecoupled())
                        .build()
                )
                .endpoint(new HttpEndpointConfig.Builder()
                        .port(httpEndpoint.getPort())
                        .build()
                )
                .endpoint(new OpcUaEndpointConfig.Builder()
                        .tcpPort(opcuaEndpoint.getTcpPort())
                        .secondsTillShutdown(opcuaEndpoint.getSecondsTillShutdown())
                        .build()
                )
                .assetConnection(new OpcUaAssetConnectionConfig.Builder()
                        .host(opcuaAssetConnection.getHost())
                        .build()
                )
                .messageBus(new MessageBusInternalConfig())
                .build();
    }

    public ServiceConfig getDefaultFaaastServiceConfig() {
        return new ServiceConfig.Builder()
                .core(new CoreConfig.Builder().requestHandlerThreadPoolSize(2).build())
                .persistence(new PersistenceInMemoryConfig())
                .endpoint(new HttpEndpointConfig())
                .assetConnection(new AssetConnectionConfig())
                .messageBus(new MessageBusInternalConfig())
                .build(); 
    }
}
