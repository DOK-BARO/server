package kr.kro.dokbaro.server.core.quizreviewreport.adapter.out

import kr.kro.dokbaro.server.common.annotation.PersistenceAdapter
import kr.kro.dokbaro.server.core.quizreviewreport.adapter.out.persistence.repository.jooq.QuizReviewReportRepository
import kr.kro.dokbaro.server.core.quizreviewreport.application.port.out.InsertQuizReviewReportPort
import kr.kro.dokbaro.server.core.quizreviewreport.domain.QuizReviewReport

@PersistenceAdapter
class QuizReviewReportPersistenceAdapter(
	private val quizReviewReportRepository: QuizReviewReportRepository,
) : InsertQuizReviewReportPort {
	override fun insert(quizReviewReport: QuizReviewReport): Long = quizReviewReportRepository.insert(quizReviewReport)
}