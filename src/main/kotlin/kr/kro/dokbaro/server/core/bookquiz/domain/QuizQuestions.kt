package kr.kro.dokbaro.server.core.bookquiz.domain

data class QuizQuestions(
	private val questions: MutableList<QuizQuestion> = mutableListOf(),
) {
	fun getQuestions(): List<QuizQuestion> = questions.toList()

	fun updateQuestions(newQuestions: Collection<QuizQuestion>) {
	}
}