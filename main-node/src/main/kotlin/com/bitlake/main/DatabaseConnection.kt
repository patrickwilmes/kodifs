package com.bitlake.main

import com.bitlake.commons.ConfigurationValue
import com.bitlake.commons.Value
import com.bitlake.commons.applyForConfigTriple
import com.bitlake.commons.configValue
import io.ktor.server.application.Application
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.MigrationInfoService
import org.jetbrains.exposed.sql.Database

fun Application.connectToDatabase() {
    applyForConfigTriple(
        {},
        { url, user, password ->
            Database.connect(
                url = url,
                user = user,
                password = password,
            )
        },
        configValue(ConfigurationValue.DatabaseUrl),
        configValue(ConfigurationValue.DatabaseUser),
        configValue(ConfigurationValue.DatabasePassword),
    )
}

fun Application.executeFlywayMigration() {
    val url = configValue(ConfigurationValue.DatabaseUrl) as Value.StringValue
    val user = configValue(ConfigurationValue.DatabaseUser) as Value.StringValue
    val password = configValue(ConfigurationValue.DatabasePassword) as Value.StringValue
    println("DB: $url, user: $user, password: $password")
    val flyway = Flyway.configure()
        .dataSource(url.value, user.value, password.value)
        .load()
    if (flyway != null) {
        with(flyway) {
            migrate()
        }
    }
}
