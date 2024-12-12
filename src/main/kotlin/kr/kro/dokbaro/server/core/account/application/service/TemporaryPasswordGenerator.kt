package kr.kro.dokbaro.server.core.account.application.service

import org.springframework.stereotype.Component

@Component
class TemporaryPasswordGenerator {
	fun generate(): String {
		val chars = ('A'..'Z') + ('a'..'z') + ('0'..'9')

		return (1..9).map { chars.random() }.joinToString("")
	}
}