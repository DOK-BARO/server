package kr.kro.dokbaro.server.core.emailauthentication.application.port.input

import kr.kro.dokbaro.server.core.emailauthentication.application.port.input.dto.MatchResponse

fun interface MatchCodeUseCase {
	fun match(
		email: String,
		code: String,
	): MatchResponse
}