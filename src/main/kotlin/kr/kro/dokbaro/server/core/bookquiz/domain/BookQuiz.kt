package kr.kro.dokbaro.server.core.bookquiz.domain

import kr.kro.dokbaro.server.common.constant.Constants

data class BookQuiz(
	var title: String,
	var description: String,
	var bookId: Long,
	val creatorId: Long,
	val questions: QuizQuestions,
	val studyGroups: MutableSet<Long> = mutableSetOf(),
	var timeLimitSecond: Int? = null,
	var viewScope: AccessScope = AccessScope.EVERYONE,
	var editScope: AccessScope = AccessScope.CREATOR,
	val id: Long = Constants.UNSAVED_ID,
) {
	fun updateBasicOption(
		title: String,
		description: String,
		bookId: Long,
		timeLimitSecond: Int?,
		viewScope: AccessScope,
		editScope: AccessScope,
	) {
		this.title = title
		this.description = description
		this.bookId = bookId
		this.timeLimitSecond = timeLimitSecond
		this.viewScope = viewScope
		this.editScope = editScope
	}

	fun updateStudyGroups(newGroups: Collection<Long>) {
		studyGroups.clear()
		studyGroups.addAll(newGroups)
	}

	fun updateQuestions() {
	}
}