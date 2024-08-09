package kr.kro.dokbaro.server.core.auth.adapter.out.web.kakao.external.resource

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class KakaoAttributeProfile(
	val nickname: String?,
	val thumbnailImageUrl: String?,
	val profileImageUrl: String?,
	val isDefaultImage: Boolean?,
)