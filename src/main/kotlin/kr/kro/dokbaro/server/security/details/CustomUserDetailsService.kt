package kr.kro.dokbaro.server.security.details

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService : UserDetailsService {
	override fun loadUserByUsername(username: String?): UserDetails {
		TODO("Not yet implemented")
	}
}