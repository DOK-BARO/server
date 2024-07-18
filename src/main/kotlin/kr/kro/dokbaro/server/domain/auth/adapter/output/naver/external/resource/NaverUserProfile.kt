package kr.kro.dokbaro.server.domain.auth.adapter.output.naver.external.resource

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class NaverUserProfile(
	val email: String?,
	val nickname: String?,
	val profileImage: String?,
	val age: String?,
	val gender: String?,
	val id: String,
	val name: String?,
	val birthday: String?,
	val birthyear: String?,
	val mobile: String?,
)