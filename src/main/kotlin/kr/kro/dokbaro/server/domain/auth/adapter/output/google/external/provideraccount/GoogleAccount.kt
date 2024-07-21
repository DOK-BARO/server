package kr.kro.dokbaro.server.domain.auth.adapter.output.google.external.provideraccount

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class GoogleAccount(
	val id: String,
	val email: String?,
	val verifiedEmail: Boolean?,
	val name: String?,
	val givenName: String?,
	val familyName: String?,
	val picture: String?,
)