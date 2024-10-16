package kr.kro.dokbaro.server.core.bookquiz.query

data class BookQuizAnswer(
	val correctAnswer: Collection<String>,
	val explanation: String,
)