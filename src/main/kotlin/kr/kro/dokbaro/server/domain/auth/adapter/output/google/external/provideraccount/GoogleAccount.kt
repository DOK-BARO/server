package kr.kro.dokbaro.server.domain.auth.adapter.output.google.external.provideraccount

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class GoogleAccount(
	val atHash: String?,
	val sub: String,
	val emailVerified: Boolean?,
	val iss: String?,
	val givenName: String?,
	val locale: String?,
	val nonce: String?,
	val picture: String?,
	val aud: List<String>?,
	val azp: String?,
	val name: String?,
	val xp: String?,
	val familyName: String?,
	val iat: String?,
	val email: String?,
)