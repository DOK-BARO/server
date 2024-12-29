package kr.kro.dokbaro.server.core.account.application.service

import kr.kro.dokbaro.server.core.account.application.port.input.RegisterSocialAccountUseCase
import kr.kro.dokbaro.server.core.account.application.port.input.dto.RegisterSocialAccountCommand
import kr.kro.dokbaro.server.core.account.application.port.out.InsertSocialAccountPort
import kr.kro.dokbaro.server.core.account.domain.SocialAccount
import kr.kro.dokbaro.server.core.member.application.port.input.command.RegisterMemberUseCase
import kr.kro.dokbaro.server.core.member.application.port.input.command.dto.RegisterMemberCommand
import kr.kro.dokbaro.server.core.member.domain.AccountType
import kr.kro.dokbaro.server.core.member.domain.Member
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class SocialAccountService(
	private val insertSocialAccountPort: InsertSocialAccountPort,
	private val registerMemberUseCase: RegisterMemberUseCase,
) : RegisterSocialAccountUseCase {
	override fun registerSocialAccount(command: RegisterSocialAccountCommand): UUID {
		val member: Member =
			registerMemberUseCase.register(
				RegisterMemberCommand(
					nickname = command.nickname,
					email = command.email,
					profileImage = command.profileImage,
					accountType = AccountType.SOCIAL,
				),
			)

		insertSocialAccountPort.insertSocialAccount(
			SocialAccount(
				socialId = command.socialId,
				provider = command.provider,
				memberId = member.id,
			),
		)

		return member.certificationId
	}
}