package kr.kro.dokbaro.server.core.member.application.service

import kr.kro.dokbaro.server.core.member.application.port.input.query.FindCertificatedMemberUseCase
import kr.kro.dokbaro.server.core.member.application.port.input.query.FindCertificationIdByEmailUserCase
import kr.kro.dokbaro.server.core.member.application.port.input.query.FindEmailAuthenticationMemberUseCase
import kr.kro.dokbaro.server.core.member.application.port.input.query.FindMyAvatarUseCase
import kr.kro.dokbaro.server.core.member.application.port.out.LoadMemberByCertificationIdPort
import kr.kro.dokbaro.server.core.member.application.port.out.ReadCertificatedMemberPort
import kr.kro.dokbaro.server.core.member.application.port.out.ReadCertificationIdByEmailPort
import kr.kro.dokbaro.server.core.member.application.port.out.ReadEmailAuthenticationMemberPort
import kr.kro.dokbaro.server.core.member.application.service.exception.NotFoundCertificationMemberException
import kr.kro.dokbaro.server.core.member.application.service.exception.NotFoundMemberException
import kr.kro.dokbaro.server.core.member.query.CertificatedMember
import kr.kro.dokbaro.server.core.member.query.EmailAuthenticationMember
import kr.kro.dokbaro.server.core.member.query.MyAvatar
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class MemberQueryService(
	private val readEmailAuthenticationMemberPort: ReadEmailAuthenticationMemberPort,
	private val readCertificatedMemberPort: ReadCertificatedMemberPort,
	private val readCertificationIdByEmailPort: ReadCertificationIdByEmailPort,
	private val loadMemberByCertificationIdPort: LoadMemberByCertificationIdPort,
) : FindEmailAuthenticationMemberUseCase,
	FindCertificatedMemberUseCase,
	FindCertificationIdByEmailUserCase,
	FindMyAvatarUseCase {
	override fun findEmailAuthenticationMember(email: String): EmailAuthenticationMember? =
		readEmailAuthenticationMemberPort.findEmailAuthenticationMember(email)

	override fun findCertificationMember(certificationId: UUID): CertificatedMember =
		readCertificatedMemberPort.findCertificatedMember(certificationId)
			?: throw NotFoundCertificationMemberException()

	override fun findCertificationIdByEmail(email: String): UUID? =
		readCertificationIdByEmailPort.findCertificationIdByEmail(email)

	override fun findMyAvatar(certificationId: UUID): MyAvatar {
		val member =
			loadMemberByCertificationIdPort.findMemberByCertificationId(certificationId)
				?: throw NotFoundMemberException()

		return MyAvatar(
			id = member.id,
			certificationId = member.certificationId,
			nickname = member.nickname,
			email = member.email.address,
			profileImage = member.profileImage,
			role = member.roles.map { it.name },
		)
	}
}