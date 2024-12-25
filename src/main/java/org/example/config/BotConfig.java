package org.example.config;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.db.MapDBContext;

@Configuration
public class BotConfig {

    @Bean
    public DB mapDb() {
        return DBMaker.memoryDB().make();
    }

    @Bean
    public DBContext dbContext(DB mapDb) {
        return new MapDBContext(mapDb);
    }
}
