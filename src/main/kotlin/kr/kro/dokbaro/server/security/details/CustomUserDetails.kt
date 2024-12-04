package kr.kro.dokbaro.server.security.details

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class CustomUserDetails(
	val email: String,
) : UserDetails {
	override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
		TODO("Not yet implemented")
	}

	override fun getPassword(): String {
		TODO("Not yet implemented")
	}

	override fun getUsername(): String {
		TODO("Not yet implemented")
	}
}