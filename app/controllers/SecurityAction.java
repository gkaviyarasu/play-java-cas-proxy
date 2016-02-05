package controllers;

import com.google.inject.Inject;
import controllers.annotation.Authenticated;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.Cas30ProxyTicketValidator;
import org.jasig.cas.client.validation.TicketValidationException;
import play.Configuration;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import store.CacheSessionStore;

import javax.net.ssl.*;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;

public class SecurityAction extends Action<Authenticated> {

    @Inject
    Configuration config;

    @Inject
    CacheSessionStore sessionStore;

    //To test with local server which is enabled with self signed certificate,
    // uncomment the below constructor to ignore the invalid certificate error
    public SecurityAction() throws GeneralSecurityException {
        super();
        acceptSsl();
    }

    public Configuration getConfig() {
        return config;
    }

    public void setConfig(Configuration config) {
        this.config = config;
    }

    @Override
    public F.Promise<Result> call(Http.Context ctx) throws Throwable {
        CasUserProfile casUserProfile = (CasUserProfile) sessionStore.get(ctx, "userProfile");

        if(casUserProfile != null) {
            return delegate.call(ctx);
        }
        String ticket = ctx.request().getQueryString("ticket");

        if (ticket == null || ticket.isEmpty()) {
            return F.Promise.pure(unauthorized());
        } else {
            String casUrl = config.getString("casUrl");
            String appService = config.getString("appService");
            Cas30ProxyTicketValidator proxyTicketValidator = new Cas30ProxyTicketValidator(casUrl);
            proxyTicketValidator.setAcceptAnyProxy(true);
            try {
                Assertion response = proxyTicketValidator.validate(ticket, appService);
                String user = response.getPrincipal().getName();
                casUserProfile = new CasUserProfile();
                casUserProfile.setEmail((String) response.getPrincipal().getAttributes().get("mail"));
                casUserProfile.setEmployeeId((String) response.getPrincipal().getAttributes().get("employeenumber"));
                casUserProfile.setUser(user);
                casUserProfile.setUuid((String) response.getPrincipal().getAttributes().get("uuid"));
                sessionStore.set(ctx, "userProfile", casUserProfile);

            } catch (TicketValidationException e) {
                return F.Promise.pure(unauthorized());
            }
        }
        return delegate.call(ctx);
    }

    private void acceptSsl() throws GeneralSecurityException {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
        };

        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Create all-trusting host name verifier
        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier((String hostname, SSLSession session) -> true);
    }
}
