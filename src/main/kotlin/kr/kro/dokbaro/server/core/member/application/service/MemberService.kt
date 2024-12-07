package kr.kro.dokbaro.server.core.member.application.service

import kr.kro.dokbaro.server.core.member.application.port.input.command.ModifyMemberUseCase
import kr.kro.dokbaro.server.core.member.application.port.input.command.RegisterMemberUseCase
import kr.kro.dokbaro.server.core.member.application.port.input.command.WithdrawMemberUseCase
import kr.kro.dokbaro.server.core.member.application.port.input.command.dto.ModifyMemberCommand
import kr.kro.dokbaro.server.core.member.application.port.input.command.dto.RegisterMemberCommand
import kr.kro.dokbaro.server.core.member.application.port.out.ExistMemberByEmailPort
import kr.kro.dokbaro.server.core.member.application.port.out.InsertMemberPort
import kr.kro.dokbaro.server.core.member.application.port.out.UpdateMemberPort
import kr.kro.dokbaro.server.core.member.domain.Email
import kr.kro.dokbaro.server.core.member.domain.Member
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class MemberService(
	private val insertMemberPort: InsertMemberPort,
	private val updateMemberPort: UpdateMemberPort,
	private val existMemberEmailPort: ExistMemberByEmailPort,
) : RegisterMemberUseCase,
	ModifyMemberUseCase,
	WithdrawMemberUseCase {
	override fun register(command: RegisterMemberCommand): Member {
		val certificationId = UUID.randomUUID()

		if (existMemberEmailPort.existByEmail(command.email)) {
			throw AlreadyRegisteredEmailException(command.email)
		}

		return insertMemberPort.insert(
			Member(
				nickname = command.nickname,
				email = Email(command.email),
				profileImage = command.profileImage,
				certificationId = certificationId,
			),
		)
	}

	override fun modify(command: ModifyMemberCommand) {
		val member: Member =
			loadMemberPort.findBy(command.certificationId)
				?: throw NotFoundMemberException()

		member.modify(
			nickName = command.nickname,
			email = command.email?.let { Email(it) },
			profileImage = command.profileImage,
		)

		updateMemberPort.update(member)
	}

	override fun withdrawBy(authId: UUID) {
		val member: Member =
			loadMemberPort.findBy(authId)
				?: throw NotFoundMemberException()

		member.withdraw()

		updateMemberPort.update(member)
	}
}