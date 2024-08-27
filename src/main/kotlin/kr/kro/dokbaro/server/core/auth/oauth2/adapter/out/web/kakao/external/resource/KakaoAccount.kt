package kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.web.kakao.external.resource

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

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class KakaoAccountAttribute(
	val profileNeedsAgreement: Boolean,
	val profileNicknameNeedsAgreement: Boolean,
	val profileImageNeedsAgreement: Boolean,
	val profile: KakaoAttributeProfile,
	val nameNeedsAgreement: Boolean,
	val name: String?,
	val emailNeedsAgreement: Boolean,
	val isEmailValid: Boolean,
	val isEmailVerified: Boolean,
	val email: String?,
	val ageRangeNeedsAgreement: Boolean,
	val ageRange: String?,
	val birthyearNeedsAgreement: Boolean,
	val birthyear: String?,
	val birthdayNeedsAgreement: Boolean,
	val birthday: String?,
	val birthdayType: String?,
	val genderNeedsAgreement: Boolean,
	val gender: String?,
	val phoneNumberNeedsAgreement: Boolean,
	val phoneNumber: String?,
	val ciNeedsAgreement: Boolean,
	val ci: String?,
	val ciAuthenticatedAt: LocalDateTime?,
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class KakaoAttributeProfile(
	val nickname: String?,
	val thumbnailImageUrl: String?,
	val profileImageUrl: String?,
	val isDefaultImage: Boolean?,
)