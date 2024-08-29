package kr.kro.dokbaro.server.configuration.security

import kr.kro.dokbaro.server.core.member.domain.Role
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService

class TestUserDetailService : UserDetailsService {
	override fun loadUserByUsername(username: String): UserDetails =
		User(
			"550e8400-e29b-41d4-a716-446655440000",
			"password",
			Role.entries.map { SimpleGrantedAuthority(it.name) }.toList(),
		)
}