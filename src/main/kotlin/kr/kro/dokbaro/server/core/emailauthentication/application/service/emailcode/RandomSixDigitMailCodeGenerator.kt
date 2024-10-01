package kr.kro.dokbaro.server.core.emailauthentication.application.service.emailcode

import kr.kro.dokbaro.server.core.emailauthentication.application.service.EmailCodeGenerator
import org.springframework.stereotype.Component

@Component
class RandomSixDigitMailCodeGenerator : EmailCodeGenerator {
	override fun generate(): String =
		(1..6)
			.map { "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".random() }
			.joinToString("")
}