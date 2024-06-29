package kr.kro.dokbaro.server.configuration.security

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class TestSecurityConfig {
	@Bean
	fun testUserDetailService(): TestUserDetailService = TestUserDetailService()
}