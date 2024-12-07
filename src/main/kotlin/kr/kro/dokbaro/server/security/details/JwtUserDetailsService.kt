package kr.kro.dokbaro.server.security.details

import kr.kro.dokbaro.server.common.util.UUIDUtils
import kr.kro.dokbaro.server.core.member.application.port.input.query.FindCertificatedMemberUseCase
import kr.kro.dokbaro.server.core.member.query.CertificatedMember
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class JwtUserDetailsService(
	private val findCertificatedMemberUseCase: FindCertificatedMemberUseCase,
) : UserDetailsService {
	override fun loadUserByUsername(username: String): UserDetails {
		val member: CertificatedMember =
			findCertificatedMemberUseCase
				.findCertificationMember(certificationId = UUIDUtils.stringToUUID(username))

		return DokbaroUser(
			id = member.id,
			certificationId = member.certificationId,
			nickname = member.nickname,
			email = member.email,
			role = member.role,
		)
	}
}