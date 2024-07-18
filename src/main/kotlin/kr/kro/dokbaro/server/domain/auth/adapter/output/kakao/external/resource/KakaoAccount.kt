package kr.kro.dokbaro.server.domain.auth.adapter.output.kakao.external.resource

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.time.LocalDateTime

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class KakaoAccount(
	val id: Long,
	val hasSignedUp: Boolean,
	val connectedAt: LocalDateTime,
	val kakaoAccount: KakaoAccountAttribute,
)