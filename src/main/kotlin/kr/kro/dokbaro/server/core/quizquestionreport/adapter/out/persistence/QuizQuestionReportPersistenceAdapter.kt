package kr.kro.dokbaro.server.core.quizquestionreport.adapter.out.persistence

import kr.kro.dokbaro.server.common.annotation.PersistenceAdapter
import kr.kro.dokbaro.server.core.quizquestionreport.adapter.out.persistence.repository.jooq.QuizQuestionReportRepository
import kr.kro.dokbaro.server.core.quizquestionreport.application.port.out.InsertQuizQuestionReportPort
import kr.kro.dokbaro.server.core.quizquestionreport.domain.QuizQuestionReport

@PersistenceAdapter
class QuizQuestionReportPersistenceAdapter(
	private val repository: QuizQuestionReportRepository,
) : InsertQuizQuestionReportPort {
	override fun insert(quizQuestionReport: QuizQuestionReport): Long = repository.insert(quizQuestionReport)
}