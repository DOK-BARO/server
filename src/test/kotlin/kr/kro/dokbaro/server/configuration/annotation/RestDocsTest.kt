package kr.kro.dokbaro.server.configuration.annotation

import kr.kro.dokbaro.server.configuration.security.TestSecurityConfig
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.context.annotation.Import
import org.springframework.security.test.context.support.WithUserDetails
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@WithUserDetails(value = "username", userDetailsServiceBeanName = "testUserDetailService")
@Import(TestSecurityConfig::class)
annotation class RestDocsTest