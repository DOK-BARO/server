package kr.kro.dokbaro.server.core.auth.email.application.service

import kr.kro.dokbaro.server.common.util.UUIDUtils
import kr.kro.dokbaro.server.core.auth.email.application.port.input.EmailLoginUseCase
import kr.kro.dokbaro.server.core.auth.email.application.port.input.dto.EmailLoginCommand
import kr.kro.dokbaro.server.core.auth.email.application.port.out.LoadEmailCertificatedAccountPort
import kr.kro.dokbaro.server.core.auth.email.domain.EmailCertificatedAccount
import kr.kro.dokbaro.server.core.token.application.port.input.GenerateAuthTokenUseCase
import kr.kro.dokbaro.server.core.token.domain.AuthToken
import kr.kro.dokbaro.server.core.token.domain.access.jwt.TokenClaims
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class EmailLoginService(
	private val loadEmailCertificatedAccountPort: LoadEmailCertificatedAccountPort,
	private val generateAuthTokenUseCase: GenerateAuthTokenUseCase,
	private val passwordEncoder: PasswordEncoder,
) : EmailLoginUseCase {
	override fun login(command: EmailLoginCommand): AuthToken {
		val account: EmailCertificatedAccount =
			loadEmailCertificatedAccountPort.findByEmail(command.email)
				?: throw NotFoundEmailCertificatedAccountException(command.email)

		if (!passwordEncoder.matches(command.password, account.password)) {
			throw PasswordNotMatchException()
		}

		return generateAuthTokenUseCase.generate(
			TokenClaims(
				UUIDUtils.uuidToString(account.certificationId),
				account.role,
			),
		)
	}
}