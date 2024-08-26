package kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.web.naver.external.resource

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class NaverAccount(
	val resultcode: String,
	val message: String,
	val response: NaverUserProfile,
)

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