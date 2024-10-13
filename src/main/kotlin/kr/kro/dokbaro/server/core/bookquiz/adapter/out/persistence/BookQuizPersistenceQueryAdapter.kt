package kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence

import kr.kro.dokbaro.server.common.annotation.PersistenceAdapter
import kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence.repository.jooq.BookQuizQueryRepository
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.ReadBookQuizQuestionPort
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizQuestions

@PersistenceAdapter
class BookQuizPersistenceQueryAdapter(
	private val bookQuizQueryRepository: BookQuizQueryRepository,
) : ReadBookQuizQuestionPort {
	override fun findBookQuizQuestionsBy(quizId: Long): BookQuizQuestions? =
		bookQuizQueryRepository.findBookQuizQuestionsBy(quizId)
}