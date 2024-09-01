package kr.kro.dokbaro.server.core.emailauthentication.application.service.emailcode

import kr.kro.dokbaro.server.core.emailauthentication.application.service.EmailCodeGenerator
import org.springframework.stereotype.Component

@Component
class RandomSixDigitMailCodeGenerator : EmailCodeGenerator {
	override fun generate(): String {
		val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"

		return (1..6)
			.map { chars.random() }
			.joinToString("")
	}
}