package store;

import controllers.CasUserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.cache.Cache;
import play.mvc.Http;

public class CacheSessionStore {
    private static final Logger logger = LoggerFactory.getLogger(CacheSessionStore.class);

    private final static String SEPARATOR = "$";
    private static final String CAS_SESSION_ID = "CasSessionId";

    // prefix for the cache
    private String prefix = "";

    // 1 hour = 3600 seconds
    private int profileTimeout = 3600;

    // 1 minute = 60 second
    private int sessionTimeout = 60;

    String getKey(final String sessionId, final String key) {
        return prefix + SEPARATOR + sessionId + SEPARATOR + key;
    }

    public String getOrCreateSessionId(final Http.Context context) {

        String sessionId = getExistingSessionId(context);
        logger.trace("retrieved sessionId: {}", sessionId);
        // if null, generate a new one
        if (sessionId == null) {
            // generate id for session
            sessionId = java.util.UUID.randomUUID().toString();
            logger.debug("generated sessionId: {}", sessionId);
            // and save it to session
            //context.args.put(CAS_SESSION_ID, sessionId);
            context.session().put(CAS_SESSION_ID, sessionId);
            context.response().setHeader(CAS_SESSION_ID, sessionId);
        }
        return sessionId;
    }

    private String getExistingSessionId(Http.Context context) {
        String sessionId = (String)context.args.get(CAS_SESSION_ID);

        if(sessionId == null) {
            final Http.Session session = context.session();
            // get current sessionId
            sessionId = session.get(CAS_SESSION_ID);
            if(sessionId == null) {
                String[] sessionIds = context.request().headers().get(CAS_SESSION_ID);
                if(sessionIds!=null && sessionIds.length == 1) {
                    sessionId = sessionIds[0];
                }
            }
        }
        return sessionId;
    }

    public Object get(final Http.Context context, final String key) {
        final String sessionId = getOrCreateSessionId(context);
        return Cache.get(getKey(sessionId, key));
    }

    public void set(final Http.Context context, final String key, final Object value) {
        int timeout;
        if (value instanceof CasUserProfile) {
            timeout = profileTimeout;
        } else {
            timeout = sessionTimeout;
        }
        final String sessionId = getOrCreateSessionId(context);
        Cache.set(getKey(sessionId, key), value, timeout);
    }

    public void remove(final Http.Context context, final  String key) {
        final String sessionId = getExistingSessionId(context);
        if(sessionId != null) {
            Cache.remove(getKey(sessionId,key));
        }
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public int getProfileTimeout() {
        return profileTimeout;
    }

    public void setProfileTimeout(int profileTimeout) {
        this.profileTimeout = profileTimeout;
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }
}
