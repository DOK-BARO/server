package kr.kro.dokbaro.server.core.member.application.service

import kr.kro.dokbaro.server.core.member.application.port.input.command.RegisterMemberUseCase
import kr.kro.dokbaro.server.core.member.application.port.input.dto.RegisterMemberCommand
import kr.kro.dokbaro.server.core.member.application.port.out.SaveMemberPort
import kr.kro.dokbaro.server.core.member.domain.EmailAddress
import kr.kro.dokbaro.server.core.member.domain.Emails
import kr.kro.dokbaro.server.core.member.domain.Member
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class MemberService(
	private val saveMemberPort: SaveMemberPort,
) : RegisterMemberUseCase {
	override fun register(command: RegisterMemberCommand) {
		val certificationId = UUID.randomUUID()

		saveMemberPort.save(
			Member(
				command.name,
				command.nickName,
				command.email?.let { Emails(EmailAddress(it)) },
				certificationId,
			),
		)
	}
}