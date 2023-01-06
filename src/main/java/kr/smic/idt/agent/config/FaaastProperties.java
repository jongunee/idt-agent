package kr.smic.idt.agent.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "faaast")
public class FaaastProperties {

    private final CoreProperties core = new CoreProperties();

    private final ModelProperties model = new ModelProperties();

    private final EndpointProperties endpoint = new EndpointProperties();
    private final AssetConnectionProperties assetconnection = new AssetConnectionProperties();

    @Data
    public static class CoreProperties {
        private int requestHandlerThreadPoolSize = 1;
    }

    @Data
    public static class ModelProperties {
        private String path = "./aas.json";

        private Boolean decoupled = true;
    }

    @Data
    public static class EndpointProperties {

        private final HttpEndpointProperties http = new HttpEndpointProperties();

        private final OpcuaEndpointProperties opcua = new OpcuaEndpointProperties();

        @Data
        public static class HttpEndpointProperties {
            private Boolean enabled = true;

            private int port = 8080;
        }

        @Data
        public static class OpcuaEndpointProperties {
            private Boolean enabled = true;

            private int tcpPort = 8081;

            private int secondsTillShutdown = 8081;
        }


    }
    @Data
    public static class AssetConnectionProperties {

        private final OpcuaAssetConnectionProperties opcuaasset = new OpcuaAssetConnectionProperties();

        @Data
        public static class OpcuaAssetConnectionProperties {
            private Boolean enabled = true;

            private String host = "opc.tcp://jw-Mac.local:53530/OPCUA/SimulationServer";
        }
    }

}
