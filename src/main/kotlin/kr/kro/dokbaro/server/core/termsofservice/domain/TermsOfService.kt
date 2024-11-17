package kr.kro.dokbaro.server.core.termsofservice.domain

enum class TermsOfService(
	val id: Long,
	val title: String,
	val subTitle: String?,
	val hasDetail: Boolean,
	val primary: Boolean,
) {
	TERMS_OF_SERVICE(
		1,
		"이용 약관 동의",
		null,
		true,
		true,
	),
	PERSONAL_INFORMATION(
		2,
		"개인 정보 수집 및 이용 동의",
		null,
		true,
		true,
	),
	EMAIL_ADVERTISE(
		3,
		"DOKBARO의 광고 메시지를 이메일로 받기",
		"DOKBARO가 고른 혜택과 소식을 이메일로 받아보세요!",
		false,
		false,
	),
}