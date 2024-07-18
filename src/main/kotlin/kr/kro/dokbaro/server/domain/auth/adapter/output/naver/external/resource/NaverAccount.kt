package kr.kro.dokbaro.server.domain.auth.adapter.output.naver.external.resource

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class NaverAccount(
	val resultcode: String,
	val message: String,
	val response: NaverUserProfile,
)