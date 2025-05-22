package com.cspl.common.gen_ai.speechaiengine.constants;


/**
 * @author: vineet.rajput
 * @use:
 **/
public class AppConstants {

    private AppConstants() {
        throw new AssertionError("Suppress default constructor for non instantiability");
    }

    public static final String AI_ADAPTOR_SUFFIX = "_ADAPTOR";

    public static final String SYSTEM_VALUE = "SYSTEM";

    public static final String D_DAILY_START_TIME = "$dailyStartTime";

    public static final String D_DAILY_STOP_TIME = "$dailyStopTime";

    public static final String D_AND = "$and";

    public static final String D_OR = "$or";

    public static final String D_EXPR = "$expr";

    public static final String D_GREATER_THAN = "$gt";

    public static final String D_LESSER_THAN = "$lt";

    public static final String START_DATE_TIME = "startDateTime";

    public static final String END_DATE_TIME = "endDateTime";

    public static final String DAILY_START_TIME = "dailyStartTime";

    public static final String DAILY_STOP_TIME = "dailyStopTime";

    public static final String REQUEST_KEY = "request";

    public static final String RESPONSE_KEY = "response";

    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public static final String KEY_SEPARATOR = "_";

    public static final String EVENT = "event";

    public static final String ONGOING_CONCURRENT_CALLS_SIZE = "ONGOING_CONCURRENT_CALLS_SIZE";

    public static final String START = "start";

    public static final String MEDIA = "media";

    public static final String PAYLOAD = "payload";

    public static final String TYPE = "type";

    public static final String TRANSCRIPT = "transcript";

    public static final String CALL_RECORDING_QUEUE = "callRecordQueue";

    public static final String PLIVO_CALL_SID = "callId";

    public static final String PLIVO_STREAM_ID = "streamId";

    public static final String EVENT_STATUS = "eventStatus";

    public static final String TEXT = "text";

    public static final String ACTIONS = "actions";

    public static final String XI_API_KEY = "xi-api-key";

    public static final String MESSAGE = "message";

    public static final String FEEDBACK = "feedback";

    public static final String TOOL_RESULTS = "tool_results";

    public static final String TOOL_CALLS = "tool_calls";

    public static final String ROLE = "role";

    public static final String AGENT = "agent";

    public static final String USER = "user";

    public static final String TWILIO_STREAM_SID = "streamSid";

    public static final String TWILIO_CALL_SID = "callSid";

    public static class KEY {

        private KEY() {
        }
    }

    public static final String API = "/api";
    public static final String V1 = "/v1";
    public static final String TENANT_ID = "x-tenant-id";
}
