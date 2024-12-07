package kr.kro.dokbaro.server.security.details

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.UUID

data class DokbaroUser(
	val id: Long,
	val certificationId: UUID,
	val nickname: String,
	val email: String,
	val role: Collection<String>,
	val password: String? = null,
) : UserDetails {
	override fun getAuthorities(): Collection<GrantedAuthority> = role.map { SimpleGrantedAuthority(it) }

	override fun getPassword(): String? = password

	override fun getUsername(): String = email
}