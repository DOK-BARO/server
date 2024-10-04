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
	EMAIL_ALARM(
		3,
		"신규 퀴즈 소식을 이메일로 받기",
		"관심도서의 신규 퀴즈 업데이트 소식을 받아보세요!",
		false,
		false,
	),
	EMAIL_ADVERTISE(
		4,
		"DOKBARO의 광고 메시지를 이메일로 받기",
		"DOKBARO가 고른 도서 소식을 이메일로 받아보세요!",
		false,
		false,
	),
}