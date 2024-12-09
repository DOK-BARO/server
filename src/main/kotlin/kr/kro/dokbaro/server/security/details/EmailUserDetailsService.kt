package kr.kro.dokbaro.server.security.details

import kr.kro.dokbaro.server.core.member.application.port.input.query.FindEmailAuthenticationMemberUseCase
import kr.kro.dokbaro.server.core.member.query.EmailAuthenticationMember
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class EmailUserDetailsService(
	private val findEmailAuthenticationMemberUseCase: FindEmailAuthenticationMemberUseCase,
) : UserDetailsService {
	override fun loadUserByUsername(username: String): UserDetails {
		val member: EmailAuthenticationMember =
			findEmailAuthenticationMemberUseCase.findEmailAuthenticationMember(
				email = username,
			) ?: throw NotFoundEmailUserException()

		return DokbaroUser(
			id = member.id,
			certificationId = member.certificationId,
			nickname = member.nickname,
			email = member.email,
			role = member.role,
			password = member.password,
		)
	}
}