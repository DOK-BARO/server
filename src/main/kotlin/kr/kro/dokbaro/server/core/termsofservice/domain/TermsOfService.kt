package kr.kro.dokbaro.server.core.termsofservice.domain

enum class TermsOfService(
	val id: Long,
	val title: String,
	val subTitle: String?,
	val hasDetail: Boolean,
	val primary: Boolean,
) {
	TERMS_OF_SERVICE(
		id = 1,
		title = "이용 약관 동의",
		subTitle = null,
		hasDetail = true,
		primary = true,
	),
	PERSONAL_INFORMATION(
		id = 2,
		title = "개인 정보 수집 및 이용 동의",
		subTitle = null,
		hasDetail = true,
		primary = true,
	),
	EMAIL_ADVERTISE(
		id = 3,
		title = "DOKBARO의 광고 메시지를 이메일로 받기",
		subTitle = "DOKBARO가 고른 혜택과 소식을 이메일로 받아보세요!",
		hasDetail = false,
		primary = false,
	),
}