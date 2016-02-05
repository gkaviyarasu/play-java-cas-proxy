package modules;

import com.google.inject.AbstractModule;
import play.Configuration;
import play.Environment;
import store.CacheSessionStore;

public class SecurityModule extends AbstractModule {

    private final Environment environment;
    private final Configuration configuration;

    public SecurityModule(Environment environment, Configuration configuration) {
        this.environment = environment;
        this.configuration = configuration;
    }

    @Override
    protected void configure() {
        bind(CacheSessionStore.class).toInstance(new CacheSessionStore());
    }
}
