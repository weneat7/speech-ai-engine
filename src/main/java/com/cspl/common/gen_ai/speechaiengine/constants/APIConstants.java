package com.cspl.common.gen_ai.speechaiengine.constants;

/**
 * @author: vineet.rajput
 * @use:
 **/
public class APIConstants {

    public static final String CREATE = "/create";

    public static final String UPLOAD = "/upload";

    private APIConstants(){}

    public static final String BASE_URL = "/api";

    public static final String HEALTH_URL = "/public/health";

    public static final String FULFILLMENT_URL = "/fulfillment";

    public static final String EXOTEL_CALLBACK_URL = "/exotel/callback";

    public static final String EXOTEL_WS_URL = "/ws";

    public static final String TWILIO_WS_URL = "/twilio";

    public static final String ALL_PATTERNS = "*";

    public static final String PLIVO_WS_URL = "/plivo";

    public static final String PLIVO_URL = "/plivo";

    public static final String TWILIO_URL = "/twilio";

    public static final String RECORDING = "/recording";

    public static final String ANSWER = "/answer";

    public static final String HANGUP = "/hangup";

    public static final String CONFIG = "/config";

    public static final String AGENT = "/agent";

    public static final String CAMPAIGN = "/campaign";

    public static final String EVENT_RECORD = "/event-records";

    public static final String EVENT_LEADS = "/event-leads";

    public static final String RULE = "/rule";

    public static final String CONVERSATIONS = "/conversations";

    public static class API_VERSION {
        private API_VERSION(){}
        public static final String V1 = "/v1";
    }

    public static class HEADERS {
        private HEADERS(){}
        public static final String X_CHANNEL_NAME = "x-channel-name";

        public static final String X_TEMPLATE_NAME = "x-template-name";

        public static final String X_EXECUTE_TYPE = "x-execute-type";

        public static final String X_AGENT_TYPE = "x-agent-type";

        public static final String X_REQUEST_ID = "x-request-id";

        public static final String X_PUSH_ALLOWED = "x-push-allowed";

        public static final String X_API_KEY = "x-api-key";
    }

    public static class REQUEST_KEYS{
        private REQUEST_KEYS(){}
        public static final String AUTH_KEY = "auth-key";

        public static final String CACHE_KEY = "cache-key";

        public static final String UPDATE_KEY = "update-key";
    }
}
