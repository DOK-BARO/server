package kr.kro.dokbaro.server.configuration.security

import kr.kro.dokbaro.server.domain.account.model.Role
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService

class TestUserDetailService : UserDetailsService {
	override fun loadUserByUsername(username: String): UserDetails =
		User(
			"username",
			"password",
			Role.entries.map { SimpleGrantedAuthority(it.name) }.toList(),
		)
}