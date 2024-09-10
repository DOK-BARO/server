package kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence

import kr.kro.dokbaro.server.common.annotation.PersistenceAdapter
import kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence.repository.jooq.BookQuizRepository
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.SaveBookQuizPort
import kr.kro.dokbaro.server.core.bookquiz.domain.BookQuiz

@PersistenceAdapter
class BookQuizPersistenceAdapter(
	private val bookQuizRepository: BookQuizRepository,
) : SaveBookQuizPort {
	override fun save(bookQuiz: BookQuiz): Long = bookQuizRepository.save(bookQuiz)
}