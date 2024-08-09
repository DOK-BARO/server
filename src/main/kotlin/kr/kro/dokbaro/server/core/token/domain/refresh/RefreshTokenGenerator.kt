package kr.kro.dokbaro.server.core.token.domain.refresh

import org.springframework.stereotype.Component
import java.util.UUID

@Component
class RefreshTokenGenerator {
	fun generate(accountId: String): String = UUID.randomUUID().toString()
}