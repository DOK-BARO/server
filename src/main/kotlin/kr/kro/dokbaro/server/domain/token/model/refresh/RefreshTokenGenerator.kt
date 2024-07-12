package kr.kro.dokbaro.server.domain.token.model.refresh

import org.springframework.stereotype.Component
import java.util.UUID

@Component
class RefreshTokenGenerator {
	fun generate(accountId: String): String = UUID.randomUUID().toString()
}