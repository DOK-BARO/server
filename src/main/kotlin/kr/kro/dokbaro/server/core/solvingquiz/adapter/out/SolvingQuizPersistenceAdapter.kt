package kr.kro.dokbaro.server.core.solvingquiz.adapter.out

import kr.kro.dokbaro.server.common.annotation.PersistenceAdapter
import kr.kro.dokbaro.server.core.solvingquiz.adapter.out.persistence.repository.jooq.SolvingQuizRepository
import kr.kro.dokbaro.server.core.solvingquiz.application.port.out.InsertSolvingQuizPort
import kr.kro.dokbaro.server.core.solvingquiz.application.port.out.LoadSolvingQuizPort
import kr.kro.dokbaro.server.core.solvingquiz.application.port.out.UpdateSolvingQuizPort
import kr.kro.dokbaro.server.core.solvingquiz.domain.SolvingQuiz

@PersistenceAdapter
class SolvingQuizPersistenceAdapter(
	private val solvingQuizRepository: SolvingQuizRepository,
) : InsertSolvingQuizPort,
	LoadSolvingQuizPort,
	UpdateSolvingQuizPort {
	override fun insert(solvingQuiz: SolvingQuiz): Long = solvingQuizRepository.insert(solvingQuiz)

	override fun findById(solvingQuizId: Long): SolvingQuiz? = solvingQuizRepository.findById(solvingQuizId)

	override fun update(solvingQuiz: SolvingQuiz) {
		solvingQuizRepository.update(solvingQuiz)
	}
}