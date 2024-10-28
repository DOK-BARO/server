package kr.kro.dokbaro.server.core.bookquiz.domain

data class QuestionAnswer(
	val explanationContent: String,
	// 추가 설명 이미지
	val explanationImages: Collection<String> = emptyList(),
	val gradeSheet: Gradable,
) {
	fun correctAnswer() = gradeSheet.getAnswers()
}