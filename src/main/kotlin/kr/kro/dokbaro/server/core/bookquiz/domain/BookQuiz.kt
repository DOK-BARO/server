package kr.kro.dokbaro.server.core.bookquiz.domain

data class BookQuiz(
	val title: String,
	val description: String,
	val bookId: Long,
	val creatorMemberId: Long,
	val id: Long = UNSAVED_BOOK_QUIZ_ID,
) {
	companion object {
		private const val UNSAVED_BOOK_QUIZ_ID = 0L
	}
}