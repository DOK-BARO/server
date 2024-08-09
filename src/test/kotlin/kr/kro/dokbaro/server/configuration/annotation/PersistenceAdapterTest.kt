package kr.kro.dokbaro.server.configuration.annotation

import kr.kro.dokbaro.server.configuration.TestcontainersConfiguration
import org.springframework.boot.test.autoconfigure.jooq.JooqTest
import org.springframework.context.annotation.Import
import org.springframework.stereotype.Component

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Component
@JooqTest
@Import(TestcontainersConfiguration::class)
annotation class PersistenceAdapterTest