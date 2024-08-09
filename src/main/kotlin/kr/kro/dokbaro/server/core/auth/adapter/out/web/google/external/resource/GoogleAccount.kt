package kr.kro.dokbaro.server.core.auth.adapter.out.web.google.external.resource

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