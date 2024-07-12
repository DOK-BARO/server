package kr.kro.dokbaro.server.domain.auth.adapter.output.kakao.external.provideraccount

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class KakaoAttributeProfile(
	val nickname: String?,
	val thumbnailImageUrl: String?,
	val profileImageUrl: String?,
	val isDefaultImage: Boolean?,
)