package kr.kro.dokbaro.server.core.bookquiz.adapter.out

import kr.kro.dokbaro.server.common.annotation.PersistenceAdapter
import kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence.repository.jooq.QuizQuestionRepository
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.InsertQuizQuestionPort
import kr.kro.dokbaro.server.core.bookquiz.domain.QuizQuestion

@PersistenceAdapter
class QuizQuestionPersistenceAdapter(
	private val quizQuestionRepository: QuizQuestionRepository,
) : InsertQuizQuestionPort {
	override fun insert(quizQuestion: QuizQuestion): Long = quizQuestionRepository.insert(quizQuestion)
}