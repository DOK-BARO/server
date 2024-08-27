package kr.kro.dokbaro.server.core.member.application.service

import kr.kro.dokbaro.server.core.member.application.port.input.command.CheckMemberEmailUseCase
import kr.kro.dokbaro.server.core.member.application.port.input.command.RegisterMemberUseCase
import kr.kro.dokbaro.server.core.member.application.port.input.dto.RegisterMemberCommand
import kr.kro.dokbaro.server.core.member.application.port.out.ExistMemberByEmailPort
import kr.kro.dokbaro.server.core.member.application.port.out.LoadMemberByCertificationIdPort
import kr.kro.dokbaro.server.core.member.application.port.out.SaveMemberPort
import kr.kro.dokbaro.server.core.member.application.port.out.UpdateMemberPort
import kr.kro.dokbaro.server.core.member.domain.Email
import kr.kro.dokbaro.server.core.member.domain.Member
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class MemberService(
	private val saveMemberPort: SaveMemberPort,
	private val updateMemberPort: UpdateMemberPort,
	private val existMemberEmailPort: ExistMemberByEmailPort,
	private val loadMemberByCertificationIdPort: LoadMemberByCertificationIdPort,
) : RegisterMemberUseCase,
	CheckMemberEmailUseCase {
	override fun register(command: RegisterMemberCommand): Member {
		val certificationId = UUID.randomUUID()

		if (existMemberEmailPort.existByEmail(command.email)) {
			throw AlreadyRegisteredEmailException(command.email)
		}

		return saveMemberPort.save(
			Member(
				command.nickName,
				Email(command.email),
				command.profileImage,
				certificationId,
			),
		)
	}

	override fun checkMail(certificationId: UUID) {
		val member: Member =
			loadMemberByCertificationIdPort.findByCertificationId(certificationId) ?: throw NotFoundMemberException()

		member.checkEmail()
		updateMemberPort.update(member)
	}
}