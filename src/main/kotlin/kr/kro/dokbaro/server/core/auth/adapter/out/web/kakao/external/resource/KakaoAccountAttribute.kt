package kr.kro.dokbaro.server.core.auth.adapter.out.web.kakao.external.resource

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.time.LocalDateTime

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