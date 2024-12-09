package kr.kro.dokbaro.server.configuration.security

import kr.kro.dokbaro.server.security.details.DokbaroUser
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import java.util.UUID

class TestUserDetailService : UserDetailsService {
	override fun loadUserByUsername(username: String): UserDetails =
		DokbaroUser(
			id = 1,
			certificationId = UUID.randomUUID(),
			nickname = "test",
			email = "test@test.com",
			role = listOf("GUEST"),
		)
}