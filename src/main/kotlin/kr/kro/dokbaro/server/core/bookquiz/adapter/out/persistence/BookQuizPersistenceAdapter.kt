package kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence

import kr.kro.dokbaro.server.common.annotation.PersistenceAdapter
import kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence.repository.jooq.BookQuizRepository
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.InsertBookQuizPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.LoadBookQuizByQuestionIdPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.LoadBookQuizPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.UpdateBookQuizPort
import kr.kro.dokbaro.server.core.bookquiz.domain.BookQuiz

@PersistenceAdapter
class BookQuizPersistenceAdapter(
	private val bookQuizRepository: BookQuizRepository,
) : InsertBookQuizPort,
	LoadBookQuizPort,
	UpdateBookQuizPort,
	LoadBookQuizByQuestionIdPort {
	override fun insert(bookQuiz: BookQuiz): Long = bookQuizRepository.insert(bookQuiz)

	override fun load(id: Long): BookQuiz? = bookQuizRepository.load(id)

	override fun update(bookQuiz: BookQuiz) {
		bookQuizRepository.update(bookQuiz)
	}

	override fun loadByQuestionId(questionId: Long): BookQuiz? = bookQuizRepository.loadByQuestionId(questionId)
}