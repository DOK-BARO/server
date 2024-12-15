package kr.kro.dokbaro.server.security.details

import kr.kro.dokbaro.server.core.member.domain.Role
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
	private val password: String? = null,
) : UserDetails {
	override fun getAuthorities(): Collection<GrantedAuthority> = role.map { SimpleGrantedAuthority(it) }

	override fun getPassword(): String? = password

	override fun getUsername(): String = email

	fun hasRole(role: Role): Boolean = this.role.contains(role.name)
}