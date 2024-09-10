package kr.kro.dokbaro.server.core.bookquiz.domain

data class QuizQuestion(
	val content: String,
	val explanation: String,
	val answer: Answerable,
	val answerOptions: List<AnswerOption> = listOf(), // 답안 선택지
	val id: Long = UNSAVED_QUIZ_QUESTION_ID,
) {
	companion object {
		private const val UNSAVED_QUIZ_QUESTION_ID = 0L
	}

	fun getQuizType(): QuizType = answer.getType()
}